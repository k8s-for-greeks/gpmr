(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'DataCounter', 'MetricJava','$log'];

    function HomeController ($scope, Principal, LoginService, $state, DataCounter, MetricJava, $log) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.dataCounters = null;
        vm.normals = null;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        vm.loadAll = function() {
            DataCounter.query({
                page: 1,
                size: 100,
            }, onSuccess, onError);
            function onSuccess(result, headers) {
                vm.dataCounters = {};
                for (var i = 0; i < result.length; i++) {
                    vm.dataCounters[result[i].vtype] = result[i].value;
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            DataCounter.query(function(result) {
            });
            
            MetricJava.get(function(result) {
               vm.javaMetric = result;
            });
            
            MetricJava.get(function(result) {
               vm.javaMetric = result;
            });
        };
        vm.loadAll();
    }
})();
