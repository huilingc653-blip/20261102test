bt.use(['form', 'datatable', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1,'desc']],
        ajax: {
            'url': basePath + "/bankacc/btbankaccfreeze/datatable"
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
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "accTypeName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAcc.accType", full.accType);
                }
            },
            {data: "bankName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "corpName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (bt.isResource('netbank:bankAcc:btBankAccFreeze:apply',full.corpId)) {
                    html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.freeze") + '</a>';
                }
                return html;
            }
            }
        ]
    });
    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccfreeze/add?bankAccId=" + data.id + "&curId=" + data.curId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankaccfreeze/add?bankAccId=" + data.id + "&curId=" + data.curId,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
});

