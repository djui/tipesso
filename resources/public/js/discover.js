function DiscoverCtrl($scope, $http) {
  $scope.discover = function() {
    $http({url: '/project',
           method: 'GET',
           params: {'name': $scope.tipProject}
          }).success(function(data) {
            alert(data);
          });
  }
}
