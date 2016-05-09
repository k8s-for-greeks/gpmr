(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceParticipants', RaceParticipants);

    RaceParticipants.$inject = ['$resource'];

    function RaceParticipants ($resource) {
        var resourceUrl =  'api/race-participants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
