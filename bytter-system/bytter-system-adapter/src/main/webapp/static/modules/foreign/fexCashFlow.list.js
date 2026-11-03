var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType', 'element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    var selectType = bt.selectType.init();
    bt.mulselect.init();
    var dateFun = bt.dateType.getFun();
    bt.changePro = function(data){
        selectType.get('eqs_proId').attr({"data-url":basePath+"/foreign/fexproduct/listProduct/" + data.value,"data-defaultValue":""});
        form.render();
    };
    bt.dicts.init();

    var updateVoucherType = {
        hide: bt.isnotResource('foreign:fexcashflow:updatevouchertype'),
        text: bt.lang('foreign.button.updateVoucherType'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            var data = dt.rows('.selected').data();
            update(data, "1");
        }
    }
    var updateCorpAcc = {
        hide: bt.isnotResource('foreign:fexcashflow:updatecorpacc'),
        className: 'printAll active',
        text: bt.lang('foreign.button.updateCorpAcc'),
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            var data = dt.rows('.selected').data();
            update(data, "2");
        }
    }
    var updateCotpAcc = {
        hide: bt.isnotResource('foreign:fexcashflow:updatecotpacc'),
        className: 'printAll active',
        text: bt.lang('foreign.button.updateCotpAcc'),
        exportOptions: {
            columns: ':visible'
        },
        action: function (e, dt, node, config) {
            var data = dt.rows('.selected').data();
            update(data, "3");
        }
    }
    var updateVoucherStat = {
        hide: bt.isnotResource('foreign:fexcashflow:updatevoucherstat'),
        className: 'printAll active',
        text: bt.lang('foreign.button.updateVoucherStat'),
        exportOptions: {
            columns: ':visible'
        },
        action: function (e, dt, node, config) {
            var data = dt.rows('.selected').data();
            update(data, "4");
        }
    }
    var updateAll = {
        extend: 'collection',
        text: bt.lang('common.button.update'),//按钮名称,
        buttons: [
            updateVoucherType,
            updateCorpAcc,
            updateCotpAcc,
            updateVoucherStat
        ],
        fade: true
    };

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
            'url': basePath + "/foreign/fexcashflow/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fexcashflow:submit'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSubmit(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fexcashflow:settledeal'),
                text: bt.lang('foreign.button.settleDeal'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    update(data, "0");
                }
            }, {
                hide: bt.isnotResource('foreign:fexcashflow:merge'),
                text: bt.lang('foreign.button.merge'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    merge(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fexcashflow:mergecancel'),
                text: bt.lang('foreign.button.mergeCancel'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    mergeCancel(data);
                }
            },updateAll,
            {
                hide: bt.isnotResource('foreign:fexcashflow:back'),
                text: bt.lang('foreign.button.back'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBack(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {
                data: "tranBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.tranBillNo!="" && full.tranBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.tranBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {data: "bursarBillNo", bSortable: true, className: 'center'},
            {
                data: "settleType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCashFlow.settleType", full.settleType);
                }
            },
            {
                data: "proId", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return  full.proName;
                }
            },
            {
                data: "settleNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.settleType=="2") {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewMerge(this)">' + full.settleNo + '</a>';
                        return html;
                    }else{
                        return "";
                    }
                }
            },
            {data: "cashFlowDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "cashFlowType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexCorp.cashFlowType", full.cashFlowType);
                }
            },
            {data: "englishCode", bSortable: true, className: 'center'},
            {
                data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBusType.cdSign", full.cdSign);
                }
            },
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "corpBankName", bSortable: true},
            {data: "corpAccName", bSortable: true},
            {data: "corpBankAcc", bSortable: true},
            {
                data: "cotpName", bSortable:false,
                render: function (data, type, full, meta) {
                    if(full.tranType=="3"){
                        return full.allocatCotpName;
                    }else{
                       return full.cotpName;
                    }
                }
            },
            {
                data: "cotpBankName", bSortable: false,
                render: function (data, type, full, meta) {
                    if(full.tranType=="3"){
                        return full.allocatCotpBankName;
                    }else{
                        return full.cotpBankName;
                    }
                }
            },
            {data: "cotpAccName", bSortable: true},
            {data: "cotpBankAcc", bSortable: true},
            {data: "voucherTypeName", bSortable: true},
            {
                data: "isPayed", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.isPayed);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("nisBillhead.status", full.status);
                }
            },
            {
                data: "landingLimitResult", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            {
                data: "payStatus", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.payStatus == "-1") {
                        return bt.dict("fexCashFlow.payStatus", full.payStatus);;
                    } else {
                        if (full.voucherStat) {
                            return full.voucherStatName;
                        } else {
                            if (full.cdSign == "0") {
                                return bt.dict("fexCashFlow.gatherStatus", full.payStatus);
                            } else {
                                return bt.dict("fexCashFlow.payStatus", full.payStatus);
                            }
                        }
                    }
                }
            },
            {data: "billFromName", bSortable: true, className: 'center'},
            {
                data: "relBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.relBillNo!="" && full.relBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelBill(this)">' + full.relBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {
                data: "blendSign", bSortable: false, className: 'center',render: function (data, type, full, meta) {
                    if (full.bankFlowSign == "1" || full.blendSign == "1") {
                       return  bt.dict("fexTranReq.blendSign","1");
                    } else {
                       return  bt.dict("fexTranReq.blendSign","0")
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
});

//批量提交
function batchSubmit(data) {
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
        $.post(basePath + "/foreign/fexcashflow/batchSubmit", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//净额结算
function merge(data) {
    var len = data.length;
    if (len < 2) {
        layer.alert(bt.lang('netbank.message.confirm.selectData'), {icon: 7});
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
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('foreign.button.merge'), basePath + "/foreign/fexcashflow/merge/" + ids);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/merge/" + ids,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//取消净额结算
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
    }
    parent.layer.confirm(bt.lang('common.message.confirm.mergecancel'), function () {
        $.post(basePath + "/foreign/fexcashflow/mergeCancel", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function batchBack(data) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.settleBack'), function () {
        $.post(basePath + "/foreign/fexcashflow/batchBack", {ids: ids}, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//修改
function update(data, opType) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (len>1) {
        layer.alert(bt.lang('fexcashflow.message.confirm.selectData.only'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        //校验状态：0-暂存/1-打回的才允许编辑
        if (rowData[i].status != "0" && rowData[i].status != "-1") {
            layer.alert(bt.lang('fex.cash.flow.message.status.change', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        //操作类型 0-结算处理、1-修改支付方式、2-修改单位账户、3-交易对手账户、4-更新支付状态
        if(opType!="0" && rowData[i].settleType == "2"){
            //现金流【结算类型】为净额，不允许修改（可以结算处理）
            layer.alert(bt.lang('fex.cash.flow.message.settletype.1', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="3" && rowData[i].tranType == "3"){
            //现金流的交易类型为调拨交易，不允许修改交易对手账户
            layer.alert(bt.lang('fex.cash.flow.message.allocation.update', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="3" && rowData[i].tranType == "1"){
            //现金流的交易类型为外汇买卖交易，不允许修改交易对手账户
            layer.alert(bt.lang('fex.cash.flow.message.trade.update', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="3" && rowData[i].tranType == "2"){
            //现金流的交易类型为外汇期权交易，不允许修改交易对手账户
            layer.alert(bt.lang('fex.cash.flow.message.option.update', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="3" && rowData[i].tranType == "4" && rowData[i].proId == "4c182f17455f4614a79d66bdfd755c8d"){
            //交易类型为资金收付交， 产品类型为payPal 没有交易对手账户不需要修改
            layer.alert(bt.lang('fex.cash.flow.message.payPal.notUpdate', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="3" && rowData[i].tranType == "4" && rowData[i].proId == "53f8408e666b42b9abbe2dcf8372bc76" && (rowData[i].cotpBankAcc == "" || rowData[i].cotpBankAcc == null)){
            //交易类型为资金收付交， 产品类型为fps,交易对手银行账户为空不用修改
            layer.alert(bt.lang('fex.cash.flow.message.cotpBankAcc.isNull', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if(opType=="4" && rowData[i].cdSign == "0"){
            //现金流的收支方向为收款，不允许修改支付状态！
            layer.alert(bt.lang('fex.cash.flow.message.cdsign.update', new Array(rowData[i].billNo)), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcashflow/update/" + ids+"/"+opType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/update/" + ids+"/"+opType,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//查看业务单据详情
function viewBill(el) {
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

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/view/" + data.id
    });
}

function viewMerge(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/viewMerge/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/viewMerge/" + data.id
    });
}

function viewRelBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcashflow/viewByBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcashflow/viewByBillNo/" + data.relBillNo
    });
}
