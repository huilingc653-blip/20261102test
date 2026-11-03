bt.use(['form','selectType','dicts','corp','dateType','moneyType','externalCorpBankAcc','btUpload'],function () {
    var selectType=bt.selectType.init();
    var dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
    form = bt.form();

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : $("#corpIdHidden").val(),
            type: "2"
        });
        return true;
    }

    up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    form.on('submit(submit)',function (data) {
        var action = '/capital/record/save';
        data.field.status = $(this).attr("value");
        data.field.attachmentId = up.getGroupId();
        if (!data.field.attachmentId && data.field.status == 1){
            parent.layer.alert(bt.lang("nis.capitalRecord.message.attachment.isnull"),{icon:7});
            return false;
        }
        $.post(basePath + action,data.field, function (result) {
            parent.layer.alert(result.msg,{icon:result.success?1:2},function () {
                parent.layer.closeAll();
                if (result.success){
                    if (bt.navtab){
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    } else{
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#payeeAcc").val(data.externalAcc);
            $("#payeeAccName").val(data.name);
            $("#payeeBankCode").val(data.bankSourceCode);
            $("#payeeBank").val(data.bank);
        } else {  // 选择网点或者成员单位信息
            $("#payeeAcc").val(data.bankAcc);
            $("#payeeAccName").val(data.corpName);
            $("#payeeBankCode").val(data.bankCode);
            $("#payeeBank").val(data.bankName);
        }
    };
});

