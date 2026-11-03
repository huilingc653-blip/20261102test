var rowMR=0;
var selectTypes;
var moneyType;
var rowArray = {};
bt.use(['form','layedit','btdate','bttable','moneynum','dicts','select','moneyType','dateType','corp', 'selectType','money'], function(){
    var $ = bt.jquery,form = bt.form();
    copmRatSize = $("#copmRatSize").val();//品种数量
    selectTypes = bt.selectType.init();//下拉框
    var ratCode = $("#ratCode").val();
    var disCode = $("#disCode").val();
	var corpId = $("#corpId").val();
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //设置保证金比例利率
    bt.setAssure = function(rowIndex, data) {
    	moneyType.get(rowIndex + "assurepercent").val(data.assurepercent);
    	moneyType.get(rowIndex + "interestrate").val(data.interestrate);
    }
    
    
    bt.calUMoney = function(data, el) {
    	var dataId = $(el).attr("data-id");
    	var index = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, index);
    	var itemOrder = dataId.substr(index);
    	var money = moneyType.get(rowIndex+"money").val();
    	bt.setMoney(rowIndex);
    	bt.calIndexOpenMoney(rowIndex);
    }
    
    bt.setMoney = function(rowIndex){
    	var sumMoney = 0;
    	$.each($("input[data-id^='"+rowIndex+"maxMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get(rowIndex+"money").val(sumMoney);
    }
    
    //根据授信额度和保证金比例费率计算敞口额度
    bt.calIndexOpenMoney = function(rowIndex) {
    	var itemFlag = $("#itemFlag"+rowIndex).val();
    	var moneyIndexVal = moneyType.get(rowIndex+"money").val();
    	if("0" == itemFlag){
    		var assurepercent = moneyType.get(rowIndex + "assurepercent").val();
    		//授信额度*（1-保证金比例/100）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(assurepercent, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	} else {
    		var interestrate = moneyType.get(rowIndex + "interestrate").val();
    		//授信额度*（1-利率/费率）
    		var moneyVal = bt.money.mul(moneyIndexVal, bt.money.sub(1, bt.money.div(interestrate, 1)));
    		moneyType.get(rowIndex+"openMoney").val(bt.money.format(moneyVal));
    	}
    }
    
    //检查最高额度与授信额度关系
    bt.checkMaxMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	var moneyIndexVal = moneyType.get(compRowIndex+"money").val();
    	if(bt.empty(moneyIndexVal)){
    		layer.alert(bt.lang("table.ratbkrationdetail.moneymsg"));
    		moneyType.get(rowIndex+"maxMoney").val(0);
    		return false;
    	}
    	//最高额度不能大于授信额度
    	if(bt.money.sub(moneyIndexVal, data) < 0){
    		layer.alert(bt.lang("table.ratbkrationdetail.maxMoneyOut"));
    		moneyType.get(rowIndex+"maxMoney").val(0);
    		return false;
    	}
    }
    
    bt.calMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calIndexOpenMoney(rowIndex);
    }
    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='maxMoney']"), function(i,item){
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
    
    bt.setDays = function(data, el) {
    	var rowIndex = "";
    	var beginDate = bt.dateType.init().get(rowIndex+"beginDate").val();
    	if(bt.empty(beginDate)){
    		return;
    	}
    	var endDate = bt.dateType.init().get(rowIndex+"endDate").val();
    	if(bt.empty(endDate)){
    		return;
    	}
    	var days = getBetweenDay(beginDate, endDate) - (-1);
    	if(days < 0){
    		bt.dateType.init().get(rowIndex+"endDate").val(" ");
    		$("#" + rowIndex + "days").val("");
    		return;
    	}
    	$("#" + rowIndex + "days").val(days);
    }
    
    
    var initSelectCode=function(row){
    	bt.dicts.init();
    	bt.moneyType.init();
    	bt.dateType.init();
    	bt.selectType.init();
    }
    
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    bt.setDays();
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
    	
        var action = "/cms/rat/raturationapply/editSave";
        $.post(basePath + action, data.field,function(result){
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
        }, 'JSON');
        return false;
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	
        var action = "/cms/rat/raturationapply/editSubmit";
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
        }, 'JSON');
        return false;
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	//bt.navtab.refreshParent();
        	bt.navtab.closeCurrent();
        }
    }
});

function setItemCode(data,elem,bv){
    bt.setItemCode(data,elem,bv)
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

closeTab = function(){
	bt.close();
}