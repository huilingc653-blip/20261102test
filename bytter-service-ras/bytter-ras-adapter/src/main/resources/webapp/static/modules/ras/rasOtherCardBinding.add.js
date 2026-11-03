var rasShops={};
var dicts;
bt.use(['form','selectTable','layedit', 'corp', 'select', 'dateType','rasShop', 'itemType','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();     
    bt.dateType.init(); 
    dicts = bt.dicts.init();
    rasShops=bt.rasShop.init();
    bt.corp.init();    
    
	//监听保存
	form.on('submit(save)', function(data){ 
		bt.loading.show();
	    $.post(basePath+ "/ras/otherCardBinding/saveOrstorage/save",data.field,function(result){
	        if(result.success){
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	            bt.navtab.refreshParentTable();//刷新父页面表格
	            location.reload();//重刷页面
	        }else{ 
	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
	        }
	        bt.loading.hide();
	    },'JSON');
	    return false;
	});
	//重置页面信息
    $("#reset").click(function(){
        location.reload();
        return false ;
    });
	
    //取消按钮
    $("#cancel").click(function(){
    	bt.navtab.closeCurrent();
    	return false;
    });
    
	
}); 

// 所属单位回调  
function corpCallback(data){  
	 bt.rasShop.get("shopInfo").init({"corpId":data.id});
	// bt.accountTable.get("accNo").initCorp(data.id,"1");
}
//回调门店
function shopCallback(obj,text,data){
	if(data!=null){
		$('#shopCode').val(data.code);
	}
}

