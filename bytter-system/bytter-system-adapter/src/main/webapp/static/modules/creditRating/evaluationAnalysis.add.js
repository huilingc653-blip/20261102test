var moneyFun;
bt.use(['form','layedit','moneynum','btdate','select','corp','moneyType','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.rateType.init();
    moneyFun =  bt.moneyType.getFun();
    var obj = {
        header: [
            {name: 'scoringIndicatorName', title: '指标类别'},
            {name: 'scoringRubric', title: '评分细则'},
            {name: 'scoringIndicatorCoefficient', title: '权重'},
            {name: 'isAddOrSubGradeName', title: '是否额外加减分'}
        ],
        body: []
    }

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    function creatTableThead(data) {
        var orgNum = data.orgName.length || 1;
        var orgName = data.orgName
        var html = '<tr><th colspan="1" style="border: none">'+bt.lang('credit.Institutional.Evaluation.form')+'</th><th colspan="16" style="border: none"></th></tr>'
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
        html += '<th rowspan="1" colspan="'+orgNum * 2 +'">'+ bt.lang('评价机构')+ '</th>'
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
                        html += '<td colspan="' + colSpan + '">'+ bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ','); +'</td>'
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
                        html += '<td colspan="' + colSpan + '">'+ bt.rateType.getFun().format(row[columns[l].name], 'percent', 4, ','); +'</td>'
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
            var isAutoScoring = parseInt(row.isAutoScoring);
            var indexProperty = parseInt(row.indexProperty);//指标性值
            if(indexProperty==1 && isAutoScoring==0){
                //定量且不自动需要手动输入数值  来得到分值
                tds += '<td><input type="text" name="v_'+ item.id +'" inId="'+row.scoringIndicatorSolidificati+'" lay-verify="required"' +
                    ' data-callback="makeScore" autocomplete="off" class="layui-input bt-biz-moneyType"></td>'
            }else {
                if (null == item.indicatorValue) {
                    tds += '<td></td>'
                } else {
                    tds += '<td>' + (item.indicatorValue).toLocaleString('en-US',{maximumFractionDigits: 6}) + '</td>'
                }
            }

            if(!bt.empty(indexProperty)){
                if(indexProperty==1){//定量
                        if (isAutoScoring == 1) {
                            if (null == item.score) {
                                tds += '<td></td>'
                            }else {
                                tds += '<td>'+ (item.score).toLocaleString('en-US')   +'</td>'
                            }
                        }else {
                            tds += '<td><div id="n_'+ item.id +'"></div><input type="hidden" id="'+ item.id +'" name="'+ item.id +'" autocomplete="off" class="layui-input bt-biz-moneyType"></td>'
                        }
                }else {//定性（手动评分）
                    tds += '<td><input type="text" name="'+ item.id +'" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType"></td>'
                }
            }else {
                tds += '<td></td>'
            }

        })
        return tds;
    }

    makeScore = function (data, el) {
        var inId = $(el).attr("inId");
        var modelId =$("#modelId").val();
        var reportId= $(el).attr("data-name").substring(2);

        //调用接口，根据手动数值得到得分
        $.post(basePath + '/credit/evaluationAnalysis/getScoreByValue', {inId: inId, value: data,modelId: modelId},
            function (result) {
                if (!bt.empty(result)) {
                    $("#"+reportId).val(Number(result));
                    $("#n_"+reportId).html(Number(result));
                }
        })
    }

    function getTaskScoringJson() {
        $.get(basePath + '/credit/evaluationAnalysis/getCorpScoringJson/' + $('#taskNo').val() + '/' + $('#corpId').val(), function (result) {
            titleRowSpan = 1;
            var columns = obj.header;
            creatTableThead(result);
            $('#tableBody').html(createTbodyHtml(columns, result.taskScoringList));
            bt.moneyType.render();
        }, "json")
    }

    getTaskScoringJson();


    //监听提交
    form.on('submit', function(data){
        var nm = $(this).attr("id");
        var opType = "0";
        if (nm == "submit") {
            opType = "1";
        }
        var field = data.field;

        var valueScore = "";
        var indicatorScore = "";
        for(var key in field){
            var score;
            if(field[key]){
                score = field[key];
            }else{
                score = null;
            }
            if(key.includes('v_')){
                valueScore +=key.substring(2) + '_' + score + ',';
            }else {
                indicatorScore += key + '_' + score + ',';
            }
        }
        var obj = {};
        obj.id = $("#taskId").val();
        obj.opType = opType;
        obj.indicatorScore = indicatorScore;
        obj.valueScore = valueScore;
        //var param = {id:$("#taskId").val(), opType:opType, indicatorScore:indicatorScore}
        // return false;
        bt.loading.show();
        var action = "/credit/evaluationAnalysis/submit";
        $.post(basePath + action,obj, function (result) {
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });
});
