(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetController', PetController);

    PetController.$inject = ['$scope', '$state', 'Pet'];

    function PetController ($scope, $state, Pet) {
        var vm = this;
        vm.pets = [];
        vm.loadAll = function() {
            Pet.query(function(result) {
                vm.pets = result;
            });
        };

        vm.loadAll();
        
    }
})();
