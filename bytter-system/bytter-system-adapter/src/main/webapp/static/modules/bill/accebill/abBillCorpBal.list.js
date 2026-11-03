var billTable = {};
var dataFun;
var idsValArray = new Array();
bt.use(['form', 'select', 'dicts', 'dateType', 'moneyType', 'corp', 'element', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    dateFun = bt.dateType.getFun();

    //查询列表时间初始化
    var start_balDate = dateFun.addDay(dateFun.get($("#end_balDate").val()), "-1");
    $("#start_balDate").val(dateFun.format(start_balDate, "yyyy-MM-dd"));

    //表格初始化前保存列头格式
    $('#customPrintVal').val($('#theadId').html());

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
            'url': basePath + "/bill/accebill/abbillcorpbal/datatable"

        },
        buttons: []
        , columns: [
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },//1
            {data: "curName", bSortable: true},//2
            {data: "balDate", bSortable: true},//3
            {
                data: "spBal", bSortable: true, className: 'textRight',//4
                render: function (data, type, full, meta) {
                    return bt.money.format(full.spBal);
                }
            },
            {
                data: "spAvailBal", bSortable: true, className: 'textRight',//5
                render: function (data, type, full, meta) {
                    return bt.money.format(full.spAvailBal);
                }
            },
            {
                data: "spTiedupBal", bSortable: true, className: 'textRight',//6
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
        /*fnDrawCallback: function () {
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
                    colNum: '4',
                    dataName: 'spBal'
                }, {
                    colNum: '5',
                    dataName: 'spAvailBal'
                }, {
                    colNum: '6',
                    dataName: 'spTiedupBal'
                }, {
                    colNum: '7',
                    dataName: 'ypBal'
                }, {
                    colNum: '8',
                    dataName: 'ypAvailBal'
                }, {
                    colNum: '9',
                    dataName: 'ypTiedupBal'
                }, {
                    colNum: '10',
                    dataName: 'bal'
                }, {
                    colNum: '11',
                    dataName: 'availBal'
                }, {
                    colNum: '12',
                    dataName: 'tiedupBal'
                }
            ]
        }
    });

});

function updateBal(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var corpId = data.corpId;
    var balDate = data.balDate;
    parent.layer.confirm(bt.lang('bill.accebill.balReset'), function () {
        $.post(basePath + "/bill/accebill/abbillaccbal/resetAbBillAccBalByBalDate", {
            "corpIds": corpId,
            "beginDate": balDate
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//重置
function setReset() {
    setTimeout(function () {
        var start_balDate = dateFun.addDay(dateFun.get($("#end_balDate").val()), "-1");
        $("#start_balDate").val(dateFun.format(start_balDate, "yyyy-MM-dd"));
    }, 100);
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            var id = datas[i].corpId + '-' + datas[i].curId + '-' + datas[i].balDate
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(id) < 0) {
                    idsValArray.push(id);
                }
            } else {
                var index = idsValArray.indexOf(id);
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
