(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceDataDeleteController',RaceDataDeleteController);

    RaceDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceData'];

    function RaceDataDeleteController($uibModalInstance, entity, RaceData) {
        var vm = this;
        vm.raceData = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (raceDataId) {
            RaceData.delete({raceDataId: raceDataId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
