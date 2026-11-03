bt.use(['form','layedit','btdate','moneynum','dateType','btUpload'], function(){
    var $ = bt.jquery,form = bt.form();
    var gInfoId = $("#annexNameInfo").val();
    var gSendId = $("#annexNameSend").val();
    bt.btUpload.init({
        elem: "#uploaderInfo",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        gid: gInfoId
    });

    bt.btUpload.init({
        elem: "#uploaderSend",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        gid: gSendId
    });


    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    bt.dateType.init();
    
    checkBefore = function(){
    	var delayDate = bt.dateType.init().get("delayDate").val();
    	if(bt.empty(delayDate)){
    		layer.alert(bt.lang("table.ratbkrationdelay.delayDateNull"));
    		return false;
    	}
    	return true;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	var action = "/cms/rat/ratbkrationdelay/save";
    	$.post(basePath + action,data.field,function(result){
    		bt.loading.hide(); //关闭加载中
    		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    		if(result.success){
    			if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
    		}
    	},'JSON');
    	return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	if(!checkBefore()){
    		return false;
    	}
        var action = "/cms/rat/ratbkrationdelay/submit";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});
