var rasShops={};
var dicts={};
var moneyType={};
bt.use(['form','moneyType','dicts'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
	//监听保存
	form.on('submit(save)', function(data){ 
		bt.loading.show();
	    $.ajax({
            type:"POST",
            url: basePath+ "/ras/auditStrategy/saveOrUpdate",
            dataType:"json",
            contentType:"application/json",
            data:JSON.stringify(data.field),
            success:function(result){
            	if(result.success){
    	            parent.layer.alert(result.msg, {icon: result.success?1:2});
                    bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
    	        }else{ 
    	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
    	        }
    	        bt.loading.hide();
            }
        });
	    return false;
	});
	//重置页面信息
    $("#reset").click(function(){
        location.reload();
        return false ;
    });
}); 

function funPayTypeSetRules(data,text,selectedData,beforeValue){
	dicts.get('rules').val("");
	if(selectedData&&selectedData.value){
		if(selectedData.value=='001'){
			dicts.get('rules').attr({
				'data-displayData' : '04',
				'data-filterData' : ''
			});
		}else if(selectedData.value=='006'){
			dicts.get('rules').attr({
				'data-displayData' : '05',
				'data-filterData' : ''
			});
		}else{
			dicts.get('rules').attr({
				'data-displayData' : '',
				'data-filterData' : '00,04,05'
			});
		}
	}else{
		dicts.get('rules').attr({
			'data-displayData' : '',
			'data-filterData' : '00'
		});
	}
}

