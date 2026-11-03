bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/budget/fbsBudgetProcessReport/dataTable"
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
        },
        columns: [
            {data: "period", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
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
        var url = basePath + "/budget/fbsBudgetProcessReport/seach";
        url += "?corpId=" + data.corpId + "&timeType=" + data.timeType;
        url += "&beginDate=" + data.beginDate + "&endDate=" + data.endDate + "&isInfo=true";
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}