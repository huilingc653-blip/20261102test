bt.use(['form', 'datatable', 'dateType', 'moneyType', 'btdate', 'corp', 'dicts', 'moneyType', 'btImport','rateType'], function () {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    bt.rateType.init();
    rateType=bt.rateType.getFun();
    bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    //生成列表3
    var idds = $("#leaseId").val();
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1, 'desc'],
        ajax: {
            'url': basePath + "/cms/report/cmsLease/detailDatatable?leaseId=" + idds
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "times", className: 'center', bSortable: true},
            {data: "payDate", className: 'center', bSortable: true,
                render: function (data,type, full) {
                    return  bt.date.format(data, "yyyy-MM-dd");
                }
            },
            {data: "planDate", className: 'center', bSortable: true,
                render: function (data,type, full) {
                    return bt.date.format(data, "yyyy-MM-dd");
                }
            },
            {
                data: "thisMoney", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.thisMoney);
                }
            },
            {
                data: "leaseMoney", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.leaseMoney);
                }
            },
            {
                data: "leaseInterest", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.leaseInterest);
                }
            },
            {
                data: "thisCost", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.thisCost);
                }
            },
            {
                data: "surplusMoney", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.surplusMoney);
                }
            },
            {data: "pointProText", className: 'center', bSortable: true},
            {data:"isRepay",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("yn_state",full.isRepay);
                }
            },
            {data: "rate", className: 'center', bSortable: true,
                render: function (data, type, full, metd) {
                    return rateType.format(full.rate);
                }},
            {data: "dates", className: 'center', bSortable: true},
            {
                data: "bailMoney", className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.bailMoney);
                }
            }
        ],
    });
});
