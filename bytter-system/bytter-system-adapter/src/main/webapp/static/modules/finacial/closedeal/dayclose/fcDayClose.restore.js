bt.use(['form','layedit','btdate','corp','dateType'], function(){
    var $ = bt.jquery,form = bt.form();
    var getCloseDate;
    
    var corp = bt.corp.init();
    var closeDate = bt.dateType.init();
    
    //监听重置
    bt.resetCode = function(netCode){
		corp.get("netCode").val(netCode);
    }

    bt.getNetCloseDate = function(netId){
    	var action = "/finacial/closedeal/fcdayclose/toViewReCloseDate?netId="+netId;
        $.post(basePath + action, null,function(result){
            if(result){
                $("#lastCloseDate").val(result.closeDateStr);//已日结日期
            }
        },'JSON');
    }
    
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/closedeal/fcdayclose/doReverseDayClose";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	bt.getNetCloseDate(data.field.netCode);
            }
        },'JSON');
        return false;
    });
});

getCloseDate = function(data){
	bt.getNetCloseDate(data);
};

//监听重置
resetCode = function(){
	var netCode = $("#netId").val();
	if("" != netCode){
		bt.resetCode(netCode);
		getCloseDate(netCode);
	}
}