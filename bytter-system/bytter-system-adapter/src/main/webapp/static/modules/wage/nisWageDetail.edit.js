bt.use(['form','layedit','dateType','bank','bankInfo','region', 'dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var region = bt.region.init();
    bt.bank.init({elem:'#bankAcc'});
    bt.bankInfo.init();
    //跨行标志回调函数
    bt.difBankCallback = function (data) {
        var difBank = bt.dicts.get('difBank').val();
        var isMustCode = $("#isMustCode").val();
        //跨行 or 同行且同行代发必填开户行行号
        if(difBank == "1" || (difBank == "0" && isMustCode == "1")){
            $('#bankCode').attr('lay-verify', 'required');
            $('#bankName').attr('lay-verify', 'required');
        }else{
            $('#bankCode').removeAttr("lay-verify");
            $('#bankName').removeAttr("lay-verify");
        }
        form.renderIcon();
    };
    var dicts = bt.dicts.init();
    
    //开户行选择
    bt.bankinfoCallback = function(data,dom){
    	$("#bankCode").val(data.bankCode);
    	$("#bankName").val(data.bankName);
    	//设置省市组的值
        region.get('payeeAddr').set([{
            name:'prov',
            defaultValue:data.prov
        },{
            name:'city',
            defaultValue:data.city
        }]);
        $('#payeeProv').val(data.accProvince);
		$('#payeeCity').val(data.accCity);
        //判断跨行标志和地域标志
		difBankAndAreaSign();
    };
    
    //省回调函数
    bt.provCallback = function(value,othis,options){
    	$('#payeeProv').val(value);
 		$('#payeeCity').val("");
    	//判断跨行标志和地域标志
        difBankAndAreaSign();
    };
    //市回调函数
    bt.cityCallback = function(value,othis,options){
 		$('#payeeCity').val(value);
    	//判断跨行标志和地域标志
        difBankAndAreaSign();
    };
    
    //跨行标志，地域标志判断
    function difBankAndAreaSign(){
		if($("#payerBankCode").val() != "" && $("#bankCode").val() != "" &&
				$("#payerBankCode").val().substr(0,3) == $("#bankCode").val().substr(0,3)){
            //商业银行根据开户行名称前缀匹配
            if($("#payerBankCode").val().substr(0, 3) == "313" || $("#payerBankCode").val().substr(0, 3) == "402"
                || $("#payerBankCode").val().substr(0, 3) == "314"){
                if($("#bankNamePrefix").val()!="" && $("#bankName").val().indexOf($("#bankNamePrefix").val()) != -1){
                    dicts.get("difBank").val(0);
                }else{
                    dicts.get("difBank").val(1);
                }
            }else {
                dicts.get("difBank").val(0);
            }
            dicts.get("difBank").val(0);
		}else{
            dicts.get("difBank").val(1);
		}
		if($("#payerProv").val() == $("#payeeProv").val()
				&& $("#payerCity").val() == $("#payeeCity").val()){
            dicts.get("areaSign").val(0);
		}else{
            dicts.get("areaSign").val(1);
		}
        var difBank = bt.dicts.get('difBank').val();
        var isMustCode = $("#isMustCode").val();
        //跨行 or 同行且同行代发必填开户行行号
        if(difBank == "1" || (difBank == "0" && isMustCode == "1")){
            $('#bankCode').attr('lay-verify', 'required');
            $('#bankName').attr('lay-verify', 'required');
        }else{
            $('#bankCode').removeAttr("lay-verify");
            $('#bankName').removeAttr("lay-verify");
        }
        form.renderIcon();
		form.render();
    };
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
        var action = "/wage/niswagedetail/modify";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
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
