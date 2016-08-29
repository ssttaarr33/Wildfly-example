var mainApp = angular.module('mainApp', ['ui.router', 'ngCookies', 'ngResource', 'ngAnimate', 'ui.bootstrap', 'ngStorage']);

mainApp.config(
    function ($stateProvider, $urlRouterProvider, $locationProvider) {
        $urlRouterProvider.otherwise('/index');

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: 'templates/lights.html'
            })            
    });