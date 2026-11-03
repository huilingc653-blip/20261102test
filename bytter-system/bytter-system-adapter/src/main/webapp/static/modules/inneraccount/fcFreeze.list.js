var fcFreezeTable = {};
bt.use(['form', 'datatable', 'corp', 'selectType', 'money', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    fcFreezeTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip2',
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcfreeze/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:innerAccount:fcFreeze:apply'),
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
            {data: "freezeType", bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("fcFreeze.freezeType",full.freezeType);
                }
            },
            {data: "beginDate", bSortable: false},
            {data: "endDate", bSortable: false},
            {
                data: "freezeMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.freezeMoney);
                }
            },
            {
                data: "accuFreezeMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accuFreezeMoney);
                }
            },
            {data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("fcfreeze.status",full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    // if(bt.isResource('netbank:innerAccount:fcFreeze:apply',full.corpId)){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    //}
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/inneraccount/fcfreeze/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcfreeze/add",
        end: function () {
            fcFreezeTable && fcFreezeTable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V");
        return;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id+"&blnctrAcc="+data.blnctrAcc+"&opType=V",
    // });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=M");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id,
            end: function () {
                fcFreezeTable && fcFreezeTable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/inneraccount/fcfreeze/delete/" + data.id, function (result) {
            // fcFreezeTable && fcFreezeTable.draw();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
