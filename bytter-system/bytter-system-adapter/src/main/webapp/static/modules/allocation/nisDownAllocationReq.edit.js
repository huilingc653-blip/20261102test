bt.use(['form', 'layedit', 'selectType', 'dateType', 'moneyType', 'rateType','btUpload', 'dicts', 'selectTable', 'selectTree','accountInfo'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.rateType.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    var accountInfo = bt.accountInfo.init();
    bt.accountInfoParams = function () {
        accountInfo.set({
            "corpId": $("#payeeCorpId").val(),
            "curId": $("#curId").val()
        });
        return true;
    }
    //内部账户选择
    bt.accountInfoCallback = function (data, dom) {
        setPayeeCounterAcc(data);
    };

    function setPayeeCounterAcc(data) {
        if(data) {
            $("#payeeCounterAcc").val(data.account);
            $("#payeeCounterAccName").text(data.accName);
            $("#subjectName").text(data.subjectName);
            $("#payeeCounterCurName").text(data.curName);
            $("#checkBalance").text(moneyFun.format(data.checkBalance));
            $("#availableBalance").text(moneyFun.format(data.availableBalance));
            if(data.overdraftMoney) {
                $("#overdraftMoney").text(moneyFun.format(data.overdraftMoney));
            } else {
                $("#overdraftMoney").text('0.00');
            }
            if(data.accOpenTime) {
                $("#accOpenTime").text(data.accOpenTime);
            } else {
                $("#accOpenTime").text('');
            }
            if(data.deadlineDate) {
                $("#deadlineDate").text(data.deadlineDate);
            } else {
                $("#deadlineDate").text('');
            }
            if(data.interestRate) {
                $("#interestRate").text(bt.rateType.getFun().format(data.interestRate));
            } else {
                $("#interestRate").text('');
            }
        } else {
            $("#payeeCounterAcc").val('');
            $("#payeeCounterAccName").text('');
            $("#subjectName").text('');
            $("#payeeCounterCurName").text('');
            $("#checkBalance").text('0.00');
            $("#availableBalance").text('0.00');
            $("#overdraftMoney").text('0.00');
            $("#accOpenTime").text('');
            $("#deadlineDate").text('');
            $("#interestRate").text('');
        }
    }
    //判断支付结算是否挂接资金计划
    var budgetSel = $("#budgetSel").val();

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
                    // 初始化收款人账户信息
                    $("#payeeBankName").val(data.bankName);
                    $("#payeeCorpId").val(data.corpId);
                    initBankAccBalance(data.bankAcc, data.curId, "payeeAccBalance");
                    // bt.moneyType.get("payeeAccBalance").val(data.bankAccBalance);
                    // 加载收款账号
                    var selectPayeeTable = payerAccInit(data.corpId, $("#payerAccId").val());
                    // 初始化付款人账户信息
                    if ("undefined" != data.parentBankAcc && null != data.parentBankAcc) {
                        $("#payerBankName").val(data.parentBankAcc.bankName);
                        $("#payerCorpId").val(data.parentBankAcc.corpName);
                        initBankAccBalance(data.parentBankAcc.bankAcc, data.parentBankAcc.curId, "payerAccBalance");
                        // bt.moneyType.get("payerAccBalance").val(data.parentBankAcc.bankAccBalance);

                        // 加载收款单位
                        bt.selectTree.init({
                            url: basePath + "/allocation/nisallocationreq/treeByNetId?prentAccId=" + $("#payerAccId").val(),
                            elem: ".demo-selectTree-normal",
                            name: 'payeeCorpId',
                            id: 'id',
                            pId: 'pId',
                            required: false,
                            selected: data.corpId,
                            clickCallBack: function (id) {
                                $("#payeeAccId").val("");
                                $("#payeeAcc").val("");
                                $("#payeeAccName").val("");
                                $("#payeeBankName").val("");
                                bt.moneyType.get("payeeAccBalance").val(0);
                                // 重新加载收款账号
                                selectPayeeTable.get("payeeAcc").reload({
                                    corpId: id,
                                    prentAccId: $("#payerAccId").val(),
                                    curId: $("#curId").val()
                                });
                                // 重新设置用途
                                var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=2&corpId=" + id;
                                bt.selectType.get("purpose").attr("data-url", purposeUrl);
                                //资金计划
                                renderBudget(true);
                            }
                        });
                        renderBudget(false);
                    }
                    form.render();
                }
            }
        });
    };
    initAccInfo();

    /**
     * 重置按钮回调
     */
    resetCallback = function () {
        $("#payeeAccId").val($("#payeeAccId").data("defaultvalue"));
        $("#curId").val($("#curId").data("defaultvalue"));
        initAccInfo();
    }

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (flag) {
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: $("#corpId").val(),
            itemCode: $("#downItem").val(),
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
    };


    onSave = function () {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            var action = "/allocation/nisallocationreq/updateAskAndDown";
            $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                bt.loading.hide();
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
        })
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        if($("#isOpenLimit").val()=="1") {
            bankAccLimit(data);
        }else{
            submit(data);
        }
        return false;
    });

    //单位限额
    function corpLimit(data,msg){
        //获取单位本位币换算汇率
        var rate = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, corpId: data.field.corpId},
            dataType: 'json',
            success: function (da) {
                rate = bt.money.mul(data.field.money,da,2)
            }
        });
        rate = parseFloat(rate);
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate ,corpId: data.field.corpId,temp:0,id:data.field.id,wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            msg += '<br>'+bt.lang('alert.over.daylimit.corp')//日累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 2){
            msg += '<br>'+bt.lang('alert.over.monthlimit.corp')//月累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        }

        if(msg.length>0){
            bt.loading.hide();
            parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info')},function (index) {
                submit(data);
            });
        }else{
            submit(data);
        }
    }

    //账号限额
    function bankAccLimit(data){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:0,id:data.field.id,wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        var msg='';
        if (result=== 1) {
            msg = bt.lang('alert.over.paylimit')//单笔支付限额
        } else if(result === 2){
            msg = bt.lang('alert.over.daylimit')//日累计支付限额
        }else if(result === 3){
            msg = bt.lang('alert.over.monthlimit')//月累计支付限额
        }
        corpLimit(data,msg);
    }

    //提交
    function submit(data){
        $("#attachmentId").val(up.getGroupId());
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
        var bankaccFlag = checkBankAccBal(bt.moneyType.get("payerAccBalance").val(), data.field.money, "money");
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
    }

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
     * 付款账号
     * @param corpId
     * @param prentAccId
     */
    payerAccInit = function (corpId, prentAccId) {
        var selectTable = bt.selectTable.init({
            elem: "#payeeAccTable",
            url: basePath + "/allocation/nisallocationreq/getPayeeAcc",
            urldata: {corpId: corpId, prentAccId: prentAccId, curId: $("#curId").val()},
            scrollX: true,
            scrollY: "200px",
            searchInput: "#payeeAcc",
            paging: true,
            columns: [
                {data: "bankAcc",},
                {data: "accName",},
                {data: "bankName",},
                {data: "curName",}
            ],
            clickCallback: function (data) {
                $("#payeeAccId").val(data.id);
                $("#payeeAcc").val(data.bankAcc);
                $("#payeeAccName").val(data.accName);
                $("#payeeBankName").val(data.bankName);
                $("#payeeBankCode").val(data.bankCode);
                bt.moneyType.get("payeeAccBalance").val(data.balance);
                initBank();
                $.ajax({
                    url: basePath + "/inneraccount/fcacc?getFcAccBalByAccount",
                    method: 'POST',
                    data: {account: data.counterAccId},
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            setPayeeCounterAcc(result.obj);
                        }
                    }
                });
            }
        });
        return selectTable;
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(flag) {
        // zuoy 2018-05-31 暂不挂资金计划
        // if (budgetSel == "1") {
        //     $('input[data-name="money"]').attr("readonly", "readonly");
        //     $genOtherHtml(flag);
        // }
    }

    /**
     * 选择日期回调
     * @param data
     * @param $elem
     */
    dateCallBack = function (data, $elem) {
        renderBudget(true);
    }
});

/**
 * 预算执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("money").val(data);
}
