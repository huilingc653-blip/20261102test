var billTable = {};
var idsValArray = [];
bt.use(['form', 'datatable', 'corp', 'dicts', 'moneyType', 'btdate', 'element', 'date','mulselect','rateType','ratioType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.mulselect.init();
    bt.btdate.init({
        elem: "#startDate",
        startname: "ged_startDate",
        endname: "led_startDate"/*,
        endDefaultVal:bt.date.format(new Date(), 'yyyy-MM-dd')*/
    });
    bt.btdate.init({
        elem: "#endDate",
        startname: "ged_endDate",
        endname: "led_endDate"
    });
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        scrollY: '455px',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abemitbillpay/datatable"
        },
        exportFile: {//输入文档设置
            printTitle: '',//设置打印标题 默认无标题
            outputTitle: function () {//设置导出标题 默认为父级当前选项卡内容，也可以传入方法
                return bt.lang('common.button.cashing.offline')
            },
            outputFilename: bt.lang('common.button.cashing.offline')//设置导出文件名称  默认为父级选项卡内容或标题名称
        },
        buttons: [
            {
                text: bt.lang('common.button.cashing.online'),
                hide: bt.isnotResource('bill:accebill:abemitbillpay:confirm'),
                action: function (e, dt, node, config) {
                    var selectData = getSelectData(); //获取选中数据
                    batchPayOnline(selectData);
                }
            },
            {
                text: bt.lang('common.button.cashing.offline'),
                hide: bt.isnotResource('bill:accebill:abemitbillpay:confirm'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    cashingConfirmation(data);
                }
            }
            /*,
            {
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                    exports();
                }
            }*/
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "insideSingName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpIdName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpIdName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "payeracc", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {
                data: "bailpercent", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html="";
                    if (data || data == "0") {
                        html = bt.rateType.getFun().format(full.bailpercent, 'percent', 2, ',');
                    }
                    return html;
                }
            },
            {
                data: "bailmoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '0.00';
                }
            },
            {
                data: "endPayMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(data ? data : 0);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "payPayeeacc", bSortable: true},
            {data: "payPayeeName", bSortable: true},
            {data: "payPayeeBankNo", bSortable: true},
            {data: "payPayeeBankName", bSortable: true},
            {data: "provName", bSortable: true},
            {
                data: "cityName", bSortable: true
            },
            {
                data: "payUse", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div   name="payUse" id="payUse' + full.accId + '" >&nbsp;&nbsp;&nbsp' + (data || '') + '</div>';
                    } else {
                        html = '<input class="layui-input" style="width:120px;" value="' + (data || '') + '" name="payUse" id="payUse' + full.id + '"  data-maxlength="250"></input>';
                    }
                    return html;
                }
            },
            {
                data: "payAbs", bSortable: true,
                render: function (data, type, full, meta) {
                    var html;
                    if (full.status == 11 || full.status == 94) {
                        html = '<div   name="payAbs" id="payAbs' + full.accId + '" >&nbsp;&nbsp;&nbsp' + (data || '') + '</div>';
                    } else {
                        html = '<input class="layui-input" style="width:120px;" value="' + (data || '') + '" name="payAbs" id="payAbs' + full.id + '"  data-maxlength="250"></input>';
                    }
                    return html;
                }
            },
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"batchNo",bSortable:true},
            {data:"blRmtrUnnIdNo",bSortable:true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (bt.isResource('bill:accebill:abemitbillpay:confirm', full.applyCorpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                        // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                        if (data.status != 11) {
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="payOnline(this)">' + bt.lang("common.button.cashing.online") + '</a>';
                        }
                    }
                    return html;
                }
            }
        ],
        /*fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '5',
                    dataName: 'billMoney'
                },
                {
                    colNum: '7',
                    dataName: 'bailmoney'
                },
                {
                    colNum: '8',
                    dataName: 'endPayMoney'
                }
            ]
        }
    });

   if(parent.adjustMainWebHeight){
       setTimeout(function () {
           parent.adjustMainWebHeight($('body').height());
       },200)
   }

});
bt.use(['btdate'], function () {
    var date = new Date();
    var currentTime = date.Format("yyyy-MM-dd");
    var dd = bt.btdate.init({
        elem: ".date-apply",
        // labelname:bt.lang('table.abVoucherinfo.Business.date'),
        name: "date"
    });
    dd.setValue({value: currentTime})
});

function cashingConfirmation(data) {
    var date = document.getElementById("date");
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids, vids, vvids;
    var abEmitBillPayOnlineVos = [];
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbillpay:confirm', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }

        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }

        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }

        var item = rowData[i];
        // 用途
        var payUse = $("#payUse" + item.id).val();
        // 备注
        var payAbs = $("#payAbs" + item.id).val();
        var abEmitBillPayObj = {
            id: item.id,
            vid: item.vid,
            vvid: item.vvid,
            payCorpId: item.corpId,
            payPayeeacc: item.payPayeeacc,
            payPayeeName: item.payPayeeName,
            payPayeeBankNo: item.payPayeeBankNo,
            payPayeeBankName: item.payPayeeBankName,
            provName: item.provName,
            cityName: item.cityName,
            payUse: payUse,
            payAbs: payAbs
        }
        abEmitBillPayOnlineVos.push(abEmitBillPayObj)
    }
    parent.layer.confirm(bt.lang('common.message.confirm.cashingConfirmation'), function () {
        /*$.post(basePath + "/bill/accebill/abemitbillpay/cashingConfirmation", {
            "date": date.value,
            "ids": ids,
            "vids": vids,
            "vvids": vvids,
            "abEmitBillPayOnlineVos": abEmitBillPayOnlineVos
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');*/

        $.ajax({
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                dateStr: date.value,
                ids: ids,
                vids: vids,
                vvids: vvids,
                abEmitBillPayOnlineVos: abEmitBillPayOnlineVos
            }),
            asycn: false,
            url: basePath + "/bill/accebill/abemitbillpay/cashingConfirmation",
            success: function (result) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            dataType: 'json'
        });
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (parent.bt.navtab) {
        parent.bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abemitbill/view?viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbillpay/view?viceId=" + data.vvid + "&accId=" + data.id,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//线上兑付
function payOnline(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.id;
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm', data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
        return;
    }
    /*if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.cashing.online'),basePath + "/bill/accebill/abemitbillpay/payOnline/"+aid);
        return;
    }*/
    if (parent.bt.navtab) {
        parent.bt.navtab.addNavtab(bt.lang('common.button.cashing.online'),
            basePath + "/bill/accebill/abemitbillpay/payOnline/" + aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbillpay/payOnline/" + aid,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//批量设置列值
function batchSetCol(opType) {
    $ = bt.jquery, form = bt.form();
    if (isSelectData()) {
        var payUse = $("#payUse").val();
        var payAbs = $("#payAbs").val();
        var selectData = getSelectData();
        for (var i = 0; i < selectData.length; i++) {
            var id = selectData[i].id;
            if (opType == 'payUse') { //用途
                if (!payUse) {
                    layer.alert(bt.lang("bill.accebill.abEmitBillPayManager.setPayUse"), {icon: 7});
                    return;
                }
                $("#payUse" + id).val(payUse);
            }
            if (opType == 'payAbs') {//备注
                $("#payAbs" + id).val(payAbs);
            }
        }
    }
}

//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}

//判断是否选择了数据
function isSelectData() {
    var selectData = getSelectData();
    if (!selectData.length) {
        layer.alert(bt.lang('bill.accebill.abBillDiscountManager.selectData'), {icon: 7});
        return false;
    }
    return true;
}

// 批量线上兑付,emitSttlmmk标记为1，线上兑付
function batchPayOnline(selectData) {
    var date = document.getElementById("date");
    var len = selectData.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = selectData;
    var ids, vids, vvids;
    var compayInfos, compayUses;
    var abEmitBillPayOnlineVos = [];
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbillpay:confirm', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        var item = rowData[i]
        if (vids) {
            vids = vids + "," + item.vid;
        } else {
            vids = item.vid;
        }

        if (vvids) {
            vvids = vvids + "," + item.vvid;
        } else {
            vvids = item.vvid;
        }

        if (ids) {
            ids = ids + "," + item.id;
        } else {
            ids = item.id;
        }

        // 用途
        var payUse = $("#payUse" + item.id).val();
        // 备注
        var payAbs = $("#payAbs" + item.id).val();

        //信息校验
        if (item.payPayeeacc && item.payPayeeBankNo && item.provName && item.cityName) {
            //用途信息必填
            if (!payUse) {
                if (compayUses) {
                    compayUses = compayUses + item.origNo + "<br>";
                } else {
                    compayUses = "<br>" + item.origNo + "<br>";
                }
            }
        } else {
            //未设置或未有收票人信息
            if (compayInfos) {
                compayInfos = compayInfos + item.origNo + "<br>";
            } else {
                compayInfos = "<br>" + item.origNo + "<br>";
            }
        }

        var abEmitBillPayObj = {
            id: item.id,
            vid: item.vid,
            vvid: item.vvid,
            payCorpId: item.corpId,
            payPayeeacc: item.payPayeeacc,
            payPayeeName: item.payPayeeName,
            payPayeeBankNo: item.payPayeeBankNo,
            payPayeeBankName: item.payPayeeBankName,
            provName: item.provName,
            cityName: item.cityName,
            payUse: payUse,
            payAbs: payAbs
        }
        abEmitBillPayOnlineVos.push(abEmitBillPayObj)
    }

    //校验提交数据
    if (compayInfos) {
        layer.alert(bt.lang("bill.accebill.abEmitBillPayManager.compayInfosMsg", [compayInfos]), {icon: 7});
        return;
    }
    if (compayUses) {
        layer.alert(bt.lang("bill.accebill.abBillDiscountManager.compayUsesMsg", [compayUses]), {icon: 7});
        return;
    }

    parent.layer.confirm(bt.lang('common.message.confirm.cashingConfirmation'), function () {
        url = basePath + "/bill/accebill/abemitbillpay/batchCashingConfirmation";
        /*$.post(basePath + "/bill/accebill/abemitbillpay/batchCashingConfirmation", JSON.stringify(param), function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');*/

        $.ajax({
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                dateStr: date.value,
                ids: ids,
                vids: vids,
                vvids: vvids,
                emitSttlmmk: "1",
                abEmitBillPayOnlineVos: abEmitBillPayOnlineVos
            }),
            asycn: false,
            url: url,
            success: function (result) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            dataType: 'json'
        });
    });
}

function exports(el) {
    parent.layer.alert(bt.lang("common.message.indevelopment"));
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm', data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/abemitbillback/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            bt.datatable.refreshCurrentPage();
        }, 'JSON');
    });
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].id) < 0) {
                    idsValArray.push(datas[i].id);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].id);
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

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);

}


/**
 * 调整票据解付金额
 * @param data
 */
function setBillEndPayMoney(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }else if(len>1){
        layer.alert(bt.lang('不支持批量调整到期解付金额，请重新选择!'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var dataInfo= rowData[0];
    var contentHtml='<form class="layui-form" method="post" id="inputForm"><div id="" class="layui-colla-content layui-show">' +
        '<table class="layui-table"><tbody>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVo.corpId')+'</td>' +
        '<td width="25%">' + '(' + dataInfo.corpCode + ')' + dataInfo.corpIdName +'</td>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfo.origNo')+'</td>' +
        '<td width="25%">' + dataInfo.origNo +'</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('银行类型')+'</td>' +
        '<td width="25%">'+ dataInfo.bankTypeName +'</td>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVice.bailPercent')+'</td>' +
        '<td width="25%">'+
        '<input type="text" id="bailpercent" class="layui-input bt-biz-ratioType" data-name="bailpercent" data-biz-name="bailpercent" value="'+dataInfo.bailpercent+'" placeholder="'+bt.lang('table.abVoucherinfoVice.bailPercent')+'" data-min="0" data-max="100" data-ratetype="percent" data-decimal="2" >' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfo.billMoney')+'</td>' +
        '<td width="25%">' + bt.money.format(dataInfo.billMoney) +'<input type="hidden" id="billMoney" value="'+dataInfo.billMoney+'" /></td>' +
        '<td class="textRight" width="8%"></td>' +
        '<td width="25%"></td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVice.bailMoney')+'</td>' +
        '<td width="25%">' +
        '<input type="text" id="bailmoney" class="layui-input bt-biz-moneyType" data-biz-name="bailmoney" value="'+dataInfo.bailmoney+'" placeholder="保证金金额" data-min="0" data-max="'+dataInfo.billMoney+'" data-callback="bailMoneyCallback" >' +
        '</td>' +
        '<td class="textRight" width="8%">到期解付金额</td>' +
        '<td width="25%">' +
        '<input type="text" id="endPayMoney" class="layui-input bt-biz-moneyType" data-biz-name="endPayMoney" value="'+dataInfo.endPayMoney+'" placeholder="票据解付金额" data-min="0" data-max="'+dataInfo.billMoney+'" data-callback="endPayMoneyCallback">' +
        '</td>' +
        '</tr>' +
        '</tbody></table>' +
        '</div></form>';
    layer.open({ type: 1,
        area: ['700px', '300px'],
        title: '调整票据解付金额',
        content: contentHtml,
        btn: ['确定', '取消'],
        success: function(layero, index) {
            bt.moneyType.init();
            bt.ratioType.init();
        },
        yes: function(index, layero){
            var endPayMoney = document.getElementById('endPayMoney').value;
            var bailmoney = document.getElementById('bailmoney').value;
            var bailpercent = bt.ratioType.get('bailpercent').val();
            if(endPayMoney && bailmoney){
                $.ajax({
                    type: 'post',
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify({
                        accId: dataInfo.id,
                        vvid: dataInfo.vvid,
                        bailpercent: bt.money.toNumber(bailpercent),
                        endPayMoney: bt.money.toNumber(endPayMoney),
                        bailmoney: bt.money.toNumber(bailmoney)
                    }),
                    asycn: false,
                    url: basePath + "/bill/accebill/abemitbillpay/setBillEndPayMoney",
                    success: function (result) {
                        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            bt.datatable.refreshCurrentPage();
                            layer.close(index); // 关闭弹出层
                        }
                    },
                    dataType: 'json'
                });
            }else{
                layer.alert(bt.lang('保证金金额和票据解付金额不允许为空！'), {icon: 7});
            }
        },
        btn2: function(index, layero){
            layer.close(index); // 关闭弹出层
        }
    });
}

function bailMoneyCallback(){
    var billMoney=bt.money.toNumber($("#billMoney").val());
    var bailmoney=bt.money.toNumber($("#bailmoney").val());
    bt.moneyType.get('endPayMoney').val((billMoney-bailmoney).toFixed(2));
}
function endPayMoneyCallback(){
    var billMoney=bt.money.toNumber($("#billMoney").val());
    var endPayMoney=bt.money.toNumber($("#endPayMoney").val());
    bt.moneyType.get('bailmoney').val((billMoney-endPayMoney).toFixed(2));
}
