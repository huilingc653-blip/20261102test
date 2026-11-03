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

    bt.bank.init({
        elem: '.bank',
    });

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
            'url': basePath + "/report/incomeAndExpenditureOfFunds/dataTable"
        },
        columns: [
            {data: "regionName", bSortable: false},
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
                    if(full.corpId && full.corpId!=''){
                        html = '<a class="" href="#" data-row="'+meta.row+'" onclick="toListBisAccBal(this)">'+bt.money.format(full.realEndBal)+'</a>';
                    }
                    return html;
                }
            },
            {
                data: "manageMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.manageMoney);
                }
            },

            {data: "remark", bSortable: false},
        ]
    });



});


//导出数据
var exportsExcel = function () {
    var corpIdStr = corp.get("corpId").val();
    var startDate = dateType.get("ged_startDate").val();
    var endDate = dateType.get("led_endDate").val();
    var showType = dicts.get("showType").val();
    var equityMaintenance = mulselect.get("equityMaintenance").val();
    var url =  basePath+"/report/incomeAndExpenditureOfFunds/exportsExcel?corpIdStr="+corpIdStr+"&startDate="+startDate+"&endDate="+endDate+"&showType="+showType+"&equityMaintenance="+equityMaintenance;
    window.location.href = url;
}
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
function toListBisAccBal(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var endDate = dateType.get("led_endDate").val();
    var type = '';
    if(data.corpCode=='100000_0'){
        type = "D1023,D1004,D1005,D1159,D1160,D1002,D1003";
    }else if(data.corpCode=='100000_1'){
        type = "D1023";
    }else if(data.corpCode=='100000_2'){
        type = "D1004,D1005,D1159,D1160";
    }else if(data.corpCode=='100000_3'){
        type = "D1002";
    }else if(data.corpCode=='100000_4'){
        type = "D1003";
    }
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('余额查询'),basePath + "/bankacc/bisaccbal/toQuery?corpId=" + data.corpId+"&queryDate="+endDate+"&type="+type);
            return;
        }
    }
}