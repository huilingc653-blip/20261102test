var billTable = {};
bt.use(['form', 'datatable', 'moneyType'], function () {

    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false, order: [[1, 'asc']],
        ignoreButtons: ['colvis', 'print', 'state', 'output'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillQuery/endorsementCountData"
        }
        , columns: [
            {data: "bussdt", bSortable: true, className: 'center'},
            {data: "reqNm", bSortable: true, className: 'center'},
            {data: "recNm", bSortable: true, className: 'center'},
            {data: "bankendrsmtmkName", bSortable: true, className: 'center'}
        ]
    });
});

