
bt.use(['form', 'datatable', 'dateType', 'corp', 'moneyType', 'dicts', 'selectType', 'bank','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: ".bank",
    });

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }

    accountCallBack2 = function (data) {
        $("#likes_oppAccNo").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payAccNo").setCorp(id);
        accountTable.get("likes_payeeAccNo").setCorp(id);
    }
    /*单位初始化*/
    bt.corp.init();
    var selectType = bt.selectType.init();
    bt.moneyType.init();
    var LODOP = getLodop();
    /**
     * 生成列表
     */
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData: "false"
        },
        ajax: {
            'url': basePath + "/receipt/bisbankreceiptdtlhis/datatable"
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
                text: bt.lang('common.button.batchDownload'),
                action: function (e, dt, node, config) {
                    batchDownload(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchPrint'),
                action: function (e, dt, node, config) {
                    batchPrint(e, dt, node, config);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 3//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "charageDate", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "receiptCheckCode", bSortable: true},
            {data: "receiptNo", bSortable: true},
            {
                data: "transAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.transAmt);
                }
            },
            {data: "cdSignName", bSortable: true},
            {data: "bifName", bSortable: true},
            // {data: "payAccNo", bSortable: true},
            // {data: "payAccName", bSortable: true},
            // {data: "payBankName", bSortable: true},
            // {data: "payeeAccNo", bSortable: true},
            // {data: "payeeAccName", bSortable: true},
            // {data: "payeeBankName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "usefor", bSortable: true},
            {data: "prints", bSortable: true, className: 'center'},
            {
                data: "dtlId", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.dtlId!=null){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="dtl(this)">'+bt.lang("common.button.viewFlow")+'</a>';
                    }else{
                        html += '<a class="" href="#" data-row="'+meta.row+'" >'+bt.lang("table.fbsBudget.detail.nothing")+'</a>';
                    }
                    return html;
                }
            },{
                data: "billId", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.cdSign=="0"){
                        html += '<a class="" href="#" data-row="'+meta.row+'" >'+bt.lang("table.fbsBudget.detail.nothing")+'</a>';
                    }else {
                        if(full.dtlId==null){
                            html += '<a class="" href="#" data-row="'+meta.row+'" >'+bt.lang("table.fbsBudget.detail.nothing")+'</a>';
                        }else{
                            if(full.billFrom=="no_one"){
                                html += '<a class="" href="#" data-row="' + meta.row + '" >' + bt.lang("table.fbsBudget.detail.nothing") + '</a>';
                            }else {
                                if (full.billId == null || full.billId == "") {
                                    html += '<a class="" href="#" data-row="' + meta.row + '" >' + bt.lang("table.fbsBudget.detail.nothing") + '</a>';
                                } else {
                                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bill(this)">' + bt.lang("common.button.viewPayment") + '</a>';
                                }
                            }
                        }
                    }
                    return html;
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
       // title: false, //不显示标题栏
        title: "预览",
        type: 2,
        area: ['870px', '80%'],
        closeBtn: 0,//不显示关闭按钮
        content: [basePath + "/receipt/bisbankreceiptdtlhis/view?id=" + data.id, 'no']
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
        content: basePath + "/receipt/bisbankreceiptdtlhis/print?id=" + data.id
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
        closeBtn: 0,//不显示关闭按钮
        content: basePath + "/receipt/bisbankreceiptdtlhis/batchPreview?ids=" + ids
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
        content: basePath + "/receipt/bisbankreceiptdtlhis/batchPrint?ids=" + ids,
        success: function (layero, index) {
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
    window.location = basePath + "/receipt/bisbankreceiptdtlhis/download?id=" + data.id;
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
    var form = $("<form></form>").attr("style", "display:none").attr("action", basePath + "/receipt/bisbankreceiptdtlhis/batchDownload").attr("method", "post");
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

//查看关联的流水
function dtl(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.dtlId.split("/").length;
    if(size==1){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/bisaccdtl/view/" + data.dtlId+",rules"
        });
    }else {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/bisbankreceiptdtl/dtl?id=" + data.dtlId
        });
    }
}
//查看关联的付款单
function bill(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.billId.split("/").length;
    if(size==1){
        if(data.billFrom=="nis_billhead") {
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/netbank/nisbillhead/view/" + data.billId
            });
        }else if(data.billFrom=="nis_wage"){
            parent.layer.open({
                type: 2,
                area: ['950px', '530px'],
                maxmin: true,
                content: basePath + "/wage/niswage/view/" + data.billId
            });
        }else if(data.billFrom=="fex_cash_flow"){
            parent.layer.open({
                type: 2,
                area: ['950px', '530px'],
                maxmin: true,
                content: basePath + "/foreign/fexcashflow/view/" + data.billId
            });
        }
    }else{
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/bisbankreceiptdtl/bill?id=" + data.billId
        });
    }
}
//查看多流水关联的付款单
function morebill(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptdtlhis/morebill?id=" + data.dtlId
    });
}

