var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

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
            {data:"proId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.proName;
                }
            },
            {data:"corpId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.corpName;
                }
            },
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "expiresDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {data: "positionMoney", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.positionMoney);
                }
            },
            {data: "contractRate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.contractRate,{decimal:6});
                }
            },
            {data: "tranCurPairIdName", bSortable: true, className: 'center'},
            {
                data: "busDirection", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranDeal.deliverySign", full.busDirection);
                }
            },
            {
                data: "updownDirection", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fex.tranApp2.updownDirection", full.updownDirection);
                }
            },
            {data: "c", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexProduct.deliveryType", full.deliveryType);
                }},
            {
                data: "fexOption1", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexOption1", full.fexOption1);
                }
            },
            {
                data: "fexOptionType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexOptionType", full.fexOptionType);
                }
            },
            {data: "cotpName", bSortable: false},
            {data: "createDate", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexTranReqOption.status", full.status);
                }
            },
            {
                data: "blendSign", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexTranReq.blendSign", full.blendSign);
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
