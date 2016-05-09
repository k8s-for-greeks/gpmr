(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDeleteController',RaceDeleteController);

    RaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Race'];

    function RaceDeleteController($uibModalInstance, entity, Race) {
        var vm = this;
        vm.race = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Race.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
