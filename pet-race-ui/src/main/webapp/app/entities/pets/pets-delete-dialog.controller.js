(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetsDeleteController',PetsDeleteController);

    PetsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pets'];

    function PetsDeleteController($uibModalInstance, entity, Pets) {
        var vm = this;
        vm.pets = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Pets.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
