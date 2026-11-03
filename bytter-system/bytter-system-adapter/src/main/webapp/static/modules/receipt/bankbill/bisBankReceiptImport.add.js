
bt.use(['form', 'btUpload', 'selectType', 'accountTable'], function () {
    var $ = bt.jquery, form = bt.form();

    bifCodeCallback = function(data){
        bt.accountTable.get("bankAcc").reload({
            urldata: {
                bifCode: data.id?data.id:""
            }
        });
        $("#bankAcc").val("");
    }
    selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#bankAcc").val(data.bankAcc);
        $("#accBifCode").val(data.bifCode);
    }

    accountTable = bt.accountTable.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: '',
        success:function () {
            console.log(arguments)
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var accBifCode = $("#accBifCode").val();
        var bifCode = selectType.get("bifCodeId").val();

        if(bifCode != accBifCode){
            parent.layer.alert("银行类型与账号银行不符，请重新选择");
            return false;
        }
        if(up.getGroupId() == null || up.getGroupId() == ""){
            parent.layer.alert("请先上传回单");
            return false;
        }
        //var fileNum = up.paramObj.hasUploadNum;
        var totalNum = up.options.totalNum;
        var uploadNum = up.options.uploadNum;
        if(uploadNum != totalNum){
            parent.layer.alert("请上传结束再保存");
            return false;
        }
        data.field.attachmentId = up.getGroupId();
        data.field.isConfirm=0;
        var action = "/receipt/bisbankreceiptimport/save";
        $.post(basePath + action, data.field, function (result) {
            if ("RECEIPT_BANKACC_NOT_MATCH"===result.messageEnum){
                parent.layer.confirm(result.msg,function(){
                    data.field.isConfirm=1;
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
                    },'JSON');
                });
            }else{
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
            }



        }, 'JSON');
        return false;
    });
});

