(function() {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('RaceResultDeleteController',RaceResultDeleteController);

    RaceResultDeleteController.$inject = ['$uibModalInstance', 'entity', 'RaceResult'];

    function RaceResultDeleteController($uibModalInstance, entity, RaceResult) {
        var vm = this;
        vm.raceResult = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RaceResult.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
