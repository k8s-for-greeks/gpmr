(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoryDeleteController',PetCategoryDeleteController);

    PetCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'PetCategory'];

    function PetCategoryDeleteController($uibModalInstance, entity, PetCategory) {
        var vm = this;
        vm.petCategory = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PetCategory.delete({petCategory: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
