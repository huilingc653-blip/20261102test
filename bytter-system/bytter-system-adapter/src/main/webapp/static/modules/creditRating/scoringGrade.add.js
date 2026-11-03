var selectTypes,moneyTypes,moneyFun,dicts
bt.use(['form','element','bttable','selectType','moneyType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    moneyTypes  = bt.moneyType.init();//金额
    moneyFun=bt.moneyType.getFun();
    dicts = bt.dicts.init();//字典

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    makeUpperScore();
    makeAddDelButton();
    //监听提交
    form.on('submit', function (data) {

        bt.loading.show();
        var action = "/creditRating/scoringGrade/submit";
        // verify();//校验
        $.post(basePath + action, data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                var webObj = bt.navtab.getParentDom();
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            }
        },'JSON');

        return false;
    });
})

function verify() {
    //评分级别校验
    var trs = $("#dataTable tbody tr");
    var lastrow=Number($(trs[trs.length - 1]).attr("data-row"));
    var scoringSymbol=dicts.get("scoringSymbol"+lastrow).val();//最后一行符号
    var value=moneyTypes.get("scoreLimitLevel"+lastrow).val();//此行值
    if(Number(value)!=0 && (scoringSymbol=="10" || scoringSymbol=="11")){
        parent.layer.msg(bt.lang("同方向评分标准最低下限分应为0"), {icon: 2});
        return false;
    }
}

function addTr() {
    var trs = $("#dataTable tbody tr");
    var uprow=Number($(trs[trs.length - 1]).attr("data-row"))
    var upScoringSymbol=dicts.get("scoringSymbol"+uprow).val();
    var scoringSymbol;
    if(upScoringSymbol=="10"){
        scoringSymbol="11,20"
        // scoringSymbol="20"
    }else if(upScoringSymbol=="11"){
        scoringSymbol="10,21"
        // scoringSymbol="21"
    }else {
        return false;
    }

    var row=uprow+1;
    var trHtml = '<tr data-row="' + row + '">';

    trHtml+='<td style="text-align:center"><div class="bt-biz-dict" data-type="select" data-width="70px" data-group="FUNDWARN_OPERATOR"' +
        '                                 data-defaultvalue='+upScoringSymbol+' data-inputvalue="false"  data-filterData="'+scoringSymbol+'"' +
        '                                 data-name="gradeLevels['+row+'].scoringSymbol" id="scoringSymbol'+row+'" data-biz-name="scoringSymbol'+row+'"' +
        '                                 data-placeholder="" data-required="true" data-callBack="makeUpperScore"' +
        '                            ></div>';
    trHtml+='<div class="layui-input-inline"><input class="layui-input   bt-biz-moneyType"  type="text" id="scoreLimitLevel'+row+'" data-biz-name="scoreLimitLevel'+row+'"'+
        ' style="width: 120px;" autocomplete="off" data-decimal="0" data-maxlength="18" data-name="gradeLevels['+row+'].scoreLimitLevel" data-callBack="makeUpperScore"></div></td>';

    trHtml+='<input type="hidden" id="upperScoreLimitLevel'+row+'" name="gradeLevels['+row+'].upperScoreLimitLevel">';
    trHtml+='<input type="hidden" id="lowerScoreLimitLevel'+row+'" name="gradeLevels['+row+'].lowerScoreLimitLevel">';
    trHtml+='<input type="hidden" id="upScoringSymbol'+row+'" name="gradeLevels['+row+'].upScoringSymbol">';
    trHtml+='<input type="hidden" id="downScoringSymbol'+row+'" name="gradeLevels['+row+'].downScoringSymbol">';

    trHtml+='<td id="showRow'+row+'" style="text-align:center"></td>';
    trHtml+='<td><input id="scoringLevel'+row+'" name="gradeLevels['+row+'].scoringLevel" value=""class="layui-input" type="text" lay-verify="required"/></td>';
    trHtml+='<td><input id="explanationOfScoringLevel'+row+'" name="gradeLevels['+row+'].explanationOfScoringLevel" value="" class="layui-input" type="text"/> </td>';
    trHtml+='<td id="showBon'+row+'" style="text-align:center"></td>';
    trHtml+='</tr>';
    $("#dataTable tbody").append(trHtml);
    bt.moneyType.init();
    bt.selectType.init();
    bt.dicts.init();
    makeUpperScore();
    makeAddDelButton();
}

function remoTr(rowIndex) {
    var tab = document.getElementById("dataTable");
    tab.deleteRow(rowIndex);
    makeUpperScore();
    makeAddDelButton();
}

function makeAddDelButton() {
    var trs = $("#dataTable tbody tr");
    var lastrow=Number($(trs[trs.length - 1]).attr("data-row"));//最后一行
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-row");
        var showBon='';
        if(rowNum==lastrow){
            showBon='<button type="button" class="layui-btn" onClick="addTr()">'+bt.lang("common.button.add")+'</button>';
            if(lastrow!=0){
                showBon=showBon+'&nbsp;&nbsp;<button type="button" class="layui-btn" onClick="remoTr(this.parentElement.parentElement.rowIndex)">'+bt.lang("common.button.delete")+'</button>';
            }
        }else {
            showBon='';
        }
            $("#showBon"+rowNum).html(showBon);
    })
}

function makeUpperScore() {
    var trs = $("#dataTable tbody tr");
    var upValue;//上一行的值
    var upScoringSymbol;//上一行符號
    var lastrow=Number($(trs[trs.length - 1]).attr("data-row"));//最后一行
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-row");
        if(rowNum!=lastrow){
            dicts.get("scoringSymbol"+rowNum).val({"disabled": true});
        }else {
            dicts.get("scoringSymbol"+rowNum).val({"disabled": false});
        }
        var scoringSymbol=dicts.get("scoringSymbol"+rowNum).val();//此行符號
        var thisValue=moneyTypes.get("scoreLimitLevel"+rowNum).val();//此行值
        if(rowNum!="0"){
            if(scoringSymbol=="10" || scoringSymbol=="11") {
                //下一行不能大于上一行
                moneyTypes.get("scoreLimitLevel" + rowNum).attr("data-max", upValue-1);
                moneyTypes.get("scoreLimitLevel"+rowNum).attr("readOnly",false);
            }else if(scoringSymbol=="20" || scoringSymbol=="21") {
                moneyTypes.get("scoreLimitLevel"+rowNum).attr("readOnly",true);
                moneyTypes.get("scoreLimitLevel" + rowNum).attr("data-max", upValue);
                moneyTypes.get("scoreLimitLevel"+rowNum).val(upValue);
                thisValue=upValue;
            }
        }
         thisValue=moneyTypes.get("scoreLimitLevel"+rowNum).val();//此行值
        var mins,minv,maxs,maxv;//最大最小值，以及值的符号
        if(!bt.empty(thisValue)){
            if(scoringSymbol=="10"){
                mins="(";
                minv=thisValue;
            }else if(scoringSymbol=="11"){
                mins="[";
                minv=thisValue;
            }
            if(bt.empty(upScoringSymbol)){
                maxs=")";
                maxv="+00";
            }else if(upScoringSymbol=="10"){
                maxs="]";
                maxv=upValue;
            }else if(upScoringSymbol=="11"){
                maxs=")";
                maxv=upValue;
            }
            //最后一行
            if(scoringSymbol=="20"){
                mins="(";
                minv="-00";
                maxs=")";
                maxv=thisValue;
            }else if(scoringSymbol=="21"){
                mins="(";
                minv="-00";
                maxs="]";
                maxv=thisValue;
            }
            //下限为0时候取等
            if(mins=="(" && minv=="0"){
                mins="[";
            }
            $("#showRow"+rowNum).html(mins+minv+","+maxv+maxs);
            if(maxv!="+00") {
                $("#upperScoreLimitLevel" + rowNum).val(maxv);
            }
            if(minv!="-00") {
                $("#lowerScoreLimitLevel" + rowNum).val(minv);
            }else{
                $("#lowerScoreLimitLevel" + rowNum).val('');
            }
            $("#upScoringSymbol" + rowNum).val(maxs);
            $("#downScoringSymbol" + rowNum).val(mins);
        }

        upValue = thisValue;
        upScoringSymbol=scoringSymbol;
    });
    dicts.render();
}

// function addTr() {
//
//     var trs = $("#dataTable tbody tr");
//     var row=Number($(trs[trs.length - 1]).attr("data-row")) + 1
//     var trHtml = '<tr data-row="' + row + '">';
//     trHtml+='<td class="textRight">'+bt.lang("评分级别")+'</td>';
//     trHtml+='<td><input type="text" class="layui-input" name="gradeLevels['+row+'].scoringLevel" id="scoringLevel_'+row+'"></td>';
//     trHtml+='<td class="textRight">'+bt.lang("上限分")+'</td>';
//     trHtml+='<td><input type="text" class="layui-input bt-biz-moneyType" data-name="gradeLevels['+row+'].upperScoreLimitLevel" ' +
//         'data-biz-name="upperScoreLimitLevel_'+row+'" id="upperScoreLimitLevel_'+row+'" readonly data-decimal="0"></td>';
//     trHtml+='<td class="textRight">'+bt.lang("下限分")+'</td>';
//     trHtml+='<td><input type="text" class="layui-input bt-biz-moneyType" data-name="gradeLevels['+row+'].lowerScoreLimitLevel" ' +
//         'data-biz-name="lowerScoreLimitLevel_'+row+'" id="lowerScoreLimitLevel_'+row+'" data-callBack="makeUpperScore" data-decimal="0"></td>';
//     trHtml+='<td class="textRight">'+bt.lang("级别说明")+'</td>';
//     trHtml+='<td><input type="text" class="layui-input " name="gradeLevels['+row+'].explanationOfScoringLevel" id="explanationOfScoringLevel_'+row+'"></td>';
//     trHtml += '<td align="center"><a href="javascript:void(0);" title="#springMessage("common.delTr")" onClick="remoTr(this.parentElement.parentElement.rowIndex)"><i class="layui-icon">&#xe640;</i></a></td>';
//     trHtml += '</tr>';
//     $("#dataTable tbody").append(trHtml);
//     bt.moneyType.init();
//     makeUpperScore();
// }
//
//
// function remoTr(rowIndex) {
//     var tab = document.getElementById("dataTable");
//     tab.deleteRow(rowIndex);
//      makeUpperScore();
// }
//
// function makeUpperScore() {
//   var upperValue=$("#scoringSystem").val();
//     var trs = $("#dataTable tbody tr");
//     $.each(trs, function (index, item) {
//         rowNum = $(item).attr("data-row");
//         if(rowNum) {
//             moneyTypes.get("upperScoreLimitLevel_" + rowNum).val(moneyFun.format(upperValue, 0));
//             moneyTypes.get("lowerScoreLimitLevel_" + rowNum).attr("data-max", moneyFun.format(upperValue, 0))
//             upperValue = moneyTypes.get("lowerScoreLimitLevel_" + rowNum).val()-1;
//             if(upperValue<0){
//                 upperValue=0;
//             }
//         }
//     })
// }
