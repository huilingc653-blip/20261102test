var selType={};
bt.use(['form','layedit','selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    selType = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function(params){
    	var infoData={
				"innerCode":params.field.innerCode,
				"innerName":params.field.innerName,
				"innerReserve":params.field.innerReserve,
				"externalCode":params.field.externalCode,
				"externalName":params.field.externalName,
				"externalReserve":params.field.externalReserve};
    	var dataList=new Array(infoData);
    	var dataTypeId=selType.get("dataTypeId").val();
    	var dataTypeCode,dataTypeName;
    	if(dataTypeId){
    		var index=dataTypeId.indexOf("(");
    		if(index!=-1){
        		if(index!=0){
        			layer.alert(bt.lang('sys.form.verify.fmt.error',['(**)**']), {icon: 7});
        			return false;
        		}
        		var index=dataTypeId.indexOf(")");
        		if(index<2){
        			layer.alert(bt.lang('sys.form.verify.fmt.error',['(**)**']), {icon: 7});
        			return false;
        		}
        		dataTypeCode=dataTypeId.substring(1,index);
        		dataTypeName=dataTypeId.substring(1+index);
        		if(isEmpty(dataTypeName)){
        			layer.alert(bt.lang('sys.form.verify.fmt.error',['(**)**']), {icon: 7});
        			return false;
        		}
        		dataTypeId="";
    		}
    	}
    	var data={"typeCode":selType.get("typeCode").val(),
    			"dataTypeId":dataTypeId,
    			"dataTypeCode":dataTypeCode,
    			"dataTypeName":dataTypeName,
    			"dataList":dataList,
    			};
    	bt.loading.show();
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
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
        	},
        	error: function(res){
        		bt.loading.hide();
        	}
        });
    	return false;
    });
});

/**判断字符串是否为空*/
function isEmpty(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == ""){
		return true;
	}
	return false;
}
