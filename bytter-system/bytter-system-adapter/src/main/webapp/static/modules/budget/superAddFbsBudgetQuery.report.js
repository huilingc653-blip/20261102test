bt.use(['form','corp','dateType','selectType','dicts','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    var dateType = bt.dateType.init();
    var selectType = bt.selectType.init();
    var dicts = bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        isInitData:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox:false,
        order: [[2,'desc']],
        ajax: {
            'url': basePath+"/budget/fbsBudgetSuperAddReport/dataTable"
        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true,className:'center'},
            {data:"beginDate",bSortable:true,className:'center'},
            {data:"endDate",bSortable:true,className:'center'},
            {data:"statusStr",bSortable:true,className:'center'},
            {data:"timeTypeName",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {data:"addCount",bSortable:true,className:'center'},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        var url = basePath + "/budget/fbsBudgetSuperAddReport/view/"+data.budgetId;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.view'), url);
            return;
        }
    }
}
