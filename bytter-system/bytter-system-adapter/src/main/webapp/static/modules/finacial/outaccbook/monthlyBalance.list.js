bt.use(['form','datatable','dicts','corp','btdate','jquery','select'], function(){
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
            'url': basePath+"/finacial/outaccbook/queryoutaccbook/dailyaccounttable"
        },
       
        columns: [
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            {data:"",bSortable:true},
            
        ]
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





