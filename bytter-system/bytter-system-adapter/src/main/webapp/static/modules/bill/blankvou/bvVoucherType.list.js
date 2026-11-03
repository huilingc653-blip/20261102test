var billTable={};
bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    billTable=bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,       
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvvouchertype/datatable",                             
        },
        buttons: [
            {
                text:bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:blankvou:bvVoucherType:save'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text:bt.lang('common.button.batchDelete'),
                hide:bt.isnotResource('bill:blankvou:bvVoucherType:delete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                 }
                },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"code",bSortable:true},
            {data:"name",bSortable:true},
            {data:"isInboxName",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:"dataSource",bSortable:true},
            {data:"rmk"},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	if(bt.isResource('bill:blankvou:bvVoucherType:update')){
            		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
            	}
            	if(bt.isResource('bill:blankvou:bvVoucherType:delete')){
            		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
            	}         
                return html;
            }}
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
});

//下拉框
bt.use(['select'], function() {
    bt.select.init({
        elem:'.selectDemo',
        name:"eqs_name",
        valueField:"name",
        textField:"name",
        defaultVal:'',
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherType"       
    });
});

function add(){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/blankvou/bvvouchertype/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px','530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouchertype/add",
        end:function(){
//        	billTable.draw();
        	bt.datatable.refreshCurrentPage();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouchertype/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
		bt.navtab.addNavtabWithId(bt.lang('common.button.edit'),basePath + "/bill/blankvou/bvvouchertype/update?id="+data.id,"card"+data.id,"edit");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouchertype/update?id=" + data.id,
        end:function(){
//        	billTable.draw();
        	bt.datatable.refreshCurrentPage();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/blankvou/bvvouchertype/delete/" + data.id, function(result){
        	layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
        		bt.datatable.refreshCurrentPage();
         }
        },'JSON');
    });
}

function deleteBatch(data){
	var len = data.length;
	var rowData = data.splice(0,len);
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvVoucherType:delete',rowData[i].corpId)){
       	layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
       	 return;
       }
    }   
    layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
        var ids;
        for(i=0;i<len;i++){
        	if(ids){
        		ids=ids+","+rowData[i].id;
        	}else{
        		ids=rowData[i].id;
        	}
        }
        $.post(basePath + "/bill/blankvou/bvvouchertype/batchDelete",{"ids":ids}, function(result){
        		layer.alert(result.msg, {icon: result.success?1:2});
            	if(result.success){
            		bt.datatable.refreshCurrentPage();
             }
        },'JSON');
    });
}













