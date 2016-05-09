(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetsDetailController', PetsDetailController);

    PetsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pets'];

    function PetsDetailController($scope, $rootScope, $stateParams, entity, Pets) {
        var vm = this;
        vm.pets = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:petsUpdate', function(event, result) {
            vm.pets = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
