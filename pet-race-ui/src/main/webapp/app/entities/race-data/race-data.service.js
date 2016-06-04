(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceData', RaceData);

    RaceData.$inject = ['$resource', 'DateUtils'];

    function RaceData ($resource, DateUtils) {
        var resourceUrl =  'api/race-data/:id';

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
