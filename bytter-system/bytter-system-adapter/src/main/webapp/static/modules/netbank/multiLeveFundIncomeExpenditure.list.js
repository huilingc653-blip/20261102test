var datatable,corp,dateType,mulselect,dicts;
bt.use(['form', 'datatable', 'dateType', 'mulselect' , 'selectType', 'moneyType', 'dicts', 'bank', 'corp','money','freezeTable','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    bt.moneyType.init();
    dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    bt.selectType.init();
    mulselect = bt.mulselect.init();

    var rateType = bt.rateType.getFun();


    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        paging:false,
        ignoreButtons:['colvis','print','state'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
        },
        ajax: {
            'url': basePath + "/report/incomeAndExpenditureOfFunds/multiLeveDatatable"
        },
        columns: [
            {data: "provinceName", bSortable: false},
            {data: "corpName", bSortable: false},
            {data: "maxRate", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (!isEmpty(full.maxRate)){
                        return rateType.format(full.maxRate);
                    }else{
                        return '';
                    }
                }
            },
            {
                data: "beginBal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.beginBal);
                }
            },
            {
                data: "income", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.income);
                }
            },
            {
                data: "expenditure", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.expenditure);
                }
            },
            {
                data: "endBal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.endBal);
                }
            },
            {
                data: "realEndBal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = bt.money.format(full.realEndBal);
                    if(!isEmpty(full.corpId)){
                        html = '<a class="" href="#" data-row="'+meta.row+'" onclick="viewBal(this)">'+bt.money.format(full.realEndBal)+'</a>';
                    }
                    return html;
                }
            },
            {
                data: "diffMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(data);
                }
            },
            {
                data: "choMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.choMoney);
                }
            },

            {data: "remark", bSortable: false},
        ]
    });



});

function viewBal(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1020px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/multiQuery?corpId=" + data.corpId+"&queryDate="+data.endDate
    });
}

function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
