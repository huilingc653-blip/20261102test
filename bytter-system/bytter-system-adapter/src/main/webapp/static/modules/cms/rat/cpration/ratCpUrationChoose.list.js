bt.use(['form','datatable','btdate','money','dateType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    
    //结转
    bt.submitRation = function(){
    	var radioArr = jQuery("input:radio:checked");
    	var urationIds = "";
    	$.each(radioArr, function(i) { 
    		urationIds = urationIds + radioArr[i].value+";";
    	});
    	if(urationIds.length < 1){
    		layer.alert(bt.lang("common.button.selectnum"));
    		return false;
    	}
    	bt.rationChange(urationIds);
    }
    
    
    //授信结转
    bt.rationChange = function(urationIds){
    	var params = {
    			urationIds : urationIds,
    			joinFlag : "1"
    	};
    	
    	//结转调用
    	var action = "/cms/rat/ratcpuration/rationChange";
        $.post(basePath + action, params, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
	            if(!bt.navtab){
	            	parent.layer?parent.layer.closeAll():layer.closeAll();
	            } else {
	            	bt.navtab.refreshCurrent();
	            	layer.closeAll();
	            	parent.layer.closeAll();
	            }
            }
        },'JSON');
    }
    
    //取消
    bt.cancelRation = function(){
    	parent.layer.closeAll();
    	layer.closeAll();
    }
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.refreshCurrent();
        	layer.closeAll();
        }
    }
    
    bt.dicts.init();
});


closeTab = function(){
	bt.close();
}

closeRation = function(){
	bt.closeRation();
}

submitRation = function(){
	bt.submitRation();
}

cancelRation = function(){
	bt.cancelRation();
}