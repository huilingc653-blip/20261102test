var select;
var form;
bt.use(['form','jquery','corp','dateType','dicts','rateType','datatable'], function(){
	$ = bt.jquery,form = bt.form();
   dicts = bt.dicts.init();//字典
	bt.dateType.init();
	form.on('select(cycle)', function(data){
		 loadData($("#repayCycel").val(data.value))
	 });
	 //生成列表
    billTable= bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/provide/cmsinterestway/datatable?id="+$("#id").val()
        },
        columns: [
                   {data:"interestWayName",bSortable:true,
					   render: function (data, type, full, meta) {
						   var text = "";
						   text += bt.dict("repay.frequency", full.interestFrequencyType);
						   if (full.interestFrequencyNext == "1") {
							   if (full.interestFrequencyNum == "num") {
								   text += " " + bt.dict("accrual.dayCycle", full.interestFrequencyNum);
								   return text;
							   }
						   }else if(full.interestFrequencyNext == "4"){
							   text += " " + bt.dict("accrual.seasonCycle", full.interestFrequencyNum);
							   return text;
						   }
						   text += " " + full.interestFrequencyNum;
						   text += " " + bt.dict("accrual.dateType", full.interestFrequencyNext);
						   return text;
					   }},
                   {data:"expiryDay",bSortable:true},
                   {data:"withholdingDay",bSortable:true},
                   {data:"activeDate",bSortable:true},
        ],

    });
    //监听提交
    form.on('submit', function(data){
        var action = "/cms/provide/cmsinterestway/save";
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
        	 bt.loading.hide();
        	parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
            		location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
            	} else {
            		if(data.field.id == ""){
            			bt.navtab.refreshCurrent();
            		}else{
            			bt.navtab.refreshParent();
            			bt.navtab.closeCurrent();
            		}
            	}
            }
        },'JSON');
        return false;
    });

	/**判断字符串是否为空*/
	bt.empty = function (ms) {
		if (typeof (ms) == "undefined" || ms == null || ms == "") {
			return true;
		}
		return false;
	}
});
bt.use(['selectType'], function() {
 select = bt.selectType.init();
//回调方法逻辑部分
 bt.text = function(data){
	 $("#expiryDayDiv").hide();
	 switch(data.value)
		{
		case '1':
			setDisplay(true,true);break;
		case '2':
			setDisplay(true,false);break;
		case '3':
			setDisplay(true,false);break;
		case '4':
			setDisplay(false,true);break;
		case '5':
			setDisplay(false,false);break;
		default: setDisplay(true,true);
		}
 }
});

function setDisplay(expiryDay,withholdingDay){
	if(!expiryDay){
		$("#expiryDayDiv").hide();
		var getCurrentSelect = select.get("expiryDay");
		$("select[name='expiryDay']").removeAttr("lay-verify");
		getCurrentSelect.val("0");
	}else{
		var getCurrentSelect = select.get("expiryDay");
		$("#expiryDayDiv").show();
		$("select[name='expiryDay']").attr("lay-verify","required");
		getCurrentSelect.val("21");
	}
	if(!withholdingDay){
		$("#withholdingDayDiv").hide();
		var getCurrentSelect = select.get("withholdingDay");
		getCurrentSelect.val("0");
		var p=getCurrentSelect.val();
		$("select[name='withholdingDay']").removeAttr("lay-verify");
	}else{
		var getCurrentSelect = select.get("withholdingDay");
		$("#withholdingDayDiv").show();
		getCurrentSelect.val("1");
		$("select[name='withholdingDay']").attr("lay-verify","required");
	}

	form.render();
	form.renderIcon();
}
//回调方法
function test(data){
bt.text(data)
}
doClose = function(){
	if(bt.navtab){
		bt.navtab.closeCurrent();
	}
}



/**显示利息支付频率第二列*/
function showInterestFrequencyType() {
	var interestFrequencyType = dicts.get("interestFrequencyType").val();
	if (bt.empty(interestFrequencyType)) {
		$("#showInterestFrequencyNext").html("");
	} else {
		// 按时间周期：accrual.dateType
		if (interestFrequencyType == "D") {
			$("#showInterestFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyNext" data-biz-name="interestFrequencyNextByDate" data-type="select" data-group="accrual.dateType" data-displayData="1,2,3,4,5" data-required="true" data-callback="showInterestFrequencyNextByDate"></div>');
		} else if (interestFrequencyType == "C") {
			// 按次数：repay.numberType
			$("#showInterestFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyNext" data-biz-name="interestFrequencyNextByNumber" data-type="select" data-group="repay.numberType" data-required="true" data-callback="showInterestFrequencyNextByNumber"></div>');
		} else {
			$("#showInterestFrequencyNext").html("");
		}
	}
	var expiryDateMonth = "expiryDateMonth";
	if ($("div[data-biz-name = 'expiryDateWeek']").length > 0) {
		expiryDateMonth = "expiryDateWeek";
	}
	if (interestFrequencyType == "D") {
		dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": false, "data-required": true});
	} else {
		dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": true, "data-required": false});
	}
	$("#showInterestFrequency").html("");
	$("#showInterestFrequencyNumInput").html("");
	bt.dicts.render();
	bt.form().renderIcon()
}



/**显示利息支付频率第三列：按时间周期*/
function showInterestFrequencyNextByDate() {
	var interestFrequencyNext = dicts.get("interestFrequencyNextByDate").val();
	if (bt.empty(interestFrequencyNext)) {
		$("#interestFrequencyNext").html("");
	} else {
		// 天：accrual.dayCycle
		if (interestFrequencyNext == "1") {
			$("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyDay" data-type="select" data-group="accrual.dayCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
		} else if (interestFrequencyNext == "2") {
			// 周：accrual.weekCycle
			$("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyWeek" data-type="select" data-group="accrual.weekCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
		} else if (interestFrequencyNext == "3") {
			// 月：accrual.monthCycle
			$("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencMonth" data-type="select" data-group="accrual.monthCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
		}else if (interestFrequencyNext == "4") {
			//,季：accrual.seasonCycle
			$("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencySeason" data-type="select" data-group="accrual.seasonCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
		} else if (interestFrequencyNext == "5") {
			// 年：accrual.yearCycle
			$("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyYear" data-type="select" data-group="accrual.yearCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
		} else {
			$("#showInterestFrequency").html("");
		}
	}
	var expiryDateMonth = "expiryDateMonth";
	if ($("div[data-biz-name = 'expiryDateWeek']").length > 0) {
		expiryDateMonth = "expiryDateWeek";
	}
	if (interestFrequencyNext == "1") {
		dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": true, "data-required": false});
	}else if (interestFrequencyNext == "2") {
		$("#expiryDateTd").html(
			"<div class=\"bt-biz-dict\" data-name=\"expiryDay\" data-type=\"select\"" +
			"data-biz-name=\"expiryDateWeek\" data-group=\"accrued.week\"" +
			"data-required=\"true\"></div>"
		);
	} else {
		$("#expiryDateTd").html(
			"<div class=\"bt-biz-dict\" data-name=\"expiryDay\" data-type=\"select\"" +
			"data-biz-name=\"expiryDateMonth\" data-group=\"bisGatherPolicy.month\"" +
			"data-required=\"true\"></div>"
		);
	}
	$("#showInterestFrequencyNumInput").html("");
	bt.dicts.render();
	bt.form().renderIcon()
}


/**显示利息支付第三列：按次数*/
function showInterestFrequencyNextByNumber() {
	var interestFrequencyNext = dicts.get("interestFrequencyNextByNumber").val();
	if (bt.empty(interestFrequencyNext)) {
		$("#showInterestFrequency").html("");
		$("#showInterestFrequencyNumInput").html("");
	} else {
		$("#showInterestFrequency").html("");
		$("#showInterestFrequencyNumInput").html('<input type="text" id="interestFrequencyNumInput" name="interestFrequencyNumInput" lay-verify="required" data-width="130px" autocomplete="off" class="layui-input">');
	}
	bt.dicts.render();
}

/**显示利息支付频率第四列：按时间周期后显示输入框*/
function callInterestFrequencyNumByDate(el, value, data) {
	if (bt.empty(data.value)) {
		$("#showInterestFrequencyNumInput").html("");
	} else {
		if (data.value == "other") {
			$("#showInterestFrequencyNumInput").html('<input type="text" id="interestFrequencyNumInput" name="interestFrequencyNumInput" lay-verify="required" data-width="130px" autocomplete="off" class="layui-input">');
		} else {
			$("#showInterestFrequencyNumInput").html("");
		}
	}
	bt.dicts.render();
}

