bt.use(['form', 'layedit', 'dateType', 'corp', 'selectTable', 'moneyType', 'rateType', 'dicts', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();
    var rateType = bt.rateType.init();
    var selectType = bt.selectType.init();

    //通知存款账号
    var noticeAccountInit = bt.selectTable.init({
        elem: "#noticeAccountTable",
        url: basePath + "/finacial/interest/single/nod/getNoticeAccount",
        urldata: {corpId: $("#defaultCorpId").val()},
        scrollX: "500px",
        scrollY: "200px",
        searchInput: "#noticeAccount",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#noticeAccountId').val(data.id);
                $("#noticeAccount").val(data.blnctrAcc);
                // 币别
                $('#curId').val(data.curId);
                // 通知类型
                dicts.get('noticeTypeShow').val(data.notifyType);
                $('#noticeType').val(data.notifyType);
                // 余额
                moneyType.get("balance").val(data.fcBalance);
                // 利率
                rateType.get('interestRate').val(data.interestRate);
                // 起息日期
                dateType.get("interestStartDate").val(data.interestStartDate);

                if (data.relationAcc) {
                    // 利息转入账号
                    $('#interestAcc').val(data.relationAcc);
                    $('#interestAccName').val(data.relationAccName);
                    $('#interestAccId').val(data.relationAccId);
                }
            } else {
                $('input.text').val('');
            }
        }
    });


    //预计利息计算
    function setExpectInterest() {
        //预计利息=预约金额*通知利率*（预约日期-起息日期）/365
        var bookMoney = moneyType.get("bookMoney").val();
        var interestRate = rateType.get('interestRate').val();
        var bookDate = dateType.get('bookDate').val();
        var interestStartDate = dateType.get("interestStartDate").val();
        var dayLimit = dateFun.getBetweenDay(interestStartDate, bookDate);

        var expectInterest = moneyFun.div(moneyFun.mul(moneyFun.mul(bookMoney, interestRate), dayLimit), 365, 2);
        moneyType.get("expectInterest").val(expectInterest);
    }

    //金额回调函数
    bt.bookMoneyCallback = function (data) {
        setExpectInterest();
    }

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //本金存入账号
    var capitalAccInit = bt.selectTable.init({
        elem: "#capitalAccTable",
        url: basePath + "/inneraccount/fcacc/listAccount",
        urldata: {corpId: $("#defaultCorpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#capitalAcc",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#capitalAcc').val(data.blnctrAcc);
                $('#capitalAccName').val(data.accName);
                $('#capitalAccId').val(data.id);
            } else {
                $('input.text').val('');
            }
        }
    });

    //利息存入账号
    var interestAccInit = bt.selectTable.init({
        elem: "#interestAccTable",
        url: basePath + "/inneraccount/fcacc/listAccount",
        urldata: {corpId: $("#defaultCorpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#interestAcc",
        paging: true,
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#interestAcc').val(data.blnctrAcc);
                $('#interestAccName').val(data.accName);
                $('#interestAccId').val(data.id);
            } else {
                $('input.text').val('');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var noticeAccount =  $("#noticeAccount").val();
        var id = $("#id").val();
        $.ajax({
            url: basePath + "/finacial/interest/single/nod/checkAccount",
            method: 'GET',
            async: true,
            data: {account:noticeAccount,id:id},
            dataType: 'json',
            success: function (da) {
                if(da.obj == true){
                    bt.loading.show();
                    var action = "/finacial/interest/single/nod/modifyBooking";
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
                }else{
                    parent.layer.alert(bt.lang("table.interestSingleNod.msg.noticeAccount"), {icon: 2});
                    return false;
                }
            }
        });
        return false;
    });

    //单位回调函数
    bt.corpCallback = function (id) {
        //重新加载通知存款账号
        noticeAccountInit.get("noticeAccount").reload({corpId: id});

        //重新加载本金存入账号
        capitalAccInit.get("capitalAcc").reload({corpId: id, curId: $("#curId").val()});

        interestAccInit.get("interestAcc").reload({corpId: id, curId: $("#curId").val()});

        // 重新设置用途
        var purposeUrl = basePath + "/basic/business/btpurpose?getByList&sysBusType=4&corpId=" + id;
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    }
    //初始化单位
    bt.corp.init();

});


