var dicts,form,rateTypes,selectTypes,moneyType;
var dateTayps;
bt.use(["jquery", 'dateType', 'dicts', 'rateType','moneyType','rateType','selectType'], function () {
    $ = bt.jquery;
    form = bt.form(),moneyFun = bt.moneyType.getFun();
    selectTypes = bt.selectType.init();//下拉框
    rateTypes = bt.rateType.init();//利率
    var dateTayps = bt.dateType.init();
    dictes = bt.dicts.init();
    moneyType=bt.moneyType.init();//金额
    //初始化利率
    bt.rateType.init();
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }
//监听提交
    form.on('submit(submit)', function (data) {
        var status = $(this).attr("value");
        var action = "/cms/LeaseExtend/modify";
        data.field.status = status;//提交状态
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    //这里填写新的js代码，请参考前端文档
});

function resetCallback() {
    window.location.reload();
}
//获取利率
// $("#getRate").click(function(){
//     getRate();
// });
//获取利率
function getRate(){
    var rate=Number($("#nowRate").val());//转Number类型
    if(bt.empty(rate)){
        parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
        return ;
    }

    var pointsProportionSelect = dictes.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyType.get("pointsProportionText").val();//浮动点数或比例值
    var floatWay = $("#floatWay").val();
    if(pointsProportionText != "" && floatWay != "01"){//非固定利率
        if(pointsProportionSelect=="01"){//点数
            //rateTypes.get("rate").val(data.rate+(pointsProportionText/100));//使用百分比  不使用万分比
            //基础利率+浮动的点数*0.01%
            rateTypes.get("rate").val(moneyFun.format(rate + pointsProportionText * (0.01 / 100), {decimal: 4}));
            return;
        }else{
            //比例 基准利率+浮动的比例*基准利率
            //rateTypes.get("rate").val((data.rate)*(1+(pointsProportionText/100)));
            rateTypes.get("rate").val(moneyFun.format(rate + (pointsProportionText / 100) * rate, {decimal: 4}));
            return;
        }
    }

    if(bt.empty(rate)){
        rateTypes.get("rate").val(0);
    }else{
        rateTypes.get("rate").val(moneyFun.format(rate, {decimal: 4}));
    }
}
//展期到期日期  计算展期期限
function computeplTerm(ele,data){
    // var start = bt.dateType.get("endDate").val();
    var end =  bt.dateType.get("expendedDate").val();  //展期日期
    var scDate =bt.dateType.get("lastExpendedDate").val();   //到期日期
    var plTerm;
    if(bt.empty(scDate) || bt.empty(end)){
        plTerm = "";
    }else{

        if(scDate > end){
            bt.dateType.init().get($(data).attr("data-biz-name")).val("");
            layer.tips(bt.lang("cms.provide.deferred.not.unique.deferredDate"), '#'+$(data).attr("data-biz-name"));
            return false;
        }

        var ternUnit = bt.jquery.trim(dictes.get("ternUnit").val());
        if(!bt.empty(ternUnit)){
            plTerm = dataSubtract(scDate,end,ternUnit);
        }else{
            //为空或者null默认月处理
            dictes.get("ternUnit").val("M");
            plTerm = dataSubtract(scDate,end,"M");
        }
    }
    bt.jquery("#expendedLimit").val(plTerm);//设置借款期限
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
/**加载浮动利率的周期信息*/
// function loadrepayCycle(){
//     var data = selectTypes.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
//     var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
//     //设置text的输入范围
//     pointsProportionTextObj.attr("data-min",data.ratedownlimit);
//     pointsProportionTextObj.attr("data-max",data.rateuplimit);
//     readonlyText();
// }
