bt.use(['form','datatable','corp','bttable'], function(){
    $ = bt.jquery,form = bt.form();
    var urlData = basePath + "/finacial/closedeal/fcdayclose/view" + $("#checkType").val() 
			+ "?netCode=" + $("#netCode").val()  + "&closeDate=" +  $("#closeDate").val()
			+ "&lastCloseDate=" + $("#lastCloseDate").val();
    
    //生成列表
    bt.bttable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        url: urlData,
        buttons: [
        ]
        ,columns: [
            {data:"tallyDate",bSortable:false},
            {data:"orderNo",bSortable:false},
            {data:"account",bSortable:false},
            {data:"debitMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.debitMoney);
			}},
            {data:"creditMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.creditMoney);
			}},
			{data:"summaryContent",bSortable:false},
            {data:"status",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Outer.Status", data.status);
            }},
            {data:"operator",bSortable:false}
        ]
    });
});


