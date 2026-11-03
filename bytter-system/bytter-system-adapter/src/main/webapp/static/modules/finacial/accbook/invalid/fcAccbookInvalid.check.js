bt.use(['element', 'form','btUpload','layedit','btdate','moneynum'], function(){
	//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
	var $ = bt.jquery,form = bt.form();
	var element = layui.element();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });
    //监听打回
    form.on('submit(back)', function(data){
    	bt.loading.show();//显示加载中
    	if($("#backReason").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
    		bt.loading.hide(); //关闭加载中
    		parent.layer.alert(bt.lang("table.fcAccBook.checkBackReason.please"));
            $("#backReason")[0].focus();
            return false;
        }
    	
    	var action = "/finacial/accbook/fcaccbookinvalid/back";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听通过
    form.on('submit(pass)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/accbook/fcaccbookinvalid/check";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParentTable();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //返回
    form.on('submit(revert)', function(data){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
        return false;
    });
    
    
});
