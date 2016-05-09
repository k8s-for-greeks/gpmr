(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoriesDetailController', PetCategoriesDetailController);

    PetCategoriesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PetCategories'];

    function PetCategoriesDetailController($scope, $rootScope, $stateParams, entity, PetCategories) {
        var vm = this;
        vm.petCategories = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:petCategoriesUpdate', function(event, result) {
            vm.petCategories = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
