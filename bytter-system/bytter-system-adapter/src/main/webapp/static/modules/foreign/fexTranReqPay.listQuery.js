var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp','mulselect','moneyType', 'allExternalBankAcc'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();

    var allExternalAccCorp=bt.allExternalBankAcc.init();//客商账号组件初始化
    bt.cotpExternalAccBeforeClick = function () {
        allExternalAccCorp.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc,fps,payPal",
        });
        return true;
    }

    //交易对手银行账户
    bt.cotpExternalAccCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "outerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "corpBankAcc") {
                $("#oppId").val(data.corpId);
                $("#cotpName").val(data.corpName);
            } else if (data.type === "fps") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "payPal") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.ecName);
            }
        }
    }

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
            'url': basePath + "/foreign/fextranreq/datatable"
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
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "proName", bSortable: true},
            // {
            //     data: "difBank", bSortable: true, className: 'center',
            //     render: function (data, type, full, meta) {
            //
            //         return bt.dict("nisBillhead.difBank", full.difBank);
            //     }
            // },
            {
                data: "templateId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.templateName;
                }
            },
            {
                data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCorp.cdSign", full.cdSign);
                }
            },
            {
                data: "remitType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexForeignReq.remitType", full.remitType);
                }
            },
            {
                data: "isPayed", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.isPayed);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "corpBankName", bSortable: true},
            {data: "corpBankAcc", bSortable: true},
            {data: "corpAccName", bSortable: true},
            {data: "cotpName", bSortable: true},
            {data: "cotpBankName", bSortable: true},
            {data: "cotpBankAcc", bSortable: true},
            {data: "englishCode", bSortable: true},
            {data: "createName", bSortable: false, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            // {data: "bursarBillNo", bSortable: true},
            // {
            //     data: "accNature", bSortable: true, className: 'center',
            //     render: function (data, type, full, meta) {
            //
            //         return bt.dict("fexTranReq.accNature", full.accNature);
            //     }
            // },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReqPay.status", full.status);
                }
            },
            {
                data: "landingLimitResult", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            {
                data: "voucherStat", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.voucherStat) {
                        return bt.dict("bisExc.voucherStat", full.voucherStat);
                    }else {
                        if (full.cdSign == "0") {
                            return bt.dict("fexCashFlow.gatherStatus", full.payStatus);
                        } else {
                            return bt.dict("fexCashFlow.payStatus", full.payStatus);
                        }
                    }
                }
            },
            {data: "approveUser", bSortable: false, className: 'center'},
            {data: "approveTime", bSortable: false, className: 'center'},
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
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + data.id
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
