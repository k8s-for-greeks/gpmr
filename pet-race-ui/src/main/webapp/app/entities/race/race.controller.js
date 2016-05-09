(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceController', RaceController);

    RaceController.$inject = ['$scope', '$state', 'Race'];

    function RaceController ($scope, $state, Race) {
        var vm = this;
        vm.races = [];
        vm.loadAll = function() {
            Race.query(function(result) {
                vm.races = result;
            });
        };

        vm.loadAll();
        
    }
})();
