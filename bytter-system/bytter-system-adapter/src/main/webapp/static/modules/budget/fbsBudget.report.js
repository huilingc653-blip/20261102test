bt.use(['form','corp','dateType','selectType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    var dateType = bt.dateType.init();
    var selectType = bt.selectType.init();
    var dicts = bt.dicts.init();

    seach = function(){
        if("" === corp.get("corpId").val()){
            parent.layer.alert(bt.lang("fbs.budgetReport.message.corp.null"), {icon: 2});
            return false;
        }
        var url = basePath + "/budget/fbsbudgetreport/seachFbsBudget";
        url += "?corpId=" + corp.get("corpId").val() + "&timeType=" + selectType.get("timeType").val();
        url += "&seachBeginDate=" + dateType.get("beginDate").val()+ "&seachEndDate=" + dateType.get("endDate").val();
        url += "&viewType=" + dicts.get("viewType").val();
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url
        });
    }
});
