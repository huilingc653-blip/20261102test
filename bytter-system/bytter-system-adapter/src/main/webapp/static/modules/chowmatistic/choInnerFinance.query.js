var datatable;
bt.use(['form','datatable','select','selectTable','dateType','corp','money','moneyType','dicts','bank','rateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
    //初始化单位
    bt.corp.init();
    bt.selectType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/choinnerfinance/dataTableQuery"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data:"billNo",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true},
            {data:"busType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choInnerFinance.busType",full.busType);
                }
            },
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"payeeAccName",bSortable:true},
            {data:"dayLimit",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.dayLimit+bt.dict("choInnerFinance.dateUnit",full.dateUnit);
                }
            },
            {data:"payDate",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    //交易日期:活期转定期、活期转通知:指"起息日期"
                    if(full.busType == 10 || full.busType == 12){
                        return full.interestStartDate;
                    }else{
                        //交易日期:定期转活期、通知转活期:指"支取日期",通知预约:"指预约日期" 取消通知预约:"取消预约日期"
                        return full.payDate;
                    }
                }
            },
            {data:"purpose",bSortable:true},
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choInnerFinance.status", full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
        ]
    });
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['900px','520px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choinnerfinance/view/" + data.id
    });
}
