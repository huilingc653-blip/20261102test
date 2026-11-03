bt.use(['form', 'layedit', 'moneyType', 'printType','element'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.printType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

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
            processDate: $("#settleDate").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            wishPayday: $("#settleDate").val(),
            payerAcc: $("#srcBankAcc").val(),
            payeeAccName: $("#targetAccName").val(),
            payeeAcc: $("#targetAcc").val(),
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
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        //收付款方币种不一致
        var curId = $("#curId").val();
        var targetAccCurId = $("#targetAccCurId").val();
        if (budgetSel == "1" && curId == targetAccCurId) {
            $genOtherHtml(corpId);
        } else {
            $("#tagItemDetail").html("");
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/foreign/fexmessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
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

    /**
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreq/viewCustInfo/" + $('#id').val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#custMsg").html("");
                $("#custMsg").html(json);
                form.render();
            }
        });
    }
    $genCustHtml();
});