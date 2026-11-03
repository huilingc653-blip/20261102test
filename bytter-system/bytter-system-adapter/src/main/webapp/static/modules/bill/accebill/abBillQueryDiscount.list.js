var billTable = {};
bt.use(['form', 'select', 'dicts', 'btdate', 'moneyType', 'corp', 'element', 'datatable', 'selectType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dateType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillQuery/dataQueryDiscountTable"
        },
        buttons: []
        , columns: [
            {data: "corpCodeAndName", bSortable: true},//1
            {data: "bankAcc", bSortable: true},//2
            {data: "origNo", bSortable: true},//3

            {data: "newStmIssuofpprInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data: "blgrdbagcrclPrmtInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},

            {data: "billFlagName", bSortable: true},//6
            {data: "billTypeName", bSortable: true},//7
            {
                data: "billMoney", bSortable: true, className: 'textRight',//8
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},//16
            {data: "payee", bSortable: true},//17
            {data: "startDate", bSortable: true},//12
            {data: "endDate", bSortable: true},//13
            /*{data:"briefIdName",bSortable:true},//4*/
            /*{data:"sourceName",bSortable:true},//5*/
            {data: "enterStockName", bSortable: true},//5
            {data: "insideSingName", bSortable: true},//5
            {data: "rearAccrecords", bSortable: true},//19
            /*{data:"applyName",bSortable:true},//9*/
            {data: "applyDate", bSortable: true},//10
            {data: "applyName", bSortable: true},//11
            {data: "businessDate", bSortable: true},//11
            {data: "discountDate", bSortable: true},//11
            {data: "rpdmkName", bSortable: true},//21
            {data: "sttlmmkName", bSortable: true},//20
            {
                data: "interestRates", bSortable: true, className: 'textRight',//14
                render: function (data, type, full, meta) {
                    if (full.interestRates == null
                        || full.interestRates == undefined) {
                        return ""
                    } else {
                        return bt.rateType.getFun().format(full.interestRates);
                    }
                }
            },
            {
                data: "interest", bSortable: true, className: 'textRight',//15
                render: function (data, type, full, meta) {
                    if (full.interest == null
                        || full.interest == undefined) {
                        return ""
                    } else {
                        return bt.money.format(full.interest);
                    }
                }
            },
            {
                data: "realMoney", bSortable: true, className: 'textRight',//15
                render: function (data, type, full, meta) {
                    if (full.realMoney == null
                        || full.realMoney == undefined) {
                        return ""
                    } else {
                        return bt.money.format(full.realMoney);
                    }
                }
            },
            {data: "aoaccninfId", bSortable: true},//11
            {data: "sendSignName", bSortable: true},
            {data: "busStatusName", bSortable: true},//20
            {data: "instructStatusName", bSortable: true},//21
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            sumList: [
                {
                    colNum: '6',
                    dataName: 'billMoney'
                },
                {
                    colNum: '19',
                    dataName: 'interest'
                },
                {
                    colNum: '20',
                    dataName: 'realMoney'
                }
            ]
        }
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (data.type == 0) {
        emitView(data);
    } else {
        receiveView(data);
    }
}

function emitView(data) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abemitbill/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + data.id
    });
}

function receiveView(data) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id,
    });
}
