package zhxie.jfx.ex.webview;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class WebEngineTest extends Application {

    private JavaApplication value;

	@Override
    public void start(Stage stage) {
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        // Early call of executeScript to get a JavaScript object, a proxy for the 
        // Java object to be accessed on the JavaScript environment
        JSObject window = (JSObject) webEngine.executeScript("window");
        value = new JavaApplication();
        value.setName("orignial");
        value.addRecentItem(new Item("AL", "test1", "./test1"));
		window.setMember("model", value);
        webEngine.setOnAlert(e-> System.out.println(e.getData()));
        webEngine.setOnError(e-> System.out.println(e.getMessage()));
        webEngine.getLoadWorker().stateProperty().addListener((ov,oldState,newState)->{
            if(newState==State.SCHEDULED){
                System.out.println("state: scheduled");
            } else if(newState==State.RUNNING){
                System.out.println("state: running");
            } else if(newState==State.SUCCEEDED){
                System.out.println("state: succeeded");
            }
        });
        webEngine.load(this.getClass().getResource("index.html").toString());

        Button btn1 = new Button("check java backend");
        btn1.setOnAction(e -> {
   			webEngine.executeScript("alert(model.getName())");
   		});
   		
        Button btn2 = new Button("reload");
		btn2.setOnAction(e -> {
			value.setName("refresh");
			value.addRecentItem(new Item("OVL", "test2", "./test2"));
			webEngine.executeScript("refresh()");
		});
		
		
		VBox vbox = new VBox(10, btn1, btn2, webView);
        Scene scene = new Scene(vbox,400,300);

        stage.setScene(scene);
        stage.show();

    }

	class Item {
		String sessionTypeName;
		String sessionName;
		String sessionPath;

		public String getSessionTypeName() {
			return sessionTypeName;
		}

		public void setSessionTypeName(String sessionTypeName) {
			this.sessionTypeName = sessionTypeName;
		}

		public String getSessionName() {
			return sessionName;
		}

		public void setSessionName(String sessionName) {
			this.sessionName = sessionName;
		}

		public String getSessionPath() {
			return sessionPath;
		}

		public void setSessionPath(String sessionPath) {
			this.sessionPath = sessionPath;
		}

		public Item(String sessionTypeName, String sessionName, String sessionPath) {
			super();
			this.sessionTypeName = sessionTypeName;
			this.sessionName = sessionName;
			this.sessionPath = sessionPath;
		}
	}
    public class JavaApplication {

        private String name;
		private List<Item> recentItems = Lists.newArrayList();

        public String getName() {
        	return name;
        }

		public void setName(String name) {
			this.name = name;
		}
		
		public String getRecentItems() {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			return gson.toJson(recentItems);
		}
		
		public void addRecentItem(Item item) {
			recentItems.add(item);
		}
    }

    public static void main(String[] args) {
        launch(args);
    }

}