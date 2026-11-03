var datatable;
bt.use(['form', 'datatable', 'dateType', 'corp', 'mulselect', 'dicts', 'moneyType', 'btdate', 'select', 'btImport', 'dicts', 'selectTree'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.corp.init();//单位
    bt.dicts.init();//数据字典
    bt.mulselect.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        query: "search",
        checkbox: true,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/credit/crTaskInfo/getApproveData"
        },
        ignoreButtons: ['colvis', 'print', 'state', 'output'],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "taskNo", bSortable: true, className: "center"},
            {data: "taskName", bSortable: false, className: "center"},
            {data: "corpName", bSortable: false, className: "center",render:function (data, type, full, meta) {
                    return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
                }},
            {data: "orgType", bSortable: false, className: "center"},
            {data: "orgName", bSortable: false, className: "center",render:function (data, type, full, meta){
                    return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
                }},
            {data: "templateName", bSortable: false, className: "center"},
            {
                data: "evaluateStartDate",
                bSortable: false,
                className: "center",
                render: function (data, type, full, meta) {
                    if (full.evaluateStartDate) {
                        return bt.date.format(full.evaluateStartDate, "yyyy-MM-dd");
                    }
                    return "";
                }
            },
            {
                data: "evaluateEndDate",
                bSortable: false,
                className: "center",
                render: function (data, type, full, meta) {
                    if (full.evaluateEndDate) {
                        return bt.date.format(full.evaluateEndDate, "yyyy-MM-dd");
                    }
                    return "";
                }
            },
            {data: "creator", bSortable: false, className: "center"},
            {
                data: "createDate", bSortable: true, className: "center", render: function (data, type, full, meta) {
                    if (full.createDate) {
                        return bt.date.format(full.createDate, "yyyy-MM-dd");
                    }
                    return "";
                }
            },
            {
                data: "status", bSortable: false, className: "center", render: function (data, type, full, meta) {
                    return bt.dict("crTaskInfo.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: "center", render: function (data, type, full, meta) {
                    var html = '';
                    if ($("#flowStatus").val() == 'todo') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }
                    if ($("#flowStatus").val() == 'pass') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    } else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

});

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/credit/crTaskInfo/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskInfo/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

//查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/credit/crTaskInfo/view?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskInfo/view?id=" + data.id
    });
}



