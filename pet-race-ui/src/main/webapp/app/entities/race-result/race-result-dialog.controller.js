(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultDialogController', RaceResultDialogController);

    RaceResultDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceResult'];

    function RaceResultDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceResult) {
        var vm = this;
        vm.raceResult = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceResultUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceResult.raceResultId !== null) {
                RaceResult.update(vm.raceResult, onSaveSuccess, onSaveError);
            } else {
                RaceResult.save(vm.raceResult, onSaveSuccess, onSaveError);
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
