(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceController', RaceController);

    RaceController.$inject = ['$scope', '$state', 'Race', 'ParseLinks', 'AlertService'];

    function RaceController ($scope, $state, Race, ParseLinks, AlertService) {
        var vm = this;
        vm.races = [];
        vm.predicate = 'raceId';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            Race.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'raceId') {
                    result.push('raceId');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.races.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.races = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
