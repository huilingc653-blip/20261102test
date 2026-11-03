var rowNum = 1;
var divName = null
var plMoney = "";//借款金额

function initSyndicatedDtl() {
    $.ajax({
        type: "post",
        url: basePath + "/cms/provide/providesyndicated/listProvideSyndicatedDtl",
        dataType: "json",
        data: {repayId: $("#id").val()},
        success: function (result) {
            if (result) {
                // 渲染表格
                createTbody(result);
            }
        }
    });
}

function createTbody(bodyData) {
    var syHtml = '';
    for (var i = 0; i < bodyData.length; i++) {
        rowNum = i;
        var data = bodyData[i];
        syHtml += '<tr data-row="' + rowNum + '">';
        syHtml += '<td style="width: 100px"><div class="layui-inline"><div class="bt-biz-dict" data-width="80px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedDtlList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + data.leadingBank + '"></div></div></td>';
        syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedDtlList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input" value="' + data.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
        syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedDtlList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + data.bankName + '" /></div></td>';
        //待还款金额
        syHtml += '<td style="width: 165px"><input readonly="readonly" class="layui-input bt-input-inline bt-biz-moneyType" style="width: 100%" divName="surplusMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="surplusMoney_' + rowNum + '" id="surplusMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].surplusMoney" value="' + (data.surplusMoney == null ? "" : fun.format(data.surplusMoney)) + '"/></td>';
        //本次还款金额
        syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline bt-biz-moneyType" style="width: 100%" divName="repaymentMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="repaymentMoney_' + rowNum + '" id="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoney" value="' + (data.repaymentMoney == null ? "" : fun.format(data.repaymentMoney)) + '"/></td>';
        //还款本金占比%
        syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-width="160px" divName="repaymentMoneyShare" data-biz-name="repaymentMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="repaymentMoneyShare_' + rowNum + '" data-min="0" data-superMax="99" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoneyShare" value="' + (data.repaymentMoneyShare == null ? "" : fun.format(data.repaymentMoneyShare)) + '"/></td>';
        //还款利息
        syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="repaymentIst" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentIst_' + rowNum + '" id="repaymentIst_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].commitmentMoney" value="' + (data.repaymentIst == null ? 0 : fun.format(data.repaymentIst)) + '"/></td>';
        syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
        syHtml += '</tr>';
        syHtml += '</tr>';
    }

    $('#dataTableSyBody').html(syHtml)
    addFun();
}

// 银团新增
function addSy() {
    var syHtml = '<tr data-row="' + rowNum + '">';
    syHtml += '<td style="width: 100px"><div class="layui-inline"><div class="bt-biz-dict" data-width="80px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedDtlList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="0"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedDtlList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"/> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedDtlList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input"/></div></td>';
    //待还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline bt-biz-moneyType" readonly="readonly" style="width: 100%" divName="surplusMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="surplusMoney_' + rowNum + '" id="surplusMoney_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedDtlList[' + rowNum + '].surplusMoney"/></td>';
    //本次还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inlin bt-biz-moneyType" style="width: 100%" divName="repaymentMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="repaymentMoney_' + rowNum + '" id="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoney"/></td>';
    //还款本金占比%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-width="160px" divName="repaymentMoneyShare" data-biz-name="repaymentMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="repaymentMoneyShare_' + rowNum + '" data-min="0" data-superMax="99" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoneyShare"/></td>';
    //还款利息
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline bt-biz-moneyType" style="width: 100%" divName="repaymentIst" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="repaymentIst_' + rowNum + '" id="repaymentIst_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedDtlList[' + rowNum + '].repaymentIst"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';

    $("#dataTableSy tbody").append(syHtml);
    bt.selectType.init();
    addFun();
}

//重新渲染日期、金额控件
function addFun() {
    //重新渲染datatype和moneyType
    rateType = bt.rateType.init();//利率
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateTypes = bt.dateType.init();//时间
    calCustomMoney();
    //+1为了以防id重复
    rowNum++;
}

//导入初始化
importExcel = function () {
    plMoney = $("#plMoney").val();//还款金额
    var surplusMoney = $("#plMoney").val();
    up = bt.btImport.init({
        code: "70000043",
        width: "100%",
        height: "100%",
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            for (let i = 0; i < select.length; i++) {
                var repaymentMoneyShare = fun.div(select[i].repaymentMoney, plMoney);
                surplusMoney = surplusMoney - select[i].repaymentMoney
                addImport(select[i], repaymentMoneyShare, surplusMoney);
            }
        }
    });
};

/**
 * 导入银团明细
 */
function addImport(o, repaymentMoneyShare, surplusMoney) {
    var leadingBank = 0;
    if (o.leadingBank == "是") {
        leadingBank = 1;
    }

    syHtml = '<tr data-row="' + rowNum + '">';
    syHtml += '<td style="width: 100px"><div class="layui-inline"><div class="bt-biz-dict" data-width="80px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedDtlList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + leadingBank + '"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedDtlList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input" value="' + o.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedDtlList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + o.bankName + '" /></div></td>';
    //待还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline bt-biz-moneyType" readonly="readonly" style="width: 100%" divName="surplusMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="surplusMoney_' + rowNum + '" id="surplusMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].surplusMoney" value="' + (surplusMoney == null ? "" : fun.format(surplusMoney)) + '"/></td>';
    //本次还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline bt-biz-moneyType" style="width: 100%" divName="repaymentMoney" data-callback="bt.calMoney" type="text" data-decimal="2" data-biz-name="repaymentMoney_' + rowNum + '" id="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoney" value="' + (o.repaymentMoney == null ? "" : fun.format(o.repaymentMoney)) + '"/></td>';
    //还款本金占比%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-width="165px" divName="repaymentMoneyShare" data-biz-name="repaymentMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="repaymentMoneyShare_' + rowNum + '" data-min="0" data-superMax="99" data-name="syndicatedDtlList[' + rowNum + '].repaymentMoneyShare" value="' + (repaymentMoneyShare == null ? "" : fun.format(repaymentMoneyShare)) + '"/></td>';
    //还款利息
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="repaymentIst" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentIst_' + rowNum + '" id="repaymentIst_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedDtlList[' + rowNum + '].commitmentMoney" value="' + (o.commitmentMoney == null ? 0 : fun.format(o.commitmentMoney)) + '"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';
    syHtml += '</tr>';
    $('#dataTableSyBody').append(syHtml)
    addFun();
};

/**
 * 合计
 */
function sumMoney() {
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (trs.length <= 0) {
        return;
    }
    var repaymentMoneyShareSum = 0;
    var repaymentIstSum = 0;
    var repaymentMoneySum = 0;
    $.each(trs, function (index, item) {
        var rowIndex = $(this).attr("data-row");
        repaymentMoneyShareSum += Number(rateType.get("repaymentMoneyShare_" + rowIndex).val());
        repaymentIstSum += Number(moneyType.get("repaymentIst_" + rowIndex).val());
        repaymentMoneySum += Number(moneyType.get("repaymentMoney_" + rowIndex).val());
    });
    moneyType.get("repaymentMoneySum").val(repaymentMoneySum);
    moneyType.get("repaymentMoneyShareSum").val(fun.format(repaymentMoneyShareSum * 100));
    moneyType.get("repaymentIstSum").val(repaymentIstSum);

}

/**
 * 删除当前行
 */
function delRow(row) {
    var index = row.parentNode.parentNode.rowIndex;
    document.getElementById("dataTableSy").deleteRow(index);
    sumMoney();
}

bt.bankCodeCallback = function (data, dom) {
    var tr = dom.parents('tr');
    var rowIndex = tr.attr("data-row");

    $("#bankName" + rowIndex).val(data.bankName);
    $("#bankCode" + rowIndex).val(data.bankCode);
}

/**
 * 金额改变事件
 */
bt.calMoney = function (data, el) {
    calCustomMoney();
}

/**
 * 自定义金额计算
 * @param trEle
 */
function calCustomMoney() {
    var total = getTotalMoney();
    //遍历表格，重新计算承贷份额%
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (trs.length <= 0) {
        return;
    }
    var surplusMoney = total;
    trs.each(function (index, tr) {
        var rowIndex = $(this).attr("data-row");
        var repayMoney = moneyType.get("repaymentMoney_" + rowIndex).val();
        surplusMoney = surplusMoney - repayMoney;
        moneyType.get("surplusMoney_" + rowIndex).val(surplusMoney);
        // 还款本金占比 = 本次还款金额/还款总金额
        var repaymentMoneyShare = fun.div(repayMoney, total,4);
        rateType.get("repaymentMoneyShare_" + rowIndex).val(repaymentMoneyShare);
    });
    sumMoney();
}

/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}

/**
 * 获取总还款金额
 * @returns {*}
 */
function getTotalMoney(){
    var total = 0;
    // 判断是否线上还款
    if ($("#onlineSign").val() === "1") {
        total = moneyType.get("onlineMoney").val();
    } else {
        total = moneyType.get("repayMoney").val();
    }
    return total;
}