var selType={};
var form={};
bt.use(['form','datatable','selectType','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    selType = bt.selectType.init();
//    //生成列表
    var addRow=0;
    var datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        isInitData: false,
//        serverSide:false,
        checkbox:true,
        order: [[1,'desc']],
        ignoreButtons:['output','print'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            callback: function (data) {
            }
        },
        ajax: {
            'url': basePath+"/dataexchange2/basic/erpdynamicdata/datatable"
        },
        buttons: [
	          {
	              hide:bt.isnotResource('dataexchange:basic:erpdynamicdata:saveUpdate'),
	              text:bt.lang('common.button.add'),
	              action: function ( e, dt, node, config ) {
	            	  add();
//	            	  	dt.serverSide(false);
//	            	  	addRow++;
//	                    dt.row.add({
//	                    "id":       "add"+addRow,
//	                    "innerCode":   "1",
//	                    "innerName":   "2",
//	                    "innerReserve":   "3",
//	                    "externalCode":   "4",
//	                    "externalName":   "5",
//	                    "externalReserve":   "6"}).draw();
//	            	  var id="add"+addRow;
//	            	  var html='<tr>';
//	        		  html+='<td></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'innerCode" value="" class="layui-input" data-maxlength="50" autocomplete="off"/></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'innerName" value="" class="layui-input" data-maxlength="100" autocomplete="off"/></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'innerReserve" value="" class="layui-input" data-maxlength="500" autocomplete="off"/></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'externalCode" value="" class="layui-input" data-maxlength="50" autocomplete="off"/></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'externalName" value="" class="layui-input" data-maxlength="100" autocomplete="off"/></td>';
//	        		  html+='<td><input type="text"  id="' + id + 'externalReserve" value="" class="layui-input" data-maxlength="500" autocomplete="off"/></td>';
//	        		  html+='<td><a class="" href="#" onclick="opColumn(\''+id+'\');">'+bt.lang("common.button.save")+'</a>';
//	        		  html+='</tr>';
//	        		  $("#appTable tbody").append(html);
	              }
	          },{
	              hide:bt.isnotResource('dataexchange:basic:erpdynamicdata:saveUpdate'),
	              text:bt.lang('common.button.save'),
	              action: function ( e, dt, node, config ) {
	                  var data = dt.rows('.selected').data();
	                  batchOp(data);
	              }
	          },{
	              hide:bt.isnotResource('dataexchange:basic:erpdynamicdata:delete'),
	              text:bt.lang('common.button.delete'),
	              action: function ( e, dt, node, config ) {
	                  var data = dt.rows('.selected').data();
	                  deleteBatch(data);
	              }
	          },{
	              hide:bt.isnotResource('dataexchange:basic:erpdynamicdata:import'),
	              text: bt.lang('common.button.imports'),
	              action: function ( e, dt, node, config ) {
	                  importExcel();
	              }
	          },{
	              text: bt.lang('common.button.exports'),
	              action: function ( e, dt, node, config ) {
	            	  exportsExcel();
	              }
	          }
	      ]
        ,columns: [
            {data:"innerCode",bSortable:true,render:function (data, type, full, meta) {
            	return '<input type="text"  id="' + full.id + 'innerCode" value="'+(full.innerCode||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="50" autocomplete="off"/>';
    			}
            },
            {data:"innerName",bSortable:true,render:function (data, type, full, meta) {
            	return '<input type="text"  id="' + full.id + 'innerName" value="'+(full.innerName||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="100" autocomplete="off"/>';
            	}
            },
            {data:"innerReserve",bSortable:true,render:function (data, type, full, meta) {
            	return '<input type="text"  id="' + full.id + 'innerReserve" value="'+(full.innerReserve||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="500" autocomplete="off"/>';
        		}
            },
            {data:"externalCode",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + full.id + 'externalCode" value="'+(full.externalCode||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="50" autocomplete="off"/>';
            	}
            },
            {data:"externalName",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + full.id + 'externalName" value="'+(full.externalName||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="100" autocomplete="off"/>';
            	}
            },
            {data:"externalReserve",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + full.id + 'externalReserve" value="'+(full.externalReserve||'')+'" class="layui-input" lay-verify="strlength" data-maxlength="500" autocomplete="off"/>';
            	}
            },
             {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                 var html = '';
                 if(bt.isResource('dataexchange:basic:erpdynamicdata:saveUpdate')){
                	 html += '<a class="" href="#" data-row="'+meta.row+'" onclick="opColumn(\''+full.id+'\');">'+bt.lang("common.button.save")+'</a>';
                 }
                 if(bt.isResource('dataexchange:basic:erpdynamicdata:delete')&&full.id.indexOf('add')!=0){
                     if(html!=''){
                    	 html+='<span class="text-explode">|</span>';
                     }
                     html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(\''+full.id+'\');">'+bt.lang("common.button.delete")+'</a>';
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
            code: "60000001",
            titleRows: 0,
            headRows: 1,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/dataexchange2/basic/erpdynamicdata/saveImport",
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

//导出数据
var exportsExcel = function () {
	if(!form.verifyFun('#searchForm')){
        return false;
    }
	var typeCode=selType.get("typeCode").val();
	var dataTypeId=selType.get("dataTypeId").val();
	window.location = basePath+"/dataexchange2/basic/erpdynamicdata/exportXls?typeCode="+typeCode+"&dataTypeId="+dataTypeId;
}

var deleteColumn=function(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
    	bt.loading.show();
        $.post(basePath + "/dataexchange2/basic/erpdynamicdata/delete/" + id, function(result){
    		bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        	bt.datatable.refreshCurrentPage();
        },'JSON');
    });
}

var deleteBatch=function(data){
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
		bt.loading.show();
		$.post(basePath + "/dataexchange2/basic/erpdynamicdata/batchDelete",{"ids":ids}, function(result){
			bt.loading.hide();
			parent.layer.alert(result.msg, {icon: result.success?1:2});
			bt.datatable.refreshCurrentPage();
		},'JSON');
	});
}

/**
 * 单个修改和新增操作
 */
var add=function(){
	var typeCode=selType.get("typeCode").val();
	var dataTypeId=selType.get("dataTypeId").val();
    parent.layer.open({
        type: 2,
        area: ['820px', '300px'],
        maxmin: true,
        content: basePath + "/dataexchange2/basic/erpdynamicdata/add?typeCode="+typeCode+"&dataTypeId="+dataTypeId,
        end:function(){
			bt.datatable.refreshCurrentPage();
        }
    });
}

/**
 * 单个修改和新增操作
 */
var opColumn=function(id){
	if(isEmpty($("#"+id+"innerCode").val())){
		layer.alert(bt.lang('dataexchange.message.innercode.isnotnull'), {icon: 7});
     	return;
	}
	var ids=new Array(id)
	saveOrUpdate(ids);
}

/**
 * 批量修改和新增操作
 */
var batchOp=function(data){
	var len = data.length;
	if(!len){
		layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
		return;
	}
	var rowData = data.splice(0,len);
	var ids=new Array();
	for(i=0;i<len;i++){
		ids[i]=rowData[i].id;
		if(isEmpty($("#"+rowData[i].id+"innerCode").val())){
			layer.alert(bt.lang('dataexchange.message.innercode.isnotnull'), {icon: 7});
	     	return;
		}
	}
	saveOrUpdate(ids);
}

/**
 * 进行修改和新增操作
 */
var saveOrUpdate=function(ids){
	if(!form.verifyFun('#appTable')){
        return false;
    }
	bt.loading.show();
	var dataList=getOpData(ids);
	var data={"typeCode":selType.get("typeCode").val(),
			"dataTypeId":selType.get("dataTypeId").val(),
			"dataList":dataList,
			};
//    $.post(basePath + "/dataexchange/basic/erpdynamicdata/saveOrUpdate",data, function(result){
//		bt.loading.hide();
//        parent.layer.alert(result.msg, {icon: result.success?1:2});
//    	bt.datatable.refreshCurrentPage();
//    },'JSON');

	$.ajax({
    	type: "POST",
    	url: basePath + "/dataexchange2/basic/erpdynamicdata/saveOrUpdate",
       	data: JSON.stringify(data),//将对象序列化成JSON字符串
       	dataType:"json",
       	contentType : 'application/json;charset=utf-8', //设置请求头信息 
    	success: function(result){
    		bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    		if(result.success){
    			bt.datatable.refreshCurrentPage();
            }
    	},
    	error: function(res){
    		bt.loading.hide();
    	}
    });
}

/**
 * 获取修改数据信息
 */
var getOpData=function(ids){
	var datas=new Array(ids.length)
	for(i=0;i<ids.length;i++){
		var data={"id":ids[i],
				"innerCode":($("#"+ids[i]+"innerCode").val()||""),
				"innerName":($("#"+ids[i]+"innerName").val()||""),
				"innerReserve":($("#"+ids[i]+"innerReserve").val()||""),
				"externalCode":($("#"+ids[i]+"externalCode").val()||""),
				"externalName":($("#"+ids[i]+"externalName").val()||""),
				"externalReserve":($("#"+ids[i]+"externalReserve").val()||"")};
		datas[i]=data;
	}
	return datas;
}
/**判断字符串是否为空*/
function isEmpty(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == ""){
		return true;
	}
	return false;
}

