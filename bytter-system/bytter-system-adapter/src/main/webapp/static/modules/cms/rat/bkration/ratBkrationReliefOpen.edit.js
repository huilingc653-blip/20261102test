var rowMR=0;
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
var corpInit;
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money'], function(){
    var $ = bt.jquery,form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //调剂品种类型监听
    form.on('select(reliefItemFlag)', function(data){
    	var corpId = $("#reliefCorp").val();
    	var ratCode = $("#ratCode").val();
    	bt.setReliefItem(ratCode, corpId, data.value);
    });
    
    //调剂单品种监听
    form.on('select(reliefItemCode)', function(data){
    	var corpId = $("#reliefCorp").val();
    	var ratCode = $("#ratCode").val();
    	var itemFlag = $("select[name='reliefItemFlag']").val();
    	bt.getItemMoney(ratCode, corpId, itemFlag, data.value);
    });
    
    //选择授信调剂单位修改已分配的授信品种类型
    bt.setItemFlag = function(corpId) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var ratCode = $("#ratCode").val();

    	$.post(basePath + "/cms/rat/ratbkrationrelief/getReliefItemFlag", {
    		ratCode : ratCode,
			corpId : corpId
		}, function(data) {
			$("#reliefItemFlag").empty();
			if(data && data.length>0){
				$.each(data,function(index,item){
					$("#reliefItemFlag").append("<option value="+item.itemFlag+">"+bt.dict("RatModel.ItemFlag", item.itemFlag)+"</option>");
				});
				var itemFlag = data[0].itemFlag;
				$("select[name='reliefItemFlag']").val(itemFlag);
				bt.setReliefItem(ratCode, corpId, itemFlag);
			}else{
				$("#reliefItemFlag").append("<option value=''></option>");
			}
			form.render();
		}, 'JSON');
    }
    
    bt.setReliefItem = function(ratCode, corpId, itemFlag) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	$.post(basePath + "/cms/rat/ratbkrationrelief/getReliefItem", {
    		ratCode : ratCode,
			corpId : corpId,
			itemFlag : itemFlag
		}, function(data) {
			$("#reliefItemCode").empty();
			if(data && data.length>0){
				$("#reliefCode").val(data[0].disCode);
				moneyType.get("reliefMoney").val("0");
				moneyType.get("reliefOpenMoney").val("0");
				if("0" == itemFlag){
					var html = '<input type="hidden" name="reliefDtls[' + data[0].itemOrder + '].itemOrder" value="'+data[0].itemOrder+'">';
					html += '<div class="layui-input-inline">' +
							'<select name="reliefDtls[' + data[0].itemOrder + '].reliefItemCode" id="reliefItemCode" lay-verify="required" lay-search="" lay-filter="reliefItemCode">';
					$.each(data,function(index,item){
						html += '<option value="' + item.itemCode + '">' + item.itemName + '</option>';
					});
					html += '</select></div>';
					$("#reliefItemCodeTd").html(html);
					$("input[data-id='reliefMoney']").removeAttr("readonly");
				} else {
					//综合品种
					var html = '<table class="layui-table" width="100%" style="margin:0px">';
					html += '<tr>' +
							'<th>' + bt.lang("table.ratBkrationDetail.itemCode") + '</th>' +
							'<th>' + bt.lang("table.ratBkrationDisDetail.leftDisMoney") + '</th>' +
							'<th>' + bt.lang("table.ratBkrationRelief.byReliefMoney") + '</th>' +
							'</tr>';
					$.each(data,function(index,item){
						html += '<tr>' +
								'<td><input type="hidden" name="reliefDtls[' + item.itemOrder + '].itemOrder" value="'+item.itemOrder+'">' +
								'<input type="hidden" name="reliefDtls[' + item.itemOrder + '].reliefItemCode" value="'+item.itemCode+'">' + item.itemName + '</td>' +
								'<td align="right">' + bt.money.format(item.maxMoney) + '</td>' +
								'<td align="right"><input type="text" data-name="reliefDtls[' + item.itemOrder + '].reliefMaxMoney" rowindex="' + item.itemOrder + '" data-biz-name="reliefMaxMoney' + item.itemOrder + '" id="reliefMaxMoney' 
								+ item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.checkReliefMoney"></td>' +
								'</tr>';
					});
					html += '</table>';
					$("#reliefItemCodeTd").html(html);
					$("input[data-id='reliefMoney']").attr("readonly", "readonly");
				}
				//获取授信品种的调剂额度
				var itemCode = data[0].itemCode;
				bt.getItemMoney(ratCode, corpId, itemFlag, itemCode);
				bt.moneyType.init();
				form.render();
			}else{
				$("#reliefItemCode").append("<option value=''></option>");
			}
			form.render();
		}, 'JSON');
    }
    
    //获取已分配的授信品种
    bt.getItemMoney = function(ratCode, corpId, itemFlag, itemCode){
    	$.post(basePath + "/cms/rat/ratbkrationrelief/getDisItemMoney", {
    		ratCode : ratCode,
			corpId : corpId,
			itemFlag : itemFlag,
			itemCode : itemCode
		}, function(data) {
			moneyType.get("byReliefMoney").val(data.money);
			moneyType.get("byReliefOpenMoney").val(data.openMoney);
		}, 'JSON');
    }
    
    //调剂品种类型监听
    form.on('select(byReliefItemFlag)', function(data){
    	var corpId = $("#byReliefCorp").val();
    	var ratCode = $("#ratCode").val();
    	bt.setByReliefItem(ratCode, corpId, data.value);
    });
    
    //调剂单品种监听
    form.on('select(byReliefItemCode)', function(data){
    	var ratCode = $("#ratCode").val();
    	bt.getItemAssure(ratCode, data.value);
    });
    
    //选择授信调剂单位修改已分配的授信品种类型
    bt.setByItemFlag = function(corpId) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var ratCode = $("#ratCode").val();

    	$.post(basePath + "/cms/rat/ratbkrationrelief/getReliefItemFlag", {
    		ratCode : ratCode,
			corpId : corpId
		}, function(data) {
			$("#byReliefItemFlag").empty();
			if(data && data.length>0){
				$.each(data,function(index,item){
					$("#byReliefItemFlag").append("<option value="+item.itemFlag+">"+bt.dict("RatModel.ItemFlag", item.itemFlag)+"</option>");
				});
				var itemFlag = data[0].itemFlag;
				$("select[name='byReliefItemFlag']").val(itemFlag);
				bt.setByReliefItem(ratCode, corpId, itemFlag);
			}else{
				$("#byReliefItemFlag").append("<option value=''></option>");
			}
			form.render();
		}, 'JSON');
    }
    
    //被调剂方品种
    bt.setByReliefItem = function(ratCode, corpId, itemFlag) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	$.post(basePath + "/cms/rat/ratbkrationrelief/getReliefItem", {
    		ratCode : ratCode,
			corpId : corpId,
			itemFlag : itemFlag
		}, function(data) {
			$("#byReliefItemCode").empty();
			if(data && data.length>0){
				$("#byReliefCode").val(data[0].disCode);
				var itemFlag = data[0].itemFlag;
				if("0" == itemFlag){
					var html = '<input type="hidden" name="reliefDtls[' + data[0].itemOrder + '].byItemOrder" value="'+data[0].itemOrder+'">';
					html += '<div class="layui-input-inline">' +
							'<select name="reliefDtls[' + data[0].itemOrder + '].byReliefItemCode" id="byReliefItemCode" lay-verify="required" lay-search="" lay-filter="byReliefItemCode">';
					$.each(data,function(index,item){
						html += '<option value="' + item.itemCode + '">' + item.itemName + '</option>';
					});
					html += '</select></div>';
					$("#byReliefItemCodeTd").html(html);
					bt.getItemAssure(ratCode, data[0].itemCode);
				} else {
					//综合品种
					var html = '<table class="layui-table" width="100%" style="margin:0px">';
					html += '<tr>' +
							'<th>' + bt.lang("table.ratBkrationDetail.itemCode") + '</th>' +
							'<th>' + bt.lang("table.ratBkrationDisDetail.leftDisMoney") + '</th>' +
							'<th>' + bt.lang("table.ratBkrationRelief.canReliefMoney") + '</th>' +
							'</tr>';
					$.each(data,function(index,item){
						html += '<tr>' +
								'<td><input type="hidden" name="reliefDtls[' + item.itemOrder + '].byItemOrder" value="'+item.itemOrder+'">' +
								'<input type="hidden" name="reliefDtls[' + item.itemOrder + '].byReliefItemCode" value="'+item.itemCode+'">' + item.itemName + '</td>' +
								'<td align="right">' + bt.money.format(item.maxMoney) + '</td>' +
								'<td align="right"><input type="text" data-name="reliefDtls[' + item.itemOrder + '].byReliefMaxMoney" data-biz-name="byReliefMaxMoney' + item.itemOrder + '" id="byReliefMaxMoney' + item.itemOrder + '" class="layui-input bt-biz-moneyType" data-decimal="2" data-minus="false" data-callback="bt.checkMoney"></td>' +
								'</tr>';
						if("1" == item.compSign){
							$("#assurepercent").val(item.assurepercent);
							$("#interestrate").val(item.interestrate);
						}
					});
					html += '</table>';
					$("#byReliefItemCodeTd").html(html);
				}
				bt.moneyType.init();
				form.render();
			}else{
				$("#byReliefItemCode").append("<option value=''></option>");
			}
			form.render();
		}, 'JSON');
    }

    //获取授信品种的保证金比例和利率
    bt.getItemAssure = function(ratCode, itemCode) {
    	var ratCode = $("#ratCode").val();
    	$.post(basePath + "/cms/rat/ratbkrationrelief/getItemAssure", {
    		ratCode : ratCode,
			itemCode : itemCode
		}, function(data) {
			if(data){
				$("#assurepercent").val(data.assurepercent);
				$("#interestrate").val(data.interestrate);
			}
		}, 'JSON');
    	
    }
    
    //检查最高额度与授信额度关系
    bt.checkMoney = function(data, el){
    	var reliefMoney = moneyType.get("reliefMoney").val();
    	if(bt.empty(reliefMoney)){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefMoneyNull"));
			moneyType.get("reliefMoney").val("0");
    	}
    	if(bt.money.sub(data, reliefMoney) > 0){
    		layer.alert(bt.lang("table.ratbkrationRelief.maxMoneyLager"));
    		var dataName = $(el).attr("data-biz-name");
    		moneyType.get(dataName).val("0");
    		return;
    	}
    }
    
    bt.calMoney = function(data, el){
    	//检查调剂品种是否是综合品种
    	var reliefItemFlag = $("select[name='reliefItemFlag']").val();
    	if(bt.empty(reliefItemFlag)){
    		layer.alert(bt.lang("table.ratbkrationRelief.reliefItemFlagNull"));
    		moneyType.get("reliefMoney").val("0");
    		return false;
    	}
    	//综合品种录入调剂品种额度
    	if("1" == reliefItemFlag){
    		
    	} else {
    		if(!bt.commonCheck()){
    			return false;
    		}
    	}
    	bt.calIndexOpenMoney(data);
    }
    
    //根据授信额度和保证金比例费率计算敞口额度
    bt.calIndexOpenMoney = function(moneyIndexVal) {
    	var itemFlag = $("select[name='byReliefItemFlag']").val();
    	if("0" == itemFlag){
    		var assurepercent = $("#assurepercent").val();
    		//授信额度*（1-保证金比例/100）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(assurepercent, 1)));
    		moneyType.get("reliefOpenMoney").val(bt.money.format(moneyVal));
    	} else {
    		var interestrate = $("#interestrate").val();
    		//授信额度*（1-利率/费率）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(interestrate, 1)));
    		moneyType.get("reliefOpenMoney").val(bt.money.format(moneyVal));
    	}
    	bt.checkReliefOpenMoney();
    }
    
    //比较授信敞口额度
    bt.checkReliefOpenMoney = function() {
    	var reliefOpenMoney = moneyType.get("reliefOpenMoney").val();//被调剂敞口额度
    	var byReliefOpenMoney = moneyType.get("byReliefOpenMoney").val();//调剂敞口额度
    	if(bt.money.sub(reliefOpenMoney, byReliefOpenMoney) > 0){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefOpenMoneyLager"));
    		moneyType.get("reliefMoney").val("0");
    		moneyType.get("reliefOpenMoney").val("0");
    		return false;
    	}
    }
    
    //检查调剂额度
    bt.checkReliefMoney = function(data, el){
    	var sumMoney = 0;
    	$.each($("input[data-id^='reliefMaxMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	//比较可调剂额度
    	if(!bt.compReliefMoney(sumMoney)){
    		var rowindex = $(el).attr("rowindex");
    		moneyType.get("reliefMaxMoney"+rowindex).val("0");
    		var sumMoney2 = 0;
    		$.each($("input[data-id^='reliefMaxMoney']"), function(i,item){
    			var index = $(item).attr("rowindex");
    			if(rowindex - index != 0){
    				sumMoney2 = bt.money.add(sumMoney2, $(item).val());
    			}
    			moneyType.get("reliefMoney").val(sumMoney2);
    			bt.calIndexOpenMoney(sumMoney2);
            });
    		
			return false;
		}
    	moneyType.get("reliefMoney").val(sumMoney);
    	bt.calIndexOpenMoney(sumMoney);
    }
    
    //比较授信调剂额度与被调剂额度
    bt.compReliefMoney = function(money){
    	//比较可调剂额度
    	var byReliefMoney = moneyType.get("byReliefMoney").val();
    	if(bt.empty(byReliefMoney)){
    		layer.alert(bt.lang("table.ratbkrationRelief.reliefMoneyNull"));
			moneyType.get("reliefMoney").val("0");
			return false;
    	}
    	var reliefMoney = money;
    	if(!bt.empty(money)){
    		reliefMoney = money;
    	} else {
    		reliefMoney = moneyType.get("reliefMoney").val();
    	}
    	if(bt.empty(reliefMoney)){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefMoneyNull"));
			moneyType.get("reliefMoney").val("0");
			return false;
    	}
    	if(bt.money.sub(reliefMoney, byReliefMoney) > 0){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefMoneyLager"));
    		moneyType.get("reliefMoney").val("0");
    		return false;
    	}
    	return true;
    }
    
    bt.commonCheck = function(){
    	var byCorpId = $("#byReliefCorp").val();
    	if(bt.empty(byCorpId)){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefCorpNull"));
    		moneyType.get("reliefMoney").val("0");
    		return false;
    	}
    	var byReliefItemFlag = $("select[name='byReliefItemFlag']").val();
    	if(bt.empty(byReliefItemFlag)){
    		layer.alert(bt.lang("table.ratbkrationRelief.byReliefItemFlagNull"));
    		moneyType.get("reliefMoney").val("0");
    		return false;
    	}
    	
    	if("0" == byReliefItemFlag){
    		var byReliefItemCode = $("select[id='byReliefItemCode']").val();
    		if(bt.empty(byReliefItemCode)){
    			layer.alert(bt.lang("table.ratbkrationRelief.byReliefItemNull"));
    			moneyType.get("reliefMoney").val("0");
    			return false;
    		}
    	}
    	var reliefMoney = moneyType.get("reliefMoney").val();
    	//比较可调剂额度
    	if(!bt.compReliefMoney(reliefMoney)){
			return false;
		}
    	
    	//比较已录入的授信调剂最高额度
    	var reliefItemFlag = $("select[name='reliefItemFlag']").val();
    	if(bt.empty(reliefItemFlag)){
    		layer.alert(bt.lang("table.ratbkrationRelief.reliefItemFlagNull"));
    		moneyType.get("reliefMoney").val("0");
    		moneyType.get("reliefOpenMoney").val("0");
    		return false;
    	}
    	if("1" == reliefItemFlag || "1" == byReliefItemFlag){
    		var flag = false;
    		$.each($("input[data-name$='reliefMaxMoney']"), function(i,item){
        		var bizName = $(item).attr("data-biz-name");
        		var maxMoney = moneyType.get(bizName).val();
        		if(bt.money.sub(maxMoney, reliefMoney) > 0){
            		flag = true;
            		return;
            	}
            });
    		if(flag){
    			//先设置所有的综合额度为0
    			layer.alert(bt.lang("table.ratbkrationRelief.byReliefMoneyLager"));
    			$.each($("input[data-name$='eliefMaxMoney']"), function(i,item){
    				var bizName = $(item).attr("data-biz-name");
    				var maxMoney = moneyType.get(bizName).val();
    				if(bt.money.sub(maxMoney, reliefMoney) > 0){
    					moneyType.get(bizName).val("0");
    				}
    			});
    			moneyType.get("reliefOpenMoney").val(reliefMoney);
    			return false;
    		}
    	}
    	return true;
    }

    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    
    
    function checkBefore(){
    	if(!bt.commonCheck()){
    		return false;
    	}
    	var reliefMoney = moneyType.get("reliefMoney").val();
    	if(reliefMoney - 0 <= 0){
    		layer.alert(bt.lang("cms.rat.relief.money.zero"));
    		moneyType.get("reliefMoney").val("0");
    		return false;
    	}
    	
    	return true;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
        var action = "/cms/rat/ratbkrationrelief/editSave";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
    	var action = "/cms/rat/ratbkrationrelief/editSubmit";
    	$.post(basePath + action,data.field,function(result){
    		bt.loading.hide(); //关闭加载中
    		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    		if(result.success){
    			if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
                	webObj.search();
                	bt.navtab.closeCurrent();
                }
    		}
    	},'JSON');
    	return false;
    });
    
    if($("select[name='byReliefItemFlag']").val() == "0"){
    	var ratCode = $("#ratCode").val();
    	var itemCode = $("select[name='byReliefItemCode']").val();
    	//bt.getItemAssure(ratCode, itemCode);
    } else {
    	var ratCode = $("#ratCode").val();
    	var byReliefCorp = $("#byReliefCorp").val();
    	//bt.setByReliefItem(ratCode, byReliefCorp, "1");
    }
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
    }
});


function setItemFlag(data){
	bt.setItemFlag(data);
}

function setByItemFlag(data){
	bt.setByItemFlag(data);
}

closeTab = function(){
	bt.close();
}