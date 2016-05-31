(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantDetailController', RaceParticipantDetailController);

    RaceParticipantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RaceParticipant'];

    function RaceParticipantDetailController($scope, $rootScope, $stateParams, entity, RaceParticipant) {
        var vm = this;
        vm.raceParticipant = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:raceParticipantUpdate', function(event, result) {
            vm.raceParticipant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
