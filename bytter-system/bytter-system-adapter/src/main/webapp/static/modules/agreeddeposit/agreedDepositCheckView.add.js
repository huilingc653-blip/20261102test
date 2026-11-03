bt.use(['form','layedit','select','corp'], function () {
    var $ = bt.jquery, form = bt.form();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var status=$(this).attr("value");
        if(status == -1 && !transactionBackPre()){
            bt.loading.hide();
            return false;
        }
        data.field.status = $(this).attr("value");
        var action = "/agreeddeposit/fcagreeddeposit/opCheck";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    var transactionBackPre = function (obj) {
        if($("#checkBackReason").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
            sysAlert(bt.lang("table.fcAcc.checkBackReason.please"));
            $("#checkBackReason")[0].focus();
            return false;
        }
        return true;
    };

    function sysAlert(val) {
        layer.msg(val, {
            icon: 5
            ,shift: 6
        });
    }

});

//关闭
var closeDoc=function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if(index){
        parent.layer.close(index); //再执行关闭
    }else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};
