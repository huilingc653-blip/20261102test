bt.use(['form','layedit','dicts'], function(){
	var $ = bt.jquery,form = bt.form();
    dicts =  bt.dicts.init();
	//监听提交
    form.on('submit(submit)', function(data){
        var action = "/bankacc/bisaccattribute/modify";
        var validSign = dicts.get("validSign").val();
        var id= $("#id").val();
            bt.jquery.ajax({
                url: basePath + "/bankacc/bisaccattribute/queryUse/" + id,
                method: 'POST',
                dataType: 'json',
                success: function (ss) {
                    if(ss=="true" && validSign=="0"){
                        layer.alert(bt.lang('table.BisAccAttribute.used'), {icon: 7});
                    }else {
                        $.post(basePath + action,data.field,function(result){
                            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                                parent.layer.closeAll();
                                if(result.success){
                                    if(bt.navtab) {
                                        bt.navtab.closeCurrent();
                                    }else {
                                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    }
                                }
                            })
                        },'JSON');
                    }
                },
            })
            return false;

    });

});
