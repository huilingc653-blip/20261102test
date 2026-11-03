bt.use(['form', 'layedit', 'dateType', 'corp', 'selectTable','selectTableType', 'bank', 'rateType',
    'bankInfo', 'moneyType', 'region', 'externalCorpBankAcc', 'accebillInfo',
    'accebillView', 'btUpload', 'dicts', 'selectType', 'inputExternalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dicts = bt.dicts.init();
    bt.rateType.init();


    /** 计划明细控制项 zuoy 2018-11-19**/
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }
    /**
     * 收款账户值改变后回调
     */
    $("#payeeAcc").change(function () {
        if (dtlColSels["payeeAcc"]) {
            renderBudget();
        }
    });
    /**
     * 用途回调
     */
     purposeCallBack = function () {
        if (dtlColSels["purpose"]) {
            renderBudget();
        }
    }
    /**
     * 业务类别回调
     * @param data
     */
    busTypeCallback = function (data) {
        $("#defItemCode").val(data.itemcode);
        renderBudget();
    }

    var selectType = bt.selectType.init();

    $.ajax({
        url: basePath + "/netbank/nisbustype?getNisBusType",
        method: 'POST',
        data: {id: $("#busTypeDef").val()},
        dataType: 'json',
        async: true,
        success: function (result) {
            if (result.obj) {
                $("#defItemCode").val(result.obj.itemCode);
            }
        }
    });

    /**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var region = bt.region.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    var payerAccCol = [
        {data: "merchantNo",},
        {data: "payTypeName",},
        {data: "isOnlineName",},
        {data: "rmk",}
    ];

    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/ttpay/tpPayment/getPayerAcc",
        urldata: {corpId: $("#corpIdDef").val(), payType: $("select[name='payType']").val()!=null? $("select[name='payType']").val() : $("#payTypeDef").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.merchantNo);
                //获取账户余额及可用余额
                $.ajax({
                    url: basePath + "/ttpay/tpPayment?getPayBillBal",
                    method: 'POST',
                    data: {merchantNo: data.merchantNo,payType: $("select[name='payType']").val()},
                    dataType: 'json',
                    success: function (data) {
                        moneyType.get("balance").val(data.totalAmount);
                        moneyType.get("availBal").val(data.availableAmount);
                        $("#balanceDiv").text(moneyFun.format(data.totalAmount));
                        $("#availBalDiv").text(moneyFun.format(data.availableAmount));
                        form.render();
                    }
                });

                corpLimit($("#corpIdDef").val(),data.merchantNo);
                //重新渲染资金计划
                renderBudget();

            } else {
                $('input.text').val('');
            }
        }
    });
    var payeeAccCol = [
        {data: "loginNo",},
        {data: "phone",},
        {data: "email",},
        {data: "shopName",},
        {data: "shopType",},
        {data: "payeeModeName",}
    ];
    //收款账号
    var payeeAccInit = bt.selectTable.init({
        elem: "#payeeAccTable",
        url: basePath + "/ttpay/tpPayment/getPayeeAcc",
        urldata: {
            corpId: $("#corpIdDef").val(),
            payType: $("select[name='payType']").val()!=null? $("select[name='payType']").val() : $("#payTypeDef").val()
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payeeAcc",
        paging: true,
        columns: payeeAccCol,
        clickCallback: function (data) {
            if (data) {
                if(data.payeeMode=='0'){
                    $('#payeeAcc').val(data.loginNo);
                }else if(data.payeeMode=='1'){
                    $('#payeeAcc').val(data.phone);
                }else if(data.payeeMode=='2'){
                    $('#payeeAcc').val(data.email);
                }else{
                    $('#payeeAcc').val(data.loginNo);
                }
                $('#payeeAccName').val(data.shopName);
                $('#payeeMode').val(data.payeeMode);
                $('#payeeModeName').val(data.payeeModeName);
                if (dtlColSels["payeeAcc"]) {
                    renderBudget();
                }
            } else {
                $('input.text').val('');
            }
        }
    });

    /**
     * 校验科目余额是否需要控制
     * @returns {Array}
     */
    checkCtl = function () {
        var attr = [];
        $("#budgetProcessDataTable").find("div[row]").each(function () {
            if ($(this).attr("data-gt-balance")) {
                attr.push($(this).attr("data-gt-balance"));
            }
        });
        return attr;
    }

    /**
     * 校验账户余额
     * @param data
     * @returns {boolean}
     */
    checkBalance = function (data) {
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        //账户余额校验
        var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
        var balControlType = $("#balControlType").val();//付款余额控制类型：0-账户余额 1-可付款余额
        var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        if(balControlType == "1"){
            balance = moneyFun.toNumber(moneyType.get("availBal").val());//可用余额
        }
        //付款金额大于账户余额
        var flag = false;
        if (payMoney > balance) {
            if (bankaccOver == "1") {
                flag = true;
            } else if (bankaccOver == "2") {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                return false;
            }
        }
        if (flag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function (index) {
                bt.loading.show();
                rePay(data);
            });
        } else {
            rePay(data);
        }
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        //校验
        corpCheck(data);
        return false;
    });

    //提交前校验
    function corpCheck(data){
        if($("#isOpenLimit").val()=="1") {
            checkLimit(data);
        }else{
            submit(data);
        }
    }

    //显示商户号限额
    function corpLimit(corpId,payerAcc){
        $.ajax({
            url: basePath + "/ttpay/tpPayment/getQuotaInfo",
            method: 'GET',
            async: false,
            data: { corpId: corpId,merchantNo:payerAcc,payType:$("select[name='payType']").val()},
            dataType: 'json',
            success: function (data) {
                if(data.id!=null){
                    if(data.daySingle!=null){
                        $("#s_daySingle").text(bt.money.format(data.daySingle));
                        $("#daysingle").val(data.daySingle);
                    }else{
                        $("#s_daySingle").text("无");
                    }
                    if(data.dayTotalAvail!=null){
                        $("#s_dayTotalAvail").text(bt.money.format(data.dayTotalAvail));
                        $("#dayTotalAvail").val(data.dayTotalAvail);
                    }else{
                        $("#s_dayTotalAvail").text("无");
                    }
                    if(data.monthTotalAvail!=null){
                        $("#s_monthTotalAvail").text(bt.money.format(data.monthTotalAvail));
                        $("#monthTotalAvail").val(data.monthTotalAvail);
                    }else{
                        $("#s_monthTotalAvail").text("无");
                    }
                    $("#quota_info").css("display","block");
                    $("#quota_title").css("display","block");
                }else{
                    $("#daysingle").val("");
                    $("#dayTotalAvail").val("");
                    $("#monthTotalAvail").val("");
                    $("#quota_info").css("display","none");
                    $("#quota_title").css("display","none");
                }
            }
        });
    }

    //获取支付方式
    $.ajax({
        url: basePath + "/ttpay/tpPayment/getPayType",
        method: 'POST',
        data: {},
        dataType: 'json',
        success: function (data) {
            var payTypeDef = $('#payTypeDef').val();
            var corpIdDef = $('#corpIdDef').val();
            //获取付款方式信息
            if (data && data.length > 0) {
                $.each(data, function (index, item) {
                    $("#payType").append("<option value=" + item.code + ">" + item.name + "</option>");
                    $("select[name='payType']").val(payTypeDef);
                });
            }
            payTypeCallback(corpIdDef,payTypeDef);
            form.render();
        }
    });
    //付款方式选择监听
    form.on('select(payType)', function (data) {
        var corpId = $("select[name='corpId']").val();
        var payType= $("select[name='payType']").val()
        //获取付款方式信息
        payTypeCallback(corpId,payType);
    });
    //付款方式选择事件
    function payTypeCallback(corpId,payType) {
        payerAccInit.get("payerAcc").rebuild({
            urldata: {corpId: corpId, payType: payType},
            columns: payerAccCol
        });
        //重新加载收款账号
        payeeAccInit.get("payeeAcc").rebuild({
            urldata: {corpId: corpId, payType: payType},
            columns: payeeAccCol
        });
        corpLimit($("input[name='corpId']").val(), $("#payerAcc").val())
        //资金计划
        renderBudget();
    }

    //初始化单位
    var corp = bt.corp.init();
    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $("#corpIdDef").val();
        if ($('input[name="corpId"]').val()) {
            corpId = $('input[name="corpId"]').val();
        }
        var itemCode = $('input[name="payItem"]').val();
        var defItemCode = $('input[name="defItemCode"]').val();
        var processDate = dateType.get("wishPayday").val();
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode:defItemCode,
            processDate: processDate,
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            purpose: selectType.get("purpose").val(),
            wishPayday: dateType.get("wishPayday").val(),
            payerAcc: $("#payerAcc").val(),
            payeeAcc: $("#payeeAcc").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            $genOtherHtml();
        }
    }
    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        renderBudget();
    }
    /**
     * 暂存提交后重新初始化数据
     */
    resetInit = function (data) {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        //重新加载收款账号
        payeeAccInit.get("payeeAcc").rebuild({
            urldata: {corpId: $("input[name='corpIdDef']").val(),payType: $("select[name='payType']").val()},
            columns: payeeAccCol
        });
        $('#payeeAcc').val("")

        moneyType.get("payMoney").val(0);
        dicts.get("isForindividual").val(0);
        dateType.get("wishPayday").val(bt.date.format(new Date()));

        // 重新设置业务类别
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + data.field.corpId;
        bt.selectType.get("busType").attr("data-url", busTypeUrl);

        renderBudget()
    }
    function checkLimit(data){
        //判断限额
        var result=0;
        $.ajax({
            url: basePath + "/ttpay/tpPayment/limitForMerchant",
            method: 'GET',
            async: false,
            data: {corpId: data.field.corpId,merchantNo: data.field.payerAcc,payMoney: data.field.payMoney, wishPayDate:data.field.wishPayday},
            dataType: 'json',
            success: function (data) {
                result = data;
            }
        });
        var msg='';
        if (result=== 1) {
            msg += '<br>'+bt.lang('alert.over.paylimit')//日单笔支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 2){
            msg += '<br>'+bt.lang('alert.over.daylimit')//日累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 3){
            msg += '<br>'+bt.lang('alert.over.monthlimit')//月累计支付限额
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

    //提交
    function submit(data){
        $("#attachmentId").val(up.getGroupId());
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }

        //判断期望支付日期（根据支付结算参数是否控制期望支付日期）
        var curDate = new Date();
        var wishPayday = dateType.get("wishPayday").val();//期望支付日期
        if (dateFun.compare(wishPayday, curDate) > 0) {
            bt.loading.hide();
            layer.confirm(bt.lang("nis.netbank.message.curDate.lt.wishPayday"), {icon:3},function () {
                submitTemp(data);
            });
        }else{
            submitTemp(data);
        }
    }

    //封装
    function submitTemp(data){
        var itemCodeCtl = checkCtl();
        if (itemCodeCtl.length) {
            bt.loading.hide();
            if (!dtlColSels.length) {
                parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.detailbalance.notctl', [itemCodeCtl]),
                    {
                        btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                        icon: 0,
                    }, function () {
                        bt.loading.show();
                        checkBalance(data);
                    });
            } else {
                var fbsItemBalance = $("#fbsItemBalance").val();
                if (fbsItemBalance == 0) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.itembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                } else if (fbsItemBalance == 1) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.topitembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                } else if (fbsItemBalance == 2) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.superitembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                }
            }
        } else {
            checkBalance(data);
        }
    }

    function submitForm() {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {

            bt.loading.show();
            var action = "/ttpay/tpPayment/modify";
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
        });
    }
    //重复付款校验
    function rePay(data) {
        var result = false;
        $.ajax({
            url: basePath + "/ttpay/tpPayment?rePay",
            method: 'POST',
            async: false,
            data: {corpId: data.field.corpId, payMoney: data.field.payMoney, payeeAcc: data.field.payeeAcc, curId: data.field.curId, id: ''},
            dataType: 'json',
            success: function (data) {
                result = data.obj;
            }
        });
        if (result) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.repay'), function (index) {
                submitForm(data);
            });
        } else {
            submitForm(data);
        }
        return false;
    }
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("payMoney").val(data);
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}

//修改
$(".bussiness").click(function () {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/ttpay/bussiness/add");
        return;
    }
});