bt.use(['jquery', "form", 'bttable', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var index = 1;
    moneyFun = bt.moneyType.getFun();
    moneyType = bt.moneyType.init();

    /**
     * 保存
     */
    form.on('submit(save)', function (data) {
        var sumTotal = parseFloat($("#sumTotal").val());
        var repayDate = $("#repayDate").val();
        var repayMoney = $("#repayMoney").val();
        data.field.repayMoney = repayMoney;
        data.field.repayDate = repayDate;
        var listSize = $("#listSize").val();
        var trueSumTotal = 0;
        for (var i = 0; i < listSize; i++) {
            trueSumTotal = new Number(parseFloat(trueSumTotal)+parseFloat(moneyType.get("repayMoney_"+i).val())).toFixed(2);
        }
        if (sumTotal!=trueSumTotal){
            parent.layer.msg(bt.lang("cms.provide.loan.sumTotalError"), {icon: 2});
            return false;
        }
        $.post(basePath + "/cms/provide/cmsprovideinterest/saveTestList", data.field, function (result) {
            bt.loading.hide();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var repayDate = $('#repayDate').val()
                var repayMoney = $('#repayMoney').val()
                var payInterest = $('#payInterest').val()
                var provideInterestId = $('#provideInterestId').val()
                var webObj = bt.navtab.getParentDom();
                webObj.setSelectInterestData(repayDate, repayMoney, payInterest, provideInterestId);
                bt.navtab.closeCurrent();
            }
        }, 'JSON');
        return false;
    });
    form.on('radio(detailtype)', function (data) {
        changeDetailType(data)
    })
});

function merge(tableId) {
    var tr = document.getElementById(tableId);
    for (var i = 1; i < tr.rows.length; i++) {                //表示数据内容的第二行
        if (tr.rows[i].cells[col].innerHTML == tr.rows[i - 1].cells[col].innerHTML) {//col代表列
            t = i - 1;
            while (tr.rows[i].cells[col].innerHTML == tr.rows[t].cells[col].innerHTML) {
                tr.rows[i].cells[col].style.display = "none";
                if (tr.rows[t].cells[col].rowSpan <= (i - t)) {
                    tr.rows[t].cells[col].rowSpan += 1;      //设置前一行的rowspan+1
                }
                i++;
            }
        }
    }
}

function interest(repayDate, repayMoney, payInterest, id) {
    var webObj = bt.navtab.getParentDom();
    webObj.setSelectInterestData(repayDate, repayMoney, payInterest, id);
    bt.navtab.closeCurrent();
}

function changeDetailType(obj) {
    var detailtype0 = obj.value;
    if ("all" == detailtype0) {
        $("#alldetailtable").attr("style", "display:block;");
        $("#currentdetailtable").attr("style", "display:none;");
    } else {
        $("#currentdetailtable").attr("style", "display:block;");
        $("#alldetailtable").attr("style", "display:none;");
    }
}
function editSumRepayMoney(){
    var listSize = $("#listSize").val();
    var trueSumTotal = 0;
    for (var i = 0; i < listSize; i++) {
        trueSumTotal = new Number(parseFloat(trueSumTotal)+parseFloat(moneyType.get("repayMoney_"+i).val())).toFixed(2);
    }
    $("#sumRepayMoney").html(trueSumTotal);
}
function editPayInterest(){
    var listSize = $("#listSize").val();
    var trueSumTotal = 0;
    for (var i = 0; i < listSize; i++) {
        trueSumTotal = new Number(parseFloat(trueSumTotal)+parseFloat(moneyType.get("payInterest_"+i).val())).toFixed(2);
    }
    $("#sumPayInterest").html(trueSumTotal);
}

