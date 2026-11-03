bt.use(['form','datatable','btdate','money','laypage','dateType', 'corp'], function(){
    $ = bt.jquery,form = bt.form(); 
    var dateType = bt.dateType.init();
    
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }
    
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    
    bt.laypage({
		cont: 'page',
		pages: $("#total").val(),
		curr:$("#curr").val(),
		skip:true,
		jump:function(obj, first){
			var curr = obj.curr;
			var param = "";
			if(!first){
				var corpId = bt.corp.init().get("corpId").val();
				if(bt.empty(corpId)){
					layer.alert(bt.lang("table.ratUrationApply.selectCorp"));
					return false;
				}
				var beginDate = dateType.get("beginDate").val();
		    	/*var beginDate = dateType.get("beginDate").val();
		    	var endDate = dateType.get("endDate").val();*/
				param = "start="+$("#curr").val() + "&corpId="+ corpId +"&contractNo="+$("#contractNo").val()
					+"&beginDate="+beginDate;
				location.href = basePath + "/cms/rat/cprationapply/list?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	var corpId1 = bt.corp.init().get("corpId").val();
    	var corpId2 = $("#corpId").val();
    	if(bt.empty(corpId1)&&bt.empty(corpId2)){
    		layer.alert(bt.lang("table.ratUrationApply.selectCorp"));
    		return false;
    	}
    	search();
    });
    
    //监听重置
	resetCode = function(){
	    var inputs = $('.rCode').find('input');
	    inputs.each(function(){
	        $(this).val("");
	    })
	}
    
    corpInit = bt.corp.init();
});

function search(){
	var action =basePath + "/cms/rat/cprationapply/list";
    $("#searchForm").attr("action",action);
    $("#searchForm").submit();
}

//单位回调函数
bt.corpCallback = function(id) {
	//获取单位信息
	$.ajax({
		url : basePath + "/sys/corp/getSysCorp",
		method : 'POST',
		data : {
			id : id
		},
		dataType : 'json',
		success : function(data) {
			if (data.obj) {
				if (data.obj.userAccountCode) {
					$("#corpId").val(data.obj.id);
				}
			}
			form.render();
		}
	});
};
function add(disCode){
	var corpId = $("#uCorpId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.urationapply.add'),basePath + "/cms/rat/cprationapply/add?disCode=" + disCode+ "&corpId="+corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/cprationapply/add?disCode="+disCode+ "&corpId="+corpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function viewAdd(disCode){
	var corpId = $("#uCorpId").val();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.urationapply.list'),basePath + "/cms/rat/cprationapply/applyList?disCode=" + disCode);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/cprationapply/applyList?disCode="+disCode,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function view(disCode){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/cprationapply/viewApply?disCode=" + disCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/cprationapply/viewApply?disCode=" + disCode
    });
}
