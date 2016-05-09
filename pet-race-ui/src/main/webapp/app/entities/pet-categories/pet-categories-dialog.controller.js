(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoriesDialogController', PetCategoriesDialogController);

    PetCategoriesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PetCategories'];

    function PetCategoriesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PetCategories) {
        var vm = this;
        vm.petCategories = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('gpmrApp:petCategoriesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.petCategories.id !== null) {
                PetCategories.update(vm.petCategories, onSaveSuccess, onSaveError);
            } else {
                PetCategories.save(vm.petCategories, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
