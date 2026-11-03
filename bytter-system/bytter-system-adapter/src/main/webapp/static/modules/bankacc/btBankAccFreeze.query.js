bt.use(['form', 'datatable', 'bank', 'corp', 'moneyType', 'dateType','dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.bank.init({
        elem: '.bank',
    });
    bt.moneyType.init();
    bt.dateType.init();
    bt.corp.init();
    bt.dicts.init();
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1,'desc']],
        ajax: {
            'url': basePath + "/bankacc/btbankaccfreeze/dataTableQuery"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "freezeNo", bSortable: true},
            {data: "ufFlag", bSortable: true, className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bankAccApp.ufFlag",full.ufFlag);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: false},
            {data: "curName", bSortable: false},
            {
                data: "freezeMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.freezeMoney);
                }
            },
            {data: "freezeDate", bSortable: true},
            {data: "status", bSortable: true, className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisExc.status",full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ]
    });
    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/bankacc/btbankaccfreeze/view/" + data.id;
        if(data.ufFlag == "1"){
            url = basePath + "/bankacc/btbankaccunfreeze/view/" + data.id;
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
});

