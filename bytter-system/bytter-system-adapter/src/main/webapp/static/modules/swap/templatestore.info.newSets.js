var form,selectType;
bt.use(['form','element','selectType','moneyType'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    selectType = bt.selectType.init();
    form.on('submit(submit)', function(data){
        var serialIds = data.field.serialIds;
        var ids = serialIds.split(",");
        var count = 0;
        for(var i=0;i<ids.length;i++){
            if(ids[i]){
                data.field['serialId['+count+']'] = ids[i];
                count++;
            }
        }
        var action = "/erp/swap/hisDtlComField/modify";
        initData(data);
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


var initData = function(data) {
    data['field']['map'] = {};
    //组合数据
    var inputs = $("input[flag='defineStyle']"); //input[flag='head_sel_input']");
    var selects =$("select"); //$("#netSetTable select[data-flag='head_sel_input']");
    for (var i = 0; i < inputs.length; i++) {
        var name = $(inputs[i]).attr("name");
        var val = $(inputs[i]).val();
        if (name) {
            data['field']['map'][name] = val;
        }
    };
    for (var i = 0; i < selects.length; i++) {
        var name = $(selects[i]).attr("name");
        var val = $(selects[i]).find("option:selected").val();
        if (name) {
            data['field']['map'][name] = val;
        }
    };
};