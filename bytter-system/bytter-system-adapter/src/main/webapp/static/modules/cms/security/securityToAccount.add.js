var dateTypes,moneyTypes,selectTypes,dicts,form;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','selectTableType'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();//单位
    dicts = bt.dicts.init();//字典

    moneyFun=bt.moneyType.getFun();



    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == "null") {
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit', function(data){
        var action = "/cms/security/toAccount/save"
        var toAccountMoney=moneyTypes.get("toAccountMoney").val();
        bt.loading.show();
        if(toAccountMoney<=0){
            bt.loading.hide();
            parent.layer.alert(bt.lang('nis.netbank.message.money.mismatch'), {icon: 2});
            return false;
        }
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
    initBankAccTable();
});

function resetCallback() {
    window.location.reload();
}

/**选择帐号的下拉列表初始化*/
function initBankAccTable(){
    selectTable = bt.selectTableType.init({
        elem: ".account",
        type: 'get',
        paging: true,
        url: basePath+"/cms/equity/info/queryPaymentAccListByCoprId?corpId=" +  $("#corpId").val() +"&curId="+ $("#curId").val(),
        columns: [
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")},
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
        ],
        clickCallback:function(severData,data){
            $("#payee").val(severData.bankAcc);
            $("#accName").val(severData.accName);
            $("#bankName").val(severData.bankName);
        }
    });


}
