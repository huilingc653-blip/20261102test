bt.use(['form', 'layedit', 'selectType', 'dicts','corp','mulselect'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    //模板回调函数
    bt.templateCallback = function (data) {
        if (data.id) {
            $genTemplateHtml(data.id);
        } else {
            $("#tagTemplate").html("");
        }
    };
    templateBeforeInit = function (data,arguments) {
        var show = "("+arguments.templateCode+")" + arguments.templateName;
        return show;
    }
    bt.selectType.init();
    bt.corp.init();
    bt.mulselect.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fexCorpExposureTemplate/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 生成敞口模板信息
     * @return
     */
    $genTemplateHtml = function (templateId) {
        var url = basePath + "/foreign/fexExposureTemplate/viewCommon/" + templateId + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagTemplate").html("");
                $("#tagTemplate").html(json);
                form.render();
            }
        });
    }
});
