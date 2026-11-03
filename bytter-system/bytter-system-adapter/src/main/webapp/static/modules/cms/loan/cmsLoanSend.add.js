var selectType,dict,rateType,moneyType,dateType;
bt.use(['form','element','layedit','dicts','selectType','rateType','moneyType','dateType','btUpload'], function(){
    $ = bt.jquery,form = bt.form(),element = layui.element();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }

    /**校验发放日期和贷款日期*/
    validataLoanDate = function(data,ele){
    	var sendDate = new Date(dateType.get("loanDate").val());
    	var loanDate = new Date($("input[name='loanDate1']").val());
    	var repayDate = new Date(dateType.get("repayDate").val());
    	if(sendDate<loanDate){//发放日期小于贷款日期
    		layer.tips(bt.lang("cms.loan.SendDateLessLoanDateMsg"), '#sendDateShow');
    		dateType.get("loanDate").val("");
    		return false;
    	}
    	if(sendDate!==""&&repayDate!==""&&sendDate>repayDate){
    		layer.tips(bt.lang("cms.loan.SendDateMoreRepayDateMsg"), '#sendDateShow');
    		dateType.get("loanDate").val("");
    	}

    }

    /**校验发放日期和还款日期*/
    validataRepayDate = function(data,ele){
    	var sendDate = new Date(dateType.get("loanDate").val());
    	var repayDate = new Date(dateType.get("repayDate").val());
    	if(sendDate>repayDate){//还款日期小于贷款发放
    		layer.tips(bt.lang('cms.loan.DeadlineDateLessSendDateMsg'), '#repayDateShow');
    		dateType.get("repayDate").val("");
    	}
    }




    /**获取利率*/
    getLoanRate = function(data){
    	moneyType.get("pointsProportionText").val(data);
    	getRate();
    }

     dict = bt.dicts.init();
     selectType = bt.selectType.init();
     rateType = bt.rateType.init();
     moneyType=bt.moneyType.init();
     dateType = bt.dateType.init();
     //setChangeFloatWay();

    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand:true,
        gid: ''
    });
    //获取利率
	$("#getRate").click(function(){
		getRate();
	});
	//选票
	$("#selectBill").click(function(){

	});
	//获取利率


    // loadLoanRate();

    /**加载基准利率和利率期限*/
    function loadLoanRate(){
    	if(bt.empty($("#curId").val())){
    		selectType.get("referenceRateType").attr("data-url","");
    	}else if($("#naturalSign").val() != 1){
    		selectType.get("referenceRateType").attr("data-url",basePath+"/cms/loanrate/maintence/queryNotBaseRate");
    	}else{//本位币
    		selectType.get("referenceRateType").attr("data-url",basePath+"/cms/loanrate/maintence/queryBaseRate");
    	}
    }

    //监听提交
    form.on('submit(submit)', function(data){
    	if($("input[data-name='loanMoney']")[0].value<=0){
    		layer.tips(bt.lang("cms.loan.sendMoney.zero"), '.loanMoneyShow');
    		return false;
    	}
    	var flag = false;
    	if($("#planStatus").val()=="1"){//维护还款计划且贷款利率、还款方式、本次放款金额被修改
    		layer.confirm(bt.lang('cms.loan.resetRepayPlanMsg'), {icon: 3, title:bt.lang('table.cmsProcessMsg.message')}, function(index){
    			layer.close(index);
    			var status=$(this).attr("value");
    	        var action = "/cms/loan/cmsloansend/save?sendBillCode="+$("#sendBillCode").val();
    	        var contractCode = strLen(data.field['contractCode']);
    	    	if(contractCode >= 50){
    	    		layer.msg(bt.lang('cms.loan.contractCode.not.eqs.fifty'));
    	    		bt.loading.hide(); //关闭加载中
    	    		return false;
    	    	}
    	        data.field.attachmentId = attachments.getGroupId();//附件ID
    	        data.field.planId = '';
    	        var mask = layer.load(1, {shade: [0.1, '#FFF']});

    	        $.post(basePath + action,data.field,function(result){
    	            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    	            if(result.success){
    	            	layer.close(mask);
    	                $.each($(".layui-form"), function(){
    	                    this.reset();
    	                });
    	                if(!bt.navtab){
    	                    parent.layer?parent.layer.closeAll():layer.closeAll();
    	                } else {
    	                	bt.navtab.refreshParent();
    	                	bt.navtab.closeCurrent();
    	                }
    	            }else{
    	            	layer.close(mask);
    	            }
    	        },'JSON');
    	        return false;
    			});
    		return false;
    	}else{
    		flag = true;
    	}
    	if(flag){//未维护还款计划
	    	var status=$(this).attr("value");
	        var action = "/cms/loan/cmsloansend/save?sendBillCode="+$("#sendBillCode").val();
	        data.field.attachmentId = attachments.getGroupId();//附件ID
	        var contractCode = strLen(data.field['contractCode']);
	    	if(contractCode >= 50){
	    		layer.msg(bt.lang('cms.loan.contractCode.not.eqs.fifty'));
	    		bt.loading.hide(); //关闭加载中
	    		return false;
	    	}
	        var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
	        $.post(basePath + action,data.field,function(result){
	            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
	            if(result.success){
	            	layer.close(mask);
	                $.each($(".layui-form"), function(){
	                    this.reset();
	                });
	                if(!bt.navtab){
	                    parent.layer?parent.layer.closeAll():layer.closeAll();
	                } else {
	                	bt.navtab.refreshParent();
	                	bt.navtab.closeCurrent();
	                }
	            }else{
	            	layer.close(mask);
	            }
	        },'JSON');
	        return false;
    	}
    	return false;
    });

    //打开还款计划
    form.on('submit(repayPlan)', function(data){
        var data = data.field;
        var params = "loanRate=" + rateType.get("loanRate").val() +"&loanDate=" + data.loanDate + "&repayDate=" + data.repayDate
        	+ "&repayType=" + data.repayType + "&loanMoney=" + data.loanMoney + "&planId=" + $("#planId").val();

        if(data){
            if(bt.navtab){
                bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/basedata/repaymentschedule/toRepayPlan?" + params);
                return false;
            }
        }
        return false;
    });
    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }

    setChangeFloatWay();
});
/**
 * 发放方式变更
 * @param data
 * @param ele
 */
sentOutTypeChange = function(data,ele){
	if(data.value.trim()!=""&&data.value!="1"){//分次发放
		$("input[data-name='loanMoney']")[0].readOnly=false;
		$("input[data-name='loanMoney']").removeAttr("onfocus");//火狐移除属性
		$("input[data-name='loanMoney']").removeAttr("unselectable");//IE移除属性
	}else{//一次性发放
		$("input[data-name='loanMoney']")[0].readOnly=true;
		$("input[data-name='loanMoney']")[0].value=$("#loan_Money_Default").val();
		moneyType.get("loanMoney_biz").reset();//重置发放金额
	}
}
/**加载浮动利率的周期信息*/
loadrepayCycle = function(){
	var data = selectType.get("repayCycle").currentObject.find("option:selected").data();//获取币别选择的全部数据
	var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
	var sel = dict.get("pointsProportionSelect").val();
	if(data.isbaseloanrate == "0"){
		dict.get("pointsProportionSelect").val("01");
		if(sel != "01"){
			pointsProportionTextObj.val(0);
		}
	}else{//本位币
		dict.get("pointsProportionSelect").val("02");
		if(sel != "02"){
			pointsProportionTextObj.val(0);
		}
	}

	//设置text的输入范围
	pointsProportionTextObj.attr("data-min",data.ratedownlimit);
	pointsProportionTextObj.attr("data-max",data.rateuplimit);
	pointsProportionTextObj.reset();
}
/**基准利率加载完成后 加载利率周期*/
loanRepayCycle=function(){
	var curId = $("#curId").val();

	loanDate = $("input[name='loanDate']").val();//借款申请日期
	if(bt.empty(loanDate)){
		loanDate = $("#inputDate").val();//为空默认录入日期
	}

	var referenceRateType = selectType.get("referenceRateType").val();//基准利率类型

	if(bt.empty(curId)){
		selectType.get("repayCycle").attr("data-url","");
	}else{//本位币
		selectType.get("repayCycle").attr("data-url",basePath+"/cms/loanrate/maintence/querRateMaintence?time="+loanDate+"&curId="+curId+"&baseId="+referenceRateType);
	}
}
retPayPlan = function(data,ele){
	if($("#planStatus").val()=="0"){
		$("#planStatus").val(1);
		$("#planId").val('');
	}
}
//重置还款计划
function retPayPlan1(){
	if($("#planStatus").val()=="0"){
		$("#planStatus").val(1);
		$("#planId").val('');
	}
}
function retPayPlan2(){
	if($("#planStatus").val()=="0"){
		$("#planStatus").val(1);
		//$("#planId").val('');
	}
}

function setChangeFloatWay(){
	var floatWay = dict.get("floatWay").val();//浮动方式
	var rateData = $("input[name='rateEntriesEffectDate']");//利率生效日期
	rateData.attr("readonly",false);
	rateData.attr("lay-verify","required");
	var postfix = "";
	var htl = '';
	if(floatWay == "01"){//固定利率
		rateData.val("");
		rateData.attr("readonly",true);
		rateData.attr("lay-verify","");
		elementDisabledOrEnabled(false);
	}else if(floatWay == "02"){//即时浮动
		rateData.val($("input[name='inputDate']").val());
		elementDisabledOrEnabled(true);
	}else if(floatWay == "03"){//按月浮动
		postfix = "Month";
		elementDisabledOrEnabled(true);
	}else if(floatWay == "04"){//按季浮动
		postfix = "Season";
		elementDisabledOrEnabled(true);
	}else if(floatWay == "05"){//按半年浮动
		postfix = "HY";
		elementDisabledOrEnabled(true);
	}else if(floatWay == "06"){//按年浮动
		postfix = "Year";
		elementDisabledOrEnabled(true);
	}

	var fwn = $("#floatWayNext1").val();
	if(postfix == ""){
		$("#ChangeFloatWay").html("");//删除页面元素
	}else{
		var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext'+ postfix +'" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay'+ postfix +'" data-defaultvalue="'+ fwn +'" data-disabled="true"></div>';
		$("#ChangeFloatWay").html(htl);
		bt.dicts.render();
	}

}

//获取字符串长度
function strLen(str) {
	var len = 0;
	for (var i=0; i<str.length; i++) {
		if (str.charCodeAt(i)>127 || str.charCodeAt(i)==94) {
			len += 2;
		} else {
			len ++;
		}
	}
	return len;
}

/**
 * 浮动利率是否禁用
 */
function elementDisabledOrEnabled(flag){
	var rateData = $("input[name='rateEntriesEffectDate']");
	if(flag){
		rateData.attr("lay-verify","required");
	}else{
		$("input[data-id='pointsProportionText']").attr("disabled","disabled");
		//moneyType.get("pointsProportionText").val(0);
		$("#getRate").hide();
		rateData.removeAttr("lay-verify");
	}
	form.renderIcon();
	selectType.get("repayCycle").render();
	selectType.get("referenceRateType").render();
	dict.get("pointsProportionSelect").render();
	//dateType.get("rateEntriesEffectDate").render();
	//setRateEntriesEffectDate();
}

/**根据浮动方式详情设置利率调整生效日期*//*
function setRateEntriesEffectDate(){
	var floatWay = dict.get("floatWay").val();
	var date =new Date($("input[name='loanDate1']").val());
    var rateData = $("input[name='rateEntriesEffectDate']");
	if(floatWay == "03"){//按月浮动
		rateData.val(bt.date.format(bt.date.addMonth(date,1)));
	}else if(floatWay == "04"){//按季浮动
		rateData.val(bt.date.format(bt.date.addMonth(date,3)));
	}else if(floatWay == "05"){//按半年浮动
		rateData.val(bt.date.format(bt.date.addMonth(date,6)));
	}else if(floatWay == "06"){//按年浮动
		rateData.val(bt.date.format(bt.date.addYear(date,1)));
	}
}*/

/**根据浮动方式详情设置利率调整生效日期*/
function setRateEntriesEffectDate(){
	var floatWay = dict.get("floatWay").val();
	//var date =new Date($("input[name='loanDate']").val());
   // var rateData = $("input[name='rateEntriesEffectDate']");
	if(floatWay == "03"){//按月浮动
		//rateData.val(bt.date.format(bt.date.addMonth(date,1)));
		nextFloatWay(1,"Month");
	}else if(floatWay == "04"){//按季浮动
		nextFloatWay(3,"Season");
		//rateData.val(bt.date.format(bt.date.addMonth(date,3)));
	}else if(floatWay == "05"){//按半年浮动
		nextFloatWay(6,"HY");
		//rateData.val(bt.date.format(bt.date.addMonth(date,6)));
	}else if(floatWay == "06"){//按年浮动
		nextFloatWay(12,"Year");
		//rateData.val(bt.date.format(bt.date.addYear(date,1)));
	}
}

/**处理生效日期**/
function nextFloatWay(i ,postfix){
	var a = getMonthStr(i);
	var date = new Date(a);
	var strYear = date.getFullYear();
	var strMonth = date.getMonth()+1;
	var rateData = $("input[name='rateEntriesEffectDate']");
	if(strMonth<10){
		strMonth = "0" + strMonth;
	}
	var rateDate = $("input[name='rateEntriesEffectDate']");
	var nextSelect = dict.get("floatWayNext"+postfix).val();
	if(nextSelect=="1"){
		rateDate.val(strYear+"-"+strMonth+"-01");
	}else if(nextSelect == "2"){
		rateData.val(getMonthStr(i));
	}else if(nextSelect=="3"){
		var action = "/cms/provide/loaninfo/queryBatchInstDay";
		$.get(basePath + action,function(data){
			rateData.val(strYear + "-" + strMonth + "-" + (parseInt(data)+1)+"");
		},'JSON')
	}else{
		rateData.val("");
	}
}
/**加载还款计划*/
function repayLan() {
	var loanCode = $("#sendId").val();//借款单号
	var planId = $("#planId").val();
	var loanRate = rateType.get("loanRate").val();//借款利率
	var loanDate = dateTypes.get("loanDate").val();//借款日期
	var repayDate = dateTypes.get("repayDate").val();//还款日期
	var repayType = selectTypes.get("repayType").val();//还款方式
	var loanMoney = moneyType.get("loanMoney_biz").val();//金额
	// var rateBase = $("[name='rateBase']:checked").val();

	if (bt.empty(loanDate)) {
		parent.layer.msg(bt.lang("cms.provide.loaninfo.pldateempty"), {icon: 2});
		return false;
	}
	if (bt.empty(repayDate)) {
		parent.layer.msg(bt.lang("cms.provide.loaninfo.repaydateempty"), {icon: 2});
		return false;
	}
	if (bt.empty(repayType)) {
		parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
		return false;
	}
	if (bt.empty(loanMoney)) {
		parent.layer.msg(bt.lang("cms.provide.loaninfo.plmoneyempty"), {icon: 2});
		return false;
	}
	if (bt.empty(loanRate)||loanRate == 0) {
		parent.layer.msg(bt.lang("cms.provide.loaninfo.plrateempty"), {icon: 2});
		return false;
	}

	var params = "billCode=" + loanCode + "&loanRate=" + loanRate + "&loanDate=" + loanDate + "&repayDate=" + repayDate
		+ "&repayType=" + repayType + "&loanMoney=" + loanMoney + "&planId=" + planId;

	if (bt.navtab) {
		// 计提计息改造暂时注掉，使用新版计提计息页面
		// bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/toRepayPlan?" + params);
		bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/makeRepayPlan?" + params);
	}
}
/**
 * 还款方式选择回调
 */
function repayTypeCallBack(data, el) {
		$("#planStatus").val("1");
}
/**计算AddMonthCount个月后的日期*/
function getMonthStr(AddMonthCount){
	var plDate = $.trim($("input[name='loanDate1']").val());
	var date = new Date(plDate);
	date.setMonth(date.getMonth()+AddMonthCount);//获取AddMonthCount天后的日期
	var daysInMonth = new Array([0],[31],[28],[31],[30],[31],[30],[31],[31],[30],[31],[30],[31]);
	var strYear = date.getFullYear();
	var strDay = date.getDate();
	var strMonth = date.getMonth();
	if(strYear%4 == 0 && strYear%100 != 0){
		daysInMonth[2] = 29;
	}
	if(strMonth + 1 == 13){
		strYear += 1;
		strMonth = 1;
	}
	else{
		strMonth += 1;
	}
	strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];
	if(strMonth<10){
		strMonth="0"+strMonth;
	}
	if(strDay<10){
		strDay="0"+strDay;
	}
	datastr = strYear + "-" + strMonth + "-" + strDay;
	return datastr;
}

function getRate(){
	var loanDate = $("input[name='loanDate1']").val();//贷款申请日期
	var referenceRateType = selectType.get("referenceRateType").val();//基准利率类型
	var curId = $("#curId").val();//币别
	var repayCycle = selectType.get("repayCycle").val();//贷款周期
	var pointsProportionSelect = dict.get("pointsProportionSelect").val();//浮动比例或点数类型
	var pointsProportionText = moneyType.get("pointsProportionText").val();//浮动点数或比例值
	var pl_rate=rateType.get("loanRate");//贷款利率值
	var action = "/cms/loan/cmsloaninfo/getRate";
	var param = {
			loanDate:loanDate,
			referenceRateType:referenceRateType,
			curId:curId,
			repayCycle:repayCycle
	};
	$.post(basePath + action, param,function(result){
   		 if(typeof result.success == "boolean" && !result.success){
   			pl_rate.val(0);
   			layer.msg(result.msg);
   			return ;
   		}
   		if(pointsProportionText != ""){
   	    	if(pointsProportionSelect == "01"){
   	    		pl_rate.val(result.rate+(pointsProportionText/100));
   	    		return;
   	    	}else{
   	    		pl_rate.val((result.rate)*(1+(pointsProportionText/100)));
   	    		return;
   	    	}
   	    }
   		pl_rate.val(result.rate);
   	},'JSON');
}

/**
 * 授信额度选择
 * @param params
 */
function beforeCpRation(){
	var corpId = $("#corpId").val();
	var curCode = $("#curCode").val();//币别
	var loanDate = $("#loanDate").val();//贷款日期
	if(bt.empty(loanDate)){
		plDate = $("#inputDate").val();//为空默认录入日期
		/*parent.layer.msg(bt.lang("cms.provide.loaninfo.pldateempty"), {icon: 2});
		return false;*/
	}
	var itemCode =$("#cpRatItemCode").val();//授信品种

	var beginDate = loanDate;
	//var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
	var params = {
			corpId : corpId,
			curCode : curCode,
			ratItemCode : itemCode,
			beginDate : beginDate,
			//isRmbRat : isRmbRat
	}
	return params;
}

//担保合同参数设置
function setOtherParam(param) {
    var corpId = $("#corpId").val();//网点id
    var startDate = $("#loanDate").val();//开始日期
    var endDate = $("#repayDate").val();//结束日期
    var curId = $("#curCode").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "" : endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}
