var moneyFun;
bt.use(['form','layedit','moneynum','select','corp','dateType','rateType','moneyType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.rateType.init();
    bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var obj = {
        header: [
            {name: 'scoringIndicatorName', title: '指标类别'},
            {name: 'scoringRubric', title: '评分细则'},
            {name: 'scoringIndicatorCoefficient', title: '权重'},
            {name: 'isAddOrSubGradeName', title: '是否额外加减分'}
        ],
        body: []
    }

    function creatTableThead(data) {
        var orgNum = data.orgName.length || 1;
        var orgName = data.orgName
        var html = '<tr><th colspan="1" style="border-right: none;text-align:left">'+bt.lang('credit.Institutional.Evaluation.form')+'</th><th colspan="16" style="border: none"></th></tr>'
        html += '<tr>'
        // 指标类别
        html += '<th rowspan="3" colspan="'+(data.maxLevel || 1)+'"> '+ bt.lang('table.creditRating.indicatorCategory')+ '</th>'
        //评分细则
        html += '<th rowspan="3">'+ bt.lang('credit.indicator.scoringRubric')+ '</th>'
        //权重
        html += '<th rowspan="3">'+ bt.lang('credit.indicator.weight')+ '</th>'
        //额外加/减分项
        html += '<th rowspan="3">'+ bt.lang('credit.indicator.addOrSub')+ '</th>'
        //评价组织
        html += '<th rowspan="1" colspan="'+orgNum * 2 +'">'+ bt.lang('table.crTaskInfo.corpName')+ '</th>'
        html += '</tr>'

        var orgNameHmtl = '';
        var orgIndicatorHtml = '';
        for (var i = 0; i < orgNum; i++) {
            orgNameHmtl += '<th colspan="2">'+orgName[i]+'</th>'
            orgIndicatorHtml += '<th >数值</th>'
            orgIndicatorHtml += '<th >得分</th>'
        }
        html += '<tr>'+orgNameHmtl+'</tr>'
        html += '<tr>'+orgIndicatorHtml+'</tr>'

        $('#singleTableThead').html(html);
    }

    function createTbodyHtml(columns, list) {
        var html = '';
        for (var i = 0; i < list.length; i++) {
            var row = list[i]
            html += '<tr>'
            if (row.childrenScoringList && row.childrenScoringList.length) {
                html += createTbodyHtmlByChildren(row, columns)
            } else {
                for (var l = 0; l < columns.length; l++) {
                    var colSpan = 1;
                    if (l == 0) {
                        colSpan = row.colSpan || 1;
                    }
                    if(columns[l].name == 'scoringIndicatorCoefficient'){
                        var show;
                        if(row[columns[l].name]){
                            show = bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ',');
                        }else{
                            show = ''
                        }
                        html += '<td colspan="' + colSpan + '">'+ show +'</td>'
                    }else if(columns[l].name == 'isAddOrSubGradeName' && row[columns[l].name] == '是'){
                        html += '<td colspan="' + colSpan + '"><i class="layui-icon">&#xe605;</i></td>'
                    }else if(columns[l].name == 'isAddOrSubGradeName' && row[columns[l].name] == '否'){
                        html += '<td colspan="' + colSpan + '"></td>'
                    }else{
                        html += '<td colspan="' + colSpan + '">' + (row[columns[l].name] || "") + '</td>'
                    }
                }
                html += createTableTbodyOrgTd(row);
                //html += '<td rowspan="' + (row.rowSpan || 1) + '"></td>' //操作
                html += '</tr>'
            }

        }
        $("#tableBody").html(html);
    }

    function createTbodyHtmlByChildren(list, columns) {
        var html = '';
        html += '<td rowspan="' + (list.rowSpan || 1) + '">' + (list[columns[0].name] || "")+ '</td>'
        for (var i = 0; i < list.childrenScoringList.length; i++) {
            var row = list.childrenScoringList[i];
            // i 不等于0时，需要添加<tr>
            if (i) {
                html += '<tr>'
            }
            if (row.childrenScoringList && row.childrenScoringList.length) {
                html += createTbodyHtmlByChildren(row, columns)
            } else {
                for (var l = 0; l < columns.length; l++) {
                    var colSpan = 1;
                    if (l == 0) {
                        colSpan = row.colSpan || 1;
                    }
                    if(columns[l].name == 'scoringIndicatorCoefficient'){
                        var show;
                        if(row[columns[l].name]){
                            show = bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ',');
                        }else{
                            show = ''
                        }
                        html += '<td colspan="' + colSpan + '">'+ show +'</td>'
                    }else if(columns[l].name == 'isAddOrSubGradeName' && row[columns[l].name] == '是'){
                        html += '<td colspan="' + colSpan + '"><i class="layui-icon">&#xe605;</i></td>'
                    }else if(columns[l].name == 'isAddOrSubGradeName' && row[columns[l].name] == '否'){
                        html += '<td colspan="' + colSpan + '"></td>'
                    }else{
                        html += '<td colspan="' + colSpan + '">' + (row[columns[l].name] || "") + '</td>'
                    }
                }

                html += createTableTbodyOrgTd(row);
                html += '</tr>'
            }
        }
        return html
    }

    function createTableTbodyOrgTd(row){
        var list = row.scoringMetricValFixReportList
        var tds = '';
        $.each(list,function (i,item) {
            if(null == item.indicatorValue){
                tds += '<td></td>'
            }else{
                tds += '<td>'+ (item.indicatorValue).toLocaleString('en-US',{maximumFractionDigits: 6}) +'</td>'
            }
            if(null == item.score){
                tds += '<td></td>'
            }else{
                tds += '<td>'+ (item.score).toLocaleString('en-US')  +'</td>'
            }
        })
        return tds;
    }
    function getTaskScoringJson() {
        $.get(basePath + '/credit/evaluationAnalysis/getCorpScoringJson/' + $('#taskNo').val() + '/' + $('#corpId').val(), function (result) {
            titleRowSpan = 1;
            var columns = obj.header;
            creatTableThead(result);
            $('#tableBody').html(createTbodyHtml(columns, result.taskScoringList));
        }, "json")
    }

    getTaskScoringJson();

});

var closeDoc=function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if(index){
        parent.layer.close(index); //再执行关闭
    }else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};
