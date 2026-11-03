var form,dateType,dict,selectType;
bt.use(['form','layedit','moneyType','btdate','dateType','corp','selectType','dicts','rateType','btUpload'], function(){
    var $ = bt.jquery;
    form = bt.form();
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
     * 担保方式变更
     */
    changeLoanType=function(){
    	if(dict.get("loanType").val()!="01"){
    		$("input[data-name=suretyMoney]").attr("lay-verify","required");
    		$("[name=suretyExplain]").attr("lay-verify","required");
    	}else{
    		$("input[data-name=suretyMoney]").attr("lay-verify","");
    		$("[name=suretyExplain]").attr("lay-verify","");
    	}
    	form.renderIcon();
    }
    
    /**
     * 贷款期限，天转成月
     */
    ternUnitTran = function(){
        var startDate = new Date(dateType.get("loanDate").val());
        var endDate = new Date(dateType.get("repayDate").val());
    	var tern=0;
    	var ternUnit = dict.get("ternUnit").val();
    	if(ternUnit==1){//月
    		tern = $("#loanTerm").val();
    	}else if(ternUnit==2){//天
    		var loanTerm = $("#loanTerm").val();
    		tern = dataSubtract(startDate,endDate,"M");
    	}
    	return tern;
    }
  
    
    
    /**计算data1 与 data2 相差多少param单位 data为字符串*/
    dataSubtract = function(data1,data2,param){
        var dates;
    	if(param == "D"){
    		dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime()))/(1000*60*60*24)+1;     
    	}else{
    		var start = new Date(data1);
    	    var end = new Date(data2);
    	    
    	    var addMonth = end.getMonth() - start.getMonth();//月份差
    	    
    	    var startLastDay = mGetDate(start.getYear(),start.getMonth());//开始时间当前月总共多少天
    	    var endLastDay = mGetDate(end.getYear(),end.getMonth());//结束时间当前月总共多少天
    	    
    	    if(startLastDay == start.getDate()){//开始日期月末最后一天
    	    	if(endLastDay == end.getDate()){//结束日期月末最后一天
    	    		addMonth ++;//月份+1
    	    	}else{
    	    		if(end.getDate() > start.getDate()){//结束日期当月天数 > 开始日期单月天数
    		    		addMonth ++;//月份+1
    		    	}
    	    	}
    	    }else{
    	    	if(endLastDay == end.getDate()){//结束日期月末最后一天
    	    		addMonth ++;//月份+1
    	    	}else{
    	    		if(end.getDate() > start.getDate()){//结束日期当月天数 > 开始日期单月天数
    		    		addMonth ++;//月份+1
    		    	}
    	    	}
    	    }
    	    
    		dates = (end.getYear() - start.getYear())*12 + addMonth;     
    	}
        return  dates;
    }
    
    mGetDate = function(year, month){
        var d = new Date(year, month+1, 0);
        return d.getDate();
    }
    
    /**
     * 验证数据
     * @returns {Boolean}
     */
    validateFrom = function(){
    	if($("#loanTerm").val()<0){
    		layer.alert(bt.lang("cms.loan.termErro"), {icon:5});//贷款期限不能小于0!    
    		return false;
    	}
    	var loanTerm = ternUnitTran();
    	if(dict.get("loanClass").val()=="01" && loanTerm>12){
    		layer.alert(bt.lang("cms.loan.shortTermErro"), {icon:5});//短期贷款月份不允许超过12个月！
    		return false;
    	}else if(dict.get("loanClass").val()=="02" && (loanTerm<=12 || loanTerm>60)){
    		layer.alert(bt.lang("cms.loan.middleTermErro"), {icon:5});//中期贷款月份不允许小于12个月，大于60个月!
    		return false;
    	}else if(dict.get("loanClass").val()=="03" && loanTerm<=60){
    		layer.alert(bt.lang("cms.loan.longTermErro"), {icon:5});//长期贷款月份不允许小于60个月!
    		return false;
    	}
    	if(strLen(bt.selectType.init().get("purpose").val())>= 50){//用途长度控制
    		layer.tips(bt.lang("cms.loan.purposemorelength"), '#purposeShow');
    		return false;
    	}
    	var isHangCredit = bt.dicts.init().get("isHangCredit").val();
    	if("1" == isHangCredit){
    		var ratCode = $("#cpRatCode");
    		if(!ratCode){
    			parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
    			return false;
    		}
    		if(bt.empty(ratCode.val())) {
    			parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
    			return false;
    		}
    	}
    	return true;
    }
    
    //初始化日期
    dateType = bt.dateType.init();
    //初始化字典
    dict = bt.dicts.init();
    //初始化单位
    bt.corp.init();
    //初始化利率
    bt.rateType.init();
    //初始化下拉框
    selectType = bt.selectType.init();
    //格式化数字
    bt.moneyType.init();
    
    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand:true,
        gid: ''
    });
    
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    
    //监听提交
    form.on('submit', function(data){
    	var status=$(this).attr("value");
        var action = "/cms/loan/cmsloaninfo/save";
        if(!validateFrom()) return false;
        data.field.status = status;//提交状态
        data.field.attachmentId = attachments.getGroupId();
        var isHangCredit=bt.dicts.init().get("isHangCredit").val();
        if(!bt.empty(isHangCredit)&&isHangCredit=="1"){
            setCpRationData(data);//设置内部授信数据信息
        }
        var mask = layer.load(1, {shade: [0.1, '#FFF']});//#393D49//遮罩层
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    if(data.field.id == ""){
                        bt.navtab.refreshCurrent();
                    }else{
                        bt.navtab.closeCurrent();
                    }
                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });
    
    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
});

function setCpRationData(data){
    data.field.ratCode = $("#cpRatCode").val();//内部授信编号
    data.field.ratItemCode =bt.selectType.init().get("cpRatItemCode").val();//内部授信品种
}

/**
 * 设置期限
 */
setTerm = function(){
    var  startValue = dateType.get("loanDate").val();
    var  endValue = dateType.get("repayDate").val();
    var  startDate = new Date(startValue);
    var  endDate = new Date(endValue);
    var  days = 0;
    var  month = 0;
    var  tern = 0;
    if(startValue !=="" && endValue !==""){
      var ternUnit = $("select[name='ternUnit']>option:selected").val();
      month = dataSubtract(startValue,endValue,"M");
      days = (endDate-startDate)/(1000*60*60*24);
      tern = month;
      if(ternUnit == 1){//月
        $("#loanTerm").val(month);
      }else if(ternUnit == 2){//天
        $("#loanTerm").val(days);
      }
      if(tern<=12){
      	dict.get("loanClass").val("01");
      }else if(tern>12 && tern<=60){
      	dict.get("loanClass").val("02");
      }else if(tern>60){
      	dict.get("loanClass").val("03");
      }
    }
  }

setTerm1=function(){ //贷款日期变化时需重新选择授信额度单
	setTerm();
	$("#cpRatCode").val("");
	$("#cpRationBtn").hide();
}
/**
 * 单位改变回调
 * @param ele
 */
changeCorp=function(ele){

	$.get(basePath + "/cms/loan/cmsloaninfo/getCenNetId?corpId="+ ele,function(data){
		$('#netName').val(data.corpName);
		$('#netCode').val(data.corpId);
	},'JSON');

	/*$.ajax({
        url: basePath+"/basic/business/btpurpose?getPurpose",
        method: 'POST',
        data: {corpId: ele,sysBusType:2},
        dataType: 'json',
        success: function(data){
        	$("#purpose").empty();
        	$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
        	if(data && data.obj.length>0){
        		$.each(data.obj,function(index,item){
        			$("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
        		});
        	}
        	form.render();
        }
    });*/
	//查询对应网点的用途
	var url =basePath+"/cms/loan/cmsloaninfo/getPurpose?corpId="+ele;
	bt.selectType.init().get("purpose").attr('data-url',url);
}


/**
 * 用途回调
 */
purposeChange=function(ele,data){
	if(strLen(data.value)>= 50){
		layer.tips(bt.lang("cms.loan.purposemorelength"), '#purposeShow');
	}
}
/**
 * 授信额度选择
 * @param params
 */
function beforeCpRation(){
	var corpId = bt.corp.init().get("corpId").val();
	if(bt.empty(corpId) || corpId == 0){
		//选择单位
		parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
		return "";
	}
	var curCode = bt.selectType.init().get("curCode").val();//币别
	if(bt.empty(curCode)){
		//选择币别
		parent.layer.msg(bt.lang("cms.provide.loaninfo.curIdnull"), {icon: 2});
		return "";
	}
	var loanDate = dateType.get("loanDate").val();//贷款日期
	if(bt.empty(loanDate)){
		loanDate = $("#inputDate").val();//为空默认录入日期
	}
	var itemCode = bt.selectType.init().get("cpRatItemCode").val();//授信品种
    if(bt.empty(itemCode)){
        itemCode="";
    }
	var beginDate = bt.dateType.init().get("loanDate").val();
	var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
	var params = {
			corpId : corpId,
			curCode : curCode,
			ratItemCode : itemCode,
			beginDate : beginDate.replace(" ", ""),
			isRmbRat : isRmbRat
	}
	return params;
}

function setOtherParam(param){
    var corpId = bt.corp.init().get("corpId").val();//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    startDate = bt.dateType.init().get("loanDate").val();//开始日期
    endDate =  bt.dateType.init().get("repayDate").val();//结束日期

    var curId = bt.selectType.init().get("curCode").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

function selectBefore(){
    var corpId = bt.corp.init().get("corpId").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    }else{
        return true;
    }
}

//币种改变
function changeCur(){
    clearRatModel();//清空授信信息
}
