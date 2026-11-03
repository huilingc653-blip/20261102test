var selectTypes, moneyTypes, moneyFun, rateType, dicts, rateFun;
bt.use(['form', 'element', 'bttable', 'selectType', 'moneyType', 'dicts', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    rateType = bt.rateType.init();//利率
    rateFun = bt.rateType.getFun();
    dicts = bt.dicts.init();//字典
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    getScoringData();
    //监听提交
    form.on('submit', function (data) {
        bt.loading.show();
        var action = "/creditRating/computationalModel/submit";
        var scCoefficientMap = makeCoefficients();
        if (!scCoefficientMap) {
            bt.loading.hide();
            //请选择指标类别
            parent.layer.msg("请选择指标类别", {icon: 2});
            return false;
        }
        data.field.scCoefficientMap = scCoefficientMap;
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            }
        }, 'JSON');

        return false;
    });
})

function makeCoefficients() {
    var scCoefficients = {};
    var oldScoringIds = $("#oldScoringIds").val();
    var newScoringIds = $("#newScoringIds").val();
    var sumNum = 0;
    if (!bt.empty(oldScoringIds)) {
        var oldIdArray = oldScoringIds.split(',');
        for (var i = 0; i < oldIdArray.length; i++) {
            var thisValue = moneyFun.div(Number($("#" + oldIdArray[i]).val()), 100, 6);
            if (!bt.empty(thisValue)) {
                scCoefficients[oldIdArray[i]] = thisValue;
                sumNum = moneyFun.add(sumNum, Number(thisValue));
            }
        }
    }
    if (!bt.empty(newScoringIds)) {
        var newIdArray = newScoringIds.split(',');
        for (var i = 0; i < newIdArray.length; i++) {
            var thisValue = moneyFun.div(Number($("#" + newIdArray[i]).val()), 100, 6);
            if (!bt.empty(thisValue)) {
                scCoefficients[newIdArray[i]] = thisValue;
                sumNum = moneyFun.add(sumNum, Number(thisValue));
            }
        }
    }
    if (bt.empty(oldScoringIds) && bt.empty(newScoringIds)) {
        return false;
    }
    //需求变动不要求总权重合计100%
    // if(Number(sumNum)!=1){
    //     bt.loading.hide();
    //     parent.layer.msg(bt.lang("总权重之和应为100"), {icon: 2});
    //     return false;
    // }
    return scCoefficients;
}


function changModel(data) {
    $("#modelFormulaNumber").val(data.modelformulanumber);
}

//选择指标跳转
function addScoringCallback() {
    var oldScoringIds = $("#oldScoringIds").val();
    var newScoringIds = $("#newScoringIds").val();
    var scoringIds;
    if (!bt.empty(oldScoringIds)) {
        scoringIds = oldScoringIds;
        if (!bt.empty(newScoringIds)) {
            scoringIds = scoringIds + "," + oldScoringIds;
        }
    } else {
        scoringIds = newScoringIds;
    }
    parent.layer.open({
        type: 2,
        title: bt.lang("credit.indicator.type.message"),
        area: ['960px', '600px'],
        maxmin: true,
        content: basePath + "/creditRating/computationalModel/tickIndicator?scoringIds=" + scoringIds,
        end: function () {

        }
    });
}

function test() {
    $.ajax({
        url: basePath + "/creditRating/computationalModel/getModelScoringJson",
        method: 'GET',
        data: {"modelId": "253fc0ae5b774fcbad3fa8270e885fe9"},
        dataType: 'json',
        success: function (result) {
            console.log(result);
        }
    });
}

//刷新指标展示数据
function getScoringData() {
    var modelId = $("#id").val();
    var copyModelId = $("#copyModelId").val();
    if (!bt.empty(copyModelId)) {
        modelId = copyModelId;
    }
    var oldScoringIds = $("#oldScoringIds").val();
    var newScoringIds = $("#newScoringIds").val();
    //定义一个对象，包含指标id和对应权重
    //定义一个集合
    var scCoefficientsArray = [];
    //遍历所有tr，封装成对象
    var coeffientInput = $(".coeffucient");
    for (var i = 0; i < coeffientInput.length; i++) {
        var scCoefficients = {
            id: coeffientInput[i].id,
            value: coeffientInput[i].value
        };
        scCoefficientsArray.push(scCoefficients);
    }
    var url = basePath + "/creditRating/computationalModel/getScoringForShow";
    var param = {
        newScoringIds: newScoringIds,
        oldScoringIds: oldScoringIds,
        modelId: modelId,
        scCoefficientsArray: scCoefficientsArray
    }
    $.ajax({
        url: url,
        method: 'POST',
        contentType: 'application/json', // 设置为JSON格式
        data: JSON.stringify(param),
        dataType: 'json',
        success: function (result) {
            console.log(result);
            makeTableDate(result);
        }
    }, 'JSON');
}

var checkBack = function (checkedIds) {
    //新选指标
    $("#newScoringIds").val(checkedIds);
    //固化指标与新选去重
    var oldScoringIds = $("#oldScoringIds").val();
    if (!bt.empty(oldScoringIds)) {
        var oldScoringArray = oldScoringIds.split(",");
        var newOldScoringArray = oldScoringArray;
        for (var i = 0; i < oldScoringArray.length; i++) {
            if (checkedIds.split(",").indexOf(oldScoringArray[i]) >= 0) {
                // oldScoringArray.splice(i, 1)
                newOldScoringArray = newOldScoringArray.filter(item => item !== oldScoringArray[i]);
            }
        }
        oldScoringIds = newOldScoringArray.toString();
        $("#oldScoringIds").val(oldScoringIds);
    }

    getScoringData();
}

/**
 * 删除相应的行
 */
function remoTr(el) {
    var that = $(el);
    var deleteIds = $(el).attr("scid");
    var currentTr = that.closest('tr');
    if (that.data('parentids')) {
        var parentIds = that.data('parentids').split(",");
        for (var p = parentIds.length - 1; p >= 0; p--) {
            var parentId = parentIds[p];
            var rowspan = $(".class_" + parentId).attr('rowspan')
            rowspan--;
            if (rowspan == 0) {
                deleteIds = deleteIds + "," + parentId;
            }
            $(".class_" + parentId).attr('rowspan', rowspan)
            //
            if (currentTr.find(".class_" + parentId)[0] && rowspan >= 1) {
                currentTr.next('tr').prepend($(".class_" + parentId))
            }
        }
    }
    currentTr.remove();
    //数据id的处理
    var oldScoringIds = $("#oldScoringIds").val();
    var newScoringIds = $("#newScoringIds").val();
    var oldIdArray = oldScoringIds.split(',');
    var newIdArray = newScoringIds.split(',');
    var deleteIdArray = deleteIds.split(',');
    for (var d = 0; d < deleteIdArray.length; d++) {
        var delId = deleteIdArray[d];
        for (var i = 0; i < oldIdArray.length; i++) {
            if (delId == oldIdArray[i]) {
                oldIdArray.splice(i, 1)
            }
        }
        for (var i = 0; i < newIdArray.length; i++) {
            if (delId == newIdArray[i]) {
                newIdArray.splice(i, 1)
            }
        }
    }
    oldScoringIds = oldIdArray.toString();
    newScoringIds = newIdArray.toString();
    $("#oldScoringIds").val(oldScoringIds);
    $("#newScoringIds").val(newScoringIds);

}

function makeTableDate(result) {
    // $('#ScoringContract').html(reateTbodyHtml(obj.header,result));
    $('#dataTable').find('tbody').remove();
    $('#dataTable').append(reateTbodyHtml(obj.header, result))

    $("#dataTable").dad({
        draggable: '.drage-item',
        target: '>tbody',
        callback: function (elem) {
            var commonMenus = [];
            console.log(55555)
        }
    })

    var tds = $("#dataTable tbody tr:first").find("td");
    var titleRowSpan = 0;
    for (var i = 0; i < tds.length; i++) {
        titleRowSpan += Number($(tds[i]).attr("colspan") || 1);
    }

    $("#nameTitle").attr("colspan", titleRowSpan - 4);
    rateType.render();
}


var obj = {
    header: [
        {name: 'scoringIndicatorName', title: bt.lang("table.creditRating.indicatorCategory")},
        {name: 'scoringRubric', title: bt.lang("credit.indicator.scoringRubric")},
        {name: 'isAddOrSubGrade', title: bt.lang("credit.indicator.extraAddOrSub")},
        {name: 'scoringIndicatorCoefficient', title: bt.lang("credit.indicator.weight")},
    ],
    body: []
}

function reateTbodyHtml(columns, list) {
    var html = '';
    for (var i = 0; i < list.length; i++) {
        var row = list[i]
        html += '<tbody><tr>'
        if (row.childrenScoringList && row.childrenScoringList.length) {
            html += createTbodyHtmlByChildren(row, columns)
            html += '</tbody>'
        } else {
            for (var l = 0; l < columns.length; l++) {
                var colSpan = 1;
                if (l == 0) {
                    colSpan = row.colSpan || 1;
                }
                if (l == 3) {
                    var scoringIndicatorCoefficient = row["scoringIndicatorCoefficient"];
                    var isAddOrSubGrade = bt.empty(row["isAddOrSubGrade"]) ? "" : row["isAddOrSubGrade"];
                    if (1 == isAddOrSubGrade && scoringIndicatorCoefficient == null) {
                        scoringIndicatorCoefficient = "1";
                    }
                    html += '<td colspan="' + colSpan + '" rowspan="1">' +
                        '<input type="text" id="' + row["scoringIndicatorId"] + '" value="' + scoringIndicatorCoefficient +
                        '" lay-verify="required" class="layui-input bt-biz-rateType coeffucient" data-width="100" data-decimal="2"></td>'
                } else {
                    var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                    if (columns[l].name == 'isAddOrSubGrade') {
                        if (show == '1') {
                            show = '<div style="text-align: center;"><i class="layui-icon">&#xe605;</i></div>'
                        } else {
                            show = '';
                        }
                    }
                    if (l == 0) {
                        html += '<td colspan="' + colSpan + '" rowspan="1" class="drage-item">' + show + '</td>'
                    } else {
                        html += '<td colspan="' + colSpan + '" rowspan="1" >' + show + '</td>'
                    }

                }
            }
            html += '<td style="text-align:center"><button type="button" class="layui-btn  delButn" scId="' + row["scoringIndicatorId"] + '" onClick="remoTr(this)">' + bt.lang("common.button.delete") + '</button></td>' //操作
            html += '</tr></tbody>'
        }
    }
    return html
}

function createTbodyHtmlByChildren(list, columns, parentId) {
    var html = '';
    var parentRowId = bt.empty(parentId) ? list.scoringIndicatorId : parentId + "," + list.scoringIndicatorId;
    html += '<td rowspan="' + (list.rowSpan || 1) + '" colspan="1" class="class_' + list.scoringIndicatorId + ' drage-item" style="min-width: 80px;">' + list[columns[0].name] + '</td>'
    for (var i = 0; i < list.childrenScoringList.length; i++) {
        var row = list.childrenScoringList[i];
        // i 不等于0时，需要添加<tr>
        if (i) {
            html += '<tr>'
        }
        if (row.childrenScoringList && row.childrenScoringList.length) {
            html += createTbodyHtmlByChildren(row, columns, parentRowId)
        } else {
            for (var l = 0; l < columns.length; l++) {
                var colSpan = 1;
                if (l == 0) {
                    colSpan = row.colSpan || 1;
                }
                if (l == 3) {
                    var scoringIndicatorCoefficient = row["scoringIndicatorCoefficient"];
                    var isAddOrSubGrade = bt.empty(row["isAddOrSubGrade"]) ? "" : row["isAddOrSubGrade"];
                    if (1 == isAddOrSubGrade && scoringIndicatorCoefficient == null) {
                        scoringIndicatorCoefficient = "1";
                    }
                    html += '<td colspan="' + colSpan + '" rowspan="1">' +
                        '<input type="text"  id="' + row["scoringIndicatorId"] + '" value="' + scoringIndicatorCoefficient +
                        '" lay-verify="required" class="layui-input bt-biz-rateType scid coeffucient" data-decimal="2" data-width="100"></td>'
                } else {
                    var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                    if (columns[l].name == 'isAddOrSubGrade') {
                        if (show == '1') {
                            show = '<div style="text-align: center;"><i class="layui-icon">&#xe605;</i></div>'
                        } else {
                            show = '';
                        }
                    }
                    html += '<td colspan="' + colSpan + '" rowspan="1" style="min-width: 80px;">' + show + '</td>'
                }
            }
            html += '<td style="text-align:center"><button type="button" class="layui-btn delButn" data-parentids="' + parentRowId + '" scId="' + row["scoringIndicatorId"] + '" onClick="remoTr(this)">' + bt.lang("common.button.delete") + '</button></td>' //操作
            html += '</tr>'
        }
    }
    return html
}


