parent.fcUnFreezeCheckList = this.window;
var fcUnFreezeCheckTable = {};
bt.use(['form', 'datatable', 'corp', 'bank', 'selectType', 'money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    fcUnFreezeCheckTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip2',
        colReorder: true,
        checkbox: false,
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcUnfreeze/checkDataTable"
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
            {data: "freezeTypeName", bSortable: true},
            {
                data: "freezeMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.freezeMoney);
                }
            },
            {data: "stateName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcFreeze:unApply',full.corpId)) {
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
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcUnfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V",
    });
}

function check(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.check'), basePath + "/inneraccount/fcUnfreeze/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=C");
            return;
        }
    }
}
