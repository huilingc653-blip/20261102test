bt.use(['form','datatable','btdate','money','dateType','corp','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form(); 
  //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        ordering : false,
        checkbox:true,
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        ajax: {
            'url': basePath+"/cms/rat/ratcpuration/datatable"
        },
        buttons: [
        ]
        ,columns: [
            {data:"urationCode",bSortable:false,className:'center'},
            {data:"corpName",bSortable:false,className:'center'},
            {data:"typeName",bSortable:false,className:'center'},
            {data:"itemCode",bSortable:false,className:'center',
            	render: function ( data, type, full, meta ) {
            		var itemFlag = full.itemFlag;
            		if("0" == itemFlag){
            			return full.itemName;
            		} else {
            			return bt.dict("RatModel.ItemFlag", itemFlag);
            		}
            	}
            },
            {data:"curName",bSortable:false,className:'center'},
            {data:"money",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"beginDate",
                render:function( data, type, full, meta){
                    return bt.date.format(full.beginDate, 'yyyy-MM-dd');
                },
                bSortable:false,className:'center'},
            {data:"endDate",
                render:function( data, type, full, meta){
                    return bt.date.format(full.endDate, 'yyyy-MM-dd');
                },
                bSortable:false,className:'center'},
            {data:"billNo",bSortable:false,className:'center'},
            {data:"ratCode",bSortable:false,className:'center'},
            {data:"status",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("uration.status",data);
                }
            }
        ]
    });

    bt.closeRation = function(){
    	var data = bt.datatable.getSelectData();
    	if(data && data.length < 1){
    		layer.alert(bt.lang("common.button.selectnum"));
    		return false;
    	}
    	submitRation();
    }
    
    //结转
    bt.submitRation = function(){
    	var data = bt.datatable.getSelectData();
    	var ids = [];
    	// 遍历行数据
    	$.each(data,function(i, tr) {
    		ids.push(tr.id);
    	});
    	var urationIds = ids.join(";");
    	bt.checkChangeRation(urationIds);
    }
    
    //判断是否可以直接结转
    bt.checkChangeRation = function(urationIds){
    	var action = "/cms/rat/ratcpuration/checkChangeRation";
    	var params = {"urationIds" : urationIds}
    	$.post(basePath + action, params, function(result){
    		if(result && result.length > 0){
    			if(!bt.navtab){
	            	parent.layer?parent.layer.closeAll():layer.closeAll();
	            } else {
	            	layer.closeAll();
	            }
    			//说明有包含关系的授信，需要手工选择授信
    			parent.layer.open({
    				type: 2,
    				area: ['1150px', '530px'],
    				maxmin: true,
    				content: basePath + "/cms/rat/ratcpuration/chooseration?urationIds="+urationIds,
    				end:function(){
    				}
    			});
    		} else {
    			//直接结转
    			bt.rationChange(urationIds);
    		}
    	},'JSON');
    }
    
    //授信结转
    bt.rationChange = function(urationIds){
    	var params = {
    			urationIds : urationIds,
    	};
    	//结转调用
    	var action = "/cms/rat/ratcpuration/rationChange";
        $.post(basePath + action, params, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
	            if(!bt.navtab){
	            	parent.layer?parent.layer.closeAll():layer.closeAll();
	            } else {
	            	bt.navtab.refreshCurrent();
	            	layer.closeAll();
	            }
            }
        },'JSON');
    }
    
    //取消
    bt.cancelRation = function(){
    	parent.layer.closeAll();
    	layer.closeAll();
    }
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.refreshParent();
        	bt.navtab.closeCurrent();
        }
    }
    
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框
});


closeTab = function(){
	bt.close();
}

closeRation = function(){
	bt.closeRation();
}

submitRation = function(){
	bt.submitRation();
}

cancelRation = function(){
	bt.cancelRation();
}