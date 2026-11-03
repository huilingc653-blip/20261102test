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
    bt.selectType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/choinnerfinance/dataTableReport"
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
            {data:"corpName",bSortable:true},
            {data:"busType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choInnerFinance.depositType",full.busType);
                }
            },
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"curName",bSortable:true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.money.format(full.payMoney) + '</a>';
                    return html;
                }
            },
            {data:"drawMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.drawMoney);
                }
            },
            {data:"calEndDate",bSortable:true,className:'center'},
            {data:"calDays",bSortable:false,className:'textRight'},
            {data:"interestReceivable",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.interestReceivable);
                }
            },
            {data:"interestEarned",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.interestEarned);
                }
            },
            {data:"interestReceivableNot",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.interestReceivableNot);
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6',
                    dataName: 'payMoney'
                },
                {
                    colNum: '7',
                    dataName: 'drawMoney'
                },
                {
                    colNum: '10',
                    dataName: 'interestReceivable'
                },
                {
                    colNum: '11',
                    dataName: 'interestEarned'
                },
                {
                    colNum: '12',
                    dataName: 'interestReceivableNot'
                }
            ]
        }
    });

    //付款账号
    var payerAccInit = bt.selectTable.init({
      	 elem: "#payerAccTable",
      	 url: basePath+"/chowmatistic/choinnerfinance/getBankAcc",
      	 urldata: {corpId: $("input[name='corpId']").val(), busType: 10},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payerAcc",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payerAcc').val(data.bankAcc);
      			 $('#payerAccId').val(data.id);
      			 $('#curId').val(data.curId);
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });

    //单位回调函数
    bt.corpCallback = function(id){
    	//重新加载付款账号
    	payerAccInit.get("payerAcc").reload({corpId: id, busType: $("#busType").val()});
    }

    //初始化单位
    bt.corp.init();
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var viewUrl = basePath + "/chowmatistic/choinnerfinance/view/" + data.id;
    if(data.busType == '14'){
        viewUrl = basePath + "/agreeddeposit/choinneragreeddeposit/viewCho/" + data.id;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content:  viewUrl
    });
}
