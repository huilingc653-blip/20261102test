bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankaccrev/datatable"
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
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "isDomesticBankName",bSortable:true},
            {data: "accTypeName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankName", bSortable: true},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("netbank.statusAll", full.status);
                }
            },
            {data: "provName", bSortable: true},
            {data: "cityName", bSortable: true},
            {data: "electricBillName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (bt.isResource('netbank:bankAcc:btBankAccRev:apply',full.corpId)) {
                        if (data.isForm == "acc") {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.change") + '</a>';
                        } else {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                            html += '<span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                            html += '<span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                        }
                    }
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrev/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccrev/update?id=" + data.id + "&&isForm=" + data.isForm);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrev/update?id=" + data.id + "&&isForm=" + data.isForm,
        end: function () {
            location.reload();
        }
    });
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/btbankaccrev/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}