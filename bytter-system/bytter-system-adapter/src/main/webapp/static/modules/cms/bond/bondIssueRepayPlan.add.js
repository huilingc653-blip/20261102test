var rowNum = 0;
var moneyType = "";
var rateType = "";
var dicts;
var dateFun;
var remainMoney = "";//剩余本金(用于每次计算使用)
var remainMoneyFixed = "";//固定的剩余金额（即除去已还金额，剩余的金额）
var beginDate = "";//起租日
var endDate = "";//到期日
var loanDate = "";//发放日期
var repayDate = "";//还款日期
var payMoney = "";//发放金额
var issueMoney = "";//债券发行金额，每次切换发行编号时进行更新
var issueId = ""; //债券发行Id
var bondIssueNum = ""; //债券发行编号
var bondIssueIdAndNumData = ""; //债券发行编号
var loanRate = "";//利率
var isRepay = "0";//初始化时，若为1：已还，则设置不可编辑
var repayTypeOld = "";
var repayType = "";//还款方式
var rateBase = 360;//利息测算基数
var repayCycel;//还款周期
var principal = 1;//在等额本金下，当本金除不尽时，控制本金合计 0:合计不做处理 1：合计需要做处理
var isInit = true;//判断是不是第一次进页面，初始化标识
var zqRate = 0;//展期利率
var zqDate = "";//展期日期
var remainDate;//已还的最后一笔日期加一天
var selectType;
var dateType;

bt.use(['form', 'element', 'moneyType', 'dateType', 'bttable', 'dicts', 'btImport', 'money', 'rateType', 'selectType'], function () {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.init();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();
    selectType = bt.selectType.init();
    issueId = $("#issueId").val();
    bondIssueNum = $("#bondIssueNum").val();

    initVariable();
    initTableData();

    //标签和状态显示
    // var bondIssueIsSave = $("#isSave").val();
    // var bondIssueStatus = $("#status").val();
    // buttonShow(bondIssueIsSave, bondIssueStatus);

    /**
     * 保存
     */
    form.on('submit(save)', function (data) {
        var repayAmountSum = Number(moneyType.get("repayAmountSum").val());
        var issueMoney = Number(moneyType.get("issueMoney").val());
        if (repayAmountSum > issueMoney) {
            layer.alert("还款金额合计不能大于本次发行金额", {icon: 2});
            return false;
        }
        bt.loading.show();
        // 数据校验
        var action = "/cms/bond/issue/savePlan/add";
        data.field.cmsBondRepayPlanDtlMap = []
        data.field.id = $("#planId").val()
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();
                //给父级选项卡页面中id为planId的input赋值;
                webObj.bt.jquery("#planId").val(result.obj);
                bt.navtab.closeCurrent();
                // bt.navtab.refreshCurrent();
            }
        }, 'JSON');
        return false;
    });
    repayTypeChanges($("#orig_repayType").val());
});

//导出
exportExcel = function () {
    var url = basePath + "/cms/bond/plan/exportBudget?id=" + $("#id").val();
    window.location.href = url;
}

function resetClick() {
    window.location.reload();
}

//重新渲染日期、金额控件
function addFun() {
    //重新渲染datatype和moneyType
    dateType.render();
    moneyType.render();
    dicts.render();
    sumMoney();
    //+1为了以防id重复
    rowNum++;
}

/**
 * 预计还款日改变事件（其他：重新计算金额）
 * @param value
 */
function clickEvent(value) {
    if (repayType === "07") {
        checkDateByCustomRepay();
    } else {
        MoneyCalculation();
    }
}

/**
 * 还款开始日改变事件
 */
function beginDateChange() {
    if (isNull(dicts.get('repayCycel').val())) {
        return;
    }
    loadData();
}

/**
 * 还款周期改变事件
 * @param data
 */
function cycleChange(data) {
    //还款方式为一次性还款就返回
    repayCycel = data.value;
    if (data.value == "3") {
        var payDate;
        if (isNull(zqDate)) {
            payDate = repayDate;
        } else {
            payDate = zqDate;
        }
        var payMoney = remainMoneyFixed;
        clearData();//清空未还数据(还款方式除了其他的时候)
        addOneOffPayment(payDate, payMoney);
        return;
    }
    //若没有选择还款开始日则提示选择日期
    if (isNull($('#repayStartDate').val())) {
        parent.layer.alert(bt.lang("cms.leaseCapitalPlanchedule.noSelectBeginDate"));
        return;
    }
    loadData();
}

/**
 * 设置还款开始日期的范围
 */
function setBeginDate() {
    var getCurrentDate = dateType.get("repayStartDate");
    //设置时间
    remainDate = dateFun.format(getCurrentDate.val());
    if (!isNull(zqDate)) {
        getCurrentDate.attr("data-max", zqDate);
    }
}

/**
 * 清空未还数据
 */
function clearData() {
    remainMoneyFixed = Number(payMoney);
    var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
    // 遍历行数据
    trs.each(function (index, tr) {
        var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
        if (isRepay == "0") {
            $(tr).remove();
        } else {
            var repayMoney_id = $(tr).find("td[name^='repayMoney']").find("input").attr("data-id");
            var money = moneyType.get(repayMoney_id).val();
            remainMoneyFixed = (Number(remainMoneyFixed) - Number(money)).toFixed(2);
            remainMoney = (Number(remainMoney) - Number(money)).toFixed(2);
        }
    })
}

/**
 * 选择还款方式为其他的时候设置数据
 */
function setOther() {
    var payDateId
    //若上一次为请选择
    if (isNull(repayTypeOld)) {
        //设置剩余本金
        remainMoneyFixed = Number(payMoney);
        var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
        //遍历行数据
        trs.each(function (index, tr) {
            var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
            if (isRepay != "0") {
                var repayMoney_id = $(tr).find("td[name^='repayMoney']").find("input").attr("data-id");
                var money = moneyType.get(repayMoney_id).val();
                remainMoneyFixed = (Number(remainMoneyFixed) - Number(money)).toFixed(2);
            } else {
                $(tr).find("td[name^='payDate']").find("input").removeAttr("disabled");
                $(tr).find("td[name^='repayMoney']").find("input").removeAttr("disabled");
                $(tr).find("td[name^='itemOrder']").find("#del").show();

                payDateId = $(tr).find("td[name^='payDate']").find("input").attr("id");
                if (!isNull(remainDate)) {
                    dateType.get(payDateId).attr("data-min", remainDate);
                }
                if (!isNull(zqDate)) {
                    dateType.get(payDateId).attr("data-max", zqDate);
                }
                //$(tr).find("td[name^='itemOrder']").find("button").removeAttr("disabled");
            }
        })
    } else {
        clearData();
    }
    addFun();
}

/**
 * 还款方式为02,04时，加载的数据(通过点击还款周期加载数据)
 */
function loadData() {
    repayCycel = dicts.get("repayCycel").val();
    var payMoney = new Array();//还款本金数组
    var payDate = getDates();//还款日期数组

    //删除之前的table数据
    clearData();
    if (isNull($('#repayStartDate').val())) {
        return;
    }

    //分期等额本金
    if (repayType == "02" || repayType == "05") {
        var money = (remainMoneyFixed / (payDate.length)).toFixed(2);//还款本金
        for (var i = 0; i < payDate.length; i++) {
            payMoney.push(money);
        }
        var payInterest = true;
        if (repayType == "05") {
            payInterest = false;
        }
        addEqualPrincipal(payDate, payMoney, payInterest);
    }
    //分期等额本息
    if (repayType == "04") {
        addEqualPrincipalInterest(payDate);
    }
}

/**
 * 还款方式回调函数
 * @param data
 */
function repayTypeChange(data) {
    repayTypeChanges(data.value);
    //若没有选择还款开始日则提示选择日期
    if (isNull($('#repayStartDate').val())) {
        parent.layer.alert(bt.lang("cms.RepaymentSchedule.noSelectBeginDate"));
        return;
    }
    loadData();
}

/**
 * 还款方式改变事件
 * @param data
 */
function repayTypeChanges(data) {
    if (isInit) {
        repayTypeOld = $("#repayType").data("defaultvalue");
    } else {
        repayTypeOld = repayType;
    }
    isInit = false;
    repayType = data;//为repayType赋值
    //等额本金
    if (repayType == "02" || repayType == "05") {
        principal = 1;
    } else {
        principal = 0;
    }
    //其它及自定义
    if (repayType == "03" || repayType == "06" || repayType == "07" || isNull(repayType)) {
        dateType.get("repayStartDate").val("");
        $("#repayStartDate").attr("disabled", "true");
        dicts.get('repayCycel').attr({
            'data-value': '',
            'data-disabledValue': '0,1,2,3'
        });
        setOther();//设置数据
        return;
    }
    //还本付息
    if (repayType == "01") {
        $("#repayStartDate").attr("disabled", "true");
        var payDate;
        if (isNull(zqDate)) {
            payDate = repayDate;
        } else {
            payDate = zqDate;
        }
        if (isNull($("#orig_repayCycel").val())) {
            dicts.get('repayCycel').attr({
                'data-value': '',
                'data-disabledValue': '0,1,2'
            });
            clearData();//清空未还数据(还款方式除了其他的时候)
            return;
        } else {
            dicts.get('repayCycel').attr({
                'data-value': 3,
                'data-disabledValue': '0,1,2'
            });
            repayCycel = '3';
        }
        clearData();//清空未还数据(还款方式除了其他的时候)
        var payMoney = remainMoneyFixed;
        $("#disposable").attr("checked", true);
        addOneOffPayment(payDate, payMoney);
        return;
    }

    //等额本金和等额本息
    $("#repayStartDate").removeAttr("disabled");
    if (isNull($("#orig_repayCycel").val())) {
        dicts.get('repayCycel').attr({
            'data-value': '0',
            'data-disabledValue': '3'
        });
        clearData();//清空未还数据(还款方式除了其他的时候)
        return;
    } else {
        if (repayType != $("#orig_repayType").val()) {
            dicts.get('repayCycel').attr({
                'data-value': '0',
                'data-disabledValue': '3'
            });
        } else {
            dicts.get('repayCycel').attr({
                'data-value': $("#orig_repayCycel").val(),
                'data-disabledValue': '3'
            });
        }
        clearData();//清空未还数据(还款方式除了其他的时候)
    }
    //repayCycel="0";
    repayCycel = dicts.get('repayCycel').val();
    dicts.render();
    loadData();
}

/**
 * 加载数据
 */
function initVariable() {
    beginDate = dateType.get("issueStartDate").val();
    endDate = dateType.get("issueEndDate").val();

    loanDate = $("#loanDate").val();//发放日期
    if ("" != $("#deferredRepayDate").val()) {
        repayDate = $("#deferredRepayDate").val();//还款日期
    } else {

    }
    repayDate = $("#repayDate").val();//还款日期
    payMoney = $("#payMoney").val();//债券金额
    loanRate = Number($("#loanRate").val());//利率
    zqRate = $("#zqRate").val();//展期利率
    zqDate = $("#zqDate").val();//展期日期
    //alert("展期---"+zqDate+",还款日期--");
    if (!isNull($("#rateBase").val())) {
        rateBase = Number($("#rateBase").val());
    }
    var billCode = $("#billCode").val();
    var id = $("#id").val();
    var type = $("#type").val();
    //设在ifram的src
    if (type == "0") {
        $("#loanRepayIframe").attr("src", basePath + "/cms/provide/loansend/viewSend/" + billCode);
    } else if (type == "1") {
        $("#loanRepayIframe").attr("src", basePath + "/cms/loan/cmsloansend/view/" + id + "?param=1");
    } else if (type == "2") {
        $("#loanRepayIframe").attr("src", basePath + "/cms/precative/loanrepay/view?sendCode=" + billCode + "&type=type");
    } else if (type == "0") {
        $("#loanRepayIframe").attr("src", basePath + "/cms/precative/loanrepay/view?sendCode=" + billCode + "&type=type");
    }
    remainMoneyFixed = Number(payMoney);//剩余本金（初始化为发放金额）
    remainMoney = Number(payMoney);//剩余本金（初始化为发放金额）
}


function initBondIssueIdAndNum() {
    var registerId = $("#registerId").val();
    $.ajax({
        type: "GET",
        url: basePath + "/cms/bond/issue/listValidBondIssueNum/" + registerId,
        dataType: "json",
        success: function (data) {
            bondIssueIdAndNumData = data
        }
    });
}

function initTableData() {
    $.ajax({
        type: "post",
        url: basePath + "/cms/bond/issue/listPlanDtl",
        dataType: "json",
        data: {planId: $("#planId").val()},
        success: function (result) {
            if (result) {
                // 渲染表格
                createTbody(result);
            }
        }
    });
}

function createTbody(bodyData) {
    var orig_repayType = $("#orig_repayType").val();
    var html = '';
    for (var i = 0; i < bodyData.length; i++) {
        rowNum = i
        var data = bodyData[i];
        data.bondIssueNum = $("#bondIssueNum").val()
        isRepay = data.isRepay == "1" ? "disabled" : "";
        html += '<tr class="bondIssueNum_' + data.bondIssueNum + '">';
        //债券发行编号
        html += '<td><input ' + isRepay + ' readonly name="cmsBondRepayPlanDtls[' + rowNum + '].bondIssueNum" id="bondIssueNum_' + rowNum + '" value="' + (data.bondIssueNum == null ? "" : data.bondIssueNum) + '" class="layui-input" type="text"/></td>';
        //还款日期
        if (data.isRepay == "1") {
            html += '<td><input disabled data-biz-name="repayDate_' + rowNum + '"name="cmsBondRepayPlanDtls[' + rowNum + '].repayDate" id="repayDate_' + rowNum + '" type="text" value="' + data.repayDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/></td>';
        } else {
            html += '<td><input divName="repayDate" data-biz-name="repayDate_' + rowNum + '" name="cmsBondRepayPlanDtls[' + rowNum + '].repayDate" id="repayDate_' + rowNum + '" type="text" value="' + data.repayDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/></td>';
        }
        //计划还款日
        if (data.isRepay == "1") {
            html += '<td><input disabled data-biz-name="planRepayDate_' + rowNum + '"name="cmsBondRepayPlanDtls[' + rowNum + '].planRepayDate" id="planRepayDate_' + rowNum + '" type="text" value="' + data.planRepayDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/></td>';
        } else {
            html += '<td><input divName="planRepayDate" data-biz-name="planRepayDate_' + rowNum + '" name="cmsBondRepayPlanDtls[' + rowNum + '].planRepayDate" id="planRepayDate_' + rowNum + '" type="text" value="' + data.planRepayDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/></td>';
        }
        //还款本金值
        if (data.isRepay != "1" && (orig_repayType == "03" || orig_repayType == "07")) {
            html += '<td><input divName="repayMoney" class="layui-input bt-input-inline bt-biz-moneyType" value="' + (data.repayMoney == null ? "" : moneyFun.format(data.repayMoney)) + '" data-callback="bt.calMoneyNow"  type="text" data-decimal="2" id="repayMoney_' + rowNum + '" data-min="0" data-max="' + payMoney + '" data-biz-name="repayMoney_' + rowNum + '" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayMoney"/></td>';
        } else {
            html += '<td><input disabled divName="repayMoney" class="layui-input bt-input-inline  bt-biz-moneyType" value="' + (data.repayMoney == null ? "" : moneyFun.format(data.repayMoney)) + '" data-callback="bt.calMoneyNow"  type="text" data-decimal="2" id="repayMoney_' + rowNum + '" data-min="0" data-max="' + payMoney + '" data-biz-name="repayMoney_' + rowNum + '" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayMoney"/></td>';
        }
        //剩余本金值
        if (orig_repayType == "03" || orig_repayType == "07") {
            html += '<td><input readonly="readonly"  value="' + (data.remainMoney == null ? "" : moneyFun.format(data.remainMoney)) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="remainMoney_' + rowNum + '" type="text" data-decimal="2" id="remainMoney_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].remainMoney"/></td>';
        } else {
            html += '<td><input disabled  value="' + (data.remainMoney == null ? "" : moneyFun.format(data.remainMoney)) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="remainMoney_' + rowNum + '" type="text" data-decimal="2" id="remainMoney_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].remainMoney"/></td>';
        }
        //本次还款金额
        if (data.isRepay != "1" && (orig_repayType == "03" || orig_repayType == "07")) {
            html += '<td><input divName="repayAmount_' + data.bondIssueNum + '" class="layui-input bt-input-inline bt-biz-moneyType" value="' + (data.repayAmount == null ? "" : moneyFun.format(data.repayAmount)) + '" data-callback="bt.calMoney"  type="text" data-decimal="2" id="repayAmount_' + rowNum + '" data-min="0" data-max="' + payMoney + '" id="repayAmount_' + rowNum + '" data-biz-name="repayAmount_' + rowNum + '" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayAmount"/></td>';
        } else {
            html += '<td><input disabled divName="repayAmount_' + data.bondIssueNum + '" class="layui-input bt-input-inline  bt-biz-moneyType" value="' + (data.repayAmount == null ? "" : moneyFun.format(data.repayAmount)) + '" data-callback="bt.calMoney"  type="text" data-decimal="2" id="repayAmount_' + rowNum + '" data-min="0" data-max="' + payMoney + '" id="repayAmount_' + rowNum + '" data-biz-name="repayAmount_' + rowNum + '" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayAmount"/></td>';
        }
        //本次偿还站发行总额比例%
        if (orig_repayType == "03" || orig_repayType == "07") {
            html += '<td><input readonly="readonly" divName="repayRate_' + data.bondIssueNum + '" divName="repayRate" class="layui-input bt-input-inline bt-biz-rateType" data-ratetype="percent" data-biz-name="repayRate_' + rowNum + '" type="text" value="' + (data.repayRate == null ? "" : moneyFun.format(data.repayRate)) + '" data-decimal="2" id="repayRate_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayRate"/></td>';
        } else {
            html += '<td><input disabled divName="repayRate_' + data.bondIssueNum + '" class="layui-input bt-input-inline bt-biz-rateType" data-ratetype="percent" data-biz-name="repayRate_' + rowNum + '" type="text" value="' + (data.repayRate == null ? "" : moneyFun.format(data.repayRate)) + '" data-decimal="2" id="repayRate_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayRate"/></td>';
        }
        //是否已还
        html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="cmsBondRepayPlanDtls[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="' + data.isRepay + '"></div></td>';
        //备注
        html += '<td><input ' + isRepay + ' name="cmsBondRepayPlanDtls[' + rowNum + '].rmk" id="rmk" value="' + (data.rmk == null ? "" : data.rmk) + '" class="layui-input" type="text"/></td>';
        //操作
        if (data.isRepay == "0") {
            html += '<td><button id="del" data-type="del" type="button" onclick="delRow(this)" name="itemOrder" value="' + rowNum + '" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
        } else {
            html += "";
        }
        html += '</tr>';
    }

    $("#cmsBondRepayPlanBody").html(html)

    // 根据当前发行编号删选数据
    $('#cmsBondRepayPlanBody').find('tr').hide()
    $('.bondIssueNum_' + bondIssueNum).show();
    bt.dicts.init();
    bt.rateType.init();

    //表格初始化回调函数
    setBeginDate();
    addFun();
}

function buttonShow(bondIssueIsSave, bondIssueStatus) {
    if (bondIssueIsSave == '1') {
        if (bondIssueStatus == '11') {
            $('#gotoUpdate').hide()
            $('#statusIsWait').show()
            $('#addTrShow').hide()
            $('#importsShow').hide()
        } else {
            $('#gotoUpdate').hide()
            $('#statusIsWait').hide()
            $('#addTrShow').show();
            $('#importsShow').show();
        }
    } else {
        if (bondIssueStatus == '11') {
            $('#gotoUpdate').show()
            $('#statusIsWait').hide()
            $('#addTrShow').hide()
            $('#importsShow').hide()
        } else {
            $('#gotoUpdate').hide()
            $('#statusIsWait').hide()
            $('#addTrShow').show();
            $('#importsShow').show();
        }
    }
}

/**
 * 合计
 */
function sumMoney() {
    //计算本次还款金额
    var repayAmountSum = 0;
    $("input[divName^='repayAmount_" + bondIssueNum + "']").each(function (i) {
        repayAmountSum = bt.money.add(repayAmountSum, $(this).val());
    });
    moneyType.get("repayAmountSum").val(repayAmountSum);

    //计算本次偿还站发行总额比例%
    var repayRateSum = 0;
    $("input[divName='repayRate_" + bondIssueNum + "']").each(function (i) {
        repayRateSum = bt.money.add(repayRateSum, $(this).val());
    });
    moneyType.get("repayRateSum").val(repayRateSum);
}

/**
 * 关闭窗口
 */
doClose = function () {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 提交时验证
 */
function verification() {
    var repayType = dicts.get("repayType").val();
    if (repayType == "02" || repayType == "05" || repayType == "04") {
        var startDate = dateType.get("repayStartDate").val();
        if (isNull(startDate)) {
            parent.layer.alert(bt.lang("cms.RepaymentSchedule.noSelectBeginDate"), {icon: 2});//未选择还款开始日期
            return false;
        }
    } else if (repayType == "03" || repayType == "06" || repayType == "07") {
        // 还款方式未其他，则校验金额
        //统计还款本金
        var sumMoney = 0;
        var flag = false;////本金利息同时为0标识
        $.each($("input[id*='repayAmount_']"), function (i, item) {
            if ($(item).val() <= 0) {
                var dataId = $(item).attr("id");
                if (-1 != dataId.indexOf("repayAmount_")) {
                    var moneyInd = dataId.substr(dataId.indexOf("_") + 1);
                    if (moneyType.get("remainMoney_" + moneyInd).val() <= 0) {
                        flag = true;//本金利息同时为0
                    }
                }
            }
            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        if (flag) {
            parent.layer.alert(bt.lang("cms.message.repayAmount.zero"));//债券本金和债券利息不能同时为零
            return false;
        }
        if (Number(sumMoney) > Number(payMoney)) {
            parent.layer.alert(bt.lang("cms.message.repayAmount_Principal"), {icon: 2});//债券本金大于债券支付金额，不能保存
            return false;
        }
    }
    return true;
}

/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}

/**
 * 金额改变事件
 */
var divName = null
bt.calMoney = function (data, el) {
    calCustomMoney(el.parents("tr"), false);
}
bt.calMoneyNow = function (data, el) {
    calCustomMoney(el.parents("tr"), true);
}

/**
 * 自定义金额计算
 * @param trEle
 */
function calCustomMoney(trEle, flag) {
    // 行下标
    var rowNum = trEle.find("input[divName^='repayMoney']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var repayMoney = moneyType.get("repayMoney_" + rowNum).val();
        if (flag) {
            moneyType.get("repayAmount_" + rowNum).val(repayMoney)
        }
        var repayAmount = moneyType.get("repayAmount_" + rowNum).val();
        var rmbIssueTotal = $("#issueMoney").val();
        if (!isNull(repayMoney)) {
            //遍历表格，重新计算剩余本金
            var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
            // 遍历行数据
            var initpayMoney = $("#issueMoney").val();
            trs.each(function (index, tr) {
                var idValue = $(this).find("input[id^='bondIssueNum']").val();
                if (idValue == bondIssueNum) {
                    var rowIndex = $(this).find("input[divName^='repayMoney']").data("id").split("_")[1];
                    var repayMoney = $(this).find("input[divName^='repayMoney']").val();
                    var remainMoney_ = moneyFun.sub(initpayMoney, repayMoney);
                    initpayMoney = remainMoney_;
                    moneyType.get("remainMoney_" + rowIndex).val(remainMoney_);
                }
            });
            sumMoney();
        }
        if (!isNull(repayAmount)) {
            //遍历表格，重新计算本次偿还占发行总额比例%
            var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
            trs.each(function (index, tr) {
                var idValue = $(this).find("input[id^='bondIssueNum']").val();
                if (idValue == bondIssueNum) {
                    var rowIndex = $(this).find("input[divName^='repayAmount']").data("id").split("_")[1];
                    var repayAmount = $(this).find("input[divName^='repayAmount']").val();
                    // 本次偿还占发行总额比例% = 本次还款金额 和 债券发行金额 的比值
                    var repayRate = moneyFun.div(repayAmount, rmbIssueTotal);
                    rateType.get("repayRate_" + rowIndex).val(repayRate);
                }
            });
            sumMoney();
        }
    }
}

/**
 *日期检查，即下一个还款日期不能小于上一个还款日期
 */
function checkDateByCustomRepay() {
    var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
    var lastpayDateId = "";//上一笔还款日期id
    // 遍历行数据
    trs.each(function (index, tr) {
        var payDate_id = "";//预计还款日期
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
                var $input = $(td).find("input");
                if ($input.length) {
                    if (!isNull($input.attr("id")) && $input.attr("id").indexOf("payDate") >= 0) {
                        payDate_id = $input.attr("id");
                    }
                }
            }
        )
        var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
        if (isRepay != "0") {
            lastpayDateId = payDate_id;
        } else {
            var id = payDate_id.split("_")[1];
            var payDate = $("#payDate_" + id).val();//预计还款日
            //上一笔日期
            var lastDate;
            if (isNull(lastpayDateId)) {
                lastDate = loanDate;
            } else {
                lastDate = dateType.get(lastpayDateId).val()
            }
            if (dateFun.compare(dateFun.get(lastDate), dateFun.get(payDate)) > 0) {
                parent.layer.alert(bt.lang("common.message.payDate"));//预计还款日期不能小于上一笔日期
                dateType.get("payDate_" + id).val("");
            }
            lastpayDateId = payDate_id;
        }
    })
}

/**
 * 导入还款计划(其他)
 */
function addImport(o, remainMoney, repayRate) {
    var html = '<tr class="bondIssueNum_' + o.bondIssueNum + '">';
    //债券发行编号
    html += '<td><input ' + isRepay + ' readonly name="cmsBondRepayPlanDtls[' + rowNum + '].bondIssueNum" id="bondIssueNum_' + rowNum + '" value="' + (o.bondIssueNum == null ? "" : o.bondIssueNum) + '" class="layui-input" type="text"/></td>';
    //还款日期
    html += '<td><input  name="cmsBondRepayPlanDtls[' + rowNum + '].repayDate" value="' + o.repayDate + '"  data-biz-name="repayDate_' + rowNum + '" id="repayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" /></td>';
    //计划还款日
    html += '<td><input  name="cmsBondRepayPlanDtls[' + rowNum + '].planRepayDate" value="' + o.planRepayDate + '"  data-biz-name="planRepayDate_' + rowNum + '" id="planRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" /></td>';
    //还款本金值
    html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" value="' + o.repayMoney + '" divName="repayMoney" data-callback="bt.calMoneyNow"  type="text" data-decimal="2" data-biz-name="repayMoney_' + rowNum + '" id="repayMoney_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayMoney"/></td>';
    //剩余本金值
    html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="remainMoney" value="' + remainMoney + '" data-biz-name="remainMoney_' + rowNum + '"  type="text" data-decimal="2" id="remainMoney_' + rowNum + '" data-min="0"  data-name="cmsBondRepayPlanDtls[' + rowNum + '].remainMoney" value="0"/></td>';
    //本次还款金额
    html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType"  divName="repayAmount_' + o.bondIssueNum + '" value="' + o.repayAmount + '" divName="repayAmount" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repayAmount_' + rowNum + '" id="repayAmount_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayAmount"/></td>';
    //本次偿还站发行总额比例%
    html += '<td><input class="layui-input bt-input-inline bt-biz-rateType" data-ratetype="percent" divName="repayRate_' + o.bondIssueNum + '" value="' + repayRate + '" data-callback="bt.calMoney" data-biz-name="repayRate_' + rowNum + '"  type="text" data-decimal="2" id="repayRate_' + rowNum + '" data-min="0"  data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayRate" value="0"/></td>';
    //是否已还
    html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="cmsBondRepayPlanDtls[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="' + o.isRepay + '"></div></td>';
    //备注
    html += '<td><input name="cmsBondRepayPlanDtls[' + rowNum + '].rmk"  id="rmk" class="layui-input" value="' + o.rmk + '" type="text"/></td>';
    //操作
    html += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#cmsBondRepayPlanTB tbody").append(html);
    addFun();
};

/**
 * 新增还款计划(其他)
 */
function add() {
    var html = '<tr class="bondIssueNum_' + bondIssueNum + '">';
    //债券发行编号
    html += '<td><input readonly name="cmsBondRepayPlanDtls[' + rowNum + '].bondIssueNum" id="bondIssueNum_' + rowNum + '" class="layui-input" type="text" value=' + bondIssueNum + '></td>';
    //还款日期
    html += '<td><input  name="cmsBondRepayPlanDtls[' + rowNum + '].repayDate" data-biz-name="repayDate_' + rowNum + '" id="repayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
    //计划还款日
    html += '<td><input  name="cmsBondRepayPlanDtls[' + rowNum + '].planRepayDate" data-biz-name="planRepayDate_' + rowNum + '" id="planRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
    //还款本金值
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repayMoney" data-callback="bt.calMoneyNow"  type="text" data-decimal="2" data-biz-name="repayMoney_' + rowNum + '" id="repayMoney_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayMoney"/></td>';
    //剩余本金值
    html += '<td><input  disabled class="layui-input bt-input-inline bt-biz-moneyType" divName="remainMoney" data-biz-name="remainMoney_' + rowNum + '" type="text" data-decimal="2" id="remainMoney_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].remainMoney"/></td>';
    //本次还款金额
    html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" divName="repayAmount_' + bondIssueNum + '" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repayAmount_' + rowNum + '" id="repayAmount_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayAmount"/></td>';
    //本次偿还站发行总额比例%
    html += '<td><input  disabled class="layui-input bt-input-inline bt-biz-rateType" divName="repayRate_' + bondIssueNum + '" data-biz-name="repayRate_' + rowNum + '" type="text" data-decimal="2" data-ratetype="percent" id="repayRate_' + rowNum + '" data-min="0" data-name="cmsBondRepayPlanDtls[' + rowNum + '].repayRate"/></td>';
    //是否已还
    html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="cmsBondRepayPlanDtls[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="0"></div></td>';
    //备注
    html += '<td><input name="cmsBondRepayPlanDtls[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
    //操作
    html += '<td><button data-type="del" type="button" onclick="delRow(this)" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#cmsBondRepayPlanTB tbody").append(html);
    addFun();
};

/**
 * 删除当前行
 */
function delRow(row) {
    var index = row.parentNode.parentNode.rowIndex;
    document.getElementById("cmsBondRepayPlanTB").deleteRow(index);
    MoneyCalculation();
}

/**
 * 根据预计还款日和预计还款本金，算出利息，本次还款本金，剩余本金（其他：循环每一行，之后调用calculation方法计算）
 */
function MoneyCalculation() {
    remainMoney = Number(remainMoneyFixed);
    var trs = $("#cmsBondRepayPlanTB").children("tbody").children("tr");
    var lastplanRepayDateId = "-1";//上一笔还款本金id
    // 遍历行数据
    trs.each(function (index, tr) {

        var planRepayDate_id = "";//预计还款日期
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
                var $input = $(td).find("input");
                if ($input.length) {
                    if (!isNull($input.attr("id")) && $input.attr("id").indexOf("planRepayDate") >= 0) {
                        planRepayDate_id = $input.attr("id");
                    }
//				if(!isNull($input.attr("data-id"))&&$input.attr("data-id").indexOf("repayAmount")>=0){
//					repayAmount_id=$input.attr("data-id");
//				}

                }
            }
        )
        var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
        if (isRepay != "0") {
            lastplanRepayDateId = planRepayDate_id;
        } else {
            calculation(lastplanRepayDateId, planRepayDate_id);
            lastplanRepayDateId = planRepayDate_id;
        }
    })
    sumMoney();
}

/**
 * 根据还款开始日期和还款日期、还款周期计算出日期数组
 */
function getDates() {
    var beginDate = dateFun.get($('#repayStartDate').val());//开始日期
    var endDate;
    if (isNull(zqDate)) {
        endDate = dateFun.get($('#repayDate').val());//还款日期
    } else {
        endDate = zqDate;
    }
    var timeSpan;
    switch (repayCycel) {
        case '0':
            timeSpan = 1;
            timeParms = 12;
            break;
        case '1':
            timeSpan = 3;
            timeParms = 4;
            break;
        case '2':
            timeSpan = 6;
            timeParms = 2;
            break;
        default:
            timeSpan = "1";
    }
    var dates = splitByMonth(beginDate, endDate, timeSpan, "0");
    return dates;
}

/**
 * 计算两个日期间相隔的（月，季，半年）
 * @param start 开始日期
 * @param end 结束日期
 * @param size 参数
 * @param type 1：还款计划修改
 * @returns
 */
function splitByMonth(start, end, size, type) {
    start = dateFun.get(start);
    end = dateFun.get(end);

    if (!start || !end || !size || size < 1) {
        return null;
    }
    ;

    var ary = [];
    ary.push(start);
    // if(type=="1"){
    // 	ary.push(start);
    // }
    var temp = start;
    var day = start.getDate();
    if (addMonth(temp, size, day).getTime() > end.getTime()) {
        ary.push(end);
    } else {
        while (addMonth(temp, size, day).getTime() < end.getTime()) {
            var newDate = addMonth(temp, size, day);
            ary.push(newDate);
            temp = newDate;
        }
        ;
        ary.push(end);
    }
    ;

    return ary;
}

function addMonth(date, size, oriDay) {
    date = dateFun.get(date);
    if (!date) {
        return null;
    }
    ;
    size = size - 0; //转数字
    var y = date.getFullYear();
    var m = date.getMonth();
    var nextY = y;
    var nextM = m;
    //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
    if ((m + size) > 11) {
        nextY = y + 1;
        nextM = parseInt(m + size) - 12;
    } else {
        nextM = date.getMonth() + size
    }
    var daysInNextMonth = daysInMonth(nextY, nextM);
    var day = oriDay;
    if (day > daysInNextMonth) {
        day = daysInNextMonth;
    }
    var newdate = new Date(nextY, nextM, day);
    return newdate;
}

function daysInMonth(year, month) {
    if (month == 1) {
        if (year % 4 == 0 && year % 100 != 0) {
            return 29;
        } else {
            return 28;
        }
    } else if (month == 3 || month == 5 || month == 7 || month == 10) {
        return 30;
    } else {
        return 31;
    }
};

/**
 * 获取两个日期之间的天数(天数)
 * @param strDateStart(Date)
 * @param strDateEnd(Date)
 * @returns
 */
function getDaysDate(strDateStart, strDateEnd) {
    var dateStr1 = dateFun.format(strDateStart, 'yyyy-MM-dd');
    var dateStr2 = dateFun.format(strDateEnd, 'yyyy-MM-dd');
    var date1 = bt.date.get(strDateStart);
    var date2 = bt.date.get(strDateEnd);
    var btt = date2 - date1;
    return btt / (1000 * 60 * 60 * 24);
}
