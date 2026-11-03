/**
 * 资金计划挂接
 */
function renderBudget(arr) {
    //判断支付结算是否挂接资金计划
    //if(moneyTypes.get("repayMoney").val() != 0){//本金不为0加载 资金计划
    var budgetSel = $("#budgetSel").val();
    var onlineSign = $("#onlineSign").val();
    var ar = $("#acrossRepair").val();
    var downSign = $("#downSign").val();
    if (budgetSel == "1") {
        if (downSign != "0") {
            if (onlineSign == "1" || arr == "1" || ar == "1") {
                //判断银行借款是否挂资金计划
                var sendItem = $("#repayItem").val();
                if (sendItem != null && sendItem != "") {
                    $genOtherHtml();
                } else {
                    $("#tagItemDetail").html("");
                }
            } else {
                $("#tagItemDetail").html("");
            }
        } else {
            if (arr == "1" || ar == "1") {
                //判断银行借款是否挂资金计划
                var sendItem = $("#repayItem").val();
                if (sendItem != null && sendItem != "") {
                    $genOtherHtml();
                } else {
                    $("#tagItemDetail").html("");
                }
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }
}

/**
 * 生成公共的属性
 * @return
 */
/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml = function () {
    var itemCode = $('#repayItem').val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateTypes.get("repayDate").val();
    var corpId = $("#corpId").val();
    var url = basePath;
    var repayId = $("#id").val();
    var curId = $("#curId").val();
    var param = null;
    if (repayId == null || repayId == "") {
        url += "/budget/fbsBudgetProcess/getItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    } else {
        url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            billNo: $("#billCode").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    }

    $.ajax({
        url: url,
        method: 'POST',
        data: param,
        async: true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            $("#tagItemDetail").show();
            bt.form().render();
        }
    });
};

//批量线下还款
function loanRepayBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    // 所选借款必须是同一种币种
    var curId = data[0].curId;
    for (var i = 0; i < data.length; i++) {
        if (curId != data[i].curId) {
            layer.alert(bt.lang('cms.message.loanrepay.currencySame'), {icon: 7});
            return;
        }
    }
    debugger

    // 确定将所选借款最近一期未还的还款计划进行归还吗？
    parent.layer.confirm(bt.lang('cms.message.loanrepay.batchSubmit'), function () {
        var loanRepayBatchObj = {};
        var loanRepayBatchList = [];

        var rowData = data.splice(0, len);
        for (var i = 0; i < rowData.length; i++) {
            loanRepayBatchObj = {
                corpId: rowData[i].corpId,
                sendCode: rowData[i].sendCode,
                createDate: rowData[i].createDate,
                repayDate: rowData[i].repayDate,   //还款日期
                repayMoney: rowData[i].repayMoney,  //还款金额
                repayAccrual: rowData[i].revertAccrual,// 还款利息
                provideInterestId: rowData[i].provideInterestId,  //借款利息预计明细ID

            };
            loanRepayBatchList.push(loanRepayBatchObj);
        }

        bt.loading.show();//显示加载中
        bt.jquery.ajax({
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                "loanRepayBatchList": loanRepayBatchList
            }),
            asycn: false,
            url: basePath + "/cms/provide/loanrepay/batchSubmit",
            success: function (result) {
                bt.loading.hide(); //关闭加载中
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            dataType: 'json'
        });
    });
}
