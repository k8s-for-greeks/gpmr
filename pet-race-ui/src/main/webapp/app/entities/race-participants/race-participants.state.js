(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-participants', {
            parent: 'entity',
            url: '/race-participants',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceParticipants'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-participants/race-participants.html',
                    controller: 'RaceParticipantsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-participants-detail', {
            parent: 'entity',
            url: '/race-participants/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceParticipants'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-participants/race-participants-detail.html',
                    controller: 'RaceParticipantsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceParticipants', function($stateParams, RaceParticipants) {
                    return RaceParticipants.get({id : $stateParams.id});
                }]
            }
        })
        .state('race-participants.new', {
            parent: 'race-participants',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participants/race-participants-dialog.html',
                    controller: 'RaceParticipantsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                petId: null,
                                petName: null,
                                petType: null,
                                petColor: null,
                                petCategory: null,
                                petCategoryId: null,
                                raceId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-participants', null, { reload: true });
                }, function() {
                    $state.go('race-participants');
                });
            }]
        })
        .state('race-participants.edit', {
            parent: 'race-participants',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participants/race-participants-dialog.html',
                    controller: 'RaceParticipantsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceParticipants', function(RaceParticipants) {
                            return RaceParticipants.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-participants', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-participants.delete', {
            parent: 'race-participants',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-participants/race-participants-delete-dialog.html',
                    controller: 'RaceParticipantsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceParticipants', function(RaceParticipants) {
                            return RaceParticipants.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-participants', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
