(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceData', RaceData);

    RaceData.$inject = ['$resource'];

    function RaceData ($resource) {
        var resourceUrl =  'api/race-data/:id';

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
