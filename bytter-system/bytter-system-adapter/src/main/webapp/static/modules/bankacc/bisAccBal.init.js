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
     * 保存按钮
     */
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var btBankAccData = [];
        var trs = $("#bankAccInfo").find("tr.selected");
        if (trs.length < 1) {
            bt.loading.hide();
            layer.alert(bt.lang('table.fcBankBill.msg.chooseLeastOne'), {icon: 2});
            return false;
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
                    bt.each($input, function () {
                        var text = $(this).val();
                        var name = $(this).attr("name");
                        if (name == undefined) {
                            name = $input.attr("data-name");
                        }
                        if (name != undefined) {
                            formObj[name] = text;
                        }
                    });
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
            })
            btBankAccData.push(formObj);
        })
        data.field.btBankAccList = btBankAccData;
        data.field.status = $(this).attr("value");
        var action = "/bankacc/bisaccbal/initBalSave";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });

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
        var sysDate = dateType.get('sysDate').val();
        bt.bttable.init({
            elem: "#bankAccInfo",
            url: basePath + "/bankacc/bisaccbal/getBankAccByAccountInit",
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
                    data: "curId",
                    render: function (data, row) {
                        return '<input type="hidden" id="accCurId" name="accCurId" value="' + data.curId + '"><span class="" name="curName" data-defaultvalue="' + data.curName + '">' + data.curName + '</span>';
                    }
                },
                {
                    data: "balDate",
                    render: function (data, row) {
                        if(data.initBalanceDate){
                            return ' <input type="text" name="balDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="balDate" data-min="' + data.regDate + '" data-max="' + sysDate + '" value="' + data.initBalanceDate + '">';
                        } else {
                            return ' <input type="text" name="balDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="balDate" data-min="' + data.regDate + '" data-max="' + sysDate + '">'
                        }
                    }
                },
                {
                    data: "bal",
                    className: "textRight",
                    render: function (data, row) {
                        if(data.initBalanceDate){
                            return '<input type="text" data-name="accBal" name="accBal" id="accBal' + row + '" value="' + data.initBalance + '" class="layui-input bt-biz-moneyType"  data-minus="true"  data-type="number" autocomplete="off"/>';
                        } else {
                            return '<input type="text" data-name="accBal" name="accBal" id="accBal' + row + '" class="layui-input bt-biz-moneyType"  data-minus="true"  data-type="number" autocomplete="off"/>';
                        }
                    }
                },
                {
                    data: "availBal",
                    className: "textRight",
                    render: function (data, row) {
                        if(data.initBalanceDate){
                            return '<input type="text" data-name="accAvailBal" name="accAvailBal" id="accAvailBal' + row + '" value="' + data.initAvailBal + '" class="layui-input bt-biz-moneyType"  data-minus="true" data-moneyendid="accBal' + row + '" data-type="number" autocomplete="off"/>';
                        } else {
                            return '<input type="text" data-name="accAvailBal" name="accAvailBal" id="accAvailBal' + row + '" class="layui-input bt-biz-moneyType"  data-minus="true" data-moneyendid="accBal' + row + '" data-type="number" autocomplete="off"/>';
                        }
                    }
                },
                {
                    data: "bankName",
                    render: function (data, row) {
                        return '<input type="hidden" id="accBifCode" name="accBifCode" value="' + data.bifCode + '"><span class="" name="bankName" data-defaultvalue="' + data.bankName + '">' + data.bankName + '</span>';
                    }
                }
            ],
            tableInitCallback: function () {
                bt.dateType.init();
                bt.moneyType.init();
            }
        });
    }

});
