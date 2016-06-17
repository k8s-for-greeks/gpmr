(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetDeleteController',PetDeleteController);

    PetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pet'];

    function PetDeleteController($uibModalInstance, entity, Pet) {
        var vm = this;
        vm.pet = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (petId) {
            Pet.delete({petId: petId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
