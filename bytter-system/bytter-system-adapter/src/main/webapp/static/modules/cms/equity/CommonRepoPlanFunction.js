var $, form, moneyType, moneyFun, rateTypes
bt.use(['form', 'element', 'rateType', 'moneyType', 'dateType', 'bttable'], function () {
    $ = bt.jquery;
    form = bt.form()
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率

});

/**
 * 判断是否为空 true：空 false:不为空
 * @param data
 */
function isNull(data) {
    return (data == "" || data == undefined || data == null) ? true : false;
}

/**
 * 导入还款计划(其他)
 */
function addImport(o, surplusRepoMoney) {
    var num = 0;
    var isMore = true;
    var invMoney = $("#invMoney").val();
    var accounted = 0;
    if (surplusRepoMoney > 0) {
        if (o.repoMoney < invMoney) {
            accounted = moneyFun.div(o.repoMoney, invMoney, 4);
        } else {
            accounted = 1;
        }
    } else {
        o.repoMoney = maxRepoMoney;
        accounted = moneyFun.div(o.repoMoney, invMoney, 4);
    }
    $("input[divName^='repoMoney']").each(function (i) {
        num++;
    });
    //出资方式，当为1现金时，可多条明细，当2实物时，只能一条,资产名称必填
    var invWay = $("#invWay").val();
    if (invWay == "1") {
        isMore = true;
    } else if (invWay == "2") {
        isMore = false;
    }
    if (!isMore && num > 0) {
        layer.alert(bt.lang('cms.equity.addPlanDetailError'), {icon: 7});
        return;
    }
    var html = '<tr>';
    html += '<td><input type="text" id="equityCode_' + rowNum + '" name="detailDtl[' + rowNum + '].equityCode" value="' + $("#equityCode").val() + '" readonly="readonly" class="layui-input" /></td>';
    html += '<td><input name="detailDtl[' + rowNum + '].repoDate" data-biz-name="repoDate_' + rowNum + '" id="repoDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" value="' + o.repoDate + '"/></td>';
    html += '<td><input divName="repoMoney" class="layui-input bt-input-inline bt-biz-moneyType" type="text" data-decimal="2" id="repoMoney_' + rowNum + '" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repoMoney_' + rowNum + '" data-min="0" data-max="' + maxRepoMoney + '" data-name="detailDtl[' + rowNum + '].repoMoney" ';
    if (!isMore) {
        html += ' disabled value="' + moneyFun.format(invMoney) + '" ';
    } else {
        html += ' value="' + moneyFun.format(o.repoMoney) + '" ';
    }
    html += '/></td>';
    html += '<td><input disabled divName="reRepoMoney" class="layui-input bt-input-inline bt-biz-moneyType" data-biz-name="reRepoMoney_' + rowNum + '" type="text" data-decimal="2" id="reRepoMoney_' + rowNum + '" data-min="0" data-name="detailDtl[' + rowNum + '].reRepoMoney"';
    if (!isMore) {
        html += ' value="0" ';
    } else {
        html += ' value="' + surplusRepoMoney + '" ';
    }
    html += '/></td>';
    html += '<td align="center"><input disabled divName="accounted" class="layui-input bt-biz-rateType" data-biz-name="accounted_' + rowNum + '"  type="text"   id="accounted_' + rowNum + '"   data-name="detailDtl[' + rowNum + '].accounted"';
    if (!isMore) {
        html += ' value="1" ';
    } else {
        html += ' value="' + accounted + '" ';
    }
    html += '/></td>';
    html += '<td align="center"><input type="checkbox" lay-skin="primary" name="detailDtl[' + rowNum + '].isPay" value="1"';
    if (o.isPay == "1") {
        html += ' checked ';
    }
    html += '/></td>';
    html += '<td><input name="detailDtl[' + rowNum + '].rmk" id="rmk"  class="layui-input" type="text" value="' + o.rmk + '"/></td>';
    html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#planDetailTB tbody").append(html);
    addFun();
    sumMoney();
};

/**
 * 新增计划
 */
function add() {
    var num = 0;
    var isMore = true;
    var invMoney = $("#invMoney").val();
    $("input[divName^='repoMoney']").each(function (i) {
        num++;
    });
    //出资方式，当为1现金时，可多条明细，当2实物时，只能一条,资产名称必填
    var invWay = $("#invWay").val();
    if (invWay == "1") {
        isMore = true;
    } else if (invWay == "2") {
        isMore = false;
    }
    if (!isMore && num > 0) {
        layer.alert(bt.lang('cms.equity.addPlanDetailError'), {icon: 7});
        return;
    }
    var html = '<tr>';
    html += '<td><input type="text" id="equityCode_' + rowNum + '" name="detailDtl[' + rowNum + '].equityCode" value="' + $("#equityCode").val() + '" readonly="readonly" class="layui-input" /></td>';
    html += '<td><input name="detailDtl[' + rowNum + '].repoDate" data-biz-name="repoDate_' + rowNum + '" id="repoDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
    html += '<td><input divName="repoMoney" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repoMoney_' + rowNum + '" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repoMoney_' + rowNum + '" data-min="0" data-max="' + maxRepoMoney + '" data-name="detailDtl[' + rowNum + '].repoMoney" ';
    if (!isMore) {
        html += ' disabled value="' + moneyFun.format(invMoney) + '" ';
    }
    html += '/></td>';
    html += '<td><input disabled divName="reRepoMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="reRepoMoney_' + rowNum + '" type="text" data-decimal="2" id="reRepoMoney_' + rowNum + '" data-min="0" data-name="detailDtl[' + rowNum + '].reRepoMoney"';
    if (!isMore) {
        html += ' value="0" ';
    }
    html += '/></td>';
    html += '<td align="center"><input disabled divName="accounted" class="layui-input   bt-biz-rateType" data-biz-name="accounted_' + rowNum + '"  type="text"   id="accounted_' + rowNum + '"   data-name="detailDtl[' + rowNum + '].accounted"';
    if (!isMore) {
        html += ' value="1" ';
    }
    html += '/></td>';
    html += '<td align="center"><input type="checkbox" lay-skin="primary" name="detailDtl[' + rowNum + '].isPay" value="1" /></td>';
    html += '<td><input name="detailDtl[' + rowNum + '].rmk" id="rmk"  class="layui-input" type="text"/></td>';
    html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#planDetailTB tbody").append(html);
    addFun();
    sumMoney();
};


/**
 * 计算剩余金额，占用利率
 * lastEquityCodeIdId 上一次
 * equityCode_id    本次
 */
function calculation(lastEquityCodeId, equityCode_id) {
    var id = equityCode_id.split("_")[1];
    //缴付金额
    var repoMoney = moneyType.get("repoMoney_" + id).val();
    if (surplusRepoMoney <= 0) {
        moneyType.get("repoMoney_" + id).val(surplusRepoMoney);
        moneyType.get("reRepoMoney_" + id).val(0);
        return;
    }
    //剩余回购金额
    if (Number(surplusRepoMoney) <= Number(repoMoney)) {
        moneyType.get("repoMoney_" + id).val(surplusRepoMoney);
        repoMoney = Number(surplusRepoMoney);
        moneyType.get("reRepoMoney_" + id).val(0);
        surplusRepoMoney = 0;
    } else {
        surplusRepoMoney = (surplusRepoMoney - Number(repoMoney)).toFixed(2);
        moneyType.get("reRepoMoney_" + id).val(surplusRepoMoney);
    }
}


/**
 * 根据本次缴付金额算出剩余缴付金额，本次占比（其他：循环每一行，之后调用calculation方法计算） */
function MoneyCalculation() {
    var trs = $("#planDetailTB").children("tbody").children("tr");
    var lastEquityCodeId = "-1";//上一笔还款本金id
    // 遍历行数据
    trs.each(function (index, tr) {
        var equityCode_id = "";//融资编码
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
                var $input = $(td).find("input");
                if ($input.length) {
                    if (!isNull($input.attr("id")) && $input.attr("id").indexOf("equityCode") >= 0) {
                        equityCode_id = $input.attr("id");
                    }
                }
            }
        )
        var isPay = $(tr).find("td[name^='isPay']").find("input").val();
        if (isPay == "1") {
            calculation(lastEquityCodeId, equityCode_id);
            lastEquityCodeId = equityCode_id;
        } else {
            lastEquityCodeId = equityCode_id;
        }
    })
    sumMoney();
}

/**
 * 各金额的合计
 */
function sumMoney() {
    var invMoney = $("#invMoney").val();
    //计算本次回购金额
    var repoMoneySum = 0;
    $("input[divName^='repoMoney']").each(function (i) {
        repoMoneySum = bt.money.add(repoMoneySum, $(this).val());
    });
    moneyType.get("repoMoneySum").val(repoMoneySum);
    //计算占比
    rateTypes.get("accountedSum").val(moneyFun.div(parseFloat(repoMoneySum), parseFloat(invMoney), 4))
    maxRepoMoney = moneyFun.sub(parseFloat(invMoney), parseFloat(repoMoneySum));//最大缴付金额
    surplusRepoMoney = maxRepoMoney
}




