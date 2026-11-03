bt.use(['form', 'layedit', 'corp', 'bttable', 'moneyType', 'dateType', 'selectType', 'accountTable'], function () {
    var $ = bt.jquery, form = bt.form();
    var select = bt.selectType.init();
    var dateType = bt.dateType.init();
    bt.moneyType.init();
    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#bankAcc").val(data.bankAcc);
        select.get("curId").val(data.curId);
        select.get("bankType").val(data.bankTypeId);
    };
    var accountTable = bt.accountTable.init();

    /**
     * 选择单位回调
     * @param id
     */
    corpCallback = function (id) {
        accountTable.get("bankAcc").setCorp(id);
        select.get("curId").val("");
    };
    bt.corp.init();


    /**
     * 查询按钮
     * @returns {boolean}
     */
    search = function () {
        if (form.verifyFun("#seachTable")) {
            initPayerAccInfo();
        }
        return false;
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        accountTable.get("bankAcc").setCorp("");
    }

    /**
     * 根据选择的收款账户初始化付款账户信息列表
     */
    function initPayerAccInfo() {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            bankAcc: $("#bankAcc").val(),
            bankType: bt.selectType.get("bankType").val(),
            curId: bt.selectType.get("curId").val(),
        };
        bt.bttable.init({
            elem: "#bankAccInfo",
            url: basePath + "/bankacc/bisaccbal/getBankAccByAccount",
            urldata: data,
            checkbox: true,
            headerCheckbox: true,
            order: [1, 'desc'],
            columns: [
                {
                    data: "bankAcc",
                    render: function (data, row) {
                        return '<input type="hidden" id="accCorpId" name="accCorpId" value="' + data.corpId + '"><span class="" name="bankAcc" data-defaultvalue="' + data.bankAcc + '">' + data.bankAcc + '</span>';
                    }
                },
                {
                    data: "accName",
                    render: function (data, row) {
                        return '<input type="hidden" id="bankAccId" name="bankAccId" value="' + data.id + '"><span class="" name="accName" data-defaultvalue="' + data.accName + '">' + data.accName + '</span>';
                    }
                },
                {
                    data: "bankName",
                    render: function (data, row) {
                        return '<input type="hidden" id="accBifCode" name="accBifCode" value="' + data.bifCode + '"><span class="" name="bankName" data-defaultvalue="' + data.bankName + '">' + data.bankName + '</span>';
                    }
                },
                {
                    data: "curId",
                    render: function (data, row) {
                        return '<input type="hidden" id="accCurId" name="accCurId" value="' + data.curId + '"><span class="" name="curName" data-defaultvalue="' + data.curName + '">' + data.curName + '</span>';
                    }
                }
            ]
        });
    }
    /**
     * 由明细生成余额
     * @returns {boolean}
     */
    toBuildBal = function () {
        bt.loading.show();
        var selectData = [];
        var trs = $("#bankAccInfo").find("tr.selected");
        if (trs.length < 1) {
            bt.loading.hide();
            layer.alert(bt.lang('table.fcBankBill.msg.chooseLeastOne'), {icon: 2});
            return false;
        }
        var beginDate = bt.dateType.get("beginDate").val();
        var endDate = bt.dateType.get("endDate").val();
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
            selectData.push(formObj);
        });
        var data = JSON.stringify({
            'btBankAccList': selectData,
            'beginDate': beginDate,
            'endDate': endDate
        });
        bt.loading.hide();
        parent.layer.confirm(bt.lang('table.bisAccBal.msg.updateBal'), function () {
            bt.loading.show();
            $.ajax({
                type: 'post',
                url: basePath + "/bankacc/btbankaccdtl/flushBal",
                contentType: 'application/json; charset=UTF-8',
                data: data,
                success: function (result) {
                    bt.loading.hide();
                    if(typeof result.success == "undefined"){
                        result = eval("(" + result + ")");
                    }
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                        parent.layer.closeAll();
                        if (result.success) {
                            if (bt.navtab) {
                                bt.navtab.refreshCurrent()
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                    });
                }
            })
        });
    }
});
