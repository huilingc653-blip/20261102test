var form;
bt.use(['form','billPool','select','jquery','selectType','selectTable','bank','bankInfo','btdate','dateType','dicts'], function(){
        var $ = bt.jquery,
        form = bt.form();
        bt.empty = function(ms){
            if(typeof(ms) == "undefined" || ms == null || ms == ""){
                return true;
            }
            return false;
        }
        bt.selectType.init();
        bt.billPool.init();
        bt.bankInfo.init({   
        	 multiLayer:true
        }); 
        bt.dicts.init();
        bt.bank.init({
        	elem:'.bank'
    	})
        //提交申请信息
        form.on('submit(retreat)', function(data){
            bt.loading.show();
            submitForm(data);
        });
	    //返回
        $("[name='revert']").click(function(){
            parent.layer.closeAll();
        });
        
        // 选择银行
    	bt.bankinfoCallback = function(data,dom){
           $("#bankNo").val(data.bankCode);
		   $("#bankName").val(data.bankName);
    	}
        loadBillFlag();
    });

    function submitForm(data){
        $.post(basePath + '/billPool/intoApply/saveAppleDtl', data.field, function (result) {
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
               // bt.navtab.closeCurrent();
                //bt.navtab.refreshParentTable();//刷新父页面表格 
                parent.layer.closeAll();
                 location.reload();//重刷页面
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                location.reload();//重刷页面
            }
            return false;
        },'JSON');
    }
    function bankinfoCallback(obj,text,data){
    	$("#bankNo").val(data.bankCode);
		$("#bankName").val(data.bankName);
    }
    //请求后台质权人信息
    function abPoolCallBack(obj,text,data){
    	var billFlag =$('#billFlag').val();
    	if(billFlag=="11"){
			return false;
    	}
	 	$.ajax({
            url:basePath + '/billPool/intoApply/getPoolInfo?id='+data.id,//请求地址
            type: "POST",
            dataType: "json",
            success: function(data) {//data是默认的，接收前台返回的数据
               $("#bankAcc").val(data.obj.bankAcc);
			   $("#accName").val(data.obj.accName);
               $("#bankNo").val(data.obj.bankNo);
			   $("#bankName").val(data.obj.bankName);
            }
        })
    }
    function loadBillFlag(){ 
    	var billFlag =$('#billFlag').val();
    	if(billFlag=="11"){
    	  	$("#bankNo").removeAttr("lay-verify");
			$('.rationSelectDIV').hide();
    	}else{
    		 $("#bankNo").attr("lay-verify", "required"); 
    	}
    	bt.form().renderIcon();
    }