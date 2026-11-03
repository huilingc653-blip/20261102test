bt.use(['form','layedit','dateType','dicts','rateType','moneyType','btUpload'], function(){
    var $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    /**
     * 设置期限
     */
    setTerm = function(){
        var  startValue = dateType.get("repayDate").val();
        var  endValue = dateType.get("deferredRepayDate").val();
        var  lastdeferredReplayDate = dateType.get("lastdeferredReplayDate").val();
        var  startDate = new Date(startValue);
        var  endDate = new Date(endValue);
        if(lastdeferredReplayDate){
        	startDate = new Date(lastdeferredReplayDate);
        	if(lastdeferredReplayDate>=endValue){
        		layer.tips(bt.lang('cms.loan.deferredDateLessLastDateMsg'), '#deferredRepayDateShow');
            	dateType.get("deferredRepayDate").val(" ");
            	$("#deferredLimt").val("");
            	return;
        	}
        }else{
        	if(startValue>=endValue){
        		layer.tips(bt.lang("cms.loan.deferredDateLessRepayDateMsg"), '#deferredRepayDateShow');
            	dateType.get("deferredRepayDate").val(" ");
            	$("#deferredLimt").val("");
            	return;
            }
        }
        var  days = 0;
        var  month = 0;
        var  tern = 0;
        if(startValue !=="" && endValue !==""){
          var ternUnit = $("select[name='ternUnit']>option:selected").val();
          if(lastdeferredReplayDate){
        	  month =  dataSubtract(lastdeferredReplayDate,endValue,"M");
          }else{
        	  month =  dataSubtract(startValue,endValue,"M");
          }
          days = (endDate-startDate)/(1000*60*60*24);
          tern = month;
          if(ternUnit == 1){//月
            $("#deferredLimt").val(month);
          }else if(ternUnit == 2){//天
            $("#deferredLimt").val(days);
          }
        }
        
      }
    
    /**计算data1 与 data2 相差多少param单位 data为字符串*/
    dataSubtract = function(data1,data2,param){
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
    
    mGetDate = function(year, month){
        var d = new Date(year, month+1, 0);
        return d.getDate();
    }
    
    //初始化日期
    var dateType = bt.dateType.init();
    //初始化字典
    var dict = bt.dicts.init();
    //初始化利率
    bt.rateType.init();
    //初始化附件
    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand:true,
        gid: $("#attachmentgid").val()
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	if($("#deferredNum").val()>0&&$("#isMoreDeferred").val()==1){
    		layer.alert(bt.lang('cms.loan.isHaveDeferred'),{icon:5});
    		return false;
    	}
    	var status=$(this).attr("value");
        var action = "/cms/loan/cmsloandeferred/modify";
        data.field.status = status;//提交状态
        data.field.attachmentId = attachments.getGroupId();//附件ID
        var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
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
        return false;
    });
    
    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
});
