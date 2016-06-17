(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceParticipantController', RaceParticipantController);

    RaceParticipantController.$inject = ['$scope', '$state', 'RaceParticipant', 'ParseLinks', 'AlertService'];

    function RaceParticipantController ($scope, $state, RaceParticipant, ParseLinks, AlertService) {
        var vm = this;
        vm.raceParticipants = [];
        vm.predicate = 'raceParticipantId';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            RaceParticipant.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'raceParticipantId') {
                    result.push('raceParticipantId');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.raceParticipants.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.raceParticipants = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
