var form;
var dict;
bt.use(['form','layedit','select','dicts'],function () {
    var $ = bt.jquery;
    form = bt.form();
    dict = bt.dicts.init();

    //监听提交
    form.on('submit(submit)', function(data) {
        var action = "/netbank/nisproject/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    form.renderIcon();
});

bt.projectCdSignCallback = function () {
    bt.matchAttrCallback();
};

bt.matchAttrCallback = function () {
    var matchContent = $("#matchContent");
    if (dict.get("projectCdSign").val()==0){
        matchContent.removeAttr('lay-verify');
    }else{
        matchContent.attr("lay-verify","required");
    }
    form.renderIcon();
};

function resetCallback() {
    bt.matchAttrCallback($("#matchAttr").val());
}