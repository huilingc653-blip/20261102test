bt.use(['form','btUpload','layedit'], function(){
	var $ = bt.jquery,form = bt.form();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });
  	//监听提交
    form.on('submit(back)', function(data){
    	bt.loading.show();//显示加载中
    	if($("#backReason").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
    		bt.loading.hide(); //关闭加载中
    		parent.layer.alert(bt.lang("table.fcAccBook.checkBackReason.please"));
            $("#backReason")[0].focus();
            return false;
        }
    	
        var action = "/finacial/accbook/fcaccbook/back";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
        },'JSON');
        return false;
    });
  	
    //监听提交
    form.on('submit(pass)', function(data){
    	bt.loading.show();//显示加载中
        var action = "/finacial/accbook/fcaccbook/checkPass";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
        },'JSON');
        return false;
    });
    
});
