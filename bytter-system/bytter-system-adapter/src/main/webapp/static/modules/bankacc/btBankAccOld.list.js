bt.use(['form', 'datatable', 'select', 'corp', 'bank', 'dicts','mulselect','selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.dicts.init();
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    afterInitCorpComplete = function (){
        initDatatable();
    }
    bt.corp.init();
    function initDatatable() {
        //生成列表
        bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            colReorder: true,
            checkbox: true,
            order: [1, 'desc'],
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/bankacc/btbankacc/dataTableOldBankAcc"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "bankAcc", bSortable: true},
                {data: "accName", bSortable: true},
                {data: "corpName", bSortable: true},
                {data: "curName", bSortable: false},
                {data: "accTypeName", bSortable: true},
                {data: "attributeName", bSortable: true},
                {data: "natureName", bSortable: true},
                {data: "bankTypeName", bSortable: true},
                {data: "bankName", bSortable: true},
                {data: "regDate", bSortable: true},
                // {data: "endDate", bSortable: true},
            ]
        });
    }
});
