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

	form.verify({
		nozero: function (value, item) {
			if (bt.empty(value)) {
				return '新追加额度不能为空'
			}
			if (value == 0) {
				return '新追加额度不能为0'
			}
		}
	});

    //设置分配金额和剩余额度
    bt.calMoney = function(data, el){
    	var dataId = $(el).attr("data-id");
    	var moneyIndex = dataId.indexOf("money");
    	var rowIndex = dataId.substr(0, moneyIndex);
    	bt.calSumMoney();
    }
    //循环计算授信额度
    bt.calSumMoney = function(){
    	var sumMoney = 0;
    	$.each($("input[id$='money']"), function(i,item){
    		sumMoney = bt.money.add(sumMoney, $(item).val());
        });
    	moneyType.get("moneysum").val(bt.money.format(sumMoney));
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
    	}
    	if(bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0) {
    		layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
    		moneyType.get(rowIndex+"maxMoney").val(moneyIndexVal);
    	}
    }

    bt.checkMaxMoney = function(rowIndex){
    	var singItemSize = $("#singItemSize").val();
    	var compRowIndex = $("#compItemIndex").val();
    	var moneyIndexVal;
    	var moneyIndexObj = $("#" + compRowIndex+"money");
    	if(moneyIndexObj && moneyIndexObj.val()){
    		moneyIndexVal = moneyType.get(compRowIndex+"money").val();
    	}
    	var maxMoneyIndexVal;
    	var maxMoneyIndexObj = $("#" + rowIndex+"maxMoney");
    	if(maxMoneyIndexObj && maxMoneyIndexObj.val()){
    		maxMoneyIndexVal = moneyType.get(rowIndex+"maxMoney").val();
    	}
    	if(bt.empty(moneyIndexVal)){
    		layer.alert(bt.lang("table.ratbkrationAdd.moneynull"));
    		moneyType.get(rowIndex+"maxMoney").val("0");
    	}
    	if(bt.money.sub(maxMoneyIndexVal, moneyIndexVal) > 0) {
    		layer.alert(bt.lang("table.ratbkrationAdd.moneyLager"));
    		moneyType.get(rowIndex+"maxMoney").val(moneyIndexVal);
    	}
    }

    bt.dicts.init();
    moneyType = bt.moneyType.init();

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
        var action = "/cms/rat/ratcprationadd/tempOrSubmit";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide(); //关闭加载中
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	var webObj = bt.navtab.getParentDom();
									setTimeout(function () {
										webObj.search();
										bt.navtab.closeCurrent();
									},200)
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
