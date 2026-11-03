bt.use(['form','datatable','corp'], function(){
    $ = bt.jquery,form = bt.form();
    var urlData = basePath + "/finacial/closedeal/fcdayclose/view" + $("#checkType").val() 
			+ "?netCode=" + $("#netCode").val()  + "&closeDate=" +  $("#closeDate").val()
			+ "&lastCloseDate=" + $("#lastCloseDate").val();
    
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': urlData
        },
        buttons: [
        ]
        ,columns: [
            {data:"blnctrAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"netName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"subjectName",bSortable:true},
            {data:"accType",bSortable:true,render:function (data, type, full, meta) {
                return full.accTypeName;
            }},
            {data:"balanceWay",bSortable:true,render:function (data, type, full, meta) {
                return full.balanceWayName;
            }},
            {data:"accState",bSortable:true,render:function (data, type, full, meta) {
                return full.accStateName;
            }}
        ]
    });
});


