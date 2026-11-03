
bt.use(['form', 'jquery'], function () {
    var form = bt.form()
            , layer = bt.layer
            , $ = bt.jquery;
    //自定义验证规则
    form.verify({
        checkPwd: function (value, item) {
            var minLength = $("#passwordLength").val() ? Number($("#passwordLength").val()) : 6;
            if ($('[name="newPasswordA"]').val().length < minLength) {
                return bt.lang("sys.user.message.minPassword",[minLength]);
            }
            if ($('[name="oldPassword"]').val() === $('[name="newPasswordA"]').val()) {
                return bt.lang("sys.user.message.newPassword.eq.oldPassword");
            }
            if ($('[name="newPassword"]').val() !== $('[name="newPasswordA"]').val()) {
                return bt.lang("sys.user.message.twoeq.newPassword");
            }
            var passwordModel = $("#passwordModel").val();
            if(passwordModel === "0"){
                if(!/^[\d]+$/.test(value)){
                    return bt.lang("sys.user.message.password.number");
                }
            }else if(passwordModel === "1"){
                if(!/^[a-zA-Z]+$/.test(value)){
                    return bt.lang("sys.user.message.password.letter");
                }
            }else if(passwordModel === "2"){
                if(!/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]$/.test(value)){
                    return bt.lang("sys.user.message.password.numberAndLetter");
                }

            }else if(passwordModel === "3"){
                if(!/(?=.*[\d]+)(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+)./.test(value)){
                    return bt.lang("sys.user.message.password.numberAndLetterAndSymbols");
                }
            }
        }
    });

    //监听提交
    form.on('submit(modifySubmit)', function (data) {
        $.get(basePath+'/randomString',function(rs) {
            var key =rs.obj;
            data.field.oldPassword = sm2Encrypt(data.field.oldPassword, key, 1);
            data.field.newPassword = sm2Encrypt(data.field.newPassword, key, 1);
            data.field.newPasswordA = sm2Encrypt(data.field.newPasswordA, key, 1);
			data.field.passkey =key;
            $.post(basePath + "/sys/user/modifyPassword", data.field, function (result) {
                if (result.success) {
                    parent.layer.closeAll();
                }else{
                    location.reload();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        },'JSON');
        return false;
    });
});
