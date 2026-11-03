bt.use(['form','layedit','dicts','moneyType'], function(){
    var $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    validateForm = function(){
    	var period = $("#period").val().trim();
    	if(period!=="" && period==="0"){
    		layer.alert(bt.lang("cms.TbCreditPeriod.period.isnotnulll"),{icon:5});
			return false;
    	}
    	return true;
    }
    
    /**设置贷款种类*/
    setloanClass = function(){
    	var period = bt.moneyType.init().get("period").val();
    	var cycle = dict.get("periodCycle").val();
    	if(period!=""&&cycle.trim()!=""){
    		var tern = gettern(period,cycle);
        	if(tern<=1){//短期
        		dict.get("loanClass").val("01");
        	}else if(tern>1 && tern <=5){//中期
        		dict.get("loanClass").val("02");
        	}else if(tern>5){//长期
        		dict.get("loanClass").val("03");
        	}
    	}
    }
    /**根据周期类型获取对应年数*/
    gettern = function(period,cycle){
    	var tern = 0;
    	if(cycle==1){//年
    		tern = period;
    	}else if(cycle==2){//半年
    		tern = moneyFun.div(period,2);
    	}else if(cycle==3){//季
    		tern = moneyFun.div(period,4);
    	}else if(cycle==4){//月
    		tern = moneyFun.div(period,12);
    	}else if(cycle==5){//周
    		tern = moneyFun.div(period,moneyFun.div(365,7));
    	}else if(cycle==6){//日
    		tern = moneyFun.div(period,365);
    	}else if(cycle==7){//贴现
    		tern = 1;
    	}
    	return tern;
    }
    
    var dict = bt.dicts.init();
    bt.moneyType.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/period/tbcreditperiod/modify";
        if(!validateForm()) return false;
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});
