var datatable;
bt.use(['form', 'datatable', 'dateType', 'moneyType', 'dicts', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox:false,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/report/nisAllocationReq/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "billNo", bSortable: true,className:'center'},
            {data: "curName", bSortable: true,className:'center'},
            {data: "money", bSortable: true,className:'textRight',
            	 render: function (data, type, full, meta) {
                     return bt.money.format(full.money);
                 }
            },
            {data: "type", bSortable: true,className:'center',
            	 render: function (data, type, full, meta) {
                     return bt.dict("nisAllocationReq.type", full.type);
                 }
            },
            {data: "status", bSortable: true,className:'center',
           	 render: function (data, type, full, meta) {
           		 if(isEmpty(full.tallySign)){
           			return bt.dict("common.status", full.status);
           		 }else{
           			 return bt.dict("finacial.manualStatus", full.tallySign);
           		 }
                }
             },
             {data: "voucherState", bSortable: true,className:'center',
            	 render: function (data, type, full, meta) {
                     return bt.dict("bisExc.voucherStat", full.voucherState);
                 }
            },
            {data: "payerAcc", bSortable: true,className:'center'},
            {data: "payerAccName", bSortable: true,className:'center'},
            {data: "payerBankName", bSortable: true,className:'center'},
            {data: "payeeAcc", bSortable: true,className:'center'},
            {data: "payeeAccName", bSortable: true,className:'center'},
            {data: "payeeBankName", bSortable: true,className:'center'}
        ]
//        summary: {
//            pageSum: false,
//            allSum:
//                true,
//            selectSum:
//                true,
//            format:
//                {
//                    decimal: 2
//                }
//            ,
//            sumList: [
//                {
//                    colNum: '5',
//                    dataName: 'payMoney'
//                }
//            ]
//        }
    });
});

function isEmpty(val){
	if(val=="" || val==null || val== undefined){
		return true
	}
	return false;
}
