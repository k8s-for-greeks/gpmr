'use strict';

describe('Controller Tests', function() {

    describe('DataCounter Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDataCounter;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDataCounter = jasmine.createSpy('MockDataCounter');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DataCounter': MockDataCounter
            };
            createController = function() {
                $injector.get('$controller')("DataCounterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:dataCounterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
