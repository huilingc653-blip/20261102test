bt.use(['form','layedit','dateType','selectType','selectTree','moneyType','ratioType','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.ratioType.init();
    bt.dateType.init();
    bt.selectType.init();
	bt.autoFilterHolidaysRule = function (data) {
		if(data.value == "1"){
			$("#autoFilterHolidaysRuleTr").removeClass("hidden");
		}else{
			$("#autoFilterHolidaysRuleTr").addClass("hidden");
		}
	}
    bt.dicts.init();
    //下拉树
    $.each($("div[type='selecttree']"),function(i,item){
        var filter = {"enable":true};
        if($(item).attr("dictField")){
            filter.idKey =  $(item).attr("dictField");
        }
        if($(item).attr("dictText")){
            filter.pIdKey =  $(item).attr("dictText");
        }
        bt.selectTree.init({
            url: $(item).attr("fieldHref"),
            elem:"[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            selected: $(item).attr("defaultVal"),
            setting:{
                data:{
                    simpleData: filter
                }
            }
        });
    });
    //监听提交
    form.on('submit(submit)', function(data){
    	//0-定时归集，6-取整归集，最小执行额度大于等于取整单位
    	var gatherType = data.field.gatherType;
    	var gatherMode = data.field.gatherMode;
    	var unit = parseFloat(data.field.unit);
    	var minAmt = parseFloat(data.field.minAmt);
    	if(gatherType == "0" && gatherMode=="6"){
    		if(unit > minAmt){
    			layer.alert(bt.lang('netbank.message.bisgatherplolicy.minamt'), {icon: 7});
    		}else{
    			submitForm(data);
    		}
    	}else{
    		submitForm(data);
    	}
    	return false;
    });
    function submitForm(data){
    	var action = "/allocation/bisgatherpolicy/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
    }
    
    init();
    
    function init(){
    	var gatherType=$("#gatherType").val();
    	gatherTypeChange(gatherType);
    }
    
    function gatherTypeChange(gatherType){
    	var gatherMode='bisGatherPolicy.gatherMode';
        $("#gatherMode").empty();
    	if(gatherType=="0"){
    		//显示归集方式
    		$("#gatherModeDiv").show();
    		$("#gatherModeSpanDiv").show();
    		$("#gatherModeSpanDiv").text(bt.lang('table.bisGatherPolicy.gatherMode'));
            gatherModeChange($("#gatherMode").val());
    	}else if(gatherType=="1"){
    		//隐藏策略模式、周期日、金额等
    		$("#gatherModeDiv").hide();
    		$("#gatherModeSpanDiv").hide();
    		$("#frqModeSpanDiv").hide();
    		$("#frqModeDiv").hide();
    		$("#amtTr").hide();
    		//默认8-全额归集
    		$("#gatherMode").append("<option value='8'>"+ bt.dict(gatherMode,'8')+"</option>");
    		$("#gatherMode").val(8);
    		$("#frqMode").val("");//调拨周期设置为空
    		//去除必填项
    		$("input[data-id='gatherAmt']").removeAttr("lay-verify");
    		$("input[data-id='differScale']").removeAttr("lay-verify");
    		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
    		$("input[data-id='minAmt']").removeAttr("lay-verify");
    		$("input[data-id='serialDay']").removeAttr("lay-verify");
    	}else if(gatherType=="2"){
    		//显示下拨方式
    		$("#gatherModeDiv").show();
    		$("#gatherModeSpanDiv").show();
    		$("#gatherModeSpanDiv").text(bt.lang('table.bisGatherPolicy.downMode'));
            gatherModeChange($("#gatherMode").val());
    	}
        form.renderIcon();
    }
    
    function gatherModeChange(gatherMode){
    	//周期日显示
    	$("#frqModeSpanDiv").show();
		$("#frqModeDiv").show();
		$("#amtTr").show();
		$("input[data-id='serialDay']").attr("lay-verify","required");
    	switch(gatherMode){
    		case "8"://全额归集
    			$("#gatherAmtSpanTd").hide();
    			$("#gatherAmtTd").hide();
    			$("#downFixedAmtSpanTd").show();
    			$("#downFixedAmtTd").show();
    			$("#downFixedAmtDiv").hide();
    			$("#minAmtDiv").show();
				$("#unitSpeDiv").hide();
    			$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.minAmt'));//最小执行额度
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").attr("lay-verify","required|number2");
    			break;
    		case "6"://取整归集
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.unit'));//取整单位
    			$("#gatherAmtDiv").hide();
    			// $("#gatherAmt").val(1);//默认元
    			$("#differScaleDiv").hide();
    			$("#downFixedAmtDiv").hide();
    			$("#minAmtDiv").show();
				$("#unitSpeDiv").hide();
    			$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.minAmt'));//最小执行额度
    			$("#unitDiv").show();
    			$("#addTd1").hide();
    			$("#addTd2").hide();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").attr("lay-verify","required|number2");
    			break;
    		case "1"://留底资金归集
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.purposeAmt'));//留底资金金额
    			$("#gatherAmtDiv").show();
    			$("#differScaleDiv").hide();
    			$("#downFixedAmtDiv").hide();
    			$("#minAmtDiv").show();
				$("#unitSpeDiv").hide();
    			$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.minAmt'));//最小执行额度
    			$("#unitDiv").hide();
    			$("#addTd1").hide();
    			$("#addTd2").hide();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").attr("lay-verify","required|number2");
    			$("input[data-id='differScale']").removeAttr("lay-verify");
    			$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").attr("lay-verify","required|number2");
    			break;
			case "5"://留底取整归集
				$("#gatherAmtSpanTd").show();
				$("#gatherAmtTd").show();
				$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.purposeAmt'));//留底资金金额
				$("#gatherAmtDiv").show();
				$("#differScaleDiv").hide();
				$("#downFixedAmtDiv").hide();
				$("#minAmtDiv").hide();
				$("#unitSpeDiv").show();
				$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.unit'));//取整单位
				$("#unitDiv").hide();
				$("#addTd1").hide();
				$("#addTd2").hide();
				//去除和增加必填项
				$("input[data-id='gatherAmt']").attr("lay-verify", "required|number2");
				$("input[data-id='differScale']").removeAttr("lay-verify");
				$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
				$("input[data-id='minAmt']").removeAttr("lay-verify");//存储取整单位
				//取整单位默认1-元
				//$("#minAmt").val(1);
				break;
    		case "9"://固定金额归集
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.fixUpAmt'));//固定上划金额
    			$("#gatherAmtDiv").show();
    			$("#differScaleDiv").hide();
    			$("#downFixedAmtSpanTd").hide();
    			$("#downFixedAmtTd").hide();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").attr("lay-verify","required|number2");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		case "b"://比例上划(交易额)
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.scale'));//比例
    			$("#gatherAmtDiv").hide();
    			$("#differScaleDiv").show();
    			$("#downFixedAmtSpanTd").hide();
    			$("#downFixedAmtTd").hide();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").attr("lay-verify","required|number2");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		case "4"://固定余额
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.fixBalance'));//账户固定余额
    			$("#gatherAmtDiv").show();
    			$("#differScaleDiv").hide();
    			$("#downFixedAmtSpanTd").hide();
    			$("#downFixedAmtTd").hide();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").attr("lay-verify","required|number2");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		case "3"://固定额度
    			$("#gatherAmtSpanTd").hide();
    			$("#gatherAmtTd").hide();
    			$("#downFixedAmtSpanTd").show();
    			$("#downFixedAmtTd").show();
    			$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.downFixedAmt'));//下拨固定额度
    			$("#downFixedAmtDiv").show();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").attr("lay-verify","required|number2");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		case "0"://根据前一日归集金额下拨
    			//隐藏、周期日、金额等
        		$("#frqModeSpanDiv").hide();
        		$("#frqModeDiv").hide();
        		$("#amtTr").hide();
        		//去除和增加必填项
        		$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
        		$("input[data-id='serialDay']").removeAttr("lay-verify");
        		$("#frqMode").val("4");//调拨周期设置为按日
        		//moneyType.get("serialDay").val(1);
    			break;
    		case "a"://余额不足时固定额度下拨
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.fixBalance'));//账户固定余额
    			$("#gatherAmtDiv").show();
    			$("#differScaleDiv").hide();
    			$("#downFixedAmtSpanTd").show();
    			$("#downFixedAmtTd").show();
    			$("#downFixedAmtSpanTd").text(bt.lang('table.bisGatherPolicy.downFixedAmt'));//下拨固定额度
    			$("#downFixedAmtDiv").show();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").hide();
    			$("#addTd2").hide();
    			//去除和增加必填项
    			$("#gatherAmt").attr("lay-verify","required|number2");
        		$("input[data-id='differScale']").removeAttr("lay-verify");
        		$("input[data-id='downFixedAmt']").attr("lay-verify","required|number2");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		case "2"://比例下拨(交易额)
    			$("#gatherAmtSpanTd").show();
    			$("#gatherAmtTd").show();
    			$("#gatherAmtSpanTd").text(bt.lang('table.bisGatherPolicy.scale'));//比例
    			$("#gatherAmtDiv").hide();
    			$("#differScaleDiv").show();
    			$("#downFixedAmtSpanTd").hide();
    			$("#downFixedAmtTd").hide();
    			$("#minAmtDiv").hide();
				$("#unitSpeDiv").hide();
    			$("#unitDiv").hide();
    			$("#addTd1").show();
    			$("#addTd2").show();
    			//去除和增加必填项
    			$("input[data-id='gatherAmt']").removeAttr("lay-verify");
        		$("input[data-id='differScale']").attr("lay-verify","required|number2");
        		$("input[data-id='downFixedAmt']").removeAttr("lay-verify");
        		$("input[data-id='minAmt']").removeAttr("lay-verify");
    			break;
    		default:
    			break;
        }
    	frqModeChange($("#frqMode").val());
    }
    
    function frqModeChange(frqMode){
    	switch(frqMode){
			case "4"://按日
				$("#serialDayDiv").show();
				$("#weekDiv").hide();
				$("#monthDiv").hide();
				break;
			case "8"://按周
				$("#serialDayDiv").hide();
				//moneyType.get("serialDay").val(2);
				$("#weekDiv").show();
				$("#monthDiv").hide();
				break;
			case "16"://按月
				$("#serialDayDiv").hide();
				//moneyType.get("serialDay").val(1);
				$("#weekDiv").hide();
				$("#monthDiv").show();
				break;
			case "32"://按季
				$("#serialDayDiv").hide();
				//moneyType.get("serialDay").val("");
				$("#weekDiv").hide();
				$("#monthDiv").hide();
				$("input[data-id='serialDay']").removeAttr("lay-verify");
				break;
			case "64"://按年
				$("#serialDayDiv").hide();
				//moneyType.get("serialDay").val("");
				$("#weekDiv").hide();
				$("#monthDiv").hide();
				$("input[data-id='serialDay']").removeAttr("lay-verify");
				break;
    	}
    }

	//取整单位
	form.on('select(unit)', function (data) {
		$("#gatherAmt").val(data.value);
		form.render();
	});

	//取整单位（5-留底取整归集）
	form.on('select(unitSpe)', function (data) {
		$("#minAmt").val(data.value);
		form.render();
	});
});
