bt.use(['form', 'datatable', 'dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();

    //从当前页面获取表格数据并设置值
    getCurrentSelectedData();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/bankacc/biscorppooldtl/batchClaimSave";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrentTable();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 从当前页面获取表格数据并设置值
     */
    function getCurrentSelectedData() {
        dataSelect = bt.navtab.getCurrentDom().bt.datatable.getSelectData('#appTable');
        var ids ='';
        if(dataSelect){
            bt.each(dataSelect,function (index,item) {
                ids += item.id +',';
            })
        }
        ids = ids.slice(0,ids.length-1);
        $('#id').val(ids);
    }
});
