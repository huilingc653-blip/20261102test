var $,form,moneyType,dateType,dateFun,dicts,moneyFun,bttable,selectTable,rateTypes,equityCode;
var rowNum = 0;//用来记录行
var maxPayMoney="";//最大缴付金额
var surplusPayMoney="";//剩余缴付金额
var minPayDate="";//最小缴付日期
var maxPayDate="";//最大缴付日期
var isPay="0";//初始化时，若为1：已缴付/过户，则设置不可编辑
bt.use(['form','element','dicts','rateType','moneyType','date','dateType','btUpload','bttable', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    equityCode=$("#equityCode").val();
    //加载数据
    initVariable();
    //生成列表
    bt.bttable.init({
        elem: "#planDetailTB",
        mulselect:false,
        checkbox: false,
        url: basePath+"/cms/equity/payPlan/getPlanDetailByEquityCode?equityCode="+equityCode,
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
            {data:"equityCode", render:function(data, row){
                    rowNum=row;
                    isPay=data.isPay=="1"?"disabled":"";
                    var html='<input type="text" id="equityCode_'+rowNum+'" name="detailDtl['+rowNum+'].equityCode" value="'+(data.equityCode==null?"":data.equityCode)+'" readonly="readonly" class="layui-input" />';
                    html=html+' <input type="hidden" id="id_'+rowNum+'" name="detailDtl['+rowNum+'].id"  value="'+data.id+'">';
                    return html;
                }
            },
            {data:"payDate",
                render:function(data, row){
                    if(isNull(data.payDate)){
                        return '<input '+isPay+' name="detailDtl['+rowNum+'].payDate" data-biz-name = "payDate_'+rowNum+'" id="payDate_'+rowNum+'" type="text"  class="layui-input bt-biz-dateType" ' +
                            /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                            'data-type="date" lay-verify="required"/>';
                    }else{
                        return '<input '+isPay+' name="detailDtl['+rowNum+'].payDate" data-biz-name = "payDate_'+rowNum+'" id="payDate_'+rowNum+'" type="text" value="'+data.payDate+'" class="layui-input bt-biz-dateType" ' +
                            /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                            'data-type="date" lay-verify="required"/>';
                    }
                }
            },

            {data:"payMoney", bSortable: true, className: "textRight",render:function(data, row){

                    return '<input '+isPay+' divName="payMoney" lay-verify="required" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+moneyFun.format(data.payMoney)+'"  type="text" data-decimal="2" id="payMoney_'+rowNum+'" data-callback="bt.calMoney" data-biz-name="payMoney_'+rowNum+'" data-min="0" data-max="'+maxPayMoney+'" data-name="detailDtl['+rowNum+'].payMoney"/>';
                }
            },
            {data:"rePayMoney",bSortable: true, className: "textRight", render:function(data, row){
                    return '<input disabled divName="rePayMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="rePayMoney_'+rowNum+'" type="text" value="'+(data.rePayMoney==null?"":moneyFun.format(data.rePayMoney))+'" data-decimal="2" id="rePayMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].rePayMoney"/>';
                }
            },
            {data:"accounted", bSortable: true, className: "textRight",render:function(data, row){
                    return '<input disabled divName="accounted" class="layui-input   bt-biz-rateType" data-biz-name="accounted_'+rowNum+'"  type="text" value="'+(data.accounted==null?"":data.accounted)+'"  id="accounted_'+rowNum+'"   data-name="detailDtl['+rowNum+'].accounted"/>';
                }
            },
            {data:"assName", render:function(data, row){
                    return '<input type="text" '+isPay+' id="assName" name="detailDtl['+rowNum+'].assName" value="'+(data.assName==null?"":data.assName)+'"  class="layui-input" />';
                }
            },
            {data:"isPay", bSortable: true,render:function(data, row){
                    if(data.isPay=="1"){
                        return '<input  '+isPay+' type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isPay" value="1"  checked/>';
                    }else{
                        return '<input  type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isPay" value="1" />';
                    }
                }
            },
            {data:"rmk", render:function(data, row){
                    return '<input '+isPay+' name="detailDtl['+rowNum+'].rmk" id="rmk" value="'+(data.rmk==null?"":data.rmk)+'" class="layui-input" type="text"/>';
                }
            },
            {data:null, render:function(data,row){
                    return '<button '+isPay+' data-type="del" type="button" name="itemOrder" value="'+rowNum+'" class="layui-btn layui-btn-mini" ><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button>';
                }
            }
        ],
        delRowCallback:function(data,$tr){
            $tr.remove();
            sumMoney();
            //MoneyCalculation();
        },
        tableInitCallback:function(data){
            rowNum = data.length;
            addFun();
            sumMoney();
        }
    });

    form.on('submit(save)', function(data){
        var sum=0;
        var flag=true;
        $("input[divName^='payMoney']").each(function(i){
            sum++;
            if($(this).val()==0){
               flag=false
            }
        });
        if(flag==false){
            parent.layer.alert(bt.lang("cms.equity.payMoney.error"),{icon: 7});//缴付金额不能为0
            return false;
        }
        if(sum==0){
            layer.alert(bt.lang('cms.equity.palnIsNull'), {icon: 7});
            return false;
        }
        bt.loading.show();
        var action = "/cms/equity/payPlan/payPlanUpdate";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                var webObj = bt.navtab.getParentDom();
                //给父级选项卡页面中id为test的input赋值;
                /*webObj.bt.jquery("#planId").val(result.obj);
                webObj.bt.jquery("#planStatus").val("0");*/
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
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
    maxPayMoney=$("#invMoney").val();//最大缴付金额
    surplusPayMoney=Number(maxPayMoney);//剩余本金（初始化为发放金额）
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
    var invMoney=$('#invMoney').val();
    // 行下标
    var rowNum = trEle.find("input[divName^='payMoney']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var payMoneyNum = moneyType.get("payMoney_"+rowNum).val();
        if (!isNull(payMoneyNum) ) {
            var allRepayMoney = 0;
            // 遍历本金
            $("input[divName^='payMoney']").each(function(i){
                var thisrow=$(this).data("id").split("_")[1];
                allRepayMoney = moneyFun.add(allRepayMoney, $(this).val());
                surplusPayMoney = parseFloat(invMoney) - parseFloat(allRepayMoney);
                moneyType.get("rePayMoney_"+thisrow).val(surplusPayMoney);
            });
            // 重新遍历刷新每一行明细payMoney字段的data-max属性值
            $("input[divName^='payMoney']").each(function(i){
                var thisrow=$(this).data("id").split("_")[1];
                var currentPayMoney = moneyFun.add(0, $(this).val());
                var maxForThisRow = currentPayMoney + surplusPayMoney;
                moneyType.get("payMoney_"+thisrow).attr("data-max", maxForThisRow);
            });

            rateTypes.get("accounted_"+rowNum).val(moneyFun.div(parseFloat(payMoneyNum), parseFloat(invMoney), 4));
            sumMoney();
        }
    }
}

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000030",
        titleRows: 0,
        headRows: 1,
        width: "70%",
        height: "70%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            var invMoney=$('#invMoney').val();
            var allRepayMoney = 0;
            // 遍历本金
            $("input[divName^='payMoney']").each(function(i){
                allRepayMoney = moneyFun.add(allRepayMoney, $(this).val());
            });
            surplusPayMoney = parseFloat(invMoney) - parseFloat(allRepayMoney);
            for (let i = 0; i < select.length; i++) {
                surplusPayMoney=surplusPayMoney-select[i].payMoney
                addImport(select[i],surplusPayMoney);
                sumMoney();
            }
        }
    });
};