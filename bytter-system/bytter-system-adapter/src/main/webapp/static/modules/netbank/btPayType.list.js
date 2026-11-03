bt.use(['form','datatable'], function(){
    var $ = bt.jquery,form = bt.form();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        query: "search",
        checkbox:false,
        order: [[0,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/btpaytype/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"sysVoucherCode",bSortable:true,className:"center"},
            {data:"sysVoucherName",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"validSign",bSortable:true,className:"center",
                render: function ( data, type, full, meta ) {
                    return bt.dict("valid_sign",full.validSign);
                }
            }
        ]
    });
});