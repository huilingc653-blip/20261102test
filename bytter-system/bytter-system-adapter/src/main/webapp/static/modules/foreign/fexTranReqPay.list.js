var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp','mulselect','moneyType', 'allExternalBankAcc'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();

    var allExternalAccCorp=bt.allExternalBankAcc.init();//客商账号组件初始化
    bt.cotpExternalAccBeforeClick = function () {
        allExternalAccCorp.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc,fps,payPal",
        });
        return true;
    }

    //交易对手银行账户
    bt.cotpExternalAccCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "outerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "corpBankAcc") {
                $("#oppId").val(data.corpId);
                $("#cotpName").val(data.corpName);
            } else if (data.type === "fps") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.ecName);
            } else if (data.type === "payPal") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.ecName);
            }
        }
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fextranreq/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },
            {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.merge'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    merge(data);
                }
            },
            {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.mergecancel'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    mergeCancel(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:pay:apply'),
                text: bt.lang('common.button.batchSetPayerAcc'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetPayerAcc(data);
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
            {data: "billNo", bSortable: true, className: 'center'},
            /*{data: "origBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.origBillNo && full.origBillNo != ""){
                        var billNo = full.origBillNo.split(',');
                        for(var i=0 ; i<billNo.length ; i++){
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBillNo(\'' + billNo[i] + '\')">' + billNo[i] + '</a>';
                            if(i < billNo.length-1){
                                html += ","
                            }
                        }
                    }
                    return html;
                }
            },*/
            {
                data: "proId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.proName;
                }
            },
            /*{
                data: "difBank", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBillhead.difBank", full.difBank);
                }
            },*/
            {
                data: "templateId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.templateName;
                }
            },
            {
                data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCorp.cdSign", full.cdSign);
                }
            },
            {
                data: "remitType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexForeignReq.remitType", full.remitType);
                }
            },
            {
                data: "isPayed", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.isPayed);
                }
            },
            {
                data: "corpId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.corpName;
                }
            },
            {
                data: "corpBank", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.corpBankName;
                }
            },
            {data: "corpBankAcc", bSortable: true},
            {data: "corpAccName", bSortable: true},
            {data: "cotpName", bSortable: true},
            {data: "cotpBankName", bSortable: true},
            {data: "cotpBankAcc", bSortable: true},
            {
                data: "curId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.englishCode;
                }
            },
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            /*{data: "bursarBillNo", bSortable: true},*/
            /*{
                data: "accNature", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReq.accNature", full.accNature);
                }
            },*/
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReqPay.status", full.status);
                }
            },
            {
                data: "billFrom", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.billFromName;
                }
            },
            {
                data: "relBillNo", bSortable: true, className: 'center',
                /*render: function (data, type, full, meta) {
                    if(full.relBillNo!="" && full.relBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelBill(this)">' + full.relBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }*/
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.relBillNo && full.relBillNo != ""){
                        var billNo = full.relBillNo.split(',');
                        for(var i=0 ; i<billNo.length ; i++){
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBillNo(\'' + billNo[i] + '\')">' + billNo[i] + '</a>';
                            if(i < billNo.length-1){
                                html += ","
                            }
                        }
                    }
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.status == "0" || full.status == "-1") {
                        if (bt.isResource('foreign:fextranreq:pay:apply', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('foreign:fextranreq:pay:apply', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('foreign:fextranreq:pay:apply', full.corpId)) {
                           if(full.mergeId == null || full.mergeId ==''){
                               if (full.splitId && full.splitId != "") {
                                   html += '<a class="" href="#" data-row="' + meta.row + '" onclick="splitCancel(this)">' + bt.lang("common.button.splitcancel") + '</a><span class="text-explode">|</span>';
                               }
                               html += '<a class="" href="#" data-row="' + meta.row + '" onclick="split(this)">' + bt.lang("common.button.split") + '</a><span class="text-explode">|</span>';
                           }
                        }
                    }
                    if (bt.isResource('foreign:fextranreq:pay:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    /*if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreq/add/"+$("#tranType").val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/add/"+$("#tranType").val(),
        end: function () {
            datatable && datatable.draw();
        }
    });*/


    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreqCust/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreqCust/add",
        end: function () {
            datatable && datatable.draw();
        }
    });

}

function clone(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreqCust/clone/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreqCust/clone/" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreqCust/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreqCust/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fextranreqCust/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fextranreqCust/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.prompt({
        title: bt.lang('abemitbill.delete.reason'),
        formType: 2
    },function(deleteMsg){
        $.post(basePath + "/foreign/fextranreq/deleteContianMsg/", {id:data.id,deleteMsg:deleteMsg}, function (result) {
            datatable && datatable.draw();
            parent.layer?parent.layer.closeAll():layer.closeAll();
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
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/foreign/fextranreq/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(data) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        bt.loading.show();
        $.post(basePath + "/foreign/fextranreq/batchSubmit", {ids: ids}, function (result) {
            bt.loading.hide();
            var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
            datatable.draw();
            showBatchSubmitResult(result, icon);
        }, 'JSON');
    });
    return false;
}

function showBatchSubmitResult(result, icon) {
    if (paymentLimitAlert.isBatchResult(result)) {
        parent.layer.alert(paymentLimitAlert.batchContent(result), paymentLimitAlert.batchAlertOptions());
        return;
    }
    parent.layer.alert(result.msg, {icon: icon});
}

function split(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        $.ajax({
            url: basePath + "/foreign/fextranreq/isMergeBill",
            method: 'POST',
            async: false,
            data: {
                id: data.id
            },
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    layer.alert(result.msg, {icon: 2});
                    return;
                }
                // basePath + "/foreign/fextranreq/split/" + data.id, --旧的
                if (bt.navtab) {
                    bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fextranreqCust/split/" + data.id);
                    return;
                }

                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/foreign/fextranreqCust/split/" + data.id,
                    end: function () {
                        datatable && datatable.draw();
                    }
                });
            }
        });
    }
}

function splitCancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.splitcancel'), function () {
        $.post(basePath + "/foreign/fextranreq/splitCancel/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//合并
function merge(data) {
    var len = data.length;
    if (len < 2) {
        layer.alert(bt.lang('netbank.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (len > 100) {
        layer.alert(bt.lang('fex.tran.message.merge.max'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids = rowData[0].id;
    for (var i = 1; i < rowData.length; i++) {
        ids += "," + rowData[i].id;
        //拆分
        /*if (rowData[0].splitId == null || rowData[0].origBillNo == null) {
            layer.alert(bt.lang('fex.foreign.message.splitId.null',[rowData[i].billNo]), {icon: 2});
            return;
        }*/
        //原单号
        if (rowData[0].origBillNo != rowData[i].origBillNo) {
            layer.alert(bt.lang('fex.foreign.message.origBillNo.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //汇款类型
        if (rowData[0].remitType != rowData[i].remitType) {
            layer.alert(bt.lang('fex.foreign.message.remittype.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //单位
        if (rowData[0].corpId != rowData[i].corpId) {
            layer.alert(bt.lang('fex.foreign.message.corpId.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //主体单位银行
        if (rowData[0].corpBankName != rowData[i].corpBankName) {
            layer.alert(bt.lang('fex.foreign.message.corpBankName.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //主体单位银行账户
        if (rowData[0].corpBankAcc != rowData[i].corpBankAcc) {
            layer.alert(bt.lang('fex.foreign.message.corpBankAcc.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //账户名称
        if (rowData[0].corpAccName != rowData[i].corpAccName) {
            layer.alert(bt.lang('fex.foreign.message.corpAccName.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //账户币种
        if (rowData[0].englishCode != rowData[i].englishCode) {
            layer.alert(bt.lang('fex.foreign.message.englishCode.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //交易对手
        if (rowData[0].cotpName != rowData[i].cotpName) {
            layer.alert(bt.lang('fex.foreign.message.cotpName.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //交易对手银行
        if (rowData[0].cotpBankName != rowData[i].cotpBankName) {
            layer.alert(bt.lang('fex.foreign.message.cotpBankName.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        //交易对手银行账户
        if (rowData[0].cotpBankAcc != rowData[i].cotpBankAcc) {
            layer.alert(bt.lang('fex.foreign.message.cotpBankAcc.mismatching',[rowData[i].billNo]), {icon: 2});
            return;
        }
        if (rowData[i].remitType != "2") {
            if (rowData[i].tranAmt1) {
                if (rowData[0].tranAmt1) {
                    // 付款类型
                    if (rowData[0].payType != rowData[i].payType) {
                        layer.alert(bt.lang('fex.foreign.message.payType.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 是否为保税货物项付款
                    if (rowData[0].isBonded != rowData[i].isBonded) {
                        layer.alert(bt.lang('fex.foreign.message.isBonded.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 交易编码
                    if (rowData[0].tranCode1 != rowData[i].tranCode1) {
                        layer.alert(bt.lang('fex.foreign.message.tranCode1.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    if (rowData[0].tranCode2 != rowData[i].tranCode2) {
                        layer.alert(bt.lang('fex.foreign.message.tranCode2.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 付汇性质
                    if (rowData[0].remitNature != rowData[i].remitNature) {
                        layer.alert(bt.lang('fex.foreign.message.remitNature.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 汇款去向
                    if (rowData[0].remitRoute != rowData[i].remitRoute) {
                        layer.alert(bt.lang('fex.foreign.message.remitRoute.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 最迟装运日期
                    if (rowData[0].shipDate != rowData[i].shipDate) {
                        layer.alert(bt.lang('fex.foreign.message.shipDate.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                    // 汇款用途
                    if (rowData[0].remitUse != rowData[i].remitUse) {
                        layer.alert(bt.lang('fex.foreign.message.remitUse.mismatching', [rowData[i].billNo]), {icon: 2});
                        return;
                    }
                } else {
                    // 付款类型
                    rowData[0].payType = rowData[i].payType;
                    // 是否为保税货物项付款
                    rowData[0].isBonded = rowData[i].isBonded;
                    // 交易编码
                    rowData[0].tranCode1 = rowData[i].tranCode1;
                    rowData[0].tranCode2 = rowData[i].tranCode2;
                    // 付汇性质
                    rowData[0].remitNature = rowData[i].remitNature;
                    // 汇款去向
                    rowData[0].remitRoute = rowData[i].remitRoute;
                    // 最迟装运日期
                    rowData[0].shipDate = rowData[i].shipDate;
                    // 汇款用途
                    rowData[0].remitUse = rowData[i].remitUse;
                }
            }
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.merge'), function () {
        $.post(basePath + "/foreign/fextranreq/merge", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//撤销合并
function mergeCancel(data) {
    var len = data.length;
    if (len < 1) {
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
        if (rowData[i].mergeId == null || rowData[i].mergeId == '') {
            layer.alert(bt.lang('fex.foreign.message.mergeId.null',[rowData[i].billNo]), {icon: 2});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.mergecancel'), function () {
        $.post(basePath + "/foreign/fextranreq/mergeCancel", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量选择付款账号
function batchSetPayerAcc(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var corpId = "";
    var tranType = "";
    for (var i = 0; i < rowData.length; i++) {
        //校验付款单位是否一致
        if (corpId == "") {
            corpId = rowData[i].corpId;
        } else {
            if (corpId != rowData[i].corpId) {
                layer.alert(bt.lang('nis.setpayeracc.message.corp.same'), {icon: 7});
                return false;
            }
        }

        if (tranType == "") {
            tranType = rowData[i].tranType;
        }
    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/toSetPayerAcc/" + corpId + "?tranType=" + tranType
    });
}

function viewBillNo(billNo) {
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreqCust/viewBillNo/" + billNo
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
        $.post(basePath + "/foreign/fextranreq/batchDelete", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function viewRelBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreqCust/viewBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreqCust/viewBillNo/" + data.relBillNo
    });
}
