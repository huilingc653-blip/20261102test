/*开证公共方法写在此处，页面特有方法请放在页面内*/

/*开证人信息设置*/
function callback(selectedData,data,bv,wrapObj) {
    /*设置显示开证银行名称*/
    $("#issuingBankShow").val(selectedData.bankname);
    /*设置开证银行行号*/
    $("#issuingBank").val(selectedData.bankcode);
    /*设置开证人账号id*/
    $("#applicantAccId").val(selectedData.id);
    // 清空保证金信息，需重新选择
    changeFcBond();
}

/*受益人信息设置*/
function externalCorpCallback(data,dom) {
    /*设置受益人*/
    $("#beneficiary").val(data.name);
    /*设置收益人银行账号*/
    $("#beneficiaryAcc").val(data.externalAcc);
    /*设置受益人开户银行*/
    $("#beneficiaryBank").val(data.bank);
}

/*费用计算*/
function compCostCalculate(nextValue,elem,preValue) {
    /*开证手续费*/
    var chargeMoney = moneyType.get("chargeMoney").val();
    /*其他费用*/
    var otherFee = moneyType.get("otherFee").val();
    /*承兑费*/
    var acceptanceFee = moneyType.get("acceptanceFee").val();
    /*贴现息*/
    var discountInterest = moneyType.get("discountInterest").val();
    /*贸易亏损*/
    var tradeDeficit = moneyType.get("tradeDeficit").val();
    var compCost = Number(chargeMoney) + Number(otherFee) + Number(acceptanceFee) + Number(discountInterest) + Number(tradeDeficit);
    moneyType.get("compCost").val(compCost);
    var acceptanceMoney = moneyType.get("acceptanceMoney").val();
    moneyType.get("rebackMoney").val(Number(acceptanceMoney) - Number(compCost));
}

/**
 * 客商筛选
 * @returns {boolean}
 */
bt.externalCorpBankAccParams = function () {
    //供应商默认不查
    externalCorpBankAcc.set({
        "corpId": bt.corp.get("applicant").val() ? bt.corp.get("applicant").val() : "0"
    });
    return true;
}

/**
 * 选择单位时根据单位获取授权银行账号
 */
function setApplicantAcc(data) {
    $("#nativeCurId").val(data.curId);
    var corpId = bt.corp.get("applicant").val();
    $("#corpIds").val(corpId);
    $("#corpName").val(data.corpName);
    var isHangCredit = bt.dicts.init().get("isHangCredit").val();
    if(typeof(isHangCredit) == 'undefined'){
        isHangCredit = $("#isHangCredit").val();
    }
    if (isHangCredit == 0) {
        /*重新渲染币别下拉框*/
        var issuingCurId = selectType.get("issuingCurId");
        issuingCurId.attr({"data-value":"","data-displayData":""});
        issuingCurId.init();
    }

    /*用途根据单位重新渲染*/
    url = basePath + "/international/credit/createCredit/getPurpose?corpId=" + corpId;
    selectType.get("purpose").attr("data-url", url);
    selectType.get("purpose").init();

    /*清空账号下拉表格数据*/
    $("#issuingBankShow").val("");//清空银行
    $("#issuingBank").val("");

    selectType.get("issuingCurId").attr({"data-value":"","data-displayData":'all'});
    $("#applicantAcc").val("");//清空账号
    if (isHangCredit==0) {
        selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + bt.corp.get("applicant").val());
    } else {
        var issuingBank = $("#issuingBank").val();
        if (issuingBank != undefined) {
            selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + bt.corp.get("applicant").val() + "&bankCode=" + issuingBank);
        }
        url2 = basePath + "/cms/provide/loaninfo/getBankName?corpId=" + corpId;
        selectType.get("btBank").attr("data-url", url2);
    }

    //加载资金计划
    if($("#id")) {
        bt.renderBudget(true);
    } else {
        bt.renderBudget();
    }
}

/**
 * 关闭
 */
function closeNavtab() {
    if (bt.navtab != null) {
        bt.navtab.closeCurrent();
    }
}

// 时间校验
function expiryDateValidate(date, obj) {
    var issuingDate = dateObj.get("issuingDate").val();
    var expiryDate = dateObj.get("expiryDate").val();
    if (bt.date.compare(issuingDate,expiryDate) > 0) {
        parent.layer?parent.layer.alert(bt.lang("international.credit.alert.info1"), {icon: 2}):layer.alert(bt.lang("international.credit.alert.info1"), {icon: 2});
        obj.val("");
    }
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

/**
 * 授信额度选择
 * @param params
 */
function beforeRation(){
    var applicant = corp.get("applicant").val();
    if(bt.empty(applicant)){
        //选择开证人
        parent.layer.msg(bt.lang("international.credit.alert.info2"), {icon: 2});
        return false;
    }
    var bankId = $("#issuingBank").val();//银行
    if(bt.empty(bankId.trim())){
        //选择开证银行
        parent.layer.msg(bt.lang("international.credit.alert.info3"), {icon: 2});
        return false;
    }
    var issuingCurId = selectType.get("issuingCurId").val();//币别

    if(bt.empty(issuingCurId)){
        //选择币别
        parent.layer.msg(bt.lang("international.credit.alert.info4"), {icon: 2});
        return false;
    }
    var issuingDate = dateObj.get("issuingDate").val();//开证日期
    var expiryDate = dateObj.get("expiryDate").val()//有效期

    var itemCode = selectType.get("ratItemCode").val();//授信品种
    if(bt.empty(itemCode)){
        itemCode="";
    }
    var itemMoney = $("#itemMoney").val();//授信金额

    var isRmbRat = $("#isNativeCur").prop("checked");//是否折合本位币
    var params = {
        corpId : applicant,
        curCode : issuingCurId,
        bankCode : bankId,
        ratItemCode : itemCode,
        beginDate : issuingDate,
        endDate : expiryDate,
        itemMoney: itemMoney,
        isRmbRat : isRmbRat ? "1" : "0"
    }
    return params;
}


/**
 * 获取银行信息
 * @param bankCode
 */
function getBankByBankCode(bankCode) {
    var bankData = null;
    var url = basePath+"/international/credit/createCredit/getBankByBankCode"
    $.ajax({
        url: url,
        async: false,
        data: {
            bankCode: bankCode
        },
        type: "POST",
        dataType: "json",
        success: function (data) {
            bankData = data;
        }
    });
    return bankData;
}



/**设置保证金*/
function setBailMoney(){
    // 开证金额
    var creditMoney = moneyType.get("creditMoney").val();
    // 保证金比例
    var bailmoneyRate = rateType.get("bailmoneyRate").val();
    // 汇率
    var exchangeRate = moneyType.get("ratExchangeRate").val();
    if(bt.empty(creditMoney) && bt.empty(exchangeRate)){
        return ;
    }
    if(!bt.empty(bailmoneyRate) && bailmoneyRate !=0){
        // 保证金币别
        var bondCurId = selectType.get("bondCurId").val();
        // 开证币别
        var issuingCurId = selectType.get("issuingCurId").val();
        // 当开证币别与保证金币别不相等，则保证金金额按照汇率计算后显示
        if (bondCurId == issuingCurId) {
            moneyType.get("bailmoney").val(fun.format(fun.mul(creditMoney,bailmoneyRate),{separator: ",",decimal: 2}));
        } else {
            moneyType.get("bailmoney").val(fun.format(fun.mul(fun.mul(creditMoney,exchangeRate),bailmoneyRate),{separator: ",",decimal: 2}));
        }
    }
}
/**设置保证金比例*/
function setBailMoneyRate(){
    // 保证金币别
    var bondCurId = selectType.get("bondCurId").val();
    // 开证币别
    var issuingCurId = selectType.get("issuingCurId").val();
    // 开证金额
    var creditMoney = moneyType.get("creditMoney").val();
    // 保证金金额
    var bailmoney = moneyType.get("bailmoney").val();
    // 汇率
    var exchangeRate = moneyType.get("ratExchangeRate").val();
    if(bt.empty(creditMoney)){
        return ;
    }
    if (bondCurId == issuingCurId) {
        if (bt.money.sub(bailmoney, creditMoney) > 0) {//判断保证金金额是否大于开证金额
            moneyType.get("bailmoney").val(creditMoney);
            rateType.get("bailmoneyRate").val(1);
        } else {
            rateType.get("bailmoneyRate").val(fun.div(bailmoney, creditMoney, 4));
        }
    }else {
        var convertMoney = fun.div(bailmoney, exchangeRate, 4)//币别不相同，则根据汇率转换金额
        if (bt.money.sub(convertMoney, creditMoney) > 0) {//转换后金额大于开证金额
            moneyType.get("bailmoney").val(moneyFun.mul(moneyFun.toNumber(creditMoney), moneyFun.toNumber(exchangeRate)));
            rateType.get("bailmoneyRate").val(1);
        } else {
            rateType.get("bailmoneyRate").val(fun.div(convertMoney,creditMoney,4));//设置保证金比例
        }
    }
}
// 校验及参数设置
function checkCorp(){
    var corpIds = bt.corp.init().get("applicant").val();
    var bondCurId = selectType.get("bondCurId").val();
    var issuingBank = $("#issuingBank").val();
    if(bt.empty(corpIds)){
        parent.layer.msg(bt.lang("international.credit.alert.info2"), {icon: 2});
        return false;
    }
    if (bt.empty(issuingBank)) {
        parent.layer.msg(bt.lang("international.issuingBank.alert.info6"), {icon: 2});
        return false
    }
    if (bt.empty(bondCurId)) {
        parent.layer.msg(bt.lang("common.bondCurId.alert.info1"), {icon: 2});
        return false
    }
    parameters.corpId = corpIds;
    parameters.bankCode = issuingBank;
    parameters.curId= bondCurId
    parameters.startDate = dateObj.get("issuingDate").val();
    parameters.endDate = dateObj.get("expiryDate").val();
    bt.bondAccInfo.set(parameters);
    return true
}


//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data,dom){
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondAccMoney").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bondBankAcc").val(data.bankAcc);//保证金账户
    if (data.bondId != $("#bondIdPre").val()) {
        $("#bailMoneyPre").val("");
    }
    rateType.get("bondRate").val(data.bondRate);
}

function bankinfoCallBackTwo(data,elem,beforevalue,wrapObj){
    $("#issuingBank").val(data.bankcode);//联行号
    var corpId = bt.corp.get("applicant").val()
    $("#applicantAcc").val("");
    selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + corpId + "&bankCode=" + data.bankcode);

}

/**
 * 是否挂授信切换
 */
var setRatModel = function(){
    var isHangCredit = bt.dicts.init().get("isHangCredit").val();
    if(typeof(isHangCredit) == 'undefined'){
        isHangCredit = "0";
    }
    $("#applicantAcc").val("");
    var corpId = bt.corp.get("applicant").val();
    if("0" == isHangCredit){
        $(".isShow").hide();
        $("#xz").show();
        $("#xl").hide();
        /*设置授信品种为非必填，并清除授信品种值*/
        $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
        selectType.get("ratItemCode").attr({"data-required": false,"data-defaultvalue":""});
        selectType.get("ratItemCode").val("");
        selectType.get("btBank").attr("data-required", false);

        /*重新渲染币别下拉框*/
        selectType.get("issuingCurId").attr({"data-value":"","data-displayData":""});
        selectType.get("issuingCurId").init();

        $("#bankAcc").removeAttr('lay-verify');
        $("#issuingBankShow").val("");
        selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + corpId);
        /*不挂授信时，清掉授信模式及其他授信信息*/
        // 设置授信敞口为否
        dicts.get("isOpenRat").val("0");
        setRationBtn();
        clearItemCode();
        $(".isNativeCur").hide();//隐藏授信相关信息
        moneyType.get("nativeCreditMoney").val("");
    } else if("1" == isHangCredit) {
        $(".isShow").show();
        if ("1" == $("#ratBankConfig").val()) {//银行类别相同
            $("#xz").show();
            $("#xl").hide();
            selectType.get("btBank").attr("data-required", false);
        } else {//行号相同
            $("#xz").hide();
            $("#xl").show();
        }
        selectType.get("ratItemCode").attr("data-required", true);
        selectType.get("ratItemCode").render();
        // 清空开证行值
        selectType.get("btBank").attr({"data-required":true,"data-defaultvalue":""});
        selectType.get("btBank").render();
        /*重新币别下拉框所有数据*/
        selectType.get("issuingCurId").attr("data-displayData",'all');
        selectType.get("issuingCurId").render();
        var issuingBankShow = $("#issuingBankShow").val();
        $("#applicantAcc").val("");
        if (issuingBankShow != undefined) {
            selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + corpId + "&bankCode=" + $("#issuingBankShow").val());
        } else {
            selectTable.get("applicantAcc").setUrl(basePath + "/international/credit/createCredit/getBankAccsByCorpId?corpId=" + corpId );
        }
    }
    // 清空业务品种数据
    selectType.get("billName").val("");
    // 设置授信敞口为是
    dicts.get("isOpenRat").val("1");
    // 清除保证金信息
    clearFcBondMsg();
    // 担保设置，每次切换授信则需重新设置担保
    dicts.get("isHangGuarantee").attr({"data-value":"0","data-disabled": false});
    var data = {"value": "0"};
    // 根据是否启用敞口判断保证金币种是否必填
    isOpenRatCallback();
    bt.setGuaInfo(data);
    form.renderIcon();
}

//清除授信信息
function setRationBtn(){
    $("#ratmodel").val("");
    $("#rationCode").val("");
    $("#rationBtn").hide();
    $("#itemCodeTd").html("");
    setBusinessRac();
}


/**
 * 清除授信品种西信息
 */
function clearItemCode(){
    selectType.get("ratItemCode").val("");
}

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
    var param = ceateParamsRationDtl(params);
    layer.open({
        type: 2,
        area: ['80%', '70%'],
        maxmin: true,
        content: basePath + "/cms/rat/raturation/toRationDtl?" + param,
        end: function () {

        }
    });
}


/**
 * 查看授信品种和额度
 */
function viewRationDtl(params) {
    var param = viewParamsRationDtl(params);
    layer.open({
        type: 2,
        area: ['80%', '70%'],
        maxmin: true,
        content: basePath + "/cms/rat/raturation/viewRationDtl?" + param,
        end: function () {

        }
    });
}


//json转url参数
function parseParam(param, key) {
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


/**
 * 授信额度回写调用
 * @param params
 */
function setRation(params) {
    var html = "";
    var rationCode = params.ratCode;//授信单号
    var itemFlag = params.itemFlag;//授信品种标识
    var itemMoney = params.itemMoney;//授信金额
    html += '<input type="hidden" name="rationCode" id="rationCode" value="' + rationCode + '">';
    html += '<input type="hidden" name="ratmodel" id="ratmodel" value="' + itemFlag + '">';
    html += '<input type="hidden" name="itemMoney" class="itemMoney" value="' + itemMoney + '">';

    var itemCode = params.itemCode;
    if (itemFlag == "0") {
        //html += '<input type="hidden" name="ratItemCode" id="ratItemCode" value="' + itemCode + '">';
    } else {
        //循环设置综合品种
        var rationDlts = params.rationDlts;
        if (rationDlts && rationDlts.length > 0) {
            var length = rationDlts.length;
            html += '<input type="hidden" name="applyDtlsLength" id="applyDtlsLength" value="' + length + '">';
            for (var i = 0; i < length; i++) {
                var rationDlt = rationDlts[i];
                html += '<input type="hidden" name="applyDtls[' + i + '].itemCode" id="itemCode' + i + '" value="' + rationDlt.itemCode + '">';
                html += '<input type="hidden" name="applyDtls[' + i + '].itemMoney" itemCode="' + rationDlt.itemCode + '" id="itemMoney' + i + '" value="' + rationDlt.itemMoney + '">';
            }
        }
    }
    //设置itemCode选中
    selectType.get("ratItemCode").val(itemCode);//授信品种
    $("#bankCode").val(params.bankCode);
    $("#itemCodeTd").html(html);
    $("#rationBtn").show();
    // 业务品种设置
    setBusinessRac();
    try {
        // 扩展函数，如调用页面需要进行后续处理，各调用页面需自己实现ratInfoExtSet函数的处理逻辑
        if(typeof(eval("ratInfoExtSet")) == "function") {
            ratInfoExtSet(params);
        }
    }catch (e) {

    }
/*
    //判断授信是否挂接了保证金
    if (bt.empty(rationCode)) {
        return;
    }
    $.post(basePath + "/cms/provide/loaninfo/checkRateCode", {
        rateCode: rationCode
    }, function (data) {
        /!*判断是否挂保证金*!/
        if (data && "1" == data.isHook) {
            // 当授信已经挂接保证金时则，隐藏保证金内容
            clearBondInfo();
            $("#isHangBond").val(0);
            $(".bondInfo").hide();
            // 授信已经挂接保证金，则不能重复填写保证金数据，则将保证金数据置0为无效（保证金数据只显示，无挂接关系）
            /!*            rateType.get("bailmoneyRate").val(data.data.bondPercent);
                        /!*开证币别*!/
                        var issuingCurId = selectType.get("issuingCurId").val();
                        var bondCurId = selectType.get("bondCurId").val();
                        /!*保证金币别和开证币别相同时则保证金金额为授信挂保证金金额*!/
                        if (issuingCurId == bondCurId) {
                            moneyType.get("bailmoney").val(data.data.applyMoney);
                        } else {
                            /!*汇率*!/
                            var exchangeRate = moneyType.get("exchangeRate").val();
                            var creditMoney = moneyType.get("creditMoney").val();
                            moneyType.get("bailmoney").val(creditMoney*Number(data.data.bondPercent)*exchangeRate);
                        }*!/
        } else {
            $(".bondInfo").show();
            // 授信未挂接保证金，则将保证金数据置1为有效（保证金数据只显示，无挂接关系）
            $("#isHangBond").val(1);
        }
    }, 'JSON');*/
}

/**
 * 清除保证金信息
 */
function clearBondInfo() {
    selectType.get("bondCurId").val("");
    rateType.get("bailmoneyRate").val("");
    moneyType.get("bailmoney").val("");
}

/**
 * 选择授信
 * @param params
 * @returns {string}
 */
function ceateParamsRationDtl(params) {
    var param = $.extend({}, params);
    var rationCode = $("#rationCode").val();//授信信息
    var itemFlag = $("#ratmodel").val();//品种类型
    var itemMoney = $("#itemMoney").val();//授信额度
    param["ratCode"] = bt.empty(rationCode) ? "" : rationCode;
    param["itemFlag"] = bt.empty(itemFlag) ? "" : itemFlag;
    param["itemMoney"] = bt.empty(itemMoney) ? "" : itemMoney;
    if(itemFlag == "1") {
        var doms = $('input[id^="itemMoney"]');
        var applyDtls = [];
        if (doms.length >= 1) {
            $.each(doms, function (i, item) {
                var itemCode = $(item).attr("itemCode");//授信品种
                var itemMoney = $(item).val();//授信品种
                var applyDtl = {};
                if (!bt.empty(itemCode)) {
                    applyDtl["itemCode"] = itemCode;
                    applyDtl["useMaxMoney"] = itemMoney;
                    applyDtls.push(applyDtl);
                }
            });
        } else {
            var applyDtl = {};
            applyDtl["itemCode"] = params.ratItemCode;
            applyDtl["useMaxMoney"] = $(".itemMoney").val();
            applyDtls.push(applyDtl);
        }
        param["applyDtls"] = applyDtls;
    } else {
        var itemCode = params.ratItemCode;
        if(bt.empty(itemCode)){
            itemCode = selectType.get("ratItemCode").val();//授信品种
        }
        param["ratItemCode"] = bt.empty(itemCode) ? "" : itemCode;
    }
    return parseParam(param);
}

/**
 * 查看授信
 * @param params
 * @returns {string}
 */
function viewParamsRationDtl(params) {
    var param = $.extend({}, params);
    var rationCode = $("#rationCode").val();//授信信息
    var itemFlag = $("#ratmodel").val();//品种类型
    var itemMoney = $("#itemMoney").val();//授信额度
    param["ratCode"] = bt.empty(rationCode) ? "" : rationCode;
    param["itemFlag"] = bt.empty(itemFlag) ? "" : itemFlag;
    param["itemMoney"] = bt.empty(itemMoney) ? "" : itemMoney;
    if(itemFlag == "1") {
        param["itemCode"] = params.ratItemCode;
        param["useMaxMoney"] = bt.empty(itemMoney) ? "" : itemMoney;
    } else {
        var itemCode = params.ratItemCode;
        if(bt.empty(itemCode)){
            itemCode = selectType.get("ratItemCode").val();//授信品种
        }
        param["ratItemCode"] = bt.empty(itemCode) ? "" : itemCode;
    }
    if ("1" == $("#ratBankConfig").val()) {
        param["bankCode"] = $("#bankCode").val();
    }
    return parseParam(param);
}

/**
 * 提交前授信数据校验
 */
function submitValidate() {
    var subtrue = true;
    var isHangBond = dicts.get("isHangBond").val();
    var rationCode = $("#rationCode").val();
    var isHangCredit = bt.dicts.init().get("isHangCredit").val();
    var creditMoney = moneyType.get("creditMoney").val();//开证金额
    var itemMoney = $("itemMoney").val();
    if (isHangCredit == undefined) {
        isHangCredit = "0"  //未挂授信
    }

    if(typeof(isHangCredit) == 'undefined'){
        isHangCredit = "0";
    }
    if (isHangCredit == "1" && (rationCode == null || rationCode == "" || rationCode == undefined)) {
        subtrue = false;
        parent.layer?parent.layer.alert(bt.lang("international.credit.alert.info5"), {icon: 2}):layer.alert(bt.lang("international.credit.alert.info5"), {icon: 2});
    }

    if (isHangCredit == "1" && creditMoney > itemMoney) {
        subtrue = false;
        parent.layer?parent.layer.alert(bt.lang("international.credit.alert.info6"), {icon: 2}):layer.alert(bt.lang("international.credit.alert.info6"), {icon: 2});
    }

    //判断是否挂接资金计划
    var creditMoney = moneyFun.toNumber(moneyType.get("creditMoney").val());
    if ($("input[name='sumMoney']").length) {
        var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
        // 校验金额与资金计划总金额一致
        if (sumMoney != creditMoney) {
            layer.alert(bt.lang("international.message.budgetMoney"), {icon: 2});
            subtrue = false;
        }
    }
// 保证金校验
    if ($("#creditIsBond").val() == "1") {
        var isHangBond = bt.dicts.init().get("isHangBond").val();
        var bondMoney = moneyType.get("bailmoney").val();//保证金金额
        var accAvailMoney = $("#accAvailMoney").val();//可用保证金金额
        if (isHangBond == undefined) {
            isHangBond = "0"; //未挂保证金
        }
        if(accAvailMoney == null || accAvailMoney == "" || accAvailMoney == undefined){
            accAvailMoney == 0;
        }
        if (isHangBond=="1") {
            if(Number(bondMoney)==Number(0)){//选择挂接保证金时保证金金额不能等于0
                layer.alert(bt.lang("fcbond.check.bailmoneyIsNull"),{icon:2});
                return false;
            }
            var bondId = $("#bondId").val();
            var bondIdPre = $("#bondIdPre").val();
            var bailMoneyPre = $("#bailMoneyPre").val();
            if (bondId == bondIdPre) {
                // 如果挂机的保证金合同id相同，则需要加上之前挂接金额在计算
                if (Number(bondMoney) > (Number(accAvailMoney) + Number(bailMoneyPre))) {
                    parent.layer ? parent.layer.alert(bt.lang("fcbond.applymoney.check"), {icon: 2}) : layer.alert(bt.lang("fcbond.applymoney.check"), {icon: 2});
                    return false;
                }
            } else {
                if (Number(bondMoney) > Number(accAvailMoney)) {
                    parent.layer ? parent.layer.alert(bt.lang("fcbond.applymoney.check"), {icon: 2}) : layer.alert(bt.lang("fcbond.applymoney.check"), {icon: 2});
                    return false;
                }
            }
        }
    }
    return subtrue;
}

/**
 * 黑白名单校验
 * @param status
 * @returns {boolean}
 */
function checkBlackWhite(status) {
    var beneficiary = $("#beneficiary").val();
    var corpId = bt.corp.get("applicant").val();
    var reData=externalCorpCheck({"name":beneficiary, "corpId": corpId});
    if(reData.reCode=='3'){
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
        return false;
    }else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            doSubmit(status);
        });
    } else {
        doSubmit(status);
    }
}


function CreateSelectTableType(data, tr, elem, full) {
    if (bt.empty($("#nativeCurId").val())) {
        layer.alert(bt.lang("international.credit.alert.info2"),{icon:2});
        return;
    }

    var isHangCredit = bt.dicts.init().get("isHangCredit").val();
    /*当不存在时默认不挂授信*/
    if (typeof (isHangCredit) == 'undefined' || isHangCredit == null || isHangCredit == "") {
        isHangCredit = 0;
    }
    if (isHangCredit == 0) {//不挂接授信
        /*设置显示开证银行名称*/
        $("#issuingBankShow").val(data.bankName);
    } else {//挂接授信
        if ("1" == $("#ratBankConfig").val()) {//授信控制参数为同类型银行时
            $("#issuingBankShow").val(data.bankName);
        }
        setRationBtn();
        clearItemCode();
    }
    /*设置开证银行行号*/
    $("#issuingBank").val(data.bankCode);
    $("#bankName").val(data.bankName);
    /*设置开证人账号id*/
    $("#applicantAccId").val(data.id);
    /*设置账号*/
    elem.val(data.bankAcc);
    var bankDate = getBankByBankCode(data.bankCode);
    /*设置银行id*/
    if (bankDate != null) {
        $("#bankId").val(bankDate.id);
    }
    selectType.get("issuingCurId").attr({"data-value": data.curId, "data-displayData": data.curId});
    if (bt.empty($("#issuingBankShow").val())) {
        selectType.get("btBank").attr({"data-value": bankDate.bankCode, "data-displayData": data.bankCode});
    }
}

// 是否代开信用证设置
function setIsReCredit() {
    var isReCredit = dicts.get("isReCredit").val();
    var reCorpCode = corp.get("reCorpCode");
    if (isReCredit == 1) {
        $(".isReCredit").show();
        $("#reCorpCode").find(".select-tree-input").attr("lay-verify","required");
        form.renderIcon();
    } else {
        $(".isReCredit").hide();
        $("#reCorpCode").find(".select-tree-input").removeAttr("lay-verify");
        form.renderIcon();
    }
    // 清空选中值
    reCorpCode.val("");
    $("#reBank").val("");
    $("#reCreditNo").val("");
}

/**
 * 根据币别获取最新汇率
 * @param curId
 */
function setCurExcRate(curId) {
    if (curId != null && curId != undefined && curId != "") {
        var nativeCurId = $("#nativeCurId").val();
        var isHangBond = dicts.get("isHangBond").val();
        if (curId == nativeCurId){//开证币种为本位币
            if ($("#ratExchangeRate").length > 0) {
                moneyType.get("ratExchangeRate").val(1.00);
            }
            if(isHangBond=="1"){//参数设置挂接保证金
                selectType.get("bondCurId").attr({"data-value": nativeCurId, "data-displayData": nativeCurId});
            }
            /*当为本币位时，则隐藏是否转换为本币位*/
            if (dicts.get("isHangCredit").val() == 1) {
                $(".isNativeCur").hide();
                $("#isNativeCur").removeAttr("checked");
            }
        }else {//开证币种为外币
            if (isHangBond == "1") {//参数设置挂接保证金
                selectType.get("bondCurId").attr({
                    "data-value": nativeCurId,
                    "data-displayData": nativeCurId + "," + curId
                });
            }
            if ($("#ratExchangeRate").length > 0) {
                var exchangeRate = getExchangeRate(nativeCurId, curId);
                if (exchangeRate == 0) {
                    // 清除保证金信息
                    clearFcBondMsg();
                    return;
                } else {
                    moneyType.get("ratExchangeRate").val(exchangeRate);
                }
            }
            if (dicts.get("isHangCredit").val() == 1) {
                $(".isNativeCur").show();
                $("#isNativeCur").attr("checked", "checked");
            }
            form.render("checkbox");
            // 由于本位币折算影响到授信，则这里做清除授信操作，需重新选择授信
            if (dicts.get("isHangCredit").val() == 1) {
                setNativeCreditMoney();
            }
        }

    }
}

function issuingDateCallback() {
    var nativeCurId = $("#nativeCurId").val();
    var curId = selectType.get("issuingCurId").val();
    if (!bt.empty(curId) && !bt.empty(nativeCurId)) {
        var exchangeRate = getExchangeRate(nativeCurId, curId);
        if (exchangeRate != 0) {
            moneyType.get("ratExchangeRate").val(exchangeRate);
        }
    }
    //加载资金计划
    if($("#id")) {
        bt.renderBudget(true);
    } else {
        bt.renderBudget();
    }
}


function getExchangeRate(origCurId,convertCurId) {
    var exchangeRate = 0;
    var issuingDate = dateObj.get("issuingDate").val();
    if (bt.empty(origCurId, convertCurId)) {
        return 0;
    }
    $.ajax({
        url: basePath + "/international/credit/createCredit/getExchangeRate",
        type: "post",
        data: {
            origCurId: convertCurId,
            convertCurId: origCurId,
            preDate: bt.empty(issuingDate) ? issuingDate : issuingDate + " 23:59:59"
        },
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.success) {
                exchangeRate = data.obj;
            } else {
                moneyType.get("ratExchangeRate").val("");
                parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            }
        }
    });
    return exchangeRate;
}

/**
 * 是否溢短装选择是时，溢短装比例则必填
 */
function spillOverSet() {
    var isSpillOver = dicts.get("isSpillOver").val();
    if ("1" == isSpillOver) {
        rateType.get("spillOverRate").attr("lay-verify", "required");
    } else {
        rateType.get("spillOverRate").attr("lay-verify", "");
        rateType.get("spillOverRate").val("")
    }

    form.renderIcon();
}


/**
 * 是否远期选择是时，远期天数必填
 */
function timeLimitSet() {
    var timeLimit = dicts.get("timeLimit").val();
    if ("1" == timeLimit) {
        moneyType.get("delayDates").removeAttr("lay-verify")
        moneyType.get("delayDates").val("");
        $(".delayDates").hide();
    } else {
        moneyType.get("delayDates").attr("lay-verify", "required");
        $(".delayDates").show();
    }
    form.renderIcon();
}

/**
 * 是否折合本位币时选择是，则计算折合后本位币金额
 */
function setNativeCreditMoney() {
    // 计算折算后金额
    calculateNativeCreditMoney();
    //清除授信信息
    setRationBtn();
    clearItemCode();
}


/**
 * 计算折算后金额
 */
function calculateNativeCreditMoney() {
    // 开证金额
    var creditMoney = moneyType.get("creditMoney").val();
    // 是否折算本位币
    var isNativeCur = $("#isNativeCur").prop("checked");

    // 汇率
    var exchangeRate = moneyType.get("ratExchangeRate").val();
    // 折算本位币
    if (isNativeCur) {
        moneyType.get("nativeCreditMoney").val(moneyType.getFun().mul(creditMoney, exchangeRate, 2));
        $(".nativeCreditMoney").show();
        /*        var params = {};
                params.curId = curId;
                var returnDate = commonAjax("/international/credit/createCredit/isNativeCur","psot",params)
                // 判断当前币别是否为外币
                if ((!returnDate.isNativeCur) && creditMoney!= 0) {
                    // 为外币时，进行金额转化为本位币对应金额
                    moneyType.get("nativeCreditMoney").val(moneyType.getFun().mul(creditMoney,exchangeRate,exchangeRate))
                    /!*设置是否外币隐藏框为1*!/
                    $("#isNativeCur").val(1);
                }*/
    } else {
		if ($("#isHangCredit").length == 0) {//授信参数否
            $(".nativeCreditMoney").hide();
            moneyType.get("nativeCreditMoney").val("");
        }
    }
}


/**
 * 1.根据是否折算本位币重新计算折算后金额
 * 2.重新计算保证金金额
 */
function setNativeCreditMoneyAndBondMoney() {
    calculateNativeCreditMoney();
    // 保证金数据计算
    if ($("#creditIsBond").length > 0) {//是否启用保证金
        var bailmoneyRate = rateType.get("bailmoneyRate").val();
        if (!bt.empty(bailmoneyRate)) {
            setBailMoney();
        }
    }
}

/**
 * 公用ajax调用函数，返回json对象
 * @param URL 会自动将该URL和basePath拼接，请勿携带basePath
 * @param type get/post
 * @param params 参数
 * @returns {*}
 */
function commonAjax(URL,type,params) {
    var returnData;
    $.ajax({
        url: basePath + URL,
        type: type,
        data: params,
        dataType: "json",
        async: false,
        success: function (data) {
            returnData = data;
        }
    });
    return returnData;
}

//如果选择了否,清空填写的保证金占用信息
function changeFcBond(){
    var isHookBond = dicts.get("isHangBond").val();
    if(isHookBond=="0"){
        rateType.get("bailmoneyRate").attr('lay-verify',false);
        moneyType.get("bailmoney").removeAttr("lay-verify");
        if ("0" == $("#isHangCredit").val()) {//授信参数否
            selectType.get("bondCurId").attr('data-required', false);
        } else {//挂接授信参数为是
            var isOpenRat = dicts.get("isOpenRat").val();
            var isHangCredit = dicts.get("isHangCredit").val();
            if ("0" == isOpenRat || "0" == isHangCredit) {
                selectType.get("bondCurId").attr('data-required', false);
            }
        }
        $("#bondBankAcc").removeAttr('lay-verify');
        $(".bondInfo").hide();
        clearFcBondMsg();
    }else{
        $(".bondInfo").show();
        rateType.get("bailmoneyRate").attr('lay-verify','required');
        $("#bondBankAcc").attr('lay-verify','required');
        selectType.get("bondCurId").attr('data-required',true);
        moneyType.get("bailmoney").attr('lay-verify','required');
    }
    form.renderIcon();
}
//清空保证金信息
function clearFcBondMsg() {
    if ("1" == $("#creditIsBond").val()) {
        $("#accAvailMoney").val("");
        $("#bondBankAcc").val("");
    // selectType.get("bondCurId").val("");
        moneyType.get("bailmoney").val("");
        rateType.get("bailmoneyRate").val("");
    }
}

// 授信回调扩展
function ratInfoExtSet(ratInfoData) {
    if(ratInfoData.assurepercent != null && ratInfoData.assurepercent != "" && ratInfoData.assurepercent != undefined && ratInfoData.assurepercent != "0"){
        rateType.get("bailmoneyRate").val(ratInfoData.assurepercent);
        setBailMoney();
    }
    /**
     * 判断授信是否挂担保
     */
    var data = {"value": "0"};
    dicts.get("isHangGuarantee").attr({"data-value":"0"});
    if (ratInfoData.isHangGuarantee == "1") {
        form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
    }else{
        form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
    }
    bt.setGuaInfo(data);
}
// 币别发生变化时,判断是否可以转换汇率,则根据比例设置金额
function changeBondCur(){
    var bondCurId = selectType.get("bondCurId").val();
    var nativeCurId = $("#nativeCurId").val();
    if (bondCurId != bondCurId) {
        if (getExchangeRate(nativeCurId, bondCurId) == 0) {
            selectType.get("bondCurId").val("");
            return;
        }
    }
    $("#accAvailMoney").val("");
    $("#bondBankAcc").val("");
    moneyType.get("bailmoney").val(0);
    rateType.get("bailmoneyRate").val(0);
    // 设置保证金
    setBailMoney();
}

/**
 * 设置业务品种
 * @param data
 * @param elem
 * @param beforevalue
 * @param wrapObj
 */
function setBusinessRac() {
    var id = selectType.get("ratItemCode").val();
    // 清空数据
    if ($("#billName") != undefined) {
        selectType.get("billName").val();
        if(id != null && id != "" && id != undefined) {
            var url = basePath + "/cms/rat/ratBusinessRace/getBillByCustomId?customId="+id;
            selectType.get("billName").attr({"data-url": url,"data-value":""});
            selectType.get("billName").init();
        }
    }
}

/**
 * 业务品种初始化回调
 * @param text
 * @param data
 */
function checkData(data) {
    var isHangCredit = dicts.get("isHangCredit").val()

    if (isHangCredit == "1") {
        if (data.length > 0) {
            $('[data-biz-name="billName"]').find('select').attr('lay-verify', 'required');
        } else {
            $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
        }
        form.renderIcon();
    }

}

/**
 *
 * @param param
 */
function setOtherParam(param){
    var viewFlag = $("#viewFlag").val();
    var corpId = corp.get("applicant").val();;//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    if(viewFlag=="1"){
        startDate = $("#startDate").val();//开始日期
        endDate = $("#enddate").val() ;//结束日期
    }else{
        startDate = $.trim(dateObj.get("issuingDate").val());//开始日期
        endDate = $.trim(dateObj.get("expiryDate").val());//结束日期
    }
    var curId = selectType.get("issuingCurId").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

/**
 *
 * @returns {boolean}
 */
function selectBefore(){
    var corpId = corp.get("applicant").val();
    if (bt.empty(corpId)) {
        parent.layer.msg(bt.lang("international.credit.alert.info2"), {icon: 2});
        return false;
    }
    var curId = selectType.get("issuingCurId").val();
    if (bt.empty(curId)) {
        parent.layer.msg(bt.lang("international.credit.alert.info7"), {icon: 2});
        return false;
    }
    return true;
}

/**
 * 只读设置
 */
function setIsGuaranteeEnabled() {
    var isGuarantee = $("#isGuarantee").val();
    if(isGuarantee == "1"){
        var ratIsHangGua = $("#ratIsHangGua").val();
        if ("1" == ratIsHangGua) {
            form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
        } else {
            form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        }
    }
}

/**
 * 补录打回操作
 * @returns {boolean}
 */
function refuse() {
    var refuseVerson = $("#refuseReason").val();
    if (refuseVerson == null || refuseVerson == "" || refuseVerson == undefined) {
        parent.layer ? parent.layer.alert(bt.lang("common.message.repulseRequired"), {icon: 2}) : layer.alert(bt.lang("common.message.repulseRequired"), {icon: 2});
        return false;
    }
    bt.loading.show();
    var url = basePath + "/international/credit/supplementCredit/refuse";
    $.ajax({
        url: url,
        data: $("#myform").serialize(),
        dataType: "json",
        type: "POST",
        success: function (data) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshParentTable();
                } else {
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }
    })
}


/**
 * 授信敞口选择是回调
 */
function isOpenRatCallback() {
    var creditIsBond = $("#creditIsBond").val();
    if ("1" == creditIsBond) {
        var isOpenRat = dicts.get("isOpenRat").val();
        var isHangCredit = dicts.get("isHangCredit").val();
        if ("1" == isOpenRat && "1" == isHangCredit) {
            selectType.get("bondCurId").attr('data-required', true);
        } else {
            var isHangBond = dicts.get("isHangBond").val();
            if ("0" == isHangBond) {
                selectType.get("bondCurId").attr('data-required', false);
            }
        }
    }
    form.renderIcon();
}
