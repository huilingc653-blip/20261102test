
bt.use(['form', 'bttable','btUpload','date','dicts'], function () {
    var form = bt.form(), $ = bt.jquery;
    var extenrnalId = $('input[name="id"]').val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete:false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });

    var columnsTab = function() {
        var isNative = $("#isNative").val();
        var columns = "";
        //国外
        if(isNative != "1"){
            columns = [
                {
                    data: "externalAcc",
                    render: function (data, row) {
                        return data.externalAcc ? data.externalAcc : "";
                    }
                },
                {
                    data: "englishCode",
                    render: function (data, row) {
                        return data.englishCode ? data.englishCode : "";
                    }
                },
                {
                    data: "externalCorpId",
                    render: function (data, row) {
                        return data.bank ? data.bank : "";
                    }
                },
                {
                    data: "externalAccName",
                    render: function (data, row) {
                        return data.externalAccName ? data.externalAccName : "";
                    }
                },
                {
                    data: 'swiftCode',
                    render: function (data, row) {
                        return data.swiftCode ? data.swiftCode : "";
                    }
                },
                {
                    data: 'localClearNo',
                    render: function (data, row) {
                        return data.localClearNo ? data.localClearNo : "";
                    }
                },
                {
                    data: 'payExcType',
                    render: function (data, row) {
                        return bt.dict("sysExternalCorpBankAcc.payExcType", data.payExcType);
                    }
                },
                {
                    data: 'intermediaryBank',
                    render: function (data, row) {
                        return data.intermediaryBank ? data.intermediaryBank : "";
                    }
                },
                {
                    data: 'intermediaryBankSwiftCode',
                    render: function (data, row) {
                        return data.intermediaryBankSwiftCode ? data.intermediaryBankSwiftCode : "";
                    }
                },
                {
                    data: 'intermediaryBankAcc',
                    render: function (data, row) {
                        return data.intermediaryBankAcc ? data.intermediaryBankAcc : "";
                    }
                },
                {
                    data: 'remark',
                    render: function (data, row) {
                        return data.remark ? data.remark : "";
                    }
                },
                {
                    data: 'isDefaultAcc',
                    render: function (data, row) {
                        return bt.dict("sf_yn", data.isDefaultAcc);
                    }
                }
            ];
        }else{
            //国内
            columns = [
                {
                    data: "externalAcc",
                    render: function (data, row) {
                        return data.externalAcc ? data.externalAcc : "";
                    }
                },
                {
                    data: "englishCode",
                    render: function (data, row) {
                        return data.englishCode ? data.englishCode : "";
                    }
                },
                {
                    data: "externalCorpId",
                    render: function (data, row) {
                        return data.bank ? data.bank : "";
                    }
                },
                {
                    data: "bankSourceCode",
                    render: function (data, row) {
                        return data.bankSourceCode ? data.bankSourceCode : "";
                    }
                },
                {
                    data: 'province',
                    render: function (data, row) {
                        return data.accProvName ? data.accProvName : "";
                    }
                },
                {
                    data: 'city',
                    render: function (data, row) {
                        return data.accCityName ? data.accCityName : "";
                    }
                },
                {
                    data: 'identifyType',
                    render: function (data, row) {
                        return bt.dict("AB_BILL_BL_ID_TPCD", data.identifyType);
                    }
                },
                {
                    data: 'swiftCode',
                    render: function (data, row) {
                        return data.swiftCode ? data.swiftCode : "";
                    }
                },
                {
                    data: 'remark',
                    render: function (data, row) {
                        return data.remark ? data.remark : "";
                    }
                },
                {
                    data: 'isDefaultAcc',
                    render: function (data, row) {
                        return bt.dict("sf_yn", data.isDefaultAcc);
                    }
                }

            ];
        }
        return columns;
    }

    //生成列表
    var bankAccTable = bt.bttable.init({
        scrollX: true,
        scrollY: true,
        elem: "#bankAcc",
        url: basePath + "/sys/externalCorp/bankAcc.json?id=" + extenrnalId,
        buttons: [],
        columns: columnsTab()
    });


    var quickPayColumnsTab = function() {
        var columns = "";
        columns = [
            {
                data: "proName",
                render: function (data, row) {
                    return data.proName ? data.proName : "";
                }
            },
            {
                data: "email",
                render: function (data, row) {
                    return data.email ? data.email : "";
                }
            },
            {
                data: "tel",
                render: function (data, row) {
                    return data.tel ? data.countryCode + "_" +data.tel : "";
                }
            },
            {
                data: "fpsId",
                render: function (data, row) {
                    return data.fpsId ? data.fpsId : "";
                }
            },
            {
                data: 'creditCard',
                render: function (data, row) {
                    return data.creditCard ? data.creditCard : "";
                }
            },
            {
                data: 'hkId',
                render: function (data, row) {
                    return data.hkId ? data.hkId : "";
                }
            },
            {
                data: 'isDefaultAcc',
                render: function (data, row) {
                    return bt.dict("sf_yn", data.isDefaultAcc);
                }
            }
        ];
        return columns;
    }


    if($("#isNative").val() && $("#isNative").val() == "0"){
        //快捷支付信息
        var quickPayTable = bt.bttable.init({
            scrollX: true,
            scrollY: true,
            elem: "#quickPay",
            url: basePath + "/sys/externalCorp/quickPay.json?id=" + extenrnalId,
            buttons: [],
            columns: quickPayColumnsTab()
        });
    }


    //审核状态：0:暂存，11:待审核，95:审核通过，-1:	打回'
    $("#pass").on("click",function(){
        audit(95);
    });

    $("#back").on("click",function(){
        var auditMessage = $('textarea[name="auditMessage"]').val();
        if(!auditMessage){
            parent.layer.alert(bt.lang("bpm.alert.pleaseInputApproveComment"),{icon:2});
            return;
        }
        audit(-1);
    });

    /**
     * 审核
     * @param target
     */
    function audit(target) {
        $.post(basePath + "/sys/externalCorp/audit", {
            ids: $("input[name='id']").val(),
            target: target,
            auditMessage: $('textarea[name="auditMessage"]').val()
        }, function (result) {
            layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                layer.closeAll();
                if (result.success) {
                    if (!bt.navtab) {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    }
                }
            });
        }, 'JSON');
        return false;
    }

    /**
     * 日志列表
     * @param target
     */
    $.get(basePath + "/sys/externalCorp/auditLog?id="+extenrnalId, null, function (result) {
        var html = "";

        var list = result.obj;
        for(var i=0; i<list.length;i++) {
            var temp = list[i];
            html += "<tr>";
            html += "<td>" + (temp.createTime?bt.date.format(temp.createTime, "yyyy-MM-dd hh:mm:ss"):"") + "</td>";
            html += "<td>" + (temp.createLoginName?temp.createLoginName:"") + "</td>";
            html += "<td>" + (temp.createUsername?temp.createUsername:"" )+ "</td>";
            html += "<td>" + bt.dict("externalCorpLog.auditType",temp.auditType) + "</td>";

            html += "<td>" + (temp.auditMessage?temp.auditMessage:"") + "</td>";
            html += "</tr>";
        }
        $("#table_externalCorpAuditLog").append(html);
    }, 'JSON');
    return false;
});
