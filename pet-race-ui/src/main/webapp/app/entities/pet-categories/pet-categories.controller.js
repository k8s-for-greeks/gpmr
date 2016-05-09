(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoriesController', PetCategoriesController);

    PetCategoriesController.$inject = ['$scope', '$state', 'PetCategories'];

    function PetCategoriesController ($scope, $state, PetCategories) {
        var vm = this;
        vm.petCategories = [];
        vm.loadAll = function() {
            PetCategories.query(function(result) {
                vm.petCategories = result;
            });
        };

        vm.loadAll();
        
    }
})();
