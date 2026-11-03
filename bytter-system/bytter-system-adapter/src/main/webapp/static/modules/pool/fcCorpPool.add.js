var corp;
bt.use(['form','corp','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    corp = bt.corp.init();
    var dict = bt.dicts.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/pool/fcCorpPool/save";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

});

//选择单位回调
poolCallback = function (obj) {
    //获取单位信息
    $.ajax({
        url: basePath + "/pool/fcCorpPool?getCorpName",
        method: 'POST',
        data: {id: obj},
        async:true,
        dataType: 'json',
        success: function (data) {
            if (data.obj) {
                if (data.obj.name) {
                    $("#corpName").val(data.obj.name);
                }
            }
        }
    });
}

corpBefore = function (data) {
    if(data){
        data[0].chkDisabled = true;
    }
    return data;
}