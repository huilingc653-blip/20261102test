bt.use([ 'form', 'layedit', 'select','dicts','selectTree','dateType','moneyType','cronGen'], function() {
	var $ = bt.jquery,form = bt.form();
	bt.dicts.init();
    bt.dateType.init();
    bt.moneyType.init();
    //表达式
    $("#cron").cronGen({
        direction : 'right'
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/fundwarn/btwarnstrategy/saveScanParameter";
        var dataInfo=data.field;
        dataInfo.taskCron = $("#cron").val();
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                $.each($(".layui-form"), function(){
                   /* this.reset();*/
                });
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
    //监听手动执行
    /*form.on('submit(execute)', function(data){
        var action = "/fundwarn/btwarnstrategy/executeScan";
        var dataInfo=data.field;
        dataInfo.taskCron = $("#cron").val();
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){

                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });*/

    $("html").on("click",":not(span.input-group-btn)",function(e){
        if(!$(e.target).parents("span.input-group-btn").length){
            if($(".popover").is(":visible")){
                $(".popover").stop().slideUp("slow");
            }
        }
    });
});
function executeScan(){
	var id=$("#id").val();//策略id
	var runDate=$("#runDate").val();//策略id
    var forwardTime=$("#forwardTime").val();//策略id
    if (id) {
        bt.loading.show();
        $.ajax({
            type:'post',
            url:basePath + "/fundwarn/btwarnstrategy/executeScan",
            data:{"id":id,"runDate":runDate,"forwardTime":forwardTime},
            async:true,
            success:function(result){
                bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            },
            dataType:'JSON'
        })
    }
}
