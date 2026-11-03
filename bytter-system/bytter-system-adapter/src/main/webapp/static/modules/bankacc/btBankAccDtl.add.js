bt.use(['form', 'layedit', 'btdate', 'select', 'dicts', 'bttable', 'dateType', 'moneyType', 'bank', 'selectTable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.bank.init({
        elem: '.bank',
    })
    var tableDtl;
    var dateType = bt.dateType.init();
    var moneyFun = bt.moneyType.getFun();

    //账号
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/bankacc/btbankacc/getListByCorpIdAndNotOnline",
        urldata: {isOnline: "0", resource: "btBankAccDtl"},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName"},
            {data: "curName"},
        ],
        clickCallback: function (data) {
            if (data) {
                $('input.text').val(data.bankAcc);
                onChangeBankAcc(data);
            } else {
                $('input.text').val('');
            }
        }
    });

    form.on('radio(cdSign)', function (data) {
        setBal();
    });

    //监听提交
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        if (isNaN(data.field.bal)) {
            bt.loading.hide();
            layer.msg(bt.lang('bt.bankaccdtl.message.amt.number'));
            return false;
        }
        //交易金额必须大于0
        if(Number(data.field.amt)==Number(0)){
            bt.loading.hide();
            layer.msg(bt.lang('nis.netbank.message.amt.mismatch'));
            return false;
        }
        var action = "/bankacc/btbankaccdtl/save";
        data.field.status = $(this).attr("value");
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    clearOppAcc();
                    // 重置交易时间
                    dateType.get("transTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
                    // 刷新明细
                    tableDtl.reload();
                    if (bt.navtab) {
                        bt.navtab.refreshParentTable();
                        // bt.navtab.refreshCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });

    function clearOppAcc() {
        bt.moneyType.get("amt").val(0);
        $("#oppAccNo").val("");
        $("#oppAccName").val("");
        $("#oppAccBank").val("");
        $("#uses").val("");
        $("#remark").val("");
        $("#remark").val("");

    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        if (isNaN(data.field.bal)) {
            bt.loading.hide();
            layer.msg(bt.lang('bt.bankaccdtl.message.amt.number'));
            return false;
        }
        //交易金额必须大于0
        if(Number(data.field.amt)==Number(0)){
            bt.loading.hide();
            layer.msg(bt.lang('nis.netbank.message.amt.mismatch'));
            return false;
        }
        var action = "/bankacc/btbankaccdtl/save";
        data.field.status = $(this).attr("value");
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });


// 根据所选本方账户加载账户其余信息
    function onChangeBankAcc(bankAcc) {
        var $ = bt.jquery, form = bt.form();
        $.ajax({
            url: basePath + "/bankacc/btbankacc?getBankAccById",
            method: 'POST',
            data: {id: bankAcc.id},
            dataType: 'json',
            success: function (result) {
                clearBanAcc();
                if (result.success == true) {
                    var data = result.obj;
                    $("#corpId").val(data.corpId);
                    $("#accId").val(data.id);
                    $("#bankAcc").val(data.bankAcc);
                    $("#accName").val(data.accName);
                    $("#bankName").val(data.bankName);
                    $("#bifCode").val(data.bifCode);
                    $("#bifName").val(data.bifName);
                    if (null != bankAcc.curId) {
                        $("#curId").val(bankAcc.curId);
                        $("#curName").val(bankAcc.curName);
                    }
                    onChangeMonths();
                    form.render();
                }
            }
        });
    }

// 清除本方账户信息
    function clearBanAcc() {
        var $ = bt.jquery, form = bt.form();
        $("#corpId").val("");
        $("#accId").val("");
        $("#bankAcc").val("");
        $("#accName").val("");
        $("#bankName").val("");
        $("#bifCode").val("");
        $("#bifName").val("");
    }

    /**
     * 日期回调
     * @param data
     */
    transTimeCallback = function (data) {
        bankDownBal(data);
    }

    /**
     * 金额回调
     * @param data
     */
    amtCallback = function (data) {
        bankDownBal(null, data);
    }


//根据选择的收款账户初始化付款账户信息列表
    onChangeMonths = function () {
        var $ = bt.jquery, form = bt.form();
        var data = {
            bankAcc: $("#bankAcc").val(),
            curId: $("#curId").val(),
            months: bt.dicts.get("months").val()
        };
        $("#submitButtonTR").attr("style", "");
        tableDtl = bt.bttable.init({
            elem: "#btBankAccDtlInfo",
            url: basePath + "/bankacc/btbankaccdtl/getTempBtBankAccDtlInfo",
            urldata: data,
            checkbox: true,
            headerCheckbox: false,
            columns: [
                {
                    data: "operation",
                    render: function (data, row) {
                        var html = '';
                        if (bt.isResource('netbank:bankAcc:btBankAccDtl:apply', data.corpId)) {
                            html += '<a class="" href="#" data-type="edit" >' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-type="del">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-type="review">' + bt.lang("common.button.view") + '</a>';
                        return html;
                    }
                },
                {
                    data: "orderNo", className: 'center', width: 60,
                },
                {
                    data: "tempAmt",
                    render: function (data, row) {
                        return '<input type="hidden" id="tempCorpId" name="tempCorpId" value="' + data.corpId + '"><span class="" name="tempBankAcc" data-defaultvalue="' + data.amt + '">' + moneyFun.format(data.amt) + '</span>';
                    }
                },
                {
                    data: "tempBal",
                    render: function (data, row) {
                        return '<input type="hidden" id="tempDtlId" name="tempDtlId" value="' + data.id + '"><span class="" name="tempBal" data-defaultvalue="' + data.bal + '">' + moneyFun.format(data.bal) + '</span>';
                    }
                },
                {
                    data: "tempTransTime",
                    render: function (data, row) {
                        return '<span class="" name="tempTransTime" data-defaultvalue="' + data.transTime + '">' + data.transTime + '</span>';
                    }
                },
                {
                    data: "tempCdSign", className: 'center', width: 80,
                    render: function (data, row) {
                        return '<span name="tempCdSign" data-defaultvalue="' + data.cdSign + '">' + bt.dict("btBankAccDtl.cdSign", data.cdSign) + '</span>';
                    }
                },
                {
                    data: "tempBankSerialNo", className: 'center', width: 80,
                    render: function (data, row) {
                        if(data.bankSerialNo=="null" || data.bankSerialNo==null){
                            data.bankSerialNo=""
                        }
                        return '<span name="tempBankSerialNo" data-defaultvalue="' + data.bankSerialNo + '">' +data.bankSerialNo + '</span>';
                    }
                },
                {
                    data: "tempOppAccNo",
                    render: function (data, row) {
                        if (null == data.oppAccNo) {
                            return '<span name="tempOppAccNo" data-defaultvalue=""></span>';
                        }
                        return '<span name="tempOppAccNo" data-defaultvalue="' + data.oppAccNo + '">' + data.oppAccNo + '</span>';
                    }
                },
                {
                    data: "tempOppAccName",
                    render: function (data, row) {
                        if (null == data.oppAccName) {
                            return '<span name="tempOppAccName" data-defaultvalue=""></span>';
                        }
                        return '<span name="tempOppAccName" data-defaultvalue="' + data.oppAccName + '">' + data.oppAccName + '</span>';
                    }
                },
                {
                    data: "tempOppAccBank",
                    render: function (data, row) {
                        if (null == data.oppAccBank) {
                            return '<span name="tempOppAccBank" data-defaultvalue=""></span>';
                        }
                        return '<span name="tempOppAccBank" data-defaultvalue="' + data.oppAccBank + '">' + data.oppAccBank + '</span>';
                    }
                },
                {
                    data: "tempAccNo",
                    render: function (data, row) {
                        return '<span name="tempAccNo" data-defaultvalue="' + data.accNo + '">' + data.accNo + '</span>';
                    }
                }
            ],
            checkChildCallback: function (data, $tr) {
            },
            reviewRowCallback: function (data, $tr, id) {
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/bankacc/btbankaccdtl/view/" + id
                });
            },
            editRowCallback: function (data, $tr, id) {
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/bankacc/btbankaccdtl/update?id=" + id,
                    end: function () {
                        tableDtl.reload();
                        bankDownBal(null, null);
                    }
                });
            },
            delRowCallback: function (data, $tr, id) {
                parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                    $.post(basePath + "/bankacc/btbankaccdtl/delete/" + id, function (result) {
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            tableDtl.reload();
                            bankDownBal(null, null);
                        });
                    }, 'JSON');
                });
            }
        });

        bankDownBal();
    }

    /**
     * 全选按钮
     */
    onCheckAll = function () {
        var $ = bt.jquery, form = bt.form();
        $("#btBankAccDtlInfo").find("tr").find('input[type="checkbox"]').prop("checked", "checked");
        $("#btBankAccDtlInfo").find('tbody tr').addClass("selected selectedCss");
        form.render('checkbox');
    }

    /**
     * 取消全选按钮
     */
    onUnCheckAll = function () {
        var $ = bt.jquery, form = bt.form();
        $("#btBankAccDtlInfo").find("tr").find('input[type="checkbox"]').prop("checked", "");
        $("#btBankAccDtlInfo").find('tbody tr').removeClass("selected selectedCss");
        form.render('checkbox');
    }

    /**
     * 提交按钮
     */
    onSubmit = function () {
        bt.loading.show();
        var $ = bt.jquery;
        var trs = $("#btBankAccDtlInfo").find("tr.selected");
        if (trs.length < 1) {
            bt.loading.hide();
            layer.msg(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
            return;
        }

        var selectData = [];
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
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
            bt.loading.show();
            $.ajax({
                type: 'post',
                url: basePath + "/bankacc/btbankaccdtl/batchSubmitTempDtl",
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({
                    'tempBtBankAccDtlList': selectData
                }),
                success: function (result) {
                    var data = result;
                    if (typeof data == "string") {
                        data = eval("(" + result + ")");
                    }
                    bt.loading.hide();
                    parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}, function () {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        if (bt.navtab) {
                            bt.navtab.refreshParent();
                        }
                        //清空内容重新加载数据
                        $("#btBankAccDtlInfo").find('tbody').html('<tr><td colspan="11" align="center">数据加载中...</td></tr>');
                        tableDtl.reload();
                    });

                }
            })
        });
    }

    /**
     * 调整按钮
     */
    onAdjust = function () {
        // bt.loading.show();
        var trs = $("#btBankAccDtlInfo").find("tr.selected");
        if (trs.length < 1) {
            bt.loading.hide();
            layer.msg(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
            return;
        }
        if (checkAdjust(trs)) {
            var data = {
                id: $("#adjustIds").val(),
                transTime: $("#adjustTransTime").val()
            }
            parent.layer.open({
                type: 2,
                area: ['1250px', '530px'],
                maxmin: true,
                content: basePath + "/bankacc/btbankaccdtl/adjust?id=" + data.id + "&transTime=" + data.transTime,
                end: function () {
                    bt.loading.hide();
                    $("#adjustTransTime").val("");
                    $("#adjustIds").val("");
                    tableDtl.reload();
                }
            });
        }
    }

    /**
     * 调整前检查是否为同一交易时间
     * @param trs
     * @returns {boolean}
     */
    checkAdjust = function (trs) {
        $("#adjustTransTime").val("");
        $("#adjustIds").val("");
        var flag = true;
        trs.each(function (index, tr) {
            // 只能选择同一交易时间的流水
            var tempTransTime = $(tr).find("span[name='tempTransTime']").text();
            var tempDtlId = $(tr).find("input[name='tempDtlId']").val();
            var oldAdjustTransTime = $("#adjustTransTime").val();
            if ("" == oldAdjustTransTime) {
                $("#adjustTransTime").val(tempTransTime);
                $("#adjustIds").val(tempDtlId);
            } else {
                if (oldAdjustTransTime != tempTransTime) {
                    layer.alert(bt.lang("table.btbankaccdtl.msg.transtime.noteq"), {icon: 7});
                    flag = false;
                    return false;
                } else {
                    $("#adjustIds").val($("#adjustIds").val() + "," + tempDtlId);
                }
            }
        });
        return flag;
    }

    bt.dicts.init();
    var moneyType = bt.moneyType.init();

    /**
     * 查询最近一天的银行行账号余额
     * @param transTime
     */
    function bankDownBal(transTime, amtVal) {
        var amt = amtVal ? amtVal : moneyType.get("amt").val();
        var data = {
            bankAcc: $("#bankAcc").val(),
            curId: $("#curId").val(),
            transTime: transTime ? transTime : bt.dateType.get("transTime").val(),
            cdSign: $('input:radio[name="cdSign"]:checked').val(),
            rbSign: $("#rbSign").val(),
            amt: amt,
            orderNo: $("#orderNo").val()
        };
        $.ajax({
            url: basePath + "/bankacc/btbankaccdtl?returnDownBankBal",
            method: 'POST',
            data: data,
            dataType: 'json',
            success: function (result) {
                $("#soucebankBal").val(result - amt);
                bt.moneyType.get("bal").val(result);
            }
        });
    }


    /**
     * 设置余额信息
     */
    function setBal() {
        var cd_sign = $('input:radio[name="cdSign"]:checked').val();
        var rb_sign = $("#rbSign").val();
        var money = moneyType.get("amt").val();
        var soucebankBal = $("#soucebankBal").val();
        if (soucebankBal != "") {
            var bal;
            if (money == "" || money == null) {
                money = 0;
            }
            if ((cd_sign == 1 && rb_sign == 2) || (cd_sign == 0 && rb_sign == 1)) {
                bal = new Number(parseFloat(soucebankBal) - parseFloat(money)).toFixed(2);
            } else {
                bal = new Number(parseFloat(soucebankBal) + parseFloat(money)).toFixed(2);
            }
            bt.moneyType.get("bal").val(bal);
        }
    }

});
