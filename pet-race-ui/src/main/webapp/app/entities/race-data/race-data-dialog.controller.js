(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDataDialogController', RaceDataDialogController);

    RaceDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceData'];

    function RaceDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceData) {
        var vm = this;
        vm.raceData = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceData.raceDataId !== null) {
                RaceData.update(vm.raceData, onSaveSuccess, onSaveError);
            } else {
                RaceData.save(vm.raceData, onSaveSuccess, onSaveError);
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
