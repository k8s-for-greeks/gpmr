'use strict';

describe('Controller Tests', function() {

    describe('RaceResults Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRaceResults;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRaceResults = jasmine.createSpy('MockRaceResults');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RaceResults': MockRaceResults
            };
            createController = function() {
                $injector.get('$controller')("RaceResultsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:raceResultsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
