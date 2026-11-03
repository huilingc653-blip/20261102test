var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'dateType', 'dicts', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,

        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bdlink/bisgathercmd/datatable"
        },
        buttons: [
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
            {data: "bifName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true},
            {data: "parentAcc", bSortable: true},
            {data: "parentAccName", bSortable: true},
            {data: "parentBank", bSortable: false},
            {
                data: "gatherAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.gatherAmt);
                }
            },
            {data: "childAcc", bSortable: true},
            {data: "childAccName", bSortable: true},
            {data: "childBank", bSortable: false},
            {data: "runTime", bSortable: true, className: 'center'},
            {data: "transTime", bSortable: true, className: 'center'},
            {
                data: "cbsBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "busOriTypeName", bSortable: true, className: 'center'},
            {data: "voucherFromName", bSortable: true, className: 'center'},
            {data: "cmdStatName", bSortable: true, className: 'center'},
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // 自动调拨策略生成的指令单号首个为8  需过滤
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8") {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + bt.lang("common.button.viewBusBill") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: false,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6',
                    dataName: 'gatherAmt'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bdlink/bisgathercmd/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        },
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.cmdStat == "9" || aData.cmdStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisgathercmd/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisgathercmd/view/" + data.id
    });
}

//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/allocation/nisallocationreq/view/" + cbsBillId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/nisallocationreq/view/" + cbsBillId
    });
}
