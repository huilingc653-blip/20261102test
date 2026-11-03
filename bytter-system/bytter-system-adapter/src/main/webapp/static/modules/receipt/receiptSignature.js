bt.use(['form','layedit','select','dateType','corp','dicts','mulselect'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/receipt/receiptQuery/signature";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
    var date = bt.dateType.init();
    bt.setSignatureDate=function(netId){
    	$.ajax({ 
			type: 'POST',
			url: basePath+"/receipt/receiptQuery/getDayOver", 
			data:{'netId':netId},	
			async: false, 
			dataType:'json',
			success: function(result){
	        	if(result.success){
	        		date.get("endDate").attr("data-max",result.attributes.dayOver);
	        		date.get("beginDate").attr("data-max",result.attributes.dayOver);
	        	}else{
	        		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
	        	}
	      }});
    };
    bt.corp.init();
});
