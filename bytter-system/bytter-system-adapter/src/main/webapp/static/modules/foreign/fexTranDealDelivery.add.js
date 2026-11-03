bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType', 'bttable'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var selectType = bt.selectType.init();
    bt.rateType.init();

    //余额计算回调函数
    bt.balCallback = function (data) {
        //交割方向  1-买入，2-卖出
        var settleMoney = moneyType.get("settleMoney").val();
        if(settleMoney) {
            var tranBal = $("#tranBal").val();
            var bal = moneyFun.sub(moneyFun.toNumber(tranBal), moneyFun.toNumber(settleMoney));
            moneyType.get("bal").val(bal);
        }
        form.render();
    }

    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rowMR = $("#dataSize").val();

    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");
        var action = "/foreign/fextranreq/saveDeal";

        var bal = moneyFun.toNumber(moneyType.get("bal").val());
        if(bal<0){
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccdtl.message.amt.notzero"), {icon: 2});
            return false;
        }
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    /**
     * 删除
     */
    delTranDeal = function () {
        var id = $("#id").val();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/foreign/fextranreq/deleteTranDeal/" + id, function (result) {
                bt.navtab.refreshParentTable();
                bt.navtab.closeCurrent();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
});
