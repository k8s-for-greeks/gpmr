(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-data', {
            parent: 'entity',
            url: '/race-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-data/race-data.html',
                    controller: 'RaceDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-data-detail', {
            parent: 'entity',
            url: '/race-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-data/race-data-detail.html',
                    controller: 'RaceDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceData', function($stateParams, RaceData) {
                    return RaceData.get({id : $stateParams.id});
                }]
            }
        })
        .state('race-data.new', {
            parent: 'race-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-data/race-data-dialog.html',
                    controller: 'RaceDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceDataId: null,
                                petId: null,
                                raceId: null,
                                petName: null,
                                petCategoryName: null,
                                petCategoryId: null,
                                interval: null,
                                runnerPosition: null,
                                runnerDistance: null,
                                startTime: null,
                                finished: null,
                                runnerPreviousDistance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-data', null, { reload: true });
                }, function() {
                    $state.go('race-data');
                });
            }]
        })
        .state('race-data.edit', {
            parent: 'race-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-data/race-data-dialog.html',
                    controller: 'RaceDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceData', function(RaceData) {
                            return RaceData.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-data.delete', {
            parent: 'race-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-data/race-data-delete-dialog.html',
                    controller: 'RaceDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceData', function(RaceData) {
                            return RaceData.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
