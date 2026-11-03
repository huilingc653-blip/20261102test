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

    // 内部账号  03-放款[内部贷款]
    var accountTableInit = bt.selectTable.init({
        elem: "#accountTable",
        url: basePath+"/finacial/interest/single/loan/getInterestAccount",
        urldata: {
            // accType: "03",
            accType: $("#accType").val(),
            resource: "finacial:interest:loan:hand"
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#account",
        paging: true,
        columns: [
            {data:"bankAcc"},
            {data:"accName"},
            {data:"corpName"},
            {data:"curName"}
        ],
        clickCallback: function (data) {
            // 显示账号余额
            var fcBalance = data.fcBalance;
            $("#showAccountBalanceElement").html(moneyFun.format(fcBalance, {separator: ",", decimal: 2}));
            accountCallBack(data);
        }
    });

    setMoneyRequire = function(){
        var payWay = dicts.get("payWay").val();//还款方式
        if ("4" != payWay) {
            moneyType.get("money").attr("lay-verify", "false");
        } else {
            moneyType.get("money").attr("lay-verify", "true");
        }
    }
});


//内部账户回调函数，初始化记账日期和止息日期
function accountCallBack(data) {
    $("#id").val("")
    $("#account").val(data.bankAcc);
    $("#accountId").val(data.bankAccId);
    $("#corpId").val(data.corpId);
    moneyType.get("money").attr("data-max", data.fcBalance);
    $.post(basePath + "/finacial/interest/single/loan/tallyDateInit", {
        accountId: data.bankAccId,
        opType: $("#opType").val()
    }, function (result) {
        if (typeof result.success == "boolean" && result.success == false) {
            layer.alert(result.msg);
            return;
        }
        // var tallyDate = result.tallyDate;//记账日期
        // var endDate = result.blnInstDate;//止息日期
        //记账日期应不小于日结日期
        dateType.get("tallyDate").attr("data-min", dateFun.format(result.tallyDate, 'yyyy-MM-dd'));
        dateType.get("tallyDate").val(dateFun.format(result.tallyDate, 'yyyy-MM-dd'));
        //止息日期应小于等于日结后日期
        dateType.get("endDate").attr("data-max", dateFun.format(result.tallyDate, 'yyyy-MM-dd'));
        dateType.get("endDate").val(dateFun.format(result.blnInstDate, 'yyyy-MM-dd'));
        //重新渲染日期控件
        dateType.render();
        $("#netId").val(result.netId);//网点ID
        $("#netName").val(result.netName);//网点名称
        $("#curId").val(result.curId);//币别ID
        $("#curName").val(result.curName);//币别名称
        $("#poolSign").val(result.poolSign);// 资金池标识 0-非资金池；1-资金池
        $("#interestSign").val(result.interestSign);//计息标志
        init();
    }, 'JSON');
}
