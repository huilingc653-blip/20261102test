var form, element, corp, dateType, selectType, moneyType, dicts, upload, ratioType;
var currencyPairList =[];
bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType",
    "dicts", "btUpload", "ratioType"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;

    // 期权费点数 add / edit
    var premiumPointMax = $('[data-name="premiumPointMax"]').val();
    if(!premiumPointMax && $('[data-id="premiumPointMin1"]').val()){
        $('#premiumPointSingle').removeClass('layui-hide');
        $('#premiumPointCycle').addClass('layui-hide');
    }

    // 期权费金额 add / edit
    var premiumMoneyMax = $('[data-name="premiumMoneyMax"]').val();
    if(!premiumMoneyMax && $('[data-id="premiumMoneyMin1"]').val()){
        $('#premiumMoneySingle').removeClass('layui-hide');
        $('#premiumMoneyCycle').addClass('layui-hide');
    }

    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.init();
    dicts = bt.dicts.init();
    ratioType = bt.ratioType.init();
    var gid = $("#gid").val();
    upload = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //监听提交
    form.on("submit(submit)", function (data) {
        var action = "/foreign/fexTranApp/saveDtl";
        data.field.attachmentId = upload.getGroupId();
        //校验期权费点数、期权费币种和期权费金额是否填写
        var premiumPointMin = $('[name="premiumPointMin"]').val();
        var premiumPointMax = $('[name="premiumPointMax"]').val();
        var curId = selectType.get("curId").val();
        var premiumMoneyMin = $('[name="premiumMoneyMin"]').val();
        var premiumMoneyMax = $('[name="premiumMoneyMax"]').val();

        // 期权费点数
        if($('#premiumPointCycle').hasClass('layui-hide')){
            data.field.premiumPointMin = $('#premiumPointSingle').find('[name="premiumPointMin"]').val();
            delete  data.field.premiumPointMax
        }else{
            data.field.premiumPointMin = $('#premiumPointCycle').find('[name="premiumPointMin"]').val();
        }

        // 期权费金额
        if($('#premiumMoneyCycle').hasClass('layui-hide')){
            data.field.premiumMoneyMin = $('#premiumMoneySingle').find('[name="premiumMoneyMin"]').val();
            delete  data.field.premiumMoneyMax
        }else{
            data.field.premiumMoneyMin = $('#premiumMoneyCycle').find('[name="premiumMoneyMin"]').val();
        }

        //期权费点数、期权费币种、期权费金额必填校验
        //期权费点数、期权费币种、期权费金额全部为空
        if(isEmpty(data.field.premiumPointMin) && isEmpty(data.field.premiumPointMax) && isEmpty(data.field.curId)
            && isEmpty(data.field.premiumMoneyMin) && isEmpty(data.field.premiumMoneyMax)){
            parent.layer.alert(bt.lang("fex.tran.app.submit.message"),{icon:2});
            return false
        }
        //开始期权费点数为空，结束期权费点数不为空
        if(isEmpty(data.field.premiumPointMin) && !isEmpty(data.field.premiumPointMax)){
            parent.layer.alert(bt.lang("fex.tran.app.submit.message"),{icon:2});
            return false
        }
        //期权费币种不为空，期权费开始、结束金额为空
        if(isNotEmpty(data.field.curId) && isEmpty(data.field.premiumMoneyMin) && isEmpty(data.field.premiumMoneyMax)){
            parent.layer.alert(bt.lang("fex.tran.app.submit.message"),{icon:2});
            return false

        }
        //期权费开始金额为空，期权费结束金额不为空
        if(isEmpty(data.field.premiumMoneyMin) && !isEmpty(data.field.premiumMoneyMax)){
            parent.layer.alert(bt.lang("fex.tran.app.submit.message"),{icon:2});
            return false
        }
        //期权费开始金额不为空，期权费币种为空
        if(isNotEmpty(data.field.premiumMoneyMin) && isEmpty(data.field.curId)){
            parent.layer.alert(bt.lang("fex.tran.app.submit.message"),{icon:2});
            return false
        }
        if(isEmpty(data.field.quotationMethod)){
            layer.alert(bt.lang('fex.tranApp.currencyPair.message',[$('#typeName').val()]), {icon: 7});
            return false;
        }

        bt.loading.show();
        $.ajax({
            url: basePath + action,
            type: "post",
            data: data.field,
            dataType: "json",
            async: false,
            success: function (result) {
                bt.loading.hide();
                if (result != null) {
                    parent.closeTab(result);
                }
            }
        });
        return false;
    });

    form.on("checkbox(autoCal)", function (data) {
        calWishRate();
    });

    // 期权费点数
    $('#premiumPointCycle i').click(function () {
        $('#premiumPointCycle').addClass('layui-hide')
        $('#premiumPointSingle').removeClass('layui-hide');
    })

    $('#premiumPointSingle i').click(function () {
        $('#premiumPointCycle').removeClass('layui-hide')
        $('#premiumPointSingle').addClass('layui-hide')
    })

    // 期权费金额
    $('#premiumMoneyCycle i').click(function () {
        $('#premiumMoneyCycle').addClass('layui-hide')
        $('#premiumMoneySingle').removeClass('layui-hide');
    })

    $('#premiumMoneySingle i').click(function () {
        $('#premiumMoneyCycle').removeClass('layui-hide')
        $('#premiumMoneySingle').addClass('layui-hide')
    })
});


/**
 * 期望汇率计算
 */
function calWishRate() {
    var buyCurId = selectType.get("buyCurId").val();
    var buyMoney = moneyType.get("buyMoney").val();
    var sellCurId = selectType.get("sellCurId").val();
    var sellMoney = moneyType.get("sellMoney").val();
    if (isNotEmpty(buyCurId) && isNotEmpty(buyMoney) && isNotEmpty(sellCurId) && isNotEmpty(sellMoney)) {
        $.ajax({
            "url": basePath + "/basic/business/btcurrencypair/getByCurId",
            data:{
                curId1: buyCurId,
                curId2: sellCurId
            },
            type:"post",
            dataType:"json",
            async: false,
            success:function (data) {
                if (data.success) {
                    if (null == data.obj) {
                        parent.layer.alert(bt.lang("fex.tranApp.currencyPair"), {icon: 2});
                        moneyType.get("wishRate").val("");
                    } else {
                        setWishRate(buyCurId, sellCurId, buyMoney, sellMoney, data.obj);
                    }
                }
            }
        });
    }
}

/**
 * 汇率计算
 * @param buyCurId
 * @param sellCurId
 * @param buyMoney
 * @param sellMoney
 * @param data
 */
function setWishRate(buyCurId, sellCurId, buyMoney, sellMoney, data) {
    var autoCal = $("input[type = 'checkbox'][name = 'autoCal']:checked");
    var wishRate = moneyType.get("wishRate").val();
    if (isNotEmpty(wishRate)) {
        // 期望汇率存在值
        if (autoCal.length > 0) {//计算买入
            if (data.baseCurId == buyCurId) {//买入币种为标准
                moneyType.get("buyMoney").val(bt.money.div(sellMoney, wishRate,2));
            } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                moneyType.get("buyMoney").val(bt.money.mul(sellMoney, wishRate,2));
            }
        } else {//计算卖出
            if (data.baseCurId == buyCurId) {//买入币种为标准
                moneyType.get("sellMoney").val(bt.money.mul(buyMoney, wishRate,2));
            } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                moneyType.get("sellMoney").val(bt.money.div(buyMoney, wishRate,2));
            }
        }
    } else {
        // 期望汇率不存在值
        if (data.baseCurId == buyCurId) {//买入币种为标准
            moneyType.get("wishRate").val(bt.money.div(sellMoney, buyMoney,4));
        } else if (data.baseCurId == sellCurId) {//卖出币种为标准
            moneyType.get("wishRate").val(bt.money.div(buyMoney, sellMoney,4));
        }
    }

}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 买卖方向回调
 */
busDirectionCallback = function (data) {
    if(data.value == '1'){
        moneyType.get('buyMoney').attr('lay-verify', 'required');
        moneyType.get('sellMoney').removeAttr('lay-verify');
    }else{
        moneyType.get('sellMoney').attr('lay-verify', 'required');
        moneyType.get('buyMoney').removeAttr('lay-verify');
    }
    setSellCurId();
    form.renderIcon();
}

function setSellCurId() {
    //交易货币对
    var tranCurValue = $('[name="tranCurId"]').val().replace(/\s+/g, '');
    // 重置期权费币别
    bt.selectType.get('curId').reset();
    if(!tranCurValue){
        return false
    }
    var tranCurSelectedObj = {};
    for (var i = 0; i < currencyPairList.length; i++) {
        if(tranCurValue === currencyPairList[i].id){
            tranCurSelectedObj = currencyPairList[i];
            break
        }
    }
    //涨跌方向 0-看涨 1-看跌
    var updownDirection = '1';
    var proBuyCurId = $('#proBuyCurId').val();
    if(proBuyCurId && proBuyCurId == tranCurSelectedObj['curId1']){
        //看涨
        updownDirection = '0'
        dicts.get('updownDirection').val('0');
    }else{
        //看跌
        dicts.get('updownDirection').val('1');
    }
    // 买卖方向 1-买入 2-卖出
    var busDirectionValue = dicts.get('busDirection').val();
    if((busDirectionValue == '2' && updownDirection == '1') || (busDirectionValue == '1' && updownDirection == '0')){
        $('#buyCurId').val(tranCurSelectedObj['curId1'])
        $('#buyCurId').siblings('div').html(tranCurSelectedObj['englishCode1'])
        $('#sellCurId').val(tranCurSelectedObj['curId2'])
        $('#sellCurId').siblings('div').html(tranCurSelectedObj['englishCode2'])
    }else{
        $('#buyCurId').val(tranCurSelectedObj['curId2'])
        $('#buyCurId').siblings('div').html(tranCurSelectedObj['englishCode2'])
        $('#sellCurId').val(tranCurSelectedObj['curId1'])
        $('#sellCurId').siblings('div').html(tranCurSelectedObj['englishCode1'])
    }
    setQuotationMethod()
}

//设置报价方式
function setQuotationMethod() {
    $('#quotationMethod').val('')
    $('[name="quotationMethodName"]').val('')
    $('#quotationMethodMsg').addClass('layui-hide');

    //交易货币对
    var tranCurValue = $('[name="tranCurId"]').val().replace(/\s+/g, '');
    if(!tranCurValue){
        return false
    }
    var tranCurSelectedObj = {};
    for (var i = 0; i < currencyPairList.length; i++) {
        if(tranCurValue === currencyPairList[i].id){
            tranCurSelectedObj = currencyPairList[i];
            break
        }
    }

    var rateTypeId = $("#rateTypeId").val();
    //获取报价方式
    $.ajax({
        type:'POST',
        url:basePath+"/foreign/fexrateinfo/getByCurId",
        data:{'curId1':tranCurSelectedObj['curId1'],'curId2':tranCurSelectedObj['curId2'],'rateTypeId':rateTypeId},
        async:false,//是否异步
        dataType:'json',
        success:function(result){
            if(result.success){
                if(result.obj){
                    $('#quotationMethod').val(result.obj.id)
                    $('#quotationMethod1').val(result.obj.cur)
                    $('#quotationMethod2').val(result.obj.referCur)
                    $('[name="quotationMethodName"]').val(result.obj.curEnCode + "/" + result.obj.referCurEnCode)
                    return true;
                }
            }else{
                $('#quotationMethodMsg').removeClass('layui-hide');
                var end = result.msg.indexOf("]");
                var length = result.msg.length;
                $("#quotationMethodMsg").text(result.msg.substring(end + 1,length));
                return false;
            }
        }
    })
}

//缓存交易货币对
function cacheCurrencyPairData(data) {
    currencyPairList = data;
    setTimeout(function () {
        setSellCurId()
        setQuotationMethod()
    },50)
    return data
}

//期权费币种加载
function initCurList(data) {
    var selectedTranCurIdOption = $('[name="tranCurId"]').find('option:selected').data();
    var newData = []
    if(selectedTranCurIdOption && data){
        var curid1 = selectedTranCurIdOption.curid1
        var curid2 = selectedTranCurIdOption.curid2;
        bt.each(data,function (i,item) {
            if(item.id === curid1 || item.id === curid2){
                newData.push(item);
            }
        })

    }
    return newData
}

//计算卖出金额
function calculateSellMoney(data) {
    var isPaymentAmount = dicts.get('isPaymentAmount').val();
    var selectedTranCurIdOption = $('[name="tranCurId"]').find('option:selected').data();
    var wishRate = $('#wishRate').val();
    //var sellCurId = $('#sellCurId').val();
    var buyCurId = $('#buyCurId').val();
    if(isPaymentAmount == '1' && wishRate){
        if(buyCurId == selectedTranCurIdOption.curid1){
            //乘
            var result = bt.money.mul(moneyType.get("buyMoney").val(), wishRate,2)
        }else{
            //除
            var result = bt.money.div(moneyType.get("buyMoney").val(), wishRate,2)
        }
        moneyType.get("sellMoney").val(result);
    }
}

//计算买入金额
function calculateBuyMoney(data) {
    var isPaymentAmount = dicts.get('isPaymentAmount').val();
    var selectedTranCurIdOption = $('[name="tranCurId"]').find('option:selected').data();
    var wishRate = $('#wishRate').val();
    //var buyCurId = $('#buyCurId').val();
    var sellCurId = $('#sellCurId').val();
    if(isPaymentAmount == '1' && wishRate){
        if(sellCurId == selectedTranCurIdOption.curid1){
            //乘
            var result = bt.money.mul(moneyType.get("sellMoney").val(), wishRate,2) || 0
        }else{
            //除
            var result = bt.money.mul(moneyType.get("sellMoney").val(), wishRate,2) || 0
        }
        moneyType.get("buyMoney").val(result);
    }
}

function changeUpdownDirection(data) {
    // 买卖方向 1-买入 2-卖出
    var selectedTranCurIdOption = $('[name="tranCurId"]').find('option:selected').data();
    var busDirectionValue = dicts.get('busDirection').val();
    //切换前，金额跟着币种走：按当前买入币种，记下交易货币对左侧金额、右侧金额
    var buyCurId = $('#buyCurId').val();
    var buyMoney = moneyType.get("buyMoney").val();
    var sellMoney = moneyType.get("sellMoney").val();
    var baseMoney = (buyCurId == selectedTranCurIdOption.curid1) ? buyMoney : sellMoney;
    var referMoney = (buyCurId == selectedTranCurIdOption.curid2) ? buyMoney : sellMoney;
    if((busDirectionValue == '2' && data.value == '1') || (busDirectionValue == '1' && data.value == '0')){
        $('#buyCurId').val(selectedTranCurIdOption.curid1)
        $('#buyCurId').siblings('div').html(selectedTranCurIdOption.englishcode1)
        $('#sellCurId').val(selectedTranCurIdOption.curid2)
        $('#sellCurId').siblings('div').html(selectedTranCurIdOption.englishcode2)
    }else{
        $('#buyCurId').val(selectedTranCurIdOption.curid2)
        $('#buyCurId').siblings('div').html(selectedTranCurIdOption.englishcode2)
        $('#sellCurId').val(selectedTranCurIdOption.curid1)
        $('#sellCurId').siblings('div').html(selectedTranCurIdOption.englishcode1)
    }
    //金额跟着币种走：按切换后的买入/卖出币种，取对应的左侧/右侧金额
    var newBuyCurId = $('#buyCurId').val();
    var newSellCurId = $('#sellCurId').val();
    moneyType.get("buyMoney").val(newBuyCurId == selectedTranCurIdOption.curid1 ? baseMoney : referMoney);
    moneyType.get("sellMoney").val(newSellCurId == selectedTranCurIdOption.curid1 ? baseMoney : referMoney);
}