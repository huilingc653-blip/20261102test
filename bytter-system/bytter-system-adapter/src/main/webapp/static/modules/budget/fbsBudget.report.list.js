bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/budget/fbsbudgetreport/dataTableReport"
        },
        buttonsLayout:false,
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "beginDate", bSortable: true, className: 'center'},
            {data: "endDate", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("fbsBudget.status", full.status);
                }
            },
            {data: "timeType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fbs.timeType", full.timeType);
                }
            },
            {data: "curName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(\'' + full.id + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

//查看
function view(id){
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/view/" + id+ "/report");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/budget/fbsbudget/view/" + id+ "/report"
    });
}