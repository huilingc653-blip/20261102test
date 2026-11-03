var dicts,dateType,warnDataTable,selectCorpsTable,selectBankAccInfoTable,selectAccInfoTable,moneyType,bankAccAtt,bankAccNature,isAttribute;
bt.use(['form','element','datatable','select','checkboxs','selectType','moneyType','corp','dicts','dateType','mulCorp','mulselect','mulBankAccInfo','mulAccInfo'], function() {
    var $ = bt.jquery, form = bt.form();
    //监听提交
    form.on('submit', function(data){
        var action = "/credit/rawData/modify"
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
});