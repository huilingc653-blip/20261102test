var moneyFun,moneyType,selectType,dateType,rateType,dicts,form;
bt.use(['form', 'rateType', 'moneyType','dateType', 'selectType', 'selectTable', 'dicts'], function () {
    var $ = bt.jquery;
    form = bt.form();


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
        }
        // 计算结算金额
        calculateSettleMoney();
    }
    // 结算币种回调
    bt.settleCurCallback = function (data) {
        bankAccTable.get("bankAcc").reload({curId: data.id, corpId: $('input[name="corpId"]').val(),bankTypeName:data.cotpname,bankTypeId: $('#corpBank').val()});
        // 计算结算金额
        calculateSettleMoney();
    }

    // 结算金额回调
    bt.settleMoneyCallback  = function () {
        var settleCur = selectType.get("curId").val();
        // 计算损益
        calculateFexLifeCycleProfitLoss(settleCur);
    }

    // 操作日记账汇率回调
    bt.recordRateCallback = function () {
        var settleCur = selectType.get("curId").val();
        calculateFexLifeCycleProfitLoss(settleCur);
    }

    // 平盘损益回调
    bt.profitLossMoneyCallback = function () {
        calculateFexLifeCycleConvertLossMoney();
    }

    // 折本位币汇率回调
    bt.convertRateCallback = function () {
        calculateFexLifeCycleConvertLossMoney();
    }
    // 提前汇率回调
    bt.earlyRateCallback = function (earlyRate){
        calRealMoney();
    }

    // 提前方式回调
    bt.earlyTypeCallback = function(earlyType) {
        changeEarlyType(earlyType.value);
        calCloseForwardRate();
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

    rateType = bt.rateType.init();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    init();



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
        if ("1" == submitData.earlyType && "11" == data.field.lifeStatus) {
            delete submitData.closeSettleDate;
            delete submitData.closeForwardRate;
            delete submitData.curId;
            delete submitData.money;
            delete submitData.bankAcc;
            delete submitData.recordRate;
            delete submitData.profitLossCur;
            delete submitData.profitLossMoney;
            delete submitData.convertCurType;
            delete submitData.convertRate;
            delete submitData.convertLossMoney;
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

    /**
     * 重置回调：
     * 新增页余额靠 JS 计算，需重算；
     * 修改页应恢复表单默认余额，不能用当前持仓重算覆盖。
     */
    resetCallBack = function () {
        if (!$("#id").val()) {
            init();
        } else {
            changeEarlyType();
        }
    };

});

function init() {
    // 修改页保留库中余额，避免用当前持仓重算
    if (!$("#id").val()) {
        var firstMoney = $("#firstMoney").val();
        calculateBal(firstMoney);
        calRealMoney();
    }
    changeEarlyType();
}

function calculateProfitLoss(){

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
                moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
                if ("Y" == $("#isShowConvert").val()) {
                    moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
                }

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
                        moneyType.get("earlyRate").val(rate);
                    }
                }
            }
        });
    }
}


// 计算折本位币生命周期损益
function calculateFexLifeCycleConvertLossMoney(calculateData){
    if ("Y" != $("#isShowConvert").val()) {
        return;
    }
    var calculateForm = {
        profitLossCur: selectType.get("profitLossCur").val(),
        convertCurType: $("#convertCurType").val(),
        profitLossMoney: moneyType.get('profitLossMoney').val(),
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
    var earlyRate = moneyType.get("earlyRate").val();
    var buyMoney = moneyType.get("buyMoney").val();
    var sellMoney = moneyType.get("sellMoney").val();
    if (!earlyRate) {
        var firstMoney = "1" == $("#busDirection").val() ? buyMoney : sellMoney;
        if ($("#buyCurId").val() == $("#tranCurPairBaseId").val()) {
            moneyType.get("realBuyMoney").val(firstMoney);
            moneyType.get("realSellMoney").val("");
        } else {
            moneyType.get("realBuyMoney").val("");
            moneyType.get("realSellMoney").val(firstMoney);
        }
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
            rate: earlyRate,
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
                // 计算损益
                calculateFexLifeCycleProfitLoss(settleCur);
            }
        }
    });
}


function changeEarlyType(earlyTypeData){
    var earlyType = null;
    if (earlyTypeData) {
        earlyType = earlyTypeData;
    } else {
        earlyType = dicts.get("earlyType").val();
    }
    // 1：原汇率减掉期点 2：平仓并开仓
    if ("1" == earlyType) {
        dateType.get("closeSettleDate").attr("lay-verify","");
        moneyType.get("closeForwardRate").attr("lay-verify","");
        moneyType.get("money").attr("lay-verify","");
        moneyType.get("recordRate").attr("lay-verify","");
        moneyType.get("profitLossMoney").attr("lay-verify","");
        selectType.get("curId").attr("data-required", false);
        $("#bankAcc").attr({"lay-verify":""});
        if("Y" == $("#isShowConvert").val()) {
            $("#convertCurType").attr({"lay-verify":""});
            moneyType.get("convertRate").attr("lay-verify","");
            moneyType.get("convertLossMoney").attr("lay-verify","");
            $(".convertTr").hide();
        }
        $(".extTr").hide();
    } else if ("2" == earlyType) {
        dateType.get("closeSettleDate").attr("lay-verify","required");
        moneyType.get("closeForwardRate").attr("lay-verify","required");
        moneyType.get("money").attr("lay-verify","required");
        moneyType.get("recordRate").attr("lay-verify","required");
        moneyType.get("profitLossMoney").attr("lay-verify","required");
        selectType.get("curId").attr("data-required", true);
        $("#bankAcc").attr({"lay-verify":"required|tableData"});
        if("Y" == $("#isShowConvert").val()) {
            $("#convertCurType").attr("lay-verify","required");
            moneyType.get("convertRate").attr("lay-verify","required");
            moneyType.get("convertLossMoney").attr("lay-verify","required");
            $(".convertTr").show();
        }
        $(".extTr").show();
    }
    form.renderIcon();
}
