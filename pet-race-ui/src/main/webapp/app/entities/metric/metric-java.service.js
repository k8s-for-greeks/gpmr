(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('MetricJava', MetricJava);

    MetricJava.$inject = ['$resource', 'DateUtils'];

    function MetricJava ($resource, DateUtils) {
        var resourceUrl =  'api/metrics-java';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                    return data;
                }
            },
        });
    }
})();
