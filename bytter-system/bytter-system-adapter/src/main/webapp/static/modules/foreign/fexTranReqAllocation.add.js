bt.use(['form','layedit','dicts', 'selectType', 'moneyType','element', 'selectTable','dateType','corp','region','allBankInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    var dateType = bt.dateType.init();

    //省回调函数
    bt.provCallback = function (value, othis, options) {
        $('#cotpProv').val(value);
        $('#cotpCity').val("");
    };
    //市回调函数
    bt.cityCallback = function (value, othis, options) {
        $('#cotpCity').val(value);
    };
    region = bt.region.init();

    bt.cotpCountryCallBack = function (data) {
        setAddress(data.id, data.namezh, '');
    }

    var allBankInfo=bt.allBankInfo.init();//客商账号组件初始化
    bt.cotpBankBeforeClick = function () {
        allBankInfo.set({
            "type": "outerBank"
        });
        return true;
    }

    /**
     * 设置地点
     * @param data
     * @param elem
     * @param bv
     */
    function setAddress(countryId, countryName,cotpAddress){
        var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + countryId;
        var requiredAddress = false;//设置地点是否必填
        var requiredAddr = false;//设置省市是否必填
        //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
        var remitType = bt.dicts.get("remitType").val();
        var bankTypeName = $("#srcBankNameDiv").text();
        if (countryName == "中国") {
            $('#addressDiv').hide();
            $('#addrDiv').show();
            if (bankTypeName == "中国银行" && remitType == "2") {
                //中国银行 且 境外账户汇款 必填
                requiredAddr = true;
            }
        } else {
            $('#addressDiv').show();
            $('#addrDiv').hide();
            if (bankTypeName == "中国银行" && remitType == "2") {
                //中国银行 且 境外账户汇款 必填
                requiredAddress = true;
            }
        }
        selectType.get("cotpAddress").attr({"data-value": cotpAddress?cotpAddress:'', "data-url": url, "data-required":requiredAddress});
        region.get('payeeAddr').set([{
            name: 'cotpProv',
            defaultValue: '',
            required:requiredAddr
        }, {
            name: 'cotpCity',
            defaultValue: '',
            required:requiredAddr
        }]);
        form.renderIcon();
        form.render();
    }

    //国内外费用承担标志回调函数
    bt.costSignCallback = function (data) {
        //国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA
        var costSign = bt.dicts.get("costSign").val();
        if (costSign == "3") {
            $("#costAccDiv").show();
            $("input[name='costAcc2']").attr("lay-verify", "required|tableData");
        } else {
            $("#costAccDiv").hide();
            $("input[name='costAcc2']").attr("lay-verify", "tableData");
        }
        var corpId = $('input[name="corpId"]').val();
        var targetCorp = $('input[name="targetCorp"]').val();
        //默认设置费用支出账户
        if(costSign == "1") {
            $('#costAccId').val($('#srcAccId').val());
            $('#costAcc').val($('#srcBankAcc').val());
            costAccInit.get("costAcc").reload({bankType: $("#srcBank").val(), corpId: corpId});
        }else if(costSign == "2") {
            $('#costAccId').val($('#targetAccId').val());
            $('#costAcc').val($('#targetBankAcc').val());
            costAccInit.get("costAcc").reload({bankType: $("#targetBank").val(), corpId: targetCorp});
        }else if(costSign == "3"){
            $('#costAccId').val($('#srcAccId').val());
            $('#costAcc').val($('#srcBankAcc').val());
            $('#costAccId2').val($('#targetAccId').val());
            $('#costAcc2').val($('#targetBankAcc').val());
            costAccInit.get("costAcc").reload({bankType: $("#srcBank").val(), corpId: corpId});
            costAcc2Init.get("costAcc2").reload({bankType: $("#targetBank").val(), corpId: targetCorp});
        }
        form.renderIcon();
    }

    //汇款类型回调函数
    bt.remitTypeCallback = function(data){
        var remitType = bt.dicts.get("remitType").val();
        var countryId = "CN";
        if ($('#srcIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#srcCountryId').val();
        }
        var bankTypeId = $('#srcBank').val();
        var bifCode = $('#srcBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, '', remitType, "other");
        form.renderIcon();
    }
    bt.dicts.init();

    bt.cotpBankCallback = function (data, dom) {
        var localClearNo = "";
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#cotpBank").val(data.bankName);
                localClearNo = data.localClearNo || "";
            } else if (data.type === "outerBank") {
                $("#cotpBank").val(data.bankName);
                localClearNo = data.localClearNo || "";
            } else {
                $("#cotpBank").val("");
            }
        } else {
            $("#cotpBank").val("");
        }
        $("#localClearNoDiv").text(localClearNo);
    }

    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }

    bt.bankPayTypeCallback = function(data) {
        var bankPayType = bt.selectType.get("bankPayType").val();
        var countryId = "CN";
        if ($('#srcIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#srcCountryId').val();
        }
        var bankTypeId = $("#srcBank").val();
        var bifCode = $('#srcBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, bankPayType, '', "payeeType");
        form.renderIcon();
    }
    var selectType = bt.selectType.init();

    //设置收款人地址
    function setTargetAddr(corpId) {
        $("#targetAddrDiv").text('');
        // 设置收款人地址
        if(corpId) {
            //获取单位信息
            $.ajax({
                url: basePath + "/sys/corp/getSysCorp",
                method: 'POST',
                data: {id: corpId},
                dataType: 'json',
                success: function (data) {
                    if (data.obj && data.obj.addressEn) {
                        $("#targetAddrDiv").text(data.obj.addressEn);
                        //交易对手所在国家地区
                        setAddress(data.obj.countryId, data.obj.countryName, data.obj.countryAddr);
                        if(data.obj.countryId){
                            selectType.get('cotpCountry').val(data.obj.countryId);
                            region.get('payeeAddr').set([{
                                name: 'cotpProv',
                                defaultValue: data.obj.province,
                                required:true
                            }, {
                                name: 'cotpCity',
                                defaultValue: data.obj.city,
                                required:true
                            }]);
                        } else {
                            selectType.get('cotpCountry').val('');
                            region.get('payeeAddr').set([{
                                name: 'cotpProv',
                                defaultValue: '',
                                required:true
                            }, {
                                name: 'cotpCity',
                                defaultValue: '',
                                required:true
                            }]);
                        }
                    }
                    form.render();
                }
            });
        }
    }
    //单位回调函数
    bt.corpCallback = function (id) {
        //重新加载源账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: '', corpId: id});
        costAccInit.get("costAcc").reload({bankType: $("#srcBank").val(), corpId: id});
        //收款方单位默认和付款方单位相同
        bt.corp.get("targetCorp").val(id);
        //设置收款人地址
        setTargetAddr(id);
        //重新加载目标账户信息
        targetBankAccInit.get("targetBankAcc").reload({curId: '', corpId: id});
        costAcc2Init.get("costAcc2").reload({bankType: $("#targetBank").val(), corpId: id});
        //加载资金计划
        renderBudget();
        //获取支付方式
        //renderBankAcc("0", id, selectType.get("curId").val(),selectType.get("proId").val());
        // 重新设置资金用途
        var capitalUseUrl = basePath + "/basic/business/btpurpose.do?getByList&sysBusType=6&corpId=" + id;
        bt.selectType.get("capitalUse").attr("data-url", capitalUseUrl);
    };
    bt.targetCorpCallback = function (id) {
        //设置收款人地址
        setTargetAddr(id);
        //重新加载目标账户信息
        targetBankAccInit.get("targetBankAcc").reload({curId: '', corpId: id});
        costAcc2Init.get("costAcc2").reload({bankType: $("#targetBank").val(), corpId: id});
    };
    //初始化单位
    var corp = bt.corp.init();

    //中国银行个性化
    function setBranchCodeForZgbank(bankTypeName) {
        var remitType = bt.dicts.get("remitType").val();
        //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
        if (remitType == "2" && bankTypeName && bankTypeName.indexOf("中国银行") != -1) {

        } else {
            selectType.get("cotpAddress").attr({"data-required":false});
            region.get('payeeAddr').set([{
                name: 'cotpProv',
                required:false
            }, {
                name: 'cotpCity',
                required:false
            }]);
        }
        form.renderIcon();
    }

    /**
     * 设置银行自定义信息
     * @param bankType 银行类型
     * @param bifCode 接口代码
     * @param countryId 国家地区
     * @param bankPayType 支付类型
     * @param remitType 汇款类型
     * @param initCustomType 初始化标识 默认-初始化全部 payeeType-只初始化收款人类型 other-只初始化其他信息
     */
    function setBankTypeCustom(bankType, bifCode, countryId, bankPayType, remitType, initCustomType) {
        // 重新支付类型
        var bankPayTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&customType=bankPayType";
        // 重新设置收款人类型
        var payeeTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&bankPayType=" + bankPayType + "&customType=payeeType";
        //获取银行类型信息
        $.ajax({
            url: basePath + "/basic/business/btbanktype/getBankType",
            method: 'POST',
            data: {bankTypeId: bankType},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (initCustomType != "payeeType" && initCustomType != "other") {
                        // 是否配置支付类型 为 1-是；支付类型应用范围 包含 2-资金调拨交易
                        if (data.isSetBankPayType == "1" && data.applayScopeBankPayType.indexOf("2") != -1) {
                            if (data.requiredBankPayType == "1") {
                                selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": true});
                            } else {
                                selectType.get('bankPayType').attr({
                                    "data-url": bankPayTypeUrl,
                                    "data-required": false
                                });
                            }
                            $(".bankPayTypeDiv").show();
                            $("#bankPayTypeTd").html(bt.lang('table.nisBillhead.bankPayType'));
                        } else {
                            selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": false});
                            $(".bankPayTypeDiv").hide();
                            $("#bankPayTypeTd").html('');
                        }
                    }

                    if (initCustomType != "other") {
                        // 是否配置收款人类型 为 1-是；收款人类型应用范围 包含 2-资金调拨交易
                        if (data.isSetPayeeType == "1" && data.applayScopePayeeType.indexOf("2") != -1) {
                            if (data.requiredPayeeType == "1") {
                                selectType.get('payeeType').attr({"data-url": payeeTypeUrl, "data-required": true});
                            } else {
                                selectType.get('payeeType').attr({"data-url": payeeTypeUrl, "data-required": false});
                            }
                            $(".payeeTypeDiv").show();
                            $("#payeeTypeTd").html(bt.lang('table.fexForeignReq.payeeType'));
                        } else {
                            selectType.get('payeeType').attr({"data-url": payeeTypeUrl, "data-required": false});
                            $(".payeeTypeDiv").hide();
                            $("#payeeTypeTd").html('');
                        }
                    }

                    if (initCustomType != "payeeType") {
                        // 是否配置其他信息模块  为 1-是；适用页面 包含 2-资金调拨交易
                        if (data.isSetOtherCustom == "1") {
                            $.ajax({
                                url: basePath + "/basic/business/btbanktype/getBankTypeOtherCustom",
                                method: 'POST',
                                data: {
                                    bankType: bankType,
                                    bifCode: bifCode,
                                    countryId: countryId,
                                    remitType: remitType,
                                    applayScope: "2"
                                },
                                dataType: 'json',
                                success: function (data) {
                                    if (data) {
                                        $("#otherInfoDiv").addClass("layui-show");
                                        $("#otherInfoDiv").removeClass("ignore-verify");
                                        $("#otherInfoItemDiv").show();
                                        //所需字段包含“capitalUse-资金用途”“rmk-备注”“addWord-附言”
                                        if (data.needField && data.needField.indexOf("capitalUse") != -1) {
                                            $("#capitalUseTd").html(bt.lang('table.fexTranReq.capitalUse'));
                                            $("#capitalUseDiv").show();
                                            if (data.requiredField && data.requiredField.indexOf("capitalUse") != -1) {
                                                bt.selectType.get('capitalUse').attr({'data-required': true});
                                            } else {
                                                bt.selectType.get('capitalUse').attr({'data-required': false});
                                            }
                                        } else {
                                            $("#capitalUseTd").html('');
                                            $("#capitalUseDiv").hide();
                                            bt.selectType.get('capitalUse').attr({'data-required': false});
                                        }
                                        if (data.needField && data.needField.indexOf("addWord") != -1) {
                                            $("#addWordTd").html(bt.lang('table.nisBillhead.addWord'));
                                            $("#addWordDiv").show();
                                            if (data.requiredField && data.requiredField.indexOf("addWord") != -1) {
                                                $("#addWord").attr("lay-verify", "required");
                                            } else {
                                                $("#addWord").removeAttr("lay-verify");
                                            }
                                        } else {
                                            $("#addWordTd").html('');
                                            $("#addWordDiv").hide();
                                            $("#addWord").removeAttr("lay-verify");
                                        }
                                        if (data.needField && data.needField.indexOf("rmk") != -1) {
                                            $("#rmkTr").show();
                                            if (data.requiredField && data.requiredField.indexOf("rmk") != -1) {
                                                $("#rmk").attr("lay-verify", "required");
                                            } else {
                                                $("#rmk").removeAttr("lay-verify");
                                            }
                                        } else {
                                            $("#rmkTr").hide();
                                            $("#rmk").removeAttr("lay-verify");
                                        }
                                        if (data.needField && data.needField.indexOf("rmkLocal") != -1) {
                                            $("#rmkLocalTr").show();
                                            if (data.requiredField && data.requiredField.indexOf("rmkLocal") != -1) {
                                                $("#rmkLocal").attr("lay-verify", "required");
                                            } else {
                                                $("#rmkLocal").removeAttr("lay-verify");
                                            }
                                        } else {
                                            $("#rmkLocalTr").hide();
                                            $("#rmkLocal").removeAttr("lay-verify");
                                        }
                                        form.renderIcon();
                                    } else {
                                        $("#otherInfoDiv").removeClass("layui-show");
                                        $("#otherInfoDiv").addClass("ignore-verify");
                                        $("#otherInfoItemDiv").hide();
                                    }
                                }
                            });
                        } else {
                            $("#otherInfoDiv").removeClass("layui-show");
                            $("#otherInfoDiv").addClass("ignore-verify");
                            $("#otherInfoItemDiv").hide();
                        }
                    }
                } else {
                    if (initCustomType != "payeeType" && initCustomType != "other") {
                        selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": false});
                        $(".bankPayTypeDiv").hide();
                        $("#bankPayTypeTd").html('');
                    }

                    if (initCustomType != "other") {
                        selectType.get('payeeType').attr({"data-url": payeeTypeUrl, "data-required": false});
                        $(".payeeTypeDiv").hide();
                        $("#payeeTypeTd").html('');
                    }

                    if (initCustomType != "payeeType") {
                        $("#otherInfoDiv").removeClass("layui-show");
                        $("#otherInfoDiv").addClass("ignore-verify");
                        $("#otherInfoItemDiv").hide();
                    }
                }
                form.renderIcon();
            }
        });
        form.render();
    }

    //源账户信息
    var srcBankAccInit = bt.selectTable.init({
        elem: "#srcBankAccTable",
        url: basePath+"/foreign/fextranreq/getSrcBankAcc",
        urldata: {curId: '', corpId: $("input[name='corpId']").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#srcBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curEnglishCode",}
        ],
        clickCallback:function(data){
            if(data){
                $('#srcBankAcc').val(data.bankAcc);
                $('#srcAccId').val(data.id);
                $('#srcAccName').val(data.accName);
                $("#srcAccNameDiv").text(data.accName);
                $('#srcBank').val(data.bankTypeId);
                $("#srcBankNameDiv").text(data.bankTypeName);
                $('#curId').val(data.curId);
                $("#curNameDiv").text(data.curEnglishCode);
                $('#srcIsDomesticBank').val(data.isDomesticBank);
                $('#srcCountryId').val(data.prov);
                $('#srcBifCode').val(data.bifCode);

                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                $("#srcCorpTd").text(data.corpName);
                $("#targetCotpBankTd").text(data.bankTypeName);
                $("#targetCotpAccNameTd").text(data.accName);
                $("#targetCotpBankAccTd").text(data.bankAcc);
                $("#targetCotpCurTd").text(data.curEnglishCode);
                $("#targetCotpTd").text(data.corpName);
                //国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA
                var costSign = bt.dicts.get("costSign").val();
                if(costSign == "1" || costSign == "3") {
                    //默认设置费用支出账户
                    $('#costAccId').val(data.id);
                    $('#costAcc').val(data.bankAcc);
                }
                //重新加载费用支出账号
                costAccInit.get("costAcc").reload({bankType: data.bankTypeId, corpId: data.corpId});

                //加载资金计划
                renderBudget();

                //现金流信息
                $("#srcCurTd").text(data.curEnglishCode);
                //收付款方币种不一致
                if(data.curId != $("#targetAccCurId").val()) {
                    //现金流信息
                    $("#targetCotpCurTd").text(data.curEnglishCode);
                    $("#srcTr").hide();
                }else{
                    $("#srcCurTd").text(data.curEnglishCode);
                    $("#targetCurTd").text(data.curEnglishCode);
                    $("#srcCotpCurTd").text(data.curEnglishCode);
                    $("#targetCotpCurTd").text(data.curEnglishCode);
                    $("#srcTr").show();
                }
                //获取支付方式
                renderBankAcc("0", $('#corpId').val(), data.curId, selectType.get("proId").val());
                //中国银行个性化
                setBranchCodeForZgbank(data.bankTypeName);
                //银行支付类型
                var countryId = "CN";
                if (data.isDomesticBank == "0") {
                    // 境外账户
                    countryId = data.prov;
                }
                //汇款类型
                var remitType = bt.dicts.get("remitType").val();
                setBankTypeCustom(data.bankTypeId, data.bifCode, countryId, '', remitType, '');

                form.render();
            }else{
                $('input.text').val('');
            }
        }
    });

    //目标账户信息
    var targetBankAccInit = bt.selectTable.init({
        elem: "#targetBankAccTable",
        url: basePath+"/foreign/fextranreq/getTargetBankAcc",
        urldata: {curId: '', corpId: $("input[name='corpId']").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#targetBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curEnglishCode",}
        ],
        clickCallback:function(data){
            if(data){
                $('#targetBankAcc').val(data.bankAcc);
                $('#targetAccId').val(data.id);
                $('#targetAccName').val(data.accName);
                $('#targetAccNameDiv').text(data.accName);
                $('#targetBank').val(data.bankTypeId);
                $("#targetBankNameDiv").text(data.bankTypeName);
                $("#targetBankCodeDiv").text(data.bankCode);
                $('#targetAccCurId').val(data.curId);
                $("#targetAccCurNameDiv").text(data.curEnglishCode);
                //本地清算号
                if (data.localClearNo) {
                    $("#localClearNoDiv").text(data.localClearNo);
                } else {
                    $("#localClearNoDiv").text('');
                }
                //现金流信息
                $("#targetBankTd").text(data.bankTypeName);
                $("#targetAccNameTd").text(data.accName);
                $("#targetBankAccTd").text(data.bankAcc);
                $("#targetCorpTd").text(data.corpName);
                $("#srcCotpBankTd").text(data.bankTypeName);
                $("#srcCotpAccNameTd").text(data.accName);
                $("#srcCotpCurTd").text(data.curEnglishCode);
                $("#srcCotpBankAccTd").text(data.bankAcc);
                $("#srcCotpTd").text(data.corpName);

                //国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA
                var costSign = bt.dicts.get("costSign").val();
                if(costSign == "2") {
                    //默认设置费用支出账户
                    $('#costAccId').val(data.id);
                    $('#costAcc').val(data.bankAcc);
                    $('#costAccId2').val(data.id);
                    $('#costAcc2').val(data.bankAcc);
                    costAccInit.get("costAcc").reload({bankType: data.bankTypeId, corpId: data.corpId});
                }else if(costSign == "3"){
                    $('#costAccId2').val(data.id);
                    $('#costAcc2').val(data.bankAcc);
                }
                //重新加载费用支出账号
                costAcc2Init.get("costAcc2").reload({bankType: data.bankTypeId, corpId: data.corpId});

                //加载资金计划
                renderBudget();

                //收付款方币种不一致
                if(data.curId != $("#curId").val()) {
                    //现金流信息
                    $("#srcCurTd").text(data.curEnglishCode);
                    $("#targetCurTd").text(data.curEnglishCode);
                    $("#srcTr").hide();
                    //获取支付方式
                    renderBankAcc("0", $('#corpId').val(), data.curId, selectType.get("proId").val());
                    form.render();
                }else{
                    $("#srcCurTd").text(data.curEnglishCode);
                    $("#targetCurTd").text(data.curEnglishCode);
                    $("#srcCotpCurTd").text(data.curEnglishCode);
                    $("#targetCotpCurTd").text(data.curEnglishCode);
                    $("#srcTr").show();
                }
            }else{
                $('input.text').val('');
            }
        }
    });

    //国家地区下拉表格
    var countryInit = bt.selectTable.init({
        elem: "#countryTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#nameZh",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='nameZh']").val(data.nameZh);
            $("input[name='countryId']").val(data.id);
        },
    });

    //交易地点下拉表格
    var tranAddrInit = bt.selectTable.init({
        elem: "#tranAddrTable",
        url: basePath + "/basic/business/syscountrycust/listAllSysCountryArea",
        searchInput: "#tranAddr",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("#tranAddrId").val(data.id);
            $("input[name='tranAddr']").val(data.nameZh);
        },
    });

    //费用支出账户信息
    var costAccInit = bt.selectTable.init({
        elem: "#costAccTable",
        url: basePath+"/foreign/fextranreq/getCostAcc",
        urldata: {bankType: $("#srcBank").val(), corpId: $("input[name='corpId']").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#costAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#costAccId').val(data.id);
                $('#costAcc').val(data.bankAcc);
            }else{
                $('input.text').val('');
            }
        }
    });
    var costAcc2Init = bt.selectTable.init({
        elem: "#costAcc2Table",
        url: basePath+"/foreign/fextranreq/getCostAcc",
        urldata: {bankType: $("#targetBank").val(), corpId: $("input[name='targetCorp']").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#costAcc2",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#costAccId2').val(data.id);
                $('#costAcc2').val(data.bankAcc);
            }else{
                $('input.text').val('');
            }
        }
    });

    //产品回调函数
    bt.proCallback = function (data) {
        var curId = $('#curId').val();
        if($('#curId').val()!=$('#targetAccCurId').val()){
            curId = $('#targetAccCurId').val();
        }
        //获取支付方式
        renderBankAcc("0", $('#corpId').val(), curId,selectType.get("proId").val());
    };

    //币别回调函数
    /*bt.curCallback = function (data) {
        //重新加载源账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: bt.selectType.get('curId').val(), corpId: $("input[name='corpId']").val()});
        costAccInit.get("costAcc").reload({bankType: $("#srcBank").val(), corpId: $("input[name='corpId']").val()});
        //重新加载目标账户信息
        targetBankAccInit.get("targetBankAcc").reload({curId: bt.selectType.get('curId').val(), corpId: $("input[name='targetCorp']").val()});
        costAcc2Init.get("costAcc2").reload({bankType: $("#targetBank").val(), corpId: $("input[name='targetCorp']").val()});

        //加载资金计划
        renderBudget();

        //现金流信息
        $("#srcCurTd").text(data.englishcode);
        $("#targetCurTd").text(data.englishcode);
        //获取支付方式
        renderBankAcc("0", $('#corpId').val(), data.id,selectType.get("proId").val());
        form.render();
    };*/

    //结算日期回调函数
    settleDateCallback = function (data, $elem){
        $("#srcSettleDateTd").text(dateType.get('settleDate').val());
        $("#targetSettleDateTd").text(dateType.get('settleDate').val());
        renderBudget();
    };

    //金额回调函数
    bt.payMoneyCallback = function(data){
        $("#srcPayMoneyTd").text('-'+moneyFun.format(moneyType.get('payMoney').val()));
        $("#targetPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
    };

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    /**
     * 获取支付方式
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     */
    function renderBankAcc(busType, corpId, curId,proId) {
        if(busType!="" && corpId!="") {
            $.ajax({
                url: basePath + "/foreign/fexcorpbankacc?getBankAcc",
                method: 'POST',
                data: {busType: busType, tranType: "3", corpId: corpId, curId: curId, proId: proId, cashFlowType: "0"},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        if(data.obj.voucherName) {
                            $("#srcPayTypeTd").text(data.obj.voucherName);
                            $("#targetPayTypeTd").text(data.obj.voucherName);
                        }else{
                            $("#srcPayTypeTd").text("");
                            $("#targetPayTypeTd").text("");
                        }
                    }
                }
            });
        }
    }

    //监听提交
    form.on('submit(submit)', function(data){
        //控制重复提交
        if($(data.elem).parent().hasClass('isSubmiting')){
            return false;
        }

        //增加已提交的标志
        $(data.elem).parent().addClass('isSubmiting');

        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        //证书签名
        var encrypt = $("#encrypt").val(); // cfca证书
        if(encrypt == 2 || encrypt == 1){ // 1 天威证书
            var signDataMsg = signData("外汇交易提交", 11,function () {
                bt.loading.show();
                var action = "/foreign/fextranreq/save";
                $.post(basePath + action,data.field,function(result){
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                        if(result.success){
                            if(bt.navtab) {
                                bt.navtab.refreshParentTable();
                                 bt.navtab.refreshCurrent();
                            }else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                        parent.layer.closeAll();
                    })
                },'JSON');
            });
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }

        }else{ // 非cfca
            var signDataMsg = signData("外汇交易提交", 11);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            bt.loading.hide();
            var action = "/foreign/fextranreq/save";
            $.post(basePath + action,data.field,function(result){
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                    if(result.success){
                        if(bt.navtab) {
                            bt.navtab.refreshParentTable();
                            bt.navtab.refreshCurrent();
                        }else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                    parent.layer.closeAll();
                })
            },'JSON');
        }

        setTimeout(function () {
            //解除已提交的标志
            $(data.elem).parent().removeClass('isSubmiting');
        },300)

        return false;
    });

    /**
     * 计划明细控制项
     */
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }
    /**
     * 收款账户值改变后回调
     */
    $("#targetAccName").change(function () {
        if (dtlColSels["payeeAccName"]) {
            renderBudget();
        }
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $('input[name="corpId"]').val();
        var itemCode = $('input[name="payItem"]').val();
        var processDate = dateType.get("settleDate").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            processDate: processDate,
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            wishPayday: processDate,
            payerAcc: $("#srcBankAcc").val(),
            payeeAccName: $("input[name=targetAccName]").val(),
            payeeAcc: $("#targetAcc").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        //收付款方币种不一致
        var curId = $("#curId").val();
        var targetAccCurId = $("#targetAccCurId").val();
        if (budgetSel == "1" && curId == targetAccCurId) {
            $genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }

    /**
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreq/addCustInfo/" + $('#tranType').val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#custMsg").html("");
                $("#custMsg").html(json);
                bt.dateType.init();
                form.renderIcon();
                form.render();
            }
        });
    }
    $genCustHtml();
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("payMoney").val(data);
}
