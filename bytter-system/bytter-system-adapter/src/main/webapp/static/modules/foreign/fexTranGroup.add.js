bt.use(['form','layedit','dicts', 'selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    var select = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/foreign/fextrangroup/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.refreshCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    bt.changeProId = function () {
        var proId = select.get("proId").val();
        if (!proId) {
            return;
        }
        var url = basePath + "/foreign/fexproduct/listFexProduct/" + proId;
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function(msg){
                var obj = typeof msg === 'string' ? JSON.parse(msg) : msg;
                if (!obj) {
                    return;
                }
                var ruleAttr = {
                    "data-defaultvalue": obj.trangroupRuleId || "",
                    "data-value": obj.trangroupRuleId || ""
                };
                if (obj.isAllowedChange === '1') {
                    ruleAttr["data-disabled"] = false;
                } else {
                    ruleAttr["data-disabled"] = true;
                }
                select.get('ruleId').attr(ruleAttr);
            }
        });
    }

});
