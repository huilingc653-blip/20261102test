var datatable={},moneyType,mulselect;
bt.use(['form','corp','dicts','mulselect','moneyType','selectType','externalCorp','bank','bankInfo','externalCorpBankAcc'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank'
    });
    bt.externalCorpBankAcc.init();
    bt.bankInfo.init();
    bt.dicts.init();
    mulselect=bt.mulselect.init();
    moneyType = bt.moneyType.init();
    var externalCorp=bt.externalCorp.init();

    //监听新增保存
    form.on('submit(save)', function(data){
        var url ;
        if($("#id").val()==""){
            url = "/bis/checkReason/save/add";
        }else{
            url = "/bis/checkReason/save/update";
        }
        $.post(basePath + url,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.navtab.refreshParentTable();//刷新父页面表格
                location.reload();//重载当前页面
            }
            bt.navtab.closeCurrent();
        },'JSON');
    });
});



