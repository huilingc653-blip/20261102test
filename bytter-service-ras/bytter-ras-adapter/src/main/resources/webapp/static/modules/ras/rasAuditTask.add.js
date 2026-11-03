var dicts={};
bt.use(['form','dicts'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
	//监听保存
	form.on('submit(save)', function(data){ 
		bt.loading.show();
	    $.ajax({
            type:"POST",
            url: basePath+ "/ras/auditTask/saveOrUpdate",
            dataType:"json",
            contentType:"application/json",
            data:JSON.stringify(data.field),
            success:function(result){
    	        bt.loading.hide();
            	if(result.success){
    	            parent.layer.alert(result.msg, {icon: result.success?1:2});
    	            closeForms();
    	        }else{ 
    	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
    	        }
            }
        });
	    return false;
	});
    form.verify({
    	codeCheck : function(value) {		
    			var success=false;
    			$.ajax({ 
    				type: 'POST',
    				url: basePath+"/ras/auditTask/codeCheck", 
    				data:{'code':value,'id':$("#id").val()},	
    				async: false, 
    				dataType:'json',
    				success: function(result){
    		        	if(!result.success){
    		        		success=true;
    		        	}
    		      }});
    			if(success){
	        		return bt.lang('ras.task.CodeCheck.uniq');
	        	}
		}
	});
}); 

