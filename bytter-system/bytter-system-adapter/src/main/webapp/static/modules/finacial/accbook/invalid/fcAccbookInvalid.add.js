bt.use(['element', 'form','layedit','btdate','moneynum'], function(){
	//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
	var $ = bt.jquery,form = bt.form();
	var element = layui.element();
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/accbook/fcaccbookinvalid/save";
    	$.post(basePath + action,data.field,function(result){
    		bt.loading.hide(); //关闭加载中
    		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/accbook/fcaccbookinvalid/submit";
        $.post(basePath + action, data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //返回
    /*form.on('submit(revert)', function(data){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
        return false;
    });*/
    doRevert=function(){
    	if(bt.navtab){
    		bt.navtab.closeCurrent();
    	}
    }
    
    
});
