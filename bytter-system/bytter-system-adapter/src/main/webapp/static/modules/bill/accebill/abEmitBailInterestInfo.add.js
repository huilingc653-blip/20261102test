bt.use(['form', 'layedit', 'selectType', 'dicts', 'corp','accountTable', 'dateType', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    bt.corp.init();
    bt.accountTable.init({urldata:{accountType :'2,3',electricBill:'1'}});
    bt.dateType.init();
    bt.selectType.init();
    bt.moneyType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/bill/accebill/bailInterestInfo/saveOrUpdate";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});

//票据单位回调函数
function corpCallback(data){
    if(data&&data.length==0)return;
    bt.accountTable.get("bankAcc").initCorp(data.id,"1",false);
    $("#corpName").val(data.corpName);
}

//选择出票人账号回调方法
function accountCallBack(data){
    var $ = bt.jquery;
    $("#bankAcc").val(data.bankAcc);
    $("#bankTypeId").val(data.bankTypeId);
    $("#bankTypeName").val(data.bankTypeName);
}