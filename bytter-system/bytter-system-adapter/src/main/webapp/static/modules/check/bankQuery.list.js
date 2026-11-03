bt.use(['form','datatable','btdate','select', 'corp','money','date','dicts','bank','selectTableType'], function(){
    $ = bt.jquery,form = bt.form();
    
    corp = bt.corp.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });

    /**
     * 帐号下拉框初始化
     */
    selectTable = bt.selectTableType.init({
        elem: "#bankAcc",
        type: 'POST',
        paging: true,
        // scrollX:"500px",scrollY:"200px",
        url: basePath + "/finacial/check/fccheckinit/jsonBankAccData",
        columns: [
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
            {"data": "curName", title: bt.lang("international.payment.curName")},
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")}
        ],
        clickCallback: function (data, tr, elem, full) {
            if (data&&data.bankAcc) {
                $("#bankAcc").val(data.bankAcc);
                $("#curId").val(data.curId);
            }
        }
    });


    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        buttonsLayout:true,
        colReorder: true,
        ordering:true,
   
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/check/bankquery/datatable"
        }
        ,columns: [
             {data:"tallyDate",bSortable:true,className:'center',
                  render: function(data,type,full,meta){
                  		 return bt.date.format(full.tallyDate,'yyyy-mm-dd');
             }},
             {data:"bankAcc",bSortable:true,className:'center'},
             {data:"curId",className:'center',bSortable:true, render: function (data, type, full, meta) {
                    return full.curName;
                }
             },
             {data:"debitMoney",bSortable:true,className:"textRight",
            	 render: function(data,type,full,meta){
            		 var debitMoney = bt.money.format(full.debitMoney);
            		 return debitMoney;
            	 }},
             {data:"creditMoney",bSortable:true,className:"textRight",
            		 render:function(data,type,full,meta){
            			 var creditMoney = bt.money.format(full.creditMoney);
            			 return creditMoney;
            		 }},
             {data:"summaryContent",bSortable:false,className:'center'},
             {data:"billNo",bSortable:true,className:'center'},
             {data:"redblueSign",bSortable:true,className:'center',
            	 render : function(data,type,full,mate){
            		 return bt.dict("Finacial.RedBlueSign",full.redblueSign);
            	 }},
             {data:"compareSign",bSortable:true,className:'center',
            	 render:function(data,type,full,meta){
            		 return bt.dict("finacial.CompareSign",full.compareSign);
            	 }},
      ],      
    });

    
    bt.btdate.init({
		elem:".date-interval",
		startname:"ged_tallyDate",
    	endname:"led_tallyDate",
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
   
 form.on('submit(excel)', function(data) {
 	   var url = basePath+"/finacial/check/bankquery/excel";
 	   DownLoadFile({url:url,data:data.field,method:'get'});
 		return false;
 	}); 
   

   
});


var DownLoadFile = function (options) {
    var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    
    $form[0].submit();
    $iframe.remove();
} 
		

/**
 * 单位回调
 */
function corpCallBack() {
    var corpId = corp.get("corpId").val();
    selectTable.get("bankAcc").setUrl({corpId:corpId});
}
		
	

	

