bt.use(['form','datatable','corp','bttable','money'], function(){
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
            {data:"loanAcc",bSortable:false,className:'center'},
			{data:"precativeId",bSortable:false,className:'center'},
			{data:"loanOpp",bSortable:false,className:'center'},
			{data:"contractCode",bSortable:false,className:'center'},
			{data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.money);
			}},
			{data:"startDate",bSortable:false,className:'center'},
			{data:"endDate",bSortable:false,className:'center'},
			{data:"curName",bSortable:false,className:'center'}
        ]
    });
});


