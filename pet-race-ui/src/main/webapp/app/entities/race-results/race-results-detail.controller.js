(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultsDetailController', RaceResultsDetailController);

    RaceResultsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceResults'];

    function RaceResultsDetailController($scope, $rootScope, $stateParams, entity, RaceResults) {
        var vm = this;
        vm.raceResults = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceResultsUpdate', function(event, result) {
            vm.raceResults = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
