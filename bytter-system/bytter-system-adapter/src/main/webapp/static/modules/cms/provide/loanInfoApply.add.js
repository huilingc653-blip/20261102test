var $, form, fun, dateTypes, moneyTypes, corp, selectTypes, dicts, rateTypes, parameters, ratioType, natualCurId, relateRat,
    selectTable,
    bondCurCorp = "";
bt.use(['layedit', 'btdate', 'moneynum','form', 'jquery', 'corp', 'dateType','conSelect', 'moneyType', 'selectType','selectTableType',  'dicts', 'rateType', 'bankInfo', 'btUpload', 'bondAccInfo', 'ratioType', 'btImport'], function () {
    $ = bt.jquery;
    form = bt.form();
    fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    bt.bankInfo.init();//联行号
    moneyFun = bt.moneyType.getFun();
    bondAccInit = bt.bondAccInfo.init();//保证金账号初始化
    ratioType = bt.ratioType.init();
    parameters = {};
    bt.conSelect.init();
    var conSelect = bt.conSelect;
     //合同选择参数
    bt.conSelectParams=function(){
        var opResource="con:conchange:resource:op";
        conSelect.set({"opResource":opResource});
        return true;
    }

    //合同选择回调函数
    bt.conSelectCallback = function(datas,dom){
        if(datas){
            $("#mid").val(datas.id);//合同ID
            $("#conName").html(datas.conName);//合同名称
            $("#conNo").html(datas.conNo);//合同代码
            if(datas.corpCode){
                $("#corpName").html('('+datas.corpCode+')'+datas.corpName);//合同发起人
            }
            $("#signType").html(datas.signTypeName);//签署方式
            $("#typeName").html(datas.typeName);//合同类型
            $("#beginDate").html(datas.beginDate);//合同生效日期
            $("#endDate").html(datas.endDate);//合同失效日期
            //附件初始化
            initAttachmenId(datas.attachmentId);
            //业务信息初始化
            initShowConBusinessInfo(datas.id);
        }
    }

    relateRat = $("#ratmodelValue").val();
    changeOverRepair();
    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    renderBudget();//计划加载
    initBankAccTable();
    //编辑页面显示或隐藏保证金额度单信息
    var editFlag = $('#editFlag').val();
    if (!bt.empty(editFlag) && editFlag == "1") {
        if ($("#configIsHookBond").val() == "1") {
            var isHookBond = $('#hasBond').val();
            if (!bt.empty(isHookBond)) {
                if (isHookBond == "1") {//挂接保证金
                    $(".bondInfo").show();
                    $("#bondBankAcc").attr('lay-verify', 'required')
                } else if (isHookBond == "0") {//不挂保证金
                    /*$("#bondBankAcc").removeAttr('lay-verify');
                    moneyTypes.get("bailmoney").removeAttr("lay-verify");
                    rateTypes.get("bailmoneyRate").attr('lay-verify', false);
                    //selectTypes.get("bondCurId").attr('lay-verify',false);
                    $('[data-name="bondCurId"]').attr('lay-verify', false);
                    $(".bondInfo").hide();*/
                    changeFcBond();
                }
            } else {//录入未开启参数，编辑时开启参数，默认挂接保证金
                $(".bondInfo").show();
                $("#bondBankAcc").attr('lay-verify', 'required')
            }

            // setBondCur();
        }
    }

    // 银团信息
    if (plClass == "1000") {
        $("#syndicatedShow").show();
        initSyndicated()
    }

    form.verify({
        verifyLength: function (value, item) {
            if (value.length >= 200) {
                return bt.lang("sys.form.verify.mustBeLessThanLength") + "200";
            }
        }
    })

    //监听提交
    form.on('submit', function (data) {
        // 如果担保是禁用则打开担保提交数据
        // dicts.get("isHangGuarantee").attr({"data-disabled": false});
        $("[data-biz-name='isHangGuarantee']").attr({"data-disabled": false});

        var rateData = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
        if (bt.empty(rateData.rate)) {
            parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
            return false;
        }
        var ratModel = bt.dicts.init().get("ratmodel").val();
        if ("1" == ratModel) {
            var ratCode = $("#ratCode");
            if (!ratCode) {
                parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
                return false;
            }
            if (bt.empty(ratCode.val())) {
                parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
                return false;
            }
            var plMoney = moneyTypes.get("plMoney").val();//借款金额
            /*
            var itemMoney = $("#itemMoney").val();
            var exchangerate = moneyTypes.get("exchangerate").val();//利率
            if(bt.empty(exchangerate)){
                exchangerate = 1.0;
            }
            plMoney = bt.money.mul(plMoney, exchangerate);//金额*利率

            if(bt.money.sub(plMoney, itemMoney) > 0){
                parent.layer.msg(bt.lang("cms.provide.loaninfo.plmoneyhigh"), {icon: 2});
                return false;
            }
            */
        }

        var isHookBond = bt.dicts.init().get("isHookBond").val();
        if (isHookBond == "1") {
            // 判断保证金的金额是否大于当前保证金账户的可用余额
            var bailmoney = moneyTypes.get("bailmoney").val();//保证金金额(就是项目的占用金额)
            if (Number(bailmoney) == Number(0)) {//保证金金额不能等于0
                layer.alert(bt.lang("fcbond.check.bailmoneyIsNull"), {icon: 2});
                return false;
            }
            var accAvailMoney = moneyFun.toNumber($("#accAvailMoney").val());
            //当前保证金账户的可用余额
            var bailMoneyPre;
            var editFlag = $("#editFlag").val();
            if (editFlag == "1") {//修改数据,保证金余额需加上修改之前占用的金额
                bailMoneyPre = bt.money.format($("#bailMoneyPre").val());
                accAvailMoney = bt.money.add(bailMoneyPre, accAvailMoney);
            }
            var leftMoney = bt.money.sub(accAvailMoney, bailmoney);
            if (leftMoney < 0) {//如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
                bt.loading.hide(); //关闭加载中
                layer.alert(bt.lang("fcbond.check.applyMoney"), {icon: 2});
                return false;
            }
            //判断借款单位与保证金账户单位是否一致
            /*            var loanCorpId=bt.corp.init().get("corpId").val();
                        var bondCorpId=$("#bondCorpId").val();
                        if(loanCorpId!=bondCorpId){
                            bt.loading.hide(); //关闭加载中
                            layer.alert(bt.lang("cms.provide.loaninfo.checkCorp"),{icon:2});
                            return false;
                        }*/

            //判断借款银行与保证金账户银行是否一致
            // var loanBankAcc=$("#bankId").val();
            // var bondBankAcc=$("#bondBankId").val();
            // if(loanBankAcc!=bondBankAcc){
            // 	layer.alert(bt.lang("cms.provide.loaninfo.checkBank"),{icon:2});
            // 	return false;
            // }

            //判断借款币别与保证金账户币别是否一致
            /*            var loanCurId=selectTypes.get("curId").val();
                        var bondCurId=$("#bondCurId").val();
                        if(loanCurId!=bondCurId){
                            bt.loading.hide(); //关闭加载中
                            layer.alert(bt.lang("cms.provide.loaninfo.checkCurId"),{icon:2});
                            return false;
                        }*/
        }

        if (selectTypes.get("plClass").val() === "1000") {
            var trs = $("#dataTableSy").children("tbody").children("tr");
            if (trs.length == 0) {
                parent.layer.alert(bt.lang("cms.loan.provideSyndicatedDtl.notExists"), {icon: 7});
                return false;
            }
        }

        var isOverRepair = dicts.get("isOverRepair").val();
        if (isOverRepair != "0"){
            //对接合同管理时处理
            var relateCon=$("#relateCon").val();
            var relateConType=$("#relateConType").val();
            if(!bt.empty(relateCon)&&relateCon=="1"){//对接合同管理
                if(!bt.empty(relateConType)&&relateConType=="0"){//生成合同
                    var conTypeId=getConTypeBankBorrow(corp.get("corpId").val());//获取合同类型
                    if (typeof conTypeId === "boolean"){
                        return false;
                    }else{
                        $("#conTypeId").val(conTypeId);
                    }
                }else if(!bt.empty(relateConType)&&relateConType=="1"){//调用合同
                    var conId=$("#conId").val();
                    if(bt.empty(conId)){
                        parent.layer.alert(bt.lang("调用合同请选择对应合同"), {icon: 7});
                        return false;
                    }
                }
            }
        }

        var action = "/cms/provide/loanInfoApply/tempOrSubmit";
        var nm = $(this).attr("id");
        var myflag = $("#myflag").val();
        data.field.myflag = myflag;
        if (nm == "submit") {
            data.field.saveType = "1";
        }
        var repayCycle = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();
        data.field.uploadId = up.getGroupId();
        data.field.conTypeId = $("#conTypeId").val();
        data.field.repayCycleName = repayCycle.creditperiodname;
        bt.loading.show();
        $.post(basePath + action, data.field, function (result) {
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });

    form.on('checkbox', function (data) {
        var nm = $(this).attr("name");
        //顾问费利率、第三方收益利率、其他利率复选框 监听
        if (nm == "consultantCb" || nm == "thirdPartyCb" || nm == "otherFeeCb") {
            setInterestFee();
        }
    });

    //获取利率
    $("#getRate").click(function () {
        getRate();
    });


    //是否折合人民币
    form.on('checkbox', function (data) {
        var nm = $(this).attr("name");
        //顾问费利率、第三方收益利率、其他利率复选框 监听
        if (nm == "isRmbRat") {
            clearRatModel();
            setCurRat();
        }
    });

    // if($("#myflag").val() == "1"){
    // 	if ($("#configIsHookBond").val() == "1") {
    // 		if (dicts.get("isHookBond").val() == "0") {
    // 			$("#bondBankAcc").removeAttr('lay-verify');
    // 			$(".bondInfo").hide();
    // 			form.renderIcon();
    // 		}
    // 		// 编辑时保证金别种设置
    // 		setBondCur();
    // 	}
    // }
    // 设置500毫秒后执行。由于无法判断表单渲染是否完成
    setTimeout(setIsGuaranteeEnabled, 500);
    // 触发是否挂授信切换，防止有必填校验未清除
    setTimeout(setRatModel, 500);
});

//是否跳过补录
function changeOverRepair() {
    var isOverRepair = dicts.get("isOverRepair").val();
    if (isOverRepair === "0") {//否
        $(".isOver").hide();
        $("#contractNo").removeAttr('lay-verify');
        rateTypes.get("overdueRate").attr('lay-verify', false);
    } else {//是
        $(".isOver").show();
        $("#contractNo").attr('lay-verify', 'required');
        rateTypes.get("overdueRate").attr('lay-verify', 'required');
    }
    form.renderIcon();
}

/**根据银行类型获取银行支行信息*/
function loadBtBankByType() {
    var bankTypeVal = selectTypes.get("btBankType").val();
    var typeid = $.trim(bankTypeVal);
    if (!bt.empty(typeid)) {
        $("[data-name='bankId']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/" + typeid);
    } else {
        $("[data-name='bankId']").data("url", basePath + "/cms/provide/loaninfo/queryBtBankByType/null");
    }
    selectTypes.get("btBank").reset();
}

function changeCur() {
    // 贷款利率设置
    loadLoanRate();
// 币种利率设置
    changeCurRate();
}

/**币别改变事件*/
function chengCur(data, elem, bv) {
    //根据币别查询保证金账户
    $("#curId").val(data.id);
    changeCur(data, elem, bv);
    if ($("#configIsHookBond").val() == "1") {
        setBondCurList();
    }
    //挂抵质押物时重选币别要重置抵质押物信息
    resetPledgeMsg();

    // 根据申请单位、借款币别、借款银行联动查询还款账号
    var corpId = bt.corp.init().get("corpId").val();
    var bankCode = $("#bankId").val()
    queryOnlineAcc(corpId, data.id, bankCode);
}

/**选择帐号的下拉列表初始化*/
function initBankAccTable() {
    var corpId = $("#corpId").val();
    var curId = $("#curId").val();
    var bankCode = $("#bankId").val();
    var url;
    if (!bt.empty(corpId) && !bt.empty(curId) && !bt.empty(bankCode)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&curId=" + curId + "&bankCode=" + bankCode
    } else if (!bt.empty(corpId) && !bt.empty(curId)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&curId=" + curId
    } else if (!bt.empty(corpId) && !bt.empty(bankCode)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&bankCode=" + bankCode
    } else {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId
    }
    selectTable = bt.selectTableType.init({
        elem: ".onlinePaymentAcc",
        type: 'get',
        paging: true,
        url: url,
        columns: [
            {data: "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
            {data: "accName", title: bt.lang("table.btBankAcc.accName")},
            {data: "bankName", title: bt.lang("table.btBankAcc.bankName")},
            {data: "curName", title: bt.lang("international.payment.curName")}
        ],
        clickCallback: function (severData, data) {
            $("#onlinePaymentAcc").val(severData.bankAcc);
            $("#onlineAccName").val(severData.accName);
            $("#onlineBankName").val(severData.bankName);
            $("#onlinePaymentAccId").val(severData.id);
            selectTypes.get("curId").val(severData.curId);
            $("#curId").val(severData.curId);
        },
        handleDeleteCallback : function (){
            clearOnlineAcc();
        }
    });


}

// 根据申请单位、借款币别、借款银行联动查询还款账号
function queryOnlineAcc(corpId, curId, bankCode) {
    var url;
    if (!bt.empty(corpId) && !bt.empty(curId) && !bt.empty(bankCode)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&curId=" + curId + "&bankCode=" + bankCode
    } else if (!bt.empty(corpId) && !bt.empty(curId)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&curId=" + curId
    } else if (!bt.empty(corpId) && !bt.empty(bankCode)) {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&bankCode=" + bankCode
    } else {
        url = basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId
    }
    selectTable.get("onlinePaymentAcc").setUrl(url)
    clearOnlineAcc();
}

//清空还款账号信息
function clearOnlineAcc () {
    $("#onlinePaymentAcc").val("");
    $("#onlineAccName").val("");
    $("#onlineBankName").val("");
    $("#onlinePaymentAccId").val("");
}

function initChengCur(data, elem, bv) {
    // 设置利率
    loadLoanRate();
    // 设置是否本位币
    showIsRmbRat();

    //设置保证金可选币别
    if ($("#configIsHookBond").val() == "1") {
        setBondCurList();
    }
}

//设置保证金可选币别
function setBondCurList() {
    //初始化保证金可选币别
    var data = selectTypes.get("curId").getData().obj;
    var billCurId;
    var bondCurId;
    var editFlag = $('#editFlag').val();
    if (!bt.empty(editFlag) && editFlag == "1") {//编辑页面
        bondCurId = $("#bondCurId").val();
        if (bt.empty(bondCurId)) {
            bondCurId = data.id;//保证金币别为空时（之前未开启保证金参数的情况下）,默认与业务币别一致
        }
        billCurId = data.id;
    } else {//录入页面，初始化借款币别为空值
        if (!bt.empty(data)) {
            bondCurId = data.id;
            billCurId = data.id;
        } else {
            bondCurId = "";
            billCurId = "";
        }
    }
    if (!bt.empty(billCurId) && !bt.empty(bondCurId)) {//保证金币别受业务币别变化影响，业务币别未加载完成时不加载保证金币别
        natualCurId = bt.empty(natualCurId) ? $("#natualCurId").val() : natualCurId;
        if (natualCurId == billCurId) {
            selectTypes.get("bondCurId").attr({
                "data-url": basePath + "/basic/business/btcurrency/json",
                "data-value": bondCurId,
                "data-displayData": billCurId
            })
        } else {
            selectTypes.get("bondCurId").attr({
                "data-url": basePath + "/basic/business/btcurrency/json",
                "data-value": bondCurId,
                "data-displayData": natualCurId + "," + billCurId
            })
        }
    }
}

/**加载基准利率和利率期限*/
function loadLoanRate() {
    var data = selectTypes.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    $("#curName").val(data.curname);
    var plDate = $.trim(bt.dateType.init().get("plDate").val());//借款申请日期
    var curId = data.id;
    if (bt.empty(plDate)) {
        plDate = $("#inputDate").val();//为空默认录入日期
    }
    if (bt.empty(curId)) {
        selectTypes.get("referenceRateType").attr("data-url", "");
    } else {
        // selectTypes.get("referenceRateType").attr("data-url", basePath + "/cms/loanrate/maintence/queryBaseRate");
        selectTypes.get("referenceRateType").attr({
            "data-url": basePath + "/cms/loanrate/maintence/queryBaseType?time=" + plDate + "&curId=" + curId,
            "data-defaultvalue": "first"
        });
    }
}

/**初始化基准利率**/
function initLoanRepayCycle() {
    loanRepayCycle();
}

/**基准利率加载完成后 加载利率周期*/
function loanRepayCycle(flag) {
    var data = selectTypes.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    natualCurId = bt.empty(natualCurId) ? $("#natualCurId").val() : natualCurId;
    plDate = $.trim(bt.dateType.init().get("plDate").val());//借款申请日期
    if (bt.empty(plDate)) {
        plDate = $("#inputDate").val();//为空默认录入日期
    }

    var pointsProportionText = $("#proportionText").val();
    if (pointsProportionText == "") {
        pointsProportionText = 0;
    }

    var sel = dicts.get("pointsProportionSelect").val();
    if (data.id != natualCurId) {//外币
        dicts.get("pointsProportionSelect").val("01");
        $("#inputMark").text("BP");
        $("#numUnit").removeClass("input-percent");
        $("#numUnit").addClass("input-bp");
        if (sel != "01") {
            bt.moneyType.init().get("pointsProportionText").val(pointsProportionText);
            bt.moneyType.init().get("pointsProportionText").reset();
        }
    }
    if (data.id == natualCurId) {//本位币
        var rateType = selectTypes.get("referenceRateType").getData();
        if (!bt.empty(rateType)) {
            var code = rateType.value;
            if (code == "lpr") {
                $("#numUnit").removeClass("input-percent");
                $("#numUnit").addClass("input-bp");
                dicts.get("pointsProportionSelect").val("01");
                $("#inputMark").text("BP");
                $("#deferred").show();
                $("#overdue").show();
            } else {
                $("#numUnit").removeClass("input-bp");
                $("#numUnit").addClass("input-percent");
                dicts.get("pointsProportionSelect").val("02");
                $("#inputMark").text("%");
                $("#deferred").hide();
                $("#overdue").hide();
            }
        }

        if (sel != "02") {
            bt.moneyType.init().get("pointsProportionText").val(pointsProportionText);
            bt.moneyType.init().get("pointsProportionText").reset();
        }
    }

    var referenceRateType = selectTypes.get("referenceRateType").val();//基准利率类型
    var curId = data.id;//币别


    if (bt.empty(data.id)) {
        selectTypes.get("repayCycle").attr("data-url", "");
    } else {//本位币
        if (bt.empty(flag)) {
            selectTypes.get("repayCycle").attr(
                {
                    "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + plDate + "&curId=" + curId + "&baseId=" + referenceRateType
                }
            );
        } else {
            if (typeof flag === "boolean") {
                selectTypes.get("repayCycle").attr(
                    {
                        "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + plDate + "&curId=" + curId + "&baseId=" + referenceRateType,
                        "data-required": flag
                    }
                );
            } else {
                selectTypes.get("repayCycle").attr(
                    {
                        "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + plDate + "&curId=" + curId + "&baseId=" + referenceRateType
                    }
                );
            }
        }

    }
}

/**初始化周期**/
function initLoadrepayCycle() {
    loadrepayCycle();
    getRate();
}


/**加载浮动利率的周期信息*/
function loadrepayCycle() {
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
    //设置text的输入范围
    pointsProportionTextObj.attr("data-min", data.ratedownlimit);
    pointsProportionTextObj.attr("data-max", data.rateuplimit);
    // pointsProportionTextObj.reset();
    readonlyText();
    // 更新利率类型的生效日期 start
    var rateType = selectTypes.get("repayCycle").getData();
    var timeOfTakeEffect = '';
    //基准利率
    var benchmarkRate = '';
    if (!bt.empty(rateType)) {
        if (rateType.obj && rateType.obj.timeOfTakeEffect) {
            timeOfTakeEffect = rateType.obj.timeOfTakeEffect;
            benchmarkRate = rateType.obj.rate;
        }
    }
    var floatWay = dicts.get("floatWay").val();
    var plDate = dateTypes.get("plDate").val();
    if (floatWay == "01") {//固定利率
        if (!plDate || plDate == '') {// 借款日期为空则报价日期的初始值为空，否则是 借款日期 之前最近的一个有效期
            timeOfTakeEffect = '';
        }
    } else {//非固定利率 报价日期的默认值为 利率调整生效日期匹配之前 最近的一个有效期
        var rateEntriesEffectDate = dateTypes.get("rateEntriesEffectDate").val();
        if (rateEntriesEffectDate && rateEntriesEffectDate != '' && data.alleffectdatearray && data.alleffectdatearray.length > 0) {
            var i = 0
            for (; i < data.alleffectdatearray.length; i++) {
                if (rateEntriesEffectDate == data.alleffectdatearray[i]) {
                    timeOfTakeEffect = data.alleffectdatearray[i];
                    break
                } else if (rateEntriesEffectDate < data.alleffectdatearray[i] && i > 0) {
                    timeOfTakeEffect = data.alleffectdatearray[i - 1];
                    break;
                }
            }
            if (i == data.alleffectdatearray.length) {
                timeOfTakeEffect = data.alleffectdatearray[i - 1];
            }
            benchmarkRate = data.effectdate2loanratemaintence[timeOfTakeEffect].rate;
        }
    }
    var rateData = $("#rateQuotedPriceDate");
    rateData.val(timeOfTakeEffect);
    rateTypes.get("benchmarkRate").val(benchmarkRate);
    // rateTypes.get("plRate").val(moneyFun.format(0, {decimal: 6}));
    rateTypes.get("plRate").val(''); //不用这个取值的原因 rateTypes 有时候报未初始化，有时候可以
    //$("#plRate").val('');
    // 更新利率类型的生效日期 end
}

function readonlyText() {
    var floatWay = dicts.get("floatWay").val();
    /*if(floatWay == "01"){//固定利率

        var rateType = selectTypes.get("referenceRateType").getData();
        if(!bt.empty(rateType)) {
            var code = rateType.obj.code;
            if (code == "001") {//利率类型是央行基准利率类型,不可以输入加减的BP点数
                var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
                pointsProportionTextObj.val(0);
                pointsProportionTextObj.attr("data-min",0);
                pointsProportionTextObj.attr("data-max",0);
                pointsProportionTextObj.attr("readonly",true);
            } else {
                var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
                pointsProportionTextObj.attr("readonly",false);
            }
        }

    }else{
        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        pointsProportionTextObj.attr("readonly",false);
    }*/
    // 目前与李总确定 不管是否是固定利率 点数或百分比的地方都要可以输入，输入的范围由利率类型和利率周期来决定
    var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
    pointsProportionTextObj.attr("readonly", false);
}

function changeRateQuotedPriceDate() {
    //rateTypes.get("plRate").val(moneyFun.format(0, {decimal: 6}));
    rateTypes.get("plRate").val('');
}

/**根据主业务担保类型查询担保方式*/
function queryPlType() {
    var mainPlTypeVal = selectTypes.get("mainPlType").val();
    var type = $.trim(mainPlTypeVal);
    var pl = '';
    if (!bt.empty(type)) {
        pl = "Cms.PlType." + type;
    } else {
        pl = "Cms.PlType.B";
    }
    htl = '<div class="bt-biz-dict" data-name="plType" data-type="select" data-group="' + pl + '" data-required="true"></div>';
    $("#plTypeTD").html(htl);
    bt.dicts.render()
}

/**根据借款日期 - 还款日期 - 期限单位 计算借款期限*/
var plDateCompare;

function computeplTerm1() {
    loanRepayCycle();//加载日期后刷新基准利率取值
    clearRatModel();//清除授信
    changeCurRate();
    computeplTerm();
    setRateEntriesEffectDate();//日期变动之后修改浮动生效日期

}

function computeplTerm() {
    //挂抵质押物时重选日期要重置抵质押物信息
    resetPledgeMsg();
    var plDate = $.trim(dateTypes.get("plDate").val());
    var repayDate = $.trim(dateTypes.get("repayDate").val());
    var plTerm;
    var month;
    if (bt.empty(plDate) || bt.empty(repayDate)) {
        plTerm = "";
    } else {
        var ternUnit = $.trim(dicts.get("ternUnit").val());
        var month = dataSubtract(plDate, repayDate, "M");
        if (!bt.empty(ternUnit)) {
            plTerm = dataSubtract(plDate, repayDate, ternUnit);
            if (ternUnit == "D") {
                //plTerm += 1;
            }
        } else {
            //为空或者null默认月处理
            dicts.get("ternUnit").val("M");
            plTerm = dataSubtract(plDate, repayDate, "M");
        }
    }
    moneyTypes.get("plTerm").val(plTerm);//设置借款期限
    setContractTerm(month);//设置合同期限

//	//日期改变记录 减少日期改变重新获取借款利率次数
//	if(!bt.empty(plDateCompare) || plDateCompare != plDate){
//		loanRepayCycle();//重新获取利率
//	}
    plDateCompare = plDate;

    checkRepayLanPrameter();

    //挂抵质押物时重选日期要重置抵质押物信息
    resetPledgeMsg();
}

/**设置合同期限*/
function setContractTermByPlTerm() {
    var plTerm = moneyTypes.get("plTerm").val();
    setContractTerm(plTerm);
}

function setContractTerm(plTerm) {
    var term = "01"
    if (plTerm < 13) {
        term = "01";//短期
    } else if (13 <= plTerm && plTerm <= 60) {
        term = "02";//中期
    } else if (plTerm > 60) {
        term = "03";//长期期
    }
    dicts.get("contractTerm").val(term);
}

/**计算data1 与 data2 相差多少param单位 data为字符串*/
function dataSubtract(data1, data2, param) {
    var dates;
    if (param == "D") {
        dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime())) / (1000 * 60 * 60 * 24);
    } else {
        var start = new Date(data1);
        var end = new Date(data2);

        var addMonth = end.getMonth() - start.getMonth();//月份差

        var startLastDay = mGetDate(start.getYear(), start.getMonth());//开始时间当前月总共多少天
        var endLastDay = mGetDate(end.getYear(), end.getMonth());//结束时间当前月总共多少天

        if (startLastDay == start.getDate()) {//开始日期月末最后一天
            // if (endLastDay == end.getDate()) {//结束日期月末最后一天
            //     addMonth++;//月份+1
            // } else {
            if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                addMonth++;//月份+1
            }
            // }
        } else {
            if (endLastDay == end.getDate()) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        }

        dates = (end.getYear() - start.getYear()) * 12 + addMonth;
    }
    return dates;
}

function mGetDate(year, month) {
    var d = new Date(year, month + 1, 0);
    return d.getDate();
}

/**
 * 设置浮动点数
 */
function setFloatPoints() {
    var floatWay = dicts.get("floatWay").val();
    /*if(floatWay == "01"){//固定利率
        //需求： （4）	利率类型不是央行基准类型时，浮动利率变动方式为点数，可以输入加减的BP点数 start
        var rateType = selectTypes.get("referenceRateType").getData();
        if(!bt.empty(rateType)) {
            var code = rateType.obj.code;
            if (code == "001") {//利率类型是央行基准利率类型,不可以输入加减的BP点数
                $("input[data-id='pointsProportionText']").attr("readonly", "readonly");
            }else{
                $("input[data-id='pointsProportionText']").removeAttr("readonly").removeAttr("unselectable");
            }
        }
        //需求： （4）	利率类型不是央行基准类型时，浮动利率变动方式为点数，可以输入加减的BP点数 end
    } else {
        loadrepayCycle();
        $("input[data-id='pointsProportionText']").removeAttr("readonly").removeAttr("unselectable");
    }*/
    // 目前与李总确定 不管是否是固定利率 点数或百分比的地方都要可以输入，输入的范围由利率类型和利率周期来决定
    loadrepayCycle();
    $("input[data-id='pointsProportionText']").removeAttr("readonly").removeAttr("unselectable");
}

/**浮动方式选择月、季、半年、年生产对应的详情选择 用来计算利率生效日期*/
function setNextDetail() {
    setFloatPoints();
    var floatWay = dicts.get("floatWay").val();

    var rateData = $("#rateEntriesEffectDate");//利率生效日期控制
    rateData.attr("readonly", true);
    rateData.attr("lay-verify", "required");
    rateData.val("");

    var postfix = "";
    var flag = false;
    if (floatWay == "01") {
        dicts.get("cplInterestPayment").val({"disabled": false});
        dicts.get("cplInterestPayment").val("01");
    } else {
        dicts.get("cplInterestPayment").val({"disabled": true});
        dicts.get("cplInterestPayment").val("01");
    }
    if (floatWay == "01") {//固定利率
        rateData.attr("lay-verify", "");

        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        pointsProportionTextObj.val(0);
        pointsProportionTextObj.attr("data-min", 0);
        pointsProportionTextObj.attr("data-max", 100);

        // pointsProportionTextObj.reset();
        flag = true;
    } else if (floatWay == "02") {//即时浮动
        var plDate = $.trim(dateTypes.get("plDate").val());
        if (bt.empty(plDate)) {
            plDate = $("#inputDate").val();//为空默认录入日期
        }
        rateData.attr("readonly", false);
        rateData.val(plDate);
    } else if (floatWay == "03") {//按月浮动
        postfix = "Month"
    } else if (floatWay == "04") {//按季浮动
        postfix = "Season"
    } else if (floatWay == "05") {//按半年浮动
        postfix = "HY"
    } else if (floatWay == "06") {//按年浮动
        postfix = "Year"
    }
    if (postfix == "") {
        $("#nextDetail").html("");//删除页面元素
    } else {
        var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext' + postfix + '" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay' +
            postfix + '" data-required="true"></div>';
        $("#nextDetail").html(htl);
        bt.dicts.render();
    }
    //disabledLoanRate(flag);//暂时不需要控制固定利率不能选择浮动方式
    bt.dicts.render();
    dateTypes.get("rateEntriesEffectDate").reset();
    loanRepayCycle(flag);
}

/**禁用基准利率类型与周期等*/
function disabledLoanRate(flag) {
    selectTypes.get("referenceRateType").val({"disabled": flag});
    selectTypes.get("repayCycle").val({"disabled": flag});
    dicts.get("pointsProportionSelect").val({"disabled": flag});
    moneyTypes.get("#pointsProportionText").val({"disabled": flag});
    if (!flag) {
        $("#getRate").show();
    } else {
        moneyTypes.get("pointsProportionText").val(0);
        $("#getRate").hide();
    }
    selectTypes.get("repayCycle").render();
    selectTypes.get("referenceRateType").render();
    dicts.get("pointsProportionSelect").render();
}

/**根据浮动方式详情设置利率调整生效日期*/
function setRateEntriesEffectDate() {
    var floatWay = dicts.get("floatWay").val();
    if (floatWay == "02") {//即时浮动
        var plDate = $.trim(dateTypes.get("plDate").val());
        if (bt.empty(plDate)) {
            plDate = $("#inputDate").val();//为空默认录入日期
        }
        $("#rateEntriesEffectDate").val(plDate);

    } else if (floatWay == "03") {//按月浮动
        nextFloatWay(1, "Month");
    } else if (floatWay == "04") {//按季浮动
        nextFloatWay(3, "Season");
    } else if (floatWay == "05") {//按半年浮动
        nextFloatWay(6, "HY");
    } else if (floatWay == "06") {//按年浮动
        nextFloatWay(12, "Year");
    }
    loadrepayCycle();
}

/**处理生效日期*/
function nextFloatWay(count, postfix) {
    var plDate = $.trim(dateTypes.get("plDate").val());
    if (bt.empty(plDate)) {
        plDate = $("#inputDate").val();//为空默认录入日期
    }

    var floatWayNext = dicts.get("floatWayNext" + postfix).val();
    var date = getFloatDate(plDate, null, floatWayNext, true, count);
    $("#rateEntriesEffectDate").val(date);
}


//获取利率
function getRate() {
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var choseDate = dateTypes.get("rateQuotedPriceDate").val();
    if (!data.effectdate2loanratemaintence || !data.effectdate2loanratemaintence[choseDate]) {
        parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
        return;
    }
    var baseRate = data.effectdate2loanratemaintence[choseDate].rate;
    // if(bt.empty(data.rate)){
    // 	parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
    // 	return ;
    // }

    var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyTypes.get("pointsProportionText").val();//浮动点数或比例值
    var floatWay = dicts.get("floatWay").val();
    if (pointsProportionText != "") {//非固定利率
        if (pointsProportionSelect == "01") {//点数
            //rateTypes.get("plRate").val(data.rate+(pointsProportionText/100));//使用百分比  不使用万分比
            //基础利率+浮动的点数*0.01%
            rateTypes.get("plRate").val(moneyFun.format(baseRate + pointsProportionText * (0.01 / 100), {decimal: 6}));
            setInterestFee();//自动计算综合利率
            return;
        } else {
            //比例 基准利率+浮动的比例*基准利率
            //rateTypes.get("plRate").val((data.rate)*(1+(pointsProportionText/100)));
            rateTypes.get("plRate").val(moneyFun.format(baseRate + (pointsProportionText / 100) * baseRate, {decimal: 6}));
            setInterestFee();//自动计算综合利率
            return;
        }
    }

    if (bt.empty(data.rate)) {
        rateTypes.get("plRate").val(0);
    } else {
        rateTypes.get("plRate").val(moneyFun.format(data.rate, {decimal: 6}));
    }

    setInterestFee();//自动计算综合利率
    checkRepayLanPrameter();
}

/**根据利率自动计算利息与费用并求和设置综合成本年华利率*/
function setInterestFee() {
    var plMoney = moneyTypes.get("plMoney").val();//借款金额
    var plRate = rateTypes.get("plRate").val();//借款利率
    var anticInterest = computeInterest(plMoney, plRate);//预计利息
    moneyTypes.get("anticInterest").val(anticInterest);
    //summation();
    checkRepayLanPrameter();
}

function setInterestFee1(data, elem) {
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("plMoney").val();//借款金额
    var consultantFee = moneyFun.mul(plMoney, data);//预计费用
    moneyTypes.get("costTypeDtl[" + rowIndex + "]advanceFees").val(consultantFee);
    //summation();
}

function setInterestFee4(data, elem) {
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("plMoney").val();//借款金额
    var rate = data / plMoney;//顾问费
    rateTypes.get("costTypeDtl[" + rowIndex + "]rate").val(rate);
}

//借款利率
function setInterestFee5(data, elem) {
    var plMoney = moneyTypes.get("plMoney").val();//借款金额
    var rateBase = $("[name='rateBase']:checked").val();//利息测算基数
    var plDate = $.trim(dateTypes.get("plDate").val());//借款日期
    var repayDate = $.trim(dateTypes.get("repayDate").val());//还款日期
    var rate = 0;
    if (plDate && repayDate) {
        //借款利率=输入的预计利息/（借款金额*期限/利息测算基数）
        var days = dataSubtract(plDate, repayDate, "D");
        rate = data / (plMoney * days / rateBase);
    }
    rateTypes.get("plRate").val(moneyFun.format(rate, {decimal: 6}));
}


/**计算综合成本年化利率**/
function summation() {
    var countRate = 0;
    var plRate = rateTypes.get("plRate").val();//借款利率
    countRate = fun.add(countRate, plRate);
    var consultantCb = $("[name='consultantCb']:checked").val();//顾问checkBox
    var consultantFeeRate = rateTypes.get("consultantFeeRate").val();//顾问利率
    if (consultantCb == 1) {
        countRate = fun.add(countRate, consultantFeeRate);
    }
    rateTypes.get("compositeFeeRate").val(countRate);//综合利率
    countCompositeFee();
}

/**计算综合成本*/
function countCompositeFee() {
    var countMoney = 0;
    var anticInterest = moneyTypes.get("anticInterest").val();//预计利息
    countMoney = fun.add(countMoney, anticInterest);
    var consultantFee = moneyTypes.get("consultantFee").val();//顾问费
    countMoney = fun.add(countMoney, consultantFee);
    //var otherFee = moneyTypes.get("otherFee").val();//其他费用
    //countMoney = fun.add(countMoney,otherFee);
    //var thirdPartyProfit = moneyTypes.get("thirdPartyProfit").val();//第三方收费
    //countMoney = fun.add(countMoney,thirdPartyProfit);

    moneyTypes.get("compositeFee").val(countMoney);//综合费用
}

function computeInterest(money, plRate) {
    if (bt.empty(money) || money == 0 || bt.empty(plRate) || plRate == 0) {
        return 0;
    }
    var plTerm;//贷款期限（日）
    var plDate = $.trim(dateTypes.get("plDate").val());//借款日期
    var repayDate = $.trim(dateTypes.get("repayDate").val());//还款日期
    if (bt.empty(plDate) || bt.empty(repayDate)) {
        var ternUnit = $.trim(dicts.get("ternUnit").val());
        plTerm = moneyTypes.get("plTerm").val();
        if (bt.empty(ternUnit) || ternUnit == "M") {
            plTerm = plTerm * 30;
        }
    } else {
        plTerm = dataSubtract(plDate, repayDate, "D");
    }
    //plTerm = dataSubtract(plDate,repayDate,"D");
    var rateBase = $("[name='rateBase']:checked").val();//利息测算基数
    return fun.format((money * plRate * plTerm) / (rateBase), {separator: "", decimal: 2});
}

/**检查还款计划参数 是否发生变化*/
function checkRepayLanPrameter() {
    var loanRate = rateTypes.get("plRate").val();//借款利率
    var loanDate = dateTypes.get("plDate").val();//借款日期
    var repayDate = dateTypes.get("repayDate").val();//还款日期
    var repayType = selectTypes.get("repayType").val();//还款方式
    var loanMoney = moneyTypes.get("plMoney").val();//金额
    if (beforeLoanRate != loanRate || beforeLoanDate != loanDate || beforeRepayDate != repayDate || beforeRepayType != repayType || beforeLoanMoney != loanMoney) {
        $("#planStatus").val("1");
    }
    beforeLoanRate = loanRate;
    beforeLoanDate = loanDate;
    beforeRepayDate = repayDate;
    beforeRepayType = repayType;
    beforeLoanMoney = loanMoney;
}

/**加载还款计划*/
function repayLan() {
    var loanCode = $("#id").val();//借款单号
    var planId = $("#planId").val();
    var loanRate = rateTypes.get("plRate").val();//借款利率
    var loanDate = dateTypes.get("plDate").val();//借款日期
    var repayDate = dateTypes.get("repayDate").val();//还款日期
    var repayType = selectTypes.get("repayType").val();//还款方式
    var loanMoney = moneyTypes.get("plMoney").val();//金额
    var rateBase = $("[name='rateBase']:checked").val();

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
    if (bt.empty(loanRate)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.plrateempty"), {icon: 2});
        return false;
    }

    var params = "billCode=" + loanCode + "&loanRate=" + loanRate + "&loanDate=" + loanDate + "&repayDate=" + repayDate
        + "&repayType=" + repayType + "&loanMoney=" + loanMoney + "&planId=" + planId + "&rateBase=" + rateBase;

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
    if (data.capitalfrequencytype == "N" && data.interestfrequencytype == "N") {
        $("#makePlanBtn").addClass("layui-disabled");
        $("#makePlanBtn").attr("disabled", "disabled");
        $("#planStatus").val("0")
        $("#planId").val("");
    } else {
        $("#makePlanBtn").removeClass("layui-disabled");
        $("#makePlanBtn").removeAttr("disabled", "disabled");
        $("#planStatus").val("1")
    }

}

//银行账号选择
function bankinfoCallback(data, dom) {
    $("#shouBankName").val(data.bankName);
    $("#bankId").val(data.bankCode);//联行号
    $("#bankType").val(data.bankTypeId);
    $("#bankTypeName").val(data.bankTypeName);
    $("#bankName").val(data.bankName);
    $("#bankCode").val(data.bankCode);

    //清空保证金信息
    if ($("#configIsHookBond").val() == "1") {
        clearFcBondMsg();
    }
}

function bankinfoCallBackTwo() {
    var data = selectTypes.get("btBank").currentObject.elem.find("option:selected").data();
    $("#shouBankName").val(data.bankname);
    $("#bankId").val(data.bankcode);//联行号
    $("#bankType").val(data.bankTypeId);
    $("#bankTypeName").val(data.banktypename);
    $("#bankName").val(data.bankname);
    $("#bankCode").val(data.bankcode);
    //清空保证金信息
    if ($("#configIsHookBond").val() == "1") {
        changeFcBond();
    }

    // 根据申请单位、借款币别、借款银行联动查询还款账号
    var corpId = bt.corp.init().get("corpId").val();
    var curId = $("#curId").val();
    var bankCode = data.bankcode;
    queryOnlineAcc(corpId, curId, bankCode);
}

/**
 * 授信额度选择
 * @param params
 */
function beforeRation() {
    var corpId = bt.corp.init().get("corpId").val();
    if (bt.empty(corpId) || corpId == 0) {
        //选择单位
        parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return "";
    }
    var curId = bt.selectType.init().get("curId").val();//币别
    if (bt.empty(curId)) {
        //选择币别
        parent.layer.msg(bt.lang("cms.provide.loaninfo.curIdnull"), {icon: 2});
        return "";
    }
    var bankId = $("#bankId").val();//银行
    if (bt.empty(bankId.trim())) {
        //选择银行
        parent.layer.msg(bt.lang("cms.provide.loaninfo.bankIdnull"), {icon: 2});
        return "";
    }
    var loanDate = dateTypes.get("plDate").val();//借款日期
    if (bt.empty(loanDate)) {
        plDate = $("#inputDate").val();//为空默认录入日期
        /*parent.layer.msg(bt.lang("cms.provide.loaninfo.pldateempty"), {icon: 2});
        return false;*/
    }
    var itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
    if (itemCode == undefined) {
        itemCode = "";
    }
    var beginDate = bt.dateType.init().get("plDate").val();
    var endDate = dateTypes.get("repayDate").val();
    var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    var params = {
        corpId: corpId,
        curCode: curId,
        bankCode: bankId,
        ratItemCode: itemCode.replace(" ", ""),
        beginDate: beginDate.replace(" ", ""),
        endDate: endDate,
        isRmbRat: isRmbRat
    }
    return params;
}

//选择授信
function chooseRatCode(params) {
    var params = beforeRation();
    if (bt.empty(params)) {
        return;
    }
    openRation(params);
}

//查看授信
function viewRatCode() {
    var params = beforeRation();
    params.beginDate = "";
    params.endDate = "";
    if (bt.empty(params)) {
        return;
    }
    viewRationDtl(params);
}

/**显示本位币*/
function showIsRmbRat() {
    var data = selectTypes.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    natualCurId = bt.empty(natualCurId) ? $("#natualCurId").val() : natualCurId;
    if (!bt.empty(natualCurId) && !bt.empty(data.id)) {
        if (data.id != natualCurId) {
            if ($("#isRmbRatTr").length > 0) {
                $("#isRmbRatTr").show();
            }
        } else {//本位币
            if ($("#isRmbRatTr").length > 0) {
                $("#isRmbRatTr").hide();
                if ($("#exchangerate").length > 0) {
                    moneyTypes.get("exchangerate").val("");
                }
            }
        }
    } else {
        $("#isRmbRatTr").hide();
        if ($("#exchangerate").length > 0) {
            moneyTypes.get("exchangerate").val("");
        }
    }
}

function changeCurRate() {
    // 本位币勾选设置计算
    showIsRmbRat();
    // 清除授信信息
    clearRatModel();
    // 设置币别汇率
    if (!bt.empty(relateRat) && relateRat == "1") {//挂接授信时
        setCurRat();
    }
    //清空保证金信息
    if ($("#configIsHookBond").val() == "1") {
        clearFcBondMsg();
        // 保证金币别设置
        // setBondCur();
    }
}

//设置币别汇率
function setCurRat() {
    var ratModel = bt.dicts.init().get("ratmodel").val();
    if (bt.empty(ratModel) || "0" == ratModel) {
        return;
    }
    var loanDate = bt.dateType.init().get("plDate").val();//借款日期
    if (bt.empty(loanDate)) {
        return;
    }

    var data = selectTypes.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    if ("0" == isRmbRat) {//不显示外币汇率
        $("#exchangerateTd1").hide();
        $("#exchangerateTd2").hide();
        $("#isRmbRat").val("0");
        moneyTypes.get("exchangerate").val("");
    } else {
        $("#exchangerateTd1").show();
        $("#exchangerateTd2").show();
        $("#isRmbRat").val("1");
        if (data.id == natualCurId) {//当前选中的币别是当前单位的本位币
            moneyTypes.get("exchangerate").val("1");
        } else {
            if (!bt.empty(natualCurId) && !bt.empty(data.id)) {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId: data.id,
                    convertCurId: natualCurId,
                    plDate: loanDate
                }, function (retData) {
                    if (retData.success) {
                        moneyTypes.get("exchangerate").val(retData.obj);
                    } else {
                        selectTypes.get("curId").val("");
                        moneyTypes.get("exchangerate").val("");
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }

        }
    }
}

//综合授信选择后是否根据综合授信额度自动设置借款金额
function showRationItemMoney(itemCode) {
    bt.selectType.init().get("ratItemCode").val(itemCode);//授信品种
    //moneyTypes.get("plMoney").val("" + itemMoney);//金额
}

/**设置保证金比例*/
function setBailMoneyRate() {
    if ($("#configIsHookBond").val() == "1") {
        var plMoney = moneyTypes.get("plMoney").val();
        var bailmoney = moneyTypes.get("bailmoney").val();

        if (bt.empty(plMoney)) {
            return;
        }
        if (!bt.empty(bailmoney)) {
            var curId = selectTypes.get("curId").val();
            var bondCurId = selectTypes.get("bondCurId").val();
            if (curId == bondCurId) {
                // 修改为保证金金额可以大于借款金额
                rateTypes.get("bailmoneyRate").val(fun.div(bailmoney, plMoney, 4));
            } else {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId: curId,
                    convertCurId: bondCurId,
                    plDate: bt.dateType.init().get("plDate").val()//借款日期
                }, function (retData) {
                    if (retData.success) {
                        var convertMoney = fun.div(bailmoney, retData.obj, 4);
                        // 修改为保证金金额可以大于借款金额
                        rateTypes.get("bailmoneyRate").val(fun.div(convertMoney, plMoney, 4));
                    } else {
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }
        }
    }

}

/**设置保证金*/
function setBailMoney() {
    if ($("#configIsHookBond").val() == "1") {
        var plMoney = moneyTypes.get("plMoney").val();
        var bailmoneyRate = rateTypes.get("bailmoneyRate").val();
        if (bt.empty(plMoney)) {
            return;
        }
        if (!bt.empty(bailmoneyRate) && bailmoneyRate != 0) {
            var billCur = selectTypes.get("curId").val();
            var bondCur = selectTypes.get("bondCurId").val();
            var loanDate = bt.dateType.init().get("plDate").val();//借款日期
            if (billCur == bondCur) {
                moneyTypes.get("bailmoney").val(fun.format(fun.mul(plMoney, bailmoneyRate), {decimal: 2}));
            } else {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId: billCur,
                    convertCurId: bondCur,
                    plDate: loanDate
                }, function (retData) {
                    if (retData.success) {
                        moneyTypes.get("bailmoney").val(fun.format(fun.mul(plMoney * retData.obj, bailmoneyRate), {decimal: 2}));
                    } else {
                        moneyTypes.get("bailmoney").val("");
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }
        }
    }
    checkRepayLanPrameter();

    /**
     * 借款金额发生改变时重新计算银团比例信息
     */
    if (selectTypes.get("plClass").val() === "1000") {
        calCustomMoney();
    }
}

//网点下拉框回调
function corpChange(data) {
    var url = basePath + "/cms/provide/loaninfo/getPurpose?corpId=" + data.id;
    bt.selectType.init().get("plPurpose").attr('data-url', url);
    natualCurId = data.curId;
    // 清除授信信息
    clearRatModel();

    //清空保证金信息
    if ($("#configIsHookBond").val() == "1") {
        clearFcBondMsg();
        bondCurCorp = data.id;//用于传参，便于保证金币别查询本位币
        var billCur = selectTypes.get("curId").getData().obj;
        if (!bt.empty(billCur)) {//因保证金币别受业务币别影响，业务币别为空时不加载保证金币别,业务币别不为空时重新加载保证金币别
            setBondCurList();//重新设置保证金币别，本位币受单位影响
        }
    }

    if (!bt.empty(relateRat) && relateRat == "1") {//参数设置挂接授信，需要查询本位币
        var url2 = basePath + "/cms/provide/loaninfo/getBankName?corpId=" + data.id;
        bt.selectType.init().get("btBank").attr('data-url', url2);
        setCurRat();//重新计算折算汇率
    }

    //挂抵质押物时重选单位要重置抵质押物信息
    resetPledgeMsg();

    // 根据申请单位、借款币别、借款银行联动查询还款账号
    var curId = $("#curId").val()
    var bankCode = $("#bankId").val()
    queryOnlineAcc(data.id, curId, bankCode);

}

bt.bankInfoBeforeClick = function () {
//	var bankInfo = bt.bankInfo.init();
//    bankInfo.set({
//        "joinBankType": true,
//    });
    return true;
}

//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data, dom) {
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondMoney").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bondBankAcc").val(data.bankAcc);//保证金账户
    $("#bondBankId").val(data.bankCode);
    selectTypes.get("bondCurId").val(data.curId);//保证金币别
    rateTypes.get("bondRate").val(data.bondRate);
    $("#bailMoneyPre").val("");
    if (data.bondId != $("#bondIdPre").val()) {
        $("#bailMoneyPre").val("");
    }
    // 设置保证金金额y
    setBailMoney();

}


//如果选择了否,清空填写的保证金占用信息
function changeFcBond() {
    var isHookBond = dicts.get("isHookBond").val();
    if (isHookBond == "0") {
        $("#bondBankAcc").removeAttr('lay-verify');
        rateTypes.get("bailmoneyRate").attr('lay-verify', false);
        moneyTypes.get("bailmoney").removeAttr("lay-verify");
        selectTypes.get("bondCurId").attr('lay-verify', false);
        $(".bondInfo").hide();
        clearFcBondMsg();
    } else {
        $(".bondInfo").show();
        $("#bondBankAcc").attr('lay-verify', 'required');
        rateTypes.get("bailmoneyRate").attr('lay-verify', 'required');
        moneyTypes.get("bailmoney").attr('lay-verify', 'required');
        selectTypes.get("bondCurId").attr('lay-verify', true);
    }
    form.renderIcon();
}

//清空保证金信息
function clearFcBondMsg() {
    $("#bondBankAcc").val("");
    // selectTypes.get("bondCurId").val("");
    moneyTypes.get("bailmoney").val("");
    rateTypes.get("bailmoneyRate").val("");
}


//提示未选择单位
function checkCorp() {
    var corpId = bt.corp.init().get("corpId").val();
    var bondCurId = selectTypes.get("bondCurId").val();
    var startDate = dateTypes.get("plDate").val();
    var endDate = dateTypes.get("repayDate").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("table.fcBond.corp.null"), {icon: 2});
        return false;
    }
    if (bt.empty(bondCurId)) {
        parent.layer.msg(bt.lang("common.bondCurId.alert.info1"), {icon: 2});
        return false
    }
    if (bt.empty(startDate)) {
        parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
        return false
    }
    if (bt.empty(endDate)) {
        parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
        return false
    }

    parameters.corpId = corpId;
    parameters.bankCode = $("#bankId").val();
    parameters.curId = selectTypes.get("bondCurId").val();
    parameters.startDate = startDate;
    parameters.endDate = endDate;
    bt.bondAccInfo.set(parameters);
    return true;

}

/**
 * 保证金币别设置
 * @param data
 */
function setBondCur() {
    var data = selectTypes.get("curId").getData().obj;
    if (!bt.empty(data)) {
        if (data.naturalSign == "1") {
            // 本位币
            selectTypes.get("bondCurId").attr({"data-value": data.id, "data-displayData": data.id})
        } else {
            // 外币
            $.ajax({
                url: basePath + "/basic/business/btcurrency/json",
                type: "get",
                dataType: "json",
                success: function (allCur) {
                    $.each(allCur, function (index, item) {
                        if (item.naturalSign == "1") {
                            // 设置只能选业务相同外币和本位币
                            selectTypes.get("bondCurId").attr({
                                "data-value": data.id,
                                "data-displayData": item.id + "," + data.id
                            })
                        }
                    })
                }
            });
        }
        selectTypes.get("bondCurId").val(data.id);
    }
}

// 币别发生变化时,则根据比例设置金额
function changeBondCur() {
    // 清空保证金信息
    $("#bondBankAcc").val("");
    moneyTypes.get("bailmoney").val("");
    rateTypes.get("bailmoneyRate").val("");
    // 设置保证金金额
    setBailMoney();
}

// 授信回调扩展
function ratInfoExtSet(ratInfoData) {
    if (ratInfoData.assurepercent != null && ratInfoData.assurepercent != "" && ratInfoData.assurepercent != undefined && ratInfoData.assurepercent != "0") {
        rateTypes.get("bailmoneyRate").val(ratInfoData.assurepercent);
        setBailMoney();
    }
    /**
     * 判断授信是否挂担保
     */
    var data = {"value": "0"};
    dicts.get("isHangGuarantee").attr({"data-value": "0"});
    if (ratInfoData.isHangGuarantee == "1") {
        form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
    } else {
        form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
    }
    bt.setGuaInfo(data);
}


/**
 * 担保选择参数设置
 * @param param
 */
function setOtherParam(param) {
    var viewFlag = $("#viewFlag").val();
    var corpId//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    var curId;//币别
    if (viewFlag == "1") {
        startDate = $("#plDate").val();//开始日期
        endDate = $("#repayDate").val();//结束日期
        corpId = $("#corpId").val();//单位
        curId = $("#curId").val();//币种
    } else {
        startDate = $.trim(dateTypes.get("plDate").val());//开始日期
        endDate = $.trim(dateTypes.get("repayDate").val());//结束日期
        corpId = corp.get("corpId").val();
        //网点id
        curId = selectTypes.get("curId").val();//币别
    }
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "" : endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

/**
 * 担保选择前参数检查
 * @returns {boolean}
 */
function selectBefore() {
    var corpId = corp.get("corpId").val();
    if (bt.empty(corpId)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    }
    // var curId = selectTypes.get("curId").val();
    // if (bt.empty(curId)) {
    //     parent.layer.msg(bt.lang("cms.provide.loaninfo.curIdnull"), {icon: 2});
    //     return false;
    // }
    return true;
}

/**
 * 只读设置
 */
function setIsGuaranteeEnabled() {
    var isGuarantee = $("#isGuarantee").val();
    if (isGuarantee == "1") {
        var ratIsHangGua = $("#ratIsHangGua").val();
        if ("1" == ratIsHangGua) {
            form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
        } else {
            form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        }
    }
}

function setItemCode(data, elem, bv) {
    bt.setItemCode(data, elem, bv)
}

//设置其他费用类型不可选
bt.setItemCode = function (data, elem, bv) {
    var Othis = elem.othis.parent('[data-link="true"]');
    var v = elem.value;
    var doms = $('[data-link="true"]');
    bt.each(doms, function () {
        var othat = $(this);
        if (!othat.is(Othis)) {
            othat.find('select option[value="' + v + '"]').prop("disabled", true);
            othat.find('select option[value="' + bv + '"]').prop("disabled", false);
        }
    })
    form.render("select");
}

function addTr() {
    var tab = document.getElementById("dataTable");
    trHtml(tab);
    var lastRow = $("#dataTable tbody").find("tr:last").data('row');
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();
}

trHtml = function (tab) {
    var rowsCount = tab.rows.length;
    var row = rowsCount - 1;
    var trHtml = '<tr data-row="' + row + '">';
    trHtml += '<td align="center"><div class="layui-inline"> <div class="bt-biz-selectType" data-biz-name="costTypeDtl[' + row + ']costCode" data-url="' + basePath + '/cms/dfCostType/getValidDfCostType"' +
        ' data-valuefield="id" data-textfield="costName" id="costCode' + row + '" data-name="costTypeDtl[' + row + '].costTypeId" data-search="true" id="costCode' + row + '" ' +
        ' data-link="true" data-callback="setItemCode"></div></div></td>';
    trHtml += '<td align="center"><input type="text" id="rate' + row + '" name="costTypeDtl[' + row + '].rate" data-name="costTypeDtl[' + row + '].rate" class="layui-input bt-biz-rateType" data-biz-name="costTypeDtl[' + row + ']rate" data-callback="setInterestFee1" ></td>';
    trHtml += '<td align="center"><input type="text" id="advanceFees' + row + '" data-name="costTypeDtl[' + row + '].advanceFees"  autocomplete="off" data-biz-name="costTypeDtl[' + row + ']advanceFees" data-callback="setInterestFee4" class="layui-input bt-biz-moneyType"></td>';

    trHtml += '<td align="center"><a href="javascript:void(0);" title="#springMessage("common.delTr")" onClick="remoTr(this.parentElement.parentElement.rowIndex)"><i class="layui-icon">&#xe640;</i></a></td>';

    trHtml += '</tr>';
    $("#dataTable tbody").append(trHtml);
    initSelectCode(row);
    bt.selectType.init();
}

var initSelectCode = function (row) {
    bt.rateType.init();
    bt.moneyType.init();
    //设置新增的费用类型行不可选已勾选的
    bt.setNewItemDisable(row);
    bt.selectType.init();
}

//新增费用类型行设置已勾选的不可选择
bt.setNewItemDisable = function (rowIndex) {
    var doms = $('[data-link="true"]');
    var disItem = "";
    bt.each(doms, function (i, item) {
        var othat = $(this);
        var dataObj = $(othat).find("option:selected").val();
        if (!bt.empty(dataObj)) {
            //$("#"+rowIndex + "itemCode").find('select option[value="'+dataObj+'"]').prop("disabled",true);
            disItem += dataObj + ",";
        }
    })
    disItem = bt.jquery.trim(disItem);
    if (disItem.length > 1) {
        disItem = disItem.substr(0, disItem.length - 1);
        $("#costCode" + rowIndex).attr("data-itemdisabled", disItem);
        form.render("select");
    }
}

/**跳过发放改变*/
function changeAcrossSend() {
    renderBudget();
}

/**
 * 资金计划挂接
 */
function renderBudget() {
    //跳过发放需要提前填写资金计划
    var loanSendOutType = dicts.get("loanSendOutType").val();
    var acrossSend = dicts.get("acrossSend").val();
    if (loanSendOutType == 1 && acrossSend == 1) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断银行借款是否挂资金计划
            var sendItem = $("#sendItem").val();
            if (sendItem != null && sendItem != "") {
                $genOtherHtml();
            } else {
                $("#tagItemDetail").html("");
            }
        }
    } else {
        $("#tagItemDetail").html("");
    }
}

/**控制是否跳过发放功能显示*/
function loadAcrossSend() {
    var loanSendOutType = dicts.get("loanSendOutType").val();
    if (loanSendOutType == 1) {//一次发放
        $("#acrossSendShow").html(bt.lang("table.cmsProvideLoanInfo.acrossSend"));
        $("#acrossSendSelect").html('<div class="bt-biz-dict" data-name="acrossSend" data-defaultvalue="0" data-required="true" data-type="radio" data-biz-name="acrossSend" data-callback="changeAcrossSend" data-group="sf_yn"></div>');
        bt.dicts.render();
    } else {//分次发放
        $("#acrossSendShow").html("");
        $("#acrossSendSelect").html("");
    }
    renderBudget();
}

/**根据plRate + 上浮BP点数/10000 */
function setDeferredRate() {
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据

    if (data && !bt.empty(data.rate)) {
        getRate();
    }
    var plRate = rateTypes.get("plRate").val();//借款利率
    var deferredPoints = moneyTypes.get("deferredPoints").val();
    if (!deferredPoints) {
        deferredPoints = 0.00;
        moneyTypes.get("deferredPoints").val(deferredPoints)
    }
    var deferredRate = moneyFun.format(parseFloat(plRate) + parseFloat(deferredPoints / 10000), {decimal: 6});
    rateTypes.get("deferredRate").val(deferredRate);
}

/**根据plRate * (1+上浮百分比) */
function setOverdueRate() {
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据

    if (data && !bt.empty(data.rate)) {
        getRate();
    }
    var plRate = rateTypes.get("plRate").val();//借款利率
    var overduePercent = moneyTypes.get("overdueProportion").val();
    if (!overduePercent) {
        overduePercent = 0.00;
        moneyTypes.get("overdueProportion").val(overduePercent)
    }
    var overdueRate = moneyFun.format(parseFloat(plRate) * parseFloat(1 + overduePercent / 100), {decimal: 6});
    rateTypes.get("overdueRate").val(overdueRate);
}

function getPostfix() {
    var floatWay = dicts.get("floatWay").val();
    if (floatWay == "03") {//按月浮动
        return postfix = "Month"
    } else if (floatWay == "04") {//按季浮动
        return postfix = "Season"
    } else if (floatWay == "05") {//按半年浮动
        return postfix = "HY"
    } else if (floatWay == "06") {//按年浮动
        return postfix = "Year"
    }
    return null;
}

function remoTr(rowIndex) {
    var tab = document.getElementById("dataTable");
    tab.deleteRow(rowIndex);
    console.log(rowIndex);
    //循环修改元素的名字
    for (var i = rowIndex; i < (tab.rows.length - 1); i++) {
        var tempTr = tab.rows.item(i);
        for (var x = 0; x < tempTr.cells.length; x++) {
            var tempTd = tempTr.cells.item(x);
            var strHTML = tempTd.innerHTML;
            console.log(strHTML);
            while (strHTML.indexOf("[" + (i - 1) + "]") > 0) {
                strHTML = strHTML.replace("[" + (i - 1) + "]", "["
                    + (i - 2) + "]");
            }
            while (strHTML.indexOf("[" + (i) + "]") > 0) {
                strHTML = strHTML.replace("[" + (i) + "]", "[" + (i - 1)
                    + "]");
            }
            tempTd.innerHTML = strHTML;
        }
    }
    return false;
}

// 借款种类回调
function setLoanType(data) {
    //1000=为系统内置银团贷款，不可修改
    if (data.code == "1000") {
        $("#syndicatedShow").show();
        initSyndicated()
    } else {
        $("#syndicatedShow").hide();
    }
    plClass = data.name;
}



/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml=function(){
    var itemCode = $('#sendItem').val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateTypes.get("plDate").val();
    var corpId = corp.get("corpId").val();
    var curId= selectTypes.get("curId").val();;//币别
    var url=basePath;
    var sendId = $("#id").val();
    var param = null;
    if(sendId == null || sendId == ""){
        url += "/budget/fbsBudgetProcess/getItemDetail";
        param = {
            corpId:corpId,
            itemCode:itemCode,
            defItemCode:defItemCode,
            processDate:processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
    }else{
        url=basePath+"/budget/fbsBudgetProcess/getEditItemDetail";
        param={
            corpId:corpId,
            itemCode:itemCode,
            billNo: $("#billCode").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode:defItemCode,
            processDate:processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
    }

    $.ajax({
        url:url,
        method: 'POST',
        data: param,
        async:true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            form.render();
        }
    });
};


function pointsProportionCallback() {
    var code = dicts.get("pointsProportionSelect").val();
    if ("01" ==  code){
        $("#inputMark").text("BP");
    }else {
        $("#inputMark").text("%");
    }
    getRate();
}
