(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantsController', RaceParticipantsController);

    RaceParticipantsController.$inject = ['$scope', '$state', 'RaceParticipants'];

    function RaceParticipantsController ($scope, $state, RaceParticipants) {
        var vm = this;
        vm.raceParticipants = [];
        vm.loadAll = function() {
            RaceParticipants.query(function(result) {
                vm.raceParticipants = result;
            });
        };

        vm.loadAll();
        
    }
})();
