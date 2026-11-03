var corpId = "";
var ratio,billPool;
var externalCorpBankAcc={};
bt.use(['form', 'layedit','moneyType','dicts','externalCorpBankAcc','accountTable','radioType','bank','jquery','bankInfo'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.bankInfo.init({
        multiLayer:true
    });
    bt.moneyType.init();
    bt.bank.init({elem:'.bank'});
    ratio = bt.radioType.init();
    externalCorpBankAcc=bt.externalCorpBankAcc.init({
        multiLayer:true
    });
    externalCorpBankAcc.set({"type":4});
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    form.verify({

    });

    //纸票时为不必填，电票则必填
    if($("#billFlag").val()=='10'){
        $("#recAcctId").attr("lay-verify","strlength");
        $("#recNm").attr("lay-verify","strlength");
        $("#recAcctsvcr").attr("lay-verify","strlength");
        bt.form().renderIcon();
    }else{
        $("#recAcctId").attr("lay-verify","required|strlength");
        $("#recNm").attr("lay-verify","required|strlength");
        $("#recAcctsvcr").attr("lay-verify","required|strlength");
        bt.form().renderIcon();
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        var ids = $("#ids").val();//票据池交易明细ids
        data.field.id=ids;
        bt.loading.show();
        var action = "/billPool/endorse/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                // bt.navtab.closeCurrent();
                //bt.navtab.refreshParentTable();//刷新父页面表格
                parent.layer.closeAll();
                location.reload();//重刷页面
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                location.reload();//重刷页面
            }
        }, 'JSON');
        return false;
    });
    bt.bailCorpCallback = function(data,dom){
        $("#recNm").val(data.name);
        $("#recAcctId").val(data.externalAcc);
        $("#recAcctsvcr").val(data.bankSourceCode);
        $("#recAcctsvcrName").val(data.bank);
    }
    //开户行选择
    bt.bankinfoCallback = function(data,dom){
        $("#recAcctsvcr").val(data.bankCode);
        $("#recAcctsvcrName").val(data.bankName);
    }
});



