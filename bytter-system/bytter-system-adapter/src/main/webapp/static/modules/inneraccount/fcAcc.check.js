bt.use(['form','layedit','moneynum','btdate','select','corp','verify'], function(){
    var $ = bt.jquery,form = bt.form();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        if(accState =='B' && !transactionBackPre()){
            bt.loading.hide();
            return false;
        }

        data.field.accState=accState;
        var action = "/inneraccount/fcacc/checkPassOrBack";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();

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
    $(document).ready(function(){
        if($('#isView').val() == 'v'){ //查看
            $('#endTable').hide();
        }

        // if($('#status').val() == 'T'){ //暂存数据需要看到打回原因
        //     $('#checkReson').hide();
        // }else{
        //     $('#checkReson').show();
        // }



        form.render('select');
    });
});
