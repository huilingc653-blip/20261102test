bt.use(['form','datatable','corp','selectType','moneyType','dateType'], function(){
	$ = bt.jquery,form = bt.form(),moneyFun=bt.moneyType.getFun();
	
	//初始化下拉框
	seletTypeL = bt.selectType.init();
    
  //监听提交
    form.on('submit(submit)', function(data){
    	var status=$(this).attr("value");
        var action = "/finacial/interest/fcloanrepay/checkBack";
        data.field.status = status;//提交状态
        if(status==0&&$("#refuseWhy").val()==""){
        	layer.alert(bt.lang("common.message.config.please"),{icon:5});
        	return false;
        }
         $.post(basePath + action,data.field,function(result){
        	 parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
             if(result.success){
             	if(!bt.navtab){
                     parent.layer?parent.layer.closeAll():layer.closeAll();
                 } else {
                 	bt.navtab.refreshParent();
                 	bt.navtab.closeCurrent();
                 }
             }
        },'JSON');
        return false;
    });
    
  //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
    
});

