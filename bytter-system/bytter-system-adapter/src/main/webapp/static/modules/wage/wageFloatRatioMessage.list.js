var $, form, datatable, dicts, dateFun;
bt.use(['form', 'datatable', 'dicts', 'dateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dateType.init();
    dicts = bt.dicts.init();//字典
    dateFun = bt.dateType.getFun();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        ajax: {
            'url': basePath + "/wage/wageFloatRatio/datatableMessage"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [],
        columns: [
            // 月份
            {
                data: "month", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.months", full.month);
                }
            },
            // 操作时间
            {
                data: "enrolDate", bSortable: true, className: 'center',
                // render: function (data, type, full, meta) {
                //     return dateFun.format(data, 'yyyy-MM-dd HH:mm:ss')
                // }
            },
            // 操作人名称
            {data: "opName", bSortable: false, className: 'center'},
            // 操作类型
            {
                data: "messageType", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisMessage.messageType", full.messageType);
                }
            },
            // 操作记录
            {data: "message", bSortable: false, className: 'center'},
        ]
    });
});