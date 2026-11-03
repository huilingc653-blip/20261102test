var dicts,corp,form,mulselect,$;
bt.use(['form','corp','dicts','treeTable','mulselect','region','btImport'], function () {
    $ = bt.jquery, form = bt.form();
    mulselect=bt.mulselect.init();
    bt.treeTable.init({
        elem: '#appTable',
        column: '1'
    });
    //监听开关事件
    form.on('switch(isOnlineFilter)', function (data) {
    	var checked = data.elem.checked;
    	var mchId = data.elem.attributes['dataId'].nodeValue;
    	var isOnline=checked?1:0;
    	bt.loading.show();
        $.post(basePath + "/ttpay/merchant/updateIsOnline",{"id":mchId,"isOnline":isOnline}, function(result){
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        	if(result.success){
                searchForm();
        	}
        },'JSON');
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
});

function searchForm(){
    var action =basePath + "/ttpay/merchant/list";
    $("#searchForm").attr("action",action);
    $("#searchForm").submit();
}

function resetClick(){
	corp.get('corpId').val("");
	dicts.get('payType').val("");
	$('#merchantNo').val("");
	//form.render('checkbox');
}

//新增
function addMerchant(){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/ttpay/merchant/add");
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/ttpay/merchant/add",
		end:function(){
			searchForm();
		}
	});
}

//导入
function importExcel(){
	bt.btImport.init({
        code: "990000005c5dcd90015c5ttpay790L00",
        titleRows: 0,
        headRows: 1,
        success: function (head,data,select) {
            $.ajax({
                type:"POST",
                url: basePath + "/ttpay/merchant/saveImport",
                dataType:"json",
                contentType:"application/json",
                data:JSON.stringify(select),
                //重写success方法
                success:function(result){
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
        			searchForm();
                }
            });
        }
    });
}

//编辑更新
function updateMerchant(mchId){
	if(bt.navtab){
      bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/ttpay/merchant/update?id=" + mchId);
      return;
  }
  parent.layer.open({
      type: 2,
      area: ['850px', '530px'],
      maxmin: true,
      content: basePath + "/ttpay/merchant/update?id=" + mchId,
      end:function(){
    	  searchForm();
      }
  });
}


//删除数据
function deleteMerchant(mchId){
    var msgTips='common.message.confirm.delete';
    layer.confirm(bt.lang(msgTips),function(){
    	bt.loading.show();
        $.post(basePath + "/ttpay/merchant/delete",{"id":mchId}, function(result){
        	if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                searchForm();
        	}else{
  	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
        	}
	        bt.loading.hide();
        },'JSON');
    });
}
