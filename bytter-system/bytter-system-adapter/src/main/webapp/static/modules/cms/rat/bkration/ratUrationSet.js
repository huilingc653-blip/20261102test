var dicts,bankCode;
bt.use(['form','datatable','btdate','money','dateType','dicts','selectType', 'moneyType'], function(){
    $ = bt.jquery,form = bt.form();
   setTimeout(function () {
      dicts = bt.dicts.init();//字典
    },80)
  
   selectTypes = bt.selectType.init();//下拉框
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }

    bt.viewRationDtl = function(params) {
    	var param = $.extend({}, params);
    	var ratCode = $("#ratCode").val();//授信信息
    	var itemFlag = $("#itemFlag").val();//品种类型
    	var itemMoney = $("#itemMoney").val();//授信额度
    	param["ratCode"] = bt.empty(ratCode) ? "" : ratCode;
    	param["itemFlag"] = bt.empty(itemFlag) ? "" : itemFlag;
    	param["itemMoney"] = bt.empty(itemMoney) ? "" : itemMoney;
    	if(itemFlag == "0") {
    		var itemCode = params.ratItemCode;
    		if(bt.empty(itemCode)){
    			itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
    		}
    		param["ratItemCode"] = bt.empty(itemCode) ? "" : itemCode;;
    	} else {
    		var doms = $('input[id^="itemMoney"]');
/*tomcat8  url地址栏不允许出现大括号，所以改一种实现方式
				var applyDtls = [];
				$.each(doms,function(i, item){
					var itemCode = $(item).attr("itemCode");//授信品种
					var itemMoney = $(item).val();//授信品种
					var applyDtl = {};
					if(!bt.empty(itemCode)){
						applyDtl["itemCode"] = itemCode;
						applyDtl["useMaxMoney"] = bt.money.toNumber($(item).val());
						applyDtls.push(applyDtl);
					}
				})
				 param["applyDtls"] = applyDtls; */

            $.each(doms,function(i, item){
                var itemCode = $(item).attr("itemCode");//授信品种
                if(!bt.empty(itemCode)){
                    param["itemCode"] = itemCode;
                    param["useMaxMoney"] = bt.money.toNumber($(item).val());
                }
            })
    	}
    	return parseParam(param);
    }

    setTimeout(setRatModelOverrive(), 500);

    var ratItemCode=$("#ratItemCode").val();
    if(!bt.empty(ratItemCode)){
        $("#rationBtn").show();
	}

    form.on('checkbox(isStandCur)', function(data){//是否折合本位币切换，清空授信信息
        // if(data=="0"){//不折合本位币时隐藏汇率
        //     $("#exchangerateTd1").hide();
        //     $("#exchangerateTd2").hide();
        // }else{
        //     $("#exchangerateTd1").show();
        //     $("#exchangerateTd2").show();
        // }
        clearRatModel();

    });
});

/**
 * 授信额度单选择页面
 * @param params 参数内容如下
 * @param corpId ：单位ID
 * @param curId :币别ID
 * @param bankId : 银行信息ID
 * @param itemCode : 授信品种
 * @param beginDate ：业务开始日期
 */
function openRation(params) {
	var param = bt.viewRationDtl(params);
	layer.open({
        type: 2,
        area: ['850px','400px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturation/toRationDtl?" +param,
        end : function(){

        }
    });
}

var setRatModel = function(){
	//银行选择
	/*var selectHtml;
	selectHtml = '<div class="layui-inline"><input class="layui-input" id="shouBankName" readonly="readonly" lay-verify="required" type="text"/></div>';
	selectHtml += '<div class="layui-inline"><button type="button" class="layui-btn bt-biz-bankInfo" data-callback="bankinfoCallback">'+bt.lang('common.button.select')+'</button>';
	//下拉框银行选择
	var pulldownHtml;
	pulldownHtml ='<div class="layui-inline"><div class="bt-biz-selectType" data-name="bankxx" lay-verify="required" data-callback="bankinfoCallBackTwo" ';
	pulldownHtml += ' data-url="'+basePath+'/cms/provide/loaninfo/getBankName" data-valuefield="bankCode" data-textfield="bankName" data-biz-name="btBank" data-required="true"></div></div> ';*/

    //授信模式切换，需清空授信品种与业务品种
    // selectTypes.get("ratItemCode").val("");
    // selectTypes.get("billName").val("");

	var ratBankConfig=$("#ratBankConfig").val();
	var ratModel = bt.dicts.init().get("ratmodel").val();
	var ratValue = $("#ratmodelValue").val();
	if(typeof(ratModel) == 'undefined'){
		ratModel = ratValue;
	}
	if("0" == ratModel){
		//bt.selectType.init().get("ratItemCode").attr("data-disabled", true);
		$(".ratInfo").hide();
		$("#ratCode").val("");
        $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
        $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
		setRationBtn();
		$("#xz").show();
		$("#xl").hide();
        if ($("#exchangerateTd1").length > 0) {
            $("#isRmbRatTr").hide();
            if ($("#exchangerate").length > 0) {
            	moneyTypes.get("exchangerate").val("");
            }
        }
	} else if("1" == ratModel) {
        // selectTypes.get("ratItemCode").attr('lay-verify', 'required');
        // $('[data-biz-name="ratItemCode"]').find('select').attr('lay-verify', 'required');
		$(".ratInfo").show();
		if(!bt.empty(ratBankConfig)&&ratBankConfig=="1"){//银行类别相同,进入联行号选择界面
            $("#xz").show();
            $("#xl").hide();
		}else{//银行行号相同,下拉选择授信银行
            $("#xl").show();
            $("#xz").hide();
		}

	}

	// 担保只读设置
	var ratIsHangGua = $("#ratIsHangGua").val();
	if ("1" == ratIsHangGua) {
		var data = {"value":"0"};
		dicts.get("isHangGuarantee").attr({"data-value":"0"});
		form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
		bt.setGuaInfo(data)
		form.renderIcon();
	}

}

function setRatModelOverrive() {
    var ratBankConfig=$("#ratBankConfig").val();
    var ratModel = bt.dicts.init().get("ratmodel").val();
    var ratValue = $("#ratmodelValue").val();
    if(typeof(ratModel) == 'undefined'){
        ratModel = ratValue;
    }
    if("0" == ratModel){
        //bt.selectType.init().get("ratItemCode").attr("data-disabled", true);
        $(".ratInfo").hide();
        $("#ratCode").val("");
        $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
        $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
        setRationBtn();
        $("#xz").show();
        $("#xl").hide();
        if ($("#exchangerateTd1").length > 0) {
            $("#isRmbRatTr").hide();
            if ($("#exchangerate").length > 0) {
                moneyTypes.get("exchangerate").val("");
            }
        }
    } else if("1" == ratModel) {
        // selectTypes.get("ratItemCode").attr('lay-verify', 'required');
        // $('[data-biz-name="ratItemCode"]').find('select').attr('lay-verify', 'required');
        $(".ratInfo").show();
        if(!bt.empty(ratBankConfig)&&ratBankConfig=="1"){//银行类别相同,进入联行号选择界面
            $("#xz").show();
            $("#xl").hide();
        }else{//银行行号相同,下拉选择授信银行
            $("#xl").show();
            $("#xz").hide();
        }

    }

    // 担保只读设置
    var ratIsHangGua = $("#ratIsHangGua").val();
    if ("1" == ratIsHangGua) {
        var data = {"value":"0"};
        dicts.get("isHangGuarantee").attr({"data-value":"0"});
        form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        bt.setGuaInfo(data)
        form.renderIcon();
    }
}

//清除选择的授信单
var clearRatModel = function(){
	$("#ratCode").val("");
	try{
		bt.selectType.init().get("ratItemCode").val(" ");
	}catch(e){
	}
	$("#rationBtn").hide();
}

//重设选择的授信
var setRationBtn = function(){
	$("#ratCode").val("");
	$("#rationBtn").hide();
	setBusinessRac();
}

/**
 * 授信额度回写调用
 * @param params
 */
var setRation = function(params){
	var ratCode = params.ratCode;//授信单号
	var itemFlag = params.itemFlag;//授信品种标识
	var itemMoney = params.itemMoney;//授信金额
	var itemCode = params.itemCode;
    var leftMoney = params.leftMoney;
    var isHangGuarantee=params.isHangGuarantee;//是否挂接担保
	$("#ratBankCode").val(params.bankCode);
    var  html = '<input type="hidden" name="itemFlag" id="itemFlag" value="' + itemFlag + '">';
    if(itemFlag == "0"){
        var singleItem=$("#singleItem").html();
        if(!bt.empty(singleItem)){
            $("#singleItem").html("");//编辑页面清空之前的授信信息
        }
		//html += '<input type="hidden" name="ratItemCode" id="ratItemCode" value="' + itemCode + '">';
        html += '<input type="hidden" name="ratCode" id="ratCode" value="' + ratCode + '">';
        html += '<input type="hidden" name="itemMoney" id="itemMoney" value="' + itemMoney + '">';
	} else {
        var compTtem=$("#compItem").html();
        if(!bt.empty(compTtem)){
            $("#compItem").html("");//编辑页面清空之前的授信信息
        }
		//循环设置综合品种
		var rationDlts = params.rationDlts;
		if(rationDlts && rationDlts.length > 0){
			var length = rationDlts.length;
			html += '<input type="hidden" name="applyDtlsLength" id="applyDtlsLength" value="' + length + '">';
            html += '<input type="hidden" name="ratCode" id="ratCode" value="' + ratCode + '">';
			for(var i = 0; i < length; i++){
				var rationDlt = rationDlts[i];
				html += '<input type="hidden" name="applyDtls['+i+'].itemCode" id="itemCode" value="' + rationDlt.itemCode + '">';
				html += '<input type="hidden" name="applyDtls['+i+'].itemMoney" itemCode="' + rationDlt.itemCode + '" id="itemMoney" value="'+rationDlt.itemMoney+'">';//挂接综合授信时只有一个品种
			}
		}
	}
	//设置itemCode选中
	bt.selectType.init().get("ratItemCode").val(itemCode);//授信品种
  $("select[name='ratItemCode']").removeClass("layui-form-danger"); // 清除授信品种必填警告样式
	$("#ratIsHangGua").val(isHangGuarantee);//授信是否挂担保
    $("#ratCode").val(ratCode);
	$("#itemCodeTd").html(html);
	$("#rationBtn").show();
	// 业务品种
	setBusinessRac();

	try {
	    // 扩展函数，如调用页面需要进行后续处理，各调用页面需自己实现ratInfoExtSet函数的处理逻辑
		if(typeof(eval("ratInfoExtSet")) == "function") {
			ratInfoExtSet(params);
		}
	}catch (e) {

	}

/*	if(itemFlag == "1"){
	}*/
	//判断授信是否挂接了保证金
	/*if(bt.empty(ratCode)){
		return;
	}
	$.post(basePath + "/cms/provide/loaninfo/checkRateCode", {
		rateCode : ratCode
	}, function(data) {
		if(data && "1" == data.isHook){
			$("#isHook").hide();
            $(".isShow").hide();
            $("#bailmoney").removeAttr('lay-verify');
            $("#bankAcc").removeAttr('lay-verify');
            bt.dicts.init().get("includeBailmoney").val("0");
            $("#includeBailmoney").val("0");
            $("#isHookBond").val("0");
		}else{
            $("#isHook").show();//显示选择是否挂接保证金按钮
            $(".isShow").show();
            $("#bailmoney").attr('lay-verify','required')
            $("#bankAcc").attr('lay-verify','required')
            bt.dicts.init().get("includeBailmoney").val("1");
		}
        form.renderIcon();
	}, 'JSON');*/
}

/**
 * 查看授信品种和额度
 */
var viewRationDtl = function(params) {
    delete params["bankCode"];
    bankCode=$("#ratBankCode").val();
    params["bankCode"] = bt.empty(bankCode) ? "" : bankCode;//查看传的联行号根据选择授信返回的为准
	var param = bt.viewRationDtl(params);
	layer.open({
        type: 2,
        area: ['980px', '430px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturation/viewRationDtl?"+param,
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
var obj = {
    "name": 'tom',
    "class": {
        "className": 'class1'
    },
    "classMates": [{
        "name": 'lily'
    }]
};

/**
 * 设置业务品种
 * @param data
 * @param elem
 * @param beforevalue
 * @param wrapObj
 */
function setBusinessRac() {
    var ratModel = bt.dicts.init().get("ratmodel").val();
    var ratValue = $("#ratmodelValue").val();
    if(typeof(ratModel) == 'undefined'){
        ratModel = ratValue;
    }
    if (ratModel == "1") {
        var id = selectTypes.get("ratItemCode").val();
        if ($("#billName") != undefined) {
            var billName = selectTypes.get("billName").val();
            if(id != null && id != "" && id != undefined) {
                var url = basePath + "/cms/rat/ratBusinessRace/getBillByCustomId?customId="+id;
                selectTypes.get("billName").attr({"data-url": url,"data-value":billName});
                selectTypes.get("billName").init();
            }
        }
    }
}

/**
 * 业务品种初始化回调
 * @param text
 * @param data
 */
function checkData(data) {
	if (data.length > 0) {
		$('[data-biz-name="billName"]').find('select').attr('lay-verify', 'required');
	}else{
		$('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
	}
	form.renderIcon();
}



