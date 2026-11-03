bt.use(['form', 'layedit', 'dateType', 'btdate', 'bttable', 'dicts', 'corp', 'accountTable', 'moneyType', 'dicts', 'btUpload', 'selectType','accountInfo'], function () {
    var $ = bt.jquery, form = bt.form();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    var accountInfo = bt.accountInfo.init();
    bt.accountInfoParams = function (dom) {
        var tr = dom.parents('tr');
        var row = tr.attr("row");
        accountInfo.set({
            "corpId": $("#payeeCorpId"+row).val(),
            "curId": $("#curId"+row).val()
        });
        return true;
    }
    //内部账户选择
    bt.accountInfoCallback = function (data, dom) {
        var tr = dom.parents('tr');
        var row = tr.attr("row");
        $("#payeeCounterAcc"+row).val(data.account);
        $("#counterAccBalance"+row).text(moneyFun.format(data.availableBalance));
        $("#accBalance"+row).text(moneyFun.format(data.checkBalance));
    };

    /**
     * 选择日期回调
     * @param data
     * @param $elem
     */
    dateCallBack = function (data, $elem) {
        $.each($("input[name='money']"), function (i, item) {
            $("#budget" + i).val("")
        });
    }

    /**
     * 选择付款账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#corpId").val(data.corpId);
        $("#payerAccId").val(data.bankAccId);
        $("#payerAcc").val(data.bankAcc);
        $("#payerAccName").val(data.accName);
        initBankAccBalance(data.bankAcc, data.curId, "payerAccBalance");
        // bt.moneyType.get("payerAccBalance").val(data.balance);
        $("#payerCurId").val(data.curId);
        $("#curName").text(data.curName);
        $("#corpName").text(data.corpName);
        initFcAccBalance(data.bankAcc, data.curId, "fcAccBalance");
        initPayeeAccInfo();
        var purposeUrl = basePath + "/basic/business/btpurpose?getByList&corpId=" + data.corpId + "&sysBusType=1";
        bt.selectType.get("purpose").attr("data-url", purposeUrl)
    }

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    /**
     * 选择单位回调
     * @param data
     */
    corpCallBack = function (data) {
        initPayeeAccInfo();
    }
    bt.corp.init();

    bt.accountTable.init({
        urldata: {
            accountType: 2,
            permission: "t",
            existChilds: false,
            containParent: false,
            containDelBankAcc: false,
            accBal: false,
            searchBal: false,
            fcBal: false,
            isMustCorp:"0",
            valueFromTable:true,
            url: basePath + "/bankacc/bisaccbal/getAccountInfo"
        },
    });
    bt.dicts.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });

    /**
     * 选择收支属性回调
     * @param data
     */
    attributeCallBack = function () {
        initPayeeAccInfo();
    };
    bt.selectType.init();

    /**
     * 重置按钮回调
     */
    resetCallback = function () {
        $("#payerAccId").val("");
        $("#payerCurId").val("");
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        initPayeeAccInfo();
    }

    /**
     * 弹出资金计划页面
     * @return
     */
    $genOtherHtml = function (rowNum) {
        var param = {
            corpId: $("#corpId").val(),
            itemCode: $("#downItem").val(),
            processDate: bt.dateType.get("wishPayday").val(),
            type: 'open',
            date: (new Date()).getTime(),
            budget: $("#budget" + rowNum).val(),
            rowNum: rowNum
        };
        var url = basePath + "/budget/fbsBudgetProcess/getOpenItemDetail";
        url += "?corpId=" + param.corpId + "&itemCode=" + param.itemCode;
        url += "&processDate=" + param.processDate + "&type=" + param.type;
        url += "&date=" + param.date + "&rowNum=" + param.rowNum + "&budget=" + param.budget;
        parent.layer.open({
            type: 2,
            area: ['1000px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
            }
        });
    };

    moneyCallBack = function (value, data) {
        initSumMoney();
    }

    onSave = function (data) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            var nisAllocationData = [];
            var trs = $("#payeeInfo").children("tbody").find('.selected');
            if (trs.length < 1) {
                bt.loading.hide();
                layer.msg(bt.lang('common.message.confirm.selectData'));
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
                        if (name == "accBalance") {
                            formObj[name] = bt.money.toNumber(text);
                            ;
                        } else {
                            formObj[name] = text;
                        }
                    }
                })
                nisAllocationData.push(formObj);
            })
            data.field.nisAllocationList = nisAllocationData;
            var action = "/allocation/nisallocationreq/saveUpAndDown";
            //控制重复提交
            if($(data.elem).parent().hasClass('isSubmiting')){
                bt.loading.hide();
                return false
            }
            //增加已提交的标志
            $(data.elem).parent().addClass('isSubmiting');
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    //解除已提交的标志
                    $(data.elem).parent().removeClass('isSubmiting');
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParentTable();
                            bt.navtab.refreshCurrent();
                            //重新设置时间戳
                            var timeStamp = Date.parse(new Date());
                            $('#timeStamp').val(timeStamp);
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
        })
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        data.field.status = $(this).attr("value");
        if($("#isOpenLimit").val()=="1") {
            bankAccLimit(data);
        }else{
            submit(data);
        }
        return false;
    });

    //单位限额
    function corpLimit(data,msg){
        //获取单位本位币换算汇率
        var rate = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, corpId: data.field.corpId},
            dataType: 'json',
            success: function (da) {
                rate = bt.money.mul(data.field.money,da,2)
            }
        });
        rate = parseFloat(rate);
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:1,wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            msg += '<br>'+bt.lang('alert.over.daylimit.corp')//日累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 2){
            msg += '<br>'+bt.lang('alert.over.monthlimit.corp')//月累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        }

        if(msg.length>0){
            bt.loading.hide();
            parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info')},function (index) {
                submit(data);
            });
        }else{
            submit(data);
        }
    }

    //账号限额
    function bankAccLimit(data){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:1,wishPayDate: data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        var msg='';
        if (result=== 1) {
            msg = bt.lang('alert.over.paylimit')//单笔支付限额
        } else if(result === 2){
            msg = bt.lang('alert.over.daylimit')//日累计支付限额
        }else if(result === 3){
            msg = bt.lang('alert.over.monthlimit')//月累计支付限额
        }
        corpLimit(data,msg);
    }

    //提交
    function submit(data){
        var sumMoney = $("#sum").find('td[name="money"]').text();
        if ('' == sumMoney || moneyFun.toNumber(sumMoney) <= 0) {
            bt.loading.hide();
            layer.alert(bt.lang('nis.allocation.message.downmoneyall.gtzero'), {icon: 2}, function () {
                layer.closeAll();
                $('[data-id ="money0"] ').addClass("layui-form-danger");
                $('[data-id ="money0"] ').focus();
            });
            return false;
        } else {
            var signDataMsg = "";
            $('form').find('input[data-name="money"]').each(function () {
                if ($(this).val() != "" && moneyFun.toNumber($(this).val()) > 0) {
                    var key = $(this).attr("data-key");
                    //证书签名 数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
                    var sourceData = $('#payerAcc').val();//付款账号
                    var random = String(parseInt(Math.random() * 10000000000));//随机数
                    while (random.length < 10) {
                        random = random + '0';
                    }
                    $("#random" + key).val(random);
                    var payMoney = moneyFun.format(moneyFun.toNumber($(this).val()), {separator: ''});//金额
                    while (payMoney.length < 14) {
                        payMoney = '0' + payMoney;
                    }
                    var wishPayday = dateFun.format(dateType.get("wishPayday").val(), 'yyyyMMdd');//期望支付日期
                    sourceData += random + payMoney + wishPayday + $('#payeeAccName' + key).val() + $('#payeeAcc' + key).val();
                    $("#encrySource" + key).val(sourceData);
                    signDataMsg = batchSignData(sourceData, "caCoding" + key, "3");
                }
            });
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
        }
        var nisAllocationData = [];
        var trs = $("#payeeInfo").children("tbody").find('.selected');
        if (trs.length < 1) {
            bt.loading.hide();
            layer.msg(bt.lang('common.message.confirm.selectData'));
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
                    if (name == "accBalance") {
                        formObj[name] = bt.money.toNumber(text);
                        ;
                    } else {
                        formObj[name] = text;
                    }
                }
            })
            nisAllocationData.push(formObj);
        })
        data.field.nisAllocationList = nisAllocationData;
        data.field.attachmentId = up.getGroupId();

        var bankaccFlag = checkBankAccBal(bt.moneyType.get("payerAccBalance").val(), moneyFun.toNumber(sumMoney), "money");
        if (!bankaccFlag && $("#bankaccOver").val() == 2) {
            bt.loading.hide();
            return false;
        }
        var accountFlag = true;
        if($("#isAccount").val() == 1) {
            accountFlag = checkFcAccBal($("#fcAccBalance").val(), moneyFun.toNumber(sumMoney), "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }
        }
        if (bankaccFlag && accountFlag) {
            onSave(data);
        } else if (!bankaccFlag && accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'), function (index) {
                onSave(data);
            });
        } else if (bankaccFlag && !accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'), function (index) {
                onSave(data);
            });
        } else {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'), function (index) {
                onSave(data);
            });
        }
    }
});


//根据选择的收款账户初始化付款账户信息列表
function initPayeeAccInfo() {
    var $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    var data = {
        prentAccId: $("#payerAccId").val(),
        attributeId: bt.selectType.get("attributeId").val(),
        curId: $("#payerCurId").val(),
        corpId: bt.corp.get("payeeCorpId").val()
    };
    bt.bttable.init({
        elem: "#payeeInfo",
        checkbox: true,
        headerCheckbox: true,
        url: basePath + "/allocation/nisallocationreq/getBankAccBy",
        urldata: data,
        columns: [
            {
                data: "payeeAcc",
                render: function (data, row) {
                    return '<input type="hidden" id="payeeAccId" name="payeeAccId" value="' + data.id + '"><input type="hidden" id="payeeAcc' + row + '" name="payeeAcc" value="' + data.bankAcc + '">' + data.bankAcc;
                }
            },
            {
                data: "corpName",
                render: function (data, row) {
                    return data.corpName;
                }
            },
            {
                data: "accountName",
                render: function (data, row) {
                    return '<input type="hidden" id="budget' + row + '"  name="budget" ><input type="hidden" id="payeeAccName' + row + '" name="payeeAccName" value="' + data.accName + '">' + data.accName;
                }
            },
            {
                data: "bankName",
                render: function (data, row) {
                    return '<input type="hidden" id="curId' + row + '" name="curId" value="' + data.curId + '"><span name="payeeBankName" data-defaultvalue="' + data.bankName + '">' + data.bankName + '</span>';
                }
            },
            {
                data: "counterAccId",
                render: function (data, row) {
                    var html = '<input type="hidden" id="payeeCorpId' + row + '" name="payeeCorpId" value="' + data.corpId + '"><div class="layui-input-inline">';
                    if (null == data.counterAccId) {
                        html += '<input type="text" id="payeeCounterAcc' + row + '" name="payeeCounterAcc" style="height: 20px" readonly="readonly">';
                    } else {
                        html += '<input type="text" id="payeeCounterAcc' + row + '" name="payeeCounterAcc"  style="height: 20px" readonly="readonly" value="' + data.counterAccId + '">';
                    }
                    html += '<div class="bt-div-search"><button class="bt-biz-accountInfo" id="selAccountBut" type="button" data-callback="bt.accountInfoCallback" data-beforeClick="bt.accountInfoParams"><span class="iconfont byicon_box_search"></span></button></div></div>';
                    return html;
                }
            },
            {
                data: "counterAccBalance",
                className: "textRight",
                render: function (data, row) {
                    if (null == data.fcBalance) {
                        return '<span name="counterAccBalance" id="counterAccBalance' + row + '" data-defaultvalue="0">0.00</span>';
                    }
                    return '<span name="counterAccBalance" id="counterAccBalance' + row + '" data-defaultvalue="' + data.fcBalance + '">' + moneyFun.format(data.fcBalance) + '</span>';
                }
            },
            {
                data: "accBalance",
                className: "textRight",
                render: function (data, row) {
                    if (null == data.balance) {
                        return '<span id="accBalance' + row + '" name="accBalance" data-defaultvalue="0">0.00</span>';
                    }
                    return '<span id="accBalance' + row + '" name="accBalance" data-defaultvalue="' + data.balance + '">' + moneyFun.format(data.balance) + '</span>';
                }
            },
            {
                data: "money",
                className: "textRight",
                render: function (data, row) {
                    //证书信息
                    var cfcaInfo = '<input type="hidden" id="random' + row + '" name="random" value=""><input type="hidden" id="encrySource' + row + '" name="encrySource" value=""><input type="hidden" id="caCoding' + row + '" name="caCoding" value="">';
                    // zuoy 2018-05-31 暂不挂资金计划
                    //判断支付结算是否挂接资金计划
                    // var budgetSel = $("#budgetSel").val();
                    // if (budgetSel == "1") {
                    //     return cfcaInfo + '<input type="text" data-name="money" name="money" id="money' + row + '" data-biz-name="money' + row + '" readonly="readonly" data-callback="moneyCallBack"  onclick="$genOtherHtml(' + row + ')" data-key="' + row + '" class="layui-input bt-biz-moneyType"/>';
                    // } else {
                    return cfcaInfo + '<input type="text" data-name="money" name="money" id="money' + row + '" data-biz-name="money' + row + '"  lay-verify="number2"  data-callback="moneyCallBack" data-key="' + row + '" class="layui-input bt-biz-moneyType"/>';
                    // }
                }
            }
        ],
        tableInitCallback: function (data) {
            $.each($("input[name='money']"), function (i, item) {
                $(item).on('input propertychange', initSumMoney);
            });
            bt.moneyType.init();
            if ($("#td_check").length == 0) {
                $("#sum").prepend("<td id='td_check'>&nbsp;</td>")
            }
            initSumMoney();
        }
    });

    /**
     * 汇总下划金额
     */
    initSumMoney = function () {
        var moneySum = 0;
        var moneyFun = bt.moneyType.getFun();
        $.each($("input[data-name='money']"), function (i, item) {
            var money = moneyFun.toNumber($(this).val());
            if (null == money || "" == money || isNaN(money)) money = 0;
            moneySum += parseFloat(money);
        });
        $("#sum").find('td[name="money"]').text(moneyFun.format(moneySum));
    }
}

/**
 * 资金计划回调
 * @param sumMoney
 * @param datas
 * @param rowNum
 */
function budgetProcessCallback(sumMoney, datas, rowNum) {
    bt.moneyType.get("money" + rowNum).val(sumMoney);
    $("#budget" + rowNum).val(datas);
    initSumMoney();
}

purposeBefore = function(data){
    if($.isArray(data)){
        data.unshift({
            purpose:'',
        })
    }
    return data
}
