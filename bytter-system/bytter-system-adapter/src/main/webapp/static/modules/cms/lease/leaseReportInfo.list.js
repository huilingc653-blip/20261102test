bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp", "dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间
    moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();//单位

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
            'url': basePath + "/cms/report/cmsLease/datatable"
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
            {
                data: "leaseType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("cms.financing.lease.info.leaseType", full.leaseType);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "lessor", bSortable: true},
            {data: "supplier", bSortable: true},
            {data: "leaseCode", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "oriCurName", bSortable: true},
            {data:"oriMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.oriMoney);
                }
            },
            {data:"rmbMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.rmbMoney);
                }
            },
            {data:"allThisMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.allThisMoney);
                }
            },
            {data:"allMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.allMoney);
                }
            },
            {data:"allInter",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.allInter);
                }
            },
            {data:"allCost",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.allCost);
                }
            },
            {data:"notThisMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.notThisMoney);
                }
            },
            {data:"notallMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.notallMoney);
                }
            },
            {data:"notallInter",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.notallInter);
                }
            },
            {data:"notallCost",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.notallCost);
                }
            },
            {
                data: "status", bSortable: true, render: function (data, type, full, meta) {
                    return bt.dict("lease.status", data);
                },
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("cms.button.detail");
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/cms/report/cmsLease/view/" + data.leaseId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/report/cmsLease/view/" + data.id
    });
}
