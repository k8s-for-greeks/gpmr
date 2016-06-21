(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceDataByRace', RaceDataByRace);

    RaceDataByRace.$inject = ['$resource', 'DateUtils'];

    function RaceDataByRace ($resource, DateUtils) {
        var resourceUrl =  'api/race-data/race/:raceId';

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
