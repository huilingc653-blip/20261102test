parent.fcFreezeCheckList = this.window;
var fcFreezeCheckTable = {};
bt.use(['form', 'datatable', 'corp', 'selectType', 'money', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    fcFreezeCheckTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip2',
        checkbox: false,
        //order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcfreeze/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
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
                    if (bt.isResource('netbank:innerAccount:fcFreeze:apply',full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V",
    });
}

function check(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.check'), basePath + "/inneraccount/fcfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=C");
            return;
        }
    }
}
