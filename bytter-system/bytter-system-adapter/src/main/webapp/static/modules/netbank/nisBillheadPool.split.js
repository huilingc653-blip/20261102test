var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'moneyType', 'bttable', 'btUpload', 'dateType'], function () {
    var $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        isExpand: true,
        gid: $("#attachmentId").val()
    });
    //生成列表
    var rowMR = 1;
    bt.bttable.init({
        elem: "#dataTable",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                rowMR++;
                var html = '<tr row=' + rowMR + '>';
                html += '<td>' + $("#payerAcc").val() + '</td>';
                html += '<td>' + $("#payerAccName").val() + '</td>';
                html += '<td>' + $("#voucherName").val() + '</td>';
                html += '<td>' + $("#curName").val() + '</td>';
                html += '<td align="center"><input type="hidden" id="budget' + rowMR + '" name="budget"><div class="layui-input-inline"><input type="text" data-name="payMoney" data-biz-name="payMoney' + rowMR + '" name="payMoney" id="payMoney' + rowMR + '" onclick="$genOtherHtmlOpen(' + rowMR + ')" class="layui-input bt-biz-moneyType" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00"/><div></td>';
                html += '<td>' + $("#payeeAcc").val() + '</td>';
                html += '<td>' + $("#payeeAccName").val() + '</td>';
                html += '<td><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a></td>';
                html += '</tr>';
                $("#dataTable").find("tbody").find("#sumTr").before(html);
                bt.moneyType.init();
                form.render();
            }
        }],
        columns: [{
            data: "payerAcc",
            render: function (data, row) {
                return $("#payerAcc").val();
            }
        }, {
            data: "payerAccName",
            render: function (data, row) {
                return $("#payerAccName").val();
            }
        }, {
            data: "voucherName",
            render: function (data, row) {
                return $("#voucherName").val();
            }
        }, {
            data: "curName",
            render: function (data, row) {
                return $("#curName").val();
            }
        }, {
            data: "curName",
            render: function (data, row) {
                return '<input type="hidden" id="budget' + rowMR + '" name="budget" value=""><input type="text" data-name="payMoney" data-biz-name="payMoney' + rowMR + '" name="payMoney" id="payMoney' + rowMR + '" onclick="$genOtherHtmlOpen(' + rowMR + ')" class="layui-input" data-type="number" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00"/>';
            }
        }, {
            data: "payeeAcc",
            render: function (data, row) {
                return $("#payeeAcc").val();
            }
        }, {
            data: "payeeAccName",
            render: function (data, row) {
                return $("#payeeAccName").val();
            }
        }, {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
            }
        }],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var payMoneys = [];
        var budgets = [];
        $('form').find('input[name="payMoney"]').each(function () {
            payMoneys.push($(this).val());
        });
        $('form').find('input[name="budget"]').each(function () {
            budgets.push($(this).val());
        });
        if (payMoneys) {
            data.field.payMoneys = payMoneys;
        }
        if (budgets) {
            data.field.budgets = budgets;
        }
        var action = "/netbank/nisbillheadPool/splitSave";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (corpId) {
        var url = basePath + "/budget/fbsBudgetProcess/getViewItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#wishPayday").val(),
            type: 'select',
            date: (new Date()).getTime(),
            detailControl: true,
            curId: $("#curId").val(),
            settlType: $("#settlType").val(),
            purpose: $("#purpose").val(),
            wishPayday: $("#wishPayday").val(),
            payerAcc: $("#payerAcc").val(),
            payeeAccName: $("#payeeAccName").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeBank").val(),
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var reportBudget = $("#reportBudget").val();
        if (budgetSel == "1" && reportBudget == "1") {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                $genOtherHtml(corpId);
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/netbank/nismessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();

    /**
     * 弹出资金计划页面
     * @return
     */
    $genOtherHtmlOpen = function (rowNum) {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var reportBudget = $("#reportBudget").val();
        if (budgetSel == "1" && reportBudget == "1") {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                var param = {
                    corpId: $("#corpId").val(),
                    itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
                    billNo: $("#billNo").val(),
                    processDate: $("#wishPayday").val(),
                    type: 'open',
                    date: (new Date()).getTime(),
                    budget: $("#budget" + rowNum).val(),
                    rowNum: rowNum,
                    detailControl: true,
                    curId: $("#curId").val() ? $("#curId").val() : "",
                    settlType: $("#settlType").val() ? $("#settlType").val() : "",
                    purpose: $("#purpose").val() ? $("#purpose").val() : "",
                    wishPayday: $("#wishPayday").val() ? $("#wishPayday").val() : "",
                    payerAcc: $("#payerAcc").val() ? $("#payerAcc").val() : "",
                    payeeAccName: $("#payeeAccName").val() ? $("#payeeAccName").val() : "",
                    payeeAcc: $("#payeeAcc").val() ? $("#payeeAcc").val() : "",
                    payeeBank: $("#payeeBank").val() ? $("#payeeBank").val() : ""
                };
                var url = basePath + "/budget/fbsBudgetProcess/getOpenItemDetail";
                parent.layer.open({
                    type: 2,
                    area: ['1000px', '530px'],
                    maxmin: true,
                    content: url + "?" + parseParam(param),
                    end: function () {
                    }
                });
            }
        }
    };

    /**
     * 重新组装参数
     * @param param
     * @param key
     * @returns {string}
     */
    var parseParam = function (param, key) {
        var paramStr = "";
        if (param instanceof String || param instanceof Number || param instanceof Boolean) {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        } else {
            $.each(param, function (i) {
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + parseParam(this, k);
            });
        }
        return paramStr.substr(1);
    };

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        $("input[name='budget']").val('');
    }
});

//合计金额
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var moneyTotal = 0;
    $('form').find('input[data-name="payMoney"]').each(function () {
        moneyTotal = moneyFun.add(moneyTotal, moneyFun.toNumber($(this).val()));
    });
    $("#sumPayMoney").text(moneyFun.format(moneyTotal));
}

/**
 * 资金计划回调
 * @param sumMoney
 * @param datas
 * @param rowNum
 */
function budgetProcessCallback(sumMoney, datas, rowNum) {
    var $ = bt.jquery, form = bt.form();
    bt.moneyType.get("payMoney" + rowNum).val(sumMoney);
    $("#budget" + rowNum).val(datas);
    var moneyTotal = 0;
    $('form').find('input[data-name="payMoney"]').each(function () {
        moneyTotal = moneyFun.add(moneyTotal, moneyFun.toNumber($(this).val()));
    });
    $("#sumPayMoney").text(moneyFun.format(moneyTotal));
}