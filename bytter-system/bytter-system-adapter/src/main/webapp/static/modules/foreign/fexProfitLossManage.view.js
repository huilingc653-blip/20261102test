bt.use(['form', 'layedit', 'dicts', 'selectType','radioType','money','moneyType','rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    var selectType = bt.selectType.init();
    bt.moneyType.init();
    bt.rateType.init();

    calcTotalMoney();
    calcLifeCycleMoney();
});

/**
 * 损益统计
 */
function calcTotalMoney() {
    var floatMoneySum = total($('#tableBody').find('.floatMoney'),true);
    var convertFloatMoneySum = total($('#tableBody').find('.convertFloatMoney'),true);
    $('#floatMoneySum').text(floatMoneySum);
    $('#convertFloatMoneySum').text(convertFloatMoneySum);
}

/**
 * 已实现损益统计
 */
function calcLifeCycleMoney() {
    var realMoneySum = total($('#tableBody2').find('.realMoney'),true)
    var convertRealMoneySum = total($('#tableBody2').find('.convertRealMoney'),true)
    $('#realMoneySum').text(realMoneySum);
    $('#convertRealMoneySum').text(convertRealMoneySum);
}

/**
 * 计算净损益
 */
function calcNetProfitAndLoss(money1,money2) {
    var total = 0;
    var busDirection = $('#busDirection').val();
    if(money1 != 0){
        if(busDirection == '1'){
            total = bt.money.sub(money1, money2);
        }else{
            total = bt.money.add(money1, money2);
        }
    }
    return bt.money.format(total)
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
    return bt.money.format(total)
}

/**
 * 查看交易详情
 * @param id
 */
function viewFexTranReq(id) {
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

function closecurrent(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
    return false;
}