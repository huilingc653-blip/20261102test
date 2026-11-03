bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType', 'mulselect', 'element', 'selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    var rateDecimalPlaces = parseInt($('#rateDecimalPlaces').val(), 10);
    if (isNaN(rateDecimalPlaces)) {
        rateDecimalPlaces = 6;
    }
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexProfit/lossManage/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fexProFitLossManage:op'),
                text: bt.lang('table.fexProfitLossManage.update'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: false, className: 'center'},
            {data: "tranTypeName", bSortable: false, className: 'center',},
            {data: "corpName", bSortable: false, className: 'center',},
            {data: "tranCurPairName", bSortable: false, className: 'center',},
            {
                data: "busDirection", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fex.tranApp.busDirection", full.busDirection);
                }
            },
            {
                data: "updownDirection", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fex.tranApp2.updownDirection", full.updownDirection);
                }
            },
            {data: "quotationDate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(!full.quotationDate){
                        return "";
                    }
                    return bt.date.format(full.quotationDate, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data: "settleDate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(!full.settleDate){
                        return "";
                    }
                    return bt.date.format(full.settleDate, "yyyy-MM-dd");
                }},
            {data: "positionMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.positionMoney){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: 2
                    }
                    return moneyFun.format(full.positionMoney, opt);
                }},
            {
                data: "contractRate", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.contractRate){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: rateDecimalPlaces
                    }
                    return moneyFun.format(full.contractRate, opt);
                }
            },
            {
                data: "referenceInterestRate", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.referenceInterestRate){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: rateDecimalPlaces
                    }
                    return moneyFun.format(full.referenceInterestRate, opt);
                }
            },
            {data: "curEnName", bSortable: false, className: 'center',},
            {data: "floatMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.floatMoney){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: 2
                    }
                    return moneyFun.format(full.floatMoney, opt);
                }},
            {data: "convertFloatMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.convertFloatMoney){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: 2
                    }
                    return moneyFun.format(full.convertFloatMoney, opt);
                }},
            {data: "realMoneyTotal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.realMoneyTotal){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: 2
                    }
                    return moneyFun.format(full.realMoneyTotal, opt);
                }},
            {data: "convertRealMoneyTotal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!full.convertRealMoneyTotal){
                        return "";
                    }
                    var opt = {
                        separator: ",",
                        decimal: 2
                    }
                    return moneyFun.format(full.convertRealMoneyTotal, opt);
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('foreign:fexProFitLossManage:op')) {
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ],
    });
});

//编辑数据
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexProfit/lossManage/update/" + data.reqId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexProfit/lossManage/update/" + data.reqId,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//删除数据
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/foreign/fexProfit/lossManage/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//查看数据
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexProfit/lossManage/view/" + data.reqId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexProfit/lossManage/view/" + data.reqId,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function submitBatch(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchUpdate'),function(){
        $.post(basePath + "/foreign/fexProfit/lossManage/batchUpdate",{ids:ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
