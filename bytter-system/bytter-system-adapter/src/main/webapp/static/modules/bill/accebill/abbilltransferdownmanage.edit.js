var corp,moneyType,recevieCorp;
bt.use(['form','layedit','btdate','corp','select','moneyType','dateType','jquery','accebillInfo','accebillView','accountTable','bank','btUpload','accebillInfoReceive'], function(){
    var $ = bt.jquery,form = bt.form();
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    //bt.accountTable.init();
    bt.bank.init({
        elem:'.bank'
    })
    //剩余金额 = 申请金额-下拨金额
    $("#surplusMoney").text(bt.money.format($("#applyMoney").val()-$("#allocationMoney").val()));
    var uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });
    /*//通过单位id加载出下拨中心账号
	bt.accountTable.get("recordBankacc").initCorp($("#netId").val(),"1");
	$("#recordBankacc").val($("#recordBankaccOld").val());
	//通过单位id加载出下拨单位账号
	bt.accountTable.get("recevieBankacc").initCorp($("#recevieCorpIdOld").val(),"1");
	$("#recevieBankacc").val($("#recevieBankaccOld").val());*/
	
	if(!$("#applyCode").val()){
		//收票单位初始化
        recevieCorp = bt.select.init({
	        elem:'#corpId',
	        name:"recevieCorpId",
	        defaultVal: $("#recevieCorpIdOld").val(),
	        valueField:"id",
	        textField:"name",
	        required: true,
	        search: true,
	        disabled: false,
	        url:basePath+"/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId="+$("#netId").val(),
	        change:function(data,text){
	        	corpCallback(data);
	        }
	    });
	}
    
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听保存
    form.on('submit(save)', function(data){
        submit(data,"/bill/accebill/abbilltransferdownmanage/save/update");
    });
    //监听提交
    form.on('submit(submit)', function(data){
        submit(data,"/bill/accebill/abbilltransferdownmanage/submit/update");
    });
    var submit = function(data,action){
        bt.loading.show();
        if(checkbillNo(data) && action == "/bill/accebill/abbilltransferdownmanage/submit/update"){
            bt.loading.hide();
            parent.layer.alert(bt.lang("bill.accebill.transferdownmanage.checkbillNo"),{icon: 2});//没有选择下拨票据
            return false;
        }
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                //bt.navtab.refreshParent();//刷新父选项卡
                bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
            }
        },'JSON');
        return false;
    }
    
    //选票点击触发事件
   // var accebillInfo = bt.accebillInfo.init();
    var accebillInfoReceive = bt.accebillInfoReceive.init();
    var accebillView = bt.accebillView.init();
    var ids;
    bt.accebillInfoParams=function(){
        if($("#netId").val()){
            if($("#recevieBankacc").val()){
                accebillInfoReceive.set({"corpId":$("#netId").val(),"bankAcc":$("#recordBankacc").val(),"trid":$("#trid").val(),"billBalance":$("#billBalance").val()});
            }else {
                //收票单位账号为空，则流通介质只能查询选择纸票
                accebillInfoReceive.set({"corpId":$("#netId").val(),"bankAcc":$("#recordBankacc").val(),
                    "billFlag":10,"billFlagName":bt.lang('table.abVoucherinfo.billFlag_10'),"trid":$("#trid").val(),"billBalance":$("#billBalance").val()});
            }
            return true;
        }else{
            parent.layer.alert(bt.lang("bill.accebill.transferdownmanage.isSelectBill"));//请先选择下拨中心
            return false;
        }
    }
    //选票回调函数
    bt.accebillInfoCallback = function(reData,dom){
    	var origNo,billAid,totalMoney,billBalance;
    	var datas=reData.data;
    	for(var i=0;i<datas.length;i++){
    		if(i==0){
    			origNo = datas[0].origNo+";"+datas[0].sbbllRngStrtSn+";"+datas[0].sbbllRngEndSn;
    			billAid = datas[0].id;
    			totalMoney = Number(datas[0].balance);
    			ids = datas[0].id;
                billBalance = datas[0].balance;
    		}else{
    			origNo = origNo +","+datas[i].origNo+";"+datas[i].sbbllRngStrtSn+";"+datas[i].sbbllRngEndSn;
        		billAid = billAid +","+datas[i].id;
        		totalMoney = Number(totalMoney)+Number(datas[i].balance);
        		ids = ids+","+datas[i].id;
                billBalance = billBalance + "," + datas[i].balance;
    		}
    	}
    	$("#billNoName").text(origNo);//选票票号
    	$("#billNo").val(origNo);
        $("#billBalance").val(billBalance);
    	$("#billAid").val(billAid);//票据流水id
    	$("#allocationMoneyName").text( bt.money.format(totalMoney));//下拨金额
        moneyType.get("allocationMoney").val(totalMoney);
        $("#surplusMoney").text(bt.money.format($("#applyMoney").val()-totalMoney));//剩余金额
        $("#oldIds").val(ids);
    } 
    //选票查看
    bt.accebillViewParams=function(){
        if(!ids){
            //修复编辑进入页面时，点选票查看-看不到票据问题
            ids = $("#aids").val();
        }
        accebillView.set({"id":ids,"billBalance":$("#billBalance").val()});
    	return true;
    }
    
});

//中心回调函数
function netCallback(netIdObj){
    var netId = netIdObj.value;
	var $ = bt.jquery;
	$("#netId").val(netId);
	/*$("#recordCorpName").val(corp.get("corpId").data().corpName);*/
	//下拨中心账号下拉框初始化
	//electricBill=1 票据专户
	bt.accountTable.get("recordBankacc").initCorp(netId,"1");
    //收票单位初始化
    recevieCorp = bt.select.init({
        elem:'#corpId',
        name:"recevieCorpId",
        valueField:"id",
        textField:"name",
        placeholder:{value:'',text:bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url:basePath+"/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId="+netId,
        change:function(data,text){
        	$("#recevieCorpName").val(text);
        	corpCallback(data);
        }
    });
	//收票单位账号
	$("#recevieBankacc").val("");
    //清除选票
    $("#billNoName").text("");//选票票号
    $("#billNo").val("");
    $("#billAid").val("");//票据流水id
    $("#allocationMoneyName").text("");//下拨金额
    moneyType.get("allocationMoney").val("");
    $("#surplusMoney").text("");//剩余金额
}

//单位回调函数
function corpCallback(data){
	var $ = bt.jquery;
    //下拨中心和收票单位不能相同
	if(data.value == $("#netId").val()){
        parent.layer.msg(bt.lang("bill.accebill.transferDownManage.corp.check"), {icon: 2});
        recevieCorp.setValue("");
        return;
    }
	//根据单位获取单位账号
	//electricBill=1 票据专户
	bt.accountTable.get("recevieBankacc").initCorp(data.value,"1");
}

//下拨中心账号回调方法
function recordBankaccCallBack(data){
	var $ = bt.jquery;
	$("#recordBankacc").val(data.bankAcc);
    //清除选票
    $("#billNoName").text("");//选票票号
    $("#billNo").val("");
    $("#billAid").val("");//票据流水id
    $("#allocationMoneyName").text("");//下拨金额
    moneyType.get("allocationMoney").val("");
    $("#surplusMoney").text("");//剩余金额
}

//下拨单位账号回调方法
function recevieBankaccCallBack(data){
	var $ = bt.jquery;
	$("#recevieBankacc").val(data.bankAcc);
}

function checkbillNo(data){
	if(!data.field.billNo){
		return true;
	}
}
