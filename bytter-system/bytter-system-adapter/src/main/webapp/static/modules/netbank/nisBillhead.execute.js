bt.billhead = {};
bt.use(['form', 'layedit', 'datatable', 'selectTable', 'moneyType', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    var moneyType = bt.moneyType.init();

    if ($("#allowEditDownacc").val() != "0") {
        //付款账号
        bt.selectTable.init({
            elem: "#payerAccTable",
            url: basePath + "/netbank/nisbillhead/getDownAccByExecute",
            urldata: {
                corpId: $("#corpId").val(),
                curId: $("#curId").val(),
                account: $("#payerAccOld").val(),
                sysVoucherCode: $("#sysVoucherCode").val()
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
                    $('#accId').val(data.id);
                    $('#bifCode').val(data.bifCode);
                    $('#payerBankCode').val(data.bankCode);
                    $('#bankNamePrefix').val(data.bankNamePrefix);
                    $('#payerProv').val(data.prov);
                    $('#payerCity').val(data.city);
                    //下拨账号
                    if ($('#bankAcc')) {
                        $('#bankAcc').val(data.prentBankAcc);
                        $('#bankAccName').val(data.prentAccName);
                    }
                    //判断跨行标志和地域标志
                    difBankAndAreaSign();
                } else {
                    $('input.text').val('');
                }
            }
        });
    }

    if ($("#sysVoucherCode").val() == "02") {
        $.ajax({
            url: basePath + "/bankacc/btbankacc?getBankAccById",
            method: 'POST',
            data: {id: $("#accId").val(), curId: $("#curId").val()},
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    var data = result.obj;
                    $('#payerBankCode').val(data.bankCode);
                    $('#payerProv').val(data.prov);
                    $('#payerCity').val(data.city);
                    //判断跨行标志和地域标志
                    difBankAndAreaSign();
                }
            }
        });
    }

    //跨行标志，地域标志判断
    function difBankAndAreaSign() {
        if ($("#payerBankCode").val() == "" || $("#payeeBankCode").val() == "") {
            dicts.get("difBank").val(0);
        } else if ($("#payerBankCode").val().substr(0, 3) == $("#payeeBankCode").val().substr(0, 3)) {
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
        if ($("#payerProv").val() == "" || $("#payeeProv").val() == "") {
            dicts.get("areaSign").val(0);
        } else if ($("#payerProv").val() == $("#payeeProv").val() && $("#payerCity").val() == $("#payeeCity").val()) {
            dicts.get("areaSign").val(0);
        } else {
            dicts.get("areaSign").val(1);
        }
        form.render('radio');
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/netbank/nisbillhead/sendSave";
        //证书签名
        var signDataMsg = signData("付款指令发送", 6);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
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
    });
});
