bt.use(['form', 'layedit','dicts', 'selectTree','rateType','selectType','moneyType','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法

    //监听保存
    form.on('submit', function(data){
        bt.loading.show();
        var money=moneyType.get("invMoney").val();
        if(Number(money)==Number(0)){
            layer.alert(bt.lang('nis.netbank.message.money.mismatch'), {icon: 2});
            bt.loading.hide(); //关闭加载中
            return false;
        }
        var action="/cms/equity/info/saveDetail";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();

                if($("#update").val()=="0") {
                    var ids = webObj.bt.jquery("#ids").val();
                    //给父级选项卡页面中id为test的input赋值;
                    if (webObj.bt.jquery("#ids").val() == "") {
                        ids = result.obj
                    } else {
                        ids = ids + "," + result.obj
                    }
                    webObj.bttable.reload({url:basePath + "/cms/equity/info/detailDatatable?ids="+ids});
                    webObj.bt.jquery("#ids").val(ids);
                }else {
                    webObj.bttable.reload();
                }

                bt.navtab.closeCurrent();
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });
})

function changeInvWay() {
    var invWay=bt.dicts.init().get("invWay").val();
    if(invWay=="2"){
        $("#assetName").attr("lay-verify", "required")
    }else {
        $("#assetName").removeAttr("lay-verify")
    }
    form.renderIcon();
}


