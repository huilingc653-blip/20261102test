var form,$,corp,cell;
bt.use(['form','dicts','cell'], function () {
    $ = bt.jquery, form = bt.form();

    dicts=bt.dicts.init();
    cell=bt.cell.init();


    //监听保存
    form.on('submit(save)', function(data){
        bt.loading.show();
        var action="/sys/cell/saveOrUpdate";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    parent.layer.closeAll();//重载当前页面
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });

});

