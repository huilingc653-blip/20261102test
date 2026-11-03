var form,$,billPool,billPoolCorp,accountTable,moneyType;
bt.use(['form','billPool','billPoolCorp','treeTable','accountTable','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    accountTable = bt.accountTable.init();
    moneyType = bt.moneyType.init();

    //监听保存
    form.on('submit(submit)', function(data){
        if($('#bankAcc').html().length < 1 || $('#bailMoney1').html().length < 1){
            layer.alert(bt.lang('billPool.bail.noBailInfoWarn'), {icon: 7});
            return false;
        }
        bt.loading.show();
        var action="/billPool/bail/submit/2";
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    location.reload();//重载当前页面
                    bt.navtab.refreshParentTable();//刷新父页面表格
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });
});

//单位回调函数
function corpCallback(data){
    //通过单位id加载出票人银行账号
    accountTable.get("oppAcc").initCorp(data);
    getBailInfo(data, billPool.get('billPool').val());
}

//票据池回调函数
function poolCallback(data){
    getBailInfo(billPoolCorp.get('corpId').val(),data.value);
}

// 公共方法用于回调保证金账户和保证金余额
function getBailInfo(corpId, bpId) {
    // 当单位ID和票据池ID都不为空时查询
    if(corpId && bpId && $.trim(corpId) && $.trim(bpId)) {
        $.ajax({
            url: basePath + "/billPool/bail/getBailInfo/2",
            method: 'POST',
            data: {'corpId': corpId,'bpId' : bpId},
            dataType: 'JSON',
            success: function (result) {
                if(result.bankAcc && (result.bailMoney||result.bailMoney==0)){
                    $('#bankAcc').html(result.bankAcc);
                    $('#bailMoney1').html(bt.money.format(result.bailMoney));
                    $('#bailMoney').val(result.bailMoney);
                    $('#bailId').val(result.id);
                    getLimitInfo(corpId,bpId,result.bankAcc);
                }
            },
            error:function(){
                layer.alert(bt.lang('billPool.bail.noBailInfoWarn'), {icon: 7});
                billPoolCorp.get("corpId").val("");
                billPool.get("billPool").val(" ");
                moneyType.get("money").val("");
                $('#oppAcc').val("");
                $('#bankAcc').html("");
                $('#bailMoney1').html("");
            }
        });
    }

}

//转出账户回调函数
function oppAccCallBack(data){
    $("#oppAcc").val(data.bankAcc);
    $("#oppAccName").val(data.accName);
    $("#oppBankName").val(data.bankName);
    $("#oppBankNo").val(data.bankCode);
}


// 公共方法用于回调查询票据池额度和可用额度
function getLimitInfo(corpId, bpId,bankAcc) {
    // 当单位ID和票据池ID都不为空时查询
    if(corpId && bpId && bankAcc && $.trim(corpId) && $.trim(bpId) && $.trim(bankAcc)) {
        $.ajax({
            url: basePath + "/billPool/limitInfo/getLimitInfo",
            method: 'POST',
            data: {'corpId': corpId,'bpId' : bpId,'bankAcc':bankAcc},
            dataType: 'JSON',
            success: function (result) {
                if(result.validMoney || result.validMoney==0){
                    $('#validMoney1').html(bt.money.format(result.validMoney));
                    $('#validMoney').val(result.validMoney);
                    setMaxMoney();
                }else{
                    setMaxMoney();
                }
            },
            error:function(){
                $('#validMoney1').html("");
                setMaxMoney();
            }
        });
    }
}

//设置最大可提取金额：取保证金余额和可用额度两者中较小的作为可提取金额，提取金额不能大于可提取金额
function setMaxMoney(){
    if($('#bailMoney').val()!="" && $("#validMoney").val()==""){
        moneyType.get("money").attr("data-max",$('#bailMoney').val());
    }else if($('#bailMoney').val()=="" && $("#validMoney").val()!=""){
        moneyType.get("money").attr("data-max",$('#validMoney').val());
    }else if($('#bailMoney').val()!="" && $("#validMoney").val()!=""){
        if(bt.money.toNumber($("#validMoney").val())<bt.money.toNumber($('#bailMoney').val())){
            moneyType.get("money").attr("data-max", $("#validMoney").val());
        }else{
            moneyType.get("money").attr("data-max", $("#bailMoney").val());
        }
    }
}


