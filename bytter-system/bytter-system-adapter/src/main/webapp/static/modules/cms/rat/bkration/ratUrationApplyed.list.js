bt.use(['form','datatable','btdate','money','laypage','dateType'], function(){
    $ = bt.jquery,form = bt.form();
  //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        ordering : true,
        checkbox:false,
        ajax: {
            'url': basePath+"/cms/rat/raturationapply/datatable?ratCode="+$("#ratCode").val()+"&corpId="+$("#corpId").val()
        },
        buttons: [
        ]
        ,columns: [
            {data:"applyCode",bSortable:true,className:'center'},
            {data:"corpId",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return full.corpName;
                }
            },
            {data:"curName",bSortable:false,className:'center'},
            {data:"itemName",bSortable:false,className:'center'},
            {data:"creditType",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("rat.creditType", full.creditType);
                }},
            {data:"itemFlag",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("RatModel.ItemFlag", data);
                }
            },
            {data:"money",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"beginDate",bSortable:false,className:'center'},
            {data:"endDate",bSortable:false,className:'center'},
            {data:"status",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("ration.status",data);
                }
            },
            // {data:"urationCode",bSortable:false,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var status = full.status;
            	var id = full.id;
                var html = '';
                if(status - 11 < 0 && status != -2 ){
	                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(\'' + id + '\')">'+bt.lang("common.button.edit")+'</a>&nbsp;|&nbsp;\
	                <a class="" href="#" data-row="'+meta.row+'" onclick="del(\'' + id + '\')">'+bt.lang("common.button.delete")+'</a>&nbsp;|&nbsp;';
                } else if (status - 95 == 0&&full.creditType=="1"){//循环授信可释放
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="release(\'' + id + '\')">'+bt.lang("common.ration.release")+'</a>&nbsp;|&nbsp;';
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(\'' + id + '\')">'+bt.lang("common.button.view")+'</a>';
                // html += '&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ]
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	//bt.navtab.refreshParent();
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
		bt.navtab.addNavtab(bt.lang('ration.urationapply.edit'),basePath + "/cms/rat/raturationapply/update?id=" + id + "&ratCode="+ratCode);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/raturationapply/update?id=" + id + "&ratCode="+ratCode,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}
function add(){
	var corpId = $("#corpId").val();
	var ratCode = $("#ratCode").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.urationapply.add'),basePath + "/cms/rat/raturationapply/add?ratCode=" + ratCode+ "&corpId="+corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturationapply/add?ratCode="+ratCode+ "&corpId="+corpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}

function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/raturationapply/delete/" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function release(id){
    parent.layer.confirm(bt.lang('common.ration.confirm.release'),function(){
        $.post(basePath + "/cms/rat/raturationapply/release?id=" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function view(id){
	var ratCode = $("#ratCode").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.urationapply.view'),basePath + "/cms/rat/raturationapply/view?id=" + id+"&ratCode="+ratCode);
        return;
    }
    parent.layer.open({
    	type: 2,
        area: ['1150px', '830px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturationapply/view?id=" + id+"&ratCode="+ratCode
    });
}

closeTab = function(){
	bt.close();
}