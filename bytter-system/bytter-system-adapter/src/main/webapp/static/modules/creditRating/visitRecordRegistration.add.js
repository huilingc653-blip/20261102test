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
        var action="/credit/visitRecordRegistration/saveDetail";
        $.post(basePath + action, data.field, function (result) {
            // debugger;
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();

                if($("#update").val()=="0") {
                    var visitIds = webObj.bt.jquery("#visitIds").val();
                    //给父级选项卡页面中id为test的input赋值;
                    if (webObj.bt.jquery("#visitIds").val() == "") {
                        visitIds = result.obj
                    } else {
                        visitIds = visitIds + "," + result.obj
                    }
                    webObj.visitBtTable.reload({url:basePath + "/credit/visitRecordRegistration/detailDatatable?ids="+visitIds});
                    webObj.bt.jquery("#visitIds").val(visitIds);
                }else {
                    webObj.visitBtTable.reload();
                }
                bt.navtab.closeCurrent();
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });
})


