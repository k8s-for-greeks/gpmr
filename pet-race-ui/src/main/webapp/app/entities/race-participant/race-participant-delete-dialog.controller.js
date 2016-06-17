(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantDeleteController',RaceParticipantDeleteController);

    RaceParticipantDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceParticipant'];

    function RaceParticipantDeleteController($uibModalInstance, entity, RaceParticipant) {
        var vm = this;
        vm.raceParticipant = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (raceParticipantId) {
            RaceParticipant.delete({raceParticipantId: raceParticipantId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
