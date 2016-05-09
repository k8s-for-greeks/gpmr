'use strict';

describe('Controller Tests', function() {

    describe('PetCategories Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPetCategories;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPetCategories = jasmine.createSpy('MockPetCategories');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PetCategories': MockPetCategories
            };
            createController = function() {
                $injector.get('$controller')("PetCategoriesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:petCategoriesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
