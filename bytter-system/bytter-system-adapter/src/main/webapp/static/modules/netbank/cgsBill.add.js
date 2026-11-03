bt.use(['form','layedit','dateType','corp','selectTree','dateForm','accInFoByCorp','moneyType','rateType','btUpload'], function(){
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    var payeeAcc = bt.accInFoByCorp.init();
    bt.dateType.init();
    var rateTye = bt.rateType.init();
    init1();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    //监听提交
    form.on('submit(submit)', function(data){
    	data.field.status=$(this).attr("value");
        data.field.attachmentId = up.getGroupId();
        var action = "/netbank/cgsbill/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    
    bt.payeeCorpCallback = function(data,dom){
    	$("#payeeAcc").val(data.blnctrAcc);
    	$("#payeeAccName").val(data.accName);
    }
    
    bt.checkBefore = function(){
    	var payeeCorpId = corp.get("payeeId").val();
    	if(payeeCorpId != null && payeeCorpId != "" && payeeCorpId != undefined){
    		payeeAcc.set({payeeCorpId:payeeCorpId});
        	return true;
    	}else{
    		layer.msg(bt.lang('nis.cgsbill.message.payeecorp.null'), {icon: 5});
    		return false;
    	}
    }
    	
    //初始化查找支付结算参数中的手续费
    function init1(){
    	$.ajax({
    		 url: basePath+"/netbank/cgsbill?getChargeRateTal",
    	        method: 'POST',
    	        data: '',
    	        dataType: 'json',
    	        success: function(data){
    	        	if(data.obj){
    	        		// $("#chargeRate").html(data.obj);
                        rateTye.get("chargeRate").val(data.obj);
    	        	}
    	        	form.render();
    	        }
    	});
    }
    
    moneyCallBack = function (data){
		$.ajax({
	        url: basePath+"/netbank/cgsbill?getChargeRate",
	        method: 'POST',
	        data: {m: data},
	        dataType: 'json',
	        success: function(data){
	        	if(data.obj || data.obj==0){
	        		$("#chargeMoney").val(data.obj);
	        	}
	        	form.render();
	        },
	        error:function(err){
	        }
	    });
	}
    bt.moneyType.init();



});

