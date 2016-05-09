(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('Pets', Pets);

    Pets.$inject = ['$resource'];

    function Pets ($resource) {
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
