bt.use(['form','datatable','select'], function(){
    $ = bt.jquery,form = bt.form();
    var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
    var ids = top.frames[currentParentIframe].window.$("#bindingBankAccHid").val();
    $("#id").val(ids);
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bankacc/btbankacc/bindingBankAccDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"bankAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"bankTypeName",bSortable:true},
            {data:"bankName",bSortable:true}
        ]
    });
});


