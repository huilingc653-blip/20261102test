var form,$,selectTree,corp;
bt.use(['form','selectTree','treeTable','corp'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();

    //监听保存
    form.on('submit(save)', function(data){
        bt.loading.show();
        var action="/billPool/corp/saveOrUpdate";
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

//单位回调函数
function corpCallback(data){
    var corpIds = $("#corpIds").val();
    // 单位只能出现一次
    if(corpIds.indexOf(data.id) > -1){
        layer.alert(bt.lang('billPool.corp.onlyWarn'), {icon: 7});
        corp.get("corpId").val("");
        $("#corpCode").html("");
    }
}


