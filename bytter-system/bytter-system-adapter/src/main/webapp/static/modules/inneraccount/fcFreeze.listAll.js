bt.use(['form', 'datatable', 'corp', 'selectType', 'money', 'bank', 'date', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip2',
        checkbox: true,
        order: [[14, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcfreeze/dataTableAll"
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
            {data: "accName", bSortable: true},
            {data: "netName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "freezeNo", bSortable: true},
            {data: "oldFreezeNo", bSortable: true},
            {data: "freezeSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("innerAccount.freezeSign",full.freezeSign);
                }
            },
            {data: "freezeType", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("fcFreeze.freezeType",full.freezeType);
                }
            },
            {
                data: "freezeMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.freezeMoney);
                }
            },
            {
                data: "accuFreezeMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accuFreezeMoney);
                }
            },
            {data: "createBy", bSortable: false},
            {data: "checkerCode", bSortable: false},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "operateTime", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.date.format(full.operateTime, "yyyy-mm-dd hh:mm:ss")
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
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
    $(document).ready(function () {
        predrawSignShow();
    });

    //业务状态
    function predrawSignShow() {
        var status = $("select[name='status']");
        var gc = 'common.status';
        status.empty();
        status.append("<option value=''>" + bt.lang("common.placeholder.all") + "</option>");
        status.append("<option value='0'>" + bt.dict(gc, '0') + "</option>");
        status.append("<option value='-1'>" + bt.dict(gc, '-1') + "</option>");
        status.append("<option value='11'>" + bt.dict(gc, '11') + "</option>");
        status.append("<option value='95'>" + bt.dict(gc, '95') + "</option>");
    }
});


function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        var viewUrl = "fcfreeze";
        if (data.freezeSign == 'T') {
            viewUrl = "fcUnfreeze";
        }

        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/" + viewUrl + "/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/" + viewUrl + "/updateOrView?id=" + data.id + "&blnctrAcc=" + data.blnctrAcc + "&opType=V",
        });
    }
}
