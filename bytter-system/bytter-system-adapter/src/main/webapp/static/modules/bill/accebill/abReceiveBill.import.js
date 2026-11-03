bt.use(['form','upload'], function(){	
	   //导入
	    layui.upload({
	    	         url: basePath+'/bill/accebill/abreceivebill/upload/',
	  	             success: function(result){
	  	            	 parent.layer.alert(result.msg, {icon: result.success?1:2}); 
	  	            	 if(result.success){
	  	            		if(bt.navtab){
        	                    parent.layer?parent.layer.closeAll():layer.closeAll();
        	                }
	  	            	 }
	  	               }
	  	             });
    });
    





