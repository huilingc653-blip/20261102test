bt.use(['form', 'layedit', 'dicts', 'selectType', 'selectTable', 'btUpload', 'moneyType','dateType','allExternalBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    var dateType = bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var selectType = bt.selectType.init();
    var allExternalBankAcc = bt.allExternalBankAcc.init();

    bt.externalBankAccBeforeClick = function () {
        allExternalBankAcc.set({
            oppId:$("#oppId").val() || '',
        })
        return true;
    }

    /**操作类型 0-结算处理、1-修改支付方式、2-修改单位账户、3-交易对手账户、4-更新支付状态*/
    var opType = $('#opType').val();
    if(opType=="0") {
        var tranUp = bt.btUpload.init({
            elem: "#tranUploader",
            auto: false,
            hasDelete: false,
            hasUpload: false,
            isExpand: true,
            gid: $("#tranAttachmentId").val()
        });
        var up = bt.btUpload.init({
            elem: "#uploader",
            auto: false,
            gid: $("#attachmentId").val()
        });
    }
    if(opType=="2") {
        var tranType = $("#tranType").val();
        var url = basePath + "/foreign/fexcashflow/getCorpBankAcc";
        var urldata = {corpId: $("#corpId").val(), curId: $("#curId").val(), bankType: ""}
        // 收付交易需要根据模板四要素限制
        if ("4" == tranType){
            url = basePath + "/foreign/fexcashflow/getCorpBankAccByTemplate";
            urldata = {
                corpId: $("#corpId").val(), curId: $("#curId").val(),
                corpAccBankTypeId: $("#corpAccBankTypeId").val(),
                corpAccCountryId: $("#corpAccCountryId").val(),
                bifCode: $("#corpAccBifCode").val()
            }
        }
        //主体账户信息
        var corpBankAccInit = bt.selectTable.init({
            elem: "#corpBankAccTable",
            url: url,
            urldata: urldata,
            scrollX: true,
            scrollY: "200px",
            searchInput: "#corpBankAcc",
            paging: true,
            columns: [
                {data: "bankAcc",},
                {data: "accName",},
                {data: "bankName",},
                {data: "curName",}
            ],
            clickCallback: function (data) {
                if (data) {
                    $('#corpBankAcc').val(data.bankAcc);
                    $('#corpAccId').val(data.id);
                    $('#corpAccName').val(data.accName);
                    $('#corpId').val(data.corpId);
                    $("#corpAccNameDiv").text(data.accName);
                    $("#corpNameDiv").text(data.corpName);
                    $('#corpBank').val(data.bankTypeId);
                    $("#corpBankNameDiv").text(data.bankTypeName);
                } else {
                    $('input.text').val('');
                }
            }
        });
    }

    //交易对手银行账户回调函数
    if(opType=="3") {
        bt.cotpBankAccCallback = function (data) {
            if(data.id) {
                $('#cotpBank').val(data.bankid);
                $("#cotpBankNameDiv").text(data.bankname);
                $("#cotpAccId").val(data.id);
                $("#cotpAccName").val(data.accname);
                $("#cotpAccNameDiv").text(data.accname);
                if(data.bankname) {
                    $("#bankId").text(data.bankname);
                }else{
                    $("#bankId").text('');
                }
                if(data.tranbankname) {
                    $("#tranBank").text(data.tranbankname);
                }else{
                    $("#tranBank").text('');
                }
                if(data.biccode) {
                    $("#bicCode").text(data.biccode);
                }else{
                    $("#bicCode").text('');
                }
                if(data.tranbiccode) {
                    $("#tranBicCode").text(data.tranbiccode);
                }else{
                    $("#tranBicCode").text('');
                }
                $("#payType").text(bt.dict("fexCotp.payType", data.paytype));
                if(data.tranbankaddr) {
                    $("#tranBankAddr").text(data.tranbankaddr);
                }else{
                    $("#tranBankAddr").text('');
                }
                if(data.tranbankacc) {
                    $("#tranBankAcc").text(data.tranbankacc);
                }else{
                    $("#tranBankAcc").text('');
                }
                if(data.bankaddr) {
                    $("#bankAddr").text(data.bankaddr);
                }else{
                    $("#bankAddr").text('');
                }
                if(data.bankrmk) {
                    $("#bankRmk").text(data.bankrmk);
                }else{
                    $("#bankRmk").text('');
                }
            }else{
                $('#cotpBank').val('');
                $("#cotpBankNameDiv").text('');
                $("#cotpAccId").val('');
                $("#cotpAccName").val('');
                $("#cotpAccNameDiv").text('');
                $("#bankId").text('');
                $("#tranBank").text('');
                $("#bicCode").text('');
                $("#tranBicCode").text('');
                $("#payType").text('');
                $("#tranBankAddr").text('');
                $("#tranBankAcc").text('');
                $("#bankAddr").text('');
                $("#bankRmk").text('');
            }
        };
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        if(opType=="0") {
            data.field.attachmentId = up.getGroupId();
            data.field.status = $(this).attr("value");
        }
        var action = "/foreign/fexcashflow/modify";
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

var externalBankAccCallback = function (data) {
    if(data) {
        if (data.type == "corpBankAcc") {
            $('#cotpBank').val(data.bank);
            $('#cotpBankAcc').val(data.bankAcc);
            $("#cotpBankNameDiv").text(data.bank);
            //$("#cotpAccId").val(data.id);
            $("#cotpAccName").val(data.accName || "");
            $("#cotpAccNameDiv").text(data.accName || "");
            //$("#bankId").text(data.bank);
            $("#curId").text(data.curId || "");
        }else{
            $('#cotpBank').val(data.bank);
            $('#cotpBankAcc').val(data.externalAcc);
            $("#cotpBankNameDiv").text(data.bank);
            //$("#cotpAccId").val(data.id);
            $("#cotpAccName").val(data.externalAccName || "");
            $("#cotpAccNameDiv").text(data.externalAccName || "");
            $("#curId").text(data.curId || "");
            if(data.bank) {
                $("#bankIdDiv").text(data.bank);
            }else{
                $("#bankIdDiv").text('');
            }
            $("#bankName").val(data.bank);

            if(data.intermediaryBank) {
                $("#tranBankDiv").text(data.intermediaryBank);
            }else{
                $("#tranBankDiv").text('');
            }
            $("#tranBank").val(data.intermediaryBank);

            if(data.swiftCode) {
                $("#bicCodeDiv").text(data.swiftCode);
            }else{
                $("#bicCodeDiv").text('');
            }
            $("#bicCode").val(data.swiftCode);

            if(data.intermediaryBankSwiftCode) {
                $("#tranBicCodeDiv").text(data.intermediaryBankSwiftCode);
            }else{
                $("#tranBicCodeDiv").text('');
            }
            $("#tranBicCode").val(data.intermediaryBankSwiftCode)

            $("#payTypeDiv").text(bt.dict("sysExternalCorpBankAcc.payExcType", data.payExcType));
            $("#payType").val(data.payExcType);

            if(data.intermediaryBankAddress) {
                $("#tranBankAddrDiv").text(data.intermediaryBankAddress);
            }else{
                $("#tranBankAddrDiv").text('');
            }
            $("#tranBankAddr").val(data.intermediaryBankAddress);

            if(data.intermediaryBank) {
                $("#tranBankAccDiv").text(data.intermediaryBank);
            }else{
                $("#tranBankAccDiv").text('');
            }
            $("#tranBankAcc").val(data.intermediaryBank);

            if(data.bankAddress) {
                $("#bankAddrDiv").text(data.bankAddress);
            }else{
                $("#bankAddrDiv").text('');
            }
            $("#bankAddr").val(data.bankAddress);

            if(data.bankrmk) {
                $("#bankRmkDiv").text(data.bankrmk);
            }else{
                $("#bankRmkDiv").text('');
            }
            $("#bankRmk").val(data.bankrmk);
        }
    }
};
