var dateTypes,dateFun,moneyTypes,selectTypes,dicts,rateTypes,parameters,form,ratioType,natualCurId,bondCurCorp="";
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','rateType','btUpload','bondAccInfo','externalCorpBankAcc','ratioType'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    externalCorpBankAcc = bt.externalCorpBankAcc.init();//客商
    dateTypes = bt.dateType.init();//时间
    dateFun = bt.dateType.getFun();
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    moneyFun=bt.moneyType.getFun();
    bondAccInit=bt.bondAccInfo.init();//保证金账号初始化
    ratioType= bt.ratioType.init();
    parameters = {};

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    //编辑页面显示或隐藏保证金额度单信息
    var editFlag=$('#editFlag').val();
    if(!bt.empty(editFlag)&&editFlag=="1"){
              var isHookBond = $('#hasBond').val();
            if(!bt.empty(isHookBond)){
                if(isHookBond=="1"){//挂接保证金
                    $(".bondInfo").show();
                    $("#bondBankAcc").attr('lay-verify','required')
                }else if (isHookBond=="0"){//不挂保证金
                    $("#bondBankAcc").removeAttr('lay-verify');
                    moneyTypes.get("bailmoney").removeAttr("lay-verify");
                    rateTypes.get("bailmoneyRate").attr('lay-verify',false);
                    // selectTypes.get("bondCurId").attr('lay-verify',false);
                    $('[data-name="bondCurId"]').attr('lay-verify',false);
                    $(".bondInfo").hide();
                }
            }else{//录入未开启参数，编辑时开启参数，默认挂接保证金
                $(".bondInfo").show();
                $("#bondBankAcc").attr('lay-verify','required')
            }

            // setBondCur();
    }


    //监听提交
    form.on('submit', function(data){
        // 如果担保是禁用则打开担保提交数据
        // dicts.get("isHangGuarantee").attr({"data-disabled": false});
        $("[data-biz-name='isHangGuarantee']").attr({"data-disabled": false});

        var rateData = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
        if(bt.empty(rateData.rate)){
            parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
            return false;
        }

        var isHookBond=bt.dicts.init().get("isHookBond").val();
        if(isHookBond=="1"){
            // 判断保证金的金额是否大于当前保证金账户的可用余额
            var bailmoney = moneyTypes.get("bailmoney").val();//保证金金额(就是项目的占用金额)
            if(Number(bailmoney)==Number(0)){//保证金金额不能等于0
                layer.alert(bt.lang("fcbond.check.bailmoneyIsNull"),{icon:2});
                return false;
            }
            var accAvailMoney=moneyFun.toNumber($("#accAvailMoney").val());//当前保证金账户的可用余额
            var oriBailmoney;
            var editFlag=$("#editFlag").val();
            if(editFlag=="1"){//修改数据,保证金余额需加上修改之前占用的金额
                oriBailmoney=bt.money.format($("#oriBailmoney").val());
                accAvailMoney=bt.money.add(oriBailmoney,accAvailMoney);
            }
            var leftMoney=bt.money.sub(accAvailMoney,bailmoney);
            if(leftMoney<0){//如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
                bt.loading.hide(); //关闭加载中
                layer.alert(bt.lang("fcbond.check.applyMoney"),{icon:2});
                return false;
            }
        }
        data.field.oriBailmoney=data.field.bailmoney;
        data.field.guaRmk=data.field.guaranteeRmk;
        var action = "/cms/lease/financing/info/tempOrSubmit"
        var nm = $(this).attr("id");

        var flag=$("#flag").val();
       if(flag=="repair"){
           action = "/cms/lease/financing/info/tempOrSubmitRepair"
       }
        if(nm == "submit"){
            data.field.saveType = "1";
        }
        data.field.uploadId = up.getGroupId();
        bt.loading.show();
        var corpData = corp.get('corpId').data()[0];
        var curData = selectTypes.get("oriCurid").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
        data.field.corpName = corpData.corpName
        data.field.oriCurName = curData.curname
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
                        bt.navtab.refreshCurrent();
                    }

                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });



    //获取利率
    $("#getRate").click(function(){
        getRate();
    });


    //是否折合人民币
    form.on('checkbox',function(data){
        var nm = $(this).attr("name");
        //顾问费利率、第三方收益利率、其他利率复选框 监听
        if(nm == "isRmbRat"){
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
    setTimeout(initFcBond, 1500);
});


function changeCur(){
    // 贷款利率设置
    loadLoanRate();
// 币种利率设置
    changeCurRate();
}

/**币别改变事件*/
function chengCur(data,elem,bv){
    //根据币别查询保证金账户
    $("#oriCurid").val(data.id);
    changeCur(data,elem,bv);
     setBondCurList();
    //挂抵质押物时重选币别要重置抵质押物信息
    resetPledgeMsg();
    //转为人名币
    conver();

}


function initChengCur(data,elem,bv){

    // 设置利率
    loadLoanRate();
    // 设置是否本位币
    showIsRmbRat();

    //设置保证金可选币别
        setBondCurList();

}

//设置保证金可选币别
function setBondCurList() {
    //初始化保证金可选币别
    var data= selectTypes.get("oriCurid").getData().obj;
    var billCurId;
    var bondCurId;
    var editFlag=$('#editFlag').val();
    if(!bt.empty(editFlag)&&editFlag=="1"){//编辑页面
        bondCurId=$("#bondCurId").val();
        if(bt.empty(bondCurId)){
            bondCurId=data.id;//保证金币别为空时（之前未开启保证金参数的情况下）,默认与业务币别一致
        }
        billCurId=data.id;
    }else{//录入页面，初始化借款币别为空值
        if(!bt.empty(data)){
            bondCurId=data.id;
            billCurId=data.id;
        }else{
            bondCurId="";
            billCurId="";
        }
    }
    if(!bt.empty(billCurId)&&!bt.empty(bondCurId)){//保证金币别受业务币别变化影响，业务币别未加载完成时不加载保证金币别
        natualCurId = bt.empty(natualCurId) ? $("#natualCurId").val() : natualCurId;
        if(natualCurId==billCurId){
            selectTypes.get("bondCurId").attr({"data-url": basePath+"/basic/business/btcurrency/json","data-value":bondCurId, "data-displayData": billCurId})
        }else{
            selectTypes.get("bondCurId").attr({"data-url": basePath+"/basic/business/btcurrency/json","data-value":bondCurId, "data-displayData": natualCurId + "," + billCurId})
        }
    }
}

/**加载基准利率和利率期限*/
function loadLoanRate(){
    var data = selectTypes.get("oriCurid").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    if(bt.empty(data.id)){
        selectTypes.get("referenceRateType").attr("data-url","");
    }else if(data.naturalsign != 1){

        selectTypes.get("referenceRateType").attr({
            "data-url":basePath+"/cms/loanrate/maintence/queryNotBaseRate",
            "data-defaultvalue":"first"});

    }else{//本位币
        selectTypes.get("referenceRateType").attr("data-url",basePath+"/cms/loanrate/maintence/queryBaseRate");
    }
}

/**初始化基准利率**/
function initLoanRepayCycle(){
    loanRepayCycle();
}

/**基准利率加载完成后 加载利率周期*/
function loanRepayCycle(flag){
    var data = selectTypes.get("oriCurid").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    natualCurId = bt.empty(natualCurId) ? $("#natualCurId").val() : natualCurId;
    beginDate = $.trim(bt.dateType.init().get("beginDate").val());//借款申请日期
    if(bt.empty(beginDate)){
        beginDate = dateFun.format(new Date(), 'yyyy-MM-dd');//为空默认当前日期
    }

    var pointsProportionText = $("#proportionText").val();
    if(pointsProportionText==""){
        pointsProportionText = 0;
    }

    var sel = dicts.get("pointsProportionSelect").val();
    if(!bt.empty(natualCurId) && data.id != natualCurId){//外币
        dicts.get("pointsProportionSelect").val("01");
        $("#inputMark").text("BP");
        $("#numUnit").removeClass("input-percent");
        $("#numUnit").addClass("input-bp");
        if(sel != "01"){
            bt.moneyType.init().get("pointsProportionText").val(pointsProportionText);
            bt.moneyType.init().get("pointsProportionText").reset();
        }
    }
    if(!bt.empty(natualCurId) && data.id == natualCurId){//本位币
        var rateType = selectTypes.get("referenceRateType").getData();
        if(!bt.empty(rateType)){
            var code = rateType.obj.code;
            if(code=="lpr"){
                $("#numUnit").removeClass("input-percent");
                $("#numUnit").addClass("input-bp");
                dicts.get("pointsProportionSelect").val("01");
                $("#inputMark").text("BP");
            }else {
                $("#numUnit").removeClass("input-bp");
                $("#numUnit").addClass("input-percent");
                dicts.get("pointsProportionSelect").val("02");
                $("#inputMark").text("%");
            }
        }

        if(sel != "02"){
            bt.moneyType.init().get("pointsProportionText").val(pointsProportionText);
            bt.moneyType.init().get("pointsProportionText").reset();
        }
    }

    var referenceRateType = selectTypes.get("referenceRateType").val();//基准利率类型
    var curId = data.id;//币别


    if(bt.empty(data.id)){
        selectTypes.get("repayCycle").attr("data-url","");
    }else{//本位币
        if (bt.empty(flag)) {
            selectTypes.get("repayCycle").attr(
                {
                    "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + beginDate + "&curId=" + curId + "&baseId=" + referenceRateType
                }
            );
        } else {
            if (typeof flag === "boolean") {
                selectTypes.get("repayCycle").attr(
                    {
                        "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + beginDate + "&curId=" + curId + "&baseId=" + referenceRateType,
                        "data-required": flag
                    }
                );
            } else {
                selectTypes.get("repayCycle").attr(
                    {
                        "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + beginDate + "&curId=" + curId + "&baseId=" + referenceRateType
                    }
                );
            }
        }

    }
}

/**初始化周期**/
function initLoadrepayCycle(){
    loadrepayCycle();
}


/**加载浮动利率的周期信息*/
function loadrepayCycle(){
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
    //设置text的输入范围
    pointsProportionTextObj.attr("data-min",data.ratedownlimit);
    pointsProportionTextObj.attr("data-max",data.rateuplimit);
    // pointsProportionTextObj.reset();
    readonlyText();
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

/**根据主业务担保类型查询担保方式*/
function queryPlType(){
    var mainPlTypeVal = selectTypes.get("mainPlType").val();
    var type = $.trim(mainPlTypeVal);
    var pl = '';
    if(!bt.empty(type)){
        pl = "Cms.PlType."+type;
    }else{
        pl="Cms.PlType.B";
    }
    htl = '<div class="bt-biz-dict" data-name="plType" data-type="select" data-group="'+ pl +'" data-required="true"></div>';
    $("#plTypeTD").html(htl);
    bt.dicts.render()
}

/**根据租赁开始日期 - 还款日期 - 期限单位 计算借款期限*/
var beginDateCompare;
function computedayLimit1(){
    //转人民币
    conver();
    loanRepayCycle();//加载日期后刷新基准利率取值
    changeCurRate();
    computedayLimit();
    seteffectiveDate();//日期变动之后修改浮动生效日期

}
function computedayLimit(){
    //挂抵质押物时重选日期要重置抵质押物信息
    resetPledgeMsg();
    var beginDate = $.trim(dateTypes.get("beginDate").val());
    var endDate = $.trim(dateTypes.get("endDate").val());
    var dayLimit;
    var month;
    if(bt.empty(beginDate) || bt.empty(endDate)){
        dayLimit = "";
    }else{
        var ternUnit = $.trim(dicts.get("ternUnit").val());
        var month = dataSubtract(beginDate,endDate,"M");
        if(!bt.empty(ternUnit)){
            dayLimit = dataSubtract(beginDate,endDate,ternUnit);
            if(ternUnit == "D"){
                //dayLimit += 1;
            }
        }else{
            //为空或者null默认月处理
            dicts.get("ternUnit").val("M");
            dayLimit = dataSubtract(beginDate,endDate,"M");
        }
    }
    moneyTypes.get("dayLimit").val(dayLimit);//设置借款期限
    setContractTerm(month);//设置合同期限


    beginDateCompare = beginDate;


    //挂抵质押物时重选日期要重置抵质押物信息
    resetPledgeMsg();
}

/**设置合同期限*/
function setContractTermBydayLimit(){
    var dayLimit = moneyTypes.get("dayLimit").val();
    setContractTerm(dayLimit);
}

function setContractTerm(dayLimit){
    var term = "01"
    if(dayLimit < 13){
        term = "01";//短期
    }else if(13 <= dayLimit && dayLimit <= 60){
        term = "02";//中期
    }else if(dayLimit > 60){
        term = "03";//长期期
    }
    dicts.get("contractTerm").val(term);
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

function mGetDate(year, month){
    var d = new Date(year, month + 1, 0);
    return d.getDate();
}

/**
 * 设置浮动点数
 */
function setFloatPoints(){
    var floatWay = dicts.get("floatWay").val();
    if(floatWay == "01"){//固定利率
        $("input[data-id='pointsProportionText']").attr("readonly", "readonly");
    } else {
        loadrepayCycle();
        $("input[data-id='pointsProportionText']").removeAttr("readonly").removeAttr("unselectable");
    }
}

/**浮动方式选择月、季、半年、年生产对应的详情选择 用来计算利率生效日期*/
function setNextDetail(){
    setFloatPoints();
    var floatWay = dicts.get("floatWay").val();

    var rateData = $("#effectiveDate");//利率生效日期控制
    rateData.attr("readonly",true);
    rateData.attr("lay-verify","required");
    rateData.val("");

    var postfix = "";
    var flag = false;
    if(floatWay == "01"){//固定利率
        rateData.attr("lay-verify","");

        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        pointsProportionTextObj.val(0);
        pointsProportionTextObj.attr("data-min", 0);
        pointsProportionTextObj.attr("data-max", 100);

        // pointsProportionTextObj.reset();
        flag = true;
    }else if(floatWay == "02"){//即时浮动
        var beginDate = $.trim(dateTypes.get("beginDate").val());
        if(bt.empty(beginDate)){
            beginDate = dateFun.format(new Date(), 'yyyy-MM-dd');//为空默认当前日期
        }
        rateData.attr("readonly",false);
        rateData.val(beginDate);
    }else if(floatWay == "03"){//按月浮动
        postfix = "Month"
    }else if(floatWay == "04"){//按季浮动
        postfix = "Season"
    }else if(floatWay == "05"){//按半年浮动
        postfix = "HY"
    }else if(floatWay == "06"){//按年浮动
        postfix = "Year"
    }
    if(postfix == ""){
        $("#nextDetail").html("");//删除页面元素
    }else{
        var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext'+ postfix +'" data-callback="seteffectiveDate" data-group="Cms.FloatWay'+
            postfix +'" data-required="true"></div>';
        $("#nextDetail").html(htl);
        bt.dicts.render();
    }
    //disabledLoanRate(flag);//暂时不需要控制固定利率不能选择浮动方式
    dateTypes.get("effectiveDate").reset();
    loanRepayCycle(flag);
}

/**禁用基准利率类型与周期等*/
function disabledLoanRate(flag){
    selectTypes.get("referenceRateType").val({"disabled":flag});
    selectTypes.get("repayCycle").val({"disabled":flag});
    dicts.get("pointsProportionSelect").val({"disabled":flag});
    moneyTypes.get("#pointsProportionText").val({"disabled":flag});
    if(!flag){
        $("#getRate").show();
    }else{
        moneyTypes.get("pointsProportionText").val(0);
        $("#getRate").hide();
    }
    selectTypes.get("repayCycle").render();
    selectTypes.get("referenceRateType").render();
    dicts.get("pointsProportionSelect").render();
}

/**根据浮动方式详情设置利率调整生效日期*/
function seteffectiveDate(){
    var floatWay = dicts.get("floatWay").val();
    if(floatWay == "02"){//即时浮动
        var beginDate = $.trim(dateTypes.get("beginDate").val());
        if(bt.empty(beginDate)){
            beginDate = dateFun.format(new Date(), 'yyyy-MM-dd');//为空默认当前日期
        }
        $("#effectiveDate").val(beginDate);

    }else if(floatWay == "03"){//按月浮动
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
    var beginDate = $.trim(dateTypes.get("beginDate").val());
    if(bt.empty(beginDate)){
        beginDate = dateFun.format(new Date(), 'yyyy-MM-dd');//为空默认当前日期
    }

    var floatWayNext = dicts.get("floatWayNext"+postfix).val();
    var date = getFloatDate(beginDate, null, floatWayNext, true, count);
    $("#effectiveDate").val(date);
}



//获取利率
function getRate(){
    var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据

    if(bt.empty(data.rate)){
        parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
        return ;
    }

    var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyTypes.get("pointsProportionText").val();//浮动点数或比例值
    var floatWay = dicts.get("floatWay").val();
    if(pointsProportionText != "" && floatWay != "01"){//非固定利率
        if(pointsProportionSelect=="01"){//点数
            //rateTypes.get("rate").val(data.rate+(pointsProportionText/100));//使用百分比  不使用万分比
            //基础利率+浮动的点数*0.01%
            rateTypes.get("rate").val(moneyFun.format(data.rate + pointsProportionText * (0.01 / 100), {decimal: 4}));

            return;
        }else{
            //比例 基准利率+浮动的比例*基准利率
            //rateTypes.get("rate").val((data.rate)*(1+(pointsProportionText/100)));
            rateTypes.get("rate").val(moneyFun.format(data.rate + (pointsProportionText / 100) * data.rate, {decimal: 4}));
            return;
        }
    }

    if(bt.empty(data.rate)){
        rateTypes.get("rate").val(0);
    }else{
        rateTypes.get("rate").val(moneyFun.format(data.rate, {decimal: 4}));
    }



}


function setInterestFee1(data , elem){
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("oriMoney").val();//租赁金额
    var consultantFee =  moneyFun.mul(plMoney,data);//预计费用
    moneyTypes.get("costTypeDtl["+rowIndex+"]advanceFees").val(consultantFee);
    //summation();
}

function setInterestFee2(){
    var plMoney = moneyTypes.get("oriMoney").val();//租赁金额
    var thirdPartyCb = $("[name='thirdPartyCb']:checked").val();//第三方checkBox
    var thirdPartyProfitRate = rateTypes.get("thirdPartyProfitRate").val();//第三方利率
    if(thirdPartyCb == 1){
        var thirdPartyProfit = computeInterest(plMoney,thirdPartyProfitRate);//第三方收费
        moneyTypes.get("thirdPartyProfit").val(thirdPartyProfit);
        /*countRate = fun.add(countRate,thirdPartyProfitRate);
        countMoney = fun.add(countMoney,thirdPartyProfit);*/
    }
    //summation();
}

function setInterestFee3(){
    var plMoney = moneyTypes.get("oriMoney").val();//租赁金额
    var otherFeeCb = $("[name='otherFeeCb']:checked").val();//其他checkBox
    var otherFeeRate = rateTypes.get("otherFeeRate").val();//其他利率
    if(otherFeeCb == 1){
        var otherFee = computeInterest(plMoney,otherFeeRate);//其他费用
        moneyTypes.get("otherFee").val(otherFee);
        /*countRate = fun.add(countRate,otherFeeRate);
        countMoney = fun.add(countMoney,otherFee);*/
    }
    //summation();
}

function setInterestFee4(data , elem){
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("oriMoney").val();//租赁金额
    var rate = data / plMoney;//顾问费
    rateTypes.get("costTypeDtl["+rowIndex+"]rate").val(rate);
}

//借款利率
function setInterestFee5(data , elem){
    var plMoney = moneyTypes.get("oriMoney").val();//租赁金额
    var rateBase = $("[name='rateBase']:checked").val();//利息测算基数
    var beginDate = $.trim(dateTypes.get("beginDate").val());//租赁开始日期
    var endDate = $.trim(dateTypes.get("endDate").val());//还款日期
    var rate = 0;
    if(beginDate && endDate){
        //借款利率=输入的预计利息/（租赁金额*期限/利息测算基数）
        var days = dataSubtract(beginDate,endDate,"D");
        rate = data / (plMoney * days / rateBase);
    }
    rateTypes.get("rate").val(moneyFun.format(rate,{decimal: 4}));
}




function computeInterest(money,rate){
    if(bt.empty(money) || money == 0 || bt.empty(rate) || rate == 0){
        return 0;
    }
    var dayLimit;//贷款期限（日）
    var beginDate = $.trim(dateTypes.get("beginDate").val());//租赁开始日期
    var endDate = $.trim(dateTypes.get("endDate").val());//还款日期
    if(bt.empty(beginDate) || bt.empty(endDate)){
        var ternUnit = $.trim(dicts.get("ternUnit").val());
        dayLimit = moneyTypes.get("dayLimit").val();
        if(bt.empty(ternUnit) || ternUnit == "M"){
            dayLimit = dayLimit * 30;
        }
    }else{
        dayLimit = dataSubtract(beginDate,endDate,"D");
    }
    //dayLimit = dataSubtract(beginDate,endDate,"D");
    var rateBase = $("[name='rateBase']:checked").val();//利息测算基数
    return fun.format((money * rate * dayLimit) / (rateBase),{separator: "",decimal: 2});
}

/**显示本位币*/
function showIsRmbRat(){
    var data = selectTypes.get("oriCurid").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
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

function changeCurRate(){
    // 本位币勾选设置计算
    showIsRmbRat();
    //清空保证金信息
        clearFcBondMsg();
        // 保证金币别设置
        // setBondCur();

}

//设置币别汇率
function setCurRat(){
    var ratModel = bt.dicts.init().get("ratmodel").val();
    if(bt.empty(ratModel) || "0" == ratModel) {
        return;
    }
    var loanDate = bt.dateType.init().get("beginDate").val();//租赁开始日期
    if(bt.empty(loanDate)){
        return ;
    }

    var data = selectTypes.get("oriCurid").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    if("0" == isRmbRat) {//不显示外币汇率
        $("#exchangerateTd1").hide();
        $("#exchangerateTd2").hide();
        $("#isRmbRat").val("0");
        moneyTypes.get("exchangerate").val("");
    } else {
        $("#exchangerateTd1").show();
        $("#exchangerateTd2").show();
        $("#isRmbRat").val("1");
        if(data.id==natualCurId){//当前选中的币别是当前单位的本位币
            moneyTypes.get("exchangerate").val("1");
        } else {
            if (!bt.empty(natualCurId) && !bt.empty(data.id)) {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId : data.id,
                    convertCurId: natualCurId,
                    beginDate: loanDate
                }, function(retData) {
                    if (retData.success) {
                        moneyTypes.get("exchangerate").val(retData.obj);
                    }else{
                        selectTypes.get("oriCurid").val("");
                        moneyTypes.get("exchangerate").val("");
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }

        }
    }
}

/**设置保证金比例*/
function setBailMoneyRate(){

        var plMoney = moneyTypes.get("oriMoney").val();
        var bailmoney = moneyTypes.get("bailmoney").val();

        if(bt.empty(plMoney)){
            return ;
        }
        if(!bt.empty(bailmoney)){
            var curId = selectTypes.get("oriCurid").val();
            var bondCurId = selectTypes.get("bondCurId").val();
            if (curId == bondCurId) {
                // 修改为保证金金额可以大于租赁金额
                rateTypes.get("bailmoneyRate").val(fun.div(bailmoney, plMoney, 4));
            } else {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId : curId,
                    convertCurId: bondCurId,
                    beginDate: bt.dateType.init().get("beginDate").val()//租赁开始日期
                }, function(retData) {
                    if (retData.success) {
                        var convertMoney = fun.div(bailmoney, retData.obj, 4);
                        // 修改为保证金金额可以大于租赁金额
                        rateTypes.get("bailmoneyRate").val(fun.div(convertMoney, plMoney, 4));
                    }else{
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }
        }


}
/**设置保证金*/
function setBailMoney(){
        conver();
        var plMoney = moneyTypes.get("oriMoney").val();
        var bailmoneyRate = rateTypes.get("bailmoneyRate").val();
        if(bt.empty(plMoney)){
            return ;
        }
        if(!bt.empty(bailmoneyRate) && bailmoneyRate !=0){
            var billCur = selectTypes.get("oriCurid").val();
            var bondCur = selectTypes.get("bondCurId").val();
            var loanDate = bt.dateType.init().get("beginDate").val();//租赁开始日期
            if (billCur == bondCur) {
                moneyTypes.get("bailmoney").val(fun.format(fun.mul(plMoney,bailmoneyRate),{decimal: 2}));
            } else {
                $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                    curId : billCur,
                    convertCurId: bondCur,
                    beginDate: loanDate
                }, function(retData) {
                    if (retData.success) {
                        moneyTypes.get("bailmoney").val(fun.format(fun.mul(plMoney*retData.obj,bailmoneyRate),{decimal: 2}));
                    }else{
                        moneyTypes.get("bailmoney").val("");
                        parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                    }
                }, 'JSON');
            }
        }

}

//网点下拉框回调
function corpChange(data){
    // var url = basePath + "/cms/provide/loaninfo/getPurpose?corpId="+data.id;
    // bt.selectType.init().get("plPurpose").attr('data-url',url);

    natualCurId = data.curId;

    //清空保证金信息
        clearFcBondMsg();
        bondCurCorp=data.id;//用于传参，便于保证金币别查询本位币
        var billCur=selectTypes.get("oriCurid").getData().obj;
        if(!bt.empty(billCur)){//因保证金币别受业务币别影响，业务币别为空时不加载保证金币别,业务币别不为空时重新加载保证金币别
            setBondCurList();//重新设置保证金币别，本位币受单位影响
        }

    //挂抵质押物时重选单位要重置抵质押物信息
    resetPledgeMsg();
}



//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data,dom){
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondMoney").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bondBankAcc").val(data.bankAcc);//保证金账户
    $("#bondBankId").val(data.bankCode);
    selectTypes.get("bondCurId").val(data.curId);//保证金币别
    rateTypes.get("bondRate").val(data.bondRate);
    $("#oriBailmoney").val("");
    if (data.bondId != $("#bondIdPre").val()) {
        $("#oriBailmoney").val("");
    }
    // 设置保证金金额y
    setBailMoney();

}


//如果选择了否,清空填写的保证金占用信息
function changeFcBond(){
    var isHookBond = dicts.get("isHookBond").val();
    if(isHookBond=="0"){
        $("#bondBankAcc").removeAttr('lay-verify');
        rateTypes.get("bailmoneyRate").attr('lay-verify',false);
        moneyTypes.get("bailmoney").removeAttr("lay-verify");
        selectTypes.get("bondCurId").attr('lay-verify',false);
        $(".bondInfo").hide();
        clearFcBondMsg();
    }else{
        $(".bondInfo").show();
        $("#bondBankAcc").attr('lay-verify','required');
        rateTypes.get("bailmoneyRate").attr('lay-verify','required');
        moneyTypes.get("bailmoney").attr('lay-verify','required');
        selectTypes.get("bondCurId").attr('lay-verify',true);
    }
    form.renderIcon();
}
function initFcBond(){
    var isHookBond = dicts.get("isHookBond").val();
    if(isHookBond=="0"){
        $("#bondBankAcc").removeAttr('lay-verify');
        rateTypes.get("bailmoneyRate").attr('lay-verify',false);
        moneyTypes.get("bailmoney").removeAttr("lay-verify");
        selectTypes.get("bondCurId").attr('lay-verify',false);
        $(".bondInfo").hide();

    }else{
        $(".bondInfo").show();
        $("#bondBankAcc").attr('lay-verify','required');
        rateTypes.get("bailmoneyRate").attr('lay-verify','required');
        moneyTypes.get("bailmoney").attr('lay-verify','required');
        selectTypes.get("bondCurId").attr('lay-verify',true);
    }
    form.renderIcon();
}

//清空保证金信息
function clearFcBondMsg(){
    $("#bondBankAcc").val("");
    // selectTypes.get("bondCurId").val("");
    moneyTypes.get("bailmoney").val("");
    rateTypes.get("bailmoneyRate").val("");
}


//提示未选择单位
function checkCorp(){
    var corpId = bt.corp.init().get("corpId").val();
    var bondCurId = selectTypes.get("bondCurId").val();
    var startDate = dateTypes.get("beginDate").val();
    var endDate = dateTypes.get("endDate").val();
    if(bt.empty(corpId)){
        layer.alert(bt.lang("table.fcBond.corp.null"),{icon:2});
        return false;
    }
    if (bt.empty(bondCurId)) {
        parent.layer.msg(bt.lang("common.bondCurId.alert.info1"), {icon: 2});
        return false
    }
    if(bt.empty(startDate)){
        parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
        return false
    }
    if(bt.empty(endDate)){
        parent.layer.msg(bt.lang("table.bisaccbal.msg.dtldata"), {icon: 2});
        return false
    }

    parameters.corpId=corpId;
    parameters.bankCode=$("#bankId").val();
    parameters.curId=selectTypes.get("bondCurId").val();
    parameters.startDate = startDate;
    parameters.endDate = endDate;
    bt.bondAccInfo.set(parameters);
    return true;

}

/**
 * 保证金币别设置
 * @param data
 */
function setBondCur(){
    var data = selectTypes.get("oriCurid").getData().obj;
    if(!bt.empty(data)){
        if(data.naturalSign == "1"){
            // 本位币
            selectTypes.get("bondCurId").attr({"data-value": data.id,"data-displayData":data.id})
        }else{
            // 外币
            $.ajax({
                url: basePath+"/basic/business/btcurrency/json",
                type: "get",
                dataType: "json",
                success: function (allCur) {
                    $.each(allCur,function(index,item){
                        if (item.naturalSign == "1") {
                            // 设置只能选业务相同外币和本位币
                            selectTypes.get("bondCurId").attr({"data-value": data.id,"data-displayData": item.id + "," + data.id})
                        }
                    })
                }
            });
        }
        selectTypes.get("bondCurId").val(data.id);
    }
}

// 币别发生变化时,则根据比例设置金额
function changeBondCur(){
    // 清空保证金信息
    $("#bondBankAcc").val("");
    moneyTypes.get("bailmoney").val("");
    rateTypes.get("bailmoneyRate").val("");
    // 设置保证金金额
    setBailMoney();
}

/**
 * 担保选择参数设置
 * @param param
 */
function setOtherParam(param){
    var viewFlag = $("#viewFlag").val();
    var corpId//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    var curId;//币别
    if(viewFlag=="1"){
        startDate = $("#beginDate").val();//开始日期
        endDate = $("#endDate").val() ;//结束日期
        corpId = $("#corpId").val() ;//单位
        curId = $("#oriCurid").val() ;//币种
    }else{
        startDate = $.trim(dateTypes.get("beginDate").val());//开始日期
        endDate = $.trim(dateTypes.get("endDate").val());//结束日期
        corpId = corp.get("corpId").val();//网点id
        curId = selectTypes.get("oriCurid").val();//币别
    }
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

/**
 * 担保选择前参数检查
 * @returns {boolean}
 */
function selectBefore(){
    var corpId = corp.get("corpId").val();
    if (bt.empty(corpId)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    }
    var curId = selectTypes.get("oriCurid").val();
    if (bt.empty(curId)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.curIdnull"), {icon: 2});
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
function resetCallback() {
    window.location.reload();
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
    var trHtml = '<tr data-row="'+row+'">';
    trHtml += '<td align="center"><div class="layui-inline"> <div class="bt-biz-selectType" data-biz-name="costTypeDtl['+row+']costCode" data-url="' + basePath + '/cms/dfCostType/getValidDfCostType"' +
        ' data-valuefield="id" data-textfield="costName" id="costCode'+row+'" data-name="costTypeDtl['+row+'].costTypeId" data-search="true" id="costCode'+row+'" ' +
        ' data-link="true" data-callback="setItemCode"></div></div></td>' ;
    trHtml += '<td align="center"><input type="text" id="rate'+row+'" name="costTypeDtl['+row+'].rate" data-name="costTypeDtl['+row+'].rate" class="layui-input bt-biz-rateType" data-biz-name="costTypeDtl['+row+']rate" data-callback="setInterestFee1" ></td>';
    trHtml +='<td align="center"><input type="text" id="advanceFees'+row+'" data-name="costTypeDtl['+row+'].advanceFees"  autocomplete="off" data-biz-name="costTypeDtl['+row+']advanceFees" data-callback="setInterestFee4" class="layui-input bt-biz-moneyType"></td>';

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

/**
 * 折合人民币
 */
function conver() {
    var curId = selectTypes.get("oriCurid").val();
    var moneysum = $("#oriMoney").val();
    var curPreTime = $("#beginDate").val();
    var rate = "";
    if (!bt.empty(curId) && !bt.empty(moneysum) && !bt.empty(curPreTime)) {
        bt.jquery.ajax({
            url: basePath + "/cms/lease/financing/info/conver/" + curId + "/" + curPreTime,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                rate = t;
                bt.moneyType.init().get("rmbMoney").val(moneyFun.mul(rate, moneysum, 2));
            },
        });
    }
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
        $("#costCode" + rowIndex ).attr("data-itemdisabled", disItem);
        form.render("select");
    }
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
/**
 * 客商筛选
 * @returns {boolean}
 */
bt.externalCorpBankAccParams = function () {
    //承租人默认不查
    externalCorpBankAcc.set({
        "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0"
    });
    return true;
}

/*出租信息设置*/
function externalCorpCallback(data,dom) {
    /*设置出租人*/
    // $("#lessor").val(data.name);
    if (typeof data.netId === "undefined") {
        $("#lessor").val(data.name);
    } else {
        $("#lessor").val(data.accName);
    }
}

