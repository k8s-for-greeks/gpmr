(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('MetricDetailController', MetricDetailController);

    MetricDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Metric'];

    function MetricDetailController($scope, $rootScope, $stateParams, entity, Metric) {
        var vm = this;
        vm.metric = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:metricUpdate', function(event, result) {
            vm.metric = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
