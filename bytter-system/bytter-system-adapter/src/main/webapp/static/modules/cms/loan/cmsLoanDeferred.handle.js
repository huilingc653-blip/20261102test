bt.use(['form','moneyType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    
    //监听提交
    form.on('submit(submit)', function(data){
    	var status=$(this).attr("value");
        var action = "/cms/loan/cmsloandeferred/doHandle";
        data.field.status = status;//提交状态
        if(status == 0){
        	parent.layer.confirm(bt.lang('common.message.confirm.repulse'),function(){
        		var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
                $.post(basePath + action,data.field,function(result){
                    parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                    if(result.success){
                    	layer.close(mask);
                    	if(!bt.navtab){
                            parent.layer?parent.layer.closeAll():layer.closeAll();
                        } else {
                        	bt.navtab.refreshParent();
                        	bt.navtab.closeCurrent();
                        }
                    }else{
                    	layer.close(mask);
                    }
                },'JSON');
            });
        }else{
        	var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
            $.post(basePath + action,data.field,function(result){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                	layer.close(mask);
                	if(!bt.navtab){
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    } else {
                    	bt.navtab.refreshParent();
                    	bt.navtab.closeCurrent();
                    }
                }else{
                	layer.close(mask);
                }
            },'JSON');
        }
        return false;
    });
    
    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
});
