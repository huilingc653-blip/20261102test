var form, moneyType, selectType,dateType, dicts, rateType, dateFun, rowNum = 0,unRepayMoney,updatefirst=false;
bt.use(["jquery", "form", "btUpload", "dateType", "dicts", "moneyType","selectType",'btImport'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    selectType = bt.selectType.init();
    moneyFun = moneyType.getFun();
    var gid = $("#sendAttachmentId").val();
    var adjuestrateAttachmentId = $("#adjuestrateAttachmentId").val();
    var up = bt.btUpload.init({
        elem: "#sendUploader",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        auto: false,
        gid: gid
    });

    var adjuestrateUpload = bt.btUpload.init({
        elem: "#AdjuestrateUploader",
        isExpand: true,
        auto: false,
        gid: adjuestrateAttachmentId
    });

    // 资金计划挂接
    // renderBudget()
    var url=basePath + "/cms/basedata/repaymentschedule/getRepayMentScheduleDtl";
    if($("#page").val()=="update"){
        updatefirst=true;//是否为修改页面第一次的数据加载
        url=basePath + "/cms/basedata/repaymentschedule/getApproveRepayMentScheduleDtl";
    }
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            planId: $("#planId").val(),
            repayType: $("#orig_repayType").val(),
        },
        async: false,
        dataType: 'json',
        success: function (result) {
            var repayStartDate;
            var endDate;
            if ($("#repayStartDate").length > 0) {
                repayStartDate = dateType.get("repayStartDate").val();
            } else {
                repayStartDate = $("#repayMinDate").val();
            }
            if (!isNull($("#deferredEndDate").val())){
                endDate = $("#deferredEndDate").val();
            }else {
                endDate = $("#endDate").val();
            }
            var html = "";
            unRepayMoney = $("#sendMoney").val();
            $.each(result, function (index, item) {
                var trsNum = $("#repayPlanTB tbody tr").length;
                html += createTr(index + trsNum, item, repayStartDate, endDate, unRepayMoney, item.hasdelbtn);
                rowNum = index;
            });
            $("#repayPlanTB tbody").html(html);
            // 重新初始化组件
            reInit();
            // 计算合计金额
            calSum();
        }
    });

    form.on("submit(submit)", function (data) {
        if (!verification()) {
            return false;
        }
        var action = "/inl/change/save";
        bt.loading.show();
        data.field.status = 11;
        data.field.attachmentId = adjuestrateUpload.getGroupId();
        jQuery.ajaxSettings.traditional = true;
        $.post(basePath + action, data.field, function (data) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshParentTable();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();

                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });
});

//导出
exportExcel = function () {
    var url = basePath + "/cms/basedata/repaymentschedule/exportBudget";
    var formData = {
        id: $("#id").val(),
        status:$("#page").val()
    }
    DownLoadFile({url: url, data: formData, method: 'post'});
}

//导入初始化
importExcel = function () {
    up = bt.btImport.init({
        code: "70000003",
        titleRows: 0,
        headRows: 1,
        params: {
            repayTypeId: selectType.get("repayType").val()
        },
        success: function (head, data, select) {
            var repayBeginDate;
            var endDate;
            if ($("#repayBeginDate").length > 0) {
                repayBeginDate = dateType.get("repayBeginDate").val();
            } else {
                repayBeginDate = $("#repayMinDate").val();
            }
            if (!isNull($("#deferredRepayDate").val())){
                endDate = $("#deferredRepayDate").val();
            }else {
                endDate = $("#repayDate").val();
            }
            var html = "";
            var trsNum = $("#repayPlanTB tbody tr").length;
            $.each(select, function (index, item) {
                html += createTr(index + trsNum, item, repayBeginDate, endDate, unRepayMoney, true);
                rowNum = index;
            });
            deleteUnRepay();//删除未还
            $("#repayPlanTB tbody").append(html);
            // 重新初始化组件
            reInit();
            // 计算合计金额
            calSum();
        }
    });
};

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();

    }
}

/**
 * 提交时验证
 */
function verification() {


    var repayTypeData = selectType.get("repayType").getData().obj;
    // 随借随还时不判断本金
    if (isNull(repayTypeData) || repayTypeData.capitalFrequencyType != "N") {
        //统计还款本金
        if (!moneyType.get("currentRepayMoneySum").val()) {
            parent.layer.alert(bt.lang("cms.message.repaymentMoney.zero"));//还款本金和还款利息不能同时为零
            return false;
        }
        if (Number(moneyType.get("repaymentMoneySum").val()) != Number($("#sendMoney").val())) {
            parent.layer.alert(bt.lang("common.message.repayment_Principal"));//还款金额与发放金额不等
            return false;
        }
    }
    // else {
    //     parent.layer.alert(bt.lang("cms.precative.repaytype.not.null"));//还款方式不能为空
    //     return false;
    // }
    return true;
}

/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}
/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}
/**
 * 非空判断
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}


/**
 * 本金利息计算
 */
function calRepayPlan() {
    var params = {};
    var repayStartDate;
    if ($("#repayStartDate").length > 0) {
        repayStartDate = dateType.get("repayStartDate").val();
        if (!repayStartDate) {
            layer.msg(bt.lang(" cms.RepaymentSchedule.noSelectBeginDate"), {icon: 5});
            return false;
        }
    }
    if (isNull(selectType.get("repayType").val())) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
        return false;
    }
    params.repayTypeId = selectType.get("repayType").val();
    params.beginDate = repayStartDate;
    params.sendDate = $("#sendDate").val();
    if (!isNull($("#deferredEndDate").val())){
        params.endDate = $("#deferredEndDate").val();
    }else {
        params.endDate = $("#endDate").val();
    }
    params.money = unRepayMoney;
    params.loanRate = $("#sendRate").val();
    params.rateBase = $("#rateBase").val();
    bt.loading.show();
    $.post(basePath + "/cms/provide/cmsprovideinterest/calInterest", params, function (data) {
        var html = "";
        if (!repayStartDate) {
            params.beginDate = $("#repayMinDate").val();
        }
        var trsNum = $("#repayPlanTB tbody tr").length;
        $.each(data, function (index, item) {
            html += createTr(index + trsNum, item, params.beginDate, params.endDate, params.money, false);
            rowNum = index;
        });
        deleteUnRepay();//删除未还
        $("#repayPlanTB tbody").append(html);
        // 重新初始化组件
        reInit();
        // 计算合计金额
        calSum();
    }, "JSON");
}

/**
 * 构建表格行
 * @param rowNum 行号
 * @param item 行数据对象
 * @returns {string} 返回行数据
 */
function createTr(rowNum, item, beginDate, endDate, money, operate) {
    var html = "";
    var payMoney = item.paymentMoneyIsEdit ? "" : 0;
    // var paymentMoneyIsEdit = item.paymentMoneyIsEdit ? "" : "disabled";
    var paymentMoneyIsEdit = "";
    var payIst = item.inerestIsEdit ? "" : 0;
    // var inerestIsEdit = item.inerestIsEdit ? "" : "disabled";
    var inerestIsEdit = "";
    item.isRepay = isNull(item.isRepay) ? 0 : item.isRepay;
    if (item.isRepay == "1") {//已还则不能操作
        paymentMoneyIsEdit = "disabled";
        inerestIsEdit = "disabled";
        operate = false;
        unRepayMoney = moneyFun.sub(unRepayMoney, item.repaymentMoney);
    }
    html += '<tr data-rownum="' + rowNum + '">';
    if (item.isRepay == "1") {
        //已还则不能操作，且无最小和最大日期限制
        html += '<td><div><input disabled value="' + (isNotEmpty(item.prediceRepayDate) ? item.prediceRepayDate : '') + '" name="repayMents[' + rowNum + '].prediceRepayDate" data-biz-name="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" /></div></td>';
    } else {
        html += '<td><div><input value="' + (isNotEmpty(item.prediceRepayDate) ? item.prediceRepayDate : '') + '" name="repayMents[' + rowNum + '].prediceRepayDate" data-biz-name="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType" data-min="' + beginDate + '" data-max="' + endDate + '" data-type="date" lay-verify="required" /></div></td>';
    }
    html += '<td><input ' + paymentMoneyIsEdit + ' value="' + (isNotEmpty(item.repaymentMoney) ? item.repaymentMoney : payMoney) + '" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" data-id="repaymentMoney_' + rowNum + '" data-biz-name="repaymentMoney_' + rowNum + '" data-min="0" ' + (item.paymentMoneyIsEdit ? 'lay-verify="required"' : '') + ' data-max="' + money + '" data-name="repayMents[' + rowNum + '].repaymentMoney" data-callback="calSum"/></td>';
    html += '<td><input ' + inerestIsEdit + ' value="' + (isNotEmpty(item.repaymentIst) ? item.repaymentIst : payIst) + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="repaymentIst_' + rowNum + '" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" data-min="0" ' + (inerestIsEdit ? 'lay-verify="required"' : '') + ' data-name="repayMents[' + rowNum + '].repaymentIst" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.currentRepayMoney) ? item.currentRepayMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="currentRepayMoney_' + rowNum + '" data-biz-name="currentRepayMoney_' + rowNum + '" type="text" data-decimal="2" data-min="0" data-name="repayMents[' + rowNum + '].currentRepayMoney" data-callback="calSum"/></td>';
    html += '<td><input disabled value="' + (isNotEmpty(item.surplusMoney) ? item.surplusMoney : '') + '" class="layui-input bt-input-inline  bt-biz-moneyType" data-id="surplusMoney_' + rowNum + '" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" readonly="true" data-min="0" data-name="repayMents[' + rowNum + '].surplusMoney"/></td>';
    html += '<td name="isRepay">' + bt.dict('yn_state', item.isRepay) + '<input readonly="true" name="repayMents[' + rowNum + '].isRepay" id="isRepay_' + rowNum + '" value="' + item.isRepay + '"  type="hidden"/></td>';
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
 * 重新初始化组件
 */
function reInit() {
    dateType.render();
    moneyType.render();
    bt.loading.hide();
}

/**
 * 删除未还行
 * @param rowNum
 */
function deleteUnRepay() {
    var trs = $("#repayPlanTB tbody tr");
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-rownum");
        if ($("#isRepay_" + rowNum).val() != "1") {
            $("tr[data-rownum='" + rowNum + "']").remove();
        }
    })
    // 重新计算合计
    calSum()
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

/**
 * 合计金额计算
 */
function calSum() {
    var trs = $("#repayPlanTB tbody tr");
    var sendMoney = $("#sendMoney").val();
    if (trs.length == 0) {
        moneyType.get("repaymentMoneySum").val("");
        moneyType.get("repaymentIstSum").val("");
        moneyType.get("currentRepayMoneySum").val("");
        return;
    }
    var sumRepaymentMoney = 0;
    var sumIst = 0;
    var sumCurrentRepayMoney = 0;
    var x;
    var y;
    var z;
    var rowNum;
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-rownum");
        x = moneyType.get("repaymentMoney_" + rowNum).val();
        y = moneyType.get("repaymentIst_" + rowNum).val();
        z = Number(x) + Number(y);
        // var currentRepayMoney = moneyType.get("currentRepayMoney_" + rowNum).val();
        // if (currentRepayMoney) {
        //     z = currentRepayMoney;
        // }else {
        //     moneyType.get("currentRepayMoney_" + rowNum).val(z);
        // }
        if (x) {
            sumRepaymentMoney += Number(x);
        }
        if (y) {
            sumIst += Number(y);
        }
        if (z) {
            sumCurrentRepayMoney += Number(z);
        }
        moneyType.get("currentRepayMoney_" + rowNum).val(z);
        moneyType.get("surplusMoney_" + rowNum).val(moneyFun.sub(sendMoney, sumRepaymentMoney));
        moneyType.get("repaymentMoneySum").val(moneyFun.format(sumRepaymentMoney, 2));
        moneyType.get("repaymentIstSum").val(moneyFun.format(sumIst, 2));
        moneyType.get("currentRepayMoneySum").val(moneyFun.format(sumCurrentRepayMoney, 2));
    });
}

/**
 * 新增行
 * @param capitalEdit
 * @param interestEdit
 */
function addRow(capitalEdit, interestEdit) {
    var trs = $("#repayPlanTB tbody tr");
    rowNum = trs.length > 0 ? Number($(trs[trs.length - 1]).attr("data-rowNum")) + 1 : 0;
    var item = {};
    item.prediceRepayDateIsEdit = true;
    item.paymentMoneyIsEdit = capitalEdit;
    item.inerestIsEdit = interestEdit;
    var beginDate = "";
    var endDate = "";
    if ($("#repayStartDate").length > 0) {
        beginDate = $("#repayStartDate").val();
    } else {
        beginDate = $("#repayMinDate").val();
    }
    if ($("#deferredEndDate").length > 0){
        endDate = $("#deferredEndDate").val();
    }else {
        endDate = $("#endDate").val();
    }

    var money = unRepayMoney;
    $("#repayPlanTB tbody").append(createTr(rowNum, item, beginDate, endDate, money, true));
    // 重新初始化组件
    reInit();
}

/**
 * 还款方式选择回调
 * @param el
 * @param value
 * @param data
 */
function repayTypeCallback(data, el,value) {
    // 利息与本金无关
    if (data.capitalandinterest == "3") {
        // 本金时间周期/次数
        if (data.capitalfrequencytype == "D" || data.capitalfrequencytype == "C") {
            $("#calBtn").removeClass("layui-disabled");
            $("#calBtn").removeAttr("disabled", "disabled");

            if (data.interestfrequencytype == "P") {//利息有计划
                $("#addRowBtn").removeClass("layui-disabled");
                $("#addRowBtn").removeAttr("disabled", "disabled");
                $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" type=\"button\" id=\"addRowBtn\" onclick=\"addRow(false,true)\">" + bt.lang("common.button.add") + "</button>")

                $("#importBtn").removeAttr("disabled")
                $("#importBtn").removeClass("layui-disabled")
            } else {
                $("#addRowBtn").addClass("layui-disabled");
                $("#addRowBtn").attr("disabled", "disabled");
                $("#importBtn").attr("disabled", "disabled");
                $("#importBtn").addClass("layui-disabled");
            }
        } else {
            $("#calBtn").addClass("layui-disabled");
            $("#calBtn").attr("disabled", "disabled");
            dateType.get("repayStartDate").attr("data-disabled", true);
            // 不规律/随借随还
            if (data.capitalfrequencytype == "N" && data.interestfrequencytype == "N") {
                // 本金利息均为随借随还
                $("#addRowBtn").addClass("layui-disabled");
                $("#addRowBtn").attr("disabled", "disabled");
            } else {
                $("#addRowBtn").removeClass("layui-disabled");
                $("#addRowBtn").removeAttr("disabled", "disabled");
                if (data.capitalfrequencytype == "P" && data.interestfrequencytype != "P") {
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,false)\">" + bt.lang("common.button.add") + "</button>")
                } else if (data.capitalfrequencytype != "P" && data.interestfrequencytype == "P") {
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(false,true)\">" + bt.lang("common.button.add") + "</button>")
                } else {
                    $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,true)\">" + bt.lang("common.button.add") + "</button>")
                }
                $("#importBtn").removeAttr("disabled")
                $("#importBtn").removeClass("layui-disabled")
            }
        }
    } else if (data.capitalandinterest == "2") {
        // 等额本息
        $("#addRowBtn").addClass("layui-disabled");
        $("#addRowBtn").attr("disabled", "disabled");
        $("#calBtn").removeClass("layui-disabled");
        $("#calBtn").removeAttr("disabled", "disabled");
        $("#addRowBtn").attr("disabled", "disabled");
    } else {
        // 按剩余本金
        $("#calBtn").removeClass("layui-disabled");
        $("#calBtn").removeAttr("disabled", "disabled");
        if (data.capitalfrequencytype == "P") {
            $("#addRowBtn").removeClass("layui-disabled");
            $("#addRowBtn").removeAttr("disabled", "disabled");

            $("#addRowDiv").html("<button class=\"layui-btn layui-btn-small\" id=\"addRowBtn\" type=\"button\" onclick=\"addRow(true,false)\">" + bt.lang("common.button.add") + "</button>");
            $("#importBtn").removeAttr("disabled");
            $("#importBtn").removeClass("layui-disabled");
        }
    }
    if(updatefirst){
        updatefirst=false;
    }else {
        deleteUnRepay();//去掉未还数据
    }

    // 计算合计数据
    calSum()
}


