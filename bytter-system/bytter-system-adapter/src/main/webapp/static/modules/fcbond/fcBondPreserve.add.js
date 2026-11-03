var dateTypes,dicts,selectTypes,moneyTypes,corp,bankInfo,bondAccInit,parameters,curId,corpId;
bt.use(['form','jquery','dateType','selectType','dicts','moneyType','corp',
    'bankInfo','selectType','bondAccInfo'],function(){
    $ = bt.jquery,form = bt.form();
    dateTypes=bt.dateType.init();
    dateFun = bt.dateType.getFun();
    dicts = bt.dicts.init();//字典
    selectTypes=bt.selectType.init();
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//网点
    bankInfo = bt.bankInfo.init();//联行号

    parameters=new Object();
    parameters.corpId=corp.get("corpId").val();
    parameters.bankCode=$("#bankId").val();
    parameters.curId=$("#curId").val();
    bondAccInit=bt.bondAccInfo.init();
    bt.bondAccInfo.set(parameters)

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    function checkBefore(){
        //校验银行是否一致
        var bankCode=$("#bankCode").val();
        var bondBank=$("#bondBank").val();
        if(!(bankCode==bondBank)){
            parent.layer.msg(bt.lang("fcbond.apply.bankmatching"), {icon: 2});
            return false;
        }

        //校验币别是否一致
        var bondCurId=$("#bondCurId").val();//保证金账户币别
        var curId=$("#curId").val();//项目币别
        if(!(bondCurId==curId)){
            parent.layer.msg(bt.lang("fcbond.apply.curmatching"), {icon: 2});
            return false;
        }

        //校验单位是否一致
        var corpId=corp.get("corpId").val();//项目单位
        var bondCorpId=$("#bondCorpId").val();//保证金项目单位
        if(!(corpId==bondCorpId)){
            parent.layer.msg(bt.lang("fcbond.apply.corpmatching"), {icon: 2});
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
        var action = "/bond/fcbondpreserve/saveFcBondItem";
        var nm = $(this).attr("id");
        if(nm == "submit"){
            data.field.saveType = "1";
        }
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            bt.loading.hide(); //关闭加载中
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshParentTable();
                }
            }
        },'JSON');
        return false;
    });
});

//期限回调函数
bt.dayLimitCallback = function(dayLimit) {
    //计算到期日期
    var dayLimit = $.trim(moneyTypes.get("itemUnit").val());//项目期限
    var startDate = $.trim(dateTypes.get("startDate").val());
    if (bt.empty(startDate) || bt.empty(dayLimit)) {

    }else{
        deadlineDateShow(startDate, dayLimit);
    }
}
//到期日期计算
function deadlineDateShow(startDate, dayLimit){
    dateTypes.get("endDate").val(dateFun.format(dateFun.addMonth(startDate, dayLimit)));
}

function computePrTerm(){
    var startDate = $.trim(dateTypes.get("startDate").val());
    var endDate = $.trim(dateTypes.get("endDate").val());
    var itemUnit= $.trim(moneyTypes.get("itemUnit").val());//项目期限

    if(bt.empty(startDate) || bt.empty(endDate)){
        itemUnit = "";
    }else{
        var termUnit = $.trim(dicts.get("termUnit").val());
        var month = dataSubtract(startDate,endDate,"M");
        if(!bt.empty(termUnit)){
            itemUnit = dataSubtract(startDate,endDate,termUnit);
            if(termUnit == "D"){
                //PrTerm = dataSubtract(startDate,endDate,"D");
            }
        }else{
            //为空或者null默认月处理
            dicts.get("termUnit").val("M");
            itemUnit = dataSubtract(startDate,endDate,"M");
        }
    }
    moneyTypes.get("itemUnit").val(itemUnit);//设置期限
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

//银行账号选择
function bankinfoCallback(data,dom){
    $("#showBankName").val(data.bankName);
    $("#bankId").val(data.bankCode);
    $("#bankType").val(data.bankTypeId);
    $("#bankCode").val(data.bankCode);
    //重新查询该开户行下的保证金账户
    parameters.corpId=corp.get("corpId").val();
    parameters.bankCode=$("#bankId").val();
    parameters.curId=$("#curId").val();
    bt.bondAccInfo.set(parameters)
}

bt.bankInfoBeforeClick = function () {
    return true;
}

bt.bondAccBeforeClick=function(){
    var curId = $("#curId").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
      if (!startDate || startDate.trim() === "") {
         parent.layer.msg(bt.lang("请选择项目开始日期"), {icon: 2});
            return false;
        }
        if (!endDate || endDate.trim() === "") {
          parent.layer.msg(bt.lang("请选择项目结束日期"), {icon: 2});
            return false;
        }
    parameters.corpId=corpId;
    parameters.bankCode=$("#bankCode").val();
    parameters.curId=curId;
    parameters.startDate = startDate;
    parameters.endDate = endDate;
    bt.bondAccInfo.set(parameters);
    return true;
}
//网点回调，重新查询网点下的保证金账户
bt.corpCallback=function(){
    corpId=corp.get("corpId").val();
    parameters.corpId=corp.get("corpId").val();
    parameters.bankCode=$("#bankCode").val();
    parameters.curId=$("#curId").val();
    bt.bondAccInfo.set(parameters)
}

//根据币别查询保证金账户
function curinfoCallback(data){
    $("#curId").val(data.id);
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    parameters.corpId=corpId;
    parameters.bankCode=$("#bankCode").val();
    parameters.curId=data.id;
    parameters.startDate = startDate;
    parameters.endDate = endDate;
    bt.bondAccInfo.set(parameters);
}
//保证金账户信息填充
function bondAccinfoCallback(data,dom){
    $("#bondId").val(data.bondId);
    $("#bankAcc").val(data.bankAcc);
    $("#accName").val(data.accName);
    $("#bankName").val(data.bankName);
    $("#bondDate").val(data.bondDate);
    $("#bondCurId").val(data.curId);
    $("#bondCorpId").val(data.corpId);
    $("#bondBank").val(data.bankCode);
    moneyTypes.get("bondMoney").val(data.accBalance);
    moneyTypes.get("availMoney").val(data.availMoney);
    $("#accAvailMoney").val(bt.money.format(data.availMoney));
}

//循环计算可用余额
function calAvailMoney(){
    var addOrUpdateFlag=$("#addOrUpdateFlag").val();
    var availMoney;
    var applyMoney = bt.money.format($("#applyMoney").val());
    var accBalance = bt.money.format($("#accAvailMoney").val());
    if(addOrUpdateFlag=="6"){//修改数据,保证金余额需加上修改之前占用的金额
        var applyMoneyPre=bt.money.format($("#applyMoneyPre").val());
        accBalance =bt.money.add(accBalance,applyMoneyPre);
    }

    if(applyMoney==0){
        moneyTypes.get("availMoney").val(accBalance);
    }else{
        availMoney=bt.money.sub(accBalance,applyMoney);
        if(availMoney<0){
            moneyTypes.get("applyMoney").val(0);
            parent.layer.msg(bt.lang("fcbond.apply.checkApplyMoney"), {icon: 2});
        }else{
            moneyTypes.get("availMoney").val(availMoney);
        }
    }
}