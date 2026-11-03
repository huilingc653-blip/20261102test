bt.use(['form', 'layedit', 'btUpload', 'printType','moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });
    bt.printType.init();
    bt.moneyType.init();

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val(),
            billNo: $("#expenseId").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#expenseDate").val(),
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

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断费用报销是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断资金计划挂接环节
            var isBudget = $("#expFbsin").val();
            if (isBudget == "0") {
                $genOtherHtml(corpId);
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

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
    $genMsgHtml();
});
