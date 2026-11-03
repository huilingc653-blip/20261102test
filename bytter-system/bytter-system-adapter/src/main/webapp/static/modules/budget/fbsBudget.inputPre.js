bt.use(['form', 'treeTable', 'date', 'select', 'element'], function () {
    $ = bt.jquery, form = bt.form();
    $("#budgetBody").show();
    bt.treeTable.init({elem: ".treeTable", column: 0});

    form.on('select(showType)', function (data) {
        var action = basePath + "/budget/fbsbudget/query";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });

    /**
     * 撤回
     * @param el
     */
    withdraw = function (id) {
        $.post(basePath + "/managerPlan/bpm/task/batchRevoke", {ids: id}, function (result) {
            if (result.success) {
                location.reload(true);
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    }

    /**
     * 计划上报
     * @param corpId
     * @param timeType
     * @param id
     * @param beginDate
     * @param endDate
     */
    input = function (corpId, timeType, id, beginDate, endDate, layId) {
        $ = bt.jquery, form = bt.form();
        beginDate = bt.date.format(beginDate, "yyyy-MM-dd");
        endDate = bt.date.format(endDate, "yyyy-MM-dd");
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/budget/fbsbudget/input?corpId=" + corpId + "&timeType=" + timeType + "&id=" + id + "&beginDate=" + beginDate + "&endDate=" + endDate + "&layId=" + layId + "&target=input");
            location.reload(true);
            return;
        }
    }

    /**
     * 删除
     * @param id
     * @param timeType
     */
    del = function (id, timeType, layId) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/budget/fbsbudget/delete/" + id + "/" + timeType + "/" + layId, function (result) {
                location.reload(true);
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    /**
     * 查看
     * @param id
     */
    view = function (id) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/view/" + id + "/input");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudget/view/" + id + "/input"
        });
    }

    /**
     * 实时查看
     * @param id
     */
    curView = function (corpId, timeType, beginDate, endDate) {
        $ = bt.jquery, form = bt.form();
        beginDate = bt.date.format(beginDate, "yyyy-MM-dd");
        endDate = bt.date.format(endDate, "yyyy-MM-dd");
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/curView/" + corpId + "/" + timeType + "/" + beginDate + "/" + endDate + "/input");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudget/curView/" + corpId + "/" + timeType + "/" + beginDate + "/" + endDate +  "/input"
        });
    }

});
