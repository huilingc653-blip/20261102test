bt.use(['form', 'dicts', 'dateType', 'corp', 'selectType', 'mulselect'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();

    bt.corpCallback = function (id) {
        $("#tagTemplate").html("");
        $("#tagExposure").html("");
        $.ajax({
            url: basePath + "/foreign/fexCorpExposure?isCollectCorp",
            method: 'POST',
            async: false,
            data: {corpId: id},
            dataType: 'json',
            success: function (data) {
                if(data.obj){
                    selectType.get("templateId").attr({"data-value": "",
                        "data-url": basePath + "/foreign/fexCorpExposureTemplate/json?corpId=" + 0});
                    layer.alert(bt.lang("foreign.message.exposure.collectCorp.input"), {icon: 7});
                } else {
                    selectType.get("templateId").attr({"data-value": "",
                        "data-url": basePath + "/foreign/fexCorpExposureTemplate/json?corpId=" + id});
                }
                selectType.render();
            }
        });
    }
    var corp = bt.corp.init();

    //单位模板回调函数
    bt.templateIdCallback = function (data) {
        var corpId = corp.get("corpId").val();
        var templateId = selectType.get("templateId").val();
        var enrolDate = dateType.get("enrolDate").val();
        //获取单位敞口信息
        $.ajax({
            url: basePath + "/foreign/fexExposure?getCorpExposure",
            method: 'POST',
            data: {corpId: corpId, templateId: templateId, enrolDate: enrolDate},
            dataType: 'json',
            success: function (result) {
                if (result.obj && result.obj.id) {
                    $("#tagTemplate").html("");
                    $("#tagExposure").html("");
                    layer.alert(bt.lang("foreign.message.exposure.exist.input"), {icon: 7});
                } else {
                    $genTemplateHtml(corpId, templateId);
                    $genExposureHtml(corpId, templateId, '0');
                }
            }
        });
    };

    //币别回调函数
    bt.curIdCallback = function (data) {
        var corpId = corp.get("corpId").val();
        var templateId = selectType.get("templateId").val();
        var cur = bt.mulselect.get("curId").val();
        var curId = "";
        for(var i=0;i<cur.length;i++){
            if(cur[i] && cur[i].id) {
                curId = curId + cur[i].id + ',';
            }
        }
        if (curId == "") {
            curId = $("#curIds").val();
        }
        $genExposureHtml(corpId, templateId, curId);
    };

    templateBeforeInit = function (data,arguments) {
        var show = "("+arguments.templateCode+")" + arguments.templateName;
        return show;
    }
    var selectType = bt.selectType.init();

    /**
     * 生成敞口模板信息
     * @return
     */
    $genTemplateHtml = function (corpId,templateId) {
        var url = basePath + "/foreign/fexCorpExposureTemplate/viewInput/" + corpId + "/" + templateId + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagTemplate").html("");
                $("#tagTemplate").html(json);
                form.render();
                form.renderIcon();
            }
        });
    }

    /**
     * 生成敞口上报信息
     * @return
     */
    $genExposureHtml = function (corpId, templateId, curId) {
        var url = basePath + "/foreign/fexExposure/viewInput/" + corpId + "/" + templateId + "/" + curId + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagExposure").html("");
                $("#tagExposure").html(json);
                form.render();
                form.renderIcon();
            }
        });
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $.post(basePath + "/foreign/fexExposure/inputSave", $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });
});
