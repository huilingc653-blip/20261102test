/**
 * 业务组件：单位网点
 * 周亮
 * 2017年5月22日
 * 0.0.1 基础版本
 * 0.0.2 新增下拉树、单选回调方法
 * 0.0.3 新增必填参数、删除按钮参数
 * 0.0.4 新增加seturl方法
 * 0.0.5 修改val()不传参数时的判断条件
 * 0.0.6 val方法中新增加下拉框获取值
 * 0.0.7 checkbox禁用属性值转数组改到checkbox组件中处理
 * 0.0.8  checkkboxtype 默认为 { "Y": "s", "N": "ps" } liuyj 2018-05-29
 * 0.0.9 下拉树新增加textStyle参数，用于设置树内容的样式
 * 0.0.10 增加data-parentId顶级属性，只显示该网点/单位及一下子单位信息 zuoy 2019-12-12
 */
layui.define(['jquery', 'form', 'selectTree'], function (exports) {
    var $ = layui.jquery, form = layui.form();
    var layer = parent ? parent.bt.layer : bt.layer;

    var url = basePath + "/sys/corp/";
    var subFix = ".json";
    var interestCorp = function () {
        this.bizName = "corp";
        this.currentObject = null;
    }

    interestCorp.prototype.v = '0.0.6';

    /**
     * 方法入口，初始化
     */
    interestCorp.prototype.init = function () {
        this.render();
    }

    interestCorp.prototype.render = function () {
        var _this = this;
        $('.bt-biz-interestCorp:not(.init)').each(function () {
            var that = $(this);
            //表单类型
            var type = that.data('type');
            switch (type) {
                case 'selectTree':
                    _this.initSelectTree(that);
                    //下拉树
                    that.removeAttr('data-scope');
                    that.removeAttr('data-placeholder');
                    that.removeAttr('data-callBack');
                    that.removeAttr('data-callBackData');
                    that.removeAttr('data-tree-type');
                    that.removeAttr('data-delete');
                    that.removeAttr('data-resource');
                    break;

            }
            that.removeAttr('data-name');
            that.removeAttr('data-biz-type');
            that.removeAttr('data-type');
            that.removeAttr('data-required');
        });
    };

    interestCorp.prototype.setWindowValue = function (jThis, obj) {
        var that = this;
        var bizName = jThis.data('biz-name'.toLowerCase());
        var type = jThis.data('type');
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName][bizName] = {type: type, obj: obj};
        }
        jThis.addClass('init');
    }

    interestCorp.prototype.initSelectTree = function (elem) {
        var that = this;
        //输入框名字
        var name = elem.data("name");
        var className = "biz-corp-select-tree-" + name.replace(".", "-");
        //数据范围
        var scope = elem.data('scope');
        //获取数据的url
        var treeUrl = basePath + "/sys/corp/";
        //占位符
        var placeholder = elem.data('placeholder');
        var treeType = elem.data('tree-type');
        //回调函数
        var callBack = elem.data('callback');
        var callBackData = elem.data('callbackdata');
        var afterInit = elem.data('afterInit'.toLowerCase());
        elem.addClass(className);
        //默认值
        var defaultValue = elem.data("defaultvalue");
        var checkkboxtype = elem.data("checkkboxtype");
        var selectDisabled = elem.data("disabled"); //所有值都禁用
        //顶层节点
        var parentId = elem.data("parentid");
        //是否显示下属网点单位
        var underNetCorp = elem.data("undernetcorp");

        var fontCss = elem.data("fontCss".toLowerCase());

        var textStyle = elem.data("textStyle".toLowerCase()); //设置文本内容的样式参数function

        var beforeInit = elem.data("beforeInit".toLowerCase());

        if (!checkkboxtype) {
            // checkkboxtype = { "Y": "", "N": "" };
            checkkboxtype = {"Y": "s", "N": "ps"};// liuyj 2018-05-29 项目经理要求更改默认方式
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
        //新增，data-params属性用于跨页面传入网点Id进行筛选，并且需要资源
        var params = elem.data("params");
        // 2019-03-28  新增加参数data-valueField，用于设置选中赋值属性，默认为id
        var valueField = elem.data("valueField".toLowerCase());
        if (params) {
            if (typeof (params) === 'string') {
                params = (new Function('return ' + params + ';'))();
            }
        }
        //资源过滤
        var resource = elem.data('resource');
        if (resource) {
            treeUrl += 'filter?resource=' + resource;
            if (parentId) {
                treeUrl += '&parentId=' + parentId;
            }
            if (underNetCorp) {
                treeUrl += '&underNetCorp=' + underNetCorp;
            }
            if (scope == "net") {
                // 网点
                treeUrl += "&type=net";
            } else if (scope == "rat") {
                //授信主体单位
                treeUrl += "&type=corp";
            } else if (scope == "corp") {
                // 单位
                treeUrl += "&type=corp";
            }
            if (!treeType) {
                treeType = "radio";
            }
        } else {
            if (scope == "net") {
                treeUrl += "tree?type=net";
            } else if (scope == "rat") {
                treeUrl += "tree?type=rat";
            } else if (scope == "corp") {
                treeUrl += "tree?type=corp";
            } else {
                treeUrl += "tree";
            }
        }

        var lazyLoad = false;
        var param = {};
        if (treeGroup && window.treeGroup && window.treeGroup[treeGroup]) {
            var treeList = window.treeGroup[treeGroup];
            for (var k in treeList) {
                var tl = treeList[k];
                if (tl.config.change === name) {
                    // lazyLoad = true;
                    if (tl.config.selected) {
                        param.id = tl.config.selected
                    }
                    break;
                }
            }
        }

        /**
         *
         * @param topNode 最顶corp
         * @param container 数组容器
         * @returns
         */
        var treeCorpListByTop = function (topNode, container) {
            if (!container) {
                container = [];
            }
            container.push(topNode);
            if (topNode.children) {
                for (i in topNode.children) {
                    treeCorpListByTop(topNode.children[i], container);
                }
            }
            //去掉children属性
            delete topNode.children;
            return container;
        }

        var treeDataInit = function (nodes, flag, resource) {
            for (var k in nodes) {
                var node = nodes[k];
                //当选择类型为网点时，过滤单位
                if (flag && node.type === 1) {
                    continue;
                }
                node.corpName = node.name;
                node.name = '(' + node.code + ') ' + node.name;
                if (node.children && node.children.length > 0) {
                    node.children = treeDataInit(node.children, flag);
                }
            }
            return nodes
        };

        var tree = bt.selectTree.initGetCurrent({
            url: treeUrl,
            elem: "." + className,
            name: name,
            checkType: treeType,
            valueField: valueField ? valueField : 'id',
            setting: {check: {chkboxType: checkkboxtype}},
            placeholder: placeholder,
            clickCallBack: callBack,
            clickCallBackData: function (treeNode) {
                if (treeGroup && tree.config.change) {
                    var changeTreeObj = window.treeGroup[treeGroup][tree.config.change];
                    var data = {
                        netId: treeNode.id,
                        underNetCorp: underNetCorp,
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
            param: params,
            disabled: selectDisabled,
            lazyLoad: lazyLoad,
            beforeClick: function (treeId, treeNode, clickFlag) {
            },
            beforeInit: function (nodes) {
                var flag = scope === "net";
                var tempNodes = treeDataInit(nodes, flag, resource);
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
                    // if (parentGroup && !parentGroup.val()) {
                    //     layer.alert(tips ? tips : bt.lang("common.msg.selectParentNode"), {icon: 2});//"请选择父级"
                    //     return false;
                    // }
                }
                return true;
            },
            //输入框点击后的事件
            inputClick: function () {

            },
            clearCallback: function () {
                if (treeGroup) {
                    var treeGroups = window.treeGroup[treeGroup];
                    for (var k in treeGroups) {
                        var group = treeGroups[k];
                        if (group.config.change) {
                            var changeTree = window.treeGroup[treeGroup][group.config.change];
                            changeTree.clearValue(changeTree);
                            var data = {
                                netId: '',
                                underNetCorp: underNetCorp,
                            }
                            changeTree.setData(data);
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
            window.biz[that.bizName][bizName] = {type: elem.data('type'), obj: tree, resource: resource};
        }
    };

    interestCorp.prototype.makeCallBack = function (callBack) {
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

    interestCorp.prototype.val = function (value) {
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
            } else if (that.currentObject.type === "checkbox") {
                var elem = that.currentObject.obj;
                var str = "";
                elem.find('input[name="' + elem.data('name') + '"]:checked').each(function () {
                    str += $(this).val() + ",";
                })
                str = str.substring(0, str.length - 1);
                return str;
            } else if (that.currentObject.type === "select") {
                var elem = that.currentObject.obj;
                return elem.find("select option:selected").val();
            }
            return "";
        } else {
            var type = that.currentObject.type;
            var elem = that.currentObject.obj;
            switch (type) {
                case 'checkbox':
                    elem.find('input[value="' + value + '"]').prop('checked', true);
                    form.render('checkbox');
                    break;
                case 'select':
                    elem.find('select').val(value);
                    form.render('select')
                    break;
                case 'radio':
                    elem.find('input[value="' + value + '"]').prop('checked', true);
                    form.render('radio');
                    break;
                case 'selectTree':
                    var tree = that.currentObject.obj;
                    tree.val(value);
                    break;
            }
        }
    }

    interestCorp.prototype.data = function () {
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
        } else if (that.currentObject.type === "checkbox") {
            var value = that.val();
            var valueArr = value.split(',');
            var elem = that.currentObject.obj;
            var data = that.currentObject.data;
            var arr = [];
            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < valueArr.length; j++) {
                    if (data[i].id == valueArr[j]) {
                        arr.push(data[i]);
                    }
                }

            }
            return arr;
        }
    }
    //下拉树新增加setUrl方法,重置url liuyj
    interestCorp.prototype.setUrl = function (url) {
        var that = this;
        if (!that.currentObject) {
            return [];
        }
        if (that.currentObject.type === "selectTree") {
            var treeUrl = basePath + "/sys/corp/";
            var tree = that.currentObject.obj;
            var resource = that.currentObject.resource;//资源
            if (resource) {
                treeUrl += 'filter?resource=' + resource;
                if (url == "net") {
                    // 网点
                    treeUrl += "&type=net";
                } else if (url == "rat") {
                    //授信主体单位
                    treeUrl += "&type=rat";
                } else if (url == "corp") {
                    // 单位
                    treeUrl += "&type=corp";
                }
            } else {
                if (url == "net") {
                    treeUrl += "tree?type=net";
                } else if (url == "rat") {
                    treeUrl += "tree?type=rat";
                } else if (url == "corp") {
                    treeUrl += "tree?type=corp";
                } else {
                    treeUrl += "tree";
                }
            }
            tree.setUrl(treeUrl);
            that.render();
        }
    }

    //设置资金池单位组件 zuoy 2019-5-16
    interestCorp.prototype.setUrlPool = function () {
        var that = this;
        if (!that.currentObject) {
            return [];
        }
        var treeUrl = basePath + "/sys/corp/poolTree/";
        var tree = that.currentObject.obj;
        tree.setUrl(treeUrl);
        that.render();
    }
    /**
     * 设置必填方法
     * isRequired　true为必填　false 设消必填
     *
     * */
    interestCorp.prototype.setRequired = function (isRequired) {
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var type = that.currentObject.type;
        var currentObject = that.currentObject.obj;
        function updateRequired(elem) {
            isRequired?elem.attr('lay-verify','required'):elem.removeAttr('lay-verify');
        }
        switch (type){
            case 'checkbox':
                updateRequired(currentObject.find('input[type="checkbox"]'))
                break;
            case 'select':
                updateRequired(currentObject.find('select'))
                break;
            case 'radio':
                updateRequired(currentObject.find('input[type="radio"]'))
                break;
            case 'mulselect':
                updateRequired(currentObject.find('select'));
                break;
            case 'selectTree':
                var treeInput = currentObject.param.treeInput;
                updateRequired($('#'+treeInput));
                break;
        }
        form.renderIcon();
    }
    function throwError(msg) {
        throw new Error(msg);
    };

    var corp = new interestCorp();
    var obj = {
        init: function (option) {
            corp.init(option);
            return this;
        },
        render: function () {
            corp.render();
        },
        get: function (bizName) {
            var currentObj = window.biz[corp.bizName][bizName];
            if (!currentObj) {
                throwError(bizName + "尚未配置data-biz-name属性！")
            }
            corp.currentObject = currentObj;
            return corp;
        }
    };
    //输出test接口
    exports('interestCorp', obj);
});

