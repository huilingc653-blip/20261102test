var corpId = "";
var ratio,billPool;
bt.use(['form', 'layedit','billPool','moneyType','dicts','radioType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    billPool = bt.billPool.init();
    ratio = bt.radioType.init();
    form.verify({

    });

    //是否票据入池
    form.on('radio(isIntoPool)', function(data){
        if($('input:radio[name="isIntoPool"]:checked').val()=="0"){
            billPool.get("bpId").val(" ");//清除票据池值
            $("#poolDiv").hide();//票据池隐藏
            $("#poolNameDiv").hide();//票据池名称隐藏
        }else{
            $("#poolDiv").show();//票据池显示
            $("#poolNameDiv").show();//票据池名称显示
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var ids = $("#ids").val();//待签收ids
        data.field.id=ids;
        var bpId = billPool.get("bpId").val();
        if($('input:radio[name="isIntoPool"]:checked').val()=="1" && (bpId==null || bpId=="" || bpId==" ")){
            layer.alert(bt.lang('billPool.Sign.bdIdIsNotNull'), {icon: 7});
            return false;
        }
        bt.loading.show();
        var action = "/billPool/abSign/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                parent.layer ? parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
        return false;
    });
});

//票据池回调函数
function poolCallback(data,text,selectedData,bv,value){
    var bifCodes= new Array();
    bifCodes = $("#bifCode").val().split(",");
    for (i=0;i<bifCodes.length ;i++ )
    {
       if(bifCodes[i]!=selectedData.bifCode){
           layer.alert(bt.lang('billPool.Sign.bifCodeIsMismatch'), {icon: 7});
           billPool.get("bpId").val(" ");//清除票据池值
           return;
       }
    }
}


