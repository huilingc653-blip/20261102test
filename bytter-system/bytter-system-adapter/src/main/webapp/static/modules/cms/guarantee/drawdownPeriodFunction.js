var zqRate=0;//展期利率
var zqDate="";//展期日期
var remainDate;//已还的最后一笔日期加一天
/**
 * 导入还款计划(其他)
 */
function addImport(o) {
    var currentRepayMoney=o.repaymentMoney+o.repaymentIst;
    var isRepay = "0";
    if(o.isRepay =="是"){
        isRepay = "1";
    }
    var html = '<tr>';

    html += '<td><input  name="repayMents[' + rowNum + '].prediceRepayDate" value="' + o.prediceRepayDate + '"  data-biz-name="prediceRepayDate_' + rowNum + '" id="prediceRepayDate_' + rowNum + '" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
    html += '<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" value="' + o.repaymentMoney + '" divName="repaymentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentMoney_' + rowNum + '" id="repaymentMoney_' + rowNum + '" data-min="0" lay-verify="required" data-max="' + loanMoney + '" value="0" data-name="repayMents[' + rowNum + '].repaymentMoney"/></td>';
    if (repayType === "07") {
        html += '<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" value="'+ o.repaymentIst +'" data-callback="bt.calMoney" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" id="repaymentIst_' + rowNum + '" data-min="0"  data-name="repayMents[' + rowNum + '].repaymentIst" value="0"/></td>';
    } else {
        html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" value="'+ o.repaymentIst +'" data-biz-name="repaymentIst_' + rowNum + '"  type="text" data-decimal="2" id="repaymentIst_' + rowNum + '" data-min="0"  data-name="repayMents[' + rowNum + '].repaymentIst"/></td>';
    }
    html += '<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" value="'+ currentRepayMoney +'" divName="currentRepayMoney" data-biz-name="currentRepayMoney_' + rowNum + '" type="text" data-decimal="2" id="currentRepayMoney_' + rowNum + '" data-min="0" data-name="repayMents[' + rowNum + '].currentRepayMoney"/></td>';
    // html += '<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" value="'+ surplusMoney +'" data-biz-name="surplusMoney_' + rowNum + '" type="text" data-decimal="2" id="surplusMoney_' + rowNum + '" data-min="0" data-name="repayMents[' + rowNum + '].surplusMoney"/></td>';
    // html += '<td name="isRepay">' + bt.lang("common.sc_yn.no") + '<input name="repayMents[' + rowNum + '].isRepay" value="0"  type="hidden"/></td>';
    html+= '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="repayMents['+rowNum+'].isRepay" data-biz-name="isRepay_'+rowNum+'" data-defaultvalue="'+isRepay+'"></div></td>';
    html += '<td><input name="repayMents[' + rowNum + '].remark"  id="remark" class="layui-input" value="'+ o.remark +'" type="text"/></td>';
    html += '<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>' + bt.lang("common.button.delete") + '</button></td>';
    html += '</tr>';
    $("#repayPlanTB tbody").append(html);
    addFun();
};
/**
 * 新增还款计划(其他)
 */
function add(){
    var html='<tr>';
    html+='<td><input  name="repayMents['+rowNum+'].prediceRepayDate" data-biz-name="prediceRepayDate_'+rowNum+'" id="prediceRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
    html+='<td><input  class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentMoney" data-callback="bt.calMoney"  type="text" data-decimal="2" data-biz-name="repaymentMoney_'+rowNum+'" id="repaymentMoney_'+rowNum+'" data-min="0" lay-verify="required" data-max="'+loanMoney+'" value="0" data-name="repayMents['+rowNum+'].repaymentMoney"/></td>';
    if (repayType === "07"||repayType === "03") {
        html+='<td><input class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-callback="bt.calMoney" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst" value="0"/></td>';
    } else {
        html+='<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="repaymentIst" data-biz-name="repaymentIst_'+rowNum+'"  type="text" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst"/></td>';
    }
    html+='<td><input  disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="currentRepayMoney" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney"/></td>';
    // html+='<td><input disabled class="layui-input bt-input-inline  bt-biz-moneyType" divName="surplusMoney" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney"/></td>';


    html+= '<td><div class="bt-biz-dict" data-type="select" data-displayData="0,1" data-group="yn_state" data-name="repayMents['+rowNum+'].isRepay" data-biz-name="isRepay_'+rowNum+'" data-defaultvalue="0"></div></td>';

    // html+='<td name="isRepay">'+bt.lang("common.sc_yn.no")+'<input name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';
    // html+='<td name="isRepay">'+bt.lang("common.sc_yn.no")+'<input name="repayMents['+rowNum+'].isRepay" value="0"  type="hidden"/></td>';

    html+='<td><input name="repayMents['+rowNum+'].remark" id="remark" class="layui-input" type="text"/></td>';
    html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
    html+='</tr>';
    $("#repayPlanTB tbody").append(html);
    addFun();
};

/**
 * 根据预计还款日和预计还款本金，算出利息，本次还款本金，剩余本金（其他：循环每一行，之后调用calculation方法计算）
 */
function MoneyCalculation(){
    surplusMoney=Number(surplusMoneyFixed);
    var trs = $("#repayPlanTB").children("tbody").children("tr");
    var lastPrediceRepayDateId="-1";//上一笔还款本金id
    // 遍历行数据
    trs.each(function(index, tr) {

        var prediceRepayDate_id="";//预计还款日期
        var tds = $(tr).find("td");
        tds.each(function(i, td) {
                var $input = $(td).find("input");
                if ($input.length) {
                    if(!isNull($input.attr("id"))&&$input.attr("id").indexOf("prediceRepayDate")>=0){
                        prediceRepayDate_id = $input.attr("id");
                    }

                }
            }
        )
        // var isRepay=$(tr).find("td[name^='isRepay']").find("input").val();
        // if(isRepay!="0"){
        //     lastPrediceRepayDateId = prediceRepayDate_id;
        // }else{
        //     lastPrediceRepayDateId = prediceRepayDate_id;
        // }
    })
    sumMoney();
}
/**
 * 根据还款开始日期和还款日期、还款周期计算出日期数组
 */
function getDates(){
    var beginDate=dateFun.get($('#repayBeginDate').val());//开始日期
    var endDate;
    if(isNull(zqDate)){
        endDate=dateFun.get($('#repayDate').val());//还款日期
    }else{
        endDate=zqDate;
    }
    var timeSpan;
    switch(repayCycel)
    {
        case '0':
            timeSpan=1;timeParms=12;break;
        case '1':
            timeSpan=3;timeParms=4;break;
        case '2':
            timeSpan=6;timeParms=2;break;
        default: timeSpan="1";
    }
    var dates = splitByMonth(beginDate, endDate, timeSpan, "0");
    return dates;
}

/**
 * 计算两个日期间相隔的（月，季，半年）
 * @param start 开始日期
 * @param end 结束日期
 * @param size 参数
 * @param type 1：还款计划修改
 * @returns
 */
function splitByMonth(start, end, size,type){
    start = dateFun.get(start);
    end = dateFun.get(end);

    if(!start || !end || !size || size < 1){
        return null;
    };

    var ary = [];
    ary.push(start);
    // if(type=="1"){
    // 	ary.push(start);
    // }
    var temp = start;
    var day = start.getDate();
    if(addMonth(temp, size, day).getTime() > end.getTime()){
        ary.push(end);
    }else{
        while(addMonth(temp, size, day).getTime() < end.getTime()){
            var newDate = addMonth(temp, size, day);
            ary.push(newDate);
            temp = newDate;
        };
        ary.push(end);
    };

    return ary;
}

function addMonth(date, size, oriDay){
    date = dateFun.get(date);
    if(!date){
        return null;
    };
    size = size - 0; //转数字
    var y = date.getFullYear();
    var m = date.getMonth();
    var nextY = y;
    var nextM = m;
    //如果当前月+要加上的月>11 这里之所以用11是因为 js的月份从0开始
    if ((m + size)> 11) {
        nextY = y + 1;
        nextM = parseInt(m + size) - 12;
    } else {
        nextM = date.getMonth() + size
    }
    var daysInNextMonth = daysInMonth(nextY, nextM);
    var day = oriDay;
    if (day > daysInNextMonth) {
        day = daysInNextMonth;
    }
    var newdate = new Date(nextY, nextM, day);
    return newdate;
}

function daysInMonth(year, month) {
    if (month == 1) {
        if (year % 4 == 0 && year % 100 != 0){
            return 29;
        } else {
            return 28;
        }
    } else if (month == 3 || month == 5 || month == 7 || month == 10) {
        return 30;
    } else{
        return 31;
    }
};


/**
 * 获取两个日期之间的天数(天数)
 * @param strDateStart(Date)
 * @param strDateEnd(Date)
 * @returns
 */
function getDaysDate(strDateStart,strDateEnd){
    var dateStr1=dateFun.format(strDateStart, 'yyyy-MM-dd');
    var dateStr2=dateFun.format(strDateEnd, 'yyyy-MM-dd');
//	strDateStart=dateFun.get(dateStr1);
//	strDateEnd=dateFun.get(dateStr2);
// 	if(repayCycel === "0"){
//         return days = 30;
// 	}else if(repayCycel === "1" ){
//         return days = 90;
// 	}else if(repayCycel === "2"){
//         return days = 180;
// 	}else{
//         return days=dateFun.getBetweenDay(dateStr1,dateStr2)
// 	}
    var date1 = bt.date.get(strDateStart);
    var date2 = bt.date.get(strDateEnd);
    var btt = date2 - date1;
    return btt/(1000*60*60*24);
}



