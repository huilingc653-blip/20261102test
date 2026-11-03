bt.use(['form', 'datatable','corp', 'dateType', 'selectType', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.selectType.init();
    customerAccountCallBack = function (data) {
        $("#likes_customerAccount").val(data.bankAcc);
    };
    var accountTable = bt.accountTable.init();

    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("like_customerAccount").setCorp(id);
    }
    /*单位初始化*/
    bt.corp.init();

    /**
     * 生成列表
     */
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns:{
            rightColumns:1
        },
        checkbox: true,
        order: [[2,'asc']],
        ajax: {
            'url': basePath + "/bisBank/bisBankMonthStatementDtl/datatable"
        },
        ignoreButtons: ['print'],
        buttons: [
            {
                text: bt.lang('common.button.batchPreview'),
                action: function (e, dt, node, config) {
                    batchPreview(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchPrint'),
                action: function (e, dt, node, config) {
                    batchPrint(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchDownload'),
                action: function (e, dt, node, config) {
                    batchDownload(e, dt, node, config);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "corpName",bSortable:true},
            {data: "customerAccount", bSortable: true},
            {data: "customerAccountName", bSortable: false},
            {data: "curName", bSortable: true},
            {data: "billBenginDate", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return full.billBenginDate + "~" + full.billEndDate;
                }
            },
            {data: "bifName", bSortable: false},
            {
                data: "closingBalance", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.closingBalance);
                }
            },
            {
                data: "totalNumber", bSortable: true, className: 'textRight'
            },
            {
                data: "totalDebitAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.totalDebitAmt);
                }
            },
            {
                data: "totalCreditAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.totalCreditAmt);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="preview(this)">' + bt.lang("common.button.excel.preview") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="downloadFile(this)">' + bt.lang("common.button.download") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="print(this)">' + bt.lang("common.button.print") + '</a>';
                    return html;
                }
            }
        ]
    });
});

/**
 * 预览
 * @param el
 */
function preview(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        title: false, //不显示标题栏
        type: 2,
        area: ['810px', '450px'],
        closeBtn :0,//不显示关闭按钮
        content: [basePath + "/bisBank/bisBankMonthStatementDtl/view?id=" + data.id]
        , btn: [bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
        , yes: function () {
            print(el);
        }
        , btn2: function () {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        }
    });
}

/**
 * 打印
 * @param el
 */
function print(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/bisBank/bisBankMonthStatementDtl/print?id=" + data.id
    });
}

/**
 * 批量打印预览
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPreview(e, dt, node, config) {
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
    parent.layer.open({
        title: false, //不显示标题栏
        type: 2,
        area: ['838px', '450px'],
        closeBtn :0,//不显示关闭按钮
        content: basePath + "/bisBank/bisBankMonthStatementDtl/batchPreview?ids=" + ids
        , btn: [bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
        , yes: function () {
            batchPrint(e, dt, node, config);
        }
        , btn2: function () {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        }
    });
}

/**
 * 批量打印
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPrint(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var loadIndex = layer && layer.load(1, {shade: [0.3, '#fff']});
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/bisBank/bisBankMonthStatementDtl/batchPrint?ids=" + ids,
        success:function (layero, index) {
            layer && layer.close(loadIndex);
        }
    });
}

/**
 * 下载
 * @param el
 */
function downloadFile(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    window.location = basePath + "/bisBank/bisBankMonthStatementDtl/download?id=" + data.id;
}

/**
 * 批量下载
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDownload(e, dt, node, config) {
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
    var form = $("<form></form>").attr("style", "display:none").attr("action", basePath + "/bisBank/bisBankMonthStatementDtl/batchDownload").attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "ids").attr("value", ids));
        form.appendTo('body');
        form.submit();
    } catch (e) {
        console.log(e);
    } finally {
        form.remove();
    }
}

