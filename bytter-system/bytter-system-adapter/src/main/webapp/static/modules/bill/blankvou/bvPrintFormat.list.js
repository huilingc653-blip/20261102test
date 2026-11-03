var billTable={};
parent.bvPrintFormatList=window;
bt.use(['form','select','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvprintformat/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('bill:blankvou:bvPrintFormat:save'),
                text:'' + bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                hide:bt.isnotResource('bill:blankvou:bvPrintFormat:delete'),
                text: ''+bt.lang('common.button.delete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }
        ]
        ,columns: [
            {data:"fmtNo",bSortable:true},
            {data:"fmtName",bSortable:true},
            {data:"voucherType",bSortable:true},
            {data:"sceneName",bSortable:true},
            {data:"paperSize",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('bill:blankvou:bvPrintFormat:update')){
                	html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                }
                if(bt.isResource('bill:blankvou:bvPrintFormat:copy')){
                	html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="copy(this)">'+bt.lang("common.button.clone")+'</a>';
                }
                if(bt.isResource('bill:blankvou:bvPrintFormat:delete')){
                	html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                }
                return html;
            }}
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
    
    bt.select.init({
        elem:'#voucherType',
        name:"eqs_voucherType",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValid"       
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/blankvou/bvprintformat/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvprintformat/add",
        end:function(){
            //location.reload();
        	//billTable.draw();
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
        content: basePath + "/bill/blankvou/bvprintformat/view/" + data.id
    });
}
function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit')+'-'+data.fmtNo,basePath + "/bill/blankvou/bvprintformat/update?id=" + data.id);
        return;
    }
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/bill/blankvou/bvprintformat/update?id=" + data.id,
		end:function(){
            //location.reload();
//        	billTable.draw();
			bt.datatable.refreshCurrentPage();
		}
	});
}
function copy(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.clone')+'-'+data.fmtNo,basePath + "/bill/blankvou/bvprintformat/copy?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvprintformat/copy?id=" + data.id,
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/blankvou/bvprintformat/delete/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        	bt.datatable.refreshCurrentPage();
        },'JSON');
    });
}
function deleteBatch(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
    	var rowData = data.splice(0,len);
        var ids;
        for(i=0;i<len;i++){
        	if(ids){
        		ids=ids+","+rowData[i].id;
        	}else{
        		ids=rowData[i].id;
        	}
        }
        $.post(basePath + "/bill/blankvou/bvprintformat/batchDelete",{"ids":ids}, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        	bt.datatable.refreshCurrentPage();
        },'JSON');
    });
}