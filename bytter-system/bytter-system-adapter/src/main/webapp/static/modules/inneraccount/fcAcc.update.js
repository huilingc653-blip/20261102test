bt.use(['form','layedit','moneynum','btdate','dateType','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    var curDate = bt.dateType.init();
    $("#reset").click(function(){
        location.reload();
        return false ;
    });
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        data.field.accState=accState;
        if(!validNetDif() || !compareDate() || !validCorpDif()){
            bt.loading.hide();
            return false;
        }
        var action = "/inneraccount/fcacc/modify";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab){
                    bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });

    //网点/联行网点不能相同
    var validNetDif = function () {
        var poolSign = $("#poolSign").val();
        if(poolSign == "1") {
            return true;
        }
        var netId= '', unionNet= '';
        if($('input[name="netId"]')){
            netId=$('input[name="netId"]').val();
        }
        if($('input[name="unionNet"]')){
            unionNet= $('input[name="unionNet"]').val();
        }

        if(netId != '' && netId == unionNet){
            sysAlert(bt.lang("table.fcAcc.validNetDif"));
            return false;
        }
        return true;
    };


    //委托存款单位不能与委托贷款单位是同一个单位
    var validCorpDif = function () {
        var corpId= '', precationCorp='';
        if($('input[name="corpId"]').length>0){
            corpId=$('input[name="corpId"]').val();
        }
        if($('input[name="precationCorp"]').length>0){
            precationCorp= $('input[name="precationCorp"]').val();
        }
        if(corpId != '' && corpId == precationCorp){
            sysAlert(bt.lang('nis.fcAcc.message.entrusted.deposit.loan'));
            return false;
        }

        return true;
    };
    var compareDate = function(){
        var dateFun = bt.dateType.getFun();//获取时间操作方法
        if($("input[name='interestStartDate']").length>0 && $("input[name='deadlineDate']").length>0 ){
            var startDate = curDate.get("interestStartDate").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(startDate,deadlineDate)>0){
                sysAlert(bt.lang('nis.fcAcc.message.less.Due.value'));
                return false;
            }
        }else if($("input[name='deadlineDate']").length>0 && $("input[name='accStartDate']").length>0 ){
            var startDate = $("input[name='accStartDate']").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(startDate,deadlineDate)>0){
                sysAlert(bt.lang('nis.fcAcc.message.earlier.expiration.activation'));
                return false;
            }
        }
        if($("input[name='deadlineDate']").length>0 && $("input[name='delayDeadlineDate']").length>0 ){
            var delayDeadlineDate = $("input[name='delayDeadlineDate']").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(delayDeadlineDate,deadlineDate)<0 || dateFun.compare(delayDeadlineDate,deadlineDate)==0){
                sysAlert(bt.lang('nis.fcAcc.message.greater.extension.expiration'));
                return false;
            }
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
        // if($('#isView').val() == 'v'){ //查看
        //     $('div.layui-input-block').hide();
        //     $('.layui-btn').hide();
        //     $('#endTable').hide();
        // }

        if($('#status').val() == 'T'){ //暂存数据需要看到打回原因
            $('#endTrCheckBackReason').hide();
        }else{
            $('#endTrCheckBackReason').show();
        }



        form.render('select');
    });
});

