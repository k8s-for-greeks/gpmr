(function() {
    'use strict';
    angular
        .module('gpmrApp')
        .factory('PetCategory', PetCategory);

    PetCategory.$inject = ['$resource'];

    function PetCategory ($resource) {
        var resourceUrl =  'api/pet-categories/:petCategoryId';

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
