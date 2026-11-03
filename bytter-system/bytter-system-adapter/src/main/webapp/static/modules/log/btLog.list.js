bt.use(['form','datatable','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[5,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/log/operate/datatable"
        },
        buttons: [{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"logcontent",bSortable:false},
            {data:"broswer",bSortable:false},
            {data:"note",bSortable:true},
            {data:"username",bSortable:true},
            {data:"operatetime",bSortable:true}
        ]
    });
});