var dateTypes,moneyTypes,selectTypes,dicts,form;
bt.use(['form','jquery','dateType','moneyType','selectType','dicts'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();


    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典

    moneyFun=bt.moneyType.getFun();
    //关闭窗口
    doClose=function () {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == "null") {
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/finacial/check/fcautocheckconfig/save"
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    if(data.field.id == ""){
                        bt.navtab.refreshCurrent();
                    }else{
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    }

                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });




});







