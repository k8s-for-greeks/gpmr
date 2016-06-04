(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('race-normal', {
            parent: 'entity',
            url: '/race-normal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceNormals'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-normal/race-normals.html',
                    controller: 'RaceNormalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('race-normal-detail', {
            parent: 'entity',
            url: '/race-normal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RaceNormal'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/race-normal/race-normal-detail.html',
                    controller: 'RaceNormalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RaceNormal', function($stateParams, RaceNormal) {
                    return RaceNormal.get({id : $stateParams.id});
                }]
            }
        })
        .state('race-normal.new', {
            parent: 'race-normal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-normal/race-normal-dialog.html',
                    controller: 'RaceNormalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raceNormalId: null,
                                raceId: null,
                                petCategoryId: null,
                                petCategoryName: null,
                                currentTime: null,
                                normalLoc: null,
                                normalScale: null,
                                normalSize: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('race-normal', null, { reload: true });
                }, function() {
                    $state.go('race-normal');
                });
            }]
        })
        .state('race-normal.edit', {
            parent: 'race-normal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-normal/race-normal-dialog.html',
                    controller: 'RaceNormalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RaceNormal', function(RaceNormal) {
                            return RaceNormal.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-normal', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('race-normal.delete', {
            parent: 'race-normal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/race-normal/race-normal-delete-dialog.html',
                    controller: 'RaceNormalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RaceNormal', function(RaceNormal) {
                            return RaceNormal.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('race-normal', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
