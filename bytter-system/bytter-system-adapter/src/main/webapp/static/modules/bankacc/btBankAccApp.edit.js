bt.use(['form', 'layedit', 'corp', 'select', 'checkboxs', 'bankInfo', 'region', 'dicts', 'btUpload', 'moneyType','rateType','dateType','swiftInfo','selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var bankInfo = bt.bankInfo.init();
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }
    var test = bt.dateType.init();
    var rate = bt.rateType.init();
    var selectType = bt.selectType.init();

    var rmb_id = ""; //人民币数据的id

    bt.chang = function (data) {
        var htmlType = '';
        var htmlText = '';
        var curTemp = $("#curId").val();
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        var chekboxObj = ck.get("cur");
        if (data.value == '07') {//协定
            var agrContract = $("#agrContract").val();
            var agrInterest = $("#agrInterest").val();
            var agrBeginDateTemp = $("#agrBeginDateTemp").val();
            var agrEndDate = $("#agrEndDateTemp").val();
            htmlType = "<span>"+bt.lang('table.btInnerAccApp.agrMoney')+"</span>";
            $(".type").html(htmlType);
            htmlText = '<input type="text" id="agrMoney" name="agrMoney" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="'+bt.lang('table.btInnerAccApp.agrMoney')+'">';
            $(".typeText").html(htmlText);
            var html1 = "<td class='textRight'>"+bt.lang('table.btBankAcc.agrContract')+"</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="'+bt.lang('table.btBankAcc.agrContract')+'"></div></td>' +
                '<td class="textRight">'+bt.lang('table.btBankAcc.agrInterest')+'</td>' +
                '<td><input type="text" data-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="'+bt.lang('table.btBankAcc.agrInterest')+'"></td></tr>';
            var html2 = '<td class="textRight">'+bt.lang('table.btBankAcc.agrBeginDate')+'</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-type="date" type="text"></div></td>' +
                '<td class="textRight">'+bt.lang('table.btBankAcc.agrEndDate')+'</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text"></div></td>' +
                '</tr>';
            $(".tp").after(tr2+html2).after(tr1+html1);
            test.render();
            rate.render();
        } else if (data.value == '02') {//定期
            htmlType = "<span>" + bt.lang('table.btBankAccApp.regulars') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-required="true" data-name="regulars" data-biz-name="regulars"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else if (data.value == '06') {//通知
            htmlType = "<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-name="noticeType" data-biz-name="noticeType"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else if (data.value == '10') {//票据专户
            $("#electricBill").val("1");
            // chekboxObj.val(rmb_id);
            htmlType = "<span>" + bt.lang('table.btBankAccApp.circulationMode') + "</span>";
            $(".type").html(htmlType)
            htmlText = '<div class="bt-biz-dict" data-type="radio" data-group="bankAcc.circulationMode" data-required="true" data-defaultvalue="0" data-name="circulationMode" data-biz-name="circulationMode"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else if (data.value == '09') {//虚拟户
            htmlType = "<span>" + bt.lang('table.btBankAccApp.entityAcc') + "</span>";
            $(".type").html(htmlType);
            var corpId = corp.get('corpId').val();
            var bankCode = $("#bankCode").val();
            htmlText = '<div class="entityAccId"></div>';
            $(".typeText").html(htmlText);
            bt.select.init({
                elem: ".entityAccId",
                name: "entityAccId",
                defaultVal: $("#entityAccId").val(),
                valueField: "id",
                textField: "bankAcc",
                required: true,
                search: true,
                disabled: false,
                url: basePath+"/bankacc/btbankacc/getEntityAcc?corpId="+corpId+"&bankCode="+bankCode
            });
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else if (data.value == '04') { //保证金
            //$(".cks").addClass("flag");
            //chekboxObj.attr('data-abledvalue','all');
            //chekboxObj.attr('data-disabledvalue','all');
            //chekboxObj.val(curTemp);
            //chekboxObj.attr('data-abledvalue',curTemp);
            $(".type").html(htmlType)
            $(".typeText").html(htmlText);
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        } else {
            $(".type").html(htmlType)
            $(".typeText").html(htmlText);
            $("#ab").attr("class","ignore-required-hide");
            $("#ac").attr("class","ignore-required-hide");
            $("#ab").hide();
            $("#ac").hide();
        }
        if(data.value !== "10"){
            $("#electricBill").val("0");
            //if(data.value !== "04") {
                //除了保证金全选全可用
                if ($(".cks").hasClass("flag")) {
                    chekboxObj.attr('data-abledvalue', 'all');
                }
            //}
        }else{
            //除了保证金全选全可用
            if ($(".cks").hasClass("flag")) {
                chekboxObj.attr('data-abledvalue', 'all');
                chekboxObj.val(rmb_id);
            }
        }
        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }

    bt.corpCallback = function(id){
        $.ajax({
            url: basePath+"/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function(data){
                if(data.obj){
                    if(data.obj.userAccountCode){
                        $("#accName").val(data.obj.name);
                    }
                }
                form.render();
            }
        });
    }
    var corp = bt.corp.init();
    // getType();
    var region = bt.region.init();

    //银行类别回调
    bt.bankTypeCallback = function(data){
        $("#bankCode").val('');
        $("#bankName").val('');
        $("#prov").val('');
        $("#city").val('');
        if (data.isdomesticbank === undefined || data.isdomesticbank == '1') {//境内
            $(".isSwiftN").show();
            $(".isSwiftY").hide();
            //selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/1"});
        }else{//境外
            $(".isSwiftN").hide();
            $(".isSwiftY").show();
            //selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/0"});
        }
        form.render('select');
    }

    //swiftcode选择
    bt.swiftInfoCallback = function (data, dom) {
        $("#bankCode").val(data.swiftCode);
        $("#bankName").val(data.bankName);
        $("#prov").val(data.countryCode);
        $("#city").val(data.cityName);
    };

    bt.swiftInfo.init();
    var dicts = bt.dicts.init();

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    bt.bankinfoCallback = function (data, dom) {
        /**省*/
        $("#prov").val(data.prov);
        $("#city").val(data.city);
        $("#bankCode").val(data.bankCode);
        $("#bankName").val(data.bankName);
        // bt.select.get("bankTypeId").val(data.bankTypeId);
        selectType.get("bankTypeId").val(data.bankTypeId);
    }

    var ck=bt.checkboxs.initGetCurrent({
        elem: ".cks",
        name: "cur",
        valueField: "id",
        textField: "curName",
        url: basePath + "/basic/business/btcurrency/json",
        checkAll: true,
        initCompleteCallback:getType,
        required:true,
        checkedValue: $("#curId").val(),
        change: function (data,allData) {
            var chekboxObj = ck.get("cur");
            var type = dicts.get("accType").val();
            if(data.value ==='all'){
                if('10' == type && !chekboxObj.getValue()){
                    chekboxObj.val(rmb_id);
                }
                return;
            }
            /*var isChecked = data.othis.hasClass('layui-form-checked');
            if(type && '04' == type){
                if(isChecked){
                    chekboxObj.attr('data-abledvalue',data.value);
                }else{
                    chekboxObj.attr('data-abledvalue','all');
                    chekboxObj.attr('data-disabledvalue','all');
                }
            }*/
            if(data.value === rmb_id && '10' == type){
                var value = chekboxObj.getValue();
                if(value){
                    if(value.indexOf(rmb_id) === -1){
                        value = value+','+rmb_id;
                    }
                }else {
                    value = rmb_id
                }
                var arr = value.split(',');
                if(arr.length === allData.length){
                    value = 'all,' + value;
                }
                chekboxObj.val(value);
            }
        }
    });

    var isCurrencyExpanded = false;
    window.toggleCurrencyList = function() {
        var cksContainer = $('.cks');
        var toggleBtn = $('#toggleCurrencyBtn');
        var toggleText = $('#toggleCurrencyText');

        isCurrencyExpanded = !isCurrencyExpanded;

        if (isCurrencyExpanded) {
            cksContainer.addClass('expanded');
            toggleBtn.parent().addClass('expanded');
            toggleText.text('收起');
        } else {
            cksContainer.removeClass('expanded');
            toggleBtn.parent().removeClass('expanded');
            toggleText.text('查看更多');
            cksContainer.scrollTop(0);
        }
    };

    function initCurrencyExpandButton(data) {
        var currencyCount = data ? data.length : 0;
        var expandBtnContainer = $('.currency-expand-btn');

        if (currencyCount > 18) {
            console.log("currencyCount币别个数:" + currencyCount)
            expandBtnContainer.show();
        } else {
            expandBtnContainer.hide();
            $('.cks').addClass('expanded');
        }

    }

    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            required: $(item).attr("data-required"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

    //监听提交
    form.on('submit(submit)', function (data) {

        saveData(data,1);

        return false;
    });

    //监听暂存
    form.on('submit(temp)', function (data) {

        saveData(data,0);

        return false;
    });

    /**
     * 提交数据方法
     * data 提交后台的数据
     * flag 0 代表为暂存；1 代表为提交
     * */
    function saveData(data,flag) {
        console.log(data)
        console.log(data.field)
        bt.loading.show();
        var isHasCur = false;
        var cur = data.field.cur;
        for (var i = 0; i < cur.length; i++){
            if (cur[i] === rmb_id) {
                isHasCur = true;
            }
        }
        //判断票据专户必须得有人民币
        if(!isHasCur && data.field.accType === "10"){
            layer.alert(bt.lang("table.btBankAcc.electric.rmb"), {icon: 2});
            bt.loading.hide();
            return false;
        }
        /*var opRession = data.field.opRession;
        if(opRession.length>50){
            layer.alert(bt.lang("table.btBankAccApp.opRession.length"), {icon: 2});
            bt.loading.hide();
            return false;
        }*/
        data.field.curId = cur.join(",");
        data.field.flag = flag;
        data.field.attachmentId = up.getGroupId();
        $.post(basePath + "/bankacc/btbankaccapp/modify", data.field, function (result) {
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
    }

    //获取人民币id
    function getRmbId(data) {
        // if(!data){
        //     return '';
        // }
        // for( var i=0; i<data.length;i++){
        //     if(data[i].id == "4028802e5c346968015c346c18850008"){
        //         return data[i].id;
        //     }
        // }
        //
        // return '';
        return "4028802e5c346968015c346c18850008"
    }

    function getType(data) {
        rmb_id = getRmbId(data);
        var type = dicts.get('accType').val();
        var regulars = $("#regulars").val();
        var agrMoney = $("#agrMoneyHid").val();
        var noticeType = $("#noticeType").val();
        var agrContract = $("#agrContract").val();
        var agrInterest = $("#agrInterestHid").val();
        var agrBeginDate = $("#agrBeginDateTemp").val();
        var agrEndDate = $("#agrEndDateTemp").val();
        var circulationMode = $("#circulationMode").val();
        var curTemp = $("#curId").val();
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        var chekboxObj = ck.get("cur");

        initCurrencyExpandButton(data);
        if ("02" == type) {//定期
            $(".type").html("<span>" + bt.lang('table.btBankAccApp.regulars') + "</span>");
            $(".typeText").html('<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars"></div>');
            bt.dicts.render();
        } else if ('07' == type) {//协定
            $(".type").html("<span>"+bt.lang('table.btBankAcc.agrMoney')+"</span>");
            $(".typeText").html('<input type="text" id="agrMoney" name="agrMoney" lay-verify="required" value="'+agrMoney+'" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="'+bt.lang('table.btBankAcc.agrMoney')+'">');
            var html1 = "<td class='textRight'>"+bt.lang('table.btBankAcc.agrContract')+"</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="'+bt.lang('table.btBankAcc.agrContract')+'" value="'+agrContract+'"></div></td>' +
                '<td class="textRight">'+bt.lang('table.btBankAcc.agrInterest')+'</td>' +
                '<td><input type="text" data-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="'+bt.lang('table.btBankAcc.agrInterest')+'" value="'+agrInterest+'"></td></tr>';
            var html2 = '<td class="textRight">'+bt.lang('table.btBankAcc.agrBeginDate')+'</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text" value="'+agrBeginDate+'"></div></td>' +
                '<td class="textRight">'+bt.lang('table.btBankAcc.agrEndDate')+'</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text" value="'+agrEndDate+'"></div></td>' +
                '</tr>';
            $(".tp").after(tr2+html2).after(tr1+html1);
            rate.render();
            test.render();
        } else if ('06' == type) {//通知
            $(".type").html("<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>");
            $(".typeText").html('<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType"></div>');
            bt.dicts.render();
        } else if (type == '10') {//票据专户
            $("#electricBill").val("1");
            if(!circulationMode){
                circulationMode = "0";
            }
            htmlType = "<span>" + bt.lang('table.btBankAccApp.circulationMode') + "</span>";
            $(".type").html(htmlType)
            htmlText = '<div class="bt-biz-dict" data-type="radio" data-group="bankAcc.circulationMode" data-required="true" data-defaultvalue="' + circulationMode + '"  data-name="circulationMode" data-biz-name="circulationMode"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
        } else if (type == '09') {//虚拟户
            var htmlType = "<span>" + bt.lang('table.btBankAccApp.entityAcc') + "</span>";
            $(".type").html(htmlType);
            var corpId = corp.get('corpId').val();
            var bankCode = $("#bankCode").val();
            var htmlText = '<div class="entityAccId"></div>';
            $(".typeText").html(htmlText);

            bt.select.init({
                elem: ".entityAccId",
                name: "entityAccId",
                defaultVal: $("#entityAccId").val(),
                valueField: "id",
                textField: "bankAcc",
                required: true,
                search: true,
                disabled: false,
                url: basePath+"/bankacc/btbankacc/getEntityAcc?corpId="+corpId+"&bankCode="+bankCode
            });
        } else if (type == '04') { //保证金

            //$(".cks").addClass("flag");
            //chekboxObj.attr('data-abledvalue','all');
            //chekboxObj.attr('data-disabledvalue','all');
            $(".type").html("")
            $(".typeText").html("");
            //chekboxObj.attr('data-abledvalue',curTemp);
        } else {
            $(".type").html("")
            $(".typeText").html("");
        }

        if(type !== "10"){
            $("#electricBill").val("0");
            //if(type !== "04") {

                //除了保证金全选全可用
                if ($(".cks").hasClass("flag")) {
                    chekboxObj.attr('data-abledvalue', 'all');
                }
            //}
        }else{

            //除了保证金全选全可用
            if ($(".cks").hasClass("flag")) {
                chekboxObj.attr('data-abledvalue', 'all');
            }
        }

        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }
});

function resetCallback() {
    window.location.reload();
}
