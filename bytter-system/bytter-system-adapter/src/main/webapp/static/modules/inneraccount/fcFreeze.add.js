bt.use(['selectTree','form', 'layedit', 'dateType', 'moneyType', 'corp', 'money','selectTable'], function () {
    var $ = bt.jquery;
    var mtype = bt.moneyType.init();
    bt.dateType.init();
    bt.corp.init();
    var form = bt.form();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var accState = $(this).attr("value");
        data.field.status = accState;

        if (parseFloat(bt.money.toNumber($("#freezeMoney").val())) > parseFloat(bt.money.toNumber($("#activeMoney").val()))) {
            bt.loading.hide();
            layer.msg(bt.lang("innerAccount.freeze.money.compare"));
            return false;
        }
        layer.confirm(validFrom(), {
            btn: [bt.lang("common.button.submit"), bt.lang("common.button.close")] //按钮
        }, function (i) {
            var action = "/inneraccount/fcfreeze/save";
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
            layer.close(i);
        }, function (i) {
            bt.loading.hide();
        });


        return false;
    });

    function validFrom() {
        var ask = bt.lang("innerAccount.freeze.confirm");
        return ask;
    }

    function setForm(data) {
        $("#curName").val(data.curName);
        $("#accName").val(data.accName);
        mtype.get("activeMoney").val(data.activeMoney);
        mtype.get("accuFreezeMoney").val(data.freezeMoney);

        $("#curNameSpan").text(data.curName);
        $("#accNameSpan").text(data.accName);
        // 可冻结金额小于0时,仅优化界面的显示值
        if (data.activeMoney < 0) {
            $("#activeMoneySpan").text(bt.money.format(0));
        } else {
            $("#activeMoneySpan").text(bt.money.format(data.activeMoney));
        }
        $("#accuFreezeMoneySpan").text(bt.money.format(data.freezeMoney));

        //动态添加历史冻结记录
        $("#dyncTable tbody").html("");
        $.ajax({
            url: basePath + "/inneraccount/fcfreeze/getHisListByAccount/" + data.blnctrAcc + "?tk=" + (new Date()).getTime(),
            method: 'GET',
            dataType: 'json',
            async: false,
            success: function (data) {
                if (data) {
                    $.each(data, function (index) {
                        $("#dyncTable tbody").append("<tr id='option" + index + "'><td>" + (data[index].freezeNo == null ? "" : data[index].freezeNo) + "</td>"
                            + "<td>" + bt.date.format(data[index].beginDate, "yyyy-mm-dd") + "</td>"
                            + "<td>" + bt.date.format(data[index].endDate, "yyyy-mm-dd") + "</td>"
                            + "<td><span id='" + index + "freezeMoney' class='bt-biz-moneyType' data-name='" + index + "freezeMoney'>" + data[index].freezeMoney + "</span></td>"
                            + "<td>" + data[index].freezeTypeName + "</td>"
                            + "<td>" + (data[index].freezeReason == null ? "" : data[index].freezeReason) + "</td></tr>");
                        mtype.init();
                    });
                }
            }
        });
    }

    bt.blnctrAccTable = bt.selectTable.init({
        elem: "#blnctrAccTable",
        url: basePath + "/inneraccount/fcfreeze/getTableData",
        searchInput: "#blnctrAcc",
        scrollX: true,
        scrollY: "200px",
        columns: [
            {data: "blnctrAcc",},
            {data: "curName",},
            {data: "accName",},
            {
                data: "activeMoney", render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.activeMoney, {separator: ",", decimal: 2});
                }
            },
            {
                data: "freezeMoney", render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.freezeMoney, {separator: ",", decimal: 2});
                }
            },
        ],
        clickCallback: function (data) {
            $("#blnctrAcc").val(data.blnctrAcc);
            setForm(data);
        }
    });


    form.on('select(freezeType)', function (data) {
        var selectedObj = $("#blnctrAcc").find("option:selected");
        if (selectedObj.val() == '') {
            $(data.elem).val('');
            layer.msg(bt.lang('innerAccount.freeze.account.selected'));
            form.render('select');
            return;
        } else {
            if (data.value == 'A' || data.value == 'S') {
                mtype.get("freezeMoney").val(bt.money.toNumber($("#activeMoney").val()));
                //$("[data-id='freezeMoney']").val(bt.money.toNumber($("#activeMoney").val()));
                //$("#freezeMoney").val(bt.money.toNumber($("#activeMoney").val()));
                $("[data-id='freezeMoney']").attr("readonly", "readonly");
                // bt.moneynum.init({elem:'#freezeMoney',decimal:2});
            } else {
                // $("[data-id='freezeMoney']").val("");
                mtype.get("freezeMoney").val('');
                $("[data-id='freezeMoney']").removeAttr("readonly");
            }
        }
    });

});

// 重置
function resetFields() {
    // 重置掉币别、账户名称、可冻结金额、累计冻结金额数据
    $("#curNameSpan").text('');
    $("#curName").val('');

    $("#accNameSpan").text('');
    $("#accName").val('');

    $("#activeMoneySpan").text('');
    $("#activeMoneySpan").siblings('input').val('');

    $("#accuFreezeMoneySpan").text('');
    $("#accuFreezeMoneySpan").siblings('input').val('');
}

//单位下拉框回调
function corpChange(obj) {
    $("#blnctrAcc").val('');

    var urldata = {
        corpId: obj.id
    };
    bt.blnctrAccTable.get("blnctrAcc").reload(urldata);

    // $("#blnctrAcc").empty();
    // $.ajax({
    //     url: basePath + "/inneraccount/fcfreeze/getValidAccountByCorpId/" + obj.id + "?tk=" + (new Date()).getTime(),
    //     method: 'GET',
    //     dataType: 'json',
    //     async: false,
    //     success: function (data) {
    //         if (data) {
    //             $("#blnctrAcc").append("<option value=''>" + bt.lang('common.placeholder.select') + "</option>");
    //             $.each(data, function (index) {
    //                 $("#blnctrAcc").append("<option value='" + data[index].blnctrAcc + "'"
    //                     + " data-curName='" + data[index].curName + "'"
    //                     + " data-accName='" + data[index].accName + "'"
    //                     + " data-activeMoney='" + data[index].activeMoney + "'"
    //                     + " data-freezeMoney='" + data[index].freezeMoney + "'"
    //                     + ">" + data[index].blnctrAcc + "</option>");
    //             });
    //         }
    //         form.render('select');
    //     }
    // });
}
//网点下拉框回调
function netIdChange() {
    $("#blnctrAcc").val('');
}
