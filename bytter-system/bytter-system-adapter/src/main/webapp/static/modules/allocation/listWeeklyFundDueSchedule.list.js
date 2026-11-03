var billTable={};
var idsValArray=[];
var rateType,selectType,dateType,mulselect,corp,accountTable;
bt.use(['form','select','dicts','btdate','moneyType','corp','mulselect','element','datatable','rateType','selectType','accountTable'], function(){
    $ = bt.jquery,form = bt.form();
    rateType = bt.rateType.init();
    dateType = bt.dateType.init();
    mulselect= bt.mulselect.init();
    selectType = bt.selectType.init();
    corp = bt.corp.init();
    accountTable = bt.accountTable.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[17,'asc'],[12,'asc'],[4,'desc'],[15,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/report/weeklyFundsDueSchedule/dataQuery"
        }
        ,columns: [
            {data: "parentName", bSortable: true},//1
            {data: "provinceName", bSortable: false},//1
            {data:"operationAreaName",bSortable:true},//2
            {data: "corpName", bSortable: true},//3
            {data: "equityName", bSortable: true},//4
            {data:"typeName",bSortable:true},//5
            {data:"bankAcc", bSortable: true},//6
            {
                data: "loanMoney", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.loanMoney);
                }
            },
            {
                data: "billMoney", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" ' +
                        'onclick="viewDtl(\'' + full.corpId + '\',\'' + full.endDate + '\',\'' + full.bailPercent + '\')">' + bt.money.format(full.billMoney) + '</a>';
                    return html;
                }
            },
            {
                data: "bailPercent", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div name="rpdintrstRate">' + bt.rateType.getFun().format(full.bailPercent, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div name="rpdintrstRate"></div>'
                    }
                    return html;
                }
            },
            {
                data: "bailMoney", bSortable: true, className: 'textRight',//8
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bailMoney);
                }
            },
            {data:"endDate",bSortable:true},//9
            {
                data: "applyRepayMoney", bSortable: true, className: 'textRight',//10
                render: function (data, type, full, meta) {
                    return bt.money.format(full.applyRepayMoney);
                }
            },
            {
                data:"fundsDisbursementDate", bSortable: true,
                render: function (data, type, full, meta) {
                    return null == data ? '/' : data;
                }
            },//6
            {data:"applicant", bSortable: true},//6
            {data:"rmk", bSortable: false},
            {data:"parentSort",visible:false}
        ],
        summary: {
            pageSum: false,
            allSum: true,//汇总功能开启
            selectSum: true,
            sumList: [
                {
                    colNum: '7',
                    dataName: 'loanMoney'
                },
                {
                    colNum: '8',
                    dataName: 'billMoney'
                },
                {
                    colNum: '10',
                    dataName: 'bailMoney'
                },
                {
                    colNum: '12',
                    dataName: 'applyRepayMoney'
                }
            ]
        }
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});

/**
 * 选择账户回调
 * @param data
 */
function accountCallBack (data) {
    $("#likes_bankAcc").val(data.bankAcc);
    //selectType.get("eqs_curId").val(data.curId);
}

//应收票据台账页
function viewDtl(corpId, endDate,bailPercent) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abbillQuery/listBillPayAccount?corpId=" + corpId + "&endDate=" + endDate+ "&bailPercent=" +  bailPercent);

    }
}