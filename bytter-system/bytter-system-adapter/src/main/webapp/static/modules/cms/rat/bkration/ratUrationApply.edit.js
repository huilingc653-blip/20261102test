var rowMR = 0;
var selectTypes;
var moneyType;
var rowArray = {};
var ratioType;
var fun;
var mulselects;
var corpId;
var $;
var form,dicts;
bt.use(['form', 'layedit', 'btdate', 'bttable', 'moneynum', 'dicts', 'select', 'moneyType', 'dateType', 'corp', 'selectType', 'money', 'ratioType', 'mulselect', 'btUpload'], function () {
    $ = bt.jquery;
    form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    var ratCode = $("#ratCode").val();
    var disCode = $("#disCode").val();
    corpId = $("#corpId").val();
    fun = bt.moneyType.getFun();
    ratioType = bt.ratioType.init();
    mulselects = bt.mulselect.init();
    var isHangGuarantee = $("#isHangGuarantee").val();
    // if(isHangGuarantee=="1"){//授信挂担保，显示银行授信录入的担保方式，不能修改
    //     mulselects.get("guaWay").attr({'data-disabled': true});//设置下拉框禁用
    //     mulselects.get("guaWay").attr({'data-itemsdisabled': $("#allGuaWayCode").val()});//设置不可选
    // }

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //设置保证金比例利率
    bt.setAssure = function (rowIndex, data) {
        moneyType.get(rowIndex + "assurepercent").val(data.assurepercent);
        moneyType.get(rowIndex + "interestrate").val(data.interestrate);
    }

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    // 业务币种change事件，设置保证金币别
    bt.initCurIdCallback = function (data) {
        var ratCurCode = $("#curCode").val();// 授信币种
        if (bt.empty(data.id)) {
            $("#exchangeMoneyRate").val(1);
            return false;
        }

        var html = '<div class="layui-inline"><div class="bt-biz-selectType" data-biz-name="bondCurId"' +
            ' data-url="' + basePath + '/cms/rat/raturationapply/getBondCurList?billCurId=' + data.id + '&corpId=' + corpId + '"\n' +
            ' data-valuefield="id" data-textfield="curName" data-required="true" data-defaultvalue="' + data.id + '" data-callback="bondCurIdCallback" ' +
            ' data-name="bondCurId" data-search="true"></div></div>'
        $("#bondCurIdSpan").empty();
        $("#bondCurIdSpan").html(html);
        bt.selectType.init();
        // 保证金汇率赋值为1
        $("#exchangeBondMoneyRate").val(1);
        var billMoney = moneyType.get("billMoney").val();//业务金额
        if (!bt.empty(billMoney)) {
            $("#exchangeBondMoney").val(billMoney);
        }
        // // 清空保证金比例和金额
        // ratioType.get("assurepercent").val(0)//保证金金额
        // moneyType.get("bondMoney").val(0)//保证金金额

        if (data.id !== ratCurCode) {//业务币别与授信币别不一致，需要查询对应的折算汇率
            $.post(basePath + "/cms/rat/raturationapply/getNearCurRat", {
                curId: data.id,//业务币别
                convertCurId: ratCurCode,//授信币别
                date: $("#beginDate").val()//授信开始时间
            }, function (retData) {
                $("#exchangeMoneyRate").val(retData);
                var exChangeMoney = 0;
                if (!bt.empty(billMoney)) {
                    exChangeMoney = fun.mul(retData, billMoney);//币别*汇率
                }
                $("#exchangeMoney").val(exChangeMoney);
                // 占用授信金额
                moneyType.get("money").val(fun.format(exChangeMoney, {
                    separator: "",
                    decimal: 2
                }));

                var itemFlag = $("#itemFlag").val();
                if (itemFlag === "1") {//综合品种
                    moneyType.get("maxMoney").val(exChangeMoney);
                }
            }, 'JSON');
        } else {
            $("#exchangeMoneyRate").val(1);
            var exChangeMoney = 0;
            if (!bt.empty(billMoney)) {
                exChangeMoney = billMoney;//币别*汇率
            }
            $("#exchangeMoney").val(exChangeMoney);
            // 占用授信金额
            moneyType.get("money").val(fun.format(exChangeMoney, {
                separator: "",
                decimal: 2
            }));

            var itemFlag = $("#itemFlag").val();
            if (itemFlag === "1") {//综合品种
                moneyType.get("maxMoney").val(exChangeMoney);
            }
        }

        //业务币别发生变化时重新设置保证金金额
        var bondMoney = moneyType.get("bondMoney").val();//保证金金额
        if ((!bt.empty(billMoney)) && (!bt.empty(bondMoney))) {
            setBondMoney();
        }
    }

    bt.setDays = function (data, el) {
        var rowIndex = "";
        var beginDate = bt.dateType.init().get(rowIndex + "beginDate").val();
        if (bt.empty(beginDate)) {
            return;
        }
        var endDate = bt.dateType.init().get(rowIndex + "endDate").val();
        if (bt.empty(endDate)) {
            return;
        }
        var days = getBetweenDay(beginDate, endDate) - (-1);
        $("#" + rowIndex + "days").val(days);
    }

    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    bt.setDays();

    function checkBefore() {

        return true;
    }


    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();//显示加载中
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }
        data.field.attachmentId = up.getGroupId();//设置附件ID
        var action = "/cms/rat/raturationapply/editSave";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    var webObj = bt.navtab.getParentDom();
                    webObj.search();
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
        return false;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();//显示加载中
        if (!checkBefore()) {
            bt.loading.hide(); //关闭加载中
            return false;
        }
        data.field.attachmentId = up.getGroupId();//设置附件ID
        var action = "/cms/rat/raturationapply/editSubmit";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    var webObj = bt.navtab.getParentDom();
                    webObj.search();
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
        return false;
    });

    bt.close = function () {
        if (!bt.navtab) {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        } else {
            //bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
    }
});

/**
 * 得到两个日期之间的天数
 * @param startDate
 * @param endDate
 */
function getBetweenDay(startDate, endDate) {
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);
    var btt = date2 - date1;
    return btt / (1000 * 60 * 60 * 24);
}

closeTab = function () {
    bt.close();
}

//json转url参数
var parseParam = function (param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function (i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};


/**根据保证金金额设置保证金比例*/
bt.setBailMoneyRate = function (data, elem) {
    var billCurId = selectTypes.get("initCurCode").val();//业务币种
    var bondCurId = selectTypes.get("bondCurId").val();//保证金币种
    var isOpenRat = dicts.get("isOpenRat").val(); // 是否启用银行授信敞口
    var billMoney;
    if (billCurId === bondCurId) {
        billMoney = moneyType.get("billMoney").val();//业务金额
    } else {
        billMoney = $("#exchangeBondMoney").val();//业务金额(进行过汇率转换的)
    }

    var bondMoney = data;
    if (bt.empty(billMoney) || bt.empty(bondMoney)) {
        return false;
    }

    if (bt.money.sub(bondMoney, billMoney) > 0) {//判断保证金金额是否大于业务金额
        if (isOpenRat === "1"){
            moneyType.get("money").val(0);
        }
        parent.layer.msg(bt.lang("cms.rat.apply.checkBondMoney"), {icon: 2});
        moneyType.get("bondMoney").val(billMoney);
        ratioType.get("assurepercent").val(fun.format(1));
        return false;
    }

    if (!bt.empty(bondMoney) && bondMoney !== 0) {
        ratioType.get("assurepercent").val(fun.div(bondMoney, billMoney, 8));
        if (isOpenRat === "1"){
            moneyType.get("money").val(fun.format(fun.sub(billMoney,bondMoney), {
                separator: "",
                decimal: 2
            }));
        }
    }
}

/**根据业务金额和保证金比例设置保证金金额*/
bt.setBailMoney = function (data) {
    setBondMoney();
}

// 业务金额change事件，初始化业务金额的转换金额
bt.billMoneyCallback = function (data) {
    var exChangeMoneyRate = $("#exchangeMoneyRate").val();
    var exChangeBondMoneyRate = $("#exchangeBondMoneyRate").val();
    var exChangeMoney = fun.mul(exChangeMoneyRate, data);//币别*汇率
    var exChangeBondMoney = fun.mul(exChangeBondMoneyRate, data);//币别*汇率
    var isOpenRat = dicts.get("isOpenRat").val(); // 是否启用银行授信敞口

    $("#exchangeMoney").val(exChangeMoney);
    $("#exchangeBondMoney").val(exChangeBondMoney);
    var bondPercent = $("#assurepercent").val();//保证金比例
    if (!bt.empty(bondPercent)) {
        // setBondMoney();//重新设置保证金金额
        var billCurId = selectTypes.get("initCurCode").val();//业务币种
        var bondCurId = selectTypes.get("bondCurId").val();//保证金币种
        var billMoney;
        if (billCurId === bondCurId) {
            billMoney = moneyType.get("billMoney").val();//业务金额
        } else {
            billMoney = $("#exchangeBondMoney").val();//业务金额(进行过汇率转换的)
        }

        if (bt.empty(billMoney)) {
            return;
        }
        if (bondPercent !== 0) {//设置保证金金额
            // 占用金额，减去保证金金额
            if (isOpenRat === "1"){
                exChangeMoney = fun.sub(exChangeMoney, fun.mul(billMoney, bondPercent));
            }
            moneyType.get("bondMoney").val(fun.format(fun.mul(billMoney, bondPercent), {separator: "", decimal: 2}));

        } else {
            moneyType.get("bondMoney").val('');
        }
    }

    // 占用授信金额
    moneyType.get("money").val(fun.format(exChangeMoney, {
        separator: "",
        decimal: 2
    }));

    var itemFlag = $("#itemFlag").val();
    if (itemFlag === "1") {//综合品种
        moneyType.get("maxMoney").val(exChangeMoney);
    }
}

//调整是否启用敞口授信
bt.changeOpenRat = function () {
    setBondMoney();
}

// 保证金币种change事件，根据业务币别和业务金额设置转换金额
function bondCurIdCallback() {
    var bondCurId = selectTypes.get("bondCurId").val();
    if (bt.empty(bondCurId)) {
        $("#exchangeBondMoneyRate").val(1);
        return false;
    }
    var billCurId = selectTypes.get("initCurCode").val();
    var billMoney = moneyType.get("billMoney").val();//业务金额
    var bondMoney = moneyType.get("bondMoney").val();//保证金金额

    if (!bt.empty(billCurId) && bondCurId !== billCurId) {//只有当业务币别和保证金币别不一致时才需要进行金额转换
        $.post(basePath + "/cms/rat/raturationapply/getNearCurRat", {
            curId: billCurId,//业务币别
            convertCurId: bondCurId,//保证金币别
            date: new Date().format('yyyy-MM-dd')//当天
        }, function (retData) {
            $("#exchangeBondMoneyRate").val(retData);
            var exChangeBondMoney = 0;
            if (!bt.empty(billMoney)) {
                exChangeBondMoney = fun.mul(retData, billMoney);//币别*汇率
            }
            $("#exchangeBondMoney").val(exChangeBondMoney);
            if ((!bt.empty(billMoney)) && (!bt.empty(bondMoney))) {
                setBondMoney();
            }
        }, 'JSON');
    } else {
        $("#exchangeBondMoneyRate").val(1);
        var exChangeBondMoney = 0;
        if (!bt.empty(billMoney)) {
            exChangeBondMoney = fun.mul(1, billMoney);//币别*汇率
        }
        $("#exchangeBondMoney").val(exChangeBondMoney);
        if ((!bt.empty(billMoney)) && (!bt.empty(bondMoney))) {
            setBondMoney();
        }
    }
}

//设置保证金金额
function setBondMoney() {
    var billCurId = selectTypes.get("initCurCode").val();//业务币种
    var bondCurId = selectTypes.get("bondCurId").val();//保证金币种
    var bondPercent = $("#assurepercent").val();//保证金比例
    var isOpenRat = dicts.get("isOpenRat").val(); // 是否启用银行授信敞口
    var billMoney;
    if (billCurId === bondCurId) {
        billMoney = moneyType.get("billMoney").val();//业务金额
    } else {
        billMoney = $("#exchangeBondMoney").val();//业务金额(进行过汇率转换的)
    }

    if (bt.empty(billMoney)) {
        return;
    }
    if (!bt.empty(bondPercent) && bondPercent !== 0) {//设置保证金金额
        moneyType.get("bondMoney").val(fun.format(fun.mul(billMoney, bondPercent), {separator: "", decimal: 2}));
        if (isOpenRat === "1"){
            moneyType.get("money").val(fun.format(fun.sub(billMoney, fun.mul(billMoney, bondPercent)), {
                separator: "",
                decimal: 2
            }));
        }else{
            moneyType.get("money").val(fun.format(billMoney, {
                separator: "",
                decimal: 2
            }));
        }

    } else {
        moneyType.get("bondMoney").val('');
    }
}




