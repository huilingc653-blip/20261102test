var dateTypes, moneyTypes, selectTypes, dicts;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType','bank',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable','rateType' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			selectTypes = bt.selectType.init();//下拉框
			 rateTypes = bt.rateType.init();//
			dicts = bt.dicts.init();//字典
			bt.bankInfo.init();//联行号
			var corp = bt.corp.init();
			bt.bank.init({
		        elem:'.bank',   
		    });
			
			// 附件处理
			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});
			
			//领取
			form.on('submit', function(data) {
				var action = "/letter/guarantee/guaPerformReg/save";
				data.field.attachmentId = up.getGroupId();
				$.post(basePath + action, data.field, function(result) {
					bt.loading.hide();
					parent.layer.alert(result.msg, {
						icon : result.success ? 1 : 2
					}, function() {
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
    var curId = $("#ratCurId").val();
    var bankCode = $("#openCorp").val();//银行
    var startDate = $("#startDate").val();//保函开始日期
    var ratItemCode = $("#ratItemCode").val();//授信品种
    var beginDate=bt.date.format(startDate, "yyyy-mm-dd")
    var params = {
        corpId : corpId,
        curCode : curId,
        bankCode : bankCode,
        ratItemCode : ratItemCode.replace(" ", ""),
        beginDate : beginDate
    }
    return params;
}