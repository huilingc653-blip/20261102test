var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'moneyType', 'selectType','element', 'dateType', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fextranreq/dataTableTranDealApprove"
        },
        buttons: [
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "tranReqNo", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "defaultDate", bSortable: true, className: 'center'},
            {data: "englishCode", bSortable: true, className: 'center'},
            {
                data: "defaultMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.defaultMoney);
                }
            },
            {data: "newSettleDate", bSortable: true, className: 'center'},
            /*{
                data: "settleMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.settleMoney);
                }
            },*/
            {
                data: "wishRate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {

                    var opt = {
                        separator: ",",
                        decimal: 6
                    }
                    return moneyFun.format(full.wishRate, opt);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html='';
                    if($("#flowStatus").val()=='todo'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});
