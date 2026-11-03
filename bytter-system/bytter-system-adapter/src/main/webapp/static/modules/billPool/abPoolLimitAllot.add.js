var form,$,billPool,billPoolCorp,accountTable,moneyType,busType;
bt.use(['form','billPool','billPoolCorp', 'dateType','busType','select', 'dicts','accountTable','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    accountTable = bt.accountTable.init();
    moneyType = bt.moneyType.init();
    busType=bt.busType.init();
    bt.dicts.init();
    bt.dateType.init();
    //监听保存
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action="/billpool/limitAllot/save";
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
    getLimitInfo(data, billPool.get('bpId').val());
}

//票据池回调函数
/*function poolCallback(data){
    layer.alert(data.value);
    getLimitInfo(billPoolCorp.get('corpId').val(),data.value);
}*/
function poolCallback(data,text,selectedData,bv,value){
    if(selectedData.bpMode=='2' || selectedData.bpMode=='3'){
        $('#subBtn').addClass("layui-btn-disabled").attr("disabled",true);
        layer.alert(bt.lang("billPool.LimitAllot.bpModeNoSubmit"), {icon: 2});
        return;
    }
    $('#subBtn').removeClass("layui-btn-disabled").attr("disabled",false);
    getLimitInfo(billPoolCorp.get('corpId').val(),data.value);
}

//调剂额度设置回调
function applyMoneyEvent(nextValue,elem,preValue){
    moneyType.get('finalMoney').val(bt.moneyType.getFun().add($('#poolMoney').val(),nextValue));
}

//调剂后额度设置回调
function finalMoneyEvent(nextValue,elem,preValue){
    moneyType.get('applyMoney').val(bt.moneyType.getFun().sub(nextValue,$('#poolMoney').val()));
}

// 公共方法用于回调查询票据池额度和可用额度
function getLimitInfo(corpId, bpId) {
    // 当单位ID和票据池ID都不为空时查询
    if(corpId && bpId && $.trim(corpId) && $.trim(bpId)) {
        $.ajax({
            url: basePath + "/billpool/limitAllot/getLimitInfo",
            method: 'POST',
            data: {'corpId': corpId,'bpId' : bpId},
            dataType: 'JSON',
            success: function (result) {
                if(result.poolMoney || result.poolMoney==0){
                    var applyMoney=moneyType.get('applyMoney').val();
                    var finalMoney=result.poolMoney;
                    if(applyMoney){
                        finalMoney=bt.moneyType.getFun().add(applyMoney,finalMoney);
                    }
                    moneyType.get('finalMoney').val(finalMoney);
                    $('#poolMoneyDiv').html(bt.money.format(result.poolMoney));
                    $('#validMoneyDiv').html(bt.money.format(result.validMoney));
                    // $('#finalMoneyDiv').html(bt.money.format(finalMoney));
                    $('#poolMoney').val(result.poolMoney);
                    $('#validMoney').val(result.validMoney);
                    $('#lockMoney').val(result.lockMoney);
                    // $('#finalMoney').val(finalMoney);
                }
            },
            error:function(){
               // layer.alert(bt.lang('billPool.LimitAllot.noLimitInfo'), {icon: 7});
                //billPoolCorp.get("corpId").val("");
                //billPool.get("bpId").val(" ");
                $('#poolMoneyDiv').html("");
                $('#validMoneyDiv').html("");
                $('#finalMoneyDiv').val("");
                $('#poolMoney').val("");
                $('#validMoney').val("");
                $('#lockMoney').val("");
                $('#finalMoney').val("");
                moneyType.get('finalMoney').val("");
                //moneyType.get("applyMoney").val("");
            }
        });
    }
}
