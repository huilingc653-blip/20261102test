bt.use(['form', 'jquery'], function () {
    var form = bt.form()
        , layer = bt.layer
        , $ = bt.jquery;

    //阻止F5或者鼠标右键刷新,使锁屏失效。
    document.onkeydown = function () {
        if (event.keyCode === 116) {
            event.keyCode = 0;
            event.returnValue = false;
        }
    };
    $(document).keydown(function (e) {
        var doPrevent;
        if (e.keyCode === 8) {
            var d = e.srcElement || e.target;
            if (d.tagName.toUpperCase() === 'INPUT' || d.tagName.toUpperCase() === 'TEXTAREA') {
                doPrevent = d.readOnly || d.disabled;
            } else {
                doPrevent = true;
            }
        } else {
            doPrevent = false;
        }
        if (doPrevent) {
            e.preventDefault();
        }
    });

    document.oncontextmenu = function () {
        event.returnValue = false;
    };
    //监听提交
    form.on('submit(unLockScreen)', function (data) {
        $.post(basePath + "/sys/user/unLockScreen", data.field, function (result) {
            if (result.success) {
                parent.layer.closeAll();
            } else {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
        return false;
    });
});
