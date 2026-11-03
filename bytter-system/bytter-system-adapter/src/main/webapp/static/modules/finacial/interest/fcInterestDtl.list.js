bt.use(['form','datatable','btdate','select', 'corp','dicts','money','dateType','bank','rateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.corp.init();
    bt.selectType.init();
    
   bt.bank.init({
	   elem:'.bank',    
   });
    
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        ordering:true,
        buttonsLayout:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
    
        ajax: {
            'url': basePath+"/finacial/interest/fcinterestdtl/datatable?isPool="+$("#isPool").val()
        }
        ,columns: [
                   {data:"account",bSortable:true,className:"center"},
                   {data:"accName",bSortable:true,className:"center"},
                   {data:"curName",bSortable:true,className:"center"},
                   {data:"interestTime",bSortable:true,className:"center"},
                   {data:"interestStartDate",bSortable:true,className:"center"},
                   {data:"blnInstDate",bSortable:true,className:"center"},
                   {data:"days",bSortable:true,className:"center"},
                   {data:"cumulation",bSortable:true,className:"textRight",
                	   render: function(data,type,full,metd){
                		   var cumulation = bt.money.format(full.cumulation);
                		   return cumulation;
                	   }},
                   {data:"interestRate",bSortable:true,className:"textRight",
                	   render : function(data,type,full,metd){
                		  var interestRate = bt.rateType.getFun().format(full.interestRate,'percent',4);
                 		  return interestRate;
//                		   var opt = {decimal:4};
//                		   var interestRate = bt.money.format(full.interestRate,opt);
//                		   return interestRate+"%";
                	   }},
                   {data:"interest",bSortable:true,className:"textRight",
                		   render : function(data,type,full,meta){
                			  var interest = bt.money.format(full.interest);
                			  return interest;
                		   }},
                   {data:"interestSignName",bSortable:true,className:"center"},             
                   ],
 
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

function formatDate(data,input){
	var date = addDate(data);
	$("#date").val(date);
}


function addDate(date,days){
	if(days == undefined || days == ''){
		days = 1;
	}
	var date = new Date(date);
	date.setDate(date.getDate() + days);
	var month = date.getMonth() +1;
	var day = date.getDate();
	return date.getFullYear() + '-' + month + '-' + day;
	
}

function reset(){
	$("#date").val("");
}

function netIdCallback(data) {
    // 重新设置资金池的子单位
    var corpPoolChildUrl = basePath + "/finacial/interest/fcinterestdtl.do?getUserCorpPoolChilds&poolId=" + data.id;
    bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);
}

