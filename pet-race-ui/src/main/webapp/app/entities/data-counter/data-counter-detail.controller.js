(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('DataCounterDetailController', DataCounterDetailController);

    DataCounterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DataCounter'];

    function DataCounterDetailController($scope, $rootScope, $stateParams, entity, DataCounter) {
        var vm = this;
        vm.dataCounter = entity;
        
        var unsubscribe = $rootScope.$on('gpmrApp:dataCounterUpdate', function(event, result) {
            vm.dataCounter = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
