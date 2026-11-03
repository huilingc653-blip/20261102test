bt.use(['form','layedit','btdate','moneynum','money'], function(){
    var $ = bt.jquery,form = bt.form();
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false,
            defaultVal:($(this).attr("value"))
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        if(accState =='-1' && !transactionBackPre()){
            bt.loading.hide();
            return false;
        }
        data.field.status=accState;
        var action = "/inneraccount/fcUnfreeze/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;

    });
    var transactionBackPre = function (obj) {
        if($("#refuseWhy").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
            sysAlert(bt.lang("table.fcAcc.checkBackReason.please"));
            $("#refuseWhy")[0].focus();
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

});
