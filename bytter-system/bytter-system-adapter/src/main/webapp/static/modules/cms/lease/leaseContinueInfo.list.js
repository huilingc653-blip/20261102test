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
            'url': basePath + "/cms/afterLease/continue/datatable"
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
            {data: "oriCurName", bSortable: true},
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
            {data: "continueCount", bSortable: true},
            {
                data: "status", bSortable: true, render: function (data, type, full, meta) {
                    if (full.continueVO === "" || full.continueVO === null || full.continueVO === undefined) {
                        return bt.dict("lease.status", full.status);
                    } else {
                        return bt.dict("lease.status", full.continueVO.status);
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.continueVO === null || full.continueVO === "" || full.continueVO.status != 11) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cont(this)">' + bt.lang("table.cmsLease.continue") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="log(this)">' + bt.lang("common.button.logInfo") + '</a>';
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

function cont(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var statu;
    if (data.continueVO === "" || data.continueVO === null || data.continueVO === undefined) {
        statu = "";
    } else {
        statu = data.continueVO.status;
    }
    if (statu === "" || statu === null || statu === undefined || statu === 95) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.cmsLease.continue'), basePath + "/cms/afterLease/continue/contLease/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/afterLease/continue/contLease/" + data.id,
            end: function () {
                location.reload();
            }
        });
    } else {//暂存或者打回
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.cmsLease.continue'), basePath + "/cms/afterLease/continue/continueUpdate/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/afterLease/continue/continueUpdate/" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}

//日志列表
function log(en) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(en).data("row")).data();
    console.log(data)
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/cms/afterLease/continue/viewlistMsg/" + data.id);
            return;
        }
    }
}
