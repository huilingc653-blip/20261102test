bt.use(['form','datatable','dateType','select','moneyType','dicts','bank','corp','rateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
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
            'url': basePath+"/chowmatistic/choinnerfinance/datatable"
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
                return '<a href="javascript:void(0);" class="choInnerFinance-select" data-row="'+meta.row+'">' + bt.lang("common.button.select") + '</a>';
            }},
            {data:"billNo",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"dayLimit",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.busType == "10") {
                        return full.dayLimit + bt.dict("pricing.fixedTerm", full.dateUnit);
                    }else{
                        return bt.dict("pricing.noticeType", full.noticeType);
                    }
                }
            },
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center',visible: setDeadlineDateVisible()},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"drawMoney",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.drawMoney);
                }
            },
            {data:"drawBal",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    var drawBal = moneyFun.sub(full.payMoney, full.drawMoney);
                    return bt.money.format(drawBal);
                }
            },
            {data:"bookMoney",bSortable:false,className:'textRight',visible: setBookMoneyVisible(),
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.bookMoney);
                }
            },
            {data:"purpose",bSortable:true}
        ]
    });
    
    table.on('click','.choInnerFinance-select',function(event){
        var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
        var func = top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-choInnerFinance']['callback'];
        if(func){
            var data = table.row($(this).data('row')).data();
            func(data);
        }
    });
});

function setDeadlineDateVisible(){
    var busType = $("#busType").val();
    if (busType == "10") {
        return true;
    } else {
        return false;
    }
}
function setBookMoneyVisible() {
    var busType = $("#busType").val();
    if (busType == "12") {
        return true;
    } else {
        return false;
    }
}