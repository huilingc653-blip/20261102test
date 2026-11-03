var form, element, corp, dateType, selectType, moneyType, rateType, dicts, corpBankAccInit;
bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType", "dicts", "selectTableType",
    "selectTable", "btUpload","region","allExternalBankAcc"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    var allExternalBankAcc=bt.allExternalBankAcc.init();
    bt.externalBankAccBeforeClick = function () {
        allExternalBankAcc.set({
            "type": "outerBankAcc,fps",
            'isCustom':'1'
        });
        return true;
    }
    // 加载主页面数据
    var mainInfo = parent.getMainInfo();
    $("#appId").val(mainInfo.appId);
    $("#corpId").val(mainInfo.corpId);
    //省回调函数
    bt.provCallback = function (value, othis, options) {
        $('#cotpProv').val(value);
        $('#cotpCity').val("");
    };
    //市回调函数
    bt.cityCallback = function (value, othis, options) {
        $('#cotpCity').val(value);
    };
    bt.cotpCountryCallBack = function (data) {
        setAddress(data.id, data.namezh, '');
    }
    // 加载主页面数据
    var mainInfo = parent.getMainInfo();
    $("#appId").val(mainInfo.appId);
    region = bt.region.init();
    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }
    //交易对手回调函数
    function setCotp(data, flag) {
        //交易对手所在国家地区
        setAddress(data.countryid, data.countryname,data.address);
        if(data.countryid){
            //$("#nameZhDiv").text(data.countryname);
            selectType.get('cotpCountry').val(data.countryid);
            setTimeout(function () {
                selectType.get('cotpAddress').val(data.address);
            }, 500)
            region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: data.prov
            }, {
                name: 'cotpCity',
                defaultValue: data.city
            }]);
        } else {
            //$("#nameZhDiv").text('');
            selectType.get('cotpCountry').val('');
            setTimeout(function () {
                selectType.get('cotpAddress').val('');
            }, 500)
            region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: ''
            }, {
                name: 'cotpCity',
                defaultValue: ''
            }]);
        }
        //交易对手地址
        if(data.addressen){
            $("#cotpAdrrDiv").text(data.addressen);
        } else {
            $("#cotpAdrrDiv").text('');
        }
        //现金流信息
        $("#srcCotpTd").text(data.cotpname);
        //FPS ID(SVID)
        if(isNotEmpty(data.fpsid) && isEmpty($("#fpsId").val())){
            var fpsIds = data.fpsid.split(";");
            $("#fpsId").empty();
            for(var i=0;i<fpsIds.length;i++){
                $("#fpsId").append($("<option/>").text(fpsIds[i]).attr("value",fpsIds[i]));
            }
        }
        //email
        if(isNotEmpty(data.email) && isEmpty($("#email").val())){
            var emails = data.email.split(";");
            $("#email").empty();
            for(var i=0;i<emails.length;i++){
                $("#email").append($("<option/>").text(emails[i]).attr("value",emails[i]));
            }
        }
        //isdCode国家代码
        if(isNotEmpty(data.isdcode) && isEmpty($("#isdCode").val())){
            var isdcodes = data.isdcode.split(";");
            $("#isdCode").empty();
            for(var i=0;i<isdcodes.length;i++){
                $("#isdCode").append($("<option/>").text(isdcodes[i]).attr("value",isdcodes[i]));
            }
        }
        //电话号码(MOBN)
        if(isNotEmpty(data.phonecode) && isEmpty($("#phoneCode").val())){
            var phonecodes = data.phonecode.split(";");
            $("#phoneCode").empty();
            for(var i=0;i<phonecodes.length;i++){
                $("#phoneCode").append($("<option/>").text(phonecodes[i]).attr("value",phonecodes[i]));
            }
        }
        //信用卡号(AIIN)
        if(isNotEmpty(data.aiin) && isEmpty($("#aiIn").val())){
            var aiins = data.aiin.split(";");
            $("#aiIn").empty();
            for(var i=0;i<aiins.length;i++){
                $("#aiIn").append($("<option/>").text(aiins[i]).attr("value",aiins[i]));
            }
        }
        //香港身份证号(HKID)
        if(isNotEmpty(data.hkid) && isEmpty($("#hkId").val())){
            var hkids = data.hkid.split(";");
            $("#hkId").empty();
            for(var i=0;i<hkids.length;i++){
                $("#hkId").append($("<option/>").text(hkids[i]).attr("value",hkids[i]));
            }
        }
        //FPS ID(SVID)
        var fpsId = $("#fpsId").val();
        //邮箱(EMAL)
        var email = $("#email").val();
        //电话号码(MOBN)
        var isdCode = $("#isdCode").val();
        var phoneCode = $("#phoneCode").val();
        //信用卡号(AIIN)
        var aiIn = $("#aiIn").val();
        //香港身份证号(HKID)
        var hkId = $("#hkId").val();
        //交易对手银行账号
        var cotpBankAcc = $("#cotpBankAcc").val();
        //交易对手银行账号为空
        if(isEmpty(cotpBankAcc)){
            if(isNotEmpty(fpsId)){
                $("#cotpBankAcc").val(fpsId);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");
                selectType.get("cotpBank").val("");

            } else if(isNotEmpty(email)){
                $("#cotpBankAcc").val(email);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");
                selectType.get("cotpBank").val("");

            } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
                $("#cotpBankAcc").val(isdCode + phoneCode);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");
                selectType.get("cotpBank").val("");

            }  else if(isNotEmpty(aiIn)) {
                $("#cotpBankAcc").val(aiIn);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");
                selectType.get("cotpBank").val("");

            } else if(isNotEmpty(hkId)) {
                $("#cotpBankAcc").val(hkId);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");
                selectType.get("cotpBank").val("");
            }
        }
    }
    bt.cotpCallback = function (data) {
        setCotp(data, true);
    };
    bt.cotpBankCallback = function (data) {
        setCotpBank(data, true);
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
        //交易对手银行所在简码
        if(data.encode){
            $("#payeeEnCode").val(data.encode);
        }else{
            $("#payeeEnCode").val("");
        }
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
    //设置交易对手fpsid的值
    setFpsId();
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.init();
    dicts = bt.dicts.init();
    var gid = $("#gid").val();
    upload = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    //监听提交
    form.on("submit(submit)", function (data) {
        var action = "/foreign/fexTranApp/saveDtl";
        data.field.attachmentId = upload.getGroupId();
        var payerEnCode = $("#payerEnCode").val();
        var payeeEnCode = "HK";
        if(payerEnCode != "HK"){
            layer.alert(bt.lang("fex.fexTranReq.message.HKProduct"), {icon: 2});
            return false;
        }
        if(payeeEnCode != "HK"){
            layer.alert(bt.lang("fex.fexTranReq.message.HKProduct"), {icon: 2});
            return false;
        }
        //FPS ID(SVID)
        var fpsId = $("#fpsId").val();
        //邮箱(EMAL)
        var email = $("#email").val();
        //电话号码(MOBN)
        var isdCode = $("#isdCode").val();
        var phoneCode = $("#phoneCode").val();
        //信用卡号(AIIN)
        var aiIn = $("#aiIn").val();
        //香港身份证号(HKID)
        var hkId = $("#hkId").val();
        //交易对手银行账号
        var cotpBankAcc = $("#cotpBankAcc").val();

        //产品选择“香港FPS产品”时，交易对手FPS ID(SVID)、邮箱(EMAL)、手机号(MOBN)、信用卡号(AIIN)、香港身份证号(HKID)、交易对手银行账户不能同时为空
        if(isEmpty(fpsId) && isEmpty(email) && (isEmpty(isdCode) || isEmpty(phoneCode)) && isEmpty(aiIn) && isEmpty(hkId) && isEmpty(cotpBankAcc)){
            layer.alert(bt.lang("fex.fexTranReq.message.HKProduct.cotpBankAcc"), {icon: 2});
            return false;
        }

        if(isEmpty(cotpBankAcc)){
            if(isNotEmpty(fpsId)){
                $("#cotpBankAcc").val(fpsId);
                // $("#cotpAccName").val("");
                // $("#cotpAccId").val("");
                // selectType.get("cotpBank").val("");

                $("#saveFields").val("SVID");

            } else if(isNotEmpty(email)){
                $("#cotpBankAcc").val(email);
                // $("#cotpAccName").val("");
                // $("#cotpAccId").val("");
                // selectType.get("cotpBank").val("");

                $("#saveFields").val("EMAL");

            } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
                $("#cotpBankAcc").val(isdCode + " " + phoneCode);
                // $("#cotpAccName").val("");
                // $("#cotpAccId").val("");
                // selectType.get("cotpBank").val("");
                $("#saveFields").val("MOBN");

            }  else if(isNotEmpty(aiIn)) {
                $("#cotpBankAcc").val(aiIn);
                // $("#cotpAccName").val("");
                // $("#cotpAccId").val("");
                // selectType.get("cotpBank").val("");
                $("#saveFields").val("AIIN");

            } else if(isNotEmpty(hkId)) {
                $("#cotpBankAcc").val(hkId);
                // $("#cotpAccName").val("");
                // $("#cotpAccId").val("");
                // selectType.get("cotpBank").val("");
                $("#saveFields").val("HKID");

            }
        }else{
            $("#saveFields").val("BBAN");
        }
        data.field.cotpBankAcc =  $("#cotpBankAcc").val();
        data.field.saveFields =  $("#saveFields").val();

        bt.loading.show();
        console.log(data.field);
        console.log(email)
        console.log($("#email").val())
        $.ajax({
            url: basePath + action,
            type: "post",
            data: data.field,
            dataType: "json",
            async: false,
            success: function (result) {
                bt.loading.hide();
                if (result != null) {
                    parent.closeTab(result);
                }
            }
        });
        return false;
    });

    //主体账户信息
    corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getCorpBankAcc",
        urldata: {corpId: $("#corpId").val(), curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#corpBankAcc",
        paging: true,
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ],
        clickCallback: function (data) {
            if (data) {
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
            } else {
                $("input.text").val("");
            }
        }
    });


    //交易编码
    var tranCodeInit1 = bt.selectTableType.init({
        elem: ".tranName1",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "tranCode", title: bt.lang("table.fexTranInfo.tranCode")},
            {data: "tranName", title: bt.lang("table.fexTranInfo.tranName")}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName1').val(data.tranName);
                $('#tranCode1').val(data.tranCode);
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: dicts.get("cdSign").val()},
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
            }
        }
    });


    //交易编码
    var tranCodeInit2 = bt.selectTableType.init({
        elem: ".tranName2",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "tranCode", title: bt.lang("table.fexTranInfo.tranCode")},
            {data: "tranName", title: bt.lang("table.fexTranInfo.tranName")}
        ],
        clickCallback: function (data) {
            if (data) {
                if (data.tranName == $("#tranName1").val() && data.tranCode == $("#tranCode1").val()) {
                    parent.parent.layer.alert(bt.lang("fex.foreign.message.trancode.same"), {icon: 2});
                } else {
                    $("#tranName2").val(data.tranName);
                    $("#tranCode2").val(data.tranCode);
                    var curId = selectType.get("curId").val();
                    if (isEmpty(curId)) {
                        parent.parent.layer.alert(bt.lang("common.select.currency"), {icon: 2});
                        return false;
                    }
                    moneyType.get("tranAmt2").attr("lay-verify", "required");
                    selectType.get("tranCurId2").attr({
                        "data-value": curId, "data-required": true,
                        "data-disabled": true
                    });
                    $("#tranAddWord2").attr("lay-verify", "required");

                    //获取交易附言
                    $.ajax({
                        url: basePath + "/foreign/fexAddWord/getFexAddWord",
                        method: 'POST',
                        data: {tranCode: data.tranCode, cdSign: dicts.get("cdSign").val()},
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
                }
                form.renderIcon();
            }
        }
    });
    //这里填写新的js代码，请参考前端文档
});

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 交易对手西选择回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function setCotp(data, elem, beforValue, wrapObj) {
    $("#postAddr").html(data.postaddr);
    $("#country").html(data.countryname)
}


/**
 * 设置显示交易对手信息
 * @param data
 * @param elem
 * @param beforevalue
 * @param wrapObj
 */
/*
function cotpBankAccCallBack(data, elem, beforevalue, wrapObj) {
    $("#cotpbankAddr").html(data.bankaddr);//交易对手银行地址
    $("#swiftCode").html(data.biccode);//swiftCode
    $("#agentBank").html(data.tranbankname);//交易对手代理行
    $("#agentBankAddr").html(data.tranbankaddr);//交易对手代理行地址
    $("#agentBankAcc").html(data.tranbankacc);//交易对手代理行账户
    $("#cotpAccId").val(data.id);
    selectType.get("cotpId").val(data.cotpid);
    selectType.get("cotpBank").val(data.bankid);
}
*/

/**
 * 校验被代付单位是否与主单位相同
 * @param id
 */
function payCorpIdCallBack(id) {
    var corpId = $("#corpId").val();
    if (id == corpId) {
        parent.parent.layer.alert(bt.lang("不能与主体单位相同"), {icon: 2});
        corp.get("payCorpId").val("");
    }
}

/**
 * 国内外费用承担方回调
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function costSignCallBack() {
    var cdSign = dicts.get("cdSign").val();
    var costSign = dicts.get("costSign").val();
    // 收款时,收款方承担费用,则费用账号必填,付款时,付款方承担费用,则费用账号必填
    if ((costSign == 1 && cdSign == 1)||(costSign == 2 && cdSign == 0)) {
        $("#costAcc").attr("lay-verify", "required");
    }else {
        $("#costAcc").attr("lay-verify", "");
    }
    form.renderIcon();
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


/**
 * 清空交易编码2时设置相应币种2,金额2,附言2非必填
 * @param obj
 */
function setTran2(obj) {
    if (isEmpty(obj.value)) {
        $("#tranCode2").val("");
        moneyType.get("tranAmt2").attr("lay-verify", "");
        selectType.get("tranCurId2").attr({"data-value": "", "data-required": false,
            "data-disabled": false});
        $("#tranAddWord2").attr("lay-verify", "");
        form.renderIcon();
    }
}

/**
 * 币种回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function curCallBack(data, elem, beforValue, wrapObj) {
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    selectType.get("tranCurId1").val(data.id);
    selectType.get("tranCurId2").val(data.id);
    var curId = selectType.get('curId').val();
    var corpBank = selectType.get('corpBank').val();
    var  corpId = $("#corpId").val();
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {corpId:corpId,curId: curId, bankType: corpBank},
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ]
    });
    form.render();
}

/**
 *主体单位银行回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function corpBankCallBack(data, elem, beforValue, wrapObj) {
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    var curId = selectType.get('curId').val();
    var corpBank = selectType.get('corpBank').val();
    var corpId = $("#corpId").val();
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {corpId:corpId,curId: curId, bankType: corpBank},
        columns: [
            {data:"bankAcc"},
            {data:"accName"},
            {data:"bankName"},
            {data:"curEnglishCode"}
        ]
    });
    form.render();
}

/**
 * 付款类型回调
 */
function payTypeCallBack() {
    var payType = dicts.get("payType").val();
    if ("1" == payType) {
        dateType.get("shipDate").attr("lay-verify", "required");
    } else {
        dateType.get("shipDate").attr("lay-verify", "");
    }
    form.renderIcon();
}

function setFpsId(){
    var fpsIdStr = $("#fpsIds").val();
    var eMailStr = $("#eMails").val();
    var isdCodeStr = $("#isdCodes").val();
    var phoneCodeStr = $("#phoneCodes").val();
    var aiInStr = $("#aiIns").val();
    var hkIdStr = $("#hkIds").val();
    //FPS ID(SVID)
    if(isNotEmpty(fpsIdStr)){
        var fpsIds = fpsIdStr.split(";");
        $("#fpsId").empty();
        for(var i=0;i<fpsIds.length;i++){
            $("#fpsId").append($("<option/>").text(fpsIds[i]).attr("value",fpsIds[i]));
        }
        $("#fpsId").val($("#fpsIdhid").val());
    }
    //email
    if(isNotEmpty(eMailStr)){
        var emails = eMailStr.split(";");
        $("#email").empty();
        for(var i=0;i<emails.length;i++){
            $("#email").append($("<option/>").text(emails[i]).attr("value",emails[i]));
        }
        $("#email").val($("#eMailhid").val());
    }
    //isdCode国家代码
    if(isNotEmpty(isdCodeStr)){
        var isdcodes = isdCodeStr.split(";");
        $("#isdCode").empty();
        for(var i=0;i<isdcodes.length;i++){
            $("#isdCode").append($("<option/>").text(isdcodes[i]).attr("value",isdcodes[i]));
        }
        $("#isdCode").val($("#isdCodehid").val());
    }
    //电话号码(MOBN)
    if(isNotEmpty(phoneCodeStr)){
        var phonecodes = phoneCodeStr.split(";");
        $("#phoneCode").empty();
        for(var i=0;i<phonecodes.length;i++){
            $("#phoneCode").append($("<option/>").text(phonecodes[i]).attr("value",phonecodes[i]));
        }
        $("#phoneCode").val($("#phoneCodehid").val());
    }
    //信用卡号(AIIN)
    if(isNotEmpty(aiInStr)){
        var aiins = aiInStr.split(";");
        $("#aiIn").empty();
        for(var i=0;i<aiins.length;i++){
            $("#aiIn").append($("<option/>").text(aiins[i]).attr("value",aiins[i]));
        }
        $("#aiIn").val($("#aiInhid").val());
    }
    //香港身份证号(HKID)
    if(isNotEmpty(hkIdStr)){
        var hkids = hkIdStr.split(";");
        $("#hkId").empty();
        for(var i=0;i<hkids.length;i++){
            $("#hkId").append($("<option/>").text(hkids[i]).attr("value",hkids[i]));
        }
        $("#hkId").val($("#hkIdhid").val());
    }
}

/**
 * 交易对手账户回调
 * @param data
 */
function externalBankAccCallback(data) {
    if(data.type=="outerBankAcc") {
        $("#cotpBankAcc").val(data.externalAcc);//账号
        $("#cotpAccName").val(data.externalAccName);//账号
        $("#cotpNameEn").val(data.ecNameEn);//名称英文
        $("#oppId").val(data.externalCorpId);//对手原id
        $("#cotpName").val(data.ecName);//对手名称

        $("#cotpAddressEn").val(data.address)//交易对手地址
        $("#cotpAdrrDiv").html(data.address);
        $("#cotpCountry").val(data.countryId)//交易对手国家地区
        if (isNotEmpty(data.country)) {
            $("#cotpCountryDiv").html(data.country);
        }
        $("#cotpBankName").val(data.bank);
        $("#cotpBankNameDiv").html(data.bank);
        $("#cotpAccCurId").val(data.curId);
        $("#cotpAccCurNameDiv").html(data.englishCode);
        $("#cotpBicCode").val(data.swiftCode);
        $("#cotpBicCodeDiv").html(data.swiftCode);
        $("#localClearNo").val(data.localClearNo);
        $("#localClearNoDiv").html(data.localClearNo);
        $("#cotpBankAddr").val(data.bankAddress)//交易对手银行地址
        $("#cotpBankAdrrDiv").html(data.bankAddress);
        $("#cotpBankCountry").val(data.bankOutCountryId)//交易对手银行国家地区
        if (isNotEmpty(data.bankCountry)) {
            $("#cotpBankCountryName").val(data.bankCountry)//交易对手银行国家地区
            $("#cotpBankCountryDiv").html(data.bankCountry);
        }

        $("#fpsId").val("");
        $("#email").val("");
        $("#isdCode").val("");
        $("#phoneCode").val("");
        $("#phoneCodeDiv").val("");
        $("#aiIn").val("");
        $("#hkId").val("");

    }else if(data.type=="fps"){
        $("#cotpBankAcc").val(data.externalAcc);//账号
        $("#cotpNameEn").val(data.ecNameEn);//名称英文
        $("#oppId").val(data.externalCorpId);//对手原id
        $("#cotpName").val(data.ecName);//对手名称

        $("#cotpAddressEn").val(data.addressEn)//交易对手地址
        $("#cotpAdrrDiv").html(data.addressEn);

        $("#cotpCountry").val(data.countryId)//交易对手国家地区
        if(isNotEmpty(data.countryName)) {
            $("#cotpCountryDiv").html(data.countryName);
        }

        $("#fpsId").val(data.fpsId);
        $("#email").val(data.email);
        $("#isdCode").val(data.countryCode);
        $("#phoneCode").val(data.tel);
        $("#phoneCodeDiv").val(data.phone);
        $("#aiIn").val(data.creditCard);
        $("#hkId").val(data.hkId);

        $("#cotpBankName").val("");
        $("#cotpBankNameDiv").html("");
        $("#cotpAccCurId").val("");
        $("#cotpAccCurNameDiv").html("");
        $("#cotpBicCode").val("");
        $("#cotpBicCodeDiv").html("");
        $("#localClearNo").val("");
        $("#localClearNoDiv").html("");
        $("#cotpBankAddr").val("")//交易对手银行地址
        $("#cotpBankAdrrDiv").html("");
        $("#cotpBankCountry").val("")//交易对手银行国家地区
        $("#cotpBankCountryName").val("")
        $("#cotpBankCountryDiv").html("");
    }
}
