var billTable={};
var dates={};
bt.use(['form','select','btdate','corp','element','datatable','rateType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[0,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillTransferDown/dataNetCollectTable"
        },
        buttons: []
        ,columns: [
            {data:"corpId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="toCorpDtl(\''+full.corpId+'\')">('+full.corpCode+')'+full.corpName+'</a>';
                    return html;
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
            ,{data:"netBalMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.netBalMoney);
                }
            }
        ],
        summary:{
            pageSum:true,
            sumList:[
                {
                    colNum:'2',
                    dataName:'shMoney'
                },{
                    colNum:'3',
                    dataName:'gjMoney'
                },{
                    colNum:'4',
                    dataName:'xbMoney'
                },{
                    colNum:'5',
                    dataName:'netBalMoney'
                }
            ]
        }
    });
    bt.corp.init();
    dates=bt.dateType.init();
});

var toCorpDtl=function(corpId){
    var beginDate=dates.get("beginDate").val();
    var endDate=dates.get("endDate").val();
    var paramsData="isView=1&corpId="+corpId+"&beginDate="+beginDate+"&endDate="+endDate;
    // if(bt.navtab){
    //     bt.navtab.addNavtab(bt.lang('common.button.view') ,
    //         basePath + "/bill/accebill/abbillTransferDown/toCorpDtl?" + paramsData);
    //     return;
    // }
    parent.layer.open({
        type: 2,
        area: ['900px', '550px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillTransferDown/toCorpDtl?" + paramsData
    });
};