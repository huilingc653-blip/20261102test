bt.use(['form','layedit','btdate','moneynum','btUpload','dateType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    
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
        data.field.attachmentId = up.getGroupId();//设置附件ID
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
        data.field.attachmentId = up.getGroupId();//设置附件ID
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
