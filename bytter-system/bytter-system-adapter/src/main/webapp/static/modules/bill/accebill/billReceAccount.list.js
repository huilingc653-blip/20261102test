var billTable = {};
var dictes;
bt.use(['form', 'dicts', 'btdate', 'corp', 'datatable', 'element', 'dateType', 'mulselect', 'moneyType', 'rateType','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    dictes = bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.mulselect.init();
    moneyType = bt.moneyType.init();
    var accountTable = bt.accountTable.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        isInitData: true,
        isLayIgnore:true,
        order: [[7, 'desc']],
        pageSizeOptions:[10, 25, 50, 100, 500,1000,2000,5000],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillQuery/billReceDataTable"
        },
        buttons: [{
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
        }, {
            text: bt.lang('common.button.billback') + bt.lang('common.btn.bank.synchro'),
            action: function (e, dt, node, config) {
                var data = dt.rows('.selected').data();
                batchBankBACKSyncro(data);
            }
        }]
        , columns: [
            {
                data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {data: "corpCodeAndName", bSortable: true},
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "billFlagName", bSortable: true},//2
            {data: "billTypeName", bSortable: true},//3
            {data: "origNo", bSortable: true},//4
            {data: "billStatusName", bSortable: true},//4
            {data: "ebillBlCrclIndcName", bSortable: true},//4
            {data: "bankAcc", bSortable: true},//4
            {data: "payer", bSortable: true},//5
            {data: "payerBankName", bSortable: true},//5
            {data: "payerAcc", bSortable: true},
            {data: "acceptor", bSortable: true},//6
            {data: "receDate", bSortable: true},//7
            {data: "startDate", bSortable: true},//8
            {data: "endDate", bSortable: true},//9
            {data: "cnAccrecords", bSortable: true},//10
            {data: "cnAccrecordsAcc", bSortable: true},
            {data: "cnAccrecordsBankName", bSortable: false},
            {data: "cnAccrecordsBankCode", bSortable: false},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },//11
            {
                data: "transferWayName", bSortable: true,
                render: function (data, type, full, meta) {
                    //   console.log("=============",JSON.stringify(data)+"||||||"+JSON.stringify(type)+"||||||"+JSON.stringify(full)+"||||||"+meta);
                    if (full.briefId == '8018' && full.transferWay == '0') {
                        data = '票据池质押';
                    }
                    return data;
                }
            },//12
            {
                data: "endorsementCount", bSortable: true, render: function (data, type, full, meta) {
                    if (data == null) {
                        data = 0;
                    }
                    var html = '<a class="" href="#" data-row="' + meta.row + '" ' +
                        'onclick="endorsementCountDtl(\'' + full.id + '\',\'' + full.briefId + '\')">' + data + '</a>';
                    return html;
                }
            },
            {
                data: "applyDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.applyDate){
                        return bt.date.format(full.applyDate);
                    }
                    return "";
                }
            },
            {
                data: "transferDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if (full.transferDate && full.transferWayName && full.status == 95 && full.optStatus == 19) {
                        return bt.date.format(full.transferDate);
                    }
                    return "";
                }
            },//13
            {
                data: "rearAccrecords", bSortable: true,
                render: function (data, type, full, meta) {
                    if (full.transferWayName == null
                        || full.transferWayName == undefined) {
                        return "";
                    }
                    return full.rearAccrecords;
                }
            },//14
            {data: "recAcctId", bSortable: false},//18
            {data: "recAcctBankCode", bSortable: false},//18
            {data: "recAcctsvcr", bSortable: false},//18
            {data: "interestRates", bSortable: true, className: 'textRight',render: function (data, type, full, meta) {
                    return full.interestRatesStr;
                }},//15
            {
                data: "interest", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },//16
            {
                data: "realMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },//17
            {data: "bankName", bSortable: true},//18
            {data: "billAnomaliesStr", bSortable: false},
            {data: "briefIdName", bSortable: true},
            {data: "sendSignName", bSortable: true},
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
            {data:"newStmIssuofpprInd",bSortable:true,render: function ( data, type, full, meta ) {
                    return full.newStmIssuofpprIndName;
                }},
            {data:"blgrdbagcrclPrmtInd",bSortable:true,render: function ( data, type, full, meta ) {
                    return full.blgrdbagcrclPrmtIndName;
                }},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.blStdAmt);
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:false},
            {data:"rmk",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="billView(\'' + full.id + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'billMoney'
                },
                {
                    colNum: '16',
                    dataName: 'interest'
                },
                {
                    colNum: '17',
                    dataName: 'realMoney'
                }
            ]
        },
        btClickRowCallback: function (data) {
            setSumMoney(bt.datatable.getSelectData("#appTable").length);
        },
        btDrawCallback: function (setting) {
            /*idsValArray = new Array()*/
           // idsValArray = [];
            setSumMoney(0);
        },
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var str = settings.json.insertInformation;
            //idsValArray = new Array();
            var showHTML = bt.lang("sys.common.isSelectSize", [0]);
            return '<div style="color: red">' + bt.lang("bill.accebill.MoneySum", [bt.money.format(str)]) + '。 ' + '<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        customExportAll: function (params, processing) {
            params = encodeURIComponent(JSON.stringify(params));
//            window.location =  basePath + "/bill/accebill/abbillQuery/exportReceiveExcelByMap?params="+params;
            var exportUrl = basePath + "/bill/accebill/abbillQuery/exportReceiveExcelByMap";
            var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
            try {
                form.append($("<input></input>").attr("type", "hidden").attr("name", "params").attr("value", params));
                form.appendTo('body');
                form.submit();
            } catch (e) {
            } finally {
                form.remove();
            }
            processing(false);
        }
    });

});

//清空数据
function clearData() {
    dictes.get('billType').val('10,11');
    dictes.get('billFlag').val('11,10');
}

// 背书转让详情页
function endorsementCountDtl(id, briefId) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abbillQuery/endorsementCountDtl?id=" + id + "&briefId=" + briefId);

    }
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

var setSumMoney = function (count) {
    var count = count || 0
    var showHTML = bt.lang("sys.common.isSelectSize", [count]);
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
function applyCorpCallback(data) {
    if (data && data.length == 0) return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp", data);
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
