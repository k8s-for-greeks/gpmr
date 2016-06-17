(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-result', {
            parent: 'entity',
            url: '/race-result',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceResults'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-result/race-results.html',
                    controller: 'RaceResultController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-result-detail', {
            parent: 'entity',
            url: '/race-result/{raceResultId}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceResult'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-result/race-result-detail.html',
                    controller: 'RaceResultDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceResult', function($stateParams, RaceResult) {
                    return RaceResult.get({raceResultId : $stateParams.raceResultId});
                }]
            }
        })
        .state('race-result.new', {
            parent: 'race-result',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-result/race-result-dialog.html',
                    controller: 'RaceResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceResultId: null,
                                raceId: null,
                                petCategoryId: null,
                                raceParticipantId: null,
                                petName: null,
                                petType: null,
                                petColor: null,
                                petCategoryName: null,
                                finishPosition: null,
                                finishTime: null,
                                startTime: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-result', null, { reload: true });
                }, function() {
                    $state.go('race-result');
                });
            }]
        })
        .state('race-result.edit', {
            parent: 'race-result',
            url: '/{raceResultId}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-result/race-result-dialog.html',
                    controller: 'RaceResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceResult', function(RaceResult) {
                            return RaceResult.get({raceResultId : $stateParams.raceResultId});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-result', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-result.delete', {
            parent: 'race-result',
            url: '/{raceResultId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-result/race-result-delete-dialog.html',
                    controller: 'RaceResultDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceResult', function(RaceResult) {
                            return RaceResult.get({raceResultId : $stateParams.raceResultId});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-result', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
