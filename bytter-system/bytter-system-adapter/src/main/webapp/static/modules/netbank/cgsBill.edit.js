bt.use(['form','layedit','dateType','corp','accInFoByCorp','moneyType','btUpload','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    var date = bt.dateType.init();
    var rateTye = bt.rateType.init();
    var payeeAcc = bt.accInFoByCorp.init();
    init1();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    bt.payeeCorpCallback = function(data,dom){
    	$("#payeeAcc").val(data.blnctrAcc);
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
		        // console.log(err);
	        }
	    });
	}
    bt.moneyType.init();

    //监听提交
    form.on('submit(submit)', function(data){
    	data.field.status=$(this).attr("value");
        data.field.attachmentId = up.getGroupId();
        var action = "/netbank/cgsbill/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    
});
