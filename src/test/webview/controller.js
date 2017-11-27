app.controller('myCtrl', ["$scope", "serviceProvider", function($scope, serviceProvider) {
	$scope.name = serviceProvider.getName();
	$scope.recentItems = function() {
    	return serviceProvider.getRecentItems();
  	};
}]);
