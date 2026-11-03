bt.use(['form', 'capNumber', 'printType'], function () {
    var $ = bt.jquery, form = bt.form();
    // 金额大写
    bt.capNumber.init({
        elem: '#money',
        linkElemDiv: "#moneyUp"
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
    bt.printType.init();
});