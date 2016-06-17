(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('RaceNormalAll', RaceNormalAll);

    RaceNormalAll.$inject = ['$resource', 'DateUtils'];

    function RaceNormalAll ($resource, DateUtils) {
        var resourceUrl =  'api/race-normals-all';

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
        });
    }
})();
