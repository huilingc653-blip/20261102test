bt.use(['form', 'layedit', 'selectType', 'dateType', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    var moneyObj = bt.moneyType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var status = $(this).attr("value");
        data.field.status = status;//提交状态
        var action = "/basic/business/fcexcrateadjust/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 设置原汇率
     * @param value
     */
    function setOldExchangeRateChange(value) {
        var params = {
            typeId: selectType.get("typeId").val(),
            curId: selectType.get("curId").val(),
            referCurId: selectType.get("referCurId").val(),
            preTime: dateType.get("preTime").val()
        };
        moneyObj.get("oldExchangeRateChange").val("");
        $.post(basePath + "/basic/business/fcexcrateadjust/getExchangeRate", params, function (result) {
            if(result){
                moneyObj.get("oldExchangeRateChange").val(result);// liuyj 2019-05-19
            }
        }, 'JSON');
        $.post(basePath +"/basic/business/btratetype/getRateDecimalPlaces", params, function (result) {
            if(result){
                moneyObj.get("oldExchangeRateChange").attr("data-decimal",result);
                moneyObj.get("newExchangeRate").attr("data-decimal",result);
                moneyObj.get("foreignExchangePurchase").attr("data-decimal",result);
                moneyObj.get("foreignExchangeSell").attr("data-decimal",result);
                moneyObj.get("foreignExchangeMiddle").attr("data-decimal",result);
            }
        }, 'JSON');
    }

    bt.typeId = function (value, data) {
        setOldExchangeRateChange(value);
    }

    bt.cur = function (value, data) {
        setOldExchangeRateChange(value);
    }

    bt.referCurId = function (value, data) {
        setOldExchangeRateChange(value);
    }

    bt.preTime = function (value, data) {
        setOldExchangeRateChange(value);
    }

    var selectType = bt.selectType.init();
});
