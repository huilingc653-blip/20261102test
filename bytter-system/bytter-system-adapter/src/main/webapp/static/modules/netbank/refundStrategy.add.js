var dicts,moneyType;
bt.use(['form', 'selectType', 'datatable', 'dicts', 'bank', 'moneyType', 'mulselect'], function () {
    bt.mulselect.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    bt.bank.init();
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/strategy/save";
        var executeSort=$("#executeSort").val();
        var bankTypeId = dicts.get("bankTypeId").val();
        var isType = '2';
        data.field.rules = data.field.rules.toString();
        var isExistId = dicts.get("isExistId").val();

        if (!isEmpty(executeSort) && !isEmpty(isType)) {
            $.ajax({
                url: basePath + "/netbank/strategy/executeSort",
                method: "POST",
                dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify({'type': isType,'executeSort':executeSort,'rules':data.field.rules,'isExistId':isExistId,'bankTypeIds':bankTypeId}),
                success: function (t) {
                    if (t == "1") {
                        layer.alert(bt.lang('table.receipt.execute.strategyNotRepeat'), {icon: 7});
                    } else if(t == "2"){
                        layer.alert(bt.lang('table.receipt.execute.sortNotRepeat'), {icon: 7});
                    } else{
                        $.post(basePath + action,$('#refundStrategyForm').serialize(), function (result) {
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
                    }
                },
            });
            return false;
        }

    });

});
/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 增加一行
 */
addTr = function () {
    var row = $("#matchContentTable tr:last").attr('row');
    if (isNaN(row)) {
        row = 0;
    }else{
        row = Number(row);
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
    html += '<td class="textCenter"><div class="bt-biz-dict" data-type="select" data-group="nisBusTypeRule.matchElements"  data-name="matchElements" data-biz-name="matchElements' + newRow + '"  data-filterData="bankAcc,accName,oppAccNo,oppAccName,curId,corpId,postscript,amt" data-callback="onchangeMatchElements"></div></td>';
    html += '<td class="textCenter"><div class="bt-biz-dict" data-type="select" data-group="nisBusTypeRule.matchRelations"  data-name="matchRelations" data-biz-name="matchRelations' + newRow + '" data-filterData="5,6,7,8,9,10" data-callback="onchangeMatchRelations"></div></td>';
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
        dicts.get('matchRelations'+row).attr({'data-filterData': '5,6,7,8,9,10'});
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
