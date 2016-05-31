(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('Pet', Pet);

    Pet.$inject = ['$resource'];

    function Pet ($resource) {
        var resourceUrl =  'api/pets/:id';

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
