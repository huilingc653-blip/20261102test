/**
 * 业务组件：账户表格
 * 左宇
 * 2017年7月13日
 * 0.0.1 组件初始化
 * 0.0.2 新增电票专户字段 zy 2017-08-01
 * 0.0.3 新增"资源过滤"参数 data-resource    zy 2017-08-02
 * 0.0.4 新增"是否上线"参数    isOnline zy 2017-08-24
 * 0.0.4 新增"默认单位"参数 data-defaultcorp    zy 2017-10-13
 * 0.0.5 更改为selectTable组件，统一格式 liuyj 20180323 (原为bttable)
 * 0.0.6 新增"银行账号忽略授权"参数 ignorePermission    zy 2018-07-22
 * 0.0.7 点击回调方法新增形参
 * 0.0.8 新增"币别"参数 data-curId  zy 2020-02-20
 * 0.0.9 新增"银行类型"参数 data-bankTypeId  zy 2020-02-21
 */
bt.define(['jquery', 'bttable', 'money','selectTableType2'], function (exports) {
    var $ = bt.jquery;
    var AccountTable = function () {
        elem = null;
        this.config = {
            SelectTable: true,
            showCallback:false,// 显示回调
            urldata: {
                accountType: 2,
                permission: "t",
                existChilds: false,
                containParent: false,
                containDelBankAcc: false,
                // electricBill:"0",
                accBal: false,
                searchBal: false,
                fcBal: false,
                isMustCorp:"0",
                url: basePath + "/sys/accountGrant/getListBy"
            },
            // handleDeleteCallback:function () { //点击清除按钮的回调方法
            //
            // }
            columns: [
                {data: "bankAcc",},
                {data: "accName",},
                {data: "bankName",},
                {data: "curName",}
            ]
        };
        this.bizName = 'accountTable';
        this.tableObj;
        this.currentObject = null;
    }

    AccountTable.prototype.set = function (ops) {
        var that = this;
        $.extend(true, that.config, ops);
        return that;
    }

    /**
     * 方法入口，初始化
     */
    AccountTable.prototype.init = function () {
        var _this = this;
        var _config = this.config;
        $('.bt-biz-accountTable:not(.init)').each(function () {
            var that = $(this);
            var config = $.extend(true, {}, _config);//解决同一个页面多个表格调用的问题
            var elem = that.attr('data-name');
            _this.elem = elem;
            //将elem存贮
            config.elem = "#" + elem + "Table";
            config.searchInput = "#" + elem;
            // input是否存在accountType
            var accountType = that.attr('accountType');
            if (typeof accountType != "undefined") {
                config.urldata.accountType = accountType;
            }
            // input是否存在permission
            var permission = that.attr('permission');
            if (typeof permission != "undefined") {
                config.urldata.permission = permission;
            }
            // input是否存在accBal
            var accBal = that.attr('accBal');
            if (typeof accBal != "undefined") {
                config.urldata.accBal = accBal;
            }
            // input是否存在searchBal
            var searchBal = that.attr('searchBal');
            if (typeof searchBal != "undefined") {
                config.urldata.searchBal = searchBal;
            }
            if (config.urldata.accBal) {
                //如果账户余额配置了要显示，则余额也必须查询
                config.urldata.searchBal = config.urldata.accBal;
                config.columns = [
                    {data: "bankAcc",},
                    {data: "accName",},
                    {data: "bankName",},
                    {data: "curName",},
                    {
                        data: "balance", className: "textRight",
                        render: function (data, type, full, meta) {
                            if (null == full.balance) {
                                return "0.00";
                            }
                            return bt.money.format(full.balance);
                        }
                    }
                ]
            }
            // input是否存在existChilds
            var existChilds = that.attr('existChilds');
            if (typeof existChilds != "undefined") {
                config.urldata.existChilds = existChilds;
            }
            // input是否存在containParent
            var containParent = that.attr('containParent');
            if (typeof containParent != "undefined") {
                config.urldata.containParent = containParent;
            }
            // input是否存在containDelBankAcc
            var containDelBankAcc = that.attr('containDelBankAcc');
            if (typeof containDelBankAcc != "undefined") {
                config.urldata.containDelBankAcc = containDelBankAcc;
            }
            // input是否存在electricBill
            var electricBill = that.attr('electricBill');
            if (typeof electricBill != "undefined") {
                config.urldata.electricBill = electricBill;
            }
            // input是否存在data-resource
            var resource = that.attr('data-resource');
            if (typeof resource != "undefined") {
                config.urldata.resource = resource;
            }
            // input是否存在isOnline
            var isOnline = that.attr('isOnline');
            if (typeof isOnline != "undefined") {
                config.urldata.isOnline = isOnline;
            }
            // input是否存在ignorePermission
            var ignorePermission = that.attr('ignorePermission');
            if (typeof ignorePermission != "ignorePermission") {
                config.urldata.ignorePermission = ignorePermission;
            }
            // input是否存在默认值
            var defaultvalue = that.attr('data-defaultvalue');
            if (typeof defaultvalue != "undefined") {
                $("#" + elem).val(defaultvalue);
            }
            // input是否存在data-defaultcorp
            var defaultcorp = that.attr('data-defaultcorp');
            if (typeof defaultcorp != "undefined") {
                config.urldata.corpId = defaultcorp;
            }
            // input是否存在data-curId
            var curId = that.attr('data-curId');
            if (typeof curId != "undefined") {
                config.urldata.curId = curId;
            }
            // input是否存在data-bankTypeId
            var bankTypeId = that.attr('data-bankTypeId');
            if (typeof bankTypeId != "undefined") {
                config.urldata.bankTypeId = bankTypeId;
            }
            // input是否存在url
            var url = that.attr('url');
            if (typeof url != "undefined") {
                config.urldata.url = url;
            }
            // input是否存在isMustCorp
            var isMustCorp = that.attr('isMustCorp');
            if (typeof isMustCorp != "undefined") {
                config.urldata.isMustCorp = isMustCorp;
            }
            //  input是否存在 排除银行类型
            var excludeBankTypeCode = that.attr('excludeBankTypeCode');
            if (typeof excludeBankTypeCode != "undefined") {
                config.urldata.excludeBankTypeCode = excludeBankTypeCode;
            }
            // input是否存在 排除账号类型
            var excludeAccTypeCode = that.attr('excludeAccTypeCode');
            if (typeof excludeAccTypeCode != "undefined") {
                config.urldata.excludeAccTypeCode = excludeAccTypeCode;
            }
            //回调函数
            var callBack = that.attr('data-callback');
            if (!callBack) {
                throwError("尚未配置data-callback属性！")
            }
            callBack = _this.makeCallBack(callBack);
            config.callBack = callBack;

            //回调函数
            var handleDeleteCallback = that.data('handledeletecallback');
            if(handleDeleteCallback){
                handleDeleteCallback = _this.makeCallBack(handleDeleteCallback);
            }
            _this.initHtml(that);
            // 调用bttable组件加载表格内容
            // 更改为selectTable组件，统一格式 liuyj 20180323
            _this.tableObj = bt.selectTableType2.init({
                SelectTable: true,
                elem: config.searchInput,
                tableElem:$(config.elem),
                url: config.urldata.url,
                urldata: config.urldata,
                scrollX: true,
                scrollY:'200px',
                columns: config.columns,
                showCallback:config.showCallback,
                clickCallback: function (data,$tr, input, all) {
                    callBack(data,$tr, input, all);
                },
                handleDeleteCallback:handleDeleteCallback
            });
            // _this.tableObj = bt.bttable.init({
            //     SelectTable: true,
            //     elem: config.elem,
            //     url: config.urldata.url,
            //     urldata: config.urldata,
            //     scrollX: true,
            //     searchInput: config.searchInput,
            //     columns: config.columns,
            //     clickCallback: function (data) {
            //         callBack(data);
            //     }
            // });

            // var bizName = that.data('biz-name');
            // if (bizName !== undefined) {
            //     if (!window.biz) {
            //         window.biz = [];
            //     }
            //     if (!window.biz[_this.bizName]) {
            //         window.biz[_this.bizName] = [];
            //     }
            //     config.tableObj = _this.tableObj;
            //     window.biz[_this.bizName][bizName] = config;
            // }
        });
    }

    // 初始化table表格
    AccountTable.prototype.initHtml = function (that) {
        var html = '<table id="' + that.attr('data-name') + 'Table" class="hide">';
        html += '	<thead>';
        html += '		<tr>';
        html += '			<th>' + bt.lang("table.accountDTO.bankAcc") + '</th>';
        html += '			<th>' + bt.lang("table.accountDTO.accName") + '</th>';
        html += '			<th>' + bt.lang("table.accountDTO.bankName") + '</th>';
        html += '			<th>' + bt.lang("table.accountDTO.curName") + '</th>';
        if (that.attr('accBal')) {
            html += '<th>' + bt.lang("table.accountDTO.balance") + '</th>';
        }
        html += '		</tr>';
        html += '	</thead>';
        html += '</table>';
        that.after(html);
    }

    // 设置单位ID重新渲染
    AccountTable.prototype.setCorp = function (corpId) {
        var that = this;
        if (!that.currentObject) {
            throw new Error("尚未配置data-biz-name属性！");
        }
        var config = that.currentObject;
        config.urldata.corpId = corpId;
        // 清除之前的账户信息
        config.urldata.bankAcc = "";
        // 清空当前下拉表格输入框内容
        $(config.searchInput).val("");
        // 重新渲染
        if (config.tableObj) {
            config.tableObj.reload(config);
        } else {
            that.tableObj.reload(config);
        }
    }

    // 设置单位ID重新初始化
    AccountTable.prototype.initCorp = function (corpId, electricBill,async) {
        var that = this;
        if (!that.currentObject) {
            return "";
        }
        var config = that.currentObject;
        config.urldata.corpId = corpId;
        if (electricBill) {//票据专户
            config.urldata.electricBill = electricBill;
        }
        // 清除之前的账户信息
        config.urldata.bankAcc = "";

        // 清空当前下拉表格输入框内容
        $(config.searchInput).val("");

        // 调用bttable组件加载表格内容
        that.tableObj = bt.selectTableType2.init({
            SelectTable: true,
            elem: config.searchInput,
            tableElem:$(config.elem),
            url: config.urldata.url,
            urldata: config.urldata,
            async:async != undefined ? async:true,
            scrollX: true,
            columns: config.columns,
            clickCallback: function (data) {
                config.callBack(data);
            }
        });
    }

    //重新加载回调方法
    AccountTable.prototype.reload = function (ops) {
        // var that = this;
        // that.set(ops);
        // that.tableObj.reload(that.config);
        var that = this;
        if (!that.currentObject) {
            throw new Error("尚未配置data-biz-name属性！");
        }
        var config = that.currentObject;
        $.extend(true, config, ops);
        // 重新渲染
        if (config.tableObj) {
            config.tableObj.reload(config);
        } else {
            that.tableObj.reload(config);
        }
    }

    AccountTable.prototype.makeCallBack = function (callBack) {
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

    //设置及获取值的方法
    AccountTable.prototype.val = function (value) {
        var that = this;
        var config = that.currentObject;
        if (value !== undefined) {
            $(that.currentObject.searchInput).val(value).data('beforevalue', value);
        } else {
            return $(that.currentObject.searchInput).val();
        }
    }

    function throwError(msg) {
        throw new Error(msg);
    }

    var accTable = new AccountTable();
    var obj = {
        init: function (ops) {
            accTable.set(ops);
            accTable.init();
            return this;
        },
        // setCorp: function (corpId) {
        //     accTable.setCorp(corpId);
        // },
        // reload: function (ops) {
        //     accTable.reload(ops);
        // },
        get: function (bizName) {
            return accTable.tableObj.get(bizName)
        },
    };
    //输出test接口
    exports('accountTable', obj);
});
