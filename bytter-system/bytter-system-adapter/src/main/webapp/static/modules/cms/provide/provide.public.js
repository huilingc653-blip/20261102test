/**
 * 获取日期sendDate的浮动生效日期
 * @param plDate 		日期
 * @param day 			计息日
 * @param floatWayNext  详细浮动方式
 * @param isStanderd	自然季(false)或标准季(true)
 * @param count		    不同浮动方式对应的数字
 * @return	浮动日期准确的年月信息
 */
function getFloatDate(plDate, day, floatWayNext, isStanderd, count){
	if(floatWayNext != "1" && floatWayNext != "2" && floatWayNext != "3"){
		return "";
	}
	var date = new Date(plDate);
	if(day == null || day == 0){
		day = 21;
	}
	day = parseInt(day);
	var intersetDay = date.getDate();//获取plDate的借款日
    date = new Date(date.getFullYear(), date.getMonth(), 1);//当月的第一天
	var pd = isStanderd;
	if(count == 1){ 
		pd = false;
	}
	if(pd){//标准
		var plDateMonth = null;
		if("1" == floatWayNext){//次《什么》第一天
			day = 1;
			do {
	        	var a= getMonthStr(1, date);
	        	date = new Date(a);
	        	plDateMonth = date.getMonth() + 1;
	        } while (plDateMonth % count != 1);
		}else{
			if("2" == floatWayNext){//次《什么》第贷款日
				day = intersetDay;
			}else{//次**计息日次日
                if (intersetDay > day) {//借款日在计息日之后，计息日取次月计息日，月份+13；借款日在计息日之前，计息日取当月计息日，月份+12
                    count += 1;
                }
                day += 1;
			}
			
			var a = getMonthStr(count, date);
	    	date = new Date(a);

	    	// plDateMonth = date.getMonth() + 1;
			// while (plDateMonth % count != 0){
	        // 	var a = getMonthStr(1, date);
	        // 	date = new Date(a);
	        // 	plDateMonth = date.getMonth() + 1;
	        // }
		}
	}else{
		if("1" == floatWayNext){//次《什么》第一天
			day = 1;
		}else if("2" == floatWayNext){
			day = intersetDay;
		}else{
			day += 1;
		}
		
		var a = getMonthStr(count, date);
		date  =new Date(a);
	}
	
	return this.setMonthDay(day, date);
}

/**
 * 日期增加多少个月
 * @param addMonthCount	增加几个月
 * @param date			需要增加月份的日期
 */
var daysInMonth = new Array([0],[31],[28],[31],[30],[31],[30],[31],[31],[30],[31],[30],[31]); 
function getMonthStr(addMonthCount,date){ 
	date.setMonth(date.getMonth() + addMonthCount);//获取addMonthCount天后的日期   
	var strYear = date.getFullYear();     
	var strDay = date.getDate();     
	var strMonth = date.getMonth();   
	if(strYear%4 == 0 && strYear%100 != 0){   
		daysInMonth[2] = 29;   
	}   
	if(strMonth + 1 == 13){
		strYear += 1;   
		strMonth = 1;   	
	}   
	else{   
		strMonth += 1;
	}
	strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];   
	if(strMonth<10){     
		strMonth="0"+strMonth;     
	}   
	if(strDay<10){     
		strDay="0"+strDay;     
	}   
	datastr = strYear + "-" + strMonth + "-" + strDay;   
	return datastr;
}

/**
 * 日期增加多少个月
 */
function setMonthDay(day,date){
	var strYear = date.getFullYear();
	var strMonth = date.getMonth();
	day = daysInMonth[strMonth + 1] >= day ? day : daysInMonth[strMonth + 1];
	
	strMonth = date.getMonth() + 1;
	if(strMonth < 10){     
		strMonth = "0"+strMonth;     
	}
	if(day < 10){     
		day = "0" + day;     
	}
	return strYear + "-" + strMonth + "-" + day; 
}