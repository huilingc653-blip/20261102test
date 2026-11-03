var selectType,dicts,moneyType,moneyFun;
bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','ratioType','element', 'selectTable','dateType', 'btUpload'], function() {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.rateType.init();
    form.renderIcon();

    dicts = bt.dicts.init();

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

    bt.spotRateCallback = function(){
        calProfitLossMoney();
    }

    bt.contractRateCallback = function(){
        var isPaymentAmount = dicts.get('isPaymentAmount').val();
        if ("1" != isPaymentAmount) {
            return;
        }
        var fixedMoney = $("#fixedMoney").val();
        // 报价方式的左侧币种
        var quotationCurId = $("#quotationCurId").val();
        // 报价方式的右侧币种
        var quotationReferCurId = $("#quotationReferCurId").val();
        // 买入币种
        var buyCurId = $("#buyCurId").val();
        // 卖出币种
        var sellCurId = $("#sellCurId").val();
        var contractRate = moneyType.get("contractRate").val();
        var money = 0;
        if(fixedMoney=="1"){
            var buyMoney = moneyType.get("buyMoney").val();
            if(sellCurId==quotationReferCurId){
                money = moneyFun.mul(buyMoney,contractRate,2);
            }else{
                money = moneyFun.div(buyMoney,contractRate,2);
            }
            moneyType.get("sellMoney").val(money);
        }else if(fixedMoney=="2"){
            var sellMoney = moneyType.get("sellMoney").val();
            if(buyCurId==quotationReferCurId){
                money = moneyFun.mul(sellMoney,contractRate);
            }else{
                money = moneyFun.div(sellMoney,contractRate,2);
            }
            moneyType.get("buyMoney").val(money);
        }
        calProfitLossMoney();
    }

    bt.buyMoneyCallback = function(){
        var fixedMoney = $("#fixedMoney").val();
        if(fixedMoney=="2"){
            // 更新合同汇率
            calContractRate();
        }
        // 计算损益
        calProfitLossMoney();
    }

    bt.sellMoneyCallback = function(){
        var fixedMoney = $("#fixedMoney").val();
        if(fixedMoney=="1"){
            // 更新合同汇率
            calContractRate();
        }
        // 计算损益
        calProfitLossMoney();
    }

    // 损益回调
    bt.profitLossMoneyCallback = function(){
        calculateFexLifeCycleConvertLossMoney();
    }

    // 折本位币汇率回调
    bt.convertRateCallback = function(){
        calculateFexLifeCycleConvertLossMoney();
    }

    // 计算合同汇率
    function calContractRate(){
        // 报价方式的左侧币种
        var quotationCurId = $("#quotationCurId").val();
        // 报价方式的右侧币种
        var quotationReferCurId = $("#quotationReferCurId").val();
        var buyCurId = $("#buyCurId").val();
        var buyMoney = moneyType.get("buyMoney").val();
        var sellMoney = moneyType.get("sellMoney").val();
        var rate = 0;
        // 买入币种=报价方式的右侧
        if(buyCurId==quotationReferCurId){
            rate = moneyFun.div(buyMoney,sellMoney,6);
        }else if(buyCurId==quotationCurId){
            rate = moneyFun.div(sellMoney,buyMoney,6);
        }
        moneyType.get("contractRate").val(rate);
    }


    // 计算损益
    function calProfitLossMoney(){
        if ("1" != $("#isSpotProfitAndLoss").val()) {
            return;
        }
        var busDirection = $("#busDirection").val();
        var buyCurId = $("#buyCurId").val();
        var spotRate = moneyType.get("spotRate").val();
        var contractRate = moneyType.get("contractRate").val();
        var profitLossCur = selectType.get("profitLossCur").val();
        var convertCurType = $("#convertCurType").val();
        var isShowConvert = null;
        if (convertCurType) {
            isShowConvert = "Y";
        }
        var calculateData = {
            tranCurPairBaseId: $("#tranCurPairBaseId").val(),tranCurPairReferId:$("#tranCurPairReferId").val(),
            quotationCurId:$("#quotationCurId").val(),quotationReferCurId:$("#quotationReferCurId").val(),
            contractRate:contractRate,spotRate:spotRate,profitLossCur:profitLossCur,corpId:$("#corpId").val(),
            buyCurId:buyCurId,buyMoney:moneyType.get("buyMoney").val(),sellMoney:moneyType.get("sellMoney").val(),
            busDirection:busDirection,isShowConvert:isShowConvert
        }
        // 计算损益
        calculateFexProfitLoss(calculateData);
    }



    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();


    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        //$("#status").val($(this).attr("value"));
        data.field.lifeStatus = $(this).attr("value");

        var submitData = data.field;
        delete submitData.currencyPairName;
        delete submitData.quotationCurId;
        delete submitData.quotationMethod;
        delete submitData.quotationReferCurId
        delete submitData.tranCurPairBaseId;
        delete submitData.tranCurPairReferId;
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
     * 重置按钮回调
     */
    resetCallBack = function () {
    }


    /**
     * 计算损益
     * @param data
     */
    function calculateFexProfitLoss(calculateData){
        $.ajax({
            "url": basePath + "/foreign/fexProfit/lossManage/calculateFexProfitLoss",
            data: JSON.stringify(calculateData),
            type:"post",
            dataType:"json",
            contentType: "application/json",
            async: false,
            success:function (result) {
                if (result.success) {
                    moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
                    if ("Y" == calculateData.isShowConvert) {
                        moneyType.get('convertLossMoney').val(result.obj.convertLossMoney);
                    }
                }
            }
        });
    }

    // 计算折本位币生命周期损益
    function calculateFexLifeCycleConvertLossMoney(){
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
});


