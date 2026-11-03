var moneyFun,moneyType,selectType,dateType,rateType,dicts,form;
bt.use(['form', 'rateType', 'moneyType','dateType', 'selectType', 'selectTable', 'dicts'], function () {
    var $ = bt.jquery;
    form = bt.form();
    // 操作日期
    var opDate = $('[name="opDate"]').val() || $('[name="opDate"]').data('min');
    // 平仓结算日期 不能小于操作日期
    $('[name="closeSettleDate"]').data('min',opDate);

    var earlySettlementTypeDefaultvalue = $('[data-name="earlySettlementType"]').data('defaultvalue');
    if(!earlySettlementTypeDefaultvalue){
        $('[data-name="earlySettlementType"]').data('defaultvalue','2');
    }
    //交易地点下拉表格
    bt.selectTable.init({
        elem: "#tranAddrTable",
        url: basePath + "/basic/business/syscountrycust/listAllSysCountryArea",
        searchInput: "#tranAddr",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("#tranAddrId").val(data.id);
            $("input[name='tranAddr']").val(data.nameZh);
        },
    });

    var payeeAccTable = bt.selectTable.init({
        elem: "#payeeAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#buyCurId").val(), corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#payeeAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data, row, inputElem){
            if(data){
                inputElem.val(data.bankAcc);
                $('#payeeAcc').val(data.bankAcc);
            }else{
                $('input.text').val('');
            }
        }
    });

    var payerAccTable = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#sellCurId").val(), corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#payerAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data, row, inputElem){
            if(data){
                inputElem.val(data.bankAcc);
                $('#payerAcc').val(data.bankAcc);
            }else{
                $('input.text').val('');
            }
        }
    });
    var curId = $("#settleCurId").val() || $("#buyCurId").val();
    var bankAccTable = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: curId, corpId: $("input[name='corpId']").val(),bankTypeId: $('#corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#bankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data, row, inputElem){
            if(data){
                inputElem.val(data.bankAcc);
                $('#bankAcc').val(data.bankAcc);
            }else{
                $('input.text').val('');
            }
        }
    });

    // 第一行操作金额回调
    bt.firstMoneyCallback = function(data) {
        var firstMoney = data;
        $("#opMoney").val(firstMoney);
        var busDirection = $("#busDirection").val();
        var contractRate = $("#contractRate").val();
        var isPaymentAmount = bt.dicts.get('isPaymentAmount').val(); // 是否自动计算
        var secondMoney = 0;
        if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
            secondMoney = moneyFun.mul(firstMoney,contractRate);
        } else {
            secondMoney = moneyFun.div(firstMoney,contractRate,2);
        }

        if ("1" == busDirection) {
            moneyType.get("sellMoney").val(secondMoney);
        } else {
            moneyType.get("buyMoney").val(secondMoney);
        }
        // 余额计算
        calculateBal(firstMoney);
        // 实际买入和卖出金额计算
        if ("1" == isPaymentAmount) {
            calRealMoney();
            calDeliveryLossMoney();
        }
        // 计算结算金额
        calculateSettleMoney();
    }
    // 第二行操作金额回调
    bt.secondMoneyCallback = function(data) {
        var secondMoney = data;
        var busDirection = $("#busDirection").val();
        var contractRate = $("#contractRate").val();
        var isPaymentAmount = bt.dicts.get('isPaymentAmount').val(); // 是否自动计算
        var firstMoney = 0;
        if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
            firstMoney = moneyFun.div(secondMoney,contractRate,6);
        } else {
            firstMoney = moneyFun.mul(secondMoney,contractRate);
        }
        $("#opMoney").val(firstMoney);
        if ("1" == busDirection) {
            moneyType.get("buyMoney").val(firstMoney);
        } else {
            moneyType.get("sellMoney").val(firstMoney);
        }
        // 余额计算
        calculateBal(firstMoney);
        // 实际买入和卖出金额计算
        if ("1" == isPaymentAmount) {
            calRealMoney();
            calDeliveryLossMoney();
        }
        // 计算结算金额
        calculateSettleMoney();
    }
    // 结算币种回调
    bt.settleCurCallback = function (data) {
        // 结算账号默认值：结算币种=买入币种取交易收款账号，结算币种=卖出币种取交易付款账号
        setSettleBankAccDefault(selectType.get("curId").val());

        bankAccTable.get("bankAcc").reload({curId: data.id, corpId: $('input[name="corpId"]').val(),bankTypeName:data.cotpname,bankTypeId: $('#corpBank').val()});

        // 计算结算金额
        calculateSettleMoney();

    }
    // 操作日记账汇率回调
    bt.recordRateCallback = function () {
        var settleCur = selectType.get("curId").val();
        calculateFexLifeCycleProfitLoss(settleCur);
    }

    // 结算金额回调
    bt.settleMoneyCallback = function (){
        var settleCur = selectType.get("curId").val();
        calculateFexLifeCycleProfitLoss(settleCur);
    }

    // 平仓损益回调
    bt.closeLossMoneyCallback = function () {
        calAllLossMoney();
    }

    // 折本位币汇率回调
    bt.convertRateCallback = function () {
        calculateFexLifeCycleConvertLossMoney();
    }

    // 展期方式回调
    bt.earlySettlementTypeCallback = function(earlySettlementType) {
        changeEarlySettlementType(earlySettlementType.value);
    }
    // 展期汇率回调
    bt.earlySettlementRateCallback = function (earlySettlementRate){
        calRealMoney();
        calDeliveryLossMoney();
    }

    // 即期汇率回调
    bt.spotRateCallback = function (spotRate){
        calDeliveryLossMoney();
    }

    // 结算日期回调
    bt.settleDateCallback = function (){
        checkDate("settleDate");
        calCloseForwardRate();
    }

    bt.closeSettleDateCallback = function (){
        checkDate("closeSettleDate");
    }

    // 平仓远期汇率回调
    bt.closeForwardRateCallback = function (){
        calculateSettleMoney();
    }

    // 交割损益回调
    bt.deliveryLossMoneyCallback = function (){
        var earlySettlementType = dicts.get("earlySettlementType").val();
        if ("1" == earlySettlementType) {
            calculateFexLifeCycleConvertLossMoney();
        } else {
            calAllLossMoney();
        }
    }

    // 平仓损益回调
    bt.closeLossMoneyCallback = function (){
        calAllLossMoney();
    }

    // 总损益回调
    bt.allLossMoneyCallback = function (){
        calculateFexLifeCycleConvertLossMoney();
    }

    rateType = bt.rateType.init();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    init();

    changeEarlySettlementType();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        data.field.lifeStatus = $(this).attr("value");

        var submitData = data.field;
        delete submitData.currencyPairName;
        delete submitData.quotationCurId;
        delete submitData.quotationMethod;
        delete submitData.quotationReferCurId
        delete submitData.tranCurPairBaseId;
        delete submitData.tranCurPairReferId;

        if ("1" == submitData.earlySettlementType && "11" == data.field.lifeStatus) {
            delete submitData.closeSettleDate;
            delete submitData.closeForwardRate;
            delete submitData.curId;
            delete submitData.money;
            delete submitData.bankAcc;
            delete submitData.recordRate;
            delete submitData.closeLossMoney;
            delete submitData.allLossMoney;
        }

        var action = "/foreign/fexTradingLifecycle/updateLifecycle";
        $.ajax({
            "url": basePath + action,
            data: JSON.stringify(submitData),
            type:"post",
            dataType:"json",
            contentType: "application/json",
            async: false,
            success:function (result) {
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
            }
        });
        return false;
    });

});

function init() {
    var firstMoney = $("#firstMoney").val();
    calculateBal(firstMoney);
    // 结算账号为空时按结算币种带出默认值
    if (!$("#bankAcc").val()) {
        setSettleBankAccDefault();
    }
}


function calculateBal(firstMoney){
    var positionMoney = $("#positionMoney").val();
    var busDirection = $("#busDirection").val();
    var firstBal = moneyFun.sub(positionMoney,firstMoney);
    var contractRate = $("#contractRate").val();
    var secondBal = 0;
    if ($("#tranCurPairIdName").val() == $("#quotationMethod").val()) {
        secondBal = moneyFun.mul(firstBal,contractRate);
    } else {
        secondBal = moneyFun.div(firstBal,contractRate,6);
    }
    $("#firstBal").val(firstBal);
    $("#secondBal").val(secondBal);
    if ("1" == busDirection) {
        moneyType.get("buyBal").val(firstBal);
        moneyType.get("sellBal").val(secondBal);
    } else {
        moneyType.get("buyBal").val(secondBal);
        moneyType.get("sellBal").val(firstBal);
    }

}

// 计算生命周期损益
function calculateFexLifeCycleProfitLoss(settleCur){
    var profitLossCur = selectType.get("profitLossCur").val();
    var settleMoney = moneyType.get("money").val();
    var recordRate = moneyType.get("recordRate").val();
    var quotationCurId = $("#quotationCurId").val();
    var deliveryLossMoney = moneyType.get("deliveryLossMoney").val();
    var calculateForm = {
        profitLossCur: profitLossCur,
        settleMoney: settleMoney,
        recordRate: recordRate,
        quotationCurId: quotationCurId,
        settleCur: settleCur,
        corpId: $("#corpId").val()
    }
    if ("Y" == $("#isShowConvert").val()) {
        calculateForm.convertRate = moneyType.get('convertRate').val()
    }
    $.ajax({
        "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleProfitLoss",
        data: JSON.stringify(calculateForm),
        type:"post",
        dataType:"json",
        contentType: "application/json",
        async: false,
        success:function (result) {
            if (result.success) {
                moneyType.get('closeLossMoney').val(result.obj.profitLossMoney);
                calAllLossMoney();
            }
        }
    });
}


// 计算折本位币生命周期损益
function calculateFexLifeCycleConvertLossMoney(calculateData){
    if ("Y" != $("#isShowConvert").val()) {
        return;
    }
    var earlySettlementType = dicts.get("earlySettlementType").val();
    var profitLossMoney = moneyType.get('allLossMoney').val();
    if ("1" == earlySettlementType) {
        profitLossMoney = moneyType.get('deliveryLossMoney').val();
    }
    var calculateForm = {
        profitLossCur: selectType.get("profitLossCur").val(),
        convertCurType: $("#convertCurType").val(),
        profitLossMoney: profitLossMoney,
        convertRate: moneyType.get('convertRate').val(),
        corpId: $("#corpId").val()
    }

    $.ajax({
        "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleConvertLossMoney",
        data: JSON.stringify(calculateForm),
        type:"post",
        dataType:"json",
        contentType: "application/json",
        async: false,
        success:function (result) {
            if (result.success) {
                moneyType.get('convertLossMoney').val(result.obj);
            }
        }
    });
}

function calRealMoney(){
    var earlySettlementRate = moneyType.get("earlySettlementRate").val();
    var buyMoney = moneyType.get("buyMoney").val();
    var sellMoney = moneyType.get("sellMoney").val();
    if (!earlySettlementRate) {
        return;
    }
    $.ajax({
        "url": basePath + "/foreign/fexTradingLifecycle/calRealMoney",
        data:{
            tranCurPairIdName: $("#tranCurPairIdName").val(),
            tranCurPairBaseId: $("#tranCurPairBaseId").val(),
            tranCurPairReferId: $("#tranCurPairReferId").val(),
            quotationMethod:$("#quotationMethod").val(),
            buyMoney: buyMoney,
            sellMoney: sellMoney,
            buyCurId: $("#buyCurId").val(),
            sellCurId: $("#sellCurId").val(),
            contractRate: $("#contractRate").val(),
            rate: earlySettlementRate,
            busDirection:$("#busDirection").val()
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (result) {
            if (result.success) {
                moneyType.get("realBuyMoney").val(result.obj.realBuyMoney);
                moneyType.get("realSellMoney").val(result.obj.realSellMoney);
            }
        }
    });
}


function calCloseForwardRate(){
    var settleDate = dateType.get("settleDate").val();
    if(settleDate){
        var quotationCurId = $("#quotationCurId").val();
        var quotationReferCurId = $("#quotationReferCurId").val();
        var rateTypeId = $("#rateTypeId").val();
        $.ajax({
            "url": basePath + "/foreign/fexrateinfo/getByCurId",
            data:{
                curId1: quotationCurId,
                curId2: quotationReferCurId,
                rateTypeId:rateTypeId,
                exchangeDate:bt.date.format(settleDate, 'yyyy-MM-dd')
            },
            type:"post",
            dataType:"json",
            async: false,
            success:function (result) {
                if (result.success) {
                    if (result.obj) {
                        var rate = 0;
                        if($("#buyCurId").val() == quotationCurId){
                            rate = result.obj.purchaseRate;
                        }else if($("#sellCurId").val() == quotationCurId){
                            rate = result.obj.sellRate;
                        }
                        moneyType.get("closeForwardRate").val(rate);
                    }
                }
            }
        });
    }
}

// 计算总损益
function calAllLossMoney() {
    var deliveryLossMoney = moneyType.get("deliveryLossMoney").val() || 0;
    var closeLossMoney = moneyType.get("closeLossMoney").val() || 0;
    var allLossMoney = moneyFun.add(deliveryLossMoney, closeLossMoney);
    moneyType.get("allLossMoney").val(allLossMoney);
    calculateFexLifeCycleConvertLossMoney();
}

// 计算结算金额
function calculateSettleMoney(){
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    var closeForwardRate = moneyType.get("closeForwardRate").val();
    var settleCur = selectType.get("curId").val();
    if (!closeForwardRate) {
        return
    }
    $.ajax({
        "url": basePath + "/foreign/fexTradingLifecycle/calculateSettleMoney",
        data:{
            tranCurPairIdName: $("#tranCurPairIdName").val(),
            tranCurPairBaseId: tranCurPairBaseId,
            quotationMethod:$("#quotationMethod").val(),
            buyMoney: moneyType.get("buyMoney").val(),
            sellMoney: moneyType.get("sellMoney").val(),
            buyCurId: $("#buyCurId").val(),
            sellCurId: $("#sellCurId").val(),
            contractRate: $("#contractRate").val(),
            rate: closeForwardRate,
            settleCurId: selectType.get("curId").val(),
            busDirection:$("#busDirection").val()
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (result) {
            if (result.success) {
                moneyType.get("money").val(result.obj);
                calculateFexLifeCycleProfitLoss(settleCur);
            }
        }
    });
}

// 计算交割损益
function calDeliveryLossMoney() {
    var earlySettlementRate = moneyType.get("earlySettlementRate").val();
    var closeLossMoney = moneyType.get("closeLossMoney").val();
    var earlySettlementType = dicts.get("earlySettlementType").val();
    if (!earlySettlementRate) {
        return
    }
    var calDeliveryForm = {
        spotRate: moneyType.get("spotRate").val(),
            earlySettlementRate: earlySettlementRate,
            buyMoney: moneyType.get("buyMoney").val(),
            sellMoney: moneyType.get("sellMoney").val(),
            tranCurPairBaseId: $("#tranCurPairBaseId").val(),
            tranCurPairReferId: $("#tranCurPairReferId").val(),
            quotationCurId:$("#quotationCurId").val(),
            quotationReferCurId:$("#quotationReferCurId").val(),
            profitLossCur: selectType.get("profitLossCur").val(),
            busDirection:$("#busDirection").val()
    }
    $.ajax({
        "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleDeliveryLossMoney",
        data: JSON.stringify(calDeliveryForm),
        type:"post",
        dataType:"json",
        contentType: "application/json",
        async: false,
        success:function (result) {
            if (result.success) {
                moneyType.get("deliveryLossMoney").val(result.obj);
                calculateFexLifeCycleConvertLossMoney();
                if ("2" == earlySettlementType) {
                    calAllLossMoney();
                }
            }
        }
    });
}

function changeEarlySettlementType(earlySettlementTypeData){
    if("Y" == $("#isShowConvert").val()){
        $(".isShowConvert").show();
    } else {
        $(".isShowConvert").hide();
    }
    var earlySettlementType = null;
    if (earlySettlementTypeData) {
        earlySettlementType = earlySettlementTypeData;
    } else {
        earlySettlementType = dicts.get("earlySettlementType").val();
    }
    // 1：原汇率减远期点 2：平仓并开仓
    if (!earlySettlementType || "1" == earlySettlementType) {
        dateType.get("closeSettleDate").attr("lay-verify","");
        moneyType.get("closeForwardRate").attr("lay-verify","");
        moneyType.get("recordRate").attr("lay-verify","");
        moneyType.get("closeLossMoney").attr("lay-verify","");
        moneyType.get("allLossMoney").attr("lay-verify","");
        moneyType.get("money").attr("lay-verify","");
        //selectType.get("curId").attr({'data-required':false,'data-value':''});
        selectType.get("curId").setAttr({"data-required":false});
        $("#settleMoney").attr({"lay-verify":""});
        $("#bankAcc").attr({"lay-verify":""});
        $(".extTr").hide();
    } else if ("2" == earlySettlementType) {
        dateType.get("closeSettleDate").attr("lay-verify","required");
        moneyType.get("closeForwardRate").attr("lay-verify","required");
        moneyType.get("recordRate").attr("lay-verify","required");
        moneyType.get("closeLossMoney").attr("lay-verify","required");
        moneyType.get("allLossMoney").attr("lay-verify","required");
        moneyType.get("money").attr("lay-verify","required");
        selectType.get("curId").setAttr({"data-required":true});
        $("#settleMoney").attr({"lay-verify":"required"});
        $("#bankAcc").attr({"lay-verify":"required|tableData"});
        $(".extTr").show();
    }
    form.render();
    form.renderIcon();
}

/**
 * 带出结算账号默认值：结算币种=买入币种取交易收款账号，结算币种=卖出币种（或未选择结算币种）取交易付款账号
 * @param settleCur 结算币种id，不传则依次取页面结算币种当前值、隐藏域、控件默认值
 */
function setSettleBankAccDefault(settleCur) {
    if (!settleCur) {
        var $settleCurElem = $('[data-biz-name="curId"]');
        settleCur = selectType.get("curId").val() || $("#settleCurId").val()
            || $settleCurElem.data("defaultvalue") || $settleCurElem.data("value");
    }
    if (settleCur && settleCur == $("#buyCurId").val()) {
        $("#bankAcc").val($("#corpBankAcc").val());
    } else {
        $("#bankAcc").val($("#srcBankAcc").val());
    }
}
