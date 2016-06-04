(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-participant', {
            parent: 'entity',
            url: '/race-participant',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceParticipants'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-participant/race-participants.html',
                    controller: 'RaceParticipantController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-participant-detail', {
            parent: 'entity',
            url: '/race-participant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceParticipant'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-participant/race-participant-detail.html',
                    controller: 'RaceParticipantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceParticipant', function($stateParams, RaceParticipant) {
                    return RaceParticipant.get({id : $stateParams.id});
                }]
            }
        })
        .state('race-participant.new', {
            parent: 'race-participant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participant/race-participant-dialog.html',
                    controller: 'RaceParticipantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceParticipantId: null,
                                petId: null,
                                raceId: null,
                                petName: null,
                                petColor: null,
                                petCategoryName: null,
                                petCategoryId: null,
                                startTime: null,
                                finishTime: null,
                                finishPosition: null,
                                finished: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-participant', null, { reload: true });
                }, function() {
                    $state.go('race-participant');
                });
            }]
        })
        .state('race-participant.edit', {
            parent: 'race-participant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participant/race-participant-dialog.html',
                    controller: 'RaceParticipantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceParticipant', function(RaceParticipant) {
                            return RaceParticipant.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-participant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-participant.delete', {
            parent: 'race-participant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participant/race-participant-delete-dialog.html',
                    controller: 'RaceParticipantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceParticipant', function(RaceParticipant) {
                            return RaceParticipant.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-participant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
