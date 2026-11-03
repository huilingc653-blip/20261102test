bt.use(['form', 'bttable', 'corp', 'selectType', 'dateType', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    var selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#bankAcc").val(data.bankAcc);
        selectType.get("curId").val(data.curId);
    }
    var accountTable = bt.accountTable.init();

    /**
     * 选择单位回调
     * @param id
     */
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("bankAcc").setCorp(id);
    };
    bt.corp.init();

    // 监听查询按钮
    form.on('submit(search)', function (data) {
        initBankAcc(data);
        return false;
    });
});

/**
 * 加载表格数据
 * @param data
 */
function initBankAcc(data) {
    //生成列表
    bt.bttable.init({
        elem: "#appTable",
        urldata: data.field,
        checkbox: true,
        headerCheckbox:true,
        url: basePath + "/bankacc/bisaccbal/listBankAccByEnd",
        columns: [
            {
                data: "corpName",
                render: function (data, row) {
                    return '<input type="hidden" id="corpId" name="corpId" value="' + data.corpId + '"><span name="corpName" data-defaultvalue="' + data.corpName + '">' + data.corpName + '</span>';
                }
            },
            {
                data: "bankAcc",
                render: function (data, row) {
                    return '<input type="hidden" id="bankAccId" name="bankAccId" value="' + data.id + '"><span name="bankAcc" data-defaultvalue="' + data.bankAcc + '">' + data.bankAcc + '</span>';
                }
            },
            {
                data: "accName",
                render: function (data, row) {
                    return '<span name="accName" data-defaultvalue="' + data.accName + '">' + data.accName + '</span>';
                }
            },
            {
                data: "bankName",
                render: function (data, row) {
                    return '<span name="bankName" data-defaultvalue="' + data.bankName + '">' + data.bankName + '</span>';
                }
            },
            {
                data: "curName",
                render: function (data, row) {
                    return '<input type="hidden" id="curId" name="curId" value="' + data.curId + '"><span name="curName" data-defaultvalue="' + data.curName + '">' + data.curName + '</span>';
                }
            },
            {
                data: "bifCode",
                render: function (data, row) {
                    if (null == data.bifName) {
                        return '<input type="hidden" id="bifCode" name="bifCode" ><span name="bifName"></span>';
                    }
                    return '<input type="hidden" id="bifCode" name="bifCode" value="' + data.bifCode + '"><span name="bifName" data-defaultvalue="' + data.bifName + '">' + data.bifName + '</span>';
                }
            },
            {
                data: "endDtlDate",
                render: function (data, row) {
                    if (null == data.endDtlDate){
                        return '<span name="endDtlDate" data-defaultvalue=""></span>';
                    }
                    return '<span name="endDtlDate" data-defaultvalue="' + data.endDtlDate + '">' + data.endDtlDate + '</span>';
                }
            }
        ]
    });
}

/**
 * 确定按钮
 */
function toComplete() {
    var $ = bt.jquery;
    var selectData = [];
    var trs = $("#appTable").find("tr.selected");
    if (trs.length < 1) {
        layer.msg(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
        return;
    }
    // 遍历行数据
    trs.each(function (index, tr) {
        var formObj = {};
        var tds = $(tr).find("td");
        tds.each(function (i, td) {
            var $span = $(td).children("span");
            var $input = $(td).find("input");
            var $select = $(td).find("select");
            if ($input.length && !$select.length) {
                var text = $input.val();
                var name = $input.attr("name");
                formObj[name] = text;
            }
            if ($select.length) {
                var text = $select.val();
                var name = $select.attr("name");
                formObj[name] = text;
            }
            if ($span.length) {
                var text = $span.text();
                var name = $span.attr("name");
                formObj[name] = text;
            }

        });
        formObj["endDate"] = bt.dateType.get("endDate").val();
        selectData.push(formObj);
    });
    // parent.layer.confirm(bt.lang('table.bisAccBal.msg.generateBalance'),function(){
    $.ajax({
        type: 'post',
        url: basePath + "/bankacc/bisaccbal/toComplete",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            'btBankAccList': selectData
        }),
        success: function (result) {
            // result = eval("(" + result + ")");
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }
    })
    // });
}