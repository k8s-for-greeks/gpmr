(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantDialogController', RaceParticipantDialogController);

    RaceParticipantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RaceParticipant'];

    function RaceParticipantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RaceParticipant) {
        var vm = this;
        vm.raceParticipant = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:raceParticipantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.raceParticipant.id !== null) {
                RaceParticipant.update(vm.raceParticipant, onSaveSuccess, onSaveError);
            } else {
                RaceParticipant.save(vm.raceParticipant, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
