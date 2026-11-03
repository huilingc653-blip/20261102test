bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp", "dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间
    bt.moneyType.init();
    corp = bt.corp.init();//单位

    var moneyFun = bt.moneyType.getFun();
    /**
     * 生成列表
     */
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData: "false"
        },
        ajax: {
            'url': basePath + "/cms/afterLease/buyBack/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data: "leaseNo", bSortable: true},
            {
                data: "leaseType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("cms.financing.lease.info.leaseType", full.leaseType);
                }
            },
            {data: "leaseCode", bSortable: true},
            {data: "payCode", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "lessor", bSortable: true},
            {data: "supplier", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "oriMoney", className: 'textRight', bSortable: false,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.oriMoney);
                }
            },
            {
                data: "rmbMoney", className: 'textRight', bSortable: false,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.rmbMoney);
                }
            },
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "sstatus", bSortable: true,
                render: function (data, type, full, meta) {
                    var sstatus = data;
                    if (full.repoStatus === null || '') {
                        sstatus = full.status
                    } else {
                        sstatus = full.repoStatus
                    }
                    return bt.dict("lease.status", sstatus);
                },
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    console.log(data)
                    var html = '';
                    if (data.repoStatus !== 11) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="buyback(this)">' + bt.lang("cms.lease.repo.buyback") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view");
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/cms/lease/financing/info/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/lease/financing/info/view/" + data.id
    });
}

function buyback(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    console.log(data)
    if (data.repoStatus === "" || data.repoStatus === null || data.repoStatus === undefined) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('cms.lease.repo.buyback'), basePath + "/cms/afterLease/buyBack/repoLease/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/afterLease/buyBack/repoLease/" + data.id,
            end: function () {
                location.reload();
            }
        });
    } else {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('cms.lease.repo.buyback'), basePath + "/cms/afterLease/buyBack/repoUpdate/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/afterLease/buyBack/repoUpdate/" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}
