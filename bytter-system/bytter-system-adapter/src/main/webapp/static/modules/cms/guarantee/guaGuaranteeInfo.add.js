var dateTypes, moneyTypes, selectTypes, dicts,corp ;
var fun,isHookBond,isHangBail,curId,parameters,rateTypes,bondCurId,curExrate,standCurId,isRat,bondCurCorp="";
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable','rateType','bondAccInfo' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();

			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			selectTypes = bt.selectType.init();//下拉框
			dicts = bt.dicts.init();//字典
			bt.bankInfo.init();//联行号
			corp = bt.corp.init();

            rateTypes = bt.rateType.init();//
            bt.bondAccInfo.init();//保证金账号初始化
            isHangBail=$("#isHangBail").val();//参数设置是否挂保证金
			isRat=$("#ratContains").val();//参数设置是否挂接授信
            parameters = new Object();

            changeFcBond();

			//检查保函编号是否重复
		    $("#guaranteeNo").blur(function(){
		    	var guaranteeNo = $(this).val();
		    	if(bt.empty(guaranteeNo)){
		    		return;
		    	}
		    	$.post(basePath + "/letter/guarantee/guaGuaranteeInfo/checkLgNo", {
		    		guaranteeNo : guaranteeNo
				}, function(data) {
					if(data){
						layer.alert(bt.lang("table.guaGuarantee.guaranteeNoRepeat"));
						$("#guaranteeNo").val("");
					}
				}, 'JSON');
		    });

			/**判断字符串是否为空*/
		    bt.empty = function(ms){
		    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
		    		return true;
		    	}
		    	return false;
		    }

			//附件处理
			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});

			//授信显示与隐藏
			setRatModel();

		    var checkBefore = function(){
		    	if(!checkDate()){
		    		layer.alert(bt.lang("table.guaGuarantee.checkExpectEndDate"));
		    		return false;
		    	}
		    	return true;
		    }


			//监听提交
			form.on('submit', function(data) {
		    	bt.loading.show();//显示加载中
		    	if(!checkBefore()){
		    		bt.loading.hide(); //关闭加载中
		    		return false;
		    	}
		    	//授信判断
		    	var ratModel = bt.dicts.init().get("ratmodel").val();
		    	if("1" == ratModel){
                    // checkRatUMoney();//计算授信正式占用额度

		    		var ratCode = $("#ratCode");
		    		if(!ratCode){
		    			parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
		    			bt.loading.hide(); //关闭加载中
		    			return false;
		    		}
		    		if(bt.empty(ratCode.val())) {
		    			parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
		    			bt.loading.hide(); //关闭加载中
		    			return false;
		    		}
		    		// var lgMoney = moneyTypes.get("lgMoney").val();//保函金额

                    var ratApplyMoney=$('#ratApplyMoney').val();
                    var itemMoney = $("#itemMoney").val();
                    if(bt.money.sub(ratApplyMoney, itemMoney) > 0){
                        parent.layer.msg(bt.lang("message.confirm.checkRatUMoney"), {icon: 2});
                        bt.loading.hide(); //关闭加载中
                        return false;
                    }

		    	}

		    	var isOpenRat=dicts.get("isOpenRat").val();
		    	if(bt.empty(isOpenRat)){//如果为空，默认不启用敞口
                    isOpenRat="0";
				}
                 data.field.isOpenRat=isOpenRat;
		    	//授信判断结束

				if(isHangBail=="1"){
					if(isHookBond=="1"){
                        var bondMoney=moneyTypes.get("bondMoney").val();//保证金金额
                        if(Number(bondMoney)==Number(0)){//保证金金额不能等于0
                            layer.alert(bt.lang("fcbond.check.bailmoneyIsNull"),{icon:2});
                            bt.loading.hide(); //关闭加载中
                            return false;
                        }
					}
                    //设置保证金相关参数
                    data.field.bondPercent = rateTypes.get("bondPercent").val();//保证金比例;
                    bondCurId=selectTypes.get("bondCurId").val();
                    data.field.bondCurId=bondCurId;//保证金币别
                    data.field.bondMoney=moneyTypes.get("bondMoney").val();//保证金金额
                    data.field.bondAcc=$("#bondAcc").val();//保证金账户
                    data.field.bondContract=$("#bondContract").val();//保证金合同
				}

                data.field.isRmbRat=$("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币

				var action = "/letter/guarantee/guaGuaranteeInfo/tempOrSubmit";
				data.field.status = $(this).attr("value");
				data.field.attachmentId = up.getGroupId();
                data.field.curName = selectTypes.get('curId').getData().obj.curName
                data.field.bankName = $("#openCorp").val();
                data.field.corpName = corp.get('corpId').data()[0].corpName
				$.post(basePath + action, data.field, function(result) {
					bt.loading.hide();
					parent.layer.alert(result.msg, {
						icon : result.success ? 1 : 2
					}, function() {
						parent.layer.closeAll();
						if (result.success) {
					        if(!bt.navtab){
			                      location.reload();
			                       bt.navtab.refreshParentTable();
			                          parent.layer?parent.layer.closeAll():layer.closeAll();
			                    } else {
			                      if(data.field.id == ""){
			                        bt.navtab.refreshCurrent();
			                      }else{
			                        bt.navtab.closeCurrent();
			                      }
			                  }
						}
					})
				}, 'JSON');
				return false;
		});
            showIsRmbRat();//设置隐藏显示授信折算
	});

	bt.bankInfoBeforeClick = function () {
	    return true;
	}

	//银行账号选择
	function bankinfoCallback(data,dom){
		$("#openCorp").val(data.bankName);
		$("#bankCode").val(data.bankCode);//联行号
		bt.reselectRatModel();
	}

    //开立方式
	function openWayCallback(){
		var openWay=bt.dicts.init().get("openWay").val();
		if("1"==openWay){
			$(".isShow").show();
			$("#turnBank").attr('lay-verify','required');
		}else{
			$(".isShow").hide();
			$("#turnBank").removeAttr('lay-verify');
		}
		form.renderIcon();
	}

	//根据保函性质设置截止日期是否必选
	function setEndDateProperty(){
		var guaranteeAttr=bt.dicts.init().get("guaranteeAttr").val();
		if("0"==guaranteeAttr){//敞口保函时结束日期可填，预计结束日期必填
			$("#endDate").removeAttr('lay-verify');
			$("#expectEndDate").attr('lay-verify','required');
		}else{
			$("#endDate").attr('lay-verify','required');
			$("#expectEndDate").removeAttr('lay-verify');
		}
		form.renderIcon();
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

	function checkDate(){
    	var startDate = $("#startDate").val();
    	if(bt.empty(startDate)){
    		return true;
    	}
    	var expectEndDate = $("#expectEndDate").val();
    	if(bt.empty(expectEndDate)){
    		return true;
    	}
    	var days = getBetweenDay(startDate, expectEndDate) - (-1);
    	if(days>0){
    		return true;
    	}else{
    		return false;
    	}
	}

	/**
	 * 授信额度选择
	 * @param params
	 */
	function beforeRation(){
		var corpId = bt.corp.init().get("corpId").val();
		if(bt.empty(corpId) || corpId == 0){
			//选择保函申请人
			parent.layer.msg(bt.lang("cms.gua.guarantee.corpIdnull"), {icon: 2});
			return "";
		}
		var curId = bt.selectType.init().get("curId").val();//币别
		if(bt.empty(curId)){
			//选择币别
			parent.layer.msg(bt.lang("cms.gua.guarantee.curIdnull"), {icon: 2});
			return "";
		}
		var bankCode = $("#bankCode").val();//银行
		if(bt.empty(bankCode.trim())){
			//选择保函开立人
			parent.layer.msg(bt.lang("cms.gua.guarantee.bankIdnull"), {icon: 2});
			return "";
		}
		var itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
        if(bt.empty(itemCode)){
            itemCode="";
        }
		var beginDate = bt.dateType.init().get("startDate").val();

        var isRmbRat=$("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
		if(isRmbRat=="1"){//折合本位币时，只能选择与本位币同币别的授信
            curId=standCurId;
		}
		var ratCode=$("#ratCode").val();
		var params = {
				corpId : corpId,
				curCode : curId,
				bankCode : bankCode,
			    ratCode:ratCode,
				ratItemCode : itemCode,
				beginDate : beginDate.replace(" ", "")
		}
		return params;
	}

	//选择授信
	function chooseRatCode(params){
		var params = beforeRation();
		if(bt.empty(params)){
			return;
		}
		openRation(params);
	}

	//查看授信
	function viewRatCode(){
		var params = beforeRation();
		if(bt.empty(params)){
			return;
		}
		viewRationDtl(params);
	}

	//重新选择授信
	bt.reselectRatModel = function(){
		clearRatModel();
	}

	//单位选择后的回调函数
	bt.corpCallback = function(data){
        bondCurCorp=data.id;
        standCurId = data.curId;//获取单位的本位币

        if(!bt.empty(isRat)&&isRat=="1"){
            bt.reselectRatModel();//清空授信信息
            setCurRat();//重新计算折算汇率
            showIsRmbRat();//重新选择单位后需要重新加载是否折合本位币的显示
        }

        if(isHangBail=="1") {//挂接保证金时重新加载保证金币别
            setBondCurList();
        }

	}

	bt.changStartDate = function(){
		bt.reselectRatModel();
        setCurRat();
	}

   //重新选择保函币别事件
	bt.changeCurId=function(){
		if(isHangBail=="1"){//页面有保证金信息，业务币别发生变化时保证金额度随着变化
            setBondCurList();//设置保证金可选币别
            setBondChangeRate();//设置保证金折算汇率
		}
        showIsRmbRat();//是否折合本位币显示
        setCurRat();
        bt.reselectRatModel();
	}

//选择保证金额度单之前校验和设置数据
function checkCorp() {
	var corpId=corp.get("corpId").val();
    var bankCode = $("#bankCode").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var endDate1 = $("#expectEndDate").val();
    var guaranteeAttr = dicts.get("guaranteeAttr").val();

    if(guaranteeAttr == '0'){//敞口保函
        if(bt.empty(endDate1)) {
            parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
            return false
        }
        parameters.endDate = endDate1;
    }else {
        if(bt.empty(endDate)) {
            parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
            return false
        }
        parameters.endDate = endDate;
    }

    if(bt.empty(corpId)){
        layer.alert(bt.lang("gua.message.corp.null"), {icon: 2});
        return false;
	}else if (bt.empty(bankCode)) {
        layer.alert(bt.lang("gua.message.bank.null"), {icon: 2});
        return false;
    }else if(bt.empty(startDate)){
        parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
        return false
    }else {
        parameters.corpId =corp.get("corpId").val();
        parameters.bankCode = $("#bankCode").val();//保函开立人
        parameters.curId = selectTypes.get("bondCurId").val();
        // parameters.curId = bondCurId;
        parameters.startDate = startDate;
        bt.bondAccInfo.set(parameters);
        return true;
    }

}

//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data, dom) {
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondMoneySum").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bondAcc").val(data.bankAcc);//保证金账户
    $("#bondCorpId").val(data.corpId);
    $("#bondBankId").val(data.bankCode);
    $("#bondAccCurId").val(data.curId);
    if(bt.empty(data.bondRate)){
        rateTypes.get("bondRate").val(0);
	}else{
        rateTypes.get("bondRate").val(data.bondRate);
	}

}

// 是否挂保证金，切换事件
function changeFcBond(){
    isHookBond=dicts.get("isHookBond").val();
    if(isHookBond=="0"){//隐藏选择保证金额度单部分
        $(".bondInfo").hide();
        $("#bondAcc").removeAttr('lay-verify');
        rateTypes.get("bondPercent").attr('lay-verify',false);
        moneyTypes.get("bondMoney").removeAttr("lay-verify");
        selectTypes.get("bondCurId").attr('lay-verify',false);
        $("#bondAcc").val("");
        // moneyTypes.get("bondMoney").val("");
        // rateTypes.get("bondPercent").val("");
    }else if(isHookBond=="1"){
        $(".bondInfo").show();
        $("#bondAcc").attr('lay-verify','required');
        rateTypes.get("bondPercent").attr('lay-verify','required');
        moneyTypes.get("bondMoney").attr('lay-verify','required');
        selectTypes.get("bondCurId").attr('lay-verify',true);
    }
    form.renderIcon();
}

// 保证金币种触发事件
function changeBondCur() {
    bondCurId = selectTypes.get("bondCurId").val();
    setBondChangeRate();//设置保证金折算汇率
    cleanFcbondMsg();//如果是挂接保证金，币别发生变化时需要重新选择保证金账户
}

//设置保证金折算汇率
function setBondChangeRate() {
    var curId=selectTypes.get("curId").val();
    var startDate = bt.dateType.init().get("startDate").val();//保函开始时间
    //币别与保函币别不一致时获取折算汇率
    if(bondCurId!=curId){
        $.post(basePath + "/letter/guarantee/guaGuaranteeInfo/getNearCurRat", {
            curId: curId,//保函币别
            convertCurId: standCurId,//单位本位币
            guaDate: bt.empty(startDate) ? startDate : startDate + " 23:59:59"
        }, function (retData) {
            if (retData.success) {
                curExrate=retData.obj;
                setBailMoney();//数据返回成功后重新设置保证金金额
            }else{
                parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
            }
        }, 'JSON');
    }else{
        curExrate=1;
        setBailMoney();//数据返回成功后重新设置保证金金额
    }

}

//清空保证金信息
function cleanFcbondMsg() {
    isHookBond=dicts.get("isHookBond").val();
    if(isHookBond=="1"){
        var bondAccCurId=$("#bondAccCurId").val();
        if(!bt.empty(bondAccCurId)){
            if(bondCurId!=bondAccCurId){//当重新选择保证金币种，与之前选择的保证金额度单不一致时，清空之前的保证金信息
                $("#bondId").val("");//保证金Id
                $("#bondMoneySum").val("");//保证金账户余额
                $("#accAvailMoney").val("");//可用余额
                $("#bondAcc").val("");//保证金账户
                $("#bondCorpId").val("");
                $("#bondBankId").val("");
                $("#bondAccCurId").val("");
            }
        }
    }
}
// 保证金比例触发事件
function setBailMoney(){
    var moneysum = moneyTypes.get("lgMoney").val();//保函金额
    var bondPercent = rateTypes.get("bondPercent").val();//保证金比例
    isHookBond=dicts.get("isHookBond").val();
    if (bt.empty(moneysum) || (isHookBond == null)) {
        return;
    }

    if (!bt.empty(bondPercent) && bondPercent != 0) {
        var  bondMoney=fun.format(fun.mul(Number(moneysum),Number(bondPercent)),{decimal: 2});
        if(curId==bondCurId) {//保函币别与保证金币别一致时，保证金金额=保函金额*保证金比例
            bondMoney=bondMoney;
        }else if(curId!=bondCurId){//保函币别与保证金币别不一致时，保证金金额=保函金额*保证金比例*汇率
            bondMoney=fun.format(fun.mul(bondMoney, curExrate),{decimal: 2});
        }
        moneyTypes.get("bondMoney").val(bondMoney);
    }

    if(isRat=="1"){
        calRatApplyMoney();//计算授信正式占用金额
    }
}
// 保证金金额触发事件
function setBailMoneyRate(){
    var moneysum = moneyTypes.get("lgMoney").val();//保函金额
    var bailmoney = moneyTypes.get("bondMoney").val();//保证金金额

    isHookBond=dicts.get("isHookBond").val();
    if (bt.empty(moneysum) || (isHookBond == null)||bt.empty(bailmoney)) {
        return;
    }

    if (bt.money.sub(bailmoney, moneysum) > 0) {//判断保证金金额是否大于保函金额
        parent.layer.msg(bt.lang("cms.gua.checkBondMoney"), {icon: 2});
        moneyTypes.get("bondMoney").val(moneysum);
        rateTypes.get("bondPercent").val('100');
        return;
    }

    if (!bt.empty(bailmoney) && bailmoney != 0) {
        if(curId==bondCurId) {//保函币别与保证金币别一致时，保证金比例=保证金金额/保函金额
            rateTypes.get("bondPercent").val(fun.div(bailmoney, moneysum, 8));
        }else if(curId!=bondCurId){//保函币别与保证金币别不一致时，保证金比例=保证金金额/（保函金额*汇率）
            var exchangeMoney=fun.format(fun.mul(moneysum, curExrate), {separator: "", decimal: 2});
            rateTypes.get("bondPercent").val(fun.div(bailmoney, exchangeMoney, 8));
        }
    }

    if(isRat=="1"){
        calRatApplyMoney();//计算授信正式占用金额
    }

}

//设置保证金可选币别
function setBondCurList() {
    //初始化保证金可选币别
    var data = selectTypes.get("curId").getData().obj;
    if(!bt.empty($("#id").val())){
        bondCurCorp=$("#corpId").val();
    }
    selectTypes.get("bondCurId").attr({
        "data-url": basePath + "/letter/guarantee/guaGuaranteeInfo/getBondCurList?billCurId=" + data.id + "&corpId=" +  bondCurCorp,
        "data-value": data.id
    });
    bondCurId = data.id;
    curId = data.id;

    //是否折合本位币显示
    standCurId = bt.empty(standCurId) ? $("#standCurId").val() : standCurId;
    if (data.id != standCurId) {
        $("#isRmbRatTr").show();
    }
}

//重新计算保证金比例和保证金金额以及授信占用金额
function cirCalMoney() {
		if(isHangBail=="1"){
            setBailMoney();
		}
		if(isRat=="1"){
            calRatApplyMoney();//计算授信正式占用金额
		}
}

/**显示本位币*/
function showIsRmbRat(){
    var data=selectTypes.get("curId").getData().obj;
    var ratmodel = bt.dicts.init().get("ratmodel").val();
    if ($("#ratContains").val() == 1) {
        if(!bt.empty(data.id)){
            if(data.id!=standCurId && standCurId != null && ratmodel == "1"){//当前选中币别非当前单位的本位币，则显示是否折合本位币
                $("#isRmbRatTr").show();
            }else{
                $("#isRmbRatTr").hide();
                moneyTypes.get("exchangerate").val("");
            }
        }else{
            $("#isRmbRatTr").hide();
            moneyTypes.get("exchangerate").val("");
        }
    }
}

//设置币别汇率
function setCurRat(){
    var ratModel = bt.dicts.init().get("ratmodel").val();
    if(bt.empty(ratModel) || "0" == ratModel) {
        return;
    }
    var startDate = bt.dateType.init().get("startDate").val();//保函开始时间
    if(bt.empty(startDate)){
        return ;
    }

    var data = selectTypes.get("curId").getData().obj;//获取币别选择的全部数据
    var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    if(isRmbRat == 0) {//不显示外币汇率
        $("#exchangerateTd1").hide();
        $("#exchangerateTd2").hide();
        moneyTypes.get("exchangerate").val("");
    } else {
        $("#exchangerateTd1").show();
        $("#exchangerateTd2").show();
        if (data.id == standCurId) {
            moneyTypes.get("exchangerate").val("1");
        } else {
            $.post(basePath + "/letter/guarantee/guaGuaranteeInfo/getNearCurRat", {
                curId: data.id,//保函币别
                convertCurId: standCurId,//单位本位币
                guaDate: bt.empty(startDate) ? startDate : startDate + " 23:59:59"
            }, function (retData) {
                if (retData.success) {
                    moneyTypes.get("exchangerate").val(retData.obj);
                }else{
                    selectTypes.get("curId").val("");
                    moneyTypes.get("exchangerate").val("");
                    parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                }
            }, 'JSON');
        }
    }
}

function ratInfoExtSet(itemParams){
    checkRatUMoney();
}

function checkRatUMoney(){
    var isOpenRatParam=$("#startOpenRat").val();
    var isOpenRat="";
    if(isOpenRatParam=="0"){
        isOpenRat=0;
    }else if(isOpenRatParam=="1"){
        isOpenRat=bt.dicts.get("isOpenRat").val()
    }

    var ratCurId = bt.selectType.init().get("curId").val();//授信币别默认与业务币别一致
    var isRmbRat=$("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    if(isRmbRat=="1"){//折合本位币时，只能选择与本位币同币别的授信
        ratCurId=standCurId;
    }
    var params = {
        curCode : ratCurId,//授信币种
        initCurCode :bt.selectType.init().get("curId").val(),//业务币种
        billMoney : moneyTypes.get("lgMoney").val(),//业务金额
        money : moneyTypes.get("lgMoney").val(),//业务金额
        bondMoney : moneyTypes.get("bondMoney").val(),//保证金金额
        bondCurId : selectTypes.get("bondCurId").val(),//保证金币种
        isOpenRat:isOpenRat//是否启用授信敞口
    }

    var param = parseParam(params);
    var action = "/cms/rat/raturationapply/getRatUrationMoney";
    $.post(basePath + action,param,function(returnData){
        if(!bt.empty(returnData)){
            $('#ratApplyMoney').val(returnData);//设置正式占用金额
            var itemMoney = $("#itemMoney").val();
            if(bt.money.sub(returnData, itemMoney) > 0){
                parent.layer.msg(bt.lang("message.confirm.checkRatUMoney"), {icon: 2});
                bt.loading.hide(); //关闭加载中
            }
        }
    },'JSON');

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

function calRatApplyMoney(){
    var ratModel = bt.dicts.init().get("ratmodel").val();
    var itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
    if(ratModel=="1"&&!bt.empty(itemCode)){
        checkRatUMoney();
    }
}

