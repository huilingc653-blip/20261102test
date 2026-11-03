var billTable={};
var corps={};
var rasShops={};
bt.use(['form','datatable','btdate','corp','dateType','select','btImport','dicts','rasShop'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    corps=bt.corp.init();
    rasShops=bt.rasShop.init();
    bt.dateType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[2,'asc'],[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/posBinding/datatable"
        },
        buttons: [
                  //新增、导入
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('ras:posBinding:op'),
                action: function ( e, dt, node, config ) {
                	add();
                }
            },{
                text: bt.lang('common.button.imports'),
                hide:bt.isnotResource('ras:posBinding:op'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	importExcel();
                }
            }
        ]
        ,columns: [
                   {data:"corpName",bSortable:true,
                       render: function ( data, type, full, meta ) {
                       return '('+full.corpCode+')'+full.corpName;
                   }},
                   {data:"shopCode",bSortable:true},
                   {data:"shopName",bSortable:true},
                   {data:"posNo",bSortable:true},
                   {data:"rmk",bSortable:true},
                   {data:"statusName",bSortable:true},
                   {data:"createDate",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   if(full.status=='01' ){//正常
						   if(bt.isResource('ras:posBinding:op',full.corpId)){
							   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
							   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="lend(this)">'+bt.lang("common.button.lend")+'</a>';
						   }
					   }
                       if(full.status=='03' ){//已借出显示收回
                           if(bt.isResource('ras:posBinding:op',full.corpId)){
                               html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="revoke(this)">'+bt.lang("common.button.revoke")+'</a>';
                           }
                       }
					   return html;
				   }}
        ],
        fixedColumns:{
        	rightColumns:1
        }

    });
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "80000002",
            titleRows: 0,
            headRows: 1,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/ras/posBinding/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(select),
                    //重写success方法
                    success:function(result){
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        location.reload();
                    }
                });
            }
        });
    }
});

//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}
//新增pos机绑定数据
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/ras/posBinding/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/posBinding/add",
        end:function(){
//        	billTable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}

//编辑pos机绑定数据
function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	var pid = data.id;
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/ras/posBinding/update/"+pid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/posBinding/update/"+pid,
        end:function(){
			bt.datatable.refreshCurrentPage();
        }
    });
}
//pos机借出数据
function lend(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var pid = data.id;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.lend'),basePath + "/ras/posBinding/lend/"+pid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/posBinding/lend/"+pid,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}
//查看
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var pid = data.id;
    
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/ras/posBinding/view/" + pid);
        return;
    }
    
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/ras/posBinding/view/" + pid
    });
}

/**pos机收回*/
function revoke(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.revoke'),function(){
        $.post(basePath + "/ras/posBinding/revokeSave",{"pid":id}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}