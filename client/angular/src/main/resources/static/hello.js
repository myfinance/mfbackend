function Hello($scope, $http) {
    $http.get('http://localhost:8080/getProducts').
        success(function(data) {
            $scope.greeting = data;
        });
}