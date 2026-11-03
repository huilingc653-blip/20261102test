var $;
var form;
var corpInit;
var dateType;
var selectType;
var dicts;
var moneyType;
var moneyFun;
var mulselect;
bt.use(['form', 'bttable', 'btdate', 'datatable', 'corp', 'moneyType', 'laypage', 'dateType', 'selectType', 'printType', 'date', 'dicts', 'jquery', 'element', 'selectTable', 'page', 'freezeTable', 'mulselect'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    bt.printType.init();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    corpInit = bt.corp.init();
    mulselect = bt.mulselect.init();

    //监听提交
    form.on('submit(search)', function (data) {
        search();
    });
    //导出表格
    form.on('submit(excel)', function (data) {
        var url = basePath + "/cms/report/ratbkstatistics/excel";
        var corpId = corpInit.get("corpId").val();

        var bankTypeId = "";
        var bankInfos = mulselect.get("bankTypeId").val();
        for (var i = 0; i < bankInfos.length; i++) {
            if (bankInfos[i] && bankInfos[i].id !== undefined && bankInfos[i].id != null && bankInfos[i].id !== "") {
                bankTypeId = bankTypeId + bankInfos[i].id + ",";
            }
        }
        var ratMode = dicts.get("ratMode").val();
        var curCode = selectType.get("curCode").val();
        var moneyType = dicts.get("moneyType").val();
        var status = dicts.get("status").val();
        var beginDate = dateType.get("beginDate").val();
        var endDate = dateType.get("endDate").val();
        var beginDate2 = dateType.get("beginDate2").val();
        var endDate2 = dateType.get("endDate2").val();
        var formData = {
            ratMode: $.trim(ratMode),
            corpId: corpId,
            bankTypeId: bankTypeId,
            curCode: curCode,
            moneyType: moneyType,
            status: status,
            beginDate: beginDate,
            endDate: endDate,
            beginDate2: beginDate2,
            endDate2: endDate2,
        }
        if (checkTables()) {
            DownLoadFile({url: url, data: formData, method: 'post'});
            return false;
        } else {
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }
    });
});

function search() {
    var action = basePath + "/cms/report/ratbkstatistics/queryBkAmountRation";
    $("#searchForm").attr("action", action);
    $("#searchForm").attr("method", "post");
    $("#searchForm").submit();
    // location.href = basePath + "/cms/report/ratbkstatistics/queryBkAmountRation?"+getParam();
}

// 重置
function amountStaticsReset() {
    corpInit.get("corpId").val("");
    mulselect.get("bankTypeId").val({value: ""});
    dicts.get("ratMode").val("");
    selectType.get("curCode").val("");
    dicts.get("moneyType").val("1");
    dicts.get("status").val("");
    dateType.get("beginDate").val("");
    dateType.get("endDate").val("");
    dateType.get("beginDate2").val("");
    dateType.get("endDate2").val("");
    form.render();
}

/**
 * 查询历史报表
 */
function searchHis() {
    layer.open({
        title: '选择报表日期',
        type: 1,
        area: ["25%", "25%"],
        btn: [bt.lang('common.button.search'), bt.lang('common.button.cancel')],
        btnAlign: "c",
        content: $("#reportDate1"),
        yes: function (index, layero) {
            var reportDate = bt.dateType.get("reportDate").val();
            if (!reportDate) {
                layer.alert('请选择报表日期', {icon: 2});
            } else {
                var ratMode = $.trim(dicts.get("ratMode").val());
                var curCode = selectType.get("curCode").val();
                var moneyType = $.trim(dicts.get("moneyType").val());
                var status = $.trim(dicts.get("status").val());
                var beginDate = dateType.get("beginDate").val();
                var endDate = dateType.get("endDate").val();
                var beginDate2 = dateType.get("beginDate2").val();
                var endDate2 = dateType.get("endDate2").val();

                var params = "reportDate=" + reportDate + "&ratMode=" + ratMode + "&curCode=" + curCode + "&moneyType=" + moneyType;
                params += "&status=" + status + "&beginDate=" + beginDate + "&endDate=" + endDate + "&beginDate2=" + beginDate2 + "&endDate2=" + endDate2;
                var href = "/cms/report/ratBkStaticHis/listTemp?" + params;
                if (bt.navtab) {
                    //授信额度综合统计历史报表
                    bt.navtab.addNavtab('授信额度综合统计历史报表', basePath + href);
                }
            }
        }
    });
    return false;
}

//查看授信占用情况
function viewRatioUse(curCode, corpId, ratCode, bankCode, beginDate, endDate, flagSign) {
    bt.navtab.addNavtab(bt.lang('ration.ratUration.view'), basePath + "/cms/rat/raturation/viewRatioUse?useType=2"
        + "&curCode=" + curCode + "&corpId=" + corpId + "&ratCode=" + ratCode);
    return;
}

//查看指定单位,指定银行指定币别的授信信息
function viewItemRatio(curCode, corpId, ratCode, bankCode, beginDate, endDate) {
    bt.navtab.addNavtab(bt.lang('ration.bkrationdis.detailed'), basePath + "/cms/report/ratbkstatistics/viewItemRatio?curCode=" + curCode +
        "&corpId=" + corpId + "&ratCode=" + ratCode + "&bankCode=" + bankCode + "&beginDate=" + beginDate + "&endDate=" + endDate);
    return;
}

//查看授信的在审占用情况
function viewApproveRatio(curCode, corpId, ratCode, bankCode, begin, endDate) {
    bt.navtab.addNavtab(bt.lang('ration.ratUration.view'), basePath + "/cms/rat/raturation/viewRatioUse?useType=1"
        + "&curCode=" + curCode + "&corpId=" + corpId + "&ratCode=" + ratCode);
    return;
}

//导出表格
var DownLoadFile = function (options) {
    var config = $.extend(true, {method: 'post'}, options);
    var $iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
    var $form = $('<form target="_self" method="post" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
    return false;
}

/**表格不为空验证*/
var checkTables = function () {
    var isreturn = true;
    //获取所有tr
    var trs = $("#appTable").find("tr");
    if (trs.length <= 1) {
        isreturn = false;
    }
    return isreturn;
}
