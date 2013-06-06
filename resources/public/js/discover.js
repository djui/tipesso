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

  $scope.discover = function() {
    $http({url: '/project',
           method: 'GET',
           params: {'name': $scope.tipProject}
          }).success(function(data) {
              $scope.tippables = data;
          });
  };
}
