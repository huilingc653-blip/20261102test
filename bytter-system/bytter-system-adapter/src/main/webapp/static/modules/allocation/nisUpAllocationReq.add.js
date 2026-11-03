bt.use(['form', 'layedit', 'select', 'bttable', 'dicts', 'dateType', 'dateForm', 'accountTable', 'moneyType', 'dicts', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    /**
     * 选择收款账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#corpId").val(data.corpId);
        $("#payeeAccId").val(data.bankAccId);
        $("#payeeAcc").val(data.bankAcc);
        $("#payeeAccName").val(data.accName);
        initBankAccBalance(data.bankAcc, data.curId, "payeeAccBalance");
        // bt.moneyType.get("payeeAccBalance").val(data.balance);
        $("#payeeCurId").val(data.curId);
        $("#curName").text(data.curName);
        $("#corpName").text(data.corpName);
        initPayerAccInfo();
        var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&busDtl=2&corpId=" + data.corpId + "&sysBusType=1";
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    }


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
    /**
     * 收支属性回调
     * @param data
     */
    attributeCallBack = function (data) {
        initPayerAccInfo();
    };
    bt.selectType.init();
    /**
     * 金额回调
     * @param value
     * @param data
     */
    moneyCallBack = function (value, data) {
        initSumMoney();
    }

    /**
     * 重置按钮回调
     */
    resetCallback = function () {
        $("#payeeAccId").val("");
        $("#payeeCurId").val("");
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        initPayerAccInfo();
    }

    var bankaccFlag = false;
    var accountFlag = false;
    var gtBal = [];
    isCheckBal = function () {
        gtBal = [];
        var accBalance = 0;
        var fcAccBalance = 0;
        var trs = $("#payeeInfo").children("tbody").find('.selected');
        var sumMoney = $("#sum").find('td[name="money"]').text();
        $.each(trs, function (index, tr) {
            var row = $(tr).attr("row");
            var upMoney = bt.moneyType.get("money" + row).val();
            $('[data-id ="money' + row + '"] ').removeClass("input-danger");
            if (upMoney) {
                accBalance += moneyFun.toNumber($("#accBalance" + row).text());
                fcAccBalance += moneyFun.toNumber($("#fcAccBalance" + row).val());
                if (upMoney > accBalance || upMoney > fcAccBalance) {
                    $('[data-id ="money' + row + '"] ').addClass("input-danger");
                }
            }
        });
        bankaccFlag = checkBankAccBal(accBalance, moneyFun.toNumber(sumMoney), "money");
        if (!bankaccFlag && $("#bankaccOver").val() == 2) {
            bt.loading.hide();
            return false;
        }
        //上划不需要考虑内部账户余额
        /*if($("#isAccount").val() == 1) {
            accountFlag = checkFcAccBal(fcAccBalance, moneyFun.toNumber(sumMoney), "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }
        }else {
            accountFlag = true;
        }*/
        accountFlag = true;
        return true;
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
                        formObj[name] = text;
                    }
                })
                nisAllocationData.push(formObj);
            })
            data.nisAllocationList = nisAllocationData;
            var action = "/allocation/nisallocationreq/saveUpAndDown";
            //控制重复提交
            if($(data.elem).parent().hasClass('isSubmiting')){
                bt.loading.hide();
                return false
            }
            //增加已提交的标志
            $(data.elem).parent().addClass('isSubmiting');
            $.post(basePath + action, data, function (result) {
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
            var trs = $("#payeeInfo").children("tbody").find('.selected');
            if (trs.length < 1) {
                bt.loading.hide();
                layer.msg(bt.lang('common.message.confirm.selectData'));
                return false;
            }
            // 遍历行数据
            trs.each(function (index, tr) {
                var fieldElem = $(tr).find('input');
                var o = {}
                bt.each(fieldElem, function(){
                    if (o[this.name] !== undefined) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push($.trim(this.value) || '');
                    } else {
                        o[this.name] = $.trim(this.value) || '';
                    }
                })
                var newField = $.extend(true, {}, data.field, o);

                if(bankAccLimit({field:newField})){
                    return false;
                }
            })
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
            data: {curId: data.field.curId, corpId: data.field.payerCorpId},
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
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.payerCorpId,temp:1,wishPayDate:data.field.wishPayday},
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
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:1,wishPayDate:data.field.wishPayday},
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
        return msg;
    }

    //提交
    function submit(data){
        var sumMoney = $("#sum").find('td[name="money"]').text();
        if ('' == sumMoney || parseFloat(sumMoney) <= 0) {
            bt.loading.hide();
            layer.alert(bt.lang('nis.allocation.message.upmoneyall.gtzero'), {icon: 2}, function () {
                layer.closeAll();
                $('[data-id ="money0"] ').addClass("layui-form-danger");
                $('[data-id ="money0"] ').focus();
            });
            return false;
        } else {
            var signDataMsg = "";
            $('form').find('input[data-name="money"]').each(function () {
                if ($(this).val() != "" && parseFloat($(this).val()) > 0) {
                    var key = $(this).attr("data-key");
                    //证书签名 数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
                    var sourceData = $('#payerAcc' + key).val();//付款账号
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
                    sourceData += random + payMoney + wishPayday + $('#payeeAccName').val() + $('#payeeAcc').val();
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
                    formObj[name] = text;
                }
            })
            nisAllocationData.push(formObj);
        })
        data.field.nisAllocationList = nisAllocationData;

        if (isCheckBal()) {
            if (bankaccFlag && accountFlag) {
                onSave(data.field);
            } else if (!bankaccFlag && accountFlag) {
                bt.loading.hide();
                parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'), function (index) {
                    onSave(data.field);
                });
            } else if (bankaccFlag && !accountFlag) {
                bt.loading.hide();
                parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'), function (index) {
                    onSave(data.field);
                });
            } else {
                bt.loading.hide();
                parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'), function (index) {
                    onSave(data.field);
                });
            }
        }
    }
});

/**
 * 根据选择的收款账户初始化付款账户信息列表
 */
function initPayerAccInfo() {
    var $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    var data = {
        prentAccId: $("#payeeAccId").val(),
        attributeId: bt.selectType.get("attributeId").val(),
        curId: $("#payeeCurId").val(),
        isOnline: "1"
    };
    bt.bttable.init({
        elem: "#payeeInfo",
        checkbox: true,
        headerCheckbox: true,
        url: basePath + "/allocation/nisallocationreq/getBankAccBy",
        urldata: data,
        columns: [
            {
                data: "payerAcc",
                render: function (data, row) {
                    return '<input type="hidden" id="payerAccId" name="payerAccId" value="' + data.id + '"><input type="hidden" id="payerAcc' + row + '" name="payerAcc" value="' + data.bankAcc + '">' + data.bankAcc;
                }
            },
            {
                data: "corpName",
                render: function (data, row) {
                    return data.corpName;
                }
            },
            {
                data: "payerAccName",
                render: function (data, row) {
                    return '<input type="hidden" id="curId" name="curId" value="' + data.curId + '"><span class="" name="payerAccName" data-defaultvalue="' + data.accName + '">' + data.accName + '</span>';
                }
            },
            {
                data: "payerBankName",
                render: function (data, row) {
                    return '<input type="hidden" id="payerCorpId" name="payerCorpId" value="' + data.corpId + '"><span name="payerBankName" data-defaultvalue="' + data.bankName + '">' + data.bankName + '</span>';
                }
            },
            {
                data: "accBalance",
                className: "textRight",
                render: function (data, row) {
                    if (null == data.balance) {
                        return '<span  id="accBalance' + row + '" name="accBalance" data-defaultvalue="0">0.00</span>';
                    }
                    return '<span  id="accBalance' + row + '" name="accBalance" data-defaultvalue="' + data.balance + '">' + moneyFun.format(data.balance) + '</span>';
                }
            },
            {
                data: "money",
                className: "textRight",
                render: function (data, row) {
                    //证书信息
                    var cfcaInfo = '<input type="hidden" id="random' + row + '" name="random" value=""><input type="hidden" id="encrySource' + row + '" name="encrySource" value=""><input type="hidden" id="caCoding' + row + '" name="caCoding" value="">';
                    return cfcaInfo + '<input type="hidden" id="fcAccBalance' + row + '" name="fcAccBalance"></input><input type="text" data-name="money" data-biz-name="money' + row + '" data-callback="moneyCallBack" id="money' + row + '" data-key="' + row + '" class="layui-input bt-biz-moneyType"/>';
                }
            }
        ],
        tableInitCallback: function (data) {
            $.each($("input[name='money']"), function (i, item) {
                $(item).on('input propertychange', initSumMoney);
            });
            for (var i = 0; i < data.length; i++) {
                initFcAccBalance(data[i].bankAcc, data[i].curId, "fcAccBalance" + i);
            }
            bt.moneyType.init();
            if ($("#td_check").length == 0) {
                $("#sum").prepend("<td id='td_check'>&nbsp;</td>")
            }
            initSumMoney();
        }
    });
}

/**
 * 汇总输入的下划金额
 */
function initSumMoney() {
    var $ = bt.jquery;
    var moneySum = 0;
    var moneyFun = bt.moneyType.getFun();
    $.each($("input[data-name='money']"), function (i, item) {
        var money = moneyFun.toNumber($(this).val());
        if (null == money || "" == money || isNaN(money)) money = 0;
        moneySum += parseFloat(money);
    });
    $("#sum").find('td[name="money"]').text(moneyFun.format(moneySum));
}

purposeBefore = function(data){
    if($.isArray(data)){
        data.unshift({
            purpose:'',
        })
    }
    return data
}
