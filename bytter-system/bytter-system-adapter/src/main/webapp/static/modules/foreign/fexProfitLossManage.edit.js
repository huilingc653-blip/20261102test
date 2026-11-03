bt.use(['form', 'layedit', 'dicts', 'selectType','radioType','money','moneyType','rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    var selectType = bt.selectType.init();
    bt.moneyType.init();
    bt.rateType.init();

    calcTotalMoney(true);
    //calcLifeCycleMoney(true);

    form.on("submit(submit)", function (data) {
        bt.loading.show();
        //getTableFields(data.field);
        $.post(basePath + "/foreign/fexProfit/lossManage/modify", data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.refreshParent();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                }
            }
            bt.loading.hide();
        }, 'JSON');
    });

});

/**
 * 损益统计
 */
function calcTotalMoney(isInit) {

    var floatMoneySum = total($('#tableBody').find('.floatMoney input[type="hidden"]'))
    var convertFloatMoneySum = total($('#tableBody').find('.convertFloatMoney input[type="hidden"]'))
    $('#floatMoneySum').text(bt.money.format(floatMoneySum));
    $('#convertFloatMoneySum').text(bt.money.format(convertFloatMoneySum));
    var tranType = $("#tranType").val();
    if(tranType == '2'){
        var id = $("#id").val();
        var profitLossCur = bt.selectType.get('curId').val();
        $.ajax({
            type:'POST',
            url:basePath+"/foreign/fexProfit/lossManage/calculatePremiumMoney",
            data:{id:id,curId:profitLossCur},
            async:false,//是否异步
            //dataType:'json',
            success:function(result){
              /*  var json = JSON.parse(result);*/
                if(result.success){
                    if(result.obj){
                        $('#premiumMoney').val(result.obj.premiumMoney);
                        $('#convertMoney').val(result.obj.convertMoney);
                    }
                }
            }
        })

        if(!isInit){
            var total1 = calcNetProfitAndLoss(floatMoneySum,$('#premiumMoney').val())
            var total2 = calcNetProfitAndLoss(convertFloatMoneySum,$('#convertMoney').val())
            $('#netProfitAndLossFloatMoney').text(bt.money.format(total1));
            $('#netProfitAndLossConvertFloatMoney').text(bt.money.format(total2));
            $("#netFloatMoney").val(total1);
            $("#netConvertFloatMoney").val(total2);
        }

    }

}

/**
 * 已实现损益统计
 */
function calcLifeCycleMoney(isInit) {
    var realMoneySum = total($('#tableBody2').find('.realMoney'),true)
    var convertRealMoneySum = total($('#tableBody2').find('.convertRealMoney'),true)
    $('#realMoneySum').text(bt.money.format(realMoneySum));
    $('#convertRealMoneySum').text(bt.money.format(convertRealMoneySum));
    var tranType = $("#tranType").val();
    if(tranType == '2'){
        if(!isInit){
            var total1 = calcNetProfitAndLoss(realMoneySum,$('#premiumMoney').val())
            var total2 = calcNetProfitAndLoss(convertRealMoneySum,$('#convertMoney').val())
            $('#netProfitAndLossRealMoneyTotal').text(bt.money.format(total1));
            $('#netProfitAndLossConvertRealMoneyTotalValue').text(bt.money.format(total2));
            $("#netRealMoneyTotal").val(total1);
            $("#netConvertRealMoneyTotal").val(total2);
        }
    }

}

/**
 * 计算净损益
 */
function calcNetProfitAndLoss(money1,money2) {
    var total = 0;
    var busDirection = $('#busDirection').val();
    if(busDirection == '1'){
        total = bt.money.sub(money1, money2);
    }else{
        total = bt.money.add(money1, money2);
    }
    return total
}

function total(inputs,isDiv) {
    var total = 0;
    for(var i = 0;i < inputs.length; i++){
        if(isDiv){
           var moneyValue = $(inputs[i]).data('value')
        }else{
            var moneyValue = $(inputs[i]).val()
        }
        total = bt.money.add(total, moneyValue);
    }
    return total
}

function getTableFields(obj) {
    var trs = $('#tableBody').find('tr');
    for (var i = 0; i < trs.length; i++) {
        var key1 = 'fexProfitLossManageList[' + i + '].id';
        var key2 = 'fexProfitLossManageList[' + i + '].floatMoney';
        var key3 = 'fexProfitLossManageList[' + i + '].convertFloatMoney';
        var key4 = 'fexProfitLossManageList[' + i + '].referenceInterestRate';
        obj[key1] = $(trs[i]).data('key');
        obj[key2] = $(trs[i]).find('.floatMoney input[type="hidden"]').val();
        obj[key3] = $(trs[i]).find('.convertFloatMoney input[type="hidden"]').val();
        obj[key4] = $(trs[i]).find('.referRate input[type="hidden"]').val();
    }
}

/**
 * 查看交易详情
 * @param id
 */
function viewFexTranReq(id) {
    var tranType = $('#tranType').val();
    if(tranType == '2'){
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + id);
            return;
        }
        layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fextranreq/view/" + id
        });
    }else{
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingReq/view/" + tranType +"/" + id);
            return;
        }
        layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexTradingReq/view/" + tranType +"/" + id
        });
    }
}

/**
 * 查看生命周期详情
 * @param id
 */
function viewFexTranLifeCycle(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingLifecycle/view/" + id);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingLifecycle/view/" + id
    });
}

//损益币种初始化
function initCurList(data) {
    var curid1 = $("#tranCurPairBaseId").val();
    var curid2 = $("#tranCurPairReferId").val();
    var newData = []
    if(curid1 && curid1){
        bt.each(data,function (i,item) {
            if(item.id === curid1 || item.id === curid2){
                newData.push(item);
            }
        })
    }
    return newData
}

function profitLossCurCallback(){
    var corpCurId = $("#corpCurId").val();
    var profitLossCur = bt.selectType.get("curId").val();
    if(corpCurId == profitLossCur){
        $('.convertCur-control').addClass('layui-hide')
        $('[data-biz-name="convertRate"]').removeAttr('lay-verify');
    }else{
        $('.convertCur-control').removeClass('layui-hide')
        $('[data-biz-name="convertRate"]').attr('lay-verify', 'required|number2');
    }
    calculatePositionProfit();
}

/**
 * 计算浮动损益
 */
function calculatePositionProfit(){
    var id = $("#id").val();
    var referenceInterestRate = $('#tableBody').find('.referRate input[type="hidden"]').val();
    var profitLossCur = bt.selectType.get('curId').val();
    $.ajax({
        type:'POST',
        url:basePath+"/foreign/fexProfit/lossManage/calculatePositionProfit",
        data:{id:id,referenceInterestRate:referenceInterestRate,curId:profitLossCur},
        async:false,//是否异步
        //dataType:'json',
        success:function(result){
            var json = JSON.parse(result);
            if(json.success){
                if(json.obj){
                    bt.moneyType.get("floatMoney").val(json.obj.floatMoney);
                    bt.moneyType.get("convertFloatMoney").val(json.obj.convertFloatMoney);
                    if(json.obj.convertRate){
                        bt.moneyType.get("convertRate").val(json.obj.convertRate);
                    }
                    if(json.obj.convertDate){
                        $('#convertDateSp').text(bt.date.format(json.obj.convertDate, "yyyy-mm-dd hh:mm:ss"));
                        $('#convertDate').val(bt.date.format(json.obj.convertDate, "yyyy-mm-dd hh:mm:ss"));
                    }
                    if(json.obj.convertCurEnCode){
                        $('#convertCurEnCodeSp').text(json.obj.convertCurEnCode);
                        $('#convertCurEnCode').val(json.obj.convertCurEnCode);
                        $('#convertCurId').val(json.obj.convertCurId);
                        $('#convertCurReferId').val(json.obj.convertCurReferId);
                    }
                    $('#premiumMoney').val(json.obj.premiumMoney);
                    $('#convertMoney').val(json.obj.convertMoney);
                    calcTotalMoney();
                    //calcLifeCycleMoney();
                }
            }
        }
    })

}

/**
 * 折本位币汇率变更回调
 */
function convertRateCallback(){
    //折本位币汇率
    var convertRate = bt.moneyType.get("convertRate").val();
    //损益币种
    var curId = bt.selectType.get("curId").val();
    //单位币种
    var corpCurId = $('#corpCurId').val();
    //折本位币标准币种
    var convertCurId = $('#convertCurId').val();
    //折本位币报价币种
    var convertCurReferId = $('#convertCurReferId').val();
    //浮动损益
    var floatMoney = bt.moneyType.get("floatMoney").val();
    //折本位币浮动损益
    var convertFloatMoney = 0;
    if(curId != corpCurId){
        if(curId == convertCurId){
            convertFloatMoney = bt.money.mul(floatMoney,convertRate);
        }else{
            convertFloatMoney = bt.money.div(floatMoney,convertRate);
        }
    }else{
        convertFloatMoney = floatMoney;
    }
    bt.moneyType.get("convertFloatMoney").val(convertFloatMoney);

    calcTotalMoney();
}

function convertFloatMoneyCallBack(){
    calcTotalMoney();
}
