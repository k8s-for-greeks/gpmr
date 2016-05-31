(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantController', RaceParticipantController);

    RaceParticipantController.$inject = ['$scope', '$state', 'RaceParticipant'];

    function RaceParticipantController ($scope, $state, RaceParticipant) {
        var vm = this;
        vm.raceParticipants = [];
        vm.loadAll = function() {
            RaceParticipant.query(function(result) {
                vm.raceParticipants = result;
            });
        };

        vm.loadAll();
        
    }
})();
