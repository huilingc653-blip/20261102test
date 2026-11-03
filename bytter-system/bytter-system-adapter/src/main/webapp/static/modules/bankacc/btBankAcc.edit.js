bt.use(['form', 'layedit', 'corp', 'select', 'checkboxs', 'dateForm', 'bankInfo', 'rateType', 'moneyType', 'bank', 'dicts', 'dateType', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.bankInfo.init();
    var rate = bt.rateType.init();
    bt.moneyType.init();
    bt.bank.init({
        elem: '.bank'
    })
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    var test = bt.dateType.init();
    bt.bankTypeChange = function (data, text, result) {
        //如果是建行
        var accType = dicts.get('accType').val();//判断定期
        $("#bankPrefix").val(result.bankPrefix);
        if (result.bankPrefix == '105' && accType == "02") {
            $("#volumeNum").attr("lay-verify", "required");
            $("#bankbookNum").attr("lay-verify", "required");
        } else {
            $("#volumeNum").removeAttr("lay-verify", "required");
            $("#bankbookNum").removeAttr("lay-verify", "required");
        }
        if (result.bankPrefix == '301') {
            $("#privateNo").attr("lay-verify", "required");
        } else {
            $("#privateNo").removeAttr("lay-verify", "required");
        }
        form.renderIcon();//渲染必填
    }
    bt.bankTypeInit = function (elem, relust) {
        var accType = dicts.get('accType').val();//判断定期
        for (var i = 0; i < relust.length; i++) {
            if ($("#bankTypeIdHid").val() == relust[i].id) {
                if (relust[i].bankPrefix == '105') {
                    $("#bankPrefix").val(relust[i].bankPrefix);
                }
                if (relust[i].bankPrefix == '105' && accType == "02") {
                    $("#volumeNum").attr("lay-verify", "required");
                    $("#bankbookNum").attr("lay-verify", "required");
                } else {
                    $("#volumeNum").removeAttr("lay-verify", "required");
                    $("#bankbookNum").removeAttr("lay-verify", "required");
                }
                if (relust[i].bankPrefix == '301') {
                    $("#privateNo").attr("lay-verify", "required");
                } else {
                    $("#privateNo").removeAttr("lay-verify", "required");
                }
            }
        }
        form.renderIcon();//渲染必填
    }
    bt.chang = function (data) {
        var htmlType = '';
        var htmlText = '';
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        if (data.value == '07') {//协定
            var agrContract = $("#agrContract").val();
            var agrInterest = $("#agrInterest").val();
            var agrBeginDateTemp = $("#agrBeginDateTemp").val();
            var agrEndDate = $("#agrEndDateTemp").val();
            htmlType = "<span>" + bt.lang('table.btInnerAccApp.agrMoney') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<input type="text" id="agrMoney" name="agrMoney" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btInnerAccApp.agrMoney') + '">';
            $(".typeText").html(htmlText);

            var html1 = "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                '<td><input type="text" data-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '"></td></tr>';
            var html2 = '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-type="date" type="text"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text"></div></td>' +
                '</tr>';
            $(".tp").after(tr2 + html2).after(tr1 + html1);
            test.render();
            rate.render();
        } else if (data.value == '02') {//定期
            htmlType = "<span>" + bt.lang('table.btBankAccApp.regulars') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-required="true" data-name="regulars" data-biz-name="regulars"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
            $("#ab").attr("class", "ignore-required-hide");
            $("#ac").attr("class", "ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else if (data.value == '06') {//通知
            htmlType = "<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-name="noticeType" data-biz-name="noticeType"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
            $("#ab").attr("class", "ignore-required-hide");
            $("#ac").attr("class", "ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else {
            $(".type").html(htmlType)
            $(".typeText").html(htmlText);
            $("#ab").attr("class", "ignore-required-hide");
            $("#ac").attr("class", "ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        }
        if ($("#bankPrefix").val() == "105" && data.value == '02') {
            $("#volumeNum").attr("lay-verify", "required");
            $("#bankbookNum").attr("lay-verify", "required");
        } else {
            $("#volumeNum").removeAttr("lay-verify", "required");
            $("#bankbookNum").removeAttr("lay-verify", "required");
        }
        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }
    var dicts = bt.dicts.init();
    getType();
    bt.bankinfoCallback = function (data, dom) {
        /**省*/
        $("#prov").val(data.prov);
        $("#city").val(data.city);
        $("#bankCode").val(data.bankCode);
        $("#bankName").val(data.bankName);
        bt.select.get("bankTypeId").val(data.bankTypeId);
        if (data.bankTypeId) {
            bt.select.get("bankTypeId").attr({disabled: true});
        } else {
            bt.select.get("bankTypeId").attr({disabled: false});
        }
        form.render('select');
    }
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: $(item).attr("disabled"),
            required: $(item).attr("data-required"),
            change: $(item).attr("data-callback"),
            initCallBack: $(item).attr("initCallBack"),
            url: $(item).attr("fieldhref")
        });
    });
    bt.use(['checkboxs'], function () {
        bt.checkboxs.init({
                elem: ".cks",
                name: "cur",
                valueField: "id",
                textField: "curName",
                url: basePath + "/basic/business/btcurrency/json",
                checkAll: true,
                checkedvalue: $("#curId").val(),
                disabled: true,
                change: function (data) {

                }
            }
        );
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankacc/modify";
        var curIdStr = "";
        $('input[name="cur"]:checked').each(function () {
            curIdStr += $(this).val() + ",";
        });
        data.field.curId = curIdStr;
        data.field.attachmentId = up.getGroupId();
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
            });
        }, 'JSON');
        return false;
    });
    form.on('radio(interestCycle)', function (data) {
        if (data.value == 1) {
            $("#interestDateDiv").show();
        } else {
            $("#interestDateDiv").hide();
        }
    });

    form.on('radio(ratesFloat)', function (data) {
        if (data.value == 1) {
            $("input[data-id='ratesFloatPer']").attr("lay-verify", "required");
            $("input[data-id='ratesFloatPer']").removeAttr("disabled");
        } else {
            $("input[data-id='ratesFloatPer']").val("");
            $("input[data-id='ratesFloatPer']").attr("disabled", "disabled");
            $("input[data-id='ratesFloatPer']").removeAttr("lay-verify");
        }
        form.renderIcon();//渲染必填
    });

    //根据账户类型得到相应的信息
    function getType() {
        var type = dicts.get('accType').val();
        var regulars = $("#regulars").val();
        var agrMoney = $("#agrMoney").val();
        var noticeType = $("#noticeType").val();
        var agrContract = $("#agrContract").val();
        var agrInterest = $("#agrInterest").val();
        var agrBeginDate = $("#agrBeginDate").val();
        var agrEndDate = $("#agrEndDate").val();
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        if ("02" == type) {//定期
            $(".type").html("<span>" + bt.lang('table.btBankAccApp.regulars') + "</span>");
            $(".typeText").html('<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars"></div>');
            bt.dicts.render();
            if ($("#bankPrefix").val() == "105") {
                $("#volumeNum").attr("lay-verify", "required");
                $("#bankbookNum").attr("lay-verify", "required");
            } else {
                $("#volumeNum").removeAttr("lay-verify", "required");
                $("#bankbookNum").removeAttr("lay-verify", "required");
            }
        } else if ('07' == type) {//协定
            $(".type").html("<span>" + bt.lang('table.btBankAcc.agrMoney') + "</span>");
            $(".typeText").html('<input type="text" id="agrMoney" name="agrMoney" lay-verify="required" value="' + agrMoney + '" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btBankAcc.agrMoney') + '">');
            var html1 = "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '" value="' + agrContract + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                '<td><input type="text" data-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '" value="' + agrInterest + '"></td></tr>';
            var html2 = '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text" value="' + agrBeginDate + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text" value="' + agrEndDate + '"></div></td>' +
                '</tr>';
            $(".tp").after(tr2 + html2).after(tr1 + html1);
            rate.render();
            test.render();
        } else if ('06' == type) {//通知
            $(".type").html("<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>");
            $(".typeText").html('<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType"></div>');
            bt.dicts.render();
        } else {
            $(".type").html("")
            $(".typeText").html("");
        }
        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }
});
