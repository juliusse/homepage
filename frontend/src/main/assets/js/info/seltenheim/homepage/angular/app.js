'use strict';

define(['angular', //
        'ngRoute', //
        'info/seltenheim/homepage/angular/controllers/skillPageController'], //
    function (angular, ngRoute, skillPageController) {
        var homepageApp = angular.module('jsHomepageApp', ['ngResource', 'ngRoute']);
        skillPageController.attach(homepageApp);


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
