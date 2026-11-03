bt.use(['form','layedit','btdate'], function(){
    var $ = bt.jquery,form = bt.form();
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/bankacc/btbankacc/save";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});
