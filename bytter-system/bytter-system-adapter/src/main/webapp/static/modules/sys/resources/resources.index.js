bt.use(['jquery', 'layer'], function () {
    var menuId = $("#menuId").val();
    $ = layui.jquery;
    $getId = function (currnet) {
        var id = currnet.parents('tr').data('id');
        return id ? id : "";
    }

    //增加项
    $(".add").click(function () {
        var id = $getId($(this));
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/resources/view/?menuId=" + menuId,
            end: function () {
                location.reload();
            }
        });
    });

    //修改
    $(".update").click(function () {
        var id = $getId($(this));
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/resources/view/?id=" + id,
            end: function () {
                location.reload();
            }
        });
    });

    //删除
    $(".delete").click(function () {
        var id = $getId($(this));
        var result = "";
        $.ajax({
            url: basePath + "/sys/resources/checkUsedByRoles",
            method: 'POST',
            async: false,
            data: {resourceId: id},
            dataType: 'json',
            success: function (data) {
                result = data.obj;
            }
        });
        if (result.length) {
            parent.layer.confirm(bt.lang('sys.resources.message.confirm.usedbyroles', [result]), function (index) {
                deleteForm(id);
            });
        } else {
            deleteForm(id);
        }
        return false;
    });

    deleteForm = function (id) {
        $.get(basePath + "/sys/resources/delete/" + id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            location.reload();
        }, 'JSON');
    }


});
