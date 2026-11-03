var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        paging: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/report/enterpriseBankGoOnline/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"regionName",bSortable:false,className:"center",
                render: function (data, type, full, meta) {
                    if(full.level == 1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg_1");
                    }
                    if(full.level == 2){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg_2");
                    }
                    return full.regionName;
                }
            },
            {data: "operateAreaName", bSortable: false,className:"center"},
            {data: "cooperationBankNum", bSortable: false,className:"center"},
            {data: "accSumNum", bSortable: false,className:"center"},
            {data: "isNotOnlineEnterpriseBankNum", bSortable: false,className:"center"},
            {data: "isOnlineEnterpriseBankNum", bSortable: false,className:"center"},
            {data: "isNotOnlineProportion", bSortable: false,className:"center",
                render:function (data, type, full, meta) {
                    if (full.isNotOnlineProportion==0||full.isNotOnlineProportion==null){
                        return '';
                    }
                    return full.isNotOnlineProportion+"%";
                }},
            {data: "isOnlineProportion", bSortable: false,className:"center",
                render:function (data, type, full, meta) {
                    if (full.isNotOnlineProportion==0||full.isNotOnlineProportion==null){
                        return '';
                    }
                    return full.isOnlineProportion+"%";
            }}
        ]
    });
});