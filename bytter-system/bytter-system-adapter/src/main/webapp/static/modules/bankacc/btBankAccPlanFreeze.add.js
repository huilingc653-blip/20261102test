var $, form, dateType, moneyType, moneyFun;
bt.use(['form', 'layedit', 'moneyType', 'bttable', 'datatable', 'money', 'dateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

    //格式化金额
    var bMoneyDiv = $('div[name="money"]');
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }

    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccplanfreeze/planFreezeTemp";
        // 冻结金额不能小于0
        var applyFreezeMoney = bt.money.toNumber(moneyType.get("applyFreezeMoney").val());
        if (applyFreezeMoney <= 0) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.gtzero"), {icon: 2});
            return false;
        }
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var selectedTrs = $("#freezeTemp").find("tbody tr.selected");
        var ids = "";
        bt.each(selectedTrs, function () {
            var id = $(this).find("td input").data("id");
            if (id) {
                ids += id + ",";
            }
        })
        if (ids === "") {
            bt.loading.hide();
            layer.alert(bt.lang("table.fcBankBill.msg.chooseLeastOne"));
            return false;
        }
        var action = "/bankacc/btbankaccplanfreeze/planFreezeSubmit?ids=" + ids;
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });

    var bttable = bt.bttable.init({
        elem: "#freezeTemp",
        url: basePath + "/bankacc/btbankaccplanfreeze/listTempPlanFreeze?bankAccId=" + $('#bankAccId').val() + "&curId=" + $('#curId').val(),
        checkbox: true,
        headerCheckbox: true,
        columns: [
            {"data": "planFreezeNo"},
            {"data": "bankAcc"},
            {"data": "freezeStartDate"},
            {"data": "freezeEndDate"},
            {
                "data": "applyFreezeMoney", className: 'textRight',
                render: function (data, id) {
                    if (data.applyFreezeMoney == null) {
                        return "0.00";
                    }
                    return bt.money.format(data.applyFreezeMoney);
                }
            },
            {
                "data": "realFreezeMoney", className: "textRight",
                render: function (data, id) {
                    if (data.realFreezeMoney == null) {
                        return "0.00";
                    }
                    return bt.money.format(data.realFreezeMoney);
                }
            },
            {"data": "freezeRession"},
            {"data": "statusName"},
            {"data": "freezeStatusName"},
            {"data": "isFreezeComplete", render: function (data, id) {
                    if (data.isFreezeComplete && data.isFreezeComplete === '1') {
                        return "是";
                    } else {
                        return "否";
                    }
                }},
            {"data": "isUnfreeze", render: function (data, id) {
                    if (data.isUnfreeze && data.isUnfreeze === '1') {
                        return "是";
                    } else {
                        return "否";
                    }
                }},
            {
                "data": "null",
                render: function (data, id) {
                    var html = '';
                    html += '<a href="javascript:void(0)" data-type="edit" class="tableElema">' + bt.lang("table.Freeze.modify") + '</a> ';
                    html += ' <a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            },
        ],
        tableInitCallback: function (data) {
            bt.moneyType.init();
            initSumMoney();
        },
        delRowCallback: function (data, $tr) {
            var id = $tr.data("id");
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                bt.jquery.ajax({
                    url: basePath + "/bankacc/btbankaccplanfreeze/delete/" + id,
                    type: "Post",
                    dataType: "json",
                    success: function (result) {
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        location.reload();
                    }
                });

            });
        },
        editRowCallback: function (data, $tr) {
            var id = $tr.data("id");
            parent.layer.open({
                type: 2,
                area: ['900px', '700px'],
                maxmin: true,
                content: basePath + "/bankacc/btbankaccplanfreeze/update?id=" + id,
                end: function () {
                }
            });
        }
    });

    //汇总冻结金额
    function initSumMoney() {
        var moneySum = 0;
        $.each($('#freezeTemp').find("td[name='applyFreezeMoney']"), function (i, item) {
            var money = moneyFun.toNumber($(this)[0].innerText);
            if (null == money || "" == money || isNaN(money)) money = 0;
            moneySum += parseFloat(money);
        });
        $("#sum").find('td[name="money"]').text(moneyFun.format(moneySum));
    }

});

// 重置
function planFreezeReset() {
    moneyType.get("applyFreezeMoney").val('');//冻结金额
    dateType.get("freezeStartDate").val('');//冻结开始时间
    dateType.get("freezeEndDate").val('');//冻结结束时间
    $("#freezeRession").val('');//冻结原因
}
