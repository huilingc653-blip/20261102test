bt.use(['form','corp','element','btdate','select','dicts','dateType','selectTable', 'selectType'], function(){
    $ = bt.jquery,form = bt.form();
    var element = layui.element();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    corpCall = function(ele){//网点回调
        if(bt.empty(ele.id)){
            return;
        }
        $("#netCode").val(ele.id);
        if("initList" == $("#opType").val()){
        	// 经办
        	location.href = basePath+"/finacial/interest/fcinterest/listBatchInterestPool?netId="+ele.id+"&opType="+$("#opType").val()+"&accType="+dicts.get('accType').val();
		}else{
        	// 复核
        	location.href = basePath+"/finacial/interest/fcinterest/checkBatchInterestPool?netId="+ele.id+"&opType="+$("#opType").val()+"&accType="+dicts.get('accType').val();
		}

//    	init();
        //accountInit.get("istAccount").reload({netId: ele.id, accType: "01"});
    }

    accTypeCall = function(ele){//开户种类回调
    	init();
    }
    bt.corp.init();
    dateType = bt.dateType.init();
    var dicts = bt.dicts.init();
    var params = "";
    bt.selectType.init();

    // 重新设置资金池
    //var corpPoolUrl = basePath + "/finacial/interest/fcinterest?getUserCorpPools";
    //bt.selectType.get("netCode").attr("data-url", corpPoolUrl);

    $.each($("div[type='list']"),function(i,item){
    	bt.select.init({
    		elem:"[field='"+$(item).attr("field")+"']",
    		name:$(item).attr("field"),
    		defaultVal:$(item).attr("defaultVal"),
    		valueField:$(item).attr("dictField"),
    		textField:$(item).attr("dictText"),
    		placeholder:{value:"",text:""},
    		search:true,
    		disabled:false,
    		url:$(item).attr("fieldhref")

    	});
    });
    
    //初始化参数
    function initParam(){
    	var accType = "";
    	if(dicts.get('accType').val()){
    		accType = dicts.get('accType').val().trim();
    	}
    	params = "netId="+$("#netId").val()+"&accType="+accType+"&opType="+$("#opType").val() +"&curr=1&poolSign=1";
    }

    //付款账号初始化
    var accountInit = bt.selectTable.init({
        elem: "#accountTable",
        url: basePath + "/finacial/interest/fcinterest?getPoolAccount",
        urldata: {netId: $("#netId").val(), accType: '01'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#account",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $("#account").val(data.blnctrAcc);
            } else {
                $("#account").val('');
                $('input.text').val('');
            }
        }
    });
    
	// 监听控制content高度
    function init() {
        // 起始判断收缩还是展开
    	initParam();
//    	getDayCloseDate();
    	initTabJX();
    	initTabPZ();
    	initTabJS();

        // 选项卡高度
        var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card2'] ul.layui-tab-title").height();
        // 需要减去的高度
        var height = $(window).height() - $('.layui-header').height() - cardTitleHeight;
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card2'] div.layui-tab-content").height(height - 200);
    }
	
 	// 初始化
    init();
    
    //利息清单
    function initTabJX(){
    	bt.jquery("#lxqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/interestBill?"+params);
		bt.jquery("#lxqdiframe").show(1000);
    }
    //计息凭证
    function initTabPZ(){
    	bt.jquery("#jxpziframe").attr("src", basePath + "/finacial/interest/fcinterest/toLoadInterestVoucher?"+params);
		bt.jquery("#jxpziframe").show(1000);
    }
    
    //积数清单
    function initTabJS(){
    	bt.jquery("#jsqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/accuBill?"+params);
		bt.jquery("#jsqdiframe").show(1000);
    }
    
    form.on('submit(calcInterest)', function(data){
    	$calcInterest("interest",bt.lang("table.fcAcc.interestSignOk"));//计息
    	return false;
    });
    
    form.on('submit(calcSubmit)', function(data){
    	$calcInterest("submit",bt.lang("common.button.submit"));//提交
    	return false;
    });
    
    form.on('submit(doCheck)',function(data){
    	$checkInterest("check",bt.lang("common.button.check"));//复核
    	return false;
    });
    form.on('submit(doBack)',function(data){
    	$checkInterest("back",bt.lang("common.button.back"));//打回
    	return false;
    });
    
    form.on('submit(doDeleteByBatch)', function(data){
    	$doDeleteByBatch();//删除
    	return false;
    });
    
    form.on('submit(doExportInterestBill)',function(data){
    	$doExportInterestBill();//导出利息清单
    	return false;
    });
    
    form.on('submit(doBatchPrint)',function(data){
    	$doBatchPrint();//打印记账凭证
    	return false;
    });
    
    form.on('submit(doPrintRateNotice)',function(data){
    	$doPrintRateNotice();//打印利息通知单
    	return false;
    });


    
    $doBatchPrint = function(){
    	var url=basePath+"/finacial/interest/fcinterest/doBatchPrint?opType="+$("#opType").val()
            +"&netCode="+$("#netId").val()+"&accTypeCode="+dicts.get("accType").val().trim()
            +"&poolSign=1";
    	parent.layer.open({
    		title:bt.lang("common.button.preview"),
            type: 2,
            area: ['70%', '50%'],
            maxmin: true,
            content: url,
            end:function(){
            }
        });
    }
    
    $doPrintRateNotice = function(){
    	if(!dateType.get("blnInstDate").val()||dateType.get("blnInstDate").val().trim()==""){
			layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon:5});//请输入止息日期！
			return;
		}
    	var  url=basePath+"/finacial/interest/fcinterest/doPrintRateNotice?opType="+$("#opType").val()+"&netCode="+$("#netId").val()+"&accType="
    	+dicts.get("accType").val().trim()+"&tallyDate="+dateType.get("tallyDate").val()+"&blnInstDate="+dateType.get("blnInstDate").val();
    	parent.layer.open({
    		title:bt.lang("common.button.preview"),
            type: 2,
            area: ['65%', '50%'],
            maxmin: true,
            content: url,
            end:function(){
            }
        });
    }
    
    $doDeleteByBatch = function(){
    	layer.confirm(bt.lang("finacial.interest.confirmMsg.deleteInterestInfo"), {//您确定要删除利息信息吗？
    		  icon: 3,
    		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
    		}, function(index, layero){
    			bt.loading.show();
    			$.ajax({
    	    		type: "POST",
    	    		url: basePath+"/finacial/interest/fcinterest/doDeleteByBatch",
    	    		data: {
    	    			"opType":$("#opType").val(),
    	    			"netCode" : $("#netId").val(),
    					"accTypeCode" : dicts.get("accType").val().trim(),
    	    		},
    	    		dataType : "json",
    	    		success: function(rs){
    	    			bt.loading.hide();
    	    			layer.msg(bt.lang("finacial.interest.InterestDelete"), {icon: 1});//删除成功
    	    			init();
    	    			//bt.navtab.refreshCurrent();
    	    		}
    	    	});
    			layer.close(index);
    		}, function(){
    		});
    }
    
    $doExportInterestBill = function(){
    	var url = basePath+"/finacial/interest/fcinterest/doExportInterestBill?opType="+$("#opType").val()
            +"&netCode="+$("#netId").val()+"&accTypeCode="+dicts.get("accType").val().trim()+"&poolSign=1";
        layer.confirm(bt.lang("finacial.interest.ExportPage"), {
        	  btn: [bt.lang("common.button.page"),bt.lang("common.button.notpage"),bt.lang("common.button.cancel")] //按钮
        	}, function(){//分页
        		location.href = url + "&impType=1";
        	}, function(){//不分页
        		location.href = url + "&impType=0";
        	}, function(){
        	});
    }
    
    $calcInterest=function(opType, opTips){
    	var tallyDate = dateType.get("tallyDate").val();
		var endInstDate = dateType.get("blnInstDate").val();
		if(!checkTallyDate()){
			return false;
		}
		if(!endInstDate||endInstDate.trim()==""){
			layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon:5});//请输入止息日期！
			return;
		}
		var dayCloseDate = $("#dayCloseDate").val();
		var closeDateEnd = bt.date.addDay(dayCloseDate, 1);
		
		if(bt.date.compare(endInstDate,closeDateEnd) > 0){//止息日期应小于等于日结后日期
			layer.alert(bt.lang("finacial.interest.endInstDatelessCloseDate",new Array(bt.date.format(closeDateEnd))), {icon:5});
			return false;
		}
		var method = "doAccInterestaccrual";
		var account = $("#account").val();
		if("" == account){
            method = "doInterestaccrualByBatch";
            // 批量计息校验计息配置
			if (!checkIstConfig()){
				layer.alert(bt.lang("fin.interest.not.doist"), {icon:5});//非计息日，无法结息
				return false;
			}
		}
		
		layer.confirm(bt.lang("finacial.interest.OpInterest",new Array(opTips)), {
  		  icon: 3,
  		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
  		}, function(index, layero){
  			bt.loading.show();
  			$.ajax({
	    		type: "POST",
	    		url: basePath+"/finacial/interest/fcinterest?" + method,
	    		data: {
	    			"opTypestr":opType,
	    			"netCode" : $("#netId").val(),
					"accTypeCode" : dicts.get("accType").val().trim(),
					"tallyDate" : tallyDate,
					"endInstDate" : endInstDate,
                    "poolSign" : '1',
                    "account" : $("#account").val()
	    		},
	    		dataType : "json",
	    		success: function(rs){
	    			if(rs.success){
	    				bt.loading.hide();
	    				if("interest" == opType){
                            layer.msg(bt.lang("finacial.interest.interest.success"), {icon:1});
						}
                        if("submit" == opType){
                            layer.msg(bt.lang("finacial.interest.submit.success"), {icon:1});
						}
	    				init();
	    			}else{
	    				bt.loading.hide();
	    				if("notBalance"==rs.obj){
	    					$calcInterest_notBalance(opType,tallyDate,endInstDate,rs.msg);
	    				}else{
                            if("interest" == opType){
                                layer.msg(bt.lang("finacial.interest.interest.fault") + ":" + rs.msg, {icon:5});
                            }
                            if("submit" == opType){
                                layer.msg(bt.lang("finacial.interest.submit.fault") + ":" + rs.msg, {icon:5});
                            }
	    					init();
	    				}
	    			}
	    			//bt.navtab.refreshCurrent();
	    		}
	    	});
  			//layer.close(index);
  		}, function(){
  		});
		
    }
    
    
    $calcInterest_notBalance = function(opType,tallyDate,endInstDate,msg){
    	layer.confirm(msg, {
    		  icon: 3,
    		  area: '500px',
    		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
    		}, function(index, layero){
    			bt.loading.show();
    			$.ajax({
  	    		type: "POST",
  	    		url: basePath+"/finacial/interest/fcinterest?doInterestaccrualByBatch",
  	    		data: {
  	    			"opTypestr":opType,
  	    			"netCode" : $("#netId").val(),
  					"accTypeCode" : dicts.get("accType").val().trim(),
  					"tallyDate" : tallyDate,
  					"endInstDate" : endInstDate,
  					"IsHoldOn":true
  	    		},
  	    		dataType : "json",
  	    		success: function(rs){
  	    			if(rs.success){
  	    				bt.loading.hide();
  	    				layer.msg(rs.obj, {icon:1});
  	    			}else{
  	    				bt.loading.hide();
  	    				layer.alert(rs.msg, {icon:5});
  	    			}
  	    			init();
  	    		}
  	    	});
    		}, function(){
    		});
    }
    
    $checkInterest = function(opType, opTips){
    	var tallyDate = dateType.get("tallyDate").val();
		var endInstDate = dateType.get("blnInstDate").val();
		if("check" == opType){
			if(!checkTallyDate()){
				return false;
			}
			if(!endInstDate||endInstDate.trim()==""){
				layer.alert(bt.lang("finacial.interest.endInstDateNotNull"), {icon:5});//请输入止息日期！
				return;
			}
			var dayCloseDate = $("#dayCloseDate").val();
			var closeDateEnd = bt.date.addDay(dayCloseDate, 1);
			
			if(bt.date.compare(endInstDate,closeDateEnd) > 0){//止息日期应小于等于日结后日期
				layer.alert(bt.lang("finacial.interest.endInstDatelessCloseDate",new Array(bt.date.format(closeDateEnd))), {icon:5});
				return false;
			}
		}
		layer.confirm(bt.lang("finacial.interest.OpInterest",new Array(opTips)), {
	  		  icon: 3,
	  		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
	  		}, function(index, layero){
	  			bt.loading.show();
	  			$.ajax({
		    		type: "POST",
		    		url: basePath+"/finacial/interest/fcinterest?doProcessInterestCheck",
		    		data: {
		    			"opTypestr":opType,
		    			"netCode" : $("#netId").val(),
						"accTypeCode" : dicts.get("accType").val().trim(),
						"tallyDate" : tallyDate,
						"endInstDate" : endInstDate,
                        "poolSign" : '1',
                        "account" : $("#account").val()
		    		},
		    		dataType : "json",
		    		success: function(rs){
		    			if(rs.success){
		    				bt.loading.hide();
		    				layer.msg(bt.lang("finacial.interest.OpInterest.state",new Array(opTips)), {icon: 1});
		    			}else{
		    				bt.loading.hide();
                            layer.alert(rs.msg, {icon:5});
		    			}
		    			init();
		    			//bt.navtab.refreshCurrent();
		    		}
		    	});
	  		}, function(){
	  		});
		
		
    }

    
    function getDayCloseDate(){
    	var netCode = $("#netId").val();
    	if(!netCode){
    		return;
    	}
    	$.ajax({
    		type: "POST",
    		url: basePath+"/finacial/interest/fcinterest?getDayCloseDate",
    		data: {"netCode":netCode},
    		dataType : "json",
    		success: function(rs){
    			$("#dayCloseDate").val(rs.obj);
    		}
    	});
    }
    
    
    /**
     * 检查记账日期
     * @returns
     */
    function checkTallyDate(){
    	var tallyDate = dateType.get("tallyDate").val();
    	if(!tallyDate||tallyDate.trim()==""){
    		layer.alert(bt.lang("fin.outbook.tally.tallydate.empty"), {icon:5});//请选择记账日期！
    		return false;
    	}
    	var dayCloseDate = $("#dayCloseDate").val();
    	if(dateCompareTo(tallyDate,dayCloseDate) >= 1){
    		layer.alert(bt.lang("finacial.interest.msg.tallyDateLessCloseDate"), {icon:5});//记账日期应不小于日结日期！
    		return false;
    	}
    	return true;
    }
    
 // 日期比较大小
    function dateCompareTo(date1,date2) {
    	var d,d1,d2,t1,t2;
    	d = date1.split("-");
    	d1 = new Date(parseInt(d[0],10),parseInt(d[1],10)-1,parseInt(d[2],10));
    	t1 = d1.getTime();
    	d = date2.split("-");
    	d2 = new Date(parseInt(d[0],10),parseInt(d[1],10)-1,parseInt(d[2],10));
    	t2 = d2.getTime();
    	if (t1 > t2)  return 0;
    	if (t1 == t2) return 1;
    	if (t1 < t2)  return 2;
    }


	/**
	 * 批量计息检查是否结息日结息
	 * @returns {*}
	 */
	function checkIstConfig() {
    	var result = null;
		$.ajax({
			type: "POST",
			url: basePath+"/finacial/interest/fcinterest/checkIstConfig",
			dataType : "json",
			data : {
				interestDate : dateType.get("blnInstDate").val()
			},
			async : false,
			success: function(rs){
				result = rs.success
			}
		});
		return result;
	}

});
