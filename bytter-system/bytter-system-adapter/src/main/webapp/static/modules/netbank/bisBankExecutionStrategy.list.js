bt.use(['form', 'datatable','dicts','selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTableDtl",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'asc'],[2,'asc'],[0,'asc']],
        searchForm: {
            formElem: "#searchFormDtl",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/strategy/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:receipt:bisBankExecutionStrategy:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    parent.layer.open({
                        type: 2,
                        area: ['850px', '550px'],
                        maxmin: true,
                        content: basePath + "/netbank/strategy/add",

                    });
                }
            },{
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "executeSort", bSortable: true, className: "center"},
            {data: "typeName", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("bank.strategy", full.type);
                }
            },
            {data: "bankTypeName", bSortable: true, className: "center"},
            {
                data: "rulesName", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    var str = "";
                    if(full.isExistId == "1"){
                        str = "table.bisBankExecution.unique";
                    }else{
                        str = full.rulesName;
                    }
                    var html = '<div class="break-row">' + bt.lang(str) + '</div>';
                    return html;
                }
            },
            {data: "isEnableName", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("fcCorpPool.status", full.isEnable);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var str = "";
                    var val= "1";
                    if(full.isEnable == "1"){
                        str = "common.button.taboo";
                        val = "0";
                    }else{
                        str = "common.button.enable";
                        val = "1";
                    }
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="updateIsEnable(this)">' + bt.lang(str) + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>'+ '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]

    });
});


function update(el) {
    var data = layui.jquery("#appTableDtl").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        console.log(data.id);
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/netbank/strategy/update/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function updateIsEnable(el) {
    var data = layui.jquery("#appTableDtl").DataTable().row(layui.jquery(el).data("row")).data();
    $.post(basePath + "/netbank/strategy/updateIsEnable/" + data.id, function (result) {
        bt.datatable.refreshCurrentPage();
        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
    }, 'JSON');
}

del = function (el) {
    var data = layui.jquery("#appTableDtl").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/netbank/strategy/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDelete(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/netbank/strategy/batchDelete/", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}