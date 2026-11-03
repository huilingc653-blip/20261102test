/**
 * 管理单元组件：
 * wangzy
 * 2020年5月13日
 * 0.0.1单元组件支持单选和多选
 */
layui.define(['jquery', 'form', 'selectTree'], function (exports) {
    var $ = layui.jquery, form = layui.form();
    var layer = parent ? parent.bt.layer : bt.layer;

    var cell = function () {
        this.bizName = "cell";
        this.currentObject = null;
    }

    cell.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    cell.prototype.init = function () {
        this.render();
    };

    cell.prototype.render = function () {
        var _this = this;
        $('.bt-biz-cell:not(.init)').each(function () {
            var that = $(this);
            //表单类型
            var type = that.data('type');
            switch (type) {
                case 'selectTree':
                    _this.initSelectTree(that);
                    //下拉树
                    that.removeAttr('data-placeholder');
                    that.removeAttr('data-callBack');
                    that.removeAttr('data-callBackData');
                    that.removeAttr('data-tree-type');
                    that.removeAttr('data-resource');
                    break;
            }

            that.removeAttr('data-name');
            that.removeAttr('data-biz-type');
            that.removeAttr('data-type');
            that.removeAttr('data-required');


        });
    };


    cell.prototype.initSelectTree = function (elem) {
        var that = this;
        //输入框名字
        var name = elem.data("name");
        var className = "biz-cell-select-tree-" + name.replace(".", "-");

        //获取数据的url
        var treeUrl = basePath+ "/sys/cell/";
        //占位符
        var placeholder = elem.data('placeholder');
        var bizName = elem.data("biz-name");
        var treeType = elem.data('tree-type');
        //目前节点
        var currentNode = elem.data('currentnode');
        //回调函数
        var callBack = elem.data('callback');
        var callBackData = elem.data('callbackdata');
        var afterInit = elem.data('afterInit'.toLowerCase());
        elem.addClass(className);
        //默认值
        var defaultValue = elem.data("defaultvalue");
        var checkkboxtype = elem.data("checkkboxtype");

        var beforeInit = elem.data("beforeInit".toLowerCase());

        var fontCss = elem.data("fontCss".toLowerCase());

        var textStyle = elem.data("textStyle".toLowerCase()); //设置文本内容的样式参数function
        var selectDisabled = elem.data("disabled"); //所有值都禁用
        if (!checkkboxtype) {
            // checkkboxtype = { "Y": "", "N": "" };
            checkkboxtype = {"Y": "s", "N": "ps"};
        } else { //将字符串转为对象
            if (typeof (checkkboxtype) === 'string') {
                checkkboxtype = (new Function('return ' + checkkboxtype + ';'))();
            }
        }

        callBack = this.makeCallBack(callBack);
        callBackData = this.makeCallBack(callBackData);
        afterInit = this.makeCallBack(afterInit);

        var fontCssFunc = this.makeCallBack(fontCss);

        var textStyleFunc = this.makeCallBack(textStyle);

        beforeInit = this.makeCallBack(beforeInit);
        var deleteFlag = elem.data('deleteFlag') ? true : false;
        var required = elem.data('required');

        //2017-09-05 14:03 新增，级联选择
        var treeGroup = elem.data('group'.toLowerCase());
        var change = elem.data('change'.toLowerCase());
        var tips = elem.data('tips'.toLowerCase());
        // 2019-03-28  新增加参数data-valueField，用于设置选中赋值属性，默认为id
        var valueField = elem.data("valueField".toLowerCase());
        //管理单元路径
        treeUrl += "treeData";

        var lazyLoad = false;
        var param = {};
        if (treeGroup && window.treeGroup && window.treeGroup[treeGroup]) {
            var treeList = window.treeGroup[treeGroup];
            for (var k in treeList) {
                var tl = treeList[k];
                if (tl.config.change === name) {
                    lazyLoad = true;
                    if (tl.config.selected) {
                        param.id = tl.config.selected
                    }
                    break;
                }
            }
        }

        var treeDataInit = function (nodes, flag) {
            for (var k in nodes) {
                var node = nodes[k];

                // 如currentNode存在，则去除当前节点及子节点
                if(currentNode == node.id){
                    nodes.splice(k,1);
                }

                if (node && node.children && node.children.length > 0) {
                    // flag为true复选框时，只有叶子节点可供选择
                    if(flag){
                        node.chkDisabled = true;
                    }
                    node.children = treeDataInit(node.children, flag);
                }
            }
            return nodes
        };

        var tree = bt.selectTree.initGetCurrent({
            url: treeUrl,
            elem: "." + className,
            name: name,
            bizName: bizName,
            checkType: treeType,
            valueField: valueField ? valueField : 'id',
            setting: {check: {chkboxType: checkkboxtype}},
            placeholder: placeholder,
            currentNode:currentNode,
            clickCallBack: callBack,
            disabled: selectDisabled,
            clickCallBackData: function (treeNode) {
                if (treeGroup && tree.config.change) {
                    var changeTreeObj = window.treeGroup[treeGroup][tree.config.change];
                    // var zTree = $.fn.zTree.getZTreeObj(changeTreeObj.param.treeList);
                    var data = {
                        id: treeNode.id
                    }
                    changeTreeObj.setData(data);
                }
                if ($.isFunction(callBackData)) {
                    callBackData(treeNode);
                }

            },
            selected: defaultValue,
            deleteFlag: deleteFlag,
            required: required,
            fontCss: fontCssFunc,
            textStyle: textStyleFunc, //设置文本内容的样式
            change: change,
            lazyLoad: lazyLoad,
            beforeClick: function (treeId, treeNode, clickFlag) {
            },
            beforeInit: function (nodes) {
                var flag = treeType === "check";

                tempNodes = treeDataInit(nodes, flag);

                if (beforeInit) {
                    tempNodes = beforeInit(tempNodes);
                }
                return tempNodes;
            },
            //输入框点击产生之前的事件，返回true允许通过，返回false不允许通过
            inputBeforeClick: function () {
                if (treeGroup) {
                    var treeGroups = window.treeGroup[treeGroup];
                    var parentGroup;
                    for (var k in treeGroups) {
                        var group = treeGroups[k];
                        if (group.config.change === name) {
                            parentGroup = group;
                            break;
                        }
                    }
                    if (parentGroup && !parentGroup.val()) {
                        layer.alert(tips ? tips : bt.lang("common.msg.selectParentNode"), {icon: 2});//"请选择父级"
                        return false;
                    }
                }
                return true;
            },
            clearCallback: function () {
                if (treeGroup) {
                    var treeGroups = window.treeGroup[treeGroup];
                    for (var k in treeGroups) {
                        var group = treeGroups[k];
                        if (group.config.change) {
                            var changeTree = window.treeGroup[treeGroup][group.config.change];
                            changeTree.clearValue(changeTree);
                        }
                    }
                }
            },
            initComplete: function (ztree) {
                if (treeGroup && change && defaultValue) {
                    var changeTree = window.treeGroup[treeGroup][change];
                    changeTree.setData(defaultValue)
                }
                if (afterInit) {
                    afterInit();
                }

            }
        });


        if (treeGroup) {
            if (!window.treeGroup) {
                window.treeGroup = [];
            }
            if (!window.treeGroup[treeGroup]) {
                window.treeGroup[treeGroup] = [];
            }
            window.treeGroup[treeGroup][tree.config.name] = tree;
        }

        //将树绑定到window全局对象中
        var bizName = elem.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName][bizName] = {type: elem.data('type'), obj: tree};
        }
    };


    cell.prototype.makeCallBack = function (callBack) {
        //校验下拉树的回调方法是否存在
        if (callBack && typeof (callBack) != "function") {
            try {
                callBack = eval(callBack);
            } catch (e) {
                alert(callBack + 'not exist！');
            }

        }
        return callBack;
    }

    cell.prototype.val = function (value) {
        var that = this;
        if (value === undefined) {
            if (!that.currentObject) {
                return "";
            }

            if (that.currentObject.type === "selectTree") {
                var tree = that.currentObject.obj;
                return tree.val();
            } else if (that.currentObject.type === "radio") {
                var elem = that.currentObject.obj;
                return elem.find('input:radio[name="' + elem.data('name') + '"]:checked').val();
            }
            return "";
        } else {
            var type = that.currentObject.type;
            var elem = that.currentObject.obj;
            switch (type) {
                case 'selectTree':
                    var tree = that.currentObject.obj;
                    tree.val(value);
                    break;
            }
        }
    }

    cell.prototype.data = function () {
        var that = this;
        if (!that.currentObject) {
            return [];
        }
        if (that.currentObject.type === "selectTree") {
            var tree = that.currentObject.obj;
            return tree.data();
        } else if (that.currentObject.type === "radio") {
            var value = that.val();
            var data = that.currentObject.data;
            for (var i = 0; i < data.length; i++) {
                if (data[i].id == value) {
                    return data[i];
                }
            }
        }
    };


    var cell = new cell();
    var obj = {
        init: function (option) {
            cell.init(option);
            return this;
        },
        render: function () {
            cell.render();
        },
        get: function (bizName) {
            var currentObj = window.biz[cell.bizName][bizName];
            if (!currentObj) {
                throwError(bizName + "尚未配置data-biz-name属性！")
            }
            cell.currentObject = currentObj;
            return cell;
        }
    };
    //输出cell接口
    exports('cell', obj);
});
