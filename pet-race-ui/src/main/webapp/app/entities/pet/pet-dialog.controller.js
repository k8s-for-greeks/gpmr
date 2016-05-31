(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetDialogController', PetDialogController);

    PetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pet'];

    function PetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pet) {
        var vm = this;
        vm.pet = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:petUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.pet.id !== null) {
                Pet.update(vm.pet, onSaveSuccess, onSaveError);
            } else {
                Pet.save(vm.pet, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
