bt.use(['form','layedit','rateType','selectTree','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.rateType.init();
    var dicts = bt.dicts.init();

    bt.selectTree.init({
        url:basePath+"/foreign/fexExposureItem/tree",
        elem:".parentIdTree",
        name:'superItemCode',
        selected:$("#superItemCodeHide").val()
    });

    function submitForm(data) {
        var action = "/foreign/fexExposureItem/modify";
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
    }

    //监听提交
    form.on('submit(submit)', function(data){
        //校验科目是否已使用，提示不影响历史数据
        var result = false;
        $.ajax({
            url: basePath + "/foreign/fexExposureItem/validItemUsed",
            method: 'POST',
            async: false,
            data: {itemCode: data.field.itemCode},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    result = data.obj;
                }
            }
        });
        if (result) {
            parent.layer.confirm(bt.lang('foreign.message.confirm.item.used'), function (index) {
                submitForm(data);
            });
        } else {
            submitForm(data);
        }
        return false;
    });
});
