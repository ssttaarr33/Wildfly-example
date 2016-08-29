angular.module('mainApp').controller('lights', function ($scope, $http, $resource) {
    var init = function () {
        var x = $resource('/control/do/lightsState');
        x.get(function (raspuns) {
            for (var i = 0; i < raspuns.Lights.length; i++) {
                if (raspuns.Lights[i].On)
                    raspuns.Lights[i].On = "on";
                else
                    raspuns.Lights[i].On = "off";
            }
            console.log(raspuns.Lights);
            $scope.params.lights = raspuns.Lights;
        });
    }
    init();

    $scope.animationsEnabled = true;

    $scope.params = {
        keyValue: "",
        invoiceId: "",
        lights: [],
        tasks: [],
        sortReverse: true,
        sortReverse1: true,
        turnOffLights: function () {
            var x = $resource('/control/do/turnLightsOff');
            x.get(function (raspuns) {
                console.log(raspuns.Lights);
                console.log(raspuns.results);
            });
        },
        switchSingleLight:function(light,state){
            var oldState = state;
            if(state === "on"){
                state = "off";
            }else{
                state = "on";
            }
            for(var i = 0;i < $scope.params.lights.length;i++){
                if($scope.params.lights[i].Light === light){
                    $scope.params.lights[i].On = state;
                }
            }
            var url = "/control/do/switchSingleLight";
            $http({
                method: 'POST',
                url: url,
                data: $.param({
                    light: light,
                    state: oldState
                }),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                console.log(response.data);                
            }),

                function (response) {
                    console.log(response.data);
                }
        },
        switchLights: function () {
            var url = "/control/do/switchLightsOff";
            $http({
                method: 'POST',
                url: url,
                data: $.param({
                    lights: JSON.stringify($scope.params.lights)
                }),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                console.log(response.data);
                for (var i = 0; i < response.data.newStates.length; i++) {
                    if (response.data.newStates[i].On)
                        response.data.newStates[i].On = "on";
                    else
                        response.data.newStates[i].On = "off";
                }                
                $scope.params.lights = response.data.newStates;
                $scope.params.tasks = response.data.results;
            }),

                function (response) {
                    $scope.auth = "not authorized";
                    var mesaj = "error";
                    console.log(mesaj);
                }
        },
        switchLightsOn: function () {

            var url = "/control/do/switchLightsOn";
            $http({
                method: 'POST',
                url: url,
                data: $.param({
                    lights: JSON.stringify($scope.params.lights)
                }),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                console.log(response.data);
                for (var i = 0; i < response.data.newStates.length; i++) {
                    if (response.data.newStates[i].On)
                        response.data.newStates[i].On = "on";
                    else
                        response.data.newStates[i].On = "off";
                }                
                $scope.params.lights = response.data.newStates;
                $scope.params.tasks = response.data.results;
            }),

                function (response) {
                    $scope.auth = "not authorized";
                    var mesaj = "error";
                    console.log(mesaj);
                }
        }
    };
});
