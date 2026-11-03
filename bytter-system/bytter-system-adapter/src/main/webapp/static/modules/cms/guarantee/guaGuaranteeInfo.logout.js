var dateTypes, moneyTypes, selectTypes, dicts,corp ;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType','dicts','btUpload' ],function() {
		$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
		dateTypes = bt.dateType.init();//时间
		var gInfoId = $("#annexNameInfo").val();
		var gSendId = $("#annexNameSend").val();
	    bt.btUpload.init({
	        elem: "#uploaderInfo",
	        hasUpload: false,
	        hasDelete: false,
	        isExpand: true,
	        gid: gInfoId
	    });
	    
	    bt.btUpload.init({
	        elem: "#uploaderSend",
	        hasUpload: false,
	        hasDelete: false,
	        isExpand: true,
	        gid: gSendId
	    }); 
		
		//监听提交
		form.on('submit', function(data) {
	    	bt.loading.show();//显示加载中
			var action = "/letter/guarantee/guaGuaranteeInfo/logout";
			$.post(basePath + action, data.field, function(result) {
				bt.loading.hide();
				parent.layer.alert(result.msg, {icon : result.success ? 1 : 2}, function() {
					parent.layer.closeAll();
					if (result.success) {
				        if(!bt.navtab){
		                    location.reload();
		                    bt.navtab.refreshParentTable();
		                    parent.layer?parent.layer.closeAll():layer.closeAll();
		                    } else {
		                      if(data.field.id == ""){
		                        bt.navtab.refreshCurrent();
		                      }else{
		                        bt.navtab.closeCurrent();
		                      }
		                  }
					}
				})
			}, 'JSON');
			return false;
	});
});

//查看授信
function viewRatCode(){
		var params = beforeRation();
		if(bt.empty(params)){
			return;
		}
		viewRationDtl(params);
	}
	
	/**
	 * 授信额度选择
	 * @param params
	 */
	function beforeRation(){
		var corpId = $("#corpId").val();
		if(bt.empty(corpId) || corpId == 0){
			//选择保函申请人
			parent.layer.msg(bt.lang("cms.gua.guarantee.corpIdnull"), {icon: 2});
			return "";
		}
		var curId = $("#curId").val();
		if(bt.empty(curId)){
			//选择币别
			parent.layer.msg(bt.lang("cms.gua.guarantee.curIdnull"), {icon: 2});
			return "";
		}
		var bankCode = $("#openCorp").val();//银行
		if(bt.empty(bankCode.trim())){
			//选择保函开立人
			parent.layer.msg(bt.lang("cms.gua.guarantee.bankIdnull"), {icon: 2});
			return "";
		}
		var startDate = $("#startDate").val();//保函开始日期
		var ratItemCode = $("#ratItemCode").val();//授信品种
		var params = {
				corpId : corpId,
				curCode : curId,
				bankCode : bankCode,
				ratItemCode : ratItemCode.replace(" ", ""),
				beginDate : startDate.replace(" ", "")
		}
		return params;
	}