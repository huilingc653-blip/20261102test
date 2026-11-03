var dicts;
bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp", "dateType"], function () {
    var $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间
    bt.moneyType.init();
    corp = bt.corp.init();//单位

    //监听提交
    form.on('submit', function (data) {
        var status = $(this).attr("value");
        var action = "/cms/afterLease/refund/save";
        data.field.status = status;//提交状态
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (!bt.navtab) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            }
        }, 'JSON');
        return false;
    });

    //关闭窗口
    doClose = function () {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
});

function compelDate() {
    var refundDate = bt.dateType.get("refundDate").val();  //退期日期
    var endDate = $("#endDate").val()  //到期日期
    var expendedDate = $("#expendedDate").val()  //展期到期日期
    if (expendedDate === "") {
        if (refundDate < endDate) {
            dicts.get('isAdvanceBack').val(1);
        } else {
            dicts.get("isAdvanceBack").val(0);
        }
    } else {
        if (refundDate < expendedDate) {
            dicts.get('isAdvanceBack').val(1);
        } else {
            dicts.get("isAdvanceBack").val(0);
        }
    }
}
