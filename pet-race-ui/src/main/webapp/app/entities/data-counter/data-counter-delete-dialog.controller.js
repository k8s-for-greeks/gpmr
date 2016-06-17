(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('DataCounterDeleteController',DataCounterDeleteController);

    DataCounterDeleteController.$inject = ['$uibModalInstance', 'entity', 'DataCounter'];

    function DataCounterDeleteController($uibModalInstance, entity, DataCounter) {
        var vm = this;
        vm.dataCounter = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (vtype) {
            DataCounter.delete({vtype: vtype},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
