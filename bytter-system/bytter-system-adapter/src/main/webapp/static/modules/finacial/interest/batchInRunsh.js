bt.use(['form','corp','element','btdate','select','dicts','date','dateType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    var element = layui.element();
    var reg=new RegExp(",","g"); //创建正则RegExp对象
    
    corpCall = function(ele){//网点回调
    	$("#netId").val(ele.value);
    	location.href = basePath+"/finacial/interest/fcinterest/listBatchRush?netId="+ele.value+"&opType="+$("#opType").val()+"&accType="+dicts.get('accType').val();
    }
    accTypeCall = function(ele){//开户种类回调
    	init();
    }
    
    accTypeCall = function(ele){//开户种类回调
    	var accTypeCode = ele.value;
		if(accTypeCode == "02" || accTypeCode == "04" || accTypeCode == "06"){
			$("#individualAccType").attr("style","display:''");
			//加载待冲销预提的账户
			bt.select.init({
	    		elem:"[field='account1']",
	    		name:"account1",
	    		defaultVal:'',
	    		valueField:"RUSHMONEY",
	    		textField:"ACCOUNT",
	    		search:true,
	    		disabled:false,
	    		url:this.basePath+"/finacial/interest/fcinterest/getPreRushAcc?netId="+$("#netId").val()+"&accType="+accTypeCode,
	    		change:function(data,text){
	    			$("#account").val(text);
	    			$("#rushMoney").val(data.value);
	    			moneyType.get("strikeInterest").val(data.value);
	    			moneyType.get("strikeInterest").attr("data-max",data.value);
	    			moneyType.get("strikeInterest").reset();
	    		}
	    	});
		} else {
			$("#individualAccType").attr("style","display:none");
			moneyType.get("strikeInterest").val("");
			$("#account").val("");
			$("#rushMoney").val("");
		}
		init();
    }
    
    strikeCall = function(ele){//冲预提金额回调
    	var rushMoney = $("#rushMoney").val();
    	if(rushMoney!=""&&rushMoney!=null&&rushMoney!=undefined){
    		if(ele){
    			if((rushMoney<0 && ele>0) || (rushMoney>0 && ele<0)){//	冲预提金额不能与预提金额正负相反！
    				layer.alert(bt.lang("finacial.interest.msg.runshMoneyOpposite"), {icon:5});
    				moneyType.get("strikeInterest").val("");
    				return false;
    			}
    			if(Math.abs(ele) - Math.abs(rushMoney) > 0){//冲预提金额不能大于预提金额！
    				layer.alert(bt.lang("finacial.interest.msg.runshMoneyErr"), {icon:5});
    				moneyType.get("strikeInterest").val("");
    				return false;
    			}
    		}
    	}
		
    }
    
    bt.corp.init();
    var dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var params = "";
    
    
  //初始化参数
    function initParam(){
    	var accType = "";
    	if(dicts.get('accType').val()){
    		accType = dicts.get('accType').val().trim();
    	}
    	params = "netId="+$("#netId").val()+"&accType="+accType+"&opType="+$("#opType").val() +"&curr=1&endInstDate="+dateType.get("blnInstDate").val()+"&tallyDate="+dateType.get("tallyDate").val()+"&account="+$("#account").val();
    }
    
    
 // 初始化
    init();
 
 // 监听控制content高度
    function init() {
        // 起始判断收缩还是展开
    	initParam();
    	initTabJX();
    	initTabPZ();
    	initTabYT();
        // 选项卡高度
        var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card2'] ul.layui-tab-title").height();
        // 需要减去的高度
        var height = $(window).height() - $('.layui-header').height() - cardTitleHeight;
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card2'] div.layui-tab-content").height(height - 200);
    }
 
 
    //利息清单
    function initTabJX(){
    	bt.jquery("#lxqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/interestRushBill?"+params);
		bt.jquery("#lxqdiframe").show(1000);
    }
    //计息凭证
    function initTabPZ(){
    	bt.jquery("#jxpziframe").attr("src", basePath + "/finacial/interest/fcinterest/toLoadInterestRushVoucher?"+params);
		bt.jquery("#jxpziframe").show(1000);
    }
    
    //预提清单
    function initTabYT(){
    	bt.jquery("#ytqdiframe").attr("src", basePath + "/finacial/interest/fcinterest/predrawBill?"+params);
		bt.jquery("#ytqdiframe").show(1000);
    }
  
    function getDayCloseDate(){
    	var netCode = $("#netId").val();
    	console.log(netCode)
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
    
    form.on('submit(temp)', function(data){
    	$rushInterest("temp",bt.lang("common.button.temp"),data);//暂存
    	return false;
    });
    form.on('submit(remove)', function(data){
    	var info = bt.lang("finacial.interest.confirmMsg.deleteAllRunshInfo");//你确定删除所有冲预提信息吗？
		if($("#blnInstDate").val()){
			info = bt.lang("finacial.interest.confirmMsg.deleteEndDateRunshInfo");//您确定要删除止息日期前冲预提信息吗？
		}
    	layer.confirm(info, {
  		  icon: 3,
  		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
  		}, function(index, layero){
  			bt.loading.show();
  			$.ajax({
  	    		type: "POST",
  	    		url: basePath+"/finacial/interest/fcinterest/doDeleteByRush",
  	    		data: {
  	    			"netId" : $("#netId").val(),
  					"accType" : dicts.get("accType").val().trim(),
  	    			"blnInstDate":$("#blnInstDate").val(),
  	    			"account":$("#account").val()
  	    		},
  	    		dataType : "json",
  	    		success: function(rs){
  	    			bt.loading.hide();
  	    			layer.msg(bt.lang("finacial.interest.runshDelete"), {icon: 1});//删除成功
  	    			init();
  	    		}
  	    	});
  			layer.close(index);
  		}, function(){
  		});
    	return false;
    });
    form.on('submit(submit)', function(data){
    	$rushInterest("submit",bt.lang("common.button.submit"),data);//提交
    	return false;
    });
    form.on('submit(check)',function(data){
    	$rushInterest("check",bt.lang("common.button.check"));//复核
    	return false;
    });
    form.on('submit(back)',function(data){
    	$rushInterest("back",bt.lang("common.button.back"));//打回
    	return false;
    });
    form.on('submit(print)', function(data){
    	return false;
    });
 	
    $rushInterest = function(opType,opTips,data){
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
		var accType = dicts.get("accType").val().trim();
		var strikeInterest = moneyType.get("strikeInterest").val();
		var interest = $("#rushMoney").val().replace(reg,"");
		var account = $("#account").val();
		layer.confirm(bt.lang("finacial.interest.OpRushInterest",new Array(opTips)), {//'您确定要进行'+opTips+'冲预提信息吗？'
	  		  icon: 3,
	  		  btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
	  		}, function(index, layero){
	  			bt.loading.show();
	  			$.ajax({
		    		type: "POST",
		    		url: basePath+"/finacial/interest/fcinterest?doSubmitRush",
		    		data: {
		    			"opType":opType,
		    			"netId" : $("#netId").val(),
						"accType" : accType,
						"tallyDate" : tallyDate,
						"endInstDate" : endInstDate,
						"account":account,
						"rushMoney":strikeInterest
		    		},
		    		dataType : "json",
		    		success: function(rs){
		    			if(rs.success){
		    				bt.loading.hide();
		    				layer.msg(bt.lang("finacial.interest.OpRushInterest.state",new Array(opTips)), {icon: 1});
		    				init();
		    			}else{
		    				bt.loading.hide();
		    				layer.alert(rs.msg, {icon:5});
		    			}
//		    			bt.navtab.refreshCurrent();
		    		}
		    	});
	  			layer.close(index);
	  		}, function(){
	  		});
		
		
    }
    
    
    /**
     * 检查记账日期
     * @returns
     */
    function checkTallyDate(){
    	var tallyDate = dateType.get("tallyDate").val();
    	if(!tallyDate||tallyDate.trim()==""){
    		layer.alert(bt.lang("fin.outbook.tally.tallydate.empty"), {icon:5});//"请选择记账日期！"
    		return false;
    	}
    	var dayCloseDate = $("#dayCloseDate").val();
		var batchInstWay = $("#batchInstWay").val();
		if("1"===batchInstWay) {
			if (dateCompareTo(tallyDate, dayCloseDate) >= 1) {
				layer.alert(bt.lang("finacial.interest.msg.tallyDateLessCloseDate"), {icon: 5});//"记账日期应不小于日结日期！"
				return false;
			}
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
 
});