bt.use(['form','dateType'], function(){
    $ = bt.jquery;
    bt.dateType.init();
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

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/foreign/fextranreq/approveTranDeal?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/approveTranDeal?id=" + data.id + "&taskId=" + data.taskId
    });
}


