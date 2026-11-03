var dict;
var selectType;
bt.use(['form','layedit','selectTree','select','btdate','corp','moneyType','selectType','dicts'], function(){
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    selectType = bt.selectType.init();
    dict = bt.dicts.init();
    bt.moneyType.init();
    //bt.moneyType.init({elem:$('[data-type="int"]'),decimal:0,minus:true});

    // 字段选择回调
    bt.fieldCallback = function (data){
        $("#fieldCode").val(data.fieldcode);
        $("#fieldName").val(data.fieldname);
    }


    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/foreign/fexTranReqConfig/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }else{
                    bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});