bt.use(['form', 'dateType', 'moneyType', 'treeTable', 'dicts', 'corp'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();
    bt.treeTable.init({elem: "#appTable", column: 1});

    //查询
    $("#search").click(function(){
        var action =basePath + "/budget/fbsbudgetanalyse/factquery";
        var corpId = $('input[name="corpId"]').val();
        if(corpId == "") {
            layer.alert(bt.lang("fbs.budget.message.corp.null"), {icon: 2});
            return false;
        }
        var queryDate = dateType.get("queryDate").val();
        if(queryDate == "") {
            layer.alert(bt.lang("fbs.budget.message.querydate.null"), {icon: 2});
            return false;
        }
        $("#inputForm").attr("action",action);
        $("#inputForm").submit();
    });

    //全选
    form.on('checkbox(checkAll)', function (data) {
        var child = $('input[name="checkDetailId"]');
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var childIds = [];//勾选的单位计划ID
        var diffReasons = [];//差异原因
        var factMoneys = [];//实际金额
        $('form').find('input[name="checkDetailId"]:checked').each(function () {
            childIds.push($(this).val());
        });
        if (childIds) {
            data.field.childIds = childIds;
            for(var i=0; i < childIds.length; i++){
                var diffReason = $('#diffReason_'+childIds[i]).val();
                if(diffReason == ""){
                    layer.alert(bt.lang('fbs.budget.message.diffreason.null'), {icon: 7});
                    return false;
                }
                diffReasons[i] = diffReason;
                factMoneys[i] = $('#factMoney_'+childIds[i]).val();
            }
            data.field.diffReasons = diffReasons;
            data.field.factMoneys = factMoneys;
        }
        if (data.field.childIds != "") {
            var action = "/budget/fbsbudgetanalyse/factsave";
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                $.post(basePath + action, data.field, function (result) {
                    parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        location.reload();
                    }
                }, 'JSON');
                return false;
            });
        } else {
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
            return false;
        }
    });

    //初始化单位
    var corp = bt.corp.init();

    exports = function () {
        var url = basePath + "/budget/fbsBudgetDownloadAndImport/exportFact?id=" + $('#id').val()
        window.location.href = url;
    };
});

//计划上报
function viewDiff(id,timeType,detailId){
    $ = bt.jquery,form = bt.form();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/budget/fbsbudgetanalyse/view/" + id+"/"+ timeType+"/"+detailId
    });
}