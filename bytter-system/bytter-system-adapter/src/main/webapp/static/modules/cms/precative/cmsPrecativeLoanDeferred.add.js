var dateTypes;
var dictes;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','rateType'], function(){
     $ = bt.jquery,form = bt.form();
    //时间
    dateTypes = bt.dateType.init();
    //字典
    dictes =  bt.dicts.init();
    //利率
    bt.rateType.init();
    
    bt.moneyType.init();
    
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }

    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();//显示加载中
    	var status = $(this).attr("value");
        var action = "/cms/precative/precativeloandeferred/save";
        data.field.status = status;
        data.field.deferredStartDate = (($('#scDeferredDate').val() == ""||$('#scDeferredDate').val() == undefined) ? $("#matureDate").val() : $('#scDeferredDate').val());
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            bt.loading.hide(); //关闭加载中
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

});

//展期到期日期  计算展期期限
function computeplTerm(ele,data){
	var end = bt.jquery.trim(dateTypes.get("deferredRepayDate").val());
	var matureDate = $("#matureDate").val();
	var scDate = $('#scDeferredDate').val();
	var start = ( (scDate == ""||scDate == undefined) ? matureDate : scDate);
	var plTerm;
	
	if(start !=="" && end !==""){
		if(start >= end){//结束日期应大于开始日期
			bt.dateType.init().get($(data).attr("data-biz-name")).val("");
			var tipsStr ;
			if((scDate == ""||scDate == undefined)){
				tipsStr = "cms.precative.deferred.not.unique";
			}else{
				tipsStr = "cms.precative.deferred.pass.lastdeferreddate";
			}
			layer.tips(bt.lang(tipsStr), '#'+$(data).attr("data-biz-name"));
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

/**计算data1 与 data2 相差多少param单位 data为字符串*/
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

/***function changeName(data){
	
	if(data.value == "1"){
		//var reg = new RegExp("","g");
		//var tds = $("#limt").text().replace(reg,index);
		$("#startDate").text("展期开始日期");
		$("#limt").text("展期期限");
		$("#replayDate").text("展期到期日期");
		$("#delayRate").text("展期利率");
	}else if(data.value == "2"){
		$("#startDate").text("开始日期");
		$("#limt").text("期限");
		$("#replayDate").text("到期日期");
		$("#delayRate").text("利率");
	}
	
	form.renderIcon();
}***/
