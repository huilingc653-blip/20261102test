bt.use(['form','datatable','btdate','select', 'corp','dicts','money','bank','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    
    bt.dicts.init();
    bt.dateType.init();
    bt.corp.init();
    
    bt.bank.init({
        elem:'.bank', 
    });
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        buttonsLayout:true,
        ordering:true,
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        ajax: {
            'url': basePath+"/finacial/interest/fcaccubalancef/datatable"
        },
       
        columns: [
            {data:"account",bSortable:true,className:"center"},
            {data:"accname",bSortable:true,className:"center"},
            {data:"subjectName",bSortable:true,className:"center"},
            {data:"balanceDate",bSortable:true,className:"center"},
            {data:"cumu",bSortable:true,className:"textRight",
            	render : function( data , type , full , meta ){
            		var cumu = bt.money.format(full.cumu);
            		return cumu;
            }},
            {data:"adjustCumu",bSortable:true,className:"textRight",
            	render : function( data , type , full , meta ){
                    var adjustCumu = bt.money.format(full.adjustCumu);
                    return adjustCumu;
            }},
            {data:"interestSign",bSortable:true,className:"center",
            	render : function( data , type , full , meta ){
            		return bt.dict("Balance.InterestSign",data);
            }}
        

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
    
    //excel
    form.on('submit(excel)',function(data){
    	var url = basePath+"/finacial/interest/fcaccubalancef/excel";
    	DownLoadFile({url:url,data:data.field,method:'get'});
    	return false;
    
    });
    
});



var DownLoadFile = function(options){
	 var config = $.extend(true, { method: 'post' }, options);
	    var $iframe = $('<iframe id="down-file-iframe" />');
	    var $form = $('<form target="_self" method="' + config.method + '" />');
	    $form.attr('action', config.url);
	    for (var key in config.data) {
	        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
	    }
	    $iframe.append($form);
	    $(document.body).append($iframe);
	    
	    $form[0].submit();
	    $iframe.remove();
	} 






