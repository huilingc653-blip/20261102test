var dateType;
var selectType,form;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'corp', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    corpInit = bt.corp.init();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    moneyType=bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dicts = bt.dicts.init();
    dateType.get("ged_queryDate").val(bt.date.format($setTime(), "yyyy-MM-dd"));
//生成列表

    var htmlx=
        '<tr class="title">'+
            '<th rowspan="2">'+bt.lang('table.fbsBudget.corpId')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.fbsBudgetDetail.curId')+'</th>'+
            '<th colspan="5">'+bt.lang('fundsBalanceStatistics.InternalDeposit')+'</th>'+
            '<th colspan="9">'+bt.lang('fundsBalanceStatistics.banksDeposit')+'</th>'+
            '<th rowspan="2">'+bt.lang('fundsBalanceStatistics.bill')+'</th>'+
        '</tr>'+
        '<tr class="title">'+
            '<th>'+bt.lang('common.subject.day.moeny')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.current')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.regular')+'</th>'+
            '<th>'+bt.lang('sys.notification.notify')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.InternalDepositTotal')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.regular')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.current')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.virtual')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.agreement')+'</th>'+
            '<th>'+bt.lang('sys.notification.notify')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.Specialized')+'</th>'+
            '<th>'+bt.lang('table.cmsProvideLoanInfo.bailmoney')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.loans')+'</th>'+
            '<th>'+bt.lang('fundsBalanceStatistics.banksDepositTotal')+'</th>'+
        '</tr>';

    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        query: "search",
        checkbox:false,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/report/fundsbalancestatistics/loadData"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        exportFile: {
            customPrint: {
                header: function () {
                    var printHtml = htmlx;
                    return printHtml;
                },
            },
        },
        columns: [
            {data:"corpName",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {
                data: "accType11", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType11);
                }
            },
            {
                data: "accType12", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType12);
                }
            },
            {
                data: "accType13", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType13);
                }
            },
            {
                data: "accType14", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType14);
                }
            },
            {
                data: "fcTotal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.fcTotal);
                }
            },
            {
                data: "accType02", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType02);
                }
            },
            {
                data: "accType01", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType01);
                }
            },
            {
                data: "accType10", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType10);
                }
            },
            {
                data: "accType09", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType09);
                }
            },
            {
                data: "accType07", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType07);
                }
            },
            {
                data: "accType06", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType06);
                }
            },
            {
                data: "accType05", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType05);
                }
            },
            {
                data: "accType04", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType04);
                }
            },
            {
                data: "accType03", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accType03);
                }
            },
            {
                data: "bankTotal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bankTotal);
                }
            },
        ],
        summary:{
            pageSum:true,
            groupColumn:'curName',
            customSum:{
                text:function(){
                    var changeCur = dicts.get("changeCur").val();
                    if(changeCur=="1") {
                        return bt.lang('table.fexForeign.report.convert.currency.total');
                    }
                    return '';
                },
                sumCallback:function(currentPageData,colName){ //汇总回调，必须要有返回值
                    //currentPageData　当前页面数据 colName　当前列名称
                    var result=0;
                    if(currentPageData) {
                        bt.each(currentPageData, function (index, item) {
                            if (colName == "accType11") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType11),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType12") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType12),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType13") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType13),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType14") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType14),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "fcTotal") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.fcTotal),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType01") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType01),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType02") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType02),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType03") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType03),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType10") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType10),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType04") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType04),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType05") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType05),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType06") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType06),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType07") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType07),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "accType09") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.accType09),moneyFun.toNumber(item.curExrate)));
                            }
                            if (colName == "bankTotal") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.bankTotal),moneyFun.toNumber(item.curExrate)));
                            }
                        });
                    }
                    return moneyFun.format(result);
                }
            },
            sumList:[
                {
                    colNum:'2',
                    dataName:'accType11'
                },
                {
                    colNum:'3',
                    dataName:'accType12'
                },
                {
                    colNum:'4',
                    dataName:'accType13'
                },
                {
                    colNum:'5',
                    dataName:'accType14'
                },
                {
                    colNum:'6',
                    dataName:'fcTotal'
                },
                {
                    colNum:'7',
                    dataName:'accType01'
                },
                {
                    colNum:'8',
                    dataName:'accType10'
                },
                {
                    colNum:'9',
                    dataName:'accType02'
                },
                {
                    colNum:'10',
                    dataName:'accType03'
                },
                {
                    colNum:'11',
                    dataName:'accType04'
                },
                {
                    colNum:'12',
                    dataName:'accType05'
                },
                {
                    colNum:'13',
                    dataName:'accType06'
                },
                {
                    colNum:'14',
                    dataName:'accType07'
                },
                {
                    colNum:'15',
                    dataName:'accType09'
                },
                {
                    colNum:'16',
                    dataName:'bankTotal'
                }
            ]
        },
    });

    resetBtn = function() {
        dateType.get("ged_queryDate").val(bt.date.format($setTime(), "yyyy-MM-dd"));
    }
});

/**
 * 去年的今天
 * @returns {Date}
 */
$setTime = function () {
    var lastTime = new Date();
    lastTime.setFullYear(lastTime.getFullYear()-1);
    return lastTime;
}

function callToCurr(data){
    if(data.value =="1"){
        selectType.get("reducedCurId").attr({'data-disabled':false,'data-required':true,'data-value':$("#rmbCur").val()});
        selectType.get("rateType").attr({'data-disabled':false,'data-required':true});
        dateType.get("rateDate").attr({'readonly':false,'data-required':true});
    }else {
        selectType.get("reducedCurId").attr({'data-disabled':true,'data-required':false});
        selectType.get("rateType").attr({'data-disabled':true,'data-required':false});
        dateType.get("rateDate").attr({'readonly':true,'data-required':false});
    }
    form.render();
    form.renderIcon();
}

/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var curId = selectType.get("curId").val();
    var reducedCurId = selectType.get("reducedCurId").val();
    var rateType = selectType.get("rateType").val();
    var rateDate = dateType.get('rateDate').val();
    if (isNotEmpty(curId) && isNotEmpty(reducedCurId) && isNotEmpty(rateType) && isNotEmpty(rateDate)) {
        $.ajax({
            url: basePath + "/report/allocationsettlement/checkConvertCurId",
            type:"post",
            data:{
                "origCurId":curId,
                "convertCurId": reducedCurId,
                "rateType": rateType,
                "rateDate": rateDate,
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("reducedCurId").val("");
                }
            }
        });
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}