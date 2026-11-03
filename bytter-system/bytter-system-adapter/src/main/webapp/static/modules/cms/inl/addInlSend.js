var form, dateType, dicts, selectType, rateType, moneyType, moneyfun;
bt.use(["jquery", "form", "dateType", "selectType", "corp", "dicts", "rateType", "moneyType", "btUpload", "conView"], function () {
    $ = bt.jquery;
    form = bt.form();
    dicts = bt.dicts.init();//字典
    selectType = bt.selectType.init();//下拉选择
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();//日期时间
    moneyfun = bt.moneyType.getFun()
    conView = bt.conView.init();//合同查看组件
    var gid = $("#attachmentId").val();
    var sendAttachmentId = $("#sendAttachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        auto: false,
        gid: gid
    });

    var sendUpload = bt.btUpload.init({
        elem: "#sendUploader",
        isExpand: true,
        auto: false,
        gid: sendAttachmentId
    });

    // 资金计划挂接
    // renderBudget()

    form.on("submit(submit)", function (data) {
        if (submitCheck()) {
            return false;
        }
        var isBudget = $("#isBudget").val();
        if ("1" == isBudget) {
            let _bHtml = $("#tagItemDetail").html();
            _bHtml = _bHtml.replace(/(\r\n|\n|\r)/gm, "");
            if (isNotEmpty(_bHtml)) {
                //判断直融业务是否挂资金计划
                var inlInfoItem = $("#inlInfoItem").val();
                if (inlInfoItem != null && inlInfoItem != "") {
                    var sendMoney = moneyType.get("sendMoney").val();
                    var processMoneys = $("input[name = 'processMoney']");
                    var totalMoney = 0;
                    var itemCode = [];
                    var itemName = [];
                    var processMoney = [];
                    $.each(processMoneys, function (index, item) {
                        totalMoney = moneyfun.add(totalMoney, moneyType.get("processMoney" + index).val());
                        itemCode.push($("input[name = 'itemCode']")[index].value);
                        itemName.push($("input[name = 'itemName']")[index].value);
                        processMoney.push(moneyType.get("processMoney" + index).val());
                    });
                    if (Number(sendMoney) != Number(totalMoney)) {
                        layer.alert(bt.lang("资金计划执行金额与发放金额相等"), {icon: 2});
                        return false;
                    } else {
                        data.field.itemCode = itemCode;
                        data.field.itemName = itemName;
                        data.field.processMoney = processMoney;
                    }
                }
            }
        }
        bt.loading.show();
        data.field.status = this.value;
        data.field.attachmentId = sendUpload.getGroupId();
        jQuery.ajaxSettings.traditional = true;
        subform(data)
        return false;
    });

    /**
     *设置点数比例值
     */
    bt.readonlyText = function () {
        var floatWay = dicts.get("floatWay").val();
        if (floatWay != "01") {//非固定利率
            moneyType.get("pointsProportionText").attr("readonly", false);
        } else {
            moneyType.get("pointsProportionText").attr("readonly", true);
        }
        moneyType.get("pointsProportionText").val("");
        // 重新计算利率
        getRate();
    };

    /**
     * 加载利率周期
     * @returns {boolean}
     */
    bt.loanRepayCycle = function () {
        var curId = selectType.get("curId").val();//获取币别选择的全部数据
        var natualCurId = $("#natualCurId").val();
        if (isEmpty(curId) || isEmpty(natualCurId)) {
            return false;
        }
        var sendDate = dateType.get("sendDate").val();
        if (isEmpty(sendDate)) {
            sendDate = bt.date.format(new Date(), "yyyy-MM-dd");
        }
        var pointsProportionText = $("#proportionText").val();
        if (pointsProportionText == "") {
            pointsProportionText = 0;
        }

        var sel = dicts.get("pointsProportionSelect").val();
        if (curId != natualCurId) {//外币
            dicts.get("pointsProportionSelect").val("01");
            $("#inputMark").text("BP");
            if (sel != "01") {
                moneyType.get("pointsProportionText").val(pointsProportionText);
                moneyType.get("pointsProportionText").reset();
            }
        }
        var rateType = selectType.get("referenceRateType").getData();
        if (curId == natualCurId) {//本位币
            var code = rateType.obj.code;
            if (code == "lpr") {//市场基准利率为BP
                dicts.get("pointsProportionSelect").val("01");
                $("#inputMark").text("BP");
            } else {//央行基准利率为百分比
                dicts.get("pointsProportionSelect").val("02");
                $("#inputMark").text("%");
            }
            if (sel != "02") {
                moneyType.get("pointsProportionText").val(pointsProportionText);
                moneyType.get("pointsProportionText").reset();
            }
        }
        selectType.get("repayCycle").attr(
            {
                "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + sendDate + "&curId=" + curId + "&baseId=" + rateType.obj.id
            }
        );
        bt.loadrepayCycle();
    };

    /**加载浮动利率的周期信息*/
    bt.loadrepayCycle = function () {
        var data = selectType.get("repayCycle").getData();
        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        //设置text的输入范围
        pointsProportionTextObj.attr("data-min", data.ratedownlimit);
        pointsProportionTextObj.attr("data-max", data.rateuplimit);
        pointsProportionTextObj.reset();
        bt.readonlyText();
    };

    // 资金计划挂接
    renderBudget()
});

function subform(data) {
    var action = "/inl/send/save";
    $.post(basePath + action, data.field, function (data) {
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
    }, 'JSON');
}

function submitCheck() {
    var flag = false;
    var sendType = dicts.get("sendType").val();
    if ("1" == sendType) {
        var money = moneyType.get("money").val();
        var sendMoney = moneyType.get("sendMoney").val();
        if (money != sendMoney) {
            flag = true;
            layer.alert(bt.lang("放款金额必须等于拆借金额"), {icon: 2});
        }
    }

    var planId = $("#planId").val();
    if (isEmpty(planId)) {
        flag = true;
        layer.alert(bt.lang("cms.noReplament.Message"), {icon: 2});
    } else {
        var planStatus = $("#planStatus").val();
        if("1" == planStatus){
            flag = true;
            layer.alert(bt.lang("cms.provide.loansend.repaymentschedulresave"), {icon: 2});
        }
    }
    return flag;
}

/**
 * 币种回调函数
 */
function curCallBack() {
    var curId = selectType.get("curId").val();
    var natualCurId = $("#natualCurId").val();
    if(isEmpty(curId)){
        selectTypes.get("referenceRateType").attr("data-url","");
    }else if (curId != natualCurId) {
        selectType.get("referenceRateType").attr({
            "data-url": basePath + "/cms/loanrate/maintence/queryNotBaseRate"
        });
    } else {//本位币
        selectType.get("referenceRateType").attr("data-url", basePath + "/cms/loanrate/maintence/queryBaseRate");
    }
}

/**
 * 开始日期/到期日期回调
 * 计算拆借期限
 */
function calTerm() {
    var sendDate = dateType.get("sendDate").val();
    var endDate = dateType.get("endDate").val();
    var unit = dicts.get("unit").val();
    if (isNotEmpty(sendDate) && isNotEmpty(endDate)) {
        if ("D" == unit) {
            $("#term").val(bt.date.getBetweenDay(sendDate, endDate));
        } else {
            $("#term").val(bt.date.getBetweenMonth(sendDate, endDate));
        }
    }

    /**
     * 日期修改后需重新计算还款计划
     */
    checkRepayLanPrameter();

    /**
     * 重新加载资金计划
     */
    renderBudget();
}

/**
 * 资金拆借利率形式回调
 */
function floatWayCallBack() {
    var floatWay = dicts.get("floatWay").val();
    if ("01" != floatWay) {
        if ("02" == floatWay) {
            $("#rateEntriesEffectDate").removeAttr("readonly");
            dateType.get("rateEntriesEffectDate").val(bt.date.format(new Date(), "yyyy-MM-dd"));
            $("#nextDetail").html("")
        } else {
            loadFlowWayNext();
        }

    } else {
        $("#nextDetail").html("")
        $("#rateEntriesEffectDate").attr("readonly", "readonly");
        dateType.get("rateEntriesEffectDate").val("");
    }
    bt.loanRepayCycle();
    bt.readonlyText();
}


function loadFlowWayNext() {
    var floatWay = dicts.get("floatWay").val();
    var rateData = $("#rateEntriesEffectDate");//利率生效日期控制
    rateData.attr("readonly",true);
    var postfix = "";
    var flag = false;
    if(floatWay == "01"){//固定利率
        flag = true;
    }else if(floatWay == "03"){//按月浮动
        postfix = "Month"
    }else if(floatWay == "04"){//按季浮动
        postfix = "Season"
    }else if(floatWay == "05"){//按半年浮动
        postfix = "HY"
    }else if(floatWay == "06"){//按年浮动
        postfix = "Year"
    }

    if(flag){
        selectType.get("repayCycle").attr("data-required",false);
    }else{
        selectType.get("repayCycle").attr("data-required",true);
    }

    if(postfix == ""){
        $("#nextDetail").html("");//删除页面元素
    }else{
        var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext'+ postfix +'" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay'+
            postfix +'" data-required="true"></div>';
        $("#nextDetail").html(htl);
        bt.dicts.render();
    }
    dateType.get("rateEntriesEffectDate").val("");
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
function nextFloatWay(count,postfix){
    var sendDate = $.trim(dateType.get("sendDate").val());
    var floatWayNext = dicts.get("floatWayNext"+postfix).val();
    if (isNotEmpty(sendDate)) {
        var date = getFloatDate(sendDate, null, floatWayNext, true, count);
        $("#rateEntriesEffectDate").val(date);
    }
}





/**
 * 发放类型回调
 */
function sendTypeCallBack() {
    var sendType = dicts.get("sendType").val();
    if ("1" == sendType) {
        var money = moneyType.get("money").val();
        moneyType.get("sendMoney").attr({"readonly": "readonly"});
        moneyType.get("sendMoney").val(money);
    } else {
        moneyType.get("sendMoney").removeAttr("readonly")
    }
}

/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 加载还款计划
 * @returns {boolean}
 */
function repayPlan() {
    var sendBillId = $("#id").val();//发放id
    var planId = $("#planId").val();
    var rate = rateType.get("rate").val();//借款利率
    var sendDate = dateType.get("sendDate").val();//借款日期
    var endDate = dateType.get("endDate").val();//还款日期
    var repayType = selectType.get("repayType").val();//还款方式
    var sendMoney = moneyType.get("sendMoney").val();//金额
    var rateBase = dicts.get("rateBase").val();
    if(isEmpty(rate) || rate == 0){
        parent.layer.msg(bt.lang("利率不能为空"), {icon: 2});
        return false;
    }
    if(isEmpty(sendDate)){
        parent.layer.msg(bt.lang("请选择发放日期"), {icon: 2});
        return false;
    }
    if(isEmpty(endDate)){
        parent.layer.msg(bt.lang("请选择到期日期"), {icon: 2});
        return false;
    }
    if(isEmpty(repayType)){
        parent.layer.msg(bt.lang("请选择还款方式"), {icon: 2});
        return false;
    }
    if(isEmpty(sendMoney)){
        parent.layer.msg(bt.lang("发放金额不能为空"), {icon: 2});
        return false;
    }

    var params = "billCode=" + (isEmpty(sendBillId) ? "" : sendBillId) + "&loanRate=" + rate + "&loanDate=" + sendDate + "&repayDate=" + endDate
        + "&repayType=" + repayType + "&loanMoney=" + sendMoney + "&planId=" + planId + "&rateBase=" + rateBase + "&internalSign=N2";

    if(bt.navtab){
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

/**显示计息日和预提日*/
function shouExpiryAccrued(){
    var interestWay = dicts.get("interestWay").val();//计息方式
    if(isEmpty(interestWay)){
        $("#showExpiryDate").html("");
        $("#showAccruedDailyDate").html("");
    }else{
        if(interestWay != "5"){
            $("#showExpiryDate").html(bt.lang('table.cmsProvideLoanSend.expiryDate') + '<div class="bt-biz-dict" data-name="expiryDate" data-type="select" data-biz-name="expiryDate" data-group="bisGatherPolicy.month" data-callback="setRateEntriesEffectDate" data-defaultvalue="21" data-width="100px"></div>');
        }else{
            $("#showExpiryDate").html("");
        }
        if(interestWay == "1" || interestWay == "4"){
            $("#showAccruedDailyDate").html(bt.lang('table.cmsProvideLoanSend.accruedDailyDate') + '<div class="bt-biz-dict" data-name="accruedDailyDate" data-type="select" data-biz-name="accruedDailyDate" data-group="bisGatherPolicy.month" data-width="100px" data-defaultvalue="21"></div>');
        }else{
            $("#showAccruedDailyDate").html("");
        }
    }
    bt.dicts.render();
}


/**影响还款计划的参数发生改变时，则改变状态*/
function checkRepayLanPrameter(){
    $("#planStatus").val("1");
    // 重新计算利息
    calInterest();
}


/**
 * 计算利息
 */
function calInterest(){
    var money = moneyType.get("sendMoney").val();//借款金额
    var rate = rateType.get("rate").val();//借款利率
    var anticInterest = computeInterest(money,rate);//预计利息
    moneyType.get("interest").val(anticInterest);
}

//获取利率
function getRate(){
    var data = selectType.get("repayCycle").getData().obj;
    if(isEmpty(data.rate)){
        parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
        return ;
    }
    var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyType.get("pointsProportionText").val();//浮动点数或比例值
    var floatWay = dicts.get("floatWay").val();
    if(pointsProportionText != "" && floatWay != "01"){//非固定利率
        if(pointsProportionSelect=="01"){//点数
            //rateTypes.get("plRate").val(data.rate+(pointsProportionText/100));//使用百分比  不使用万分比
            //基础利率+浮动的点数*0.01%
            dateType.get("rate").val(data.rate);//发放利率
            $("#benchmarkRate").val(data.rate);//基准利率
            setInterestFee();//自动计算综合利率
            return;
        }else{
            //比例 基准利率+浮动的比例*基准利率
            //rateTypes.get("plRate").val((data.rate)*(1+(pointsProportionText/100)));
            rateType.get("rate").val(data.rate);//发放利率
            $("#benchmarkRate").val(data.rate);//基准利率
            setInterestFee();//自动计算综合利率
            return;
        }
    }

    if(isEmpty(data.rate)){
        rateType.get("rate").val(0);
    }else{
        rateType.get("rate").val(data.rate);
    }
    setInterestFee();//自动计算综合利率
    checkRepayLanPrameter();
}

/**根据利率自动计算利息与费用并求和设置综合成本年华利率*/
function setInterestFee(){
    var money = moneyType.get("money").val();//借款金额
    var rate = rateType.get("rate").val();//借款利率
    var anticInterest = computeInterest(money,rate);//预计利息
    moneyType.get("interest").val(anticInterest);
    checkRepayLanPrameter();
}

/**
 * 利息计算
 * @param money
 * @param plRate
 * @returns {number|*}
 */
function computeInterest(money,rate){
    if(isEmpty(money) || money == 0 || isEmpty(rate) || rate == 0){
        return 0;
    }
    var term;//贷款期限（日）
    var startDate = $.trim(dateType.get("sendDate").val());//借款日期
    var endDate = $.trim(dateType.get("endDate").val());//还款日期
    if(isEmpty(startDate) || isEmpty(endDate)){
        var unit = $.trim(dicts.get("unit").val());
        term = $("#term").val();
        if(isNotEmpty(unit) || unit == "M"){
            term = term * 30;
        }
    }else{
        term = dataSubtract(startDate,endDate,"D");
    }
    //plTerm = dataSubtract(plDate,repayDate,"D");
    var rateBase = dicts.get("rateBase").val();//利息测算基数
    return moneyfun.format((money * rate * term) / (rateBase),{separator: "",decimal: 2});
}


/**
 * 账号选择
 * @param obj
 * @returns {boolean}
 */
function selectAcc(obj) {
    var url;
    var accId;
    var flag = $(obj).data("acc");
    if (flag == "borrowOutAcc") {
        accId = $("#borrowInAccId").val();
    }else{
        accId = $("#borrowOutAccId").val();
    }
    if (isNotEmpty(accId)) {
        url = "/inl/info/selectAcc";
    } else {
        url = "/inl/info/selectAcc2";
    }
    var borrowInCorp = $("#borrowInCorp").val();
    var borrowOutCorp = $("#borrowOutCorp").val();
    if (isEmpty(borrowInCorp) || isEmpty(borrowOutCorp)) {
        layer.alert(bt.lang("请选择拆入，拆出单位"), {icon: 2});
        return false;
    }
    var curId = selectType.get("curId").val();
    if (isEmpty(curId)) {
        layer.alert(bt.lang("请选择币种"), {icon: 2});
        return false;
    }
    url += "?&borrowInCorp=" + borrowInCorp + "&borrowOutCorp=" + borrowOutCorp + "&accId=" + accId + "&curId=" + curId + "&flag=" + flag;
    parent.layer.open({
        title: "选择账号",
        type: 2,
        area: ["50%", "70%"],
        btn: [bt.lang("common.button.confirm"), bt.lang("common.button.close")],
        btnAlign: "c",
        content: basePath + url,
        yes: function (index, layero) {
            //按钮【按钮一】的回调
            var iframeObj = layero.find("iframe")[0];
            var selectData = iframeObj.contentWindow.getSelectDate();
            if (null == selectData) {
                parent.layer.alert(bt.lang("请选择数据"), {icon: 2});
                return;
            }
            /**
             * 业务逻辑处理
             * selectData为选中返回数据
             *
             */
            doBusiness(obj, selectData[0]);

            parent.layer.close(index);
        },
        btn2: function (index, layero) {
            parent.layer.close(index);
        }
    });
}

/**
 * 业务数据设置
 * @param obj
 * @param data
 */
function doBusiness(obj, data) {
    var acc = null;
    if (isNotEmpty(data.bankAcc)) {
        acc = data.bankAcc;
    }else {
        acc = data.blnctrAcc;
    }
    if ($(obj).data("acc") == "borrowOutAcc") {
        $("#borrowOutAccId").val(data.id);
        $("#borrowOutAcc").val(acc);
    } else {
        $("#borrowInAccId").val(data.id);
        $("#borrowInAcc").val(acc);
    }
}


/**
 * 资金计划挂接
 */
function renderBudget(){
    //判断支付结算是否挂接资金计划
    var isBudget = $("#isBudget").val();
    if(isBudget == "1"){
        var inlInfoItem = $("#inlInfoItem").val();
        // 跳过发放时才允许挂接资金计划
        if (inlInfoItem != null && inlInfoItem != "") {
            genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
}

/**
 * 生成公共的属性
 * @return
 */
function genOtherHtml(){
    var itemCode = $("#dfInfoItem").val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateType.get("sendDate").val();
    var corpId = $("#borrowInCorp").val();
    var curId = selectType.get("curId").val();//币别
    var param = {};
    var url = "";
    if (isEmpty($("#id").val())) {
        url = "/budget/fbsBudgetProcess/getItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    } else {
        url = "/budget/fbsBudgetProcess/getEditItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            billNo: $("#sendBillNo").val(),
            billId: $("#id").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    }
    $.ajax({
        url:basePath + url,
        method: 'POST',
        data: param,
        async:true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            bt.form().render();
        }
    });
}


/**
 * 设置授信参数
 * @returns {{beginDate, corpId: *, ratItemCode, curCode: *}}
 */
function beforeCpRation(){
    var borrowInCorp = $("#borrowInCorp").val();
    var curId = selectType.get("curId").val();
    var cpRatItemCode = $("#cpRatItemCode").val();
    var startDate = $("#startDate").val();
    var params = {
        corpId: borrowInCorp,
        curCode: curId,
        ratItemCode: cpRatItemCode,
        beginDate: startDate
    };
    return params;
}



/**计算data1 与 data2 相差多少param单位 data为字符串*/
function dataSubtract(data1,data2,param){
    var dates;
    if(param == "D"){
        dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime()))/(1000*60*60*24);
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
