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
            {data:"blnctrAcc",bSortable:false},
            {data:"accName",bSortable:false},
            {data:"corpName",bSortable:false},
            {data:"curName",bSortable:false},
            {data:"accType",bSortable:false,render:function (data, type, full, meta) {
                return bt.dict("InnerAccount.AccType",data.accType);

            }},
            {data:"balanceWay",bSortable:false,render:function (data, type, full, meta) {
                return bt.dict("Admin.BalanceWay",data.balanceWay);
            }},
            {data:"accStartDate",bSortable:false},
            {data:"accOpenTime",bSortable:false},
            {data:"operatorCode",bSortable:false,render:function (data, type, full, meta) {
            	if(null == data.operatorCode || '' == data.operatorCode){
            		return '';
            	}
                return data.operatorCode;
            }},
            {data:"accState",bSortable:true,render:function (data, type, full, meta) {
                return bt.dict("inneraccount.fcStatus", data.accState);
            }}
        ]
    });
});


