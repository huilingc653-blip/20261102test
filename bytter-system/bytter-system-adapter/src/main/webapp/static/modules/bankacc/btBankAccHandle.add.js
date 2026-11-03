bt.use(['form', 'upload','layedit', 'btdate', 'corp','mulselect', 'select', 'fcAccGroup','checkboxs', 'dateForm', 'bankInfo', 'rateType', 'moneyType', 'bank', 'dicts', 'dateType', 'btUpload', 'ratioType', 'laydate','swiftInfo','selectType','checkboxs', 'bttable'], function () {
    var $ = bt.jquery, form = bt.form();
    var laydate = layui.laydate;
    var fcAccGroup = bt.fcAccGroup.init();
    var mulselectType=bt.mulselect.init();
    //生成列表
    var rowMR = 0;
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
    //绑定账号
    var bindingBankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    bt.corpCallback = function (id) {
        $.ajax({
            url: basePath + "/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    if (data.obj.userAccountCode) {
                        $("#accName").val(data.obj.name);
                    }
                }
                form.render();
            }
        });
        // var bindingBankAccInit = bt.selectTable.init({
        //     elem: "#bindingBankAccTable",
        //     url: basePath + "/bankacc/btbankacc/getBindingBankAcc",
        //     urldata: {corpId:id},
        //     scrollX: true,
        //     scrollY: "200px",
        //     searchInput: "#bindingBankAcc",
        //     paging: true,
        //     columns: bindingBankAccCol,
        //     clickCallback: function (data) {
        //         if (data) {
        //             $('#bindingBankAcc').val(data.bankAcc);
        //             $('#bindingBankAccHid').val(data.id);
        //         } else {
        //             $('input.text').val('');
        //             $('#bindingBankAccHid').val('');
        //         }
        //     }
        // });
    }
    var corp = bt.corp.init();
    var bankInfo = bt.bankInfo.init();

    var rmb_id = ""; //人民币数据的id

    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }
    bt.bank.init({
        elem: '.bank'
    })
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

	//境内境外账号回调
    bt.isDomesticBank = function(data){
        $("#bankCode").val('');
        $("#bankName").val('');
        $("#prov").val('');
        $("#city").val('');
        if (data.value == '1') {//境内
            $(".isSwiftN").show();
            $(".isSwiftY").hide();
            $("#localClearNoTr").hide();
            /*selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/1"});
            selectType.get("bifCode").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/bisbifinit/json/1"});*/
        }else{//境外
            $(".isSwiftN").hide();
            $(".isSwiftY").show();
            $("#localClearNoTr").show();
            /*selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/0"});
            selectType.get("bifCode").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/bisbifinit/json/0"});*/
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

    bt.bankTypeChange = function (data, text, result) {
        //如果是建行
        var accType = dicts.get('accType').val();//判断定期
        if(data){
            $("#bankPrefix").val(data.bankPrefix);
            //设置验证属性
            setVerify(data,accType);
        }else{
            $("#bankPrefix").val(result.bankPrefix);
            //设置验证属性
            setVerify(result,accType);
        }
        if(data) {
            $("#bankCode").val('');
            $("#bankName").val('');
            $("#prov").val('');
            $("#city").val('');
            // if (data.isdomesticbank === undefined || data.isdomesticbank == '1') {//境内
            //     $(".isSwiftN").show();
            //     $(".isSwiftY").hide();
            // } else {//境外
            //     $(".isSwiftN").hide();
            //     $(".isSwiftY").show();
            // }
            if (data.isthirdparty == '1') {
                $("#bindingBankAccTd").show();
                $("#bindingBankAccTextTd").show();
            } else {
                $("#bindingBankAccTd").hide();
                $("#bindingBankAccTextTd").hide();
            }
            selectType.get("bifCode").attr({"data-defaultvalue":"first","data-url":basePath+"/basic/business/bisbifinit/getByBankType/"+data.id});
            form.render('select');

            // 如果银行类型为8500则去掉input框种class为bank得值
            if (data.banktype == '8500') {
                $("#bankAcc").removeClass("bank");
            }else{
                // 如果银行类型不为8500并且input框class不包含bank则加上
                if (!$("#bankAcc").hasClass("bank")) {
                    $("#bankAcc").addClass("bank");
                }
                // $("#bankAcc").val('');
            }

        }
        showVirtualTag();
        form.renderIcon();//渲染必填
    }
    var selectType = bt.selectType.init();
    var test = bt.dateType.init();

    bt.chang = function (data) {
        setBankacc(data.value);
        if (data.value != "07") {
            $("#ab").remove();
            $("#ac").remove();
        }
        if ($("#bankPrefix").val() == "105" && data.value == '02') {
            $("#volumeNum").attr("lay-verify", "required");
            $("#bankbookNum").attr("lay-verify", "required");
        } else {
            $("#volumeNum").removeAttr("lay-verify", "required");
            $("#bankbookNum").removeAttr("lay-verify", "required");
        }
        showVirtualTag();
        form.renderIcon();//渲染必填
        bt.moneyType.init();
    }

    var setBankacc = function (type, init) {
        var agrMoney = 0;
        var regulars = "";
        var noticeType = "";
        var agrBeginDate = "";
        var circulationMode = "";
        var agrBeginDateTemp = "";
        if (init) {
            regulars = $("#regulars").val();
            agrMoney = $("#agrMoney").val();
            noticeType = $("#noticeType").val();
            agrBeginDate = $("#agrBeginDate").val();
            circulationMode = $("#circulationMode").val();
        } else {
            agrBeginDateTemp = $("#agrBeginDateTemp").val();
        }
        var agrContract = $("#agrContract").val();
        var agrInterest = $("#agrInterest").val();
        var agrEndDate = $("#agrEndDateTemp").val();
        var htmlType = '';
        var htmlText = '';
        var chekboxObj = ck.get("cur");

        if (type == "02") {//定期
            htmlType = "<span>" + bt.lang('table.btBankAccApp.regulars') + "</span>";
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars"></div>';

        } else if (type == '07') {//协定
            var tr1 = "<tr id='ab'>";
            var tr2 = "<tr id='ac'>";
            htmlType = "<span>" + bt.lang('table.btBankAcc.agrMoney') + "</span>";
            htmlText = '<input type="text" id="agrMoney" name="agrMoney" lay-verify="required" value="' + agrMoney + '" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btBankAcc.agrMoney') + '">';

            tr1 += "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                '<td><input type="text" data-name="agrInterest" class="layui-input bt-biz-rateType" lay-verify="required" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '"></td></tr>';
            tr2 += '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text"></div></td>' +
                '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text"></div></td>' +
                '</tr>';
            $(".tp").after(tr2).after(tr1);
            rate.render();
            test.render();
        } else if (type == '06') {//通知
            htmlType = "<span>" + bt.lang('table.btBankAccApp.noticeType') + "</span>";
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType"></div>';
        } else if (type == '10') {//票据专户
            $("#electricBill").val("1");
            // chekboxObj.val(rmb_id);
            if (!circulationMode) {
                circulationMode = "0";
            }
            htmlType = "<span>" + bt.lang('table.btBankAccApp.circulationMode') + "</span>";
            htmlText = '<div class="bt-biz-dict" data-type="radio" data-group="bankAcc.circulationMode" data-required="true" data-defaultvalue="' + circulationMode + '" data-name="circulationMode" data-biz-name="circulationMode"></div>';
        } else if (type == '09') {//虚拟户
            htmlType = "<span>" + bt.lang('table.btBankAccApp.entityAcc') + "</span>";
            htmlText = '<div class="entityAccId"></div>';
            $(".type").html(htmlType);
            $(".typeText").html(htmlText);
            var corpId = corp.get('corpId').val();
            var bankCode = $("#bankCode").val();
            bt.select.init({
                elem: ".entityAccId",
                name: "entityAccId",
                defaultVal: '',
                valueField: "id",
                textField: "bankAcc",
                required: true,
                search: true,
                disabled: false,
                url: basePath + "/bankacc/btbankacc/getEntityAcc?corpId=" + corpId + "&bankCode=" + bankCode
            });
        //} else if (type == '04') { //保证金
            //$(".cks").addClass("flag");
            //chekboxObj.attr('data-abledvalue', 'all');
            //chekboxObj.attr('data-disabledvalue', 'all');
        }

        if (type !== "10") {
            $("#electricBill").val("0");
            //if (type !== "04") {
                //除了保证金全选全可用
                if ($(".cks").hasClass("flag")) {
                    chekboxObj.attr('data-abledvalue', 'all');
                }
            //}
        } else {
            //除了保证金全选全可用
            if ($(".cks").hasClass("flag")) {
                chekboxObj.attr('data-abledvalue', 'all');
                chekboxObj.val(rmb_id);
            }
        }
        if (type != "09") {
            $(".type").html(htmlType);
            $(".typeText").html(htmlText);
            bt.dicts.render();
        }
        bt.moneyType.init();
    };

    var rate = bt.rateType.init();
    bt.ratioType.init();
    bt.moneyType.init();
    //时间
    $.each($("div[type='date']"), function (i, item) {
        var $item = $(item);
        bt.btdate.init({
            elem: "[field='" + $item.attr("field") + "']",
            name: $item.attr("field"),
            start: $item.attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false,
            placeholder: new Date().Format("yyyy-MM-dd")
        });
    });
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        var $item = $(item);
        bt.select.init({
            elem: "[field='" + $item.attr("field") + "']",
            name: $item.attr("field"),
            defaultVal: $item.attr("defaultVal"),
            valueField: $item.attr("dictField"),
            textField: $item.attr("dictText"),
            required: $item.attr("data-required"),
            change: $item.attr("data-callback"),
            initCallBack: $item.attr("initCallBack"),
            search: true,
            disabled: false,
            url: $item.attr("fieldhref")
        });
    });

    var ck = bt.checkboxs.initGetCurrent({
        elem: ".cks",
        name: "cur",
        valueField: "id",
        textField: "curName",
        url: basePath + "/basic/business/btcurrency/json",
        checkAll: true,
        initCompleteCallback: getRmb,
        checkedvalue: $("#curId").val(),
        required:true,
        change: function (data) {
            setBalance($("input[name='cur']:checked").length > 1);
            setCur(data);
        }
    });
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
        bt.bankTypeChange(null, null, data);
        if (data.isthirdparty == '1') {
            $("#bindingBankAccTd").show();
            $("#bindingBankAccTextTd").show();
        } else {
            $("#bindingBankAccTd").hide();
            $("#bindingBankAccTextTd").hide();
        }
        form.render('select');
    }

    //获取人民币id
    function getRmbId(data) {
        // if (!data) {
        //     return '';
        // }
        // for (var i = 0; i < data.length; i++) {
        //     if (data[i].id == "4028802e5c346968015c346c18850008") {
        //         return data[i].id;
        //     }
        // }
        //
        // return '';
        return "4028802e5c346968015c346c18850008"
    }

    //币别初始化回调
    function getRmb(data) {
        rmb_id = getRmbId(data);
        initCurrencyExpandButton(data);
    }

    /**
     * 判断是否为空 true：空 false:不为空
     * @param data
     */
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? true : false;
    }
    chooseBindingBankAcc = function(){
        parent.layer.open({
            type: 2,
            area: ['950px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankacchandle/chooseBindingBankAcc/"
        });
    }

    viewBindingBankAcc = function(){
        parent.layer.open({
            type: 2,
            area: ['950px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankacchandle/viewBindingBankAcc/"
        });
    }


    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var isHasCur = false;
        var cur = data.field.cur;
        if (isNull(cur)) {
            layer.alert(bt.lang("nis.netbank.message.curname.null"), {icon: 2});
            bt.loading.hide();
            return false;
        }
        for (var i = 0; i < cur.length; i++) {
            if (cur[i] === rmb_id) {
                isHasCur = true;
            }
        }
        //判断票据专户必须得有人民币
        if (!isHasCur && data.field.accType === "10") {
            layer.alert(bt.lang("table.btBankAcc.electric.rmb"), {icon: 2});
            bt.loading.hide();
            return false;
        }

        if(typeof(data.field.isLimitQuota) == "undefined"){
            data.field.isLimitQuota = 0;
        }else if(data.field.isLimitQuota === "on"){
            data.field.isLimitQuota = 1;
        }else{
            data.field.isLimitQuota = 0;
        }

        var regDate = data.field.regDate;
        var initBalanceDate = data.field.initBalanceDate;
        if (bt.date.compare(bt.date.format(regDate), bt.date.format(initBalanceDate)) > 0) {
            bt.loading.hide();
            layer.alert(bt.lang("btBankAcc.initBalanceDateCheck"));
            return false;
        }
        data.field.curId = cur.join(",");
        data.field.attachmentId = up.getGroupId();
        data.field.status = $(this).attr("value");

        //组装网银功能字符串
        var arrayUK,joinArray;
        arrayUK = data.field.uKeyFunction;
        if(!bt.empty(arrayUK)){
            joinArray = arrayUK.join(",");
            data.field.uKeyFunction = joinArray;
        }
        $.post(basePath + "/bankacc/btbankacchandle/save", data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
    form.on('radio(interestCycle)', function (data) {
        setInterestDataDisplay(data.value);
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
    form.on('radio(isOnline)', function (data) {
        if (data.value == 1) {
            $("input[data-id='initBalance']").val("");
            $('input[name="initBalanceDate"]').val("");
            //$("#initBalance").val("");
            $("input[data-id='initBalance']").attr("disabled", "disabled");
            $('input[name="initBalanceDate"]').attr("disabled", "disabled");

            $('.isLimit').show();
            $('.limit').show();
        } else {
            $("input[data-id='initBalance']").removeAttr("disabled", "disabled");
            $('input[name="initBalanceDate"]').removeAttr("disabled", "disabled");
            //$('input[name="initBalanceDate"]').val($("#todate").val());
            $('.isLimit').hide();
            $('.limit').hide();
        }
    });
    if ($("input[name='isOnline']:checked").val() == 1) {
        $("input[data-id='initBalance']").attr("disabled", "disabled");
        $('input[name="initBalanceDate"]').attr("disabled", "disabled");
    }

    // arg 为boolean值  多币别不能初始化余额
    var setBalance = function (arg) {
        if (arg) {
            $("input[data-id='initBalance']").val("").attr("readonly", "readonly");
            ;
            $('input[name="initBalanceDate"]').val("").attr("readonly", "readonly");
            ;
            $("#initBalance").val("");
        } else {
            $("input[data-id='initBalance']").removeAttr("readonly");
            $('input[name="initBalanceDate"]').removeAttr("readonly");
        }
    }

    var setCur = function (data) {
        var chekboxObj = ck.get("cur");
        var type = dicts.get("accType").val();
        if (data.value === 'all') {
            if ('10' == type && !chekboxObj.val()) {
                chekboxObj.val(rmb_id);
            }
            return;
        }
        if (data.value === 'all') {
            return;
        }
        var isChecked = data.othis.hasClass('layui-form-checked');
        /*if (type && '04' == type) {
            if (isChecked) {
                chekboxObj.attr('data-abledvalue', data.value);
            } else {
                chekboxObj.attr('data-abledvalue', 'all');
                chekboxObj.attr('data-disabledvalue', 'all');
            }
        }*/
        if (data.value === rmb_id && '10' == type) {
            var value = chekboxObj.val();
            if (value) {
                value = value + ',' + rmb_id;
            } else {
                value = rmb_id
            }
            chekboxObj.val(value);
        }
    }
    // 设置计息日的显示与隐藏
    setInterestDataDisplay = function (type) {
        if (type == 1) {
            $("#interestDateDiv").show();
        } else {
            $("#interestDateDiv").hide();
        }
    };
    setInterestDataDisplay(0);

    /**账户开户日期与年检日期控制begin*/
    //渲染账户年检日期
    var checkExpireDate = laydate.render({
            elem: '#checkExpireDate',//选择器
            type: 'date',//日期类型
            min: "1970-1-1",//默认最小值
            trigger:'click',
            btns: ['clear'],
            done: function (value, date) {//点击确认后回调
                //年检时间选择后，重新控制开户日期
                regDate.config.max = {
                    year: date.year,
                    month: date.month - 1,
                    date: date.date,
                    hours: 0,
                    minutes: 0,
                    seconds: 0
                }
            }
        });
    //渲染开户日期
    var regDate = laydate.render({
        elem: '#regDate',
        type: 'date',
        max: "2099-12-31",//默认最大值
        trigger:'click',
        btns: ['clear'],
        done: function (value, date) {//点击确认后回调
            //移除年检日期禁用样式
            $("#checkExpireDate").removeAttr("disabled").removeClass("layui-disabled");
            //重新控制年检日期范围
            checkExpireDate.config.min = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: 0,
                minutes: 0,
                seconds: 0
            };
        }
    });
    /** 账户开户日期与年检日期控制end*/

    /** 账户限额 begin*/
    form.on('switch(limit)', function(data){
        if(this.checked === true){
            $(".limit").show();
        }else{
            $(".limit").hide();
        }
    });
    /** 账户限额 end*/

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

    ukeyFunc = function(data,text,selectedData,beforeValue){
        var array = dicts.get('uKeyFunction').val()
        var flag = false;
        for(var i =0;i<array.length;i++){
            if(array[i]==='9'){
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
            $("#virtualTag").attr("class", "ignore-required-hide");
            $("#virtualTag").hide();
        }
    }
    var dicts = bt.dicts.init();
})

function divclick() {
    if ($("#otherInfo").is(":hidden")) {
        $("#otherInfo").show();    //如果元素为隐藏,则将它显现
        $("#otherInfoIcon").html("&#xe61a;")
    } else {
        $("#otherInfo").hide();     //如果元素为显现,则将其隐藏
        $("#otherInfoIcon").html("&#xe602;")
    }
}


// 密码显示
$('.pwBox').on('mousedown','.eyeBtn',function(){
    $(this).siblings("input").attr("type","text")
    $(this).find(".eye_show").show()
    $(this).find(".eye").hide()
})
$('.pwBox').on('mouseup','.eyeBtn',function(){
    $(this).siblings("input").attr("type","password")
    $(this).find(".eye_show").hide()
    $(this).find(".eye").show()
})




function resetCallback() {
    window.location.reload();
}

function showImg(imgData) {
    var imgSrc = imgData.attr("src");
    getImageWidth(imgSrc,function(width,height){
        var winHeight = $(window).height() - 80;  // 浏览器可视部分高度
        var winWidth = $(window).width() - 100;  // 浏览器可视部分宽度
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

function setBindingBankAcc(id) {
    if(id) {
        $("#bindingBankAccHid").val(id);
    }
    parent.layer.closeAll();
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