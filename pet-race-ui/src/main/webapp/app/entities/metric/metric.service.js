(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('Metric', Metric);

    Metric.$inject = ['$resource', 'DateUtils'];

    function Metric ($resource, DateUtils) {
        var resourceUrl =  'api/metrics/:metricId';

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
            'update': { method:'PUT' }
        });
    }
})();
