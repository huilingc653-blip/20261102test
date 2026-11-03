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
        var action =basePath + "/budget/fbsbudgetanalyse/salescheckquery";
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
        $('form').find('input[name="checkDetailId"]:checked').each(function () {
            childIds.push($(this).val());
        });
        if (childIds) {
            data.field.childIds = childIds;
            data.field.passFlag = $(this).attr("value");
        }
        if (data.field.childIds != "") {
            var msg = "common.message.confirm.batchadopt";
            if($(this).attr("value") == "1"){
                msg = "common.message.confirm.batchBack";
            }
            parent.layer.confirm(bt.lang(msg), function () {
                var action = "/budget/fbsbudgetanalyse/batchsalescheck";
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
        var corpId = $("input[name='corpId']").val();
        var queryDate = dateType.get("queryDate").val();
        var url = basePath + "/budget/fbsBudgetDownloadAndImport/exportSales?corpId=" + corpId+"&queryDate="+queryDate;
        window.location.href = url;
    };
});

//审核
function checkSubmit(id,passFlag){
    $ = bt.jquery,form = bt.form();
    var msg = "common.message.confirm.adopt";
    if(passFlag == "1"){
        msg = "common.message.confirm.back";
    }
    parent.layer.confirm(bt.lang(msg),function(){
        $.post(basePath + "/budget/fbsbudgetanalyse/saleschecksave/" + id+"/"+ passFlag, function(result){
            location.reload(true);
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}