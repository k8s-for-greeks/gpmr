(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('PetDetailController', PetDetailController);

    PetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pet'];

    function PetDetailController($scope, $rootScope, $stateParams, entity, Pet) {
        var vm = this;
        vm.pet = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:petUpdate', function(event, result) {
            vm.pet = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
