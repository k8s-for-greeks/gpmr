(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultsController', RaceResultsController);

    RaceResultsController.$inject = ['$scope', '$state', 'RaceResults'];

    function RaceResultsController ($scope, $state, RaceResults) {
        var vm = this;
        vm.raceResults = [];
        vm.loadAll = function() {
            RaceResults.query(function(result) {
                vm.raceResults = result;
            });
        };

        vm.loadAll();
        
    }
})();
