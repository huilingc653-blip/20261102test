var $, form, datatable;
bt.use(['form', 'datatable', 'corp', 'mulselect', 'btImport', 'dicts', 'rateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.rateType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        ajax: {
            'url': basePath + "/bill/accebill/creditMaintain/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:credit:maintain:apply'),//应用权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    addCreditInfo();
                }
            },
            {
                hide: bt.isnotResource('bill:accebill:credit:maintain:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importCreditInfo();
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
            {
                data: "bondRatio", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.rateType.getFun().format(full.bondRatio, 'percent', 2, ',');
                }
            },
            {
                data: "openBillRate", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.rateType.getFun().format(full.openBillRate, 'percent', 2, ',');
                }
            },
            {data: "bondRate", bSortable: false, className: 'center'},
            {
                data: "isUsed", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isUsed);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('bill:accebill:credit:maintain:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updateCreditInfo(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="delCreditInfo(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });


});

// 新增
function addCreditInfo() {
    parent.layer.open({
        type: 2,
        area: ['900px', '580px'],
        maxmin: true,
        content: basePath + "/bill/accebill/creditMaintain/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

// 导入
function importCreditInfo() {
    up = bt.btImport.init({
        code: "20000008",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/bill/accebill/creditMaintain/saveImport",
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
function updateCreditInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            area: ['900px', '580px'],
            maxmin: true,
            content: basePath + "/bill/accebill/creditMaintain/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

// 删除
function delCreditInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/creditMaintain/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable && datatable.draw();
            }
        }, 'JSON');
    });
}