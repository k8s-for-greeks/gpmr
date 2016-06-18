(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('MetricDeleteController',MetricDeleteController);

    MetricDeleteController.$inject = ['$uibModalInstance', 'entity', 'Metric'];

    function MetricDeleteController($uibModalInstance, entity, Metric) {
        var vm = this;
        vm.metric = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (metricId) {
            Metric.delete({metricId: metricId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
