var rowMR=0;
var comprowMR=0;
var singrowMR=0;
var compRowIndex = 0;
var copmRatSize;
var selectTypes;
var moneyType;
bt.use(['form','layedit','bttable','moneynum','dicts','select','moneyType','mulselect','corp', 'selectType','mulselect','money'], function(){
    var $ = bt.jquery,form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //设置分配金额和剩余额度
    bt.calMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calIndexOpenMoney(rowIndex);
    	bt.calSumMoney();
    	var compItemSize = $("#compItemSize").val();
    	var compRowIndex = $("#compItemIndex").val();
    	var itemSize = compRowIndex - (0 - compItemSize);
    	for(var i = compRowIndex; i < itemSize; i++){
    		if(rowIndex == i){
    			bt.setMaxReadOnly(data);
    		}
    		bt.checkMaxMoney(i);
    	}
    }
    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));
    }
    //循环计算敞口授信额度
    bt.calSumOpenMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='openMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("openMoneysum").val(bt.money.format(sumMoney));
    }
    
    //根据授信额度计算敞口额度
    bt.calIndexOpenMoney = function(rowIndex) {
    	var itemFlag = $("#itemFlag"+rowIndex).val();
    	var moneyIndexVal = moneyType.get(rowIndex+"money").val();
    	if("0" == itemFlag){
    		var assurepercent = $("#assurepercent"+rowIndex).val();
    		//授信额度*（1-保证金比例/100）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(assurepercent, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	} else {
    		var interestrate = $("#interestrate"+rowIndex).val();
    		//授信额度*（1-利率/费率）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(interestrate, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	}
    	bt.calSumOpenMoney();
    }
    
    bt.calMaxMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	var singItemSize = $("#singItemSize").val();
    	var compRowIndex = $("#compItemIndex").val();
    	var moneyIndexVal = moneyType.get(compRowIndex+"money").val();
    	var maxMoneyIndexVal = moneyType.get(rowIndex+"maxMoney").val();
    	if(bt.empty(moneyIndexVal)){
    		layer.alert(bt.lang("table.ratbkrationAdd.moneynull"));
    		moneyType.get(rowIndex+"maxMoney").val("0");
    		return;
    	}
    	if(bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0) {
    		layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
    		moneyType.get(rowIndex+"maxMoney").val(moneyIndexVal);
    		return;
    	}
    }

    bt.checkMaxMoney = function(rowIndex){
    	var singItemSize = $("#singItemSize").val();
    	var compRowIndex = $("#compItemIndex").val();
    	var moneyIndexVal = moneyType.get(compRowIndex+"money").val();
    	var maxMoneyIndexVal = moneyType.get(rowIndex+"maxMoney").val();
    	if(bt.empty(moneyIndexVal)){
    		layer.alert(bt.lang("table.ratbkrationAdd.moneynull"));
    		moneyType.get(rowIndex+"maxMoney").val("0");
    	}
    	if(bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0) {
    		layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
    		moneyType.get(rowIndex+"maxMoney").val(moneyIndexVal);
    	}
    }
    
    //设置敞口最高额度不可编辑
    bt.setMaxReadOnly = function(compMoney) { 
    	var addFlag = true;
    	if(compMoney - 0 < 0) {//追减
    		addFlag = false;
    	}
    	
    	$.each($("input[data-id$='maxMoney']"), function(i,item){
    		if(addFlag) {//追加
    			$(item).removeAttr("readonly");
    		} else {
    			$(item).attr("readonly", "readonly");
    		}
    		var rowindex = $(item).attr("rowindex");
    		moneyType.get(rowindex+"maxMoney").val(compMoney);
        });
    }

    bt.dicts.init();
    moneyType = bt.moneyType.init();
    
    function checkBefore(){
    	
    	
    	return true;
    }
    
    //监听暂存
    form.on('submit(temp)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
        var action = "/cms/rat/ratbkrationadd/cpeditSave";
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
    	
    	var action = "/cms/rat/ratbkrationadd/cpeditSubmit";
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
    
    //监听返回
    form.on('submit(back)', function(data){
    	parent.layer ? parent.layer.closeAll() : layer.closeAll();
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

closeTab = function(){
	bt.close();
}
