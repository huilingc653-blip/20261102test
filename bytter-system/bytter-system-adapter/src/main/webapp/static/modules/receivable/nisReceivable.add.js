bt.use(['form', 'layedit', 'dateType', 'corp', 'selectTable', 'bank', 'rateType',
    'bankInfo', 'moneyType', 'region', 'externalCorpBankAcc', 'accebillInfo',
    'accebillView', 'btUpload', 'dicts', 'selectType', 'inputExternalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dicts = bt.dicts.init();
    bt.rateType.init();

    /**
     * 供应商查询
     * @param value
     */
    seachExternalCorpBankAcc = function (value) {
        value = encodeURI(value);
        var url = basePath + "/sys/externalCorp/listByInputExternalCorp?corpId=" + corp.get("corpId").val() + "&seach=" + value;
        inputExternalCorpBankAcc.get("oppAccName").setUrl({url: url});
    }

    /**
     * 供应商查询选择回调
     * @param data
     * @param dom
     *
     */

    inputExternalCorpCallback = function (value) {
        if (value) {
            var data = value.data;
            $("#oppAccNo").val(data.externalAcc);
            $("#oppAccName").val(data.name);
            $("#oppBankCode").val(data.bankSourceCode);
            $("#oppAccBank").val(data.bank);
        }
    }

    var inputExternalCorpBankAcc = bt.inputExternalCorpBankAcc.init();

    var selectType = bt.selectType.init();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.bank.init({elem: '#oppAccNo'});
    bt.bankInfo.init();
    var region = bt.region.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //type 空是所有 0客商 1单位
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: ""
        });
        return true;
    }


    //本方账号
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/receivable/nisreceivable/getBankAcc",
        urldata: {corpId: $("#accCorpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
                $('#accName').val(data.accName);
                $('#bankName').val(data.bankName);
                $("#curDiv").text(data.curName);
                $('#curId').val(data.curId);
                moneyType.get("balance").val(data.balance);
                $("#balanceDiv").text(moneyFun.format(data.balance));
            } else {
                $('input.text').val('');
            }
        }
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
                        $("#accCorpId").val(data.obj.userAccountCode);
                        bankAccInit.get("bankAcc").reload({corpId: $("#accCorpId").val()});
                    }
                }
                form.render();
            }
        });
        // 重新设置业务类别
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
        bt.selectType.get("busType").attr("data-url", busTypeUrl);
    };


    //初始化单位
    var corp = bt.corp.init();

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#oppAccNo").val(data.externalAcc);
            inputExternalCorpBankAcc.get('oppAccName').val(data.name);
            $("#oppBankCode").val(data.bankSourceCode);
            $("#oppAccBank").val(data.bank);
        }else{
            $("#oppAccNo").val(data.bankAcc);
            inputExternalCorpBankAcc.get('oppAccName').val(data.accName);
            $("#oppBankCode").val(data.bankCode);
            $("#oppAccBank").val(data.bankName);
        }
    };

    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#oppBankCode").val(data.bankCode);
        $("#oppAccBank").val(data.bankName);
    };

    //监听提交
    form.on('submit(submit)', function (data) {
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        var action = "/receivable/nisreceivable/save";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent();
                        bt.navtab.refreshParentTable();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

});

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}