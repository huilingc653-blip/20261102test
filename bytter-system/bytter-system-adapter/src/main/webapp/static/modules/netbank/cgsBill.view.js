bt.use(['form', 'moneyType', 'btUpload', 'printType'], function () {
    $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var num = $("#lower").text()
    var toUp = moneyType.getFun().toChinese(num);
    $("#upper").text(toUp);
    var moneyFun = bt.moneyType.getFun();
    $("#lower").text(moneyFun.format(num));
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });
    bt.printType.init();
    //初始化查找支付结算参数中的手续费
    $.ajax({
        url: basePath + "/netbank/cgsbill?getChargeRateTal",
        method: 'POST',
        data: '',
        dataType: 'json',
        success: function (data) {
            if (data.obj) {
                $("#chargeRate").html(data.obj);
            }
        }
    });

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