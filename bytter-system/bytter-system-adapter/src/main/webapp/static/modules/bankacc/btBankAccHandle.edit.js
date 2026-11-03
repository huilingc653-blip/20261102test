bt.use(['form','upload','layedit','btdate','mulselect','corp','select','checkboxs','dateForm', 'fcAccGroup','bankInfo','rateType','moneyType','bank','dicts','btUpload','dateType','ratioType','swiftInfo','selectType', 'bttable'], function(){
    var $ = bt.jquery, form = bt.form()
        , laydate = bt.laydate;
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
    //绑定账号
    var bindingBankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var corpId = $('#corpId').val();
    // var bindingBankAccInit = bt.selectTable.init({
    //     elem: "#bindingBankAccTable",
    //     url: basePath + "/bankacc/btbankacc/getBindingBankAcc",
    //     urldata: {corpId:corpId},
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

    // var corp = bt.corp.init();
    var bankInfo = bt.bankInfo.init();
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }
    var test = bt.dateType.init();
    var selectType = bt.selectType.init();

    var rmb_id = ""; //人民币数据的id

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
    bt.bankTypeChange = function(data,text,result){
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
            // if (data.isdomesticbank == '1') {//境内
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
        }
        showVirtualTag();
        form.renderIcon();//渲染必填
    }

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


    bt.bankTypeInit = function (elem,result) {
        var accType = dicts.get('accType').val();//判断定期
        for(var i = 0;i<result.length;i++){
            if($("#bankTypeIdHid").val() == result[i].id){
                if(result[i].bankPrefix == '105'){
                    $("#bankPrefix").val(result[i].bankPrefix);
                }
                //设置验证属性
                setVerify(result[i],accType);
            }
        }
        form.renderIcon();//渲染必填
    }

    //swiftcode选择
    bt.swiftInfoCallback = function (data, dom) {
        $("#bankCode").val(data.swiftCode);
        $("#bankName").val(data.bankName);
        $("#prov").val(data.countryCode);
        $("#city").val(data.cityName);
    };

    bt.swiftInfo.init();

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    bt.chang = function(data){
        setBankacc(data.value);
        if(data.value != "07"){
            $("#ab").remove();
            $("#ac").remove();
        }

        if($("#bankPrefix").val() == "105" && data.value == '02'){
            $("#volumeNum").attr("lay-verify","required");
            $("#bankbookNum").attr("lay-verify","required");
        }else{
            $("#volumeNum").removeAttr('lay-verify');
            $("#bankbookNum").removeAttr("lay-verify");
        }
        showVirtualTag();
        form.renderIcon();//渲染必填
    };


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


    var rate = bt.rateType.init();
    bt.ratioType.init();
    bt.moneyType.init();
    bt.bank.init({
        elem:'.bank'
    })
    //时间
    $.each($("div[type='date']"),function(i,item){
        var $item = $(item);
        bt.btdate.init({
            elem: "[field='"+$item.attr("field")+"']",
            name: $item.attr("field"),
            start: $item.attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false,
            defaultVal: new Date().Format("yyyy-MM-dd")
        });
    });
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        var $item = $(item);
        var ifForm = $("#isFrom").val();
        if(ifForm == 1 || $item.attr("field") == 'attributeId') {
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
        }else{
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
                disabled: true,
                url: $item.attr("fieldhref")
            });
        }
    });
    // arg 为boolean值  多币别不能初始化余额
    var setBalance = function (arg) {
        if( arg){
            $("input[data-id='initBalance']").val("").attr("readonly","readonly");;
            $('input[name="initBalanceDate"]').val("").attr("readonly","readonly");;
            $("#initBalance").val("");
        }else{
            $("input[data-id='initBalance']").removeAttr("readonly");
            $('input[name="initBalanceDate"]').removeAttr("readonly");
        }
    }
    //选择保证金币别只能选择一个
    /*var setCur = function (data) {
        var isChecked = data.othis.hasClass('layui-form-checked');
        var type = dicts.get("accType").val();
        var chekboxObj = ck.get("cur");
        if(type && '04' == type){
            if(isChecked){
                chekboxObj.attr('data-abledvalue',data.value);
            }
        }
    }*/
    if($(".cks")) {
        var ck = bt.checkboxs.initGetCurrent({
            elem: ".cks",
            name: "cur",
            valueField: "id",
            textField: "curName",
            url: basePath + "/basic/business/btcurrency/json",
            initCompleteCallback: getType,
            required:true,
            checkAll: true,
            checkedValue: $("#curId").val(),
            fixedCheck: $("#curId").val(),
            change: function (data) {
                //多币别不能初始化余额
                setBalance($("input[name='cur']:checked").length > 1);
                //setCur(data);
            }
        });
    }

    bt.bankinfoCallback = function(data,dom){
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
        bt.bankTypeChange(null,null,data);
        if (data.isthirdparty == '1') {
            $("#bindingBankAccTd").show();
            $("#bindingBankAccTextTd").show();
        } else {
            $("#bindingBankAccTd").hide();
            $("#bindingBankAccTextTd").hide();
        }
        form.render('select');
    }

    if($("input[name='ratesFloat']:checked").val() == 1){
        $("input[data-id='ratesFloatPer']").removeAttr("disabled");
    }
    if($("input[name='interestCycle']:checked").val() == 0){
        $("#interestDateDiv").hide();
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
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var curIdStr = "";
        if(data.field.isFrom === "1"){
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
            curIdStr = cur.join(",");
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
        if(bt.date.compare(bt.date.format(regDate),bt.date.format(initBalanceDate)) > 0){
            bt.loading.hide();
            layer.alert(bt.lang("btBankAcc.initBalanceDateCheck"));
            return false;
        }

        if(curIdStr === ""){
            curIdStr = $("#curId").val();
        }
        data.field.curId=curIdStr;
        data.field.attachmentId = up.getGroupId();

        //组装网银功能字符串
        var arrayUK,joinArray;
        arrayUK = data.field.uKeyFunction;
        if(!bt.empty(arrayUK)){
            joinArray = arrayUK.join(",");
            data.field.uKeyFunction = joinArray;
        }

        $.post(basePath + "/bankacc/btbankacchandle/modify",data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    form.on('radio(interestCycle)', function(data){
    	  setInterestDataDisplay(data.value);
    });
    form.on('radio(ratesFloat)', function (data) {
        setRatesFloat(data.value);
    });
    form.on('radio(isOnline)', function (data) {
        //多币别不能初始化余额
        setBalance( data.value == 1);
        var that = this;
        if(data.value == 1){
            $("input[data-id='initBalance']").val("");
            $('input[name="initBalanceDate"]').val("");
            //$("#initBalance").val("");
            $("input[data-id='initBalance']").attr("disabled", "disabled");
            $('input[name="initBalanceDate"]').attr("disabled","disabled");

            $('.isLimit').show();
            $('.limit').show();
        }else{
            $("input[data-id='initBalance']").removeAttr("disabled", "disabled");
            $('input[name="initBalanceDate"]').removeAttr("disabled","disabled");
            //$('input[name="initBalanceDate"]').val($("#todate").val());

            $('.isLimit').hide();
            $('.limit').hide();
        }
        //如果存在父子关系的账号不能修改在线标记
        var id = $("#id").val();
        $.ajax({
            url: basePath + "/bankacc/btbankacchandle/isHasPrantAndChild",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (t) {
                if(t.success){
                    if(data.value === "0"){
                        $(that).prop('checked', false);
                        $(that).parent().find('input[value="1"]').prop('checked', true);
                        layer.msg(bt.lang('bt.bankacc.message.hasPrantAndChild'));
                        $("input[data-id='initBalance']").val("");
                        $('input[name="initBalanceDate"]').val("");
                        $("#initBalance").val("");
                        $("input[data-id='initBalance']").attr("disabled", "disabled");
                        $('input[name="initBalanceDate"]').attr("disabled","disabled");
                        form.render('radio');
                    }

                }
            }
        });
    });
    form.on('radio(validSign)', function (data) {
        var that = this;
        //如果存在父子关系的账号不能修改在线标记
        var id = $("#id").val();
        $.ajax({
            url: basePath + "/bankacc/btbankacchandle/isHasPrantAndChild",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (t) {
                if(t.success){
                    if(data.value === "0"){
                        $(that).prop('checked', false);
                        $(that).parent().find('input[value="1"]').prop('checked', true);
                        layer.msg(bt.lang('bt.bankacc.message.hasPrantAndChild'));
                        form.render('radio');
                    }

                }
            }
        });
    });

    if($("input[name='isOnline']:checked").val() == 1){
        $("input[data-id='initBalance']").attr("disabled", "disabled");
        $('input[name="initBalanceDate"]').attr("disabled","disabled");
    }
    var setBankacc = function (type,init) {
        var regulars,agrMoney,noticeType,agrBeginDate,agrEndDate,circulationMode,curTemp;
        var isFrom = $("#isFrom").val();
        var agrMoney = 0;
        var regulars = $("#regulars").val();
        var noticeType = "";
        var agrBeginDate = "";
        var circulationMode = "";
        if(init){
            //regulars = $("#regulars").val();
            agrMoney = $("#agrMoney").val();
            noticeType = $("#noticeType").val();
            agrBeginDate = $("#agrBeginDate").val();
            agrEndDate = $("#agrEndDate").val();
            circulationMode = $("#circulationMode").val();
        }
        curTemp = $("#curId").val();
        if (agrBeginDate == undefined){
            agrBeginDate = '';
        }
        if (agrEndDate == undefined){
            agrEndDate = '';
        }
        var agrContract = $("#agrContract").val();
        var agrInterest = $("#agrInterest").val();
        var htmlType = '';
        var htmlText = '';

        if(isFrom == 1) {
            var chekboxObj = ck.get("cur");
        }

        if( type == "02" ){//定期
            htmlType = "<span>"+bt.lang('table.btBankAccApp.regulars')+"</span>";
            /*if(isFrom == 1) {
                htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars"></div>';
            }else{
                htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars" data-disabled="true"></div>';
            }*/
            htmlText = '<div class="bt-biz-dict" data-type="select" data-group="btBankAcc.Term" data-defaultvalue="' + regulars + '"  data-required="true" data-name="regulars" data-biz-name="regulars"></div>';

        }else if(type == '07'){//协定
            var tr1 = "<tr id='ab'>";
            var tr2 = "<tr id='ac'>";
            htmlType = "<span>"+bt.lang('table.btBankAcc.agrMoney')+"</span>";
            if(isFrom == 1) {
                htmlText = '<input type="text" id="agrMoney2" name="agrMoney" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btBankAcc.agrMoney') + '" value="' + agrMoney + '" >';
                tr1 += "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                    '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '" value="' + agrContract + '"></div></td>' +
                    '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                    '<td><div class="layui-input-inline"><input type="text" id="agrInterest2" name="agrInterest" lay-verify="required" autocomplete="off" class="layui-input bt-biz-rateType" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '" value="' + agrInterest + '"></div></td>';
                tr2 += '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                    '<td><div class="layui-inline"><input name="agrBeginDate" class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text" value="' + agrBeginDate + '"></div></td>' +
                    '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                    '<td><div class="layui-inline"><input name="agrEndDate" class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text" value="' + agrEndDate + '"></div></td>' +
                    '</tr>';
            }else{
                htmlText = '<input readonly="readonly" type="text" id="agrMoney2" name="agrMoney" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" placeholder="' + bt.lang('table.btBankAcc.agrMoney') + '" value="' + agrMoney + '" >';
                tr1 += "<td class='textRight'>" + bt.lang('table.btBankAcc.agrContract') + "</td>" +
                    '<td><div class="layui-input-inline"><input type="text" id="agrContract" name="agrContract" readonly="readonly" lay-verify="required" autocomplete="off" class="layui-input" placeholder="' + bt.lang('table.btBankAcc.agrContract') + '" value="' + agrContract + '"></div></td>' +
                    '<td class="textRight">' + bt.lang('table.btBankAcc.agrInterest') + '</td>' +
                    '<td><div class="layui-input-inline"><input readonly="readonly" type="text" id="agrInterest2" name="agrInterest" lay-verify="required" autocomplete="off" class="layui-input bt-biz-rateType" placeholder="' + bt.lang('table.btBankAcc.agrInterest') + '" value="' + agrInterest + '"></div></td>';
                tr2 += '<td class="textRight">' + bt.lang('table.btBankAcc.agrBeginDate') + '</td>' +
                    '<td><div class="layui-inline"><input name="agrBeginDate" readonly  class="layui-input bt-biz-dateType" lay-verify="required" data-endid="agrEndDate" data-type="date" type="text" value="' + agrBeginDate + '"></div></td>' +
                    '<td class="textRight">' + bt.lang('table.btBankAcc.agrEndDate') + '</td>' +
                    '<td><div class="layui-inline"><input name="agrEndDate" readonly class="layui-input bt-biz-dateType" lay-verify="required" id="agrEndDate" data-type="date" type="text" value="' + agrEndDate + '"></div></td>' +
                    '</tr>';
            }
            $(".tp").after(tr2).after(tr1);
            rate.render();
            test.render();
        }else if( type  == '06'){//通知
            htmlType = "<span>"+bt.lang('table.btBankAccApp.noticeType')+"</span>";
            if(isFrom == 1) {
                htmlText = '<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType"></div>';
            }else{
                htmlText = '<div class="bt-biz-dict" data-type="select" data-group="bankAcc.noticeType" data-required="true" data-defaultvalue="' + noticeType + '" data-name="noticeType" data-biz-name="noticeType" data-disabled="true"></div>';
            }
        } else if (type == '10') {//票据专户
            $("#electricBill").val("1");
            if(isFrom == 1) {
                if($(".cks").length > 0 ) {
                    // chekboxObj.attr('data-abledvalue','all');
                    // chekboxObj.val(curTemp);
                }
            }
            if(!circulationMode){
                circulationMode = "0";
            }
            htmlType = "<span>"+bt.lang('table.btBankAccApp.circulationMode')+"</span>";
            htmlText = '<div class="bt-biz-dict" data-type="radio" data-group="bankAcc.circulationMode" data-required="true" data-defaultvalue="'+circulationMode+'" data-name="circulationMode" data-biz-name="circulationMode"></div>';
        } else if (type == '09') {//虚拟户
            htmlType = "<span>" + bt.lang('table.btBankAccApp.entityAcc') + "</span>";
            htmlText = '<div class="entityAccId"></div>';
            $(".type").html(htmlType);
            $(".typeText").html(htmlText);
            var corpId = $('#corpId').val();
            var bankCode = $("#bankCode").val();
            if(isFrom == 1) {
                bt.select.init({
                    elem: ".entityAccId",
                    name: "entityAccId",
                    defaultVal: $("#entityAccId").val(),
                    valueField: "id",
                    textField: "bankAcc",
                    required: true,
                    search: true,
                    disabled: false,
                    url: basePath + "/bankacc/btbankacc/getEntityAcc?corpId=" + corpId + "&bankCode=" + bankCode
                });
            }else{
                bt.select.init({
                    elem: ".entityAccId",
                    name: "entityAccId",
                    defaultVal: $("#entityAccId").val(),
                    valueField: "id",
                    textField: "bankAcc",
                    required: true,
                    search: true,
                    disabled: true,
                    url: basePath + "/bankacc/btbankacc/getEntityAcc?corpId=" + corpId + "&bankCode=" + bankCode
                });
            }
        /*}else if (type == '04') { //保证金
            if(isFrom == 1) {
                $(".cks").addClass("flag");
                chekboxObj.attr('data-abledvalue','all');
                chekboxObj.attr('data-disabledvalue','all');
                chekboxObj.attr('data-abledvalue',curTemp);
            }*/
        }

        if(isFrom == 1) {
            if(type !== "10"){
                //if(type !== "04") {
                    //除了保证金全选全可用
                    if ($(".cks").hasClass("flag")) {
                        chekboxObj.attr('data-abledvalue', 'all');
                        chekboxObj.val(curTemp);
                    }
                //}
            }else{
                //币别只能选择人民币
                if ($(".cks").hasClass("flag")) {
                    chekboxObj.attr('data-abledvalue', 'all');
                    // chekboxObj.val(curTemp);
                }
            }
        }else{
            if(type !== "10"){
                $("#electricBill").val("0");
            }
        }
        if(type != "09"){
            $(".type").html(htmlType);
            $(".typeText").html(htmlText);
            bt.dicts.render();
        }
        bt.moneyType.init();
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
        return "4028802e5c346968015c346c18850008";
    }

    //根据账户类型得到相应的信息
    function getType(data) {
        rmb_id = getRmbId(data);
        var type = dicts.get('accType').val();
        setBankacc(type,'init');
        if($("#bankPrefix").val() == "105" && type == "02"){
            $("#volumeNum").attr("lay-verify","required");
            $("#bankbookNum").attr("lay-verify","required");
        }else{
            $("#volumeNum").removeAttr("lay-verify");
            $("#bankbookNum").removeAttr("lay-verify");
        }
        setInterestDataDisplay($("input[name='interestCycle']:checked").val());
        setRatesFloat($("input[name='ratesFloat']:checked").val());
        initCurrencyExpandButton(data);
        form.renderIcon();//渲染必填
    }

    setInterestDataDisplay = function (type) {
        if(type == 1){
            $("#interestDateDiv").show();
        }else{
            $("#interestDateDiv").hide();
        }
    };

    //设置浮动百分比
    setRatesFloat = function (type) {
        if (type == 1) {
            $("input[data-id='ratesFloatPer']").attr("lay-verify", "required").removeAttr("disabled");
        } else {
            $("input[data-id='ratesFloatPer']").val("").attr("disabled", "disabled").removeAttr("lay-verify");
        }
        form.renderIcon();//渲染必填
    }

    /**账户开户日期与年检日期控制begin*/
        //渲染账户年检日期
    var checkExpireDate;
    if($("#regDate").val()!=null&&$("#regDate").val()!=""){
        //移除年检日期禁用样式
        $("#checkExpireDate").removeAttr("disabled").removeClass("layui-disabled");
        checkExpireDate = laydate.render({
            elem: '#checkExpireDate',//选择器
            type: 'date',//日期类型
            min:$("#regDate").val(),//默认最小值
            btns: ['clear'],
            done: function(value,date){//点击确认后回调
                //年检时间选择后，重新控制开户日期
                regDate.config.max={
                    year:date.year,
                    month:date.month-1,
                    date: date.date,
                    hours: 0,
                    minutes: 0,
                    seconds : 0
                }
            }
        });
    }else {
        checkExpireDate= laydate.render({
            elem: '#checkExpireDate',//选择器
            type: 'date',//日期类型
            min:"1970-1-1",//默认最小值
            btns: ['clear'],
            done: function(value,date){//点击确认后回调
                //年检时间选择后，重新控制开户日期
                regDate.config.max={
                    year:date.year,
                    month:date.month-1,
                    date: date.date,
                    hours: 0,
                    minutes: 0,
                    seconds : 0
                }
            }
        });
    }

    //渲染开户日期
    var regDate;
    if($("#checkExpireDate").val()!=null&&$("#checkExpireDate").val()!=""){
        regDate= laydate.render({
            elem: '#regDate',
            type: 'date',
            max:$("#checkExpireDate").val(),//默认最大值
            btns: ['clear'],
            done: function(value, date){//点击确认后回调
                //移除年检日期禁用样式
                $("#checkExpireDate").removeAttr("disabled").removeClass("layui-disabled");
                //重新控制年检日期范围
                checkExpireDate.config.min ={
                    year:date.year,
                    month:date.month-1,
                    date: date.date,
                    hours: 0,
                    minutes: 0,
                    seconds : 0
                };
            }
        });
    }else{
        regDate= laydate.render({
            elem: '#regDate',
            type: 'date',
            max:"2099-12-31",//默认最大值
            btns: ['clear'],
            done: function(value, date){//点击确认后回调
                //移除年检日期禁用样式
                $("#checkExpireDate").removeAttr("disabled").removeClass("layui-disabled");
                //重新控制年检日期范围
                checkExpireDate.config.min ={
                    year:date.year,
                    month:date.month-1,
                    date: date.date,
                    hours: 0,
                    minutes: 0,
                    seconds : 0
                };
            }
        });
    }
    /** 账户开户日期与年检日期控制end*/

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
        $("#otherInfoIcon").html("&#xe61a;")
    }else{
        $("#otherInfo").hide();     //如果元素为显现,则将其隐藏
        $("#otherInfoIcon").html("&#xe602;")
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