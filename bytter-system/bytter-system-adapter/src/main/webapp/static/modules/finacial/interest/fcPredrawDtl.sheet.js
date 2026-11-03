bt.use(['form','datatable','btdate','selectType', 'corp','dicts','mulselect','dateType','laypage','moneyType','rateType'], function(){
	$ = bt.jquery,form = bt.form();
	var mulSelect = bt.mulselect.init();
	//网点下拉框回调
	netChange = function(obj){
		var ids;
		var len = obj.length;
		if(len == "undifine"){
			ids = obj.id;
		}else{
			for(var i=0;i<obj.length;i++){
				if(i==0){
					ids = obj[i].id;
				}else{
					ids += "," + obj[i].id;
				}
			}
		}
		
		var url = basePath + "/finacial/interest/fcpredrawdtl/accountlist?netId="+ids;
		mulSelect.get("account").attr({"data-url":url});
		mulSelect.get("account").render();
	}
    
    
    bt.dicts.init();
    
    bt.corp.init();
    
    bt.dateType.init();
    var moneyFun = bt.moneyType.getFun();
    
    var select =  bt.selectType.init();
 
    //生成列表
    datatable = bt.datatable.init({
    	 elem: "#appTable",
         colReorder: true,
         ordering:true,
         buttonsLayout:true,
         searchForm: {
             formElem: "#searchForm",
             formFilter: "search"
         },
        
        ajax: {
            'url': basePath+"/finacial/interest/fcpredrawdtl/tablefactSheet"
        },
       
        columns: [
            {data:"corpName",bSortable:true,className:"center"},
            {data:"blnctrAcc",bSortable:true,className:"center"},
            {data:"accName",bSortable:true,className:"center"},
            {data:"accType",bSortable:true,className:"center",
            	render : function(data,type,full,meta){
            		return bt.dict("InnerAccount.AccType",full.accType);
            	}},
            {data:"interestTime",bSortable:true,className:"center"},
            {data:"interestStartDate",bSortable:true,className:"center"},
            {data:"blnInstDate",bSortable:true,className:"center"},
            {data:"days",bSortable:true,className:"center"},
            {data:"interestType",bSortable:true,className:"center",
            	render : function(data,type,full,meta){
            		return bt.dict("interest.type",full.interestType);
            	}},
            {data:"interestSign",bSortable:true,className:"center",
            	render : function(data,type,full,meta){
            		return bt.dict("interest.sign",full.interestSign);
            	}},
            {data:"interestRate",bSortable:true,className:"textRight",
            	render : function(data,type,full,meta){
            		var interestRate = bt.rateType.getFun().format(full.interestRate,'percent',4);
           		  	return interestRate;
            		
            	}},
            {data:"interest",bSortable:true,className:"textRight",
            		render : function(data,type,full,meta){
                		var interest = moneyFun.format(full.interest);
                		return interest;
                }},
          
        ]
    });
    
    
    $("#search").click(function(){
    	var curId = select.get("curId").val();
    	if(curId){
    		var curText = $('[data-biz-name="curId"]').find('select option[value="'+curId+'"]').text()
    		$("#curName").html(curText);
    		
    	}
    })
  /*  
    $("#reset").click(function(){
    	reset();
    });*/
    
  /*  $.each($("div[type='list']"),function(i,item){
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
    });*/
    
   /* bt.laypage({
		cont: 'page',
		pages: $("#size").val(),
		curr:$("#pageNum").val(),
		limit:20,
		skip:true,
		jump:function(obj, first){
			var curr = obj.curr;
			if(!first){
				location.href = basePath + "/finacial/interest/fcpredrawdtl/factSheet";
				
			}
		}
    });
    
    //查询
    form.on('submit(search)', function(data){
    	var action =basePath + "/finacial/interest/fcpredrawdtl/factSheet";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    });*/
    
    //excel
    form.on('submit(excel)',function(data){
        	var url = basePath+"/finacial/interest/fcpredrawdtl/excelFactSheet";
        	 DownLoadFile({url:url,data:data.field,method:'get'});
     		return false;
     	}); 
});
    
function reset(){
	/*bt.dicts.init().get("accType").val("");
	bt.dateType.init().get("ge_interestTime").val("");
	bt.dateType.init().get("le_interestTime").val("");
	bt.corp.init().get("netId").val("");
	bt.corp.init().get("corpId").val("");
	$("#curId").val("");*/
	bt.mulselect.init().get("account").reset();
}

var DownLoadFile = function (options) {
    var config = $.extend(true, { method: 'get' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="_self" method="get" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
} 

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
	var month = date.getMonth() + 1;
	var day = date.getDate();
	return date.getFullYear() + '-' + month + '-' + day;
}



