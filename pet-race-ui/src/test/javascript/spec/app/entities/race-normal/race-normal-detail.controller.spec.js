'use strict';

describe('Controller Tests', function() {

    describe('RaceNormal Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRaceNormal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRaceNormal = jasmine.createSpy('MockRaceNormal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RaceNormal': MockRaceNormal
            };
            createController = function() {
                $injector.get('$controller')("RaceNormalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:raceNormalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
