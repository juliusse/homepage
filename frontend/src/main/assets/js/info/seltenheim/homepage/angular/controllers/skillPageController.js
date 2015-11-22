'use strict';

define(['angular', 'ngResource'], //
    function (angular) {
        var controller = {
            attach: function (angularApp) {

                angularApp.factory('SkillGroupService', ['$resource', function ($resource) {
                    var SkillGroup = $resource('/api/skillgroups/:id', {
                        id: '@id'
                    }, {
                        fetchAll: {
                            method: 'GET',
                            isArray: true
                        }
                    });


                    return SkillGroup;
                }]);

                angularApp.controller("skillPageController", ['$scope', '$http', 'SkillGroupService', function ($scope, $http, SkillGroupService) {

                    // fields
                    $scope.skillGroups = [];
                    $scope.allowModifications = false;
                    $scope.currentLangKey = "en";
                    $scope.isInEditMode = {};

                    // methods
                    $scope.init = function (allowModifications, currentLangKey) {
                        $scope.loadGroups();
                        $scope.allowModifications = allowModifications;
                        $scope.currentLangKey = currentLangKey;
                    }

                    $scope.getSkillFontWeight = function (knowledge) {
                        return knowledge > 0.75 ? 'bold' : 'normal';
                    }

                    $scope.loadGroups = function () {
                        SkillGroupService.fetchAll().$promise.then(function (groups) { //success
                            $scope.skillGroups = groups;
                        }, function () { //failure
                            // TODO handle
                        })
                    }

                    $scope.submitChange = function (group, skill, sourceType, event) {
                        if (!$scope.allowModifications) {
                            return false;
                        }

                        var elementWidth = event.srcElement.clientWidth;
                        var clickPosition = event.offsetX;
                        var newValue = clickPosition / elementWidth;
                        if (sourceType === 'full') {
                            var curValue = skill.knowledge;
                            newValue = newValue * curValue;
                        }

                        group.skills.filter(function (val) {
                            return val.name === skill.name
                        })[0].knowledge = newValue;

                        group.$save({id: group.id}, function () {
                            $scope.loadGroups();
                        });
                    }

                    $scope.isEditModeEnabled = function (obj) {
                        return $scope.isInEditMode[obj];
                    };

                    $scope.setEditable = function (obj) {
                        if ($scope.allowModifications) {
                            $scope.isInEditMode[obj] = true;
                        }
                    }

                    $scope.editGroupName = function (group, langKey, $event) {
                        group.nameMap[langKey] = $event.srcElement.value;
                        $scope.isInEditMode[group.id] = false;

                        group.$save({id: group.id}, function () {
                            $scope.loadGroups();
                        });
                    }

                    $scope.editSkillName = function (group, skill, $event) {
                        group.skills.filter(function (val) {
                            return val.name === skill.name
                        })[0].name = $event.srcElement.value;

                        $scope.isInEditMode[skill.name] = false;
                        group.$save({id: group.id}, function () {
                            $scope.loadGroups();
                        });
                    }

                }]);
            }
        }
        return controller;
    }
);
