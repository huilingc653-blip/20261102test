var dicts;
var form;
bt.use(['form', 'layedit', 'dicts', 'selectType'], function () {
    var $ = bt.jquery;
    dicts = bt.dicts.init();
    form = bt.form();
    bt.selectType.init();

    //监听提交
    form.on('submit(save)', function (data) {
        var action = "/bankacc/biscorppoolconfig/modify";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    form.on('select', function (data) {
        var row = data.othis.closest("tr").attr('row');
        if(data.value == 1 || data.value == 2){
            if($("#matchContent"+ row).is(":hidden")) {
                $("#matchContent" + row).val(data.value);
            }
        }
    });
});

/**
 * 增加一行
 */
addTr = function () {
    var row = $("#matchContentTable tbody tr:last").attr('row');
    if (isNaN(row)) {
        row = 0;
    }
    var newRow = row + 1;
    var html = '<tr row="' + newRow + '">';
    html += '<td class="textCenter"><div class="bt-biz-dict" data-type="select" data-group="FUNDWARN_NUM_OPERATOR"  data-name="matchRule"></div></td>';
    html += '\t<td class="textCenter">\n' +
        '<div class="layui-input-inline">\n' +
        '<select name="leftParenthesis"   data-search="false" data-inputvalue="true">\n' +
        '<option value=" ">'+ bt.lang("common.placeholder.select")+'</option>\n' +
        '<option value="(">(</option>\n' +
        '</select>\n' +
        '</div>\n' +
        '</td>';
    html += '<td class="textCenter"><div class="bt-biz-dict" data-type="select" data-group="nisBusTypeRule.matchElements"  data-name="matchElements" data-biz-name="matchElements' + newRow + '" data-callback="onchangeMatchElements"></div></td>';
    html += '<td class="textCenter"><div class="bt-biz-dict" data-type="select" data-group="nisBusTypeRule.matchRelations"  data-name="matchRelations" data-biz-name="matchRelations' + newRow + '" data-filterData="5,6,7,8" data-callback="onchangeMatchRelations"></div></td>';
    html += '<td class="textCenter"><input type="text" name="matchContent"  id="matchContent' + newRow + '" placeholder="'+bt.lang('table.netbank.nisproject.matchContent')+'" autocomplete="off" class="layui-input" lay-verify="strlength" data-maxlength="100">' +
        '<div class="layui-input-inline" id="number' + newRow + '" style="display: none;">\n' +
        '<select  lay-search="" data-inputvalue="true" >\n' +
        '<option value="1">'+ bt.lang("table.number.odd") +'</option>\n' +
        '<option value="2">'+ bt.lang("table.number.even") +'</option>\n' +
        '</select>\n' +
        '</div></td>';
    html += '<td class="textCenter">\n' +
        '<div class="layui-input-inline">\n' +
        '<select name="rightParenthesis"  data-search="false" data-inputvalue="true">\n' +
        '<option value=" ">'+ bt.lang("common.placeholder.select")+'</option>\n' +
        '<option value=")">)</option>\n' +
        '</select>\n' +
        '</div>\n' +
        '</td>';
    html += '<td class="textCenter"><a href="javascript:void (0)" onclick="deleteDtl(this)">' + bt.lang("common.button.delete") + '</a></td>';
    html += '</tr>';
    $("#matchContentTable").find('tbody').append(html);
    dicts.render();
}
/**
 * 删除
 * @param _this
 */
deleteDtl = function (_this) {
    $(_this).parents("tr").not("#matchContentTr").remove();
}
onchangeMatchElements = function (data) {
    var row = data.othis.closest("tr").attr('row');
    var val = data.value;
    if(val == "amt"){
        $("#matchContent"+row).hide();
        $("#number"+row).show();
        $("#matchContent"+row).val("1");
        dicts.get('matchRelations'+row).attr({'data-filterData': ''});
        dicts.get('matchRelations'+row).val("3");
    }else{
        $("#matchContent"+row).show();
        $("#number"+row).hide();
        $("#matchContent"+row).val("");
        dicts.get('matchRelations'+row).attr({'data-filterData': '5,6,7,8'});
        dicts.get('matchRelations'+row).val("");
    }
}
onchangeMatchRelations = function (data) {
    var row = data.othis.closest("tr").attr('row');
    var val = data.value;
    var matchElements = dicts.get('matchElements'+row).val();
    if(matchElements == "amt" && (val == "1" || val == "2" || val == "3" || val == "4")){
        $("#matchContent"+row).hide();
        $("#number"+row).show();
        $("#matchContent"+row).val("1");
    } else {
        $("#matchContent"+row).show();
        $("#number"+row).hide();
        $("#matchContent"+row).val("");
    }
}