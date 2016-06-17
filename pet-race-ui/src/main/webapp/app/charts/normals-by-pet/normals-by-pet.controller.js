/**
 * Created by clove on 6/16/16.
 */
var randomColor = (function(){
    var golden_ratio_conjugate = 0.618033988749895;
    var h = Math.random();

    var hslToRgb = function (h, s, l){
        var r, g, b;

        if(s == 0){
            r = g = b = l; // achromatic
        }else{
            function hue2rgb(p, q, t){
                if(t < 0) t += 1;
                if(t > 1) t -= 1;
                if(t < 1/6) return p + (q - p) * 6 * t;
                if(t < 1/2) return q;
                if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
                return p;
            }

            var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            var p = 2 * l - q;
            r = hue2rgb(p, q, h + 1/3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1/3);
        }

        return '#'+Math.round(r * 255).toString(16)+Math.round(g * 255).toString(16)+Math.round(b * 255).toString(16);
    };

    return function(){
        h += golden_ratio_conjugate;
        h %= 1;
        return hslToRgb(h, 0.5, 0.60);
    };
})();

(function () {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('NormalsByPetController', NormalsByPetController);

    NormalsByPetController.$inject = ['$scope', 'RaceNormalAll','$log'];



    function NormalsByPetController($scope, RaceNormalAll, $log) {

        var vm = this;
        vm.count = 0;
        vm.data = [];
        vm.page = 1;
        vm.normals = {};
        vm.size = 100;
        vm.options = {
            chart: {
                type: 'boxPlotChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 60,
                    left: 40
                },
                color: [],
                x: function (d) {
                    return d.label;
                },
                // y: function(d){return d.values.Q3;},
                maxBoxWidth: 75,
                yDomain: [-10, 0]
            }
        };

        // FIXME automatically paginate
        
        vm.loadAll = function () {

            RaceNormalAll.query({
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    var j = data[i];
                    if (!(j.petCategoryName in vm.normals)) {
                        vm.normals[j.petCategoryName] = {
                            normals: j.normals,
                            normalLoc: j.normalLoc,
                            normalScale: j.normalScale
                        }
                    } else {
                        vm.normals[j.petCategoryName].normals.concat(j.normals);
                    }
                }

                Object.keys(vm.normals).forEach(function(key,index) {
                    var a = vm.normals[key].normals;
                    var nums = stats(vm.normals[key].normals).sort();
                    var _outliers = nums.clone().findOutliers();
                    var _clean_nums = nums.removeOutliers();
                    var _max = _clean_nums.max();
                    vm.data.push({
                        label: key,
                        values: {
                            Q1: _clean_nums.q1(),
                            Q2: _clean_nums.median(),
                            Q3: _clean_nums.q3(),
                            whisker_low: _clean_nums.min(),
                            whisker_high: _max,
                            outliers: _outliers.toArray()
                        }
                    });
                    vm.options.chart.color.push(randomColor());
                    if (vm.options.chart.yDomain[1] < _max + 10) {
                        vm.options.chart.yDomain[1] = Math.ceil((_max + 10));
                    }
                });

                $scope.options = vm.options;
                $scope.data = vm.data;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.loadAll();

    }
})();

