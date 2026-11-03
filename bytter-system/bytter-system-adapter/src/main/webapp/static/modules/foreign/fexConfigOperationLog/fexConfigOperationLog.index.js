var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    dataTypes = bt.dateType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexTranConfigOperationLog/datatable"
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
            {data: "operationPageName",className: "center"},
            {data: "operationContent",className: "center",bSortable: false,
                render : function (record, rowindex, value, column) {
                    var operationContent="";
                 value.operationContent?operationContent=value.operationContent:operationContent;
                    return '<div title="'+operationContent+'">'+operationContent+'</div>'
                }},
            {data: "createName",className: "center"},
            {data: "createDateStr",className: "center"},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">'  + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});




function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTranConfigOperationLog/view/" + data.id
    });
}





