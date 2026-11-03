var form ={};
var dicts={};
var corps={};
var rasShops={};
bt.use(['form','layedit','dicts','select','corp','rasShop'], function(){
    var $ = bt.jquery,form = bt.form();
    dicts=bt.dicts.init();
    corps=bt.corp.init();
    rasShops=bt.rasShop.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/ras/posBinding/lendSave";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                location.reload();//重载当前页面
            }
        },'JSON');
        return false;
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}

//门店回调函数
function shopCallback(obj,text,data){
    if(data!=null) {
        $("#shopCode").val(data.code);
    }
}