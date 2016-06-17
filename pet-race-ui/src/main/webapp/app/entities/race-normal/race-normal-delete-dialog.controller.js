(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceNormalDeleteController',RaceNormalDeleteController);

    RaceNormalDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceNormal'];

    function RaceNormalDeleteController($uibModalInstance, entity, RaceNormal) {
        var vm = this;
        vm.raceNormal = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (raceNumberId) {
            RaceNormal.delete({raceNumberId: raceNumberId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
