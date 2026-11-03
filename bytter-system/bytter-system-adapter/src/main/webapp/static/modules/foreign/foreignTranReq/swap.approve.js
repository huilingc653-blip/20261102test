var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'moneyType','mulselect','allBankInfo'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.allBankInfo.init();

    bt.cotpBankCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#cotpName").val(data.bankName);
            } else if (data.type === "outerBank") {
                $("#cotpName").val(data.bankName);
            }
        } else {
            $("#cotpName").val("");
        }
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[4, 'desc'],[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexTradingReq/dataTableForeignApprove"
        },
        buttons: [
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "swapTypeName", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if ("1" == full.swapType) {
                        return bt.lang('fex.tranApp.near');
                    } else {
                        return bt.lang('fex.tranApp.remote');
                    }
                }
            },
            {data: "proName", bSortable: true},
            {
                data: "fexBusType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexProduct.fexBusType", full.fexBusType);
                }
            },
            {
                data: "fexTranType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexTranType", full.fexTranType);
                }
            },
            {
                data: "corpId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.corpName;
                }
            },
            {
                data: "buyCurId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.buyEnglishCode;
                }
            },
            {
                data: "buyMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.buyMoney);
                }
            },
            {
                data: "sellCurId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.sellEnglishCode;
                }
            },
            {
                data: "sellMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.sellMoney);
                }
            },
            {
                data: "contractRate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {

                    var opt = {
                        separator: ",",
                        decimal: 6
                    }
                    return moneyFun.format(full.contractRate, opt);
                }
            },
            {data: "cotpName", bSortable: true},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "accNature", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReq.accNature", full.accNature);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("RECONCILIATION_STATUS", full.status);
                }
            },
            {
                data: "billFrom", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.billFromName;
                }
            },
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
                    var html='';
                    if($("#flowStatus").val()=='todo'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
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
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingReq/view/swap/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingReq/view/swap/" + data.id
    });
}

function viewRelBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo
    });
}
