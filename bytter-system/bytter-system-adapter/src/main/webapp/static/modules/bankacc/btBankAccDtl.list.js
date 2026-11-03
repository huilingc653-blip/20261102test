bt.use(['form', 'datatable', 'select', 'dateType', 'moneyType', 'btImport','dicts', 'accountTable','selectType','bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });
    var selectType = bt.selectType.init();
    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    };
    bt.accountTable.init();

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000007",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/btbankaccdtl/saveImport",
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

    /**
     * 导入功能
     */
    importRpaExcel = function () {
        up = bt.btImport.init({
            // url: basePath+"/import/excel/uploadRpa",
            code: "100000072",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/btbankaccdtl/saveRpaImport",
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

    //智能导入
    importByConfig = function () {
        up = bt.btImport.init({
            url: basePath+"/import/excel/uploadCustom",
            customParameUrl:basePath +"/bankacc/btbankaccdtl/importByConfig",
            code:"100000071",
            width:"100%",
            height:"100%",
            hideDownTempBtn:true,
            success:function(head,data,select,cacheCode){
                bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath + "/bankacc/btbankaccdtl/saveImportByConfig",
                    dataType:"json",
                    data:{"cacheCode":cacheCode},
                    success:function(result){
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        bt.loading.hide();
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }
        });
    }

    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        lengthMenu: [10, 25, 50, 100, 500,1000],//每页显示多少条
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox: true,
        order:[[5, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/btbankaccdtl/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: 'RPA导入',
                action: function (e, dt, node, config) {
                    importRpaExcel();
                }
            },{
                text: bt.lang('bankacc.button.imports.config'),
                hide:bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                action: function ( e, dt, node, config ) {
                    importByConfig();
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    batchSubmit(e, dt, node, config);
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:defeat'),
                text: bt.lang('common.button.batchDefeat'),
                action: function (e, dt, node, config) {
                    batchDefeat(e, dt, node, config);
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
                text: bt.lang('bankacc.button.updateBal'),
                action: function (e, dt, node, config) {
                    updateBal();
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('table.bisAccBal.btn.generate'),
                action: function (e, dt, node, config) {
                    generateBalance();
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
            {data: "accNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "transTime", bSortable: true},
            {
                data: "amtIn", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.amtIn);
                }
            },
            {
                data: "amtOut", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.amtOut);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.bal);
                }
            },
            {
                data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {data: "curName", bSortable: true},
            {data: "bankSerialNo", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("cgsBill.status", full.status);
                }
            },
            {data: "createDate", bSortable: true},
            {data: "projectRemark", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                // }
                if (bt.isResource('netbank:bankAcc:btBankAccDtl:apply',full.corpId)) {
                    if(full.status == 0 || full.status == -1) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                }
                //变更
                if (bt.isResource('netbank:bankAcc:btBankAccDtl:change',full.corpId)) {
                    if (full.status == 95) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("netbank.button.change") + '</a><span class="text-explode">|</span>';
                    }
                }
                //作废
                if (bt.isResource('netbank:bankAcc:btBankAccDtl:defeat',full.corpId)) {
                    if (full.status == 95) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defeatColumn(this)">' + bt.lang("common.button.defeat") + '</a><span class="text-explode">|</span>';
                    }
                }
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                // html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
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
            sumTextCol:2,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5',
                    dataName: 'amtIn'
                },
                {
                    colNum: '6',
                    dataName: 'amtOut'
                },
                {
                    colNum: '7',
                    dataName: 'bal'
                }
            ]
        }
    });

    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.btBankAccDtl.btn.create'), basePath + "/bankacc/btbankaccdtl/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccdtl/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankaccdtl/view/" + data.id
        });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.btBankAccDtl.btn.update'), basePath + "/bankacc/btbankaccdtl/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankaccdtl/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/btbankaccdtl/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function defeatColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.tovoid'), function () {
        $.post(basePath + "/bankacc/btbankaccdtl/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDelete(e, dt, node, config) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bankacc/btbankaccdtl/batchDelete/", {ids: ids,opType:'0'}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 批量作废
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDefeat(e, dt, node, config) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchTovoid'), function () {
        $.post(basePath + "/bankacc/btbankaccdtl/batchDelete/", {ids: ids,opType:'1'}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 批量删除
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchSubmit(e, dt, node, config) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/bankacc/btbankaccdtl/batchSubmit", {ids: ids}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function generateBalance() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.bisAccBal.btn.generate'), basePath + "/bankacc/bisaccbal/add?addFlag=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/add?addFlag=1",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function updateBal() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('bankacc.button.updateBal'), basePath + "/bankacc/btbankaccdtl/updateBal");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccdtl/updateBal",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}