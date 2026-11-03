bt.use(['form', 'layedit', 'btdate', 'moneyType', 'date','treeTable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    //监听汇总
    form.on('submit(collect)', function (data) {
        bt.loading.show();
        var childIds = [];//勾选的单位计划ID
        $('form').find('input[name="checkChildId"]:checked').each(function () {
            childIds.push($(this).val());
        });
        if (childIds) {
            data.field.childIds = childIds;
        }
        if (data.field.childIds != "") {
            var action = "/budget/fbsbudget/collect";
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.getParentDom().location.reload();
                        location.reload();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            }, 'JSON');
            return false;
        } else {
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.collectcorp.null'), {icon: 7});
            return false;
        }
    });

    //全选
    form.on('checkbox(checkAll)', function (data) {
        var child = $('input[name="checkChildId"]');
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genDetailHtml = function () {
        var superaddId = $('#superaddId').val() == "" ? "0" : $('#superaddId').val();
        var target = $('#target').val() == "" ? "0" : $('#target').val();
        var getTimestamp = new Date().getTime();
        var url = basePath + "/budget/fbsbudget/inputDetail/" + $('#id').val() + "/" + superaddId + "/" + target + "?timestamp=" + getTimestamp;
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {

                $("#targtCollectDetail").html("");
                $("#targtCollectDetail").html(json);
                bt.moneyType.init();
                bt.treeTable.init({elem: "#appTable", column: 1});
                form.render();
            }
        });
    };
    $genDetailHtml();

    /**
     * 导出
     */
    exports = function () {
        var url = basePath + "/budget/fbsBudgetDownloadAndImport/exportBudgetCollect?id=" + $('#id').val() + "&timeType=" + $("#timeType").val() + "&target=" + $("#target").val();
        window.location.href = url;
    };
});

//编辑
function input(corpId, corpName, timeType, id, beginDate, endDate) {
    $ = bt.jquery, form = bt.form();
    beginDate = bt.date.format(beginDate, "yyyy-MM-dd");
    endDate = bt.date.format(endDate, "yyyy-MM-dd");
    var target = $('#target').val() == "" ? "0" : $('#target').val();
    var superaddId = $('#superaddId').val() == "" ? "0" : $('#superaddId').val();
    var action = "/budget/fbsbudget/input?corpId=" + corpId + "&timeType=" + timeType + "&id=" + id + "&beginDate=" + beginDate + "&endDate=" + endDate + "&target=" + target + "&isCollect=1";
    if (target == "super") {
        action = "/budget/fbsbudgetsuperadd/superadd?id=" + id + "&superaddId=" + superaddId + "&target=" + target + "&isCollect=1";
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data') + "-" + corpName, basePath + action);
        return;
    }
}

//创建
function create(corpId, corpName, timeType, id, beginDate, endDate,fictitiousCorp) {
    $ = bt.jquery, form = bt.form();
    beginDate = bt.date.format(beginDate, "yyyy-MM-dd");
    endDate = bt.date.format(endDate, "yyyy-MM-dd");
    var target = $('#target').val() == "" ? "0" : $('#target').val();
    var action = "/budget/fbsbudget/input?corpId=" + corpId + "&timeType=" + timeType + "&id=" + id + "&beginDate=" + beginDate + "&endDate=" + endDate + "&target=" + target + "&fictitiousCorp="+fictitiousCorp;
    if (target == "super") {
        action = "/budget/fbsbudgetsuperadd/superadd?id=" + id + "&superaddId=&target=" + target + "&fictitiousCorp="+fictitiousCorp;
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + action);
        return;
    }
}

//打回
function back(id) {
    $ = bt.jquery, form = bt.form();
    var backMsg = $("#backMsg").val();
    if (backMsg == "") {
        layer.alert(bt.lang('budget.message.backMsg.null'), {icon: 7});
        $("#backMsg").focus();
    } else {
        parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
            $.post(basePath + "/budget/fbsbudget/back", {id: id, backMsg: backMsg}, function (result) {
                location.reload();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}

//删除
function del(id, timeType) {
    $ = bt.jquery, form = bt.form();
    var target = $('#target').val() == "" ? "0" : $('#target').val();
    var superaddId = $('#superaddId').val() == "" ? "0" : $('#superaddId').val();
    var layId = $('#layId').val();
    var action = "/budget/fbsbudget/delete/" + id + "/" + timeType+"/"+layId;
    if (target == "super") {
        action = "/budget/fbsbudgetsuperadd/delete/" + id + "/" + superaddId;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + action, function (result) {
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
    });
}

//查看
function view(id) {
    var target = $('#target').val() == "" ? "0" : $('#target').val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/view/" + id + "/" + target);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/budget/fbsbudget/view/" + id + "/" + target
    });
}

