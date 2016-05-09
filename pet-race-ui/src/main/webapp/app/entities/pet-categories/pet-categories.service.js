(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('PetCategories', PetCategories);

    PetCategories.$inject = ['$resource'];

    function PetCategories ($resource) {
        var resourceUrl =  'api/pet-categories/:id';

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
