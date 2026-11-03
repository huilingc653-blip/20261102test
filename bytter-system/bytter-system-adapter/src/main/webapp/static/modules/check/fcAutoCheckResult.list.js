bt.use(["jquery", "datatable", "form", 'corp', "selectType", "dicts","dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    corp = bt.corp.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间


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
            isInitData:"false"
        },
        ajax: {
            'url': basePath + "/finacial/check/fcautocheckconfig/resultDatatable"
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
            {data:"runTime",className:"center",render:function(data, type, full, meta){
                    return bt.date.format(full.runTime, "yyyy-MM-dd hh:mm:ss");
                }},
            {data:"launchTime",className:"center",render:function(data, type, full, meta){
                    return bt.date.format(full.launchTime, "yyyy-MM-dd hh:mm:ss");
                }},
            {data: "corpName",className:'center', bSortable: true},
            {data: "bankAcc",className:'center', bSortable: true},
            {data: "bankAccName",className:'center', bSortable: true},
            {
                data: "checkType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAutoCheckConfig.checkType", full.checkType);
                }
            },
            {
                data: "checkCycle", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAutoCheckConfig.checkCycle", full.checkCycle);
                }
            },
            {data: "autoCheckCode",className:'center', bSortable: true},
            {data: "autoCheckName",className:'center', bSortable: true},
            {
                data: "isSuccess", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAutoCheckConfig.result", full.isSuccess);
                }
            }
        ]
    });
});




