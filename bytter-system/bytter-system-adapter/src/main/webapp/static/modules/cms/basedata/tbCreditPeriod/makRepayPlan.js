var dateType, moneyType, rowNum = 0, moneyFun,dateFun;
bt.use(["form", "element", "dateType", "moneyType", "btImport"], function () {
    $ = bt.jquery;
    form = bt.form()
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    moneyFun = moneyType.getFun();

    var planId = $("#planId").val();
    if (planId) {
        $.ajax({
            type : 'POST',
            url : basePath + "/cms/basedata/repaymentschedule/getRepayMentScheduleDtl",
            data : {
                planId: planId,
                repayType: $("#repayType").val(),
            },
            async : false,
            dataType : 'json',
            success : function(result) {
                var repayBeginDate;
                if ($("#repayBeginDate").length > 0) {
                    repayBeginDate = dateType.get("repayBeginDate").val();
                } else {
                    repayBeginDate =  dateFun.addDay($("#loanDate").val(),1);
                }
                var html = "";
                $.each(result, function (index, item) {
                    html += createTr(index, item, repayBeginDate, $("#repayDate").val(), $("#loanMoney").val(), item.hasdelbtn);
                    rowNum = index;
                });
                $("#repayPlanTB tbody").html(html);
                // 重新初始化组件
                reInit();
                // 计算合计金额
                calSum();
            }
        });
    }

    form.on('submit(save)', function (data) {
        bt.loading.show();
        if (verification() == false) {
            bt.loading.hide();
            return false;
        }
        var action = "/cms/basedata/repaymentschedule/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();
                //给父级选项卡页面中id为test的input赋值;
                webObj.bt.jquery("#planId").val(result.obj);
                webObj.bt.jquery("#planStatus").val("0");
                bt.navtab.closeCurrent();
            }
        }, 'JSON');
        return false;
    });
});

//导入初始化
importExcel = function () {
    up = bt.btImport.init({
        code: "70000003",
        width: "100%",
        height: "100%",
        params: {
            repayTypeId: $("#repayType").val()},
        titleRows: 0,
        headRows: 1,
        success: function (head, data, select) {
            var repayBeginDate;
            if ($("#repayBeginDate").length > 0) {
                repayBeginDate = dateType.get("repayBeginDate").val();
            } else {
                repayBeginDate =  dateFun.addDay($("#loanDate").val(),1);
            }
            var html = "";
            $.each(select, function (index, item) {
                html += createTr(index, item, repayBeginDate, $("#repayDate").val(), $("#loanMoney").val(), true);
                rowNum = index;
            });
            $("#repayPlanTB tbody").html(html);
            // 重新初始化组件
            reInit();
            // 计算合计金额
            calSum();
        }
    });
};

/**
 * 提交时验证
 */
function verification() {
    //统计还款本金
    if (!moneyType.get("currentRepayMoneySum").val()) {
        parent.layer.alert(bt.lang("cms.message.repaymentMoney.zero"));//还款本金和还款利息不能同时为零
        return false;
    }
    if($("#capitalFrequencyType").val()!="N") {
        if (Number(moneyType.get("repaymentMoneySum").val()) != Number($("#loanMoney").val())) {
            parent.layer.alert(bt.lang("common.message.repayment_Principal"));//还款金额与发放金额不等
            return false;
        }
    }
    return true;
}

/**
 * 本金利息计算
 */
function calRepayPlan() {
    var params = {};
    var repayBeginDate;
    if ($("#repayBeginDate").length > 0) {
        repayBeginDate = dateType.get("repayBeginDate").val();
        if (!repayBeginDate) {
            layer.msg(bt.lang(" cms.RepaymentSchedule.noSelectBeginDate"),{icon: 5});
            return false;
        }
    }
    params.repayTypeId = $("#repayType").val();
    params.beginDate = repayBeginDate;
    params.sendDate = $("#loanDate").val();
    params.endDate = $("#repayDate").val();
    params.money = $("#loanMoney").val();
    params.loanRate = $("#loanRate").val();
    params.rateBase = $("#rateBase").val();
    bt.loading.show();
    $.post(basePath + "/cms/provide/cmsprovideinterest/calInterest", params, function (data) {
        var html = "";
        if(!repayBeginDate){
            params.beginDate=  dateFun.addDay(params.sendDate,1);
        }
        $.each(data, function (index, item) {
            html += createTr(index, item, params.beginDate, params.endDate, params.money, false);
            rowNum = index;
        });
        $("#repayPlanTB tbody").html(html);
        // 重新初始化组件
        reInit();
        // 计算合计金额
        calSum();
    }, "JSON");
}

/**
 * 重新初始化组件
 */
function reInit() {
    dateType.render();
    moneyType.render();
    bt.loading.hide();
}

/**
 * 新增行
 * @param capitalEdit
 * @param interestEdit
 */
function addRow(capitalEdit,interestEdit) {
    var trs = $("#repayPlanTB tbody tr");
    rowNum = trs.length > 0 ? Number($(trs[trs.length - 1]).attr("data-rowNum")) + 1 : 0;
    var item = {};
    item.prediceRepayDateIsEdit = true;
    item.paymentMoneyIsEdit = capitalEdit;
    item.inerestIsEdit = interestEdit;
    var beginDate = "";
    if ($("#repayBeginDate").length > 0) {
        beginDate = $("#repayBeginDate").val();
    } else {
        beginDate = dateFun.addDay($("#loanDate").val(),1);
    }
    var endDate = $("#repayDate").val();
    var money = $("#loanMoney").val();
    $("#repayPlanTB tbody").append(createTr(rowNum, item, beginDate, endDate, money, true));
    // 重新初始化组件
    reInit();
}

/**
 * 构建表格行
 * @param rowNum 行号
 * @param item 行数据对象
 * @returns {string} 返回行数据
 */
function createTr(rowNum, item, beginDate, endDate, money,operate) {
    var html = "";
    var prediceRepayDateIsEdit = item.prediceRepayDateIsEdit ? "" : "disabled";
    var payMoney=item.paymentMoneyIsEdit ? "" : 0;
    var paymentMoneyIsEdit = item.paymentMoneyIsEdit ? "" : "disabled";
    var payIst=item.inerestIsEdit ? "":  0;
    var inerestIsEdit = item.inerestIsEdit ? "" : "disabled";
    html += '<tr data-rownum="' + rowNum + '">';
    html += '<td><div><input ' + prediceRepayDateIsEdit + ' value="' + (isNotEmpty(item.prediceRepayDate) ? item.prediceRepayDate : '') + '" name="repayMents[' + rowNum + '].prediceRepayDate" data-biz-name="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" /></div></td>';
    html += '<td><input ' + paymentMoneyIsEdit + ' value="' + (isNotEmpty(item.repaymentMoney) ? item.repaymentMoney : payMoney) + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" data-id="repaymentMoney_' + rowNum + '" data-biz-name="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + money + '" data-name="repayMents[' + rowNum + '].repaymentMoney" data-callback="calSum"/></td>';
    html += '<td><input ' + inerestIsEdit + ' value="' + (isNotEmpty(item.repaymentIst) ? item.repaymentIst : payIst) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="repaymentIst_' + rowNum + '" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" data-min="0" lay-verify="required"  data-name="repayMents[' + rowNum + '].repaymentIst" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.currentRepayMoney) ? item.currentRepayMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="currentRepayMoney_' + rowNum + '" data-biz-name="currentRepayMoney_' + rowNum + '" type="text" data-decimal="2" data-min="0" data-name="repayMents[' + rowNum + '].currentRepayMoney" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.surplusMoney) ? item.surplusMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="surplusMoney_' + rowNum + '" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" data-min="0" data-name="repayMents[' + rowNum + '].surplusMoney"/></td>';
    html += '<td name="isRepay">' + bt.dict('yn_state', "0") + '<input readonly="true" name="repayMents[' + rowNum + '].isRepay" value="0"  type="hidden"/></td>';
    html += '<td><input name="repayMents[' + rowNum + '].remark" id="remark" class="layui-input" type="text"/></td>';
    if (operate) {
        html += '<td><button class="layui-btn layui-btn-small" type="button" onclick="delRow(' + rowNum + ')">删除</button></td>';
    } else {
        html += '<td></td>';
    }
    html += '</tr>';
    return html;
}

/**
 * 删除行
 * @param rowNum
 */
function delRow(rowNum) {
    $("tr[data-rownum='" + rowNum + "']").remove();
    // 重新计算合计
    calSum()
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 判断为空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value === "" || value === " " || value == undefined || value == null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判断非空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * 合计金额计算
 */
function calSum() {
    var trs = $("#repayPlanTB tbody tr");
    if (trs.length == 0) {
        moneyType.get("repaymentMoneySum").val("");
        moneyType.get("repaymentIstSum").val("");
        moneyType.get("currentRepayMoneySum").val("");
        return;
    }
    var sumRepaymentMoney = 0;
    var sumIst = 0;
    var sumCurrentRepayMoney = 0;
    var sendMoney = $("#loanMoney").val();//发放金额
    var rowNum;
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-rownum");
        var x = moneyType.get("repaymentMoney_" + rowNum).val();
        var y = moneyType.get("repaymentIst_" + rowNum).val();
        var z = Number(x) + Number(y);
        if (isNotEmpty(x)) {
            sumRepaymentMoney += Number(x);
        }
        if (isNotEmpty(y)) {
            sumIst += Number(y);
        }
        if (isNotEmpty(z)) {
            sumCurrentRepayMoney += Number(z);
        }
        moneyType.get("currentRepayMoney_" + rowNum).val(z);
        moneyType.get("surplusMoney_" + rowNum).val(moneyFun.sub(sendMoney, sumRepaymentMoney));
    });
    moneyType.get("repaymentMoneySum").val(moneyFun.format(sumRepaymentMoney,2));
    moneyType.get("repaymentIstSum").val(moneyFun.format(sumIst,2));
    moneyType.get("currentRepayMoneySum").val(moneyFun.format(sumCurrentRepayMoney,2));
}
