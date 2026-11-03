bt.use(['form','datatable','btdate','money','dateType','dicts'], function(){
    $ = bt.jquery,form = bt.form();



    //结转
    bt.submitRation = function(){
    	var radioArr = jQuery("input:radio:checked");
    	var urationIds = "";
    	$.each(radioArr, function(i) {
    		urationIds = urationIds + radioArr[i].value+";";
    	});
    	if(urationIds.length < 1){
    		layer.alert(bt.lang("common.button.selectnum"));
    		return false;
    	}
    	bt.rationChange(urationIds);
    }


	//授信结转
	bt.rationChange = function(urationIds){
		var changeType = $("#changeType").val();
		var params = {
			urationIds : urationIds,
			changeType : changeType,
		};

		//结转调用
		var action = "/cms/rat/raturation/rationChange";
		$.post(basePath + action, params, function(result){
			if(result.success){
				if(!bt.navtab){
					parent.layer?parent.layer.closeAll():layer.closeAll();
				} else {
					bt.navtab.refreshCurrentTable();
					parent.layer?parent.layer.closeAll():layer.closeAll();
				}
			}
			parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
		},'JSON');
		return false;
	}

    //取消
    bt.cancelRation = function(){
    	parent.layer.closeAll();
    	layer.closeAll();
    }

    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.refreshCurrent();
        	layer.closeAll();
        }
    }

    bt.dicts.init();
});


closeTab = function(){
	bt.close();
}

closeRation = function(){
	bt.closeRation();
}

submitRation = function(){
	bt.submitRation();
}

cancelRation = function(){
	bt.cancelRation();
}

function selectAll(){
	// 查询所有tr包含uration-id属性的tr
	var trs = $("tr[uration-id]");
	if (trs && trs.length > 0){
		// 遍历tr
		for (let i = 0; i < trs.length; i++) {
			var urationId = $(trs[i]).attr("uration-id");
			// 查询所有radio value以uration-id开头的radio
			var radio = $("input[type='radio'][value^='"+urationId+"']");
			if (radio && radio.length == 1){
				radio.prop("checked",true);
			}
			if (radio && radio.length > 1){
				// 设置第一个radio为选中
				radio.eq(0).prop("checked",true);
			}
			form.render('radio')
		}


	}
}
