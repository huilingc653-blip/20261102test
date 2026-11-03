var corp;
var selectType;
var myChartSum;
var search;
bt.use(['form', 'dateType', 'selectType', 'moneyType', 'corp', 'datatablecore','customTable','jquery','form'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    bt.moneyType.init();
    selectType = bt.selectType.init();
    corp = bt.corp.init();

    /**
     * 查询
     */
    search = function () {
        bt.loading.show();
        $.ajax({
            type: "POST",
            url: basePath + "/bi/bankAccBalTrend/listDataTables?" + $('#searchForm').serialize(),
            dataType: "json",
            contentType: "application/json",
            success: function (result) {
                bt.loading.hide();
                initHtml(result);
            }
        });
    }
    function initHtml(data) {
        $("#appTable").show();
        // 获取产品的肤色
        // if(store.get('web-skin') != 'light-skin'){
        //     $('#appTable').addClass('table-dark')
        // }
        bt.customTable.init({
            elem:'#appTable',// table ID
            allData:data,//所有数据
            columns: [
                {
                    data: "corpName", className: 'center', bSortable: false,
                },
                {
                    data: "balDate", className: 'center', bSortable: false,
                    render: function (data, type, full, meta) {
                        if (full.balDate > dateType.get("seachEndDate").val()) {
                            return dateType.get("seachEndDate").val();
                        } else {
                            return full.balDate;
                        }
                    }
                },
                {
                    data: "curName", bSortable: false, className: 'center',
                },
                {
                    data: "bal", className: 'textRight', bSortable: false,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.bal);
                    }
                },
                {
                    data: "budgetMoney", className: 'textRight', bSortable: false,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.budgetMoney);
                    }
                },
                {
                    data: "lastBal", className: 'textRight', bSortable: false,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.lastBal);
                    }
                },
                {
                    data: "compareRate", className: 'textRight', bSortable: false,
                    render: function (data, type, full, meta) {

                        if (!full.bal && !full.lastBal) {
                            return "0%";
                        }
                        if (!full.lastBal) {
                            return "100%";
                        }
                        if (!full.bal) {
                            return "-100%";
                        }
                        var compareRate = bt.money.sub(full.bal, full.lastBal);
                        compareRate = compareRate / full.bal;
                        compareRate = bt.money.mul(compareRate, 100);
                        return compareRate + "%";
                    }
                }
            ],
           // mergeColumn:[0],//合并列信息
            btDrawCallback:function () {
                initBar();
            },
            summary: {
                pageSum: false,
                allSum: true,
                selectSum: true,
                format: {
                    decimal: 2
                },
                sumList: [
                    {
                        money: true,
                        colNum: '4',
                        dataName: 'bal'
                    },{
                        money: true,
                        colNum:'5',
                        dataName:'budgetMoney'
                    },
                    {
                        money: true,
                        colNum:'6',
                        dataName:'lastBal'
                    }
                ]
            }
        })
    }


    myChartSum = echarts.init(document.getElementById('chartSum'));
    function initBar(){
        var data = {
            corpId: corp.get("ins_corpId").val(),
            // seachBeginDate: bt.dateType.get("seachBeginDate").val(),
            seachEndDate: bt.dateType.get("seachEndDate").val(),
            curId: $("#curId").val()
        };
        var url = basePath + "/bi/bankAccBalTrend/getBankAccBal";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                result = $.parseJSON(result);
                var option = initChartSum(result);
                myChartSum.hideLoading();
                myChartSum.setOption(option, true);
                window.onresize = myChartSum.resize;

            }
        });
    }

    function initChartSum(result) {
        var option = result;
        return option;
    }

});
