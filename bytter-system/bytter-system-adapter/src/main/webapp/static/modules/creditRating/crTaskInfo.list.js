var datatable;
bt.use(['form', 'datatable', 'dateType', 'corp', 'mulselect', 'dicts'], function () {
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
        checkbox: false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/credit/crTaskInfo/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('credit:crTaskInfo:edit'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ], columns: [
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
                    var html = ' <a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a>';
                    if (data.status == 0 || data.status == -1) {
                        html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a>';
                        html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    html += ' <a class="" href="#" data-row="' + meta.row + '" onclick="toLog(this)">' + bt.lang("common.button.logInfo") + '</a>';
                    return html;
                }
            }
        ]
    });

});

//新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.crTaskInfo.tab.add'), basePath + "/credit/crTaskInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskInfo/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}


//编辑
function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.crTaskInfo.tab.edit'), basePath + "/credit/crTaskInfo/edit?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskInfo/edit?id=" + data.id,
        end: function () {
            datatable && datatable.draw();
        }
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

//日志
function toLog(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('bill.logInfo.title'), basePath + "/credit/evaluationLog/viewLog/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/evaluationLog/viewLog/" + data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//删除
function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/credit/crTaskInfo/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//编辑
function clone(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.clone'), basePath + "/credit/crTaskInfo/taskInfoClone?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskInfo/edit?id=" + data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}



