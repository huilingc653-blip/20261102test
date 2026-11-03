var billTable = {}, accTbale = {}, dicts, selectType, corp, moneyType;
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'dicts', 'selectType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();

    //银行流水列表
    accTbale = bt.datatable.initMultiTables({
        elem: "#appAccTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: true,
        selectCss: true,
        checkbox: false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchAccForm",
            formFilter: "searchAcc"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abClaim/datatableAcc"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "transTime", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            // {data: "cdSignName", bSortable: true},
            {
                data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {data: "claimSequence", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true},
            {data: "postscript", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {data: "bankSerialNo", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},

        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '2',
                    dataName: 'amt'
                }
            ]
        },
        btClickRowCallback: function (data) {
            if (data[0].isSelect == true) {
                var accTable = $('#appAccTable').DataTable();
                var accData = accTable.rows('.selected').data();
                var accLen = accData.length
                var accRowData = accData.splice(0, accLen);
                accJudge(accRowData, accLen)
            }
        }
    });

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: true,
        selectCss: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abClaim/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
                    {data: "origNo", bSortable: true},
                    {data: "claimTypeName", bSortable: true},
                    {data: "billSortName", bSortable: true},
                    {data: "claimDate", bSortable: true},
                    {data:"billMoney",bSortable:true,className:'textRight',
                        render: function ( data, type, full, meta ) {
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
                        data: "payMoney", bSortable: true, className: 'textRight',
                        render: function (data, type, full, meta) {
                            return bt.money.format(data ? data : 0);
                        }
                    },
                    {data: "corpName", bSortable: true},
                    {data: "bankAcc", bSortable: true},
                    {data: "bankTypeName", bSortable: true},
                    {data: "claimSequence", bSortable: true},
                    {data: "claimStatusName", bSortable: true},
                    {data: "claimPerson", bSortable: true},
                    {data: "claimTime", bSortable: true},
                    {data: "billFlagName", bSortable: true},
                    {data: "billTypeName", bSortable: true},
                    {data: "payer", bSortable: true},
                    {data: "payerBankName", bSortable: true},
                    {data: "payee", bSortable: true},
                    {data: "cnAccrecords", bSortable: true},
                    {data: "rearAccrecords", bSortable: true},
                    {data: "sendSignName", bSortable: true}
        ],
        btClickRowCallback: function (data) {
            if (data[0].isSelect == true) {
                bt.dicts.get("eqi_cdSign").val(data[0].billSort == 1 ? 0 : 1)
                bt.selectType.get("bankTypeId").val(data[0].bankTypeId)
                bt.corp.get("corpId").val('')
                bt.corp.get("corpId").val(data[0].corpId)
                var table = $('#appTable').DataTable();
                var appData = table.rows('.selected').data();
                var appLen = appData.length
                var appRowData = appData.splice(0, appLen);
                // billJudge(appRowData, appLen)
                $("#claimId").val(data[0].id);
                bt.datatable.refreshTable("appAccTable");
                $("#claimId").val('');
            }
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
                    dataName: 'payMoney'
                }
            ]
        }
    });
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    var dd = bt.btdate.init({
        elem: ".date-single",
        // labelname:bt.lang('table.abVoucherinfo.Business.date'),
        name: "businessdate"
    });
    dd.setValue({value: new Date().Format("yyyy-MM-dd")})
});

function billJudge(appRowData, appLen) {
    var billTypeFlag = true, corpFlag = true, flag = true
    if (appLen) {
        var billSort = appRowData[0].billSort
        var corpId = appRowData[0].corpId
        var claimTypeName = appRowData[0].claimTypeName
        if (appLen > 1) {
            for (var i = 0; i < appLen; i++) {
                //不允许同时选择应付和应收票据！
                if (appRowData[i].billSort !== billSort) {
                    billTypeFlag = false
                }
                //不允许同时选择贴现和托收业务！
                if (appRowData[i].claimTypeName !== claimTypeName) {
                    flag = false
                }
                //不允许同时选择不同单位的票据！
                if (appRowData[i].corpId !== corpId) {
                    corpFlag = false
                }
            }
        }
    }
    if (!billTypeFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffBillType'), {icon: 2});
        return false
    }
    if (!flag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffReceiveBillType'), {icon: 2});
        return false
    }
    if (!corpFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffCorpBill'), {icon: 2});
        return false
    }
    return true;
}

function accJudge(accRowData, accLen) {
    var accCorpFlag = true
    if (accLen) {
        var corpId = accRowData[0].corpId
        if (accLen > 1) {
            for (var i = 0; i < accLen; i++) {
                //不允许同时选择不同单位的流水！
                if (accRowData[i].corpId !== corpId) {
                    accCorpFlag = false
                }
            }
        }
    }
    if (!accCorpFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffFlow'), {icon: 2});
        return false
    }
    return true
}

//提交认领
function submit(data, accData) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectBill'), {icon: 7});
        return;
    }
    var accLen = accData.length
    if (!accLen) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectFlow'), {icon: 7});
        return;
    }
    var accRowData = accData.splice(0, accLen);
    var rowData = data.splice(0, len);

    if (!billJudge(rowData, len) || !accJudge(accRowData, accLen)) {
        return;
    }
    if ((len > 1 && accLen > 1)) {
        layer.alert('只支持票据记录与银行流水记录一对多或多对一，不支持多对多', {icon: 2});
        return false
    }
    var billSum = 0, accSum = 0
    for (var i = 0; i < len; i++) {
        billSum += rowData[i].billMoney
    }
    for (var i = 0; i < accLen; i++) {
        accSum += accRowData[i].amt
    }
    if (accRowData[0].corpId !== rowData[0].corpId) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowCorpIsDiff'), function () {
            if (accRowData[0].bankAcc !== rowData[0].bankAcc) {
                parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowAccIsDiff'), function () {
                    if (billSum !== accSum) {
                        parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'), function () {
                            detailSubmit(rowData, accRowData, len, accLen)
                        })
                    } else {
                        detailSubmit(rowData, accRowData, len, accLen)
                    }
                })
            } else {
                detailSubmit(rowData, accRowData, len, accLen)
            }
        })
    } else if (accRowData[0].bankAcc !== rowData[0].bankAcc) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowAccIsDiff'), function () {
            if (billSum !== accSum) {
                parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'), function () {
                    detailSubmit(rowData, accRowData, len, accLen)
                })
            } else {
                detailSubmit(rowData, accRowData, len, accLen)
            }
        })
    } else if (billSum !== accSum) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'), function () {
            detailSubmit(rowData, accRowData, len, accLen)
        })
    } else {
        detailSubmit(rowData, accRowData, len, accLen)
    }
}

function detailSubmit(rowData, accRowData, len, accLen) {
    parent.layer.confirm(bt.lang('bill.accebill.abClaim.confirmClaim'), function () {
        var vids, vvids, aids, claimIds, billMoneys, origNos, briefId, accDtlIds, realMoneys;
        briefId = rowData[0].briefId
        for (i = 0; i < len; i++) {
            if (vids) {
                vids = vids + "," + rowData[i].vid
            } else {
                vids = rowData[i].vid
            }
            if (vvids) {
                vvids = vvids + "," + rowData[i].vvid
            } else {
                vvids = rowData[i].vvid
            }
            if (aids) {
                aids = aids + "," + rowData[i].aid
            } else {
                aids = rowData[i].aid
            }
            if (claimIds) {
                claimIds = claimIds + "," + rowData[i].id
            } else {
                claimIds = rowData[i].id
            }
            if (billMoneys) {
                billMoneys = billMoneys + "," + rowData[i].billMoney
            } else {
                billMoneys = rowData[i].billMoney
            }
            if (realMoneys) {
                realMoneys = realMoneys + "," + rowData[i].realMoney
            } else {
                realMoneys = rowData[i].realMoney
            }
            if (origNos) {
                origNos = origNos + "," + rowData[i].origNo
            } else {
                origNos = rowData[i].origNo
            }
        }

        for (var j = 0; j < accLen; j++) {
            if (accDtlIds) {
                accDtlIds = accDtlIds + "," + accRowData[j].id
            } else {
                accDtlIds = accRowData[j].id
            }
            if (claimIds) {
                claimIds = claimIds + "," + accRowData[j].claimId
            } else {
                claimIds = accRowData[j].claimId
            }
        }
        $.post(basePath + "/bill/accebill/abClaim/submitClaim", {
            "vids": vids,
            "vvids": vvids,
            "aids": aids,
            "claimIds": claimIds,
            "briefId": briefId,
            "billMoneys": billMoneys,
            "origNos": origNos,
            "accDtlIds": accDtlIds,
            "realMoneys": realMoneys
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                corp.get("corpId").val('')
                dicts.get("eqi_cdSign").val('')
                selectType.get("bankTypeId").val('')
                moneyType.get("startMoney").val('')
                moneyType.get("endMoney").val('')
                dateType.get("startDate").val('')
                dateType.get("endDate").val('')
                $("#bankAcc").val('')
                $("#uses").val('')
                $("#postscript").val('')
                $("#remark").val('')
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}
