bt.use(['form', 'treeTable', 'date', 'select'], function () {
    $ = bt.jquery, form = bt.form();
    $("#budgetBody").show();
    bt.treeTable.init({elem: "#curTable", column: 0});

    /**
     * 计划追加
     * @param budgetId
     * @param superaddId
     */
    superadd = function (budgetId, superaddId) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/budget/fbsbudgetsuperadd/superadd?id=" + budgetId + "&superaddId=" + superaddId + "&target=super");
            return;
        }
    }

    /**
     * 删除
     * @param budgetId
     * @param superaddId
     */
    del = function (budgetId, superaddId) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/budget/fbsbudgetsuperadd/delete/" + budgetId + "/" + superaddId, function (result) {
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
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/view/" + id + "/super");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudget/view/" + id + "/super"
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
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/curView/" + corpId + "/" + timeType + "/" + beginDate + "/" + endDate + "/super");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudget/curView/" + corpId + "/" + timeType + "/" + beginDate + "/" + endDate +  "/super"
        });
    }
});


function refresh() {
    location.reload(true);
}