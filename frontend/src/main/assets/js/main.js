'use strict';

require.config({
    paths : {
        angular : '../lib/angularjs/angular',
        ngResource : '../lib/angularjs/angular-resource.min',
        ngRoute : '../lib/angularjs/angular-route.min',
        app: 'info/seltenheim/homepage/angular/app'
    },
    shim : {
        'angular' : {
            exports : 'angular'
        },
        'ngResource' : [ 'angular' ],
        'ngRoute' : [ 'angular' ]
    },
    priority : [ "jquery", "angular" ]

});

define([
// These are path alias that we configured in our bootstrap
    'angular' //
    , 'app' ], function(angular, app) {

    angular.element(document).ready(function() {
        angular.bootstrap(document, [ 'jsHomepageApp' ]);
    });
});
