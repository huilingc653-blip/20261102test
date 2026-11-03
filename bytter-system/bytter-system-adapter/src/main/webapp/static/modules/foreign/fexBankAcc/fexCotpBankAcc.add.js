var dateTypes, moneyTypes, selectTypes, dicts,mulselect;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType','mulselect',
		'dicts', 'rateType', 'bankInfo','bank', 'btUpload', 'selectTable'],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			mulselect = bt.mulselect.init();
            bt.bank.init({
                elem:'.bank',
            });

			dicts = bt.dicts.init();//字典
			var corp = bt.corp.init();

			// 监听提交
			form.on('submit', function(data) {
				bt.loading.show();//显示加载中
				var action = "/foreign/fexcotpbankacc/tempOrSubmit";
				data.field.status = $(this).attr("value");
				$.post(basePath + action,data.field,function(result){
					parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
					bt.loading.hide(); //关闭加载中
					if(result.success){
						if(!bt.navtab){
							parent.layer?parent.layer.closeAll():layer.closeAll();
						} else {
							bt.navtab.closeCurrent();
							bt.navtab.refreshParentTable();
						}
					}
				},'JSON');
				return false;
			});

			//银行回调函数
			bankCallback = function(data){
				$("#bicCode").val(data.biccode);
				$("#bankAddr").val(data.postaddr);
				$("#localClearNo").val(data.localclearno);
			}

			//中转行回调函数
			tranBankCallback = function(data){
				$("#tranBicCode").val(data.biccode);
				$("#tranBankAddr").val(data.postaddr);
			}
			selectTypes = bt.selectType.init();//下拉框
	});