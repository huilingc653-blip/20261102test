var form,$,billPool,billPoolCorp,datatable,moneyType;
bt.use(['form','billPool','billPoolCorp','dateType','mulselect','dicts','accountTable','bankInfo','externalCorpBankAcc','bank','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    bt.accountTable.init({urldata:{accountType:'2,3',corpId:billPoolCorp.get('corpId').val(),electricBill:'1'}});
    bt.bankInfo.init();
    bt.bank.init({
        elem:'.bank'
    });

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    //收票人客商
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查询
        externalCorpBankAcc.set({"corpId": billPoolCorp.get("corpId").val() ? billPoolCorp.get("corpId").val() : "0","type":"4"});
        return true;
    }
    //承兑人客商
    bt.acceptorCustomerBankAccParams = function () {
        //供应商默认不查询
        externalCorpBankAcc.set({"corpId": billPoolCorp.get("corpId").val() ? billPoolCorp.get("corpId").val() : "0","type":"4"});
        return true;
    }


    //选择银行回调函数
    bt.bailBankInfoCallback = function(data,dom){
        $("#acceptor").val(data.bankName);//承兑人
        $("#acceptoracc").val("0");//承兑人账号--银行默认为0
        $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
        $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
        $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
        if($("#isRelateRat").val()==true){
            setRationBtn();
        }
    }

    //承兑人选择客商回调函数
    bt.bailCorpCallback = function(data,dom){
        $("#acceptor").val(data.name);
        $("#acceptoracc").val(data.externalAcc);
        $("#acceptorbankno").val(data.bankSourceCode);
        $("#acceptorBankName").val(data.bank);
    }
    //开户行选择
    bt.bankinfoCallback = function(data,dom){
        $("#acceptorbankno").val(data.bankCode);
        $("#acceptorBankName").val(data.bankName);
    }

    //收票人选择客商回调函数
    bt.payeeCorpCallback = function(data,dom){
        $("#payeeAccName").val(data.name);//客商名称
        $("#payeeacc").val(data.externalAcc);//客商账号
        $("#payeeBankName").val(data.bank);//客商开户银行名称
        $("#payeebankno").val(data.bankSourceCode);//客商开户银行行号
    };

    //监听新增保存
    form.on('submit(save)', function(data){
        submit(data,"/billpool/emitFinaApply/save/add");
    });
    //监听新增提交
    form.on('submit(submit)', function(data){
        submit(data,"/billpool/emitFinaApply/save/submit");
    });

    var submit = function(data,action){
        bt.loading.show();

        // 出票人和收票人不能相同
        if($("#payeracc").val() == $("#payeeacc").val()){
            //出票人和收票人不能一样！
            bt.loading.hide();
            layer.alert(bt.lang('billPool.emit.warn'), {icon: 7});
            return false;
        }

        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if(result.success){
                bt.navtab.closeCurrent();//关闭当前Tab
                bt.navtab.refreshParentTable();//刷新父页面表格
            }
        },'JSON');
        return false;
    }
});

//选择出票人账号回调方法
function accountPayeeCallBack(data){
    $("#payer").val(data.accName);//【出票人以户名为准】
    $("#payeracc").val(data.bankAcc);
    $("#payerBankName").val(data.bankName);
    $("#payerbankno").val(data.bankCode);
}

//开票单位回调函数
function corpCallback(data){
    if(data&&data.length==0)return;
    //清空出票人开户银行名称、出票人开户行行号
    $("#payerBankName").val("");
    $("#payerbankno").val("");
    //通过单位id加载出票人银行账号
    //electricBill=1 票据专户
    bt.accountTable.get("payeracc").initCorp(data,"1");

}

//票据池回调函数
function poolCallback(data){
    if(data&&data.length==0)return;
    $.get(basePath + "/billpool/emitFinaApply/selectPledgee?bpId="+data.value,function(data){
        $('#acceptoracc').val(data.acceptoracc);//承兑人账号
        $('#acceptor').val(data.acceptor);//承兑人
        $('#acceptorbankno').val(data.acceptorbankno);//承兑人开户行号
        $('#acceptorBankName').val(data.acceptorBankName);//承兑人开户行名称
    },'JSON');
}


//打回
function repulse(){
    var reason=$("#reason").val();
    if(isEmpty(reason)){
        //打回原因必填
        layer.alert(bt.lang('common.message.repulseRequired'), {icon: 7});
        return;
    }
    $.post(basePath + "/billpool/emitFinaApply/batchDelete", {"ids": $('#dtlId').val(), "reason": reason}, function (result) {
        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        if(result.success){
            if(!bt.navtab){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
        }
    }, 'JSON');
}

/**判断字符串是否为空*/
function isEmpty(ms) {
    if (typeof (ms) == "undefined" || ms == null || ms == "") {
        return true;
    }
    return false;
}
