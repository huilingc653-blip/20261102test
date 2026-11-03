var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'bank', 'date', 'corp', 'dicts', 'accountTable','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.dateType.init();
    var dict = bt.dicts.init();
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
                hide: bt.isnotResource('netbank:bdLink:bisExc:sendApply'),
                text: bt.lang('common.button.batchSend'),
                action: function (e, dt, node, config) {
                    batchSend(e, dt, node, config);
                }
           /* }, {
                hide: bt.isnotResource('netbank:bdLink:bisExc:sendApply'),
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
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
                        $.post(basePath + "/bdlink/bisexc/batchBack", {ids: ids}, function (result) {
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }*/
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
            {data: "reqDate", bSortable: true, className: 'center'},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {
                data: "cbsBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8" && full.voucherFrom != "11") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "busOriTypeName", bSortable: true, className: 'center'},
            {data: "voucherStatName", bSortable: true, className: 'center'},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bdLink:bisExc:sendApply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="sendColumn(this)">' + bt.lang("common.button.send") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backColumn(this)">' + bt.lang("common.button.return") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a>';
                    // if (full.voucherFrom && full.voucherFrom == "9") {
                    //     html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    // }
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8" && full.voucherFrom != "11") {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + bt.lang("common.button.viewBusBill") + '</a>';
                    }
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

function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/bdlink/bisexc/edit/" + data.id);
        return;
    }
}

//发送
function sendColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    //校验期望支付日期是否超过系统当前日期，给出相应的提示
    var msg = "common.message.confirm.send";
    var wishPayday = bt.date.format(data.wishPayday, 'yyyy-MM-dd');
    var nowDate = bt.date.format(new Date(), 'yyyy-MM-dd');
    if (bt.date.compare(wishPayday, nowDate) > 0) {
        msg = "bdlink.message.wishpayday.send";
    }

    //校验对私明细金额与付款金额是否一致
    var returnCbsBillNo = "";
    $.ajax({
        url: basePath + "/bdlink/bisexc?validPayMoney",
        method: 'POST',
        async: false,
        data: {
            ids: data.id
        },
        dataType: 'json',
        success: function (data) {
            returnCbsBillNo = data.obj;
        }
    });
    if(returnCbsBillNo){
        layer.alert(bt.lang('bdlink.message.paymoney.mismatch', [returnCbsBillNo]), {icon: 2});
        return false;
    }

    $.ajax({
        url: basePath + "/bdlink/bisexc?rePay",
        method: 'POST',
        async: false,
        data: {
            id: data.id
        },
        dataType: 'json',
        success: function (result) {
            if(result.success == true){
                msg = result.msg;
                if(result.obj) {
                    msg += "[" + bt.lang('table.bisExc.voucherNo') + result.obj.voucherNo + "；" + bt.lang('table.bisExc.payeeAcc') + result.obj.payeeAcc + "；" + bt.lang('table.bisExc.payMoney') + bt.money.format(result.obj.payMoney) + "]";
                }
            }
        }
    });
    //证书签名
    var signDataMsg = signData("付款指令发送", 6);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang(msg), function () {
        $.post(basePath + "/bdlink/bisexc/send/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
    return false;
}

/**
 * 批量发送
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchSend(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    var flag = false;
    var nowDate = bt.date.format(new Date(), 'yyyy-MM-dd');
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        var wishPayday = bt.date.format(rowData[i].wishPayday, 'yyyy-MM-dd');
        if (bt.date.compare(wishPayday, nowDate) > 0) {
            flag = true;
        }
    }
    //校验对私明细金额与付款金额是否一致
    var returnCbsBillNo = "";
    $.ajax({
        url: basePath + "/bdlink/bisexc?validPayMoney",
        method: 'POST',
        async: false,
        data: {
            ids: ids
        },
        dataType: 'json',
        success: function (data) {
            returnCbsBillNo = data.obj;
        }
    });
    if(returnCbsBillNo){
        layer.alert(bt.lang('bdlink.message.paymoney.mismatch', [returnCbsBillNo]), {icon: 2});
        return false;
    }

    var msg = "common.message.confirm.send";
    if (flag) {
        msg = "bdlink.message.wishpayday.send";
    }
    $.ajax({
        url: basePath + "/bdlink/bisexc?batchRePay",
        method: 'POST',
        async: false,
        data: {
            ids: ids
        },
        dataType: 'json',
        success: function (result) {
            if(result.success == true){
                msg = result.msg;
                if(result.obj) {
                    msg += "[" + bt.lang('table.bisExc.voucherNo') + result.obj.voucherNo + "；" + bt.lang('table.bisExc.payeeAcc') + result.obj.payeeAcc + "；" + bt.lang('table.bisExc.payMoney') + bt.money.format(result.obj.payMoney) + "]";
                }
            }
        }
    });
    //证书签名
    var signDataMsg = signData("付款指令发送", 6);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang(msg), function () {
        $.post(basePath + "/bdlink/bisexc/batchSend", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
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
        data: {
            cbsBillNo: data.cbsBillNo,
            cbsBillId: data.cbsBillId,
            voucherFrom: data.voucherFrom,
            busOriType: data.busOriType
        },
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


//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    var action = "/netbank/nisbillhead/view/" + cbsBillId;
    //资金调拨
    if (data.voucherFrom != "1" && data.voucherFrom != "10" && data.voucherFrom != "12") {
        action = "/allocation/nisallocationreq/view/" + cbsBillId;
    }
    //代发工资
    if (data.voucherFrom == "10") {
        action = "/wage/niswage/view/" + cbsBillId;
    }
    //资金需求单
    if(data.voucherFrom == "12"){
        action = "/netbank/nisdemand/view/" + cbsBillId;
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
