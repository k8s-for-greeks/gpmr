(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDialogController', RaceDialogController);

    RaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Race'];

    function RaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Race) {
        var vm = this;
        vm.race = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.race.raceId !== null) {
                Race.update(vm.race, onSaveSuccess, onSaveError);
            } else {
                Race.save(vm.race, onSaveSuccess, onSaveError);
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
