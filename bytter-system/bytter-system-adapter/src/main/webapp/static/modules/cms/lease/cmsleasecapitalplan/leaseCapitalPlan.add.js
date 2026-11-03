var rowNum = 0;
var moneyType = "";
var dicts;
var dateFun;
var surplusMoney = "";//剩余本金(用于每次计算使用)
var surplusMoneyFixed = "";//固定的剩余金额（即除去已还金额，剩余的金额）
var beginDate = "";//起租日
var endDate = "";//到期日
var loanDate = "";//发放日期
var repayDate = "";//还款日期
var payMoney = "";//发放金额
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

bt.use(['form', 'element', 'moneyType', 'dateType', 'bttable', 'dicts', 'btImport', 'money'], function () {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();
    var dateDis = "disabled";
    if (repayType == "03" || repayType == "06") {
        dateDis = "";
    }
    initVariable();
    var orig_repayType = $("#orig_repayType").val();
    //生成列表
    bt.bttable.init({
        elem: "#leaseCapitalPlanVoTB",
        url: basePath + "/cms/lease/leasecapitalplan/listLeaseCapitalPlanDel?leasePlanId=" + $("#id").val(),
        buttons: [
            {
                text: '<i class="layui-icon">&#xe654;</i>' + bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                text: bt.lang('common.button.exports'),
                action: function (e, dt, node, config) {
                    exportExcel();
                }
            },
        ],
        columns: [
            {
                data: "payDate",
                render: function (data, row) {
                    rowNum = row;
                    isRepay = data.isRepay == "1" ? "disabled" : "";
                    if (isNull(data.payDate)) {
                        if (data.isRepay == "1") {
                            return '<input disabled data-biz-name="payDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text"  class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        } else {
                            return '<input ' + dateDis + ' data-biz-name="payDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text"  class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        }
                    } else {
                        if (data.isRepay == "1") {
                            return '<input disabled data-biz-name="payDate_' + rowNum + '"name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" value="' + data.payDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        } else {
                            if ("07" == repayType) {
                                return '<input divName="payDate" data-biz-name="payDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" value="' + data.payDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                            } else {
                                return '<input ' + dateDis + ' data-biz-name="payDate_' + rowNum + '"name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" value="' + data.payDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                            }
                        }
                    }
                }
            },
            {
                data: "payPlanDate",
                render: function (data, row) {
                    rowNum = row;
                    isRepay = data.isRepay == "1" ? "disabled" : "";
                    if (isNull(data.payPlanDate)) {
                        if (data.isRepay == "1") {
                            return '<input disabled data-biz-name="payPlanDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text"  class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        } else {
                            return '<input ' + dateDis + ' data-biz-name="payPlanDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text"  class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        }
                    } else {
                        if (data.isRepay == "1") {
                            return '<input disabled data-biz-name="payPlanDate_' + rowNum + '"name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" value="' + data.payPlanDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                        } else {
                            if ("07" == repayType) {
                                return '<input divName="payPlanDate" data-biz-name="payPlanDate_' + rowNum + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" value="' + data.payPlanDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                            } else {
                                return '<input ' + dateDis + ' data-biz-name="payPlanDate_' + rowNum + '"name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" value="' + data.payPlanDate + '" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" data-callback="clickEvent"/>';
                            }
                        }
                    }
                }
            },
            {
                data: "leaseMonry",
                render: function (data, row) {
                    if (data.isRepay != "1" && (orig_repayType == "03" || orig_repayType == "07")) {
                        return '<input divName="leaseMonry" class="layui-input bt-input-inline  bt-biz-moneyType" value="' + (data.leaseMonry == null ? "" : moneyFun.format(data.leaseMonry)) + '" data-callback="bt.calMoney"  type="text" data-decimal="2" id="leaseMonry_' + rowNum + '" data-min="0" data-max="' + payMoney + '" id="leaseMonry_' + rowNum + '" data-biz-name="leaseMonry_' + rowNum + '" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/>';
                    } else {
                        return '<input disabled divName="leaseMonry" class="layui-input bt-input-inline  bt-biz-moneyType" value="' + (data.leaseMonry == null ? "" : moneyFun.format(data.leaseMonry)) + '" data-callback="bt.calMoney"  type="text" data-decimal="2" id="leaseMonry_' + rowNum + '" data-min="0" data-max="' + payMoney + '" id="leaseMonry_' + rowNum + '" data-biz-name="leaseMonry_' + rowNum + '" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/>';
                    }
                }
            },
            {
                data: "leaseInterest",
                render: function (data, row) {
                    if (data.isRepay != "1" && orig_repayType == "03") {
                        return '<input divName="leaseInterest" data-callback="bt.leaseInterest" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" value="' + (data.leaseInterest == null ? "" : moneyFun.format(data.leaseInterest)) + '" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/>';
                    } else if (data.isRepay != "1" && orig_repayType == "07") {
                        return '<input divName="leaseInterest" data-callback="bt.leaseInterest" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" value="' + (data.leaseInterest == null ? "" : moneyFun.format(data.leaseInterest)) + '" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/>';
                    } else {
                        return '<input disabled divName="leaseInterest" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" value="' + (data.leaseInterest == null ? "" : moneyFun.format(data.leaseInterest)) + '" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/>';
                    }
                }
            },
            {
                data: "thisMoney",
                render: function (data, row) {
                    if (orig_repayType == "03" || orig_repayType == "07") {
                        return '<input readonly="readonly" divName="thisMoney" divName="thisMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisMoney_' + rowNum + '" type="text" value="' + (data.thisMoney == null ? "" : moneyFun.format(data.thisMoney)) + '" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/>';
                    } else {
                        return '<input disabled divName="thisMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisMoney_' + rowNum + '" type="text" value="' + (data.thisMoney == null ? "" : moneyFun.format(data.thisMoney)) + '" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/>';
                    }
                }
            },
            {
                data: "thisCost",
                render: function (data, row) {
                    if (orig_repayType == "03" || orig_repayType == "07") {
                        return '<input divName="thisCost" data-callback="bt.thisCost" class="layui-input bt-input-inline bt-biz-moneyType" data-biz-name="thisCost_' + rowNum + '"  type="text" value="' + (data.thisCost == null ? "" : moneyFun.format(data.thisCost)) + '" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost"/>';
                    } else {
                        return '<input disabled divName="thisCost" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisCost_' + rowNum + '"  type="text" value="' + (data.thisCost == null ? "" : moneyFun.format(data.thisCost)) + '" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost"/>';
                    }
                }
            },
            {
                data: "surplusMoney",
                render: function (data, row) {
                    if (orig_repayType == "03" || orig_repayType == "07") {
                        return '<input readonly="readonly"  value="' + (data.surplusMoney == null ? "" : moneyFun.format(data.surplusMoney)) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/>';
                    } else {
                        return '<input disabled  value="' + (data.surplusMoney == null ? "" : moneyFun.format(data.surplusMoney)) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/>';
                    }
                }
            },
            {
                data: "bailMoney",
                render: function (data, row) {
                    if (orig_repayType == "03" || orig_repayType == "07") {
                        return '<input divName="bailMoney" data-callback="bt.bailMoney" class="layui-input bt-input-inline bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '"  type="text" value="' + (data.bailMoney == null ? "" : moneyFun.format(data.bailMoney)) + '" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/>';
                    } else {
                        return '<input disabled divName="bailMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '"  type="text" value="' + (data.bailMoney == null ? "" : moneyFun.format(data.bailMoney)) + '" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/>';
                    }
                }
            },
            {
                data: "isRepay",
                render: function (data, row) {
                    return '<div class="bt-biz-dict" data-type="select" data-displayData="0,1"  data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-defaultvalue="' + data.isRepay + '"></div>';
                }
            },
            {
                data: "rmk",
                render: function (data, type, full, meta) {
                    return '<input ' + isRepay + ' name="leaseCapitalPlanDels[' + rowNum + '].rmk" id="rmk" value="' + (data.rmk == null ? "" : data.rmk) + '" class="layui-input" type="text"/>';
                }
            },
            {
                data: "itemOrder",
                render: function (data, row) {
                    if (data.isRepay == "0") {
                        if (orig_repayType == "03" || orig_repayType == "07") {
                            return '<button id="del" data-type="del" type="button" name="itemOrder" value="' + rowNum + '" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button>';
                        } else {
                            return '<button style="display:none" id="del" data-type="del" type="button" name="itemOrder" value="' + rowNum + '" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button>';
                        }
                    } else {
                        return "";
                    }
                }
            }
        ],
        delRowCallback: function (data, $tr) {
            $tr.remove();
            MoneyCalculation();
        },
        tableInitCallback: function (data) {
            rowNum = data.length;
            setBeginDate();
            addFun();
        }
    });

    /**
     * 保存
     */
    form.on('submit(save)', function (data) {
        bt.loading.show();
        if (verification() == false) {
            bt.loading.hide();
            return false;
        }
        var action = "/cms/lease/leasecapitalplan/save/add";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();
                //给父级选项卡页面中id为planId的input赋值;
                webObj.bt.jquery("#planId").val(result.obj);
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            }
        }, 'JSON');
        return false;
    });
    repayTypeChanges($("#orig_repayType").val());
});

//导出
exportExcel = function () {
    var url = basePath + "/cms/lease/leasecapitalplan/exportBudget?id=" + $("#id").val();
    window.location.href = url;
}

//导入初始化
importExcel = function () {
    if (repayType != "07") {
        return;
    }
    var surplusMoney = $("#payMoney").val();
    up = bt.btImport.init({
        code: "70000015",
        width: "100%",
        height: "100%",
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            for (let i = 0; i < select.length; i++) {
                surplusMoney = surplusMoney - select[i].leaseMonry
                addImport(select[i], surplusMoney);
            }
        }
    });
};

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
        var payMoney = surplusMoneyFixed;
        clearData();//清空未还数据(还款方式除了其他的时候)
        addOneOffPayment(payDate, payMoney);
        return;
    }
    //若没有选择还款开始日则提示选择日期
    if (isNull($('#repayBeginDate').val())) {
        parent.layer.alert(bt.lang("cms.leaseCapitalPlanchedule.noSelectBeginDate"));
        return;
    }
    loadData();
}

/**
 * 设置还款开始日期的范围
 */
function setBeginDate() {
    var getCurrentDate = dateType.get("repayBeginDate");
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
    surplusMoneyFixed = Number(payMoney);
    var trs = $("#leaseCapitalPlanVoTB").children("tbody").children("tr");
    // 遍历行数据
    trs.each(function (index, tr) {
        var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
        if (isRepay == "0") {
            $(tr).remove();
        } else {
            var leaseMonry_id = $(tr).find("td[name^='leaseMonry']").find("input").attr("data-id");
            var money = moneyType.get(leaseMonry_id).val();
            surplusMoneyFixed = (Number(surplusMoneyFixed) - Number(money)).toFixed(2);
            surplusMoney = (Number(surplusMoney) - Number(money)).toFixed(2);
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
        surplusMoneyFixed = Number(payMoney);
        var trs = $("#leaseCapitalPlanVoTB").children("tbody").children("tr");
        //遍历行数据
        trs.each(function (index, tr) {
            var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
            if (isRepay != "0") {
                var leaseMonry_id = $(tr).find("td[name^='leaseMonry']").find("input").attr("data-id");
                var money = moneyType.get(leaseMonry_id).val();
                surplusMoneyFixed = (Number(surplusMoneyFixed) - Number(money)).toFixed(2);
            } else {
                $(tr).find("td[name^='payDate']").find("input").removeAttr("disabled");
                $(tr).find("td[name^='leaseMonry']").find("input").removeAttr("disabled");
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
    if (isNull($('#repayBeginDate').val())) {
        return;
    }

    //分期等额本金
    if (repayType == "02" || repayType == "05") {
        var money = (surplusMoneyFixed / (payDate.length)).toFixed(2);//还款本金
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
    if (isNull($('#repayBeginDate').val())) {
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
        dateType.get("repayBeginDate").val("");
        $("#repayBeginDate").attr("disabled", "true");
        dicts.get('repayCycel').attr({
            'data-value': '',
            'data-disabledValue': '0,1,2,3'
        });
        setOther();//设置数据
        return;
    }
    //还本付息
    if (repayType == "01") {
        $("#repayBeginDate").attr("disabled", "true");
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
        var payMoney = surplusMoneyFixed;
        $("#disposable").attr("checked", true);
        addOneOffPayment(payDate, payMoney);
        return;
    }

    //等额本金和等额本息
    $("#repayBeginDate").removeAttr("disabled");
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
    beginDate = $("#beginDate").val();//起租日
    endDate = $("#endDate").val();//到期日

    loanDate = $("#loanDate").val();//发放日期
    if ("" != $("#deferredRepayDate").val()) {
        repayDate = $("#deferredRepayDate").val();//还款日期
    } else {

    }
    repayDate = $("#repayDate").val();//还款日期
    payMoney = $("#payMoney").val();//租赁金额
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
    surplusMoneyFixed = Number(payMoney);//剩余本金（初始化为发放金额）
    surplusMoney = Number(payMoney);//剩余本金（初始化为发放金额）
}

/**
 * 合计
 */
function sumMoney() {
    //计算租赁本金
    var leaseMonrySum = 0;
    $("input[divName^='leaseMonry']").each(function (i) {
        leaseMonrySum = bt.money.add(leaseMonrySum, $(this).val());
    });
    if (principal == 1) {
        moneyType.get("leaseMonrySum").val(payMoney);
    } else {
        moneyType.get("leaseMonrySum").val(leaseMonrySum);
    }

    //计算租赁利息
    var leaseInterestSum = 0;
    $("input[divName^='leaseInterest']").each(function (i) {
        leaseInterestSum = bt.money.add(leaseInterestSum, $(this).val());
    });
    moneyType.get("leaseInterestSum").val(leaseInterestSum);

    //计算本期租金
    var thisMoneySum = 0;
    $("input[divName^='thisMoney']").each(function (i) {
        thisMoneySum = bt.money.add(thisMoneySum, $(this).val());
    });
    moneyType.get("thisMoneySum").val(thisMoneySum);
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
        var startDate = dateType.get("repayBeginDate").val();
        if (isNull(startDate)) {
            parent.layer.alert(bt.lang("cms.RepaymentSchedule.noSelectBeginDate"), {icon: 2});//未选择还款开始日期
            return false;
        }
    } else if (repayType == "03" || repayType == "06" || repayType == "07") {
        // 还款方式未其他，则校验金额
        //统计还款本金
        var sumMoney = 0;
        // var flag = false;////本金利息同时为0标识
        $.each($("input[id*='leaseMonry_']"), function (i, item) {
            // if ($(item).val() <= 0) {
            //     var dataId = $(item).attr("id");
            //     if (-1 != dataId.indexOf("leaseMonry_")) {
            //         var moneyInd = dataId.substr(dataId.indexOf("_") + 1);
            //         if (moneyType.get("leaseInterest_" + moneyInd).val() <= 0) {
            //             flag = true;//本金利息同时为0
            //         }
            //     }
            // }

            sumMoney = bt.money.add(sumMoney, $(item).val());
        });
        // if (flag) {
        //     parent.layer.alert(bt.lang("cms.message.leaseMonry.zero"));//租赁本金和租赁利息不能同时为零
        //     return false;
        // }
        if (Number(sumMoney) > Number(payMoney)) {
            parent.layer.alert(bt.lang("cms.message.leaseMonry_Principal"), {icon: 2});//租赁本金大于租赁支付金额，不能保存
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
    if (repayType === "07") {
        calCustomMoney(el.parents("tr"));
    } else if (repayType === "03") {
        var rowNum = el.parents("tr").find("input[divName^='leaseMonry']").data("id").split("_")[1];
        divName = $(el).attr("divName") + rowNum;
        if (divName === 'leaseInterest' + rowNum && Number(moneyType.get("leaseMonry_" + rowNum).val()) < Number(moneyType.get("leaseInterest_" + rowNum).val())) {
            var minmoney = moneyType.get("leaseMonry_" + rowNum).val();
            moneyType.get("leaseInterest_" + rowNum).val(minmoney);
        }
        MoneyCalculation();
    } else {
        MoneyCalculation();
    }
}

/**
 * 自定义金额计算
 * @param trEle
 */
function calCustomMoney(trEle) {
    // 行下标
    var rowNum = trEle.find("input[divName^='leaseMonry']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var leaseMonry = moneyType.get("leaseMonry_" + rowNum).val();
        var leaseInterest = moneyType.get("leaseInterest_" + rowNum).val();
        if (!isNull(leaseMonry) && !isNull(leaseInterest)) {
            moneyType.get("thisMoney_" + rowNum).val(moneyFun.add(leaseMonry, leaseInterest));
            var allRepayMoney = 0;
            //遍历表格，重新计算剩余本金
            var trs = $("#leaseCapitalPlanVoTB").children("tbody").children("tr");
            // 遍历行数据
            var initpayMoney = $("#payMoney").val();
            trs.each(function (index, tr) {
                var rowIndex = $(this).find("input[divName^='leaseMonry']").data("id").split("_")[1];
                var leaseMonry = $(this).find("input[divName^='leaseMonry']").val();
                var surplusMoney_ = moneyFun.sub(initpayMoney, leaseMonry);
                initpayMoney = surplusMoney_;
                moneyType.get("surplusMoney_" + rowIndex).val(surplusMoney_);
            });
            sumMoney();
        }
    }
}

/**
 *日期检查，即下一个还款日期不能小于上一个还款日期
 */
function checkDateByCustomRepay() {
    var trs = $("#leaseCapitalPlanVoTB").children("tbody").children("tr");
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
function addImport(o, surplusMoney) {
    var thisMoney = bt.money.add(o.leaseMonry, o.leaseInterest)
    var html = '<tr>';
    /* // 03 其他-含息 06 其他-不含息 07 自定义
     if (repayType != "03" && repayType != "06" && repayType != "07") {
         return;
     }
     //如果remainDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为remainDate(用来设置最小日期)
     var showRemainDate;
     if (isNull(remainDate)) {
         showRemainDate = dateFun.format(dateFun.addDay(loanDate, 1));
     } else {
         showRemainDate = remainDate;
     }
     //如果zqDate为空，证明没有已还数据，起始日期的计算为loanDate,反之就为zqDate(用来设置最大日期)
     var showZqDate;
     if (isNull(zqDate)) {
         showZqDate = endDate;
     } else {
         showZqDate = zqDate;
     }*/
    html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].payDate" value="' + o.payDate + '"  data-biz-name="payDate_' + rowNum + '" id="payDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" /></td>';
    html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" value="' + o.payPlanDate + '"  data-biz-name="payPlanDate_' + rowNum + '" id="payPlanDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" /></td>';
    html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" value="' + o.leaseMonry + '" divName="leaseMonry" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="leaseMonry_' + rowNum + '" id="leaseMonry_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/></td>';
    if (repayType === "07") {
        html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="leaseInterest" value="' + o.leaseInterest + '" data-callback="bt.calMoney" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest" value="0"/></td>';
    } else {
        html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="leaseInterest" value="' + o.leaseInterest + '" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/></td>';
    }
    html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" value="' + thisMoney + '" divName="thisMoney" data-biz-name="thisMoney_' + rowNum + '" type="text" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/></td>';
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="thisCost" value="' + o.thisCost + '" data-callback="bt.calMoney" data-biz-name="thisCost_' + rowNum + '"  type="text" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost" value="0"/></td>';
    html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" value="' + surplusMoney + '" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/></td>';
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="bailMoney" value="' + o.bailMoney + '" data-callback="bt.calMoney" data-biz-name="bailMoney_' + rowNum + '"  type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney" value="0"/></td>';
    html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="' + o.isRepay + '"></div></td>';
    html += '<td><input name="leaseCapitalPlanDels[' + rowNum + '].rmk"  id="rmk" class="layui-input" value="' + o.rmk + '" type="text"/></td>';
    html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#leaseCapitalPlanVoTB tbody").append(html);
    addFun();
};

/**
 * 新增还款计划(其他)
 */
function add() {
    var html = '<tr>';
    // 03 其他-含息 06 其他-不含息 07 自定义
    /*if (repayType != "03" && repayType != "06" && repayType != "07") {
        return;
    }
    //如果remainDate为空，证明没有已还数据，起始日期的计算为beginDate,反之就为remainDate(用来设置最小日期)
    var showRemainDate;
    if (isNull(remainDate)) {
        showRemainDate = dateFun.format(dateFun.addDay(beginDate, 1));
    } else {
        showRemainDate = remainDate;
    }
    //如果zqDate为空，证明没有已还数据，起始日期的计算为beginDate,反之就为zqDate(用来设置最大日期)
    var showZqDate;
    if (isNull(zqDate)) {
        showZqDate = endDate;
    } else {
        showZqDate = zqDate;
    }*/
    html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].payDate" data-biz-name="payDate_' + rowNum + '" id="payDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
    html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" data-biz-name="payPlanDate_' + rowNum + '" id="payPlanDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" data-callback="clickEvent"/></td>';
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="leaseMonry" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="leaseMonry_' + rowNum + '" id="leaseMonry_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" value="0" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/></td>';
    if (repayType === "07" || repayType === "03") {
        html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="leaseInterest" data-callback="bt.calMoney" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest" value="0"/></td>';
    } else {
        html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="leaseInterest" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/></td>';
    }
    html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="thisMoney" data-biz-name="thisMoney_' + rowNum + '" type="text" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/></td>';
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="thisCost" data-callback="bt.calMoney" data-biz-name="thisCost_' + rowNum + '"  type="text" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost" value="0"/></td>';
    html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/></td>';
    html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="bailMoney" data-callback="bt.calMoney" data-biz-name="bailMoney_' + rowNum + '"  type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney" value="0"/></td>';
    html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="0"></div></td>';
    html += '<td><input name="leaseCapitalPlanDels[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
    html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#leaseCapitalPlanVoTB tbody").append(html);
    addFun();
};

/**
 * 新增还款计划(还本付息)
 * @param payDate 日期数组
 * @param payMoney 金钱数组
 */
function addOneOffPayment(payDate, payMoney) {
    var html = '';
    var obj;
    var headDate;//计算开始日期（判断remainDate，空位loanDate，反之取remainDate）
    if (isNull(remainDate)) {
        headDate = beginDate;
    } else {
        headDate = dateFun.format(dateFun.addDay(remainDate, -1));
    }
    obj = calculationOneOffPayment(payDate, payMoney, headDate);
    html += '<tr class="dataRow">';
    html += '<td><input readonly="true" value="' + payDate + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + payDate + '" data-type="date" lay-verify="required" /></td>';
    html += '<td><input readonly="true" value="' + payPlanDate + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + payPlanDate + '" data-type="date" lay-verify="required" /></td>';
    html += '<td><input readonly="true" divName="leaseMonry" value="' + leaseMonry + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="leaseMonry_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/></td>';
    html += '<td><input readonly="true" divName="leaseInterest" value="' + obj['leaseInterest'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/></td>';
    html += '<td><input readonly="true" divName="thisMoney" value="' + obj['thisMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisMoney_' + rowNum + '" type="text" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/></td>';
    html += '<td><input readonly="true" divName="thisCost" value="' + obj['thisCost'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisCost_' + rowNum + '" type="text" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost"/></td>';
    html += '<td><input readonly="true" divName="bailMoney" value="' + obj['bailMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '" type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/></td>';
    html += '<td><input readonly="true" value="' + obj['surplusMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/></td>';
    html += '<td><input readonly="true" divName="bailMoney" value="' + obj['bailMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '" type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/></td>';
    html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="0"></div></td>';
    html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
    html += '</tr>';
    rowNum++;
    $("#leaseCapitalPlanVoTB tbody").append(html);
    addFun();
};

/**
 * 新增还款计划（等额本金）
 * @param payDate 日期数组
 * @param payMoney 金钱数组
 */
function addEqualPrincipal(payDate, payMoney, payInterest) {

    var html = '';
    var obj;
    surplusMoney = surplusMoneyFixed;
    var lastDate = loanDate;//上一笔日期,默认赋值为贷款发放开始日期
    if (!isNull(remainDate)) {
        lastDate = dateFun.format(dateFun.addDay(remainDate, -1));
    }
    //console.log("lastDate------"+lastDate);
    //console.log("remainDate-------"+remainDate);
    $.each(payDate, function (i, val) {
        if (i == (payDate.length - 1)) {//最后一次还款
            obj = calculationEqualPrincipal(lastDate, payDate[i], payMoney[i], true, payInterest);
        } else {
            obj = calculationEqualPrincipal(lastDate, payDate[i], payMoney[i], false, payInterest);
        }
        html += '<tr class="dataRow">';
        html += '<td><input disabled value="' + val + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" /></td>';
        html += '<td><input disabled value="' + val + '" name="leaseCapitalPlanDels[' + rowNum + '].payPlanDate" id="payPlanDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + payPlanDate + '" data-type="date" lay-verify="required" /></td>';
        html += '<td><input  disabled divName="leaseMonry" value="' + obj['leaseMonry'] + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="leaseMonry_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/></td>';
        html += '<td><input disabled divName="leaseInterest" value="' + obj['leaseInterest'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/></td>';
        html += '<td><input disabled divName="thisMoney" value="' + obj['bailMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisMoney_' + rowNum + '" type="text" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/></td>';
        html += '<td><input disabled divName="thisCost" value="' + obj['thisCost'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisCost_' + rowNum + '" type="text" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost"/></td>';
        html += '<td><input disabled value="' + obj['surplusMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/></td>';
        html += '<td><input disabled divName="bailMoney" value="' + obj['bailMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '" type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/></td>';
        html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="0"></div></td>';
        html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
        html += '</tr>';
        rowNum++;
        lastDate = payDate[i];
    });
    $("#leaseCapitalPlanVoTB tbody").append(html);
    addFun();
};


/**
 * 新增还款计划（等额本息）
 * @param payDate
 */
function addEqualPrincipalInterest(payDate) {
    var qs = 0;
    var f1 = 0;//辅助列1
    var f2 = 1;//辅助列2
    var sumf2 = 0;//辅助列2的合计
    var html = '';
    var obj;
    var thisMoney = 0;//每期的还款金额
    surplusMoney = surplusMoneyFixed;
    var lastDate;
    var lastDateFixed;
    //当remainDate为空时，取还款开始日期，反之，取上一笔已还数据日期
    if (!isNull(remainDate)) {
        lastDateFixed = dateFun.format(dateFun.addDay(remainDate, -1));
    } else {
        lastDateFixed = loanDate;//上一笔日期,默认赋值为开始日期
    }
    lastDate = lastDateFixed;
    $.each(payDate, function (i, val) {
        //console.log('------------------------------------');
        var dateStr1 = dateFun.format(payDate[i], 'yyyy-MM-dd');
        //console.log('payDate一:'+dateStr1);
        //console.log('repayDate一:'+repayDate);
        if (dateFun.compare(dateFun.get(payDate[i]), dateFun.get(repayDate)) <= 0) {//当前日期在还款日期之前，采用loanRate
            var days = getDaysDate(lastDate, payDate[i]);
            qs = Number(1 + days * loanRate / rateBase).toFixed(10);//对应1+期间利息
            //console.log('days一:'+days);
        } else {
            if (dateFun.compare(dateFun.get(lastDate), dateFun.get(repayDate)) >= 0) {//利率取zqRate
                var days = getDaysDate(lastDate, payDate[i]);
                qs = Number(1 + days * zqRate / rateBase).toFixed(10);//对应1+期间利息
                //console.log('days二:'+days);
            } else {//分段处理
                var daysone = getDaysDate(lastDate, repayDate);
                var daystwo = getDaysDate(repayDate, payDate[i]);
                qs = Number(1 + daysone * loanRate / rateBase + daystwo * zqRate / rateBase).toFixed(10);
                //console.log('daysone:'+daysone);
                //console.log('daystwo:'+daystwo);
            }

        }
        f1 = Number(1 / qs).toFixed(10);//辅助列2
        f2 = bt.moneyType.getFun().mul(Number(f1), Number(f2), 9);//辅助列4
        sumf2 = Number(Number(sumf2) + Number(f2)).toFixed(10);//辅助列4的合计
        lastDate = payDate[i];
    });
    thisMoney = (surplusMoneyFixed / sumf2).toFixed(2);
    //因为前面计算thisMoney使得lastDate改变，所以需要重新赋值
    lastDate = lastDateFixed;
    $.each(payDate, function (i, val) {
        if (i == (payDate.length - 1)) {
            obj = calculationEqualPrincipalAndInterest(lastDate, payDate[i], thisMoney, true);
        } else {
            obj = calculationEqualPrincipalAndInterest(lastDate, payDate[i], thisMoney, false);
        }
        html += '<tr class="dataRow">';
        html += '<td><input disabled value="' + val + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + repayDate + '" data-type="date" lay-verify="required" /></td>';
        html += '<td><input disabled value="' + val + '" name="leaseCapitalPlanDels[' + rowNum + '].payDate" id="payDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + loanDate + '" data-max="' + repayDate + '" data-type="date" lay-verify="required" /></td>';
        html += '<td><input  disabled divName="leaseMonry" value="' + obj['leaseMonry'] + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="leaseMonry_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + payMoney + '" data-name="leaseCapitalPlanDels[' + rowNum + '].leaseMonry"/></td>';
        html += '<td><input disabled divName="leaseInterest" value="' + obj['leaseInterest'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="leaseInterest_' + rowNum + '"  type="text" data-decimal="2" id="leaseInterest_' + rowNum + '" data-min="0"  data-name="leaseCapitalPlanDels[' + rowNum + '].leaseInterest"/></td>';
        html += '<td><input disabled divName="thisMoney" value="' + obj['thisMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisMoney_' + rowNum + '" type="text" data-decimal="2" id="thisMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisMoney"/></td>';
        html += '<td><input disabled divName="thisCost" value="' + obj['thisCost'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="thisCost_' + rowNum + '" type="text" data-decimal="2" id="thisCost_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].thisCost"/></td>';
        html += '<td><input disabled value="' + obj['surplusMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" id="surplusMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].surplusMoney"/></td>';
        html += '<td><input disabled divName="bailMoney" value="' + obj['bailMoney'] + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="bailMoney_' + rowNum + '" type="text" data-decimal="2" id="bailMoney_' + rowNum + '" data-min="0" data-name="leaseCapitalPlanDels[' + rowNum + '].bailMoney"/></td>';
        html += '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="leaseCapitalPlanDels[' + rowNum + '].isRepay" data-biz-name="isRepay_' + rowNum + '" data-defaultvalue="0"></div></td>';
        html += '<td><input  name="leaseCapitalPlanDels[' + rowNum + '].rmk" id="rmk" class="layui-input" type="text"/></td>';
        html += '</tr>';
        rowNum++;
        lastDate = payDate[i];
    });
    $("#leaseCapitalPlanVoTB tbody").append(html);
    addFun();
};

/**
 * 计算剩余本金，利息，本期还款金额(其他)
 * lastpayPlanDateId 上一次还款日
 * payPlanDate_id    本次还款日
 */
function calculation(lastpayPlanDateId, payPlanDate_id) {
    var id = payPlanDate_id.split("_")[1];
    //var leaseMonry=$("#leaseMonry_"+id).val();
    var leaseMonry = moneyType.get("leaseMonry_" + id).val();
    //获取现在利息值确定要不要主动计算
    var leaseInterestbefor = moneyType.get("leaseInterest_" + id).val();
    var payPlanDate = $("#payPlanDate_" + id).val();//预计还款日
    //上一笔日期
    var lastDate;
    if (lastpayPlanDateId == "-1") {
        lastDate = loanDate;
    } else {
        lastDate = $("#" + lastpayPlanDateId).val();
    }

    if (dateFun.compare(dateFun.get(lastDate), dateFun.get(payPlanDate)) > 0) {
        parent.layer.alert(bt.lang("common.message.payPlanDate"));//预计还款日期不能小于上一笔日期
        var getCurrentDate = dateType.get("payPlanDate_" + id).val("");
    }
    if (surplusMoney <= 0) {
        moneyType.get("leaseMonry_" + id).val(surplusMoney);
        moneyType.get("leaseInterest_" + id).val(0);
        moneyType.get("surplusMoney_" + id).val(0);
        moneyType.get("thisMoney_" + id).val(0);
        return;
    }

    //如果预计还款日或者还款本金有一项为空则跳过计算
    if (isNull(leaseMonry) || isNull(payPlanDate)) {
        return;
    }
    var surplusMoneyOld = Number(surplusMoney);
    var sign = false;//最后一次还款
    var payInterest = true;//还本时还息
    if ("06" == repayType) {
        payInterest = false;//还本时不付息
    }

    //剩余本金
    if (Number(surplusMoney) <= Number(leaseMonry)) {
        moneyType.get("leaseMonry_" + id).val(surplusMoney);
        leaseMonry = Number(surplusMoney);
        moneyType.get("surplusMoney_" + id).val(0);
        surplusMoney = 0;
        sign = true;//最后一次还款
    } else {
        surplusMoney = (surplusMoney - Number(leaseMonry)).toFixed(2);
        moneyType.get("surplusMoney_" + id).val(surplusMoney);
    }
    var leaseInterest = null;
    if (divName == "leaseMonry" + id) {
        leaseInterest = commonCalPayIst(lastDate, payPlanDate, repayDate, leaseMonry, sign, payInterest);
    } else {
        leaseInterest = leaseInterestbefor;
    }
    moneyType.get("leaseInterest_" + id).val(leaseInterest);
    //计算本期还款金额
    var thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
    moneyType.get("thisMoney_" + id).val(thisMoney);
}

/**
 * 计算剩余本金，利息，本期还款金额（还本付息）
 */
function calculationOneOffPayment(payPlanDate, leaseMonry, headDate) {

    if (isNull(zqDate)) {//非展期
        //计算利息
        var days = getDaysDate(headDate, payPlanDate);
        var leaseInterest = (surplusMoneyFixed * days * loanRate / Number(rateBase)).toFixed(2);
        //计算本期还款金额
        var thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
        //剩余本金
        surplusMoney = (Number(surplusMoneyFixed) - Number(leaseMonry)).toFixed(2);

        var obj = {leaseInterest: leaseInterest, thisMoney: thisMoney, surplusMoney: surplusMoney};
        return obj;
    } else {//展期
        var leaseInterest;
        //分段计算利息
        if (dateFun.compare(dateFun.get(headDate), dateFun.get(repayDate)) >= 0) {
            var days = getDaysDate(headDate, zqDate);
            leaseInterest = (surplusMoneyFixed * days * zqRate / Number(rateBase)).toFixed(2);
        } else {
            var daysone = getDaysDate(headDate, repayDate);
            var daysTwo = getDaysDate(repayDate, zqDate);
            leaseInterest = bt.moneyType.getFun().add((surplusMoneyFixed * (loanRate / Number(rateBase)) * daysone),
                (surplusMoneyFixed * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
        }
        //计算本期还款金额
        var thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
        //剩余本金
        surplusMoney = (Number(surplusMoneyFixed) - Number(leaseMonry)).toFixed(2);
        var obj = {leaseInterest: leaseInterest, thisMoney: thisMoney, surplusMoney: surplusMoney};
        return obj;
    }
}

/**
 * 计算剩余本金，利息，本期还款金额(等额本金)
 * repayDate: 到期日期
 * lastDate: 上次还款日
 * payPlanDate: 本次还款日
 * sign: 最后一次还款
 * payInterest: 是否还息
 */
function calculationEqualPrincipal(lastDate, payPlanDate, leaseMonry, sign, payInterest) {
    var leaseInterest;//利息
    var thisMoney;//本金

    if (dateFun.compare(dateFun.get(payPlanDate), dateFun.get(repayDate)) <= 0) {
        if (sign) {//最后一次还款
            if (payInterest) {//还本时归还利息
                var days = getDaysDate(lastDate, payPlanDate);
                leaseInterest = (surplusMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                var days = getDaysDate(loanDate, payPlanDate);//本次日期-发放日期
                leaseInterest = (payMoney * loanRate / rateBase * days).toFixed(2);//发放金额计算
            }
        } else {
            if (payInterest) {//还本时归还利息
                var days = getDaysDate(lastDate, payPlanDate);
                leaseInterest = (surplusMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息
                leaseInterest = (0).toFixed(2);//发放金额计算
            }
        }
    } else {
        if (dateFun.compare(dateFun.get(lastDate), dateFun.get(repayDate)) >= 0) {
            if (sign) {//最后一次还款
                if (payInterest) {//还本时归还利息
                    var days = getDaysDate(lastDate, payPlanDate);
                    leaseInterest = (surplusMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, payPlanDate);
                    leaseInterest = bt.moneyType.getFun().add((payMoney * (loanRate / Number(rateBase)) * daysone),
                        (payMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                }
            } else {
                if (payInterest) {//还本时归还利息
                    var days = getDaysDate(lastDate, payPlanDate);
                    leaseInterest = (surplusMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    leaseInterest = (0).toFixed(2);//发放金额计算
                }
            }
        } else {
            if (sign) {//最后一次还款
                if (payInterest) {//还本时归还利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, payPlanDate);
                    leaseInterest = bt.moneyType.getFun().add((surplusMoney * (loanRate / Number(rateBase)) * daysone),
                        (surplusMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, payPlanDate);
                    leaseInterest = bt.moneyType.getFun().add((payMoney * (loanRate / Number(rateBase)) * daysone),
                        (payMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                }
            } else {
                if (payInterest) {//还本时归还利息
                    var daysone = getDaysDate(lastDate, repayDate);
                    var daysTwo = getDaysDate(repayDate, payPlanDate);
                    leaseInterest = bt.moneyType.getFun().add((surplusMoney * (loanRate / Number(rateBase)) * daysone),
                        (surplusMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    leaseInterest = (0).toFixed(2);//发放金额计算
                }
            }
        }
    }
    if (sign) {
        leaseMonry = Number(surplusMoney);
        //计算本期还款金额
        thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
        surplusMoney = 0;
    } else {
        //计算本期还款金额
        thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
        //剩余本金
        surplusMoney = (Number(surplusMoney) - Number(leaseMonry)).toFixed(2);
    }
    var obj = {
        leaseInterest: leaseInterest,
        thisMoney: thisMoney,
        surplusMoney: surplusMoney,
        leaseMonry: leaseMonry
    };
    return obj;
}

/**
 * 公共利息测算方法: 如果还本时不还息，则中间的还款都不计算利息，只有最后一次还款才计算所有的利息
 * @param lastReapyDate     上一次还款日
 * @param currentReapyDate  本次还款日
 * @param endReapyDate      到期日
 * @param curRepayMoney     当前还款金额
 * @param sign              是否最后一次还款
 * @param payInterest       还款时是否还息
 * @returns {number}        返回还息金额
 */
function commonCalPayIst(lastReapyDate, currentReapyDate, endReapyDate, curRepayMoney, sign, payInterest) {
    var repayInterest = 0.00;
    if (dateFun.compare(dateFun.get(currentReapyDate), dateFun.get(endReapyDate)) <= 0) {
        if (sign) {//最后一次还款
            if (payInterest) {//还本时归还利息
                var days = getDaysDate(lastReapyDate, currentReapyDate);
                repayInterest = (curRepayMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                var days = getDaysDate(loanDate, currentReapyDate);//本次日期-发放日期
                repayInterest = (payMoney * loanRate / rateBase * days).toFixed(2);//发放金额计算
            }
        } else {
            if (payInterest) {//还本时归还利息
                var days = getDaysDate(lastReapyDate, currentReapyDate);
                repayInterest = (curRepayMoney * loanRate / rateBase * days).toFixed(2);
            } else {//还本不归还利息
                repayInterest = (0).toFixed(2);//发放金额计算
            }
        }
    } else {
        if (dateFun.compare(dateFun.get(lastReapyDate), dateFun.get(endReapyDate)) >= 0) {
            if (sign) {//最后一次还款
                if (payInterest) {//还本时归还利息
                    var days = getDaysDate(lastReapyDate, currentReapyDate);
                    repayInterest = (curRepayMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((payMoney * (loanRate / Number(rateBase)) * daysone),
                        (payMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                }
            } else {
                if (payInterest) {//还本时归还利息
                    var days = getDaysDate(lastReapyDate, currentReapyDate);
                    repayInterest = (curRepayMoney * zqRate / rateBase * days).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repayInterest = (0).toFixed(2);//发放金额计算
                }
            }
        } else {
            if (sign) {//最后一次还款
                if (payInterest) {//还本时归还利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((curRepayMoney * (loanRate / Number(rateBase)) * daysone),
                        (curRepayMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                } else {//还本不归还利息,则用所有发放金额从发放日期计算利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((payMoney * (loanRate / Number(rateBase)) * daysone),
                        (payMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                }
            } else {
                if (payInterest) {//还本时归还利息
                    var daysone = getDaysDate(lastReapyDate, endReapyDate);
                    var daysTwo = getDaysDate(endReapyDate, currentReapyDate);
                    repayInterest = bt.moneyType.getFun().add((curRepayMoney * (loanRate / Number(rateBase)) * daysone),
                        (curRepayMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
                } else {//还本不归还利息,最后一次计算利息
                    repayInterest = (0).toFixed(2);//发放金额计算
                }
            }
        }
    }
    return repayInterest;
}

/**
 * 计算剩余本金，利息，本期还款本金(等额本息)
 */
function calculationEqualPrincipalAndInterest(lastDate, payPlanDate, thisMoney, sign) {
    var leaseInterest;
    var leaseMonry;
    if (dateFun.compare(dateFun.get(payPlanDate), dateFun.get(repayDate)) <= 0) {
        var days = getDaysDate(lastDate, payPlanDate);
        leaseInterest = (surplusMoney * loanRate / rateBase * days).toFixed(2);
    } else {
        if (dateFun.compare(dateFun.get(lastDate), dateFun.get(repayDate)) >= 0) {
            var days = getDaysDate(lastDate, payPlanDate);
            leaseInterest = (surplusMoney * zqRate / rateBase * days).toFixed(2);
        } else {
            var daysone = getDaysDate(lastDate, repayDate);
            var daysTwo = getDaysDate(repayDate, payPlanDate);
            leaseInterest = bt.moneyType.getFun().add((surplusMoney * (loanRate / Number(rateBase)) * daysone),
                (surplusMoney * (zqRate / Number(rateBase)) * daysTwo)).toFixed(2);
        }
    }
    if (sign) {
        leaseMonry = Number(surplusMoney);
        //计算本期还款金额
        thisMoney = (Number(leaseMonry) + Number(leaseInterest)).toFixed(2);
        surplusMoney = 0;
    } else {
        //计算本金
        leaseMonry = (Number(thisMoney) - Number(leaseInterest)).toFixed(2);
        //剩余本金
        surplusMoney = (Number(surplusMoney) - Number(leaseMonry)).toFixed(2);
    }
    var obj = {
        leaseInterest: leaseInterest,
        thisMoney: thisMoney,
        surplusMoney: surplusMoney,
        leaseMonry: leaseMonry
    };
    return obj;
}

/**
 * 根据预计还款日和预计还款本金，算出利息，本次还款本金，剩余本金（其他：循环每一行，之后调用calculation方法计算）
 */
function MoneyCalculation() {
    surplusMoney = Number(surplusMoneyFixed);
    var trs = $("#leaseCapitalPlanVoTB").children("tbody").children("tr");
    var lastpayPlanDateId = "-1";//上一笔还款本金id
    // 遍历行数据
    trs.each(function (index, tr) {

        var payPlanDate_id = "";//预计还款日期
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
                var $input = $(td).find("input");
                if ($input.length) {
                    if (!isNull($input.attr("id")) && $input.attr("id").indexOf("payPlanDate") >= 0) {
                        payPlanDate_id = $input.attr("id");
                    }
//				if(!isNull($input.attr("data-id"))&&$input.attr("data-id").indexOf("leaseMonry")>=0){
//					leaseMonry_id=$input.attr("data-id");
//				}

                }
            }
        )
        var isRepay = $(tr).find("td[name^='isRepay']").find("input").val();
        if (isRepay != "0") {
            lastpayPlanDateId = payPlanDate_id;
        } else {
            calculation(lastpayPlanDateId, payPlanDate_id);
            lastpayPlanDateId = payPlanDate_id;
        }
    })
    sumMoney();
}

/**
 * 根据还款开始日期和还款日期、还款周期计算出日期数组
 */
function getDates() {
    var beginDate = dateFun.get($('#repayBeginDate').val());//开始日期
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

// 重置
function capitalPlanReset() {
    window.location.reload();
}
