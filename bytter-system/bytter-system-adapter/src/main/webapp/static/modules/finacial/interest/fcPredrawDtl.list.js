bt.use(['form','datatable','btdate','selectType', 'corp','dicts','mulselect','dateType','laypage','moneyType','rateType'], function(){
	$ = bt.jquery,form = bt.form();
	   
    bt.mulselect.init();
    
    bt.dicts.init();
    
    bt.corp.init();
    
    var date = bt.dateType.init();
    var moneyFun = bt.moneyType.getFun();
    
    var select =  bt.selectType.init();
 
    //生成列表
    datatable = bt.datatable.init({
    	 elem: "#appTable",
         colReorder: true,
         order: [2,'desc'],
         buttonsLayout:true,
         searchForm: {
             formElem: "#searchForm",
             formFilter: "search"
         },
        
        ajax: {
            'url': basePath+"/finacial/interest/fcpredrawdtl/datatable"
        },
       
        columns: [
            {data:"account",bSortable:false,className:"center"},
            {data:"accName",bSortable:false,className:"center"},
            {data:"orderNo",bSortable:true,className:"center"},
            {data:"interestTime",bSortable:false,className:"center"},
            {data:"interestStartDate",bSortable:false,className:"center"},
            {data:"blnInstDate",bSortable:false,className:"center"},
            {data:"interestRate",bSortable:false,className:"textRight",
            	render : function(data,type,full,meta){
            		  var interestRate = bt.rateType.getFun().format(full.interestRate,'percent',4);
            		  return interestRate;
            		//            		if(full.interestRate != null){
//            			var interestRate = moneyFun.format(full.interestRate, {decimal:6});
//            			return interestRate;
//            		}else{
//            			return null;
//            		}
//            		
            	}},
            {data:"interest",bSortable:false,className:"textRight",
            		render : function(data,type,full,meta){
                		var interest = moneyFun.format(full.interest);
                		return interest;
                }},
            {data:"strikeDate",bSortable:false,className:"center"},
            {data:"strikeInterest",bSortable:false,className:"textRight",
            	render : function(data,type,full,meta){
            		return moneyFun.format(full.strikeInterest);
            	}
            },
        ],
        rowsGroup:[2,3,7]
    });
    
    
    $("#search").click(function(){
    	var curId = select.get("curId").val();
    	if(curId){
    		var curText = $('[data-biz-name="curId"]').find('select option[value="'+curId+'"]').text()
    		$("#curName").html(curText);
    		
    	}
    })
    
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
        	var url = basePath+"/finacial/interest/fcpredrawdtl/excel";
        	 DownLoadFile({url:url,data:data.field,method:'get'});
     		return false;
     	}); 
});
    
function reset(){
	bt.dicts.init().get("accType").val("");
	bt.dateType.init().get("ged_interestTime").val("");
	bt.corp.init().get("netId").val("");
	$("account").val("");
	$("#date").val("");
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

/*//网点下拉框回调
function netChange(obj){
	var netId = bt.corp.init().get("netId").val();
	var url = basePath + "/finacial/interest/fcpredrawdtl/getCorpsByNetId?netId="+netId;
	bt.mulselect.init().get("corpId").attr('data-url',url);
}*/

function formatDate(data,input){
	var date = addDate(data);
	$("#date").val(date);
}

function addDate(date, days) {
    if (days == undefined || days == '') {
        days = 1;
    }
    var date = new Date(date);
    date.setDate(date.getDate() + days);
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return date.getFullYear() + '-' + month + '-' + day;
}

