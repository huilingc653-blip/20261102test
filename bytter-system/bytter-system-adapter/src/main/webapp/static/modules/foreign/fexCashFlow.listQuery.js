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
        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexcashflow/datatable"
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
            {
                data: "tranBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.tranBillNo!="" && full.tranBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.tranBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {data: "bursarBillNo", bSortable: true, className: 'center'},
            {
                data: "settleType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCashFlow.settleType", full.settleType);
                }
            },
            {data: "proName", bSortable: true},
            {
                data: "settleNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.settleType=="2") {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewMerge(this)">' + full.settleNo + '</a>';
                        return html;
                    }else{
                        return "";
                    }
                }
            },
            {data: "cashFlowDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "cashFlowType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCorp.cashFlowType", full.cashFlowType);
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
                data: "cotpName", bSortable: false,
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
                data: "landingLimitResult", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            {
                data: "payStatus", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.payStatus == "-1") {
                        return bt.dict("fexCashFlow.payStatus", full.payStatus);;
                    } else {
                        if (full.voucherStat) {
                            return full.voucherStatName;
                        } else {
                            if (full.cdSign == "0") {
                                return bt.dict("fexCashFlow.gatherStatus", full.payStatus);
                            } else {
                                return bt.dict("fexCashFlow.payStatus", full.payStatus);
                            }
                        }
                    }
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
                data: "blendSign", bSortable: false, className: 'center',render: function (data, type, full, meta) {
                    if (full.bankFlowSign == "1" || full.blendSign == "1") {
                        return  bt.dict("fexTranReq.blendSign","1");
                    } else {
                        return  bt.dict("fexTranReq.blendSign","0")
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
});
//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + data.tranBillId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + data.id
    });
}

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

function viewMerge(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/viewMerge/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/viewMerge/" + data.id
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
