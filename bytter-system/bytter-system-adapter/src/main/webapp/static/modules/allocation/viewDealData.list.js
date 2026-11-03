var datatable;
bt.use(['form', 'datatable', 'dateType', 'moneyType', 'corp','date','money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
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
            'url': basePath + "/report/merchantreceipt/viewDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "payerAcc", bSortable: true,className:'center'},
            {data: "payerAccName", bSortable: true,className:'center'},
            {data: "payerBank", bSortable: true,className:'center'},
            {data: "curName", bSortable: true,className:'center'},
            {data: "voucherName", bSortable: true,className:'center'},
            {data: "payMoney", bSortable: true,className:'center',
            	 render: function (data, type, full, meta) {
                     return bt.money.format(full.payMoney);
                 }
            },
            {data: "payeeAcc", bSortable: true,className:'center'},
            {data: "payeeAccName", bSortable: true,className:'center'},
            {data: "payeeBank", bSortable: true,className:'center'},
            {data: "createDate", bSortable: true,className:'center',
           	 render: function (data, type, full, meta) {
                    if(full.createDate != null) {
                        return bt.date.format(full.createDate, "yyyy-MM-dd");
                    }else{
                        return "";
                    }
                }
           },
           {data: "wishPayDay", bSortable: true,className:'center',
          	 render: function (data, type, full, meta) {
                   return bt.date.format(full.wishPayDay,"yyyy-MM-dd");
               }
          }
        ]

    });
});

function isEmpty(val){
	if(val=="" || val==null || val== undefined){
		return true
	}
	return false;
}
