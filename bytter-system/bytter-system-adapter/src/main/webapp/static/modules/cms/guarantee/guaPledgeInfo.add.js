var dateTypes, moneyTypes, selectTypes, dicts;
bt.use(['form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType',
        'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable'],
    function () {
        $ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
        dateTypes = bt.dateType.init();//时间
        moneyTypes = bt.moneyType.init();//金额
        selectTypes = bt.selectType.init();//下拉框
        dicts = bt.dicts.init();//字典
        bt.bankInfo.init();//联行号
        var corp = bt.corp.init();

        // 附件处理
        var gid = $("#annexName").val();
        var up = bt.btUpload.init({
            elem: "#uploader",
            isExpand: true,
            auto: false,
            gid: gid
        });

        //监听提交
        form.on('submit', function (data) {
            var action = "/cms/guarantee/pledgeInfo/tempOrSubmit";
            data.field.status = $(this).attr("value");
            data.field.attachmentId = up.getGroupId();
            // if (corp.get('regCorp').data()[0].corpName) {
            //     data.field.regCorpName = corp.get('regCorp').data()[0].corpName
            // }
            data.field.corpName = $('#corpName').val();
            data.field.regCorpName = $("#regCorpName").val();
            data.field.curName = selectTypes.get('curId').getData().obj.curName;
            // data.field.corpName = corp.get('corpId').data()[0].corpName;
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {
                    icon: result.success ? 1 : 2
                }, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (!bt.navtab) {
                            location.reload();
                            bt.navtab.refreshParentTable();
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        } else {
                            if (data.field.id == "") {
                                bt.navtab.refreshCurrent();
                            } else {
                                bt.navtab.closeCurrent();
                            }
                        }
                    }
                })
            }, 'JSON');
            return false;
        });
        // 抵押信息的显示与隐藏
        mortgageInfoShowOrHide();
    });

bt.changeType = function (pledgeAttr) {
    var getCurrentSelect = selectTypes.get("pledgeType");
    getCurrentSelect.attr({
        "data-url": basePath + "/cms/guarantee/pledgeType/json/" + pledgeAttr.value,
        "data-defaultvalue": ""
    });
    selectTypes.render();
    // 抵押信息的显示与隐藏
    $('#pledgeAttr').val(dicts.get('pledgeAttr').val());
    mortgageInfoShowOrHide();
}

/**
 *抵押信息的显示与隐藏
 */
function mortgageInfoShowOrHide() {
    var pledgeAttr = $("#pledgeAttr").val();
    if (pledgeAttr == '1') {
        $(".mortgageInfo").hide();
    } else {
        $(".mortgageInfo").show();
    }
}

function corpCallback(ele) {
    $.get(basePath + "/cms/guarantee/pledgeInfo/getCenNetId?corpId="+ ele,function(data){
        if(data != null){
            $('#corpName').val(data.corpName);
        } else {
            $('#corpName').val('');
        }

    },'JSON');
}

function corpRegCallback(ele) {
    $.get(basePath + "/cms/guarantee/pledgeInfo/getCenNetId?corpId="+ ele,function(data){
        if(data != null){
            $('#regCorpName').val(data.corpName);
        } else {
            $('#regCorpName').val('');
        }

    },'JSON');
}
