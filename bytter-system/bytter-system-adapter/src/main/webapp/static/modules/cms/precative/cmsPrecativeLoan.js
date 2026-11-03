var floatWayChangeFlag={
    isChange:false,
    flag:false
}
//初始化贷款周期
function initRepayCycle(type){
	loadLoanRate();
	//selectType.render();
	if(type == '01' || type == "" ){
		//selectType.get("referenceRateType").val({"disabled":true});
		selectType.get("repayCycle").val({"disabled":true});
		//dicts.get("pointsProportionSelect").val({ disabled:true});
		//$("#pointsProportionText").attr("disabled","disabled");
		//moneyType.get("pointsProportionText").attr({"disabled":"disabled"});
	}else{
		//selectType.get("referenceRateType").val({"disabled":false});
		selectType.get("repayCycle").val({"disabled":false});
		//dicts.get("pointsProportionSelect").val({ disabled:false});
		//$("#pointsProportionText").removeAttr("disabled");
		//moneyType.get("pointsProportionText").attr({"disabled":""});
	}
	selectType.render();
	if(type !== '01' && type !== '02' && type !=""){
		setNextDetail();
	}
}


/**加载基准利率和利率期限*/
function loadLoanRate(){
	//var data = selectType.get("curId").currentObject.find("option:selected").data();//获取币别选择的全部数据
	var curId = $("#curId").val();
	var isNatualCurrency = $("#isNatualCurrency").val();
	if(isEmpty(curId)){
		selectType.get("referenceRateType").attr("data-url","");
	}else if(isNatualCurrency == "false"){
		selectType.get("referenceRateType").attr("data-url",basePath+"/cms/loanrate/maintence/queryNotBaseRate");
	}else{//本位币
		selectType.get("referenceRateType").attr("data-url",basePath+"/cms/loanrate/maintence/queryBaseRate");
	}
}

/**加载浮动利率的周期信息*/
function loadrepayCycle(){
	var curId = $("#curId").val();
	var isNatualCurrency = $("#isNatualCurrency").val();
	//var data = selectType.get("repayCycle").currentObject.find("option:selected").data();//获取币别选择的全部数据
	var data = setPeriod();
	var pointsProportionTextObj = moneyType.get("pointsProportionText");
	var sel = dicts.get("pointsProportionSelect").val();
	if(isNatualCurrency == "false"){
		dicts.get("pointsProportionSelect").val("01");
		$("#inputMark").text("BP");
		//dicts.get("pointsProportionSelect").attr({'data-disabledValue':'02'});
		dicts.render();
		if(sel != "01"){
			pointsProportionTextObj.val(0);
		}
	}else{//本位币
        var rateType = selectType.get("referenceRateType").getData();
        var code = rateType.obj.code;
        if(code=="lpr"){
            dicts.get("pointsProportionSelect").val("01");
            $("#inputMark").text("BP");
        }else {
            dicts.get("pointsProportionSelect").val("02");
            $("#inputMark").text("%");
        }
		//dicts.get("pointsProportionSelect").attr({'data-disabledValue':'01'});
		if(sel != "02"){
			pointsProportionTextObj.val(0);
		}
	}
	//设置text的输入范围
	pointsProportionTextObj.attr("data-min",$(data).attr("data-ratedownlimit"));
	pointsProportionTextObj.attr("data-max",$(data).attr("data-rateuplimit"));
	pointsProportionTextObj.reset();
	readonlyText();
}

function setPeriod(){
	var options = $("select[name='repayCycle'] option");//获取币别选择的全部数据
	var ternUnit = $("#ternUnit").val();
	var tern;
	var cur=0;
	if(ternUnit == "M"){
		tern = gettern($("#term").val(),4);
	}else if(ternUnit == "D"){
		tern = gettern($("#term").val(),6);
	}
	if (options.length > 1) {
        options.each(function (index, element) {
            var term = gettern(element.getAttribute("data-period"), element.getAttribute("data-periodcycle"));
            if (parseFloat(tern) <= parseFloat(term)) {
                return false;
            }
            if (options[index].getAttribute("data-periodcycle") == 7) {
                cur -= 1;
                return false;
            }
            cur = index;
        })
        cur += 1;
        var repayCycleId = $(options[cur]).data("creditperiodid");
        if (repayCycleId == null) {
            cur = 1;
        }
        selectType.get("repayCycle").val($(options[cur]).data("creditperiodid"));
        selectType.render();
    }
	return options[cur];
}

/**根据周期类型获取对应年数*/
function gettern(period,cycle){
	var tern = 0;
	if(cycle==1){//年
		tern = period;
	}else if(cycle==2){//半年
		tern = moneyFun.div(period,2);
	}else if(cycle==3){//季
		tern = moneyFun.div(period,4);
	}else if(cycle==4){//月
		tern = moneyFun.div(period,12);
	}else if(cycle==5){//周
		tern = moneyFun.div(period,moneyFun.div(365,7));
	}else if(cycle==6){//日
		tern = moneyFun.div(period,365);
	}else if(cycle==7){//贴现
		tern = 1;
	}
	return tern;
}

function readonlyText(){
	var floatWay = dicts.get("floatWay").val();
	if(floatWay == "01"){//固定利率
		var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
		pointsProportionTextObj.val(0);
		pointsProportionTextObj.attr("data-min",0);
		pointsProportionTextObj.attr("data-max",0);
		pointsProportionTextObj.attr("readonly",true);
	}else{
		var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
		pointsProportionTextObj.attr("readonly",false);
	}
}

/**浮动方式选择月、季、半年、年生产对应的详情选择 用来计算利率生效日期*/
function setNextDetail(){
	var floatWay = dicts.get("floatWay").val();
    floatWayChangeFlag.isChange = true;
	var rateData = $("input[name='rateEntriesEffectDate']");
	rateData.attr("readonly",false);
	rateData.attr("lay-verify","required");
	var postfix = "";
	var htl = '';
	if(floatWay == "01"){//固定利率
		rateData.val("");
		//rateData.attr("readonly",true);
		rateData.attr("lay-verify","");
        floatWayChangeFlag.flag = false;
		elementDisabledOrEnabled(false);
	}else if(floatWay == "02"){//即时浮动
		rateData.val($("input[name='createDate']").val());
        floatWayChangeFlag.flag = true;
		elementDisabledOrEnabled(true);
	}else if(floatWay == "03"){//按月浮动
		postfix = "Month";
        floatWayChangeFlag.flag = true;
		elementDisabledOrEnabled(true);
	}else if(floatWay == "04"){//按季浮动
		postfix = "Season";
        floatWayChangeFlag.flag = true;
		elementDisabledOrEnabled(true);
	}else if(floatWay == "05"){//按半年浮动
		postfix = "HY";
        floatWayChangeFlag.flag = true;
		elementDisabledOrEnabled(true);
	}else if(floatWay == "06"){//按年浮动
		postfix = "Year";
        floatWayChangeFlag.flag = true;
		elementDisabledOrEnabled(true);
	}
	if(postfix == ""){
		$("#nextDetail").html("");//删除页面元素
	}else{
		var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext'+ postfix +'" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay'+ postfix +'" ></div>';
		$("#nextDetail").html(htl);
		bt.dicts.render();
	}
	/*if(postfix != ""){
		htl = '<div class="bt-biz-dict" data-name="nextSelect" data-type="select" data-biz-name="nextSelect" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay'+ postfix +'" ></div>';
	}
	$("#nextDetail").html(htl);
	bt.dicts.render();*/
	//dateType.get("rateEntriesEffectDate").reset();
	readonlyText();
}

function elementDisabledOrEnabled(flag){
	//selectType.get("referenceRateType").val({"disabled":!flag});
	dicts.get("pointsProportionSelect").val({ disabled:true});
	var rateData = $("input[name='rateEntriesEffectDate']");
	if(flag){
		$("#pointsProportionText").attr("disabled",false);
		moneyType.get("pointsProportionText").attr("disabled",false);
        selectType.get("repayCycle").val({"disabled":false});
		//$("#getRate").show(); 
		rateData.attr("lay-verify","required");
	}else{
		moneyType.get("pointsProportionText").val(0);
		moneyType.get("pointsProportionText").attr("disabled","disabled");
        selectType.get("repayCycle").val({"disabled":true});
		//$("#getRate").hide(); 
		rateData.removeAttr("lay-verify");
	}
	form.renderIcon();
	dicts.get("pointsProportionSelect").render();
    selectType.render();
}

/**根据浮动方式详情设置利率调整生效日期*/
function setRateEntriesEffectDate(){
	var floatWay = dicts.get("floatWay").val();
	if(floatWay == "03"){//按月浮动
		nextFloatWay(1,"Month");
	}else if(floatWay == "04"){//按季浮动
		nextFloatWay(3,"Season");
	}else if(floatWay == "05"){//按半年浮动
		nextFloatWay(6,"HY");
	}else if(floatWay == "06"){//按年浮动
		nextFloatWay(12,"Year");
	}
}

/**处理生效日期*/
function nextFloatWay(i,postfix){
	var plDate = $.trim(dateType.get("startDate").val());
	if(isEmpty(plDate)){
		plDate = $.trim($("input[name='createDate']").val());//为空默认录入日期
	}
	var floatWay = dicts.get("floatWay").val();
	var floatWayNext = dicts.get("floatWayNext"+postfix).val();
	var date = getFloatDate(plDate, floatWay, floatWayNext, true, i);
	var strYear = date.getFullYear();     
    var strMonth = date.getMonth()+1;
    if(strMonth<10){
    	strMonth = "0" + strMonth;
    }
    var rateData = $("input[name='rateEntriesEffectDate']");
	if(floatWayNext=="1"){
		rateData.val(strYear+"-"+strMonth+"-01");
	}else if(floatWayNext=="2"){
		var day = parseInt(date.getDate());
		if(day<10){
    		day = "0" + day;
        }
		rateData.val(strYear + "-" + strMonth + "-" + day);//getMonthStr(i,date)
	}else if(floatWayNext=="3"){
		var action = "/cms/provide/loaninfo/queryBatchInstDay";
        $.get(basePath + action,function(data){
        	var day = parseInt(data) + 1;
        	if(day<10){
        		day = "0" + day;
            }
        	rateData.val(strYear + "-" + strMonth + "-" + day + "");
        },'JSON');
	}else{
		rateData.val("");
	}
}

/**
 * 获取日期sendDate的浮动生效日期
 * @param plDate 		日期
 * @param floatWay 		发动方式
 * @param floatWayNext  详细浮动方式
 * @param isStanderd	自然季(false)或标准季(true)
 * @param count		    不同浮动方式对应的数字
 * @return	浮动日期准确的年月信息
 */
function getFloatDate(plDate,floatWay,floatWayNext,isStanderd,count){
	var date = new Date(plDate);
	var day = date.getDate();//获取plDate的借款日
	
    date = new Date(date.getFullYear(), date.getMonth(), 1);//当月的第一天

    var intersetDay=21;//计息日默认为21号
    intersetDay = parseInt(intersetDay);

	if("02" == floatWay){//即时浮动
		return date;
	}
	var pd = isStanderd;
	if(count == 1){ 
		pd = false;
	}
	
	if(pd){//标准
		var plDateMonth = date.getMonth() + 1;
		if("1" == floatWayNext){//次《什么》第一天
			do {
	        	var a= getMonthStr(1, date);
	        	date = new Date(a);
	        	plDateMonth = date.getMonth() + 1;
	        } while (plDateMonth % count != 1);
		}else{
            if("3" == floatWayNext){//次**计息日次日
                if (day > intersetDay ) {//借款日在计息日之后，计息日取次月计息日，月份+13；借款日在计息日之前，计息日取当月计息日，月份+12
                    count += 1;
                }
                day += 1;
            }
			var a = getMonthStr(count, date);
	    	date = new Date(a);
			// while (plDateMonth % count != 0){
	        // 	var a = getMonthStr(1, date);
	        // 	date = new Date(a);
	        // 	plDateMonth = date.getMonth() + 1;
	        // }
		}
	}else{
		var a = getMonthStr(count, date);
		date = new Date(a);
	}
	
	return this.setMonthDay(day,date);
}

/**
 * 日期增加多少个月
 * @param addMonthCount	增加几个月
 * @param date			需要增加月份的日期
 */
var daysInMonth = new Array([0],[31],[28],[31],[30],[31],[30],[31],[31],[30],[31],[30],[31]); 
function getMonthStr(addMonthCount,date){ 
	date.setMonth(date.getMonth() + addMonthCount);//获取addMonthCount天后的日期   
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

/**
 * 日期增加多少个月
 */
function setMonthDay(day,date){
	var strYear = date.getFullYear();
	var strMonth = date.getMonth();
	day = daysInMonth[strMonth + 1] >= day ? day : daysInMonth[strMonth + 1];
	return date = new Date(strYear, strMonth, day);
}

//获取利率
function getRate(){
	var data = selectType.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
	var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
	var pointsProportionText = moneyType.get("pointsProportionText").val();//浮动点数或比例值
	var pl_rate=rateType.get("rate");//借款利率值
	if(pointsProportionText != ""){
    	if(pointsProportionSelect=="01"){//点数
    		//rateType.get("rate").val(data.rate+(pointsProportionText/100));
            //基础利率+浮动的点数*0.01%
            rateType.get("rate").val(data.rate+pointsProportionText*(0.01/100));
    		return;
    	}else{
    		//rateType.get("rate").val((data.rate)*(1+(pointsProportionText/100)));
            //比例 基准利率+浮动的比例*基准利率
            rateType.get("rate").val((data.rate+(pointsProportionText/100)*data.rate));
    		return;
    	}
    }
	pl_rate.val(data.rate);
	//checkRepayLanPrameter();*/
}

/**判断字符串是否为空*/
function isEmpty(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == ""){
		return true;
	}
	return false;
}

