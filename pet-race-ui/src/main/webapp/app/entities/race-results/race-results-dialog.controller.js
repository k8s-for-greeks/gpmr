(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultsDialogController', RaceResultsDialogController);

    RaceResultsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceResults'];

    function RaceResultsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceResults) {
        var vm = this;
        vm.raceResults = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceResultsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceResults.id !== null) {
                RaceResults.update(vm.raceResults, onSaveSuccess, onSaveError);
            } else {
                RaceResults.save(vm.raceResults, onSaveSuccess, onSaveError);
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
