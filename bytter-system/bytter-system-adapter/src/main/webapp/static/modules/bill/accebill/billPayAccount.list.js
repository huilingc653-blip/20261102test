var billTable = {};
var dictes,rateType;
bt.use(['form', 'dicts', 'corp', 'element', 'datatable', 'mulselect', 'moneyType', 'dateType','rateType','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    dictes = bt.dicts.init();
    rateType = bt.rateType.init();
    bt.corp.init();
    var billDateType = bt.dateType.init();
    bt.mulselect.init();
    moneyType = bt.moneyType.init();
    var accountTable = bt.accountTable.init();
    var tableSummary = {
        pageSum: true,
        selectSum: true,
        sumList: [
            {
                colNum: '20',
                dataName: 'billMoney'
            },
            {
                colNum: '21',
                dataName: 'bailMoney'
            },
            {
                colNum: '22',
                dataName: 'payMoney'
            }
        ]
    };
    var columns = [
        {data: "applyCorpName", bSortable: true,
            render: function (data, type, full, meta) {
                return '(' + full.applyCorpCode + ')' + full.applyCorpName;
            }
        },
        {data: "corpCodeAndName", bSortable: true},
        {data: "busCorpName", bSortable: true},
        {data: "busPersonnel", bSortable: true},
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
        {data: "origNo", bSortable: true},
        {data: "bankAcc", bSortable: true},
        {data: "billFlagName", bSortable: true},
        {data: "billTypeName", bSortable: true},
        {data: "payer", bSortable: true},
        {data: "payerBankName", bSortable: true},
        {data: "emitTypeName", bSortable: true},
        {data: "emitGuandisSign", bSortable: true,
            render: function (data, type, full, meta) {
                if (data == 0) {
                    return "否";
                }else if(data == 1){
                    return "是"
                }
                return '';
            }},
        {data: "startDate", bSortable: true},
        {data: "endDate", bSortable: true},
        {data: "payee", bSortable: true},//15
        {data: "payeeacc", bSortable: true},//15
        {data: "payeebankno", bSortable: true},//15
        {data: "payeeBankName", bSortable: true},//15
        {data: "applyName", bSortable: true},//15
        {
            data: "billMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                if (data) {
                    return bt.money.format(data);
                }
                return '';
            }
        },
        {
            data: "bailPercent", bSortable: true, className: "textRight",
            render: function (data, type, full, meta) {
                var html="";
                if (data || data == "0") {
                    html = bt.rateType.getFun().format(full.bailPercent, 'percent', 2, ',');
                }
                return html;
            }
        },
        {
            data: "bailMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                if (data) {
                    return bt.money.format(data);
                }
                return '0.00';
            }
        },
        {
            data: "payMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(data ? data : 0);
            }
        },
        {data: "isPayName", bSortable: true},
        {data: "payDateStr", bSortable: true},
        {data: "bankName", bSortable: true},
        {data: "billAnomaliesStr", bSortable: false},
        {data: "dueRemindStr", bSortable: true, className: 'textCenter'},
        {data: "emitOpenOkName", bSortable: true},
        {data: "emitOpenDate", bSortable: true},
        {data: "billStatusName", bSortable: true},
        {data: "ebillBlCrclIndcName", bSortable: true},
        {data: "briefIdName", bSortable: true},
        {data: "sendSignName", bSortable: true},
        {data:"newStmIssuofpprIndName",bSortable:false},
        {data:"blgrdbagcrclPrmtIndName",bSortable:false},
        {data:"sbbllRngStrtSn",bSortable:true},//30
        {data:"sbbllRngEndSn",bSortable:true},
        {data:"blStdAmt",bSortable:false,className:'textRight',
            render: function ( data, type, full, meta ) {
                if(full.newStmIssuofpprInd == 1){
                    return bt.money.format(full.blStdAmt);
                }else {
                    return "";
                }
            }
        },
        {data:"blRmtrUnnIdNo",bSortable:false},
        {data:"rmk",bSortable:true},
        {
            data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="billView(\'' + full.id + '\')">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
        }
    ];
    var openExportSelectedExcel = function (exportUrl, params, code) {
        if (!exportUrl) {
            exportUrl = basePath + "/export/excel/exportAll";
        }
        var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
        try {
            form.append($("<input></input>").attr("type", "hidden").attr("name", "params").attr("value", encodeURIComponent(params)));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "ids").attr("value", encodeURIComponent(code)));
            form.appendTo('body');
            form.submit();
        } catch (e) {
            console.log(e);
        } finally {
            form.remove();
        }
    };
    var tableTitelObj = {};
    bt.each($("#appTable thead tr th"),function (i,th){
        tableTitelObj[columns[i].data] = $(th).text() || '';
    })
        //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        isLayIgnore:true,
        isInitData: true,
        order: [[16, 'asc'],[41, 'asc']],
        pageSizeOptions:[10, 25, 50, 100, 500,1000,2000,5000],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillQuery/billPayDataTable"
        },
        buttons: [
            {
                text: bt.lang('accebill.common.button.electView'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBillView(data);
                }
            }, {
                text: bt.lang('common.button.billfront') + bt.lang('common.btn.bank.synchro'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBankInfoSyncro(data);
                }
            },
            {
                text: bt.lang('common.button.billback') + bt.lang('common.btn.bank.synchro'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBankBACKSyncro(data);
                }
            }]
        , columns: columns,
        summary: tableSummary,
        btClickRowCallback: function (data) {
            opIdsValArray(bt.datatable.getSelectData("#appTable"));
        },
        btDrawCallback: function (setting) {
            setSumMoney(0,0,0,'0.00','0.00');
        },
        customExportSelected:function (params,processing){
            var selectedList = bt.datatable.getSelectData("#appTable");
            var ids = "";
            var exportExcelUrl = "/bill/accebill/abbillQuery/exportPayExcelByMap";
            for(var idx = 0; idx < selectedList.length; idx++){
                if(ids == ""){
                    ids = selectedList[idx].id;
                }else {
                    ids = ids + "," + selectedList[idx].id;
                }
            }
            var sumLists = tableSummary.sumList
            // tableTitelObj
            var tableColumns = params.columns;
            var newColumns = [];
            bt.each(tableColumns,function (i,col){
                col.title = tableTitelObj[col.data];
                if(col.title){
                    newColumns.push(col);
                    for(var cindex = 0; cindex < sumLists.length; cindex++){
                        var colName = sumLists[cindex].dataName;
                        if(colName == col.name){
                            col.isPagesum = true;
                        }
                    }
                }
            })
            params.columns = newColumns;
            openExportSelectedExcel(exportExcelUrl,JSON.stringify(params),ids);
            processing(false)
        },
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var str = settings.json.insertInformation;
            var showHTML = bt.lang("sys.common.isSelectSize", [0]);
            var returnHtml =  '<div style="color: red">' + bt.lang("bill.accebill.MoneySum", [bt.money.format(str)]) + '。 ' + '<span id="sumMoneyBillSpan" style="color: red">' + showHTML;
            returnHtml = returnHtml + "，"+"到期解付的金额合计：";
            returnHtml = returnHtml + (settings.json.data && settings.json.data[0] ? (bt.money.format(settings.json.data[0].sumPayMoney)) : '0.00');
            returnHtml = returnHtml + "，"+"已选到期解付的金额合计：0.00";
            return returnHtml + '</span></div>';
        },
        customExportAll: function (params, processing, data) {
            /*params = encodeURIComponent(JSON.stringify(params));
            var exportUrl = basePath + "/bill/accebill/abbillQuery/exportPayExcelByMap";
            var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
            try {
                form.append($("<input></input>").attr("type", "hidden").attr("name", "params").attr("value", params));
                form.appendTo('body');
                form.submit();
            } catch (e) {
            } finally {
                form.remove();
            }
            processing(false);*/

            var exportUrl = basePath + "/bill/accebill/abbillQuery/exportPayAll";
            openExportExcel(exportUrl, data);
            processing(false);

        }
    });
});

//清空数据
function clearData() {
    dictes.get('billType').val('10,11');
    dictes.get('billFlag').val('11,10');
}

function billView(accIds) {
    top.batchBillEcdsInfoViewAccIds=accIds;
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/batchBillEcdsInfoView?accIds=post"
    });
}

function batchBillView(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var accIds = "";
    for (i = 0; i < len; i++) {
        if (accIds.length > 0) {
            accIds += ",";
        }
        accIds += rowData[i].id;
    }
//	if(bt.navtab){
//		bt.navtab.addNavtab(bt.lang('common.button.view') ,
//				basePath + "/bill/accebill/abvoucherinfo/batchBillEcdsInfoView?accIds=" + accIds);
//		return;
//	}
    billView(accIds);
}


//批量银行同步背面信息
function batchBankBACKSyncro(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    var rowData = data.splice(0, len);
    var accIds = "";
    for (i = 0; i < len; i++) {
        if (accIds.length > 0) {
            accIds += ",";
        }
        accIds += rowData[i].id;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchBankSyncro'), function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/abbillreceivebiz/batchBankSyncroBillInfo", {
            "acId": accIds,
            "type": '02'
        }, function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 6});
            } else {
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 2});
            }
        }, 'JSON');
    });
}

//批量银行同步正面信息
function batchBankInfoSyncro(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    var rowData = data.splice(0, len);
    var accIds = "";
    for (i = 0; i < len; i++) {
        if (accIds.length > 0) {
            accIds += ",";
        }
        accIds += rowData[i].id;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchBankSyncro'), function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/abbillreceivebiz/batchBankSyncroBillInfo", {
            "acId": accIds,
            "type": '03'
        }, function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 6});
            } else {
                parent.layer.alert(bt.lang(result.attributes.MSG_INFO), {icon: 2});
            }
        }, 'JSON');
    });
}

// 计算总条数
var opIdsValArray = function (datas) {
    var sumBillMoney = 0;
    var sumBailMoney = 0;
    var sumPayMoney = datas.length ? bt.money.format(datas[0].sumPayMoney) : 0;
    var yxSumPayMoney = 0;
    var count = datas.length || 0
    if (datas && datas.length > 0) {
        for (var i = 0; i < datas.length; i++) {
            sumBillMoney=bt.money.add(sumBillMoney,datas[i].billMoney);
            sumBailMoney=bt.money.add(sumBailMoney,datas[i].bailMoney);
            yxSumPayMoney = bt.money.add(yxSumPayMoney,datas[i].payMoney ? datas[i].payMoney : 0)
        }
    }
    setSumMoney(sumBillMoney,sumBailMoney, count,sumPayMoney, yxSumPayMoney);
};

var setSumMoney = function (sumBillMoney,sumBailMoney,count,sumPayMoney,yxSumPayMoney) {
    var showHTML = bt.lang("sys.common.isSelectSize", [count])+","+bt.lang("sys.common.isSelectMoney",[bt.money.format(sumBillMoney)])+","+bt.lang("sys.common.isSelectBailMoney",[bt.money.format(sumBailMoney)])
        + "，"+"到期解付的金额合计：" + sumPayMoney + "，"+"已选到期解付的金额合计：" + bt.money.format(yxSumPayMoney);
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
 * 选择账户回调
 * @param data
 */
function accountCallBack (data) {
    $("#likes_bankAcc").val(data.bankAcc);
    //selectType.get("eqs_curId").val(data.curId);
}

function bankTypeCallBack(data){
    if(!data){
        return;
    }
    var bankTypeIdStr = "";
    for(var i = 0; i < data.length; i++){
        if(bankTypeIdStr == ""){
            bankTypeIdStr = data[i].id;
        }else {
            bankTypeIdStr = bankTypeIdStr + "," + data[i].id;
        }
    }
    bt.accountTable.get("likes_bankAcc").reload({
        urldata: {
            bankTypeId: bankTypeIdStr,
        }
    });
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

//比较保证金比例
function compareBailpercent(){
    var geg_bailpercent = rateType.get("geg_bailPercent").val();
    var leg_bailpercent = rateType.get("leg_bailPercent").val();
    if(geg_bailpercent && leg_bailpercent && geg_bailpercent > leg_bailpercent){
        rateType.get("leg_bailPercent").val(geg_bailpercent);
    }
}
