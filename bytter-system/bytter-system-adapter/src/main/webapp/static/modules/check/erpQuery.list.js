bt.use(['form','datatable','btdate','select','corp','money','date','dicts','bank','selectTableType'], function(){
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
        elem: "#compAcc",
        type: 'POST',
        paging: true,
        // scrollX:"500px",scrollY:"200px",
        url: basePath + "/finacial/check/erpDataManager/jsonBankAccErpData",
        columns: [
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
            {"data": "curName", title: bt.lang("international.payment.curName")},
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")}
        ],
        clickCallback: function (data, tr, elem, full) {
            if (data&&data.bankAcc) {
                $("#compAcc").val(data.bankAcc);
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
            'url': basePath+"/finacial/check/erpquery/datatable?type="+$("#type").val()
        }
        ,columns: [
             {data:"tallyDate",bSortable:true,
            	 render:function(data,type,full,meta){
            		 return bt.date.format(full.tallyDate,'yyyy-mm-dd');
            	 }
             },
             {data:"compAcc",bSortable:true},
             {data:"curCode",className:'center',bSortable:true, render: function (data, type, full, meta) {
                    return full.curName;
                }
             },
             {data:"debitMoney",bSortable:true,className:"textRight",
            	 render:function(data,type,full,meta){
            		 var debitMoney = bt.money.format(full.debitMoney);
            		 return debitMoney;
            	 }},
             {data:"creditMoney",bSortable:true,className:"textRight",
            		 render : function(data,type,full,mate){
            			 var creditMoney = bt.money.format(full.creditMoney);
            			 return creditMoney;
            		 }},
             {data:"balance",bSortable:true,className:"textRight",
            			 render : function(data,type,full,mate){
            				 var balance = bt.money.format(full.balance);
            				 return balance;
            			 }},
             {data:"voucherNo",bSortable:true},
             {data:"redblueSign",bSortable:true,
            	 render : function(data,type,full,mate){
            		 return bt.dict("Finacial.RedBlueSign",full.redblueSign);
            	 }},
             {data:"compareSign",bSortable:true,
            	 render: function(data,type,full,mate){
            		 return bt.dict("finacial.CompareSign",full.compareSign);
            	 }},
             {data:"summaryContent",bSortable:true},
             {data:"dataSource",bSortable:true},
           
      ],      
    });
 
    bt.corp.init();
    
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
	   var url = basePath+"/finacial/check/erpquery/excel";
	   DownLoadFile({url:url,data:data.field,method:'post'});
		return false;
	}); 

    
});


/*===================下载文件
 * options:{
 * url:'',  //下载地址
 * data:{name:value}, //要发送的数据
 * method:'post'
 * }
 */
var DownLoadFile = function (options) {
    var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="post" />');
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
    selectTable.get("compAcc").setUrl({corpId:corpId});
}




