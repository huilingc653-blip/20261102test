var moneyType;
var rateType;
var selectType, dicts, corp, dateType, element, dateFun, accountTable;
bt.use(['form', 'corp', 'element', 'btdate', 'select', 'selectType', 'dicts', 'dateType', 'moneyType', 'printType', 'rateType', 'accountTable', 'selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    element = layui.element();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    rateType = bt.rateType.getFun();
    accountTable = bt.accountTable.init();

    var moneyFun = moneyType.getFun();

    // 计息
    form.on('submit(calcInterest)', function (data) {
        calcInterest("interest", bt.lang("table.fcAcc.interestSignOk"));
        return false;
    });

    // 提交
    form.on('submit(calcSubmit)', function (data) {
        calcInterest("submit", bt.lang("common.button.submit"));
        return false;
    });

    // 导出利息清单
    form.on('submit(doExportInterestBill)', function (data) {
        doExportInterestBill();
        return false;
    });

    // 打印记账凭证
    form.on('submit(doBatchPrint)', function (data) {
        doBatchPrint();
        return false;
    });

    // 打印利息通知单
    form.on('submit(doPrintRateNotice)', function (data) {
        doPrintRateNotice();
        return false;
    });
    init();
});
