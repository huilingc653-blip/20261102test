parent.fcUnFreezeList = this.window;
var fcUnFreezeTable = {};
bt.use(['form', 'datatable', 'corp', 'selectType', 'money', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    fcUnFreezeTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip2',
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcUnfreeze/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:innerAccount:fcFreeze:unApply'),
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
            {data: "blnctrAcc", bSortable: true},
            {data: "accName", bSortable: false},
            {data: "netName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "freezeTypeName", bSortable: true},
            {
                data: "unFreezeMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.unFreezeMoney);
                }
            },
            {data: "stateName"},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcFreeze:unApply',full.corpId)) {
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
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/inneraccount/fcUnfreeze/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcUnfreeze/add",
        end: function () {
            fcUnFreezeTable && fcUnFreezeTable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcUnfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V",
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/inneraccount/fcUnfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=M");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/fcUnfreeze/updateOrView?id=" + data.id,
            end: function () {
                fcUnFreezeTable && fcUnFreezeTable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/inneraccount/fcUnfreeze/delete/" + data.id, function (result) {
            // fcUnFreezeTable.draw();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}