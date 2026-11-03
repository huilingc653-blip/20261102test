bt.use(['form', 'layedit', 'select', 'dateType', 'corp', 'datatable', 'selectTable','dicts',
    'dateForm', 'bank', 'bankInfo', 'moneyType', 'region', 'selectType','externalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.bankInfo.init();
    var selectType = bt.selectType.init();
    var region = bt.region.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        data.field.status = $(this).attr("value");
        var voucherCode = $("#sysVoucherCode").val();
        //拨付不校验黑名单
        if(voucherCode != "10"){
            //校验黑名单
            corpCheck(data);
        }else{
            submitForm(data);
        }
        return false;
    });

    //校验黑白名单
    function corpCheck(data){
        //对黑白名单进行验证
        var reData=externalCorpCheck({"name":data.field.payeeAccName,"corpId":data.field.corpId});
        if(reData.reCode=='3'){
            bt.loading.hide();
            parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
            return;
        }else if(reData.reCode=='2'){
            bt.loading.hide();
            parent.layer.confirm(reData.msg,function(){
                submitForm(data);
            });
        }else{
            submitForm(data);
        }
    }

    //提交
    function submitForm(data) {
        var action = "/bdlink/bisexc/modify";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    }

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: "0"
        });
        return true;
    }

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        $("#payeeAcc").val(data.externalAcc);
        $('#payeeAccName').val(data.name);
        $("#payeeBankCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
        $("#bankTypeId").val(data.bankType);
        //设置省市组的值
        region.get('payeeAddr').set([{
            name: 'payeeProv',
            defaultValue: data.accProvince
        }, {
            name: 'payeeCity',
            defaultValue: data.accCity
        }]);
        $('#payeeProvShow').val(data.accProvince);
        $('#payeeCityShow').val(data.accCity);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };

    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#payeeBankCode").val(data.bankCode);
        $("#payeeBank").val(data.bankName);
        //设置省市组的值
        region.get('payeeAddr').set([{
            name: 'payeeProv',
            defaultValue: data.prov
        }, {
            name: 'payeeCity',
            defaultValue: data.city
        }]);
        $('#payeeProvShow').val(data.prov);
        $('#payeeCityShow').val(data.city);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };

    //跨行标志，地域标志判断
    function difBankAndAreaSign() {
        if ($("#payerBankCode").val() != "" && $("#payeeBankCode").val() != "" &&
            $("#payerBankCode").val().substr(0, 3) == $("#payeeBankCode").val().substr(0, 3)) {
            //商业银行根据开户行名称前缀匹配
            if($("#payerBankCode").val().substr(0, 3) == "313" || $("#payerBankCode").val().substr(0, 3) == "402"
                || $("#payerBankCode").val().substr(0, 3) == "314"){
                if($("#bankNamePrefix").val()!="" && $("#payeeBank").val().indexOf($("#bankNamePrefix").val()) != -1){
                    dicts.get("difBank").val(0);
                }else{
                    dicts.get("difBank").val(1);
                }
            }else {
                dicts.get("difBank").val(0);
            }
        } else {
            dicts.get("difBank").val(1);
        }
        if ($("#payerProvShow").val() == $("#payeeProvShow").val()
            && $("#payerCityShow").val() == $("#payeeCityShow").val()) {
            dicts.get("areaSign").val(0);
        } else {
            dicts.get("areaSign").val(1);
        }
        form.render('radio');
    }

    /**
     * 单位树加载时    根据付款方式控制网点和无上级父类的单位不可选择
     * @param nodes
     * @returns {Array}
     */
    beforeInitCorp = function (nodes) {
        var newNodes = [];
        var voucherType = selectType.get("voucherTypeId").getData().text;
        for (k in nodes) {
            var node = nodes[k];
            // TransferPay 转付 payForAnother 代付 appropriation 拨付
            if (bt.lang('table.bisExc.if.voucherType.TransferPay') == voucherType || bt.lang('table.bisExc.if.voucherType.payForAnother') == voucherType) {
                if (node.pId == null || node.type == 0) {
                    node.chkDisabled = true;
                }
            }
            // else if (bt.lang('table.bisExc.if.voucherType.payForAnother') == voucherType
            //     || bt.lang('table.bisExc.if.voucherType.appropriation') == voucherType) {
            //     if (node.type == 1) {
            //         node.chkDisabled = true;
            //     }
            // }
            newNodes.push(nodes[k]);
        }
        return newNodes;
    }

    //单位回调函数
    bt.corpCallback = function (id) {
        //获取单位用途
        $.ajax({
            url: basePath + "/basic/business/btpurpose?getPurpose",
            method: 'POST',
            data: {corpId: id, sysBusType: 1},
            dataType: 'json',
            success: function (data) {
                $("#purpose").empty();
                $("#purpose").append("<option value=' '></option><option value='inputText'></option>");
                if (data && data.obj && data.obj.length > 0) {
                    $.each(data.obj, function (index, item) {
                        $("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
                    });
                } else {
                    $("#purpose").append("<option value=''></option>");
                }
                form.render();
            }
        });

        //加载收付款账号
        initAcc();
    }
    //初始化单位
    var corp = bt.corp.init();

    //银企接口回调函数
    bt.bifCodeCallback = function (data) {
        //加载收付款账号
        initAcc();
    };

    //币别回调函数
    bt.curCallback = function (data) {
        //加载收付款账号
        initAcc();
    };

    //付款方式回调函数
    bt.voucherTypeCallback = function (data) {
        if (data != "") {
            $.ajax({
                url: basePath + "/netbank/fcvouchertype?getFcVoucherType",
                method: 'POST',
                data: {id: data.id},
                dataType: 'json',
                async: true,
                success: function (result) {
                    if (result.obj) {
                        $("#sysVoucherCode").val(result.obj.sysVoucherCode);
                        //加载收付款账号
                        initAcc();
                        show();
                        var oldCorpId = corp.get("corpId").val();
                        corp.get("corpId").setUrl();
                        corp.get("corpId").val(oldCorpId);
                    }
                }
            });
        }
    };
    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/bdlink/bisexc/findBankAcc",
        urldata: {
            corpId: $("#corpIdDef").val(),
            curId: $("#curIdDef").val(),
            bifCode: $("#bifCodeDef").val(),
            voucherTypeId: $("#voucherTypeIdDef").val(),
            flag: "1"
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccName').val(data.accName);
                $('#payerBank').val(data.bankName);
                $('#counterAcc').val(data.counterAccId);
                $('#payerBankCode').val(data.bankCode);
                $('#bankNamePrefix').val(data.bankNamePrefix);
                //设置省市组的值
                region.get('payerAddr').set([{
                    name: 'payerProv',
                    defaultValue: data.prov
                }, {
                    name: 'payerCity',
                    defaultValue: data.city
                }]);
                $('#payerProvShow').val(data.prov);
                $('#payerCityShow').val(data.city);
                //判断跨行标志和地域标志
                difBankAndAreaSign();
                //重新加载收款账号
                payeeAccInit.get("payeeAccTemp").reload({
                    corpId: $("input[name='corpId']").val(),
                    voucherTypeId: $("select[name='voucherTypeId']").val(),
                    payerAcc: $('#payerAcc').val(),
                    flag: "0"
                });
            } else {
                $('input.text').val('');
            }
        }
    });

    //收款账号
    var payeeAccInit = bt.selectTable.init({
        elem: "#payeeAccTable",
        url: basePath + "/bdlink/bisexc/findBankAcc",
        urldata: {
            corpId: $("#corpIdDef").val(),
            voucherTypeId: $("#voucherTypeIdDef").val(),
            payerAcc: $('#payerAcc').val(),
            flag: "0"
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payeeAccTemp",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payeeAccTemp').val(data.bankAcc);
                $('#payeeAccName').val(data.accName);
                $('#payeeBank').val(data.bankName);
                $('#payeeBankCode').val(data.bankCode);
                //设置省市组的值
                region.get('payeeAddr').set([{
                    name: 'payeeProv',
                    defaultValue: data.prov
                }, {
                    name: 'payeeCity',
                    defaultValue: data.city
                }]);
                $('#payeeProvShow').val(data.prov);
                $('#payeeCityShow').val(data.city);
                //判断跨行标志和地域标志
                difBankAndAreaSign();
            } else {
                $('input.text').val('');
            }
        }
    });

    function show() {
        var sysVoucherCode = $("#sysVoucherCode").val();//系统结算方式
        if (sysVoucherCode == "10") {
            $("#payeeAccDiv").hide();
            $("#payeeAcc").removeAttr("lay-verify");
            $("#payeeAcc").removeAttr("name");

            $("#payeeAccTempDiv").show();
            $("#payeeAccTemp").attr("lay-verify", "required|tableData");
            $("#payeeAccTemp").attr("name", "payeeAcc");
            $("#payeeAcc").val("");
        }else{
            $("#payeeAccDiv").show();
            $("#payeeAcc").attr("lay-verify", "required");
            $("#payeeAcc").attr("name", "payeeAcc");

            $("#payeeAccTempDiv").hide();
            $("#payeeAccTemp").removeAttr("lay-verify");
            $("#payeeAccTemp").removeAttr("name");
            $("#payeeAccTemp").val("");
        }
    }

    $.ajax({
        url: basePath + "/netbank/fcvouchertype?getFcVoucherType",
        method: 'POST',
        data: {id: $("#voucherTypeIdDef").val()},
        dataType: 'json',
        async: true,
        success: function (result) {
            if (result.obj) {
                $("#sysVoucherCode").val(result.obj.sysVoucherCode);
                show();
            }
        }
    });

    function initAcc() {
        //重新加载付款账号
        payerAccInit.get("payerAcc").reload({
            corpId: $("input[name='corpId']").val(),
            curId: $("select[name='curId']").val(),
            bifCode: $("select[name='bifCode']").val(),
            voucherTypeId: $("select[name='voucherTypeId']").val(),
            flag: 1
        });
        //重新加载收款账号
        payeeAccInit.get("payeeAccTemp").reload({
            corpId: $("input[name='corpId']").val(),
            voucherTypeId: $("select[name='voucherTypeId']").val(),
            payerAcc: $('#payerAcc').val(),
            flag: 0
        });
    };

    //获取单位用途
    $.ajax({
        url: basePath + "/basic/business/btpurpose?getPurpose",
        method: 'POST',
        data: {corpId: $("input[name='corpId']").val(), sysBusType: 1},
        dataType: 'json',
        success: function (data) {
            $("#purpose").empty();
            $("#purpose").append("<option value=' '></option><option value='inputText'></option>");
            $("#purpose").append("<option selected='selected' value=" + $("#purposeDef").val() + ">" + $("#purposeDef").val() + "</option>");
            if (data && data.obj.length > 0) {
                $.each(data.obj, function (index, item) {
                    $("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
                    $("select[name='purpose']").val($("#purposeDef").val());
                });
            } else {
                $("#purpose").append("<option value=''></option>");
            }
            form.render();
        }
    });

    //付款方省回调函数
    bt.payerProvCallback = function (value, othis, options) {
        $('#payerProvShow').val(value);
        $('#payerCityShow').val("");
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };
    //付款方市回调函数
    bt.payerCityCallback = function (value, othis, options) {
        $('#payerCityShow').val(value);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };

    //收款方省回调函数
    bt.payeeProvCallback = function (value, othis, options) {
        $('#payeeProvShow').val(value);
        $('#payeeCityShow').val("");
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };
    //收款方市回调函数
    bt.payeeCityCallback = function (value, othis, options) {
        $('#payeeCityShow').val(value);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };
});
