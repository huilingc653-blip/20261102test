var rasShops={};
var dicts;
bt.use(['form','selectTable','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
	//监听保存
	form.on('submit(save)', function(data){ 
		if($("#fileType").find('select').val()==" "){
    		layer.alert(bt.lang('ras.RasImportConf.fileType.notnull'), {icon: 7});
			return false;
		}
//		$("#dataText input[type='text']").each(function(){
//            if($(this).val()==""){
//            	return;
//            }
//		})
	    $.post(basePath+ "/ras/importConf/saveOrstorage/revenue",data.field,function(result){
	        if(result.success){
	           parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
	           location.reload();//重刷页面
	        }else{ 
	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
	        }
	        bt.loading.hide();
	    },'JSON');
	    return false;
	});
	//取消按钮
    $("#cancel").click(function(){
    	bt.navtab.closeCurrent();
    	return false;
    });
	$("#appTable").find('tbody td span.pl').remove();
	
}); 

//下拉回调
function selectfileType(data){
	if(data.value!=""){
    	location.replace(basePath+"/ras/importConf/revenueConf?fileType="+data.value+"&startLine="+$("#startLine").val());
	}
}
