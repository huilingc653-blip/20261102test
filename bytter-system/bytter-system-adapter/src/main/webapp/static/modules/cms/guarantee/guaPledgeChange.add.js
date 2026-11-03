var dateTypes, moneyTypes, selectTypes, dicts;
var oldData = new Object();//刚进入页面的数据
var newData = new Object();//提交时取到的数据
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			selectTypes = bt.selectType.init();//下拉框
			dicts = bt.dicts.init();//字典
			bt.bankInfo.init();//联行号
			var corp = bt.corp.init();
			// 下拉框
			$.each($("div[type='list']"), function(i, item) {
				bt.select.init({
					elem : "[field='" + $(item).attr("field") + "']",
					name : $(item).attr("field"),
					defaultVal : $(item).attr("defaultVal"),
					valueField : $(item).attr("dictField"),
					textField : $(item).attr("dictText"),
					search : true,
					disabled : false,
					url : $(item).attr("fieldhref")
				});
			});
			//附件处理
			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});

			//监听提交
			form.on('submit', function(data) {
				//如果抵押数据已被担保合同关联,则给出提示
				var flag = $("#flag").val();
				var oldMoney = $("#oldMoney").val();
				var pledgeMoney = $("#pledgeMoney").val();
				if (flag=='1' && (oldMoney!=pledgeMoney)) {
					parent.layer.confirm(bt.lang('gua.guaPledgeChange.warn'), function() {
						toChange(data);
					});
				} else {
					toChange(data);
				}
				return false;
			});

			function toChange(data) {
				var action = "/cms/guarantee/pledgeChange/saveChange";
				data.field.status = $(this).attr("value");
				data.field.attachmentId = up.getGroupId();
				// if (corp.get('regCorp').data()[0].corpName) {
				// 	data.field.regCorpName = corp.get('regCorp').data()[0].corpName
				// }
				if($("#regCorpName").val() != 'undefined'){
					data.field.regCorpName = $("#regCorpName").val();
				}
				$.post(basePath + action, data.field, function(result) {
					bt.loading.hide();
					parent.layer.alert(result.msg, {
						icon : result.success ? 1 : 2
					}, function() {
						parent.layer.closeAll();
						if (result.success) {
							if (!bt.navtab) {
								location.reload();
								bt.navtab.refreshParentTable();
								parent.layer ? parent.layer.closeAll() : layer
										.closeAll();
							} else {
								if (data.field.id == "") {
									bt.navtab.refreshCurrent();
								} else {
									bt.navtab.closeCurrent();
								}
							}
						}
					})
				}, 'JSON');
				return false;
			}

			var setData = function(data) {
				oldData = data.field;//刚进入页面的数据
			}
            // 抵押信息的显示与隐藏
            mortgageInfoShowOrHide();
		});

//监听重置
resetCode = function() {
	var inputs = $('.rCode').find('input');
	inputs.each(function() {
		$(this).val("");
	})
}

checkBefore = function() {
	newData = data.field;//点击提交时的页面数据
	return true;
}

/**
 *抵押信息的显示与隐藏
 */
function mortgageInfoShowOrHide(){
    var pledgeAttr = $("#pledgeAttr").val();
    if (pledgeAttr == '1'){
        $(".mortgageInfo").hide();
    }else{
        $(".mortgageInfo").show();
    }
}

function corpRegCallback(ele) {
	$.get(basePath + "/cms/guarantee/pledgeInfo/getCenNetId?corpId="+ ele,function(data){
		if(data != null){
			$('#regCorpName').val(data.corpName);
		} else {
			$('#regCorpName').val('');
		}

	},'JSON');
}

