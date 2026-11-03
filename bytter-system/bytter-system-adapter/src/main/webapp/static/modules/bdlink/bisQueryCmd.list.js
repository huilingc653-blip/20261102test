bt.use(['form', 'datatable', 'selectType', 'dateType', 'dicts', 'bank','corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.corp.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bdlink/bisquerycmd/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [

            {data: "voucherNo", bSortable: true, className: 'center'},
            {data: "bifName", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "transName", bSortable: true, className: 'center'},
            {data: "startTime", bSortable: true, className: 'center'},
            {data: "endTime", bSortable: true, className: 'center'},
            {data: "sendTime", bSortable: true, className: 'center'},
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true, className: 'center'},
            {data: "nextReturnMsg", bSortable: true, className: 'center'},
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "cmdStatName", bSortable: true, className: 'center'}
        ],
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.cmdStat == "9" || aData.cmdStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
});