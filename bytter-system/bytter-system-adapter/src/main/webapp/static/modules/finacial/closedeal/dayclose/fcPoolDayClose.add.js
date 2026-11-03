bt.use(['form','layedit','btdate','corp','dateType','datatable', 'bttable', 'moneynum','date', 'selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    var getCloseDate;
    
    var closeDayMoney = bt.moneynum.init({elem:$('[data-type="int"]'),decimal:0,minus:false,callback : function(data){
    	bt.calcCloseDate(data);
    }});
    var corp = bt.corp.init();
    var closeDate = bt.dateType.init();
    bt.selectType.init();

    // 重新设置资金池
    var corpPoolUrl = basePath + "/finacial/closedeal/fcdayclose?getUserCorpPools";
    bt.selectType.get("netCode").attr("data-url", corpPoolUrl);

    bt.setCloseDay = function(closeDateStr){
    	var lastCloseDate = $("#lastCloseDate").val();
    	var closeDay = getBetweenDay(lastCloseDate, closeDateStr);
    	if(closeDay - 0 <= 0){
    		closeDayMoney.setValue(1);
    		bt.calcCloseDate(1);
    		layer.alert(bt.lang("Finacial.Close.closeDate.GE.overDate"));
    		return;
    	}
    	closeDayMoney.setValue(closeDay);
    };
    
    bt.calcCloseDate = function(closeDay){
    	var lastCloseDate = $("#lastCloseDate").val();
    	//设置时间
    	var datestr = bt.date.format(bt.date.addDay(lastCloseDate, closeDay));
        closeDate.get("closeDate").val(datestr);
    };

    bt.getNetCloseDate = function(netId){
    	var action = "/finacial/closedeal/fcdayclose/toViewCloseDate?netId="+netId+"&poolSign=1";
        $.post(basePath + action, null,function(result){
            if(result){
                $("#lastCloseDate").val(result.lastCloseDateStr);
                $("#closeDay").val(result.closeDay);
                //设置时间
                closeDate.get("closeDate").val(result.closeDateStr);
            }
        },'JSON');
    };
    
    //监听重置
    bt.resetCode = function(netCode){
		corp.get("netCode").val(netCode);
		closeDayMoney.setValue("1");
    }
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var action = "/finacial/closedeal/fcdayclose/dayClosePreCheck";
        $.post(basePath + action,data.field,function(result0){
        	bt.loading.hide(); //关闭加载中
        	if("0" == result0.success){
        		action = "/finacial/closedeal/fcdayclose/doPoolDayClose";
        		$.post(basePath + action,data.field,function(result){
                    parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                    if(result.success){
                    	closeDayMoney.setValue(1);
                    	bt.getNetCloseDate(data.field.netCode);
                    	//隐藏表格
                    }
                },'JSON');
        		var noControlData = result0.unCheckList;
            	if(noControlData && noControlData.length > 0){
            		bt.jquery("#closeTable").show();
            		$loadnoCheckTables(noControlData, data.field.netCode, data.field.closeDate, data.field.lastCloseDate);
            	} else {
            		bt.jquery("#closeTable").hide();
            	}
        		return false;
            } else {
            	//加载表格
            	bt.jquery("#closeTable").show(1000);
            	var controlData = result0.checkList;
            	if(controlData && controlData.length > 0){
            		$loadcheckTables(controlData, data.field.netCode, data.field.closeDate, data.field.lastCloseDate);
            	}
            	var noControlData = result0.unCheckList;
            	if(noControlData && noControlData.length > 0){
            		$loadnoCheckTables(noControlData, data.field.netCode, data.field.closeDate, data.field.lastCloseDate);
            	}
            	if(controlData.length > 0){
            		layer.alert(bt.lang("Finacial.Close.dayCloseFail"));
            	}
            	return false;
            }
        },'JSON');
        
        /*$.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	bt.getNetCloseDate(data.field.netCode);
            }
        },'JSON');*/
        return false;
    });
    
    //加载日结不成功原因表格
	$loadcheckTables = function(datas, netCode, closeDa, lastCloseDate){
		bt.bttable.init({
            elem:"#checkTable",
            checkbox:false,
            mulselect: false,
            selectCss:false,
            columns:[
                {data:"modelName",bSortable:false, render : function(data, type, full, meta){
                	return bt.dict("Finacial.Close.DayCheckModel",data.modelName);
                }},
                {data:"checkMessage",bSortable:false,render : function(data, type, full, meta){
                	var html = "";
                	if("CheckCounterAccounts" == data.checkType){
						html += bt.lang("Finacial.Close.hasHandleAccBook");
					}else{
						html += bt.lang("Finacial.Close.DayCheckControlItem.exits") + bt.dict("Finacial.Close.DayCheckControlItem",data.checkType);
					}
					html += bt.lang("Finacial.Close.DayCheckControlItem.deal");
                	return html;
                }},
                {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" onclick="view(\'' + data.checkType + '\',\'' 
                    	+ netCode + '\',\'' + closeDa + '\',\'' + lastCloseDate + '\')">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
            ],
            data : datas
       });
	}
	
	//加载日结不成功原因表格
	$loadnoCheckTables = function(datas, netCode, closeDa, lastCloseDate){
		bt.bttable.init({
            elem:"#unCheckTable",
            checkbox:false,
            mulselect: false,
            selectCss:false,
            columns:[
                {data:"modelName",bSortable:false, render : function(data, type, full, meta){
                	return bt.dict("Finacial.Close.DayCheckModel", data.modelName);
                }},
                {data:"checkMessage",bSortable:false,render : function(data, type, full, meta){
                	var html = bt.lang("Finacial.Close.DayNoCheckControlItem.exits") + bt.dict("Finacial.Close.DayCheckControlItem",data.checkType)
                		+ bt.lang("Finacial.Close.DayCheckControlItem.deal");
                	return html;
                }},
                {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" onclick="view(\'' + data.checkType + '\',\'' 
                    	+ netCode + '\',\'' + closeDa + '\',\'' + lastCloseDate +'\')">'+bt.lang("common.button.view")+'</a>';
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

function view(checkType, netCode, closeD, lastCloseDate, poolSign){
    var data = "?checkType="+checkType + "&netCode="+netCode + "&closeDate="+ closeD
    	+ "&lastCloseDate=" +  lastCloseDate + "&poolSign=1";
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/closedeal/fcdayclose/toView" + data
    });
}


var getCloseDate = function(data){
	bt.jquery("#closeTable").hide();
	bt.getNetCloseDate(data.id);
};

var setCloseDay = function(data){
	bt.setCloseDay(data);
}

var calcCloseDate = function(closeDay){
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

