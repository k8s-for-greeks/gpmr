(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceNormalController', RaceNormalController);

    RaceNormalController.$inject = ['$scope', '$state', 'RaceNormal', 'ParseLinks', 'AlertService'];

    function RaceNormalController ($scope, $state, RaceNormal, ParseLinks, AlertService) {
        var vm = this;
        vm.raceNormals = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            RaceNormal.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.raceNormals.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.raceNormals = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
