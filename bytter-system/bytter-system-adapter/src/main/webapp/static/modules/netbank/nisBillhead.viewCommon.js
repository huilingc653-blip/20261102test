bt.use(['form', 'layedit', 'moneyType','printType', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });
    bt.printType.init();

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#wishPayday").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            settlType: $("#settlType").val(),
            purpose:  $("#purpose").val(),
            wishPayday: $("#wishPayday").val(),
            payerAcc: $("#payerAcc").val(),
            payeeAccName: $("#payeeAccName").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeBank").val(),
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

    //showIsPayed 显示已付款标识
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        $("#isPayedTitle").show();
        $("#isPayed").show();
    } else {
        $("#isPayedTitle").hide();
        $("#isPayed").hide();
    }

    //是否垫支
    var isAdvance = $("#showIsAdvance").val();
    if (isAdvance == "1") {
        $("#isAdvanceTr").show();
    } else {
        $("#isAdvanceTr").hide();
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var expFbsin = $("#expFbsin").val();
        if (budgetSel == "1" && (expFbsin == "" || expFbsin == "1")) {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                $genOtherHtml(corpId);
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);
});