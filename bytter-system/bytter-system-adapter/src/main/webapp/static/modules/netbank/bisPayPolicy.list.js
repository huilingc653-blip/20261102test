var $, form, datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'corp', 'selectType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.selectType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/bispaypolicy/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:netbank:bisPayPolicy:apply'),
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
            {data: "corpName", bSortable: false, className: 'center'},
            {data: "policyCode", bSortable: true, className: 'center'},
            {data: "policyName", bSortable: false, className: 'center'},
            {data: "payeeName", bSortable: false},
            {data: "busTypeName", bSortable: false},
            {data: "curName", bSortable: false, className: 'center'},
            {
                data: "validSign", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:netbank:bisPayPolicy:apply')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/netbank/bispaypolicy/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/netbank/bispaypolicy/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/netbank/bispaypolicy/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/bispaypolicy/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/netbank/bispaypolicy/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/netbank/bispaypolicy/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
