var dict,form;
bt.use(['form','layedit','dicts','moneyType','selectType','select'], function(){
    var $ = bt.jquery,moneyFun = bt.moneyType.getFun();
    form = bt.form();
    dict = bt.dicts.init();
    bt.selectType.init();

    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }

    // 检查编号是否重复
    $("#code").blur(function(){
        var code = $(this).val();
        if(bt.empty(code)){
            return;
        }
        $.post(basePath + "/pricing/depositRateType/interestRateType/checkCode", {
            code : code
        }, function(data) {
            if(data && "0" == data){
                layer.alert(bt.lang("table.depositRateType.codeNoRepeat"));
                $("#code").val("");
            }
        }, 'JSON');
    });

    bt.moneyType.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/pricing/depositRateType/interestRateType/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab){
                	bt.navtab.closeCurrent();
                }
            }
        },'JSON');
        return false;
    });
});

/**根据存款种类改变页面显示*/
setShowTd=function() {
    var depositType = dict.get("depositClass").val();
    if (!bt.empty(depositType)) {
            if (depositType == "05") {//定期
                $(".periodShow").show();
                $('[data-name="period"]').attr('lay-verify', 'required');
                $(".noticeShow").hide();
            } else if (depositType == "06") {//通知
                $(".noticeShow").show();
                $('[data-name="period"]').removeAttr('lay-verify');
                dict.get("periodCycle").val("");
                dict.get("noticeType").attr({'data-required': true});
                $(".periodShow").hide();
            } else if (depositType == "04") {//活期
                $('[data-name="period"]').removeAttr('lay-verify');
                dict.get("noticeType").attr({'data-required': false});
                dict.get("periodCycle").val("");
                dict.get("period").val("");
                $(".periodShow").hide();
                $(".noticeShow").hide();
            }
            form.renderIcon();
        } else {
            return;
        }
}

//关闭当前页面
function closecurrent(){
    if(!bt.navtab){
        parent.layer?parent.layer.closeAll():layer.closeAll();
    } else {
        bt.navtab.closeCurrent();
    }
    return false;
}
