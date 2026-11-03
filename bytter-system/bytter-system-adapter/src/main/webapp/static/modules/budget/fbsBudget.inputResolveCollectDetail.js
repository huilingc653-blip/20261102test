bt.use(['form', 'layedit', 'btdate', 'moneyType', 'treeTable', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.treeTable.init({elem: "#appTable", column: 1});
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        //校验上报/追加金额必须填写
        /*var bMoneyTotal = 0;
        var bMoneyName = "bMoney";
        if ($("#target").val() == "super") {
            bMoneyName = "btempMoney";
        }
        if ($('form').find('input[data-name="' + bMoneyName + '"]').length) {
            $('form').find('input[data-name="' + bMoneyName + '"]').each(function () {
                bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
            });
        } else {
            //月滚动
            $('form').find('input[data-name="bMoney1"]').each(function () {
                bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
            });
        }
        if (bMoneyTotal == 0) {
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.collectcorp.num.null'), {icon: 7});
            return false;
        }*/

        var isCollect = false;
        $.each($("input[name='statusHid']"),function(index,date){
            if(date.value == 81 || date.value == 100){
                isCollect = true;
            }
        });
        if(!isCollect){
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.collectcorp.num.null'), {icon: 7});
            return false;
        }

        var saveMethod = $("#saveMethod").val();
        var action = "/budget/fbsbudget/inputSave";
        if (saveMethod != "") {
            action = "/budget/fbsbudget/" + saveMethod;
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.getParentDom().location.reload();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });
    var bMoneyDiv = $('div[name="bMoneyDiv"]');
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/budget/fbsmessage/list/" + $('#id').val() + "/" + $("#busName").val() + "?timestamp=" + new Date().getTime();
        if ($("#target").val() == "super") {
            url = basePath + "/budget/fbsmessage/list/" + $('#superaddId').val() + "/" + $("#busName").val() + "?timestamp=" + new Date().getTime();
        }
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
