var rasShops={};
var dicts={};
bt.use(['form','dateType','cronGen','dicts'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
    bt.dateType.init();
  //表达式
    $("#cron").cronGen({
        direction : 'right'
    });
	//监听保存
	form.on('submit(save)', function(data){ 
		bt.loading.show();
		var dataInfo=data.field;
        var taskData={
        		"id":dataInfo.id,
        		"opType":dataInfo.opType,
        		"startDate":$("#startDate").val(),
        		"validSign":dataInfo.validSign,
        		"payType":dataInfo.payType,
        		"taskCron":$("#cron").val()
        };
	    $.ajax({
            type:"POST",
            url: basePath+ "/ras/auditTask/saveOrUpdate",
            dataType:"json",
            contentType:"application/json",
            data:JSON.stringify(taskData),
            success:function(result){
            	if(result.success){
            		$.each($(".layui-form"), function(){
                     });
    	            parent.layer.alert(result.msg, {icon: result.success?1:2});
    	            closeForms();
    	        }else{ 
    	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
    	        }
    	        bt.loading.hide();
            }
        });
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

function executeScan(){
	var id=$("#id").val();//任务信息id
	var startDate=$("#startDate").val();//开始执行日期
    if (id) {
        bt.loading.show();
        $.ajax({
            type:'post',
            url:basePath + "/ras/auditTask/executeScan",
            data:JSON.stringify({"id":id,"startDate":startDate}),
            contentType:"application/json",
            async:true,
            success:function(result){
                bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            },
            dataType:'JSON'
        })
    }
}