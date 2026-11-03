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
    var showTranAmt = $("#showTranAmt").val();
    var columns = [
        {
            data: "corpBankAcc",
            render: function (data, row) {
                return $("#corpBankAcc").val();
            }
        }, {
            data: "corpAccName",
            render: function (data, row) {
                return $("#corpAccName").val();
            }
        }, {
            data: "curEnglishCode",
            render: function (data, row) {
                return $("#curName").val();
            }
        }, {
            data: "payMoney",
            render: function (data, row) {
                return '<input type="hidden" id="budget' + rowMR + '" name="budget" value=""><input type="text" data-name="payMoney" data-biz-name="payMoney' + rowMR + '" name="payMoney" id="payMoney' + rowMR + '"  onclick="$genOtherHtmlOpen(' + rowMR + ')" class="layui-input" data-type="number" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00"/>';
            }
        }, {
            data: "cotpBankAcc",
            render: function (data, row) {
                return $("#payeeAcc").val();
            }
        }, {
            data: "payeeAccName",
            render: function (data, row) {
                return $("#cotpName").val();
            }
        }, {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
            }
        }
    ];
    if (showTranAmt == "1") {
        columns = [
            {
                data: "corpBankAcc",
                render: function (data, row) {
                    return $("#corpBankAcc").val();
                }
            }, {
                data: "corpAccName",
                render: function (data, row) {
                    return $("#corpAccName").val();
                }
            }, {
                data: "curEnglishCode",
                render: function (data, row) {
                    return $("#curName").val();
                }
            }, {
                data: "payMoney",
                render: function (data, row) {
                    return '<input type="hidden" id="budget' + rowMR + '" name="budget" value=""><input type="text" data-name="payMoney" data-biz-name="payMoney' + rowMR + '" name="payMoney" id="payMoney' + rowMR + '"  onclick="$genOtherHtmlOpen(' + rowMR + ')" class="layui-input" data-type="number" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00" data-max="' + payMoney + '"/>';
                }
            }, {
                data: "payMoney",
                render: function (data, row) {
                    return '<input type="text" data-name="payMoney" data-biz-name="tranAmt1' + rowMR + '" name="tranAmt1" id="tranAmt1' + rowMR + '" class="layui-input" data-type="number" autocomplete="off"  lay-verify="required|number2" data-callback="calTranAmt" value="0.00" data-max="' + payMoney + '"/>';
                }
            }, {
                data: "payMoney",
                render: function (data, row) {
                    return '<input type="text" data-name="payMoney" data-biz-name="tranAmt2' + rowMR + '" name="tranAmt2" id="tranAmt2' + rowMR + '" class="layui-input" data-type="number" autocomplete="off" value="0.00"/>';
                }
            }, {
                data: "cotpBankAcc",
                render: function (data, row) {
                    return $("#payeeAcc").val();
                }
            }, {
                data: "payeeAccName",
                render: function (data, row) {
                    return $("#cotpName").val();
                }
            }, {
                data: null,
                render: function (data, row) {
                    return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
                }
            }
        ];
    }
    var payMoney = $("#money").val();
    bt.bttable.init({
        elem: "#dataTable",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                rowMR++;
                var html = '<tr row=' + rowMR + '>';
                html += '<td>' + $("#corpBankAcc").val() + '</td>';
                html += '<td>' + $("#corpAccName").val() + '</td>';
                html += '<td>' + $("#curEnglishCode").val() + '</td>';
                html += '<td align="center" class="payMoneyCol"><input type="hidden" id="budget' + rowMR + '" name="budget"><div class="layui-input-inline"><input type="text" data-name="payMoney" data-biz-name="payMoney' + rowMR + '" name="payMoney" id="payMoney' + rowMR + '" onclick="$genOtherHtmlOpen(' + rowMR + ')" class="layui-input bt-biz-moneyType" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00" data-max="' + payMoney + '"/><div></td>';
                if (showTranAmt == "1") {
                    html += '<td align="center" class="tranAmt1Col"><div class="layui-input-inline"><input type="text" data-name="tranAmt1" data-biz-name="tranAmt1' + rowMR + '" name="tranAmt1" id="tranAmt1' + rowMR + '" class="layui-input bt-biz-moneyType" autocomplete="off" lay-verify="required|number2" data-callback="calTranAmt" data-max="' + payMoney + '"/><div></td>';
                    html += '<td align="center" class="tranAmt2Col"><div class="layui-input-inline"><input type="text" data-name="tranAmt2" data-biz-name="tranAmt2' + rowMR + '" name="tranAmt2" id="tranAmt2' + rowMR + '" class="layui-input bt-biz-moneyType" autocomplete="off" readonly="readonly"/><div></td>';
                }
                html += '<td>' + $("#cotpBankAcc").val() + '</td>';
                html += '<td>' + $("#cotpName").val() + '</td>';
                html += '<td><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a></td>';
                html += '</tr>';
                $("#dataTable").find("tbody").find("#sumTr").before(html);
                bt.moneyType.init();
                form.render();
            }
        }],
        columns: columns,
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
        if (budgets) {
            data.field.budgets = budgets;
        }
        if (payMoneys) {
            data.field.payMoneys = payMoneys;
        }
        var action = "/foreign/fextranreq/splitSave";
        var showTranAmt = $("#showTranAmt").val();
        if (showTranAmt == "1") {
            var tranAmt1s = [];//相应金额1
            $('form').find('input[name="tranAmt1"]').each(function () {
                tranAmt1s.push($(this).val());
            });
            var tranAmt2s = [];//相应金额2
            $('form').find('input[name="tranAmt2"]').each(function () {
                tranAmt2s.push($(this).val());
            });
            if (tranAmt1s) {
                data.field.tranAmt1s = tranAmt1s;
            }
            if (tranAmt2s) {
                data.field.tranAmt2s = tranAmt2s;
            }
            action = "/foreign/fextranreq/splitAmtSave";
        }
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
            itemCode: $("#payItem").val(),
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: $("#settleDate").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            wishPayday: $("#settleDate").val(),
            payerAcc: $("#corpBankAcc").val(),
            payeeAccName: $("#cotpAccName").val(),
            payeeAcc: $("#cotpBankAcc").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    }

    var corpId = $("#corpId").val();
    renderBudget(corpId);

    /**
     * 资金计划挂接
     */
    function renderBudget(corpId) {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var cdSign = $("#cdSign").val();
        if (budgetSel == "1" && cdSign=="1") {
            $genOtherHtml(corpId);
        } else {
            $("#tagItemDetail").html("");
        }
    }


    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/foreign/fexmessage/list/" + $('#id').val() + "/" + $("#busType").val() + "?timestamp=" + new Date().getTime();
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
    //$genMsgHtml();
    /**
     * 弹出资金计划页面
     * @return
     */
    $genOtherHtmlOpen = function (rowNum) {
        var budgetSel = $("#budgetSel").val();
        var cdSign = $("#cdSign").val();
        if (budgetSel == "1" && cdSign=="1") {
            var param = {
                corpId: $("#corpId").val(),
                itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
                billNo: $("#billNo").val(),
                processDate: $("#settleDate").val(),
                type: 'open',
                date: (new Date()).getTime(),
                budget: $("#budget" + rowNum).val(),
                rowNum: rowNum,
                detailControl: true,
                curId: $("#curId").val() ? $("#curId").val() : "",
                settlType: $("#settlType").val() ? $("#settlType").val() : "",
                purpose: $("#purpose").val() ? $("#purpose").val() : "",
                wishPayday: $("#settleDate").val() ? $("#settleDate").val() : "",
                payerAcc: $("#corpBankAcc").val() ? $("#corpBankAcc").val() : "",
                payeeAccName: $("#cotpAccName").val() ? $("#cotpAccName").val() : "",
                payeeAcc: $("#cotpAccName").val() ? $("#cotpAccName").val() : "",
                payeeBank: $("#cotpBankAcc").val() ? $("#cotpBankAcc").val() : "",
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
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreq/viewCustInfo/" + $('#id').val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#custMsg").html("");
                $("#custMsg").html(json);
                form.render();
            }
        });
    }
    $genCustHtml();

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        $("input[name='budget']").val('');
        $("#sumPayMoney").text(0.00);
    }
});

//合计金额
function calItemMoney(value, elem) {
    var $ = bt.jquery, form = bt.form();
    //拆分金额
    var payMoneyNum = moneyFun.toNumber(value);
    //获取相应金额1
    var tranAmt1 = elem.parents('tr').find('.tranAmt1Col').find('input[name="tranAmt1"]').val();
    //相应金额1未填写时，默认根据拆分金额自动带出（此时相应金额2为0）
    if (payMoneyNum && !tranAmt1) {
        elem.parents('tr').find('.tranAmt1Col').find('input[name="tranAmt1"]').val(moneyFun.format(payMoneyNum, {
            separator: "",
            decimal: 2
        }));
        elem.parents('tr').find('.tranAmt1Col').find('input[data-name="tranAmt1"]').val(moneyFun.format(payMoneyNum, {
            separator: ",",
            decimal: 2
        }));
        tranAmt1 = payMoneyNum;
    }
    //拆分金额为空或者为0，或者相应金额1大于拆分金额（相应金额1必须小于等于拆分金额）
    if (!payMoneyNum || !tranAmt1 || moneyFun.toNumber(tranAmt1) > payMoneyNum) {
        elem.parents('tr').find('.tranAmt1Col').find('input[name="tranAmt1"]').val('');
        elem.parents('tr').find('.tranAmt1Col').find('input[data-name="tranAmt1"]').val('');
        elem.parents('tr').find('.tranAmt2Col').find('input[name="tranAmt2"]').val('');
        elem.parents('tr').find('.tranAmt2Col').find('input[data-name="tranAmt2"]').val('');
    } else {
        //相应金额2 = 拆分金额 - 相应金额1
        var tranAmt2 = moneyFun.sub(payMoneyNum, moneyFun.toNumber(tranAmt1));
        elem.parents('tr').find('.tranAmt2Col').find('input[name="tranAmt2"]').val(moneyFun.format(tranAmt2, {
            separator: "",
            decimal: 2
        }));
        elem.parents('tr').find('.tranAmt2Col').find('input[data-name="tranAmt2"]').val(moneyFun.format(tranAmt2, {
            separator: ",",
            decimal: 2
        }));
    }
    var moneyTotal = 0;
    $('form').find('input[data-name="payMoney"]').each(function () {
        moneyTotal = moneyFun.add(moneyTotal, moneyFun.toNumber($(this).val()));
    });
    $("#sumPayMoney").text(moneyFun.format(moneyTotal));
}

// 计算相应金额2
function calTranAmt(value, elem) {
    //获取拆分金额
    var payMoney = elem.parents('tr').find('.payMoneyCol').find('input[name="payMoney"]').val();
    var payMoneyNum = moneyFun.toNumber(payMoney);
    var tranAmt1Num = moneyFun.toNumber(value);
    //相应金额1必须小于等于拆分金额
    if (tranAmt1Num > payMoneyNum) {
        layer.alert(bt.lang("foreign.message.tranAmt2.gt.payMoney"), {icon: 2});
        elem.parents('tr').find('.tranAmt1Col').find('input[name="tranAmt1"]').val('');
        elem.parents('tr').find('.tranAmt1Col').find('input[data-name="tranAmt1"]').val('');
        elem.parents('tr').find('.tranAmt2Col').find('input[name="tranAmt2"]').val('');
        elem.parents('tr').find('.tranAmt2Col').find('input[data-name="tranAmt2"]').val('');
        return false;
    }
    //相应金额2 = 拆分金额 - 相应金额1
    var tranAmt2 = moneyFun.sub(payMoneyNum, tranAmt1Num);
    elem.parents('tr').find('.tranAmt2Col').find('input[name="tranAmt2"]').val(moneyFun.format(tranAmt2, {
        separator: "",
        decimal: 2
    }));
    elem.parents('tr').find('.tranAmt2Col').find('input[data-name="tranAmt2"]').val(moneyFun.format(tranAmt2, {
        separator: ",",
        decimal: 2
    }));
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
    //回调
    calItemMoney(sumMoney,$('[data-biz-name="payMoney'+rowNum+'"]'));
    $("#budget" + rowNum).val(datas);
    var moneyTotal = 0;
    $('form').find('input[data-name="payMoney"]').each(function () {
        moneyTotal = moneyFun.add(moneyTotal, moneyFun.toNumber($(this).val()));
    });
    $("#sumPayMoney").text(moneyFun.format(moneyTotal));
}