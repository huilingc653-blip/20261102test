var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank','corp', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payerAcc").setCorp(id);
    }
    bt.corp.init();
    accountCallBack = function (data) {
        $("#likes_payerAcc").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();
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
            'url': basePath + "/bdlink/bisexc/datatable"
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

            {
                data: "voucherNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.isSpecFailForTranPay=="1") {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "bifName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true},
            {data: "voucherTypeName", bSortable: true},
            {
                data: "failReason", bSortable: true, visible: setFailReasonVisible(),
                render: function (data, type, full, meta) {

                    return full.failReason;
                }
            },
            {data: "corpName", bSortable: true},
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
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "7" && full.voucherFrom != "11") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "busOriTypeName", bSortable: true, className: 'center'},
            {data: "voucherFromName", bSortable: true, className: 'center'},
            {
                data: "voucherStatName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var desc = bt.lang("table.bisExc.isSpecFailForTranPay");
                    if(full.isSpecFailForTranPay=="1"){
                        return full.voucherStatName+desc;
                    } else {
                        return full.voucherStatName;
                    }
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // if (full.voucherFrom && full.voucherFrom == "9") {
                    //     html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("commn.button.trackFlow") + '</a>';
                    // }
                    // 自动调拨策略生成的指令单号首个为8  需过滤
                    if (full.cbsBillId && full.cbsBillId != "" && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "7" && full.voucherFrom != "11") {
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
                    colNum: '7',
                    dataName: 'payMoney'
                }
            ]
        },
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bdlink/bisexc/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        },
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payerAcc").setCorp("");
    }
});

function setFailReasonVisible(){
    var isMathcFailReason = $("#isMathcFailReason").val();
    if (isMathcFailReason == "1") {
        return true;
    } else {
        return false;
    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/view/" + data.id
    });
}

//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    var action = "/netbank/nisbillhead/view/" + cbsBillId;
    //资金调拨
    if (data.voucherFrom != "1" && data.voucherFrom != "10") {
        action = "/allocation/nisallocationreq/view/" + cbsBillId;
    }
    //代发工资
    if (data.voucherFrom == "10") {
        action = "/wage/niswage/view/" + cbsBillId;
    }
    //资金需求单
    if (data.voucherFrom == "12") {
        action = "/netbank/nisdemand/view/" + cbsBillId;
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