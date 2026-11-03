var selectTypes,dicts;
bt.use(['form','money','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();  
    selectTypes = bt.selectType.init();//下拉框
    dicts=bt.dicts.init();
    /**判断字符串是否为空*/

    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    bt.viewCpRationDtl = function(params) {
    	var param = $.extend({}, params);
    	var ratCode = $("#cpRatCode").val();//授信信息
    	var itemFlag = $("#cpItemFlag").val();//品种类型
    	var itemMoney = $("#cpItemMoney").val();//授信额度
    	param["ratCode"] = bt.empty(ratCode) ? "" : ratCode;
    	param["itemFlag"] = bt.empty(itemFlag) ? "" : itemFlag;
    	param["itemMoney"] = bt.empty(itemMoney) ? "" : itemMoney;
    	if(itemFlag == "0") {//内部授信只有单品种
    		var itemCode = params.ratItemCode;
    		if(bt.empty(itemCode)){
    			itemCode =selectTypes.get("cpRatItemCode").val();//授信品种
    		}    		
    		param["ratItemCode"] = bt.empty(itemCode) ? "" : itemCode;
    	}
    	return parseParam(param);
    }

    var isHangCredit = $("#isHangCredit").val();
    if("0" == isHangCredit){
        $("#cpRatItemCodeDiv").hide();
        $("#ratCode").val("");
        setCpRationBtn();
    } else if("1" == isHangCredit) {
        $("#cpRatItemCodeDiv").show();
    }

    var ratItemCode=$("#cpRatItemCode").val();//授信品种如果不为空则显示查看按钮
    if(!bt.empty(ratItemCode)){
        $("#cpRationBtn").show();
    }
});


//选择授信
function chooseCpRatCode(){
    var params = beforeCpRation();//选择授信前设置参数
    if(bt.empty(params)){
        return;
    }
    openCpRation(params);
}

//查看授信
function viewCpRatCode(){
    var params = beforeCpRation();//查看授信前设置参数
    if(bt.empty(params)){
        return;
    }
    viewCpRationDtl(params);
}

/**
 * 授信额度单选择页面
 */
function openCpRation(params) {
	var param = bt.viewCpRationDtl(params);
	layer.open({
        type: 2,
        area: ['850px', '400px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcpuration/toRationDtl?" +param,
        end : function(){
        	
        }
    });
}

var setCpRatModel = function(data){
    if(data != undefined) {
        if (data.value == '1') {
            $("#cpRatItemCode_select").find('select').attr("lay-verify", "required");
            if (!$("#cpRatItemCode_verify").find(".pl").length) {
                $("#cpRatItemCode_verify").prepend('<span class="pl"><span class="required-icon">*</span></span>')
            }
        } else {
            $("#cpRatItemCode_select").find('select').removeAttr("lay-verify");
        }
    }

	var isHangCredit = dicts.get("isHangCredit").val();
	var ratValue = $("#ratmodelValue").val();
	if(typeof(isHangCredit) == 'undefined'){
		isHangCredit = ratValue;
	}
	if("0" == isHangCredit){
		$("#cpRatItemCodeDiv").hide();
		$("#cpRatCode").val("");
        setCpRationBtn();
	} else if("1" == isHangCredit) {
		$("#cpRatItemCodeDiv").show();
	}
}

//清除选择的授信单
var clearCpRatModel = function(){
	$("#cpRatCode").val("");
	try{
		selectTypes.get("cpRatItemCode").val(" ");
	}catch(e){
		
	}
	$("#cpRationBtn").hide();
}

//重设选择的授信
var setCpRationBtn = function(){
	$("#cpRatCode").val("");
	$("#cpRationBtn").hide();
}


/**
 * 授信额度回写调用
 * @param params
 */
var setCpRation = function(params){
    var ratCode = params.ratCode;//授信单号
    var itemMoney = params.itemMoney;//授信金额
    var itemCode = params.itemCode;//授信品种编号
    var leftMoney = params.leftMoney;//剩余额度
    var html = '<input type="hidden" name="cpRatCode" id="cpRatCode" value="' + ratCode + '">';
    html += '<input type="hidden" name="cpItemMoney" id="cpItemMoney" value="' + itemMoney + '">';
    html += '<input type="hidden" name="leftMoney" id="leftMoney" value="' + leftMoney + '">';
    //设置itemCode选中
   selectTypes.get("cpRatItemCode").val(itemCode);//授信品种
    $("#cpItemCodeTd").html(html);
    $("#cpRationBtn").show();
}

/**
 * 查看授信品种和额度
 */
var viewCpRationDtl = function(params) {
	var param = bt.viewCpRationDtl(params);
	layer.open({
        type: 2,
        area: ['850px', '400px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcpuration/viewRationDtl?"+param,
        end : function(){
        	
        }
    });
}




//json转url参数
var parseParam = function(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};





	
