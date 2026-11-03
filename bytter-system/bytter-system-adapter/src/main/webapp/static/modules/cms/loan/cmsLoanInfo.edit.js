var dateType,dict;
bt.use(['form','layedit','moneyType','btdate','dateType','corp','selectType','dicts','rateType','btUpload'], function(){
    var $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    
    changeCorp=function(ele){

		$.get(basePath + "/cms/loan/cmsloaninfo/getCenNetId?corpId="+ ele,function(data){
			$('#netName').val(data.corpName);
			$('#netCode').val(data.corpId);
		},'JSON');

    	var url = basePath + "/cms/loan/cmsloaninfo/getPurpose?corpId="+ele;
    	selectType.get("purpose").attr('data-url',url);
    	//.get("purpose").attr('data-defaultvalue',"");
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
    	if(strLen(selectType.get("purpose").val())>100){//用途长度控制
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
    var selectType = bt.selectType.init();
    //格式化数字
    bt.moneyType.init();
    
    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand:true,
        gid: $("#attachmentgid").val()
    });
    
    changeLoanType();
    
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
    form.on('submit(submit)', function(data){
    	var status=$(this).attr("value");
        var action = "/cms/loan/cmsloaninfo/modify";
        data.field.status = status;//提交状态
        data.field.attachmentId = attachments.getGroupId();
        var isHangCredit=dict.get("isHangCredit").val();
        if(!bt.empty(isHangCredit)&&isHangCredit=="1"){
            setCpRationData(data);//设置授信信息
		}
		var result = false;
		if(!validateFrom()) return false;
		var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
		$.ajax({
			url: basePath + "/cms/loan/cmsloaninfo/check",
			method: 'GET',
			async: false,
			data: {corpId: data.field.corpId,isHangGuarantee: data.field.isHangGuarantee},
			dataType: 'json',
			success: function (t) {
				if(!t){
					layer.close(mask);
					parent.layer.msg(bt.lang("cms.check.corpId.cmsLoanInfo"), {icon: 2});
				}else{
					$.post(basePath + action,data.field,function(result){
						parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
						if(result.success){
							layer.close(mask);
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


				}
			}
		});
		return false;


    });

    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
});

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
    var  loanDate = $("#loanDate").val();
    if(!(startValue==loanDate)){//如果更改贷款日期需要重新选择授信额度单
    	$("#ratCode").val("");
    	$("#rationBtn").hide();
    }
  } 
/**
 * 用途回调
 * @param ele
 * @param data
 */
purposeChange=function(ele,data){
	if(strLen(data.value)>100){
		layer.tips(bt.lang("cms.loan.purposemorelength"), '#purposeShow');
	}
}
/**
 * 字符串长度
 * @param str
 * @returns {Number}
 */
strLen=function(str) {  
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
	var loanDate =  bt.dateType.init().get("loanDate").val();//贷款日期
	if(bt.empty(loanDate)){
		loanDate = $("#inputDate").val();//为空默认录入日期
	}
	var itemCode = bt.selectType.init().get("cpRatItemCode").val();//授信品种
	
	var beginDate = bt.dateType.init().get("loanDate").val();
	var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
	var params = {
			corpId : corpId,
			curCode : curCode,
			ratItemCode : itemCode,
			beginDate : beginDate,
			isRmbRat : isRmbRat
	}
	return params;
}

function setOtherParam(param){
    var corpId = bt.corp.init().get("corpId").val();//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    if(viewFlag=="1"){
        startDate = $("#startDate").val();//开始日期
        endDate = $("#enddate").val() ;//结束日期
    }else{
        startDate = bt.dateType.init().get("loanDate").val();//开始日期
        endDate =  bt.dateType.init().get("repayDate").val();//结束日期
    }

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
    clearCpRatModel();//清空授信信息
}

//设置内部授信数据
function setCpRationData(data){
    data.field.ratCode = $("#cpRatCode").val();//内部授信编号
    data.field.ratItemCode =bt.selectType.init().get("cpRatItemCode").val();//内部授信品种
    data.field.urationId = $("#cpUrationId").val();//授信占用的id
}