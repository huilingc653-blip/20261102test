var $, form, datatable;
bt.use(['form', 'datatable', 'dicts'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nissharebustype/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:basicdata:nissharebustype:apply'),
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
        columns: [
            {data: "shareBusType", bSortable: true, className: 'center'},
            {data: "shareBusTypeName", bSortable: false, className: 'center'},
            {data: "busTypeNameShow", bSortable: false, className: 'center'},
            {data: "statusName", bSortable: false, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('netbank:basicdata:nissharebustype:apply')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/netbank/nissharebustype/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            area: ['850px', '500px'],
            maxmin: true,
            content: basePath + "/netbank/nissharebustype/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/netbank/nissharebustype/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

