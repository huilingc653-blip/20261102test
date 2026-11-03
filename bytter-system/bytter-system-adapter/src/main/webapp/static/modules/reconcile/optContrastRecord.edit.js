bt.use(['form','mulselect','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.mulselect.init();
    bt.dicts.init();
    //提交退票
    form.on('submit(retreat)', function(data){
        optSure(data);
    });
    //返回
    $("[name='revert']").click(function(){
        parent.layer.closeAll();
    });
});

function optSure(data){
    var url = basePath +"/bis/bankEnterprise/optSure/";
    var bean = new Object();
    $.post(url,{bean},function(result){
        bt.loading.hide();
        parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
        if(result.success){
            parent.layer.closeAll();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }
        return false;
    },'JSON');
}