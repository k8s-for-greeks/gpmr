(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantsDetailController', RaceParticipantsDetailController);

    RaceParticipantsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceParticipants'];

    function RaceParticipantsDetailController($scope, $rootScope, $stateParams, entity, RaceParticipants) {
        var vm = this;
        vm.raceParticipants = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceParticipantsUpdate', function(event, result) {
            vm.raceParticipants = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
