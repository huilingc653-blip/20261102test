var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank', 'corp', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payerAcc").setCorp(id);
    }
    bt.corp.init();
    accountCallBack = function (data) {
        $("#likes_payerAcc").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,

        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bdlink/bisexc/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bdLink:bisExc:salePaymentApply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisExc:salePaymentApply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //校验黑名单
                    corpCheck(data);
                    // submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisExc:salePaymentApply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
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

            {data: "voucherNo", bSortable: true, className: 'center'},
            {data: "bifName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true},
            {data: "voucherTypeName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "reqDate", bSortable: true},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bisExc.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bdLink:bisExc:salePaymentApply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        }
    });
    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payerAcc").setCorp("");
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bdlink/bisexc/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bdlink/bisexc/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bdlink/bisexc/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bdlink/bisexc/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//校验黑白名单
function corpCheck(res){
    var len = res.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = res.splice(0, res.length);
    var params=[];
    for (var i = 0; i < rowData.length; i++) {
        var param = {};
        param.name = rowData[i].payeeAccName;
        param.appNo = rowData[i].voucherNo;
        param.corpId = rowData[i].corpId;
        params.push(param);
    }
    var data = {};
    data.params = params;
    //对黑白名单进行验证
    var reData=externalCorpCheckBatch(data);
    if(reData.reCode=='3'){
        bt.loading.hide();
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
        return;
    }else if(reData.reCode=='2'){
        bt.loading.hide();
        parent.layer.confirm(reData.msg,function(){
            submitBatch(rowData);
        });
    }else{
        submitBatch(rowData);
    }
}

//批量提交
function submitBatch(rowData) {
    var ids = "";
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/bdlink/bisexc/batchSubmit", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量删除
function deleteBatch(data) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bdlink/bisexc/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}