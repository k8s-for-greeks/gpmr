(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('data-counter', {
            parent: 'entity',
            url: '/data-counter',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Data Counters'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-counter/data-counters.html',
                    controller: 'DataCounterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('data-counter-detail', {
            parent: 'entity',
            url: '/data-counter/{vtype}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Data Counter'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-counter/data-counter-detail.html',
                    controller: 'DataCounterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DataCounter', function($stateParams, DataCounter) {
                    return DataCounter.get({vtype : $stateParams.vtype});
                }]
            }
        })
        .state('data-counter.new', {
            parent: 'data-counter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-counter/data-counter-dialog.html',
                    controller: 'DataCounterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                vtype: null,
                                value: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('data-counter', null, { reload: true });
                }, function() {
                    $state.go('data-counter');
                });
            }]
        })
        .state('data-counter.edit', {
            parent: 'data-counter',
            url: '/{vtype}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-counter/data-counter-dialog.html',
                    controller: 'DataCounterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataCounter', function(DataCounter) {
                            return DataCounter.get({vtype : $stateParams.vtype});
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-counter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-counter.delete', {
            parent: 'data-counter',
            url: '/{vtype}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-counter/data-counter-delete-dialog.html',
                    controller: 'DataCounterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DataCounter', function(DataCounter) {
                            return DataCounter.get({vtype : $stateParams.vtype});
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-counter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
