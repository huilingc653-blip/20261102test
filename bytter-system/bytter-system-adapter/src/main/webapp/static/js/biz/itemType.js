/**
 * 业务组件：预算科目
 * 2017年7月17日
 * zhangxb
 * 0.0.1 参考corp.js
 * 0.0.2 参考corp，优化设置val()的方法  zuoy 2017-10-13
 */
layui.define(['jquery', 'form', 'selectTree'], function (exports) {
    var $ = layui.jquery, form = layui.form();

    var url = basePath + "/budget/fbscorpitem/";
    var subFix = ".json";
    var Item = function () {
        this.bizName = "itemType";
        this.currentObject = null;
    }

    Item.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    Item.prototype.init = function () {
        this.render();
    }

    Item.prototype.render = function ($parentContainer) {
        var _this = this;
        var itemTypes = null
        if($parentContainer){
            itemTypes = $parentContainer.find('.bt-biz-itemType:not(.init)')
        }else{
            itemTypes = $('.bt-biz-itemType:not(.init)')
        }
        itemTypes.each(function () {
            var that = $(this);
            //表单类型
            var type = that.data('type');
            switch (type) {
                case 'checkbox':
                    _this.initCheckBox(that);
                    _this.setWindowValue(that);
                    break;
                case 'select':
                    _this.initSelect(that);
                    _this.setWindowValue(that);
                    break;
                case 'radio':
                    _this.initRadio(that);
                    _this.setWindowValue(that);
                    break;
                case 'selectTree':
                    _this.initSelectTree(that);
                    //下拉树
                    that.removeAttr('data-scope');
                    that.removeAttr('data-placeholder');
                    that.removeAttr('data-callBack');
                    that.removeAttr('data-callBackData');
                    that.removeAttr('data-initComplete');
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
    }

    Item.prototype.setWindowValue = function (jThis, obj) {
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

    Item.prototype.initSelectTree = function (elem) {
        var that = this;
        //输入框名字
        var name = elem.data("name");
        var className = "biz-item-select-tree-" + name.replace(".", "-");
        //数据范围
        var scope = elem.data('scope');
        //获取数据的url
        var treeUrl = basePath + "/budget/fbscorpitem/";
        //占位符
        var placeholder = elem.data('placeholder');
        var treeType = elem.data('tree-type');
        //回调函数
        var callBack = elem.data('callback');
        var callBackData = elem.data('callbackdata');
        var initComplete = elem.data('initcomplete');
        elem.addClass(className);
        //默认值
        var defaultValue = elem.data("defaultvalue");
        //单位Id
        var corpId = elem.data("corpid");

        //科目代码
        var itemCode = elem.data("itemcode");

        // 挂接日期
        var processDate = elem.data("processdate");

        //流向
        var sign = elem.data("sign");

        var checkkboxtype = elem.data("checkkboxtype");
        if (!checkkboxtype) {
            checkkboxtype = {"Y": "", "N": ""};
        }
        callBack = this.makeCallBack(callBack);
        callBackData = this.makeCallBack(callBackData);
        initComplete = this.makeCallBack(initComplete);
        var deleteFlag = elem.data('deleteFlag') ? true : false;
        var required = elem.data('required');
        //资源过滤
        var resource = elem.data('resource');
        if (resource) {
            treeUrl += 'filter?resource=' + resource;
            if (!treeType) {
                treeType = "radio";
            }
        } else {
            if (scope == "ALL") {
                treeUrl += "tree?corpId=all";
                if(sign != ""){
                    treeUrl += "tree?corpId=all&sign=" + sign;
                }
            } else if (corpId && corpId != "") {
                if (processDate && processDate != "") {
                    treeUrl += "tree?corpId=" + corpId + "&itemCode=" + itemCode + "&processDate=" + processDate;
                } else if (itemCode && itemCode != "") {
                    treeUrl += "tree?corpId=" + corpId + "&itemCode=" + itemCode;
                }else if(sign != ""){
                    treeUrl += "tree?corpId=" + corpId + "&sign=" + sign;
                } else {
                    treeUrl += "tree?corpId=" + corpId;
                }
            }
        }

        var exchangeCallBack = function (data) {
            if (elem.data("biz-name")) {
                callBackData(data, elem.data("biz-name"));
            } else {
                callBackData(data, elem.data("name"));
            }
        };

        var tree = bt.selectTree.initGetCurrent({
            url: treeUrl,
            elem: "." + className,
            name: name,
            checkType: treeType,
            setting: {
                check: {
                    chkboxType: checkkboxtype
                }
            },
            placeholder: placeholder,
            clickCallBack: callBack,
            clickCallBackData: exchangeCallBack,
            selected: defaultValue,
            deleteFlag: deleteFlag,
            required: required,
            initComplete: initComplete,
            beforeClick: function (treeId, treeNode, clickFlag) {
            },
            beforeInit: function (nodes) {
                for (k in nodes) {
                    var node = nodes[k];
                    if (!node.canSelect) {
                        nodes[k].chkDisabled = true;
                    }
                    nodes[k].itemName = node.name;
                    nodes[k].name = '(' + node.code + ') ' + node.name;
                }

                return nodes;
            }
        });
        var jThis = elem;
        var bizName = jThis.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName][bizName] = {type: jThis.data('type'), obj: tree};
            // console.log(window.biz[that.bizName][bizName]);
        }
    }

    Item.prototype.initSelect = function (elem) {
        var name = elem.data("name");
        var bizType = elem.data("biz-type");
        var defaultValue = elem.data("defaultValue".toLowerCase());
        var required = elem.data('required');

        //回调函数
        var callBack = elem.data('callback');

        callBack = this.makeCallBack(callBack);

        bt.use(['select'], function () {
            bt.select.init({
                elem: elem,
                valueField: "id",
                textField: "name",
                defaultVal: defaultValue,
                name: name,
                change: callBack,
                url: url + bizType + subFix,
                required: required,

            });
        })

    }

    Item.prototype.initCheckBox = function (elem) {
        var name = elem.data("name");
        var bizType = elem.data("biz-type");
        var required = elem.data('required');
        //回调函数
        var callBack = elem.data('callback');
        callBack = this.makeCallBack(callBack);
        bt.use(['checkboxs'], function () {
            bt.checkboxs.init({
                    elem: elem,
                    url: url + bizType + subFix + "?",
                    name: name,
                    valueField: "id",
                    textField: "name",
                    checkAll: true,
                    change: callBack,
                    required: required
                }
            );
        });
    }
    Item.prototype.initRadio = function (elem) {
        var that = this;
        var name = elem.data("name");
        var bizType = elem.data("biz-type");
        //回调函数
        var callBack = elem.data('callback');

        callBack = this.makeCallBack(callBack);

        var required = elem.data('required') ? 'lay-varify="required"' : '';

        $.get(url + bizType + subFix, function (data) {
            var html = '';
            var filter = callBack ? 'lay-filter="' + name + '"' : '';
            for (var i = 0; i < data.length; i++) {
                html += '<input type="radio" name="' + name + '" value="' + data[i].id + '"  ' + filter + ' ' + required + ' title="' + data[i].name + '">';
            }
            elem.html(html);

            if (callBack) {
                form.on('radio(' + name + ')', function (data) {
                    callBack(data);
                })
            }
            form.render('radio');

            var jThis = elem;
            var bizName = jThis.data('biz-name'.toLowerCase());
            if (bizName) {
                if (!window.biz) {
                    window.biz = [];
                }
                if (!window.biz[that.bizName]) {
                    window.biz[that.bizName] = [];
                }
                window.biz[that.bizName][bizName] = {type: jThis.data('type'), obj: jThis, data: data};
            }
        }, "JSON");


    }

    Item.prototype.makeCallBack = function (callBack) {
        //校验下拉树的回调方法是否存在
        if (callBack && typeof(callBack) != "function") {
            try {
                callBack = eval(callBack);
            } catch (e) {
                alert(callBack + 'not exist！');
            }

        }
        return callBack;
    }

    Item.prototype.val = function (value) {
        var that = this;
        if (!value) {
            if (!that.currentObject) {
                return "";
            }

            if (that.currentObject.type === "selectTree") {
                var tree = that.currentObject.obj;
                return tree.val(value);
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

    Item.prototype.data = function () {
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
            var valueArr = String(value).split(',');
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

    //重新加载回调方法
    Item.prototype.reload = function (ops) {
        var that = this;
        if (!that.currentObject) {
            return [];
        }
        if (that.currentObject.type === "selectTree") {
            var tree = that.currentObject.obj;
            // tree.setUrl({url:ops.url});
            tree.setUrl(ops.url);
            // console.log(that.currentObject.obj);
            // var sos= bt.selectTree.initGetCurrent(tree.config);
            // var bizName =tree.config.name;
            // if (bizName) {
            //     if (!window.biz) {
            //         window.biz = [];
            //     }
            //     if (!window.biz[that.bizName]) {
            //         window.biz[that.bizName] = [];
            //     }
            //     window.biz[that.bizName][bizName] = {type:tree.type,obj:sos};
            //     // console.log(window.biz[that.bizName][bizName]);
            // }
        }

    }
    //重新设置url参数(根据参数重新获取计划科目列表)
    Item.prototype.setUrl = function (object) {
        var that = this;
        if (!that.currentObject) {
            return [];
        }
        if (that.currentObject.type === "selectTree") {
            var tree = that.currentObject.obj;
            var treeUrl = basePath + "/budget/fbscorpitem/";
            if (object.itemCode && object.itemCode != "") {
                treeUrl += "tree?corpId=" + object.corpId + "&itemCode=" + object.itemCode + "&sign=" + object.sign + "&processDate=" + object.processDate;
            } else {
                treeUrl += "tree?corpId=" + object.corpId + "&sign=" + object.sign + "&processDate=" + object.processDate;
            }
            tree.setUrl(treeUrl)
        }
    }

    var item = new Item();
    var obj = {
        init: function (option) {
            item.init(option);
            return this;
        },
        render: function ($parentContainer) {
            item.render($parentContainer);
        },
        reload: function (option) {
            item.reload(option);
            return this;
        },
        get: function (bizName) {

            var currentObj = window.biz[item.bizName][bizName];
            if (!currentObj) {
                throwError(bizName + "尚未配置data-biz-name属性！")
            }
            item.currentObject = currentObj;
            return item;
        }
    };
    //输出test接口
    exports('itemType', obj);
});