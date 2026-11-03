bt.use(['form','layedit','moneynum','btdate','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        if(accState =='Z' && !transactionBackPre()){
            bt.loading.hide();
            return false;
        }
        data.field.accState=accState;

        var action = "/inneraccount/fcacccancel/checkPassOrBack";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }else{
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
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
        // layer.alert(val, {
        //     skin: 'layui-layer-molv', //样式类名
        //     closeBtn: 0
        // });
        layer.msg(val, {
            icon: 5
            ,shift: 6
        });
    }

    /**
     * 记账分录界面
     * @return
     */
    $genAccountHtml=function(){
        var account = $("#blnctrAcc").val();
        var busGlobalId = $("#busGlobalId").val();
        if(busGlobalId == ""){
            return ;
        }
        var url=basePath+"/inneraccount/fcacccancel/checkFcCancelAccount";
        var param={
            account:account,
            busGlobalId: busGlobalId,
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:true,
            success: function (json) {
                $("#tagFcCancelAccount").html("");
                $("#tagFcCancelAccount").html(json);
                form.render();
            }
        });
    };
    $(document).ready(function(){
        $genAccountHtml();
    });
});