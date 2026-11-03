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

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == "null") {
            return true;
        }
        return false;
    }
//关闭窗口
    doClose=function () {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/finacial/check/fcautocheckconfig/modify"
        var oldcheckType=$("#oldcheckType").val();
        var oldvalidSign=$("#oldvalidSign").val();
        var size=$("#size").val();
        if(size!=0) {
            if (oldcheckType != data.field.checkType) {
                layer.confirm(bt.lang("当前配置已设关联单位，若修改对账类型，则清空该配置的所有关联单位！"),
                    {
                        icon: 3,
                        btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
                    }, function () {
                        if (oldvalidSign == "1" && data.field.validSign == "0") {
                            layer.confirm(bt.lang("当前配置已设关联单位，若设置为无效，则清空该配置的所有关联单位！"), {
                                    icon: 3,
                                    btn: [bt.lang("common.button.confirm"), bt.lang("common.button.cancel")]
                                }, function () {
                                    topost(data, action);
                                }
                                , function () {
                                })
                        } else {
                            topost(data, action);//没有从有效改为无效
                        }
                    }, function () {
                    }
                );
            } else {
                topost(data, action);//类别没改变
            }
        }else {
            topost(data, action);//没有关联单位的时候
        }
       return false;
    });

    function topost(data,action) {
            bt.loading.show();
            $.post(basePath + action, data.field, function (result) {
                parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    if (!bt.navtab) {
                        location.reload();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        if (data.field.id == "") {
                            bt.navtab.refreshCurrent();
                        } else {
                            bt.navtab.closeCurrent();
                            bt.navtab.refreshCurrent();
                        }

                    }
                } else {
                    bt.loading.hide();
                }
            }, 'JSON')
        }

});







