var select;
bt.use(['form','dicts','selectType','bttable','moneyType','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    var dict = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curEnglishCode",}
    ];
    //主账号
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/sys/accountGrant/getListBy",
        urldata: {accountType: "2,3",permission:"t",corpId:$("#corpId").val(),curId:$("#curId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $("#accId").val(data.bankAccId);
                $("#curId").val(data.curId);
                $("#bankAcc").val(data.bankAcc);
                $("#bankName").html(data.bankName);
                $("#accName").html(data.accName);
            } else {
                $('input.text').val('');
            }
        }
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        var limitTypeTemp= "";
        for(i=0;i<data.field.limitTypes.length;i++){
            if(i == 0){
                limitTypeTemp=data.field.limitTypes[i]
            }else{
                limitTypeTemp=limitTypeTemp+","+data.field.limitTypes[i]
            }
        }
        if(!bt.empty(limitTypeTemp)){
            data.field.limitTypes = limitTypeTemp;
        }
        var action = "/foreign/fexcorppool/modifyPoolAcc";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.navtab.getParentDom().bt.send();
                bt.navtab.closeCurrent();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#accId").val(data.bankAccId);
        $("#curId").val(data.curId);
        $("#bankAcc").val(data.bankAcc);
        $("#bankName").html(data.bankName);
        $("#accName").html(data.accName);
    };
});

/**
 * 限额设置回调
 * @param data
 */
typeCallBack = function(data){
    var isChecked = data.othis.hasClass('layui-form-checked');
    if("all" == data.value){
        moneyType.get("limitMoney1").val("");
        moneyType.get("limitMoney2").val("");
        moneyType.get("limitMoney3").val("");
        if(isChecked){
            moneyType.get("limitMoney1").removeAttr("readonly");
            moneyType.get("limitMoney2").removeAttr("readonly");
            moneyType.get("limitMoney3").removeAttr("readonly");
            moneyType.get("limitMoney1").attr("lay-verify","required");
            moneyType.get("limitMoney2").attr("lay-verify","required");
            moneyType.get("limitMoney3").attr("lay-verify","required");
        }else {
            moneyType.get("limitMoney1").attr("readonly","readonly");
            moneyType.get("limitMoney2").attr("readonly","readonly");
            moneyType.get("limitMoney3").attr("readonly","readonly");
            moneyType.get("limitMoney1").removeAttr("lay-verify");
            moneyType.get("limitMoney2").removeAttr("lay-verify");
            moneyType.get("limitMoney3").removeAttr("lay-verify");
        }
    }else{
        moneyType.get("limitMoney"+data.value).val("");
        if(isChecked){
            moneyType.get("limitMoney"+data.value).removeAttr("readonly");
            moneyType.get("limitMoney"+data.value).attr("lay-verify","required");
        }else {
            moneyType.get("limitMoney"+data.value).attr("readonly","readonly");
            moneyType.get("limitMoney"+data.value).removeAttr("lay-verify");
        }
    }
    form.renderIcon();
}

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}