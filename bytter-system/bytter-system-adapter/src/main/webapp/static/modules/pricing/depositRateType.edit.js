var dict;
bt.use(['form','layedit','dicts','moneyType'], function(){
    var $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    dict = bt.dicts.init();
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }

    bt.moneyType.init();
    // 监听提交
    form.on('submit(submit)', function(data){
        var action = "/pricing/depositRateType/interestRateType/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});

//关闭当前页面
function closecurrent(){
    if(!bt.navtab){
        parent.layer?parent.layer.closeAll():layer.closeAll();
    } else {
        bt.navtab.closeCurrent();
    }
    return false;
}