var moneyType;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery, form = bt.form();
     moneyType = bt.moneyType.init();



     form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/accountReceivable/writeOff/cancelWriteOff";
        if(!data.field.batchNo){
            data.field.batchNo = data.field.radioWrite;
        }

        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                result.msg = bt.lang("submitted.successfully.pending.approval");
            }
            parent.layer.open({content:result.msg, yes: function(){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            });
        },'JSON');
        return false;
     });
});

function cancle(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}