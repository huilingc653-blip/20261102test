bt.use(['form', 'capNumber', 'btUpload', 'printType'], function () {
    var $ = bt.jquery, form = bt.form();
    // 金额大写
    bt.capNumber.init({
        elem: '#money',
        linkElemDiv: "#moneyUp"
    });
    bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: $("#corpId").val(),
            itemCode: $("#downItem").val(),
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

    //判断支付结算是否挂接资金计划
    // zuoy 2018-05-31 暂不挂资金计划
    // var budgetSel = $("#budgetSel").val();
    // if (budgetSel == "1") {
    //     $genOtherHtml();
    // }

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
    $genMsgHtml();
});
