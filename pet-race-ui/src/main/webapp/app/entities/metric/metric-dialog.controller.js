(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('MetricDialogController', MetricDialogController);

    MetricDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Metric'];

    function MetricDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Metric) {
        var vm = this;
        vm.metric = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:metricUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.metric.metricId !== null) {
                Metric.update(vm.metric, onSaveSuccess, onSaveError);
            } else {
                Metric.save(vm.metric, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
