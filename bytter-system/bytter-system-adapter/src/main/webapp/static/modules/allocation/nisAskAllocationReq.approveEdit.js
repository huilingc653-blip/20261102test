bt.use(['form', 'layedit', 'corp', 'selectType', 'dateType', 'moneyType', 'accountTable', 'dicts', 'btUpload','accountTable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete:false,
        hasUpload:false,
        gid: $("#attachmentId").val()
    });

    bt.dateType.init();
    accountCallBack = function (data){
        $("#payerAccId").val(data.bankAccId);
        $("#payerAcc").val(data.bankAcc);
        $("#payerAccName").val(data.accName);
        $("#payerBankName").val(data.bankName);
        $("#curId").val(data.curId);
        $("#curName").val(data.curName);
        $("#payerBankCode").val(data.bankCode);
        $("#payerBankTypeId").val(data.bankTypeId);
        initProvAndCity(data.bankAccId, data.curId, "payeeProvCity");
        //资金计划
        renderBudget();
        initBank();
        $.ajax({
            url: basePath + "/bankacc/bisaccbal?getAllBal",
            method: 'POST',
            data: {bankAcc: data.bankAcc,curId: data.curId},
            dataType: 'json',
            success: function (data) {
                if(data) {
                    $("#balDiv").text(moneyFun.format(data.bal));
                    $("#availbalDiv").text(moneyFun.format(data.availBal));
                }else{
                    $("#balDiv").text(moneyFun.format(0));
                    $("#availbalDiv").text(moneyFun.format(0));
                }
                form.render();
            }
        });
    }
    var payerCorpId = $("#payerCorpId").val();
    bt.accountTable.init({
        urldata: {
            accountType: "2,3",
            permission: "t",
            existChilds: false,
            containParent: true,
            containDelBankAcc: false,
            accBal: false,
            searchBal: false,
            fcBal: false,
            isMustCorp:"0",
            corpId: payerCorpId,
            url: basePath + "/bankacc/btbankacc/getListBy"
        },
    });


    /**
     * 加载账户信息
     */
    initAccInfo = function () {
        $.ajax({
            url: basePath + "/bankacc/btbankacc?getBankAccInfoById",
            method: 'POST',
            data: {id: $("#payeeAccId").val(), curId: $("#curId").val()},
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    var data = result.obj;
                    $("#payeeBankName").val(data.bankName);
                    // 若存在父类账户信息，初始化收款人账户信息
                    // if ("undefined" != data.parentBankAcc && null != data.parentBankAcc) {
                    //     $("#payerCorpId").val(data.parentBankAcc.corpName);
                    //     $("#payerBankName").val(data.parentBankAcc.bankName);
                    //     $("#balance").val(data.parentBankAcc.bankAccBalance);
                    //     initFcAccBalance(data.parentBankAcc.bankAcc, data.parentBankAcc.curId, "fcAccBalance");
                    // }
                }
            }
        });
    };
    initAccInfo();


    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId, flag) {
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#askItem").val() ? $("#askItem").val() : "200",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: bt.dateType.get("wishPayday").val(),
            type: 'select',
            date: (new Date()).getTime()
        };
        if (flag) {
            param.billNo = "";
        }
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }

    renderBudget($("#corpIdDef").val());

    onSave = function () {
        bt.loading.show();
        var action = "/allocation/nisallocationreq/approveUpdate";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
    };

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        var budgetSel = $("#budgetSel").val();
        // 是否挂接资金计划
        if ($("input[name='sumMoney']").length) {
            var sumMoney = bt.moneyType.get("sumMoney").val();
            // 校验金额与资金计划总金额一致
            if (sumMoney != data.field.money) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        var bankaccFlag = checkBankAccBal($("#balance").val(), data.field.money, "money");
        if (!bankaccFlag && $("#bankaccOver").val() == 2) {
            bt.loading.hide();
            return false;
        }
        var accountFlag = true;
        if ($("#isAccount").val() == 1) {
            accountFlag = checkFcAccBal($("#fcAccBalance").val(), data.field.money, "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }
        }
        //证书签名
        var signDataMsg = signCaData(data);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        if (bankaccFlag && accountFlag) {
            onSave();
        } else if (!bankaccFlag && accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'), function (index) {
                onSave();
            });
        } else if (bankaccFlag && !accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'), function (index) {
                onSave();
            });
        } else {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'), function (index) {
                onSave();
            });
        }
        return false;
    });

    //证书签名
    function signCaData(data) {
        //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
        var sourceData = $('#payerAcc').val();//付款账号
        var random = String(parseInt(Math.random() * 10000000000));//随机数
        while (random.length < 10) {
            random = random + '0';
        }
        $("#random").val(random);
        var payMoney = moneyFun.format(moneyFun.toNumber(moneyType.get("money").val()), {separator: ''});//金额
        while (payMoney.length < 14) {
            payMoney = '0' + payMoney;
        }
        var wishPayday = dateFun.format(dateType.get("wishPayday").val(), 'yyyyMMdd');//期望支付日期
        sourceData += random + payMoney + wishPayday + $('#payeeAccName').val() + $('#payeeAcc').val();
        var msg = signData(sourceData, "3");
        data.field.random = random;
        data.field.encrySource = sourceData;
        data.field.caCoding = $("#caCoding").val();
        return msg;
    }

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/netbank/nismessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();
});

/**
 * 资金计划挂接
 */
function renderBudget(corpId, flag) {
    //判断支付结算是否挂接资金计划
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        // $('input[data-name="money"]').attr("readonly", "readonly");
        $genOtherHtml(corpId, flag);
    }
}

/**
 * 预算执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("money").val(data);
}
