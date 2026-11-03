bt.use(['form','datatable','corp','btdate','dicts','bank','select','money','dateType'], function(){
    $ = bt.jquery,form = bt.form();
   
    //网点初始化
    bt.corp.init();
   
    //字典初始化
    bt.dicts.init();
    
   //银行帐号
    bt.bank.init({
        elem:'#account',
    });
    
    //时间
    bt.dateType.init();
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable", 
        colReorder: true,
        ordering:true,
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/interest/fcaccubalancef/datatablef"
        },
        buttons: [
            {
            	text: bt.lang('common.button.save'),
            	hide:bt.isnotResource('finacial:accubalancea:save'),
            	action: function ( e, dt, node, config ) {
            		bt.loading.show();
            		var data = dt.rows('.selected').data();
            		var len = data.length;
            		if(len<1){
            			layer.msg(bt.lang('common.message.confirm.selectData'));
                        bt.loading.hide();
            			return;
            		}
            		var ids="";
            		//var interestSign="";
            		var rowData = data.splice(0,len);
            		for(var i=0;i<rowData.length;i++){
            			if (ids == "") {
            				ids += rowData[i].id;
            			} else {
            				ids += "," + rowData[i].id;
            			}
            		};
            		parent.layer.confirm(bt.lang('finacial.check.cdm.msg.save'),function(){
            			$.post(basePath + "/finacial/interest/fcaccubalancef/save/",{ids:ids}, function(result){
                            bt.loading.hide();
            				bt.navtab.refreshCurrentTable();
            				parent.layer.alert(result.msg, {icon: result.success?1:2});
                            if(result.success){

                            }
            			},'JSON');
            		},function () {
                        bt.loading.hide();
                    });
            	}
            }
        ],
        columns: [
            
            {data:"account",bSortable:true,className:"center"},
            {data:"accname",bSortable:true,className:"center"},
            {data:"balanceDate",bSortable:true,className:"center"},
            {data:"cumu",bSortable:true,className:"textRight",
            	render : function(data,type,full,mate){
            		 var cumu = bt.money.format(full.cumu);
            		 return cumu;
            	}},
            {data:"adjustCumu",bSortable:true,className:"textRight",
            		render : function(data,type,full,mate){
            			var adjustCumu = bt.money.format(full.adjustCumu);
            			return adjustCumu;
            		}},
            {data:"interestSign",bSortable:true,className:"center",
            			render : function(data,type,full,mate){
            				var a = bt.lang("common.message.interest.no.interest.accrual");//未计息
            				var b = bt.lang("common.message.interest.yet.interest.accrual");//已计息
            				if(full.interestSign==0){
            					return a;
            				}else{
            					return b;
            				}
            				
            				//return bt.dict("Admin.InterestSign",full.interestSign);
            			}},
            {data:"interestTime",bSortable:true,className:"center"},			
            {data:"preSign",bSortable:true,className:"center",
            	render : function(data,type,full,mate){
            		var a = bt.lang("accu.balance.pre.sign.no");
            		if(full.preSign == null){
            			return a;
            		}else{
            			return bt.dict("accu.balance.pre.sign",full.preSign);
            		}
            	}},           
        ]
    });
    
    
   //币别
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
