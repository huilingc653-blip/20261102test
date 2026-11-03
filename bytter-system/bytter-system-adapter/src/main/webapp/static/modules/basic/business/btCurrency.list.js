bt.use(['form', 'datatable', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/btcurrency/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            // {
            //     data: "naturalSign", bSortable: true, className: "center",
            //     render: function (data, type, full, meta) {
            //         return bt.dict("sf_yn", full.naturalSign);
            //     }
            // },
            {data: "curCode", bSortable: true, className: "center"},
            {data: "curName", bSortable: true, className: "center"},
            {data: "englishCode", bSortable: true, className: "center"},
            {data: "pbScaleCode", bSortable: true, className: "center"},
            {
                data: "curExrate", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var opt = {
                        separator: ",",
                        decimal: 4
                    }
                    return moneyFun.format(full.curExrate, opt);
                }
            },
            {data: "curSign", bSortable: true, className: "center"},
            {data: "rmk", bSortable: true},
            {
                data: "validSign", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>\
	               <a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.btCurrency.btn.create'), basePath + "/basic/business/btcurrency/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btcurrency/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btcurrency/view/" + data.id
    });
}

function update(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.btCurrency.btn.update'), basePath + "/basic/business/btcurrency/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/btcurrency/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/btcurrency/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
} 