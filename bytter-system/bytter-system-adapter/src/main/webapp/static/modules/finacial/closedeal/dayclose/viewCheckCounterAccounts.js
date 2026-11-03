bt.use(['form','datatable','corp','money','bttable'], function(){
    $ = bt.jquery,form = bt.form();
    var urlData = basePath + "/finacial/closedeal/fcdayclose/view" + $("#checkType").val() 
	    	+ "?netCode=" + $("#netCode").val()  + "&closeDate=" +  $("#closeDate").val()
			+ "&lastCloseDate=" + $("#lastCloseDate").val() + "&poolSign=" + $("#poolSign").val();
    //生成列表
    bt.bttable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [3,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        url : urlData,
        buttons: [
        ]
        ,columns: [
            {data:"busOriType",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.BusOriType", data.busOriType);
            }},
			{data:"raceName",bSortable:false,render: function (data, type, full, meta) {
				if(data.raceName){
					return data.raceName;
				}
                return "";
            }},
			{data:"voucherName",bSortable:false,render: function (data, type, full, meta) {
				if(data.voucherName){
					return data.voucherName;
				}
                return "";
            }},
			{data:"tallyDate",bSortable:false},
			{data:"interestStartDate",bSortable:false},
			{data:"orderNo",bSortable:false},
			{data:"account",bSortable:false},
			{data:"debitMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.debitMoney);
			}},
			{data:"creditMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
				return bt.money.format(data.creditMoney);
			}},
			{data:"voucherState",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.VoucherState", data.voucherState);
            }},
			{data:"operatorCode",bSortable:false}
        ]
    });
});


