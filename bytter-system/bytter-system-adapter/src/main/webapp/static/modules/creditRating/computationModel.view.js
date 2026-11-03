var selectTypes,moneyTypes,moneyFun,rateType,dicts;
bt.use(['form','element','bttable','selectType','moneyType', 'dicts','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    rateType = bt.rateType.init();//利率
    dicts = bt.dicts.init();//字典
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    getScoringData();


//刷新指标展示数据
    function getScoringData() {
        var modelId = $("#id").val();
        var oldScoringIds = $("#oldScoringIds").val();
        var newScoringIds = $("#newScoringIds").val();
        // var oldIdArray=oldScoringIds.split(',');
        // var newIdArray=newScoringIds.split(',');
        // for(var i=0;i<oldIdArray.length;i++){
        //     if(oldIdArray[i] in newIdArray){
        //         oldIdArray.splice(i,1)
        //     }
        // }
        // oldScoringIds=oldIdArray.toString();
        // $("#oldScoringIds").val(oldScoringIds);
        //定义一个集合
        var scCoefficientsArray=[];
        var param = {
            newScoringIds:newScoringIds,
            oldScoringIds:oldScoringIds,
            modelId:modelId,
            scCoefficientsArray:scCoefficientsArray
        }
        var url = basePath + "/creditRating/computationalModel/getScoringForShow";
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
        },'JSON');
    }

    var checkBack = function (checkedIds) {
        //新选指标
        $("#newScoringIds").val(checkedIds);
        //固化指标与新选去重
        var oldScoringIds = $("#oldScoringIds").val();
        if (!bt.empty(oldScoringIds)) {
            var oldScoringArray = oldScoringIds.split(",")
            for (var i = 0; i < oldScoringArray.length; i++) {
                if (oldScoringArray[i] in checkedIds.split(",")) {
                    oldScoringArray.splice(i, 1)
                }
            }
            oldScoringIds = oldScoringArray.toString();
            $("#oldScoringIds").val(oldScoringIds);
        }

        getScoringData();
    }

    /**
     * 删除相应的行
     */
    function remoTr(el) {
        var rowIndex = el.parentElement.parentElement.rowIndex;
        var scId = $(el).attr("scId");
        var oldScoringIds = $("#oldScoringIds").val();
        var newScoringIds = $("#newScoringIds").val();
        var oldIdArray = oldScoringIds.split(',');
        var newIdArray = newScoringIds.split(',');
        for (var i = 0; i < oldIdArray.length; i++) {
            if (scId == oldIdArray[i]) {
                oldIdArray.splice(i, 1)
            }
        }
        for (var i = 0; i < newIdArray.length; i++) {
            if (scId == newIdArray[i]) {
                newIdArray.splice(i, 1)
            }
        }
        oldScoringIds = oldIdArray.toString();
        newScoringIds = newIdArray.toString();
        $("#oldScoringIds").val(oldScoringIds);
        $("#newScoringIds").val(newScoringIds);

        var tab = document.getElementById("dataTable");
        tab.deleteRow(rowIndex);
    }

    function makeTableDate(result) {
        $('#ScoringContract').html(reateTbodyHtml(obj.header, result));
        var tds=$("#dataTable tbody tr:first").find("td");
        var titleRowSpan=0;
        for(var i=0;i<tds.length;i++){
            titleRowSpan+=Number($(tds[i]).attr("colspan") || 1);
        }

        $("#nameTitle").attr("colspan", titleRowSpan-3);
        rateType.render();
    }


    var obj = {
        header: [
            {name: 'scoringIndicatorName', title: '指标类别'},
            {name: 'scoringRubric', title: '评分细则'},
            {name: 'isAddOrSubGrade', title: '是否额外加减分'},
            {name: 'scoringIndicatorCoefficient', title: '权重'},
        ],
        body: []
    }

    function reateTbodyHtml(columns, list) {
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
                    if (l == 3) {
                        if (row["scoringIndicatorCoefficient"]==0) {
                            html +='<td colspan="' + colSpan + '">0.00%</td>';
                        }else{
                            html +='<td colspan="' + colSpan + '">'+bt.rateType.getFun().format(row["scoringIndicatorCoefficient"],'percent', 2)+'</td>';
                        }
                    } else {
                        var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                        if(columns[l].name=='isAddOrSubGrade'){
                            if(show=='1'){
                                show='<div style="text-align: center;"><i class="layui-icon">&#xe605;</i></div>'
                            }else {
                                show='';
                            }
                        }
                        html += '<td colspan="' + colSpan + '">' + show + '</td>'
                    }
                }

                html += '</tr>'
            }
        }
        return html
    }

    function createTbodyHtmlByChildren(list, columns) {
        var html = '';
        html += '<td rowspan="' + (list.rowSpan || 1) + '" style="min-width: 80px;">' + list[columns[0].name] + '</td>'
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
                    if (l == 3) {
                        if (row["scoringIndicatorCoefficient"]==0) {
                            html +='<td colspan="' + colSpan + '">0.00%</td>';
                        }else{
                            html +='<td colspan="' + colSpan + '">'+bt.rateType.getFun().format(row["scoringIndicatorCoefficient"],'percent', 2)+'</td>';
                        }

                    } else {
                        var show = bt.empty(row[columns[l].name]) ? " " : row[columns[l].name];
                        if(columns[l].name=='isAddOrSubGrade'){
                            if(show=='1'){
                                show='<div style="text-align: center;"><i class="layui-icon">&#xe605;</i></div>'
                            }else {
                                show='';
                            }
                        }
                        html += '<td colspan="' + colSpan + '">' + show + '</td>'
                    }
                }
                html += '</tr>'
            }
        }
        return html
    }

    //监听
    form.on('submit(close)', function(data){
        if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
            //bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
        return false;
    });
})


