var rowNum = 1;
var divName = null

function initSyndicatedSend() {
    var id = $("#id").val();//判断是否为暂存还是待发放
    if(id){
        $.ajax({
            type: "post",
            url: basePath + "/cms/provide/providesyndicated/listProvideSyndicatedSend",
            dataType: "json",
            data: {provideLoanSendId: id},
            success: function (result) {
                if (result) {
                    // 渲染表格
                    createTbody(result);
                }
            }
        });
    } else {
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
}

function createTbody(bodyData) {
    var syHtml = '';
    for (var i = 0; i < bodyData.length; i++) {
        rowNum = i;
        var data = bodyData[i];
        syHtml += '<tr data-row="' + rowNum + '">';
        syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-width="100px" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedSendList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + data.leadingBank + '"></div></div></td>';
        syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedSendList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"value="' + data.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
        syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedSendList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + data.bankName + '" /></div></td>';
        //未发放金额
        syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="notsendMoney" style="width: 100%" type="text" data-decimal="2" data-biz-name="notsendMoney_' + rowNum + '" id="notsendMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedSendList[' + rowNum + '].notsendMoney" value="' + (data.notsendMoney == null ? "" : fun.format(data.notsendMoney)) + '"/></td>';
        //本次放款
        syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="sendMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="sendMoney_' + rowNum + '" id="sendMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedSendList[' + rowNum + '].sendMoney" value="' + (data.sendMoney == null ? "" : fun.format(data.sendMoney)) + '"/></td>';
        //放款占比%
        syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-superMax="99" data-width="160px" divName="sendMoneyShare" data-biz-name="sendMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="sendMoneyShare_' + rowNum + '" data-min="0" data-name="syndicatedSendList[' + rowNum + '].sendMoneyShare" value="' + (data.sendMoneyShare == null ? "" : moneyFun.format(data.sendMoneyShare)) + '"/></td>';
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
    syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-width="100px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedSendList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="0"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedSendList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"/> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedSendList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input"/></div></td>';
    //未发放金额
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="notsendMoney" type="text" data-decimal="2" data-biz-name="notsendMoney_' + rowNum + '" id="notsendMoney_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedSendList[' + rowNum + '].notsendMoney"/></td>';
    //本次放款
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="sendMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="sendMoney_' + rowNum + '" id="sendMoney_' + rowNum + '" data-min="0" lay-verify="required" value="0" data-name="syndicatedSendList[' + rowNum + '].sendMoney"/></td>';
    //放款占比%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-superMax="99" data-width="160px" divName="sendMoneyShare" data-biz-name="sendMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="sendMoneyShare_' + rowNum + '" data-min="0" data-name="syndicatedSendList[' + rowNum + '].sendMoneyShare"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';
    $("#dataTableSy tbody").append(syHtml);
    bt.selectType.init();
    addFun();
}

//重新渲染日期、金额控件
function addFun() {
    //重新渲染datatype和moneyType
    rateTypes.render();
    moneyTypes.render();
    dicts.render();
    //+1为了以防id重复
    rowNum++;
    calCustomMoney();
}

//导入初始化
importExcel = function () {
    var loanMoney = beforeLoanMoney;
    var notsendMoney = beforeLoanMoney;
    up = bt.btImport.init({
        code: "70000042",
        width: "100%",
        height: "100%",
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            for (var i = 0; i < select.length; i++) {
                var sendMoneyShare = moneyFun.div(select[i].sendMoney, loanMoney);
                notsendMoney = notsendMoney - select[i].sendMoney
                addImport(select[i], sendMoneyShare, notsendMoney);
            }
        }
    });
};

/**
 * 导入银团明细
 */
function addImport(o, sendMoneyShare, notsendMoney) {
    var leadingBank = 0;
    if (o.leadingBank == "是") {
        leadingBank = 1;
    }
    var syHtml = '<tr data-row="' + rowNum + '">';
    syHtml += '<td style="width: 120px"><div class="layui-inline"><div class="bt-biz-dict" data-width="100px" data-type="select" data-displayData="0,1" data-group="sf_yn" data-name="syndicatedSendList[' + rowNum + '].leadingBank" data-biz-name="leadingBank' + rowNum + '" data-defaultvalue="' + leadingBank + '"></div></div></td>';
    syHtml += '<td style="width: 165px"><div class="layui-input-inline"><input type="text" style="width: 100%" id="bankCode' + rowNum + '" name="syndicatedSendList[' + rowNum + '].bankCode" autocomplete="off" lay-verify="required" readonly class="layui-input"value="' + o.bankCode + '" /> <div class="bt-div-search"> <button class="bt-biz-bankInfo" type="button" data-callback="bt.bankCodeCallback"><i class="layui-icon">&#xe615;</i> </button></div></div></td>';
    syHtml += '<td style="width: 200px"><div class="layui-inline"><input type="text" name="syndicatedSendList[' + rowNum + '].bankName" id="bankName' + rowNum + '" lay-verify="required" autocomplete="off" readonly class="layui-input" value="' + o.bankName + '" /></div></td>';
    //未发放金额
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-moneyType" style="width:100%" divName="notsendMoney"  type="text" data-decimal="2" data-biz-name="notsendMoney_' + rowNum + '" id="notsendMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedSendList[' + rowNum + '].notsendMoney" value="' + (notsendMoney == null ? "" : fun.format(notsendMoney)) + '"/></td>';
    //本次放款
    syHtml += '<td style="width: 165px"><input class="layui-input bt-input-inline  bt-biz-moneyType" style="width: 100%" divName="sendMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="sendMoney_' + rowNum + '" id="sendMoney_' + rowNum + '" data-min="0" lay-verify="required" data-name="syndicatedSendList[' + rowNum + '].sendMoney" value="' + (o.sendMoney == null ? "" : fun.format(o.sendMoney)) + '"/></td>';
    //放款占比%
    syHtml += '<td style="width: 165px"><input disabled class="layui-input bt-input-inline bt-biz-rateType" data-superMax="99" data-width="160px" divName="sendMoneyShare" data-biz-name="sendMoneyShare_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="sendMoneyShare_' + rowNum + '" data-min="0" data-name="syndicatedSendList[' + rowNum + '].sendMoneyShare"  value="' + (sendMoneyShare == null ? "" : moneyFun.format(sendMoneyShare)) + '"/></td>';
    syHtml += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    syHtml += '</tr>';
    syHtml += '</tr>';
    $("#dataTableSy tbody").append(syHtml);
    addFun();
};

/**
 * 合计
 */
function sumMoney() {
    //本次放款
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (!trs) {
        return
    }
    var sendMoneySum = 0;
    var sendMoneyShareSum = 0;
    $.each(trs, function (index,item) {
        var rowIndex = $(this).attr("data-row");
        sendMoneySum += Number(moneyTypes.get("sendMoney_" + rowIndex).val());
        sendMoneyShareSum += Number(rateTypes.get("sendMoneyShare_" + rowIndex).val());
    });
    // 放款金额合计
    moneyTypes.get("sendMoneySum").val(sendMoneySum);
    //放款占比%
    moneyTypes.get("sendMoneyShareSum").val(sendMoneyShareSum * 100);
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
    calCustomMoney(el.parents("tr"));
}

/**
 * 自定义金额计算
 * @param trEle
 */
function calCustomMoney() {
    var sendMoney = moneyTypes.get("money").val();
    if (!sendMoney) {
        return;
    }
    //遍历表格，重新计算承贷份额%
    var trs = $("#dataTableSy").children("tbody").children("tr");
    if (!trs) {
        return;
    }
    var initplMoney = sendMoney;
    $.each(trs, function (index, tr) {
        var rowIndex = $(this).attr("data-row");
        var itemSendMoney = moneyTypes.get("sendMoney_" + rowIndex).val();
        var notsendMoney_ = fun.sub(initplMoney, itemSendMoney);
        initplMoney = notsendMoney_;
        moneyTypes.get("notsendMoney_" + rowIndex).val(notsendMoney_);
        // 放款占比% = 借款金额 和 本次放款 的比值
        var sendMoneyShare = fun.div(itemSendMoney, sendMoney, 4);
        rateTypes.get("sendMoneyShare_" + rowIndex).val(sendMoneyShare);
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
