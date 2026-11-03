bt.use(['form','datatable','dateType','select','moneyType','dicts','bank','corp','rateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
    var rateType = bt.rateType.getFun();
    //生成列表
    var table = bt.datatable.init({
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
        columns: [

            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                return '<a href="javascript:void(0);" class="bisFinanceExc-select" data-row="'+meta.row+'">' + bt.lang("common.button.select") + '</a>';
            }},
            {data:"billNo",bSortable:true,className:'center'},
            {data:"payerAcc",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"drawMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.drawMoney);
                }
            },
            {data:"drawBal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    var drawBal = moneyFun.sub(full.payMoney, full.drawMoney);
                    return bt.money.format(drawBal);
                }
            },
            {data:"blInterestRate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return rateType.format(full.blInterestRate);
                }
            },
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"isOnline",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isOnline);
                }
            },
            {data:"bankBillNo",bSortable:true,className:'center'}
        ]
    });
    
    table.on('click','.bisFinanceExc-select',function(event){
        //var func = top.btSelectCallBack['bt-biz-bisFinanceExc']['callback'];
        var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
        var func = top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-bisFinanceExc']['callback'];
        if(func){
            var data = table.row($(this).data('row')).data();
            func(data);
        }
    });
    
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
});