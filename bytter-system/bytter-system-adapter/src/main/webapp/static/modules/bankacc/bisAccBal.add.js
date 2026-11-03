var balAdd=10000;//用于同一个账号新增多条数据

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
     * 暂存/提交按钮
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
        var action = "/bankacc/bisaccbal/save";
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

    var sysDate = dateType.get('sysDate').val();
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
        //新增标志：0-表银行账户余额管理新增 1-表银行账户流水管理新增
        var addFlag = $("#addFlag").val();
        if (addFlag == "1"){
            bt.bttable.init({
                elem: "#bankAccInfo",
                url: basePath + "/bankacc/bisaccbal/getBankAccByAccount",
                urldata: data,
                checkbox: true,
                headerCheckbox:true,
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
        } else {
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
                        data: "bal",
                        className: "textRight",
                        render: function (data, row) {
                            return '<input type="text" data-name="accBal" name="accBal" id="accBal' + row + '" class="layui-input bt-biz-moneyType"  data-minus="true"  data-type="number" autocomplete="off"/>';
                        }
                    },
                    {
                        data: "availBal",
                        className: "textRight",
                        render: function (data, row) {
                            return '<input type="text" data-name="accAvailBal" name="accAvailBal" id="accAvailBal' + row + '" class="layui-input bt-biz-moneyType"  data-minus="true" data-moneyendid="accBal' + row + '" data-type="number" autocomplete="off"/>';
                        }
                    },
                    {
                        data: "balDate",
                        render: function (data, row) {
                            return ' <input type="text" name="balDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="balDate" lay-verify="date" data-today="true" data-max="' + sysDate + '">'
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
                    },
                    {
                        data: null, className: 'center',
                        render: function (data, row) {
                            var html = '<a href="javascript:void(0);" class="add-data">' + bt.lang("common.button.add") + '</a>';
                            return html;
                        }
                    }
                ],
                tableInitCallback: function () {
                    bt.dateType.init();
                    bt.moneyType.init();
                }
            });
        }
    }

    $(document).on('click', ".delete-data", function () {
        $(this).parents('tr').remove();
    });
    $(document).on('click', ".add-data", function () {
        let oTr = $(this).parents('tr');
        var oTr2 = oTr.clone();
        oTr2.find('input[data-name=accBal]').remove();
        oTr2.find('input[data-name=accAvailBal]').remove();
        oTr2.find('input[name=balDate]').remove();
        oTr2.find('td:eq(2)').find('input').remove();
        oTr2.find('td:eq(3)').find('input').remove();
        oTr2.find('td:eq(4)').find('input').remove();
        oTr2.find('td:eq(2)').append('<input type="text" data-name="accBal" name="accBal" id="accBal' + balAdd + '" class="layui-input bt-biz-moneyType"  data-minus="true"  data-type="number" autocomplete="off" data-required="true" />');
        oTr2.find('td:eq(3)').append('<input type="text" data-name="accAvailBal" name="accAvailBal" id="accAvailBal' + balAdd + '" data-moneyendid="accBal' + balAdd + '" class="layui-input bt-biz-moneyType"  data-minus="true" data-type="number" autocomplete="off"/>');
        oTr2.find('td:eq(4)').append('<input type="text" name="balDate" class="layui-input bt-biz-dateType" data-type="date" data-biz-name="balDate" lay-verify="date" data-required="true" data-max="' + sysDate + '"/>');
        if(oTr2.find('td:last-child').find('a').length!=2){
            oTr2.find('td:last-child').find('a').append('<span class="text-explode">|</span><a href="javascript:void(0);" class="delete-data">' + bt.lang("common.button.delete") + '</a>')
        }
        oTr.after(oTr2);
        balAdd++;
        bt.dateType.init();
        bt.moneyType.init();
    });
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
        if (!(beginDate && endDate)) {
            layer.alert(bt.lang('table.bisaccbal.msg.dtldata'), {icon: 2}, function () {
                layer.closeAll();
                $('input[data-name="beginDate"]').addClass("layui-form-danger");
                $('input[data-name="beginDate"]').focus();
            });
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
            'endDate': endDate,
            'generateBalance': $("[name='generateBalance']:checked").val()
        });
        bt.loading.hide();
        parent.layer.confirm(bt.lang('table.bisAccBal.msg.generateBalance'), function () {
            bt.loading.show();
            $.ajax({
                type: 'post',
                url: basePath + "/bankacc/bisaccbal/saveAccBalByDtl",
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
