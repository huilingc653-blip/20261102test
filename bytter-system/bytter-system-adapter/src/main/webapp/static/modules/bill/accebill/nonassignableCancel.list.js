var billTable = {};
var dictes;
var idsValArray = [];
bt.use(['form', 'dicts', 'btdate', 'corp', 'datatable', 'element', 'dateType', 'mulselect', 'moneyType', 'rateType','money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    dictes = bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.mulselect.init();
    moneyType = bt.moneyType.init();
    // mulselectType = bt.mulselectType.init();
    //生成列表，
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        isInitData: true,
        // order: [[7, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/nonassignableCancel/dataTable",

        },
        buttons: [
            {
                text: bt.lang('accebill.common.button.nonassignableCancelApply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    application(data);
                }
            }]
        , columns: [
            {
                data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {data: "corpName", bSortable: true},//1
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},//2
            {data: "billFlagName", bSortable: true},
            {data: "billTypeName", bSortable: true},//3
            {data: "origNo", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},//4
            {data: "sbbllRngEndSn", bSortable: true},
            {
                data: "blStdAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "blHoldUnnIdNo", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="billView(\'' + full.accId + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '10',
                    dataName: 'blStdAmt'

                },
                {
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        },
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            /*idsValArray = new Array()*/
            idsValArray = [];
            setSumMoney();
        },
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var str = settings.json.insertInformation;
            var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
            return '<div style="color: red" ><span id="total_money_id">' + bt.lang("bill.accebill.MoneySum", [bt.money.format(str)]) + '。 ' + '</span><span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        }
    });

});

//reset
function clearData() {
    dictes.get('billType').val('10,11');
    dictes.get('billFlag').val('11,10');
}

//查看页面
function billView(accId) {
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/nonassignableCancel/view?accId=" + accId
    });
}

//不可转让撤销申请
function application(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var vt = true, accIds, vids, vvids, billSources;

    for (i = 0; i < len; i++) {
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

        if (accIds) {
            accIds = accIds + "," + rowData[i].accId;
        } else {
            accIds = rowData[i].accId;
        }
        if (billSources) {
            billSources = billSources + "," + rowData[i].billSource;
        } else {
            billSources = rowData[i].billSource;
        }

    }
    // console.log("不可转让撤销申请url")
    parent.layer.confirm("您确定提交吗？", function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/nonassignableCancel/nonassignableCancelApplication", {
            "accIds": accIds,
            "infoIds": vids,
            "viceIds": vvids,
            "billSources": billSources
        }, function (result) {
            bt.loading.hide();
            if (result.success == true) {
                parent.layer.alert(result.msg, {icon: 6});
                bt.datatable.refreshCurrentPage();
            } else {
                parent.layer.alert(bt.lang(result.messageEnum.toLowerCase().replaceAll("_",".")), {icon: 2});
            }
        }, 'JSON');
    });


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

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].accId) < 0) {
                    idsValArray.push(datas[i].accId);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].accId);
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
function applyCorpCallback(data) {
    if (data && data.length == 0) return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp", data);
}
