(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultController', RaceResultController);

    RaceResultController.$inject = ['$scope', '$state', 'RaceResult', 'ParseLinks', 'AlertService'];

    function RaceResultController ($scope, $state, RaceResult, ParseLinks, AlertService) {
        var vm = this;
        vm.raceResults = [];
        vm.predicate = 'raceResultsId';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            RaceResult.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'raceResultId') {
                    result.push('raceResultId');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.raceResults.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.raceResults = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
