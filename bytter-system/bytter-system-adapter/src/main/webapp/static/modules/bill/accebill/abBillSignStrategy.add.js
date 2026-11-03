var datatable={},moneyType,mulselect;
bt.use(['form','corp','dicts','mulselect','moneyType','externalCorp','bank','bankInfo','externalCorpBankAcc'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.bank.init({
        elem:'.bank'
    });
    bt.externalCorpBankAcc.init();
    bt.bankInfo.init();
    bt.dicts.init();
    mulselect=bt.mulselect.init();
    moneyType = bt.moneyType.init();
    var externalCorp=bt.externalCorp.init();
    
    //监听新增保存
    form.on('submit(save)', function(data){
        var url = "/bill/accebill/abbillsignstrategy/save/add";
        $.post(basePath + url,data.field,function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                location.reload();//重载当前页面
            }
        },'JSON');
    });
    
    
   
    //承兑人客商
//    bt.acceptorCustomerBankAccParams = function () {
//        //供应商默认不查询
//    	bt.externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0","type":"4"});
//        return true;
//    }
    
    //选择银行回调函数
    bt.bailBankInfoCallback = function(data,dom){
//    	alert("12121");
//    	$('#acceptorIds').val(data.id);
//    	$("#acceptoracc").val(data.bankName);//承兑人账号
    }
    
    
    //承兑人选择客商回调函数
//    bt.acceptorCorpCallback = function(data,dom){
//        if(data.bank){
//        	$('#acceptorIds').val(data.id);
//        	$("#acceptoracc").val(data.externalAcc);//承兑人账号
//        }else{
//        	 $("#acceptorIds").val(data.corpId);
//        	 $("#acceptoracc").val(data.name);//承兑人账号
//        }
//    }
    
    var externalCorpInfo={
    		"corpId":bt.corp.get("corpId").val(),
    		"corpIdVal":"",
    		"externalIdVal":""};
    var acceptorCorpInfo={
    		"corpId":bt.corp.get("corpId").val(),
    		"corpIdVal":"",
    		"externalIdVal":""
    };
    var reqCorpInfo={
    		"corpId":bt.corp.get("corpId").val(),
    		"corpIdVal":"",
    		"externalIdVal":""
    };
	
    //提交
    bt.externalCorpCallback=function(data,dom){
    	if(dom.data('value')=="01"){
    		externalCorpInfo.corpIdVal=data.corpIdVal;
    		externalCorpInfo.externalIdVal=data.externalIdVal;
    		$('#payerIds').val(externalCorpInfo.corpIdVal);
    		$('#payerExternalIds').val(externalCorpInfo.externalIdVal);
    		$('[data-name="viewPayer"]').show();
    	}else if(dom.data('value')=="02"){
    		acceptorCorpInfo.corpIdVal=data.corpIdVal;
    		acceptorCorpInfo.externalIdVal=data.externalIdVal;
    		$('#acceptorIds').val(acceptorCorpInfo.corpIdVal);
    		$('#acceptorExternalIds').val(acceptorCorpInfo.externalIdVal);
   		 	$('[data-name="viewAcceptor"]').show();
	    }else if(dom.data('value')=="03"){
	    	reqCorpInfo.corpIdVal=data.corpIdVal;
	    	reqCorpInfo.externalIdVal=data.externalIdVal;
	    	$('#reqIds').val(reqCorpInfo.corpIdVal);
	    	$('#reqExternalIds').val(reqCorpInfo.externalIdVal);
	    	$('[data-name="viewReq"]').show();
	    }
	}

    //选择
	bt.externalCorpParams=function(data,dom){
		if(data.data('value')=="01"){
    		parmsContains("01",externalCorpInfo,false);
			externalCorp.set(externalCorpInfo);
			return true;
    	}else if(data.data('value')=="02"){
    		parmsContains("02",acceptorCorpInfo,false);
    		externalCorp.set(acceptorCorpInfo);
    		return true;
    	}else if(data.data('value')=="03"){
    		parmsContains("03",reqCorpInfo,false);
    		externalCorp.set(reqCorpInfo);
    		return true;
    	}
		
	}

	//查看
	bt.externalCorpViewParams=function(data,dom){
		if(data.data('value')=="01"){
			parmsContains("01",externalCorpInfo,true);
			externalCorp.set(externalCorpInfo);
			return true;
		}else if(data.data('value')=="02"){
			parmsContains("02",acceptorCorpInfo,true);
			externalCorp.set(acceptorCorpInfo); 
			return true;
    	}else if(data.data('value')=="03"){
    		parmsContains("03",reqCorpInfo,true);
    		externalCorp.set(reqCorpInfo); 
    		return true;
    	}
	}
	
	if($('#payerIds').val()!="" || $('#payerExternalIds').val()!=""){
		$('[data-name="viewPayer"]').show();
	}
	
	if($('#onlineType').val()!="10" && ($('#acceptorIds').val()!="" || $('#acceptorExternalIds').val()!="")){
		$('[data-name="viewAcceptor"]').show();
	}
	
	if($('#reqIds').val()!="" || $('#reqExternalIds').val()!=""){
		$('[data-name="viewReq"]').show();
	}
	
	function parmsContains(status,$this,flag){
		$this.corpId=bt.corp.get("corpId").val();
		$this.isView=flag==true?"1":"0";
		if(status=="01"){
			$this.corpIdVal=$('#payerIds').val();
			$this.externalIdVal=$('#payerExternalIds').val();
		}else if(status=="02"){
			$this.corpIdVal=$('#acceptorIds').val();
			$this.externalIdVal=$('#acceptorExternalIds').val();
		}else if(status=="03"){
			$this.corpIdVal=$('#reqIds').val();
			$this.externalIdVal=$('#reqExternalIds').val();
		}
	}
	
	if($('#id').val()!=""){
		if($('#onlineType').val()=="10"){
			 $("#acceptorBankButtonDiv").show();//银行选择按钮
		     $("#acceptorCustomerButtonDiv").hide();//客商按钮
		}else{
			 $("#acceptorBankButtonDiv").hide();
		     $("#acceptorCustomerButtonDiv").show();
		}
	}
});


function switchBillType(){
	$('#acceptoracc').val("");
	$('#acceptorIds').val("")
	$('#acceptorExternalIds').val("")
	$('[data-name="typeName"]').find('select').val();;
	initAccter();
}

function initAccter(){
   $('#acceptoracc').val("");
   $('#acceptorIds').val("")
   $('#acceptorExternalIds').val("")
  if($('[data-name="billType"]').find('select').val()=="10"){
        //银票,承兑人选择银行信息
        $("#acceptorBankButtonDiv").show();//银行选择按钮
        $("#acceptorCustomerButtonDiv").hide();//客商按钮
    }else{
    	//商票,承兑人选择成员单位或客商信息
        $("#acceptorBankButtonDiv").hide();
        $("#acceptorCustomerButtonDiv").show();
    }
    bt.form().render();
}


function switchAcceptor(data,dom){
	$('#acceptoracc').val("");
	$('#acceptorIds').val("")
	$('#acceptorExternalIds').val("")
	var ids;
	for(i=0;i<data.length;i++){
    	if(ids){
    		ids=ids+","+data[i].id;
    	}else{
    		ids=data[i].id;
    	}
    }
	$('#acceptoracc').val(ids);
}


