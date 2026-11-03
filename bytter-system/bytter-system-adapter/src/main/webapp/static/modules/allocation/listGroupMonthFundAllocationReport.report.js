var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank', 'corp','date'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
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
                'url': basePath + "/report/groupMonthFundaAllocation/datatable"
            },
            columns: [
                {data: "equityName", bSortable: false},
                {
                    data: "sumUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sumUpward);
                    }
                },
                {
                    data: "huanBiUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.huanBiUpward) + "%";
                    }
                },
                {
                    data: "tongBiUpward", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.tongBiUpward) + "%";
                    }
                },
                {
                    data: "sumAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sumAllocation);
                    }
                },
                {
                    data: "huanBiAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.huanBiAllocation) + "%";
                    }
                },
                {
                    data: "tongBiAllocation", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.tongBiAllocation) + "%";
                    }
                },
                {
                    data: "retainedAmount", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.retainedAmount);
                    }
                },
                {
                    data: "huanBiRetained", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.huanBiRetained) + "%";
                    }
                },
                {
                    data: "tongBiRetained", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.tongBiRetained) + "%";
                    }
                },
                {
                    data: "retentionRate", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.retentionRate) + "%";
                    }
                },
            ]
        });
    }

});