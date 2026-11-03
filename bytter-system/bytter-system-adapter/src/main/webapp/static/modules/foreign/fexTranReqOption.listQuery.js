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
            {data: "corpName", bSortable: true},
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
            {data: "buyEnglishCode", bSortable: true},
            {data: "sellEnglishCode", bSortable: true},
            {data: "cotpName", bSortable: true},
            {data: "createName", bSortable: false, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "expiresDate", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReqOption.status", full.status);
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