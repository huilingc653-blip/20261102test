var dateTypes, moneyTypes, selectTypes, dicts, rateTypes, parameters, form, ratioType,dateTypeFun;
bt.use(['form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType','selectTable', 'dicts', 'rateType', 'ratioType'], function () {
    $ = bt.jquery;
    form = bt.form(), fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    moneyFun = bt.moneyType.getFun();
    ratioType = bt.ratioType.init();
    dateTypeFun = bt.dateType.getFun();
    //监听提交
    form.on('submit', function (data) {
        var nm = $(this).attr("id");
        var saveType = "0";
        if (nm == "submit") {
            saveType = "1";
        }
        var action = "/cms/bond/extend/save?saveType=" + saveType;

        data.field.status = $(this).val();
        bt.loading.show();
        $.post(basePath + action, data.field, function (result) {
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });
    //债券发行编号
    var payerAccInit = bt.selectTable.init({
        elem: "#bondIssueNumTable",
        url: basePath+"/cms/bond/extend/listValidBondIssueNum/"+$("input[name='registerId']").val(),
        scrollX: true,
        scrollY:"400px",
        searchInput:"#bondIssueNum",
        paging:true,
        columns: [
            {data:"bondIssueNum"},
            {data:"issueMoney",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(data);
                }},
            {data:"rmbIssueTotal",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(data);
                }},
            {data:"issueStartDate"},
            {data:"issueEndDate"},
            {data:"issuePrice",className:"textRight",render: function ( data, type, full, meta ){
                    return data ? bt.money.format(data) : '';
                }},
            {data:"issueRmk"}
        ],
        clickCallback:function(data){
            if(data){
                $('#issueId').val(data.id);
                $('#bondIssueNum').val(data.bondIssueNum);
                moneyTypes.get("issueMoney").val(data.issueMoney);
                moneyTypes.get("outstandingAmount").val(data.unpaidMoney);

                $.ajax({
                    url: basePath+'/cms/bond/extend/getLastExtendDate/'+data.id,
                    method: 'GET',
                    async:false,
                    success: function (obj) {
                        var dateTime = new Date(obj.attributes.lastExtendDate);
                        dateTime = dateTime.setDate(dateTime.getDate() + 1);
                        dateTime = new Date(dateTime);
                        dateTypes.get("lastExtendDate").val(obj.attributes.lastExtendDate);
                        // dateTypes.get("extendEndDate").attr("data-min",bt.dataType.getFun().format(dateTime));
                        var min = dateTypes.getFun().format(dateTime);
                        dateTypes.get("endDate").attr("data-min",min);
                        dateTypes.render();
                        form.render();
                    }
                });
            }else{
                $('input.text').val('');
            }
        }
    });

});

/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}

//监听贷款期限单位
function setTerm(ele,data){
    var  startValue = dateTypes.get("startDate").val();
    var  lastExtendDate = $("#lastExtendDate").val();
    if (!isNull(lastExtendDate)){
        startValue = lastExtendDate;
    }
    var  endValue = dateTypes.get("endDate").val();
    var  startDate = new Date(startValue);
    var  endDate = new Date(endValue);
    var  planStartTime=$("#extendApplyDate").val();
    var  planEndTime=$("#extendEndDate").val();

    if(startValue !=="" && endValue !==""){
        if(startValue >= endValue){//结束日期应大于开始日期
            dateTypes.get($(data).attr("data-biz-name")).val("");
            layer.tips(bt.lang("cms.precative.startdateenddate.more"), '#extendEndDate');
            return false;
        }

        var ternUnit = dicts.get("deadlineUnit").val();
        // console.log(ternUnit)
        if(ternUnit == "M"){
            $("#extendDeadline").val(dataSubtract(startValue,endValue,"M"));
        }else if(ternUnit == "D"){
            $("#extendDeadline").val((endDate-startDate)/(1000*60*60*24));
        }else if(ternUnit == "Y"){
            $("#extendDeadline").val(Math.ceil((endDate-startDate)/(1000*60*60*24*365)));
        }
    }

}

//监听贷款期限单位 开始日期回调
function setStartTerm(ele,data){
    var  startValue = dateTypes.get("startDate").val();
    var  endValue = dateTypes.get("endDate").val();
    var  startDate = new Date(startValue);
    var  endDate = new Date(endValue);
    var  planStartTime=$("#extendApplyDate").val();
    var  planEndTime=$("#extendEndDate").val();
    if(startValue !=="" && endValue !==""){
        if(startValue >= endValue){//结束日期应大于开始日期
            dateTypes.get($(data).attr("data-biz-name")).val("");
            layer.tips(bt.lang("cms.precative.startdateenddate.more"), '#extendApplyDate');
            return false;
        }

        var ternUnit = dicts.get("deadlineUnit").val();
        if(ternUnit == "M"){
            $("#extendDeadline").val(dataSubtract(startValue,endValue,"M"));
        }else if(ternUnit == "D"){
            $("#extendDeadline").val((endDate-startDate)/(1000*60*60*24));
        }else if(ternUnit == "Y"){
            $("#extendDeadline").val(Math.ceil((endDate-startDate)/(1000*60*60*24*365)));
        }
    }

}

/**计算data1 与 data2 相差多少param单位 data为字符串*/
function dataSubtract(data1,data2,param){
    var dates;
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
    return  dates;
}

function mGetDate(year, month){
    var d = new Date(year, month  + 1, 0);
    return d.getDate();
}

function resetCallback() {
    window.location.reload();
}

/**加载利息预计明细*/
function repayInterest() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('cms.button.cmsBondRepayPlan.view'), basePath + "/cms/bond/cmsbondrepayplan/getPlanDtlList?registerId=" + $("#registerId").val() + "&planDtlId=" + $("#planDtlId").val() + "&showSign=0");
    }
}

/**选择还款计划后执行的方法*/
function setSelectInterestData(issueId, bondIssueNum, issueMoney, unpaidMoney, planRepayDate, repayMoney, id) {
    $("#bondIssueNum").val(bondIssueNum);
    moneyTypes.get("issueMoney").val(issueMoney);
    moneyTypes.get("outstandingAmount").val(unpaidMoney);
}


