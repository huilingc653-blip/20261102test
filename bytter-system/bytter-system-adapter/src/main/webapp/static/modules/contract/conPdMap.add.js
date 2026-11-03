var $, form, dataTable, dicts, conType;
bt.use(['form', 'select', 'bttable', 'dicts', 'conType'], function () {
    $ = bt.jquery, form = bt.form();

    //监听保存
    form.on('submit(save)', function (data) {
        bt.loading.show();
        var action = "/contract/conPdMap/saveOrUpdate";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
            });
        }, 'JSON');
        return false;
    });
    dicts = bt.dicts.init();
    conType = bt.conType.init();
});


selectDict = function selectDict(data, text, selectedData) {
    $("#fieldNameTab tr").remove();
    if (selectedData) {
        select(selectedData.value, conType.get('typeName').val());
    }
};

selectField = function selectField(elem, text, selectedData) {
    $("#fieldNameTab tr").remove();
    if (selectedData) {
        select(dicts.get('pdCode').val(), selectedData.id);
    }
};


// 公共方法用于查询合同类型映射，添加tr
function select(data, value) {

    if (data && value && $.trim(data) && $.trim(value)) {
        $.post(basePath + "/contract/conPdMap/listTypeField", {'conTypeId': value, 'pdCode': data}, function (result) {
            var conWarnRulesHtml = '';
            for (var i = 0; i < result.length; i++) {
                conWarnRulesHtml += '<tr>';
                conWarnRulesHtml += '<input type="hidden" value="' + result[i].conTypeDtlId + '" name="conTypeDtlIds"/>';
                conWarnRulesHtml += '<td style="text-align: center">' + bt.lang(result[i].fieldName) + '</td>';
                conWarnRulesHtml += '<td><div class="bt-biz-dict" data-group="' + data + '" data-type="select" data-name="pdDtlCodes" data-value="' + result[i].pdDtlCode + '" ';
                if (result[i].requiredSign === "1") {
                    conWarnRulesHtml += ' data-required="true" ';
                }
                conWarnRulesHtml += '></div></td>';
                conWarnRulesHtml += '<tr>';
            }
            $("#fieldNameTab").append(conWarnRulesHtml);
            dicts.render();
        }, 'JSON');
    }

}


