var dateTayps;
var dictes;
var rateTypes;
var moneyTypes;
var fun;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','rateType'], function(){
	var $ = bt.jquery,form = bt.form();
	//利率
	rateTypes=bt.rateType.init();
	//时间
	dateTayps = bt.dateType.init();
	//字典
	dictes = bt.dicts.init();
	//金额
	moneyTypes = bt.moneyType.init();
	fun =  bt.moneyType.getFun();

	bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }
    //监听提交 21 -1
    form.on('submit(submit)', function(data){
    	var status = $(this).attr("value");
        var action = "/cms/provide/provideloandeferred/modify";
        data.field.status = status;
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){

                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParent();
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });

    //监听提交 21 -1
    form.on('submit(back)', function(data){
    	var status = $(this).attr("value");
    	var id = $("input[name='id']").val();
        var refuseWhy = $("#refuseWhy").val();
        var billCode = $("#billCode").val();
        var action = "/cms/provide/provideloandeferred/modify";
        data.field.status = status;
        data.field.id = id;
        data.field.refuseWhy = refuseWhy;
        data.field.billCode = billCode;
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


//展期到期日期  计算展期期限
function computeplTerm(ele,data){
	var start = bt.jquery.trim(bt.jquery("#repayData").val());
	var end = bt.jquery.trim(dateTayps.get("deferredRepayDate").val());
	var plTerm;
	if(bt.empty(start) || bt.empty(end)){
		plTerm = "";
	}else{
		if(start >= end){
			bt.dateType.init().get($(data).attr("data-biz-name")).val("");
			layer.tips(bt.lang("cms.provide.deferred.not.unique"), '#'+$(data).attr("data-biz-name"));
			return false;
		}

		var ternUnit = bt.jquery.trim(dictes.get("ternUnit").val());
		if(!bt.empty(ternUnit)){
			plTerm = dataSubtract(start,end,ternUnit);
		}else{
			//为空或者null默认月处理
			dictes.get("ternUnit").val("M");
			plTerm = dataSubtract(start,end,"M");
		}
	}
	bt.jquery("#deferredLimt").val(plTerm);//设置借款期限
}

function dataSubtract(data1,data2,param){
    var dates;
	if(param == "D"){
		dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime()))/(1000*60*60*24);
	}else{
		var start = new Date(data1);
	    var end = new Date(data2);

	    var addMonth = end.getMonth() - start.getMonth();//月份差

	    var startLastDay = mGetDate(start.getYear(),start.getMonth());//开始时间当前月总共多少天
	    var endLastDay = mGetDate(end.getYear(),end.getMonth());//结束时间当前月总共多少天

	    if(startLastDay == start.getDate()){//开始日期月末最后一天
	    	if(endLastDay == end.getDate()){//结束日期月末最后一天
	    		addMonth ++;//月份+1
	    	}else{
	    		if(end.getDate() > start.getDate()){//结束日期当月天数 > 开始日期单月天数
		    		addMonth ++;//月份+1
		    	}
	    	}
	    }else{
	    	if(endLastDay == end.getDate()){//结束日期月末最后一天
	    		addMonth ++;//月份+1
	    	}else{
	    		if(end.getDate() > start.getDate()){//结束日期当月天数 > 开始日期单月天数
		    		addMonth ++;//月份+1
		    	}
	    	}
	    }

		dates = (end.getYear() - start.getYear())*12 + addMonth;
	}
    return  dates;
}

function mGetDate(year, month){
    var d = new Date(year, month + 1, 0);
    return d.getDate();
}

/**根据plRate + 上浮BP点数/10000 */
function setDeferredRate(){
	var plRate = $("#plRate").val();//借款利率
	var deferredPoints = moneyTypes.get("deferredPoints").val();
	if(!deferredPoints){
		deferredPoints=0.00;
		moneyTypes.get("deferredPoints").val(deferredPoints)
	}
	var deferredRate=fun.format(parseFloat(plRate)+parseFloat(deferredPoints/10000), {decimal: 6});
	rateTypes.get("deferredRate").val(deferredRate);
}
