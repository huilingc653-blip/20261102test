var corpId="";
bt.use(['form','layedit','btdate','corp','dicts','users'], function(){
    var $ = bt.jquery,form = bt.form();  
    bt.funUserReset=function(data){
    	bt.otheruser.get("userName").reset();
    	corpId=data;
    }
    bt.otheruser=bt.users.init();
    bt.corp.init();
    bt.dicts.init();
    form.verify({
    	codeCheck : function(value) {		
    			var success=false;
    			$.ajax({ 
    				type: 'POST',
    				url: basePath+"/bill/blankvou/bvstoragebox/checkCorpIdCode", 
    				data:{'code':value,'corpId':corpId,'id':"-1"},	
    				async: false, 
    				dataType:'json',
    				success: function(result){
    		        	if(!result.success){
    		        		success=true;
    		        	}
    		      }});
    			if(success){
	        		return bt.lang('bvStorageBox.noSameCode');
	        	}
		}
	});

    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/bill/blankvou/bvstoragebox/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                parent.layer?parent.layer.closeAll():layer.closeAll(); 
            }else{
                parent.layer?parent.layer.alert(bt.lang("bvStorageBox.noSameCode"), {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            }
        },'JSON');
        return false;
    });
    
    
 
});


