bt.use(['form', 'capNumber','btUpload', 'printType'], function () {
    var $ = bt.jquery, form = bt.form();
    // 金额大写
    bt.capNumber.init({
        elem: '#money',
        linkElemDiv: "#moneyUp"
    });

    /**
     * 挂接资金计划
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#askItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#wishPayday").val(),
            type: 'select',
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

    bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete:false,
        hasUpload:false,
        gid: $("#attachmentId").val()
    });

    bt.printType.init();

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/netbank/nismessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    renderBudget($("#corpId").val());
    $genMsgHtml();
});

/**
 * 资金计划挂接
 */
function renderBudget(corpId) {
    //判断支付结算是否挂接资金计划
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        $genOtherHtml(corpId);
    }
}