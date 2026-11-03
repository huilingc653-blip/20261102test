bt.use(['form','layedit','moneynum'], function(){
    var $ = bt.jquery,form = bt.form();

    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/cms/provide/abolish/abolishSave";
        var status = $(this).attr("value");
        data.field.status = status;
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    $("#close").click(function(){
    	bt.navtab.closeCurrent();
    });
});
