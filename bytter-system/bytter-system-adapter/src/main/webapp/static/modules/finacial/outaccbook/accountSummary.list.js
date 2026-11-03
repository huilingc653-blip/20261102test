bt.use(['form','datatable','dicts','corp','btdate','money','select'], function(){
    $ = bt.jquery,form = bt.form();
  
    //字典初始化
    bt.dicts.init();
    
    //网点初始化
    bt.corp.init();
    
    
    //生成列表
    datatable = bt.datatable.init({
    	elem: "#appTable",
        
        colReorder: true,
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        ajax: {
            'url': basePath+"/finacial/outaccbook/queryoutaccbook/accountSummarytable"
        },
       
        columns: [
            {data:"",bSortable:true},
            {data:"account",bSortable:true},
            {data:"accname",bSortable:true},
            {data:"subjectName",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true,},
            
        ]
    });
    
    //记账日期
    bt.btdate.init({
		elem:".date-interval",
		startname:"ged_tallyDate",
		endname:"led_tallyDate"
			});
    
    $.each($("div[type='list']"),function(i,item){
    	bt.select.init({
    		elem:"[field='"+$(item).attr("field")+"']",
    		name:$(item).attr("field"),
    		defaultVal:$(item).attr("defaultVal"),
    		valueField:$(item).attr("dictField"),
    		textField:$(item).attr("dictText"),
    		placeholder:{value:"",text:""},
    		search:true,
    		disabled:false,
    		url:$(item).attr("fieldhref")

    	});
    });
    
    
});





