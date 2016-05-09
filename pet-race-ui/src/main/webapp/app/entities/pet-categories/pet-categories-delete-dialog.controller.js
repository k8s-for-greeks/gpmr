(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoriesDeleteController',PetCategoriesDeleteController);

    PetCategoriesDeleteController.$inject = ['$uibModalInstance', 'entity', 'PetCategories'];

    function PetCategoriesDeleteController($uibModalInstance, entity, PetCategories) {
        var vm = this;
        vm.petCategories = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PetCategories.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
