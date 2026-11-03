var dateTypes,moneyTypes,selectTypes,dicts,rateTypes,parameters,form,ratioType;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','btUpload','rateType','ratioType'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    moneyFun=bt.moneyType.getFun();
    ratioType= bt.ratioType.init();

    var gid = $("#attachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //监听提交
    form.on('submit', function(data){
        var nm = $(this).attr("id");
        var saveType="0";
        if(nm == "submit"){
            //data.field.saveType = "1";
            saveType="1";
        }
        var action = "/cms/bond/transfer/save?saveType="+saveType;
        data.field.attachmentId = up.getGroupId();
        data.field.status = $(this).val();
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    /*if(data.field.id == ""){
                        bt.navtab.refreshCurrent();
                    }else{*/
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    // }

                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });

});

function corpCallback(data) {
    $("#corpName").val(data.corpName)
}

function resetCallback() {
    window.location.reload();
}

function typeCallBack(data) {
    if (data.bondname === '可转债' || data.bondname === '可交债') {
        $("#convertStartDate").attr("lay-verify", "required")
        $("#convertEndDate").attr("lay-verify", "required")
        $("#convertRate").attr("lay-verify", "required")
    } else {
        $("#convertStartDate").removeAttr("lay-verify")
        $("#convertEndDate").removeAttr("lay-verify")
        $("#convertRate").removeAttr("lay-verify")
    }
    form.renderIcon()
}