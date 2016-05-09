(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race', {
            parent: 'entity',
            url: '/race',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Races'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race/races.html',
                    controller: 'RaceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-detail', {
            parent: 'entity',
            url: '/race/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Race'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race/race-detail.html',
                    controller: 'RaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Race', function($stateParams, Race) {
                    return Race.get({id : $stateParams.id});
                }]
            }
        })
        .state('race.new', {
            parent: 'race',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-dialog.html',
                    controller: 'RaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numOfPets: null,
                                length: null,
                                numOfSamples: null,
                                winnerId: null,
                                winnerName: null,
                                winnnerPetCategory: null,
                                startTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: true });
                }, function() {
                    $state.go('race');
                });
            }]
        })
        .state('race.edit', {
            parent: 'race',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-dialog.html',
                    controller: 'RaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Race', function(Race) {
                            return Race.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race.delete', {
            parent: 'race',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race/race-delete-dialog.html',
                    controller: 'RaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Race', function(Race) {
                            return Race.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
