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

    
    bt.calUMoney = function(data, el) {
    	var dataId = $(el).attr("data-id");
    	var index = dataId.indexOf("maxMoney");
    	var rowIndex = dataId.substr(0, index);
    	var itemOrder = dataId.substr(index);
    	var money = moneyType.get(rowIndex+"money").val();
    	bt.setMoney(rowIndex);
    	//bt.calIndexOpenMoney(rowIndex);
    }
    
    bt.setMoney = function(rowIndex){
    	var sumMoney = 0;
    	$.each($("input[data-id^='"+rowIndex+"maxMoney']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get(rowIndex+"money").val(sumMoney);
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
    	//bt.calIndexOpenMoney(rowIndex);
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
    
    //监听暂存或提交
    form.on('submit', function(data){
    	bt.loading.show();//显示加载中
    	if(!checkBefore()){
    		bt.loading.hide(); //关闭加载中
    		return false;
    	}
    	var nm = $(this).attr("id");    
        if(nm == "submit"){
        	data.field.saveType = "1";//提交
        }else{
        	data.field.saveType = "0"; //暂存
        }
        var action = "/cms/rat/cprationapply/editTempOrSubmit";
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
        },'JSON');
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
    
    function compareLeftMoney() {
        var moneyDiv = moneyType.get("money");
        var value = moneyDiv.attr("value");
        var maxValue = moneyDiv.attr("data-max");
        if(bt.money.sub(value, maxValue) > 0){
            parent.layer.alert(bt.lang("cms.provide.loaninfo.plmoneyhigh"), {icon: 7});
            return false;
        }
    }
    compareLeftMoney();
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