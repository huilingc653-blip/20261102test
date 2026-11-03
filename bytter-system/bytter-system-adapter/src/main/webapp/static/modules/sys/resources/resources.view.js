bt.use(['form', 'layedit', 'laydate', 'jquery'], function () {
    var form = bt.form(), layer = bt.layer, $ = bt.jquery;

    //自定义验证规则
    form.verify({
        name: function (value) {
            if (value.length < 3) {
                // 资源名称至少得3个字符！
                return bt.lang('sys.resource.name.minLength');
            }
            if (value.length > 30) {
                // 资源名称不能大于30个字符！
                return bt.lang('sys.resource.name.maxLength');
            }
        }
    });

    //监听提交
    form.on('submit(resourcesSubmit)', function (data) {
        if (typeof (data.field.status) == "undefined") {
            data.field.status = 0;
        } else {
            data.field.status = 1;
        }
        var action = $('input[name="id"]').val() ? "update" : "save";
        $.post(basePath + "/sys/resources/" + action, data.field, function (result) {
            if (result.success) {
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });


});
