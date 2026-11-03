/**
 * 报表看板-增删改查Controller
 * Created by yfyuan on 2016/8/2.
 */
'use strict';
cBoard.controller('boardCtrl',
    function ($rootScope, $scope, $http, ModalUtils, $filter, updateService, $uibModal,
              $timeout, dataService, $state, $window, $stateParams) {

        // 定义重用HTML文件的路径：解决HTML文件被浏览器缓存的问题
        // 网格布局HTML文件
        $scope.boardGridFilePath = 'org/cboard/view/config/board/grid/grid.html?_btv=' + resourceVersion;
        // 时间轴布局HTML文件
        $scope.boardTimelineFilePath = 'org/cboard/view/config/board/timeline/timeline.html?_btv=' + resourceVersion;
        // 网格布局-参数行HTML文件
        $scope.boardGridParamFilePath = 'org/cboard/view/config/board/grid/param.html?_btv=' + resourceVersion;
        // 网格布局-图表行HTML文件
        $scope.boardGridWidgetFilePath = 'org/cboard/view/config/board/grid/widget.html?_btv=' + resourceVersion;
        // 时间轴布局-参数行HTML文件
        $scope.boardTimelineParamFilePath = 'org/cboard/view/config/board/timeline/param.html?_btv=' + resourceVersion;
        // 时间轴布局-图表行HTML文件
        $scope.boardTimelineWidgetFilePath = 'org/cboard/view/config/board/timeline/widget.html?_btv=' + resourceVersion;


        // 图表配置 HTML文件
        $scope.chartOptionsCommonGridFilePath = 'org/cboard/view/config/chart/options/common/grid.html?_btv=' + resourceVersion;
        $scope.chartOptionsCommonLegendFilePath = 'org/cboard/view/config/chart/options/common/legend.html?_btv=' + resourceVersion;
        $scope.chartOptionsCommonCategoryAxisFilePath = 'org/cboard/view/config/chart/options/common/categoryAxis.html?_btv=' + resourceVersion;

        var translate = $filter('translate');

        $scope.optFlag = 'none';
        $scope.curBoard = {layout: {rows: []}};
        $scope.alerts = [];
        $scope.verify = {boardName: true};
        $rootScope.freeLayout = false;
        $scope.treeData = [];

        // 记录用户权限分配的数据
        var boardUserPermissionAssignment = {
            userChooseType: 1,
            candidateUsers: [],     // 已选用户
            candidateGroups: []     // 已选角色
        };

        var treeID = "boardTreeID";
        var originalData = [];
        var updateUrl = "dashboard/updateBoard";

        /**
         * 初始化看板的用户权限数据
         */
        var initBoardUserPermissionAssignment = function () {
            var curBoard = angular.copy($scope.curBoard);
            boardUserPermissionAssignment = {
                userChooseType: 1,
                candidateUsers: [],     // 已选用户
                candidateGroups: []     // 已选角色
            };

            var userIdList = [];
            if (curBoard.userIdList) {
                userIdList = angular.copy(curBoard.userIdList);
            }
            _.each(userIdList, function (userId) {
                boardUserPermissionAssignment.candidateUsers.push({
                    value: userId
                });
            });

            var roleIdList = [];
            if (curBoard.roleIdList) {
                roleIdList = angular.copy(curBoard.roleIdList);
            }
            _.each(roleIdList, function (roleId) {
                boardUserPermissionAssignment.candidateGroups.push({
                    value: roleId
                });
            });
        };

        /**
         * 更新看板对象的权限数据
         * （注意：在modal页面分配权限后，点击确认按钮才执行此方法）
         */
        var setCurBoardPermission = function () {
            var userIdList = getPermissionUserIdList();
            var roleIdList = getPermissionRoleIdList();
            $scope.curBoard.userIdList = userIdList;
            $scope.curBoard.roleIdList = roleIdList;
        };

        /**
         * 获取已分配权限的用户ID列表
         */
        function getPermissionUserIdList() {
            var userIdList = [];
            _.each(boardUserPermissionAssignment.candidateUsers, function (user) {
                userIdList.push(user.value);
            });
            return userIdList;
        }

        /**
         * 获取已分配权限的用户ID列表
         */
        function getPermissionRoleIdList() {
            var roleIdList = [];
            _.each(boardUserPermissionAssignment.candidateGroups, function (role) {
                roleIdList.push(role.value);
            });
            return roleIdList;
        }


        $scope.openBoardHelpView = function () {
            $uibModal.open({
                templateUrl: 'org/cboard/view/help/v1.0/boardHelpView.html?_btv=' + resourceVersion,
                windowTemplateUrl: 'org/cboard/view/util/modal/windowHelp.html?_btv=' + resourceVersion,
                backdrop: false,
                size: 'lg',
                scope: $scope,
                controller: function ($scope, $uibModalInstance) {
                    $scope.close = function () {
                        $uibModalInstance.close();
                    };
                    $scope.ok = function () {
                        $uibModalInstance.close();
                    };
                }
            });
        };


        var getBoardList = function () {
            return $http.get("dashboard/getBoardList").success(function (response) {
                $scope.boardList = response;
                originalData = jstree_CvtVPath2TreeData(
                    $scope.boardList.map(function (ds) {
                        var categoryName = ds.categoryName == null ? translate('CONFIG.DASHBOARD.MY_REPORT_DASHBOARD') : ds.categoryName;
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
                    name: translate('CONFIG.DASHBOARD.MY_REPORT_DASHBOARD')
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

        var loadBoardDataset = function (status) {
            var datasetIdArr = [];
            var widgetArr = [];
            _.each($scope.curBoard.layout.rows, function (row) {
                _.each(row.widgets, function (widget) {
                    var w = _.find($scope.widgetList, function (w) {
                        return w.id == widget.widgetId
                    });
                    if (w.data.datasetId) {
                        datasetIdArr.push(w.data.datasetId);
                    } else {
                        widgetArr.push(w);
                    }
                });
            });
            datasetIdArr = _.union(datasetIdArr);
            $scope.boardDataset = [];
            _.each(datasetIdArr, function (d) {
                status.i++;
                dataService.getColumns({
                    datasource: null,
                    query: null,
                    datasetId: d,
                    callback: function (dps) {
                        $scope.alerts = [];
                        if (dps.msg == "1") {
                            var dataset = _.find($scope.datasetList, function (ds) {
                                return ds.id == d;
                            });
                            if (dataset != undefined) {
                                $scope.boardDataset.push({
                                    name: dataset.name,
                                    columns: dps.columns,
                                    datasetId: dataset.id
                                });
                            }
                            status.i--;
                        } else {
                            $scope.alerts = [{msg: dps.msg, type: 'danger'}];
                        }
                    }
                });
            });
            _.each(widgetArr, function (w) {
                status.i++;
                dataService.getColumns({
                    datasource: w.data.datasource,
                    query: w.data.query,
                    datasetId: null,
                    callback: function (dps) {
                        if (dps.msg == "1") {
                            $scope.boardDataset.push({name: w.name, columns: dps.columns, widgetId: w.id});
                            status.i--;
                        } else {
                            $scope.alerts = [{msg: dps.msg, type: 'danger'}];
                        }

                    }
                });
            });
        };

        var boardChange = function () {
            $scope.verify = {boardName: true};
            $scope.$emit("boardChange");
        };

        var boardListPromise = getBoardList();
        getCategoryList();
        getDatasetList();

        $scope.newOperate = function () {
            $('div.newBoard').toggleClass('hideOperate');
        };

        $scope.newGridLayout = function () {
            $rootScope.freeLayout = false;
            $scope.optFlag = 'new';
            $scope.curBoard = {layout: {rows: []}};
            $('div.newBoard').addClass('hideOperate');
        };

        $scope.newTimelineLayout = function () {
            $rootScope.freeLayout = false;
            $scope.optFlag = 'new';
            $scope.curBoard = {
                layout: {
                    type: 'timeline', rows: [{
                        height: '',
                        params: [],
                        type: 'param'
                    }]
                }
            };
            $('div.newBoard').addClass('hideOperate');
        };

        $scope.newFreeLayout = function () {
            $rootScope.freeLayout = true;
            $('div.newBoard').addClass('hideOperate');
        };

        // 编辑看板
        $scope.editBoard = function (board) {
            var b = angular.copy(board);
            updateService.updateBoard(b);
            $scope.curBoard = b;
            $scope.optFlag = 'edit';
        };

        $scope.copyBoard = function (board) {
            var o = angular.copy(board);
            o.name = o.name + '_copy';
            $http.post("dashboard/saveNewBoard", {json: angular.toJson(o)}).success(saveBoardCallBack);
        };

        $scope.deleteBoard = function (board) {
            //删除看板
            var confirmMsg = translate("COMMON.CONFIRM_DELETE")+" [ "+board.name+" ]";
            ModalUtils.confirm(confirmMsg, "modal-warning", "lg", function () {
                $http.post("dashboard/deleteBoard", {id: board.id}).success(function (serviceStatus) {
                    if (serviceStatus.status == '1') {
                        getBoardList();
                        boardChange();
                        ModalUtils.alert(serviceStatus.msg, "modal-success", "sm");
                    } else {
                        ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
                    }
                    $scope.optFlag == 'none';
                });
            });
        };

        $scope.showInfo = function () {
            if (!checkTreeNode("info")) return;
            var content = getSelectedBoard();
            ModalUtils.info(content, "modal-info", "lg");
        };

        $scope.widgetGroup = function (item) {
            return item.categoryName;
        };

        $scope.addWidget = function (row) {
            var w = {};
            w.name = translate('CONFIG.DASHBOARD.CHART_NAME');
            w.width = 12;
            w.widgetId = $scope.widgetList[0].id;
            row.widgets.push(w);
        };

        $scope.addRow = function () {
            var row = {type: 'widget', widgets: []};
            $scope.curBoard.layout.rows.push(row);
            return row;
        };

        $scope.addNode = function (node) {
            $scope.curBoard.layout.rows.push({node: node, type: 'widget', widgets: []});
        };

        $scope.addPramRow = function () {
            $scope.curBoard.layout.rows.unshift({type: 'param', params: []});
        };

        $scope.addRelations = function (widget) {
            widget.relations = {};
            widget.relations.relations = [];
            $scope.changeSourceCol(widget, widget.widgetId);
        };

        $scope.delRelations = function (widget) {
            if (widget.relations) {
                delete widget.relations;
            }
        };

        var validate = function () {
            $scope.alerts = [];
            if (!$scope.curBoard.name) {
                $scope.alerts = [{
                    msg: translate('CONFIG.DASHBOARD.NAME') + translate('COMMON.NOT_EMPTY'),
                    type: 'danger'
                }];
                $scope.verify = {boardName: false};
                $("#BoardName").focus();
                return false;
            }
            return true;
        };

        function saveBoardCallBack(serviceStatus) {
            if (serviceStatus.status == '1') {
                getBoardList();
                if (!$scope.curBoard.id) {
                    $scope.curBoard.id = serviceStatus.id;
                }
                $scope.optFlag = 'edit';
                ModalUtils.alert(serviceStatus.msg, "modal-success", "sm");
                boardChange();
            } else {
                ModalUtils.alert(serviceStatus.msg, "modal-warning", "sm");
            }
        }

        $scope.checkBeforPreview = function (Id) {  //看板预览操作
            $scope.isPreview = true;
            if (!validate()) {
                return;
            }
            ModalUtils.confirm(translate("COMMON.CONFIRM_SAVE_BEFORE_PREVIEW"), "modal-warning", "lg", function () {
                //修复添加看板时，执行预览操作后页面报错的问题
                $scope.saveBoard(true)
                    .then(function () {
                        if (!Id) {
                            Id = $scope.curBoard.id;
                        }
                        // 打开看板预览页面
                        // var pageName = $scope.curBoard.name;
                        // var openUrl = boardViewUrl + Id;
                        // addPageToNavTab(pageName, openUrl);
                        // $state.go('mine.view', {id: Id});

                        // 使用velocity渲染首页后，需要改变看板页面的进入方式
                        var pageName = $scope.curBoard.name;
                        var openUrl = boardViewBaseUrl + Id;
                        addPageToNavTab(pageName,openUrl);
                    });
            });
        };

        $scope.saveBoard = function (notify) {
            if (!validate()) {
                return;
            }
            clearDirty();
            var callBack = saveBoardCallBack;
            if (notify == false) {
                callBack = function () {
                };
            }
            if ($scope.optFlag == 'new') {
                return $http.post("dashboard/saveNewBoard", {json: angular.toJson($scope.curBoard)}).success(callBack);
            } else if ($scope.optFlag == 'edit') {
                return $http.post(updateUrl, {json: angular.toJson($scope.curBoard)}).success(callBack);
            }
        };

        $scope.cancel = function () {
            $scope.optFlag = 'none';
        };

        var clearDirty = function () {
            _.each($scope.curBoard.layout.rows, function (row) {
                _.each(row.widgets, function (widget) {
                    delete widget.sourceId;
                    if (!_.isUndefined(widget.relations)) {
                        delete widget.relations.sourceFields;
                        _.each(widget.relations.relations, function (relation) {
                            delete relation.targetFields;
                        });
                    }
                });
            })
        };

        $scope.editParam = function (row, index) {
            var status = {i: 0};
            loadBoardDataset(status);
            var parent = $scope;
            var ok;
            var param;
            if (_.isUndefined(index)) {
                param = {col: []};
                ok = function (p) {
                    if (!row.params) {
                        row.params = [];
                    }
                    row.params.push(p);
                };
            } else {
                param = angular.copy(row.params[index]);
                ok = function (p) {
                    row.params[index] = p;
                };
            }
            $uibModal.open({
                templateUrl: 'org/cboard/view/config/board/modal/param.html?_btv=' + resourceVersion,
                windowTemplateUrl: 'org/cboard/view/util/modal/window.html?_btv=' + resourceVersion,
                backdrop: false,
                size: 'lg',
                controller: function ($scope, $uibModalInstance) {
                    $scope.param_types = [
                        {name: translate('CONFIG.DASHBOARD.PARAM_TYPE_SELECTOR'), value: 'selector'},
                        // 隐藏滑动器
                        // {name: translate('CONFIG.DASHBOARD.PARAM_TYPE_SLIDER'), value: 'slider'}
                    ];
                    $scope.status = status;
                    $scope.param = param;
                    if (!$scope.param.paramType) {
                        $scope.param.paramType = 'selector';
                    }
                    $scope.boardDataset = parent.boardDataset;
                    $scope.add = function (selectedDataset, column) {
                        var v = angular.copy(selectedDataset);
                        delete v.columns;
                        v.column = column;
                        var paramCol = $scope.param.col;
                        var haveCol = null;
                        for (var i = 0; i < paramCol.length; i++) {
                            (paramCol[i].column == v.column && paramCol[i].name == v.name) ? haveCol = true : null;
                        }
                        (!haveCol || $scope.param.col == []) ? $scope.param.col.push(v) : null;
                    };
                    $scope.close = function () {
                        $uibModalInstance.close();
                    };
                    $scope.deleteSelected = function (index) {
                        var select = $scope.param.col[index].column;
                        var nodes = $('.cube>span');
                        for (var i = 0; i < nodes.length; i++) {
                            if (($(nodes[i]))[0].innerText == select) {
                                $(nodes[i]).removeClass('itemSelected');
                            }
                        }
                        $scope.param.col.splice(index, 1);
                    };
                    $scope.ok = function () {
                        if ($scope.param.name) {
                            ok($scope.param);
                            $uibModalInstance.close();
                        } else {
                            ModalUtils.alert(translate('CONFIG.DASHBOARD.ENTER_PARAMETER_NAME'), "modal-warning", "lg");
                        }
                    };
                    $scope.foldCube = function (cube, e) {
                        var node = (e.target.localName == 'img') ? e.target.parentNode.parentNode : e.target.parentNode;
                        var imgNode = node.getElementsByTagName("img");
                        if (e.target.className == "cubeName ng-binding" || e.target.localName == 'img') {
                            if (node.style.height == "25px" || node.style.height == "") {
                                node.style.height = 25 * (cube.columns.length + 1) + "px";
                                imgNode[0].style.webkitTransform = "rotate(90deg)";
                            } else {
                                node.style.height = "25px";
                                imgNode[0].style.webkitTransform = "rotate(0deg)";
                            }
                        } else if ($(e.target)[0].localName == 'span') {
                            $(e.target).addClass('itemSelected');
                        }
                        $scope.param.col.map(function (d) {
                            var columnSelect = d.column;
                            var cubeName = d.name;
                            var nodeList = $('.cube>span');
                            for (var i = 0; i < nodeList.length; i++) {
                                var name = nodeList[i].parentNode.firstElementChild.innerText;
                                if (($(nodeList[i]))[0].innerText == columnSelect && cubeName == name) {
                                    $(nodeList[i]).addClass('itemSelected');
                                }
                            }
                        });
                    };
                }
            });
        };


        /**  js tree related start **/
        $scope.treeConfig = angular.copy(jsTreeConfig1);
        $scope.treeConfig.plugins = ['types', 'unique', 'state', 'sort'];

        $("#" + treeID).keyup(function (e) {
            if (e.keyCode == 46) {
                $scope.deleteBoard(getSelectedBoard());
            }
        });

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

        $scope.copyNode = function () {
            if (!checkTreeNode("copy")) return;
            $scope.copyBoard(getSelectedBoard());
        };

        $scope.editNode = function () {
            if (!checkTreeNode("edit")) return;
            $scope.editBoard(getSelectedBoard());
            var selectedNode = jstree_GetSelectedNodes(treeID)[0];
            $state.go('config.board', {boardId: selectedNode.id}, {notify: false});
        };

        $scope.deleteNode = function () {
            if (!checkTreeNode("delete")) return;
            $scope.deleteBoard(getSelectedBoard());
        };

        $scope.treeEventsObj = function () {
            var baseEventObj = jstree_baseTreeEventsObj({
                ngScope: $scope, ngHttp: $http, ngTimeout: $timeout,
                treeID: treeID, listName: "boardList"
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

        $scope.changeSourceCol = function (e, widgetId) {
            if (!e.relations) {
                return;
            }
            //源表字段默认为原表的group key指定字段
            $http.get("dashboard/dashboardWidget?id=" + e.widgetId).then(function (response) {
                if (!response) {
                    return false;
                }
                var config = response.data.data.config;
                var fields = [];
                _.each(config.groups, function (e) {
                    fields.push(e.col);
                });
                _.each(config.keys, function (e) {
                    fields.push(e.col);
                });
                if (!e.relations.sourceField || e.relations.sourceField.length <= 0) {
                    e.relations.sourceField = fields;
                }
                e.relations.sourceFields = fields;
            });
        };

        $scope.changeTargetParam = function (e, boardId, index) {
            if (!e.relations) {
                return;
            }
            var w = _.find($scope.boardList, function (w) {
                return w.id == boardId;
            });
            if (!w) {
                return;
            }
            var cols = [];
            _.each(w.layout.rows, function (row) {
                if (row.type == "param") {
                    _.each(row.params, function (param) {
                        _.each(param.col, function (col) {
                            if ($.inArray(param.name, cols) == -1) {
                                cols.push(param.name); //col.column+"("+col.datasetId+")"
                            }
                        });
                    });
                }
            });
            //e.relations.relations[index].targetField = [];
            e.relations.relations[index].targetFields = cols;

        };

        $scope.addWidgetRelation = function (widget) {
            widget.relations.relations.push({"type": "widget"});
            $('div.newRelation').addClass('hideOperate');
        };

        $scope.addBoardRelation = function (widget) {
            widget.relations.relations.push({"type": "board"});
            $('div.newRelation').addClass('hideOperate');
        };

        $scope.changeActive = function (rowIndex, widgetIndex, index) {
            var prefixId = rowIndex + "_" + widgetIndex + "_";
            var list = $('li[id^=' + prefixId + '].active');
            if (list.length > 0 && list[0].id.split("_")[2] != index) {
                return;
            }
            if (index - 1 < 0) {
                index = 0;
            } else {
                index = index - 1;
            }
            $("#" + prefixId + index + "_" + "tab").addClass('active');
            $("#" + prefixId + index + "_" + "content").addClass('active');
        };

        var paramBoardId = $stateParams.boardId;
        if (paramBoardId) {
            boardListPromise.then(function () {
                var board = _.find($scope.boardList, function (ds) {
                    return ds.id == paramBoardId;
                });
                if (board) {
                    $scope.editBoard(board)
                }
            });
        }


        // 打开角色的用户列表
        var openRoleWatchPage = function (data) {
            $scope.watchData = data;
            $uibModal.open({
                templateUrl: 'org/cboard/view/config/board/assignment/watch-popup.html?_btv=' + resourceVersion,
                backdrop: false,
                scope: $scope,
                controller: function ($scope, $uibModalInstance) {
                    $scope.closeRoleUserList = function () {
                        $uibModalInstance.close();
                    };

                    $scope.data = {};
                    $scope.dataList = [];
                    $scope.cols = [0, 1, 2, 3];
                    $scope.loadding = false;

                    if ($scope.watchData.username) {
                        // 查看用户拥有角色
                        $scope.data = {
                            id: $scope.watchData.id || $scope.watchData.value,
                            username: $scope.watchData.username,
                            loginName: $scope.watchData.loginName
                        };
                        $scope.loadding = true;
                        $http({
                            method: 'GET',
                            url: basePath + "/sys/user/userRole",
                            params: {
                                userId: $scope.data.id
                            }
                        }).success(function (data, status, headers, config) {
                            $scope.dataList = data;
                            $scope.loadding = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loadding = false;
                            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
                        });
                    } else {
                        // 查询角色拥有的用户列表
                        $scope.data = {
                            id: $scope.watchData.id,
                            name: $scope.watchData.name
                        };
                        $scope.loadding = true;
                        // 根据角色ID获取用户列表
                        $http({
                            method: 'GET',
                            url: basePath + "/sys/role/roleSelectedUser",
                            params: {
                                roleId: $scope.data.id
                            }
                        }).success(function (data, status, headers, config) {
                            $scope.dataList = data;
                            $scope.loadding = false;
                        }).error(function (data, status, headers, config) {
                            $scope.loadding = false;
                            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
                        });
                    }
                }
            });
        };

        // 打开用户权限分配modal
        $scope.openUserPermissionAssignmentView = function () {
            // 初始化用户权限数据
            initBoardUserPermissionAssignment();
            $uibModal.open({
                templateUrl: 'org/cboard/view/config/board/assignment/assignment-popup.html?_btv=' + resourceVersion,
                backdrop: false,
                size: 'lg',
                scope: $scope,
                controller: function ($scope, $uibModalInstance) {
                    $scope.close = function () {
                        $uibModalInstance.close();
                    };
                    $scope.ok = function () {
                        // 保存用户权限分配数据
                        $scope.assignment.assignee = "${assignee}";
                        angular.copy($scope.assignment, $scope.assignmentOld);
                        boardUserPermissionAssignment = $scope.assignment;
                        setCurBoardPermission();
                        $uibModalInstance.close();
                    };

                    $scope.rolesCols = [0, 1, 2, 3];
                    $scope.roles = [];

                    $scope.passObj = {
                        userNumber: 0,
                        roleUserNumber: 0,
                        repeatUserNumber: 0
                    };

                    $scope.slider = {
                        value: 0,
                        options: {floor: 0, ceil: 0}
                    };
                    $scope.assignment = boardUserPermissionAssignment;
                    $scope.assignmentOld = {};


                    $scope.watchAction = function (data) {
                        openRoleWatchPage(data);
                    };

                    function getQueryString() {
                        var qs = location.search.substr(1), // 获取url中"?"符后的字串
                            args = {}, // 保存参数数据的对象
                            items = qs.length ? qs.split("&") : [], // 取得每一个参数项,
                            item = null,
                            len = items.length;

                        for (var i = 0; i < len; i++) {
                            item = items[i].split("=");
                            var name = decodeURIComponent(item[0]),
                                value = decodeURIComponent(item[1]);
                            if (name) {
                                args[name] = value;
                            }
                        }
                        return args;
                    }

                    var qs = getQueryString();

                    $scope.calculateRolesUsers = function () {
                        if (!$scope.assignment.candidateGroups || $scope.assignment.candidateGroups.length === 0) {
                            $scope.passObj.roleUserNumber = 0;
                            $scope.passObj.repeatUserNumber = 0;
                            return;
                        }
                        var ids = [];
                        var candidateGroups = $scope.assignment.candidateGroups;
                        angular.forEach(candidateGroups, function (data, key) {
                            ids.push(data.value);
                        });

                        // var deferred = $q.defer();
                        $http({
                            method: 'POST',
                            url: basePath + "/sys/user/listByRoleIdList",
                            params: {
                                ids: ids,
                                corpId: qs["corpId"]
                            }
                        }).success(function (data, status, headers, config) {
                            $scope.passObj.roleUserNumber = data.length;
                            var tempNumber = 0;
                            angular.forEach($scope.assignment.candidateUsers, function (userData, key) {
                                angular.forEach(data, function (userId, key) {
                                    if (userData.value === userId) {
                                        tempNumber++;
                                    }
                                });
                            });
                            $scope.passObj.repeatUserNumber = tempNumber;
                            // deferred.resolve();
                        }).error(function (data, status, headers, config) {
                            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
                        });
                        // deferred.promise;
                    };
                    // Put json representing assignment on scope
                    if ($scope.assignment) {

                        $scope.assignment = $scope.assignment;
                        angular.copy($scope.assignment, $scope.assignmentOld);

                        if ($scope.assignment.candidateUsers && $scope.assignment.candidateUsers.length !== 0) {

                            var userIds = [];
                            angular.forEach($scope.assignment.candidateUsers, function (data, key) {
                                userIds.push(data.value);
                            });
                            // 设置已选用户数量
                            $scope.passObj.userNumber = $scope.assignment.candidateUsers.length;
                            $http({
                                method: 'POST',
                                url: basePath + "/sys/user/listByUserIds",
                                params: {
                                    ids: userIds
                                }
                            }).success(function (data, status, headers, config) {
                                angular.forEach($scope.assignment.candidateUsers, function (tempA, keyA) {
                                    var userId = tempA.value;
                                    angular.forEach(data, function (tempB, keyB) {
                                        if (userId === tempB.id) {
                                            tempA.username = tempB.username;
                                            tempA.loginName = tempB.loginName;
                                            tempA.corpName = tempB.corpName;
                                            $scope.assignment.candidateUsers[keyA] = tempA;
                                        }
                                    });
                                });
                            });

                        } else {
                            $scope.assignment = $scope.assignment;
                        }
                        // 计算用户选择数量
                        $scope.calculateRolesUsers();
                    }

                    $scope.removeAssigneeUserValue = function () {
                        var temp = [];
                        for (var key in $scope.assignment) {
                            if (key !== "assignee" && key !== "assigneeName") {
                                temp[key] = $scope.assignment[key];
                            }
                        }
                        $scope.assignment = temp;
                    };
                    // 检查用户是否选择
                    $scope.checkUserSelected = function (user) {
                        var flag = false;
                        angular.forEach($scope.assignment.candidateUsers, function (data, key) {
                            if (data.value === user.id) {
                                flag = true;
                                return;
                            }
                        });
                        return flag;
                    };
                    // 检查角色是否选择
                    $scope.checkRoleSelected = function (role) {
                        var flag = false;
                        angular.forEach($scope.assignment.candidateGroups, function (data, key) {
                            if (data.value === role.id) {
                                flag = true;
                                return;
                            }
                        });
                        return flag;
                    };

                    $http({
                        method: 'GET',
                        url: basePath + "/sys/role/specialRole",
                        params: {corpId: qs["corpId"]}
                    }).success(function (data, status, headers, config) {
                        $scope.roles = data;
                    }).error(function (data, status, headers, config) {
                        console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
                    });

                    // Click handler for + button after enum value
                    var userValueIndex = 1;
                    $scope.addCandidateUserValue = function (index, user) {
                        // $scope.assignment.candidateUsers.push({value: user.id,name:user.username});
                        // 授权一个人
                        if ($scope.assignment.userChooseType === 0) {
                            $scope.assignment.assignee = user.id;
                            $scope.assignment.assigneeName = user.username;
                        } else {

                            // 授权多人
                            var flag = true;
                            angular.forEach($scope.assignment.candidateUsers, function (data, key) {
                                if (data.value == user.id) {
                                    flag = false;
                                    $scope.assignment.candidateUsers.splice(key, 1);
                                    //设置已选用户
                                    $scope.passObj.userNumber--;
                                    return;
                                }
                            });
                            if (flag) {
                                var userObj = {
                                    value: user.id,
                                    name: user.username,
                                    loginName: user.loginName,
                                    corpName: user.corpName
                                };
                                if (!$scope.assignment.candidateUsers) {
                                    $scope.assignment.candidateUsers = [];
                                    $scope.assignment.candidateUsers.push(userObj);

                                } else {
                                    $scope.assignment.candidateUsers.splice(index + 1, 0, userObj);
                                }
                                userValueIndex++;
                                //设置已选用户
                                $scope.passObj.userNumber++;
                            }
                            $scope.calculateRolesUsers();
                        }

                    };

                    // Click handler for - button after enum value
                    $scope.removeCandidateUserValue = function (index) {
                        $scope.assignment.candidateUsers.splice(index, 1);
                        $scope.passObj.userNumber--;
                        $scope.calculateRolesUsers();
                    };

                    var groupValueIndex = 1;
                    // 选择某个角色的用户列表
                    $scope.addCandidateGroupValue = function (index, role) {
                        var flag = true;
                        angular.forEach($scope.assignment.candidateGroups, function (data, key) {
                            if (data.value == role.id) {
                                flag = false;
                                $scope.assignment.candidateGroups.splice(key, 1);
                                return;
                            }
                        });

                        if (flag) {
                            var roleObj = {value: role.id, name: role.name};
                            if (!$scope.assignment.candidateGroups) {
                                $scope.assignment.candidateGroups = [];
                                $scope.assignment.candidateGroups.push(roleObj);
                            } else {
                                $scope.assignment.candidateGroups.splice(index + 1, 0, roleObj);
                            }
                            groupValueIndex++;
                        }
                        $scope.calculateRolesUsers();
                    };

                    // Click handler for - button after enum value
                    $scope.removeCandidateGroupValue = function (index) {
                        $scope.assignment.candidateGroups.splice(index, 1);
                        $scope.calculateRolesUsers();
                    };

                }
            });
        };




    });
