var $, form, rowStr = 0;
bt.use(['form', 'element'], function () {
    $ = bt.jquery;
    form = bt.form();
    if ($("#rubicNum").val() > 0) {
        rowStr = $("#rubicNum").val() - 1//初始化评分细则数量
    }
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }


    afterIndexProperty($("#indexProperty").val())//编辑界面进来，判断指标性质
    if ($("#indexProperty").val() == 1) {
        replaceScoringRubric($("#scoringMethod").val())//编辑界面进来，判断评分方法
    }
});


/**
 * 判断评分方法后处理
 * @param scoringMethod
 */
function replaceScoringRubric(scoringMethod) {

    var text;
    var html = '';
    //定义是否需要去除必填校验的标记
    var isRequire1 = true;
    var isRequire2 = true;

    if ($("#indexProperty").val() != 2) {
        if (!scoringMethod || scoringMethod == " ") {//如果评分方法为空，那么默认为归一法
            $("#scoringR").hide();
        } else {
            $("#scoringR").show();
        }
    }
    html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 600px;table-layout: fixed">'
    html += '<thead>'
    html += '<tr><th colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</th><th>' + bt.lang("credit.indicator.grade") + '</th></tr>'
    html += '</thead>'
    html += '<tbody>'
    if (scoringMethod == 1) {
        text = '评分方法说明：以授信额度为例，获取评价任务中评价机构的最高授信额度、最低授信额度，机构得分=“(该组织的<br>授信额度-最低授信额度)/(最高授信额度-最低授信额度)*满分分值';
        // $("#remind").html(text)
       // html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 500px;table-layout: fixed">'
       // html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td colspan="2" style="max-width: 60px;"><div class="layui-input"> 满分 </div></td><td width="100px;">10</td></tr> '
        html += '</tbody></table>'
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").show();
        $("#description").hide()
        $("#example").show()
    } else if (scoringMethod == 2) {
        text = ' 评分方法说明：以授信额度为例，将授信额度进行分段并匹配对应区间分值。系统获取评价机构的授信额度，根据授信额<br>度坐落区间取该评价机构得分。<br>'
            + '假设评分细则为：<br> 1.授信额度≥20亿：10分<br>2.10亿≤授信额度＜20亿：8分<br>3.5亿≤授信额度＜10亿：6分<br>4.授信额度＜5亿：4分<br>若评价机构授信额度为15亿，则其得分为8分；'
       // $("#remind").html(text)
        // html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
       // html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td style="max-width: 60px;"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 2000000000 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 1000000000 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 500000000 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 0.00 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
    } else if (scoringMethod == 3) {
        text = '评分方法说明：以授信额度为例，将授信额度进行分段并匹配对应区间分值。系统获取评价机构的授信额度，通过授信额<br>度坐落区间估算评价机构得分。<br>'
            + '假设评分细则为：<br> 1.授信额度>=20亿：10分<br>2.授信额度=10亿：8分<br>3.授信额度=5亿：6分<br>4.授信额度<=1亿：0分<br>若评价机构授信额度为15亿，则其得分为9分；'
       // $("#remind").html(text)
       // html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
       // html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td style="max-width: 60px;"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 2000000000 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td><div class="layui-input"> = </div></td><td width="200px"><div class="layui-input"> 1000000000 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td><div class="layui-input"> = </div></td><td width="200px"><div class="layui-input"> 500000000 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 100000000 </div></td><td width="100px;">0</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
    } else if (scoringMethod == 4) {
        text = '评分方法说明：以授信额度为例，获取评价任务中评价机构的授信额度，将授信额度倒序排列后根据坐落区间取评价机构得分。<br>'
            + '假设评分细则为：<br> 1、1-3名  10分<br> 2、4-6名   8分<br>  3、7-9名   6分<br>  4、其他     4分<br>若评价机构授信额度排名第2，则其得分为10分；'
       // $("#remind").html(text)
        html += '<tr><td style="max-width: 60px;"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 3 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 6 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td><div class="layui-input"> > </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#description").hide()
        $("#example").show()
        $("#scoringRubricTable1").show();
        $("#scoringRubricTable2").hide();
    } else if (scoringMethod == 5) {
        text = '评分方法说明：以融资成本为例，获取评价任务中评价机构的融资成本，将融资成本升序排列后根据坐落区间取评价机构得分。<br>'
            + '假设评分细则为：<br> 1、1-3名  10分<br> 2、4-6名   8分<br>  3、7-9名   6分<br>  4、其他     4分<br>若评价机构授信额度排名第2，则其得分为10分；'
       // $("#remind").html(text)
       // html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 500px;table-layout: fixed">'
       // html += '<thead>'
       // html += '<tr><th colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</th><th>' + bt.lang("credit.indicator.grade") + '</th></tr>'
        //html += '</thead>'
       // html += '<tbody>'
        html += '<tr><td style="max-width: 60px;"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 3 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 6 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td><div class="layui-input"> > </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
    } else if (scoringMethod == 6) {
        $("#example").hide()
        $("#description").show()
        //隐藏table
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").hide();
    } else {

    }
    if(text){
        $("#remind").html(text)
    }
    $("#scoringExample").html(html)

}

/**
 * 判断指标性质后处理
 * @param value
 */
function afterIndexProperty(value) {
    //定量
    if (value == 1) {
        $(".dataSource").show();
        $("#description").hide()
        $("#example").show()
        replaceScoringRubric($("#scoringMethod").val())
    } else if (value == 2) {//定性
        $(".isAutoScoring").hide();
        $(".scoringMethod").hide();
        $(".dataSource").hide();
        $("#description").show()
        $("#example").hide()
        //隐藏table
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").hide();
    } else {

    }
}
