(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('DataCounter', DataCounter);

    DataCounter.$inject = ['$resource'];

    function DataCounter ($resource) {
        var resourceUrl =  'api/data-counters/:vtype';

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
