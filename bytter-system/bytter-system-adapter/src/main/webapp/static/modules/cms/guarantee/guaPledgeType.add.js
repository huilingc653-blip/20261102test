var dateTypes,dicts;
bt.use(['form','layedit','dateType','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    dateTypes = bt.dateType.init();//初始化
    dicts = bt.dicts.init();//字典
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
        }
    });

	/**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }	
    
	//检查保函编号是否重复
    $("#typeCode").blur(function(){
    	var typeCode = $(this).val();
    	var id=$("#id").val()
    	if(bt.empty(typeCode)){
    		return;
    	}
    	$.post(basePath + "/cms/guarantee/pledgeType/checkTypeCode", {
    		typeCode : typeCode,
    		id : id
		}, function(data) {
			if(data){
                parent.layer.msg(bt.lang("table.guaPledge.typeCodeRepeat"), {icon: 2});
				$("#typeCode").val("");
			}
		}, 'JSON');
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/cms/guarantee/pledgeType/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                $.each($(".layui-form"), function(){
                    this.reset();
                });
                if(!bt.navtab){
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                	bt.navtab.refreshParent();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});


/**
 * 检查不允许输入汉字及特殊字符
 * @param obj
 */
function checkNO(obj) {
    var regx =/[\u4e00-\u9fa5]/,//汉字
        regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im,//英文特殊字符
        regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;//中文特殊字符
    if (regx.test(obj.value)||regEn.test(obj.value)||regCn.test(obj.value)) {
        obj.value = "";
    }
}

