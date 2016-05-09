(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceResults', RaceResults);

    RaceResults.$inject = ['$resource', 'DateUtils'];

    function RaceResults ($resource, DateUtils) {
        var resourceUrl =  'api/race-results/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.time = DateUtils.convertDateTimeFromServer(data.time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
