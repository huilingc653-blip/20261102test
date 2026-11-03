bt.use(['form', 'jquery', 'corp', 'mulselect'], function () {
    var form = bt.form()
        , layer = bt.layer
        , $ = bt.jquery;

    //自定义验证规则
    form.verify({
        name: function (value) {
            if (value.length > 50) {
                // 名称不能大于50个字符！
                return '名称不能大于50个字符！';
            }
        },
        sort: function (value) {
            var pattern = /^-?\d{0,5}$/;
            if (value && !pattern.test(value)) {
                // 排序值为正整数，并且不能大于5位数！
                return bt.lang('sys.menu.sort.show');
            }
        },
        tag: function (value) {
            if (value && value.length > 100) {
                // 标签名不能大于100个字符！
                return '标签名不能大于100个字符！';
            }
        },
        content: function (value) {
            if (value && value.length > 500) {
                // 模版不能大于500个字符！
                return '模版不能大于500个字符！';
            }
        },
        listUrl: function (value) {
            if (value && value.length > 255) {
                // 列表跳转地址不能大于255个字符！
                return '列表跳转地址不能大于255个字符！';
            }
        },
        detailUrl: function (value) {
            if (value && value.length > 255) {
                // 详情跳转地址不能大于255个字符！
                return '详情跳转地址不能大于255个字符！';
            }
        },
        resourceContent: function (value) {
            if (value && value.length > 255) {
                // 资源不能大于255个字符！
                return '资源不能大于255个字符！';
            }
        }
    });

    // var userSelect = bt.mulselect.init();
    // parent.userIdLayerIndex = null;
    $("#userIdsSearch").click(function () {
        parent.layer.open({
            title: '用户选择',
            type: 2,
            area: ['1000px', '800px'],
            maxmin: true,
            content: basePath + "/sys/user/selectUserPage"
        })
    })

    // 用户信息选择回调
    bt.getUerInfo = function (data) {
        // console.log("返回：" + JSON.stringify(data));
        var userNames = $("#userNames").val();
        if (userNames) {
            userNames += "," + data.loginName;
        } else {
            userNames = data.loginName;
        }
        $("#userNames").val(userNames);
        var userIds = $("#userIds").val();
        if (userIds) {
            userIds += "," + data.id;
        } else {
            userIds = data.id;
        }
        $("#userIds").val(userIds);
    }


    bt.selectTree.init({
        url: basePath + "/sys/notification/categoryTree",
        elem: ".notificationCategory-parentId",
        name: 'parentId',
        selected: $("#categoryParentId").val(),
        currentNode: '$!{category.id}',
        inputBeforeClick:function (){
            return !$('#categoryId').val()
        }
    });
    bt.selectTree.init({
        url: basePath + "/sys/menu/tree",
        elem: ".menu-parentId",
        name: 'menuId',
        selected: $("#categoryMenuId").val(),
        placeholder: bt.lang('sys.notification.category.menuBelong'),
        currentNode: '$!{category.id}',
        inputBeforeClick:function (){
            return !$('#categoryId').val()
        }
    });
    //监听指定开关
    var sendRole = $('.send-role');
    var sendUser = $('.send-user');
    form.on('radio(notifyObjectType)', function (data) {
        if (data.value == 0) {
            sendRole.addClass('hidden');
            sendUser.addClass('hidden');
        } else if (data.value == 1) {
            sendRole.removeClass('hidden');
            sendUser.addClass('hidden');
        } else if (data.value == 2) {
            sendRole.addClass('hidden');
            sendUser.removeClass('hidden');
        }
    });
    //监听提交
    form.on('submit(categorySubmit)', function (data) {
        //if(data.field.type == 'on'){
        //    data.field.type = 1;
        //}else{
        //    data.field.type = 2;
        //}
        // if(data.field.roleFilter == 'on'){
        //     data.field.roleFilter = 0;
        // }else{
        //     data.field.roleFilter = 1;
        // }
        var roleId = [];
        $('input[type="checkbox"]:checked').each(function () {
            roleId.push($(this).val());
        });
        data.field.roleIds = roleId;
        var userNames = [];
        var splitIds = data.field.userNames.split(",");
        for (var i = 0; i < splitIds.length; i++) {
            userNames.push(splitIds[i]);
        }
        data.field.userNames = userNames;

        $.post(basePath + "/sys/notification/categoryView", data.field, function (result) {
            if (result.success) {
                // 在window添加一个保存成功的标识，用于判断是否要刷新主页面
                window.top.editSuccessForCategoryViewFlag = true
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });


});

// 重置
function categoryViewReset(){
    window.location.reload();
}
