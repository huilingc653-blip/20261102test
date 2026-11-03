var $, form, dateType;
bt.use(['form', 'datatable', 'moneyType', 'corp', 'select', 'dateType', 'dicts', 'rateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    rateType = bt.rateType.init();
    bt.moneyType.init();

    dateType = bt.dateType.init();

    // 隐藏加载动画
    parent.bt.loading.hide();

    // 监听查询
    form.on('submit(search)', function (data) {
        var action = basePath + "/bill/accebill/abbillQuery/listTargetProportionAndQuotaUsageStatistics";
        $("#searchForm").attr("action", action);
        $("#searchForm").attr("method", "get");
        // 显示加载动画
        parent.bt.loading.show();
        $("#searchForm").submit();
    });

    //监听导出
    form.on('submit(excel)', function (data) {
        if (!checkTables()) {
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }
        var url = basePath + "/bill/accebill/abbillQuery/exportAcUsageRatioStatistics";
        DownLoadFile({url: url, data: data.field, method: 'post'});
        return false;
    });
});

/**表格不为空验证*/
function checkTables() {
    var flag = true;
    //获取所有tr
    var trs = $("#appTable").find("tr");
    if (trs.length <= 3) {
        flag = false;
    }
    return flag;
}

function DownLoadFile(options) {
    var config = $.extend(true, {method: 'post'}, options);
    var _iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
    var _form = $('<form target="_self" method="post" />');
    _form.attr('action', config.url);
    for (var key in config.data) {
        _form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    _iframe.append(_form);
    $(document.body).append(_iframe);
    _form[0].submit();
    _iframe.remove();
    return false;
}

// 重置
function resetAcUsageRatioStatistics() {
    dateType.get("ym").val($("#defaultYm").val());
}