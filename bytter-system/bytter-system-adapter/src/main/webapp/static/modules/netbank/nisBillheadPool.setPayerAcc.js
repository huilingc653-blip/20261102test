bt.use(['form', 'layedit', 'select','selectTable', 'datatable', 'dateForm', 'selectType', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //从当前页面获取表格数据并设置值
    getCurrentSelectedData();

    //付款账号
    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",},
        {data: "isOnlineStr",visible: false}
    ];
    var isAccount = $("#isAccount").val();
    if (isAccount == "0") {
        payerAccCol = [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",},
            {data: "isOnlineStr",visible: true}
        ];
    }
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillheadPool/getPayerAcc",
        urldata: {corpId: $("#corpId").val(), voucherTypeId: $("#voucherTypeId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccName').val(data.accName);
                $('#payerBank').val(data.bankName);
                $('#accId').val(data.id);
                $('#curId').val(data.curId);
                $('#bifCode').val(data.bifCode);
                $('#payerBankCode').val(data.bankCode);
                $('#payerProv').val(data.prov);
                $('#payerCity').val(data.city);
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
                //转付，重新加载资金下拨账号
                if ($("#sysVoucherCode").val() == "02") {
                    downAccInit.get("downAcc").reload({
                        corpId: $("#accCorpId").val(),
                        curId: $('#curId').val(),
                        account: $("#payerAcc").val()
                    });
                }
            } else {
                $('input.text').val('');
            }
        }
    });

    //资金下拨账号
    var downAccInit = bt.selectTable.init({
        elem: "#downAccTable",
        url: basePath + "/netbank/nisbillheadPool/getDownAcc",
        urldata: {corpId: $("#accCorpId").val(), curId: $('#curId').val(), account: $("#payerAcc").val()},
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
            } else {
                $('input.text').val('');
            }
        }
    });

    var sysVoucherCode = $("#sysVoucherCode").val();//系统结算方式
    var isAccount = $("#isAccount").val();//是否记账
    //02-转付，且记账情况下
    if (sysVoucherCode == "02" && isAccount == "1") {
        $("#downAccDiv").show();//下拨账号显示
        $("#downAccNameDiv").show();
        $("#downAcc").attr("lay-verify", "required");
    } else {
        $("#downAccDiv").hide();//下拨账号隐藏
        $("#downAccNameDiv").hide();
        $("#downAcc").removeAttr("lay-verify");
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/netbank/nisbillheadPool/batchSetPayerAcc";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrentTable();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 从当前页面获取表格数据并设置值
     */
    function getCurrentSelectedData() {
        dataSelect = bt.navtab.getCurrentDom().bt.datatable.getSelectData('#appTable');
        var ids ='';
        if(dataSelect){
            bt.each(dataSelect,function (index,item) {
                ids += item.id +',';
            })
        }
        ids = ids.slice(0,ids.length-1);
        $('#id').val(ids);
    }
});
