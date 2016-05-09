(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantsDialogController', RaceParticipantsDialogController);

    RaceParticipantsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceParticipants'];

    function RaceParticipantsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceParticipants) {
        var vm = this;
        vm.raceParticipants = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceParticipantsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceParticipants.id !== null) {
                RaceParticipants.update(vm.raceParticipants, onSaveSuccess, onSaveError);
            } else {
                RaceParticipants.save(vm.raceParticipants, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
