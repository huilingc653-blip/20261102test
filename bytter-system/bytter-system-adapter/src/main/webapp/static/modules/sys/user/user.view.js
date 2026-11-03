bt.use(['form', 'jquery', 'corp'], function () {
    var form = bt.form()
        , layer = bt.layer
        , laydate = bt.laydate
        , $ = bt.jquery;
    bt.corp.init();

    function isEmpty(stringData) {
        if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
            return true;
        } else {
            return false;
        }
    }

    //自定义验证规则
//        form.verify({
//            loginName: function(value){
//                if(value.length < 3){
//                    return '登录账号至少得3个字符！';
//                }
//                if(value.length>30){
//                    return '登录账号不能大于30个字符！';
//                }
//            },
//            username:function(value){
//                if(value.length < 2){
//                    return '用户名至少得2个字符！';
//                }
//                if(value.length>30){
//                    return '用户名不能大于30个字符！';
//                }
//            }
//        });

    //监听提交
    form.on('submit(userSubmit)', function (data) {

        if (typeof (data.field.isAdmin) == "undefined") {
            data.field.isAdmin = 0;
        } else {
            data.field.isAdmin = 1;
        }
        if (typeof (data.field.status) == "undefined") {
            data.field.status = 2;
        } else {
            data.field.status = 1;
        }

        if (typeof (data.field.showReport) == "undefined") {
            data.field.showReport = 0;
        } else {
            data.field.showReport = 1;
        }

        if (typeof (data.field.faceVerifyStatus) == "undefined") {
            data.field.faceVerifyStatus = 0;
        } else {
            data.field.faceVerifyStatus = 1;
        }

        // 钉钉ID
        var dingTalkUserId = $("#dingTalkUserId").val();
        if (!isEmpty(dingTalkUserId)) {
            data.field.dingTalkUserId = dingTalkUserId;
        }
        // 企业微信ID
        var weiXinUserId = $("#weiXinUserId").val();
        if (!isEmpty(weiXinUserId)) {
            data.field.weiXinUserId = weiXinUserId;
        }
        // 飞书ID
        var feishuUserId = $("#feishuUserId").val();
        if (!isEmpty(feishuUserId)) {
            data.field.feishuUserId = feishuUserId;
        }

        var action = $('input[name="id"]').val() ? "update" : "save";
        $.post(basePath + "/sys/user/" + action, data.field, function (result) {
            if (result.success) {
                parent.layer.closeAll("",true);
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });


});
