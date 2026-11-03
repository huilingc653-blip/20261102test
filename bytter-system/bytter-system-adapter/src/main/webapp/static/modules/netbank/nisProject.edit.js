var form;
var dict;
bt.use(['form','layedit','select','dicts'],function () {
    var $ = bt.jquery;
    form = bt.form();
    dict = bt.dicts.init();

    //监听提交
    form.on('submit(save)', function(data) {
        var action = "/netbank/nisproject/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
    setContentRequired($("#projectCdSign").val());

    form.renderIcon();
});

bt.projectCdSignCallback = function () {
    setContentRequired(dict.get("projectCdSign").val());
    form.renderIcon();
};

function setContentRequired(cdSign) {
    var matchContent = $("#matchContent");
    if (cdSign == 0){
        matchContent.removeAttr('lay-verify');
    }else{
        matchContent.attr("lay-verify","required");
    }
}

function resetCallback() {
    setContentRequired($("#defMatchmatchAttr").val());
    form.renderIcon();
}