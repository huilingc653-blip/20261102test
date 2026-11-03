var moneyType, moneyFun
bt.use(['form','selectType','dicts','moneyType','dateType'],function () {
    moneyType = bt.moneyType.init()
    moneyFun = moneyType.getFun()
    bt.dateType.init()
    var form = bt.form()


    form.on('submit(submit)',function (data) {
        bt.loading.show();
        var action = '/chowmatistic/choproductNetVal/modify';
        $.post(basePath + action,data.field, function (result) {
            parent.layer.alert(result.msg,{icon:result.success?1:2},function () {
                parent.layer.closeAll();
                if (result.success){
                    bt.loading.hide();
                    if (bt.navtab){
                        bt.navtab.refreshCurrent();
                    } else{
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
})

// 日净值回调函数
function dailyNetValCallback(data) {
    if (data == null) {
        return;
    }
    // 是否有赎回
    var isRedeem = $('#isRedeem').val() !== '';
    // 上日日净值
    var lastDailyNetVal = Number($('#lastDailyNetVal').val());
    // 上日实际收益
    var lastActualIncome = Number($('#lastActualIncome').val());
    // 当日日净值
    var dailyNetVal = Number(moneyType.get('dailyNetVal').val());
    // 购买份额 （该购买份额已减去了赎回份额）
    var buyPortion = Number(moneyType.get('buyPortion').val());
    // 购买金额
    var buyMoney = Number(moneyType.get('buyMoney').val());
    // 当日每万份收益=（本期日净值-上期日净值）*10000
    var tenThousandIncome = (dailyNetVal - lastDailyNetVal) * 10000
    // 当日收益=（本期日净值-上期日净值）*购买份额
    var dayIncome = (dailyNetVal - lastDailyNetVal) * buyPortion
    // 实际收益
    var actualIncome
    // 余额
    var bal
    // 是否有赎回
    if (!isRedeem) {
        // 实际收益=上日实际收益+本日当日收益
        actualIncome = dayIncome + lastActualIncome;
        // 余额=购买金额+实际收益
        bal = buyMoney + actualIncome
    } else {
        // 购买日净值
        var buyDailyNetVal = Number($('#buyDailyNetVal').val())
        // 赎回份额
        var redeemPortion = Number($('#redeemPortion').val())
        // 上日余额
        var lastBal = Number($('#lastBal').val())
        // 实际收益=上日实际收益-（上日净值-购买日净值）*赎回份额+当日收益
        actualIncome = lastActualIncome - (lastDailyNetVal - buyDailyNetVal) * redeemPortion + dayIncome
        // 余额=上日余额-（赎回份额*上日净值）+当日收益
        bal = lastBal - (redeemPortion * lastDailyNetVal) + dayIncome
    }
    moneyType.get('tenThousandIncome').val(moneyFun.format(tenThousandIncome, {decimal: 2}))
    moneyType.get('dayIncome').val(moneyFun.format(dayIncome, {decimal: 2}))
    moneyType.get('bal').val(moneyFun.format(bal, {decimal: 2}))
    moneyType.get('actualIncome').val(moneyFun.format(actualIncome, {decimal: 2}))
    moneyType.render()
}

// 每万份收益回调函数
function tenThousandIncomeCallback(data) {
    if (data == null) {
        return;
    }

    // 是否有赎回
    var isRedeem = $('#isRedeem').val() !== '';
    // 上日日净值
    var lastDailyNetVal = Number($('#lastDailyNetVal').val());
    // 上日实际收益
    var lastActualIncome = Number($('#lastActualIncome').val());
    // 购买份额 （该购买份额已减去了赎回份额）
    var buyPortion = Number(moneyType.get('buyPortion').val());
    // 购买金额
    var buyMoney = Number(moneyType.get('buyMoney').val());
    // 当日每万份收益
    var tenThousandIncome = Number(moneyType.get('tenThousandIncome').val());
    // 当日日净值
    var dailyNetVal = lastDailyNetVal + (tenThousandIncome / 10000)
    // 当日收益=（本期日净值-上期日净值）*购买份额
    var dayIncome = (dailyNetVal - lastDailyNetVal) * buyPortion
    // 实际收益
    var actualIncome
    // 余额
    var bal
    // 是否有赎回
    if (!isRedeem) {
        // 实际收益=上日实际收益+本日当日收益
        actualIncome = dayIncome + lastActualIncome;
        // 余额=购买金额+实际收益
        bal = buyMoney + actualIncome
    } else {
        // 购买日净值
        var buyDailyNetVal = Number($('#buyDailyNetVal').val())
        // 赎回份额
        var redeemPortion = Number($('#redeemPortion').val())
        // 上日余额
        var lastBal = Number($('#lastBal').val())
        // 实际收益=上日实际收益-（上日净值-购买日净值）*赎回份额+当日收益
        actualIncome = lastActualIncome - (lastDailyNetVal - buyDailyNetVal) * redeemPortion + dayIncome
        // 余额=上日余额-（赎回份额*上日净值）+当日收益
        bal = lastBal - (redeemPortion * lastDailyNetVal) + dayIncome
    }
    moneyType.get('dailyNetVal').val(moneyFun.format(dailyNetVal, {decimal: 6}))
    moneyType.get('dayIncome').val(moneyFun.format(dayIncome, {decimal: 2}))
    moneyType.get('bal').val(moneyFun.format(bal, {decimal: 2}))
    moneyType.get('actualIncome').val(moneyFun.format(actualIncome, {decimal: 2}))
    moneyType.render()
}