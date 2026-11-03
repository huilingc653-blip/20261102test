var form;
bt.use(['form','dicts'], function(){
    form = bt.form(),$  = bt.jquery;
    bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function(data){
        $.post(basePath + "/erp/swap/interconfig/save",data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },"JSON");
        return false;
    });

    $(document).ready(function() {
        setTimeout(isPayerSet,500);
        setTimeout(isPayeeSet,500);
    });
});

function configSet(type){
    var name="dataexchange.payment.makeup.setting";
    if("payee"==type){
        name="dataexchange.payment.makeup.setting";
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang(name),basePath + "/erp/swap/interconfig/set/"+type);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['750px', '530px'],
        maxmin: true,
        content: basePath + "/erp/swap/interconfig/set/"+type
    });
}
var isPayerSet=function(){
    if($('input:radio[name="payer.newSet"]:checked').val()=="1"){
        $("#payerSet").show();
    }else{
        $("#payerSet").hide();
    }
};

var isPayeeSet=function(){
    if($('input:radio[name="payee.newSet"]:checked').val()=="1"){
        $("#payeeSet").show();
    }else{
        $("#payeeSet").hide();
    }
};
