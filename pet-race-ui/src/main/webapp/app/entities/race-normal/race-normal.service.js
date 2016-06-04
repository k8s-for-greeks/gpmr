(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceNormal', RaceNormal);

    RaceNormal.$inject = ['$resource', 'DateUtils'];

    function RaceNormal ($resource, DateUtils) {
        var resourceUrl =  'api/race-normals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.currentTime = DateUtils.convertDateTimeFromServer(data.currentTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
