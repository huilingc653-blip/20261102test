bt.use(['form', 'layedit', 'laydate', 'jquery', 'selectTree'], function () {
    var form = bt.form(), layer = bt.layer, $ = bt.jquery;
    var iconValue = $("#icon").val();

    $('input[name="icon"]').val(iconValue);

    bt.selectTree.init({
        url: basePath + "/sys/module/tree",
        elem: ".module-parentId",
        name: 'parentId',
        selected: $("#parentId").val(),
        currentNode: $("#id").val(),
    });


    //自定义验证规则
    form.verify({
        name: function (value) {
            if (value.length < 2) {
                // 模块名称至少得2个字符！
                return bt.lang('sys.module.name.minLength');
            }
            if (value.length > 30) {
                // 模块名称不能大于30个字符！
                return bt.lang('sys.module.name.maxLength');
            }
        }
    });

    //监听提交
    form.on('submit(moduleSubmit)', function (data) {
        var action = $('input[name="id"]').val() ? "update" : "save";
        $.post(basePath + "/sys/module/" + action, data.field, function (result) {
            if (result.success) {
                parent.layer.closeAll("",true);
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });
});
