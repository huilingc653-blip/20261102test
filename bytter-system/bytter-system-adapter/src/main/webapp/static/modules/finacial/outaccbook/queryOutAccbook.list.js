bt.use(['form','datatable','dicts','corp','btdate','money','bank'], function(){
    $ = bt.jquery,form = bt.form();
  
    //字典初始化
    bt.dicts.init();
    
    //网点初始化
    bt.corp.init();
    
    //账号
    bt.bank.init({
        elem:'#account',
    })
    
    //生成列表
    datatable = bt.datatable.init({
    	elem: "#appTable",
        
        colReorder: true,
        buttonsLayout:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        ajax: {
            'url': basePath+"/finacial/outaccbook/queryoutaccbook/datatable"
        },
       
        columns: [
            {data:"tallyDate",bSortable:true},
            {data:"voucherNo",bSortable:true},
            {data:"orderNo",bSortable:true},
            {data:"account",bSortable:true},
            {data:"accname",bSortable:true},
            {data:"debitMoney",bSortable:true,className:"textRight",
            	render:function(data,type,full,mate){
            		var debitMoney = bt.money.format(full.debitMoney);
            		return debitMoney;
            	}},
            {data:"creditMoney",bSortable:true,className:"textRight",
            		render:function(data,type,full,mate){
            			var creditMoney = bt.money.format(full.creditMoney);
            			return creditMoney;
            		}},
            		
            {data:"subjectName",bSortable:true},
            {data:"measureUnit",bSortable:true,
            	render : function(data,type,full,meta){
            		return bt.dict("fcSubject.measureUnit",full.measureUnit);
            	}},
            {data:"summaryContent",bSortable:true},
            {data:"status",bSortable:true,
            	render:function(data,type,full,meta){
            		return bt.dict("Outer.Status",full.status);
            	}},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
            
        ]
    });
    
    //记账日期0
    bt.btdate.init({
		elem:".date-interval",
		startname:"ged_tallyDate",
		endname:"led_tallyDate"
			});
});



function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	
	parent.layer.open({
		type:2,
		area:['65%','70%'],
		maxmin:true,
		content:basePath+"/finacial/outaccbook/queryoutaccbook/view/"+data.id

	});
}
