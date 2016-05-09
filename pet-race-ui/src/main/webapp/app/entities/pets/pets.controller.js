(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetsController', PetsController);

    PetsController.$inject = ['$scope', '$state', 'Pets'];

    function PetsController ($scope, $state, Pets) {
        var vm = this;
        vm.pets = [];
        vm.loadAll = function() {
            Pets.query(function(result) {
                vm.pets = result;
            });
        };

        vm.loadAll();
        
    }
})();
