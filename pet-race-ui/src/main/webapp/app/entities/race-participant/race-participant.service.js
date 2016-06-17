(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceParticipant', RaceParticipant);

    RaceParticipant.$inject = ['$resource', 'DateUtils'];

    function RaceParticipant ($resource, DateUtils) {
        var resourceUrl =  'api/race-participants/:raceParticipantId';

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
