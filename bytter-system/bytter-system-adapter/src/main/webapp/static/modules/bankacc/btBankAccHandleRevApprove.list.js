bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.bank.init({
        elem: '.bank',
    });
    bt.selectType.init();
    bt.dicts.init();

        bt.workFlow.beforeSubmit = function(submitData){
            var data = bt.datatable.getSelectData("#appTable");
            if(data.length < 1){
                layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 2});
                return;
            }
            var ids = [];
            var taskIds = [];
            // 遍历行数据
            $.each(data,function(i, tr) {
                taskIds.push(tr.taskId);
            });
            bt.workFlow.taskList = taskIds;
            return true;
        }

    });

/**
 * 查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrev/view/" + data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/bankacc/btbankaccrevhandle/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
}