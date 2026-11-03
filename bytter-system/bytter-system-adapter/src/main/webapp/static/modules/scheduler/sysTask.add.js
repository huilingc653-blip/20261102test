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
        placeholder:{value:'',text:'请选择'},
        url:basePath+"/sys/dict/dictlist?"+encodeURI("code[]=scheduler.status"),
        change:function(data,text){
        }
    });
    bt.select.init({
        elem:'.selectperiod',
        name:'period',
        required: true,
        valueField: "code",
        textField:"name",
        placeholder:{value:'',text:'请选择'},
        url:basePath+"/sys/dict/dictlist?"+encodeURI("code[]=scheduler.type"),
        change:function(data,text){
        }
    });
    //表达式
    $("#cron").cronGen({
        direction : 'right'
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/scheduler/systask/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                $.each($(".layui-form"), function(){
                    this.reset();
                });
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            }
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
