(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDataController', RaceDataController);

    RaceDataController.$inject = ['$scope', '$state', 'RaceData', 'ParseLinks', 'AlertService'];

    function RaceDataController ($scope, $state, RaceData, ParseLinks, AlertService) {
        var vm = this;
        vm.raceData = [];
        vm.predicate = 'raceDataId';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            RaceData.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'raceDataId') {
                    result.push('raceDataId');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.raceData.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.raceData = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
