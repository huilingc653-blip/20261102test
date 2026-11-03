bt.use(['form','datatable','btdate','money','laypage','dateType'], function(){ 
    $ = bt.jquery,form = bt.form();
  //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:false,
        ajax: {
            'url': basePath+"/cms/rat/ratcprationrelief/datatable?ratCode="+$("#ratCode").val()
        },
        buttons: [
        ]
        ,columns: [
            {data:"refCode",bSortable:false,className:'center'},
            {data:"corpName",bSortable:false,className:'center'},
            {data:"curName",bSortable:false,className:'center'},
            {data:"reliefCorpName",bSortable:false,className:'center'},
            {data:"reliefItemFlag",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                	var itemFlag = data;
                	if("0" == itemFlag){
                		return full.reliefItemName;
                	} else {
                		return bt.dict("RatModel.ItemFlag", data);
                	}
                }
            },
            {data:"byReliefCorpName",bSortable:false,className:'center'},
            {data:"byReliefItemFlag",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                	var itemFlag = data;
                	if("0" == itemFlag){
                		return full.byReliefItemName;
                	} else {
                		return bt.dict("RatModel.ItemFlag", data);
                	}
                }
            },
            {data:"reliefMoney",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"status",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("common.status",data);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var status = full.status;
            	var id = full.id;
                var html = '';
                if(bt.isResource('cms:cprat:relief:apply',full.corpId)){//编辑和删除权限
                	if(status - 11 < 0 && status != -2 ){
                		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(\'' + id + '\')">'+bt.lang("common.button.edit")+'</a>\
                		&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="del(\'' + id + '\')">'+bt.lang("common.button.delete")+'</a>&nbsp;|&nbsp;';
                	}
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(\'' + id + '\')">'+bt.lang("common.button.view")+'</a>&nbsp;|&nbsp;';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(\'' + id + '\')">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}
        ]
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.closeCurrent();
        }
    }
    
});

function search(){
	bt.navtab.refreshCurrent();
}

function update(id){
	var ratCode = $("#ratCode").val();
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('ration.bkrationrelief.edit'),basePath + "/cms/rat/ratcprationrelief/update?id=" + id + "&ratCode="+ratCode);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratcprationrelief/update?id=" + id + "&ratCode="+ratCode,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}
function add(){
	var id = $("#ratCode").val();
	var corpId = $("#corpId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationrelief.add'),basePath + "/cms/rat/ratcprationrelief/add?ratCode=" + id + "&corpId=" +corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationrelief/add?ratCode="+id + "&corpId=" +corpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}

function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratcprationrelief/delete/" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function view(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationrelief.view'),basePath + "/cms/rat/ratcprationrelief/view/" + id);
        return;
    }
    parent.layer.open({
    	type: 2,
        area: ['1150px', '830px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationrelief/view/" + id
    });
}

//日志列表
function listMsg(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/rat/ratcprationlog/listlog?rationId=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationlog/listlog?rationId=" + id
    });
}

closeTab = function(){
	bt.close();
}