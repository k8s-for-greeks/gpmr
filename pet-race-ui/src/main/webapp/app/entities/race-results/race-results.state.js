(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-results', {
            parent: 'entity',
            url: '/race-results',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceResults'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-results/race-results.html',
                    controller: 'RaceResultsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-results-detail', {
            parent: 'entity',
            url: '/race-results/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceResults'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-results/race-results-detail.html',
                    controller: 'RaceResultsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceResults', function($stateParams, RaceResults) {
                    return RaceResults.get({id : $stateParams.id});
                }]
            }
        })
        .state('race-results.new', {
            parent: 'race-results',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-results/race-results-dialog.html',
                    controller: 'RaceResultsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                petId: null,
                                petName: null,
                                petCategory: null,
                                petCategoryId: null,
                                place: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-results', null, { reload: true });
                }, function() {
                    $state.go('race-results');
                });
            }]
        })
        .state('race-results.edit', {
            parent: 'race-results',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-results/race-results-dialog.html',
                    controller: 'RaceResultsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceResults', function(RaceResults) {
                            return RaceResults.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-results', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-results.delete', {
            parent: 'race-results',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-results/race-results-delete-dialog.html',
                    controller: 'RaceResultsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceResults', function(RaceResults) {
                            return RaceResults.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-results', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
