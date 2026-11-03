var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'moneyType','mulselect','allBankInfo'], function () {
    $ = bt.jquery, form = bt.form();
    if (bt.dictContext) {
        if (!bt.dictContext['fexTranReqOption.status']) {
            bt.dictContext['fexTranReqOption.status'] = {};
        }
        if (!bt.dictContext['fexTranReqOption.status']['115']) {
            bt.dictContext['fexTranReqOption.status']['115'] = {name: '到期交割', code: '115', index: 115};
        }
    }
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
    // 表格操作列下拉选择事件
    $("#appTable").on('change','.life-cycle-select',function () {
        var value = $.trim($(this).val())
        if(value){

            var url = basePath + "/foreign/fexTradingLifecycle//addLifecycle/"+value+'/'+$(this).data('id');
            if (bt.navtab) {
                bt.navtab.addNavtab($(this).find('option:selected').text(), url);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: url
            });
        }
    })

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        resize:false,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexTradingReq/datatableForeign"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fextranreq:foreign:spot:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:foreign:spot:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    submitBatch(dt.rows('.selected').data());
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:foreign:spot:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    deleteBatch(dt.rows('.selected').data());
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:combination:apply'),
                text: bt.lang('common.button.batchTovoid'),
                action: function (e, dt, node, config) {
                    cancelBatch(dt.rows('.selected').data());
                }
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
                    return moneyFun.format(full.contractRate, {separator: ",",decimal: 6});
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
                   return bt.dict("fexTranReqOption.status", String(full.status));
                }
            },
            {
                data: "voucherStat", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.voucherStat != null && full.voucherStat !== '') {
                        return bt.dict("bisExc.voucherStat", full.voucherStat);
                    }
                    return '';
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
                        return '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelBill(this)">' + full.relBillNo + '</a>';
                    }
                    return '';
                }
            },
            {data: "tranCurPairIdName", bSortable: true, className: 'center'},
            {
                data: "busDirection", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fex.tranApp.busDirection", full.busDirection);
                }
            },
            {
                data: "positionMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.positionMoney);
                }
            },
            {
                data: "blendSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexTranReq.blendSign", full.blendSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<div style="min-width: 300px;">';
                    if (full.status == "0" || full.status == "-1") {
                        if (bt.isResource('foreign:fextranreq:foreign:spot:apply')) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }else if(full.status == "117" && full.isShowLifeCycle == "1"){ // 汇率确认
                        html += creatSpotLifeCycleSelect('12',full.id)
                    } else if("130" == full.status && full.isShowLifeCycle == "1"){
                        html += creatSpotLifeCycleSelect('6,11',full.id)
                    } else if("95" == full.status && full.isShowChange=='1') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">bt.lang("choBankFinance.chg")</a>' +
                            '<span class="text-explode">|</span>';
                    }
                    if ("0" != full.status) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a><span class="text-explode">|</span>';
                    }
                    if ("0" != full.status && "-1" != full.status) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="goLifeCycle(this)">' + bt.lang("table.fexTranReq.viewLifeCycle") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a></div>';
                    return html;
                }
            }
        ],
        btDrawCallback:function () {
            bt.dicts.init();
        }
    });
    // 字典
    var lifeCycleDictList = bt.dictContext['fexProduct.lifeCycle'];
    if(!lifeCycleDictList){
        throwError("字典组:fexProduct.lifeCycle不存在")
    }
    var data = [];
    for(var key in lifeCycleDictList){
        var value = lifeCycleDictList[key];
        data.push({
            value:key,
            text:value.name,
            index:value.index
        });
    }
    data.sort(function(a,b){
        return a.index - b.index;
    });

    function creatSpotLifeCycleSelect(filterData,id) {
        // 确保filterData是数组，避免indexOf报错
        var filterArr = [];
        if (typeof filterData === 'string' && filterData.trim() !== '') {
            filterArr = filterData.split(',').map(item => Number(item.trim())).filter(num => !isNaN(num));
        }
        var html = '<select lay-ignore class="life-cycle-select" data-id="' + id + '" style="display: inline-block;width: 80px;">';
        html += '<option value="">' + bt.lang("common.placeholder.select") + '</option>'; // 空值默认选项

        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            var itemValue = Number(item.value);
            if (filterArr.includes(itemValue)) {
                html +='<option value="'+item.value+'">'+item.text+'</option>';
            }
        }
        html += '</select>';
        html += '<span class="text-explode">|</span>';
        return html;
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTradingReq/add/spot");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingReq/add/spot/1",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function clone(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTradingReq/clone/spot/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingReq/clone/spot/" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingReq/view/spot/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingReq/view/spot/" + data.id
    });
}

function goLifeCycle(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexTradingLifecycle/list?relBillNo=" + data.billNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingLifecycle/list?relBillNo=" + data.billNo
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexTradingReq/update/spot/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexTradingReq/update/spot/" + data.id,
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
function rateConfirm(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.rateconfirm'), basePath + "/foreign/fexTradingLifecycle/addLifecycle/12/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexTradingLifecycle/addLifecycle/12/"+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function maturityDelivery(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.maturity.delivery'), basePath + "/foreign/fextranlifecycle/update?opType=11&reqId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranlifecycle/update?opType=11&reqId="+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function deferred(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.deferred'), basePath + "/foreign/fextranlifecycle/update?opType=10&reqId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath +"/foreign/fextranlifecycle/update?opType=10&reqId="+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function closingPosition(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.close.position'), basePath + "/foreign/fextranlifecycle/update?opType=6&reqId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranlifecycle/update?opType=6&reqId="+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function advance(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.advance'), basePath + "/foreign/fextranlifecycle/update?opType=9&reqId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranlifecycle/update?opType=9&reqId="+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function earlyDelivery(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('fextranreq.button.early.delivery'), basePath + "/foreign/fextranlifecycle/update?opType=8&reqId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranlifecycle/update?opType=8&reqId="+data.id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function updateRmk(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/updateRmk?id="+data.id,
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
        if (rowData[i].status != "0"&&rowData[i].status != "-1") {
            layer.alert("仅可操作暂存与打回的单据，请重新选择", {icon: 7});
            return;
        }
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
        if (rowData[i].status != "0"&&rowData[i].status != "-1") {
            layer.alert("仅可操作暂存与打回的单据，请重新选择", {icon: 7});
            return;
        }
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

function cancelBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].status < 95) {
            layer.alert("仅可作废审批通过的单据，请重新选择", {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('确认作废交易单据以及损益、生命周期、结算管理的相关信息？'), function (index) {
        parent.layer.close(index);
        parent.layer.prompt({
            title: bt.lang('作废原因'),
            formType: 2
        }, function (cancelReason, promptIndex) {
            $.post(basePath + "/foreign/fextranreq/batchCancel", {ids: ids, cancelReason: cancelReason}, function (result) {
                parent.layer.close(promptIndex);
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
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
