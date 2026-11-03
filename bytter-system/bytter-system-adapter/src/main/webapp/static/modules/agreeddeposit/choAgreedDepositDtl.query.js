bt.use(['form', 'datatable', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[7, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/agreeddeposit/choinneragreeddeposit/datatablequerydtl"
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
        }
        , columns: [
        {data: "billNo", bSortable: false, className: 'center'},
        {data: "busType", bSortable: false, className: 'center',
            render:function (data, type, full, meta) {
                return bt.dict("agreed.choBusType", full.busType);
            }
        },
        {data: "beginDate", bSortable: false, className: 'center'},
        {data: "oldEndDate", bSortable: false, className: 'center'},
        {data: "endDate", bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = "-";
                if(full.endDate != null){
                    html = full.endDate;
                }
                return html;
            }
        },
        {
            data: "certificateWay", bSortable: false,className: 'center',
            render: function (data, type, full, meta) {
                return bt.dict("agreed.certificateWay", full.certificateWay);
            }
        },
            {data: "status", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.status", full.status);
                }
            },

            {data: "createDate", bSortable: false, className: 'center'},
            {data: "createBy", bSortable: false, className: 'center'}
    ]
    });

});
