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
    	var corpId = selectTypes.get("reliefCorp").val();
    	var ratCode = $("#ratCode").val();
    	bt.setReliefItem(ratCode, corpId, data.value);
    });
    
    //调剂单品种监听
    form.on('select(reliefItemCode)', function(data){
    	var corpId = selectTypes.get("reliefCorp").val();
    	var ratCode = $("#ratCode").val();
    	var itemFlag = $("select[name='reliefItemFlag']").val();
    	bt.getItemMoney(ratCode, corpId, itemFlag, data.value);
    });
    
    //选择授信调剂单位修改已分配的授信品种类型
    bt.setItemFlag = function(data) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var corpId = data.id;
    	var ratCode = $("#ratCode").val();
    	
		//被调剂单位和调剂单位保持一致
		$("#byReliefCorps").val(data.name);
		$("#byReliefCorp").val(data.id);
    	
    	$.post(basePath + "/cms/rat/ratcprationrelief/getReliefItemFlag", {
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
    	bt.setByItemFlag(corpId);
    }
    
    bt.setReliefItem = function(ratCode, corpId, itemFlag) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	$.post(basePath + "/cms/rat/ratcprationrelief/getReliefItem", {
    		ratCode : ratCode,
			corpId : corpId,
			itemFlag : itemFlag
		}, function(data) {
			$("#reliefItemCode").empty();
			if(data && data.length>0){
				$("#reliefCode").val(data[0].disCode);
				moneyType.get("reliefMoney").val("0");
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
    	$.post(basePath + "/cms/rat/ratcprationrelief/getDisItemMoney", {
    		ratCode : ratCode,
			corpId : corpId,
			itemFlag : itemFlag,
			itemCode : itemCode
		}, function(data) {
			moneyType.get("byReliefMoney").val(data);
		}, 'JSON');
    }
    
    //调剂品种类型监听
    form.on('select(byReliefItemFlag)', function(data){
    	//var corpId = selectTypes.get("byReliefCorp").val();
    	var corpId = $('#byReliefCorp').val();
    	var ratCode = $("#ratCode").val();
    	bt.setByReliefItem(ratCode, corpId, data.value);
    });
    
    //调剂单品种监听
    form.on('select(byReliefItemCode)', function(data){
    	var ratCode = $("#ratCode").val();
    	bt.getItemAssure(ratCode, data.value);
    });
    
    //选择授信调剂单位修改已分配的授信品种类型
    bt.setByItemFlag = function(corpIds) {
    	//异步获取授信单位的品种信息，如果只有一种品种默认选择第一条
    	var corpId = corpIds;
    	var ratCode = $("#ratCode").val();

    	$.post(basePath + "/cms/rat/ratcprationrelief/getReliefItemFlag", {
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
    	$.post(basePath + "/cms/rat/ratcprationrelief/getReliefItem", {
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
				} else
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
    	$.post(basePath + "/cms/rat/ratcprationrelief/getItemAssure", {
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
    }
    
    //检查调剂额度
    bt.checkReliefMoney = function(data, el){
    	var sumMoney = 0;
    	$.each($("input[id^='reliefMaxMoney']"), function(i,item){
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
            });
			return false;
		}
    	moneyType.get("reliefMoney").val(sumMoney);
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
    	//var byCorpId = selectTypes.get("byReliefCorp").val();
    	var byCorpId = $('#byReliefCorp').val();
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
		data.field.reliefCorpName = selectTypes.get('reliefCorp').currentObject.obj[0].name
		data.field.byReliefCorpName = data.field.byReliefCorps;
		// data.field.byReliefCorpName = selectTypes.get('byReliefCorp').currentObject.obj[0].name
		var action = "/cms/rat/ratcprationrelief/save";
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
		data.field.reliefCorpName = selectTypes.get('reliefCorp').currentObject.obj[0].name
		data.field.byReliefCorpName = data.field.byReliefCorps;
		// data.field.byReliefCorpName = selectTypes.get('byReliefCorp').currentObject.obj[0].name
    	var action = "/cms/rat/ratcprationrelief/submit";
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