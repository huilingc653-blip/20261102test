var rasShops={};
var dicts;
bt.use(['form','selectTable','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
	//监听保存
	form.on('submit(save)', function(data){ 
		if($("#dateSource").find('select').val()==" "|| $("#dateSource").find('select').val()==""){
    		layer.alert(bt.lang('ras.message.datasoure.notnull'), {icon: 7});
			return false;
		}
		if($("#fileType").find('select').val()==" " || $("#fileType").find('select').val()==""){
    		layer.alert(bt.lang('ras.RasImportConf.fileType.notnull'), {icon: 7});
			return false;
		}
		if($("#fileType").find('select').val()=="txt"){
			if(dicts.get('isFormat').val()=="1" && $("#txtFormat").val()==""){
				layer.alert(bt.lang('ras.received.import.isseparator.not.null'), {icon: 7});
				return false;
			}
		}
		
		bt.loading.show();
	    $.post(basePath+ "/ras/importConf/saveOrstorage/received",data.field,function(result){
	        if(result.success){
	           parent.layer.alert(result.msg, {icon: result.success?1:2});
	           location.reload();//重刷页面
	           bt.navtab.refreshParentWithoutDatatable();
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
function selectfileType(){
	if($("#dateSource").find('select').val()!=" " && $("#fileType").find('select').val()!=" "){
    	location.replace(basePath+"/ras/importConf/add?fileType="+$("#fileType").find('select').val()+"&payType="+$("#payType").find('select').val()
    	+"&startLine="+$("#startLine").val()+"&dateSource="+$("#dateSource").find('select').val()
    	+"&isFormat="+dicts.get('isFormat').val());
	}
}

//TXT格式回调
function isFormatCallBack(){
}
