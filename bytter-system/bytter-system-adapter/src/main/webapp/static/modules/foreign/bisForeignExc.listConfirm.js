var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'bank', 'date', 'corp', 'dicts','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.corp.init();
    bt.selectType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/bisforeignexc/datatable"
        },
        buttons: [
            /*{
                text: bt.lang('common.button.batchSend'),
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
                    parent.layer.confirm(bt.lang('common.message.confirm.send'), function () {
                        $.post(basePath + "/foreign/bisforeignexc/batchConfirmSend", {ids: ids}, function (result) {
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }, {
                text: bt.lang('common.button.batchDefeat'),
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
                    parent.layer.confirm(bt.lang('common.message.confirm.defeat'), function () {
                        $.post(basePath + "/foreign/bisforeignexc/batchDefeat", {ids: ids}, function (result) {
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            },*/
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [

            {data: "voucherNo", bSortable: true, className: 'center'},
            {data: "bankTypeName", bSortable: true, className: 'center'},
            {data: "englishCode", bSortable: true},
            {
                data: "remitType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexForeignReq.remitType", full.remitType);
                }
            },
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "reqDate", bSortable: true, className: 'center'},
            {
                data: "cbsBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.cbsBillNo != "" && full.cbsBillNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {
                data: "cmdType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bisExc.cmdType", full.cmdType);
                }
            },
            {
                data: "voucherStat", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bisExc.voucherStat", full.voucherStat);
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('foreign:foreignExc:bisForeignExc:confirmApply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sendColumn(this)">' + bt.lang("common.button.send") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backColumn(this)">' + bt.lang("common.button.return") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeatColumn(this)">' + bt.lang("common.button.defeat") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (full.cbsBillNo != "" && full.cbsBillNo != null) {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + bt.lang("common.button.viewBusBill") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'englishCode',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        }
    });
});

//发送
function sendColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.send'), function () {
        $.post(basePath + "/bdlink/bpdubioustrade/send/" + data.id+"/" + data.voucherNo+"/1", function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//退回
function backColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.prompt({
        title: bt.lang('table.bisExc.backMsg'),
        formType: 2
    },function(backMsg){
        $.post(basePath + "/foreign/bisforeignexc/back", {id:data.id,backMsg:backMsg}, function (result) {
            datatable && datatable.draw();
            parent.layer?parent.layer.closeAll():layer.closeAll();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//作废
function defeatColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.defeat'), function () {
        $.post(basePath + "/bdlink/bpdubioustrade/defeat/" + data.id+"/" + data.voucherNo+"/1", function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/bisforeignexc/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/bisforeignexc/view/" + data.id
    });
}

//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var action = "/foreign/fextranreq/view/" + data.cbsBillId;
    //外汇交易现金流
    if (data.voucherFrom == "2") {
        action = "/foreign/fexcashflow/view/" + data.cbsBillId;
        if (data.excSource == "REQ") {
            action = "/foreign/fexTradingReq/view/" + data.cbsBillId;
        }
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + action);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + action
    });
}