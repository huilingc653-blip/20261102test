var dict,moneyType,selectType,datatable;
var dtlId,billId,opType;

bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    dtlId = $('#dtlId').val();
    billId = $('#eqs_id').val();
    opType = $('#opType').val();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    selectType = bt.selectType.init();
    if (opType == "0" || opType == "1" || billId != "") {
        //生成列表
        datatable = bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            colReorder: true,
            checkbox: opType == "0" ? true : false,
            order: [[1, 'desc']],
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/foreign/fexcashflow/getDataTablesUnionForRefundCheck"
            },
            buttons: [
                {
                    hide: opType == "2"?true:false,
                    text: bt.lang('common.button.submit'),
                    className: 'submitButton',
                    action: function (e, dt, node, config) {
                        var data = dt.rows('.selected').data();
                        submit(data, '11');
                    }
                }, {
                    hide: opType == "2"?true:false,
                    text: bt.lang('common.button.temp'),
                    className: 'tempButton',
                    action: function (e, dt, node, config) {
                        var data = dt.rows('.selected').data();
                        submit(data, '0');
                    }
                }, {
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
                        if (full.tranBillNo != "" && full.tranBillNo != null) {
                            var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewTranReq(this)">' + full.tranBillNo + '</a>';
                            return html;
                        } else {
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
                        return full.settleNo;
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
                    data: "cotpName", bSortable: true,
                    render: function (data, type, full, meta) {
                        if (full.tranType == "3") {
                            return full.allocatCotpName;
                        } else {
                            return full.cotpName;
                        }
                    }
                },
                {
                    data: "cotpBankName", bSortable: true,
                    render: function (data, type, full, meta) {
                        if (full.tranType == "3") {
                            return full.allocatCotpBankName;
                        } else {
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
                    data: "payStatus", bSortable: true, className: 'center',
                    render: function (data, type, full, meta) {
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
            ]
        });
    }
});

//自动勾兑
function submit(data, opFlag) {
    bt.loading.show();
    var refundCheckHandle = dict.get('refundCheckHandle').val();
    var billIdCur = billId;
    if (opType == "0") {
        if (data.length == 0 || data.length > 1) {
            bt.loading.hide();
            parent.layer.alert(bt.lang('fexcashflow.message.confirm.selectData.only'), {icon: 7});
            return false;
        }
        billIdCur = data[0].id;
    }
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/foreign/refundCheck/manualSave",
            method: 'POST',
            data: {dtlId: dtlId,billId:billIdCur,refundCheckHandle: refundCheckHandle,opFlag:opFlag},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.closeCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }
        });
    });
}

//查看业务单据详情
function viewTranReq(el) {
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
