var datatable;
bt.use(['form', 'datatable', 'dateType','mulselect' , 'selectType', 'moneyType', 'dicts', 'bank', 'corp','date','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    renderTable();
    function renderTable(){
        //生成列表
        datatable = bt.datatable.init({
            elem: "#appTable",
            colReorder: true,
            checkbox: false,
            paging:false,
            ignoreButtons:['colvis','print','state'],// 忽略的表单默认按钮 state取消状态保存按钮
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search",
            },
            ajax: {
                'url': basePath + "/report/monthFundAllocationCollect/datatable"
            },
            columns: [
                {data: "supCorpName", bSortable: false},
                {data: "operationAreaName", bSortable: false},
                {data: "name", bSortable: false},
                {
                    data: "sumUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sumUpward);
                    }
                },
                {
                    data: "tongBiUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(bt.money.mul(full.tongBiUpward,100,0),{decimal:0})+'%';
                    }
                },
                {
                    data: "huanBiUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(bt.money.mul(full.huanBiUpward,100,0),{decimal:0})+'%';
                    }
                },
                {
                    data: "sumAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sumAllocation);
                    }
                },
                {
                    data: "tongBiAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(bt.money.mul(full.tongBiAllocation,100,0),{decimal:0})+'%';
                    }
                },
                {
                    data: "huanBiAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(bt.money.mul(full.huanBiAllocation,100,0),{decimal:0})+'%';
                    }
                },
                {
                    data: "retainedAmount", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.retainedAmount);
                    }
                },
                {
                    data: "retentionRate", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(bt.money.mul(full.retentionRate,100,0),{decimal:0})+'%';
                    }
                },
                {data: "isExcess", bSortable: false},
                {
                    data: "accountIncome", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.accountIncome);
                    }
                },
                {
                    data: "accountPayOut", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.accountPayOut);
                    }
                },
                {data: "remark", bSortable: false},
            ]
        });
    }

});
