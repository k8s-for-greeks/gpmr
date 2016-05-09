(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultsDeleteController',RaceResultsDeleteController);

    RaceResultsDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceResults'];

    function RaceResultsDeleteController($uibModalInstance, entity, RaceResults) {
        var vm = this;
        vm.raceResults = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RaceResults.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
