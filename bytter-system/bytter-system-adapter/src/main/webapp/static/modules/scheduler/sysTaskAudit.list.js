bt.use(['form','datatable','btdate'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/scheduler/systaskaudit/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"taskname",bSortable:true},
            {data:"startdtm",bSortable:true},
            {data:"enddtm",bSortable:true},
            {data:"arguments",bSortable:true},
            {data:"results",bSortable:true}
        ]
    });
    //时间
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
});