bt.use(['form', 'upload','layedit', 'btdate','mulselect', 'corp', 'select', 'linkage', 'checkboxs', 'fcAccGroup', 'dateForm', 'bankInfo', 'dicts', 'rateType', 'moneyType', 'dateType', 'btUpload','swiftInfo','selectType', 'bttable'], function () {
    var $ = bt.jquery, form = bt.form();
    var fcAccGroup = bt.fcAccGroup.init();
    var mulselectType=bt.mulselect.init();
    //生成列表
    var rowMR = $("#dataListSize").val()-1;
    bt.bttable.init({
        elem: "#virtualTable",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                rowMR++;
                var html = '<tr row=' + rowMR + '>';
                html += '<td><input type="text" id="virtualNo' + rowMR + '" name="bankAccVirtual[' + rowMR + '].virtualNo" lay-verify="required|strlength" data-maxlength="10"></td>';
                html += '<td><input type="text" id="virtualName' + rowMR + '" name="bankAccVirtual[' + rowMR + '].virtualName" lay-verify="required|strlength" data-maxlength="20"></td>';
                html += '<td><div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.bankCode" data-defaultvalue="" data-name="bankAccVirtual[' + rowMR + '].bankCode" data-biz-name="bankCode' + rowMR + '" data-required="true"></div></td>';
                html += '<td><div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.overCtrl" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].overCtrl" data-biz-name="overCtrl' + rowMR + '" data-required="true"></div></td>';
                html += '<td><div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.closeType" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].closeType" data-biz-name="closeType' + rowMR + '" data-required="true"></div></td>';
                html += '<td><div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.backType" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].backType" data-biz-name="backType' + rowMR + '" data-required="true"></div></td>';
                html += '<td><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a></td>';
                html += '</tr>';
                $("#virtualTable").find("tbody").find("#sumTr").before(html);
                bt.dicts.init();
                form.render();
            }
        }],
        columns: [{
            data: "virtualNo",
            render: function (data, row) {
                return '<input type="text" id="virtualNo' + rowMR + '" name="bankAccVirtual[' + rowMR + '].virtualNo" value="" lay-verify="required|strlength" data-maxlength="10">';
            }
        }, {
            data: "virtualName",
            render: function (data, row) {
                return '<input type="text" id="virtualName' + rowMR + '" name="bankAccVirtual[' + rowMR + '].virtualName" value="" lay-verify="required|strlength" data-maxlength="20">';
            }
        }, {
            data: "bankCode",
            render: function (data, row) {
                return '<div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.bankCode" data-defaultvalue="" data-name="bankAccVirtual[' + rowMR + '].bankCode" data-biz-name="bankCode' + rowMR + '" data-required="true"></div>';
            }
        }, {
            data: "closeType",
            render: function (data, row) {
                return '<div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.overCtrl" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].overCtrl" data-biz-name="overCtrl' + rowMR + '" data-required="true"></div>';
            }
        }, {
            data: "backType",
            render: function (data, row) {
                return '<div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.closeType" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].closeType" data-biz-name="closeType' + rowMR + '" data-required="true"></div>';
            }
        }, {
            data: "backType",
            render: function (data, row) {
                return '<div class="bt-biz-dict" data-type="select" data-group="btBankAccVirtual.backType" data-defaultvalue="Y" data-name="bankAccVirtual[' + rowMR + '].backType" data-biz-name="backType' + rowMR + '" data-required="true"></div>';
            }
        }, {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
            }
        }],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });
    /**
     * 账户组维护查询选择回调
     * @param data
     * @param dom
     */
    fcAccGroupCallback = function (selectedData) {
        console.log(selectedData);
        var ids = ''
        var names = ''
        bt.each(selectedData,function (i,item){
            ids += item.accGroupCode
            names += item.accGroupName
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
            }
        })
        $("#fcAccGroupCode").val(ids)
        $("#fcAccGroupName").val(names)
    }
    receiptTypeCallback = function(code){

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
    var bankInfo = bt.bankInfo.init();
    var selectType = bt.selectType.init();

    var rmb_id = ""; //人民币数据的id

    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }
    var rate = bt.rateType.init();
    bt.moneyType.init();
    var test = bt.dateType.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    //设置验证属性
    var setVerify = function (result,accType) {
        if(result.bankPrefix == '105' && accType == "02"){
            $("#volumeNum").attr("lay-verify","required");
            $("#bankbookNum").attr("lay-verify","required");
        }else{
            $("#volumeNum").removeAttr('lay-verify');
            $("#bankbookNum").removeAttr("lay-verify");
        }
        if(result.bankPrefix == '301'){
            $("#privateNo").attr("lay-verify","required");
        }else{
            $("#privateNo").removeAttr("lay-verify");
        }
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
        var curTemp = $("#curId").val();
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        var chekboxObj = ck.get("cur");

        if (data.value == '07') {//协定
            var agrContract = $("#agrContractTemp").val();
            var agrInterest = $("#agrInterestTemp").val();
            var agrBeginDateTemp = $("#agrBeginDateTemp").val();
            var agrEndDate = $("#agrEndDateTemp").val();
            htmlType = "<span>" + bt.lang('table.btInnerAccApp.agrMoney') + "</span>";
            $(".type").html(htmlType);
            htmlText = '<input type="text" id="agrMoney" name="agrMoney" data-biz-name="agrMoney" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btInnerAccApp.agrMoney') + '">';
            $(".typeText").html(htmlText);

            var html1 = "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                '<td><input type="text" name="agrInterest" data-biz-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '"></td></tr>';
            var html2 = '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" data-biz-name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-type="date" type="text"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" data-biz-name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text"></div></td>' +
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
        } else if (data.value == '10') {//票据专户
            $("#electricBill").val("1");
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
                defaultVal: "",
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
        }else {
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

        if(data.value !== "10"){
            $("#electricBill").val("0");
            //if(data.value !== "04") {
                //除了保证金全选全可用
                if ($(".cks").hasClass("flag")) {
                    chekboxObj.attr('data-abledvalue', 'all');
                    chekboxObj.val(curTemp);
                }
            //}
        }else{
            //除了保证金全选全可用
            if ($(".cks").hasClass("flag")) {
                chekboxObj.attr('data-abledvalue', 'all');
                chekboxObj.val(curTemp);
            }
        }
        showVirtualTag();
        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }
    // getType();
    bt.bankinfoCallback = function (data, dom) {
        /**省*/
        $("#prov").val(data.prov);
        $("#city").val(data.city);
        $("#bankCode").val(data.bankCode);
        $("#bankName").val(data.bankName);
        if (data.bankTypeId) {
            selectType.get("bankTypeId").attr({"data-defaultvalue":data.bankTypeId,disabled: true});
        } else {
            selectType.get("bankTypeId").attr({"data-defaultvalue":data.bankTypeId,disabled: false});
        }
        selectType.get("bifCode").attr({"data-defaultvalue":"first","data-url":basePath+"/basic/business/bisbifinit/getByBankType/"+data.bankTypeId});
        form.render('select');
    }
    //银行类别回调
    bt.bankTypeCallback = function(data){
        $("#bankCode").val('');
        $("#bankName").val('');
        $("#prov").val('');
        $("#city").val('');
        if (data.isdomesticbank === undefined || data.isdomesticbank == '1') {//境内
            $(".isSwiftN").show();
            $(".isSwiftY").hide();
        }else{//境外
            $(".isSwiftN").hide();
            $(".isSwiftY").show();
        }
        selectType.get("bifCode").attr({"data-defaultvalue":"first","data-url":basePath+"/basic/business/bisbifinit/getByBankType/"+data.id});
        form.render('select');
        showVirtualTag();
    }

    //swiftcode选择
    bt.swiftInfoCallback = function (data, dom) {
        $("#bankCode").val(data.swiftCode);
        $("#bankName").val(data.bankName);
        $("#prov").val(data.countryCode);
        $("#city").val(data.cityName);
    };

    bt.swiftInfo.init();


    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false,
            defaultVal: new Date().Format("yyyy-MM-dd")
        });
    });
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
    var ck=bt.checkboxs.initGetCurrent({
        elem: ".cks",
        name: "cur",
        valueField: "id",
        textField: "curName",
        url: basePath + "/basic/business/btcurrency/json",
        checkAll: true,
        checkedvalue: $("#curId").val(),
        fixedCheck:$("#curId").val(),
        initCompleteCallback:getType,
        required:true,
        change: function (data) {
            /*var isChecked = data.othis.hasClass('layui-form-checked');
            var type = dicts.get("accType").val();
            var chekboxObj = ck.get("cur");
            if(type && '04' == type){
                if(isChecked){
                    chekboxObj.attr('data-abledvalue',data.value);
                    $(".cks").addClass("flag");
                }
            }*/
        }
    });

    if ($("input[name='ratesFloat']:checked").val() == 1) {
        $("input[data-id='ratesFloatPer']").attr("lay-verify", "required");
        $("input[data-id='ratesFloatPer']").removeAttr("disabled");
    }

    form.renderIcon();//渲染必填
    if ($("input[name='interestCycle']:checked").val() == 0) {
        $("#interestDateDiv").hide();
    }


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


    //获取人民币id
    function getRmbId(data) {
        return "4028802e5c346968015c346c18850008"
    }

    //监听提交
    form.on('submit(submit)', function (data) {
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

        data.field.curId = cur.join(",");
        data.field.attachmentId = up.getGroupId();

        if(typeof(data.field.isLimitQuota) == "undefined"){
            data.field.isLimitQuota = 0;
        }else if(data.field.isLimitQuota === "on"){
            data.field.isLimitQuota = 1;
        }else{
            data.field.isLimitQuota = 0;
        }

        //组装网银功能字符串
        var arrayUK,joinArray;
        arrayUK = data.field.uKeyFunction;
        if(!bt.empty(arrayUK)){
            joinArray = arrayUK.join(",");
            data.field.uKeyFunction = joinArray;
        }

        $.post(basePath + "/bankacc/btbankaccrevhandle/modify", data.field, function (result) {
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
    /** 邮箱格式验证 begin*/
    form.verify({
        emailFormat: function(value, item){
            if(value!=""){  //值不是空的时候再去走验证
                if(!/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/.test(value)){
                    return bt.lang("sys.form.verify.emailError");
                }
            }
        }
    });
    /** 邮箱格式验证 end*/
    /** 预留印鉴上传begin*/
    layui.upload({
        elem:'#seal1',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger1").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory1").attr("value",res[0].id);
            }
        }
    });
    layui.upload({
        elem:'#seal2',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger2").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory2").attr("value",res[0].id);
            }
        }
    });
    layui.upload({
        elem:'#seal3',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger3").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory3").attr("value",res[0].id);
            }
        }
    });
    $('#imger1,#imger2,#imger3').on('click',function(event){
        showImg($(this));
    });
    /** 预留印鉴上传end*/
    //根据账户类型得到相应的信息
    function getType(data) {
        rmb_id = getRmbId(data);
        var type = dicts.get('accType').val();
        var regulars = $("#regularsTemp").val();
        var agrMoney = $("#agrMoneyTemp").val();
        var noticeType = $("#noticeTypeTemp").val();
        var agrContract = $("#agrContractTemp").val();
        var agrInterest = $("#agrInterestTemp").val();
        var agrBeginDate = $("#agrBeginDateTemp").val();
        var agrEndDate = $("#agrEndDateTemp").val();
        var curTemp = $("#curId").val();
        var tr1 = "<tr id='ab'>";
        var tr2 = "<tr id='ac'>";
        var chekboxObj = ck.get("cur");

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
            $(".typeText").html('<input type="text" id="agrMoney" name="agrMoney" data-biz-name="agrMoney" lay-verify="required" value="' + agrMoney + '" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btBankAcc.agrMoney') + '">');
            var html1 = "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '" value="' + agrContract + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                '<td><input type="text" id="agrInterest" name="agrInterest" data-biz-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '" value="' + agrInterest + '"></td></tr>';
            var html2 = '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" data-biz-name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text" value="' + agrBeginDate + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" data-biz-name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text" value="' + agrEndDate + '"></div></td>' +
                '</tr>';
            $(".tp").after(tr2 + html2).after(tr1 + html1);
            rate.render();
            test.render();
        } else if ('06' == type) {//通知
            $(".type").html("<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>");
            $(".typeText").html('<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType"></div>');
            bt.dicts.render();
        } else if (type == '10') {//票据专户
            $("#electricBill").val("1");
            var circulationMode = $("#circulationMode").val();
            if(!circulationMode){
                circulationMode = "0";
            }
            htmlType = "<span>" + bt.lang('table.btBankAccApp.circulationMode') + "</span>";
            $(".type").html(htmlType)
            htmlText = '<div class="bt-biz-dict" data-type="radio" data-group="bankAcc.circulationMode" data-required="true" data-defaultvalue="' + circulationMode + '" data-name="circulationMode" data-biz-name="circulationMode"></div>';
            $(".typeText").html(htmlText);
            bt.dicts.render();
        } else if (type == '09') {//虚拟户
            var htmlType = "<span>" + bt.lang('table.btBankAccApp.entityAcc') + "</span>";
            $(".type").html(htmlType);
            var corpId = $("#corpId").val();
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
            chekboxObj.attr('data-abledvalue',curTemp);
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
        initCurrencyExpandButton(data)
    }

    /** 预留印鉴上传begin*/
    layui.upload({
        elem:'#seal1',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger1").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory1").attr("value",res[0].id);
            }
        }
    });
    layui.upload({
        elem:'#seal2',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger2").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory2").attr("value",res[0].id);
            }
        }
    });
    layui.upload({
        elem:'#seal3',
        url: basePath+'/attachment/sysattachment/netBankUpload'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger3").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#sealDirectory3").attr("value",res[0].id);
            }
        }
    });
    $('#imger1,#imger2,#imger3').on('click',function(event){
        showImg($(this));
    });
    /** 预留印鉴上传end*/

    /** 账户限额 begin*/
    form.on('switch(limit)', function(data){
        if(this.checked === true){
            $(".limit").show();
        }else{
            $(".limit").hide();
        }
    });
    /** 账户限额 end*/

    ukeyFunc = function(data,text,selectedData,beforeValue){
        var array = dicts.get('uKeyFunction').val()
        var flag = false;
        for(var i =0;i<array.length;i++){
            if(array[i]==='8'){
                flag = true;
            }
        }
        if(flag){
            $("input[name='uKeyFunctionOther']").show();
        }else{
            $("input[name='uKeyFunctionOther']").hide();
        }
    }

    /**
     * rpa功能
     * @param data
     * @param text
     * @param selectedData
     * @param beforeValue
     */
    rpaFunctionCallback = function(data,text,selectedData,beforeValue){
        var array = dicts.get('rpaFunction').val()
        var flag = false;
        for(var i =0;i<array.length;i++){
            if(array[i]==='1'){
                flag = true;
            }
        }
        if(flag){
            $("#isRpaEscrowTr").show();
        }else{
            $("#isRpaEscrowTr").hide();
        }
    }

    bt.isOnlineHandleCallback = function (data) {
        showVirtualTag();
    }
    function showVirtualTag() {
        var isOnlineHandle = dicts.get('isOnlineHandle').val();
        var accType = dicts.get('accType').val();
        var bifCode = selectType.get('bifCode').val();
        //线上办理 招行 虚拟胡
        if (isOnlineHandle == "1" && bifCode=="402880385c20008b015c2025c382001d" && accType == "09") {
            $("#virtualTag").removeAttr("class", "ignore-required-hide");
            $("#virtualTag").show();
        } else {
            $("#virtualTag").hide();
            $("#virtualTag").attr("class", "ignore-required-hide");
        }
    }
    var dicts = bt.dicts.init();

    ukeyFunc();
});

function divclick() {
    if($("#otherInfo").is(":hidden")){
        $("#otherInfo").show();    //如果元素为隐藏,则将它显现
    }else{
        $("#otherInfo").hide();     //如果元素为显现,则将其隐藏
    }
}

function resetCallback() {
    window.location.reload();
}

function showImg(imgData) {
    let imgSrc = imgData.attr("src");
    getImageWidth(imgSrc,function(width,height){
        let winHeight = $(window).height() - 80;  // 浏览器可视部分高度
        let winWidth = $(window).width() - 100;  // 浏览器可视部分宽度
        // 如果图片高度或者宽度大于限定的高度或者宽度则进行等比例尺寸压缩
        if (height > winHeight || width > winWidth) {
            // 1.原图片宽高比例 大于等于 图片框宽高比例
            if ( winWidth / winHeight < width / height) {
                height = winWidth * (height / width);
                width = winWidth;   //以框的宽度为标准
            }else{
                width = winHeight * (width / height);
                height = winHeight;   //以框的高度为标准
            }
        }
        var imgHtml = "<img src='" + imgSrc + "' width='" + width + "px' height='" + height + "px' />";
        layer.open({
            type: 1
            ,title: false //不显示标题栏
            ,offset: 'auto'
            ,closeBtn: true
            ,area: [width + 'px',height + 'px']
            ,content: imgHtml,
        });
    });
}

function getImageWidth(url,callback){
    var img = new Image();
    img.src = url;
    // 如果图片被缓存，则直接返回缓存数据
    if(img.complete){
        callback(img.width, img.height);
    }else{
        // 完全加载完毕的事件
        img.onload = function(){
            callback(img.width, img.height);
        }
    }
}

/**判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}


function validateAccUseCode(element) {
    var $element = $(element);

    // 获取隐藏的选中值字段（class 为 txtSel 的 hidden input）
    var $hiddenInput = $element.find('input[type="hidden"].txtSel[name="accUseCode"]');
    var selectedValue = $hiddenInput.val();

    // 获取显示的输入框（通常在隐藏域下方）
    var $textInput = $element.find('input[type="text"]:not(.layui-input-disabled)');

    console.log('选中 Code:', selectedValue);
    console.log('输入值:', $textInput.val());

    // 如果隐藏域的 value 为空，清空下方的 input
    if (!selectedValue || selectedValue === '') {
        $textInput.val('');
        console.log('已清空输入框');
    }
}