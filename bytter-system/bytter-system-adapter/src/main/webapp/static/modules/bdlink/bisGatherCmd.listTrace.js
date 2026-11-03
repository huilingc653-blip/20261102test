var datatable;
bt.use(['form', 'datatable', 'selectType', 'moneyType', 'dicts', 'bank', 'dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.dateType.init();
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
            'url': basePath + "/bdlink/bisgathercmd/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bdLink:bisGatherCmd:traceResend'),
                text: bt.lang('common.button.batchResend'),
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
                    parent.layer.confirm(bt.lang('common.message.confirm.resend'), function () {
                        $.post(basePath + "/bdlink/bisgathercmd/batchResend", {ids: ids}, function (result) {
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisGatherCmd:traceBack'),
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
                    parent.layer.prompt({
                        title: bt.lang('table.bisExc.backMsg'),
                        formType: 2
                    },function(backMsg){
                        $.post(basePath + "/bdlink/bisgathercmd/batchBack", {ids: ids,backMsg:backMsg}, function (result) {
                            datatable && datatable.draw();
                            parent.layer?parent.layer.closeAll():layer.closeAll();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }, {
                hide: bt.isnotResource('netbank:bdLink:bisGatherCmd:traceDefeat'),
                text: bt.lang('common.button.batchDefeat'),
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
                    parent.layer.confirm(bt.lang('common.message.confirm.defeat'), function () {
                        $.post(basePath + "/bdlink/bisgathercmd/batchDefeat", {ids: ids}, function (result) {
                            datatable.draw();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
                /*},{
                    hide:bt.isnotResource('netbank:bdLink:bisGatherCmd:traceApply'),
                    text: bt.lang('common.button.batchRecover'),
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
                         parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
                             $.post(basePath + "/bdlink/bisgathercmd/batchRecover",{ids:ids}, function(result){
                                datatable.draw();
                                parent.layer.alert(result.msg, {icon: result.success?1:2});
                            },'JSON');
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
            {data: "parentAcc", bSortable: true},
            {data: "parentAccName", bSortable: true},
            {data: "parentBank", bSortable: false},
            {
                data: "gatherAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.gatherAmt);
                }
            },
            {data: "childAcc", bSortable: true},
            {data: "childAccName", bSortable: true},
            {data: "childBank", bSortable: false},
            {data: "runTime", bSortable: true, className: 'center'},
            {
                data: "cbsBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBill(this)">' + full.cbsBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "cmdTypeName", bSortable: true, className: 'center'},
            {data: "busOriTypeName", bSortable: true, className: 'center'},
            {data: "voucherFromName", bSortable: true, className: 'center'},
            {data: "cmdStatName", bSortable: true, className: 'center'},
            {data: "returnTime", bSortable: true, className: 'center'},
            {data: "firstReturnMsg", bSortable: true},
            {data: "nextReturnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.cmdStat == '1') {
                        if (bt.isResource('netbank:bdLink:bisGatherCmd:setResult', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="setPayResultColumn(this, 0)">' + bt.lang("common.button.paySuccess") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('netbank:bdLink:bisGatherCmd:setResult', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="setPayResultColumn(this, 2)">' + bt.lang("common.button.payFailure") + '</a><span class="text-explode">|</span>';
                        }
                    }else{
                        if (bt.isResource('netbank:bdLink:bisGatherCmd:traceResend', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="resendColumn(this)">' + bt.lang("common.button.resend") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('netbank:bdLink:bisGatherCmd:traceDefeat', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeatColumn(this)">' + bt.lang("common.button.defeat") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('netbank:bdLink:bisGatherCmd:traceBack', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backColumn(this)">' + bt.lang("common.button.return") + '</a><span class="text-explode">|</span>';
                            /*html += '<a class="" href="#" data-row="'+meta.row+'" onclick="recoverColumn(this)">'+bt.lang("common.button.recover")+'</a><span class="text-explode">|</span>';*/
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (full.cbsBillNo != "" && full.cbsBillNo != null && full.voucherNo.substring(0, 1) != "8" && full.voucherFrom != "8") {
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
                    colNum: '7',
                    dataName: 'gatherAmt'
                }
            ]
        }
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisgathercmd/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisgathercmd/view/" + data.id
    });
}

//重发
function resendColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.resend'), function () {
        $.post(basePath + "/bdlink/bisgathercmd/resend/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//作废
function defeatColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.defeat'), function () {
        $.post(basePath + "/bdlink/bisgathercmd/defeat/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//退回
function backColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.prompt({
        title: bt.lang('table.bisExc.backMsg'),
        formType: 2
    },function(backMsg){
        $.post(basePath + "/bdlink/bisgathercmd/back", {id:data.id,backMsg:backMsg}, function (result) {
            datatable && datatable.draw();
            parent.layer?parent.layer.closeAll():layer.closeAll();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//恢复
/*function recoverColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
        $.post(basePath + "/bdlink/bisgathercmd/recover/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}*/

//设为支付成功/失败
function setPayResultColumn(el, voucherStat) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisgathercmd/setPayResult/" + data.id + "/" + voucherStat
    });
}

//查看业务单据详情
function viewBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var cbsBillId = data.cbsBillId;
    if (cbsBillId.indexOf("_D") > -1) {
        cbsBillId = cbsBillId.replace("_D", "");
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/allocation/nisallocationreq/view/" + cbsBillId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/nisallocationreq/view/" + cbsBillId
    });
}
