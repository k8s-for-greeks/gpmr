(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metric', {
            parent: 'entity',
            url: '/metric',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Metrics'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric/metrics.html',
                    controller: 'MetricController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('metric-detail', {
            parent: 'entity',
            url: '/metric/{metricId}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Metric'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric/metric-detail.html',
                    controller: 'MetricDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Metric', function($stateParams, Metric) {
                    return Metric.get({metricId : $stateParams.metricId});
                }]
            }
        })
        .state('metric.new', {
            parent: 'metric',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-dialog.html',
                    controller: 'MetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                metricId: null,
                                connectionErrors: null,
                                writeTimeouts: null,
                                readTimeouts: null,
                                unavailables: null,
                                otherErrors: null,
                                retries: null,
                                ignores: null,
                                knownHosts: null,
                                connectedTo: null,
                                openConnections: null,
                                reqCount: null,
                                reqMinLatency: null,
                                reqMaxLatency: null,
                                reqMeanLatency: null,
                                reqStdev: null,
                                reqMedian: null,
                                req75percentile: null,
                                req97percentile: null,
                                req98percentile: null,
                                req99percentile: null,
                                req999percentile: null,
                                dateCreated: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('metric');
                });
            }]
        })
        .state('metric.edit', {
            parent: 'metric',
            url: '/{metricId}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-dialog.html',
                    controller: 'MetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Metric', function(Metric) {
                            return Metric.get({metricId : $stateParams.metricId});
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric.delete', {
            parent: 'metric',
            url: '/{metricId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-delete-dialog.html',
                    controller: 'MetricDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Metric', function(Metric) {
                            return Metric.get({metricId : $stateParams.metricId});
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
