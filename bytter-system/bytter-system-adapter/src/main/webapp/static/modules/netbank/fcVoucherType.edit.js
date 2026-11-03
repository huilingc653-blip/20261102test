bt.use(['form', 'layedit', 'selectType', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();

    /**
     * 系统结算方式回调函数
     * @param data
     */
    payTypeCallback = function (data) {
        if (data.sysvouchercode == "02" || data.sysvouchercode == "33") {
            //转付/代付/拨付，默认走银企直连且记账
            $("input[name='isElectronPay']").removeAttr("disabled");
            $("input[name='isAccount']").removeAttr("disabled");
            dicts.get("isElectronPay").val("1");
            dicts.get("isAccount").val("1");
        } else if (data.sysvouchercode == "04") {
            //现付，默认不走银企直连且记账
            $("input[name='isElectronPay']").removeAttr("disabled");
            $("input[name='isAccount']").removeAttr("disabled");
            dicts.get("isElectronPay").val("0");
            dicts.get("isAccount").val("1");
        } else if (data.sysvouchercode == "34" || data.sysvouchercode == "48") {
            //外付网银/代发工资，默认走银企直连且不记账，不允许修改
            $("input[name='isElectronPay']").attr("disabled", "disabled");
            $("input[name='isAccount']").attr("disabled", "disabled");
            dicts.get("isElectronPay").val("1");
            dicts.get("isAccount").val("0");
        } else if (data.sysvouchercode == "05") {
            //内转，默认不走银企直连且记账，不允许修改
            $("input[name='isElectronPay']").attr("disabled", "disabled");
            $("input[name='isAccount']").attr("disabled", "disabled");
            dicts.get("isElectronPay").val("0");
            dicts.get("isAccount").val("1");
        } else if (data.sysvouchercode == "49" || data.sysvouchercode == "50") {
            //应收/付票据，默认票据，不走银企直连且不记账
            $("input[name='isElectronPay']").attr("disabled", "disabled");
            $("input[name='isAccount']").attr("disabled", "disabled");
            dicts.get("settlType").val("1");
            dicts.get("isElectronPay").val("0");
            dicts.get("isAccount").val("0");
        } else if (data.sysvouchercode == "62" || data.sysvouchercode == "63" || data.sysvouchercode == "06") {
            //现金支票/转账支票/电汇，默认现金，不走银企直连且不记账
            $("input[name='isElectronPay']").attr("disabled", "disabled");
            $("input[name='isAccount']").attr("disabled", "disabled");
            dicts.get("settlType").val("0");
            dicts.get("isElectronPay").val("0");
            dicts.get("isAccount").val("0");
        } else {
            $("input[name='isElectronPay']").removeAttr("disabled");
            $("input[name='isAccount']").removeAttr("disabled");
            dicts.get("settlType").val("0");
        }
        //转付/外付网银/代付，指令发送模式设置
        if (data.sysvouchercode == "02" || data.sysvouchercode == "33" || data.sysvouchercode == "34") {
            $("#sendModeTr").show();//选票显示
        }else{
            $("#sendModeTr").hide();//选票隐藏
            dicts.get("sendMode").val("0");
        }
        //应付票据增加开票区间控制选项
        if(data.sysvouchercode == "49"){
            dicts.get("billSectionControl").setRequired(true);
            $("#billSectionControlTr").show();
            dicts.render();
        }else{
            dicts.get("billSectionControl").setRequired(false);
            dicts.get("billSectionControl").attr({"data-defaultvalue": ""});
            $("#billSectionControlTr").hide();
            dicts.render();
        }
    }
    bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/fcvouchertype/modify";
        if(data.field.sysVoucherCode != "49"){
            data.field.billSectionControl=null;
        }
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

});
