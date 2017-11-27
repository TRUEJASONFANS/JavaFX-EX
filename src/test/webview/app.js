var app = angular.module('myApp', []);
app.service('serviceProvider', function() {
	var model = window.model;
  	var repository = {};
  	repository.getName = function() {
  		return 'zhxie';
  	}
  	repository.getRecentItems = function() {
  		return JSON.parse(model.getRecentItems());
    }
  	return repository;
});
function refresh() {
	var scope = angular.element($("#recent-folder-row")).scope();
	scope.$apply(function() {
	
	});
}
