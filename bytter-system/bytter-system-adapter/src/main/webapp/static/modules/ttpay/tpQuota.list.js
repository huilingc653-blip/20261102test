bt.use(['form', 'datatable', 'region', 'corp', 'btImport', 'dicts', 'moneyType', 'dateType', 'select', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.region.init();
    bt.corp.init();
    bt.dicts.init();//字典
    date = bt.dateType.init();//时间
    moneyType = bt.moneyType.init();//金额


    //生成列表-加载数据
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox: true,
        ajax: {
            'url': basePath + "/ttpay/quota/datatable"
        },
        selectCss: false,
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }

            // 批量设置有效无效
           /* , {
                text: bt.lang('common.button.batchValidSignYes'),
                hide: bt.isnotResource('ttpay:quota:op'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchValidSign(data, "1");
                }
            }, {
                text: bt.lang('common.button.batchValidSignNo'),
                hide: bt.isnotResource('ttpay:quota:op'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchValidSign(data, "0");
                }
            }*/
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "merchantNo", bSortable: true},
            {data: "corpName", bSortable: false},
            {
                data: "payWay", className: 'center', bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("TTPAY_PAY_TYPE", full.payWay);
                }
            },
            {data: "effectDate", bSortable: true, className: "center"},
            {
                data: "daySingle", className: "textRight",
                render: function (data, type, full, meta) {
                    return full.daySingle ? bt.money.format(full.daySingle) : "";
                }
            },
            {
                data: "dayTotal", className: "textRight",
                render: function (data, type, full, meta) {
                    return full.dayTotal ? bt.money.format(full.dayTotal) : "";
                }
            },
            {
                data: "monthTotal", className: "textRight",
                render: function (data, type, full, meta) {
                    return full.monthTotal ? bt.money.format(full.monthTotal) : "";
                }
            },
            {
                data: "validSign", className: "center", bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">&nbsp;</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">&nbsp;</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">&nbsp;</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="log(this)">' + bt.lang("common.button.logInfo") + '</a><span class="text-explode">&nbsp;</span>';
                    return html;
                }
            }
        ]
    });
});

//新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/ttpay/quota/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/quota/add",
        end: function () {
            table && table.draw();
        }
    });
}

//批量设置是否有效标志， 0 -无效， 1 -有效
function batchValidSign(data, validSign) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    if (validSign == 1) {
        parent.layer.confirm(bt.lang('common.message.confirm.batchValidSignYes'), function () {
            $.post(basePath + "/ttpay/quota/updateValidSign", {ids: ids, validSign: '1'}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
    if (validSign == 0) {
        parent.layer.confirm(bt.lang('common.message.confirm.batchValidSignNo'), function () {
            $.post(basePath + "/ttpay/quota/updateValidSign", {ids: ids, validSign: '0'}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}

//查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    parent.layer.open({
        type: 2,
        area: ['1000px', '700px'],
        maxmin: true,
        content: basePath + "/ttpay/quota/view/" + data.id

    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/ttpay/quota/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ttpay/quota/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//删除
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/ttpay/quota/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//日志列表
function log(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/ttpay/tpLog/listMsgData?quoteId=" + data.id);
            return;
        }
    }
}
