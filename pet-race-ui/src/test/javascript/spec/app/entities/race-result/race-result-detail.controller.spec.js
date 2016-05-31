'use strict';

describe('Controller Tests', function() {

    describe('RaceResult Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRaceResult;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRaceResult = jasmine.createSpy('MockRaceResult');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RaceResult': MockRaceResult
            };
            createController = function() {
                $injector.get('$controller')("RaceResultDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:raceResultUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
