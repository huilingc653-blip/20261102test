/**
 * 看板列表查看页面
 * Created by chenlw on 2019/11/06.
 */
'use strict';
cBoard.controller('dashboardListViewCtrl',
    function ($rootScope, $scope, $http, ModalUtils, $filter, updateService, $uibModal,
              $timeout, dataService, $state, $window, $stateParams) {
        var translate = $filter('translate');

        $scope.optFlag = 'none';
        $scope.curBoard = {layout: {rows: []}};
        $scope.alerts = [];
        $scope.verify = {boardName: true};
        $rootScope.freeLayout = false;
        $scope.treeData = [];

        var treeID = "boardTreeID";
        var originalData = [];

        /**
         * 获取看板列表
         * @returns {*}
         */
        var getBoardList = function () {
            return $http.get("dashboard/getBoardListByPermission").success(function (response) {
                $scope.boardList = response;
                originalData = jstree_CvtVPath2TreeData(
                    $scope.boardList.map(function (ds) {
                        var categoryName = ds.categoryName == null ? translate('SIDEBAR.MY_REPORT_DASHBOARD') : ds.categoryName;
                        return {
                            "id": ds.id,
                            "name": ds.name,
                            "categoryName": categoryName
                        };
                    })
                );
                jstree_ReloadTree(treeID, originalData);
            });
        };

        var getCategoryList = function () {
            $http.get("dashboard/getCategoryList").success(function (response) {
                $scope.categoryList = [{
                    id: null,
                    // name: translate('CONFIG.DASHBOARD.MY_DASHBOARD')
                    name: translate('SIDEBAR.MY_REPORT_DASHBOARD')
                }];
                _.each(response, function (o) {
                    $scope.categoryList.push(o);
                });
            });
        };

        var getDatasetList = function () {
            $http.get("dashboard/getDatasetList")
                .then(function (response) {
                    $scope.datasetList = response.data;
                    return $http.get("dashboard/getWidgetList");
                })
                .then(function (response) {
                        $scope.widgetList = response.data;
                        $scope.widgetList = $scope.widgetList.map(function (w) {
                            if (w.data.datasetId != null) {
                                var dataset = _.find($scope.datasetList, function (ds) {
                                    return ds.id == w.data.datasetId;
                                });
                                w.dataset = dataset == null ? 'Lost DataSet' : dataset.name;
                            } else {
                                w.dataset = "Query";
                            }
                            return w;
                        });
                    }
                );
        };

        var boardListPromise = getBoardList();
        getCategoryList();
        getDatasetList();

        $scope.newOperate = function () {
            $('div.newBoard').toggleClass('hideOperate');
        };


        $scope.showInfo = function () {
            if (!checkTreeNode("info")) return;
            var content = getSelectedBoard();
            ModalUtils.info(content, "modal-info", "lg");
        };


        /**  js tree related start **/
        $scope.treeConfig = angular.copy(jsTreeConfig1);
        $scope.treeConfig.plugins = ['types', 'unique', 'state', 'sort'];

        var getSelectedBoard = function () {
            var selectedNode = jstree_GetSelectedNodes(treeID)[0];
            return _.find($scope.boardList, function (ds) {
                return ds.id == selectedNode.id;
            });
        };

        var checkTreeNode = function (actionType) {
            return jstree_CheckTreeNode(actionType, treeID, ModalUtils.alert);
        };

        var switchNode = function (id) {
            $scope.ignoreChanges = false;
            var dataSetTree = jstree_GetWholeTree(treeID);
            dataSetTree.deselect_all();
            dataSetTree.select_node(id);
        };

        $scope.applyModelChanges = function () {
            return !$scope.ignoreChanges;
        };


        $scope.editNode = function () {
            // 双击节点事件
            var selectedNode = jstree_GetSelectedNodes(treeID)[0];
            // var boardId = selectedNode.id;
            // var openUrl = boardViewUrl + boardId;
            // addPageToNavTab(selectedNode.text, openUrl);
            // top.bt.navtab.addNavtab(selectedNode.text,openUrl);
            // $state.go('config.board', {boardId: selectedNode.id}, {notify: false});

            var boardId = selectedNode.id;
            var openUrl = boardViewBaseUrl + boardId;
            addPageToNavTab(selectedNode.text,openUrl);
        };

        $scope.treeEventsObj = function () {
            var baseEventObj = jstree_baseTreeEventsObj({
                ngScope: $scope,
                ngHttp: $http,
                ngTimeout: $timeout,
                treeID: treeID,
                listName: "boardList"
                //, updateUrl: updateUrl
            });
            return baseEventObj;
        }();
        /**  js tree related start **/

        $scope.changeTargetCol = function (e, widgetId, index, row) {
            if (!e.relations) {
                return;
            }
            var w = _.find($scope.widgetList, function (w) {
                return w.id == widgetId;
            });
            if (!w) {
                return;
            }
            var dataSet = _.find($scope.datasetList, function (e) {
                return w.data.datasetId === e.id;
            });
            var cols = [];
            _.each(dataSet.data.schema.dimension, function (e) {
                if (e.type == "column") {
                    if ($.inArray(e, cols) == -1) {
                        cols.push(e.column);
                    }
                } else if (e.type == "level") {
                    _.each(e.columns, function (e) {
                        if ($.inArray(e, cols) == -1) {
                            cols.push(e.column);
                        }
                    });
                }
            });
            e.relations.relations[index].targetFields = cols;
            if (cols.length == 0) {
                dataService.getColumns({
                    datasource: null,
                    query: null,
                    datasetId: w.data.datasetId,
                    callback: function (dps) {
                        $scope.alerts = [];
                        if (dps.msg == "1") {
                            e.relations.relations[index].targetFields = dps.columns;
                        } else {
                            $scope.alerts = [{msg: dps.msg, type: 'danger'}];
                        }
                    }
                });
            }

            //add target widget
            var flattenWgts = [];
            _.each($scope.curBoard.layout.rows, function (row) {
                flattenWgts = flattenWgts.concat(row.widgets);
            });
            if (_.where(flattenWgts, {"widgetId": widgetId}).length > 0 || _.isUndefined(row)) {
                return;
            }
            e.relations.relations[index].targetField = [];
            var w = {};
            w.name = _.find($scope.widgetList, function (e) {
                return e.id === widgetId
            }).name;
            w.width = 12;
            w.widgetId = widgetId;
            w.sourceId = e.widgetId;
            w.index = index;
            row.widgets = _.filter(row.widgets, function (e) {
                return e.sourceId !== w.sourceId || e.index !== index;
            });
            row.widgets.push(w);
        };


    });
