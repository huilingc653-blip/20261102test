var moneyType;
var rateType;
var selectType, dicts, corp, dateType, element, dateFun, accountTable;
bt.use(['form', 'corp', 'element', 'btdate', 'select', 'selectType', 'dicts', 'dateType', 'moneyType', 'printType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    element = layui.element();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.getFun();
    form.on('submit(calcInterest)', function (data) {
        calcInterest("interest", bt.lang("table.fcAcc.interestSignOk"));//计息
        return false;
    });

    form.on('submit(calcSubmit)', function (data) {
        calcInterest("submit", bt.lang("common.button.submit"));//提交
        return false;
    });

    form.on('submit(doBatchPrint)', function (data) {
        doBatchPrint();//打印记账凭证
        return false;
    });

    form.on('submit(doPrintRateNotice)', function (data) {
        doPrintRateNotice();//打印利息通知单
        return false;
    });
    init();
});
