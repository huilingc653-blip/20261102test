bt.use(['form','layedit','moneyType','dateType','corp','selectType','dicts', 'bankInfo'], function(){
    form = bt.form();
    var $ = bt.jquery

    //初始化日期
    bt.dateType.init();
    //初始化字典
    dict = bt.dicts.init();
    //初始化单位
    bt.corp.init();
    //初始化下拉框
    selectType = bt.selectType.init();

    //格式化数字
    bt.moneyType.init();

    const bankInfo = bt.bankInfo.init(); //联行号
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }

    //监听提交
    form.on('submit(submit)', function (data){
        const action = "/cms/bank/credit/plan/saveOrUpdate";
        let mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
        $.post(basePath + action, data.field, function(result){
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            layer.close(mask);
            if(result.success){
                if(!bt.navtab){
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });
});

// 银行账号选择
function bankInfoCallback(data) {
    $("#bankId").val(data.id);//银行id
    $("#bankCode").val(data.bankCode);//联行号
    $("#bankName").val(data.bankName);
}

