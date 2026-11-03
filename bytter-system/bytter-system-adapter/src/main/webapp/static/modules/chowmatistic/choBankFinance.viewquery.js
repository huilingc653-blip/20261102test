var datatable;
bt.use(['form','datatable','select','selectTable','dateType','corp','money','moneyType','rateType','dicts','bank', 'printType'], function(){
    var $ = bt.jquery, form = bt.form();
    bt.printType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/chobankfinance/datatable"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        }
        ,columns: [
            {data:"billNo",bSortable:true,className:'center'},
            {data:"busType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choBankFinance.busType",full.busType);
                }
            },
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"interestRate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return rateType.format(full.interestRate);
                }
            },
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            }
        ]
    });});