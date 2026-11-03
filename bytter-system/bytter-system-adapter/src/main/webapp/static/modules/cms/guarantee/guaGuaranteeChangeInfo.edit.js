var dateTypes, moneyTypes, selectTypes, dicts,lgInfoInit;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			selectTypes = bt.selectType.init();//下拉框
			dicts = bt.dicts.init();//字典
			bt.bankInfo.init();//联行号
			
			/**判断字符串是否为空*/
		    bt.empty = function(ms){
		    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
		    		return true;
		    	}
		    	return false;
		    }
		    
		    //保函申请信息列表
		    lgInfoInit = bt.selectTable.init({
		        elem: "#lgInfoTable",
		        url: basePath + "/letter/guarantee/guaGuaranteeChangeInfo/queryLgInfo",
		        urldata: {corpId: $("#applyCorp").val(),guaranteeNo:$("#guaranteeNo").val()},
		        scrollX: true,
		        scrollY: "200px",
		        searchInput: "#guaranteeNo",
		        paging: true,
		        columns: [
		            {data: "guaranteeNo",},
		            {data: "guaranteeType",
		            	render: function ( data, type, full, meta ) {
	                          return bt.dict("guarantee.Lg.TypeAttr",full.guaranteeType);
	                      }
		            },
		            {data: "openCorpName",},
		            {data: "incomeCorp",},
		            {data:"guaranteeMoney",}
		        ],
		        clickCallback: function (data) {
		        	var guaranid=data.id;
		        	if (data) {
		                 $.ajax({
		                     type: "POST",
		                     url:basePath + "/letter/guarantee/guaGuaranteeChangeInfo/queryLgInfoById",
		                     dataType: 'json',
		                     async: false,
		                     data: {"guaranid": guaranid},//保函申请id
		                     success: function (ret) {//ret为返回的GuaGuaranteeInfoVO对象
		                         if (ret!=null) {                     
		                        	 $('#guaraId').val(ret.id); //保函申请id
				                    	$('#guaranteeNo').val(ret.guaranteeNo);//保函编号
				                        bt.dicts.init().get("guaranteeType").val(ret.guaranteeType)//保函类型
				                        bt.dicts.init().get("openCorpType").val(ret.openCorpType)//开立人类型
				                        bt.dicts.init().get("openWay").val(ret.openWay)//开立方式
				                        bt.dicts.init().get("openType").val(ret.openType)//开立类型
				                        $('#bankCode').val(ret.openCorp); //保函开立人(联行号)
				                        $('#openCorpName').val(ret.openCorpName); //保函开立人名称
				                        $('#incomeCorp').val(ret.incomeCorp); //保函受益人
				                        $('#curId').val(ret.curId); //币别id
				                        $('.curName').val(ret.curName); //币别名称
				                        bt.dicts.init().get("guaranteeAttr").val(ret.guaranteeAttr)//保函性质
				                        $('#expectEndDateOld').val(bt.date.format(ret.expectEndDate)); //原保函预计结束时间
				                        $('#expectEndDate').val(bt.date.format(ret.expectEndDate)); //保函新预计结束时间
				                        $('#startDate').val(bt.date.format(ret.startDate)); //保函开始时间
				                        $('#endDateOld').val(bt.date.format(ret.endDate)); //原保函截止时间
				                        $('#endDate').val(bt.date.format(ret.endDate)); //保函新截止时间
				                        $('#changeCount').val(ret.changeCount); //更改次数				      
				                        moneyTypes.get("guaranteeMoneyOld").val(ret.guaranteeMoney);//调整前保函金额
				                        $("#guaOldMoney").val(ret.guaranteeMoney);////调整前保函金额用于暂存或提交时判断授信额度
				                        moneyTypes.get("guaranteeMoney").val(ret.guaranteeMoney);//调整后保函金额
				                        $("#bondMoneyOld").val(ret.bondMoney);//调整前保函保证金金额
				                        moneyTypes.get("bondMoney").val(ret.bondMoney);//调整后保函保证金金额
				                        var ratModel = ret.ratmodel;
				                        if(ratModel=='1'){
				                        	ratInfo(ret.id);//授信信息显示
				                       }
				                       if(ratModel=='0'){
				                    	   //授信信息隐藏
				                    	   $("#isShowRatInfo").hide();
				                       }
		                         }
		                     }
		                 });  
		            } else {
		                $('input.text').val('');
		            }
		        }
		    });
		    
			//单位回调函数
			bt.corpCallback = function(id) {
				//获取单位信息
				$.ajax({
					url : basePath + "/sys/corp/getSysCorp",
					method : 'POST',
					data : {
						id : id
					},
					dataType : 'json',
					success : function(data) {
						if (data.obj) {
							if (data.obj.userAccountCode) {
								$("#applyCorp").val(data.obj.id);
							}
							//重新加载保函申请列表
								lgInfoInit.get("guaranteeNo").reload({
									corpId : $("#applyCorp").val()
								});	
							//清空保函申请信息
								  $('#guaraId').val(""); //保函申请id
			                      $('#guaranteeNo').val("");//保函编号
			                      bt.dicts.init().get("guaranteeType").val("")//保函类型
			                      bt.dicts.init().get("openCorpType").val("")//开立人类型
			                      bt.dicts.init().get("openWay").val("")//开立方式
			                      bt.dicts.init().get("openType").val("")//开立类型
			                      $('#bankCode').val(""); //保函开立人(联行号)
			                      $('#openCorpName').val(""); //保函开立人名称
			                      $('#incomeCorp').val(""); //保函受益人
			                      $('#curId').val(""); //币别id
			                      $('.curName').val(""); //币别名称
			                      bt.dicts.init().get("guaranteeAttr").val("")//保函性质
			                      $('#expectEndDateOld').val(""); //原保函预计结束时间
			                      $('#expectEndDate').val(""); //保函新预计结束时间
			                      $('#startDate').val(""); //保函开始时间 
			                      $('#endDateOld').val(""); //原保函截止时间 
			                      $('#endDate').val(""); //保函新截止时间 
			                      $('#changeCount').val(""); //更改次数
                                  moneyTypes.get("guaranteeMoneyOld").val("");//调整前保函金额
                                  moneyTypes.get("guaranteeMoney").val("");//调整后保函金额
                                  $("#bondMoneyOld").val("");//调整前保函保证金金额
                                  moneyTypes.get("bondMoney").val("");//调整后保函保证金金额
						}
						form.render();
					}
				});
			   //授信信息隐藏
			   $("#isShowRatInfo").hide();
			};
			var corp = bt.corp.init();
			//附件处理
			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});
			
		    var checkBefore = function(){
		    	if(!checkExpectEndDate()){
		    		layer.alert(bt.lang("table.guaGuarantee.checkExpectEndDate"));
		    		return false;
		    	}
		    	if(!checkNewEndDate()){
		    		layer.alert(bt.lang("table.guaGuarantee.checkEndDate"));//截止日期不能小于开始日期！
		    		return false;
		    	}
		    	return true;
		    }
			
			//监听提交
			form.on('submit', function(data) {
		    	bt.loading.show();//显示加载中
		    	if(!checkBefore()){
		    		bt.loading.hide(); //关闭加载中
		    		return false;
		    	}
		    	//判断调整后的保函金额是否大于0,小于0给出提示
		    	var guaranteeMoney = moneyTypes.get("guaranteeMoney").val();
		    	if(guaranteeMoney<=0){
		    		parent.layer.msg(bt.lang("gua.guarantee.changeMoney"), {icon: 2});
    				bt.loading.hide(); //关闭加载中
    				return false;
		    	}
		    	// var isRat=$("#isRat").val();
		    	// if(isRat=="1"){//挂接授信时做此判断
                //     //判断保函变更后的额度是否超出了授信额度，超出了则给予提示
                //     var bondChangeType = dicts.get('bondChangeType').val();
                //     if(bondChangeType=='0'){
                //         var guOldMoney = $("#guaOldMoney").val();
                //         var guaChangeMoney = bt.money.sub(guaranteeMoney,guOldMoney);//保函前后变化的额度
                //         var itemMoney = $("#itemMoney").val();
                //         if(bt.money.sub(guaChangeMoney, itemMoney) > 0){
                //             parent.layer.msg(bt.lang("gua.guarantee.changeRatMoney"), {icon: 2});
                //             bt.loading.hide(); //关闭加载中
                //             return false;
                //         }
                //     }
                //     //授信额度判断结束
				// }

				var action = "/letter/guarantee/guaGuaranteeChangeInfo/tempOrSubmit";
				data.field.status = $(this).attr("value");
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
	
    //开立方式
	function openWayCallback(){
		var openWay=bt.dicts.init().get("openWay").val();
		if("1"==openWay){
			$(".isShow").show();
			$("#turnBank").attr('lay-verify','required');
		}else{
			$(".isShow").hide();
			$("#turnBank").removeAttr('lay-verify');;
		}
		form.renderIcon();
	}
	
	/**
	 * 得到两个日期之间的天数
	 * @param startDate
	 * @param endDate
	 */
	function getBetweenDay(startDate,endDate){
		var date1 = bt.date.get(startDate);
		var date2 = bt.date.get(endDate);
		var btt = date2 - date1;
		return btt/(1000*60*60*24);
	}
	
	//检验新预计结束日期
	function checkExpectEndDate(){
    	var startDate = $("#startDate").val();
    	if(bt.empty(startDate)){
    		return true;
    	}
    	var expectEndDate = $("#expectEndDate").val();
    	if(bt.empty(expectEndDate)){
    		return true;
    	}
    	var days = getBetweenDay(startDate, expectEndDate) - (-1);
    	if(days>0){
    		return true;
    	}else{
    		return false;
    	}
	}
	//检验新截止日期
	function checkNewEndDate(){
    	var startDate = $("#startDate").val();
    	if(bt.empty(startDate)){
    		return true;
    	}
    	var endDate = $("#endDate").val();
    	if(bt.empty(endDate)){
    		return true;
    	}
    	var days = getBetweenDay(startDate,endDate) - (-1);
    	if(days>0){
    		return true;
    	}else{
    		return false;
    	}
	}

	//循环计算保函金额
	function calLgMoney(){
		var guaranteeMoney;//调整后的保函金额
	    var guaChangeMoney = bt.money.format($("#guaChangeMoney").val());//调整金额
	    var guaranteeMoneyOld = bt.money.format($("#guaOldMoney").val()); //原保函金额
	    var guaChangeType=bt.dicts.init().get("guaChangeType").val();//调整方式
	    if(guaChangeType==0){//调增
		    if(guaChangeMoney==0){
                moneyTypes.get("guaranteeMoney").val(guaranteeMoneyOld);
		    }else{
		    	guaranteeMoney=bt.money.add(guaranteeMoneyOld,guaChangeMoney);
                if(guaranteeMoney<0){
                    parent.layer.msg(bt.lang("gua.guarantee.checkLgChangeMoney"), {icon: 2});
                    moneyTypes.get("guaranteeMoney").val(guaranteeMoneyOld);
                    moneyTypes.get("guaChangeMoney").val(0);
                }else{
                    moneyTypes.get("guaranteeMoney").val(guaranteeMoney);
                }
		    }
	    }else{//调减
		    if(guaChangeMoney==0){
		    	moneyTypes.get("guaranteeMoney").val(guaranteeMoneyOld);
		    }else{
		    	guaranteeMoney=bt.money.sub(guaranteeMoneyOld,guaChangeMoney);
                if(guaranteeMoney<0){
                    parent.layer.msg(bt.lang("gua.guarantee.checkLgChangeMoney"), {icon: 2});
                    moneyTypes.get("guaranteeMoney").val(guaranteeMoneyOld);
                    moneyTypes.get("guaChangeMoney").val(0);
                }else{
                    moneyTypes.get("guaranteeMoney").val(guaranteeMoney);
                }
		    }
	    }
	}
	
	//循环计算保函保证金金额
	function calLgBondMoney(){
		var bondMoney;//调整后的保函保证金金额
	    var bondChangeMoney = bt.money.format($("#bondChangeMoney").val());//调整金额
	    var bondMoneyOld = bt.money.format($("#bondMoneyOld").val()); //原保函金额
	    var bondChangeType=bt.dicts.init().get("bondChangeType").val();//保证金调整方式
	    if(bondChangeType==0){//调增
		    if(bondChangeMoney==0){
		    	moneyTypes.get("bondMoney").val(bondMoneyOld);
		    }else{
		    	bondMoney=bt.money.add(bondMoneyOld,bondChangeMoney);
		    	if(bondMoney<0){
		    		if(bondMoneyOld!=0){
                        parent.layer.msg(bt.lang("gua.guarantee.checkLgChangeMoney"), {icon: 2});
					}
		    		moneyTypes.get("bondMoney").val(bondMoneyOld);
		    		moneyTypes.get("bondChangeMoney").val(0);
		    	}else{
		    		moneyTypes.get("bondMoney").val(bondMoney);
		    	}
		    }
	    }else{//调减
		    if(bondChangeMoney==0){//调整金额为零时
		    	moneyTypes.get("bondMoney").val(bondMoneyOld);
		    }else{
		    	bondMoney=bt.money.sub(bondMoneyOld,bondChangeMoney);
		    	if(bondMoney<0){
                    if(bondMoneyOld!=0){
                        parent.layer.msg(bt.lang("gua.guarantee.checkLgChangeMoney"), {icon: 2});
                    }
		    		moneyTypes.get("bondMoney").val(bondMoneyOld);
		    		moneyTypes.get("bondChangeMoney").val(0);
		    	}else{
		        	moneyTypes.get("bondMoney").val(bondMoney);
		    	}
		    }
	    }
	}
	
	//授信信息
	function ratInfo(id){
		$("#isShowRatInfo").show();
		$.ajax({
    		url:basePath + "/letter/guarantee/guaGuaranteeChangeInfo/queryRatUration",
    		method:'POST',
    		data:{
    			id:id
    		},
    		dataType:'json',
    		success:function(rat){
    			var itemFlag = rat.itemFlag;
    			var html = '<input type="hidden" name="ratCode" id="ratCode" value="' + rat.ratCode + '">';
    			html += '<input type="hidden" name="itemFlag" id="itemFlag" value="' + rat.itemFlag + '">';
    			html += '<input type="hidden" name="itemMoney" id="itemMoney" value="' + rat.money + '">';
    			html += '<input type="hidden" name="ratItemCode" id="ratItemCode" value="' + rat.ratItemCode + '">';
    			
    			//授信品种名
    			$("#itemCode").text(rat.ratItemName);
    			
    			if(itemFlag == "0"){
    				//html += '<input type="hidden" name="ratItemCode" id="ratItemCode" value="' + itemCode + '">';
    			} else {
    				//循环设置综合品种
    				var rationDlts = rat.applyDtls;
    				if(rationDlts && rationDlts.length > 0){
    					var length = rationDlts.length;
    					html += '<input type="hidden" name="applyDtlsLength" id="applyDtlsLength" value="' + length + '">';
    					for(var i = 0; i < length; i++){
    						var rationDlt = rationDlts[i];
    						html += '<input type="hidden" name="applyDtls['+i+'].itemCode" id="itemCode'+i+'" value="' + rationDlt.itemCode + '">';
    						html += '<input type="hidden" name="applyDtls['+i+'].itemMoney" itemCode="' + rationDlt.itemCode + '" id="itemMoney'+i+'" value="0">';
    					}
    				}
    			}
    			$("#itemCodeTd").html(html);
    		}
    	});
	}
	
	/**
	 * 授信额度选择
	 * @param params
	 */
	function beforeRation(){
		var corpId = $("#applyCorp").val();
		if(bt.empty(corpId) || corpId == 0){
			//选择保函申请人
			parent.layer.msg(bt.lang("cms.gua.guarantee.corpIdnull"), {icon: 2});
			return "";
		}
		var curId = $("#ratCurId").val();
		if(bt.empty(curId)){
			//选择币别
			parent.layer.msg(bt.lang("cms.gua.guarantee.curIdnull"), {icon: 2});
			return "";
		}
		var bankCode = $("#bankCode").val();//银行
		if(bt.empty(bankCode.trim())){
			//选择保函开立人
			parent.layer.msg(bt.lang("cms.gua.guarantee.bankIdnull"), {icon: 2});
			return "";
		}
		var ratItemCode = $("#ratItemCode").val();//授信品种
		
		var beginDate = bt.dateType.init().get("startDate").val();
		var params = {
				corpId : corpId,
				curCode : curId,
				bankCode : bankCode,
				ratItemCode : ratItemCode.replace(" ", ""),
				beginDate : beginDate.replace(" ", "")
		}
		return params;
	}
	
	//查看授信
	function viewRatCode(){
		var params = beforeRation();
		if(bt.empty(params)){
			return;
		}
		viewRationDtl(params);
	}
	
