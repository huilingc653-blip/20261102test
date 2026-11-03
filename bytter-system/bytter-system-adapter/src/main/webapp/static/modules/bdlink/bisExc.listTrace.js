var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'mulselect','moneyType', 'dicts', 'bank', 'corp', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    var dict = bt.dicts.init();
    bt.dateType.init();
    bt.mulselect.init(); // 多选
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
                hide: bt.isnotResource('netbank:bdLink:bisExc:traceResend'),
                text: bt.lang('common.button.batchResend'),
                action: function (e, dt, node, config) {
                    batchResend(e, dt, node, config);
                }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisExc:traceApply'),
                text: bt.lang('common.button.batchReturn'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var ids = "";
                    var rowData = data.splice(0, len);
                    var expenseBillNo = "";
                    var backNodeCheckNo = "";//可选择退回环节的单
                    var backNodeCheckId = "";
                    //代发工资指令
                    var wageVoucherNos = "";
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                        if(rowData[i].voucherFrom == '10'){
                            if (wageVoucherNos == "") {
                                wageVoucherNos += rowData[i].voucherNo;
                            } else {
                                wageVoucherNos += "," + rowData[i].voucherNo;
                            }
                            //代发工资指令不支持该操作
                            continue;
                        }
                        var result = validAccBook(rowData[i]);//账务校验
                        if (result != "0") {
                            if (result == "1") {
                                layer.alert(bt.lang('bdlink.message.accbook.defeat', [rowData[i].cbsBillNo]), {icon: 2});
                                return;
                            } else if (result == "2") {
                                layer.alert(bt.lang('bdlink.message.accbook.closeDate', [rowData[i].cbsBillNo]), {icon: 2});
                                return;
                            } else if (result == "3") {
                                layer.alert(bt.lang('bdlink.message.expense.waitresult', [rowData[i].cbsBillNo]), {icon: 2});
                                return;
                            } else if (result == "4") {
                                if (backNodeCheckNo == "") {
                                    backNodeCheckNo += rowData[i].cbsBillNo;
                                    backNodeCheckId += rowData[i].id;
                                } else {
                                    backNodeCheckNo += "," + rowData[i].cbsBillNo;
                                    backNodeCheckId += "," + rowData[i].id;
                                }
                            } else {
                                if (expenseBillNo == "") {
                                    expenseBillNo += rowData[i].cbsBillNo;
                                } else {
                                    expenseBillNo += "," + rowData[i].cbsBillNo;
                                }
                            }
                        }
                    }
                    if(wageVoucherNos != ""){
                        layer.alert(bt.lang('bdlink.message.wage.forbid',  [wageVoucherNos]), {icon: 2});
                        return;
                    }
                    if (expenseBillNo != "") {
                        parent.layer.confirm(bt.lang('bdlink.message.accbook.expense.batch', [expenseBillNo]), function () {
                            parent.layer.prompt({
                                title: bt.lang('table.bisExc.backMsg'),
                                formType: 2
                            },function(backMsg) {
                                $.post(basePath + "/bdlink/bisexc/batchBack", {ids: ids, backMsg: backMsg}, function (result) {
                                    datatable && datatable.draw();
                                    parent.layer?parent.layer.closeAll():layer.closeAll();
                                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                                }, 'JSON');
                            });
                        });
                    } else {
                        if(backNodeCheckNo != ""){
                            saveBackNode(ids, backNodeCheckId, backNodeCheckNo);
                        } else {
                            parent.layer.prompt({
                                title: bt.lang('table.bisExc.backMsg'),
                                formType: 2
                            }, function (backMsg) {
                                $.post(basePath + "/bdlink/bisexc/batchBack", {
                                    ids: ids,
                                    backMsg: backMsg
                                }, function (result) {
                                    datatable && datatable.draw();
                                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                                }, 'JSON');
                            });
                        }
                    }
                }},
                {
                    hide:bt.isnotResource('netbank:bdLink:bisExc:traceApply'),
                    text: bt.lang('common.button.batchDefeat'),
                    action: function ( e, dt, node, config ) {
                        var data = dt.rows('.selected').data();
                        var len = data.length;
                        if(len<1){
                            layer.msg(bt.lang('common.message.confirm.selectData'));
                            return;
                        }
                        var ids="";
                         var rowData = data.splice(0,len);
                         for(var i=0;i<rowData.length;i++){
                             if (ids == "") {
                                ids += rowData[i].id;
                            } else {
                                ids += "," + rowData[i].id;
                            }
                         }
                         parent.layer.confirm(bt.lang('common.message.confirm.defeat'),function(){
                             $.post(basePath + "/bdlink/bisexc/batchDefeat",{ids:ids}, function(result){
                                datatable.draw();
                                parent.layer.alert(result.msg, {icon: result.success?1:2});
                            },'JSON');
                         });
                    }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisExc:traceQuery'),
                text: bt.lang('common.button.batchQueryResult'),
                action: function (e, dt, node, config) {
                    batchQuery(e, dt, node, config);
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
            {
                data: "voucherNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.isSpecFailForTranPay=="1") {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "bifName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true},
            {data: "voucherTypeName", bSortable: true},
            {
                data: "failReason", bSortable: true, visible: setFailReasonVisible(),
                render: function (data, type, full, meta) {

                    return full.failReason;
                }
            },
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
            {data: "reqDate", bSortable: true, className: 'center'},
            {
                data: "cbsBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "7" && full.voucherFrom != "11") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "busOriTypeName", bSortable: true, className: 'center'},
            {data: "voucherFromName", bSortable: true, className: 'center'},
            {
                data: "voucherStatName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var desc = bt.lang("table.bisExc.isSpecFailForTranPay");
                    if(full.isSpecFailForTranPay=="1"){
                        return full.voucherStatName+desc;
                    } else {
                        return full.voucherStatName;
                    }
                }
            },
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.voucherStat == '1' || full.voucherStat == 'W') {
                        if (bt.isResource('netbank:bdLink:bisExc:setResult', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="setPayResultColumn(this, 0)">' + bt.lang("common.button.paySuccess") + '</a><span class="text-explode">|</span>';
                            /* html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                        }
                        if (bt.isResource('netbank:bdLink:bisExc:setResult', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="setPayResultColumn(this, 2)">' + bt.lang("common.button.payFailure") + '</a><span class="text-explode">|</span>';
                            /* html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                        }
                    } else {
                        if(full.voucherFrom == '10'){
                            //代发工资增加重发
                            if (bt.isResource('netbank:bdLink:bisExc:traceResend', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="resendColumn(this)">' + bt.lang("common.button.resend") + '</a><span class="text-explode">|</span>';
                            }
                        } else {
                            if (bt.isResource('netbank:bdLink:bisExc:traceResend', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="resendColumn(this)">' + bt.lang("common.button.resend") + '</a><span class="text-explode">|</span>';
                            }
                            if (bt.isResource('netbank:bdLink:bisExc:traceBack', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backColumn(this)">' + bt.lang("common.button.return") + '</a><span class="text-explode">|</span>';
                            }
                            if (bt.isResource('netbank:bdLink:bisExc:traceDefeat', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeatColumn(this)">' + bt.lang("common.button.defeat") + '</a><span class="text-explode">|</span>';
                                /* html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                            }
                            if (bt.isResource('netbank:bdLink:bisExc:traceQuery', full.corpId)) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="queryColumn(this)">' + bt.lang("common.button.queryResult") + '</a><span class="text-explode">|</span>';
                                /* html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                            }
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // if (full.voucherFrom && full.voucherFrom == "9") {
                    //     html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    // }
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "7" && full.voucherFrom != "11") {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + bt.lang("common.button.viewBusBill") + '</a>';
                    }
                    return html;
                }
            }
        ],
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var showHTML =  '已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0;
            return '<div style="color: red" <span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        btClickRowCallback:function(){
            $("#sumMoneyBillSpan").text('已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0)
        },
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

function setFailReasonVisible(){
    var isMathcFailReason = $("#isMathcFailReason").val();
    if (isMathcFailReason == "1") {
        return true;
    } else {
        return false;
    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var viewMethod = "view";
    if (data.excFrom == "1") {
        viewMethod = "viewExcProc";
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/"+viewMethod+"/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/"+viewMethod+"/" + data.id
    });
}

//重发
function resendColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var flag = false;
    var msg = "";
    $.ajax({
        url: basePath + "/bdlink/bisexc?rePay",
        method: 'POST',
        async: false,
        data: {
            type: "trace",
            id: data.id
        },
        dataType: 'json',
        success: function (result) {
            flag = result.success;
            if(result.success == true){
                msg = result.msg;
                if(result.obj) {
                    msg += "[" + bt.lang('table.bisExc.voucherNo') + result.obj.voucherNo + "；" + bt.lang('table.bisExc.payeeAcc') + result.obj.payeeAcc + "；" + bt.lang('table.bisExc.payMoney') + bt.money.format(result.obj.payMoney) + "]";
                }
            }
        }
    });
    if(flag){
        parent.layer.confirm(bt.lang(msg), function () {
            parent.layer.confirm(bt.lang('common.message.confirm.resendConfirm'), function () {
                $.post(basePath + "/bdlink/bisexc/resend/" + data.id, function (result) {
                    datatable && datatable.draw();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        });
    }else{
        parent.layer.confirm(bt.lang('common.message.confirm.resendConfirm'), function () {
            $.post(basePath + "/bdlink/bisexc/resend/" + data.id, function (result) {
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

}

/**
 * 批量重发
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchResend(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
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
    var flag = false;
    var msg = "";
    $.ajax({
        url: basePath + "/bdlink/bisexc?batchRePay",
        method: 'POST',
        async: false,
        data: {
            type: "trace",
            ids: ids
        },
        dataType: 'json',
        success: function (result) {
            flag = result.success;
            if(result.success == true){
                msg = result.msg;
                if(result.obj) {
                    msg += "[" + bt.lang('table.bisExc.voucherNo') + result.obj.voucherNo + "；" + bt.lang('table.bisExc.payeeAcc') + result.obj.payeeAcc + "；" + bt.lang('table.bisExc.payMoney') + bt.money.format(result.obj.payMoney) + "]";
                }
            }
        }
    });
    if(flag){
        parent.layer.confirm(bt.lang(msg), function () {
            parent.layer.confirm(bt.lang('common.message.confirm.resendConfirm'), function () {
                $.post(basePath + "/bdlink/bisexc/batchResend", {ids: ids}, function (result) {
                    datatable.draw();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        });
    }else{
        parent.layer.confirm(bt.lang('common.message.confirm.resendConfirm'), function () {
            $.post(basePath + "/bdlink/bisexc/batchResend", {ids: ids}, function (result) {
                datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}

//作废
function defeatColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var result = validAccBook(data);//账务校验
    if (result != "0" && result != "4") {
        if(data.isSpecFailForTranPay=="1"){
            var msg = "common.message.confirm.defeat";
            if (result == "1") {
                msg = "bdlink.message.accbook.defeat";
            } else if (result == "2") {
                msg = "bdlink.message.accbook.closeDate";
            } else if (result == "3") {
                layer.alert(bt.lang('bdlink.message.expense.waitresult', [data.cbsBillNo]), {icon: 2});
                return false;
            }
            var isMathcFailReason = $("#isMathcFailReason").val();
            if (isMathcFailReason == "1") {
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/bdlink/bisexc/viewDefeat/" + data.id
                });
            } else {
                parent.layer.confirm(bt.lang(msg, [data.cbsBillNo]), function () {
                    $.post(basePath + "/bdlink/bisexc/defeat/" + data.id, function (result) {
                        datatable && datatable.draw();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            }
        }else {
            if (result == "1") {
                layer.alert(bt.lang('bdlink.message.accbook.defeat', [data.cbsBillNo]), {icon: 2});
            } else if (result == "2") {
                layer.alert(bt.lang('bdlink.message.accbook.closeDate', [data.cbsBillNo]), {icon: 2});
            } else if (result == "3") {
                layer.alert(bt.lang('bdlink.message.expense.waitresult', [data.cbsBillNo]), {icon: 2});
            } else if (result == "4") {

            } else {
                var busInfo = result.split(",");
                parent.layer.confirm(bt.lang('bdlink.message.accbook.expense', [data.cbsBillNo, busInfo[0], busInfo[1]]), function () {
                    $.post(basePath + "/bdlink/bisexc/defeat/" + data.id, function (result) {
                        datatable && datatable.draw();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            }
        }
        return false;
    } else {
        var isMathcFailReason = $("#isMathcFailReason").val();
        if (isMathcFailReason == "1") {
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/bdlink/bisexc/viewDefeat/" + data.id
            });
        } else {
            parent.layer.confirm(bt.lang('common.message.confirm.defeat'), function () {
                $.post(basePath + "/bdlink/bisexc/defeat/" + data.id, function (result) {
                    datatable && datatable.draw();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }
    }
}

//设为支付成功/失败
function setPayResultColumn(el, voucherStat) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/setPayResult/" + data.id + "/" + voucherStat + "/" + data.excFrom
    });
}

//退回
function backColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var result = validAccBook(data);//账务校验
    if (result != "0") {
        if (result == "1") {
            layer.alert(bt.lang('bdlink.message.accbook.defeat', [data.cbsBillNo]), {icon: 2});
        } else if (result == "2") {
            layer.alert(bt.lang('bdlink.message.accbook.closeDate', [data.cbsBillNo]), {icon: 2});
        } else if (result == "3") {
            layer.alert(bt.lang('bdlink.message.expense.waitresult', [data.cbsBillNo]), {icon: 2});
        } else if (result == "4") {
            saveBackNode(data.id, data.id, data.cbsBillNo);
        } else {
            var busInfo = result.split(",");
            parent.layer.confirm(bt.lang('bdlink.message.accbook.expense', [data.cbsBillNo, busInfo[0], busInfo[1]]), function () {
                parent.layer.prompt({
                    title: bt.lang('table.bisExc.backMsg'),
                    formType: 2
                },function(backMsg) {
                    $.post(basePath + "/bdlink/bisexc/back", {id:data.id,backMsg:backMsg}, function (result) {
                        datatable && datatable.draw();
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            });
        }
        return false;
    } else {
        parent.layer.prompt({
            title: bt.lang('table.bisExc.backMsg'),
            formType: 2
        },function(backMsg){
            $.post(basePath + "/bdlink/bisexc/back", {id:data.id,backMsg:backMsg}, function (result) {
                datatable && datatable.draw();
                parent.layer?parent.layer.closeAll():layer.closeAll();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}
function saveBackNode(backIds, backNodeIds, voucherNo){
    var content = '<form class="layui-form">';
    content += '<table  style="border: none;width: 98%;padding-top: 10px;">'
    content += '<tr><td style="border: none;height: 24px;padding-left: 30px;font-size: 14px;">'+bt.lang('table.bisExc.cbsBillId')+voucherNo+'</td></tr>';
    content += '<tr><td style="border: none;height: 24px;padding-left: 30px;font-size: 14px;">'+bt.lang('table.bisExc.back.dec')+'</td></tr>';
    content += '<tr><td style="border: none;height: 24px;padding-left: 30px;font-size: 14px;"><div class="bt-biz-dict" data-type="radio" data-group="bisExc.backNode" data-name="backNode" data-biz-name="backNode" data-defaultvalue="1"></div></td></tr>';
    content += '</tr></table>'
    content += '<div style="padding-left: 30px;padding-top: 10px"><textarea lay-verify="strlength" rows="4" data-maxlength="100" class="layui-textarea" id="backMsg" name="backMsg" style="width: 90%" ></textarea></div></form>';
    layer.open({
        title: bt.lang('table.bisExc.backMsg'),
        type: 1,
        area: ['360px', '300px'],
        btn:[bt.lang('common.button.sure'),bt.lang('common.button.cancel')],
        content: content,
        yes: function (index, layero) {
            var backMsg = $('#backMsg').val();
            var backNode = bt.dicts.get('backNode').val();
            if(backMsg == "" || backMsg == "undefined") {
                $('#backMsg').focus();
            }else{
                $.post(basePath + "/bdlink/bisexc/saveBackNode", {
                    backIds: backIds,
                    backNodeIds: backNodeIds,
                    backNode: backNode,
                    backMsg: backMsg
                }, function (result) {
                    datatable && datatable.draw();
                    layer.closeAll();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            }
        }
    });
    bt.dicts.init();
}
//账务校验
function validAccBook(data) {
    var result = "0";
    $.ajax({
        url: basePath + "/bdlink/bisexc?getFcAccBook",
        method: 'POST',
        async: false,
        data: {cbsBillNo: data.cbsBillNo,cbsBillId: data.cbsBillId, voucherFrom: data.voucherFrom, busOriType: data.busOriType},
        dataType: 'json',
        success: function (data) {
            if (data.obj) {
                //返回结果：
                //0-可退回/作废
                //1-业务单{0}的凭证需作废，请先处理账务
                //2-业务单{0}的凭证记账日期已进行日结需冲账，请先处理账务
                //其他-业务单{0}有{1}笔明细失败，失败金额合计{2}，需做手工账冲销，请确认是否已进行账务处理
                result = data.obj;
            }
        }
    });
    return result;
}

//恢复
/*function recoverColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
        $.post(basePath + "/bdlink/bisexc/recover/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}*/

//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    var action = "/netbank/nisbillhead/view/" + cbsBillId;
    //资金调拨
    if (data.voucherFrom != "1" && data.voucherFrom != "10") {
        action = "/allocation/nisallocationreq/view/" + cbsBillId;
    }
    //代发工资
    if (data.voucherFrom == "10") {
        action = "/wage/niswage/view/" + cbsBillId;
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + action);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + action
    });
}

//结果查询
function queryColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.queryResult'), function () {
        $.post(basePath + "/bdlink/bisexc/query/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//结果结果查询
function batchQuery(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    //代发工资指令
    var wageVoucherNos = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        if(rowData[i].voucherFrom == '10'){
            if (wageVoucherNos == "") {
                wageVoucherNos += rowData[i].voucherNo;
            } else {
                wageVoucherNos += "," + rowData[i].voucherNo;
            }
        }
    }
    if(wageVoucherNos != ""){
        layer.alert(bt.lang('bdlink.message.wage.forbid',  [wageVoucherNos]), {icon: 2});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.queryResult'), function () {
        $.post(basePath + "/bdlink/bisexc/batchQuery", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
