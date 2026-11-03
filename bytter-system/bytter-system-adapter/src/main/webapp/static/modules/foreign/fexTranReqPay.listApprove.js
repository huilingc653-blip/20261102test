var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp','mulselect', 'allExternalBankAcc'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();

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
            'url': basePath + "/foreign/fextranreq/dataTableApprove"
        },
        buttons: [
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "proName", bSortable: true},
            {
                data: "difBank", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBillhead.difBank", full.difBank);
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
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "bursarBillNo", bSortable: true},
            {
                data: "accNature", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReq.accNature", full.accNature);
                }
            },
            {data: "billFromName", bSortable: true, className: 'center'},
            {
                data: "relBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.relBillNo && full.relBillNo != ""){
                        var billNo = full.relBillNo.split(',');
                        for(var i=0 ; i<billNo.length ; i++){
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBillNo(\'' + billNo[i] + '\')">' + billNo[i] + '</a>';
                            if(i < billNo.length-1){
                                html += ","
                            }
                        }
                    }
                    return html;
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


function viewBillNo(billNo) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/viewBillNo/" + billNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/viewBillNo/" + billNo
    });
}