var form,selectType;
bt.use(['form','element','selectType','moneyType','dateType','selectCallback'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    var configType = $("input[name='configType']").val();
    var money = $("#info_money_val").text();
    if(money){
        $("#info_money").html(bt.money.toChinese(money));
    }
    $.each($("div.bt-biz-dict"),function (i,item) {
        var v = bt.dict($(this).data("group"),$(this).data("value"));
        $(this).parent("td").html(v);
    });
    selectType = bt.selectType.init();
    form.on('submit(submit)', function(data){
        var action = "/erp/swap/hisdtltemplate/modify";
        
        if($("#enteringIsEdit").val()==='1'){
            var flag = initTemplateStoreDate(data);
            if(!flag) return false;
        }
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});
