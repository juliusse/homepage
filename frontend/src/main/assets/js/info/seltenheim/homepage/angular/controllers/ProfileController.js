'use strict';

define(['angular', 'ngResource', 'timeline'], //
    function (angular, ngResource, timeline) {
        var controller = {
            attach: function (angularApp) {

                angularApp.factory('EmploymentService', ['$resource', function ($resource) {
                    var EmploymentService = $resource('/api/employment', {}
                        , {
                            fetchAll: {
                                method: 'GET',
                                isArray: true
                            }
                        });


                    return EmploymentService;
                }]);

                angularApp.factory('EducationService', ['$resource', function ($resource) {
                    var EducationService = $resource('/api/education/:id', {}, {
                        fetchAll: {
                            method: 'GET',
                            isArray: true
                        }
                    });


                    return EducationService;
                }]);

                angularApp.controller("ProfileController", ['$scope', '$timeout', 'EmploymentService', 'EducationService', function ($scope, $timeout, EmploymentService, EducationService) {

                    // fields
                    $scope.employments = [];
                    $scope.educations = [];

                    $scope.allowModifications = false;
                    $scope.currentLangKey = "en";
                    $scope.timelineContainerId = null;
                    $scope.timelineHeightElementClass = null;
                    $scope.isInEditMode = {};

                    // methods
                    $scope.init = function (allowModifications, currentLangKey, timelineContainerId, timelineHeightElementClass) {
                        $scope.reloadEntries();
                        $scope.allowModifications = allowModifications;
                        $scope.currentLangKey = currentLangKey;
                        $scope.timelineContainerId = timelineContainerId;
                        $scope.timelineHeightElementClass = timelineHeightElementClass;
                    }

                    $scope.reloadEntries = function () {
                        EmploymentService.fetchAll().$promise.then(function (employments) { //success
                            $scope.employments = employments;
                            $scope.createTimeline();
                        }, function () { //failure
                            // TODO handle
                        })

                        EducationService.fetchAll().$promise.then(function (educations) { //success
                            $scope.educations = educations;
                            $scope.createTimeline();
                        }, function () { //failure
                            // TODO handle
                        })
                    }

                    $scope.submitEmployment = function (employment, event) {
                        if (!$scope.allowModifications) {
                            return false;
                        }

                        employment.$save({}, function () {
                            $scope.reloadEntries();
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

                    $scope.createTimeline = function () {
                        if ($scope.timelineContainerId === 'undefined') {
                            return;
                        }
                        $timeout(function () {
                            var eduEntryData = [];
                            $scope.educations.forEach(function (education) {
                                var endDate = education.toDate !== null ? new Date(education.toDate) :  new Date();
                                eduEntryData.push([education.place + ' - ' + education.degree, new Date(education.fromDate), endDate, education.id]);
                            });


                            var emplEntryData = [];
                            $scope.employments.forEach(function (employment) {
                                var endDate = employment.toDate !== null ? new Date(employment.toDate) : new Date();
                                emplEntryData.push([employment.place + ' - ' + employment.titleMap[$scope.currentLangKey], new Date(employment.fromDate), endDate, employment.id]);
                            });

                            var containerElement = document.getElementById($scope.timelineContainerId);
                            containerElement.style.height = document.getElementsByClassName($scope.timelineHeightElementClass)[0].clientHeight + 'px';
                            containerElement.innerHTML = "";
                            var timelineDiv = document.createElement('div');
                            timelineDiv.style.float = 'left';
                            timelineDiv.style.width = '90%';
                            timelineDiv.style.position = 'absolute';
                            timelineDiv.style.height = '100%';

                            containerElement.appendChild(timelineDiv);
                            var timeline = new Timeline(2008)

                            for (var index in eduEntryData) {
                                var entryData = eduEntryData[index];
                                var timelineEntry = new TimelineEntry(entryData[0], entryData[1], entryData[2], 'bfe5bf');
                                timeline.addTimelineEntry(timelineEntry);
                                timelineEntry.addHTMLElementToTriggerHover(document.getElementById(entryData[3]));
                            }

                            for (var index = (emplEntryData.length - 1); index >= 0; index--) {
                                var entryData = emplEntryData[index];
                                var timelineEntry = new TimelineEntry(entryData[0], entryData[1], entryData[2], 'fad8c7');
                                timeline.addTimelineEntry(timelineEntry);
                                timelineEntry.addHTMLElementToTriggerHover(document.getElementById(entryData[3]));
                            }

                            var visu = timeline.addVisualisation(Timeline.Visualisations.VerticalMinimal, timelineDiv);
                            visu.config.scale.numbersMarginRight = 25;
                            visu.repaint();
                        }, 10);
                    }
                }
                ]);
            }
        }
        return controller;
    }
)
