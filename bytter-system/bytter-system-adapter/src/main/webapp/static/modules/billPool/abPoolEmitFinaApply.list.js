var form, $, billPool, billPoolCorp, datatable, moneyType;
var idsValArray = new Array();
bt.use(['form', 'billPool', 'billPoolCorp', 'dateType', 'datatable', 'mulselect', 'dicts', 'moneyType', 'btImport'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/billpool/emitFinaApply/datatable"
        },
        buttons: [

            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    addEmitFina();
                }
            },
            {
                text: bt.lang('common.button.delete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchDelete(data);
                }
            },
            {
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //layer.alert(bt.lang('common.message.indevelopment'));
                    importExcel();
                }
            }
        ]
        , columns: [
            {data: "corpCodeAndName", bSortable: true},
            {data: "appNo", bSortable: true},
            {data: "pbCodeAndName", bSortable: true},
            {data: "billType", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }
            },
            {data: "billFlag", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }
            },
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(data);
                }
            },
            {data: "payeracc", bSortable: true},
            {data: "abs", bSortable: true},
            {data: "banendrsmtmk",bSortable: true, className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BANEM",full.banendrsmtmk);
                }
            },
            {
                data: "startDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {
                data: "endDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data: "payee", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "source", bSortable: true, className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_EMITBILL_SOURCE",full.source);
                }
            },
            {
                data: "payNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.payNo != "" && full.payNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewNisBillhead(this)">' + full.payNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"status",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_POOL_STATUS",full.status);
                }
            },
            {data:"optStatus",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_POOL_DTL_OPT_STATUS",full.optStatus);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.status == '0' || full.status == '-1' || full.optStatus == '-1') {
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="updateInfo(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="dtlView(this)">' + bt.lang("common.button.view") + '</a>';
                    if (full.conId && (full.conStatus != '2' || full.conOptStatus == '-1')) {
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="downloadFile(this)">' + bt.lang("contract.suspend.query") + '</a>';
                    }
                    if ((full.conId && full.conStatus == '0') || full.conOptStatus == '-1') {
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '"  onClick="regenerateContract(this)" >' + bt.lang("billPool.emitFinaApply.regenerateContract") + '</a>';
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="contractConfirm(this)">' + bt.lang("billPool.emitFinaApply.contractConfirm") + '</a>';
                    }

                    return html;
                }
            }
        ],
       /* fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            if (idsValArray.length > 0) {
                bt.datatable.onCheckRow("appTable", {
                    "key": "id",
                    "value": idsValArray,
                    "checked": true
                });
            }
            setSumMoney();
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '4',
                    dataName: 'billMoney'
                }
            ]
        }
    });

    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "a03c23089fee4a9fbdae1f53773ae1da",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/billpool/emitFinaApply/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }
        });
    }
});

// 新增界面跳转
function addEmitFina() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/billpool/emitFinaApply/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/emitFinaApply/add",
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 修改界面跳转
function updateInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/billpool/emitFinaApply/update?id=" + data.dtlId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/emitFinaApply/update?id=" + data.dtlId,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

//融资申请查看页面
function dtlView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param;
    if (data.dtlId) {
        param = "?id=" + data.dtlId;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billpool/emitFinaApply/view" + param,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//批量提交融资申请
function submitBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    for (i = 0; i < len; i++) {
        //付款过来暂存数据判断必须要暂存过才可以提交
        if (rowData[i].source == '10' && !rowData[i].endDate) {
            layer.alert(bt.lang('bill.emit.isBillPayadd.notTS', [(i + 1)]), {icon: 7});
            return;
        }
        if (ids) {
            ids = ids + "," + rowData[i].aid;
        } else {
            ids = rowData[i].aid;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        bt.loading.show();
        $.post(basePath + "/billpool/emitFinaApply/batchSubmit", {"ids": ids}, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
    bt.loading.hide();
}

function batchDelete(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    var reason = $("#reason").val();
    for (i = 0; i < len; i++) {
        //付款过来暂存数据判断必须要暂存过才可以提交
        if (rowData[i].source == '10' && isEmpty(reason)) {
            //付款票据的删除原因必填
            layer.alert(bt.lang('abemitbillback.backReasonRequired'), {icon: 7});
            return;
        }
        if (ids) {
            ids = ids + "," + rowData[i].aid;
        } else {
            ids = rowData[i].aid;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {//确定批量删除?
        bt.loading.show();
        $.post(basePath + "/billpool/emitFinaApply/batchDelete", {"ids": ids, "reason": reason}, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

// 重新生成合同
function regenerateContract(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var conId = data.conId;
    if (!conId) {
        layer.alert(bt.lang('billPool.emitFinaApply.contractConfirmWarn'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('billPool.emitFinaApply.regenerateContractWarn'), function () {
        bt.loading.show();
        $.post(basePath + "/billpool/emitFinaApply/regenerateContract", {"conId": conId}, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//合同确认
function contractConfirm(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var conId = data.conId;
    if (!conId) {
        layer.alert(bt.lang('billPool.emitFinaApply.contractConfirmWarn'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('billPool.emitFinaApply.contractConfirmWarn2'), function () {
        bt.loading.show();
        $.post(basePath + "/billpool/emitFinaApply/contractConfirm", {"conId": conId}, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

// 查看合同
function downloadFile(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data.conId) {
        window.location = basePath + "/billpool/emitFinaApply/viewContract?conId=" + data.conId;
    } else {
        layer.alert(bt.lang('billPool.emitFinaApply.contractConfirmWarn'), {icon: 7});
        return;
    }
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].dtlId) < 0) {
                    idsValArray.push(datas[i].dtlId);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].dtlId);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};


/**判断字符串是否为空*/
function isEmpty(ms) {
    if (typeof (ms) == "undefined" || ms == null || ms == "") {
        return true;
    }
    return false;
}

//付款单查看
function viewNisBillhead(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.payId
    });
}
