var table;var $genOtherHtml;
bt.use(['form', 'dateType', 'dicts', 'date'], function () {
    var $ = bt.jquery,form = bt.form();
    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml= function () {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: $("#corpId").val(),
            billId: $("#id").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#processDate").val(),
            type: 'select',
            date: (new Date()).getTime(),
            detailControl: true,
            curId: $("#curId").val() ? $("#curId").val() : "",
            settlType: 0,
            purpose:  "",
            wishPayday: $("#wishPayday").val(),
            payerAcc: $("#payerAcc").val() ? $("#payerAcc").val() : "",
            payeeAccName: $("#payeeAccName").val()? $("#payeeAccName").val() : "",
            payeeAcc: $("#payeeAcc").val()? $("#payeeAcc").val() : "",
            payeeBank: $("#payeeBank").val()? $("#payeeBank").val() : "",
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }
    $genOtherHtml();
});


