bt.use(['form','layedit','dateType','dicts','moneyType','btUpload'], function(){
    $ = bt.jquery,form = bt.form();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }

    /**
     * 还款金额变更，重新计息
     * @param data
     * @param ele
     */
    changeRepayMoney = function(data,ele){
        if(dicts.get("repayType").val()!=1){
            getFixedMoneyInterest();
        }
    }

    /**
     * 还款日期变更，重新计息
     * @param data
     * @param ele
     */
    changeRepayDate = function(data,ele){
        if(dicts.get("repayType").val()!=1){
            getFixedMoneyInterest();
        }
    }

    /**
     * 还款类型变更
     * @param data
     * @param ele
     */
    changeRepayType = function(data,ele){
        moneyType.get("repayAccrual").removeAttr("readOnly");
        if(data.value==1){//归还本金
            $("#repayPlanButton").show();
            moneyType.get("repayAccrual").attr("lay-verify","");
            moneyType.get("repayAccrual").val("");
            resetRepayInfo();
        }else if(data.value==2){//归还本金及归还金额利息
            $("#repayPlanButton").show();
            moneyType.get("repayAccrual").val(0);
            moneyType.get("repayAccrual").attr("lay-verify","required");
            resetRepayInfo();
            getFixedMoneyInterest();
        }else if(data.value==3){//归还本金及所有利息
            $("#repayPlanButton").show();
            moneyType.get("repayAccrual").val(0);
            moneyType.get("repayAccrual").attr("lay-verify","required");
            resetRepayInfo();
            getFixedMoneyInterest();
        }else if(data.value==4){//归还利息
            $("#repayPlanButton").hide();
            moneyType.get("repayAccrual").val(0);
            moneyType.get("repayAccrual").attr("lay-verify","required");
            clearRepaymentSchedule();
            getFixedMoneyInterest();
            var tallySign = $("#tallySign").val();
            if("false" == tallySign) {
                calcAllInterest();
                return;
            }
        } else {
            moneyType.get("repayAccrual").val("");
            moneyType.get("repayMoney").val("");
            dateType.get("repayDate").val("");
        }
    }

    /**
     * 清空还款计划
     */
    clearRepaymentSchedule=function(){
        if($("#isHaveRepayMent").val()=="1"){
            $("#repaymentScheduleId").val("");
            moneyType.get("repayMoney").val(dicts.get("repayType").val()==4?0:"");
            dateType.get("repayDate").attr("readonly",dicts.get("repayType").val()==4?false:true);
            dateType.get("repayDate").val("");
            dateType.get("repayDate").render();
        }else{
            if(dicts.get("repayType").val()==4){
                moneyType.get("repayMoney").val(0);
            }
        }
    }

    /**
     * 重置还款信息
     */
    resetRepayInfo=function(){
        if(!isSelectRepayment()){
            if($("#isHaveRepayMent").val()=="1"){
                $("#repaymentScheduleId").val("");
                moneyType.get("repayMoney").val(dicts.get("repayType").val()==4?0:"");
                dateType.get("repayDate").attr("readonly",dicts.get("repayType").val()==4?false:true);
                dateType.get("repayDate").val("");
                dateType.get("repayDate").render();
            }else{
                if(dicts.get("repayType").val()==4){
                    moneyType.get("repayMoney").val($("#repayMoneyOld").val());
                }
            }
        }else{
            moneyType.get("repayMoney").val(dicts.get("repayType").val()==4?0:$("#repayMoneyOld").val());
            dateType.get("repayDate").val($("#repayDateOld").val());
            dateType.get("repayDate").render();
        }

    }

    /**
     * 是否已选还款计划
     */
    isSelectRepayment = function(){
        if(bt.empty($("#isSelectRepayment").val())){//是否已选还款计划
            return false;
        }else{
            return true;
        }
    }

    /**
     * 根据账号金额获取利息
     * @param params
     */
    getFixedMoneyInterest = function(lastSign){
        var tallySign = $("#tallySign").val();
        var isHaveRepayMent = $("#isHaveRepayMent").val();
        var repayway = dicts.get("repayType").val();//还款方式
        if(0 == isHaveRepayMent) {//还本
            if("3" == repayway || "4" == repayway) {//还本
                calcAllInterest();
            }
        }

        if(bt.empty(moneyType.get("repayMoney").val())||bt.empty(dateType.get("repayDate").val()) || repayway==1 || repayway == " "){
            moneyType.get("repayAccrual").val("");
            if("1" == repayway) {//还本
                moneyType.get("repayAccrual").attr("readOnly", "true");
            }
            return false;
        }

        if("false" == tallySign) {
            setReapyInterest(lastSign);
            return;
        }
        var params = {
            "account":$("#loanAcc").val(),
            "money":dicts.get("repayType").val()==2?moneyType.get("repayMoney").val(): 0,
            "businessID":$("#orderNo").val(),
            "endInstDate":dateType.get("repayDate").val(),
            "tallySign":tallySign
        };
        // console.log("account:"+$("#loanAcc").val()+"  money:"+(dicts.get("repayType").val()==2?moneyType.get("repayMoney").val():$("#sendMoney").val())+"  endInstDate"+dateType.get("repayDate").val())
        $.ajax({
            type: "POST",
            url: basePath+"/cms/loan/cmsloanrepay?getFixedMoneyInterest",
            data: params,
            dataType : "json",
            success: function(rs){
                moneyType.get("repayAccrual").val(rs.obj.interest);
            }
        });
    }

    setReapyInterest = function (lastSign) {
        var repayway = dicts.get("repayType").val();//还款方式
        var isHaveRepayMent = $("#isHaveRepayMent").val();
        if(1 == isHaveRepayMent && "3" == repayway) {//非归还本金及所有利息
            if ("true" != lastSign) {//最后一次还款
                layer.alert(bt.lang("cms.loanRepay.submit.lastRepay.repayType"), {icon:5});//还款计划最后一期才支持归还本金及所有利息！
                moneyType.get("repayAccrual").val("");
                moneyType.get("repayMoney").val("");
                dateType.get("repayDate").val("");
                return;
            }
        }

        if("1" == repayway) {//还本
            moneyType.get("repayAccrual").val("");
            moneyType.get("repayAccrual").attr("readOnly", "true");
        } else if("2" == repayway) {//归还本金及归还金额利息

        } else if("3" == repayway) {//归还本金及所有利息
            calcAllInterest();
        } else if("4" == repayway) {//归还利息
            calcAllInterest();
        }
    }

    calcAllInterest = function(){
        var params = {
            "id" : $("#sendId").val()
        };
        $.ajax({
            type: "POST",
            url: basePath+"/cms/loan/cmsloanrepay?getLoanSendInterest",
            data: params,
            dataType : "json",
            success: function(rs){
                moneyType.get("repayAccrual").val(rs.obj.interest);
            }
        });
    }

    var dateType = bt.dateType.init();
    var dicts = bt.dicts.init();
    //格式化数字
    var moneyType = bt.moneyType.init();
    //初始化附件
    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand:true,
        gid: $("#attachmentgid").val()
    });

    //监听提交
    form.on('submit(submit)', function(data){
        if(!validataRepayMent()){//验证还款计划
            layer.tips(bt.lang("cms.loan.repaymentIsModifyMsg"), '#repayPlanButton',{tips: 1});
            return false;
        }else{
            var status=$(this).attr("value");
            var action = "/cms/loan/cmsloanrepay/modify";
            data.field.status = status;//提交状态
            data.field.attachmentId = attachments.getGroupId();//附件ID
            if(data.field.repayType == "4"&&data.field.repayAccrual == "0"){
                layer.alert(bt.lang("cms.loanRepay.submit.repayAccrual.zero"),{icon:5});//利息为零不能进行还款
                return false;
            }else if(data.field.repayType != "4" && data.field.repayMoney =="0"){
                layer.alert(bt.lang("cms.loanRepay.submit.repayMoney.zero"),{icon:5});//本金为零不能进行还款
                return false;
            }
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
        }
        return false;
    });

    /**验证还款计划*/
    validataRepayMent = function(){
        var valiSign = true;
        if(!bt.empty($("#repaymentScheduleId").val())){
            $.ajax({
                type: "POST",
                url: basePath+"/cms/loan/cmsloanrepay?validataRepayMent",
                async:false,
                data: {
                    "repaymentId":$("#repaymentScheduleId").val()
                },
                dataType : "json",
                success: function(rs){
                    if(!rs.obj){
                        valiSign = false;
                    }else{
                        valiSign = true;
                    }
                }
            });
        }
        return valiSign;
    }

    //关闭窗口
    doClose = function(){
        if(bt.navtab){
            bt.navtab.closeCurrent();
        }
    }

    var oriRepayType = $("#oriRepayType").val();
    if(oriRepayType == 4){//归还利息
        $("#repayPlanButton").hide();
        dateType.get("repayDate").attr("readonly", false);
    }
});

//打开还款计划
function repayPlan(){
    var dicts = bt.dicts.init();
    var repayway = dicts.get("repayType").val();
    if($("#isHaveRepayMent").val()==0){//未维护还款计划
        layer.tips(bt.lang("cms.noReplament.Message"), '#repayPlanButton',{tips: 1});
        return false;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/basedata/repaymentschedule/toRepayPlanSave?billCode="+$("#sendId").val()+"&tallySign="+$("#tallySign").val() +"&repayway="+ repayway);
        return false;
    }
}
