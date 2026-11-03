bt.use(['form','layedit','btdate','corp','dateType','datatable', 'bttable', 'moneynum','date'], function(){
    var $ = bt.jquery,form = bt.form();
    var getCloseDate;
    
    var corp = bt.corp.init();
    
    bt.getNetCloseDate = function(netId){
    	var action = "/finacial/closedeal/fcdayclose/toViewMonthCloseDate?netId="+netId;
        $.post(basePath + action, null,function(result){
            if(result){
                $("#closeYear").val(result.closeYear);
                $("#closeMonth").val(result.closeMonth);
            }
        },'JSON');
    };
    
    //监听重置
    bt.resetCode = function(netCode){
		corp.get("netCode").val(netCode);
    }
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/closedeal/fcdayclose/monthClosePreCheck";
        $.post(basePath + action,data.field,function(resu){
        	bt.loading.hide(); //关闭加载中
        	if("0" == resu.success){
        		action = "/finacial/closedeal/fcdayclose/doMonthClose";
        		$.post(basePath + action,data.field,function(result){
                    parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                    if(result.success){
                    	bt.getNetCloseDate(data.field.netCode);
                    	var noControlData = resu.unCheckList;
                    	if(noControlData.length > 0){
                    		$loadunCheckTables(noControlData, data.field.netCode, data.field.closeYear, data.field.closeMonth);
                    	}
                    	//隐藏表格
                    	bt.jquery("#closeTable").hide();
                    }
                },'JSON');
        		return false;
            } else {
            	//加载表格
            	bt.jquery("#closeTable").show(1000);
            	var controlData = resu.checkList;
            	var noControlData = resu.unCheckList;
            	$loadcheckTables(controlData, data.field.netCode, data.field.closeYear, data.field.closeMonth);
            	$loadunCheckTables(noControlData, data.field.netCode, data.field.closeYear, data.field.closeMonth);
            	if(controlData.length > 0){
            		layer.alert(bt.lang("Finacial.Close.dayMonthFail"));
            	}
            	return false;
            }
        },'JSON');

        return false;
    });
    
    //加载日结不成功原因表格
	$loadcheckTables = function(datas, netCode, closeYear, closeMonth){
		bt.bttable.init({
            elem:"#checkTable",
            checkbox:false,
            mulselect: false,
            selectCss:false,
            columns:[
                {data:"checkMessage",bSortable:false,render : function(data, type, full, meta){
                	var html = bt.dict("Finacial.Close.ClsMonthCheck",data.checkType);
                	return html;
                }}
            ],
            data : datas
       });
	}
	
	//加载日结不成功原因表格
	$loadunCheckTables = function(datas, netCode, closeYear, closeMonth){
		bt.bttable.init({
            elem:"#unCheckTable",
            checkbox:false,
            mulselect: false,
            selectCss:false,
            columns:[
                {data:"checkMessage",bSortable:false,render : function(data, type, full, meta){
                	var html = bt.dict("Finacial.Close.ClsMonthCheck",data.checkType);
                	return html;
                }}
            ],
            data : datas
       });
	}
	
	/**
	 * 得到两个日期之间的天数
	 * @param startDate
	 * @param endDate
	 */
	function getBetweenDay(startDate,endDate){
		var date1 = bt.date.get(startDate);
		var date2 = bt.date.get(endDate);
		var btt = date2 - date1;
		return btt/(1000*60*60*24);
	}
	/**
	 * 的到一个日期几天之后的时间
	 * @param date
	 * @param dayCount
	 * @returns {Date}
	 */
	function getDateAddDay(date,dayCount){
		return new Date(date.getTime()+dayCount*1000*60*60*24);
	}	

});

getCloseDate = function(data){
	bt.getNetCloseDate(data);
};

setCloseDay = function(data){
	bt.setCloseDay(data);
}

calcCloseDate = function(closeDay){
	bt.calcCloseDate(closeDay);
}

//监听重置
resetCode = function(){
	var netCode = $("#netId").val();
	if("" != netCode){
		bt.resetCode(netCode);
		getCloseDate(netCode);
	}
}

