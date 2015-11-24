'use strict';

define(['angular', //
        'ngRoute', //
        'info/seltenheim/homepage/angular/controllers/skillPageController',//
        'info/seltenheim/homepage/angular/controllers/ProfileController' ], //
    function (angular, ngRoute, skillPageController, ProfileController) {
        var homepageApp = angular.module('jsHomepageApp', ['ngResource', 'ngRoute']);
        skillPageController.attach(homepageApp);
        ProfileController.attach(homepageApp);

        homepageApp.directive('focusOnShow', function($timeout) {
            return {
                restrict: 'A',
                link: function($scope, $element, $attr) {
                    if ($attr.ngShow){
                        $scope.$watch($attr.ngShow, function(newValue){
                            if(newValue){
                                $timeout(function(){
                                    $element.focus();
                                }, 0);
                            }
                        })
                    }
                    if ($attr.ngHide){
                        $scope.$watch($attr.ngHide, function(newValue){
                            if(!newValue){
                                $timeout(function(){
                                    $element.focus();
                                }, 0);
                            }
                        })
                    }

                }
            };
        })

        return homepageApp;
    });
