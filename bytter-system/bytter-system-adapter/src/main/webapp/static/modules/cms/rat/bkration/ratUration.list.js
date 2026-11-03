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
            'url': basePath+"/cms/rat/raturation/datatable"
        },
        buttons: [
        ]
        ,columns: [
            {data:"urationCode",bSortable:false,className:'center'},
            {data:"corpName",bSortable:false,className:'center'},
            {data:"bankName",bSortable:false,className:'center'},
            {data:"typeName",bSortable:false,className:'center'},
            {data:"itemName",bSortable:false,className:'center'},
            {data:"billName",bSortable:false,className:'center'},
            {data:"itemFlag",bSortable:false,className:'center',
            	render: function ( data, type, full, meta ) {
                    return bt.dict("RatModel.ItemFlag", data);
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
                    return bt.date.format(full.beginDate, 'yyyy-mm-dd');
                },
                bSortable:false,className:'center'},
            {data:"endDate",
                render:function( data, type, full, meta){
                    return bt.date.format(full.endDate, 'yyyy-mm-dd');
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

    	layer.open({
            type: 1,
            area: ['465px', '240px'], //宽高
            content: $('#test1'), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
    	});
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
        if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
            layer.closeAll();
        }
        var changeType = bt.dicts.init().get("changeType").val();
        // 手工选择授信
        parent.layer.open({
            type: 2,
            area: ['1150px', '530px'],
            maxmin: true,
            content: basePath + "/cms/rat/raturation/chooseration?urationIds="+urationIds+"&changeType="+changeType,
            end:function(){
            }
        });
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
