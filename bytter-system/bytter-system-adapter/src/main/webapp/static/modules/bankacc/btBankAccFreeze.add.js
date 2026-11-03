bt.use(['form', 'layedit', 'moneyType', 'bttable', 'datatable', 'money'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    moneyType.get("freezeMoney").val($("#availBal").val());
    //格式化金额
    var bMoneyDiv = $('div[name="money"]');//上报金额
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }
    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccfreeze/freezeSaveTemp";
        var freezeMoney = $("#freezeMoney").val();
        var freezeMoneyNum = bt.money.toNumber(freezeMoney);
        var availBal = $("#availBal").val();
        if (freezeMoneyNum <= 0) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.gtzero"), {icon: 2});
            return false;
        }
        if (freezeMoneyNum > availBal) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.geavailBal"), {icon: 2});
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
        var availBal = bt.money.toNumber($("#availBal").val());
        var sumFreezeMoney = 0;
        bt.each(selectedTrs, function () {
            var id = $(this).find("td input").data("id");
            var freezeMoney = $(this).find("td[name='freezeMoney']").text();
            var freezeMoneyNum = bt.money.toNumber(freezeMoney);
            if (id) {
                ids += id + ",";
            }
            sumFreezeMoney +=freezeMoneyNum;
        })
        if (sumFreezeMoney > availBal) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.geavailBal"), {icon: 2});
            return false;
        }
        if (ids == "") {
            bt.loading.hide();
            layer.alert(bt.lang("table.fcBankBill.msg.chooseLeastOne"));
            return false;
        }
        var action = "/bankacc/btbankaccfreeze/freezeSave?ids=" + ids;
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
        url: basePath + "/bankacc/btbankaccfreeze/getTempFreeze?bankAccId=" + $('#bankAccId').val() + "&curId=" + $('#curId').val(),
        checkbox: true,
        headerCheckbox: true,
        columns: [
            {"data": "freezeNo",},
            {
                "data": "freezeMoney", className: 'textRight',
                render: function (data, id) {
                    if (data.freezeMoney == null) {
                        return "0.00";
                    }
                    return bt.money.format(data.freezeMoney);
                }
            },
            {"data": "updateDate"},
            {
                "data": "ufFreezeMoney", className: "textRight",
                render: function (data, id) {
                    if (data.ufFreezeMoney == null) {
                        return "0.00";
                    }
                    return bt.money.format(data.ufFreezeMoney);
                }
            },
            {
                "data": "ufavalBal", className: "textRight",
                render: function (data, id) {
                    if (data.ufavalBal == null) {
                        return "0.00";
                    }
                    return bt.money.format(data.ufavalBal);
                }
            },
            {"data": "freezeRession",},
            {"data": "statusName",},
            {
                "data": "null",
                render: function (data, id) {
                    //console.log(data.id);
                    var html = '';
                    html += '<a href="javascript:void(0)" data-type="edit" class="tableElema">' + bt.lang("table.Freeze.modify") + '</a> ';
                    html += ' <a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            },
        ],
        tableInitCallback: function (data) {
            // $.each($('#freezeTemp').find("td[name='freezeMoney']"), function (i, item) {
            //     $(item).on('input propertychange', initSumMoney);
            // });
            bt.moneyType.init();
            // if ($("#td_check").length == 0) {
            //     $("#sum").prepend("<td id='td_check'>&nbsp;</td>")
            // }
            initSumMoney();
        },
        delRowCallback: function (data, $tr) {
            var id = $tr.data("id");
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                bt.jquery.ajax({
                    url: basePath + "/bankacc/btbankaccfreeze/delete/" + id,
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
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/bankacc/btbankaccfreeze/update?id=" + id,
                end: function () {
                    location.reload();
                }
            });
        }
    });

    /**
     * 汇总冻结金额
     */
    initSumMoney = function () {
        var moneySum = 0;
        $.each($('#freezeTemp').find("td[name='freezeMoney']"), function (i, item) {
            var money = moneyFun.toNumber($(this)[0].innerText);
            if (null == money || "" == money || isNaN(money)) money = 0;
            moneySum += parseFloat(money);
        });
        $("#sum").find('td[name="money"]').text(moneyFun.format(moneySum));
    }

    bt.bttable.init({
        elem: "#freeze",
        // checkbox: true,
        // headerCheckbox: true,
        url: basePath + "/bankacc/btbankaccunfreeze/getUnFreeze?bankAccId=" + $('#bankAccId').val() + "&curId=" + $('#curId').val(),
        columns: [
            {"data": "freezeNo",},
            {
                "data": "freezeMoney", className: "textRight", render: function (data, id) {
                if (data.freezeMoney == null) {
                    return 0;
                }
                return bt.money.format(data.freezeMoney);
            }
            },
            {
                "data": "freezeDateStr", render: function (data, id) {
                if (data.freezeDateStr == null) {
                    return "--";
                }
                return data.freezeDateStr;
            }
            },
            {
                "data": "ufFreezeMoney", className: "textRight", render: function (data, id) {
                if (data.ufFreezeMoney == null) {
                    return 0;
                }
                return bt.money.format(data.ufFreezeMoney);
            }
            },
            {
                "data": "ufavalBal", className: "textRight", render: function (data, id) {
                if (data.ufavalBal == null) {
                    return 0;
                }
                return bt.money.format(data.ufavalBal);
            }
            },
            {"data": "freezeRession",},
            {"data": "statusName", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.dict("bisExc.status", data.status);
                }
            }
        ],
        tableInitCallback: function (data) {
            // $.each($('#freeze').find("td[name='freezeMoney']"), function (i, item) {
            //     $(item).on('input propertychange', initSumMoney);
            // });
            bt.moneyType.init();
            // if ($("#td_check1").length == 0) {
            //     $("#freeSum").prepend("<td id='td_check1'>&nbsp;</td>")
            // }
            initFreezeSumMoney();
        }
    })
    /**
     * 汇总冻结金额
     */
    initFreezeSumMoney = function () {
        var moneySum = 0;
        $.each($('#freeze').find("td[name='freezeMoney']"), function (i, item) {
            var money = moneyFun.toNumber($(this)[0].innerText);
            if (null == money || "" == money || isNaN(money)) money = 0;
            moneySum += parseFloat(money);
        });
        $("#freeSum").find('td[name="freeMoney"]').text(moneyFun.format(moneySum));
    }
});

function update(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccfreeze/update?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccfreeze/update?id=" + id,
        end: function () {
            location.reload();
        }
    });
}

function del(id) {
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.jquery.ajax({
            url: basePath + "/bankacc/btbankaccfreeze/delete/" + id,
            type: "Post",
            dataType: "json",
            success: function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                location.reload();
            }
        });

    });
}
