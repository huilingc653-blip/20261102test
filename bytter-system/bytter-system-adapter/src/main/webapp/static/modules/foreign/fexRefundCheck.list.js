var dict,moneyType,selectType,datatable;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    selectType = bt.selectType.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }
    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();
    bt.corp.init();
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax:{
            'url':basePath+"/foreign/refundCheck/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:refundCheck:apply'),
                text: bt.lang('common.button.auto.paymentblending'),
                className:'aotuButton',
                action: function (e, dt, node, config) {
                    var data = dt.rows().data();
                    autoCheck(data);
                }
            },{
                hide: bt.isnotResource('foreign:refundCheck:apply'),
                text: bt.lang('ttpay.refund.batchSure'),
                className:'aotuTrueButton',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoConfirm(data);
                }
            },{
                hide: bt.isnotResource('foreign:refundCheck:apply'),
                text: bt.lang('common.button.cancel.paymentblending'),
                className:'cancelButton',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchCancel(data);
                }
            },{
                hide: bt.isnotResource('foreign:refundCheck:apply'),
                text: bt.lang('common.button.manual.scrap'),
                className:'scrapButton',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchDefeat(data);
                }
            },{
                hide: bt.isnotResource('foreign:refundCheck:apply'),
                text: bt.lang('common.button.cancel.scrap'),
                className:'cancelScrapButton',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchCancelDefeat(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data: "blendSignName", bSortable: true},
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "amt", bSortable: true,className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "refundBlendType", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("foreign.refundCheckHandle", full.refundBlendType);
                }},
            {data: "paymentBlendingDate", bSortable: true},
            {
                data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.billNo!="" && full.billNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewCashFlow(this)">' + full.billNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {
                data: "tranBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.tranBillNo!="" && full.tranBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewTranReq(this)">' + full.tranBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(full.billNo!="" && full.billNo!=null) {
                        return bt.money.format(full.payMoney);
                    } else {
                        return '';
                    }
                }
            },
            {data: "corpAccName", bSortable: true},
            {data: "corpBankAcc", bSortable: true},
            {data: "cotpAccName", bSortable: true},
            {data: "cotpBankAcc", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('foreign:refundCheck:apply', full.corpId)) {
                        if (full.blendSign == "" || full.blendSign == "0") {
                            // 勾对 & 作废
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="manualCheck(this)">' + bt.lang("common.button.refundCheck") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeat(this)">' + bt.lang("common.button.manual.scrap") + '</a><span class="text-explode">|</span>';
                        } else if (full.blendSign == "2") {
                            // 取消作废
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancelDefeat(this)">' + bt.lang("common.button.cancel.scrap") + '</a><span class="text-explode">|</span>';
                        } else if (full.blendSign == "1" || full.blendSign == "3") {
                            if (full.blendSign == "3") {
                                // 确认
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="confirm(this)">' + bt.lang("common.button.sure") + '</a><span class="text-explode">|</span>';
                            }
                            //取消勾对
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancel(this)">' + bt.lang("common.button.cancel.paymentblending") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/foreign/refundCheck/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    })
});

//自动勾兑
function autoCheck(data) {
    bt.loading.show();
    var refundCheckHandle = dict.get('refundCheckHandle').val();
    var refundCheckRule = '';
    $('input[name="eqs_refundCheckRule"]:checked').each(function () {
        refundCheckRule += $(this).val() + ",";
    })
    /*if(refundCheckRule.trim() == ""){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.matchingrules'), {icon: 2});
        return false;
    }*/
    var ids = '';
    for(var i = 0;i< data.length;i++){
        if(data[i].blendSign != '0'){
            continue;
        }
        ids += ""+data[i].id+",";
    }
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.auto'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/foreign/refundCheck/autoCheck",
            method: 'POST',
            data: {ids: ids,refundCheckRule:refundCheckRule,refundCheckHandle: refundCheckHandle},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success) {
                    parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTable");
                }else{
                    parent.layer.alert(bt.lang('bankacc.message.notDate'), {icon: result.success?1:2});
                }
            }
        });
    });
}

//确认
function confirm(el) {
    var refundCheckRule = '';
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.sure'), basePath + "/foreign/refundCheck/manualCheck?id=" + data.id+"&refundCheckRule="+refundCheckRule+"&opType=1");
            return;
        }
    }
}

//查看
function view(el) {
    var refundCheckRule = '';
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/foreign/refundCheck/manualCheck?id=" + data.id+"&refundCheckRule="+refundCheckRule+"&opType=2");
            return;
        }
    }
}

//批量勾对确认
function autoConfirm(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.refundblending.true.dtl'),{icon:2});
        return false;
    }
    var refundCheckHandle = dict.get('refundCheckHandle').val();
    var ids = '';
    for(var i = 0;i< data.length;i++){
        if(data[i].blendSign != '3'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.refundblending.true.dtl'), {icon: 2});
            return false;
        }
        ids += ""+data[i].id+",";
    }
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.refundCheck'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/foreign/refundCheck/autoConfirm",
            method: 'POST',
            data: {ids: ids,refundCheckHandle: refundCheckHandle},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success) {
                    parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTable");
                }else{
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            }
        });
    });
}

//取消勾兑
function cancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data.blendSign != '1' && data.blendSign != '3'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.cancelmanual'), function () {
        $.post(basePath + "/foreign/refundCheck/cancel/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量取消勾兑
function batchCancel(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'),{icon:2});
        return false;
    }
    var ids = '';
    for(var i = 0;i< data.length;i++){
        if(data[i].blendSign != '1' && data[0].blendSign != '3'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
            return false;
        }
        ids += ""+data[i].id+",";
    }
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.cancelmanual'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/foreign/refundCheck/batchCancel",
            method: 'POST',
            data: {ids: ids},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success) {
                    parent.layer.alert(bt.lang('bankacc.message.paymentblending.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTable");
                }else{
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            }
        });
    });
}

//作废
function defeat(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data.blendSign != '0'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.tovoid'), function () {
        $.post(basePath + "/foreign/refundCheck/defeat/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量作废
function batchDefeat(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].blendSign != '0'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'), {icon: 2});
            return false;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.tovoid'), function () {
        $.post(basePath + "/foreign/refundCheck/batchDefeat", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//取消作废
function cancelDefeat(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data.blendSign != '2'){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.scrap.dtl'), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.cancle.tovoid'), function () {
        $.post(basePath + "/foreign/refundCheck/cancelDefeat/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量取消作废
function batchCancelDefeat(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].blendSign != '2'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.scrap.dtl'), {icon: 2});
            return false;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.cancle.tovoid'), function () {
        $.post(basePath + "/foreign/refundCheck/batchCancelDefeat", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//手工勾对
function manualCheck(el) {
    var refundCheckRule = '';
    $('input[name="eqs_refundCheckRule"]:checked').each(function () {
        refundCheckRule += $(this).val() + ",";
    })
    /*if(refundCheckRule.trim() == ""){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.matchingrules'), {icon: 2});
        return false;
    }*/
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.refundCheck'), basePath + "/foreign/refundCheck/manualCheck?id=" + data.id+"&refundCheckRule="+refundCheckRule+"&opType=0");
            return;
        }
    }
}

//查看业务单据详情
function viewTranReq(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + data.tranBillId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + data.id
    });
}

function viewCashFlow(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/view/" + data.cashFlowId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/view/" + data.cashFlowId
    });
}
