var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType', 'element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    var selectType = bt.selectType.init();
    bt.mulselect.init();
    var dateFun = bt.dateType.getFun();

    bt.changePro = function(data){
        selectType.get('eqs_proId').attr({"data-url":basePath+"/foreign/fexproduct/listProduct/" + data.value,"data-defaultValue":""});
        form.render();
    };
    bt.dicts.init();

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
            'url': basePath + "/foreign/fexcashflow/dataTableApprove"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "tranBillNo", bSortable: true, className: 'center'},
            {data: "proName", bSortable: true},
            {data: "settleNo", bSortable: true, className: 'center'},
            {data: "cashFlowDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "settleType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCashFlow.settleType", full.settleType);
                }
            },
            {data: "englishCode", bSortable: true, className: 'center'},
            {
                data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBusType.cdSign", full.cdSign);
                }
            },
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "corpBankName", bSortable: true},
            {data: "corpAccName", bSortable: true},
            {data: "corpBankAcc", bSortable: true},
            {
                data: "cotpName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.tranType=="3"){
                        return full.allocatCotpName;
                    }else{
                        return full.cotpName;
                    }
                }
            },
            {
                data: "cotpBankName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.tranType=="3"){
                        return full.allocatCotpBankName;
                    }else{
                        return full.cotpBankName;
                    }
                }
            },
            {data: "cotpAccName", bSortable: true},
            {data: "cotpBankAcc", bSortable: true},
            {data: "voucherTypeName", bSortable: true},
            {
                data: "isPayed", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.isPayed);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBillhead.status", full.status);
                }
            },
            {
                data: "voucherStat", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("bisExc.voucherStat", full.voucherStat);
                }
            },
            {data: "billFromName", bSortable: true, className: 'center'},
            {
                data: "relBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.relBillNo!="" && full.relBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelBill(this)">' + full.relBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if ($("#flowStatus").val() == 'todo') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/view/" + data.id
    });
}

/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/foreign/fexcashflow/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

function viewRelBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/viewByBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/viewByBillNo/" + data.relBillNo
    });
}