var billTable={};
bt.use(['form','select','dicts','btdate','moneyType','corp','element','datatable','rateType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    var isInitData=false;
    if($("#isView").val()==1){
    	isInitData=true;
    }
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        isInitData: isInitData,
        order: [[2,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillTransferDown/dataCorpDtlTable"
        },
        buttons: []
        ,columns: [
            {data:"corpCode",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"netCode",bSortable:true,
                render: function ( data, type, full, meta ) {
                	if(full.netCode){
                		return '('+full.netCode+')'+full.netName;
                	}
                    return '';
                }
            },
            {data:"businessDate",bSortable:true},
            {data:"allocatedCode",bSortable:true},
            {data:"shMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.shMoney);
                }
            },
            {data:"gjMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.gjMoney);
                }
            },
            {data:"xbMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.xbMoney);
                }
            }
            // ,{data:"netBalMoney",bSortable:true,className:'textRight',
            //     render: function ( data, type, full, meta ) {
            //         return bt.money.format(full.netBalMoney);
            //     }
            // }
        ],
        summary:{
            pageSum:true,
            sumList:[
                {
                    colNum:'4',
                    dataName:'shMoney'
                },{
                    colNum:'5',
                    dataName:'gjMoney'
                },{
                    colNum:'6',
                    dataName:'xbMoney'
                }
            ]
        }
    });
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
});