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
            {data:"corpName",bSortable:false,className:'center'},
			{data:"billCode",bSortable:false,className:'center'},
			{data:"enterDate",bSortable:false,className:'center'},
			{data:"curName",bSortable:false,className:'center'},
			{data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.money);
			}},
			{data:"sendDate",bSortable:false,className:'center'},
			{data:"deadlineDate",bSortable:false,className:'center'},
			{data:"repayTypeName",bSortable:false,className:'center'}
        ]
    });
});


