bt.use(['form','dateType','moneyType','dicts','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.corp.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/ras/shop/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(result.msg, {icon:1});
                if(bt.navtab) {
                    bt.navtab.closeCurrent();//关闭当前tab，刷新父tab页
                }else{
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }else{
                parent.layer.alert(result.msg, {icon: 2});
            }
        },'JSON');
        return false;
    });
});

//单位按钮回调
function corpCallBack(data){
    //判断（是否单位门店按钮）选中的值
    if($("[name='isCorp']:checked").val()==='1'){
        //是单位门店，直接赋值
        $("#name").val(data.corpName);
        $("#code").val(data.code);
    }
    //备份选择的单位信息
    $("#backUpCode").val(data.code);
    $("#backUpName").val(data.corpName);
}
//是否单位门店按钮回调
function isCorpShopCallBack(data){
    if(data.value==='1'){
        //赋值备份的单位信息
        $("#code").val($("#backUpCode").val());
        $("#name").val($("#backUpName").val());
    }else{
        $("#name").val('');
        $("#code").val('');
    }
}