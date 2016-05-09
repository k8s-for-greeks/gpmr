(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDataDetailController', RaceDataDetailController);

    RaceDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceData'];

    function RaceDataDetailController($scope, $rootScope, $stateParams, entity, RaceData) {
        var vm = this;
        vm.raceData = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceDataUpdate', function(event, result) {
            vm.raceData = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
