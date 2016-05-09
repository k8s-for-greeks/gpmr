(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDataController', RaceDataController);

    RaceDataController.$inject = ['$scope', '$state', 'RaceData'];

    function RaceDataController ($scope, $state, RaceData) {
        var vm = this;
        vm.raceData = [];
        vm.loadAll = function() {
            RaceData.query(function(result) {
                vm.raceData = result;
            });
        };

        vm.loadAll();
        
    }
})();
