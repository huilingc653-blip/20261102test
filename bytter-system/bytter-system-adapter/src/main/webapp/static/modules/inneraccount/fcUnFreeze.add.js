bt.use(['form', 'jquery', 'selectTable', 'moneyType', 'dateType', 'corp', 'money'], function () {
    var $ = bt.jquery, form = bt.form();
    var mType = bt.moneyType.init();
    var dateType = bt.dateType.init();
    bt.corp.init();
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var accState = $(this).attr("value");
        data.field.status = accState;
        if (!validFrom()) {
            bt.loading.hide();
            layer.msg('解冻金额不能大于冻结金额！');
            return false;
        }
        var action = "/inneraccount/fcUnfreeze/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });

    function validFrom() {
        if (parseFloat(bt.money.toNumber($("#unFreezeMoney").val())) < parseFloat(bt.money.toNumber($("#freezeMoney").val()))) {
            return false;
        }
        return true;
    }

    bt.blnctrAccTable = bt.selectTable.init({
        elem: "#blnctrAccTable",
        url: basePath + "/inneraccount/fcUnfreeze/getTableData",
        searchInput: "#blnctrAcc",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "blnctrAcc",},
            {data: "freezeTypeName",},
            {
                data: "freezeMoney", render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.freezeMoney, {separator: ",", decimal: 2});
                }
            },
            {
                data: "unFreezeMoney", render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.unFreezeMoney, {separator: ",", decimal: 2});
                }
            },
            {data: "beginDate",},
            {data: "endDate",},
            {data: "freezeNo"}
        ],
        clickCallback: function (data) {
            $("#blnctrAcc").val(data.blnctrAcc);
            setForm(data);
        },
    });

    function setForm(data) {
        bt.clearElme(data);
        $("#unFreezeId").val(data.id);
        $("#curName").val(data.curName);
        $("#accName").val(data.accName);
        $("#freezeType").val(data.freezeType);
        $("#freezeTypeName").text(data.freezeTypeName);
        $("#unFreezeMoney").val(data.unFreezeMoney);
        $("#accuFreezeMoney").val(data.accuFreezeMoney); //累计冻结金额
        $("#freezeMoneyShow").text(bt.moneyType.getFun().format(data.unFreezeMoney, {separator: ",", decimal: 2}));
        dateType.get("beginDate").val(data.beginDate);
        dateType.get("endDate").val(data.endDate);
        $("#curNameSpan").text(data.curName);
        $("#accNameSpan").text(data.accName);
        mType.get('freezeMoney').val(bt.money.toNumber(data.unFreezeMoney));
        if ($("[data-id='freezeMoney']").length > 0) {
            $("[data-id='freezeMoney']").attr("data-max", bt.money.toNumber(data.unFreezeMoney));
        }
        if (data.freezeType == 'A' || data.freezeType == 'S') {
            mType.get('freezeMoney').attr("readonly", "readonly");
        } else {
            $("[data-id='freezeMoney']").removeAttr("readonly");
        }

    }

    //清空相关元素
    bt.clearElme = function (data) {
        if (!data) {
            $("#blnctrAcc").val("");
        }
        $("#unFreezeId").val("");
        $("#curName").val("");
        $("#accName").val("");
        $("#freezeType").val("");
        $("#freezeTypeName").text("");
        $("#freezeMoney").val("");
        $("#accuFreezeMoney").val("0"); //累计冻结金额
        $("#freezeMoneyShow").text("");
        $("#beginDate").val("");
        $("#endDate").val("");
        if ($("[data-id='unfreezeMoney']").length > 0) {
            $("[data-id='unfreezeMoney']").removeAttr("readonly");
        }
        if ($("[data-id='freezeMoney']").length > 0) {
            $("[data-id='freezeMoney']").removeAttr("data-max");
        }
    }


});

//网点下拉框回调
function netChange(obj) {
}

//单位下拉框回调
function corpChange(obj) {
    var $ = bt.jquery;
    var urldata = {
        corpId: obj.id
    };
    bt.blnctrAccTable.get("blnctrAcc").reload(urldata);
    bt.clearElme();
}

