var moneyType;
var moneyFun;
var rateType;
var dateType;
var moneyFun;
bt.use(['form', 'layedit', 'corp', 'selectType', 'dateType', 'moneyType', 'rateType', 'accountTable', 'dicts', 'btUpload','accountInfo'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.accountTable.init();
    bt.dicts.init();
    bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    rateType = bt.rateType.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    bt.dateType.init();
    var accountInfo = bt.accountInfo.init();
    bt.accountInfoParams = function () {
        accountInfo.set({
            "corpId": bt.corp.get("corpId").val(),
            "curId": $("#curId").val()
        });
        return true;
    }
    //内部账户选择
    bt.accountInfoCallback = function (data, dom) {
        setPayeeCounterAcc(data);
    };

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = bt.corp.get("corpId").val();
        var itemCode = $("#askItem").val();
        var processDate = bt.dateType.get("wishPayday").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "200",
            processDate: processDate,
            curId: $("#curId").val(),
            type: 'select',
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };

    onSave = function (data) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            var action = "/allocation/nisallocationreq/save";
            //控制重复提交
            if($(data.elem).parent().hasClass('isSubmiting')){
                bt.loading.hide();
                return false
            }
            //增加已提交的标志
            $(data.elem).parent().addClass('isSubmiting');
            $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    //解除已提交的标志
                    $(data.elem).parent().removeClass('isSubmiting');
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParentTable();
                            bt.navtab.refreshCurrent();
                            var timeStamp = Date.parse(new Date());
                            $('#timeStamp').val(timeStamp);
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                });
            }, 'JSON');
        })
    };

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
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
            data: {curId: data.field.curId, corpId: data.field.payerCorpId},
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
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.payerAccId,temp:1,wishPayDate: data.field.wishPayday},
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
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:1,wishPayDate: data.field.wishPayday},
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
        if($("#isAccount").val() == 1) {
            accountFlag = checkFcAccBal($("#fcAccBalance").val(), data.field.money, "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }

            // 校验内部账号是否必填payeeCounterAcc
            var payeeCounterAcc = $("#payeeCounterAcc").val();
            if (isEmpty(payeeCounterAcc)){
                bt.loading.hide();
                layer.alert(bt.lang("内部账号为必填，如果没有请维护！"), {icon: 2});
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
            onSave(data);
        } else if (!bankaccFlag && accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'), function (index) {
                onSave(data);
            });
        } else if (bankaccFlag && !accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'), function (index) {
                onSave(data);
            });
        } else {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'), function (index) {
                onSave(data);
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
});

//根据选择的收款单位加载付款账户信息
function corpCallback(id) {
    var $ = bt.jquery, form = bt.form();
    clearPayerAcc();
    // 重新根据单位ID加载账户表格
    bt.accountTable.get("payeeAcc").setCorp(id);
    // 重新设置用途
    var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=2&corpId=" + id;
    bt.selectType.get("purpose").attr("data-url", purposeUrl);
    //资金计划
    renderBudget();
}

function accountCallBack(data) {
    var $ = bt.jquery;
    clearPayerAcc();
    $("#payeeAccId").val(data.bankAccId);
    $("#payeeAcc").val(data.bankAcc);
    $("#payeeAccName").val(data.accName);
    $("#payeeBankName").val(data.bankName);
    $("#curId").val(data.curId);
    $("#curName").val(data.curName);
    $("#payeeBankCode").val(data.bankCode);
    $("#payeeBankTypeId").val(data.bankTypeId);
    initProvAndCity(data.bankAccId, data.curId, "payeeProvCity");
    //资金计划
    renderBudget();
    if (null != data.parentAccount) {
        $("#payerCorpId").val(data.parentAccount.corpName);
        $("#payerAccId").val(data.parentAccount.bankAccId);
        $("#payerAcc").val(data.parentAccount.bankAcc);
        $("#payerAccName").val(data.parentAccount.accName);
        $("#payerBankName").val(data.parentAccount.bankName);
        $("#payerBankCode").val(data.parentAccount.bankCode);
        $("#payerBankTypeId").val(data.parentAccount.bankTypeId);
        // $("#balance").val(data.parentAccount.balance);
        initBankAccBalance(data.parentAccount.bankAcc, data.parentAccount.curId, "balance");
        initFcAccBalance(data.parentAccount.bankAcc, data.parentAccount.curId, "fcAccBalance");
        initProvAndCity(data.parentAccount.bankAccId, data.parentAccount.curId, "payerProvCity");
    }
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
    //获取账户余额可用金额
    $.ajax({
        url: basePath + "/bankacc/bisaccbal?getBal",
        method: 'POST',
        data: {bankAcc: data.bankAcc,curId: data.curId},
        dataType: 'json',
        success: function (data) {
            $("#availBalDiv").text(moneyFun.format(data));
        }
    });
}

function setPayeeCounterAcc(data) {
    if(data) {
        $("#payeeCounterAcc").val(data.account);
        $("#payeeCounterAccName").text(data.accName);
        $("#subjectName").text(data.subjectName);
        $("#payeeCounterCurName").text(data.curName);
        $("#checkBalance").text(moneyFun.format(data.checkBalance));
        $("#availableBalance").text(moneyFun.format(data.availableBalance));
        if (data.overdraftMoney) {
            $("#overdraftMoney").text(moneyFun.format(data.overdraftMoney));
        } else {
            $("#overdraftMoney").text('0.00');
        }
        if (data.accOpenTime) {
            $("#accOpenTime").text(data.accOpenTime);
        } else {
            $("#accOpenTime").text('');
        }
        if (data.deadlineDate) {
            $("#deadlineDate").text(data.deadlineDate);
        } else {
            $("#deadlineDate").text('');
        }
        if (data.interestRate) {
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

/**
 * 资金计划挂接
 */
function renderBudget() {
    //判断支付结算是否挂接资金计划
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        // $('input[data-name="money"]').attr("readonly","readonly");
        $genOtherHtml();
    }
}

/**
 * 预算执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("money").val(data);
}

/**
 * 选择日期回调
 * @param data
 * @param $elem
 */
function dateCallBack() {
    renderBudget();
};

purposeBefore = function(data){
    if($.isArray(data)){
        data.unshift({
            purpose:'',
        })
    }
    return data
}

/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}
