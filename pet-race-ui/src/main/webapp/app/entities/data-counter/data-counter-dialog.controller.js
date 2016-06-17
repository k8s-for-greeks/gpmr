(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('DataCounterDialogController', DataCounterDialogController);

    DataCounterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DataCounter'];

    function DataCounterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DataCounter) {
        var vm = this;
        vm.dataCounter = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:dataCounterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.dataCounter.vtype !== null) {
                DataCounter.update(vm.dataCounter, onSaveSuccess, onSaveError);
            } else {
                DataCounter.save(vm.dataCounter, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
