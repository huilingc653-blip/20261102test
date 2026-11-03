bt.use(['form','select','dateType','date','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    var selectYear = bt.select.init({
        elem:'.selectYear',
        name:'year',
        valueField: "year",
        textField:"year",
        search:false,
        disabled:false,
        url:basePath+"/budget/fbsbudgetperiod/getChooseYear",
        defaultVal:$("#defaultYear").val(),
        placeholder:{value:"",text:""},
        change:function(data,text){
        	$.ajax({
        	    url:basePath +"/budget/fbsbudgetperiod/getBtTable?year="+data.value,
        		type:"Post",
        	    dataType:"json",
        	    success:function(data){
        	    	createTr(data);
        	    	form.render();
        	    	init();
        	    	bt.dateType.init(); 
        	    	bt.moneyType.init();
        	  }});
        }
    });
    $.ajax({
	    url:basePath +"/budget/fbsbudgetperiod/getBtTable?year="+$("#defaultYear").val(),
		type:"Post",
	    dataType:"json",
	    success:function(data){
	    	createTr(data);
	    	form.render();
	    	init();
	    	bt.dateType.init(); 
	    	bt.moneyType.init();
	  }});
    
  //监听提交
    form.on('submit(submit)', function(data){
        var action = "/budget/fbsbudgetperiod/save";
        $.post(basePath + action,$('#fbsPeriodForm').serialize(),function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
    
    
    /**
     * 创建表格
     */
    function createTr(data){
    	var $table = $("#dataTable");
    	$($table.find("tbody")).empty();//先清空表格然后重新加载
    	if($table.find("tbody").length ==0){
    		$table.append('<tbody></tbody>');
		}
    	if(data.length > 1){
    		createTrByDate(data);
    	}else{
    		var defaultWeek = 4;
    		var month = 12;
    		var defaultCount = defaultWeek*month;
    		for (var i = 0; i < month; i++) {
                var dataMin = new Date();
                var dataMax = new Date();
                var year = $(".selectYear").find("select option:selected").val();
                dataMin.setFullYear(year,i,1);
                dataMax.setFullYear(year,i+1,1);
                dataMax.setDate(dataMax.getDate()-1);
    			var $tr = $('<tr class = "'+i+'"></tr>');
    			$tr.append('<td rowspan="'+defaultWeek+'" class="month" >'+(i+1)+bt.lang("table.fbsBudgetPeriod.month")+'<input type="hidden" name="month" value="1" /></td>');
        		$tr.append('<td rowspan="'+defaultWeek+'" class="week">' +
        				'<select name="week" onchange="toChangeWeek('+i+',this)" lay-ignore >'+
        				' <option>3</option>'+
        				' <option selected="selected">4</option>'+
        				' <option>5</option>'+
        				' <option>6</option>'+
        				'</select>' +
        				'</td>');
        		$tr.append('<td><input type="text" lay-verify= "required" name="beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="beginDate_'+i+'_'+0+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" ></td>');
    			$tr.append('<td><input type="text" lay-verify= "required" name="endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="endDate_'+i+'_'+0+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" data-callback="getBeginDate"></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="weekdays" id = "weekdays_'+i+'_'+0+'" data-biz-name="weekdays_'+i+'_'+0+'" data-min="0" data-max="31"/></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="workdays" id = "workdays_'+i+'_'+0+'" data-biz-name="workdays_'+i+'_'+0+'" data-min="0" data-max="31"/></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="holidays" id = "holidays_'+i+'_'+0+'" data-biz-name="holidays_'+i+'_'+0+'" data-min="0" data-max="31"/></td>');
    			$tr.append('<td><input type="text" class="layui-input" name="rmk" id = "rmk_'+i+'_0"/></td>');
    			$table.append($tr);
        		for (var j = 1; j < defaultWeek; j++) {
        			var $tr1 =  $('<tr class = "'+i+'"></tr>');
        			$tr1.append('<td><input type="text" lay-verify= "required" name="beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="beginDate_'+i+'_'+j+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" ></td>');
        			$tr1.append('<td><input type="text" lay-verify= "required" name="endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="endDate_'+i+'_'+j+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" data-callback="getBeginDate"></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="weekdays" id = "weekdays_'+i+'_'+j+'" data-biz-name="weekdays_'+i+'_'+j+'" data-min="0" data-max="31"/></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="workdays" id = "workdays_'+i+'_'+j+'" data-biz-name="workdays_'+i+'_'+j+'" data-min="0" data-max="31"/></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="holidays" id = "holidays_'+i+'_'+j+'" data-biz-name="holidays_'+i+'_'+j+'" data-min="0" data-max="31"/></td>');
        			$tr1.append('<td><input type="text" class="layui-input" name="rmk" id = "rmk_'+i+'"/></td>');
        			$table.append($tr1);
				}
			}
    	}
    }
   
});
/**
 * 更改周数改变页面效果
 * @param i
 * @param obj
 */
function toChangeWeek(i,obj){
	var $trs = $("#dataTable").find("."+i);
	var length = $trs.length;
	var choseLength = obj.value;
    var dataMin = new Date();
    var dataMax = new Date();
    var year = $(".selectYear").find("select option:selected").val();
    dataMin.setFullYear(year,i,1);
    dataMax.setFullYear(year,i+1,1);
    dataMax.setDate(dataMax.getDate()-1);
	if(length > choseLength){//删除行
		$($trs[0]).find("td.month").attr("rowspan",choseLength);
		$($trs[0]).find("td.week").attr("rowspan",choseLength);
		for (var j = 0; j < length - choseLength; j++) {
            $($trs[length - 1-j]).remove();
		}
		initTr(i);
	}else{//增加行
		$($trs[0]).find("td.month").attr("rowspan",choseLength);
		$($trs[0]).find("td.week").attr("rowspan",choseLength);
		for (var j = 0; j < choseLength - length; j++) {
			var $tr1 =  $('<tr class = "'+i+'"></tr>');
			$tr1.append('<td><input type="text" lay-verify= "required" name="beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="beginDate_'+i+'_'+(length+j)+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" ></td>');
			$tr1.append('<td><input type="text" lay-verify= "required" name="endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="endDate_'+i+'_'+(length+j)+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" data-callback="getBeginDate"></td>');
			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="weekdays" id = "weekdays_'+i+'_'+(length+j)+'" data-biz-name="weekdays_'+i+'_'+(length+j)+'" data-min="0" data-max="20"/></td>');
			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="workdays" id = "workdays_'+i+'_'+(length+j)+'" data-biz-name="workdays_'+i+'_'+(length+j)+'" data-min="0" data-max="20"/></td>');
			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="holidays" id = "holidays_'+i+'_'+(length+j)+'" data-biz-name="holidays_'+i+'_'+(length+j)+'" data-min="0" data-max="20"/></td>');
			$tr1.append('<td><input type="text" class="layui-input" class="layui-input" name="rmk" id = "rmk_'+i+'"/></td>');
			$($trs[length - 1]).after($tr1);
		}
		initTr(i);
		bt.dateType.init();
	}
}
/**
 * 初始化日期
 */
function init(){
	var length = 12;//12个月
	var $table = $("#dataTable");
	var year = $(".selectYear").find("select option:selected").val();
	var beginDate=new Date();
	var endDate=new Date();
	for (var i = 0; i < length; i++) {
		beginDate.setFullYear(year,i,1);
		endDate.setFullYear(year,i+1,1);
		endDate.setDate(endDate.getDate()-1);
		var $trs = $table.find("."+i);
		$($trs[0]).find('td input[name="beginDate"]').val(bt.date.format(beginDate));
		for (var j = 0; j < $trs.length; j++) {
			$($trs[j]).find('td input[name="beginDate"]').attr("readonly","readonly");
			$($trs[j]).find('td input[name="endDate"]').attr("data-min",bt.date.format(beginDate));
			$($trs[j]).find('td input[name="endDate"]').attr("data-max",bt.date.format(endDate));
			$($trs[j]).find('td input[name="endDate"]').attr("data-callback","getBeginDate");
			if(j != 0 ){
				$($trs[j]).find('td input[name="endDate"]').attr("readonly","readonly");
			}
		}
		$($trs[$trs.length-1]).find('td input[name="endDate"]').val(bt.date.format(endDate));
	}
}
/**
 * 初始化一个月
 * @param i
 */
function initTr(i){
	var $table = $("#dataTable");
	var year = $(".selectYear").find("select option:selected").val();
	var beginDate=new Date();
	var endDate=new Date();
	beginDate.setFullYear(year,i,1);
	endDate.setFullYear(year,i+1,1);
	endDate.setDate(endDate.getDate()-1);
	var $trs = $table.find("."+i);
	$($trs[0]).find('td input[name="beginDate"]').val(bt.date.format(beginDate));
	for (var j = 0; j < $trs.length; j++) {
		$($trs[j]).find('td input[name="beginDate"]').attr("readonly","readonly");
		$($trs[j]).find('td input[name="endDate"]').attr("data-min",bt.date.format(beginDate));
		$($trs[j]).find('td input[name="endDate"]').attr("data-max",bt.date.format(endDate));
		$($trs[j]).find('td input[name="endDate"]').attr("data-callback","getBeginDate");
		if(j != 0 ){
			$($trs[j]).find('td input[name="endDate"]').attr("readonly","readonly");
			$($trs[j]).find('td input[name="endDate"]').val("");
		}else{
            $($trs[j]).find('td input[name="endDate"]').attr("value",bt.date.format(beginDate));
		}
	}
	$($trs[$trs.length-1]).find('td input[name="endDate"]').val(bt.date.format(endDate));
}
/**
 * 根据结束时间得到开始时间
 * @param value
 * @param obj
 */
function getBeginDate(value,obj){
	var $tr = $(obj).parent().parent();//本行
    var t = bt.date.compare(bt.date.addDay(value, 2),$tr.next("tr").find('td input[name="beginDate"]').data("max"))<0;
    if(t) {
        $tr.next("tr").find('td input[name="beginDate"]').val(bt.date.format(bt.date.addDay(value, 1)));
        if(bt.date.compare( $tr.next("tr").find('td input[name="endDate"]').data("max"),$tr.next("tr").find('td input[name="endDate"]').val()) > 0) {
            $tr.next("tr").find('td input[name="endDate"]').val(bt.date.format(bt.date.addDay(value, 2)));
            $tr.next("tr").find('td input[name="endDate"]').removeAttr("readonly");
        }
        if(bt.date.compare( $tr.next("tr").find('td input[name="endDate"]').data("min"),bt.date.format(bt.date.addDay(value, 2))) < 0) {
            $tr.next("tr").find('td input[name="endDate"]').data("min", bt.date.format(bt.date.addDay(value, 2)));
        }
    }else{
        $tr.next("tr").find('td input[name="beginDate"]').val("");
        $tr.next("tr").find('td input[name="endDate"]').val("");
    }
	//计算本周天数
	var beginDate = $tr.find('td input[name="beginDate"]').val();
	var endDate = $tr.find('td input[name="endDate"]').val();
	var weekdays = bt.date.compare(endDate,beginDate)/(24*3600*1000)+1;
	setNum($tr.find('td input[data-name="weekdays"]').data("biz-name"),weekdays);
	$tr.find('td input[data-name="weekdays"]').attr("readonly","readonly");
    $.ajax({
        url:basePath +"/budget/fbsbudgetperiod/getWorkDays?beginDate="+beginDate+"&endDate="+endDate,
        type:"Post",
        dataType:"json",
        success:function(data){
            setNum($tr.find('td input[data-name="workdays"]').data("biz-name"),data);
            setNum($tr.find('td input[data-name="holidays"]').data("biz-name"),weekdays-data);
        }});
	//如果结束时间有值了就直接计算时间
	if($tr.next("tr").find('td input[name="endDate"]').val().trim() != ""){
		var beginDate1 = $tr.next("tr").find('td input[name="beginDate"]').val();
		var endDate1 = $tr.next("tr").find('td input[name="endDate"]').val();
		var weekdays1 = bt.date.compare(endDate1,beginDate1)/(24*3600*1000)+1;
		setNum($tr.next("tr").find('td input[data-name="weekdays"]').data("biz-name"),weekdays1);
		$tr.next("tr").find('td input[data-name="weekdays"]').attr("readonly","readonly");
        $.ajax({
            url:basePath +"/budget/fbsbudgetperiod/getWorkDays?beginDate="+beginDate1+"&endDate="+endDate1,
            type:"Post",
            dataType:"json",
            success:function(data1){
                setNum($tr.next("tr").find('td input[data-name="workdays"]').data("biz-name"),data1);
                setNum($tr.next("tr").find('td input[data-name="holidays"]').data("biz-name"),weekdays1-data1);
            }});
	}else{
        if(t) {
            $tr.next("tr").find('td input[name="endDate"]').removeAttr("readonly");
            $tr.next("tr").find('td input[name="endDate"]').val(bt.date.format(bt.date.addDay(value, 2)));
        }else{
            $tr.next("tr").find('td input[name="endDate"]').val("");
        }
	}
    var endDateBiz = $tr.next("tr").find('td input[name="endDate"]').data("biz-name");
    var endDateType = bt.dateType.init();
    endDateType.get(endDateBiz).reset();//重新加载时间控件
}
/**
 * 设置数字控件的值
 * @param name
 * @param val
 * @returns
 */
function setNum(name,val){
	//console.log(name);
	var test = bt.moneyType.init();
	test.get(name).val(val);//重新加载数字控件
}
/**
 * 自动生成自然周
 * @returns
 */
function autoAenerate(){
      var year = $(".selectYear").find("select option:selected").val();
      $.ajax({
  	    url:basePath +"/budget/fbsbudgetperiod/isAutoAenerate?year="+year,
  		type:"Post",
  	    dataType:"json",
  	    success:function(data){
  	    	if(data.success == true){
  	    		 $.ajax({
  	    	  	    url:basePath +"/budget/fbsbudgetperiod/autoAenerate?year="+year,
  	    	  		type:"Post",
  	    	  	    dataType:"json",
  	    	  	    success:function(data){
  	    	  	    	createTrByDate(data);
  	    	  	    	form.render();
  	    	  	    	bt.dateType.init(); 
  	    	  	    	bt.moneyType.init();
  	    	  	  }});
  	    	}
  	  }});
}
/**
 * 自动生成标准周
 * @returns
 */
function autoStandard() {
    var year = $(".selectYear").find("select option:selected").val();
    $.ajax({
        url:basePath +"/budget/fbsbudgetperiod/isAutoAenerate?year="+year,
        type:"Post",
        dataType:"json",
        success:function(data){
            if(data.success == true){
                $.ajax({
                    url:basePath +"/budget/fbsbudgetperiod/autoStandard?year="+year,
                    type:"Post",
                    dataType:"json",
                    success:function(data){
                        createTrByDate(data);
                        form.render();
                        bt.dateType.init();
                        bt.moneyType.init();
                    }});
            }
        }});
}

function createTrByDate(data1){
    	if(data1.length > 1){
    		var $table = $("#dataTable");
    		$($table.find("tbody")).empty();//先清空表格然后重新加载
    		if($table.find("tbody").length ==0){
        		$table.append('<tbody></tbody>');
    		}
    		var month = 12;
    		var count = 0;
    		for (var i = 0; i < month; i++,count++) {
    			var dataMin = new Date();
    			var dataMax = new Date();
                var year = $(".selectYear").find("select option:selected").val();
                dataMin.setFullYear(year,i,1);
                dataMax.setFullYear(year,i+1,1);
                dataMax.setDate(dataMax.getDate()-1);
    			var data = data1[count];
    			var defaultWeek = data.week;
    			var $tr = $('<tr class = "'+i+'"></tr>');
    			$tr.append('<td rowspan="'+defaultWeek+'" class="month" >'+(i+1)+bt.lang("table.fbsBudgetPeriod.month")+'<input type="hidden" name="month" value="'+data.month+'" /></td>');
        		$tr.append('<td rowspan="'+defaultWeek+'" class="week"> ' +
        				'<select name="week" onchange="toChangeWeek('+i+',this)" lay-ignore >'+
        				' <option>3</option>'+
        				' <option>4</option>'+
        				' <option>5</option>'+
        				' <option>6</option>'+
        				'</select>' +
        				'</td>');
        		var rmk = data.rmk==null?"":data.rmk;
        		$tr.append('<td><input type="text" lay-verify= "required" name="beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="beginDate_'+i+'_'+0+'" value="'+data.beginDateStr+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" readonly></td>');
    			$tr.append('<td><input type="text" lay-verify= "required" name="endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="endDate_'+i+'_'+0+'" value="'+data.endDateStr+'" data-callback="getBeginDate" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'"></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="weekdays" id = "weekdays_'+i+'_'+0+'" data-biz-name="weekdays_'+i+'_'+0+'" data-min="0" data-max="31" value="'+data.weekdays+'" readonly /></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="workdays" id = "workdays_'+i+'_'+0+'" data-biz-name="workdays_'+i+'_'+0+'" data-min="0" data-max="31" value="'+data.workdays+'" readonly/></td>');
    			$tr.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="holidays" id = "holidays_'+i+'_'+0+'" data-biz-name="holidays_'+i+'_'+0+'" data-min="0" data-max="31" value="'+data.holidays+'" readonly/></td>');
    			$tr.append('<td><input type="text" class="layui-input" name="rmk" id = "rmk_'+i+'_0" value="'+rmk+'" /><input type="hidden" name="id" value="'+data.id+'" /></td>');
    			$table.append($tr);
    			$tr.find("td select").val(defaultWeek);
    			$tr.find("td select").attr("readonly","readonly");
        		for (var j = 1; j < defaultWeek; j++) {
        			count++;
        			data = data1[count];
        			var $tr1 =  $('<tr class = "'+i+'"></tr>');
        			var rmkTemp = data.rmk==null?"":data.rmk;
        			$tr1.append('<td><input type="text" lay-verify= "required" name="beginDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="beginDate_'+i+'_'+j+'" value="'+data.beginDateStr+'" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'" readonly></td>');
        			$tr1.append('<td><input type="text" lay-verify= "required" name="endDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="endDate_'+i+'_'+j+'" value="'+data.endDateStr+'" data-callback="getBeginDate" data-min="'+bt.date.format(dataMin)+'" data-max="'+bt.date.format(dataMax)+'"></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="weekdays" id = "weekdays_'+i+'_'+j+'" data-biz-name="weekdays_'+i+'_'+j+'" data-min="0" data-max="31" value="'+data.weekdays+'" readonly/></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="workdays" id = "workdays_'+i+'_'+j+'" data-biz-name="workdays_'+i+'_'+j+'" data-min="0" data-max="31" value="'+data.workdays+'" readonly/></td>');
        			$tr1.append('<td><input type="text" lay-verify= "number" class="layui-input bt-biz-moneyType"  data-decimal="0" data-name="holidays" id = "holidays_'+i+'_'+j+'" data-biz-name="holidays_'+i+'_'+j+'" data-min="0" data-max="31" value="'+data.holidays+'" readonly/></td>');
        			$tr1.append('<td><input type="text" class="layui-input" name="rmk" id = "rmk_'+i+'" value="'+rmkTemp+'"/><input type="hidden" name="id" value="'+data.id+'" /></td>');
        			$table.append($tr1);
				}
			}
    	}
 }
