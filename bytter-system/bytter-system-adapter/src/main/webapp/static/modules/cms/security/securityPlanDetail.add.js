var $,form,moneyType,dateType,dateFun,dicts,moneyFun,bttable,selectTable,rateTypes,securityIssueCode;
var rowNum = 0;//用来记录行
var maxrepayMoney="";//最大缴付金额
var surplusPayMoney="";//剩余缴付金额
var minPayDate="";//最小缴付日期
var maxPayDate="";//最大缴付日期
var isRepay="0";//初始化时，若为1：已缴付/过户，则设置不可编辑
bt.use(['form','element','dicts','rateType','moneyType','date','dateType','btUpload','bttable', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    securityIssueCode=$("#securityIssueCode").val();
    //加载数据
    initVariable();
    //生成列表
    bt.bttable.init({
        elem: "#planDetailTB",
        mulselect:false,
        checkbox: false,
        url: basePath+"/cms/security/plan/getPlanDetailBysecurityIssueCode?securityIssueCode="+securityIssueCode,
        buttons: [
            {
                text: '<i class="layui-icon">&#xe654;</i>'+bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            }
        ],
        columns:[
            {data:"securityIssueCode", render:function(data, row){
                    rowNum=row;
                    isRepay=data.isRepay=="1"?"disabled":"";
                    var html='<input type="text" id="securityIssueCode_'+rowNum+'" name="detailDtl['+rowNum+'].securityIssueCode" value="'+(data.securityIssueCode==null?"":data.securityIssueCode)+'" readonly="readonly" class="layui-input" />';
                    html=html+' <input type="hidden" id="id_'+rowNum+'" name="detailDtl['+rowNum+'].id"  value="'+data.id+'">';
                    return html;
                }
            },
            {data:"repayDate",
                render:function(data, row){
                        return '<input '+isRepay+' name="detailDtl['+rowNum+'].repayDate" data-biz-name = "repayDate_'+rowNum+'" id="repayDate_'+rowNum+'" type="text" value="'+data.repayDate+'" class="layui-input bt-biz-dateType" ' +
                            /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                            'data-type="date" lay-verify="required"/>';
                }
            },
            {data:"planRepayDate",
                render:function(data, row){
                    return '<input '+isRepay+' name="detailDtl['+rowNum+'].planRepayDate" data-biz-name = "planRepayDate_'+rowNum+'" id="planRepayDate_'+rowNum+'" type="text" value="'+data.planRepayDate+'" class="layui-input bt-biz-dateType" ' +
                        /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                        'data-type="date" lay-verify="required"/>';
                }
            },
            {data:"repayMoney", bSortable: true, className: "textRight",render:function(data, row){
                    var html = '<input '+isRepay+' divName="repayMoney" lay-verify="required" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+moneyFun.format(data.repayMoney)+'"  type="text" data-decimal="2" id="repayMoney_'+rowNum+'" data-callback="bt.calMoney" data-biz-name="repayMoney_'+rowNum+'" data-min="0" data-max="'+maxrepayMoney+'" data-name="detailDtl['+rowNum+'].repayMoney"/>';
                    maxrepayMoney=moneyFun.sub(parseFloat(maxrepayMoney),parseFloat(data.repayMoney));//最大缴付金额
                    return html
                }
            },
            {data:"repayInterest", bSortable: true, className: "textRight",render:function(data, row){
                    return '<input '+isRepay+' divName="repayInterest" lay-verify="required" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+moneyFun.format(data.repayInterest)+'"  type="text" data-decimal="2" id="repayInterest_'+rowNum+'" data-callback="bt.calMoney" data-biz-name="repayInterest_'+rowNum+'" data-min="0"  data-name="detailDtl['+rowNum+'].repayInterest"/>';
                }
            },
            {data:"repayAmount", bSortable: true, className: "textRight",render:function(data, row){
                    return '<input disabled divName="repayAmount" lay-verify="required" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+moneyFun.format(data.repayAmount)+'"  type="text" data-decimal="2" id="repayAmount_'+rowNum+'" data-biz-name="repayAmount_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].repayAmount"/>';
                }
            },
            {data:"remainMoney",bSortable: true, className: "textRight", render:function(data, row){
                    return '<input disabled divName="remainMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="remainMoney_'+rowNum+'" type="text" value="'+(data.remainMoney==null?"":moneyFun.format(data.remainMoney))+'" data-decimal="2" id="remainMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].remainMoney"/>';
                }
            },

            {data:"isRepay", bSortable: true, className: "textRight",render:function(data, row){
                    if(data.isRepay=="1"){
                        return '<input  '+isRepay+' type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isRepay" value="1"  checked/>';
                    }else{
                        return '<input  type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isRepay" value="1" />';
                    }
                }
            },
            {data:"rmk", render:function(data, row){
                    return '<input '+isRepay+' name="detailDtl['+rowNum+'].rmk" id="rmk" value="'+(data.rmk==null?"":data.rmk)+'" class="layui-input" type="text"/>';
                }
            },
            {data:null, render:function(data,row){

                    return '<button '+isRepay+' data-type="del" type="button" name="itemOrder" value="'+rowNum+'" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button>';
                }
            }
        ],
        delRowCallback:function(data,$tr){
            $tr.remove();
            MoneyCalculation();
        },
        tableInitCallback:function(data){
            rowNum = data.length-1;
            addFun();
            sumMoney();
        }
    });

    form.on('submit(save)', function(data){
        var sum=0;
        $("input[divName^='repayMoney']").each(function(i){
            sum++;
        });
        if(sum==0){
            layer.alert(bt.lang('cms.equity.palnIsNull'), {icon: 7});
            return false;
        }
        bt.loading.show();
        var action = "/cms/security/plan/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                var webObj = bt.navtab.getParentDom();
                //给父级选项卡页面中id为test的input赋值;
                /*webObj.bt.jquery("#planId").val(result.obj);
                webObj.bt.jquery("#planStatus").val("0");*/
                bt.navtab.closeCurrent();
                if(isNull($("#isPass").val())) {
                    bt.navtab.refreshCurrent();
                }
            }
        },'JSON');

        return false;
    });
});

/**
 * 初始化数据
 */
function initVariable(){
    //minPayDate=$("#loanDate").val();//最小缴付日期
    //maxPayDate=$("#repayDate").val();//最小缴付日期
    maxrepayMoney=$("#issueMoney").val();//最大缴付金额
    surplusPayMoney=Number(maxrepayMoney);//剩余本金（初始化为发放金额）
    /* loanRate=Number($("#loanRate").val());//利率

     surplusMoneyFixed=Number(loanMoney);//剩余本金（初始化为发放金额）
     if(!isNull($("#rateBase").val())){
         rateBase=Number($("#rateBase").val());
     }
     $("#repayBeginDate").attr("disabled","true");*/
}

function addFun(){
    //重新渲染datatype和moneyType
    dateType.render();
    moneyType.render();
    rateTypes.render();//利率
    form.render('checkbox');
    //+1为了以防id重复
    rowNum++;
}

/**
 * 预计还款本金回调事件
 */
var divName = null
bt.calMoney = function(data,el){
    calCustomMoney(el.parents("tr"));
}

/*
    *
    * 自定义金额计算
    * @param trEle
*/
function calCustomMoney(trEle){
    var issueMoney=$('#issueMoney').val();
    // 行下标
    var rowNum = trEle.find("input[divName^='repayMoney']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var repayMoneyNum = moneyType.get("repayMoney_"+rowNum).val();
        if (!isNull(repayMoneyNum) ) {
            var repayMoney = 0;
            var allReRepoMoney=0;
            var repayInterestNum=0;
            var repayAmount=0;
            surplusPayMoney=issueMoney;
            // 遍历本金
            $("input[divName^='repayMoney']").each(function(i){
                var thisrow=$(this).data("id").split("_")[1];
                repayMoney =moneyFun.format($(this).val());//此行金额
                repayInterestNum = moneyType.get("repayInterest_"+thisrow).val();//此行利息
                allReRepoMoney = moneyFun.add(allReRepoMoney, $(this).val());
                if (repayInterestNum == undefined || repayInterestNum == ""){
                    repayInterestNum = 0;
                }

                if (surplusPayMoney > 0){
                    surplusPayMoney= parseFloat(issueMoney) - parseFloat(allReRepoMoney);
                    repayAmount=  moneyFun.add(repayMoney,repayInterestNum);
                    moneyType.get("remainMoney_"+thisrow).val(surplusPayMoney);//剩余金额
                    moneyType.get("repayAmount_"+thisrow).val(repayAmount);//此行支付总额
                }else{// 表示无需换本金了
                    moneyType.get("repayMoney_"+thisrow).val(0);//本金
                    moneyType.get("remainMoney_"+thisrow).val(0);//剩余金额
                    moneyType.get("repayAmount_"+thisrow).val(repayInterestNum);//此行支付总额
                }

            });
            sumMoney();
        }
    }
}

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000050",
        titleRows: 0,
        headRows: 1,
        width: "70%",
        height: "70%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            for (let i = 0; i < select.length; i++) {
                if(surplusPayMoney<select[i].repayMoney){
                    select[i].repayMoney=surplusPayMoney;
                }
                surplusPayMoney=surplusPayMoney-select[i].repayMoney
                addImport(select[i],surplusPayMoney);
                sumMoney();
            }
        }
    });
};
