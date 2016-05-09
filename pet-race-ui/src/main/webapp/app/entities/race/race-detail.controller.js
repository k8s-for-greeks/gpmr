(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDetailController', RaceDetailController);

    RaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Race'];

    function RaceDetailController($scope, $rootScope, $stateParams, entity, Race) {
        var vm = this;
        vm.race = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceUpdate', function(event, result) {
            vm.race = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
