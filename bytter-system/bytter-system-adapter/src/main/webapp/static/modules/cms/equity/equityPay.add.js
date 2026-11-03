bt.use(['form', 'layedit','dicts', 'selectTree','rateType','selectType','moneyType','dateType','selectTableType'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    initBankAccTable()
    //监听保存
    form.on('submit', function(data){
        bt.loading.show();

        var nm = $(this).attr("id");
        if(nm == "submit"){
            data.field.saveType = "1";
        }else {
            data.field.saveType = "0";
        }
        var action="/cms/equity/payThe/savePay";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();

                }
            }else {
                bt.loading.hide();
            }

        }, 'JSON');
        return false;
    });

})

/**选择帐号的下拉列表初始化*/
function initBankAccTable(){
    selectTable = bt.selectTableType.init({
        elem: "#account",
        type: 'get',
        paging: true,
        url: basePath+"/cms/equity/info/queryPaymentAccListByCoprId?corpId=" +  $("#corpId").val() +"&curId="+ $("#curId").val(),
        columns: [
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")},
            {"data": "bankCode", title: bt.lang("table.btBankAcc.bankCode")},
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
        ],
        clickCallback:function(severData,data){
            $("#account").val(severData.bankAcc);
            $("#accName").val(severData.accName);
            $("#bankName").val(severData.bankName);
            $("#bankNo").val(severData.bankCode);
        }
    });


}

/**加载利息预计明细*/
function equityPlans(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/equity/payThe/equityPlans?equityCode="+$("#equityCode").val()+"&planDetailId="+$("#planDetailId").val());
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
 * 求汇率
 */
function changeCur() {
    var paycurId = select.get("payCurId").val();
    var curId = $("#curId").val();
    var payMoney = $("#payMoney").val();
    var curPreTime = $("#payDate").val();
    var rate = "";
    if (!bt.empty(paycurId) &&!bt.empty(curId) && !bt.empty(payMoney) && !bt.empty(curPreTime)) {
        bt.jquery.ajax({
            url: basePath + "/cms/equity/payThe/conver?payCurId="+paycurId+"&curId="+curId+"&curPreTime="+curPreTime,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                rate = t;
                var bwb= bt.money.mul(rate, payMoney, 2)
                $("#rate").val(t);
                $("#rate1").html(t);
                $("#rmbMoney").val(bwb);
                $("#rmbMoney1").html(bt.money.format(bwb));
                setProfitAndLoss();
            },
        });
    }
}

/**选择计划后执行的方法*/
function setSelectInterestData(payDate,payMoney,id){
    dateType.get("payDate").val(payDate);
    $("#needPayMoney").val(payMoney);
    $("#needPayMoney1").html(bt.money.format(payMoney));
    $("#planDetailId").val(id);
    moneyType.get("payMoney").val(payMoney);
    changeCur();
}

/*计算汇总损益*/
function setProfitAndLoss(){
    var rmbMoney=$("#rmbMoney").val();
    var needPayMoney= $("#needPayMoney").val();
    var profitAndLoss=bt.money.sub(rmbMoney-needPayMoney);
    $("#profitAndLoss").val(profitAndLoss);
    $("#profitAndLoss1").html(bt.money.format(profitAndLoss));
}


