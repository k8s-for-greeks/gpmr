(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pet-category', {
            parent: 'entity',
            url: '/pet-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PetCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet-category/pet-categories.html',
                    controller: 'PetCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pet-category-detail', {
            parent: 'entity',
            url: '/pet-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PetCategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet-category/pet-category-detail.html',
                    controller: 'PetCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PetCategory', function($stateParams, PetCategory) {
                    return PetCategory.get({id : $stateParams.id});
                }]
            }
        })
        .state('pet-category.new', {
            parent: 'pet-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-category/pet-category-dialog.html',
                    controller: 'PetCategoryDialogController',
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
                    $state.go('pet-category', null, { reload: true });
                }, function() {
                    $state.go('pet-category');
                });
            }]
        })
        .state('pet-category.edit', {
            parent: 'pet-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-category/pet-category-dialog.html',
                    controller: 'PetCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PetCategory', function(PetCategory) {
                            return PetCategory.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet-category', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pet-category.delete', {
            parent: 'pet-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet-category/pet-category-delete-dialog.html',
                    controller: 'PetCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PetCategory', function(PetCategory) {
                            return PetCategory.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet-category', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
