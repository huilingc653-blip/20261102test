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
			{data:"blnctrAcc",bSortable:false},
			{data:"accName",bSortable:false},
			{data:"freezeSign",bSortable:false,render: function (data, type, full, meta) {
				return bt.dict("innerAccount.freezeSign",data.freezeSign);
			}},
			{data:"freezeType",bSortable:false,render: function (data, type, full, meta) {
				return bt.dict("fcFreeze.freezeType",data.freezeType);
			}},
			{data:"freezeMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.freezeMoney);
			}},
			{data:"accuFreezeMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.accuFreezeMoney);
			}},
			{data:"freezeOperator",bSortable:false},
			{data:"operateTime",bSortable:false},
			{data:"status",bSortable:false,render: function (data, type, full, meta) {
				return bt.dict("common.status",data.status);
			}}
        ]
    });
});


