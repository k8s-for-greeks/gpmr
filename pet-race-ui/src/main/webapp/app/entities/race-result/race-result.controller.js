(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultController', RaceResultController);

    RaceResultController.$inject = ['$scope', '$state', 'RaceResult'];

    function RaceResultController ($scope, $state, RaceResult) {
        var vm = this;
        vm.raceResults = [];
        vm.loadAll = function() {
            RaceResult.query(function(result) {
                vm.raceResults = result;
            });
        };

        vm.loadAll();
        
    }
})();
