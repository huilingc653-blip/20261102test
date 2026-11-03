var datatable;
bt.use(['form', 'datatable', 'corp', 'dicts', 'moneyType', 'dateType', 'selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/finacial/interest/single/nod/dataTableBooking"
        },
        buttons: [
            {
                hide: bt.isnotResource('finacial:interest:nod:booking:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('finacial:interest:nod:booking:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    if (data.length < 1) {
                        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
                        return;
                    }
                    var ids = "";
                    var rowData = data.splice(0, data.length);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
                        $.post(basePath + "/chowmatistic/choinnerfinance/batchDelete/", {ids: ids}, function (result) {
                            bt.datatable.refreshCurrentPage();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        }
        , columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "noticeAccount", bSortable: true},
            {data: "bookDate", bSortable: true, className: 'center'},
            {
                data: "bookMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bookMoney);
                }
            },
            {data: "purpose", bSortable: true},
            {
                data: "noticeTypeName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAcc.notifyType", full.noticeType);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("InterestSingleNodBook.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('finacial:interest:nod:booking:apply', full.corpId)) {
                        if (full.status == 0) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            if (bt.isResource('finacial:interest:nod:booking:unbooking', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="unBooking(this)">' + bt.lang("table.interestSingleNod.unBooking") + '</a><span class="text-explode">|</span>';
                            }
                        }
                        if (full.status != -2 && full.status != 3) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ]
    });

    //通知存款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#noticeAccountTable",
        url: basePath + "/finacial/interest/single/nod/getNoticeAccount",
        scrollX: "500px",
        scrollY: "200px",
        searchInput: "#eqs_noticeAccount",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
        ],
        clickCallback: function (data) {
            if (data) {
                $("#eqs_noticeAccount").val(data.blnctrAcc);
            } else {
                $('input.text').val('');
            }
        }
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/finacial/interest/single/nod/addBooking");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/single/nod/addBooking",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/single/nod/viewBooking/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/finacial/interest/single/nod/updateBooking?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/finacial/interest/single/nod/updateBooking?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/finacial/interest/single/nod/deleteBooking/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function unBooking(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('table.interestSingleNod.message.confirm.unBooking'), function () {
        $.post(basePath + "/finacial/interest/single/nod/unBooking/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}