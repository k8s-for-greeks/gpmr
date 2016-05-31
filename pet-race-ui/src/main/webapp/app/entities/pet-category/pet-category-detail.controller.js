(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetCategoryDetailController', PetCategoryDetailController);

    PetCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PetCategory'];

    function PetCategoryDetailController($scope, $rootScope, $stateParams, entity, PetCategory) {
        var vm = this;
        vm.petCategory = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:petCategoryUpdate', function(event, result) {
            vm.petCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
