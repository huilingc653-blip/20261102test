bt.use(['form','layedit','dicts', 'selectType', 'moneyType','element', 'selectTable','dateType','corp', 'btUpload','region','allExternalBankAcc'], function(){
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

    var allExternalAccCorp=bt.allExternalBankAcc.init();//客商账号组件初始化
    bt.cotpExternalAccBeforeClick = function () {
        allExternalAccCorp.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc",
        });
        return true;
    }

    //交易对手银行账户
    bt.cotpExternalAccCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            // 默认中国的国家ID
            var chinaId = $('#chinaId').val();
            if (data.type === "innerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
                $("#cotpEnName").val(data.ecNameEn);
                $("#cotpBankName").val(data.bank);
                $("#cotpBankAcc").val(data.externalAcc);
                //这个对应 交易对手银行账户ID
                // $("#cotpAccId").val(data.id);
                //$("#cotpAccName").val(data.externalAccName);
                if (data.externalAccName) {
					$("#cotpAccName").val(data.externalAccName);
                } else {
                    $("#cotpAccName").val(data.ecName);
                }
                $("#cotpAccCurId").val(data.curId); //账户币种
                if (isNotEmpty(data.englishCode)) {
                    $("#cotpAccCurNameDiv").text(data.englishCode);
                }
                $("#cotpAdrr").val(data.address); //交易对手地址
                /*if (isNotEmpty(data.address)) {
                    $("#cotpAdrrDiv").text(data.address);
                }*/
                /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
                $("#cotpProv").val(data.externalPro);
                $("#cotpCity").val(data.externalCity);*/
                selectType.get('cotpCountry').val(chinaId);
                $('#addressDiv').hide();
                $('#addrDiv').show();
                selectType.get("cotpAddress").attr({"data-value": '', "data-required":false});
                region.get('payeeAddr').set([{
                        name: 'cotpProv',
                        defaultValue: data.externalPro,
                        required:true
                    }, {
                        name: 'cotpCity',
                        defaultValue: data.externalCity,
                        required:true
                    }]
                );
                $('#cotpProv').val(data.externalPro);
                $('#cotpCity').val(data.externalCity);
                $("#cotpProvName").val(data.externalProName);
                $("#cotpCityName").val(data.externalCityName);
                /*if (isNotEmpty(data.country)) {
                    $("#cotpCountryDiv").text(data.country);
                }
                if (isNotEmpty(data.externalProName)) {
                    $("#cotpProvDiv").text(data.externalProName);
                }
                if (isNotEmpty(data.externalCityName)) {
                    $("#cotpCityDiv").text(data.externalCityName);
                }*/
                /*$("#countryId").val(data.countryId);//交易对手银行所在国家地区&地点
                if (isNotEmpty(data.bankCountry)) {
                    $("#nameZhBankDiv").text(data.bankCountry);
                }*/
                //交易对手银行所在国家地区&地点
                selectType.get('countryId').val(data.chinaId);
                $("#cotpSwiftCode").val(data.swiftCode);
                if (isNotEmpty(data.swiftCode)) {
                    $("#cotpSwiftCodeDiv").text(data.swiftCode);//交易对手银行SWIFT CODE
                }
                $("#localClearNo").val(data.localClearNo);
                if (isNotEmpty(data.localClearNo)) {
                    $("#localClearNoDiv").text(data.localClearNo);//本地清算号
                }
                // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
                $("#cotpAgentBank").val(data.intermediaryBank);
                $("#cotpAgentBankAcc").val(data.intermediaryBankAcc);
                $("#cotpAgentBankAddr").val(data.intermediaryBankAddress);
                if (isNotEmpty(data.intermediaryBank)) {
                    // $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
                }
                if (isNotEmpty(data.intermediaryBankAddress)) {
                    // $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
                }
                if (isNotEmpty(data.intermediaryBankAcc)) {
                    // $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
                }
                if (isNotEmpty(data.ecName)) {
                    $("#srcCotpTd").text(data.ecName);
                }
                if (isNotEmpty(data.bank)) {
                    $("#srcCotpBankTd").text(data.bank);
                }
                //现金流信息
                if (isNotEmpty(data.externalAcc)) {
                    $("#srcCotpBankAccTd").text(data.externalAcc);
                }
                if (isNotEmpty(data.externalAccName)) {
                    $("#srcCotpAccNameTd").text(data.externalAccName);
                }
            } else if (data.type === "outerBankAcc") {
                $("#oppId").val(data.externalCorpId);
                $("#cotpName").val(data.ecName);
                $("#cotpEnName").val(data.ecNameEn);
                $("#cotpBankName").val(data.bank);
                $("#cotpBankAcc").val(data.externalAcc);
                //这个对应 交易对手银行账户ID
                // $("#cotpAccId").val(data.id);
                $("#cotpAccName").val(data.externalAccName);
                $("#cotpAccCurId").val(data.curId); //账户币种
                if (isNotEmpty(data.englishCode)) {
                    $("#cotpAccCurNameDiv").text(data.englishCode);
                }
                $("#cotpAdrr").val(data.address1); //交易对手地址
                /*if (isNotEmpty(data.address1)) {
                    $("#cotpAdrrDiv").text(data.address1);
                }*/
                /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
                // $("#cotpProv").val(data.outExternalPro);
                // $("#cotpCity").val(data.outExternalCity);
                // $("#cotpProvName").val(data.outExternalProName);
                // $("#cotpCityName").val(data.outExternalCityName);
                $("#cotpProv").val('');
                $("#cotpCity").val('');*/
                selectType.get('cotpCountry').val(data.countryId);
                $('#addressDiv').show();
                $('#addrDiv').hide();
                var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.countryId;
                selectType.get("cotpAddress").attr({"data-value": data.address, "data-url": url, "data-required":true});
                region.get('payeeAddr').set([{
                        name: 'cotpProv',
                        defaultValue: ''
                    }, {
                        name: 'cotpCity',
                        defaultValue: ''
                    }]
                );
                $("#cotpProvName").val('');
                $("#cotpCityName").val('');
                /*$("#cotpProvDiv").text('');
                $("#cotpCityDiv").text('');
                if (isNotEmpty(data.country)) {
                    $("#cotpCountryDiv").text(data.country);
                }*/
                // if (isNotEmpty(data.outExternalProName)) {
                //     $("#cotpProvDiv").text(data.outExternalProName);
                // }
                // if (isNotEmpty(data.outExternalCityName)) {
                //     $("#cotpCityDiv").text(data.outExternalCityName);
                // }
                /*$("#countryId").val(data.bankOutCountryId);//交易对手银行所在国家地区&地点
                if (isNotEmpty(data.bankCountry)) {
                    $("#nameZhBankDiv").text(data.bankCountry);
                }*/
                //交易对手银行所在国家地区&地点
                selectType.get('countryId').val(data.bankOutCountryId);
                $("#cotpSwiftCode").val(data.swiftCode);
                if (isNotEmpty(data.swiftCode)) {
                    $("#cotpSwiftCodeDiv").text(data.swiftCode);//交易对手银行SWIFT CODE
                }
                $("#localClearNo").val(data.localClearNo);
                if (isNotEmpty(data.localClearNo)) {
                    $("#localClearNoDiv").text(data.localClearNo);//本地清算号
                }
                $("#cotpBankAdrr").val(data.bankAddress);
                if (isNotEmpty(data.bankAddress)) {
                    // $("#cotpBankAdrrDiv").text(data.bankAddress);//交易对手银行地址
                }
                $("#cotpAgentBank").val(data.intermediaryBank);
                $("#cotpAgentBankAcc").val(data.intermediaryBankAcc);
                $("#cotpAgentSwiftCode").val(data.intermediaryBankSwiftCode);//中转行SwiftCode

                $("#cotpAgentBankAddr").val(data.intermediaryBankAddress);
                if (isNotEmpty(data.intermediaryBank)) {
                    // $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
                }
                if (isNotEmpty(data.intermediaryBankAddress)) {
                    // $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
                }
                if (isNotEmpty(data.intermediaryBankAcc)) {
                    // $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
                }
                if (isNotEmpty(data.ecName)) {
                    $("#srcCotpTd").text(data.ecName);
                }
                if (isNotEmpty(data.bank)) {
                    $("#srcCotpBankTd").text(data.bank);
                }
                //现金流信息
                if (isNotEmpty(data.externalAcc)) {
                    $("#srcCotpBankAccTd").text(data.externalAcc);
                }
                if (isNotEmpty(data.externalAccName)) {
                    $("#srcCotpAccNameTd").text(data.externalAccName);
                }

            } else if (data.type === "corpBankAcc") {
                $("#oppId").val(data.corpId);
                $("#cotpName").val(data.corpName);
                $("#cotpEnName").val(data.corpNameEn);
                $("#cotpBankName").val(data.bankName);
                $("#cotpBankAcc").val(data.bankAcc);
                //这个对应 交易对手银行账户ID
                // $("#cotpAccId").val(data.id);
                $("#cotpAccName").val(data.accName);
                $("#cotpAccCurId").val(data.curId); //账户币种
                if (isNotEmpty(data.englishCode)) {
                    $("#cotpAccCurNameDiv").text(data.englishCode);
                }
                $("#cotpAdrr").val(data.addressEn); //交易对手地址
                /*if (isNotEmpty(data.addressEn)) {
                    $("#cotpAdrrDiv").text(data.addressEn);
                }*/
                /*$("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
                $("#cotpProv").val('');
                $("#cotpCity").val('');*/
                /*$("#cotpProvDiv").text('');
                $("#cotpCityDiv").text('');
                // $("#cotpProv").val(data.bankProv);
                // $("#cotpCity").val(data.bankCity);
                // $("#cotpProvName").val(data.bankProvName);
                // $("#cotpCityName").val(data.bankCityName);
                if (isNotEmpty(data.countryName)) {
                    $("#cotpCountryDiv").text(data.countryName);
                }*/
                // if (isNotEmpty(data.bankProvName)) {
                //     $("#cotpProvDiv").text(data.bankProvName);
                // }
                // if (isNotEmpty(data.bankCityName)) {
                //     $("#cotpCityDiv").text(data.bankCityName);
                // }
                /*$("#countryId").val(data.bankCountryId);//交易对手银行所在国家地区&地点
                if (isNotEmpty(data.bankCountryName)) {
                    $("#nameZhBankDiv").text(data.bankCountryName);
                }*/
                selectType.get('countryId').val(chinaId);
                //境内
                if (isNotEmpty(data.isDomesticBank) && data.isDomesticBank == "1") {
                    selectType.get('cotpCountry').val(chinaId);
                    $('#addressDiv').hide();
                    $('#addrDiv').show();
                    selectType.get("cotpAddress").attr({"data-value": '', "data-required":false});
                    region.get('payeeAddr').set([{
                            name: 'cotpProv',
                            defaultValue: data.accProvince,
                            required:true
                        }, {
                            name: 'cotpCity',
                            defaultValue: data.accCity,
                            required:true
                        }]
                    );

                    $("#cotpProvName").val(data.accProvinceName);
                    $("#cotpCityName").val(data.accCityName);
                } else if (isNotEmpty(data.isDomesticBank) && data.isDomesticBank == "0") {
                    selectType.get('cotpCountry').val(data.countryId);
                    $('#addressDiv').show();
                    $('#addrDiv').hide();
                    var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.accProvince;
                    selectType.get("cotpAddress").attr({"data-value": data.accCity, "data-url": url, "data-required":true});
                    region.get('payeeAddr').set([{
                            name: 'cotpProv',
                            defaultValue: '',
                            required:false
                        }, {
                            name: 'cotpCity',
                            defaultValue: '',
                            required:false
                        }]
                    );
                    $("#cotpProvName").val('');
                    $("#cotpCityName").val('');
                    if (isNotEmpty(data.bankCode)) {
                        $("#cotpSwiftCodeDiv").text(data.bankCode);//交易对手银行SWIFT CODE
                        $("#cotpSwiftCode").val(data.bankCode);
                    }
                }
                $("#localClearNo").val(data.localClearNo);
                if (isNotEmpty(data.localClearNo)) {
                    $("#localClearNoDiv").text(data.localClearNo);//本地清算号
                }
                $("#cotpBankAdrr").val(data.bankAddress);
                if (isNotEmpty(data.bankAddress)) {
                    // $("#cotpBankAdrrDiv").text(data.bankAddress);//交易对手银行地址
                }
                // if (isNotEmpty(data.intermediaryBank)) {
                //     $("#cotpAgentBankDiv").text(data.intermediaryBank);//交易对手代理行
                // }
                // if (isNotEmpty(data.intermediaryBankAddress)) {
                //     $("#cotpAgentBankAddrDiv").text(data.intermediaryBankAddress);//交易对手代理行地址
                // }
                // if (isNotEmpty(data.intermediaryBankAcc)) {
                //     $("#cotpAgentBankAccDiv").text(data.intermediaryBankAcc);//交易对手代理行账户
                // }
                if (isNotEmpty(data.corpName)) {
                    $("#srcCotpTd").text(data.corpName);
                }
                if (isNotEmpty(data.bankName)) {
                    $("#srcCotpBankTd").text(data.bankName);
                }
                //现金流信息
                if (isNotEmpty(data.bankAcc)) {
                    $("#srcCotpBankAccTd").text(data.bankAcc);
                }
                if (isNotEmpty(data.accName)) {
                    $("#srcCotpAccNameTd").text(data.accName);
                }
            }
        } else {
            $("#oppId").val('');
            $("#cotpName").val('');
            $("#cotpEnName").val('');
            $("#cotpBankName").val('');
            $("#cotpBankAcc").val('');
            $("#cotpAccCurId").val(''); //账户币种
            $("#cotpAccCurNameDiv").text('');
            $("#cotpAdrr").val(''); //交易对手地址
            /*$("#cotpAdrrDiv").text('');*/
            /*$("#cotpCountry").val('');//交易对手所在国家地区&地点
            $("#cotpProv").val('');
            $("#cotpCity").val('');
            $("#cotpCountryDiv").text('');
            $("#cotpProvDiv").text('');
            $("#cotpCityDiv").text('');
            $("#countryId").val('');//交易对手银行所在国家地区&地点
            $("#nameZhBankDiv").text('');*/
            selectType.get('cotpCountry').val('');
            region.get('payeeAddr').set([{
                    name: 'cotpProv',
                    defaultValue: ''
                }, {
                    name: 'cotpCity',
                    defaultValue: ''
                }]
            );
            selectType.get('countryId').val('');
            $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
            $("#localClearNoDiv").text('');//本地清算号
            // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
            // $("#cotpAgentBankDiv").text('');//交易对手代理行
            // $("#cotpAgentBankAddrDiv").text('');//交易对手代理行地址
            // $("#cotpAgentBankAccDiv").text('');//交易对手代理行账户
        }

        //获取支付方式
        renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),selectType.get("proId").val());
    }

    //相应金额2回调
    bt.tranAmt2Callback = function(data) {
        var tranAmt2 = moneyType.get("tranAmt2").val();
        if (tranAmt2) {
            //相应金额2有值，则交易编码2和交易附言2必填
            moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
            $('#tranName2').attr('lay-verify','tableData|required');
            $('#tranEnglishCode2').attr('lay-verify', 'required');
            $('#tranAddWord2').attr('lay-verify', 'required');
            //同步币种
            $("#tranCurId2").val($("#tranCurId1").val());
            $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
        } else {
            if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
                moneyType.get('tranAmt2').attr('lay-verify', 'number2');
                $('#tranName2').attr('lay-verify','tableData');
                $('#tranEnglishCode2').removeAttr("lay-verify");
                $('#tranAddWord2').removeAttr("lay-verify");
                //同步币种
                $("#tranCurId2").val('');
                $("#tranEnglishCode2").val('');
            }
        }
        form.renderIcon();
    }
    //交易附言2回调
    $('#tranAddWord2').on('blur',function () {
        var tranAddWord2 = $('#tranAddWord2').val();
        if (tranAddWord2) {
            //交易附言2有值，则交易编码2和相应金额2必填
            moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
            $('#tranName2').attr('lay-verify','tableData|required');
            $('#tranEnglishCode2').attr('lay-verify', 'required');
            $('#tranAddWord2').attr('lay-verify', 'required');
            //同步币种
            $("#tranCurId2").val($("#tranCurId1").val());
            $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
        } else {
            if (!moneyType.get('tranAmt2').val() && !$('#tranName2').val()) {
                moneyType.get('tranAmt2').attr('lay-verify', 'number2');
                $('#tranName2').attr('lay-verify','tableData');
                $('#tranEnglishCode2').removeAttr("lay-verify");
                $('#tranAddWord2').removeAttr("lay-verify");
                //同步币种
                $("#tranCurId2").val('');
                $("#tranEnglishCode2").val('');
            }
        }
        form.renderIcon();
    })

    /**
     * 设置地点 (新交易对手此方法无用)
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
        var bankTypeName = $("#srcBankTd").text();
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

    //汇款类型回调函数
    bt.remitTypeCallback = function(data){
        setReportInfo();
        var remitType = bt.dicts.get("remitType").val();
        var countryId = "CN";
        if ($('#payerIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#payerCountryId').val();
        }
        var bankTypeId = bt.selectType.get('corpBank').val();
        var bifCode = $('#payerBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, '', remitType, "other");
        form.renderIcon();
    }
    function setReportInfo(){
        //汇款类型=2-境外账户汇款时，“主体单位联系人”、“主体单位联系电话”为非必填
        var remitType = bt.dicts.get("remitType").val();
        if (remitType == "2") {
            $("input[name='corpContact']").removeAttr("lay-verify");
            $("input[name='corpContactTel']").removeAttr("lay-verify");
            //汇款类型=2-境外账户汇款时，“其它信息”非必填，并默认为折叠状态
            $("#reportInfoDiv").removeClass("layui-show");
            $("#reportInfoDiv").addClass("ignore-verify");
            //隐藏被代收/代付单位 和  加急标志
            $("#isQuickTr").hide();
            $("#payCorpIdTitleDiv").hide();
            $("#payCorpIdDiv").hide();
            $("#reportInfoItemDiv").hide();
        } else {
            $("input[name='corpContact']").attr("lay-verify", "required");
            $("input[name='corpContactTel']").attr("lay-verify", "required");
            $("#reportInfoDiv").addClass("layui-show");
            $("#reportInfoDiv").removeClass("ignore-verify");
            $("#isQuickTr").show();
            $("#payCorpIdTitleDiv").show();
            $("#payCorpIdDiv").show();
            $("#reportInfoItemDiv").show();
        }

        //工商银行个性化
        var bankTypeName = $("#srcBankTd").text();
        setBranchCodeForGsbank(bankTypeName);
    }

    //付款类型回调函数
    bt.payTypeCallback = function (data) {
        //付款类型=1-预付货款时，最迟装运日期为必填项
        var payType = bt.dicts.get("payType").val();
        if (payType == "1") {
            $("input[name='shipDate']").attr("lay-verify", "required");
        } else {
            $("input[name='shipDate']").removeAttr("lay-verify");
        }
        form.renderIcon();
    }
    //国内外费用承担标志回调函数
    bt.costSignCallback = function (data) {
        //收付方向 0-收款 1-付款
        var cdSign = bt.dicts.get('cdSign').val();
        //国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA
        var costSign = bt.dicts.get("costSign").val();
        if ((cdSign == "0" && costSign == "2") || (cdSign == "1" && costSign == "1")) {
            $("input[name='costAcc']").attr("lay-verify", "required|tableData");
        } else {
            $("input[name='costAcc']").attr("lay-verify", "tableData");
        }
        setReportInfo();
        form.renderIcon();
        //现金流
        if(cdSign=="0") {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
            $("#contractRateTr").hide();
        }else {
            $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('payMoney').val()));
            $("#contractRateTr").show();
        }
        //计算汇率折算金额
        calPayMoneyConvert();
        renderBudget();
    }
    /**
     * 汇款方式回调
     */
    bt.remitMethodCallBack = function (data, $elem) {
        var remitMethod = dict.get("remitMethod").val();
        /*var payeeType = "0,1,2,3,7";
        if(remitMethod == "2" || remitMethod == "3" || remitMethod == "4"){
            //当“汇款方式”为“2-实时电汇”、“3-实时特快转账CHATS”、“4-转数快FPS”
            payeeType = payeeType + ",4,5";
        } else if (remitMethod == "5"){
            //当“汇款方式”为“5-实时转账”
            payeeType = payeeType + ",4,5,6";
        }
        dict.get("payeeType").attr({"data-displayData": payeeType});
        if(remitMethod == "2" || remitMethod == "3" || remitMethod == "5"){
            //当“汇款方式”为“2-实时电汇”、“3-实时特快转账CHATS”、“5-实时转账”
            dict.get("payeeType").attr({"data-required": true});
        } else {
            // 农业银行、渣打银行印尼时必填
            var bankTypeName = $("#srcBankTd").text();
            var payerEnCode = $('#payerEnCode').val();
            if (bankTypeName == "农业银行" || (bankTypeName == "渣打银行" && payerEnCode == "ID")) {
                dict.get("payeeType").attr({"data-required": true});
            } else {
                dict.get("payeeType").attr({"data-required": false});
            }
        }*/
        setPayType();
        form.renderIcon();
        form.render();
    }

    /**
     * 付款类型
     */
    function setPayType() {
        var remitMethod = dict.get("remitMethod").val();
        var payType = "1,2,3,4";
        //MY-马来西亚
        var payerEnCode = $('#payerEnCode').val();
        if(payerEnCode == "MY" && (remitMethod == "2" || remitMethod == "5")){
            //当“汇款方式”为“2-实时电汇”、“5-实时转账”
            payType = payType + ",5,6,7,8";
        }
        dict.get("payType").attr({"data-displayData": payType});
    }
    var dict = bt.dicts.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });

    //单位回调函数
    bt.corpCallback = function (id) {
        //获取单位信息
        $.ajax({
            url: basePath + "/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    if (data.obj.userAccountCode) {
                        $('#corpNameEn').val(data.obj.nameEn);
                        $('#corpAddrEn').val(data.obj.addressEn);
                        $('#corpAddr').val(data.obj.address);
                        //清空主体单位联系人信息
                        $('#corpContact').val("");
                        $('#corpContactTel').val("");
                        //重新加载主体单位联系人
                        corpContactInit.get("corpContact").reload({corpId: id});
                        //加载资金计划
                        renderBudget();

                        $("#srcCorpTd").text(data.obj.name);
                    }
                }
                form.render();
            }
        });
        // 重新设置用途
        initPurpose();
        //获取支付方式
        renderBankAcc("0", id, selectType.get("curId").val(),selectType.get("proId").val());
        //重新加载源账户信息
        corpBankAccInit.get("corpBankAcc").reload({corpId: id, curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()});
        costAccInit.get("costAcc").reload({bankType: bt.selectType.get('corpBank').val(), corpId: id});
    };
    function initPurpose() {
        var corpId = corp.get("corpId").val();
        var curId = selectType.get("curId").val();
        var countryId = $("#payerCountryId").val();
        var bankTypeId = selectType.get("corpBank").val();
        var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
            + "&curId=" + curId+ "&countryId=" + countryId+ "&bankTypeId=" + bankTypeId;
        bt.selectType.get("remitUse").attr("data-url", purposeUrl);
    }
    //初始化单位
    var corp = bt.corp.init();

    /**
     * 获取支付方式
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     */
    function renderBankAcc(busType, corpId, curId,proId) {
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busType!="" && corpId!="") {
            $.ajax({
                url: basePath + "/foreign/fexcorpbankacc/getNewBankAcc",
                method: 'POST',
                async: false,
                data: {busType: busType, tranType: "4", corpId: corpId, curId: curId, proId: proId, cashFlowType: "0", cdSign: bt.dicts.get('cdSign').val(), oppCorpId: oppId},
                dataType: 'json',
                success: function (data) {
                    if (data.obj && data.obj.bankAcc) {
                        selectType.get('corpBank').val(data.obj.bankType);
                        $('#corpAccId').val(data.obj.accId);
                        $('#corpBankAcc').val(data.obj.bankAcc);
                        $('#corpAccName').val(data.obj.accName);
                        $('#corpAccCurId').val(data.obj.curId);

                        //账户类别
                        if (data.obj.accType) {
                            $('#accType').val(data.obj.accType);
                            $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.obj.accType));
                        }

                        //费用支出账户
                        $('#costAccId').val(data.obj.accId);
                        $('#costAcc').val(data.obj.bankAcc);
                        $("#srcBankTd").text(data.obj.bankTypeName);
                        $("#srcAccNameTd").text(data.obj.accName);
                        $("#srcBankAccTd").text(data.obj.bankAcc);
                        if(data.obj.voucherName) {
                            $("#srcPayTypeTd").text(data.obj.voucherName);
                        }else{
                            $("#srcPayTypeTd").text("");
                        }
                        $("#corpAccNameDiv").text(data.obj.accName);
                        $('#corpAccCurNameDiv').text(data.obj.curName);
                    }
                }
            });
        }
    }

    //起运国下拉表格
    var originCountryInit = bt.selectTable.init({
        elem: "#originCountryTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#originCountryName",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='originCountryName']").val(data.nameZh);
            $("input[name='originCountry']").val(data.id);
        },
    });

    //主体账户信息
    var corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getCorpBankAcc",
        urldata: {corpId: $("input[name='corpId']").val(), curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#corpBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curEnglishCode",}
        ],
        clickCallback:function(data){
            if(data){
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccId').val(data.id);
                $('#corpAccName').val(data.accName);
                $('#corpAccCurId').val(data.curId);
                $("#corpAccCurNameDiv").text(data.curEnglishCode);
                $('#corpId').val(data.corpId);
                $("#corpAccNameDiv").text(data.accName);
                $("#corpNameDiv").text(data.corpName);
                bt.selectType.get('corpBank').val(data.bankTypeId);
                $('#payerBifCode').val(data.bifCode);
                //境内外账号标志
                $('#payerIsDomesticBank').val(data.isDomesticBank);
                //本地清算号
                if (data.localClearNo) {
                    $("#payerLocalClearNoDiv").text(data.localClearNo);
                } else {
                    $("#payerLocalClearNoDiv").text('');
                }
                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                $("#srcCorpTd").text(data.corpName);
                //国家地区信息
                $('#payerCountryId').val(data.prov);
                $('#payerCountryNameZh').val(data.provName);
                $('#payerEnCode').val(data.countryEnCode);
                //付款类型
                setPayType();
                //账户类别
                $('#accType').val(data.accType);
                $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));

                var corpId = corp.get("corpId").val();
                var curId = selectType.get("curId").val();
                //var countryId = $("#payerCountryId").val();
                var bankTypeId = selectType.get("corpBank").val();
                var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
                    + "&curId=" + curId+ "&countryId=" + $("#payerCountryId").val()+ "&bankTypeId=" + bankTypeId;
                //农业银行：收款人类型、汇款去向、汇款用途改为必填字段
                if(data.bifCodeStr == "9009"){
                    bt.dicts.get('remitRoute').attr({'data-required': true});
                    bt.dicts.get('payeeType').attr({'data-required': true});
                    selectType.get('remitUse').attr({"data-url": purposeUrl,'data-required': true});
                } else {
                    bt.dicts.get('remitRoute').attr({'data-required': false});
                    bt.dicts.get('payeeType').attr({'data-required': false});
                    selectType.get('remitUse').attr({"data-url": purposeUrl,'data-required': false});
                }
                form.renderIcon();
                //加载资金计划
                renderBudget();
                //默认设置费用支出账户
                $('#costAccId').val(data.id);
                $('#costAcc').val(data.bankAcc);
                //重新加载费用支出账号
                costAccInit.get("costAcc").reload({bankType: data.bankTypeId, corpId: data.corpId});
                //民生银行个性化
                setBranchCodeForMsbank(data.bankTypeName);
                //兴业银行个性化
                setBranchCodeForXybank(data.bankTypeName);
                //工商银行个性化
                setBranchCodeForGsbank(data.bankTypeName);
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
                //计算汇率折算金额
                calPayMoneyConvert();
            }else{
                $('input.text').val('');
            }
        }
    });

    //交易对手账户信息
    // var cotpBankAccInit = bt.selectTable.init({
    //     elem: "#cotpBankAccTable",
    //     url: basePath+"/foreign/fexcotpbankacc/selectJoinCur",
    //     urldata: {cotpId:bt.selectType.get("cotpId").val(), bankId: bt.selectType.get("cotpBank").val()},
    //     scrollX: true,
    //     scrollY:"200px",
    //     searchInput:"#cotpBankAcc",
    //     paging:true,
    //     columns: [
    //         {data:"bankAcc",},
    //         {data:"accName",},
    //         {data:"bankName",},
    //         {data:"curName",}
    //     ],
    //     clickCallback:function(data){
    //         if(data){
    //             selectType.get("cotpId").val(data.cotpId);
    //             //反写交易对手信息
    //             setCotp(selectType.get("cotpId").getSelectedData(), false);
    //             selectType.get("cotpBank").val(data.bankId);
    //             //反写交易对手银行信息
    //             setCotpBank(selectType.get("cotpBank").getSelectedData(), false);
    //             //交易对手银行地址(交易对手银行账号-地址-fexCotpBankAcc.bankAddr)
    //             if(data.bankAddr) {
    //                 $("#cotpBankAdrrDiv").text(data.bankAddr);
    //             } else {
    //                 $("#cotpBankAdrrDiv").text('');
    //             }
    //             //交易对手代理行(交易对手银行账户-中转行-fexCotpBankAcc.tranBank)
    //             if(data.tranBank) {
    //                 $("#cotpAgentBankDiv").text(data.tranBankName);
    //             } else {
    //                 $("#cotpAgentBankDiv").text('');
    //             }
    //             //交易对手代理行地址(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAddr)
    //             if(data.tranBankAddr) {
    //                 $("#cotpAgentBankAddrDiv").text(data.tranBankAddr);
    //             } else {
    //                 $("#cotpAgentBankAddrDiv").text('');
    //             }
    //             //交易对手在代理行的账户(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAcc)
    //             if(data.tranBankAcc) {
    //                 $("#cotpAgentBankAccDiv").text(data.tranBankAcc);
    //             } else {
    //                 $("#cotpAgentBankAccDiv").text('');
    //             }
    //             $("#cotpBankAcc").val(data.bankAcc);
    //             $("#cotpAccId").val(data.id);
    //             $("#cotpAccName").val(data.accName);
    //             $("#cotpAccCurId").val(data.curId);
    //             $("#cotpAccCurNameDiv").text(data.curName);
    //
    //             //现金流信息
    //             $("#srcCotpBankAccTd").text(data.bankAcc);
    //             $("#srcCotpAccNameTd").text(data.accName);
    //         }else{
    //             $('input.text').val('');
    //         }
    //     }
    // });

    //办理机构
    var branchCodeInit = bt.selectTable.init({
        elem: "#branchCodeTable",
        url: basePath + "/basic/business/bisBankBranch/listAll",
        searchInput: "#branchCode",
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "branchCode",},
            {data: "branchName",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#branchCode').val(data.branchCode);
                $('#branchName').val(data.branchName);
            } else {
                $('input.text').val('');
            }
        }
    });

    //费用支出账户信息
    var costAccInit = bt.selectTable.init({
        elem: "#costAccTable",
        url: basePath+"/foreign/fextranreq/getCostAcc",
        urldata: {bankType: bt.selectType.get('corpBank').val(), corpId: $("input[name='corpId']").val()},
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
                bt.selectType.get('costCurId').val(data.curId);
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

    //交易编码
    var tranCodeInit1 = bt.selectTable.init({
        elem: "#tranCodeTable1",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName1').val(data.tranName);
                $('#tranCode1').val(data.tranCode);
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: bt.dicts.get("cdSign").val()},
                    dataType: 'json',
                    success: function (data) {
                        if (data.obj) {
                            $('#tranAddWord1').val(data.obj);
                        } else {
                            $('#tranAddWord1').val('');
                        }
                        form.render();
                    }
                });
            } else {
                $('input.text').val('');
                $('#tranAddWord1').val('');
            }
        }
    });

    var tranCodeInit2 = bt.selectTable.init({
        elem: "#tranCodeTable2",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName2",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName2').val(data.tranName);
                $('#tranCode2').val(data.tranCode);
                $('#tranName2').attr('lay-verify','tableData|required');
                moneyType.get('tranAmt2').attr('lay-verify', 'required|number2');
                $('#tranEnglishCode2').attr('lay-verify', 'required');
                $('#tranAddWord2').attr('lay-verify', 'required');
                //同步币种
                $("#tranCurId2").val($("#tranCurId1").val());
                $("#tranEnglishCode2").val($("#tranEnglishCode1").val());
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: bt.dicts.get("cdSign").val()},
                    dataType: 'json',
                    success: function (data) {
                        if (data.obj) {
                            $('#tranAddWord2').val(data.obj);
                        } else {
                            $('#tranAddWord2').val('');
                        }
                        form.render();
                    }
                });
            } else {
                $('input.text').val('');
                $('#tranCode2').val('');
                if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
                    moneyType.get('tranAmt2').attr('lay-verify', 'number2');
                    $('#tranName2').attr('lay-verify','tableData');
                    $('#tranEnglishCode2').removeAttr("lay-verify");
                    $('#tranAddWord2').removeAttr("lay-verify");
                    //同步币种
                    $("#tranCurId2").val('');
                    $("#tranEnglishCode2").val('');
                }
            }
            form.renderIcon();
        },
        blurInputCallback: function() {
            $('input.text').val('');
            $('#tranCode2').val('');
            if (!$('#tranName2').val() && !$('#tranAddWord2').val()) {
                moneyType.get('tranAmt2').attr('lay-verify', 'number2');
                $('#tranName2').attr('lay-verify','required');
                $('#tranEnglishCode2').removeAttr("lay-verify");
                $('#tranAddWord2').removeAttr("lay-verify");
                //同步币种
                $("#tranCurId2").val('');
                $("#tranEnglishCode2").val('');
            }
            form.renderIcon();
        }
    });

    //主体单位联系人
    var corpContactInit = bt.selectTable.init({
        elem: "#corpContactTable",
        url: basePath + "/foreign/fexlinkmaninfo/listByCorpId",
        urldata: {corpId: $("input[name='corpId']").val()},
        searchInput: "#corpContact",
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "payerName",},
            {data: "payerTel",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#corpContact').val(data.payerName);
                $('#corpContactTel').val(data.payerTel);
            } else {
                $('input.text').val('');
            }
        }
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

    //产品回调函数
    bt.proCallback = function (data) {
        //如果产品选择香港FPS产品，则跳到新页面
        if(data.id == "53f8408e666b42b9abbe2dcf8372bc76"){
            location.href = basePath + "/foreign/fextranreq/addHK";
        }else if(data.id == "4c182f17455f4614a79d66bdfd755c8d"){
            location.href = basePath + "/foreign/fextranreq/addPaypal";
        }
        //获取支付方式
        renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),selectType.get("proId").val());
    };

    //币别回调函数
    bt.curCallback = function (data) {
        $("#tranCurId1").val(data.id);
        $("#tranEnglishCode1").val(data.englishcode);
        var tranCode2 = $('#tranCode2').val();
        if (tranCode2 && tranCode2 != "") {
            $("#tranCurId2").val(data.id);
            $("#tranEnglishCode2").val(data.englishcode);
        } else {
            $("#tranCurId2").val('');
            $("#tranEnglishCode2").val('');
        }
        renderBudget();

        //现金流信息
        $("#srcCurTd").text(data.englishcode);
        //获取支付方式
        renderBankAcc("0", $("input[name='corpId']").val(), data.id,selectType.get("proId").val());
        //计算汇率折算金额
        calPayMoneyConvert();
        form.render();
        // 重新设置用途
        initPurpose();
    };

    /**
     * 汇率折算金额计算
     */
    calPayMoneyConvert = function() {
        //交易币种
        var curId = selectType.get("curId").val();
        //主体单位账户币种
        var corpAccCurId = $("#corpAccCurId").val();
        //金额
        var payMoney = moneyType.get("payMoney").val();
        var cdSign=dict.get('cdSign').val();
        if (cdSign == "1" && isNotEmpty(curId) && isNotEmpty(corpAccCurId)) {
            if (curId == corpAccCurId) {
                moneyType.get("payMoneyConvert").val(payMoney);
                return;
            }
            $.ajax({
                "url": basePath + "/basic/business/btcurrencypair/getByCurId",
                data:{
                    curId1: corpAccCurId,
                    curId2: curId
                },
                type:"post",
                dataType:"json",
                async: false,
                success:function (data) {
                    if (data.success) {
                        if (null == data.obj) {
                            parent.layer.alert(bt.lang("fex.tranApp.currencyPair"), {icon: 2});
                            moneyType.get("contractRate").val("");
                            moneyType.get("payMoneyConvert").val("");
                        } else {
                            var contractRate = moneyType.get("contractRate").val();
                            if (isNotEmpty(contractRate)) {
                                var baseCurId = data.obj.baseCurId;
                                if (baseCurId == corpAccCurId) {//主体单位账号币种为标准
                                    moneyType.get("payMoneyConvert").val(bt.money.mul(payMoney, contractRate, 2));
                                } else if (baseCurId == curId) {//交易币种为标准
                                    moneyType.get("payMoneyConvert").val(bt.money.div(payMoney, contractRate, 2));
                                } else {
                                    moneyType.get("payMoneyConvert").val("");
                                }
                            }
                        }
                    }
                }
            });
        } else {
            moneyType.get("payMoneyConvert").val("");
        }
    }

    /**
     * 为空判断
     * @param stringData
     * @returns {boolean}
     */
    function isEmpty(stringData) {
        if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 非空判断
     * @param stringData
     * @returns {boolean}
     */
    function isNotEmpty(stringData) {
        return !isEmpty(stringData)
    }

    //主体单位银行回调函数
    bt.corpBankCallback = function (data) {
        //重新加载源账户信息
        corpBankAccInit.get("corpBankAcc").reload({corpId: $("input[name='corpId']").val(), curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()});
        costAccInit.get("costAcc").reload({bankType: bt.selectType.get('corpBank').val(), corpId: $("input[name='corpId']").val()});
        //现金流信息
        $("#srcBankTd").text(data.typename);
        $("#srcAccNameTd").text('');
        $("#srcBankAccTd").text('');
        $("#srcCorpTd").text('');
        var remitMethod = "0,1";
        if (data.remitmethod) {
            remitMethod = remitMethod + "," + data.remitmethod;
        }
        dict.get("remitMethod").attr({"data-displayData": remitMethod});
        form.render();
        // 重新设置用途
        initPurpose();
        //民生银行个性化
        setBranchCodeForMsbank(data.typename);
        //兴业银行个性化
        setBranchCodeForXybank(data.typename);
        //工商银行个性化
        setBranchCodeForGsbank(data.typename);
    };

    //民生银行个性化
    function setBranchCodeForMsbank(bankTypeName) {
        if (bankTypeName == "民生银行") {
            $('#payerBankSpan').show();
            $('#branchDiv').show();
            $("#branchCode").attr("lay-verify","required");
        } else {
            $('#payerBankSpan').hide();
            $('#branchDiv').hide();
            $("#branchCode").removeAttr('lay-verify');
        }
        form.renderIcon();
    }

    //兴业银行个性化
    function setBranchCodeForXybank(bankTypeName) {
        if (bankTypeName == "兴业银行") {
            $('#xyBankTr').show();
            //$("#declareNo").attr("lay-verify","required");
            bt.dateType.get('arrivalDate').attr({'data-required': true});
            $("#corpAddrEn").attr("lay-verify","required");
            $("#rmk").attr("lay-verify","required|langKey|strlength");
            $('#originCountryTr').show();
        } else {
            $('#xyBankTr').hide();
            //$("#declareNo").removeAttr('lay-verify');
            bt.dateType.get('arrivalDate').attr({'data-required': false});
            $("#corpAddrEn").removeAttr('lay-verify');
            $("#rmk").attr("lay-verify","strlength");
            $('#originCountryTr').hide();
        }
        form.renderIcon();
    }

    //工商银行个性化
    function setBranchCodeForGsbank(bankTypeName) {
        var remitType = bt.dicts.get("remitType").val();
        //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
        if ((remitType == "1" || remitType == "2") && bankTypeName && bankTypeName.indexOf("工商银行") != -1) {
            $("input[name='corpNameEn']").attr("lay-verify", "required");
        } else {
            $("input[name='corpNameEn']").removeAttr("lay-verify");
        }
        form.renderIcon();
    }

    //中国银行个性化
    function setBranchCodeForZgbank(bankTypeName) {
        var remitType = bt.dicts.get("remitType").val();
        //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
        if (remitType == "2" && bankTypeName && bankTypeName.indexOf("中国银行") != -1) {

        } else {
            // selectType.get("cotpAddress").attr({"data-required":false});
            // region.get('payeeAddr').set([{
            //     name: 'cotpProv',
            //     required:false
            // }, {
            //     name: 'cotpCity',
            //     required:false
            // }]);
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
        // 重新设置受益人关联关系
        var bnfRelateUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&customType=bnfRelate";
        //获取银行类型信息
        $.ajax({
            url: basePath + "/basic/business/btbanktype/getBankType",
            method: 'POST',
            data: {bankTypeId: bankType},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (initCustomType != "payeeType" && initCustomType != "other") {
                        // 是否配置支付类型 为 1-是；支付类型应用范围 包含 3-资金收付交易
                        if (data.isSetBankPayType == "1" && data.applayScopeBankPayType.indexOf("3") != -1) {
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

                        // 是否配置受益人关联关系 为 1-是；受益人关联关系应用范围 包含 3-资金收付交易
                        if (data.isSetBnfRelate == "1" && data.applayScopeBnfRelate.indexOf("3") != -1) {
                            if (data.requiredBnfRelate == "1") {
                                selectType.get('bnfRelate').attr({"data-url": bnfRelateUrl, "data-required": true});
                            } else {
                                selectType.get('bnfRelate').attr({"data-url": bnfRelateUrl, "data-required": false});
                            }
                            $(".bnfRelateDiv").show();
                            $("#bnfRelateTd").html(bt.lang('table.fexForeignReq.bnfRelate'));
                        } else {
                            selectType.get('bnfRelate').attr({"data-url": bnfRelateUrl, "data-required": false});
                            $(".bnfRelateDiv").hide();
                            $("#bnfRelateTd").html('');
                        }
                    }

                    if (initCustomType != "other") {
                        // 是否配置收款人类型 为 1-是；收款人类型应用范围 包含 3-资金收付交易
                        if (data.isSetPayeeType == "1" && data.applayScopePayeeType.indexOf("3") != -1) {
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
                        // 是否配置其他信息模块  为 1-是；适用页面 包含 3-资金收付交易
                        if (data.isSetOtherCustom == "1") {
                            $.ajax({
                                url: basePath + "/basic/business/btbanktype/getBankTypeOtherCustom",
                                method: 'POST',
                                data: {
                                    bankType: bankType,
                                    bifCode: bifCode,
                                    countryId: countryId,
                                    remitType: remitType,
                                    applayScope: "3"
                                },
                                dataType: 'json',
                                success: function (data) {
                                    if (data) {
                                        $("#otherInfoDiv").addClass("layui-show");
                                        $("#otherInfoDiv").removeClass("ignore-verify");
                                        $("#otherInfoItemDiv").show();
                                        //所需字段包含“regulatoryType-监管类型”“payType-付款类型”“remitNature-付汇性质”
                                        if (data.needField && data.needField.indexOf("payType") != -1) {
                                            $("#payTypeTd").html(bt.lang('table.fexForeignReq.payType'));
                                            $("#payTypeDiv").show();
                                            if (data.requiredField && data.requiredField.indexOf("payType") != -1) {
                                                bt.dicts.get('payType').attr({'data-required': true});
                                            } else {
                                                bt.dicts.get('payType').attr({'data-required': false});
                                            }
                                        } else {
                                            $("#payTypeTd").html('');
                                            $("#payTypeDiv").hide();
                                            bt.dicts.get('payType').attr({'data-required': false});
                                        }
                                        if (data.needField && data.needField.indexOf("remitNature") != -1) {
                                            $("#remitNatureTd").html(bt.lang('table.fexForeignReq.remitNature'));
                                            $("#remitNatureDiv").show();
                                            if (data.requiredField && data.requiredField.indexOf("remitNature") != -1) {
                                                bt.dicts.get('remitNature').attr({'data-required': true});
                                            } else {
                                                bt.dicts.get('remitNature').attr({'data-required': false});
                                            }
                                        } else {
                                            $("#remitNatureTd").html('');
                                            $("#remitNatureDiv").hide();
                                            bt.dicts.get('remitNature').attr({'data-required': false});
                                        }
                                        if (data.needField && data.needField.indexOf("regulatoryType") != -1) {
                                            $("#regulatoryTypeTd").html(bt.lang('table.fexForeignReq.regulatoryType'));
                                            $("#regulatoryTypeDiv").show();
                                            if (data.requiredField && data.requiredField.indexOf("regulatoryType") != -1) {
                                                bt.dicts.get('regulatoryType').attr({'data-required': true});
                                            } else {
                                                bt.dicts.get('regulatoryType').attr({'data-required': false});
                                            }
                                        } else {
                                            $("#regulatoryTypeTd").html('');
                                            $("#regulatoryTypeDiv").hide();
                                            bt.dicts.get('regulatoryType').attr({'data-required': false});
                                        }
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

                        selectType.get('bnfRelate').attr({"data-url":bnfRelateUrl,"data-required":false});
                        $(".bnfRelateDiv").hide();
                        $("#bnfRelateTd").html('');
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

    //交易对手回调函数
    function setCotp(data, flag) {
        //交易对手所在国家地区
        // setAddress(data.countryid, data.countryname,data.address);
        // if(data.countryid){
        //     //$("#nameZhDiv").text(data.countryname);
        //     // selectType.get('cotpCountry').val(data.countryid);
        //     setTimeout(function () {
        //         // selectType.get('cotpAddress').val(data.address);
        //     }, 500)
        //     region.get('payeeAddr').set([{
        //         name: 'cotpProv',
        //         defaultValue: data.prov
        //     }, {
        //         name: 'cotpCity',
        //         defaultValue: data.city
        //     }]);
        // } else {
        //     //$("#nameZhDiv").text('');
        //     // selectType.get('cotpCountry').val('');
        //     setTimeout(function () {
        //         // selectType.get('cotpAddress').val('');
        //     }, 500)
        //     region.get('payeeAddr').set([{
        //         name: 'cotpProv',
        //         defaultValue: ''
        //     }, {
        //         name: 'cotpCity',
        //         defaultValue: ''
        //     }]);
        // }
        // //交易对手地址
        // if(data.addressen){
        //     $("#cotpAdrrDiv").text(data.addressen);
        // } else {
        //     $("#cotpAdrrDiv").text('');
        // }
        // //现金流信息
        // $("#srcCotpTd").text(data.cotpname);
        // if (flag) {
        //     //交易对手银行账户
        //     setCotpBankAcc();
        // }
        // //获取支付方式
        // renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),selectType.get("proId").val());
    }
    bt.cotpCallback = function (data) {
        setCotp(data, true);
    };

    //交易对手银行回调函数
    function setCotpBank(data, flag) {
        //交易对手银行所在国家地区
        if(data.countryname){
            $("#nameZhBankDiv").text(data.countryname);
        } else {
            $("#nameZhBankDiv").text('');
        }
        $("#countryId").val(data.countryid);
        if (flag) {
            //交易对手银行账户
            setCotpBankAcc();
        }
        //现金流信息
        $("#srcCotpBankTd").text(data.cotpname);
        //交易对手银行Swift Code(交易对手银行账户-BIC Code-fexCotpBankAcc.bicCode)
        if(data.biccode) {
            $("#cotpSwiftCodeDiv").text(data.biccode);
        } else {
            $("#cotpSwiftCodeDiv").text('');
        }
        //交易对手银行本地清算号
        if (data.localclearno) {
            $("#localClearNoDiv").text(data.localclearno);
        } else {
            $("#localClearNoDiv").text('');
        }
    }
    bt.cotpBankCallback = function (data) {
        setCotpBank(data, true);
    };

    //交易对手银行账户
    function setCotpBankAcc(){
        // var cotpId = selectType.get("cotpId").val();
        // var bankId = selectType.get("cotpBank").val();
        /*selectType.get("cotpBankAcc").attr({"data-value": "",
            "data-url": basePath + "/foreign/fexcotpbankacc/select?cotpId=" + cotpId + "&bankId=" + bankId});
        selectType.render();*/
        //暂时隐藏
        // cotpBankAccInit.get("cotpBankAcc").reload({cotpId: cotpId, bankId: bankId});
        form.render();
    }

    //交易对手银行账户回调函数
    /*bt.cotpBankAccCallback = function (data) {
        selectType.get("cotpId").val(data.cotpid);
        //反写交易对手信息
        setCotp(selectType.get("cotpId").getSelectedData(), false);
        selectType.get("cotpBank").val(data.bankid);
        //反写交易对手银行信息
        setCotpBank(selectType.get("cotpBank").getSelectedData(), false);
        //交易对手银行地址(交易对手银行账号-地址-fexCotpBankAcc.bankAddr)
        if(data.bankaddr) {
            $("#cotpBankAdrrDiv").text(data.bankaddr);
        } else {
            $("#cotpBankAdrrDiv").text('');
        }
        //交易对手代理行(交易对手银行账户-中转行-fexCotpBankAcc.tranBank)
        if(data.tranbank) {
            $("#cotpAgentBankDiv").text(data.tranbankname);
        } else {
            $("#cotpAgentBankDiv").text('');
        }
        //交易对手代理行地址(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAddr)
        if(data.tranbankaddr) {
            $("#cotpAgentBankAddrDiv").text(data.tranbankaddr);
        } else {
            $("#cotpAgentBankAddrDiv").text('');
        }
        //交易对手在代理行的账户(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAcc)
        if(data.tranbankacc) {
            $("#cotpAgentBankAccDiv").text(data.tranbankacc);
        } else {
            $("#cotpAgentBankAccDiv").text('');
        }
        $("#cotpAccId").val(data.id);
        $("#cotpAccName").val(data.accname);
        $("#cotpAccCurNameDiv").text(data.curname);

        //现金流信息
        $("#srcCotpBankAccTd").text(data.bankacc);
        $("#srcCotpAccNameTd").text(data.accname);
    };*/

    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }

    bt.bankPayTypeCallback = function(data) {
        var bankPayType = bt.selectType.get("bankPayType").val();
        var countryId = "CN";
        if ($('#payerIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#payerCountryId').val();
        }
        var bankTypeId = bt.selectType.get('corpBank').val();
        var bifCode = $('#payerBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, bankPayType, '', "payeeType");
        form.renderIcon();
    }

    /**
     * 现金流项目
     * @param data
     */
    bt.cashItemCallback = function (data) {
        if(data.itemname){
            $("#srcCashItemTd").text(data.itemname);
        }else{
            $("#srcCashItemTd").text('');
        }
    }
    var selectType = bt.selectType.init();

    //结算日期回调函数
    settleDateCallback = function (data, $elem){
        $("#srcSettleDateTd").text(dateType.get('settleDate').val());
        renderBudget();
    };

    //金额回调函数
    bt.payMoneyCallback = function (data) {
        //根据收付标志显示金额
        var cdSign=dict.get('cdSign').val();
        if(cdSign=="0") {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
        }else {
            $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('payMoney').val()));
        }
        //计算汇率折算金额
        calPayMoneyConvert();
    };


    //合同汇率回调函数
    bt.contractRateCallback = function (data) {
        //计算汇率折算金额
        calPayMoneyConvert();
    };
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    var timeoutID = null;
    //监听提交
    form.on('submit(submit)', function(data){
        //控制重复提交
        if($(data.elem).parent().hasClass('isSubmiting')){
            bt.loading.hide();
            return false;
        }

        bt.loading.show();
        //增加已提交的标志
        $(data.elem).parent().addClass('isSubmiting');
        if(timeoutID){
            clearTimeout(timeoutID);
        }
        timeoutID = setTimeout(function () {
            $(data.elem).parent().removeClass('isSubmiting');
        },300)

        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
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
        //汇款类型=2-境外账户汇款时，其他信息隐藏无需校验
        var remitType = bt.dicts.get("remitType").val();
        if (remitType != "2") {
            //校验交易编码
            var tranCode1 = $('#tranCode1').val();
            var tranCode2 = $('#tranCode2').val();
            var tranAmt1 = moneyType.get("tranAmt1").val();
            var tranAmt2 = moneyType.get("tranAmt2").val();
            if (tranCode2 != "") {
                //交易编码1和交易编码2不允许相同
                if (tranCode1 == tranCode2) {
                    bt.loading.hide();
                    layer.alert(bt.lang("fex.foreign.message.trancode.same"), {icon: 2});
                    return false;
                }
                //相应金额2不能为空
                var tranAmt2 = moneyType.get("tranAmt2").val();
                if (tranAmt2 == null) {
                    bt.loading.hide();
                    layer.alert(bt.lang("fex.foreign.message.tranamt2.null"), {icon: 2});
                    return false;
                }
                //交易附言2不能为空
                var tranAddWord2 = $('#tranAddWord2').val();
                if (!tranAddWord2) {
                    bt.loading.hide();
                    layer.alert(bt.lang("fex.foreign.message.tranaddword2.null"), {icon: 2});
                    return false;
                }
            }
            if (!tranAmt2) {
                // 相应金额2未填写时，默认0
                tranAmt2 = 0;
            }
            //校验相应金额1和相应金额2之和 是否与 付款金额一致
            var tranAmt = moneyFun.add(tranAmt1, tranAmt2);
            if (tranAmt != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("fex.foreign.message.tranAmt.mismatch"), {icon: 2});
                return false;
            }
        }
        //证书签名
        //证书签名
        var encrypt = $("#encrypt").val(); // cfca证书
        if(encrypt == 2 || encrypt == 1){ // 1 天威证书
            bt.loading.hide();
            var signDataMsg = signData("外汇交易提交", 11,function () {
                var action = "/foreign/fextranreq/save";
                var corpAccCurId = $("#corpAccCurId").val();//主体单位账号币种
                var cotpAccCurId = $("#cotpAccCurId").val();//交易对手账号币种
                var curId = selectType.get("curId").val();//交易币种
                if (corpAccCurId != cotpAccCurId) {
                    //跨币种交易，是否继续?
                    parent.layer.confirm(bt.lang('foreign.message.confirm.cur'), function (index) {
                        bt.loading.show();
                        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                            bt.loading.hide();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                                //解除已提交的标志
                                $(data.elem).parent().removeClass('isSubmiting');
                                if (result.success) {
                                    if (bt.navtab) {
                                        bt.navtab.refreshParentTable();
                                        bt.navtab.refreshCurrent();
                                    } else {
                                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    }
                                }
                                parent.layer.closeAll();
                            })
                        }, 'JSON');
                    },function (){
                        $(data.elem).parent().removeClass('isSubmiting');
                    });
                } else if (corpAccCurId == cotpAccCurId && corpAccCurId != curId) {
                    //交易币种与交易主体单位/交易对手账号币种不一致，是否继续?
                    bt.loading.hide();
                    parent.layer.confirm(bt.lang('foreign.message.confirm.cur1'), function (index) {
                        bt.loading.show();
                        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                            bt.loading.hide();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                                parent.layer.closeAll();
                                //解除已提交的标志
                                $(data.elem).parent().removeClass('isSubmiting');
                                if (result.success) {
                                    if (bt.navtab) {
                                        bt.navtab.refreshParentTable();
                                        bt.navtab.refreshCurrent();
                                    } else {
                                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    }
                                }
                            })
                        }, 'JSON');
                    });
                } else {
                    bt.loading.show();
                    $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                            parent.layer.closeAll();
                            //解除已提交的标志
                            $(data.elem).parent().removeClass('isSubmiting');
                            if (result.success) {
                                if (bt.navtab) {
                                    bt.navtab.refreshParentTable();
                                    bt.navtab.refreshCurrent();
                                } else {
                                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                }
                            }
                        })
                    }, 'JSON');
                }
            });
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
        }else{
            var signDataMsg = signData("外汇交易提交", 11);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            var action = "/foreign/fextranreq/save";
            var corpAccCurId = $("#corpAccCurId").val();//主体单位账号币种
            var cotpAccCurId = $("#cotpAccCurId").val();//交易对手账号币种
            var curId = selectType.get("curId").val();//交易币种
            if (corpAccCurId != cotpAccCurId) {
                //跨币种交易，是否继续?
                bt.loading.hide();
                parent.layer.confirm(bt.lang('foreign.message.confirm.cur'), function (index) {
                    bt.loading.show();
                    $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                            //解除已提交的标志
                            $(data.elem).parent().removeClass('isSubmiting');
                            if (result.success) {
                                if (bt.navtab) {
                                    bt.navtab.refreshParentTable();
                                    bt.navtab.refreshCurrent();
                                } else {
                                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                }
                            }
                            parent.layer.closeAll();
                        })
                    }, 'JSON');
                },function (){
                    $(data.elem).parent().removeClass('isSubmiting');
                });
            } else if (corpAccCurId == cotpAccCurId && corpAccCurId != curId) {
                //交易币种与交易主体单位/交易对手账号币种不一致，是否继续?
                bt.loading.hide();
                parent.layer.confirm(bt.lang('foreign.message.confirm.cur1'), function (index) {
                    bt.loading.show();
                    $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                            parent.layer.closeAll();
                            //解除已提交的标志
                            $(data.elem).parent().removeClass('isSubmiting');
                            if (result.success) {
                                if (bt.navtab) {
                                    bt.navtab.refreshParentTable();
                                    bt.navtab.refreshCurrent();
                                } else {
                                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                }
                            }
                        })
                    }, 'JSON');
                });
            } else {
                bt.loading.show();
                $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                        parent.layer.closeAll();
                        //解除已提交的标志
                        $(data.elem).parent().removeClass('isSubmiting');
                        if (result.success) {
                            if (bt.navtab) {
                                bt.navtab.refreshParentTable();
                                bt.navtab.refreshCurrent();
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                    })
                }, 'JSON');
            }
        }

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
            curId: selectType.get("curId").val(),
            wishPayday: processDate,
            payerAcc: $("#corpBankAcc").val(),
            payeeAcc: $("#cotpBankAcc").val(),
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
        var cdSign = bt.dicts.get('cdSign').val();
        if (budgetSel == "1" && cdSign=="1") {
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
        //$("#nameZhDiv").text('');
        // $("#cotpAdrrDiv").text('');
        // $("#cotpBankAdrrDiv").text('');
        // $("#cotpSwiftCodeDiv").text('');
        // $("#cotpAgentBankDiv").text('');
        // $("#cotpAgentBankAddrDiv").text('');
        // $("#cotpAgentBankAccDiv").text('');
        // $("#cotpProvDiv").text('');
        // $("#cotpCityDiv").text('');
        // $("#cotpCountryDiv").text('');

        $("#corpAccNameDiv").text('');
        $("#oppId").val('');
        $("#cotpName").val('');
        $("#cotpNameEn").val('');
        $("#cotpBankName").val('');
        $("#cotpBankAcc").val('');
        $("#cotpAccCurId").val(''); //账户币种
        $("#cotpAccCurNameDiv").text('');
        $("#cotpAdrr").val(''); //交易对手地址
        /*$("#cotpAdrrDiv").text('');*/
        /*$("#cotpCountry").val('');//交易对手所在国家地区&地点
        $("#cotpProv").val('');
        $("#cotpCity").val('');
        $("#cotpCountryDiv").text('');
        $("#cotpProvDiv").text('');
        $("#cotpCityDiv").text('');*/
        selectType.get('cotpCountry').val('');
        region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: ''
            }, {
                name: 'cotpCity',
                defaultValue: ''
            }]
        );
        /*$("#countryId").val('');//交易对手银行所在国家地区&地点
        $("#nameZhBankDiv").text('');*/
        selectType.get('countryId').val('');
        $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
        $("#localClearNoDiv").text('');//本地清算号
        // $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
        // $("#cotpAgentBankDiv").text('');//交易对手代理行
        // $("#cotpAgentBankAddrDiv").text('');//交易对手代理行地址
        // $("#cotpAgentBankAccDiv").text('');//交易对手代理行账户

        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
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
