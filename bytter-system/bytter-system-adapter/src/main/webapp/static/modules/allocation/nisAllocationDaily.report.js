var datatable,dateType,dateFun;
bt.use(['form', 'datatable', 'dateType', 'mulselect' ,'selectType', 'moneyType', 'dicts', 'bank', 'corp','date','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();
    bt.selectType.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    var rateType = bt.rateType.getFun();

    renderTable();
    function renderTable(){
        //生成列表
        datatable = bt.datatable.init({
            elem: "#appTable",
            colReorder: true,
            checkbox: false,
            paging:false,
            ignoreButtons:['colvis','print','state'],// 忽略的表单默认按钮 state取消状态保存按钮
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search",
                callback:function () {
                    $("#appTable").DataTable().destroy();
                    var queryDate = $('[name="queryDate"]').val();
                    if(queryDate){
                        var month = new Date(queryDate).getMonth() + 1;
                        var day = new Date(queryDate).getDate()
                        $("#currentDateBalance").text(month+'月'+day+'日银行余额')
                        $("#currentUsableBalance").text(month+'月'+day+'日银行可用余额')
                        $("#currentFrozenBalance").text(month+'月'+day+'日银行冻结余额')

                        var beforeDate = new Date(queryDate).getTime() - 86400000;
                        var month2 = new Date(beforeDate).getMonth() + 1;
                        var day2 = new Date(beforeDate).getDate()
                        $("#beforeDateBalance").text(month2+'月'+day2+'日银行余额')
                    }
                    renderTable()
                }
            },
            ajax: {
                'url': basePath + "/report/allocationDaily/datatable"
            },
            columns: [
                {data: "supCorpName", bSortable: false},
                {data: "name", bSortable: false},
                {data: "maxRate", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        if(null != full.maxRate && "" != full.maxRate){
                            return rateType.format(full.maxRate);
                        }else{
                            return '';
                        }
                    }
                },
                {data: "minRate", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        if(null != full.minRate && "" != full.minRate){
                            return rateType.format(full.minRate);
                        }else{
                            return '';
                        }
                    }
                },
                {data: "avgRate", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        if(null != full.avgRate && "" != full.avgRate){
                            return rateType.format(full.avgRate);
                        }else{
                            return '';
                        }
                    }
                },
                {data: "beforeDayBalance", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        var html = bt.money.format(full.beforeDayBalance);
                        if (!isEmpty(full.id)){
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBal(this,-1)">' + bt.money.format(full.beforeDayBalance) + '</a>';
                        }
                        return html;
                    }
                },
                {data: "currentBalance", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        var html = bt.money.format(full.currentBalance);
                        if (!isEmpty(full.id)){
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewBal(this,0)">' + bt.money.format(full.currentBalance) + '</a>';
                        }
                        return html;
                    }
                },
                {data: "availBal", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.availBal);
                    }
                },
                {data: "frzBal", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.frzBal);
                    }
                },
                {data: "finaMoney", bSortable: false, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.finaMoney);
                    }
                },
                {data: "remark", bSortable: false},
            ]
        });
    }

});

function viewBal(el,d) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    var queryDate = data.queryDate;
    queryDate = dateFun.format(dateFun.addDay(queryDate,d),"yyyy-MM-dd");

    parent.layer.open({
        type: 2,
        area: ['1020px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/query?corpId=" + data.id+"&queryDate="+queryDate
    });
}

function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
