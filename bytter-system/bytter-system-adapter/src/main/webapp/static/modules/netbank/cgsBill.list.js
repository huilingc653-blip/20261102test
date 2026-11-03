var table
bt.use(['form','datatable','dateType','corp','money','moneyType','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000002",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head,data,select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type:"POST",
                    url: basePath + "/netbank/cgsbill/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }
    //生成列表
    table = bt.datatable.init({
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
            'url': basePath+"/netbank/cgsbill/datatable"
        },
        buttons: [
            {
            	hide:bt.isnotResource('netbank:netBank:cgsBill:apply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                	importExcel();
                }
            },{
            	hide:bt.isnotResource('netbank:netBank:cgsBill:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data()
                    var len = data.length;
                    if(len<1){
                    	layer.msg(bt.lang('common.button.selectnum'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,len);
                    var flag = false;
                    for(var i=0;i<rowData.length;i++) {
                        if (bt.isnotResource('netbank:netBank:cgsBill:apply', rowData[i].payeeCorpId)) {
                            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
                            return flag;
                        }
                        if(!flag){
                            if (ids == "") {
                                ids += rowData[i].id;
                            } else {
                                ids += "," + rowData[i].id;
                            }
                        }
                    }
                 	parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
                 	 $.post(basePath + "/netbank/cgsbill/batchSubmit/",{ids:ids}, function(result){
                         bt.datatable.refreshCurrentPage();
                         parent.layer.alert(result.msg, {icon: result.success?1:2});
                     },'JSON');
                 	});
                }
            },{
            	hide:bt.isnotResource('netbank:netBank:cgsBill:apply'),
            	text: bt.lang("common.button.batchDelete"),
	            action: function ( e, dt, node, config ) {
	                var data = dt.rows('.selected').data()
	                var len = data.length;
	                if(len<1){
	                	layer.msg(bt.lang('common.button.selectnum'));
	                	return;
	                }
	                var ids="";
	             	var rowData = data.splice(0,len);
	             	var flag = false;
                    for(var i=0;i<rowData.length;i++) {
                        if (bt.isnotResource('netbank:netBank:cgsBill:apply', rowData[i].payeeCorpId)) {
                            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
                            return flag;
                        }
                        if(!flag){
                            if (ids == "") {
                                ids += rowData[i].id;
                            } else {
                                ids += "," + rowData[i].id;
                            }
                        }
                    }
	             	parent.layer.confirm(bt.lang("common.message.confirm.batchDelete"),function(){
	             	 $.post(basePath + "/netbank/cgsbill/batchDelete/",{ids:ids}, function(result){
                         bt.datatable.refreshCurrentPage();
	                     parent.layer.alert(result.msg, {icon: result.success?1:2});
	                 },'JSON');
	             	});
            	}
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"consignDate",bSortable:true},
            {data:"consignCode",bSortable:true},
            {data:"payerCorpName",bSortable:true},
            {data:"payeeCorpName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"money",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.money);
                }
            },
            {data:"pactCode",bSortable:true},
            {data:"abs",bSortable:true},
            {data:"status",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("cgsBill.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	 var html='';
                 if(bt.isResource('netbank:netBank:cgsBill:apply')) {
                     html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                     html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                 }
                 html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                 return html;
            }}
        ],
        summary:{
            pageSum:false,
            allSum:true,
            selectSum:true,
            format:{
                decimal:2
            },
            sumList:[
                {
                    colNum:'6',
                    dataName:'money'
                }
            ]
        }
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/netbank/cgsbill/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/cgsbill/add",
        end:function(){
        	table && table.draw();
        }
    });
}

function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/netbank/cgsbill/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/cgsbill/update?id=" + data.id,
            end:function(){
                table && table.draw();
            }
        });
    }
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/cgsbill/view/" + data.id
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/netbank/cgsbill/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
