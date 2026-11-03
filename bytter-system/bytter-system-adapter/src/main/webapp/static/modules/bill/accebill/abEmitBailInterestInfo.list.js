var billTable = {};
var idsValArray = [];
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'btImport', 'dicts','mulselect','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.mulselect.init();
    bt.accountTable.init({urldata:{accountType :'2,3',electricBill:'1'}});
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[5, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/bailInterestInfo/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide: bt.isnotResource('bill:accebill:abemitbill:bailInterestInfo:opreate'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('table.emitBill.bailInterest.calculatedInterest'),
                hide: bt.isnotResource('bill:accebill:abemitbill:bailInterestInfo:opreate'),
                action: function (e, dt, node, config) {
                    calculatedInterest();
                }
            },
            {
                text: bt.lang('table.emitBill.bailInterest.calculatedInterest.batchSure'),
                hide: bt.isnotResource('bill:accebill:abemitbill:bailInterestInfo:opreate'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSure(data);
                }
            },
            {
                text: bt.lang('table.emitBill.bailInterest.calculatedInterest.batchNotSure'),
                hide: bt.isnotResource('bill:accebill:abemitbill:bailInterestInfo:opreate'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchNotSure(data);
                }
            },
            {
                text: bt.lang('table.emitBill.bailInterest.calculatedInterest.pushSharing'),
                hide: bt.isnotResource('bill:accebill:abemitbill:bailInterestInfo:opreate'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    pushSharing(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "id", bSortable: false},
            {data: "corpName", bSortable: false},
            {data: "bankAcc", bSortable: false},
            {data: "bankTypeName", bSortable: false},
            {data: "currentMonth", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {
                data: "bailMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bailMoney);
                }
            },
            {
                data: "dueSettlementAmount", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.dueSettlementAmount);
                }
            },
            {
                data: "actualSettlementAmount", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.actualSettlementAmount);
                }
            },
            {
                data: "interestMargin", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.interestMargin);
                }
            },
            {data: "lastOperator", bSortable: false},
            {data: "statusName", bSortable: true},
            {data: "pushStatusName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('bill:accebill:abemitbill:bailInterestInfo:opreate', full.applyCorpId) && full.status != 11) {
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
});

//手动新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/bill/accebill/bailInterestInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/bailInterestInfo/add",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

//计算利息
function calculatedInterest() {
    parent.layer.open({
        type: 2,
        area: ['850px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/bailInterestInfo/addCalculatedInterest",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//批量确认
function batchSure(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.bailInterestInfo.batchSure'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
        }
        $.post(basePath + "/bill/accebill/bailInterestInfo/batchSure", {
            "ids": ids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//批量反确认
function batchNotSure(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.bailInterestInfo.batchNotSure'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
            if(rowData[i].pushStatus == '2'){
                layer.alert(bt.lang('common.message.confirm.bailInterestInfo.has.push'), {icon: 7});
                return;
            }
        }
        $.post(basePath + "/bill/accebill/bailInterestInfo/batchNotSure", {
            "ids": ids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//推送共享
function pushSharing(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.bailInterestInfo.pushSharing'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
            if(rowData[i].pushStatus != '0'){
                layer.alert(bt.lang('bailInterestInfo.pushStatus.is.not.temp'), {icon: 7});
                return;
            }
        }
        $.post(basePath + "/bill/accebill/bailInterestInfo/pushSharing", {
            "ids": ids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//跳转到编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bill/accebill/bailInterestInfo/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/bailInterestInfo/update?id=" + data.id,
            end:function(){
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

//删除
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data.status == '1'){
        layer.alert(bt.lang('accebill.message.error.isSureInfo.can.not.delete'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        if(data.mark === "1"){
            layer.alert(bt.lang('accebill.message.error.claimaccount.validsign'), {icon: 7});
            return;
        }else{
            $.post(basePath + "/bill/accebill/bailInterestInfo/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if(result.success){
                    bt.navtab.refreshParent();
                    bt.navtab.refreshCurrent()
                }
            }, 'JSON');
        }
    });
}

//票据单位账号回调
function bankAccCallBack(data){
    var $ = bt.jquery;
    $("#bankAcc").val(data.bankAcc);
}