/**
 * Created by yfyuan on 2016/10/11.
 */
cBoard.controller('datasetCtrl', function ($scope, $http, $state, $stateParams, dataService, $uibModal, ModalUtils, $filter, chartService, $timeout, uuid4) {

    /**
     * 适配T2数据源
     * （1）值为0时，标识数据集使用T2数据库
     * （2）值为1时，标识数据集使用datasource字段关联的数据库
     * @type {number}
     */
    $scope.datasourceMode = 0;

    var translate = $filter('translate');
    $scope.optFlag = 'none';
    $scope.curDataset = {data: {expressions: [], filters: [], schema: {dimension: [], measure: []}}};
    $scope.curWidget = {};
    $scope.alerts = [];
    $scope.verify = {dsName: true};
    $scope.loadFromCache = true;
    $scope.queryAceOpt = cbAcebaseOption;
    $scope.hierarchy = translate("CONFIG.DATASET.HIERARCHY");
    $scope.uuid4 = uuid4;
    $scope.params = [];

    // 图表列表数据
    $scope.allWidgetDataList = [];

    // 用于控制维度列和指标列依赖转化的判断：当拖拽元素被图表依赖，不能转化为其他类型时，要删除对应列表添加的元素。
    // dropCreateNodeType：拖拽元素的类型。其数据值为： dimension/levelDimension/measure
    $scope.dropCreateNodeType = null;
    $scope.dropCreateNodeId = null;


    var treeID = 'dataSetTreeID'; // Set to a same value with treeDom
    var originalData = [];
    var updateUrl = "dashboard/updateDataset";

    var trash = {};

    /**
     * 从数组中删除元素
     * @param array  数组
     * @param index  要删除的元素
     */
    $scope.toTrash = function (array, index) {
        var o = array[index];
        if (o.type == 'column') {
            if (!trash[o.column]) {
                trash[o.column] = [];
            }
            trash[o.column].push(o);
        }
        array.splice(index, 1);
    };

    $scope.dndTransfer = {
        dimension: function (list, index, item, type) {
            if (type == 'column') {
                list[index] = {type: 'column', column: item};
            }
        },
        measure: function (list, index, item, type) {
            if (type == 'column') {
                list[index] = {type: 'column', column: item};
            }
        }
    };

    // $http.get("dashboard/getDatasourceList").success(function (response) {
    //     $scope.datasourceList = response;
    // });

    var getDatasetList = function () {
        $http.get("dashboard/getDatasetList").success(function (response) {
            $scope.datasetList = response;
            $scope.searchNode();
            if ($stateParams.id) {
                $scope.editDs(_.find($scope.datasetList, function (ds) {
                    return ds.id == $stateParams.id;
                }));
            }
        });
    };

    var getCategoryList = function () {
        $http.get("dashboard/getDatasetCategoryList").success(function (response) {
            $scope.categoryList = response;
            $("#DatasetName").autocomplete({
                source: $scope.categoryList
            });
        });
    };

    getCategoryList();
    getDatasetList();

    $scope.newDs = function () {
        $scope.optFlag = 'new';
        $scope.curDataset = {data: {expressions: [], filters: [], schema: {dimension: [], measure: []}}};
        $scope.curWidget = {};
        $scope.selects = [];
        cleanPreview();
    };

    $scope.editDs = function (ds) {

        doEditDs(ds);
        // $scopeConfigParams();

        //检查用户是否拥有该资源权限
        // $http.post("dashboard/checkDatasource", {id: ds.data.datasource}).success(function (response) {
        //     if (response.status == '1') {
        //         doEditDs(ds);
        //         $scopeConfigParams();
        //     } else {
        //         ModalUtils.alert(translate("ADMIN.CONTACT_ADMIN") + "：Datasource/" + response.msg, "modal-danger", "lg");
        //     }
        // });
    };

    /**
     * 编辑数据集
     * @param ds
     */
    var doEditDs = function (ds) {
        $scope.optFlag = 'edit';
        $scope.curDataset = angular.copy(ds);
        $scope.curDataset.name = $scope.curDataset.categoryName + '/' + $scope.curDataset.name;
        if (!$scope.curDataset.data.expressions) {
            $scope.curDataset.data.expressions = [];
        }
        if (!$scope.curDataset.data.filters) {
            $scope.curDataset.data.filters = [];
        }
        if (!$scope.curDataset.data.schema) {
            $scope.curDataset.data.schema = {dimension: [], measure: []};
        }
        $scope.datasource = _.find($scope.datasourceList, function (ds) {
            return ds.id == $scope.curDataset.data.datasource;
        });
        $scope.curWidget.query = $scope.curDataset.data.query;
        $scope.selects = ds.data.selects;
        //$scope.loadData();
    };

    $scope.checkExist = function (column) {
        var find = _.find($scope.curDataset.data.schema.measure, function (e) {
            return e.column == column;
        });
        if (!_.isUndefined(find)) {
            return true;
        }
        find = _.find($scope.curDataset.data.schema.dimension, function (e) {
            if (e.type == 'level') {
                var _find = _.find(e.columns, function (_e) {
                    return _e.column == column;
                });
                return !_.isUndefined(_find);
            } else {
                return e.column == column;
            }
        });
        return !_.isUndefined(find);
    };

    $scope.deleteDs = function (ds) {

        var confirmMsg = translate("COMMON.CONFIRM_DELETE")+" [ "+ds.name+" ]";
        ModalUtils.confirm(confirmMsg, "modal-warning", "lg", function () {
            $http.post("dashboard/deleteDataset", {id: ds.id}).success(function (serviceStatus) {
                if (serviceStatus.status == '1') {
                    getDatasetList();
                } else {
                    ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
                }
                $scope.optFlag = 'none';
            });
        });

        // $http.get("dashboard/getAllWidgetList").then(function (response) {
        //     if (!response) {
        //         return false;
        //     }
        //
        //     // 判断数据集是否被依赖，如果被图表依赖，则不允许删除。
        //     var resDs = [];
        //     for (var i = 0; i < response.data.length; i++) {
        //         if (response.data[i].data.datasetId == ds.id) {
        //             resDs.push(response.data[i].name);
        //         }
        //     }
        //     if (resDs.length > 0) {
        //         var warnStr = translate("CONFIG.WIDGET.WIDGET") + ":[" + resDs.toString() + "]";
        //         ModalUtils.alert(translate("COMMON.NOT_ALLOWED_TO_DELETE_BECAUSE_BE_DEPENDENT") + warnStr, "modal-warning", "lg");
        //         return false;
        //     }
        //
        //     var confirmMsg = translate("COMMON.CONFIRM_DELETE")+" [ "+ds.name+" ]";
        //     ModalUtils.confirm(confirmMsg, "modal-warning", "lg", function () {
        //
        //         $http.post("dashboard/deleteDataset", {id: ds.id}).success(function (serviceStatus) {
        //             if (serviceStatus.status == '1') {
        //                 getDatasetList();
        //             } else {
        //                 ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
        //             }
        //             $scope.optFlag = 'none';
        //         });
        //     });
        // });
    };


    /**
     * 获取图表列表数据，数据格式如下：
     * （1）groups：指标。
     * （2）keys：维度列。
     * （3）filters：可选过滤。
     * （4）expressions：可选表达式。
     *
     * @param callback
     */
    var getAllWidgetDataFieldList = function (callback) {
        // 第一次没有数据时，就去加载数据。后面就不加载数据。降低对服务器的性能压力。
        if ($scope.allWidgetDataList.length == 0) {
            $http.get("dashboard/getAllWidgetDataFieldList").then(function (response) {
                // console.log("getAllWidgetDataFieldList.response", response);
                $scope.allWidgetDataList = response.data;
                callback && callback(response.data);
            });
        } else {
            callback && callback($scope.allWidgetDataList);
        }

        // $http.get("dashboard/getAllWidgetDataFieldList").then(function (response) {
        //     callback && callback(response.data);
        // });
    };


    /**
     * 根据维度列获取图表名称数组（用于删除可选过滤时判断该维度列是否被图表依赖）
     * @param findDimension
     * @param callback
     */
    var getWidgetNameListByDimensionId = function (findDimension, callback) {
        if (!findDimension || _.isUndefined(findDimension.id)) {
            callback && callback([]);
            return;
        }
        getAllWidgetDataFieldList(function (widgetList) {
            var widgetNameList = [];
            if (widgetList) {
                for (var i = 0, widgetLength = widgetList.length; i < widgetLength; i++) {
                    var widget = widgetList[i];
                    if (_.isUndefined(widget.data) || _.isUndefined(widget.data.config)) {
                        continue;
                    }
                    // 搜寻行维
                    var keyList = widget.data.config.keys;
                    if (keyList) {
                        for (var j = 0, keysLength = keyList.length; j < keysLength; j++) {
                            var key = keyList[j];
                            if (key.id == findDimension.id || (!_.isUndefined(key.level) && key.id == findDimension.id)) {
                                widgetNameList.push(widget.name);
                                // 只要维度列被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                callback && callback(widgetNameList);
                                return;
                            }
                        }
                    }
                    // 搜寻列维
                    var groupList = widget.data.config.groups;
                    if (groupList) {
                        for (var j = 0, groupLength = groupList.length; j < groupLength; j++) {
                            var group = groupList[j];
                            if (group.id == findDimension.id || (!_.isUndefined(key.level) && key.id == findDimension.id)) {
                                widgetNameList.push(widget.name);
                                // 只要维度列被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                callback && callback(widgetNameList);
                                return;
                            }
                        }
                    }
                }
                callback && callback(widgetNameList);
            }
        });
    };

    /**
     * 根据指标列获取图表名称数组（用于删除可选过滤时判断该指标是否被图表依赖）
     * @param findMeasure
     * @param callback
     */
    var getWidgetNameListByMeasure = function (findMeasure, callback) {
        if (!findMeasure) {
            callback && callback([]);
            return;
        }
        getAllWidgetDataFieldList(function (widgetList) {
            var widgetNameList = [];
            if (widgetList) {
                for (var i = 0, widgetLength = widgetList.length; i < widgetLength; i++) {
                    var widget = widgetList[i];
                    if (_.isUndefined(widget.data) || _.isUndefined(widget.data.config) || _.isUndefined(widget.data.config.values)) {
                        continue;
                    }
                    var values = widget.data.config.values;
                    for (var j = 0, valuesLength = values.length; j < valuesLength; j++) {
                        var cols = values[j].cols;
                        if (!cols) {
                            continue;
                        }
                        for (var k = 0, colsLength = cols.length; k < colsLength; k++) {
                            var value = cols[k];
                            if (value.col == findMeasure.column) {
                                widgetNameList.push(widget.name);
                                // 只要表达式被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                callback && callback(widgetNameList);
                                return;
                            }
                        }

                    }
                }
                callback && callback(widgetNameList);
            }
        });
    };

    /**
     * 根据表达式ID列获取图表名称数组（用于删除可选表达式时判断该表达式是否被图表依赖）
     * @param findExpress   可选表达式
     * @param callback      回调函数
     */
    var getWidgetNameListByExpressId = function (findExpress, callback) {
        if (!findExpress || _.isUndefined(findExpress.id)) {
            callback && callback([]);
            return;
        }
        getAllWidgetDataFieldList(function (widgetList) {
            var widgetNameList = [];
            if (widgetList) {
                for (var i = 0, widgetLength = widgetList.length; i < widgetLength; i++) {
                    var widget = widgetList[i];
                    if (_.isUndefined(widget.data) || _.isUndefined(widget.data.config) || _.isUndefined(widget.data.config.values)) {
                        continue;
                    }
                    var values = widget.data.config.values;
                    for (var j = 0, valuesLength = values.length; j < valuesLength; j++) {
                        var cols = values[j].cols;
                        if (!cols) {
                            continue;
                        }
                        for (var k = 0, colsLength = cols.length; k < colsLength; k++) {
                            var value = cols[k];
                            if (!_.isUndefined(value.type) && value.type == 'exp' && value.id == findExpress.id) {
                                widgetNameList.push(widget.name);
                                // 只要表达式被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                callback && callback(widgetNameList);
                                return;
                            }
                        }

                    }
                }
                callback && callback(widgetNameList);
            }
        });
    };


    /**
     * 根据可选过滤ID列获取图表名称数组（用于删除可选过滤时判断该过滤是否被图表依赖）
     * @param findFilter   可选过滤
     * @param callback     回调函数
     */
    var getWidgetNameListByFilterId = function (findFilter, callback) {
        if (!findFilter || _.isUndefined(findFilter.id)) {
            callback && callback([]);
            return;
        }
        getAllWidgetDataFieldList(function (widgetList) {
            var widgetNameList = [];
            if (widgetList) {
                for (var i = 0, widgetLength = widgetList.length; i < widgetLength; i++) {
                    var widget = widgetList[i];
                    if (_.isUndefined(widget.data) || _.isUndefined(widget.data.config) || _.isUndefined(widget.data.config.filters)) {
                        continue;
                    }
                    var filters = widget.data.config.filters;
                    for (var j = 0, filtersLength = filters.length; j < filtersLength; j++) {
                        var filter = filters[j];
                        if (filter.id == findFilter.id) {
                            widgetNameList.push(widget.name);
                            // 只要指标被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                            callback && callback(widgetNameList);
                            return;
                        }
                    }
                }
                callback && callback(widgetNameList);
            }
        });
    };



    $scope.copyDs = function (ds) {
        var data = angular.copy(ds);
        data.name = data.name + "_copy";
        $http.post("dashboard/saveNewDataset", {json: angular.toJson(data)}).success(function (serviceStatus) {
            if (serviceStatus.status == '1') {
                $scope.optFlag = 'none';
                getDatasetList();
                ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
            } else {
                ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
            }
        });
    };

    var validate = function () {
        $scope.alerts = [];
        if (!$scope.curDataset.name) {
            $scope.alerts = [{msg: translate('CONFIG.DATASET.NAME') + translate('COMMON.NOT_EMPTY'), type: 'danger'}];
            $scope.verify = {dsName: false};
            $("#DatasetName").focus();
            return false;
        }
        for (i in $scope.params) {
            var name = $scope.params[i].name;
            var label = $scope.params[i].label;
            var required = $scope.params[i].required;
            var value = $scope.curWidget.query[name];
            if (required == true && value != 0 && (value == undefined || value == "")) {
                var pattern = /([\w_\s\.]+)/;
                var msg = pattern.exec(label);
                if (msg && msg.length > 0)
                    msg = translate(msg[0]);
                else
                    msg = label;
                $scope.alerts = [{msg: "[" + msg + "]" + translate('COMMON.NOT_EMPTY'), type: 'danger'}];
                $scope.verify[name] = false;
                return false;
            }
        }
        return true;
    };

    $scope.cancel = function(){
        $scope.optFlag = '';
    };

    /**
     * 保存数据集
     */
    $scope.save = function () {
        $scope.datasource ? $scope.curDataset.data.datasource = $scope.datasource.id : null;
        $scope.curDataset.data.query = $scope.curWidget.query;

        if (!validate()) {
            return;
        }
        var ds = angular.copy($scope.curDataset);
        var index = ds.name.lastIndexOf('/');
        ds.categoryName = $scope.curDataset.name.substring(0, index).trim();
        ds.name = $scope.curDataset.name.slice(index + 1).trim();
        if (ds.categoryName == '') {
            ds.categoryName = translate("COMMON.DEFAULT_CATEGORY");
        }

        if ($scope.optFlag == 'new') {
            $http.post("dashboard/saveNewDataset", {json: angular.toJson(ds)}).success(function (serviceStatus) {
                if (serviceStatus.status == '1') {
                    getCategoryList();
                    getDatasetList();
                    $scope.verify = {dsName: true};
                    ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                    if(serviceStatus.id){
                        ds.id = serviceStatus.id;
                        doEditDs(ds);
                    }
                } else {
                    $scope.alerts = [{msg: serviceStatus.msg, type: 'danger'}];
                }
            });
        } else {
            $http.post(updateUrl, {json: angular.toJson(ds)}).success(function (serviceStatus) {
                if (serviceStatus.status == '1') {
                    $scope.optFlag = 'edit';
                    getCategoryList();
                    getDatasetList();
                    $scope.verify = {dsName: true};
                    ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                } else {
                    $scope.alerts = [{msg: serviceStatus.msg, type: 'danger'}];
                }
            });
        }

    };

    $scope.editFilterGroup = function (col) {
        var columnObjs = schemaToSelect($scope.curDataset.data.schema);
        $uibModal.open({
            templateUrl: 'org/cboard/view/config/modal/filterGroup.html?_btv=' + resourceVersion,
            windowTemplateUrl: 'org/cboard/view/util/modal/window.html?_btv=' + resourceVersion,
            backdrop: false,
            scope: $scope,
            controller: function ($scope, $uibModalInstance) {
                if (col) {
                    $scope.data = angular.copy(col);
                } else {
                    $scope.data = {group: '', filters: [], id: uuid4.generate()};
                }
                $scope.columnObjs = columnObjs;
                $scope.close = function () {
                    $uibModalInstance.close();
                };
                $scope.addColumn = function (str) {
                    $scope.data.filters.push({col: str, type: '=', values: []})
                };
                $scope.ok = function () {
                    if(!$scope.data.group || $scope.data.group.toString().trim().length == 0){
                        ModalUtils.alert(translate('CONFIG.WIDGET.ALIAS') + translate('COMMON.NOT_EMPTY'), "modal-warning", "lg");
                        return;
                    }
                    if (col) {
                        col.group = $scope.data.group;
                        col.filters = $scope.data.filters;
                    } else {
                        if ($scope.$parent.curDataset.data.filters == null) {
                            $scope.$parent.curDataset.data.filters = [];
                        }
                        $scope.$parent.curDataset.data.filters.push($scope.data);
                    }
                    $uibModalInstance.close();
                };
                $scope.editFilter = function (filter) {
                    $uibModal.open({
                        templateUrl: 'org/cboard/view/dashboard/modal/param.html?_btv=' + resourceVersion,
                        windowTemplateUrl: 'org/cboard/view/util/modal/window.html?_btv=' + resourceVersion,
                        backdrop: false,
                        size: 'lg',
                        resolve: {
                            param: function () {
                                return angular.copy(filter);
                            },
                            filter: function () {
                                return false;
                            },
                            getSelects: function () {
                                return function (byFilter, column, callback) {
                                    dataService.getDimensionValues(undefined, $scope.curWidget.query, undefined, column, undefined, function (filtered) {
                                        callback(filtered);
                                    });
                                };
                            },
                            ok: function () {
                                return function (param) {
                                    filter.type = param.type;
                                    filter.values = param.values;
                                }
                            }
                        },
                        controller: 'paramSelector'
                    });
                };
            }
        });
    };

    /**
     * 删除层级维度元素
     * @param levelDimension    层级的维度列
     * @param index             下标
     */
    $scope.deleteDimensionLevelNode = function (levelDimension, callback) {
        if (_.isUndefined(levelDimension.columns) || levelDimension.columns.length == 0) {
            callback && callback([]);
            return;
        }
        getAllWidgetDataFieldList(function (widgetList) {
            var widgetNameList = [];
            if (widgetList) {
                // 遍历维度层级里面的元素
                for (var i = 0, columnsSize = levelDimension.columns.length; i < columnsSize; i++) {
                    var column = levelDimension.columns[i];
                    // 遍历图表列表
                    for (var j = 0, widgetLength = widgetList.length; j < widgetLength; j++) {
                        var widget = widgetList[j];
                        if (_.isUndefined(widget.data) || _.isUndefined(widget.data.config)) {
                            continue;
                        }

                        // 搜寻行维
                        var keyList = widget.data.config.keys;
                        if (keyList) {
                            for (var k = 0, keysLength = keyList.length; k < keysLength; k++) {
                                var key = keyList[k];
                                if (key.id == column.id || (!_.isUndefined(key.level) && key.id == column.id)) {
                                    widgetNameList.push(widget.name);
                                    // 只要维度列被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                    callback && callback(widgetNameList);
                                    return;
                                }
                            }
                        }

                        // 搜寻列维
                        var groupList = widget.data.config.groups;
                        if (groupList) {
                            for (var k = 0, groupLength = groupList.length; k < groupLength; k++) {
                                var group = groupList[k];
                                if (group.id == column.id || (!_.isUndefined(key.level) && key.id == column.id)) {
                                    widgetNameList.push(widget.name);
                                    // 只要维度列被一个图表引用，就返回数据。不必再查找，减少性能损耗。
                                    callback && callback(widgetNameList);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            callback && callback(widgetNameList);
        });
    };

    /**
     * 删除维度列元素
     * @param dimension     维度列对象
     * @param index         下标
     */
    $scope.deleteDimensionNode = function (dimension, index, columns, type) {
        if (dimension.type == 'column') {
            // 删除普通维度列
            getWidgetNameListByDimensionId(dimension, function (dependentWidgetNameList) {
                if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                    var msg = '维度列 [ ' + dimension.column + ' ] 不能删除，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                    ModalUtils.alert(msg, "modal-warning", "lg");
                } else {
                    if (type == 'level') {
                        ModalUtils.confirm(translate("CONFIG.DATASET.DIMENSION") + ": [" + dimension.column + "], " +
                            translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                            function () {
                                columns.splice(index, 1);
                            }
                        );
                    } else {
                        ModalUtils.confirm(translate("CONFIG.DATASET.DIMENSION") + ": [" + dimension.column + "], " +
                            translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                            function () {
                                $scope.curDataset.data.schema.dimension.splice(index, 1);
                            }
                        );
                    }

                }
            });
        } else if (dimension.type == 'level' && dimension.columns.length > 0) {
            // 删除层级元素
            $scope.deleteDimensionLevelNode(dimension, function (dependentWidgetNameList) {
                if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                    var msg = '维度层级 [ ' + dimension.alias + ' ] 不能删除，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                    ModalUtils.alert(msg, "modal-warning", "lg");
                } else {
                    ModalUtils.confirm(translate("CONFIG.DATASET.HIERARCHY") + ": [" + dimension.alias + "], " +
                        translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                        function () {
                            $scope.curDataset.data.schema.dimension.splice(index, 1);
                        }
                    );
                }
            });
        } else if (dimension.type == 'level' && dimension.columns.length == 0) {
            $scope.curDataset.data.schema.dimension.splice(index, 1);
        }

    };


    /**
     * 删除指标元素
     * @param measure   指标对象
     * @param index     下标
     */
    $scope.deleteMeasureNode = function (measure, index) {
        getWidgetNameListByMeasure(measure, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '指标 [ ' + measure.column + ' ] 不能删除，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                ModalUtils.confirm(translate("CONFIG.DATASET.MEASURE") + ": [" + measure.column + "], " +
                    translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                    function () {
                        $scope.curDataset.data.schema.measure.splice(index, 1);
                    }
                );

            }
        });
    };


    /**
     * 删除可选过滤器
     * @param filterGroup   可选过滤
     * @param index         下标
     */
    $scope.deleteFilterGroup = function (filterGroup, index) {

        getWidgetNameListByFilterId(filterGroup, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '可选过滤 [ ' + filterGroup.group + ' ] 不能删除，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                ModalUtils.confirm(translate("COMMON.FILTER_GROUP") + ": [" + $scope.curDataset.data.filters[index].group + "], " +
                    translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                    function () {
                        $scope.curDataset.data.filters.splice(index, 1)
                    }
                );
            }
        });


    };

    var schemaToSelect = function (schema, rawSelects) {
        if (schema.selects) {
            return angular.copy(schema.selects);
        } else {
            var selects = [];
            selects = selects.concat(schema.measure);
            _.each(schema.dimension, function (e) {
                if (e.type == 'level') {
                    _.each(e.columns, function (c) {
                        selects.push(c);
                    });
                } else {
                    selects.push(e);
                }
            });
            _.each(rawSelects, function (col) {
                if (_.find(selects, function (o) {
                        return col == o.column;
                    }) === undefined) {
                    selects.push({
                        column: col
                    });
                }
            });
            return angular.copy(selects);
        }
    };


    /**
     * 删除可选表达式
     * @param express   表达式对象
     * @param index     下标
     */
    $scope.deleteExpress = function (express, index) {

        getWidgetNameListByExpressId(express, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '表达式 [ ' + express.alias + ' ] 不能删除，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                ModalUtils.confirm(translate("CONFIG.COMMON.CUSTOM_EXPRESSION") + ": [" + $scope.curDataset.data.expressions[index].alias + "], " +
                    translate("COMMON.CONFIRM_DELETE"), "modal-warning", "lg",
                    function () {
                        $scope.curDataset.data.expressions.splice(index, 1)
                    }
                );
            }
        });

    };

    /**
     * 编辑可选表达式
     * @param col
     */
    $scope.editExp = function (col) {
        var aggregate = [
            {name: 'sum', value: 'sum'},
            {name: 'count', value: 'count'},
            {name: 'avg', value: 'avg'},
            {name: 'max', value: 'max'},
            {name: 'min', value: 'min'},
            // {name: 'distinct', value: 'distinct'}
        ];
        var ok;
        var data = {expression: ''};
        if (!col) {
            ok = function (exp, alias) {
                $scope.curDataset.data.expressions.push({
                    type: 'exp',
                    exp: data.expression,
                    alias: data.alias,
                    id: uuid4.generate()
                });
            }
        } else {
            data.expression = col.exp;
            data.alias = col.alias;
            ok = function (data) {
                col.exp = data.expression;
                col.alias = data.alias;
            }
        }
        var columnObjs = schemaToSelect($scope.curDataset.data.schema, $scope.selects);
        var expressions = $scope.curDataset.data.expressions;
        $uibModal.open({
            templateUrl: 'org/cboard/view/config/modal/exp.html?_btv=' + resourceVersion,
            windowTemplateUrl: 'org/cboard/view/util/modal/window.html?_btv=' + resourceVersion,
            backdrop: false,
            size: 'lg',
            scope: $scope,
            controller: function ($scope, $uibModalInstance) {
                $scope.data = data;
                $scope.columnObjs = columnObjs;
                $scope.aggregate = aggregate;
                $scope.expressions = expressions;
                $scope.alerts = [];
                $scope.expAceOpt = expEditorOptions($scope.selects, aggregate, function (_editor) {
                    $scope.expAceEditor = _editor;
                    $scope.expAceSession = _editor.getSession();
                    _editor.focus();
                });
                $scope.close = function () {
                    $uibModalInstance.close();
                };
                $scope.addToken = function (str, agg) {
                    var editor = $scope.expAceEditor;
                    editor.session.insert(editor.getCursorPosition(), str);
                    editor.focus();
                    if (agg) editor.getSelection().moveCursorLeft();
                };
                $scope.verify = function () {
                    $scope.alerts = [];
                    var v = verifyAggExpRegx($scope.data.expression);
                    $scope.alerts = [{
                        msg: v.isValid ? translate("COMMON.SUCCESS") : v.msg,
                        type: v.isValid ? 'success' : 'danger'
                    }];
                };
                $scope.ok = function () {
                    if (!$scope.data.alias) {
                        ModalUtils.alert(translate('CONFIG.WIDGET.ALIAS') + translate('COMMON.NOT_EMPTY'), "modal-warning", "lg");
                        return;
                    }
                    $scope.data.expression = $scope.expAceSession.getValue();
                    ok($scope.data);
                    $uibModalInstance.close();
                };
            }
        });
    };


    /**
     * 新增节点
     * 元素拖拽到维度列、指标列时会执行此方法。（首先执行create，然后执行delete）
     *
     * @param item
     * @returns {*|T|*}  如果方法没有返回值，则对应列表不会添加元素
     */
    $scope.createNode = function (item) {
        if (trash[item.column]) {
            var _i = trash[item.column].pop();
            if (_i) {
                return _i;
            }
        }
        item.id = uuid4.generate();
        return item;
    };

    /**
     * 新增维度列节点
     * 元素拖拽到维度时会执行此方法
     * @param item
     * @returns {*}
     */
    $scope.createDimensionNode = function (item) {
        if (_.isUndefined(item.id)) {
            // 新增字段，无需判断
            item.id = uuid4.generate();
            return item;
        }
        item.id = uuid4.generate();
        $scope.dropCreateNodeType = 'dimension';
        $scope.dropCreateNodeId = item.id;
        return item;
    };

    /**
     * 新增层级-维度列节点
     * 元素拖拽到层级维度时会执行此方法
     * @param item
     * @returns {*}
     */
    $scope.createLevelDimensionNode = function (item) {
        if (_.isUndefined(item.id)) {
            // 新增字段，无需判断
            item.id = uuid4.generate();
            return item;
        }
        item.id = uuid4.generate();
        $scope.dropCreateNodeType = 'levelDimension';
        $scope.dropCreateNodeId = item.id;
        return item;
    };


    /**
     * 新增指标列节点
     * 元素拖拽到指标列时会执行此方法。
     * 方法返回值作为一个指标列元素添加指标列数组当中。如不返回，则不能添加元素。
     *
     * @param item
     * @returns {*|T|*}
     */
    $scope.createMeasureNode = function (item) {
        if (_.isUndefined(item.id)) {
            // 新增字段，无需判断
            item.id = uuid4.generate();
            return item;
        }
        item.id = uuid4.generate();
        $scope.dropCreateNodeType = 'measure';
        $scope.dropCreateNodeId = item.id;
        return item;
    };


    /**
     * 拖拽维度列元素离开 维度列表 后执行的事件
     * @param dimension
     * @param index
     */
    $scope.onDimensionDndMoved = function (dimension, index) {
        // console.log("onDimensionDndMoved.dimension", dimension);
        // $scope.curDataset.data.schema.dimension.splice(index, 1);
        getWidgetNameListByDimensionId(dimension, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '维度列 [ ' + dimension.column + ' ] 目前不能转为其他类型的元素，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");

                // 根据对应类型，要删除添加的元素
                deleteDropDndMovedNode();
            } else {
                $scope.curDataset.data.schema.dimension.splice(index, 1);
            }
        });

    };

    /**
     * 拖拽层级的维度列元素离开 维度列表 后执行的事件
     * 这里加入依赖判断
     *
     * @param dimension
     * @param index
     */
    $scope.onLevelDimensionDndMoved = function (dimension, index, columns) {
        // console.log("onLevelDimensionDndMoved.dimension", dimension);
        // $scope.curDataset.data.schema.dimension.splice(index, 1);
        getWidgetNameListByDimensionId(dimension, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '维度列 [ ' + dimension.column + ' ] 目前不能转为其他类型的元素，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
                // 当拖拽元素被图表依赖，不能转化为其他类型时，要删除对应列表添加的元素。
                deleteDropDndMovedNode();
            } else {
                columns.splice(index, 1);
            }
        });

    };


    /**
     * 拖拽指标列元素离开 指标列 后执行的事件
     * @param measure
     * @param index
     */
    $scope.onMeasureDndMoved = function (measure, index) {
        // $scope.curDataset.data.schema.measure.splice(index, 1);
        getWidgetNameListByMeasure(measure, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '指标列 [ ' + measure.column + ' ] 目前不能转为其他类型的元素，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
                // 当拖拽元素被图表依赖，不能转化为其他类型时，要删除对应列表添加的元素。
                deleteDropDndMovedNode();
            } else {
                $scope.curDataset.data.schema.measure.splice(index, 1);
            }
        });

    };

    /**
     *  当拖拽元素被图表依赖，不能转化为其他类型时，要删除对应列表添加的元素。
     */
    var deleteDropDndMovedNode = function () {
        //console.log("");
        //console.log("deleteDropDndMovedNode.$scope.dropCreateNodeType",$scope.dropCreateNodeType);
        //console.log("deleteDropDndMovedNode.$scope.dropCreateNodeId",$scope.dropCreateNodeId);
        if ($scope.dropCreateNodeType == 'dimension') {
            var dimensionList = $scope.curDataset.data.schema.dimension;
            for (var i = 0, len = dimensionList.length; i < len; i++) {
                if ($scope.dropCreateNodeId == dimensionList[i].id) {
                    $scope.curDataset.data.schema.dimension.splice(i, 1);
                    return;
                }
            }
        } else if ($scope.dropCreateNodeType == 'levelDimension') {
            var dimensionList = $scope.curDataset.data.schema.dimension;
            for (var i = 0, len = dimensionList.length; i < len; i++) {
                if (dimensionList[i].type == 'level') {
                    for (var j = 0, levelLength = dimensionList[i].columns.length; j < levelLength; j++) {
                        if ($scope.dropCreateNodeId == dimensionList[i].columns[j].id) {
                            $scope.curDataset.data.schema.dimension[i].columns.splice(j, 1);
                            return;
                        }
                    }
                }
            }
        } else if ($scope.dropCreateNodeType == 'measure') {
            var measureList = $scope.curDataset.data.schema.measure;
            for (var i = 0, len = measureList.length; i < len; i++) {
                if ($scope.dropCreateNodeId == measureList[i].id) {
                    $scope.curDataset.data.schema.measure.splice(i, 1);
                    return;
                }
            }
        }
    };


    /**
     * 维度列转为指标列
     * @param index
     * @param dimension
     */
    $scope.dimensionToMeasure = function (index, dimension) {

        getWidgetNameListByDimensionId(dimension, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '维度列 [ ' + dimension.column + ' ] 目前不能转化为指标，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                $scope.curDataset.data.schema.dimension.splice(index, 1);
                $scope.curDataset.data.schema.measure.push(dimension);
            }
        });

    };


    /**
     * 层级维度的维度列转为指标列
     * @param index
     * @param dimension
     * @param columns
     */
    $scope.levelDimensionToMeasure = function (index, dimension, columns) {

        getWidgetNameListByDimensionId(dimension, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '维度列 [ ' + dimension.column + ' ] 目前不能转化为指标，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                columns.splice(index, 1);
                $scope.curDataset.data.schema.measure.push(dimension);
            }
        });

    };

    /**
     * 指标转化成维度列
     * @param index
     * @param object
     */
    $scope.measureToDimension = function (index, measure) {

        getWidgetNameListByMeasure(measure, function (dependentWidgetNameList) {
            if (dependentWidgetNameList && dependentWidgetNameList.length > 0) {
                var msg = '指标 [ ' + measure.column + ' ] 目前不能转化为维度列，它被图表所依赖！[ ' + dependentWidgetNameList.join(" , ") + ' ]';
                ModalUtils.alert(msg, "modal-warning", "lg");
            } else {
                $scope.curDataset.data.schema.measure.splice(index, 1);
                $scope.curDataset.data.schema.dimension.push(measure);
            }
        });
    };

    /**
     * 字段转化成维度列
     * @param object
     */
    $scope.toDimension = function (object) {
        $scope.curDataset.data.schema.dimension.push($scope.createNode(object));
    };


    $scope.custom = function (o) {
        var selects = $scope.selects;
        var datasource = $scope.datasource;
        $uibModal.open({
            templateUrl: 'org/cboard/view/config/modal/custom.html?_btv=' + resourceVersion,
            windowTemplateUrl: 'org/cboard/view/util/modal/window.html?_btv=' + resourceVersion,
            backdrop: false,
            size: 'lg',
            controller: function ($scope, $uibModalInstance) {
                $scope.c = o;
                $scope.ok = function () {
                    $uibModalInstance.close();
                };
                if (datasource) {
                    $scope.customAceOpt = schemaCustomOpt(selects, datasource.type);
                } else {
                    $scope.customAceOpt = schemaCustomOpt(selects, undefined);
                }
            }
        });
    };

    $scope.loadData = function () {
        cleanPreview();
        $scope.loading = true;

        /**
         * 读取数据集选择的字段
         */
        dataService.getColumns({
            // datasource: $scope.datasource.id,
            query: $scope.curWidget.query,
            datasetId: null,
            reload: !$scope.loadFromCache,
            callback: function (dps) {
                $scope.loading = false;
                $scope.toChartDisabled = false;
                if (dps.msg == "1") {
                    $scope.alerts = [];
                    $scope.selects = dps.columns;
                } else {
                    $scope.alerts = [{msg: dps.msg, type: 'danger'}];
                }

                var widget = {
                    chart_type: "table",
                    filters: [],
                    groups: [],
                    keys: [],
                    selects: [],
                    values: [{
                        cols: []
                    }
                    ]
                };
                // 构造widget模型
                _.each($scope.selects, function (c) {
                    widget.keys.push({
                        col: c,
                        type: "eq",
                        values: []
                    });
                });
                $scope.curDataset.data.selects = $scope.selects;
            }
        });
    };

    var cleanPreview = function () {
        $('#dataset_preview').html("");
    };

    /**  js tree related start **/
    $scope.treeConfig = jsTreeConfig1;

    $("#" + treeID).keyup(function (e) {
        if (e.keyCode == 46) {
            $scope.deleteNode();
        }
    });

    var getSelectedDataSet = function () {
        var selectedNode = jstree_GetSelectedNodes(treeID)[0];
        return _.find($scope.datasetList, function (ds) {
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
        $scope.copyDs(getSelectedDataSet());
    };

    $scope.editNode = function () {
        if (!checkTreeNode("edit")) return;
        var selectedNode = jstree_GetSelectedNodes(treeID)[0];
        //跳转到编辑数据集页面
        $state.go('config.dataset', {id: selectedNode.id}, {notify: false});
        $scope.editDs(getSelectedDataSet());
    };


    // 导出数据集
    $scope.exportDataSet = function () {
        if (!checkTreeNode("edit")) {
            return;
        }
        var selectedDataSet = getSelectedDataSet();
        $http.post("dashboard/exportDataSet",
            {json: angular.toJson(selectedDataSet)},
            {responseType: "blob"})
            .success(function (data, status, headers) {
                headers = headers();
                var fileSuffix = headers['x-file-suffix'] || '';
                var blob = new Blob([data], {type: "application/octet-stream"});
                var fileName = selectedDataSet.name + '.' + fileSuffix;
                var a = document.createElement("a");
                document.body.appendChild(a);
                awnload = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();
                a.remove();
            })
            .error(function (data, status) {
                console.log("exportDataSet error data:", data);
            });
    };

    /**
     * 进入导入数据集页面
     */
    $scope.toImportDatePage= function(){
        addPageToNavTab(translate("COMMON.IMPORT_DATASET"), basePath + '/olap/importDataset');
    };

    /**
     * 删除数据集操作
     */
    $scope.deleteNode = function () {
        if (!checkTreeNode("delete")) return;
        $scope.deleteDs(getSelectedDataSet());
    };
    $scope.showInfo = function () {
        if (!checkTreeNode("info")) return;
        var content = getSelectedDataSet();
        ModalUtils.info(content, "modal-info", "lg");
    };
    /**
     * 数据集搜索
     */
    $scope.searchNode = function () {
        var para = {dsName: '', dsrName: ''};
        //map datasetList to list (add datasourceName)
        var list = $scope.datasetList.map(function (ds) {
            var dsr = _.find($scope.datasourceList, function (obj) {
                return obj.id == ds.data.datasource
            });
            return {
                "id": ds.id,
                "name": ds.name,
                "categoryName": ds.categoryName,
                "datasourceName": dsr ? dsr.name : ''
            };
        });
        //split search keywords
        if ($scope.keywords) {
            if ($scope.keywords.indexOf(' ') == -1 && $scope.keywords.indexOf(':') == -1) {
                para.dsName = $scope.keywords;
            } else {
                var keys = $scope.keywords.split(' ');
                for (var i = 0; i < keys.length; i++) {
                    var w = keys[i].trim();
                    if (w.split(':')[0] == 'ds') {
                        para["dsName"] = w.split(':')[1];
                    }
                    if (w.split(':')[0] == 'dsr') {
                        para["dsrName"] = w.split(':')[1];
                    }
                }
            }
        }
        //filter data by keywords
        originalData = jstree_CvtVPath2TreeData(
            $filter('filter')(list, {name: para.dsName, datasourceName: para.dsrName})
        );

        jstree_ReloadTree(treeID, originalData);
    };

    $scope.treeEventsObj = function () {
        var baseEventObj = jstree_baseTreeEventsObj({
            ngScope: $scope, ngHttp: $http, ngTimeout: $timeout,
            treeID: treeID, listName: "datasetList", updateUrl: updateUrl
        });
        return baseEventObj;
    }();

    $scopeConfigParams = function () {
        // $http.get('dashboard/getConfigParams?type=' + $scope.datasource.type + '&page=dataset.html').then(function (response) {
        //     $scope.params = response.data;
        // });
    };

    $scope.changeDs = function () {
        $scope.curWidget.query = {};
        // $http.get('dashboard/getConfigParams?type=' + $scope.datasource.type + '&page=dataset.html').then(function (response) {
        //     $scope.params = response.data;
        //     for (i in $scope.params) {
        //         var name = $scope.params[i].name;
        //         var value = $scope.params[i].value;
        //         var checked = $scope.params[i].checked;
        //         var type = $scope.params[i].type;
        //         if (type == "checkbox" && checked == true) {
        //             $scope.curWidget.query[name] = true;
        //         }
        //         if (type == "number" && value != "" && !isNaN(value)) {
        //             $scope.curWidget.query[name] = Number(value);
        //         } else if (value != "") {
        //             $scope.curWidget.query[name] = value;
        //         }
        //     }
        // });
    };

    /**  js tree related end **/


    /** Ace Editor Starer... **/
    $scope.queryAceOpt = datasetEditorOptions();

});