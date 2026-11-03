var moneyType,moneyFun,dicts,dataType,dateFun,selectType,dataSelect;

var dataStore = {};

bt.use(['form', 'layedit', 'select', 'selectTable', 'dateType', 'datatable','datatable2', 'dateForm', 'moneyType', 'money', 'btUpload', 'selectType', 'dicts','layer'], function () {
    var $ = bt.jquery, form = bt.form();
    moneyFun = bt.moneyType.getFun();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    /**
     * 业务类别回调
     * @param data
     */
    bt.busTypeCallback = function (data) {

        $("#defItemCode").val(data? data.itemCode:'');

        //资金计划
        renderBudget();
    }

    selectType = bt.selectType.init();

    //从父页面获取相关值并设置值
    getParentSelectedData();

    /**
     * 生成明细
     */
    bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        checkbox: false,
        scrollY:'auto',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
        },
        ajax: {
            'url': basePath + "/expense/nisexpensedetail/dataTableCollect"
        },
        columns:[
            {data: "expenseId", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.expenseId")},
            {data: "expenseDate", bSortable: true, className: 'center',tile:bt.lang("table.nisExpenseDetail.expenseDate")},
            {
                data: "expenseMoney", bSortable: true, className: 'textRight',title:bt.lang("table.nisExpenseDetail.expenseMoney"),
                render: function (data, type, full, meta) {
                    if(full.busType == "04"){
                        return "****";
                    }else {
                        return bt.money.format(full.expenseMoney);
                    }
                }
            },
            {data: "accName", bSortable: true,title:bt.lang("table.nisExpenseDetail.accName")},
            {data: "bankAcc", bSortable: true,title:bt.lang("table.nisExpenseDetail.bankAcc")},
            {data: "bankName", bSortable: true,title:bt.lang("table.nisExpenseDetail.bankName")},
            {data: "bankCode", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.bankCode")},

            {data: "provName", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.prov")},
            {data: "cityName", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.city")},
            {
                data: "difBank",
                bSortable: true,
                className: 'center keep-all remark-col',
                title: bt.lang("table.nisExpenseDetail.difBank"),
                render: function (data, type, full, meta) {
                    var html = "";
                    html += '<input data-list-id="'+full.id +'" id="difBank_' + full.id + '" name="difBank_' + full.id + '" type="radio" data-name="difBank_' + full.id + '" value="0" title="' + bt.dict("nisBillhead.difBank", 0) + '" lay-filter = "changSign"';
                    if(dataStore[full.id].difBank == null || dataStore[full.id].difBank == '0'){
                        html += 'checked="checked"';
                    }
                    html += '/>';

                    html += '<input data-list-id="'+full.id +'" id="difBank_' + full.id + '" name="difBank_' + full.id + '" type="radio" data-name="difBank_' + full.id + '" value="1" title="' + bt.dict("nisBillhead.difBank", 1) + '" lay-filter = "changSign"';
                    if(dataStore[full.id].difBank == '1'){
                        html += 'checked="checked"';
                    }
                    html += '/>';
                    return html;
                }
            },

            {data: "areaSign", bSortable: true, className: 'center keep-all remark-col',title:bt.lang("table.nisExpenseDetail.areaSign"),
                render: function (data, type, full, meta) {
                    var html = "";
                    html += '<input data-list-id="'+full.id +'" name="areaSign_' + full.id + '" type="radio" data-name="areaSign_' + full.id + '" value="0" title="' + bt.dict("nisBillhead.areaSign", 0) + '" lay-filter = "changSign"';
                    if(dataStore[full.id].areaSign == null || dataStore[full.id].areaSign == '0'){
                        html += 'checked="checked"';
                    }
                    html += '/>';

                    html += '<input data-list-id="'+full.id +'" name="areaSign_' + full.id + '" type="radio" data-name="areaSign_' + full.id + '" value="1" title="' + bt.dict("nisBillhead.areaSign", 1) + '" lay-filter = "changSign"';
                    if(dataStore[full.id].areaSign == '1'){
                        html += 'checked="checked"';
                    }
                    html += '/>';

                    return html;
                }
            },
            {data: "busTypeName", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.busType")},
            {data: "opTypeName", bSortable: true, className: 'center',title:bt.lang("table.nisExpenseDetail.opType")},
            {data: "purpose", bSortable: true,title:bt.lang("table.nisExpenseDetail.purpose")}
        ],
        btDrawCallback:function () {
            $('#searchForm').addClass('layui-form')
            form.render('radio');
            $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            bt.loading.hide();
        }
    });

    //跨行，地域标识列隐藏操作
    function setTableColumnsHide(flag) {
        $('#appTable').DataTable().columns('.remark-col').visible( flag );
        form.render('radio');
        $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();

    }

    /**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }
    bt.money.reFormat();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });

    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",},
        {data: "isOnlineStr",}
    ];
    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAcc",
        urldata: {corpId: $("#corpId").val(), voucherTypeId: $("select[name='voucherTypeId']").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                bt.loading.show();
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccName').val(data.accName);
                $('#payerBank').val(data.bankName);
                $('#accId').val(data.id);
                $("#curDiv").text(data.curName);
                $('#curId').val(data.curId);
                //获取账户余额
                var isAccount = $("#isAccount").val();//是否记账：0-否 1-是
                if(isAccount == "0"){
                    //获取单位信息
                    $.ajax({
                        url: basePath + "/bankacc/bisaccbal?getBal",
                        method: 'POST',
                        data: {bankAcc: data.bankAcc,curId: data.curId},
                        dataType: 'json',
                        success: function (data) {
                            moneyType.get("balance").val(data);
                            $("#balanceDiv").text(moneyFun.format(data));
                            form.render();
                        }
                    });
                } else {
                    //内部账号
                    moneyType.get("balance").val(data.balance);
                    $("#balanceDiv").text(moneyFun.format(data.balance));
                }

                //获取账户余额包含在途金额
                $.ajax({
                    url: basePath + "/netbank/nisbillhead?getAccountBal",
                    method: 'POST',
                    data: {account: data.bankAcc,curId: data.curId,isAccount:isAccount},
                    dataType: 'json',
                    success: function (data) {
                        moneyType.get("availBal").val(data);
                        $("#availBalDiv").text(moneyFun.format(data));
                        form.render();
                    }
                });
                $('#bifCode').val(data.bifCode);
                $('#payerBankCode').val(data.bankCode);
                $('#bankNamePrefix').val(data.bankNamePrefix);
                $('#payerProv').val(data.prov);
                $('#payerCity').val(data.city);
                //判断跨行标志和地域标志
                difBankAndAreaSign();
                //转付，重新加载资金下拨账号
                if ($("#sysVoucherCode").val() == "02") {
                    downAccInit.get("downAcc").reload({
                        corpId: $("#corpId").val(),
                        curId: $('#curId').val(),
                        account: $("#payerAcc").val()
                    });
                }
                //重新渲染资金计划
                renderBudget();
            } else {
                $('input.text').val('');
            }
        }
    });

    //资金下拨账号
    var downAccInit = bt.selectTable.init({
        elem: "#downAccTable",
        url: basePath + "/netbank/nisbillhead/getDownAcc",
        urldata: {corpId: $("#corpId").val(), curId: $('#curId').val(), account: $("#payerAcc").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#downAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#downAccId').val(data.id);
                $('#downAcc').val(data.bankAcc);
                $('#downAccName').val(data.accName);
                //转付以下拨账号的接口代码为准
                $('#bifCode').val(data.bifCode);
            } else {
                $('input.text').val('');
            }
        }
    });

    //付款方式选择监听
    form.on('select(voucherTypeId)', function (data) {
        //获取付款方式信息
        voucerTypeCallback(data.value);
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
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
                rate = bt.money.mul(data.field.payMoney,da,2)
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
            data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:1,wishPayDate: data.field.wishPayday},
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
        var signDataMsg = signCaData();
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }

        //账户余额校验
        var isAccount = $("#isAccount").val();//是否记账：0-否 1-是
        var accountOver = $("#accountOver").val();//内部账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var balControlType = $("#balControlType").val();//付款余额控制类型：0-账户余额 1-可付款余额
        var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
        if(balControlType == "1"){
            balance = moneyFun.toNumber(moneyType.get("availBal").val());//可用余额
        }
        //付款金额大于账户余额
        var flag = false;
        if (payMoney > balance) {
            if (isAccount == "0") {
                if (bankaccOver == "1") {
                    flag = true;
                } else if (bankaccOver == "2") {
                    bt.loading.hide();
                    layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                    return false;
                }
            } else {
                if (accountOver == "1") {
                    flag = true;
                } else if (accountOver == "2") {
                    bt.loading.hide();
                    layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                    return false;
                }
            }
        }
        if (flag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function (index) {
                submitForm(data);
            });
        } else {
            submitForm(data);
        }
    }

    function submitForm(data) {
        bt.loading.show();
        var action = "/netbank/nisbillhead/save";
        //控制重复提交
        if($(data.elem).parent().hasClass('isSubmiting')){
            bt.loading.hide();
            return false
        }

        //增加已提交的标志
        var data1 =  $('#inputForm').serialize();
        var ids = $('#collectId').val();
        var idArr = ids.split(",");
        var difBanks = new Array();
        var areaSigns = new Array();
        var expenseId = new Array();
        for(var i = 0; i < idArr.length; i++){
            expenseId[i] = idArr[i];
            difBanks[i] = dataStore[idArr[i]].difBank;
            areaSigns[i] = dataStore[idArr[i]].areaSign;
        }
        data1 += "&expenseIdStr=" + expenseId +"&difBankStr=" + difBanks +"&areaSignStr=" +areaSigns;
        $(data.elem).parent().addClass('isSubmiting');
        $.post(basePath + action, data1, function (result) {
            bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
                parent.layer.closeAll();
                //解除已提交的标志
                $(data.elem).parent().removeClass('isSubmiting');
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                        var timeStamp = Date.parse(new Date());
                        $('#timeStamp').val(timeStamp);
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
    }

    //证书签名
    function signCaData() {
        //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
        var sourceData = $('#payerAcc').val();//付款账号
        var random = String(parseInt(Math.random() * 10000000000));//随机数
        while (random.length < 10) {
            random = random + '0';
        }
        $("#random").val(random);
        var payMoney = moneyFun.format(moneyFun.toNumber(moneyType.get("payMoney").val()), {separator: ''});//金额
        while (payMoney.length < 14) {
            payMoney = '0' + payMoney;
        }
        var wishPayday = dateFun.format(dateType.get("wishPayday").val(), 'yyyyMMdd');//期望支付日期
        sourceData += random + payMoney + wishPayday + $('#payeeAccName').val() + $('#payeeAcc').val();
        return signData(sourceData, "2");
    }

    /**
     * 根据付款方式显示相应的字段
     */
    function show() {
        var $ = bt.jquery;
        var sysVoucherCode = $("#sysVoucherCode").val();//系统结算方式
        var isAccount = $("#isAccount").val();//是否记账 0，否 1，是
        //02-转付，且记账情况下
        if (sysVoucherCode == "02" && isAccount == "1") {
            $("#downAccDiv").show();//下拨账号显示
            $("#downAcc").attr("lay-verify", "required");
        } else {
            $("#downAccDiv").hide();//下拨账号隐藏
            $("#downAcc").removeAttr("lay-verify");
        }
        //跨行，地域标识列隐藏操作
        setTableColumnsHide(isAccount != "1");
        $("#directPayFlag").val(isAccount == "1"?'n':'y');

    };

    /**
     * 跨行标志，地域标志判断
     */
    function difBankAndAreaSign() {

        var payerBankCode = $("#payerBankCode").val();
        if(payerBankCode !== ""){
            payerBankCode = payerBankCode.substr(0, 3)
        }
        var payerProv = $("#payerProv").val();
        var payerCity = $("#payerCity").val();
        for( key in dataStore){
            var bankCode = dataStore[key].bankCode;
            var bankName = dataStore[key].bankName;
            if (!bankCode) {
                dataStore[key].difBank = 0;
            } else if(payerBankCode !== "" && bankCode !== "" && payerBankCode == bankCode.substr(0, 3)){
                //商业银行根据开户行名称前缀匹配
                if(payerBankCode == "313" || payerBankCode == "402" || payerBankCode == "314"){
                    if($("#bankNamePrefix").val()!="" && bankName.indexOf($("#bankNamePrefix").val()) != -1){
                        dataStore[key].difBank = 0;
                    }else{
                        dataStore[key].difBank = 1;
                    }
                }else {
                    dataStore[key].difBank = 0;
                }
            }else{
                dataStore[key].difBank = 1;
            }
            if(payerProv == dataStore[key].payeeProv && payerCity == dataStore[key].payeeCity){
                dataStore[key].areaSign = 0;
            }else {
                dataStore[key].areaSign = 1;
            }
        }
        bt.datatable.refreshTable('appTable');

    };

    /**
     * 付款方式选择事件
     * @param voucherTypeId 付款方式id
     */
    function voucerTypeCallback(voucherTypeId) {
        var $ = bt.jquery;
        if (voucherTypeId != "") {
            $.ajax({
                url: basePath + "/netbank/fcvouchertype?getFcVoucherType",
                method: 'POST',
                data: {id: voucherTypeId},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        $("#sysVoucherCode").val(data.obj.sysVoucherCode);
                        $("#isAccount").val(data.obj.isAccount);
                        $("#isBudget").val(data.obj.isBudget);
                        show();
                        //重新加载付款账号
                        if(data.obj.isAccount && data.obj.isAccount == "1") {
                            payerAccCol = [
                                {data: "bankAcc",},
                                {data: "accName",},
                                {data: "bankName",},
                                {data: "curName",},
                                {data: "isOnlineStr",visible: false}
                            ];
                        }else{
                            payerAccCol = [
                                {data: "bankAcc",},
                                {data: "accName",},
                                {data: "bankName",},
                                {data: "curName",},
                                {data: "isOnlineStr",visible: true}
                            ];
                        }
                        payerAccInit.get("payerAcc").rebuild({
                            urldata: {corpId: $("#corpId").val(), voucherTypeId: $("select[name='voucherTypeId']").val()},
                            columns: payerAccCol
                        });
                        //转付，重新加载资金下拨账号
                        if ($("#sysVoucherCode").val() == "02") {
                            downAccInit.get("downAcc").reload({
                                corpId: $("#corpId").val(),
                                curId: $('#curId').val(),
                                account: $("#payerAcc").val()
                            });
                        }
                        //资金计划
                        renderBudget();
                    }
                }
            });
        }
    }
    /**
     * 根据单位id查找付款方式
     */
    function voucherTypeIdRender() {
        var id = $("#corpId").val();
        //获取单位分配的付款方式
        $.ajax({
            url: basePath + "/netbank/btassignpayway?getAssignPayWayFilter",
            method: 'POST',
            data: {corpId: id},
            dataType: 'json',
            success: function (data) {
                $("#voucherTypeId").empty();
                if (data && data.obj && data.obj.length > 0) {
                    $.each(data.obj, function (index, item) {
                        $("#voucherTypeId").append("<option value=" + item.voucherCode + ">" + item.voucherName + "</option>");
                        $("select[name='voucherTypeId']").val(item.defVoucherCode);
                    });
                    //获取付款方式信息
                    var voucherTypeId = $("select[name='voucherTypeId']").val();
                    voucerTypeCallback(voucherTypeId);
                } else {
                    $("#voucherTypeId").append("<option value=''></option>");
                }
                form.render();
            }
        });
    }

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $('input[name="corpId"]').val();
        var itemCode = $('input[name="payItem"]').val();
        var defItemCode = $('input[name="defItemCode"]').val();
        var processDate = dateType.get("wishPayday").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            defItemCode: defItemCode,
            processDate: processDate,
            curId: $("#curId").val(),
            type: 'select',
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断费用报销是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        var expFbsin = $("#expFbsin").val();
        if (budgetSel == "1" && expFbsin == "1") {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                $genOtherHtml();
            } else {
                $("#tagItemDetail").html("");
            }
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        renderBudget();
    }

    /**
     * 从父页面获取表格值并设置值
     */
    function getParentSelectedData() {
        dataSelect = bt.navtab.getParentDom().bt.datatable.getSelectData('#appTable');
        var ids ='';
        var corpIdTemp = '';
        var corpNameTemp = '';
        if(dataSelect){
            bt.each(dataSelect,function (index,item) {
                dataStore[item.id] = {
                    difBank:item.difBank,
                    areaSign:item.areaSign,
                    bankCode:item.bankCode,
                    bankName:item.bankName,
                    payeeProv:item.prov,
                    payeeCity:item.city

                }
                ids += item.id +',';
                if(index == 0){
                    corpIdTemp = item.corpId;
                    corpNameTemp = item.corpName;
                }
            })
        }
        ids = ids.slice(0,ids.length-1);


        $('#collectId').val(ids);
        $('#busType').addClass('bt-biz-selectType');
        $('#purpose').addClass('bt-biz-selectType');
        $('#busType').data('url',basePath+"/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId="+corpIdTemp);
        $('#purpose').data('url',basePath+"/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=1&corpId="+corpIdTemp);
        bt.selectType.init();
        $('#corpName').html(corpNameTemp);
        $('#corpId').val(corpIdTemp);
        voucherTypeIdRender();

        $.ajax({
            url: basePath + "/expense/nisexpensedetail?collectData",
            method: 'POST',
            data: {ids: ids},
            async:true,
            dataType: 'json',
            success: function (temp) {
                $('#payMoney').addClass('bt-biz-moneyType');

                if (temp.obj) {
                    var data = temp.obj;
                    $('#payMoney').val(data.payMoney);
                }
                bt.moneyType.init();
            }
        });
    }

    /**
     * 改变跨行，地域标识回调
     */
    form.on('radio(changSign)',function (data) {
        var id = $(data.elem).data('list-id');
        var name = $(data.elem).data('name').split('_')[0];
        dataStore[id][name] = parseInt(data.value);
    });

});
