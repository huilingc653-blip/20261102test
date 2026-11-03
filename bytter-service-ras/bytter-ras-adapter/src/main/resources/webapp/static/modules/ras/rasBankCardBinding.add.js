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
		if(dicts.get('isVirtualAcc').val()=="1"){
			if($("#entityAccNo").val()==""){
	    		layer.alert(bt.lang('ras.message.rasbankcardbinding.entityaccno.empty'), {icon: 7});
				return false;
			}
	    }
		bt.loading.show();
	    $.post(basePath+ "/ras/bankCardBinding/saveOrstorage/save",data.field,function(result){
	        if(result.success){
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	            //parent.layer?parent.layer.closeAll():layer.closeAll();
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
	 bindBankplugIn(data.id);
}
//回调门店
function shopCallback(obj,text,data){
	if(data!=null){
		$('#shopCode').val(data.code);
	}
}
var selectTableObj = null;
var bindBankplugIn = function(corpId){
	$("#accNo").val('');
	$("#accId").val('');
	$('#accName').val('');
	$('#accBankName').val('');
	if(selectTableObj){
		selectTableObj.get('selectTable').reload({
			corpId: corpId,
			isVirtualAcc:dicts.get('isVirtualAcc').val()
		})
		return;
	}
	selectTableObj = bt.selectTableType.init({
        elem:".rasBank",
        url:basePath+"/bankacc/btbankacc/getRasBankListBy",
        scrollX:true,
        scrollY:"300px",
        paging:false,
        urldata: {corpId: corpId,isVirtualAcc:dicts.get('isVirtualAcc').val()},
        columns:[
           {data:"bankAcc",title:bt.lang('table.accountDTO.bankAcc')},
           {data:"accName",title:bt.lang('table.accountDTO.accName')},
           {data:"bankName",title:bt.lang('table.accountDTO.bankName')},
           {data:"curName",title:bt.lang('table.accountDTO.curName')},
        ],
        clickCallback:function(sdata,$tr){
        	$("#accNo").val(sdata.bankAcc);
        	$("#accId").val(sdata.bankAccId);
        	$('#accName').val(sdata.accName);
        	$('#accBankName').val(sdata.bankName);
        	if(dicts.get('isVirtualAcc').val()=="1"){
        		$('#entityAccNo').val(sdata.entityAccNo);
            	$('#entityAccId').val(sdata.entityAccId);
        	}
        },
        tableInitCallback:function(full,$elem){ 
        	
        },
    })
}



function isVirtualAccCallBack(){
   $("#accNo").val('');
   $("#accId").val('');
   $('#accName').val('');
   $('#accBankName').val('');
   if(dicts.get('isVirtualAcc').val()=="1"){
	   $("#entityAcc").css('display',''); 
   }else{
	   $("#entityAcc").css('display','none'); 
   }
   if(bt.corp.get("corpId").val()!=""){
	   bindBankplugIn(bt.corp.get("corpId").val());
   }
}

