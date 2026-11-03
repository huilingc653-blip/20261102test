var rowNum = 1;
var divName = null
var plMoney = 0;//借款金额
function initSyndicated() {
    $.ajax({
        type: "post",
        url: basePath + "/cms/provide/providesyndicated/listProvideSyndicated",
        dataType: "json",
        data: {directFinanceId: $("#directFinanceId").val()},
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
        syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-width="100px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + data.leadingBank + '"></div></div></td>';
        syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"value="' + data.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
        syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + data.bankName + '" /></div></td>';
        //承贷份额%
        syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-superMax="99" data-width="160px" divName="commitmentShare" data-biz-name="commitmentShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="commitmentShare_' + rowNum + '" data-min="0" data-name="syndicatedList[' + rowNum + '].commitmentShare" value="' + (data.commitmentShare == null ? "" : moneyFun.format(data.commitmentShare)) + '"/></td>';
        //本次还款金额
        syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="commitmentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="commitmentMoney_' + rowNum + '" id="commitmentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedList[' + rowNum + '].commitmentMoney" value="' + (data.commitmentMoney == null ? "" : moneyFun.format(data.commitmentMoney)) + '"/></td>';
        syHtml += '<td style="width: 165px"><input name="syndicatedList[' + rowNum + '].rmk" lay-verify="verifyLength" style="width: 100%" id="rmk" class="layui-input" type="text" value="' + (data.rmk == null ? "" : data.rmk) + '"/></td>';
        syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
        syHtml += '</tr>';
        syHtml += '</tr>';
    }
    $('#dataTableSyBody').html(syHtml)
    addFun();
}

// 银团新增
function addSy() {
    var plMoney = moneyType.get("plMoney").val();
    if (plMoney == 0) {
        layer.alert(bt.lang("cms.loaninfo.plmoney.message.miszero"),{icon: 7});
    }
    var syHtml = '<tr data-row="' + rowNum + '">';
    syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-width="100px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="0"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"/> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input"/></div></td>';
    //承贷份额%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-superMax="99" data-width="160" divName="commitmentShare" data-biz-name="commitmentShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="commitmentShare_' + rowNum + '" data-min="0" data-name="syndicatedList[' + rowNum + '].commitmentShare"/></td>';
    //本次还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="commitmentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="commitmentMoney_' + rowNum + '" id="commitmentMoney_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedList[' + rowNum + '].commitmentMoney"/></td>';
    syHtml += '<td style="width: 165px"><input style="width: 100%" lay-verify="verifyLength" name="syndicatedList[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';
    $("#dataTableSy tbody").append(syHtml);
    bt.selectType.init();
    bt.moneyType.init();
    addFun();
}

//重新渲染日期、金额控件
function addFun() {
    //重新渲染datatype和moneyType
    rateType = bt.rateType.init();//利率
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    calCustomMoney();
    //+1为了以防id重复
    rowNum++;
}

//导入初始化
importExcel = function () {
    plMoney = $("#plMoney").val();
    up = bt.btImport.init({
        code: "70000041",
        width: "100%",
        height: "100%",
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            for (let i = 0; i < select.length; i++) {
                var commitmentShare = moneyFun.div(select[i].commitmentMoney, plMoney);
                addImport(select[i], commitmentShare);
            }
        }
    });
};

/**
 * 导入银团明细
 */
function addImport(o, commitmentShare) {
    var leadingBank = 0;
    if (o.leadingBank == "是") {
        leadingBank = 1;
    }

    var syHtml = '<tr data-row="' + rowNum + '">';
    syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-width="100px" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + leadingBank + '"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" id="bankCode' + rowNum + '" style="width: 100%" name="syndicatedList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"value="' + o.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + o.bankName + '" /></div></td>';
    //承贷份额%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-width="160px" data-superMax="99" divName="commitmentShare" data-biz-name="commitmentShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="commitmentShare_' + rowNum + '" data-min="0" data-name="syndicatedList[' + rowNum + '].commitmentShare" value="' + (commitmentShare == null ? "" : moneyFun.format(commitmentShare)) + '"/></td>';
    //本次还款金额
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="commitmentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="commitmentMoney_' + rowNum + '" id="commitmentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedList[' + rowNum + '].commitmentMoney" value="' + (o.commitmentMoney == null ? "" : moneyFun.format(o.commitmentMoney)) + '"/></td>';
    syHtml += '<td style="width: 165px"><input name="syndicatedList[' + rowNum + '].rmk" lay-verify="verifyLength" id="rmk" style="width: 100%" class="layui-input" type="text" value="' + (o.rmk == null ? "" : o.rmk) + '"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';
    syHtml += '</tr>';

    $("#dataTableSy tbody").append(syHtml);
    bt.selectType.init();
    addFun();
};

/**
 * 合计
 */
function sumMoney() {
    plMoney = $("#plMoney").val();//还款金额
    if (!plMoney) {
        return;
    }
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (trs.length <= 0) {
        return;
    }
    var commitmentMoneySum = 0;
    var commitmentShareSum = 0;
    $.each(trs, function (index, item) {
        var rowIndex = $(this).attr("data-row");
        commitmentMoneySum += Number(moneyType.get("commitmentMoney_" + rowIndex).val());
        commitmentShareSum += Number(rateType.get("commitmentShare_" + rowIndex).val());
    });
    //计算承贷金额
    moneyType.get("commitmentMoneySum").val(commitmentMoneySum);
    //计算承贷份额%
    moneyType.get("commitmentShareSum").val(moneyFun.format(commitmentShareSum * 100, 2));
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
    plMoney = $("#plMoney").val();
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (trs.length == 0) {
        return;
    }
    $.each(trs, function (index, tr) {
        var rowIndex = $(this).attr("data-row");
        var commitmentMoney = moneyType.get("commitmentMoney_" + rowIndex).val();
        // 承贷份额% = 借款金额 和 承贷金额 的比值
        var commitmentShare = moneyFun.div(commitmentMoney, plMoney, 4);
        rateType.get("commitmentShare_" + rowIndex).val(commitmentShare);
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
