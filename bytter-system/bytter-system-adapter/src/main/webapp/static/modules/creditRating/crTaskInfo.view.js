var mulselect, select, $, corp;
var obj = {
    header: [
        {name: 'scoringIndicatorName', title: '指标类别'},
        {name: 'scoringRubric', title: '评分细则'},
        {name: 'scoringIndicatorCoefficient', title: '权重'},
        {name: 'isAddOrSubGrade', title: '是否额外加减分'},
    ],
    body: []
}
bt.use(['form', 'datatable', 'dateType', 'corp', 'mulselect', 'dicts', 'selectType', 'jquery','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    corp = bt.corp.init();//单位
    bt.dicts.init();//数据字典
    select = bt.selectType.init();
    mulselect = bt.mulselect.init();
    $ = bt.jquery;

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    createModelScoring();
});

function createModelScoring() {
    var taskInfoId = $("#taskInfoId").val();
    $.get(basePath + '/credit/crTaskInfo/getViewData?id=' + taskInfoId, function (result) {
        $('#tableBody').html(reateTbodyHtmlInit(obj.header, result.scoringList))
        var tds = $("#dataTable tbody tr:first").find("td");
        var titleRowSpan = 0;
        for (var i = 0; i < tds.length; i++) {
            titleRowSpan += Number($(tds[i]).attr("colspan") || 1);
        }
        $("#nameTitle").attr("colspan", titleRowSpan - 4);
        bt.corp.init();
    }, "json")
}

var topRowSpan = 0;
var isTopNode = false;
var initCorpIds = '';

function reateTbodyHtmlInit(columns, list) {
    var html = '';
    for (var i = 0; i < list.length; i++) {
        titleRowSpan = 0;
        var row = list[i]
        html += '<tr>'
        if (row.childrenScoringList && row.childrenScoringList.length) {
            topRowSpan = row.rowSpan
            isTopNode = true
            initCorpIds = row.corpIds
            html += createTbodyHtmlByChildrenInit(row, columns)
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
            html += '<td rowspan="' + (row.rowSpan || 1) + '">' + row.corpIds + '</td>' //操作
            html += '</tr>'
        }

    }
    return html
}

function createTbodyHtmlByChildrenInit(list, columns) {
    var html = '';
    html += '<td rowspan="' + (list.rowSpan || 1) + '">' + list[columns[0].name] + '</td>'
    for (var i = 0; i < list.childrenScoringList.length; i++) {
        var row = list.childrenScoringList[i];
        // i 不等于0时，需要添加<tr>
        if (i) {
            html += '<tr>'
        }
        if (row.childrenScoringList && row.childrenScoringList.length) {
            html += createTbodyHtmlByChildrenInit(row, columns)
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
            if (!i) {
                if(isTopNode){
                    html += '<td style="text-align:center" rowspan="' + (topRowSpan || 1) + '">' + initCorpIds + '</td>'
                    isTopNode = false;
                }
            }
            html += '</tr>'
        }
    }
    return html
}

var closeDoc = function () {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if (index) {
        parent.layer.close(index); //再执行关闭
    } else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};
