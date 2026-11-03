var moneyType;
var rateType;
var selectType, dicts, corp, dateType;
bt.use(['form', 'corp', 'element', 'btdate', 'select', 'selectType', 'datatable', 'dicts', 'dateType', 'moneyType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.getFun();
    rateType = bt.rateType.getFun();
    dataTableInit();
});

function dataTableInit() {
    //生成列表
    fcAccTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/finacial/interest/single/loan/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('finacial:interest:loan:hand'),
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
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "account", bSortable: true, className: 'center'},
            {data: "accName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true, className: 'center'},
            {
                data: "payWay", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                    return bt.dict("cms.repayType", data);
                }
            },
            {data: "endDate", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                    var endDate = bt.date.format(bt.date.addDay(data, 1));
                    return endDate;
                }
            },
            {
                data: "money", bSortable: true, className: 'textRight', render: function (data, type, full, meta) {
                    if (data){
                        return bt.money.format(data);
                    }
                    return '';
                }
            },
            {
                data: "status", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                    return bt.dict("interest.busStatus", data);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('finacial:interest:loan:hand', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;

                }
            }
        ]
    });

}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/finacial/interest/single/loan/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/single/loan/add",
        end: function () {
            location.reload();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/finacial/interest/single/loan/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/single/loan/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/finacial/interest/single/loan/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/finacial/interest/single/loan/update?id=" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/finacial/interest/single/loan/delete", {
            "id": data.id,
            "account": data.account,
            "netCode": data.netId,
            "accTypeCode": data.accType
        }, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
