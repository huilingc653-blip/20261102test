var billTable={};
var idsValArray=[];
var rateType,selectType,dateType,mulselect,corp;
bt.use(['form','select','dicts','btdate','moneyType','corp','mulselect','element','datatable','rateType','selectType','accountTable'], function(){
    $ = bt.jquery,form = bt.form();
    rateType = bt.rateType.init();
    dateType = bt.dateType.init();
    mulselect= bt.mulselect.init();
    selectType = bt.selectType.init();
    corp = bt.corp.init();
    var accountTable = bt.accountTable.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[3,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillQuery/dataQueryBankAccBalCompareData"
        }
        ,columns: [
            {data: "parentName", bSortable: true},//1
            {data:"operationAreaName"},//2
            {data: "corpName", bSortable: true},//3
            {data: "equityName", bSortable: true},//4
            {data:"bankName",bSortable:true},//5
            {data:"bankAcc", bSortable: true},//6
            {
                data: "loanMoney", className: 'textRight',//7
                render: function (data, type, full, meta) {
                    if(full.loanMoney != null){
                        return bt.money.format(full.loanMoney);
                    }else{
                        return '';
                    }
                }
            },
            {
                data: "repayTotalMoney", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.repayTotalMoney);
                }
            },
            {
                data: "billMoney", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "discrepancy", className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.discrepancy);
                }
            },
            {
                data: "bailPercent", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0" || data == 0 ) {
                        html = '<div name="rpdintrstRate">' + bt.rateType.getFun().format(full.bailPercent, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div name="rpdintrstRate">' + bt.rateType.getFun().format(0, 'percent', 2, ',') + '</div>'
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
            {data:"endDateStr"},//9
            {
                data: "applyRepayMoney", bSortable: true, className: 'textRight',//10
                render: function (data, type, full, meta) {
                    return bt.money.format(full.applyRepayMoney);
                }
            },
            {data:"applicant",bSortable:true},//9
            {data:"rmk"},//9
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
                    colNum: '7',
                    dataName: 'reapyInterest'
                },
                {
                    colNum: '9',
                    dataName: 'billMoney'
                },
                {
                    colNum: '10',
                    dataName: 'bal'
                },
                {
                    colNum: '11',
                    dataName: 'discrepancy'
                },
                {
                    colNum: '13',
                    dataName: 'bailMoney'
                },
                {
                    colNum: '15',
                    dataName: 'applyRepayMoney'
                }
            ]
        }
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});

// 到期时间回调函数
function endDateCallback(){
    var gedEndDate = dateType.get("ged_endDate").val();
    var ledEndDate = dateType.get("led_endDate").val();
    if(gedEndDate > ledEndDate){
        dateType.get("led_endDate").val("");
    }
}

/**
 * 选择账户回调
 * @param data
 */
function accountCallBack (data) {
    $("#likes_bankAcc").val(data.bankAcc);
}
