bt.use(['form','layedit','dateType','dicts','moneyType','date'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }
    
    changeRepayType = function(data,ele){
    	if(data.value==1){//归还本金
    		moneyType.get("repayAccrual").attr("lay-verify","");
    		moneyType.get("repayAccrual").val("");
    		$("#lixqdButton").attr("disabled","true");
    		$("#lixqdButton").addClass("layui-btn-disabled");
    	}else{
    		moneyType.get("repayAccrual").val(0);
    		moneyType.get("repayAccrual").attr("lay-verify","required");
    		getFixedMoneyInterest();
    		$("#lixqdButton").removeAttr("disabled");
    		$("#lixqdButton").removeClass("layui-btn-disabled");
    	}
    	if(data.value==4){
    		$("#repayMoneyShow").html(moneyFun.format(0));
    	}else{
    		$("#repayMoneyShow").html(moneyFun.format($("#repayMoney").val()));
    	}
    }
    
    
    /**
     * 根据账号金额获取利息
     * @param params
     */
    getFixedMoneyInterest = function(){
    	if(bt.empty($("#loanAcc").val())||bt.empty($("#repayMoney").val())||dicts.get("repayType").val()==1||dicts.get("repayType").val()==" "){
    		moneyType.get("repayAccrual").val("");
			return false;
		}
    	var params = {
				"account":$("#loanAcc").val(),
				"money":dicts.get("repayType").val()==2?$("#repayMoney").val():$("#sendMoney").val(),
				"businessID":$("#orderNo").val(),
				"endInstDate":dateType.get("repayDate").val()
		};
    	$.ajax({
    		type: "POST",
    		url: basePath+"/cms/loan/cmsloanrepay?getFixedMoneyInterest",
    		data: params,
    		dataType : "json",
    		success: function(rs){
    			moneyType.get("repayAccrual").val(rs.obj.interest);
    			$("#SyncAccrual").val(0);
    			$("#curAccrualShow").html("");
    		}
    	});
    }
    
    $initAccrualInfo = function(){
    	if(bt.empty($("#loanAcc").val())||$("#initRepayType").val()==1){
    		moneyType.get("repayAccrual").val("");
			return false;
		}
    	var params = {
				"account":$("#loanAcc").val(),
				"money":$("#initRepayType").val()==2?$("#repayMoney").val():$("#sendMoney").val(),
				"businessID":"",
				"endInstDate":dateType.get("repayDate").val()
		};
    	$.ajax({
    		type: "POST",
    		url: basePath+"/cms/loan/cmsloanrepay?getFixedMoneyInterest",
    		data: params,
    		dataType : "json",
    		success: function(rs){
    			if(rs.obj.interest!=$("#repayAccrual2").val()){//利息已变更，当前利息为：
    				$("#curAccrualShow").html(bt.lang("cms.loan.repayAccrual.isChange")+"<strong>"+moneyFun.format(rs.obj.interest)+"</strong>");
    				$("#SyncAccrual").val(1);
    			}
    		}
    	});
    }
    
    var dateType = bt.dateType.init();
    var dicts = bt.dicts.init();
    //格式化数字
    var moneyType = bt.moneyType.init();
    
    if($("#initRepayType").val()==1){
    	$("#lixqdButton").attr("disabled","true");
    	$("#lixqdButton").addClass("layui-btn-disabled");
    }
    $initAccrualInfo();
    //监听提交
    form.on('submit(submit)', function(data){
    		var status=$(this).attr("value");
    		data.field.status = status;//提交状态
    		if(status==0){//打回
    			parent.layer.confirm(bt.lang('common.message.confirm.repulse'),{icon:3},function(){
    				submitData(data);
    			});
    		}else{//确认通过
    			if(bt.empty($("#loanAcc").val())){//没有贷款账号不做利息校验
    				submitData(data);
    			}else if(dicts.get("repayType").val()!=1){//已记账产生积数且还款类型包含利息则计息
    				if((!bt.empty($("#SyncAccrual").val()))&&$("#SyncAccrual").val()==1){
    					//还款利息与当前系统利息不符，请更改还款方式以重新计算利息
    					layer.tips(bt.lang("cms.loan.repay.NotSyncAccrual"), '#lixqdButton',{tips: 3});
    					return false;
    				}
    				var closeDay = new Date($("#closeDay").val());
        			var endIntDate = bt.date.addDay(new Date(dateType.get("repayDate").val()),-1);
        			if(closeDay < endIntDate){//日结日期小于还款前一天
        				parent.layer.confirm(bt.lang("cms.loan.notDayColseEndDate",new Array(bt.date.format(endIntDate))),{icon:3},function(){
        					submitData(data);
        				});
        			}else{//日结到还款前一天
        				submitData(data);
        			}
    			}else{
    				submitData(data);
    			}
    		}
        return false;
    });
    
    submitData = function(data){
    	var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
    	var action = "/cms/loan/cmsloanrepay/interetAffirmPass";
    	$.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	layer.close(mask);
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParent();
                	bt.navtab.closeCurrent();
                }
            }else{
            	layer.close(mask);
            }
        },'JSON');
    }
    
    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
    //显示利息清单
    showlxqd = function(){
    	if($("#loanAcc").val()==null || $("#loanAcc").val()==undefined || $("#loanAcc").val() == ""){
    		layer.alert(bt.lang("cms.loan.notFcAccBook.NotInterest"),{icon:5});//未产生账务，不计息
    		return false;
    	}
    	var params= "account="+ $("#loanAcc").val()+"&money="+(dicts.get("repayType").val()==2?$("#repayMoney").val():$("#sendMoney").val())+"&endInstDate="+$("#repayDate").val()+"&businessID="+$("#orderNo").val();
    	console.log(params)
	    parent.layer.open({
	    	title:bt.lang("table.interet.lxqd"),
	        type: 2,
	        area: ['75%', '80%'],
	        maxmin: true,
	        content: basePath + "/cms/loan/cmsloanrepay/repayInteretDtl?" + params
	    });
    }
});
