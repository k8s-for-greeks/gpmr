(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceNormalDetailController', RaceNormalDetailController);

    RaceNormalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceNormal'];

    function RaceNormalDetailController($scope, $rootScope, $stateParams, entity, RaceNormal) {
        var vm = this;
        vm.raceNormal = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceNormalUpdate', function(event, result) {
            vm.raceNormal = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
