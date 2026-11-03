bt.use(['form', 'layedit', 'corp', 'dicts', 'dateType', 'moneyType', 'selectType', 'money'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //监听提交
    form.on('submit(submit)', function (data) {
        // 限额控制
        // 日单笔限额
        var daySingle = $("#daySingle").val();
        // 日累计限额
        var dayTotal = $("#dayTotal").val();
        // 月累计限额
        var monthTotal = $("#monthTotal").val();
        if (!daySingle && !dayTotal && !monthTotal) {
            parent.layer.alert(bt.lang("ttpay.message.quotaIsNull"), {icon: 2});//三个限额至少填写一个限额种类才能保存!
            return false;
        }
        if (daySingle && dayTotal && Number(daySingle) > Number(dayTotal)) {
            parent.layer.alert(bt.lang("ttpay.message.daySingle_dayTotal"), {icon: 2});//日单笔限额大于日累计限额，不能保存!
            return false;
        }
        if (daySingle && monthTotal && Number(daySingle) > Number(monthTotal)) {
            parent.layer.alert(bt.lang("ttpay.message.daySingle_monthTotal"), {icon: 2});//日单笔限额大于月累计限额，不能保存!
            return false;
        }
        if (dayTotal && monthTotal && Number(dayTotal) > Number(monthTotal)) {
            parent.layer.alert(bt.lang("ttpay.message.dayTotal_monthTotal"), {icon: 2});//日累计限额大于月累计限额，不能保存!
            return false;
        }

        // 日已用限额
        var dayTotalUse = $("#dayTotalUse").val();
        // 月已用限额
        var monthTotalUse = $("#monthTotalUse").val();

        // 校验可用余额和填写的限额大小
        if (dayTotal && Number(dayTotalUse) >= Number(dayTotal)) {
            if (monthTotal && Number(monthTotalUse) >= Number(monthTotal)) {
                parent.layer.confirm(bt.lang('ttpay.message.quotaNotEnough'), function () {
                    submit(data);
                });
            } else {
                parent.layer.confirm(bt.lang('ttpay.message.quotaNotEnough'), function () {
                    submit(data);
                });
            }
        } else if (monthTotal && Number(monthTotalUse) >= Number(monthTotal)) {
            parent.layer.confirm(bt.lang('ttpay.message.quotaNotEnough'), function () {
                submit(data);
            });
        } else {
            submit(data);
        }
        return false;
    });
});

/*//单位回调函数
bt.corpCallback = function (corpId) {
    merchantValue();
};

//商户类别回调函数
bt.payWayCallback = function () {
    merchantValue();
};

function merchantValue() {
    var corpId = bt.corp.get('corpId').val();
    var payWay = bt.dicts.get("payWay").val();
    if (payWay == " ") {
        payWay = "";
    }
    // 重新设置商户号
    var merchantUrl = basePath + "/ttpay/merchant/getByList?corpId=" + corpId + "&payType=" + payWay
    bt.selectType.get("merchantId").attr("data-url", merchantUrl);
}*/

//日累计限额回调
var dayTotalBack = function (data) {
    // 日已用限额
    var dayTotalUse = $("#dayTotalUse").val();
    var dayTotalAvail = bt.money.sub(data, dayTotalUse)
    if (dayTotalAvail < 0) {
        dayTotalAvail = 0
    }
    $("#dayTotalAvail").html(bt.money.format(dayTotalAvail));
}

//月累计限额回调
var monthTotalBack = function (data) {
    // 月已用限额
    var monthTotalUse = $("#monthTotalUse").val();
    var monthTotalAvail = bt.money.sub(data, monthTotalUse)
    if (monthTotalAvail < 0) {
        monthTotalAvail = 0
    }
    $("#monthTotalAvail").html(bt.money.format(monthTotalAvail));
};

//保存
submit = function (data) {
    var action = "/ttpay/quota/saveOrUpdate";
    $.post(basePath + action, data.field, function (result) {
        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        if (result.success) {
            if (bt.navtab) {
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            } else {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        }
        bt.loading.hide();
    }, 'JSON');
};
