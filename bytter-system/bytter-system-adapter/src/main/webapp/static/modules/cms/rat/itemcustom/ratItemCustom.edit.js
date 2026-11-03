bt.use(['form','layedit','moneynum', 'select','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    
    bt.getSysItemCode = function(type){
    	var action = "/cms/rat/ratitemcustom/sysItemJson?type="+type.value;
        $.post(basePath + action, null,function(data){
            if(data){
            	if(data && data.length>0){
            		$("#sysItemCode").empty();
            		$.each(data,function(index,item){
            			$("#sysItemCode").append("<option value="+item.code+">"+item.name+"</option>");
            		});
            	}else{
            		$("#sysItemCode").append("<option value=''></option>");
            	}
            	form.render();
            }
        },'JSON');
    };
    
    
    bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/cms/rat/ratitemcustom/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParent();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});
