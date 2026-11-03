bt.use(['form','datatable','dicts','corp','btdate','money','bank','dateType'], function(){
    $ = bt.jquery,form = bt.form();
  
    bt.dateType.init();
    //字典初始化
    bt.dicts.init();
    
    //网点初始化
    bt.corp.init();
    
    //账号
    bt.bank.init({
        elem:'.bank',
    });
    
    //生成列表
    datatable = bt.datatable.init({
    	elem: "#appTable",
    	buttonsLayout:true,
        colReorder: true,
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        ajax: {
            'url': basePath+"/finacial/outaccbook/queryoutaccbook/balancetable"
        },
       
        columns: [
            {data:"balanceOptDate",bSortable:true,className:"center"},
            {data:"account",bSortable:true,className:"center"},
            {data:"accname",bSortable:true,className:"center"},
            {data:"subjectName",bSortable:true,className:"center"},
            {data:"balanceWay",bSortable:true,className:"center",
            	render : function(data,type,full,meta){
            		return bt.dict("Tally.Direction",full.balanceWay);
            	}},
            {data:"tBalance",bSortable:true,className:"textRight",
            		render : function(datam,type,full,meta){
            			var tBalance = bt.money.format(full.tBalance);
            			return tBalance;
            		}},
            
        ]
    });

});



