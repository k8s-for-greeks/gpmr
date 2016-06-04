(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceNormalDialogController', RaceNormalDialogController);

    RaceNormalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceNormal'];

    function RaceNormalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceNormal) {
        var vm = this;
        vm.raceNormal = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceNormalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceNormal.id !== null) {
                RaceNormal.update(vm.raceNormal, onSaveSuccess, onSaveError);
            } else {
                RaceNormal.save(vm.raceNormal, onSaveSuccess, onSaveError);
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
