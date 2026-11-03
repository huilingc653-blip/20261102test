/**
 * @histoty
 *  1. 2019-12-30  chenlw  移除单据配置查询、单据配置数据处理的相关方法。
 */
(function ($, undefined) {
    $.fn.getCursorPosition = function () {
        var el = $(this).get(0);
        var pos = 0;
        if ('selectionStart' in el) {
            pos = el.selectionStart;
        } else if ('selection' in document) {
            el.focus();
            var Sel = document.selection.createRange();
            var SelLength = document.selection.createRange().text.length;
            Sel.moveStart('character', -el.value.length);
            pos = Sel.text.length - SelLength;
        }
        return pos;
    }
})(jQuery);
layui.element();
var form = layui.form();
var app = angular.module('flowOption', ['ui.sortable']);
app.directive('tree', function () {
    return {
        require: '?ngModel', restrict: 'A',
        link: function ($scope, element, attrs, ngModel) {
            var setting = {
                data: {simpleData: {enable: true}},
                callback: {
                    beforeClick: function (treeId, treeNode, clickFlag) {
                        return !treeNode.isParent;
                    },
                    onClick: function (event, treeId, treeNode) {
                        $scope.category = treeNode.id;
                        $scope.currentFlowName = treeNode.name;
                        loadOptionList($scope);
                    }
                }
            };
            var ztree;
            $.get(basePath + "/bpm/flowCategory/tree?time="+new Date().getTime(), function (nodes) {
                ztree = $.fn.zTree.init(element, setting, nodes);
                ztree.expandAll(true);
            }, "JSON");
            $("#corpCollapseAll").click(function () {
                ztree.expandAll(false);
            });
            $("#corpExpandAll").click(function () {
                ztree.expandAll(true);
            });
        }
    }
});

var optionCtrl = ["$scope", '$http', function ($scope, $http) {
    $scope.category = null;
    $scope.flowList = [];
    $scope.usertaskList = [];
    $scope.extendParamsList = [];
    $scope.messageTemplate = {type: 3};
    $scope.selectType = [
        {label:"字符", value: 0},
        {label: bt.lang("bpm.option.selecttype.value"), value: 1},
        {label: bt.lang("bpm.option.selecttype.dict"), value: 2}
        ];
    $scope.selectVisible = [{label: bt.lang("bpm.option.sc_yn.yes"), value: 1}, {
        label: bt.lang("bpm.option.sc_yn.no"),
        value: 2
    }];

    $scope.dicts = [];
    $scope.currentFlowName;

    $http({method: 'GET', url: basePath + "/sys/dict/list.json"}).success(function (result, status, headers, config) {
        $scope.dicts = result;
    }).error(function (data, status, headers, config) {
        console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
    });

    $scope.addFlow = function () {
        var obj = {name: "", "type": "", fieldName: "", selectType: ""};
        $scope.flowList.push(obj);
        setTimeout(function () {
            form.render();
        }, 500)
    };

    form.on('select(selectType)', function (data) {
        var index = $(data.elem).attr("index");
        $scope.flowList[index].selectType = data.value;
        $scope.$apply();
        form.render();
    });

    form.on('select(dictGroup)', function (data) {
        var index = $(data.elem).attr("index");
        $scope.flowList[index].dictGroup = data.value;
        $scope.$apply();
        form.render();
    });

    form.on('select(selectVisible)', function (data) {
        var index = $(data.elem).attr("index");
        $scope.extendParamsList[index].visible = data.value;
        $scope.$apply();
        form.render();
    });

    $scope.removeFlow = function (i, flow) {
        layer.confirm(bt.lang("common.message.confirm.delete"), function (index) {
            $scope.flowList.splice(i, 1);
            $scope.$apply();
            layer.close(index);
            if (flow && flow.id) {
                $http({
                    method: 'GET',
                    url: basePath + "/bpm/option/delete",
                    params: {id: flow.id ? flow.id : ''}
                }).success(function (result, status, headers, config) {
                    layer.alert(result.msg, {icon: result.success ? 1 : 2});
                });
            }
        });

    };

    $scope.addUsertask = function () {
        var obj = {"name": "", "event": ""};
        $scope.usertaskList.push(obj);
    };

    $scope.removeUsertask = function (i, flow) {
        layer.confirm(bt.lang("common.message.confirm.delete"), function (index) {
            $scope.usertaskList.splice(i, 1);
            $scope.$apply();
            layer.close(index);
            if (flow && flow.id) {
                $http({
                    method: 'GET',
                    url: basePath + "/bpm/option/delete",
                    params: {id: flow.id ? flow.id : ''}
                }).success(function (result, status, headers, config) {
                    layer.alert(result.msg, {icon: result.success ? 1 : 2});
                });
            }
        });
    };

    $scope.copyData = function (item) {
        var pos = $('[name="messageTemplate"]').getCursorPosition();
        var tag = "$!{" + item.fieldName + "}";
        var msgTpl = $scope.messageTemplate.msgTpl;
        var beginText = '';
        var endText = '';
        if (msgTpl) {
            beginText = msgTpl.substring(0, pos);
            endText = msgTpl.substring(pos, msgTpl.length);
        }
        $scope.messageTemplate.msgTpl = beginText + tag + endText;
    };


    $scope.addExtendParam = function () {
        var obj = {name: "", type: "", fieldName: "", visible: "", sort: $scope.extendParamsList.length};
        $scope.extendParamsList.push(obj);
        setTimeout(function () {
            form.render();
        }, 500)
    };
    $scope.removeExtendParam = function (i, extendParam) {
        layer.confirm(bt.lang("common.message.confirm.delete"), function (index) {
            $scope.extendParamsList.splice(i, 1);
            $scope.$apply();
            layer.close(index);
            if (extendParam && extendParam.id) {
                $http({
                    method: 'GET',
                    url: basePath + "/bpm/option/delete",
                    params: {id: extendParam.id ? extendParam.id : ''}
                }).success(function (result, status, headers, config) {
                    layer.alert(result.msg, {icon: result.success ? 1 : 2});
                });
            }
        });
    };

    $scope.sortableOptions = {
        // called after a node is dropped
        // ui-sortable拖拽组件是双向数据绑定，当拖拽Item行变动的时候，数组的元素也随之变化
        stop: function (e, ui) {
            var tempList = $scope.extendParamsList.slice(0);
            // 按照下标排序
            for (var index = 0; index < tempList.length; index++) {
                tempList[index].sort = index;
            }
            $scope.extendParamsList = tempList;
        }
    };


    $scope.save = function () {
        if (!$scope.category) {
            layer.alert(bt.lang("bpm.option.please.select.category"));
            return;
        }
        var postData = [];
        var tempFlow = [];
        var result = false;
        angular.forEach($scope.flowList, function (data, index, array) {
            data.type = 1;
            if (data.name == "" || data.fieldName == "") {
                result = true;
                return false;
            }
            tempFlow.push(data);
        });
        if (result) {
            layer.alert(bt.lang("bpm.option.conditions.null"));
            return;
        }

        postData = postData.concat(tempFlow);

        var tempUserTask = [];
        angular.forEach($scope.usertaskList, function (data, index, array) {
            data.type = 2;
            if (data.name == "" || data.event == "") {
                result = true;
                return false;
            }
            tempUserTask.push(data);
        });
        if (result) {
            layer.alert(bt.lang("bpm.option.tasknode.null"));
            return;
        }
        postData = postData.concat(tempUserTask);

        postData = postData.concat([$scope.messageTemplate]);

        var tempExtendParamsList = [];
        angular.forEach($scope.extendParamsList, function (data, index, array) {
            data.type = 5;
            if (data.name == "" || data.fieldName == "") {
                result = true;
                return false;
            }
            tempExtendParamsList.push(data);
        });
        if (result) {
            layer.alert(bt.lang("bpm.option.customfield.null"));
            return;
        }
        postData = postData.concat(tempExtendParamsList);


        if (postData.length < 1) {
            layer.alert(bt.lang("bpm.option.no.are.set"));
            return;
        }

        layer.load(1);

        for (var i = 0; i < postData.length; i++) {
            postData[i]['category'] = $scope.category;
        }

        $http({
            method: 'POST',
            url: basePath + "/bpm/option/save",
            data: postData
        }).success(function (result, status, headers, config) {
            layer.closeAll('loading');
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                // 重新加载数据，解决点击保存按钮两次，会插入重复数据的问题
                loadOptionList($scope);
            }
        }).error(function (data, status, headers, config) {
            layer.closeAll('loading');
            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
        });

    };

}];


/**
 * 加载数据
 * @param $scope
 */
function loadOptionList($scope) {
    $.get(basePath + "/bpm/option/list/" + $scope.category, function (nodes) {
        var tempFlowList = [];
        var tempUsertaskList = [];
        var tempExtendParamsList = [];

        for (var key in nodes) {
            var node = nodes[key];
            if (node.type === 1) {
                // 流程条件
                tempFlowList.push(node);
            } else if (node.type === 2) {
                // 任务节点
                tempUsertaskList.push(node);
            } else if (node.type === 3) {
                // 消息模板
                $scope.messageTemplate = node;
            } else if (node.type === 4) {
                // 单据配置  已被弃用

            } else if (node.type === 5) {
                // 定制显示字段
                tempExtendParamsList.push(node);
            }
        }
        $scope.flowList = tempFlowList;
        $scope.usertaskList = tempUsertaskList;

        // 对定制显示字段排序
        tempExtendParamsList = tempExtendParamsList.sort(function (obj1, obj2) {
            return obj1.sort - obj2.sort;
        });

        $scope.extendParamsList = tempExtendParamsList;
        $scope.$apply();
        form.render();

    }, "JSON");
}
