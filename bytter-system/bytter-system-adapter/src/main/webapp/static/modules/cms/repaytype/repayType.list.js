var datatable;
bt.use(['form', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/cms/repay/repaytype/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "repayCode", bSortable: true, className: "center"},
            {data: "repayName", bSortable: true},
            {data: "createDate", bSortable: true, className: "center", visible: false},
            {data: "rmk", bSortable: false},
            {
                data: "validSign", bSortable: false, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var  html = '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    if (full.validSign == "1") {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updateValidSign(\'' + full.id + '\',0)">' + bt.lang("common.button.taboo") + '</a>';
                    } else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updateValidSign(\'' + full.id + '\',1)">' + bt.lang("common.button.enable") + '</a>';
                    }
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });

    function add() {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/repay/repaytype/add");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/repay/repaytype/add",
            end: function () {
                location.reload();
            }
        });
    }

    clone = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.button.clone'), basePath + "/cms/repay/repaytype/update?id=" + data.id+ "&type=clone");
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/cms/repay/repaytype/update?id=" + data.id,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }

    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/repay/repaytype/view/" + data.id
        });
    }

    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/repay/repaytype/update?id=" + data.id);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/cms/repay/repaytype/update?id=" + data.id,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }

    del = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/cms/repay/repaytype/delete/" + data.id, function (result) {
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
            $.post(basePath + "/cms/repay/repaytype/batchDelete/", {ids: ids}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
});

/**
 * 启动/禁用
 * @param id 主键id
 * @param validSign 启用/禁用状态
 */
function updateValidSign(id,validSign) {
    bt.loading.show();
    $.post(basePath + "/cms/repay/repaytype/updateValidSign", {id: id, validSign: validSign}, function (result) {
        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        datatable && datatable.draw();
        bt.loading.hide();
    }, 'JSON');
}
