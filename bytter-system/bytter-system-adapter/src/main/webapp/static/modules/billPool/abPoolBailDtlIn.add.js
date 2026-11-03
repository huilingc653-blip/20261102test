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
        var action="/billPool/bail/submit/1";
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
            url: basePath + "/billPool/bail/getBailInfo/1",
            method: 'POST',
            data: {'corpId': corpId,'bpId' : bpId},
            dataType: 'JSON',
            success: function (result) {
                if(typeof result.success == "boolean"){
                    layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    billPoolCorp.get("corpId").val("");
                    billPool.get("billPool").val(" ");
                    $('#bankAcc').html("");
                    moneyType.get("money").val("");
                    $('#oppAcc').val("");
                    $('#bailMoney1').html("");
                    return;
                }
                if(result.bankAcc && (result.bailMoney||result.bailMoney==0)){
                    $('#bankAcc').html(result.bankAcc);
                    $('#bailMoney1').html(bt.money.format(result.bailMoney));
                    $('#bailMoney').val(result.bailMoney);
                    $('#bailId').val(result.id);
                }
            },
            error:function(){
                layer.alert(bt.lang('billPool.bail.noBailInfoWarn'), {icon: 7});
                billPoolCorp.get("corpId").val("");
                billPool.get("billPool").val(" ");
                $('#bankAcc').html("");
                moneyType.get("money").val("");
                $('#oppAcc').val("");
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
