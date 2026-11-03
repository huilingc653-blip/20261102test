var rowNum = 0;
var moneyType="";
var surplusMoney="";
var loanDate="";//发放日期
var repayDate="";//还款日期
var loanMoney="";//发放金额
var loanRate="";//利率
var flag=0;//判断是否显示选择 0:可以选择 1不可以选择
var repayType=$("#repayType").val();
var webObj = bt.navtab.getParentDom();
var hadRepaymentIst=webObj.bt.jquery("#hadRepayAccrual").val();
bt.use(['form','element','dateType','moneyType','date','bttable','dicts'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    moneyType=bt.moneyType.init();
    bt.dicts.init();
//	dateType=bt.dateType.init();
    initVariable();
    //生成列表
    bt.bttable.init({
        elem: "#repayPlanTB",
        url: basePath+"/cms/basedata/repaymentschedule/getRepayMentScheduleByBillCode?billCode="+$("#billCode").val(),
        columns:[
            {data:"prediceRepayDate",
                render:function(data, row){
                    rowNum=row;
                    return '<input  name="repayMents['+rowNum+'].prediceRepayDate" id="prediceRepayDate_'+rowNum+'" type="text" value="'+data.prediceRepayDate+'" class="layui-input bt-biz-dateType" data-min="'+loanDate+'" data-max="'+repayDate+'" data-type="date" data-callback="clickEvent" readonly="true"/>';
                }


            },
            {data:"repaymentMoney",
                render:function(data, row){
                    return '<input class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentMoney_'+rowNum+'" type="text" value="'+moneyFun.format(data.repaymentMoney)+'" data-decimal="2" id="repaymentMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].repaymentMoney" readonly="true"/>';
                }
            },
            {data:"repaymentIst",
                render:function(data, row){
                    return '<input class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="repaymentIst_'+rowNum+'"  type="text" value="'+moneyFun.format(data.repaymentIst)+'" data-decimal="2" id="repaymentIst_'+rowNum+'" data-min="0"  data-name="repayMents['+rowNum+'].repaymentIst" readonly= "true"/>';
                }
            },
            {data:"currentRepayMoney",
                render:function(data, row){
                    return '<input class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="currentRepayMoney_'+rowNum+'" type="text" value="'+moneyFun.format(data.currentRepayMoney)+'" data-decimal="2" id="currentRepayMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].currentRepayMoney" readonly="true"/>';
                }
            },
            /* {data:"realityRepayDate",className:'center',render: function ( data, type, full, meta ) {
                 if(data.realityRepayDate==null){
                     return "";
                 }else{
                     return data.realityRepayDate;
                 }
               }},
               {data:"realityRepayMoney",className:'textRight',render: function ( data, type, full, meta ) {
                   if(data.realityRepayMoney==null){
                       return "";
                   }else{
                     return moneyFun.format(data.realityRepayMoney);
                   }
                 }},*/
            {data:"surplusMoney",
                render:function(data, row){
                    return '<input value="'+moneyFun.format(data.surplusMoney)+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="surplusMoney_'+rowNum+'" type="text" data-decimal="2" id="surplusMoney_'+rowNum+'" data-min="0" data-name="repayMents['+rowNum+'].surplusMoney" readonly= "true"/>';
                }
            },
            {data:"isRepay",
                render:function(data, row){
                    return bt.dict('yn_state',data.isRepay)+'<input name="repayMents['+rowNum+'].isRepay" value="'+data.isRepay+'"  type="hidden"/>';
                }
            },
            {data:"remark",
                render:function(data, type, full, meta){
                    return '<input name="repayMents['+rowNum+'].remark" id="remark" value="'+(data.remark==null?"":data.remark)+'" class="layui-input" type="text" readonly= "true"/>';
                }
            },
            {data:"",bSortable:false,className:'center',
                render:function(data, type, full, meta){
                    if(repayType =="1"){
                        var repaymentIst=0;
                    } else if(repayType =="2"){
                        var repaymentIst=data.repaymentIst;
                    }else if(repayType =="3"){
                        if(isNull(hadRepaymentIst)== "0"){
                            hadRepaymentIst = 0
                        }
                        var repaymentIst=data.sumRepaymentIst-hadRepaymentIst;
                    }else{
                        if($("#tallySign").val() == "false"){
                            if($("#repayType").val()==1){
                                var repaymentIst = 0;
                            }else {
                                var repaymentIst = data.repaymentIst;
                            }

                        }else {
                            var repaymentIst = 0;
                        }
                    }

                    var html='';
                    //已还
                    if(data.isRepay=="1"){
                        if(data.used>0){
                            return '<span style="color:#5FB878">'+bt.lang('cms.repaymentSchedule.used')+'</span>';
                        }
                    }

                    if(data.isRepay=="0"){
                        if(data.used>0){
                            flag=1;
                            return '<span style="color:#009688">'+bt.lang('cms.msg.select')+'</span>';

                        }
                        if(flag==0){
                            flag=1;
                            return '<input type="button" value="'+bt.lang("common.button.select")+'" class="layui-btn layui-btn-mini" onclick="selectPlan(this)" lastSign="'+data.lastSign+'" repayMoney="'+data.repaymentMoney+'" repayDate="'+data.prediceRepayDate+'" remark="'+data.remark+'" repaymentIst="'+data.repaymentIst+'" repayMentDtlId ="'+data.id+'">';
                        }
                    }
                    return bt.lang('cms.repaymentSchedule.unused');

                }
            }

        ],
        delRowCallback:function(data,$tr){
        },
        tableInitCallback:function(data){
            rowNum = data.length;
            addFun();
        }

    });

});

bt.use(['dateType'], function() {
    dateType = bt.dateType.init();
//设置时间
    /*test.get("t3").val("2017-05-16")*/
//数据改变时回调
    bt.fun = function(value){
        MoneyCalculation();
    }
})

//全局方法  
function clickEvent(value){
    bt.fun(value);
}

function addFun(){
    //重新渲染datatype和moneyType
    dateType.render();
    moneyType.render();
    //+1为了以防id重复
    rowNum++;
}

function initVariable(){
    loanDate=$("#loanDate").val();//发放日期
    repayDate=$("#repayDate").val();//还款日期
    loanMoney=$("#loanMoney").val();//发放金额
    loanRate=$("#loanRate").val();//利率
    surplusMoney=Number(loanMoney);//剩余本金（初始化为发放金额）
}

//关闭窗口
doClose = function(){
    if(bt.navtab){
        bt.navtab.closeCurrent();
    }
}

//判断对象是否为空
/**
 * 判断是否null
 * @param data
 */
function isNull(data){
    return (data == "" || data == undefined || data == null) ? "0" : "1";
}

function selectPlan(el){
    //layer.alert(layui.jquery(el).attr("repayDate"));
    //layer.alert(layui.jquery(el).attr("repayMoney"));

//	alert(data);
    //var data = bt.jquery("#repayPlanTB").DataTable().row(bt.jquery(el).data("row")).data();
    var webObj = bt.navtab.getParentDom();
    //给父级选项卡页面中的input赋值;
    webObj.bt.jquery("#repayDate").val(layui.jquery(el).attr("repayDate"));
    //函数回调将还款日期传递
    if(typeof webObj.initRepayDate =="function"){
        webObj.initRepayDate(layui.jquery(el).attr("repayDate"));
    }
    //webObj.bt.jquery("#repayMoney").val(layui.jquery(el).attr("repayMoney"));
    webObj.bt.moneyType.get("repayMoney").val(layui.jquery(el).attr("repayMoney"));
    webObj.bt.jquery("#repaymentScheduleId").val(layui.jquery(el).attr("repayMentDtlId"));
    //webObj.bt.jquery("input[data-name='repayMoney']").val(layui.jquery(el).attr("repayMoney"));
    if(layui.jquery(el).attr("remark") != "null"){
        webObj.bt.jquery("input[assignment^='rmk']").val(layui.jquery(el).attr("remark"));
    }
    //webObj.bt.jquery("input[assignment^='repayAccrual']").attr("value",layui.jquery(el).attr("repaymentIst")); 
    //webObj.bt.jquery("input[assignment^='repayAccrual']").removeClass("init"); 
    webObj.bt.moneyType.get("repayAccrual").val(layui.jquery(el).attr("repaymentIst"));//传递还款利息
//    var isCalcInterest = webObj.bt.jquery("#calcInterest").val();//是否计算利息
//    if(isCalcInterest!=undefined&&isCalcInterest!=null&&isCalcInterest==1){
//    	webObj.getFixedMoneyInterest();
//    }
    webObj.getFixedMoneyInterest(layui.jquery(el).attr("lastSign"));
    bt.navtab.closeCurrent("false");
}
