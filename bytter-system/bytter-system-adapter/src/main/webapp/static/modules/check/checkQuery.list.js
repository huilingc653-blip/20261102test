
bt.use(['form','datatable','dateType','moneyType','dicts','bank','date'], function(){

    $ = bt.jquery,form = bt.form();
    //生成列表
    bt.dicts.init();
    bt.dateType.init();//时间
    var moneyType = bt.moneyType.init();

    var dateFun = bt.dateType.getFun();//

    bt.bank.init({elem:'.bank',});
    

    bt.datatable.init({
        elem: "#appTable",
      
        colReorder: true,
        ordering:true,
   
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/check/erpquery/datatable1?type="+$("#type").val()
        }
        ,columns: [
             {data:"compAcc",bSortable:true,className:"center"},
             {data:"curCode",className:'center',bSortable:true, render: function (data, type, full, meta) {
                    return full.curName;
                }
             },
             {data: "voucherNo",className:'center',bSortable:true},
             {data: "billNo",className:'center',bSortable:true},
             {data: "redblueSign",className:'center',bSortable:true,render: function( data, type, full, meta ){
            	 return bt.dict("Finacial.RedBlueSign",full.redblueSign);
			 }},
             {data:"tallyDate",bSortable:true,className:"center",
            	 render:function(data,type,full,meta){
            		 //数据查询出来默认转化成毫秒数据。具体原因为查询到
            		 if(full.tallyDate == null || full.tallyDate == ""){
            			 return "";
            		 }
            		 var tallyDate=bt.date.format(new Date(full.tallyDate), "yyyy-MM-dd");
            		 return tallyDate;
            	 }
             },
             {data:"debitMoney",bSortable:true,className:"textRight",
            	 render:function(data,type,full,meta){
            		 var debitMoney =bt.moneyType.getFun().format(full.debitMoney);
            		 return debitMoney;
            	 }
             },
             {data:"creditMoney",bSortable:true,className:"textRight",
            		 render:function(data,type,full,meta){
            			 var creditMoney = bt.moneyType.getFun().format(full.creditMoney);
            			 return creditMoney;
            		 }},
            /* {data:"balance",bSortable:true,className:"textRight",
            	 render: function(data,type,full,meta){
            		 var balance = bt.moneyType.getFun().format(full.balance);
            		 return balance;
            	 }},*/
            {data:"compareSign",bSortable:true,className:"center",
                	render:function(data,type,full,meta){
                		var compareSign = bt.dict("finacial.CompareSign",full.compareSign);
                		return compareSign;
                	}},	
      ]   
    });
});
