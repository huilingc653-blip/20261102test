bt.use(['form', 'datatable', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        // order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/syscountrycust/datatable"
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
            {data: "nameZh", bSortable: true, className: "center"},
            {data: "nameEn", bSortable: true, className: "center"},
            {data: "englishCode", bSortable: true, className: "center"},
            {data: "enCode", bSortable: true, className: "center"},
            {data: "timeZone", bSortable: true, className: "center"},
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
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/basic/business/syscountrycust/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/syscountrycust/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/syscountrycust/view/" + data.id
    });
}

function update(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/syscountrycust/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/syscountrycust/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/syscountrycust/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
} 