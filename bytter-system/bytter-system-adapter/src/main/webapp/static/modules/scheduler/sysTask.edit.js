bt.use(['form','layedit','dateType','select','cronGen', 'dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //时间
    bt.dateType.init();
    bt.select.init({
        elem:'.selectactive',
        name:'active',
        required: true,
        valueField: "code",
        textField:"name",
        defaultVal: $(".selectactive").data("value"),
        placeholder:{value:'',text:'请选择'},
        url:basePath+"/sys/dict/dictlist?"+encodeURI("code[]=scheduler.status")
    });
    bt.select.init({
        elem:'.selectperiod',
        name:'period',
        required: true,
        valueField: "code",
        textField:"name",
        defaultVal: $(".selectperiod").data("value"),
        placeholder:{value:'',text:'请选择'},
        url:basePath+"/sys/dict/dictlist?"+encodeURI("code[]=scheduler.type")
    });
    //表达式
    $("#cron").cronGen({
        direction : 'right'
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/scheduler/systask/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    $("html").on("click",":not(span.input-group-btn)",function(e){
        if(!$(e.target).parents("span.input-group-btn").length){
            if($(".popover").is(":visible")){
                $(".popover").stop().slideUp("slow");
            }
        }
    });
});
