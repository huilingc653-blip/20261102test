bt.use(['form','layedit','btdate','moneyType','corp','money','jquery'], function(){
    var $ = bt.jquery,form = bt.form();
    var mType=bt.moneyType.init();
    bt.corp.init();
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
        data.field.status=accState;
        if(!validFrom()){
            bt.loading.hide();
            layer.alert('解冻金额不能大于冻结金额！');
            return false;
        }
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
    function validFrom() {
        if(parseFloat(bt.money.toNumber($("#unFreezeMoney").val())) < parseFloat(bt.money.toNumber($("#freezeMoney").val()))){
            return false;
        }
        return true;
    }
    $(document).ready(function(){
        if($("#freezeType").val() == 'A' || $("#freezeType").val() == 'S'){
            mType.get('freezeMoney').attr("readonly","readonly");
        }else{
            $("[data-id='freezeMoney']").removeAttr("readonly");
        }
    });

});
