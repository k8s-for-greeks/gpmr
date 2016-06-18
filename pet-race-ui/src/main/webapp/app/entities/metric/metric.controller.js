(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('MetricController', MetricController);

    MetricController.$inject = ['$scope', '$state', 'Metric', 'ParseLinks', 'AlertService'];

    function MetricController ($scope, $state, Metric, ParseLinks, AlertService) {
        var vm = this;
        vm.metrics = [];
        vm.predicate = 'metricId';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            Metric.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'metricId') {
                    result.push('metricId');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.metrics.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.metrics = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
