(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantsDeleteController',RaceParticipantsDeleteController);

    RaceParticipantsDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceParticipants'];

    function RaceParticipantsDeleteController($uibModalInstance, entity, RaceParticipants) {
        var vm = this;
        vm.raceParticipants = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RaceParticipants.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
