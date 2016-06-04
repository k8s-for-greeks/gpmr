(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoryDialogController', PetCategoryDialogController);

    PetCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PetCategory'];

    function PetCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PetCategory) {
        var vm = this;
        vm.petCategory = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:petCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.petCategory.petCategoryId !== null) {
                PetCategory.update(vm.petCategory, onSaveSuccess, onSaveError);
            } else {
                PetCategory.save(vm.petCategory, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
