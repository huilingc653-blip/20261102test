var table;var $genOtherHtml;
bt.use(['form', 'dateType', 'dicts', 'date', 'moneyType'], function () {
    var $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
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
            payeeAcc: $("#payeeAcc").val()? $("#payeeAcc").val() : "",
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


