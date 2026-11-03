var datatable,select;
var selectType,form;
bt.use(['form', 'datatable', 'selectType', 'dateType', 'selectTable','date','dicts'], function () {
    $ = bt.jquery, form = bt.form();
    select = bt.selectType.init();
    selectType = select;
    var dict = bt.dicts.init();
    bt.dateType.init();
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "bankName",},
        {data: "curEnglishCode",},
        {data: "isOnlineStr",}
    ];
    //主账号
    var bankAccInit = bt.selectTable.init({
        elem: "#mainAccountTable",
        url: basePath + "/foreign/fexcorppool/getBankAcc",
        urldata: {poolId: ""},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
                $('#curId').val(data.curId);
            } else {
                $('input.text').val('');
            }
        }
    });

    //单位回调函数
    bt.poolCallback = function (data) {
        var poolIdParanm = data.poolid || '';
        //重新加载付主账号
        bankAccInit.get("bankAcc").rebuild({
            urldata: {poolId: poolIdParanm},
            columns: bankAccCol
        });
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        clickRowSelected:false,
        order: [[0, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        isInitData: false,
        ajax: {
            'url': basePath + "/foreign/fexcorppoolbankaccquota/datatable"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "poolName", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "bankAcc", bSortable: true, className: 'center'},
            {data: "englishCode", bSortable: true, className: 'center'},
            {
                data: "externalDebtBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.externalDebtBal)){
                       html = bt.money.format(full.externalDebtBal);
                    }
                    return html;
                }
            },
            {
                data: "externalDebtLimit", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.externalDebtLimit)){
                       html = bt.money.format(full.externalDebtLimit);
                    }
                    return html;
                }
            },
            {
                data: "remainingDebtQuota", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.remainingDebtQuota)){
                        html = bt.money.format(full.remainingDebtQuota);
                    }
                    return html;
                }
            },
            {
                data: "externalLoanBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.externalLoanBal)){
                        html = bt.money.format(full.externalLoanBal);
                    }
                    return html;
                }
            },
            {
                data: "externalLoanLimit", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.externalLoanLimit)){
                        html = bt.money.format(full.externalLoanLimit);
                    }
                    return html;
                }
            },
            {
                data: "remainingLoanQuota", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.remainingLoanQuota)){
                        html = bt.money.format(full.remainingLoanQuota);
                    }
                    return html;
                }
            },
            {
                data: "inflowBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.inflowBal)){
                        html = bt.money.format(full.inflowBal);
                    }
                    return html;
                }
            },
            {
                data: "outflowBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.outflowBal)){
                        html = bt.money.format(full.outflowBal);
                    }
                    return html;
                }
            },
            {
                data: "netInflowBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.netInflowBal)){
                        html = bt.money.format(full.netInflowBal);
                    }
                    return html;
                }
            },
            {
                data: "netInflowLimit", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.netInflowLimit)){
                        html = bt.money.format(full.netInflowLimit);
                    }
                    return html;
                }
            },
            {
                data: "remainingNetInflowQuota", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(!bt.empty(full.remainingNetInflowQuota)){
                        html = bt.money.format(full.remainingNetInflowQuota);
                    }
                    return html;
                }
            },
        ],
        btDrawCallback:function (setting) {
            var tableData = setting.json.data;
            if(tableData && tableData.length>0){
                var temp= {}
                var msg=bt.lang('alert.please.maintain')+ ' ';
                var flag=false;
                for(var i=0;i<tableData.length;i++){
                    if(tableData[i].id){
                        flag=true;
                        if(!temp[tableData[i].accEnglishCode+tableData[i].englishCode]){
                            msg += tableData[i].accEnglishCode+'-'+tableData[i].englishCode+' ';
                            temp[tableData[i].accEnglishCode+tableData[i].englishCode] = tableData[i].accEnglishCode+tableData[i].englishCode;
                        }
                    }
                }
                msg += bt.lang('table.btCurrency.curExrate');
                if(flag){
                    layer.msg(msg)
                }
            }
        }
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
});

/**
 * 重置
 */
function resetCode() {
    select.get("poolId").val('');
    $("#bankAcc").val('');
    $("#curId").val('');
    //$("#balDate").val('');
    var nowdate = bt.date.format(new Date(), 'yyyy-MM-dd');
    bt.dateType.init().get("balDate").val(nowdate);
}
