var moneyType;
var rateType;
var form, selectType, dicts, corp, dateType, element, dateFun, accountTable;
bt.use(['form', 'corp', 'element', 'btdate', 'select', 'selectType', 'dicts', 'dateType', 'moneyType', 'printType', 'rateType', 'selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    element = layui.element();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.getFun();
    // accountTable = bt.accountTable.init();
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
    //内部账号
    var accountTableInit = bt.selectTable.init({
        elem: "#accountTable",
        url: basePath + "/finacial/interest/single/nod/getNoticeAccount",
        urldata: {corpId: ""},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#account",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
            {data: "fcBalance",},
        ],
        clickCallback: function (data) {
            accountCallBack(data);
        }
    });
});

//内部账户回调函数，初始化记账日期和止息日期
function accountCallBack(data) {
    $("#account").val(data.blnctrAcc);
    $("#accountId").val(data.id);
    $("#corpId").val(data.corpId);
    moneyType.get("balance").val(data.fcBalance);
    $.post(basePath + "/finacial/interest/single/dda/tallyDateInit", {
        accountId: data.id,
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

