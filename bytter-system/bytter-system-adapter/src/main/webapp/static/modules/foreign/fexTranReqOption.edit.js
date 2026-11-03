var selectType,dict,moneyFun,moneyType;
var cacheCurrecyList = null;
bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','ratioType','radioType','element', 'selectTable','dateType','corp', 'btUpload','allBankInfo','money'], function(){
    var $ = bt.jquery,form = bt.form();
    //判断单剧来源
    var billFrom = $('#billFrom').val();
    //这里给后续测试发现问题时，发现某些字段会影响其它联动字段，可用这个字段屏蔽联动
    var isEidt = true;
    //如果是交易需求来源
    if(billFrom == '1'){
        isEidt = false;
        //单位不可更改 data-flag = readonly
        if($('#corpId').data('flag') != 'readonly'){
            $('[data-name="corpId"]').data("disabled","true");
        }
        //产品不可更改
        $('[data-name="proId"]').data("disabled","true");
        //交易货币对不可更改
        $('[data-name="tranCurPairId"]').data("disabled","true");
        //买卖方向不可更改
        $('[data-name="busDirection"]').data("disabled",true);
        //买入金额不可更改
        $('[data-name="buyMoney"]').data("disabled","true");
        //卖出金额不可更改
        $('[data-name="sellMoney"]').data("disabled","true");
        //涨跌方向不可更改
        $('[data-name="upDownDirection"]').data("disabled","true");
        //执行汇率不可更改
        $('[data-name="contractRate"]').data("disabled","true");
        //交易日期不能更改
        $('[data-name="settleDate"]').data("disabled","true");
    }

    //获取卖出币种
    var sellCurId = $('[data-name="sellCurId"]').data('value');
    //获取交易货币对左侧币种
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    var targetDataName = sellCurId == tranCurPairBaseId?'sellMoney':'buyMoney'
    var money =  $('[data-name="'+targetDataName+'"]').val();
    $('[data-name="positionMoney"]').val(money)

    // 结算日期 callback
    bt.settleDateCallback = function () {
        settleDateCallback()
    }
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.rateType.init();
    bt.ratioType.init();
    bt.radioType.init();
    bt.allBankInfo.init();

   // moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();


    getCurrecyList();
    var defaultDisplayPremiumValue = $('#tranCurPairBaseId').val() +','+ $('#tranCurPairReferId').val()
    var premiumCurHtml = creatCurrecySelectHtml(defaultDisplayPremiumValue,$('#defaulPremiumCur').val());
    $('select[name="premiumCur"]').html(premiumCurHtml);
    $('#defaulPremiumCur').remove();
    form.on('select(premiumCur)',function () {
        premiumCurCallback()
    })

    //买入方向回调函数
    bt.busDirectionCallback = function(data){
        //加载主体单位和交易对手账户信息
        //买入方向：1买入，2卖出
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        var busDirection = bt.dicts.get('busDirection').val();
        if(busDirection == "2") {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('premiumMoney').val()));
            //加载主体单位和交易对手账户信息
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        }else{
            $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('premiumMoney').val()));
            //加载主体单位和交易对手账户信息
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
        }
        //加载资金计划
        renderBudget();
        //如果（买卖方向=买入&涨跌方向=看涨）or（买卖方向=卖出&涨跌方向=看跌），买入币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        //如果（买卖方向=卖出&涨跌方向=看涨）or（买卖方向=买入&涨跌方向=看跌），卖出币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        resetBuyCurId();
        resetSellCurId()
    }
    //涨跌方向回调函数
    bt.updownDirectionCallback = function(data){
        //如果（买卖方向=买入&涨跌方向=看涨）or（买卖方向=卖出&涨跌方向=看跌），买入币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        //如果（买卖方向=卖出&涨跌方向=看涨）or（买卖方向=买入&涨跌方向=看跌），卖出币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        resetBuyCurId();
        resetSellCurId()
    }

    selectType = bt.selectType.init();
    dict = bt.dicts.init();
    //主体账户信息
    var corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getCorpBankAcc",
        urldata: {corpId: $('#corpIdDef').val(), curId: $('#curIdDef').val(), bankType: ''},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#corpBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccId').val(data.id);
                $('#corpAccName').val(data.accName);
                $('#corpBank').val(data.bankTypeId);
                if (data.bankAcc) {
                    $("#corpBankNameDiv").text(data.bankTypeName);
                    $("#srcBankTd").text(data.bankTypeName);
                    $("#srcAccNameTd").text(data.accName);
                    $("#srcBankAccTd").text(data.bankAcc);
                }else{
                    $("#corpBankNameDiv").text("");
                    $("#srcBankTd").text("");
                    $("#srcAccNameTd").text("");
                    $("#srcBankAccTd").text("");
                }
            }else{
                $('input.text').val('');
            }
        }
    });

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
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

    //交易需求过来的数据，进入页面自动匹配主体账号
    if($("#tranAppDtlId").val() && $("#corpAccId").val()==""){
        //加载主体单位和交易对手账户信息
        //买入方向：1买入，2卖出
        var busDirection = bt.dicts.get('busDirection').val();
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busDirection == "2") {
            // renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#cotpIdDef').val());
            // renderBankAcc("1", $('#cotpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#corpIdDef').val());

            renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", oppId);
            renderBankAcc("1", oppId, $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#corpIdDef').val());
        }else {
            // renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#cotpIdDef').val());
            // renderBankAcc("1", $('#cotpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#corpIdDef').val());

            renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", oppId);
            renderBankAcc("1", oppId, $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#corpIdDef').val());
        }
    }

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
                        //加载资金计划
                        renderBudget();
                        //重新加载源账户信息
                        corpBankAccInit.get("corpBankAcc").reload({corpId: id, curId: bt.selectType.get('curId').val(), bankType: ''});
                        //加载主体单位和交易对手账户信息
                        //买入方向：1买入，2卖出
                        var busDirection = bt.dicts.get('busDirection').val();
                        var oppId = $("#oppId").val();
                        if (isEmpty(oppId)) {
                            oppId = null;
                        }
                        if(busDirection == "2") {
                            // renderBankAcc("0", id, selectType.get("curId").val(), selectType.get("proId").val(), "1", "0", selectType.get("cotpId").val());
                            renderBankAcc("0", id, selectType.get("curId").val(), selectType.get("proId").val(), "1", "0", oppId);
                        }else {
                            // renderBankAcc("0", id, selectType.get("curId").val(), selectType.get("proId").val(), "1", "1", selectType.get("cotpId").val());
                            renderBankAcc("0", id, selectType.get("curId").val(), selectType.get("proId").val(), "1", "1", oppId);
                        }
                        //现金流
                        $("#srcCorpTd").text(data.obj.name);
                    }
                    //给单位本位币赋值
                    $("#corpCurId").val(data.obj.curId);
                    //判断折本位币的隐藏和现实
                    showHideBaseConvertTr()
                }
                form.render();
            }
        });
    };
    //初始化单位
    var corp = bt.corp.init();

    bt.cotpBankCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.bankName);
                $("#cotpBankNameDiv").text(data.bankName);
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);
            } else if (data.type === "outerBank") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.bankName);
                $("#cotpBankNameDiv").text(data.bankName);
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);

            } else {
                $("#oppId").val("");
                $("#cotpName").val("");
                $("#cotpBankNameDiv").text("");
                $("#srcCotpTd").text("");
                $("#srcCotpBankTd").text("");
            }
        } else {
            $("#oppId").val("");
            $("#cotpName").val("");
            $("#cotpBankNameDiv").text("");
            $("#srcCotpTd").text("");
            $("#srcCotpBankTd").text("");
        }
        //加载主体单位和交易对手账户信息
        //买入方向：1买入，2卖出
        var busDirection = bt.dicts.get('busDirection').val();
        var oppId = data.id;
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busDirection == "2") {
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), selectType.get("proId").val(), "1", "0", oppId);
        }else{
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), selectType.get("proId").val(), "1", "1", oppId);
        }
    }

    $('#cotpBankAcc').on('blur',function () {
        var value = $(this).val();
        if (isNotEmpty(value)) {
            $("#srcCotpBankAccTd").text(value);
        } else {
            $("#srcCotpBankAccTd").text('');
        }
    });

    //交易对手回调函数
    // bt.cotpCallback = function (data) {
    //     $("#srcCotpTd").text(data.cotpname);
    //     setCotpBankAcc();
    //     //加载主体单位和交易对手账户信息
    //     //买入方向：1买入，2卖出
    //     var busDirection = bt.dicts.get('busDirection').val();
    //     if(busDirection == "2") {
    //         renderBankAcc("1", data.id, selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
    //     }else{
    //         renderBankAcc("1", data.id, selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
    //     }
    // };

    //交易对手银行账户
    // function setCotpBankAcc(){
    //     var cotpId = selectType.get("cotpId").val();
    //     selectType.get("cotpBankAcc").attr({"data-value": "",
    //         "data-url": basePath + "/foreign/fexcotpbankacc/select?cotpId=" + cotpId});
    //     selectType.render();
    // }

    //交易对手银行账户
    // bt.cotpBankAccCallback = function (data) {
    //     $('#cotpBankAcc').val(data.bankacc);
    //     $('#cotpAccId').val(data.id);
    //     $('#cotpAccName').val(data.accname);
    //     $('#cotpBank').val(data.bankid);
    //     if (data.bankacc) {
    //         $("#cotpBankNameDiv").text(data.bankname);
    //         $("#srcCotpBankTd").text(data.bankid);
    //         $("#srcCotpAccNameTd").text(data.accname);
    //         $("#srcCotpBankAccTd").text(data.bankacc);
    //     }else{
    //         $("#cotpBankNameDiv").text("");
    //         $("#srcCotpBankTd").text("");
    //         $("#srcCotpAccNameTd").text("");
    //         $("#srcCotpBankAccTd").text("");
    //     }
    // }

    //产品回调函数
    bt.proCallback = function (data) {
        $("#fexOption1").val(data.fexoption1);
        $("#fexOption2").val(data.fexoption2);
        $("#fexOption3").val(data.fexoption3);
        var fexOption = bt.dict("fexProduct.fexOption1", data.fexoption1)+" "+bt.dict("fexProduct.fexOption2", data.fexoption2)+" "+bt.dict("fexProduct.fexOption3", data.fexoption3);
        $("#fexOptionDiv").text(fexOption);

        $("#fexOptionType").val(data.fexoptiontype);
        $("#fexOptionTypeDiv").text(bt.dict("fexProduct.fexOptionType", data.fexoptiontype));

        if(data.buycurid){
            selectType.get('buyCurId').val(data.buycurid);
        }else{
            selectType.get('buyCurId').val('');
        }
        if(data.sellcurid){
            selectType.get('sellCurId').val(data.sellcurid);
        }else{
            selectType.get('sellCurId').val('');
        }

        //设置期权费币种
        if(selectType.get("buyCurId").val()!=null && selectType.get("sellCurId").val()!=null){
            var premiumCur = selectType.get("buyCurId").val()+','+selectType.get("sellCurId").val();
            if(selectType.get("buyCurId").val() == selectType.get("sellCurId").val()){
                premiumCur = selectType.get("buyCurId").val();
            }
            //selectType.get("premiumCur").attr({"data-disabled":"","data-displayData":premiumCur})
            var premiumCurHtml = creatCurrecySelectHtml(premiumCur);
            $('select[name="premiumCur"]').html(premiumCurHtml);
        }

        //结算方式
        bt.dicts.get('tranSettleMethod').val(data.transettlemethod);
        //期权费折算汇率
        bt.dicts.get('premiumRate').val(data.fexoptionrate);

        //外汇期权品种1 0-普通期权、1-奇异期权
        //外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权
        //外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)

        //障碍验证标准：外汇期权品种为“2-单障碍期权”和“3-双障碍期权”才显示
        //障碍信息：外汇期权品种为“2-单障碍期权”和“3-双障碍期权”才显示
        if(data.fexoption2=="2" || data.fexoption2=="3"){
            $("#validStandardTdDiv").show();
            $("#validStandardDiv").show();
            bt.dicts.get('validStandard').attr({'data-required': true});
            bt.dicts.get('validStandard').val(data.validstandard);
            $("#exceptInfoDiv").show();
            //moneyType.get('exceptLowVal').attr('lay-verify', 'required|number2');
            if(data.fexoption2=="2"){
                //$("#exceptValTdDiv").text(bt.lang('table.fexTranReq.exceptVal'));//障碍值
                //moneyType.get('exceptLowVal').attr('placeholder', bt.lang('table.fexTranReq.exceptVal'));
                /*$("#exceptHignValTdDiv").hide();
                $("#exceptHignValDiv").hide();
                $("#exceptLowValTdDiv").hide();
                $("#exceptHignValDiv").hide();*/
                $("#exceptValTrDiv").show();
                $("#exceptValDoubleTrDiv").hide();
                moneyType.get('exceptVal').attr('lay-verify', 'required|number2');
                moneyType.get('exceptHignVal').attr('lay-verify', 'number2');
                moneyType.get('exceptLowVal').attr('lay-verify', 'number2');
            }else{
                //$("#exceptLowValTdDiv").text(bt.lang('table.fexTranReq.exceptLowVal'));//低障碍值
                //moneyType.get('exceptLowVal').attr('placeholder', bt.lang('table.fexTranReq.exceptLowVal'));
                //$("#exceptHignValTdDiv").show();
                // $("#exceptHignValDiv").show();
                //$("#exceptLowValTdDiv").show();
                //$("#exceptHignValDiv").show();
                $("#exceptValTrDiv").hide();
                $("#exceptValDoubleTrDiv").show();
                moneyType.get('exceptHignVal').attr('lay-verify', 'required|number2');
                moneyType.get('exceptLowVal').attr('lay-verify', 'required|number2');
                moneyType.get('exceptVal').attr('lay-verify', 'number2');
            }
        }else{
            bt.dicts.get('validStandard').attr({'data-required': false});
            $("#validStandardTdDiv").hide();
            $("#validStandardDiv").hide();
            $("#exceptInfoDiv").hide();
            moneyType.get('exceptLowVal').attr('lay-verify', 'number2');
        }

        //平均期开始日期：外汇期权品种为“1-亚式期权”才显示
        //平均期结束日期：外汇期权品种为“1-亚式期权”才显示
        //平均期天数：外汇期权品种为“1-亚式期权”才显示
        if(data.fexoption2=="1"){
            $("#avgDateTr").show();
            $("#avgDaysTr").show();
        }else{
            $("#avgDateTr").hide();
            $("#avgDaysTr").hide();
        }

        //退款/折扣信息：外汇期权品种为“2-上涨失效单障碍期权(Up & Out)”和“3-下跌失效单障碍期权(Down & Out)”才显示
        if(data.fexoption3=="2" || data.fexoption3=="3"){
            $("#refundInfoDiv").show();
            //是否涉及退款、退款金额、退款支付时间设置必填/必选；退款点数、退款百分比非必输
            bt.dicts.get('isRefund').attr({'data-required': true});
            moneyType.get('refundPoint').attr('lay-verify', 'number2');
            bt.ratioType.get('refundRatio').currentObject.removeAttr('lay-verify');
            moneyType.get('refundMoney').attr('lay-verify', 'required|number2');
            bt.dicts.get('refundTime').attr({'data-required': true});
            bt.dicts.get('refundTime').val(data.refundtime);
        }else{
            $("#refundInfoDiv").hide();
            bt.dicts.get('isRefund').attr({'data-required': false});
            moneyType.get('refundPoint').attr('lay-verify', 'number2');
            bt.ratioType.get('refundRatio').currentObject.removeAttr('lay-verify');
            moneyType.get('refundMoney').attr('lay-verify', 'number2');
            bt.dicts.get('refundTime').attr({'data-required': false});
        }

        //在交易时展示经纪人 0-否、1-是
        if(data.isshowagent=="1"){
            $("#cotpAgentTitleTd").show();
            $("#cotpAgentTd").show();
        }else{
            $("#cotpAgentTitleTd").hide();
            $("#cotpAgentTd").hide();
        }

        //在交易时展示清算所 0-否、1-是
        if(data.isshowclearhouse=="1"){
            $("#cotpClearHouseTitleTd").show();
            $("#cotpClearHouseTd").show();
        }else{
            $("#cotpClearHouseTitleTd").hide();
            $("#cotpClearHouseTd").hide();
        }

        if((data.isshowagent=="1" && data.isshowclearhouse=="0") || (data.isshowagent=="0" && data.isshowclearhouse=="1")){
            $("#blankTitleTd").show();
            $("#blankTd").show();
        }else{
            $("#blankTitleTd").hide();
            $("#blankTd").hide();
        }

        //加载主体单位和交易对手账户信息
        //买入方向：1买入，2卖出
        var busDirection = bt.dicts.get('busDirection').val();
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busDirection == "2") {
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), data.id, "1", "0", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(), data.id, "1", "1", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), data.id, "1", "0", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(), data.id, "1", "1", $('input[name="corpId"]').val());
        }else {
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), data.id, "1", "1", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(), data.id, "1", "0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(), data.id, "1", "1", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(), data.id, "1", "0", $('input[name="corpId"]').val());
        }
        //根据产品的交割类型，带出页面的交割类型
        dict.get("deliveryType").val(data.deliverytype);
        $("#productBuyCurId").val(data.buycurid);
        //根据产品的买入币种和卖出币种根据货币对管理的配置带出货币对
        $.post(basePath + "/basic/business/btcurrencypair/getBtCurrencyPairByCurId/" + data.buycurid+"/"+data.sellcurid, function (result) {
            //设置货币对
            dict.get("tranCurPairId").val(result.obj.id);
            //这里直接将curid1作为左边货币储存，后端是根据baseCurId来判断谁是左边货币的
            if (result.obj.curId1 == result.obj.baseCurId){
                $("#tranCurPairBaseId").val(result.obj.curId1);
                $("#tranCurPairReferId").val(result.obj.curId2);
            }else{
                $("#tranCurPairBaseId").val(result.obj.curId2);
                $("#tranCurPairReferId").val(result.obj.curId1);
            }
            //货币对回调
            var obj = {
                curid1: result.obj.curId1,//左侧货币
                curid2: result.obj.curId2,//右侧货币
                basecurid: result.obj.baseCurId//基准货币
            }
            bt.currencyPairCallback(obj);
        }, 'JSON');
        //涨跌方向

        form.renderIcon();
        form.render();
    };
    //交易货币对回调函数
    bt.currencyPairCallback = function(data){
        if(!data.curid1 && !data.curid2 && !data.basecurid){
            bt.selectType.get('tranCurPairId').val('');
        }
        //这里直接将curid1作为左边货币储存，后端是根据baseCurId来判断谁是左边货币的
        var defaultPremiumCur;
        if (data.curid1 == data.basecurid){
            $("#tranCurPairBaseId").val(data.curid1);
            $("#tranCurPairReferId").val(data.curid2);
            defaultPremiumCur = data.curid2;
        }else{
            $("#tranCurPairBaseId").val(data.curid2);
            $("#tranCurPairReferId").val(data.curid1);
            defaultPremiumCur = data.curid1;
        }

        //如果产品的默认买入币种=交易货币对的左侧，则涨跌方向=看涨，否则=看跌
        var productBuyCurId = $("#productBuyCurId").val();
        if ($("#tranCurPairBaseId").val() == productBuyCurId){
            dict.get("updownDirection").val('1')
        }else{
            dict.get("updownDirection").val('0')
        }
        //设置报价方式
        setQuotationMethod(data.curid1,data.curid2)

        //如果（买卖方向=买入&涨跌方向=看涨）or（买卖方向=卖出&涨跌方向=看跌），买入币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        //如果（买卖方向=卖出&涨跌方向=看涨）or（买卖方向=买入&涨跌方向=看跌），卖出币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
        resetBuyCurId();
        resetSellCurId();

        //设置期权费币种
        if(selectType.get("buyCurId").val()!=null && selectType.get("sellCurId").val()!=null){
            var premiumCur = selectType.get("buyCurId").val()+','+selectType.get("sellCurId").val();
            if(selectType.get("buyCurId").val() == selectType.get("sellCurId").val()){
                premiumCur = selectType.get("buyCurId").val();
            }
            //selectType.get("premiumCur").attr({"data-disabled":"","data-displayData":premiumCur,"data-defaultValue":defaultPremiumCur})
            var premiumCurHtml = creatCurrecySelectHtml(premiumCur,defaultPremiumCur);
            $('select[name="premiumCur"]').html(premiumCurHtml);
        }

        //期权费金额(期权费币种)初始化默认值
        resetPremiumMoneyOfPremiumCur();
        //设置结算币种下拉内容
        changeSettleCurId();
        showHideBaseConvertTr1(defaultPremiumCur);
        resetPremiumMoneyOfCorpCur();
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

    /**
     * 合同汇率计算
     */
    // function calContractRate() {
    //     var buyCurId = selectType.get("buyCurId").val();
    //     var buyMoney = moneyType.get("buyMoney").val();
    //     var sellCurId = selectType.get("sellCurId").val();
    //     var sellMoney = moneyType.get("sellMoney").val();
    //     if (isNotEmpty(buyCurId) && isNotEmpty(buyMoney) && isNotEmpty(sellCurId) && isNotEmpty(sellMoney)) {
    //         $.ajax({
    //             "url": basePath + "/basic/business/btcurrencypair/getByCurId",
    //             data:{
    //                 curId1: buyCurId,
    //                 curId2: sellCurId
    //             },
    //             type:"post",
    //             dataType:"json",
    //             async: false,
    //             success:function (data) {
    //                 if (data.success) {
    //                     if (null == data.obj) {
    //                         parent.layer.alert(bt.lang("fex.tranApp.currencyPair"), {icon: 2});
    //                         moneyType.get("contractRate").val("");
    //                     } else {
    //                         setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data.obj);
    //                     }
    //                 }
    //             }
    //         });
    //     }
    // }

    /**
     * 汇率计算
     * @param buyCurId
     * @param sellCurId
     * @param buyMoney
     * @param sellMoney
     * @param data
     */
    function setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data) {
        var autoCal = $("input[type = 'checkbox'][name = 'autoCal"+"']:checked");
        var contractRate = moneyType.get("contractRate").val();
        if (isNotEmpty(contractRate)) {
            // 合同汇率存在值
            if (autoCal.length > 0) {//计算买入
                if (data.baseCurId == buyCurId) {//买入币种为标准
                    moneyType.get("buyMoney").val(bt.money.div(sellMoney, contractRate,2));
                } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                    moneyType.get("buyMoney").val(bt.money.mul(sellMoney, contractRate,2));
                }
            } else {//计算卖出
                if (data.baseCurId == buyCurId) {//买入币种为标准
                    moneyType.get("sellMoney").val(bt.money.mul(buyMoney, contractRate,2));
                } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                    moneyType.get("sellMoney").val(bt.money.div(buyMoney, contractRate,2));
                }
            }
        } else {
            // 合同汇率不存在值
            if (data.baseCurId == buyCurId) {//买入币种为标准
                moneyType.get("contractRate").val(bt.money.div(sellMoney, buyMoney,6));
            } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                moneyType.get("contractRate").val(bt.money.div(buyMoney, sellMoney,6));
            }
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

    //合同汇率回调函数
    bt.contractRateCallback = function (data) {
        // calContractRate();
        //初始化期权费金额（结算币种）
        resetPremiumMoneyOfSettle();
        //获取是否自动计算金额
        var autoCal = $('input[name="isPaymentAmount"]:checked').val();
        if (autoCal == 1){
            //计算卖出
            resetSellMoney();
        }
        //计算持仓金额
        calPositionMoney();

    };
    //币别回调函数
    bt.buyCurCallback = function (data) {
        // calContractRate();
        //设置期权费币种
        if(selectType.get("buyCurId").val()!=null && selectType.get("sellCurId").val()!=null){
            var data = selectType.get("buyCurId").val()+','+selectType.get("sellCurId").val();
            if(selectType.get("buyCurId").val() == selectType.get("sellCurId").val()){
                data = selectType.get("buyCurId").val();
            }
            //selectType.get("premiumCur").attr({"data-disabled":"","data-displayData":data})
            var premiumCurHtml = creatCurrecySelectHtml(data);
            $('select[name="premiumCur"]').html(premiumCurHtml);
        }
        //重新加载源账户信息
        corpBankAccInit.get("corpBankAcc").reload({corpId: $('input[name="corpId"]').val(), curId: bt.selectType.get('curId').val(), bankType: ''});
        form.render();
    };
    bt.sellCurCallback = function (data) {
        // calContractRate();
        //设置期权费币种
        if(selectType.get("buyCurId").val()!=null && selectType.get("sellCurId").val()!=null){
            var data = selectType.get("buyCurId").val()+','+selectType.get("sellCurId").val();
            if(selectType.get("buyCurId").val() == selectType.get("sellCurId").val()){
                data = selectType.get("buyCurId").val();
            }
            //selectType.get("premiumCur").attr({"data-disabled":"","data-displayData":data})
            var premiumCurHtml = creatCurrecySelectHtml(data);
            $('select[name="premiumCur"]').html(premiumCurHtml);
        }
        form.render();
    };

    //金额回调函数
    bt.buyMoneyCallback = function (data) {
        //回调判断货币对左侧币种是否对应买入币种，如果是，则反写买入金额给持仓金额
       if($("#billFrom").val() != '1'){
           var buyCurId = selectType.get("buyCurId").val();
           var curid1 = $("#tranCurPairBaseId").val();
           if (buyCurId == curid1) {
               bt.moneyType.get("positionMoney").val(data);
           }
           // calContractRate();
           //期权费金额(期权费币种)
           resetPremiumMoneyOfPremiumCur();
           resetPremiumMoneyOfCorpCur();
           //获取是否自动计算金额
           var autoCal = $('input[name="isPaymentAmount"]:checked').val();
           if (autoCal == 1){
               //计算卖出
               resetSellMoney();
           }
           //持仓金额赋值
           calPositionMoney();
       }
    };
    bt.sellMoneyCallback = function (data) {
        if($("#billFrom").val() != '1') {
            //回调判断货币对左侧币种是否对应卖出币种，如果是，则反写买入金额给持仓金额
            var sellCurId = selectType.get("sellCurId").val();
            var curid1 = $("#tranCurPairBaseId").val();
            if (sellCurId == curid1) {
                bt.moneyType.get("positionMoney").val(data);
            }
            // calContractRate();
            //获取是否自动计算金额
            var autoCal = $('input[name="isPaymentAmount"]:checked').val();
            if (autoCal == 1){
                //计算买入
                resetBuyMoney();
            }
            //持仓金额赋值
            calPositionMoney();
            //期权费金额(期权费币种)
            resetPremiumMoneyOfPremiumCur();
            resetPremiumMoneyOfCorpCur();
        }
    };

    //期权费币别回调函数
    bt.curCallback = function (data) {
        //现金流信息
        $("#srcCurTd").text(data.englishcode);
        //加载资金计划
        renderBudget();
        //重新初始化期权费金额(结算金额)
        resetPremiumMoneyOfSettle();
        form.render();
    };

    //期权费金额回调函数
    bt.premiumMoneyCallback = function (data) {
        //买入方向：1买入，2卖出
        var busDirection = bt.dicts.get('busDirection').val();
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busDirection == "2") {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('premiumMoney').val()));
            //加载主体单位和交易对手账户信息
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        }else{
            $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('premiumMoney').val()));
            //加载主体单位和交易对手账户信息
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", selectType.get("cotpId").val());
            // renderBankAcc("1", selectType.get("cotpId").val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("curId").val(),selectType.get("proId").val(),"1","1", oppId);
            renderBankAcc("1", oppId, selectType.get("curId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
        }
        //初始化期权费金额(结算币种)
        resetPremiumMoneyOfSettle();
    };
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    //设置结算币种下拉内容
    changeSettleCurId();
    showHideBaseConvertTr();
    // //这里必须放在所有前端组件初始化之后
    // //判断单剧来源
    // var billFrom = $('#billFrom').val();
    // //这里给后续测试发现问题时，发现某些字段会影响其它联动字段，可用这个字段屏蔽联动
    // var isEidt = true;
    // //如果是交易需求来源
    // if(billFrom == '1'){
    //     isEidt = false;
    // }
    //结算日期回调函数
     function settleDateCallback(){
        var settleDate = dateType.get('settleDate').val();
        //现金流
        //$("#srcSettleDateTd").text(settleDate);
        $("#targetSettleDateTd").text(settleDate);
        //加载资金计划
        renderBudget();
    };

    /**
     * 获取账户信息
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     * @param swapType 掉期交易 1-近端 2-远端
     */
    function renderBankAcc(busType, corpId, curId,proId,swaptype,cdSign,oppCorpId){
        $.ajax({
            url: basePath + "/foreign/fexcorpbankacc/getNewBankAcc",
            method: 'POST',
            data: {busType: busType, tranType: "2", corpId: corpId, curId: curId, proId: proId, cashFlowType: "3", cdSign: cdSign, oppCorpId: oppCorpId},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    if (busType == "0") {
                        if(data.obj.accId){
                            $('#corpBank').val(data.obj.bankType);
                            $('#corpAccId').val(data.obj.accId);
                            $('#corpBankAcc').val(data.obj.bankAcc);
                            $('#corpAccName').val(data.obj.accName);
                        }
                        if (data.obj.bankAcc) {
                            $("#corpBankNameDiv").text(data.obj.bankTypeName);
                            $("#srcBankTd").text(data.obj.bankTypeName);
                            $("#srcAccNameTd").text(data.obj.accName);
                            $("#srcBankAccTd").text(data.obj.bankAcc);
                            if(data.obj.voucherName) {
                                $("#srcPayTypeTd").text(data.obj.voucherName);
                            }/*else{
                                $("#srcPayTypeTd").text("");
                            }*/
                        } /*else {
                            $("#corpBankNameDiv").text("");
                            $("#srcBankTd").text("");
                            $("#srcAccNameTd").text("");
                            $("#srcBankAccTd").text("");
                            $("#srcPayTypeTd").text("");
                        }*/
                    } else {
                        if(data.obj.accId){
                            $('#cotpBank').val(data.obj.bankType);
                            $('#cotpAccId').val(data.obj.accId);
                            $('#cotpBankAcc').val(data.obj.bankAcc);
                            // selectType.get("cotpBankAcc").val(data.obj.bankAcc);
                            /*$('#cotpBankAcc').val(data.obj.bankAcc);*/
                            $('#cotpAccName').val(data.obj.accName);
                        }
                        if (data.obj.bankAcc) {
                            // $("#cotpBankNameDiv").text(data.obj.bankTypeName);
                            // $("#srcCotpBankTd").text(data.obj.bankTypeName);
                            $("#srcCotpAccNameTd").text(data.obj.accName);
                            $("#srcCotpBankAccTd").text(data.obj.bankAcc);
                            $('#cotpBankAcc').val(data.obj.bankAcc);
                        } /*else {
                            $("#cotpBankNameDiv").text("");
                            $("#srcCotpBankTd").text("");
                            $("#srcCotpAccNameTd").text("");
                            $("#srcCotpBankAccTd").text("");
                        }*/
                    }

                }
            }
        });
    }

    if($("#tranAppDtlId").val()){
        form.setReadonlyForRadio('#busDirectionDiv',true);
        form.setReadonlyForRadio('#updownDirectionDiv',true);

        var busDirection = $('#busDirectionDef').val();
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        if(busDirection == "2") {
            // renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#cotpIdDef').val());
            // renderBankAcc("1", $('#cotpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#corpIdDef').val());

            renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", oppId);
            renderBankAcc("1", oppId, $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#corpIdDef').val());
        }else {
            // renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", $('#cotpIdDef').val());
            // renderBankAcc("1", $('#cotpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#corpIdDef').val());

            renderBankAcc("0", $('#corpIdDef').val(), $('#curIdDef').val(), $('#proIdDef').val(), "1", "1", oppId);
            renderBankAcc("1", oppId, $('#curIdDef').val(), $('#proIdDef').val(), "1", "0", $('#corpIdDef').val());
        }
    }else{
        form.setReadonlyForRadio('#busDirectionDiv',false);
        form.setReadonlyForRadio('#updownDirectionDiv',false);
    }

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

        var isVerifyPass = verifyQuotationMethod();
        if(!isVerifyPass){
            return isVerifyPass
        }

        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");


        data.field.status = $(this).attr("value");
        $("#attachmentId").val(up.getGroupId());
        data.field.attachmentId = up.getGroupId();
        //设置买入币种和卖出币种
        var buyCurId = selectType.get("buyCurId").val();
        var sellCurId = selectType.get("sellCurId").val();
        var disableParam = "&buyCurId="+buyCurId+"&sellCurId="+sellCurId;
        var busDirectionIsDisabled = $('[data-name="busDirection"]').data('disabled');
        var updownDirectionIsDisabled = $('[data-name="updownDirection"]').data('disabled');
        if(busDirectionIsDisabled){
            var busDirection = bt.dicts.get('busDirection').val()
            disableParam = disableParam + "&busDirection=" + busDirection;
        }
        if(updownDirectionIsDisabled){
            var updownDirection = bt.dicts.get('updownDirection').val()
            disableParam = disableParam + "&updownDirection=" + updownDirection;
        }
        var proIdIsDisabled = $('[data-name="proId"]').data('disabled');
        if(proIdIsDisabled){
            var proId = selectType.get('proId').val()
            disableParam = disableParam + "&proId=" + proId;
        }
        var tranCurPairIdIsDisabled = $('[data-name="tranCurPairId"]').data('disabled');
        if(tranCurPairIdIsDisabled){
            var tranCurPairId = selectType.get('tranCurPairId').val()
            disableParam = disableParam + "&tranCurPairId=" + tranCurPairId;
        }

        //获取单位的本位币
        var corpCurId = $("#corpCurId").val();
        //获取期权费币种
        var premiumCur = $('select[name="premiumCur"]').val();
        if (corpCurId == premiumCur) {
           bt.moneyType.get('convertRate').val('')
           bt.moneyType.get('convertMoney').val('')
        }
        /*var corpIdIsDisabled = $('[data-name="corpId"]').data('disabled');
        if(corpIdIsDisabled){
            var corpId = selectType.get('corpId').val()
            disableParam = disableParam + "&corpId=" + corpId;
        }*/
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("premiumMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        var corpAccId = $("#corpAccId").val();
        var corpBankAcc = $("#corpBankAcc").val();
        if(!corpAccId || !corpBankAcc){
            bt.loading.hide();
            layer.alert(bt.lang("fex.cash.flow.message.bankacc.match"), {icon: 2});
            return false;
        }
        //证书签名
        //证书签名
        var encrypt = $("#encrypt").val(); // cfca证书
        if(encrypt == 2 || encrypt == 1){ // 1 天威证书
            signData("外汇交易提交", 11,function () {
                $.post(basePath + "/foreign/fextranreq/modify", $('#inputForm').serialize()+disableParam, function (result) {
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                        if (result.success) {
                            if (bt.navtab) {
                                parent.layer.closeAll();
                                bt.navtab.closeCurrent();
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                    })
                }, 'JSON');
            })
        }else {
            var signDataMsg = signData("外汇交易提交", 11);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }

            var action = "/foreign/fextranreq/modify";
            $.post(basePath + action, $('#inputForm').serialize()+disableParam, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    if (result.success) {
                        if (bt.navtab) {
                            parent.layer.closeAll();
                            bt.navtab.closeCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
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
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $("#corpIdDef").val();
        if ($('input[name="corpId"]').val()) {
            corpId = $('input[name="corpId"]').val();
        }
        var curId = $("#curIdDef").val();
        if (selectType.get("curId").val()) {
            curId = selectType.get("curId").val();
        }
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: dateType.get("settleDate").val(),
            type: 'select',
            detailControl: true,
            curId: curId,
            wishPayday: dateType.get("settleDate").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url,
            method: 'POST',
            data: param,
            async: true,
            success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };
    renderBudget();

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        //买入方向：1买入，2卖出
        var busDirection = bt.dicts.get('busDirection').val();
        if (budgetSel == "1" && busDirection == "1") {
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
        var custInfoBusId = $('#custInfoBusId').val() || $('#id').val();
        if (!custInfoBusId) {
            return;
        }
        var url = basePath + "/foreign/fextranreq/updateCustInfo/" + custInfoBusId + "?timestamp=" + new Date().getTime();
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
    bt.moneyType.get("premiumMoney").val(data);
}

/**
 * 获取报价方式
 * @param curId1
 * @param curId2
 */
function setQuotationMethod(curId1,curId2){
    var rateTypeId = $("#rateTypeId").val();
    $.ajax({
        "url": basePath + "/foreign/fexrateinfo/getByCurId",
        data:{
            curId1: curId1,
            curId2: curId2,
            rateTypeId:rateTypeId
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (result) {
            if (result.success) {
                if (result.obj) {
                    //展示报价方式
                    $("#quotationCurId").val(result.obj.cur);
                    $("#quotationReferCurId").val(result.obj.referCur);
                    $("#quotationMethod").val(result.obj.curEnCode+'/'+result.obj.referCurEnCode)
                    $("#quotationMethodText").text(result.obj.curEnCode+'/'+result.obj.referCurEnCode);
                }
            }else{
                var end = result.msg.indexOf("]");
                var length = result.msg.length;
                $("#quotationMethodText").text(result.msg.substring(end + 1,length));
            }
        }
    });
}

//记录切换前交易货币对左侧金额、右侧金额（金额跟着币种走，供 resetSellCurId 重新赋值）
var resetBuySellMoneyCache = null;
/**
 * 每次交易货币对、买卖方向或看跌方向更改后，更新买入币种
 */
function resetBuyCurId(){
    //获取买卖方向
    var busDirection = bt.dicts.get("busDirection").val();
    //获取跌涨方向
    var updownDirection = bt.dicts.get("updownDirection").val();
    //获取货币对左侧币种
    var curId1 = $("#tranCurPairBaseId").val();
    //获取货币对右侧币种
    var curId2 = $("#tranCurPairReferId").val();
    //切换前，金额跟着币种走：按当前买入币种，记下交易货币对左侧金额、右侧金额
    var buyCurId = selectType.get("buyCurId").val();
    var buyMoney = bt.moneyType.get("buyMoney").val();
    var sellMoney = bt.moneyType.get("sellMoney").val();
    resetBuySellMoneyCache = {
        baseMoney: (buyCurId == curId1) ? buyMoney : sellMoney,
        referMoney: (buyCurId == curId2) ? buyMoney : sellMoney
    };
    //如果（买卖方向=买入&涨跌方向=看涨）or（买卖方向=卖出&涨跌方向=看跌），买入币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
    if((busDirection == "1" && updownDirection == "0") || (busDirection == "2" && updownDirection == "1")){
        selectType.get("buyCurId").val(curId1);
    }else{
        selectType.get("buyCurId").val(curId2);
    }
}
/**
 * 每次交易货币对、买卖方向或看跌方向更改后，更新卖出币种
 */
function resetSellCurId(){
    //获取买卖方向
    var busDirection = bt.dicts.get("busDirection").val();
    //获取跌涨方向
    var updownDirection = bt.dicts.get("updownDirection").val();
    //获取货币对左侧币种
    var curId1 = $("#tranCurPairBaseId").val();
    //获取货币对右侧币种
    var curId2 = $("#tranCurPairReferId").val();
    //如果（买卖方向=卖出&涨跌方向=看涨）or（买卖方向=买入&涨跌方向=看跌），卖出币种=交易货币对的左侧,否则，买入币种=交易货币对的右侧
    if((busDirection == "2" && updownDirection == "0") || (busDirection == "1" && updownDirection == "1")){
        selectType.get("sellCurId").val(curId1);
    }else{
        selectType.get("sellCurId").val(curId2);
    }
    //金额跟着币种走：按切换后的买入/卖出币种，取对应的左侧/右侧金额
    if(resetBuySellMoneyCache){
        var newBuyCurId = selectType.get("buyCurId").val();
        var newSellCurId = selectType.get("sellCurId").val();
        bt.moneyType.get("buyMoney").val(newBuyCurId == curId1 ? resetBuySellMoneyCache.baseMoney : resetBuySellMoneyCache.referMoney);
        bt.moneyType.get("sellMoney").val(newSellCurId == curId1 ? resetBuySellMoneyCache.baseMoney : resetBuySellMoneyCache.referMoney);
        resetBuySellMoneyCache = null;
    }
}

/**
 * 1、如果期权费币种=交易货币对的右侧&交易货币对=报价方式：期权费金额（期权费币种）=期权费点数/10000*交易货币对左侧的金额
 * 2、如果期权费币种=买入币种：期权费金额（期权费币种）=期权费百分比/100*买入金额
 * 3、如果期权费币种=卖出币种：期权费金额（期权费币种）=期权费百分比/100*卖出金额
 * 每次期权费币种、期权费点数、交易货币对左侧的金额、期权百分比、买入金额、卖出金额更改后，更新默认值
 */
function resetPremiumMoneyOfPremiumCur(){
    //获取期权费币种
    //var premiumCur = selectType.get("premiumCur").val();
    var premiumCur = $('select[name="premiumCur"]').val();
    //获取交易货币对左侧币种
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    //获取交易货币对的右侧币种
    var tranCurPairReferId = $("#tranCurPairReferId").val();
    //获取报价方式左侧币种
    var quotationCurId = $("#quotationCurId").val();
    //获取报价方式右侧币种
    var quotationReferCurId = $("#quotationReferCurId").val();
    //获取买入币种
    var buyCurId = selectType.get("buyCurId").val();
    //获取卖出币种
    var sellCurId = selectType.get("sellCurId").val();
    //获取买入金额
    var buyMoney = bt.moneyType.get("buyMoney").val();

    //获取卖出金额
    var sellMoney = bt.moneyType.get("sellMoney").val();
    //期权费百分比
    var premiumRatio = bt.ratioType.get("premiumRatio").val();
    //期权费金额(期权费币种)
    var premiumMoneyOfPremiumCur = 0;
    //期权费点数
    var premiumPoint = bt.moneyType.get("premiumPoint").val();

    //1、如果期权费币种=交易货币对的右侧&交易货币对=报价方式：期权费金额（期权费币种）=期权费点数/10000*交易货币对左侧的金额
    if (premiumCur == tranCurPairReferId && tranCurPairBaseId == quotationCurId && tranCurPairReferId == quotationReferCurId) {
        var tranCurPairBaseMoney;
        //如果买入币种等于交易货币对左侧币种
        if (buyCurId == quotationCurId){
            //获取交易货币对左侧金额等于买入金额
            /*if(!buyMoney){
                return false
            }*/
            tranCurPairBaseMoney = buyMoney;
        }else{
            //获取交易货币对左侧金额等于卖出金额
            /*if(!sellMoney){
                sellMoney = 0;
            }*/
            tranCurPairBaseMoney = sellMoney;
        }
        /*if(!premiumPoint){
            return false
        }*/
        //premiumMoneyOfPremiumCur = premiumPoint/10000*tranCurPairBaseMoney;
        premiumMoneyOfPremiumCur = bt.money.div(premiumPoint,10000,6);
        premiumMoneyOfPremiumCur = bt.money.format(bt.money.mul(premiumMoneyOfPremiumCur,tranCurPairBaseMoney),{separator:"",decimal:2})
    }
    //2、如果期权费币种=买入币种：期权费金额（期权费币种）=期权费百分比/100*买入金额
    if (premiumCur == buyCurId && premiumMoneyOfPremiumCur == 0) {
        //premiumMoneyOfPremiumCur = premiumRatio/100*buyMoney;
        /*if(!premiumRatio || !buyMoney){
            return false
        }*/
        //premiumMoneyOfPremiumCur = bt.money.div(premiumRatio,100);
        premiumMoneyOfPremiumCur = bt.money.mul(premiumRatio,buyMoney)
    }
    //3、如果期权费币种=卖出币种：期权费金额（期权费币种）=期权费百分比/100*卖出金额
    if (premiumCur == sellCurId && premiumMoneyOfPremiumCur == 0) {
        //premiumMoneyOfPremiumCur = premiumRatio/100*sellMoney;
        /*if(!premiumRatio || !sellMoney){
            return false
        }*/
        //premiumMoneyOfPremiumCur = bt.money.div(premiumRatio,100);
        premiumMoneyOfPremiumCur = bt.money.mul(premiumRatio,sellMoney)
    }
    bt.moneyType.get("premiumMoneyOfPremiumCur").val(premiumMoneyOfPremiumCur);
    resetPremiumMoneyOfSettle();
}

/**
 * 根据货币对修改更改结算币种的下拉内容，默认值为交易货币对的右侧币种
 */
function changeSettleCurId(){
    //设置期权费币种
    //获取交易货币对左侧币种
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    //获取交易货币对的右侧币种
    var tranCurPairReferId = $("#tranCurPairReferId").val();
    var premiumCur = tranCurPairBaseId+','+tranCurPairReferId;
    if(tranCurPairBaseId == tranCurPairReferId){
        premiumCur = tranCurPairReferId;
    }
    bt.selectType.get("curId").attr({"data-disabled":"","data-displayData":premiumCur,"data-defaultValue":tranCurPairReferId});
}

/**
 * 1）如果结算币种=期权费币种：期权费金额（结算币种）=期权费金额（期权费币种）
 * 2）如果结算币种不等于期权费币种：期权费金额（结算币种）=期权费金额（期权费币种）【* or /】执行汇率（即期汇率，根据期权费折算汇率决定），其中，如果期权费币种 = 报价方式的左侧，取 * 号；否则，取 / 号。
 * 每次结算币种、期权费币种、期权费金额（期权费币种）、执行汇率（即期汇率）、or期权费折算汇率变化后，更新默认值
 */
function resetPremiumMoneyOfSettle(){
    //获取结算币种
    var settleCurId = selectType.get("curId").val();
    //获取期权费币种
    //var premiumCur = selectType.get("premiumCur").val();
    var premiumCur = $('select[name="premiumCur"]').val();
    //获取汇率
    var rate;
    //获取期权费折算汇率 0-使用协定汇率 1-使用即期汇率
    var premiumRate = bt.dicts.get("premiumRate").val();
    //期权费金额(结算币种)
    var premiumMoney = 0;
    //如果结算币种=期权费币种：期权费金额（结算币种）=期权费金额（期权费币种）
    if (settleCurId == premiumCur) {
        var premiumMoneyOfPremiumCur = bt.moneyType.get("premiumMoneyOfPremiumCur").val();
        bt.moneyType.get("premiumMoney").val(premiumMoneyOfPremiumCur);
    }
    //如果结算币种不等于期权费币种：期权费金额（结算币种）=期权费金额（期权费币种）【* or /】执行汇率（即期汇率，根据期权费折算汇率决定），其中，如果期权费币种 = 报价方式的左侧，取 * 号；否则，取 / 号。
    if (settleCurId != premiumCur) {
        if(premiumRate && premiumRate == '0'){
            rate = bt.moneyType.get("contractRate").val();
        }else if(premiumRate && premiumRate == '1'){
            rate = bt.moneyType.get("spotRate").val();
        }
        //期权费币种 = 报价方式的左侧
        if (premiumCur == $("#quotationCurId").val()) {
            premiumMoney = bt.money.format(bt.money.mul(bt.moneyType.get("premiumMoneyOfPremiumCur").val(),rate),{separator:"",decimal:2})
        }else{
            premiumMoney = bt.money.div(bt.moneyType.get("premiumMoneyOfPremiumCur").val(),rate,2);
        }
        bt.moneyType.get("premiumMoney").val(premiumMoney);
    }
}

/**
 * 1）如果期权费币种=折本位币报价方式的左侧：
 * 期权费金额（本位币）=期权费金额（期权费币种）*折本位币汇率；
 * 2）否则，
 * 期权费金额（本位币）=期权费金额（期权费币种）/折本位币汇率；
 */
function resetPremiumMoneyOfCorpCur(){
    //获取期权费币种
    //var premiumCur = selectType.get("premiumCur").val();
    var premiumCur = $('select[name="premiumCur"]').val();
    //获取折本位币报价方式左侧
    var convertCurId = $("#convertCurId").val();
    //获取折本位币汇率
    var convertRate = bt.moneyType.get("convertRate").val();
    //获取期权费金额(期权费币种)
    var premiumMoneyOfPremiumCur = bt.moneyType.get("premiumMoneyOfPremiumCur").val();
    var premiumMoneyOfCorpCur = 0;
    //如果期权费币种=折本位币报价方式的左侧：期权费金额（本位币）=期权费金额（期权费币种）*折本位币汇率；
    if (premiumCur == convertCurId) {
        premiumMoneyOfCorpCur = bt.money.format(bt.money.mul(premiumMoneyOfPremiumCur,convertRate),{separator:"",decimal:2});
    }else{
        premiumMoneyOfCorpCur = bt.money.div(premiumMoneyOfPremiumCur,convertRate,2);
    }
    bt.moneyType.get("convertMoney").val(premiumMoneyOfCorpCur);
}

/**
 * 买入金额=卖出金额* or /执行汇率，其中，如果卖出币种 = 报价方式的左侧， 取 * 号；否则，取 / 号。
 * 如果金额是否自动计算=是时，每次卖出金额更改后，更新默认值
 */
function resetBuyMoney(){
    //获取卖出金额
    var sellMoney = bt.moneyType.get("sellMoney").val();
    //获取卖出币种
    var sellCurId = selectType.get("sellCurId").val();
    //获取报价方式左侧币种
    var quotationCurId = $("#quotationCurId").val();
    //获取执行汇率
    var consractRate = bt.moneyType.get("contractRate").val();
    //买入金额
    var buyMoney = 0;
    //买入金额=卖出金额* or /执行汇率，其中，如果卖出币种 = 报价方式的左侧， 取 * 号；否则，取 / 号。
    if( sellCurId == quotationCurId){
        buyMoney = bt.money.format(bt.money.mul(sellMoney,consractRate),{separator:"",decimal:2});
    }else{
        buyMoney = bt.money.div(sellMoney,consractRate,2);
    }
    bt.moneyType.get("buyMoney").val(buyMoney);
}
/**
 * 卖出金额=买入金额* or /执行汇率，其中，如果买入币种=报价方式的左侧， 取*号；否则，取/号
 * 如果金额是否自动计算=是时，每次买入金额、合同汇率更改后，更新默认值
 */
function resetSellMoney(){
    //获取买入金额
    var buyMoney = bt.moneyType.get("buyMoney").val();
    //获取执行汇率
    var consractRate = bt.moneyType.get("contractRate").val();
    //获取买入币种
    var buyCurId = selectType.get("buyCurId").val();
    //获取报价方式左侧币种
    var quotationCurId = $("#quotationCurId").val();
    //卖出金额=买入金额* or /执行汇率，其中，如果买入币种=报价方式的左侧， 取*号；否则，取/号
    var sellMoney = 0;
    if (buyCurId == quotationCurId) {
        sellMoney = bt.money.format(bt.money.mul(buyMoney,consractRate),{separator:"",decimal:2});
    }else{
        sellMoney = bt.money.div(buyMoney,consractRate,2);
    }
    bt.moneyType.get("sellMoney").val(sellMoney);
}
/**
 * 期权费币种回调函数
 */
function premiumCurCallback(){
    //期权费期权费币种重新初始化默认值
    resetPremiumMoneyOfPremiumCur();
    //期权费结算币种重新初始化默认值
    resetPremiumMoneyOfSettle();
    //判断折本位币的显示和隐藏
    showHideBaseConvertTr()
    resetPremiumMoneyOfCorpCur();
}

/**
 * 期权费点数回调函数
 */
function premiumPointCallback(){
    resetPremiumMoneyOfPremiumCur();
    resetPremiumMoneyOfCorpCur();
}

/**
 * 期权百分比回调函数
 */
function premiumRatioCallback(){
    resetPremiumMoneyOfPremiumCur();
    resetPremiumMoneyOfCorpCur();
}

/**
 * 即期汇率回调函数
 */
function spotRateCallback(){
    resetPremiumMoneyOfSettle();
}

/**
 * 期权费折算汇率回调函数
 */
function premiumRateCallback(){
    resetPremiumMoneyOfSettle();
}
/**
 * 期权费金额(期权费币种)
 */
function premiumMoneyOfPremiumCurCallback(){
    //初始化期权费金额(结算币种)
    resetPremiumMoneyOfSettle();
    //判断折本位币的隐藏和现实
    //showHideBaseConvertTr()
    //期权费金额(本位币币种)计算
    resetPremiumMoneyOfCorpCur();
}
/**
 * 控制折本位币的的隐藏和显示
 */
function showHideBaseConvertTr(){
    //获取单位的本位币
    var corpCurId = $("#corpCurId").val();
    //获取期权费币种
    //var premiumCur = selectType.get("premiumCur").val();
    var premiumCur = $('select[name="premiumCur"]').val();
    if (corpCurId && premiumCur){
        if (corpCurId != premiumCur) {
            $(".baseConvertTr").removeClass('layui-hide');
            bt.moneyType.get('convertMoney').attr('lay-verify', 'required|number2');
            getBaseConvertInfo();
            resetPremiumMoneyOfCorpCur();
        }else{
            $(".baseConvertTr").addClass("layui-hide");
            bt.moneyType.get('convertMoney').attr('lay-verify', '');
            bt.moneyType.get('convertMoney').val('');
        }
    }else{
        $(".baseConvertTr").removeClass('layui-hide');
        bt.moneyType.get('convertMoney').attr('lay-verify', 'required|number2');
    }
}

function showHideBaseConvertTr1(premiumCur){
    //获取单位的本位币
    var corpCurId = $("#corpCurId").val();
    if (corpCurId && premiumCur){
        if (corpCurId != premiumCur) {
            $(".baseConvertTr").removeClass('layui-hide');
            bt.moneyType.get('convertMoney').attr('lay-verify', 'required|number2');
            getBaseConvertInfo();
            resetPremiumMoneyOfCorpCur();
        }else{
            $(".baseConvertTr").addClass("layui-hide");
            bt.moneyType.get('convertMoney').attr('lay-verify', '');
            bt.moneyType.get('convertMoney').val('');
        }
    }else{
        $(".baseConvertTr").removeClass('layui-hide');
        bt.moneyType.get('convertMoney').attr('lay-verify', 'required|number2');
    }
}

/**
 * 获取折本位币报价方式和折本位币汇率
 */
function getBaseConvertInfo(){
    //获取单位本位币
    var corpCurId = $("#corpCurId").val();
    //获取期权费币种
    //var premiumCur = selectType.get("premiumCur").val();
    var premiumCur = $('select[name="premiumCur"]').val();
    $.ajax({
        "url": basePath + "/foreign/fexrateinfo/getBaseCurFexRate",
        data:{
            curId: corpCurId,
            referCurId: premiumCur
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (data) {
            if (data.success) {
                if (data.obj){
                    $("#convertCurId").val(data.obj.cur);
                    $("#convertCurReferId").val(data.obj.referCur);
                    $("#convertCurType").val(data.obj.curEnCode+'/'+data.obj.referCurEnCode);
                    //如果期权费币种=单位本位币，取买入价否则取卖出价
                    if (corpCurId == premiumCur) {
                        bt.moneyType.get("convertRate").val(data.obj.purchaseRate);
                    }else{
                        bt.moneyType.get("convertRate").val(data.obj.sellRate);
                    }
                }else{
                    $("#convertCurId").val('');
                    $("#convertCurReferId").val('');
                    $("#convertCurType").val('');
                }
            }else{
                $("#convertCurId").val('');
                $("#convertCurReferId").val('');
                $("#convertCurType").val('');
            }
        }
    });
}
/**
 * 折本位币汇率回调函数
 */
function convertRateCallback(){
    //控制折本位币的隐藏喝显示
    resetPremiumMoneyOfCorpCur();
}

function verifyQuotationMethod(){
    var selectedTranCurId = $('[name="tranCurPairId"]').find('option:selected').data();
    var curId1 = selectedTranCurId.curid1;
    var curId2 = selectedTranCurId.curid2;
    var rateTypeId = $("#rateTypeId").val();
    var isReturn = true;
    $.ajax({
        "url": basePath + "/foreign/fexrateinfo/getByCurId",
        data:{
            curId1: curId1,
            curId2: curId2,
            rateTypeId:rateTypeId
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (result) {
            if (!result.success) {
                parent.layer.alert(result.msg, {icon: 2});
                isReturn = false;
            }
        }
    });
    return isReturn
}

function changeRefundCallback(data){
    if(data.value == '1'){
        $('.isRefund-control').removeClass('layui-hide')
        //是否涉及退款、退款金额、退款支付时间设置必填/必选；退款点数、退款百分比非必输
        $('[data-biz-name="refundPoint"]').attr('lay-verify', 'number2');
        $('[data-biz-name="refundRatio"]').removeAttr('lay-verify');
        $('[data-biz-name="refundMoney"]').attr('lay-verify', 'required|number2');
        $('[data-biz-name="refundMoney"]').attr('lay-verify', 'required|number2');
    }else{
        $('[data-biz-name="refundPoint"]').removeAttr('lay-verify');
        $('[data-biz-name="refundRatio"]').removeAttr('lay-verify');
        $('[data-biz-name="refundMoney"]').removeAttr('lay-verify');
        $('.isRefund-control').addClass('layui-hide')
    }
    var selectedProIdOption = $('[name="proId"]').find('option:selected').data();
    var refundtime = selectedProIdOption.refundtime || '0'
    bt.dicts.get('refundTime').val(refundtime);

    form.renderIcon();
}

function getCurrecyList() {
    $.ajax({url: basePath+"/basic/business/btcurrency/json",
        method: 'GET', dataType: 'json', async:false,
        success: function(data){
            cacheCurrecyList = JSON.stringify(data);
        }
    })
}
function creatCurrecySelectHtml(displayValueStr,selectedId) {
    var currencyList = JSON.parse(cacheCurrecyList)
    var html = ''
    bt.each(currencyList,function (i,item) {
        if(displayValueStr.indexOf(item.id) != -1){
            html += '<option value="'+item.id+'"';
            if(selectedId === item.id){
                html += ' selected';
            }
            html += '>'+item.englishCode+'</option>';
        }
    })

    return html
}

/**
 * 计算持仓金额
 */
function calPositionMoney() {
    //获取卖出币种
    var sellCurId = bt.selectType.get("sellCurId").val();
    //获取交易货币对左侧币种
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    var money = null;
    if (sellCurId == tranCurPairBaseId) {
        money = bt.moneyType.get("sellMoney").val();
    } else {
        money = bt.moneyType.get("buyMoney").val()
    }
    bt.moneyType.get("positionMoney").val(money);
}

function premiumDateCallback(){
    $("#srcSettleDateTd").text(bt.dateType.get('premiumDate').val());
}
