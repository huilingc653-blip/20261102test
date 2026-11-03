var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'moneyType','mulselect','allBankInfo'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.allBankInfo.init();

    bt.cotpBankCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#cotpName").val(data.bankName);
            } else if (data.type === "outerBank") {
                $("#cotpName").val(data.bankName);
            }
        } else {
            $("#cotpName").val("");
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
                hide: bt.isnotResource('foreign:fextranreq:foreign:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:foreign:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:foreign:apply'),
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
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "proName", bSortable: true},
            {
                data: "fexBusType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexBusType", full.fexBusType);
                }
            },
            {
                data: "fexTranType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexTranType", full.fexTranType);
                }
            },
            {
                data: "corpId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.corpName;
                }
            },
            {
                data: "buyCurId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.buyEnglishCode;
                }
            },
            {
                data: "buyMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.buyMoney);
                }
            },
            {
                data: "sellCurId", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.sellEnglishCode;
                }
            },
            {
                data: "sellMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.sellMoney);
                }
            },
            {
                data: "contractRate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {

                    var opt = {
                        separator: ",",
                        decimal: 6
                    }
                    return moneyFun.format(full.contractRate, opt);
                }
            },
            {data: "cotpName", bSortable: true},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "accNature", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranReq.accNature", full.accNature);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("choBankFinance.status", full.status);
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
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.status == "0" || full.status == "-1") {
                        if (bt.isResource('foreign:fextranreq:foreign:apply', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }else if(full.status == "95" && !full.relBillNo){
                        /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
                        var opType = full.opType;
                        if(full.fexTranType == "1"){
                            // 外汇交易类型：1-远期
                            if(opType=="0" || opType=="1") {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="delivery(this)">' + bt.lang("fextranreq.button.delivery") + '</a><span class="text-explode">|</span>';
                            }
                            if(opType=="0" || opType=="1" || opType=="2") {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="defaulted(this)">' + bt.lang("fextranreq.button.default") + '</a><span class="text-explode">|</span>';
                            }
                        }else if(full.fexTranType == "0"){
                            // 外汇交易类型：0-即期 固定金额 单选买入/卖出
                            if(full.isShowRateConfirm == "1"){
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="rateConfirm(this)">' + bt.lang("fextranreq.button.rateconfirm") + '</a><span class="text-explode">|</span>';
                            }
                        }
                        //交割类型：0-本金交割 1-无本金交割
                        if(full.deliveryType == "1"){
                            if(opType=="0" || opType=="3") {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="ndfDelivery(this)">' + bt.lang("fextranreq.button.ndfdelivery") + '</a><span class="text-explode">|</span>';
                            }
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
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
    });
}

function clone(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreq/clone/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/clone/" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fextranreq/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fextranreq/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fextranreq/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function delivery(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.delivery'), basePath + "/foreign/fextranreq/addFexTranDeal/1/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/addFexTranDeal/1/"+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function defaulted(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.default'), basePath + "/foreign/fextranreq/addFexTranDeal/2/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/addFexTranDeal/2/"+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function ndfDelivery(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.ndfdelivery'), basePath + "/foreign/fextranreq/addFexTranDeal/3/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/addFexTranDeal/3/"+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function rateConfirm(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.rateconfirm'), basePath + "/foreign/fextranreq/addFexTranDeal/4/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/addFexTranDeal/4/"+data.id,
        end: function () {
            datatable && datatable.draw();
        }
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
    //证书签名
    var encrypt = $("#encrypt").val(); // cfca证书
    if(encrypt == 2 || encrypt == 1){ // 1 天威证书
        signData("外汇交易提交", 11,function () {
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                bt.loading.show();
                $.post(basePath + "/foreign/fextranreq/batchSubmit", {ids: ids}, function (result) {
                    bt.loading.hide();
                    var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                    datatable.draw();
                    parent.layer.alert(result.msg, {icon: icon});
                }, 'JSON');
            });
        })
    }else{
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
            bt.loading.show();
            $.post(basePath + "/foreign/fextranreq/batchSubmit", {ids: ids}, function (result) {
                bt.loading.hide();
                var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                datatable.draw();
                parent.layer.alert(result.msg, {icon: icon});
            }, 'JSON');
        });
    }
    return false;
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
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo
    });
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}
