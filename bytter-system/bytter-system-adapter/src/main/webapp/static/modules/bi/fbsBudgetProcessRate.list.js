bt.use(['form', 'dateType', 'moneyType', 'dicts', 'corp', 'select'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();

    //查询
    form.on('submit(submit)', function(data){
        var action =basePath + "/bi/budget/processRateQuery";
        $("#inputForm").attr("action",action);
        $("#inputForm").submit();
        return false;
    });

    //初始化单位
    var corp = bt.corp.init();
});

//查看
function view(id,curId,timeType,beginDate,endDate){
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/bi/budget/processRateDtlQuery/" + id+ "/" + curId+ "/" + timeType+ "/" + beginDate +"/"+ endDate);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bi/budget/processRateDtlQuery/" + id+ "/" + curId+ "/" + timeType+ "/" + beginDate +"/"+ endDate,
        end: function () {
            datatable && datatable.draw();
        }
    });
}