bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    var fcInitAccTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcinitacc/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:innerAccount:fcInitAcc:apply'),
                text: bt.lang('common.button.check'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var ids = "";
                    var curId = "", netId = "", accStartDate = "";
                    var flag = false, flag2 = false, flag3 = false;
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }

                        if (curId == "") {
                            curId = rowData[i].curId;
                        } else {
                            if (curId != rowData[i].curId) {
                                flag = true;
                                break;
                            }
                        }

                        if (netId == "") {
                            netId = rowData[i].netId;
                        } else {
                            if (netId != rowData[i].netId) {
                                flag2 = true;
                                break;
                            }
                        }

                        if (accStartDate == "") {
                            accStartDate = rowData[i].accStartDate;
                        } else {
                            if (accStartDate != rowData[i].accStartDate) {
                                flag3 = true;
                                break;
                            }
                        }
                    }

                    if (flag) {
                        layer.msg(bt.lang('请选择同币别账号！'));
                        return;
                    }
                    if (flag2) {
                        layer.msg(bt.lang('请选择同网点账号！'));
                        return;
                    }
                    if (flag3) {
                        layer.msg(bt.lang('请选择同启用日期账号！'));
                        return;
                    }
                    var action = "/inneraccount/fcinitacc/check";
                    parent.layer.confirm(bt.lang('common.message.confirm.check'), function () {
                        $.post(basePath + action, {ids: ids, status: 95}, function (result) {
                            fcInitAccTable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }, {
                hide: bt.isnotResource('netbank:innerAccount:fcInitAcc:apply'),
                text: bt.lang('common.button.back'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var ids = "";
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var action = "/inneraccount/fcinitacc/check";
                    parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
                        $.post(basePath + action, {ids: ids, status: -1}, function (result) {
                            fcInitAccTable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }
        ],
        columns: [
            {data: "account", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "netName", bSortable: true},
            {
                data: "debitMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (full.balanceWay == 1) {
                        return bt.money.format(full.balance);
                    } else {
                        return '';
                    }
                }
            },
            {
                data: "creditMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (full.balanceWay == -1) {
                        return bt.money.format(full.balance);
                    } else {
                        return '';
                    }
                }
            },
            {
                data: "cumulation", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.cumulation);
                }
            },
            {
                data: "yearBalance", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.yearBalance);
                }
            },
            {
                data: "yearTotal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.yearTotal);
                }
            },
            {data: "accStartDate", bSortable: true}
        ]
    });

});