bt.use(['form','upload'], function(){	
	//导入
    layui.upload({
    	url: basePath+'/netbank/nisbustype/upload/',
    	success: function(result){
			if(result.success){
				if(bt.navtab){
					parent.layer?parent.layer.closeAll():layer.closeAll();
				}
			}
			parent.layer.alert(result.msg, {icon: result.success?1:2}); 
    	}
    });
});