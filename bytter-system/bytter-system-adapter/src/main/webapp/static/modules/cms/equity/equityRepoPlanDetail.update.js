var $,form,moneyType,dateType,dateFun,dicts,moneyFun,bttable,selectTable,rateTypes,equityCode;
var rowNum = 0;//用来记录行
var maxRepoMoney="";//最大缴付金额
var surplusRepoMoney="";//剩余缴付金额
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
        url: basePath+"/cms/equity/repoPlan/getRepoDetailByEquityCode?equityCode="+equityCode,
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
            {data:"repoDate",
                render:function(data, row){
                    if(isNull(data.repoDate)){
                        return '<input '+isPay+' name="detailDtl['+rowNum+'].repoDate" data-biz-name = "repoDate_'+rowNum+'" id="repoDate_'+rowNum+'" type="text"  class="layui-input bt-biz-dateType" ' +
                            /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                            'data-type="date" lay-verify="required"/>';
                    }else{
                        return '<input '+isPay+' name="detailDtl['+rowNum+'].repoDate" data-biz-name = "repoDate_'+rowNum+'" id="repoDate_'+rowNum+'" type="text" value="'+data.repoDate+'" class="layui-input bt-biz-dateType" ' +
                            /*'data-min="'+minPayDate+'" data-max="'+maxPayDate+'" data-callback="clickEvent"' +*/
                            'data-type="date" lay-verify="required"/>';
                    }
                }
            },

            {data:"repoMoney", bSortable: true, className: "textRight",render:function(data, row){

                    return '<input '+isPay+' divName="repoMoney" lay-verify="required" class="layui-input bt-input-inline  bt-biz-moneyType" value="'+moneyFun.format(data.repoMoney)+'"  type="text" data-decimal="2" id="repoMoney_'+rowNum+'" data-callback="bt.calMoney" data-biz-name="repoMoney_'+rowNum+'" data-min="0" data-max="'+maxRepoMoney+'" data-name="detailDtl['+rowNum+'].repoMoney"/>';
                }
            },
            {data:"reRepoMoney",bSortable: true, className: "textRight", render:function(data, row){
                    return '<input disabled divName="reRepoMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="reRepoMoney_'+rowNum+'" type="text" value="'+(data.reRepoMoney==null?"":moneyFun.format(data.reRepoMoney))+'" data-decimal="2" id="reRepoMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].reRepoMoney"/>';
                }
            },
            {data:"accounted", bSortable: true, className: "textRight",render:function(data, row){
                    return '<input disabled divName="accounted" class="layui-input   bt-biz-rateType" data-biz-name="accounted_'+rowNum+'"  type="text" value="'+(data.accounted==null?"":data.accounted)+'"  id="accounted_'+rowNum+'"   data-name="detailDtl['+rowNum+'].accounted"/>';
                }
            },
            {data:"isPay", bSortable: true, className: "textRight",render:function(data, row){
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
        //本次回购金额合计
        var repoMoneySum=0;
        //最大回购金额
        var repoMoney = $("#repoMoney").val();
        $("input[divName^='repoMoney']").each(function(i){
            repoMoneySum = moneyFun.add(repoMoneySum, $(this).val());
            sum++;
            if($(this).val()==0){
                flag=false
            }
        });
        if(flag==false){
            parent.layer.alert(bt.lang("cms.equity.repoMoney.error"),{icon: 7});//回购金额不能为0
            return false;
        }
        if(sum==0){
            layer.alert(bt.lang('cms.equity.palnIsNull'), {icon: 7});
            return false;
        }
        //如果计划本次回购金额合计大于最大回购金额，则提示返回
        if(parseFloat(repoMoneySum)>parseFloat(repoMoney)){
            layer.alert(bt.lang('cms.equity.rePoMoneyMaxError'), {icon: 7});
            return false;
        }
        bt.loading.show();
        var action = "/cms/equity/repoPlan/repoPlanUpdate";
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
    maxRepoMoney=$("#invMoney").val();//最大回购金额
    surplusRepoMoney=Number(maxRepoMoney);//剩余回购金额（初始化为回购金额）
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
    var rowNum = trEle.find("input[divName^='repoMoney']").data("id").split("_")[1];
    if (!isNull(rowNum)) {
        var repoMoneyNum = moneyType.get("repoMoney_"+rowNum).val();
        if (!isNull(repoMoneyNum) ) {
            var allReRepoMoney = 0;
            // 遍历本金
            $("input[divName^='repoMoney']").each(function(i){
                var thisrow=$(this).data("id").split("_")[1];
                allReRepoMoney = moneyFun.add(allReRepoMoney, $(this).val());
                surplusRepoMoney = parseFloat(invMoney) - parseFloat(allReRepoMoney);
                moneyType.get("reRepoMoney_"+thisrow).val(surplusRepoMoney);
            });

            rateTypes.get("accounted_"+rowNum).val(moneyFun.div(parseFloat(repoMoneyNum), parseFloat(invMoney), 4));
            sumMoney();
        }
    }
}

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000031",
        titleRows: 0,
        headRows: 1,
        width: "70%",
        height: "70%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            for (let i = 0; i < select.length; i++) {
                surplusRepoMoney=surplusRepoMoney-select[i].repoMoney
                addImport(select[i],surplusRepoMoney);
                sumMoney();
            }
        }
    });
};
