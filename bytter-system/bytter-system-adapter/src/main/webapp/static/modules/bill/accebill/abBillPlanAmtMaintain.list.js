var $, form, datatable;
bt.use(['form', 'datatable', 'corp', 'mulselect', 'btImport', 'dicts', 'moneyType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        ajax: {
            'url': basePath + "/bill/accebill/planAmtMaintain/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:planamt:maintain:apply'),//应用权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    addPlanAmt();
                }
            },
            {
                hide: bt.isnotResource('bill:accebill:planamt:maintain:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importPlanAmt();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "bankTypeName", bSortable: false, className: 'center'},
            {
                data: "creditType", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bill:accebill.creditType", full.creditType);
                }
            },
            {data: "weekStartDay", bSortable: true, className: 'center'},
            {data: "weekEndDay", bSortable: true, className: 'center'},
            {
                data: "currWeekBillMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.currWeekBillMoney, {separator: ",", decimal: 2});
                }
            },
            {
                data: "nextWeekBillMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.nextWeekBillMoney, {separator: ",", decimal: 2});
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('bill:accebill:planamt:maintain:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updatePlanAmt(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="delPlanAmt(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });


});

// 新增
function addPlanAmt() {
    parent.layer.open({
        type: 2,
        area: ['900px', '580px'],
        maxmin: true,
        content: basePath + "/bill/accebill/planAmtMaintain/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

// 导入
function importPlanAmt() {
    up = bt.btImport.init({
        code: "20000009",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/bill/accebill/planAmtMaintain/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            });
        }
    });
}

// 编辑
function updatePlanAmt(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            area: ['900px', '580px'],
            maxmin: true,
            content: basePath + "/bill/accebill/planAmtMaintain/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

// 删除
function delPlanAmt(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/planAmtMaintain/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable && datatable.draw();
            }
        }, 'JSON');
    });
}