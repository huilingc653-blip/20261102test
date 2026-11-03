var mulselect, select, $, corp;
bt.use(['form', 'datatable', 'dateType', 'corp', 'mulselect','rateType', 'dicts', 'selectType', 'jquery'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    corp = bt.corp.init();//单位
    bt.dicts.init();//数据字典
    select = bt.selectType.init();
    mulselect = bt.mulselect.init();
    $ = bt.jquery;
    rateType = bt.rateType.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit', function (data) {
        var saveData = getSaveData(data.field)
        var nm = $(this).attr("id");
        if (nm == "submit") {
            saveData.operationType = "1";
        } else {
            saveData.operationType = "0";
        }
        bt.loading.show();
        var action = "/credit/crTaskInfo/submit";
        $.ajax({
            url: basePath + action,
            type: 'POST',
            contentType: 'application/json', // 设置为JSON格式
            data: JSON.stringify(saveData), // 将对象转换为JSON字符串
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    var webObj = bt.navtab.getParentDom();
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();
                }
            },
        });
        return false;
    });

    function getSaveData(field) {
        var obj = {
            scoringList: getTableList(),
            taskNo: $("#taskNo").val(),
            computationalModelId: field.computationalModelId,
            evaluateEndDate: field.evaluateEndDate,
            evaluateStartDate: field.evaluateStartDate,
            orgIds: field.orgId,
            orgType: field.orgType,
            rmk: field.rmk,
            taskCycle: field.taskCycle,
            taskName: field.taskName,
            taskWeight: field.taskWeight
        }
        return obj;
    }

    function getTableList() {
        var obj = {}
        var tableCorps = $('.corp-box').find('input[type="hidden"]');
        var tableList = []
        bt.each(tableCorps, function () {
            var name = $(this).attr('name');
            var id = name.split('_');
            var corpIds = $(this).val();
            obj = {
                id: id[1],
                corpIds: corpIds,
            }
            tableList.push(obj)
        })
        return tableList;
    }

});

var obj = {
    header: [
        {name: 'scoringIndicatorName', title: '指标类别'},
        {name: 'scoringRubric', title: '评分细则'},
        {name: 'scoringIndicatorCoefficient', title: '权重'},
        {name: 'isAddOrSubGrade', title: '是否额外加减分'},
    ],
    body: []
}

function getModelScoring(data) {
    $.get(basePath + '/creditRating/computationalModel/getModelScoringJson?modelId=' + data.id, function (result) {
        $('#tableBody').html(reateTbodyHtml(obj.header, result))
        var tds = $("#dataTable tbody tr:first").find("td");
        var titleRowSpan = 0;
        for (var i = 0; i < tds.length; i++) {
            titleRowSpan += Number($(tds[i]).attr("colspan") || 1);
        }
        $("#nameTitle").attr("colspan", titleRowSpan - 4);
        bt.corp.init();
    }, "json")
}

function resetCode() {
    getModelScoring({id: ''})
}

var topRowSpan = 0;
var isTopNode = false;

function reateTbodyHtml(columns, list) {
    var defaultCorpId = $("#defaultCorpId").val();
    var html = '';
    for (var i = 0; i < list.length; i++) {
        var row = list[i]
        html += '<tr>'
        if (row.childrenScoringList && row.childrenScoringList.length) {
            topRowSpan = row.rowSpan
            isTopNode = true
            html += createTbodyHtmlByChildren(row, columns,row.scoringIndicatorId)
        } else {
            for (var l = 0; l < columns.length; l++) {
                var colSpan = 1;
                if (l == 0) {
                    colSpan = row.colSpan || 1;
                }
                var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                if(columns[l].name=='scoringIndicatorCoefficient'){
                    show = bt.rateType.getFun().format(show, 'percent', 4, ',');
                }else if(columns[l].name=='isAddOrSubGrade'){
                    if(show=='1'){
                        show='<div style="text-align: center;">是</div>'
                    }else {
                        show='<div style="text-align: center;">否</div>'
                    }
                }
                html += '<td colspan="' + colSpan + '" rowspan="1">' + show + '</td>'
            }
            html += '<td rowspan="' + (row.rowSpan || 1) + '"><div  class="layui-inline corp-box"><div class="bt-biz-corp" data-type="selectTree" data-name="corpIds_' + row.scoringIndicatorId + '" data-resource="credit:crTaskInfo:edit" data-scope="all" data-required="true" data-tree-type="check"></div></div></td>' //操作
            html += '</tr>'
        }

    }
    return html
}

function createTbodyHtmlByChildren(list, columns,topParentId) {
    var defaultCorpId = $("#defaultCorpId").val();
    var html = '';
    html += '<td rowspan="' + (list.rowSpan || 1) + '" colspan="1">' + list[columns[0].name] + '</td>'
    for (var i = 0; i < list.childrenScoringList.length; i++) {
        var row = list.childrenScoringList[i];
        // i 不等于0时，需要添加<tr>
        if (i) {
            html += '<tr>'
        }
        if (row.childrenScoringList && row.childrenScoringList.length) {
            html += createTbodyHtmlByChildren(row, columns,topParentId)
        } else {
            for (var l = 0; l < columns.length; l++) {
                var colSpan = 1;
                if (l == 0) {
                    colSpan = row.colSpan || 1;
                }
                var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                if(columns[l].name=='scoringIndicatorCoefficient'){
                    show = bt.rateType.getFun().format(show, 'percent', 4, ',');
                }else if(columns[l].name=='isAddOrSubGrade'){
                    if(show=='1'){
                        show='<div style="text-align: center;">是</div>'
                    }else {
                        show='<div style="text-align: center;">否</div>'
                    }
                }
                html += '<td  colspan="' + colSpan + '" rowspan="1">' + show + '</td>'
            }
            if (!i) {
                if (isTopNode) {
                    html += '<td style="text-align:center" rowspan="' + (topRowSpan || 1) + '"><div class="layui-inline corp-box"><div class="bt-biz-corp" data-type="selectTree" data-name="corpIds_' + topParentId + '" data-resource="credit:crTaskInfo:edit" data-required="true" data-scope="all" data-tree-type="check"></div></div></td>'
                    isTopNode = false;
                }
            }
            html += '</tr>'
        }
    }


    return html
}

//获取机构名称
function getOrgData(data) {
    let joinedString = "";
    for (let i = 0; i < data.length; i++) {
        joinedString += data[i].value;
        if (i !== data.length - 1) {
            joinedString += ",";
        }
    }
    mulselect.get('orgId').attr({'data-url': basePath + '/credit/institutionalRawData/queryByTypes?types=' + joinedString})
}
