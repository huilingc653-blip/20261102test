bt.use(['form','layedit','dicts', 'selectType', 'moneyType','element', 'selectTable','dateType','corp', 'btUpload','region','allExternalBankAcc'], function(){
    var $ = bt.jquery,form = bt.form();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    var dateType = bt.dateType.init();
    var allExternalAccCorp=bt.allExternalBankAcc.init();//客商账号组件初始化
    bt.cotpExternalAccBeforeClick = function () {
        allExternalAccCorp.set({
            "type": "payPal"
        });
        return true;
    }
    //省回调函数
    // bt.provCallback = function (value, othis, options) {
    //     $('#cotpProv').val(value);
    //     $('#cotpCity').val("");
    // };
    //市回调函数
    // bt.cityCallback = function (value, othis, options) {
    //     $('#cotpCity').val(value);
    // };
    region = bt.region.init();

    //交易对手银行账户
    bt.cotpExternalAccCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "payPal") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.ecName);
                $("#cotpAdrr").val(data.addressEn); //交易对手地址
                if (isNotEmpty(data.addressEn)) {
                    $("#cotpAdrrDiv").text(data.addressEn);
                }
                $("#cotpCountry").val(data.countryId);//交易对手所在国家地区&地点
                // $("#cotpProv").val(data.prov);
                // $("#cotpCity").val(data.city);
                // $("#cotpProvName").val(data.provName);
                // $("#cotpCityName").val(data.cityName);
                if (isNotEmpty(data.countryName)) {
                    $("#cotpCountryDiv").text(data.countryName);
                }
                // if (isNotEmpty(data.provName)) {
                //     $("#cotpProvDiv").text(data.provName);
                // }
                // if (isNotEmpty(data.cityName)) {
                //     $("#cotpCityDiv").text(data.cityName);
                // }

                $("#email").val(data.email);
                $("#isdCode").val(data.countryCode);
                $("#phoneCode").val(data.tel);

                if (isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
                    $("#mobnValue").val(data.countryCode+"-"+data.tel);
                } else {
                    $("#mobnValue").val(data.tel);
                }

                if(isNotEmpty(data.email)) {
                    $("#cotpBankAcc").val(data.email);
                    $("#srcCotpBankAccTd").text(data.email);
                } else if(isNotEmpty(data.countryCode) && isNotEmpty(data.tel)) {
                    $("#cotpBankAcc").val(data.countryCode+data.tel);
                    $("#srcCotpBankAccTd").text(data.countryCode+data.tel);
                }
                if (isNotEmpty(data.ecName)) {
                    $("#srcCotpTd").text(data.ecName);
                }
                $("#srcCotpBankTd").text('');
                $("#srcCotpAccNameTd").text('');
            }
        }
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),'4c182f17455f4614a79d66bdfd755c8d');

        // else {
        //     $("#oppId").val('');
        //     $("#cotpName").val('');
        //     $("#cotpBankName").val('');
        //     $("#fpsId").val('');
        //     $("#email").val('');
        //     $("#phoneCode").val('');
        //     $("#aiIn").val('');
        //     $("#hkId").val('');
        //     $("#cotpBankNameDiv").text('');
        //     $("#cotpBankAcc").val('');
        //     $("#cotpAccCurId").val(''); //账户币种
        //     $("#cotpAccCurNameDiv").text('');
        //     $("#cotpAdrr").val(''); //交易对手地址
        //     $("#cotpAdrrDiv").text('');
        //     $("#cotpCountry").val('');//交易对手所在国家地区&地点
        //     $("#cotpProv").val('');
        //     $("#cotpCity").val('');
        //     $("#cotpCountryDiv").text('');
        //     $("#cotpProvDiv").text('');
        //     $("#cotpCityDiv").text('');
        //     $("#countryId").val('');//交易对手银行所在国家地区&地点
        //     $("#nameZhBankDiv").text('');
        //     $("#cotpSwiftCodeDiv").text('');//交易对手银行SWIFT CODE
        //     $("#localClearNoDiv").text('');//本地清算号
        //     $("#cotpBankAdrrDiv").text('');//交易对手银行地址 （境内银行为空）
        //     $("#cotpAgentBankDiv").text('');//交易对手代理行
        //     $("#cotpAgentBankAddrDiv").text('');//交易对手代理行地址
        //     $("#cotpAgentBankAccDiv").text('');//交易对手代理行账户
        // }
    }

    bt.cotpCountryCallBack = function (data) {
        setAddress(data.id, data.namezh, '');
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
        // selectType.get("cotpAddress").attr({"data-value": cotpAddress?cotpAddress:'', "data-url": url, "data-required":requiredAddress});
        // region.get('payeeAddr').set([{
        //     name: 'cotpProv',
        //     defaultValue: '',
        //     required:requiredAddr
        // }, {
        //     name: 'cotpCity',
        //     defaultValue: '',
        //     required:requiredAddr
        // }]);
        form.renderIcon();
        form.render();
    }

    var dict = bt.dicts.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    //产品回调函数
    bt.proCallback = function (data) {
        //如果产品选择香港FPS产品，则跳到新页面
        if(data.id == "53f8408e666b42b9abbe2dcf8372bc76"){
            location.href = basePath + "/foreign/fextranreq/addHK";
        }else if(data.id == "4c182f17455f4614a79d66bdfd755c8d"){
            return;
        }else{
            location.href = basePath + "/foreign/fextranreq//add/4";
        }
        //获取支付方式
        renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),'4c182f17455f4614a79d66bdfd755c8d');
    };
    //币别回调函数
    bt.curCallback = function (data) {
        renderBudget();
        //现金流信息
        renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),'4c182f17455f4614a79d66bdfd755c8d');
        $("#srcCurTd").text(data.englishcode);
        form.render();
    };

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
                        //加载资金计划
                        renderBudget();

                        $("#srcCorpTd").text(data.obj.name);
                    }
                }
                form.render();
            }
        });
        renderBankAcc("0", $("input[name='corpId']").val(), bt.selectType.get('curId').val(),'4c182f17455f4614a79d66bdfd755c8d');
        //重新加载源账户信息
        corpBankAccInit.get("corpBankAcc").reload({corpId: id, curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()});
    };
    //初始化单位
    var corp = bt.corp.init();


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
                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                $("#srcCorpTd").text(data.corpName);
                //国家地区信息
                $('#payerCountryId').val(data.prov);
                $('#payerCountryNameZh').val(data.provName);
                $('#payerEnCode').val(data.countryEnCode);

                //账户类别
                $('#accType').val(data.accType);
                $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));
                //付款行Swift Code
                $("#payBankSwiftCodeDiv").text(data.bankCode);
                //主体银行本地清算号
                if(data.localClearNo){
                    $("#payLocalClearNoDiv").text(data.localClearNo);
                }else{
                    $("#payLocalClearNoDiv").text("");
                }

                //主体单位银行地址
                if(data.bankAdd){
                    $("#payBankAdrrDiv").text(data.bankAdd);
                }else{
                    $("#payBankAdrrDiv").text("");
                }
                //主体单位国家地区
                $("#payNameBankDiv").text(data.provName);
                //主体单位国家地区 简码
                $("#payNameBankEnCode").text(data.provName);
                form.renderIcon();
                //加载资金计划
                renderBudget();
                //银行支付类型
                var countryId = "CN";
                if (data.isDomesticBank == "0") {
                    // 境外账户
                    countryId = data.prov;
                }
                setBankPayType(data.bankTypeId, data.bifCode, countryId);
            }else{
                $('input.text').val('');
            }
        }
    });

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

                        //国家地区信息
                        $('#payerCountryId').val(data.obj.prov);
                        $('#payerCountryNameZh').val(data.obj.provName);
                        $('#payerEnCode').val(data.obj.countryEnCode);

                        //账户类别
                        $('#accType').val(data.obj.accType);
                        $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.obj.accType));
                        //付款行Swift Code
                        $("#payBankSwiftCodeDiv").text(data.obj.bankCode);
                        //主体银行本地清算号
                        if(data.obj.localClearNo){
                            $("#payLocalClearNoDiv").text(data.obj.localClearNo);
                        }else{
                            $("#payLocalClearNoDiv").text("");
                        }

                        //主体单位银行地址
                        if(data.obj.bankAdd){
                            $("#payBankAdrrDiv").text(data.obj.bankAdd);
                        }else{
                            $("#payBankAdrrDiv").text("");
                        }
                        if(data.obj.provName){
                            //主体单位国家地区
                            $("#payNameBankDiv").text(data.obj.provName);
                            //主体单位国家地区 简码
                            $("#payNameBankEnCode").text(data.obj.provName);
                        }else{
                            //主体单位国家地区
                            $("#payNameBankDiv").text("");
                            //主体单位国家地区 简码
                            $("#payNameBankEnCode").text("");
                        }
                        form.renderIcon();
                        //加载资金计划
                        renderBudget();
                        //银行支付类型
                        var countryId = "CN";
                        if (data.obj.isDomesticBank == "0") {
                            // 境外账户
                            countryId = data.obj.prov;
                        }
                        setBankPayType(data.obj.bankTypeId, data.obj.bifCode, countryId);
                    }
                }
            });
        }
    }

    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }

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
        //现金流信息
        $("#srcBankTd").text(data.typename);
        $("#srcAccNameTd").text('');
        $("#srcBankAccTd").text('');
        $("#srcCorpTd").text('');
        form.render();
    };

    //设置银行支付类型
    function setBankPayType(bankType, bifCode, countryId) {
        // 重新支付类型
        var bankPayTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&customType=bankPayType";
        //获取银行类型信息
        $.ajax({
            url: basePath + "/basic/business/btbanktype/getBankType",
            method: 'POST',
            data: {bankTypeId: bankType},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    // 是否配置支付类型 为 1-是；支付类型应用范围 包含 3-资金收付交易
                    if (data.isSetBankPayType == "1" && data.applayScopeBankPayType.indexOf("3") != -1) {
                        if (data.requiredBankPayType == "1") {
                            selectType.get('bankPayType').attr({"data-url":bankPayTypeUrl,"data-required":true});
                        } else {
                            selectType.get('bankPayType').attr({"data-url":bankPayTypeUrl,"data-required":false});
                        }
                        $(".bankPayTypeDiv").show();
                        $("#bankPayTypeTd").html(bt.lang('table.nisBillhead.bankPayType'));
                    } else {
                        selectType.get('bankPayType').attr({"data-url":bankPayTypeUrl,"data-required":false});
                        $(".bankPayTypeDiv").hide();
                        $("#bankPayTypeTd").html('');
                    }
                } else {
                    selectType.get('bankPayType').attr({"data-url":bankPayTypeUrl,"data-required":false});
                    $(".bankPayTypeDiv").hide();
                    $("#bankPayTypeTd").html('');
                }
                form.renderIcon();
            }
        });
        form.render();
    }

    //交易对手回调函数
    // function setCotp(data, flag) {
    //     //交易对手所在国家地区
    //     setAddress(data.countryid, data.countryname,data.address);
    //     if(data.countryid){
    //         $("#countryId").val(data.countryid);
    //         //$("#nameZhDiv").text(data.countryname);
    //         selectType.get('cotpCountry').val(data.countryid);
    //         setTimeout(function () {
    //             selectType.get('cotpAddress').val(data.address);
    //         }, 500)
    //         region.get('payeeAddr').set([{
    //             name: 'cotpProv',
    //             defaultValue: data.prov
    //         }, {
    //             name: 'cotpCity',
    //             defaultValue: data.city
    //         }]);
    //     } else {
    //         //$("#nameZhDiv").text('');
    //         selectType.get('cotpCountry').val('');
    //         setTimeout(function () {
    //             selectType.get('cotpAddress').val('');
    //         }, 500)
    //         region.get('payeeAddr').set([{
    //             name: 'cotpProv',
    //             defaultValue: ''
    //         }, {
    //             name: 'cotpCity',
    //             defaultValue: ''
    //         }]);
    //     }
    //     //交易对手地址
    //     if(data.addressen){
    //         $("#cotpAdrrDiv").text(data.addressen);
    //     } else {
    //         $("#cotpAdrrDiv").text('');
    //     }
    //     //现金流信息
    //     $("#srcCotpTd").text(data.cotpname);
    //     //email
    //     if(isNotEmpty(data.email) && isEmpty($("#email").val())){
    //         var emails = data.email.split(";");
    //         $("#email").empty();
    //         for(var i=0;i<emails.length;i++){
    //             $("#email").append($("<option/>").text(emails[i]).attr("value",emails[i]));
    //         }
    //     }
    //     //isdCode国家代码
    //     if(isNotEmpty(data.isdcode)  && isEmpty($("#isdCode").val())){
    //         var isdcodes = data.isdcode.split(";");
    //         $("#isdCode").empty();
    //         for(var i=0;i<isdcodes.length;i++){
    //             $("#isdCode").append($("<option/>").text(isdcodes[i]).attr("value",isdcodes[i]));
    //         }
    //     }
    //     //电话号码(MOBN)
    //     if(isNotEmpty(data.phonecode)  && isEmpty($("#phoneCode").val())){
    //         var phonecodes = data.phonecode.split(";");
    //         $("#phoneCode").empty();
    //         for(var i=0;i<phonecodes.length;i++){
    //             $("#phoneCode").append($("<option/>").text(phonecodes[i]).attr("value",phonecodes[i]));
    //         }
    //     }
    //     //邮箱(EMAL)
    //     var email = $("#email").val();
    //     //电话号码(MOBN)
    //     var isdCode = $("#isdCode").val();
    //     var phoneCode = $("#phoneCode").val();
    //     //交易对手银行账号
    //     var cotpBankAcc = $("#cotpBankAcc").val();
    //     //交易对手银行账户名称
    //     if(data.cotpname){
    //         $("#cotpAccName").val(data.cotpname);
    //         $("#srcCotpAccNameTd").text(data.cotpname);
    //     }
    //     //交易对手银行账号为空
    //     if(isEmpty(cotpBankAcc)){
    //         if(isNotEmpty(email)){
    //             $("#cotpBankAcc").val(email);
    //
    //             //现金流信息
    //             $("#srcCotpBankAccTd").text(email);
    //             // $("#srcCotpAccNameTd").text("");
    //             $("#srcCotpBankTd").text("");
    //         } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
    //             $("#cotpBankAcc").val(isdCode + phoneCode);
    //
    //             //现金流信息
    //             $("#srcCotpBankAccTd").text(isdCode + phoneCode);
    //             // $("#srcCotpAccNameTd").text("");
    //             $("#srcCotpBankTd").text("");
    //         }
    //     }
    // }
    // bt.cotpCallback = function (data) {
    //     setCotp(data, true);
    // };


    //交易对手银行回调函数
    function setCotpBank(data, flag) {
        //交易对手银行所在国家地区
        if(data.countryname){
            $("#nameZhBankDiv").text(data.countryname);
        } else {
            $("#nameZhBankDiv").text('');
        }
        $("#countryId").val(data.countryid);
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
        //交易对手银行所在简码
        if(data.encode){
            $("#payeeEnCode").val(data.encode);
        }else{
            $("#payeeEnCode").val("");
        }
    }
    bt.cotpBankCallback = function (data) {
        setCotpBank(data, true);
    };



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
        $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('payMoney').val()));
    };

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //监听提交
    form.on('submit(submit)', function(data){
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        //邮箱(EMAL)
        var email = $("#email").val();
        //电话号码(MOBN)
        var isdCode = $("#isdCode").val();
        var phoneCode = $("#phoneCode").val();

        //产品选择“PAYPAL”时，邮箱/手机号字段不能同时为空
        if(isEmpty(email) && (isEmpty(isdCode) || isEmpty(phoneCode))){
            layer.alert(bt.lang("fex.fexTranReq.message.paypalProduct.email"), {icon: 2});
            return false;
        }
         if(isNotEmpty(email)){
            $("#cotpBankAcc").val(email);

            $("#saveFields").val("EMAL");

        } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
            $("#cotpBankAcc").val(isdCode + " " + phoneCode);
            $("#saveFields").val("MOBN");
        }

        bt.loading.show();
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
        var signDataMsg = signData("外汇交易提交", 11);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        //控制重复提交
        if($(data.elem).parent().hasClass('isSubmiting')){
            bt.loading.hide();
            return false;
        }
        //增加已提交的标志
        $(data.elem).parent().addClass('isSubmiting');
        var action = "/foreign/fextranreq/save";
        var corpAccCurId = $("#corpAccCurId").val();//主体单位账号币种
        var cotpAccCurId = $("#cotpAccCurId").val();//交易对手账号币种
        if (corpAccCurId != cotpAccCurId) {
            //跨币种交易，是否继续?
            bt.loading.hide();
            parent.layer.confirm(bt.lang('foreign.message.confirm.cur'), function (index) {
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
        $("#cotpAdrrDiv").text('');
        $("#corpAccNameDiv").text('');
        $("#cotpBankAdrrDiv").text('');
        $("#cotpSwiftCodeDiv").text('');
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
