var datatable={},intercept=$('#intercept').val();
bt.use(['form','layedit','btdate','datatable','dicts','btImport','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/blackwhite/configure/datatable"
        },
        buttons: [
            //新增、批量删除
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                	addorUpdate(data,true);
                }
            },{
                text: bt.lang('common.button.batchDelete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchDel(data,true);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            }
        ]
        ,columns: [
            {data:"corpCodeName",bSortable:true},
            {data:"code",bSortable:true},
            {data:"listTypeName",bSortable:true},
            {data:"name",bSortable:true},
            {data:"isCommonName",bSortable:true},
            {data:"dataSourcesName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='<a class="" href="#" data-row="'+meta.row+'" onclick="viewDtl(this)">'+bt.lang("common.button.view")+'</a>'
                    if(data.dataSources=="02"){
                    	html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="addorUpdate(this)">'+bt.lang("common.button.edit")+'</a>';
                        html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="batchDel(this)">'+bt.lang("common.button.delete")+'</a>';
                    }
                    return html;
                }}
        ]
    });
    
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
//            url: basePath+"/import/excel/uploadCustom", 
            customParameUrl:basePath +"/blackwhite/configure/importConf/"+intercept,
            code: "e291a359a8864ccf888c5a028e9c6a03",
            titleRows: 0,
            headRows: 1,
            width:"100%",
            heigth:"100%",
            hideDownTempBtn:false,
            success: function (head,data,select) {
            	var datasource = $("#intercept").val();
            	bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath + "/blackwhite/configure/saveImport?intercept="+intercept,
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if(result.success){
                            bt.datatable.refreshCurrentPage();
                        }
                    }
                });
            }
        });
    };
    
});

function addorUpdate(data,flag){
	if(flag &&bt.navtab){
	    bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/blackwhite/configure/add/"+intercept);
	    return;
	}else{
	    var el = datatable.row(layui.jquery(data).data("row")).data();
		bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/blackwhite/configure/update/"+el.id);
	    return;
	}
}

function viewDtl(data){
    var el = datatable.row(layui.jquery(data).data("row")).data();
	if(bt.navtab){
	    bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/blackwhite/configure/view/"+el.id);
	    return;
	}
}

function batchDel(data,flag){
	var ids;
	if(!flag){
	   ids = datatable.row(layui.jquery(data).data("row")).data().id;
	}else{
        var len = data.length;
		if(!len){
	        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
	        return;
	    }
		var rowData = data.splice(0,len);
	    for(i=0;i<len;i++){
	        if(ids){
	            ids=ids+","+rowData[i].id;
	        }else{
	            ids=rowData[i].id;
	        }
	    }
	}
	parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/blackwhite/configure/batchDel",{"ids":ids}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
	
	
}
