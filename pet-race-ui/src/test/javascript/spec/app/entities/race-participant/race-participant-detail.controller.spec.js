'use strict';

describe('Controller Tests', function() {

    describe('RaceParticipant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRaceParticipant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRaceParticipant = jasmine.createSpy('MockRaceParticipant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RaceParticipant': MockRaceParticipant
            };
            createController = function() {
                $injector.get('$controller')("RaceParticipantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpmrApp:raceParticipantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
