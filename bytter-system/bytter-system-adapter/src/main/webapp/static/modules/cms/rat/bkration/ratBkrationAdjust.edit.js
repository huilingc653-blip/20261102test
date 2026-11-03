var moneyTypes,fun, $, form;
bt.use(['form','layedit','jquery','moneyType','btUpload'], function(){
    var $ = bt.jquery;
    form = bt.form();
    fun=bt.moneyType.getFun();
    moneyTypes=bt.moneyType.init();

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    /**判断金额是否为空*/
    bt.moneyEmpty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "0" || ms == ""|| ms == " "|| ms =="undefined"){
            return true;
        }
        return false;
    }

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //检查每项授信品种是否有输入额度
    var checkItemMoney = function () {
        var success = true;
        $.each($("input[id$='money']"), function(i,item){
            if (bt.empty($(item).val())) {
                success = false;
                return false;
            }
        });
        return success;
    };

    function checkBefore() {
        if (!checkItemMoney()){
            layer.alert(bt.lang("rat.bkration.adjustMaxMoney.notNull"), {icon: 2});
            return false;
        }

        var sumAdjust = 0;//调待调整品种数
        var zeroAsjust = 0;//调整额度为零的数量
        $.each($("input[id$='money']"), function (i, item) {
            sumAdjust += 1;
            if (bt.moneyEmpty($(item).val())) {
                zeroAsjust += 1;
            }
        });
        if (sumAdjust === zeroAsjust) {//调整额度均为零
            layer.alert(bt.lang("rat.bkration.adjust.checkMoney"), {icon: 2});
            return false;
        }
        return true;
    }

    //监听提交
    form.on('submit', function (data) {
        var op=$(this).attr("lay-filter")
        if(op=="close"){
            if(!bt.navtab){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            } else {
                //bt.navtab.refreshParent();
                bt.navtab.closeCurrent();
            }
            return false;
        }else{
            bt.loading.show();//显示加载中
            if (!checkBefore()) {
                bt.loading.hide(); //关闭加载中
                return false;
            }
            data.field.status= $(this).attr("value");
            data.field.attachmentId = up.getGroupId();//设置附件ID
            var action = "/cms/rat/ratbkrationadjust/saveAdjust";
            $.post(basePath + action, data.field, function (result) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.loading.hide(); //关闭加载中
                if (result.success) {
                    if (!bt.navtab) {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        var webObj = bt.navtab.getParentDom();
                        webObj.search();
                        bt.navtab.closeCurrent();
                    }
                }
            }, 'JSON');
            return false;
        }

    });
});

bt.calMoney=function(data, elem) {
    var businessType=$("#businessType").val();
    var rowIndex = $(elem).data("row");
    var adjustMoney = moneyTypes.get(rowIndex + "adjustMoney").val();
    if(businessType=="1"){
        var disLeftMoney=moneyTypes.get(rowIndex + "disLeftMoney").val();
        var disUnUseMoney=moneyTypes.get(rowIndex + "disUnUseMoney").val();
        var maxAdjustMoney=0;
        if(bt.money.sub(disLeftMoney,disUnUseMoney)>0){
            maxAdjustMoney=disUnUseMoney;
        }else{
            maxAdjustMoney=disLeftMoney;
        }
        if(bt.money.sub(adjustMoney,maxAdjustMoney)>0){
            parent.layer.msg(bt.lang("rat.bkration.adjust.checkAdjustMoney")+maxAdjustMoney, {icon: 2});
            moneyTypes.get(rowIndex + "adjustMoney").val(maxAdjustMoney);
            adjustMoney=maxAdjustMoney;
        }
    }
    var maxMoney =moneyTypes.get(rowIndex + "maxMoney").val();
    var money = bt.money.add(maxMoney, adjustMoney);
    moneyTypes.get(rowIndex + "money").val(money);
    var ratMode=$("#ratMode").val();
    var usedMoney=0;
    if(businessType=="0"&&ratMode=="0"){
        usedMoney=moneyTypes.get(rowIndex + "disMoney").val();
    }else{
        usedMoney=moneyTypes.get(rowIndex + "applyMoney").val();
    }
    $("#"+rowIndex+"canUseMaxMoney").html("");
    $("#"+rowIndex+"canUseMaxMoney").html(bt.money.format(bt.money.sub(money,usedMoney)));
    // bt.calSumMoney();
}