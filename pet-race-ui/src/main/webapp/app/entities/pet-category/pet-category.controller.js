(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoryController', PetCategoryController);

    PetCategoryController.$inject = ['$scope', '$state', 'PetCategory'];

    function PetCategoryController ($scope, $state, PetCategory) {
        var vm = this;
        vm.petCategories = [];
        vm.loadAll = function() {
            PetCategory.query(function(result) {
                vm.petCategories = result;
            });
        };

        vm.loadAll();
        
    }
})();
