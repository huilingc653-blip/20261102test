bt.use(['form', 'dateType', 'moneyType', 'dicts', 'corp', 'select'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();

    //查询
    $("#search").click(function(){
        var action =basePath + "/budget/fbsbudgetanalyse/analysequery";
        var queryDate = dateType.get("queryDate").val();
        if(queryDate == "") {
            layer.alert(bt.lang("fbs.budget.message.querydate.null"), {icon: 2});
            return false;
        }
        $("#inputForm").attr("action",action);
        $("#inputForm").submit();
    });

    exports = function () {
        var url = basePath + "/budget/fbsBudgetDownloadAndImport/exportFact?id=" + $('#id').val()
        window.location.href = url;
    };
});
