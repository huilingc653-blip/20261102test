var datatable,moneyFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

        //客商选择
        bt.bankinfoCallback = function (data, dom) {
//            $("#payerBankNo").val(data.bankCode);
//            $("#payerBankName").val(data.bankName);
        };


    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        clickRowSelected:false,
        order: [[1, 'asc'],[13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/accountReceivable/query/docOverdueNotice/datatable"
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
            {data: "code", bSortable: true},
            {data: "corpName", bSortable: true},
            /*{data: "batchBusTypeName", bSortable: true},*/
            {
                data: "voucherType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("account.receivable.doc.voucherType", full.voucherType);
                }
            },
            {data: "receiptDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "agingDate", bSortable: true},
            {data: "externalName", bSortable: true},
            {
                data: "externalType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            /*{
                data: "overdueRate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var opt = {
                        separator: ",",
                        decimal: 6
                    }
                    return moneyFun.format(full.overdueRate, opt);
                }
            },*/
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {
                data: "writeOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.writeOffBal);
                }
            },
            {data: "curName", bSortable: true},
            {
                data: "writeOffMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.writeOffMoney);
                }
            },
            {
                data: "unexpiredMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.unexpiredMoney);
                }
            },
            {
                data: "overdueMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.overdueMoney);
                }
            },
            //{data: "overdueReason", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('accountReceivable:docOverdueNotice:update')){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    if(bt.isResource('accountReceivable:docOverdueNotice:query')){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            groupColumn: 'curName',
            format: {
                decimal: 2
            },
             sumList: [
                 {
                     money: true,
                     colNum: '9',
                     dataName: 'receivableMoney'
                 },{
                     money: true,
                     colNum:'10',
                     dataName:'writeOffBal'
                 },{
                    money: true,
                    colNum:'12',
                    dataName:'writeOffMoney'
                 },{
                    money: true,
                    colNum:'13',
                    dataName:'unexpiredMoney'
                 },{
                    money: true,
                    colNum:'14',
                    dataName:'overdueMoney'
                 }
             ]
         }
    });
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

});

function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    /*if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/accountReceivable/query/docOverdueNotice/view?id=" + data.id);
        return;
    }*/
    parent.layer.open({
        type: 2,
        title: '编辑',
        area: ['1200px', '530px'],
        //area: ['auto', 'auto'],
        maxmin: true,
        content: basePath + "/accountReceivable/query/docOverdueNotice/edit?docId=" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    /*if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/accountReceivable/query/docOverdueNotice/view?id=" + data.id);
        return;
    }*/
    parent.layer.open({
        type: 2,
        area: ['1200px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/query/docOverdueNotice/view?docId=" + data.id
    });
}
