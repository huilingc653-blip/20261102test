var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp', 'moneyType','mulselect','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.rateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    // 表格操作列下拉选择事件
    $("#appTable").on('change','.life-cycle-select',function () {
        var value = $.trim($(this).val())
        if(value){

            var url = basePath + "/foreign/fexTradingLifecycle/addLifecycle/"+value+'/'+$(this).data('id');
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
        colReorder: true,
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
                hide: bt.isnotResource('foreign:fextranreq:option:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:option:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:option:apply'),
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
            {data:"proId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.proName;
                }
            },
            {data:"corpId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.corpName;
                }
            },
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "expiresDate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.date.format(full.expiresDate, 'yyyy-MM-dd');
                }
            },
            {data: "settleDate", bSortable: true, className: 'center'},
            {data: "positionMoney", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.positionMoney);
                }
            },
            {data: "contractRate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.contractRate,{decimal:6});
                }
            },
            {data: "tranCurPairIdName", bSortable: true, className: 'center'},
            {
                data: "busDirection", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexTranDeal.deliverySign", full.busDirection);
                }
            },
            {
                data: "updownDirection", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fex.tranApp2.updownDirection", full.updownDirection);
                }
            },
            {data: "c", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexProduct.deliveryType", full.deliveryType);
                }},
            {
                data: "fexOption1", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexOption1", full.fexOption1);
                }
            },
            {
                data: "fexOptionType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexProduct.fexOptionType", full.fexOptionType);
                }
            },
            {data: "cotpName", bSortable: true},
            {data: "createDate", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexTranReqOption.status", full.status);
                }
            },
            {
                data: "blendSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexTranReq.blendSign", full.blendSign);
                }
            },
            {data: "billFromName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    //如果状态为暂存和打回，显示编辑、删除
                    if (full.status == "0" || full.status == "-1") {
                        if (bt.isResource('foreign:fextranreq:option:apply', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    //操作列有 1-失效 2-录入均价 3-行权 4-敲出 5-敲入 6-平仓 7-放弃行权
                    //如果是普通期权
                    if (full.fexOption1 == "0" && full.isShowLifeCycle == "1") {
                        if (full.status == '130'){//如果是存续，并且无暂存或审批中的生命周期数据 显示【行权】【放弃行权】【平仓】
                            html += creatSpotLifeCycleSelect('3,6,7',full.id);
                        }
                    }
                    //如果是亚式期权
                    if (full.fexOption2 == "1" && full.isShowLifeCycle == "1") {
                        if (full.status == '130') {//如果是存续，并且无暂存或审批中的生命周期数据 显示【录入均价】【平仓】
                            html += creatSpotLifeCycleSelect('2,6',full.id);
                        }else if(full.status == '97'){//如果是已确认均价，并且无暂存或审批中的生命周期数据 显示【行权】【放弃行权】【平仓】
                            html += creatSpotLifeCycleSelect('3,6,7',full.id);
                        }
                    }
                    //如果是障碍期权
                    /**
                     * 外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、
                     * 4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)
                     */
                    if (full.fexOption3 == "2" || full.fexOption3 == "3" || full.fexOption3 == "6") {
                        if (full.status == '130' && full.isShowLifeCycle == "1") {//如果是存续，并且无暂存或审批中的生命周期数据 显示【敲出】【行权】【放弃行权】【平仓】
                            html += creatSpotLifeCycleSelect('3,4,6,7',full.id);
                        } else if (full.status == '100' && full.isShowLifeCycle == "1") {//如果是已敲入，并且无暂存或审批中的生命周期数据 显示【行权】【放弃行权】【平仓】
                            html += creatSpotLifeCycleSelect('3,6,7',full.id);
                        }
                    }else if (full.fexOption3 == "1" || full.fexOption3 == "4" || full.fexOption3 == "5") {
                        if (full.status == '130' && full.isShowLifeCycle == "1") {//如果是存续，并且无暂存或审批中的生命周期数据 显示【敲入】【失效】【平仓】
                            html += creatSpotLifeCycleSelect('1,5,6',full.id);
                        }else if (full.status == '100' && full.isShowLifeCycle == "1") {//如果是已敲入，并且无暂存或审批中的生命周期数据 显示【行权】【放弃行权】【平仓】
                            html += creatSpotLifeCycleSelect('3,6,7',full.id);
                        }
                    }
                    if ("0" != full.status && "-1" != full.status) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="goLifeCycle(this)">' + bt.lang("table.fexTranReq.viewLifeCycle") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        btDrawCallback:function () {
            bt.dicts.init();
        }
    });

});

// 字典,存储操作下拉列
var lifeCycleDictList = bt.dictContext['fexProduct.options.lifeCycle'];
if(!lifeCycleDictList){
    throwError("字典组:fexProduct.options.lifeCycle不存在")
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

/**
 * 创建下拉内容
 * @param filterData
 * @param id
 * @returns {string}
 */
function creatSpotLifeCycleSelect(filterData,id) {
    var html = '<select lay-ignore class="life-cycle-select" data-id="'+id+'" style="display: inline-block;width: 80px;">';
    html +='<option value=" ">'+bt.lang("common.placeholder.select")+'</option>'
    for (var i = 0; i < data.length; i++) {
        var item = data[i]
        if(filterData && filterData.indexOf(item.value) == -1){

        }else {
            html +='<option value="'+item.value+'">'+item.text+'</option>'
        }
    }
    html +='</select>'
    return html
}

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

/**
 * 期权处理
 * @param el
 * @param opType 操作类型 1-失效，2-已确认均价，3-行权，4-敲出，5-敲入
 */
// function optionDeal(el, opType) {
//     var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
//     var viewData = bt.lang('common.view.data');
//     if(opType == "1"){
//         viewData = bt.lang('fextranreq.button.invalid');
//     }else if(opType == "2"){
//         viewData = bt.lang('fextranreq.button.enterAvg');
//     }else if(opType == "3"){
//         viewData = bt.lang('fextranreq.button.exercise');
//     }else if(opType == "4"){
//         viewData = bt.lang('fextranreq.button.knockOut');
//     }else if(opType == "5"){
//         viewData = bt.lang('fextranreq.button.knockIn');
//     }
//     if (bt.navtab) {
//         bt.navtab.addNavtab(viewData, basePath + "/foreign/fextranreq/addFexTranOptionDeal/"+opType+"/"+data.id);
//         return;
//     }
//     parent.layer.open({
//         type: 2,
//         area: ['850px', '530px'],
//         maxmin: true,
//         content: basePath + "/foreign/fextranreq/addFexTranOptionDeal/"+opType+"/"+data.id,
//         end: function () {
//             datatable && datatable.draw();
//         }
//     });
// }


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


