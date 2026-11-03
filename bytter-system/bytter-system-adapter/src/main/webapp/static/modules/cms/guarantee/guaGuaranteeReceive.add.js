var dateTypes, moneyTypes, selectTypes, dicts,isHookBond,curId,bondCurId,exchangeRate;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType','bank',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable','rateType' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			moneyTypes = bt.moneyType.init();//金额
			selectTypes = bt.selectType.init();//下拉框
			rateTypes = bt.rateType.init();//
			dicts = bt.dicts.init();//字典
			bt.bankInfo.init();//联行号
			fun = bt.moneyType.getFun();
			var corp = bt.corp.init();
            isHookBond=$("#isHookBond").val();
            curId=$("#curId").val();
            bondCurId=$("#bondCurId").val();
            exchangeRate=$("#exchangeRate").val();


			var guaranteeAttr = $("#guaranteeAttr").val();
			if(guaranteeAttr =="0"){
				$("#endDate").removeAttr('lay-verify');
				form.renderIcon();
			}else{
				$("#endDate").attr('lay-verify','required');
				form.renderIcon();
			}

            /**设置被担保单位**/
            $.each($(".selectTreeDemo"),function(i,item){
                var attr= $("#guaranteeedCorp").val().split(",");
                if(attr[0]==""){
                    return;
                } else if(attr.length>0){
                    selectTree = bt.selectTree.initGetCurrent({
                        url: basePath + '/letter/guarantee/guaGuaranteeInfo/listGuaranteedCorp?corps=' + $("#guaranteeedCorp").val(),
                        elem:".selectTreeDemo",
                        name:"guaranteedCorp",
                        selected:attr[0],//设置默认值为第一个单位
                        setting:{
                            data:{
                                simpleData: {
                                    pIdKey: "parentId"
                                }
                            },
                            check : {
                                chkboxType: { "Y": "s", "N": "s" }
                            }
                        },
                        beforeInit:function(nodes){
                            for(k in nodes){
                                nodes[k].name = "(" + nodes[k].code + ")" + nodes[k].name;
                            }
                            return nodes;
                        }
                    });
                }
            });
			// 附件处理
			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});

			//领取
			form.on('submit', function(data) {
                if(!checkBefore()){
                    return false;
                }
				var action = "/letter/guarantee/guaGuaranteeRecive/save";
				data.field.attachmentId = up.getGroupId();
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

            bt.changStartDate = function(){
                setCurRat();
            };

	});
//检查领取时间是否大于等于保函开始时间，小于等于保函结束时间
var checkBefore = function(){
    /**检查授信额度开始*/
    var ratApplyMoney=$('#ratApplyMoney').val();
    var itemMoney = fun.add( $("#itemMoney").val(),$("#ratApplyMoneyPre").val());
    if(bt.money.sub(ratApplyMoney, itemMoney) > 0){
        parent.layer.msg(bt.lang("message.confirm.checkRatUMoney"), {icon: 2});
        bt.loading.hide(); //关闭加载中
        return false;
    }
    /**检查授信额度结束*/

    var startDate = dateTypes.get("startDate").val();
    var guaranteeAttr=$("#guaranteeAttr").val();
    var endDate;
    if(guaranteeAttr==0){//敞口保函，取预计结束时间
        endDate= $("#expectEndDate").val();
    }else if(guaranteeAttr==1){//闭口保函，取截止时间
        endDate= dateTypes.get("endDate").val();
	}

    var receiveDate = dateTypes.get("receiveDate").val();
    if(receiveDate==""||receiveDate==null){
        return true;
    }
    var minday = getBetweenDay(startDate, receiveDate) - (-1);
    var maxday = getBetweenDay(receiveDate, endDate) - (-1);
    if(minday>0&&maxday>0){
        return true;
    }else{
        if(guaranteeAttr==0){//敞口保函
            layer.alert(bt.lang("table.guaGuarantee.checkReceiveDate2"));
        }else if(guaranteeAttr==1){//闭口保函
            layer.alert(bt.lang("table.guaGuarantee.checkReceiveDate"));
        }
        return false;
    }
}
	/**计算费率手续费保证金保证金比例*/
	function circular(){
		setHandleCharge();
        setBailMoney();
		setHandleRate();
        setBailMoneyRate();

        var isRat=$("#isRat").val();
        if(isRat=="1"){//挂接授信
            checkRatUMoney();
        }

	}

	/**计算手续费用*/
	function setHandleCharge(handleRate){
		var guaranteeMoney = moneyTypes.get("guaranteeMoney").val();
		if(bt.empty(guaranteeMoney)){
			return;
		}
		if(!(bt.empty(guaranteeMoney)) && !(bt.empty(handleRate))){
			moneyTypes.get("handleCharge").val(fun.format(fun.mul(guaranteeMoney,handleRate),{separator: "",decimal: 2}));
		}
	}
	/**计算费率*/
	function setHandleRate(){
		var guaranteeMoney = moneyTypes.get("guaranteeMoney").val();
		var handleCharge = moneyTypes.get("handleCharge").val();
		if(bt.empty(guaranteeMoney)){
			return;
		}
		if(!(bt.empty(guaranteeMoney)) && !(bt.empty(handleCharge))){
			rateTypes.get("handleRate").val(fun.div(handleCharge,guaranteeMoney,8));
		}
	}


	/**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
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

// 保证金比例触发事件
function setBailMoney(){
    var moneysum = moneyTypes.get("guaranteeMoney").val();//保函金额
    var bondPercent = rateTypes.get("bondPercent").val();//保证金比例
    if (bt.empty(moneysum) || (isHookBond == null)) {
        return;
    }

    if (!bt.empty(bondPercent) && bondPercent != 0) {
        var  bondMoney=fun.format(fun.mul(moneysum, bondPercent), {separator: "", decimal: 2})
        if(curId==bondCurId) {//保函币别与保证金币别一致时，保证金金额=保函金额*保证金比例
            bondMoney=bondMoney;
        }else if(curId!=bondCurId){//保函币别与保证金币别不一致时，保证金金额=保函金额*保证金比例*汇率
            bondMoney=fun.format(fun.mul(bondMoney, exchangeRate), {separator: "", decimal: 2});
        }
        moneyTypes.get("bondMoney").val(bondMoney);
    }
}
// 保证金金额触发事件
function setBailMoneyRate(){
    var moneysum = moneyTypes.get("guaranteeMoney").val();//保函金额
    var bailmoney = moneyTypes.get("bondMoney").val();//保证金金额


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
            var exchangeMoney=fun.format(fun.mul(moneysum, exchangeRate), {separator: "", decimal: 2});
            rateTypes.get("bondPercent").val(fun.div(bailmoney, exchangeMoney, 8));
        }
    }
}
//查看授信
function viewRatCode(){
    var params = beforeRation();
    if(bt.empty(params)){
        return;
    }
    viewRationDtl(params);
}

/**
 * 授信额度选择
 * @param params
 */
function beforeRation(){
    var corpId = $("#corpId").val();
    var curId = $("#ratCurId").val();
    var bankCode = $("#openCorp").val();//银行
    var startDate = dateTypes.get("startDate").val();//保函开始日期
    var ratItemCode = $("#ratItemCode").val();//授信品种
    var beginDate=bt.date.format(startDate, "yyyy-mm-dd")
    var params = {
        corpId : corpId,
        curCode : curId,
        bankCode : bankCode,
        ratItemCode : ratItemCode.replace(" ", ""),
        beginDate : beginDate
    }
    return params;
}


function checkRatUMoney(){
    var isOpenRat=$("#isOpenRat").val();
    var params = {
        curCode : $("#ratCurId").val(),//授信币种
        initCurCode :$("#curId").val(),//业务币种
        billMoney : moneyTypes.get("guaranteeMoney").val(),//业务金额
        money : moneyTypes.get("guaranteeMoney").val(),//业务金额
        bondMoney : moneyTypes.get("bondMoney").val(),//保证金金额
        bondCurId : $("#bondCurId").val(),//保证金币种
        isOpenRat:isOpenRat//是否启用授信敞口
    }

    var param = parseParam(params);
    var action = "/cms/rat/raturationapply/getRatUrationMoney";
    $.post(basePath + action,param,function(returnData){
        if(!bt.empty(returnData)){
            $('#ratApplyMoney').val(returnData);//设置正式占用金额
            var itemMoney = fun.add( $("#itemMoney").val(),$("#ratApplyMoneyPre").val());
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

//重新获取币别折算汇率
function setCurRat(){
    var startDate = bt.dateType.init().get("startDate").val();//保函开始时间
    if(bt.empty(startDate)){
        return ;
    }
    var curId=$("#curId").val();
    var corpCurId=$("#corpCurId").val();
    var bondCurId=$("#bondCurId").val();

    if (curId == bondCurId) {//保函币别和保证金币别一致时汇率为1
        moneyTypes.get("exchangeRate").val("1");
    } else {
        $.post(basePath + "/letter/guarantee/guaGuaranteeInfo/getNearCurRat", {
            curId: curId,//保函币别
            convertCurId: corpCurId,//单位本位币
            guaDate: bt.empty(startDate) ? startDate : startDate + " 23:59:59"
        }, function (retData) {
            if (retData.success) {
                $("#exchangeRate").val(retData.obj);
            }else{
                $("#exchangeRate").val("");
                parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
            }
        }, 'JSON');
    }
}

//领取失败
function failreason() {
    if($("#failReason").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
        layer.alert(bt.lang("table.guaGuarantee.failreason.not.null"),{icon: 0});
        $("#failReason")[0].focus();
        return false;
    }else {
        var failReason = $("#failReason").val();
        var id = $("#id").val();
        var action = "/letter/guarantee/guaGuaranteeRecive/fail";
        $.post(basePath + action,{failReason:failReason,id:id} , function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (!bt.navtab) {
                        location.reload();
                        bt.navtab.refreshParentTable();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        if (id == "") {
                            bt.navtab.refreshCurrent();
                        } else {
                            bt.navtab.closeCurrent();
                        }
                    }
                }
            })
        }, 'JSON');
        return ;
    }
};



