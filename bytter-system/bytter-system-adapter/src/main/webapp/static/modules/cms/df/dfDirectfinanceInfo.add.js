var form, dateType, dicts, selectType, rateType, moneyType, moneyfun,corp;
bt.use(['form', 'dateType', 'select', 'selectType', 'corp', 'dicts', 'rateType', 'moneyType', 'btUpload','selectTree'], function () {
    var $ = bt.jquery;
    form = bt.form();
    dicts = bt.dicts.init();//字典
    selectType = bt.selectType.init();//下拉选择
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();//日期时间
    moneyfun = bt.moneyType.getFun()
    var gid = $("#gid").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    // 资金计划挂接
    renderBudget();

    //单位回调函数
    bt.corpOtherSet = function (id) {
        if(id){
            //获取单位信息
            $.ajax({
                url: basePath + "/sys/corp/getSysCorp",
                method: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (data) {
                    form.render();
                }
            });
        }
        renderBudget();
    };

    form.on("submit(submit)", function (data) {
        var action = "/cms/dfDirectFinanceInfo/save";
        var isBudget = $("#isBudget").val();
        if ("1" == isBudget) {
            if (isNotEmpty($("#tagItemDetail").html())) {
                //判断直融业务是否挂资金计划
                var dfInfoItem = $("#dfInfoItem").val();
                if (dfInfoItem != null && dfInfoItem != "") {
                    var surplusMoney = moneyType.get("surplusMoney").val();
                    var processMoneys = $("input[name = 'processMoney']");
                    var totalMoney = 0;
                    var itemCode = [];
                    var itemName = [];
                    var processMoney = [];
                    $.each(processMoneys, function (index, item) {
                        totalMoney = moneyfun.add(totalMoney, moneyType.get("processMoney" + index).val());
                        itemCode.push($("input[name = 'itemCode']")[index].value);
                        itemName.push($("input[name = 'itemName']")[index].value);
                        processMoney.push(moneyType.get("processMoney" + index).val());
                    });
                    if (Number(surplusMoney) != Number(totalMoney)) {
                        layer.alert(bt.lang("df.isBudget.money.check"), {icon: 2});
                        return false;
                    } else {
                        data.field.itemCode = itemCode;
                        data.field.itemName = itemName;
                        data.field.processMoney = processMoney;
                    }
                }
            }
        }

        bt.loading.show();
        data.field.status = this.value;
        data.field.uploadId = up.getGroupId();
        data.field.corpName = corp.get('corpId').data()[0].corpName;
        var curData = selectType.get("curId").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
        data.field.curName = curData.curname;
        jQuery.ajaxSettings.traditional = true;
        $.post(basePath + action, data.field, function (data) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshParentTable();
                } else {
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });
});


var index = 1;

function addRows(type) {
    var deleteStr = bt.lang("common.button.delete");
    var html = "";
    if (type == 1) {
        html += "<tr>" +
            "<td><input type=\"text\" name=\"dfThirdPartyAgencyVOS[" + index + "].agencyType\" class=\"layui-input\" lay-verify=\"strlength\" data-maxlength=\"32\"></td>" +
            "<td><input type=\"text\" name=\"dfThirdPartyAgencyVOS[" + index + "].agencyName\" class=\"layui-input\" lay-verify=\"strlength\" data-maxlength=\"100\"></td>" +
            "<td class=\"inCenter\"><span class=\"addRow\" onclick=\"deleteRows(this)\">" + deleteStr + "</span></td>" +
            "</tr>";
        $("#thirdAgency").append(html);
        index++;
    } else if (type == 2) {
        html += "<tr rows = \"" + index + "\">" +
            "<td><div class=\"bt-biz-selectType\" data-biz-name=\"costTypeId" + index + "\" data-url=\"" + basePath + "/cms/dfCostType/getValidDfCostType\"" +
            "data-valuefield=\"id\" data-textfield=\"costName\" data-name=\"dfIntermediateCostVOS[" + index + "].costTypeId\"></div>" +
            "</td>" +
            "<td><input type=\"text\" name=\"dfIntermediateCostVOS[" + index + "].rate\" data-id=\"rate" + index + "\" data-callback=\"calAdvanceFees\" data-biz-name=\"rate" + index + "\" class=\"layui-input bt-biz-rateType\"></td>" +
            "<td><input type=\"text\" name=\"dfIntermediateCostVOS[" + index + "].advanceFees\" data-id=\"advanceFees" + index + "\" data-biz-name=\"advanceFees" + index + "\" readonly=\"readonly\" class=\"layui-input bt-biz-moneyType\"></td>" +
            "<td class=\"inCenter\"><span class=\"addRow\" onclick=\"deleteRows(this)\">" + deleteStr + "</span></td>" +
            "</tr>";
        $("#intermediateCost").append(html);
        selectType.render();
        rateType.render();
        moneyType.render();
        index++;
    } else if (type == 3) {
        var endDate = dateType.get("endDate").val();
        if (isEmpty(endDate)) {
            layer.alert(bt.lang("df.select.end.date"), {icon: 2});
            return false;
        }
        html += "<tr rows = \"" + index + "\">" +
            "<td><input type=\"text\" rows = \"" + index + "\" data-max=\"" + endDate + "\" data-biz-name=\"repayDate" + index + "\" name=\"dfDirectFinanceRepayTmpVOS[" + index + "].repayDate\" class=\"layui-input bt-biz-dateType\"></td>" +
            "<td><input type=\"text\" rows = \"" + index + "\" data-callback=\"calMoney\" data-id=\"repayMoney" + index + "\" data-biz-name=\"repayMoney" + index + "\" data-name=\"dfDirectFinanceRepayTmpVOS[" + index + "].repayMoney\" class=\"layui-input bt-biz-moneyType\"></td>" +
            "<td><input type=\"text\" rows = \"" + index + "\" data-callback=\"calMoney\" data-id=\"interest" + index + "\" data-biz-name=\"interest" + index + "\" data-name=\"dfDirectFinanceRepayTmpVOS[" + index + "].interest\" class=\"layui-input bt-biz-moneyType\"></td>" +
            "<td><input type=\"text\" rows = \"" + index + "\" data-callback=\"calMoney\" data-id=\"serviceCharge" + index + "\" data-biz-name=\"serviceCharge" + index + "\" data-name=\"dfDirectFinanceRepayTmpVOS[" + index + "].serviceCharge\" class=\"layui-input bt-biz-moneyType\"></td>" +
            "<td id=\"total" + index + "\"></td>" +
            "<td class=\"inCenter\"><span class=\"addRow\" onclick=\"deleteRows(this)\">" + deleteStr + "</span></td>" +
            "</tr>";
        $("#repayPlan").append(html);
        dateType.render();
        moneyType.render();
        index++;
    }
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 删除行
 * @param obj
 */
function deleteRows(obj) {
    var ptr = $(obj).parents("tr")[0];
    $(ptr).remove();
}

/**
 * 还本付息计划计算
 * @returns {boolean}
 */
function calMoney() {
    var surplusMoney = moneyType.get("surplusMoney").val();
    var trArray = $("#repayPlan").find("tr");
    var rowIndex = 0;
    if (isEmpty(surplusMoney)) {
        layer.alert(bt.lang("df.input.surplusMoney"), {icon: 2});
        $.each(trArray, function (index1, item) {
            rowIndex = $(item).attr("rows");
            if (isNotEmpty(rowIndex)) {
                moneyType.get("repayMoney" + rowIndex).val("");
                calTotalMoney(rowIndex)
            }
        })
        return false;
    }
    var repayTotalMoney = 0;
    var flag = true;
    // 循环获取累计还款金额
    $.each(trArray, function (index1, item) {
        rowIndex = $(item).attr("rows");
        if (isNotEmpty(rowIndex)) {
            repayTotalMoney = moneyfun.add(repayTotalMoney, moneyType.get("repayMoney" + rowIndex).val());
            // 判断累计金额与余额大小
            if (repayTotalMoney > surplusMoney) {
                if (flag) {
                    layer.alert(bt.lang("df.total.repay.notThen.balance"), {icon: 2});
                }
                flag = false;
                moneyType.get("repayMoney" + rowIndex).val("");
            }
            calTotalMoney(rowIndex)
        }
    })
}

/**
 * 合计金额计算
 * @param currRowindex
 */
function calTotalMoney(currRowindex) {
    // 设置合计值
    var currRepayMoney = moneyType.get("repayMoney" + currRowindex).val()
    var currinterest = moneyType.get("interest" + currRowindex).val()
    var currserviceCharge = moneyType.get("serviceCharge" + currRowindex).val()
    var rowTotal = moneyfun.add(moneyfun.add(currRepayMoney, currinterest), currserviceCharge);
    $("#total" + currRowindex).html(moneyfun.format(rowTotal));
}

/**
 * 日期校验
 * @param data
 * @param jq
 * @returns {boolean}
 */
function validRepay(data, jq) {
    var endDate = dateType.get("endDate").val();
    if (isEmpty(endDate)) {
        layer.alert(bt.lang("df.select.end.date"), {icon: 2});
        dateType.get("repayDate0").val("");
        return false;
    }

    if (bt.date.compare(endDate, data) < 0) {
        layer.alert(bt.lang("df.repay.date.notThen.endDate"), {icon: 2});
        dateType.get("repayDate0").val("");
        return false;
    }
}


function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * 预计费用计算
 * @param obj
 */
function calAdvanceFees() {
    var surplusMoney = moneyType.get("surplusMoney").val();
    var trArray = $("#intermediateCost").find("tr");
    var rowIndex = 0;
    if (isEmpty(surplusMoney)) {
        layer.alert(bt.lang("df.input.surplusMoney"), {icon: 2});
        $.each(trArray, function (index, item) {
            rowIndex = $(item).attr("rows");
            if (isNotEmpty(rowIndex)) {
                rateType.get("rate" + rowIndex).val("");
                moneyType.get("advanceFees" + rowIndex).val();
            }
        })
        return false;
    }
    $.each(trArray, function (index1, item) {
        rowIndex = $(item).attr("rows");
        if (isNotEmpty(rowIndex)) {
            var rate = rateType.get("rate" + rowIndex).val();
            moneyType.get("advanceFees" + rowIndex).val(moneyfun.mul(surplusMoney, rate));
        }
    })
}

/**
 * 重新计算
 */
function reCal() {
    // 重新计算中间费用
    calAdvanceFees();
    //重新计算还本付息计划
    calMoney();
}

//单位回调函数
function corpOtherSet(data) {
    if(data){
        //获取单位信息
        $.ajax({
            url: basePath + "/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: data},
            dataType: 'json',
            success: function (data) {
                form.render();
            }
        });
    }
    renderBudget();
};

/**
 * 资金计划挂接
 */
function renderBudget() {
    //判断支付结算是否挂接资金计划
    var isBudget = $("#isBudget").val();
    if (isBudget == "1") {
        //判断直融业务是否挂资金计划
        var dfInfoItem = $("#dfInfoItem").val();
        if (dfInfoItem != null && dfInfoItem != "") {
            genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
}


/**
 * 生成公共的属性
 * @return
 */
function genOtherHtml() {
    var itemCode = $("#dfInfoItem").val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateType.get("createDate").val();
    var corpId = corp.get("corpId").val();
    var curId = selectType.get("curId").val();//币别
    if (!corpId) {
        return false;
    }
    var param = {
        corpId: corpId,
        itemCode: itemCode,
        defItemCode: defItemCode,
        processDate: processDate,
        curId: curId,
        type: 'select',
        date: (new Date()).getTime()
    };
    $.ajax({
        url: basePath + "/budget/fbsBudgetProcess/getItemDetail",
        method: 'POST',
        data: param,
        async: true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            form.render();
        }
    });
}

