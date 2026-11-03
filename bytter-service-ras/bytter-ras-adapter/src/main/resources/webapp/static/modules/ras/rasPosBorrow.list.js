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
            'url': basePath+"/ras/posBorrow/datatable"
        },
        buttons: [
                  //确认，打回
            {
                text: bt.lang('common.button.sure'),
                hide:bt.isnotResource('ras:posBorrow:op'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data()
                    batchSure(data);
                }
            },{
                text: bt.lang('common.button.back'),
                hide:bt.isnotResource('ras:posBorrow:op'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data()
                    backBatch(data);
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
                   {data:"bdDate",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   if(full.status=='01' ){//待确认
						   if(bt.isResource('ras:posBorrow:op',full.corpId)){
							   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="sure(this)">'+bt.lang("common.button.sure")+'</a>';
							   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="back(this)">'+bt.lang("common.button.back")+'</a>';
						   }
					   }
					   return html;
				   }}
        ],
        fixedColumns:{
        	rightColumns:1
        }

    });
});

//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}
//确认
function sure(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var pid = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.sure'),function(){
        $.post(basePath + "/ras/posBorrow/sure",{"pid":pid}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}
//打回
function back(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var pid = data.id;
    var msg = $("#msg").val();
    if(isEmpty(msg)){
        //请输入打回原因
        layer.alert(bt.lang('common.message.config.please'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
        $.post(basePath + "/ras/posBorrow/back",{"pid":pid,"msg":msg}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}
//批量打回
function backBatch(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var msg=$("#msg").val();
    var pids;
    for(i=0;i<len;i++){
        if(bt.isnotResource('ras:posBorrow:op',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(isEmpty(msg)){
            //请输入打回原因
            layer.alert(bt.lang('common.message.config.please'), {icon: 7});
            return;
        }
        if(pids){
            pids=pids+","+rowData[i].id;
        }else{
            pids=rowData[i].id;
        }
        if(rowData[i].status!=01){
            layer.alert(bt.lang('common.message.config.please.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchBack'),function(){
        $.post(basePath + "/ras/posBorrow/batchBack",{"pids":pids,"msg":msg}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}
//批量确认
function batchSure(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var pids;
    for(i=0;i<len;i++){
        if(bt.isnotResource('ras:posBorrow:op',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(pids){
            pids=pids+","+rowData[i].id;
        }else{
            pids=rowData[i].id;
        }
        if(rowData[i].status!=01){
            layer.alert(bt.lang('common.message.config.sure.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchsure'),function(){
        $.post(basePath + "/ras/posBorrow/batchSure",{"pids":pids}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}
//查看
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var pid = data.id;
    
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/ras/posBorrow/view/" + pid);
        return;
    }
    
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/ras/posBorrow/view/" + pid
    });
}

/**判断字符串是否为空*/
function isEmpty(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == ""){
		return true;
	}
	return false;
}
