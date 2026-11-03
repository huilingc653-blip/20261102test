var billTable={};
var rasShops={};
bt.use(['form','datatable','btdate','moneynum','corp','rasShop','moneyType','select','dateType','bank','dicts','btImport','element'], function(){
    $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    rasShops=bt.rasShop.init();
    bt.bank.init({
        elem:'.bank'  
    });
    init(); 
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
            'url': basePath+"/ras/otherCardBinding/datatable"
        }, 
        buttons: [
            {
	            text:bt.lang('common.button.add'),
	            hide:bt.isnotResource('ras:otherCardBinding:op'),
	            action: function ( e, dt, node, config ) {
	                add();   
	            }
            },{ 
                text:bt.lang('common.button.taboo'),
                hide:bt.isnotResource('ras:otherCardBinding:op'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    dealBatch(data,"close"); 
                }
            },{ 
                text:bt.lang('common.button.enable'),
                hide:bt.isnotResource('ras:otherCardBinding:op'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    dealBatch(data,"open"); 
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            }
            
        ]
        ,columns: [
			{data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) { 
                	return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"shopCode",bSortable:true}, 
            {data:"shopName",bSortable:true},
            {data:"payTypeName",bSortable:true},
            {data:"accNo",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) { 
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            			html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
            	return html;
            }}
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
    corpCallback=function (data) {
    	 bt.rasShop.get("shopInfo").init({"corpId":data.id});
    }
    bt.corp.init();
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "80000098", 
            titleRows: 0, 
            headRows: 1,
            success: function (head,data,select) { 
                $.ajax({
	                    type:"POST", 
                    url: basePath + "/ras/otherCardBinding/saveImport", 
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
//跳转至添加页面 
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/ras/otherCardBinding/add");
        return;
    }
	parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/otherCardBinding/add",
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}

//查看详情 
function view(el){
	 var data = billTable.row(layui.jquery(el).data("row")).data();
	    if(bt.navtab){
	        bt.navtab.addNavtab(bt.lang('common.button.view') ,
	        		basePath + "/ras/otherCardBinding/view/" + data.id);
	        return;
	    }
	    parent.layer.open({
	        type: 2,
	        area: ['1080px', '720px'],
	        maxmin: true,
	        content: basePath + "/ras/otherCardBinding/view/" + data.id
	    });
}

//编辑更新
function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/ras/otherCardBinding/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/otherCardBinding/update/" + data.id,
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}


//启用禁用
function dealBatch(data,operate){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
    	if(bt.isnotResource('ras:otherCardBinding:op',rowData[i].corpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
//    	if(rowData[i].status<-1 && rowData[i].status>0){
//    		layer.alert(bt.lang('bill.blankvou.bvPayeeCheque.delete.check'), {icon: 7});
//        	return;
//    	}
    }
    var msgTips;
    if(operate=="open"){
    	msgTips='common.message.confirm.batchEnable';
    }else{
    	msgTips='common.message.confirm.taboo';
    }
    parent.layer.confirm(bt.lang(msgTips),function(){
        var ids;
        for(i=0;i<len;i++){
        	if(ids){
        		ids=ids+","+rowData[i].id;
        	}else{
        		ids=rowData[i].id;
        	}
        }
        $.post(basePath + "/ras/otherCardBinding/batchStorage/"+operate,{"ids":ids}, function(result){
        	if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
        		bt.datatable.refreshCurrentPage();
        	}else{
 	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
        	}
        },'JSON');
    });
}

//重置
function resetUpdate(){
    init();
}

function  init(){
}
