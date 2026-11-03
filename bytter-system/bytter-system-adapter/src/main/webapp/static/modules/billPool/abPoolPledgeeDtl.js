bt.use(['form','bankInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    //提交退票
    form.on('submit(retreat)', function(data){
        bt.loading.show();
        submitForm(data);
    });
    bt.bankInfo.init({   
   	 multiLayer:true
    }); 
    //返回
    $("[name='revert']").click(function(){
    	parent.layer.closeAll();
    });
    
    // 选择银行
	bt.bankinfoCallback = function(data,dom){ 
       $("#bankNo").val(data.bankCode);
       $("#bankName").val(data.bankName);
	}
}); 

function submitForm(data){
	   $.post(basePath + '/abpool/pledgee/updateAbPoolPledgee', data.field, function (result) {
			bt.loading.hide();
	        if(result.success){
	        	parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
	            parent.layer.closeAll();
	        }else{
		        parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
	       	}
	        return false;
    },'JSON');
}