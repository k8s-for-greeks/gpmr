(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pets', {
            parent: 'entity',
            url: '/pets',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pets/pets.html',
                    controller: 'PetsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pets-detail', {
            parent: 'entity',
            url: '/pets/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pets/pets-detail.html',
                    controller: 'PetsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Pets', function($stateParams, Pets) {
                    return Pets.get({id : $stateParams.id});
                }]
            }
        })
        .state('pets.new', {
            parent: 'pets',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pets/pets-dialog.html',
                    controller: 'PetsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                petCategory: null,
                                petCategoryId: null,
                                petSpeed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pets', null, { reload: true });
                }, function() {
                    $state.go('pets');
                });
            }]
        })
        .state('pets.edit', {
            parent: 'pets',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pets/pets-dialog.html',
                    controller: 'PetsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pets', function(Pets) {
                            return Pets.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pets', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pets.delete', {
            parent: 'pets',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pets/pets-delete-dialog.html',
                    controller: 'PetsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pets', function(Pets) {
                            return Pets.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pets', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
