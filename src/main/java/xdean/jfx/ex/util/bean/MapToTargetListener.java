package xdean.jfx.ex.util.bean;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import javafx.beans.WeakListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import xdean.jex.util.collection.ListUtil;

class MapToTargetListener<F, T> implements ListChangeListener<F>, WeakListener {
  WeakReference<ObservableList<F>> sourceList;
  WeakReference<ObservableList<T>> targetList;
  Function<F, T> function;
  BooleanProperty updating = new SimpleBooleanProperty(false);

  public MapToTargetListener(ObservableList<F> sourceList, ObservableList<T> targetList, Function<F, T> function) {
    this.targetList = new WeakReference<>(targetList);
    this.sourceList = new WeakReference<>(sourceList);
    this.function = function;
  }

  @Override
  public void onChanged(Change<? extends F> change) {
    if (updating.get()) {
      return;
    }
    ObservableList<F> sourceList = this.sourceList.get();
    ObservableList<T> targetList = this.targetList.get();
    if (sourceList == null || targetList == null) {
      if (sourceList != null) {
        sourceList.removeListener(this);
      }
      return;
    }
    if (updating.get()) {
      return;
    }
    updating.set(true);
    while (change.next()) {
      if (change.wasPermutated()) {
        targetList.remove(change.getFrom(), change.getTo());
        targetList.addAll(change.getFrom(),
            ListUtil.map(change.getList().subList(change.getFrom(), change.getTo()), function));
      } else {
        if (change.wasRemoved()) {
          targetList.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
        }
        if (change.wasAdded()) {
          targetList.addAll(change.getFrom(), ListUtil.map(change.getAddedSubList(), function));
        }
      }
    }
    updating.set(false);
  }

  @Override
  public boolean wasGarbageCollected() {
    return (sourceList.get() == null) || (targetList.get() == null);
  }
}