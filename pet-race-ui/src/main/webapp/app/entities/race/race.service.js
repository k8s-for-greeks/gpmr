(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('Race', Race);

    Race.$inject = ['$resource', 'DateUtils'];

    function Race ($resource, DateUtils) {
        var resourceUrl =  'api/races/:raceId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
