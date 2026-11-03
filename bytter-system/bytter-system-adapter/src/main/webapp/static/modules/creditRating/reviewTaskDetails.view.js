
bt.use(['form','layedit','moneynum','btdate','select','corp','moneyType','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    var $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.rateType.init();
    var obj = {
        header: [
            {name: 'scoringIndicatorName', title: '指标类别'},
            {name: 'scoringRubric', title: '评分细则'},
            {name: 'scoringIndicatorCoefficient', title: '权重'},
            {name: 'isAddOrSubGrade', title: '是否额外加减分'},
            {name: 'indicatorWeightingAvgScore',title: '指标得分'},
            {name: 'total',title: '合计'}
        ],
        body: []
    }


    function creatTableThead(data) {
        // var corpNum = data.corpName.length || 1;
        // var corpName = data.corpName
        var html = ''
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
        // html += '<th rowspan="1" colspan="'+ corpNum +'">'+ bt.lang('table.crTaskInfo.corpName')+ '</th>'
        html += '<th rowspan="3" >指标得分</th>'
        html += '<th rowspan="3" >合计</th>'
        html += '</tr>'
        $('#singleTableThead').html(html);
    }

    function createTbodyHtml(columns, list,isRootNode,totalScore,totalRowSpan) {
        var html = '';
        for (var i = 0; i < list.length; i++) {
            var row = list[i]
            html += '<tr>'
            if (row.childrenScoringList && row.childrenScoringList.length) {
                html += createTbodyHtmlByChildren(row, columns,isRootNode,row.indicatorWeightingAvgScore,totalRowSpan)
            } else {
                for (var l = 0; l < columns.length; l++) {
                    var colSpan = 1;
                    if (l == 0) {
                        colSpan = row.colSpan || 1;
                    }
                    if(l === 5){
                        if(isRootNode  && i==0 ){
                            var rowSpan = totalRowSpan || 1;
                            html += '<td  rowspan="'+colSpan+'">' + row.indicatorWeightingAvgScore + '</td>'
                            isRootNode = false;
                        }
                    }else if(columns[l].name == 'scoringIndicatorCoefficient'){
                        var show;
                        if(row[columns[l].name]){
                            show = bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ',');
                        }else{
                            show = ''
                        }
                        html += '<td colspan="' + colSpan + '">'+ show +'</td>'
                    }else if(columns[l].name == 'isAddOrSubGrade' && row[columns[l].name] == '1'){
                        html += '<td colspan="' + colSpan + '"><i class="layui-icon">&#xe605;</i></td>'
                    }else if(columns[l].name == 'isAddOrSubGrade' && row[columns[l].name] == '0'){
                        html += '<td colspan="' + colSpan + '"></td>'
                    }else if (columns[l].name == 'indicatorWeightingAvgScore'){
                        var show=row[columns[l].name]==null ? "":row[columns[l].name];
                        html += '<td colspan="' + colSpan + '">' + show.toLocaleString('en-US') + '</td>'
                    }else{
                        var show=row[columns[l].name]==null ? "":row[columns[l].name];
                        html += '<td colspan="' + colSpan + '">' + show + '</td>'
                    }
                }
                html += '</tr>'
            }
        }
        $("#tableBody").html(html);
    }

    function createTbodyHtmlByChildren(list, columns,isRootNode,totalScore,totalRowSpan) {
        var html = '';
        html += '<td rowspan="' + (list.rowSpan || 1) + '">' + (list[columns[0].name] || "")+ '</td>'
        for (var i = 0; i < list.childrenScoringList.length; i++) {
            var row = list.childrenScoringList[i];
            // i 不等于0时，需要添加<tr>
            if (i) {
                html += '<tr>'
            }
            if (row.childrenScoringList && row.childrenScoringList.length) {
                html += createTbodyHtmlByChildren(row, columns,isRootNode,totalScore,totalRowSpan)
            } else {
                for (var l = 0; l < columns.length; l++) {
                    var colSpan = 1;
                    if (l == 0) {
                        colSpan = row.colSpan || 1;
                    }


                    if(l === 5 ){
                        if(isRootNode){
                            var rowSpan = totalRowSpan || 1;
                            html += '<td  rowspan="'+rowSpan+'">' + totalScore + '</td>'
                            isRootNode = false;
                        }
                    }else if(columns[l].name == 'scoringIndicatorCoefficient'){
                        var show;
                        if(row[columns[l].name]){
                            show = bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ',');
                        }else{
                            show = ''
                        }
                        html += '<td colspan="' + colSpan + '">'+ show +'</td>'
                    }else if(columns[l].name == 'isAddOrSubGrade' && row[columns[l].name] == '1'){
                        html += '<td colspan="' + colSpan + '"><i class="layui-icon">&#xe605;</i></td>'
                    }else if(columns[l].name == 'isAddOrSubGrade' && row[columns[l].name] == '0'){
                        html += '<td colspan="' + colSpan + '"></td>'
                    } else if (columns[l].name == 'indicatorWeightingAvgScore'){
                        var show=row[columns[l].name]==null ? "":row[columns[l].name];
                        html += '<td colspan="' + colSpan + '">' + show.toLocaleString('en-US') + '</td>'
                    } else{
                        var show=row[columns[l].name]==null ? "":row[columns[l].name];
                        html += '<td colspan="' + colSpan + '">' + show + '</td>'
                    }
                }
                html += '</tr>'
            }
        }
        return html
    }

    function createTableTbodyOrgTd(row){
        var list = row.scoringMetricValFixReportList
        var tds = '';
        $.each(list,function (i,item) {
            if(null == item.score){
                tds += '<td></td>'
            }else{
                tds += '<td>'+ (item.score)  +'</td>'
            }
        })
        return tds;
    }

    function getTaskScoringJson() {
        $.get(basePath + '/credit/evaluationReport/getScoringIndicatorFixReport/' + $('#taskNo').val() + "/" +  $('#orgId').val(), function (result) {
            var columns = obj.header;
            creatTableThead(result);
            $('#tableBody').html(createTbodyHtml(columns, result.taskScoringList,true,result.totalScore,result.totalRowSpan));
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
