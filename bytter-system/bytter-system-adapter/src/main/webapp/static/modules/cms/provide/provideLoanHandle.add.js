var dateTayps;
var dictes;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','rateType'], function(){
	var $ = bt.jquery,form = bt.form();
    //利率
	bt.rateType.init();
    //时间
	dateTayps = bt.dateType.init();
    //字典
	dictes = bt.dicts.init();
	
	bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }
	 //监听提交 110 -1
    form.on('submit(submit)', function(data){
    	var status = $(this).attr("value");
        var action = "/cms/provide/provideloandeferred/modify";
        data.field.status = status;
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});


