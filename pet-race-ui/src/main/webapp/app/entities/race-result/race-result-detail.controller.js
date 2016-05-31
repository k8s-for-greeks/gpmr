(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultDetailController', RaceResultDetailController);

    RaceResultDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceResult'];

    function RaceResultDetailController($scope, $rootScope, $stateParams, entity, RaceResult) {
        var vm = this;
        vm.raceResult = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceResultUpdate', function(event, result) {
            vm.raceResult = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
