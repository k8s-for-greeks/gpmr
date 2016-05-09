(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pet-categories', {
            parent: 'entity',
            url: '/pet-categories',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PetCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet-categories/pet-categories.html',
                    controller: 'PetCategoriesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pet-categories-detail', {
            parent: 'entity',
            url: '/pet-categories/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PetCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet-categories/pet-categories-detail.html',
                    controller: 'PetCategoriesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PetCategories', function($stateParams, PetCategories) {
                    return PetCategories.get({id : $stateParams.id});
                }]
            }
        })
        .state('pet-categories.new', {
            parent: 'pet-categories',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-categories/pet-categories-dialog.html',
                    controller: 'PetCategoriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pet-categories', null, { reload: true });
                }, function() {
                    $state.go('pet-categories');
                });
            }]
        })
        .state('pet-categories.edit', {
            parent: 'pet-categories',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-categories/pet-categories-dialog.html',
                    controller: 'PetCategoriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PetCategories', function(PetCategories) {
                            return PetCategories.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet-categories', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pet-categories.delete', {
            parent: 'pet-categories',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-categories/pet-categories-delete-dialog.html',
                    controller: 'PetCategoriesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PetCategories', function(PetCategories) {
                            return PetCategories.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet-categories', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
