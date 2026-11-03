
var versionNo = '';
bt.use(['form','layedit','dicts', 'selectType', 'moneyType','element', 'selectTable','dateType','corp', 'btUpload','region','allExternalBankAcc'], function(){
    var $ = bt.jquery,form = bt.form();

    // 模块选择
    $('#templateBtn').click(function () {
        var selectedIds = $("#templateId").val() || '';
        parent.layer.open({
            title:'选择模版',
            type: 2,
            area: ['1000px', '700px'],
            maxmin: true,
            content: basePath + '/foreign/fexTranConfigTemplate/fexConfigTemplateMulselect?selectedIds=' + encodeURIComponent(selectedIds),
        });
    })


    // 模块选择
    $('#custFieldBtn').click(function () {
        var selectedIds = $("#custFields").val() || '';
        parent.layer.open({
            title:'选择字段',
            type: 2,
            area: ['1000px', '700px'],
            maxmin: true,
            content: basePath + '/foreign/fexTranReqConfig/chooseCustFields?scope=4&selectedIds=' + encodeURIComponent(selectedIds),
        });
    })


    // 监听提交
    form.on('submit(submit)', function(data){

        var templateIds = $("#templateId").val();
        var custFieldIds = $("#custFields").val();
        if (!templateIds || !custFieldIds){
            return false;
        }
        var saveData = {
            templateIds: templateIds,
            custFieldIds: custFieldIds
        };

        bt.loading.show();
        var action = "/foreign/fexTranReqConfig/batchSaveTemplateConfig";

        $.post(basePath + action, saveData, function (result) {
            bt.loading.hide();
            var icon = result.success ? 1 : 2;
            if (parent.layer) {
                parent.layer.alert(result.msg, { icon: icon });
            } else {
                layer.alert(result.msg, { icon: icon });
            }
        }, "json");
        return false;
    });

});

function choosetemplateCallBack(datas){
    var templateNames = [];
    var templateIds = [];
    for (var i=0; i<datas.length; i++){
        templateIds.push(datas[i].id);
        templateNames.push(datas[i].templateName);
    }
    $("#templateId").val(templateIds.join(","));
    $("#templateName").val(templateNames.join(","));
}


function addFieldCallBack(datas){
    var fieldNames = [];
    var fieldIds = [];
    for (var i=0; i<datas.length; i++){
        fieldIds.push(datas[i].id);
        fieldNames.push(datas[i].fieldName);
    }

    $("#custFields").val(fieldIds.join(","));
    $("#custFieldName").val(fieldNames.join(","));
}