var billTable = {};
var idsValArray = new Array();
bt.use(['form', 'select', 'dicts', 'btdate', 'moneyType', 'corp', 'element', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();

    //表格初始化前保存列头格式
    $('#customPrintVal').val($('#theadId').html());

    bt.btdate.init({
        elem: "#balDate",
        startname: "ged_balDate",
        startDefaultVal:getNowDateStr(),
        endname: "led_balDate"
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
         dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        mulselect: true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillaccbal/datatable"
        },
        buttons: []
        , columns: [
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },//1
            {data: "bankAcc", bSortable: true},//2
            {data: "curName", bSortable: true},//3
            {data: "balDate", bSortable: true},//4
            {
                data: "spBal", bSortable: true, className: 'textRight',//5
                render: function (data, type, full, meta) {
                    return bt.money.format(full.spBal);
                }
            },
            {
                data: "spAvailBal", bSortable: true, className: 'textRight',//6
                render: function (data, type, full, meta) {
                    return bt.money.format(full.spAvailBal);
                }
            },
            {
                data: "spTiedupBal", bSortable: true, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.spTiedupBal);
                }
            },
            {
                data: "ypBal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.ypBal);
                }
            },
            {
                data: "ypAvailBal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.ypAvailBal);
                }
            },
            {
                data: "ypTiedupBal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.ypTiedupBal);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "availBal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availBal);
                }
            },
            {
                data: "tiedupBal", bSortable: true, className: 'textRight',//
                render: function (data, type, full, meta) {
                    return bt.money.format(full.tiedupBal);
                }
            }
        ],
       /* fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            idsValArray = new Array()
            setSumMoney();
        },
        exportFile: {
            customPrint: {
                breakword: [{title: bt.lang('table.sysCorp.corpName'), width: 100, name: 'corpName'}],
                header: function () {
                    return $('#customPrintVal').val();
                }
            }
        },
        summary: {
            pageSum: true,
            selectSum: false,
            sumList: [
                {
                    colNum: '5',
                    dataName: 'spBal'
                }, {
                    colNum: '6',
                    dataName: 'spAvailBal'
                }, {
                    colNum: '7',
                    dataName: 'spTiedupBal'
                }, {
                    colNum: '8',
                    dataName: 'ypBal'
                }, {
                    colNum: '9',
                    dataName: 'ypAvailBal'
                }, {
                    colNum: '10',
                    dataName: 'ypTiedupBal'
                }, {
                    colNum: '11',
                    dataName: 'bal'
                }, {
                    colNum: '12',
                    dataName: 'availBal'
                }, {
                    colNum: '13',
                    dataName: 'tiedupBal'
                }
            ]
        }
    });



    bt.btdate.init({
        elem: "#balDate",
        startname: "ged_balDate",
        startDefaultVal:getNowDateStr(),
        endname: "led_balDate"
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});

function getNowDateStr(){
    var now = new Date();
    var Y = now.getFullYear() + '-';
    var M = (now.getMonth()+1 < 10 ? '0'+(now.getMonth()+1) : now.getMonth()+1) + '-';
    var D = now.getDate();
    var result = Y + M + D;
    return result;
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].id) < 0) {
                    idsValArray.push(datas[i].id);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].id);
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
