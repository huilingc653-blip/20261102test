var dateTypes,moneyTypes,selectTypes,dicts,rateTypes,parameters,form,ratioType;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','btUpload','rateType','ratioType', 'bondAccInfo'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    moneyFun=bt.moneyType.getFun();
    ratioType= bt.ratioType.init();
    //保证金账号初始化
    bondAccInit=bt.bondAccInfo.init();
    parameters = new Object();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    var gid = $("#attachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //监听提交
    form.on('submit', function(data){
        var isHookBond=bt.dicts.init().get("isHookBond").val();
        data.field.uploadId = up.getGroupId();
        if(isHookBond=="1"){
            //保证金金额(就是项目的占用金额)
            var bailmoney = moneyTypes.get("bailmoney").val();
            //保证金金额不能等于0
            if(Number(bailmoney)==Number(0)){
                layer.alert(bt.lang("fcbond.check.bailmoneyIsNull"),{icon:2});
                return false;
            }
            //当前保证金账户的可用余额
            var accAvailMoney=moneyFun.toNumber($("#accAvailMoney").val());
            var leftMoney=bt.money.sub(accAvailMoney,bailmoney);
            if(leftMoney<0){//如果保证金金额大于当前保证金账户的可用余额,那么应该提示用户
                bt.loading.hide(); //关闭加载中
                layer.alert(bt.lang("fcbond.check.applyMoney"),{icon:2});
                return false;
            }
        }

        data.field.planId=$('#planId').val();

        data.field.oriBailmoney=data.field.bailmoney;

        data.field.bondBankacc=data.field.bondBankAcc;


        data.field.attachmentId = up.getGroupId();
        data.field.status = $(this).val();
        var action = "/cms/security/issue/save"
        bt.loading.show();
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

    var curId = $("#curId").val()
    selectTypes.get("bondCurId").attr({"data-url": basePath+"/basic/business/btcurrency/json","data-value": curId, "data-displayData": curId, "data-disabled": true})
    setIsGuaranteeEnabled()
    initFcBond()
    // setTimeout(setIsGuaranteeEnabled, 500);
    // setTimeout(initFcBond, 500);
});



function resetCallback() {
    window.location.reload();
}

/**
 * 定价基准回调函数
 */
function loanRepayCycle() {
    var curId = $("#curId").val()
    var fixPriceBase = selectTypes.get("referenceRateType").val();
    selectTypes.get("repayCycle").attr(
        {
            "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + "&curId=" + curId + "&baseId=" + fixPriceBase
        }
    );
}

/**
 * 发行开始日期回调函数
 */
function issueStartDateCallback() {
    conver();
    getLimit();
}

function faceValueCallback(){
    var faceValue=moneyTypes.get("faceValue").val();
    moneyTypes.get("issuePrice").val(faceValue);
}

/**设置保证金比例*/
function setBailMoneyRate(){

    var plMoney = moneyTypes.get("thisIssueMoney").val();
    var bailmoney = moneyTypes.get("bailmoney").val();

    if(bt.empty(plMoney)){
        return ;
    }
    if(!bt.empty(bailmoney)){
        var curId = $("#curId").val();
        var bondCurId = selectTypes.get("bondCurId").val();
        if (curId == bondCurId) {
            // 修改为保证金金额可以大于发行金额
            rateTypes.get("bailmoneyRate").val(fun.div(bailmoney, plMoney, 4));
        } else {
            $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                curId : curId,
                convertCurId: bondCurId,
                beginDate: bt.dateType.init().get("issueDate").val()//发行开始日期
            }, function(retData) {
                if (retData.success) {
                    var convertMoney = fun.div(bailmoney, retData.obj, 4);
                    // 修改为保证金金额可以大于发行金额
                    rateTypes.get("bailmoneyRate").val(fun.div(convertMoney, plMoney, 4));
                }else{
                    parent.layer ? parent.layer.alert(retData.msg, {icon: retData.success ? 1 : 2}) : layer.alert(retData.msg, {icon: retData.success ? 1 : 2});
                }
            }, 'JSON');
        }
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

/**设置保证金*/
function setBailMoney(){
    $('#planId').val('')
    conver();
    var plMoney = moneyTypes.get("thisIssueMoney").val();
    var bailmoneyRate = rateTypes.get("bailmoneyRate").val();
    if(bt.empty(plMoney)){
        return ;
    }
    if(!bt.empty(bailmoneyRate) && bailmoneyRate !=0){
        var billCur = $("#curId").val();
        var bondCur = selectTypes.get("bondCurId").val();
        var startDate = bt.dateType.init().get("issueDate").val();//开始日期
        if (billCur == bondCur) {
            moneyTypes.get("bailmoney").val(fun.format(fun.mul(plMoney,bailmoneyRate),{decimal: 2}));
        } else {
            $.post(basePath + "/cms/provide/loaninfo/getNearCurRat", {
                curId : billCur,
                convertCurId: bondCur,
                beginDate: startDate
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

//获取期限
function getLimit(){
    var startdate = dateTypes.get("issueDate").val();
    var enddate = dateTypes.get("legalEndDate").val();
    if(!bt.empty(startdate) && !bt.empty(enddate)){
      var  dayLimit = dataSubtract(startdate,enddate,"M");
      var limit = moneyFun.format(dayLimit/12, {decimal:2})
        $("#timeLimit").val(limit);
        $("#timeLimit0").html(limit);
    }
}
/**
 * 折合人民币
 */
function conver() {
    var curId = $("#curId").val();
    var moneysum = $("#thisIssueMoney").val();
    var curPreTime = dateTypes.get("issueDate").val();
    var rate = "";
    if (!bt.empty(curId) && !bt.empty(moneysum) && !bt.empty(curPreTime)) {
        bt.jquery.ajax({
            url: basePath + "/cms/lease/financing/info/conver/" + curId + "/" + curPreTime,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                rate = t;
                issueRmbMoney=moneyFun.mul(rate, moneysum, 2);
                bt.moneyType.init().get("issueRmbMoney").val(issueRmbMoney);
                $("#issueRmbMoney0").html(moneyFun.format(issueRmbMoney, {decimal:2}));
            },
        });
    }
    if(bt.empty(moneysum)){
        bt.moneyType.init().get("issueRmbMoney").val("");
        $("#issueRmbMoney0").html("");
    }
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
    var corpId = $("#corpId").val();
    var bondCurId = $("#curId").val();
    var startDate = dateTypes.get("issueDate").val();
    var endDate = dateTypes.get("legalEndDate").val();
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

    parameters.corpId = corpId;
    parameters.bankCode=$("#bankId").val();
    parameters.curId= bondCurId;
    parameters.startDate = startDate;
    parameters.endDate = endDate;
    bt.bondAccInfo.set(parameters);
    return true;

}


//保证金账户选择弹出的表格点击选择后的回调函数，通过data返回了保证金账户相关的数据
function bondAccinfoCallback(data,dom){
    $("#bondId").val(data.bondId);//保证金Id
    $("#bondMoney").val(data.bondMoney);//保证金账户余额
    $("#accAvailMoney").val(bt.money.format(data.availMoney));//可用余额
    $("#bondBankAcc").val(data.bankAcc);//保证金账户

    selectTypes.get("bondCurId").val(data.curId);//保证金币别
    rateTypes.get("bondRate").val(data.bondRate);
    $("#oriBailMoney").val("");
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
        startDate = $("#issueDate").val();//开始日期
        endDate = $("#legalEndDate").val() ;//结束日期
        corpId = $("#corpId").val() ;//单位
        curId = $("#curId").val() ;//币种
    }else{
        startDate = $.trim(dateTypes.get("issueDate").val());//起息日
        endDate = $.trim(dateTypes.get("legalEndDate").val());//兑付日
        corpId = $("#corpId").val();//网点id
        curId = $("#curId").val();//币别
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
    var corpId = $("#corpId").val();
    if (bt.empty(corpId)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    }
    var curId = $("#curId").val();
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

function setInterestFee1(data , elem){
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("thisIssueMoney").val();//租赁金额
    var consultantFee =  moneyFun.mul(plMoney,data);//预计费用
    moneyTypes.get("costTypeDtl["+rowIndex+"]advanceFees").val(bt.money.format(bt.money.mul(plMoney,data,8),{decimal:4}));
    // moneyTypes.get("costTypeDtl["+rowIndex+"]advanceFees").val(consultantFee);
    //summation();
}

function setInterestFee4(data , elem){
    var rowIndex = elem.parents('tr').data('row');
    var plMoney = moneyTypes.get("thisIssueMoney").val();//租赁金额
    var rate = data / plMoney;//顾问费
    rateTypes.get("costTypeDtl["+rowIndex+"]rate").val(bt.money.format(bt.money.div(data, plMoney,8), {decimal:6}));
    // rateTypes.get("costTypeDtl["+rowIndex+"]rate").val(rate);
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
//加载资产层级信息
function checkLevel(){
    var securityInfoId=$("#securityInfoId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('资产分层信息'),
            basePath + "/cms/security/issue/checkLevel?infoId="+securityInfoId);
    }
}
//加载还款计划
function repayLan(){
    var securityIssueCode = $("#securityIssueCode").val();
    var issueMoney = $("#thisIssueMoney").val();
    var corpId = $("#corpId").val();
    if(bt.empty(securityIssueCode)){
        parent.layer.msg(bt.lang("发行编号不能为空"), {icon: 2});
        return false;
    }
    if(bt.empty(issueMoney)){
        parent.layer.msg(bt.lang("本次发行金额不能为空"), {icon: 2});
        return false;
    }

    var param = "&securityIssueCode=" + securityIssueCode
        + "&issueMoney=" + issueMoney+"&corpId="+corpId
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),
            basePath + "/cms/security/issue/addPlan?" + param);
    }
}

// function securityIssueCodeCallback() {
//     $('#planId').val('')
// }


function remoTr(rowIndex) {
    var tab = document.getElementById("dataTable");
    tab.deleteRow(rowIndex);
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
//选择资产层级信息填入
function setSelectData(levelName,productName,money) {
    $("#levelName").val(levelName);
    $("#levelName0").html(levelName);
    $("#productName").val(productName);
    $("#productName0").html(productName);
    moneyTypes.get("issueMoney").val(money);
    $("#issueMoney0").html(money);
    moneyTypes.get("thisIssueMoney").attr("data-max",moneyFun.toNumber(money));
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
