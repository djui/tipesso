function DiscoverCtrl($scope, $http) {
  
  // Initial recommendations
  $scope.tippables = [
      {"location": "github",
       "username": "djui",
       "reason": "Coauthor of Tipesso!"},
      {"location": "github",
       "username": "onlyafly",
       "reason": "Coauthor of Tipesso!"}
  ];
  
  $scope.$on('discovery.begin', function() {
    $scope.discovery_loading = true;
    $scope.discovery_error = false;
  });
  $scope.$on('discovery.success', function() {
    $scope.discovery_loading = false;
    $scope.discovery_error = false;
  });
  $scope.$on('discovery.error', function() {
    $scope.discovery_loading = false;
    $scope.discovery_error = true;
  });
  
  $scope.discover = function() {
    var successCB = function(data) {
      $scope.$emit('discovery.success');
      $scope.tippables = data;
    };
    var errorCB = function(data, code) {
      $scope.$emit('discovery.error');
    };
    
    $scope.$emit('discovery.begin');
    var params = {params: {'name': $scope.tipProject}};
    $http.get('/project', params).success(successCB).error(errorCB);
};
}
