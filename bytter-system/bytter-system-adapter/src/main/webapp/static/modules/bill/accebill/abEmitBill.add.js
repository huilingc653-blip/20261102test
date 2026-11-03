var corp,dateCheck,moneyType,isBudget,isRelateRat,rate,uploader,ratio,form,parameters,isBail,bondAccInit,availMoney,mouthRat,isBear,maxBillMoney,dicts,isOpenCpRat,isBatchBill;
var isPayFlg,payerAccNum;
var cmbBailAccountInfo;
var payerAccCallBackData = {};
bt.use(['form','radioType','layedit','btdate','corp','dicts','select','moneyType','selectType','capNumber','jquery','element','dateType','externalCorpBankAcc','bankInfo','rateType'
    ,'accountTable','bank','btUpload','dicts','ratioType','bondAccInfo','cmbBnkbilneg','cmbCrlcon','cmbBailAccountInfo'], function(){
    var $ = bt.jquery,date = new Date(),month;
    isPayFlg = $("#isPayFlg").val();//是否是付款办理的开票
    if( isPayFlg == "1"){
        if(!$("#voucherId").val()){
            var paramsMap = bt.navtab.getCurrentDom().transmitParams();
            $("#payer").val(paramsMap.get("payer"));
            $("#payeeAccName").val(paramsMap.get("payeeAccName"));
            $("#payeeAccName").val(paramsMap.get("payeeAccName"));
            $("#payeeBankName").val(paramsMap.get("payeeBankName"));
            $("#payerBankName").val(paramsMap.get("payerBankName"));
            $("#payerbankno").val(paramsMap.get("payerbankno"));
            $("#payeracc").val(paramsMap.get("payeracc"));
            payerAccNum = paramsMap.get("payeracc");
            payerAccCallBackData.accName = paramsMap.get("payer");
            payerAccCallBackData.bankAcc = paramsMap.get("payeracc");
            payerAccCallBackData.bankName = paramsMap.get("payerBankName");
            payerAccCallBackData.bankCode = paramsMap.get("payerbankno");
            payerAccCallBackData.bifCode = $("#bifCode").val();
            payerAccCallBackData.circulationMode = $("#circulationMode").val();
        }else {
            payerAccNum = $("#payeracc").val();
            payerAccCallBackData.accName = $("#payer").val();
            payerAccCallBackData.bankAcc = $("#payeracc").val();
            payerAccCallBackData.bankName = $("#payerBankName").val();
            payerAccCallBackData.bankCode = $("#payerbankno").val();
            payerAccCallBackData.bifCode = $("#bifCode").val();
            payerAccCallBackData.circulationMode = $("#circulationMode").val();
        }
    }
    form = bt.form();
    isBatchBill=$('#isBatchBill').val();
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }
    dicts=bt.dicts.init();
    //挂授信标识
    isRelateRat=$("#isRelateRat").val()=="true"?true:false;
    //挂内部授信标识
    isOpenCpRat=$("#isOpenCpRat").val()=="true"?true:false;
    //是否挂担保
    isBear=$("#isGuarantee").val();
    //挂保证金标识
    isBail=$("#isBail").val()=="true"?true:false;//授信敞口标识
    mouthRat=$("#mouthRat").val();
    if( isPayFlg == "1"){
        //保证金账号的初始化
        bondAccInit = bt.bondAccInfo.init({
            multiLayer:true
        });
    }else {
        bondAccInit = bt.bondAccInfo.init();
    }
    //保证金参数
    parameters={};
    //纸票最大金额
    maxBillMoney = Number($("#maxBillMoney").val());
    corp=bt.corp.init();
    dateCheck = bt.dateType.init();
    moneyType = bt.moneyType.init();
    //bt.externalCorpBankAcc.init();
    if( isPayFlg == "1"){
        bt.bankInfo.init({
            multiLayer:true,
            multiIframes:true
        });
    }else {
        bt.bankInfo.init();
    }

    rate = bt.rateType.init();
    ratio = bt.ratioType.init();
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    bt.bank.init({
        elem:'.bank'
    });
    uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    form.on('checkbox(isCommon)', function (data) {
        var checked = data.elem.checked;
        if(checked){
            $("#isCommonDiv").show();
        }else{
            $("#isCommonDiv").hide();
        }
        form.render("checkbox");
    });
    var externalCorpBankAcc = "";
    if( isPayFlg == "1"){
        externalCorpBankAcc = bt.externalCorpBankAcc.init({
            multiLayer:true,
        });
    }else {
        externalCorpBankAcc = bt.externalCorpBankAcc.init();
    }

    //收票人客商
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查询
        externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0","type":"4"});
        return true;
    }


    var cmbBnkbilneg;
    if( isPayFlg == "1"){
        cmbBnkbilneg = bt.cmbBnkbilneg.init({
            multiLayer:true,
            multiIframes:true
        });
    }else {
        cmbBnkbilneg = bt.cmbBnkbilneg.init();
    }
    //合作银行号
    bt.cmbBnkbilnegParams = function () {
        cmbBnkbilneg.set({"brnnbr": $("#accptncagrmtBranchNo").val()});
        return true;
    }
    var cmbCrlcon;
    if( isPayFlg == "1"){
        cmbCrlcon = bt.cmbCrlcon.init({
            multiLayer:true,
            multiIframes:true
        });
    }else {
        cmbCrlcon = bt.cmbCrlcon.init();
    }
    //风险合同号
    bt.cmbCrlconParams = function () {
        cmbCrlcon.set({"creditlineid": $("#cvmLimitNo").val()});
        return true;
    }

    if( isPayFlg == "1"){
        cmbBailAccountInfo = bt.cmbBailAccountInfo.init({
            multiLayer:true,
            multiIframes:true
        });
    }else {
        cmbBailAccountInfo = bt.cmbBailAccountInfo.init();
    }


    //承兑人客商
    bt.acceptorCustomerBankAccParams = function () {
        //供应商默认不查询
        externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0","type":"4"});
        return true;
    }
    moneyType.get("test").val({max:1000000000});//页面默认为电票，设置金额为电票最大值
    //电票时出票人只读
//    $("#payer").attr("readonly","readonly");
    //默认对外开出,出票人选择成员单位
    $("#payeeCorpId").hide();
    $("#payeeAccNameDiv").show();
    $("#pledgeeNameDiv").show();
    $("#corpPayee").hide();//收票单位

    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            //required:true,
            istime: false
        });
    });
    var payItem = $("#payItem").val();//预算科目
    //监听新增保存
    form.on('submit(save)', function(data){
        submit(data,false);
    });
    //监听新增提交
    form.on('submit(submit)', function(data){
        submit(data,true);
    });
    var submit = function(data,isSubmit){
        if(!checkDateInput()){
            return false;
        }
        bt.loading.show();
        if($("#payeracc").val() && $("#payeracc").val() == $("#payeeacc").val()){
            //出票人账号和收票人账号不能一样！
            bt.loading.hide();
            layer.alert(bt.lang('common.accebill.abEmitBill.AccountErrorMessage'), {icon: 7});
            return false;
        }
        if(!$("#payeracc").val() && $("#payer").val() == $("#payeeAccName").val()){
            //出票人和收票人不能一样！
            bt.loading.hide();
            layer.alert(bt.lang('common.accebill.abEmitBill.personErrorMessage'), {icon: 7});
            return false;
        }

        if(isBudget){
            /**预算校验*/
            if(bt.money.toNumber(bt.moneyType.get("sumMoney").val())!=bt.money.toNumber(data.field.billMoney)){
                //执行总金额与票据金额不一致
                bt.loading.hide();
                layer.alert(bt.lang('common.accebill.fbsBudget.errorMessage'), {icon: 7});
                return false;
            }
        }
        //授信判断
        if(!checkRelateRat()){
            bt.loading.hide();
            return false;
        }
        //保证金判断
        if(!checkBailMoney()){
            bt.loading.hide();
            return false;
        }

        //担保提交判断
        if(!checkBear()){
            bt.loading.hide();
            return false;
        }
        //挂接内部授信信息
        if(!formSubmitCpCheckRat()){
            bt.loading.hide();
            return false;
        }

        //$("#attachmentId").val(uploader.getGroupId());
        if(isSubmit){
            //对黑白名单进行验证
            var reData=externalCorpCheck({"name":data.field.payee,"corpId":data.field.corpId});
            if(reData.reCode=='3'){
                parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
                bt.loading.hide();
            }else if(reData.reCode=='2'){
                parent.layer.confirm(reData.msg,function(){
                    emitSubmit(isSubmit,data);
                });
                bt.loading.hide();
            }else{
                emitSubmit(isSubmit,data);
            }
        }else{
            emitSubmit(isSubmit,data);
        }
        return false;
    }

    var emitSubmit=function(isSubmit,submitDt){
        initAttachmentInfo();
        var action="/bill/accebill/abemitbill/save";
        if(isBatchBill=='1'){
            batchEmitSubmit(isSubmit);
        }else{
            if(isSubmit){
                action="/bill/accebill/abemitbill/submit/add";
            }
            var emitType=dicts.get("emitType").val();
            if($("#isBillPool").val()
                && $('#poolLimitValidMoneyVal').val()!="false"
                && emitType=='10'
                && bt.money.toNumber($("#billMoney").val())>bt.money.toNumber($('#poolLimitValidMoneyVal').val())){
                layer.alert(bt.lang('bill.accebill.batchEmit.moneyGrePoolMeony'), {icon: 7});
                bt.loading.hide();
                return false;
            }
            //证书签名
            var signDataMsg = signData("开票登记提交", 9);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            var subData = $('#inputForm').serialize();
            if($('#inputForm').serialize().indexOf("billFlag=") < 0){
                subData = $('#inputForm').serialize()+"&billFlag="+billFlagVal;
            }
            if($("#isPayFlg").val() == 1 && !$("#voucherId").val()){
                bt.navtab.getCurrentDom().openBillResult(deserialize(subData));
                parent.layer?parent.layer.closeAll():layer.closeAll();
                return true;
            }
            $.post(basePath + action,subData,function(result){
                bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    //if(!bt.navtab){
                        //parent.layer?parent.layer.closeAll():layer.closeAll();
                   // }
                    if($("#isPayFlg").val() == 1 && $("#voucherId").val()){
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    }else {
                        bt.navtab.refreshParentTable();//刷新父页面表格
                        location.reload();//重载当前页面
                    }
                }
            },'JSON');
        }
    }

    //电票且商票时交易合同号必填
//    var aContractNo = function(){
//        if($('input:radio[name="billFlag"]:checked').val()=="11" && $('input:radio[name="billType"]:checked').val()=="11"){
//            $("#aContractNo").attr("lay-verify","strlength|required");
//        }else{
//            $("#aContractNo").attr("lay-verify","strlength");
//        }
//        bt.form().renderIcon();
//    }
    //流通介质
    form.on('radio(billFlag)', function(data){
        //日期检查
        checkDate();
        //金额控制
        checkMoney();

        // 执行回调方法
        billFlagCallback();

        //授信改造
        ratVerify();

    });

    $("#closeBtn").click(function(){
        parent.layer.closeAll();
    })
    //开出方式
    form.on('radio(insideSing)', function(data){
        if($('input:radio[name="insideSing"]:checked').val()=="0"){
            //对外开出,收票人选择客商信息
            $("#corpPayee").hide();//成员单位选择框
            $("#payeeAccDiv").hide();//成员单位账号
            //$("#payeeAccNameDiv").show();//客商输入框
            $("#pledgeeAccDiv").show();//客商账号
            $("#pledgeeNameDiv").show();//客商按钮
            $("#payeeAccName").val("");//收票人
            $("#payeeacc").val("");//收票人账号
            $("#payeeBankName").val("");//开户银行名称
            $("#payeebankno").val("");//开户银行行号
            if(corp.get("corpId").data().length!=0){
                if($("#fbsbudgetprocessCorp").val() == 1){
                    //业务单位
                    $renderBudget(corp.get("busCorpId").data()[0].id,dateCheck.get("businessDate").val());
                }else {
                    $renderBudget(corp.get("corpId").data()[0].id,dateCheck.get("businessDate").val());
                }
            }
//            $("#pledgeeAccSpanDiv").show();//收票人账号必填符号
            $("#payeeSpanDiv").hide();//收票单位必填符号
            $("#payeeAccName").removeAttr("readonly");
            $("#payeeBankBtDiv").show();//银行查询按钮
        }else{//对内自开,收票人选择成员单位
            $("#corpPayee").show();//收票单位
            $("#payeeAccDiv").show();
            //$("#payeeAccNameDiv").hide();
            $("#pledgeeAccDiv").hide();
            $("#pledgeeNameDiv").hide();
            $("#payeeAccName").val("");//收票人
            $("#payeeSpanDiv").show();//收票单位必填符号
            if(corp.get("payeeCorpId").data()){
                $("#payeeBankName").val("");//开户银行名称
                $("#payeebankno").val("");//开户银行行号
                payeeCorpCallback(corp.get("payeeCorpId").data());//根据收票人单位重新加载收票人账号
                //$("#payeeAccName").val(corp.get("payeeCorpId").data()[0].corpName);//收票人
            }
            $("#payeeAccName").attr("readonly","true");
            if(corp.get("payeeCorpId").data()[0]){
                $("#payeeAccName").val(corp.get("payeeCorpId").data()[0].corpName);
            }
            $("#payeeBankBtDiv").hide();//银行查询按钮
        }
        setVerify();
    });
    //票据类型
    form.on('radio(billType)', function(data){
        if($('input:radio[name="billType"]:checked').val()=="10"){
            //银票,承兑人选择银行信息
            $("#acceptorBankButtonDiv").show();//银行选择按钮
            $("#acceptorCustomerButtonDiv").hide();//客商按钮
            $("#acceptorBankButtonAcceptDiv").hide();//商票承兑人银行按钮
            //票据类型为银票时，承兑人默认为出票人开户行
            $("#acceptor").val($("#payerBankName").val());//承兑人
            $("#acceptoracc").val('0');//承兑人账号
            $("#blAcptrUnnIdNo").val('');//承兑人统一信用代码
            $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
            $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
            $("#acceptorbankaddress").val("");//承兑人开户行地址
            //银票授信单位类型把承兑人置灰
            dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
            relateRatOp(true);
            isBailOp(true);//银票显示保证金
            showEndors(false);
        }else{
            //商票,承兑人选择成员单位或客商信息
            $("#acceptorBankButtonDiv").hide();
            $("#acceptorCustomerButtonDiv").show();
            $("#acceptorBankButtonAcceptDiv").show();
            //商票时承兑人默认为出票人
            $("#acceptor").val($("#payer").val());//承兑人
            $("#acceptoracc").val($("#payeracc").val());//承兑人账号
            $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
            $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
            $("#acceptorbankaddress").val("");//承兑人开户行地址
            //授信单位类型都能选
            dicts.get("ratBankCorpType").attr({"data-disabledValue": ""});
            relateRatOp(false);
            if($("#isRelateRat").val()==true){
                setRationBtn();
            }
            isBailOp(false);//商票不显示保证金

            showEndors(true);
        }
        // 执行回调方法
        billFlagCallback();

        zhaohangRequire();
    });

    // form.on('radio(ratmodel)', function(data){
    // 	ratModelCut();
    // });

    //保证金挂接
    form.on('radio(bailmodel)', function(data){
        isBailSelectShow();
    });


    //收票人选择客商回调函数
    bt.payeeCorpCallback = function(data,dom){
        $("#payeeAccName").val(data.name);//客商名称
        $("#payeeacc").val(data.externalAcc);//客商账号
        $("#payeeBankName").val(data.bank);//客商开户银行名称
        $("#payeebankno").val(data.bankSourceCode);//客商开户银行行号
        $("#blRcvpymtpsUnnIdNo").val(data.socCode);//承兑人统一识别代码 bank_code 222333
    }
    //承兑人选择客商回调函数
    bt.acceptorCorpCallback = function(data,dom){
        if(data.bank){
            $("#acceptoracc").val(data.externalAcc);//承兑人账号
            $("#acceptorbankno").val(data.bankSourceCode);//承兑人开户行号
            $("#acceptorBankName").val(data.bank);//承兑人开户行名称
            $("#acceptoracc").attr("name","acceptoracc");
            $("#acceptoracc").attr("type","text");
            $("#acceptoraccCorp").removeAttr("name");
            $("#acceptoraccCorp").attr("type","hidden");
        }else{
            $("#acceptoracc").val("");//承兑人账号
            $("#acceptorbankno").val("");//承兑人开户行号
            $("#acceptorBankName").val("");//承兑人开户行名称
            $("#acceptoracc").removeAttr("name");
            $("#acceptoracc").attr("type","hidden");
            $("#acceptoraccCorp").attr("name","acceptoracc");
            $("#acceptoraccCorp").attr("type","text");
            bt.accountTable.get("acceptoraccCorp").initCorp(data.id,"1");
        }
        $("#blAcptrUnnIdNo").val(data.socCode);//承兑人统一信用代码
        $("#acceptor").val(data.name);//承兑人
        $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
        //$("#acceptorCustomerNameDiv").show();//承兑人，客商输入框
        $("#acceptoraccCorpDiv").hide();//承兑人账号--成员单位
        //$("#acceptoraccCustomerDiv").show();//承兑人账号--客商或银行
        $("#acceptProvince").val(data.accProvince);//承兑人客商所在省
        $("#acceptCity").val(data.accCity);//承兑人客商所在市
        $("#acceptBankTypeId").val(data.bankType);//承兑人客商银行类别id
        if($("#isRelateRat").val()==true){
            setRationBtn();
        }
        //当选择的是客商时，则内部授信单位类型只能选择开票人，置灰承兑人
        if(data.corpId){
            dicts.get("ratBankCorpType").attr({"data-disabledValue": ""});
            $("#acceptorCorpId").val(data.corpId);
        }else{
            dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
            $("#acceptorCorpId").val("");
        }
        //保证金参数设置
        bailParameters();
        //授信设置
        ratVerify();
    }
    //选择银行回调函数
    bt.bailBankInfoCallback = function(data,dom){
        var opType=$(dom).attr("opType");
        if(opType){
            // 收票人
            if(opType=='payeeBankClick'){
                $("#payeeBankName").val(data.bankName);
                $("#payeebankno").val(data.bankCode);
                $("#blRcvpymtpsUnnIdNo").val(data.socCode);//承兑人统一识别代码
                $('input[name="payeeBankName"]').val(data.bankName);
                $('input[name="payeebankno"]').val(data.bankCode);
                $('input[name="blRcvpymtpsUnnIdNo"]').val(data.socCode);//承兑人统一识别代码
            }
            // 承兑人
            if(opType=='acceptorBankClick'){
                $("#acceptor").val(data.bankName);//承兑人
                $("#acceptoracc").val("0");//承兑人账号--银行默认为0
                $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
                $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
                $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
                $("#blAcptrUnnIdNo").val(data.socCode);//承兑人统一识别代码
                if($("#isRelateRat").val()==true){
                    setRationBtn();
                }
                //保证金参数设置
                bailParameters();

                if (data.bankName && data.bankName.includes("招商银行")) {
                    $("#acceptorBankType").val("招商银行");
                }else {
                    $("#acceptorBankType").val(data.bankName);
                }
                zhaohangRequire();

            }
        }
    }
    //选择银行回调函数
    bt.bailBankInfoCallbackAccept = function(data,dom){
        $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
        $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
        $("#acceptProvince").val(data.prov);//承兑人客商所在省
        $("#acceptCity").val(data.city);//承兑人客商所在市
        $("#acceptBankTypeId").val(data.bankTypeId);//承兑人客商银行类别id
        $("#blAcptrUnnIdNo").val(data.socCode);//承兑人统一识别代码
        //保证金参数设置
        bailParameters();
    }

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml=function(corpId,processDate){
        var itemCode = $("#payItem").val();
        var defItemCode = $("#defItemCode").val();
        var curId = $("#curId").val();
        var businessDate = dateCheck.get("businessDate").val();
        if(!processDate){
            processDate = new Date();
            var mon = processDate.getMonth() + 1;
            var day = processDate.getDate();
            processDate = processDate.getFullYear() + "-" + (mon<10?"0"+mon:mon) + "-" +(day<10?"0"+day:day);
        }
        var url=basePath+"/budget/fbsBudgetProcess/getItemDetail";
        var param={
            corpId:corpId,
            itemCode:itemCode,
            defItemCode:defItemCode,
            processDate:processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,success: function (json) {
                $("#fbsbudgetprocessDiv").show();
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                bt.form().render();
            }
        });
    };
    /**
     * 资金计划控件渲染
     */
    $renderBudget = function(corpId,processDate){
        //判断付款方式是否挂资金计划   1、票据模块配置开启挂接资金计划(控制层判断) 2、预算科目维护不为空  3、当前单位是否要挂预算
        $.ajax({
            type: 'POST',
            url: basePath+"/bill/accebill/abvoucherinfoVice/isBudget",
            data:{'corpId':corpId,'briefId':'001'},
            dataType:'json',
            success: function(result){
                if(result.success){
                    if(payItem && result.attributes.isBudget){
                        isBudget = true;
                        $genOtherHtml(corpId,processDate);
                    }else{
                        isBudget = false;
                        $("#tagItemDetail").html("");
                    }
                }else{
                    isBudget = false;
                    $("#tagItemDetail").html("");
                    //parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                }
            }});
    }
    relateRatOp(true);
    setVerify();
    showEndors(false);
    newStmIssuofpprIndChange({value:dicts.get("AB_BILL_NEW_STM_ISSUOFPPR_IND").val()});


    if(isPayFlg&&$("#voucherId").val()){
        var sendId = bt.navtab.getCurrentDom().getVoucherIds();
        if(sendId){
            $.ajax({
                type:'POST',
                url:basePath + "/netbank/nisbillhead/billListGetInfo",
                contentType: "application/json",
                async:false,
                data:JSON.stringify({voucherId:sendId}),
                success:function(result) {
                    if(result.attributes && result.attributes.payMoney){
                        bt.moneyType.get("billMoney").val(result.attributes.payMoney);
                        $("#voucherId").val(sendId);
                    }
                }
            })
        }
    }
});

function recursionDealData(treeData){
    var selectDt;
    for(var i =0;i < treeData.length;i++){
        var itemData = treeData[i];
        if($("#applyCorpIdInput").val() == itemData.id){
            return itemData;
        }
        if(i == itemData.length-1){
            return selectDt;
        }
        if(!itemData.children || itemData.children.length <= 0){
            continue;
        }
        selectDt = recursionDealData(itemData.children);
        if(selectDt){
            return selectDt;
        }
    }
}

function applyCorpAfterInit(){
    var applyCorpIdData = bt.corp.get("applyCorpId").data();
    var selApplyCorpIdData = recursionDealData(applyCorpIdData);
    if(selApplyCorpIdData){
        applyCorpCallback(selApplyCorpIdData)
    }else {
        isPayFlg = '0';
    }
}

//申请单位回调函数
function applyCorpCallback(data){
    if(!data||data.length==0){
        isPayFlg = '0';
        return
    };
    if(isPayFlg != 1){
        if($('input:radio[name="billFlag"]:checked').val()=="10"){
            $("#payer").val(data.corpName);//出票人名称
        }
        if(data.socCode != undefined){
            $("#blRmtrUnnIdNo").val(data.socCode);

            if($('input:radio[name="billType"]:checked').val()!="10"){
                $("#blAcptrUnnIdNo").val(data.socCode);
            }else {
                $("#blAcptrUnnIdNo").val('');
            }

        }
    }


    if(isPayFlg == 1 && payerAccNum){
        bt.accountTable.get("payeracc").initCorp($("#corpIdInput").val(),"1");
        accountPayeeCallBack(payerAccCallBackData);
    }else {
        bt.accountTable.get("payeracc").initCorp(data.id,"1");
    }
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data.id);
    //初始化票据单位和业务单位
    initCorp(data.id);

    //清空出票人开户银行名称、出票人开户行行号
    if(isPayFlg != "1"){
        $("#payerBankName").val("");
        $("#payerbankno").val("");
    }
    //通过单位id加载出票人银行账号
    //electricBill=1 票据专户
    //资金计划
    if($("#fbsbudgetprocessCorp").val() == 1){
        //业务单位
        if(isPayFlg == "1"){
            $renderBudget(corp.get("applyCorpId").data()[0].id,dateCheck.get("businessDate").val());
        }else {
            $renderBudget(corp.get("busCorpId").data()[0].id,dateCheck.get("businessDate").val());
        }
    }else {
        if(isPayFlg == "1"){
            $renderBudget(corp.get("applyCorpId").data()[0].id,dateCheck.get("businessDate").val());
        }else {
            $renderBudget(corp.get("corpId").data()[0].id,dateCheck.get("businessDate").val());
        }
    }
    //保证金参数设置
    bailParameters();

    if($("#isRelateRat").val()==true){
        setRationBtn();
    }
    corpCallback2(data.id);
    // 执行回调方法
    billFlagCallback();

    reselectBear();

    //授信改造
    ratVerify();

    getLimitInfo();
    isPayFlg = "0";
}
function corpCallback2(id) {
    const $ = bt.jquery, form = bt.form();
    //获取单位信息
    $.ajax({
        url: basePath + "/sys/corp/getSysCorp",
        method: 'POST',
        data: {id: id},
        dataType: 'json',
        success: function (data) {
                $("#blRmtrUnnIdNo").val(data.obj.socCode);
        }
    });
}

var setTimeNum=0;
//初始化票据单位和业务单位
function initCorp(id){
    if(id==undefined || id==null || id==""){
        return
    }

    if( isPayFlg != '1'){
        bt.corp.get("busCorpId").val(id);
        bt.corp.get("corpId").val(id);
    }
    /*if($.fn.zTree.getZTreeObj($('#corpId').find('.select-tree-list').attr('id')).getNodes().length==0){
        if(setTimeNum<4){
            setTimeNum++;
            setTimeout(initCorp(id),500);
        }
    }else{*/
    //}
}

//票据单位回调函数
function corpCallback(data){
    if(data&&data.length==0)return;
    if($('input:radio[name="billFlag"]:checked').val()=="10"){
        $("#payer").val(data.corpName);//出票人名称
    }
    //清空出票人开户银行名称、出票人开户行行号
    $("#payerBankName").val("");
    $("#payerbankno").val("");
    //通过单位id加载出票人银行账号
    //electricBill=1 票据专户
    bt.accountTable.get("payeracc").initCorp(data.id,"1",false);
    //资金计划
    $renderBudget(data.id,dateCheck.get("businessDate").val());
    //保证金参数设置
    bailParameters();

    if($("#isRelateRat").val()==true){
        setRationBtn();
    }

    // 执行回调方法
    billFlagCallback();

    reselectBear();

    //授信改造
    ratVerify();

    getLimitInfo();
}


//业务办理日期回调
function businessDateCallback(val){
    if(corp.get("corpId").data().length!=0){
        $renderBudget(corp.get("corpId").val(),val);
    }
}

//收票单位回调函数
function payeeCorpCallback(data){
    if(!data)return;
    var $ = bt.jquery,form = bt.form();
    $("#payeeAccName").val(data.corpName);//收票人
    $("#blRcvpymtpsUnnIdNo").val(data.socCode);//收票人统一识别代码
    //通过单位id加载收票人银行账号
    if(data.id){
        $("#payeeCorpId").val(data.id);
        bt.accountTable.get("payeeacc1").reload({urldata:{corpId:data.id,electricBill:"1",permission:"",ignorePermission:"true"}});
    }
    $("#payeeBankName").val("");//收票人开户行名称
    $("#payeebankno").val("")//收票人开户行行号
}

//选择收票人账号回调方法
function onChangePayeeacc(data){
    var $ = bt.jquery;
    $("#payeeAccName").val(data.accName);//【出票人以户名为准】
    $("#payeeacc1").val(data.bankAcc);
    $("#payeeacc").val(data.bankAcc);
    $("#payeeBankName").val(data.bankName);//开户行名称
    $("#payeebankno").val(data.bankCode);//开户行行号 bank_code 222333
    //$("#blRcvpymtpsUnnIdNo").val(data.socCode);//收款人统一识别代码 bank_code 222333
}

//招行是否授信提用判断
function zhaohangRatFun(){
    zhaohangRequire();
}

//招行是否授信提用必填判断
function zhaohangRequire(){
    var isCreditDraw = $('input:radio[name="isCreditDraw"]:checked').val();
    var billType = $('input:radio[name="billType"]:checked').val();
    var acceptorBankType = $("#acceptorBankType").val();
    //如果（是否授信提用=是）&（承兑人的银行类别=招商银行）&（票据类型=银票）时，承兑协议编号、合作银行号、风险合同编号、授信合同编号、银承合作协议分行为必填
    if(isCreditDraw == '1' && billType == '10' && acceptorBankType == '招商银行'){
        $(".zhaohangclsr").attr("lay-verify", "strlength|required");
    }else{
        $(".zhaohangclsr").attr("lay-verify", "strlength");
    }
    bt.form().renderIcon();
}

//选择出票人账号回调方法
function accountPayeeCallBack(data){
    var $ = bt.jquery;
    $("#payer").val(data.accName);//【出票人以户名为准】
    $("#payeracc").val(data.bankAcc);
    $("#payerBankName").val(data.bankName);
    $("#payerbankno").val(data.bankCode);
    //挂接保证金 招行帐号显示招行保证金列表进行账号选择
    if(isBail){
        if(("招商银行" == data.bankTypeName || data.bankName.includes("招商银行"))){
            $("#cmbcBailAccountInfoBtn").hide();
            $("#cmbBailAccountInfoBtn").show();
            $("#bailAcc").val('');//保证金账号
            cmbBailAccountInfo.set({"curacc":data.bankAcc});
        }
        if(("民生银行" == data.bankTypeName || data.bankName.includes("民生银行"))){
            $("#cmbBailAccountInfoBtn").hide();
            $("#cmbcBailAccountInfoBtn").show();
            $("#bailAcc").val('');//保证金账号
            cmbBailAccountInfo.set({"bankType":"Ms","accName":data.accName});
        }
    }else{
        $("#cmbBailAccountInfoBtn").hide();
        $("#cmbcBailAccountInfoBtn").hide();
        cmbBailAccountInfo.set({"curacc":"","bankType":""});
    }
    if(isRelateRat){
        $("#ratCode").val("");
        bt.selectType.init().get("ratItemCode").val("");
        bt.selectType.init().get("billName").val("");
        $("#rationBtn").hide();
    }
    $(".zhaohangclsr").val("");
    // $(".zhaohangclsr").attr("lay-verify", "strlength");
    if($('input:radio[name="billType"]:checked').val()!="10"){
        //票据类型为商票时，承兑人默认为出票人
        $("#acceptor").val($("#payer").val());//承兑人
        $("#acceptoracc").val($("#payeracc").val());//承兑人账号
        $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
        $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
        $("#acceptorCorpId").val(data.corpId);//承兑人单位id
        $("#blAcptrUnnIdNo").val($("#blRmtrUnnIdNo").val());
    }
    if($('input:radio[name="billType"]:checked').val()=="10"){
        //票据类型为银票时，承兑人默认为出票人开户行
        $("#acceptor").val($("#payerBankName").val());//承兑人
        $("#acceptoracc").val('0');//承兑人账号
        $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
        $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
        $("#acceptorCorpId").val("");//承兑人单位id
        //保证金参数设置
        bailParameters();
    }
    $("#isOnline").val(data.circulationMode);
    getBankbillConfig(data.bifCode);
    setVerify();
    //授信设置
    ratVerify();
    //获取票据池信息
    getLimitInfo();
}

var isnewBillFlg = false;

/**
 * 获取银行票据配置
 */
function getBankbillConfig(bifCode){
    $.ajax({
        url: basePath + "/bill/accebill/abbillbankautodtl/getBillBankAutoDtlByBifCode",
        method: 'GET',
        async: false,
        data: {'bifCode':bifCode},
        contentType:'application/json',
        dataType: 'JSON',
        success: function (result) {
            if(result != undefined && result.newBillFlg == "1"){
                isnewBillFlg = true;
            }
        },
        error:function(){

        }
    });
}

//根据出票日期获取结束日期
function changeEndDate(month) {
    //获取出票日期
    var startDate = dateCheck.get("startDate").val();
    if (startDate == null || startDate== undefined || startDate=="" ) {
        layer.alert(bt.lang('accebill.message.startDate.isnotselect'), {icon: 2});
        return;
    }
    var date = new Date(Date.parse(startDate.replace(/-/g,"/"))); //转换成Data();
    //算头不算尾，即：到期日期 = 开票日期 + 月份month - 1天
    //$("#endDate").val(bt.date.format(bt.date.addDay(AddMouth(date,month),-1)));
    //算头不算尾，即：到期日期 = 开票日期 + 月份month 天
    $("#endDate").val(bt.date.format(AddMouth(date,month)));
}

//添加月份
function AddMouth(date,addMonth) {
    var y = date.getFullYear();
    var m = date.getMonth();
    var nextY = y;
    var nextM = m;
    //如果当前月+增加的月>11 这里之所以用11是因为 js的月份从0开始
    if ((m + addMonth)> 11) {
        nextY = y + 1;
        nextM = parseInt(m + addMonth) - 12;
    } else {
        nextM = date.getMonth() + addMonth
    }
    var daysInNextMonth = daysInMonth(nextY, nextM);
    var day = date.getDate();
    if (day > daysInNextMonth) {
        day = daysInNextMonth;
    }
    return new Date(nextY, nextM, day);
}

//计算当前月最大天数
function daysInMonth(year, month) {
    if (month == 1) {
        if (year % 4 == 0 && year % 100 != 0)
            return 29;
        else
            return 28;
    } else if ((month <= 6 && month % 2 == 0) || (month > 6 && month % 2 == 1))
        return 31;
    else
        return 30;
}


//承兑人账号回调方法
function accountAcceptorCallBack(data){
    $("#acceptoraccCorp").val(data.bankAcc);//承兑人账号
    $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
    $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
}

//判断出票日期与到期日期手动输入是否正确
function checkDateInput(){
    var startDate = dateCheck.get("startDate").val();
    var endDate = dateCheck.get("endDate").val();
    if(startDate>endDate){
        parent.layer.alert(bt.lang("bill.accebill.startandend"));//出票日期不能大于到期日期
        return false;
    }else{
        return true;
    }
}

//日期检查
function checkDate(val){
    //去掉开票的时间间隔检查，不做控制
    reselectBear();
}

//金额控制
function checkMoney(){
    var $=bt.jquery;
    if($('input:radio[name="billFlag"]:checked').val()=="10"){//纸票
        //出票人可修改
//        $("#payer").removeAttr("readonly");
        if(Number($("#billMoney").val())>maxBillMoney){
            moneyType.get("test").val("0");
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.billFlag10",[maxBillMoney]),{icon: 7});//纸票金额不能大于设定的值
        }moneyType.get("test").reset();
        moneyType.get("test").val({max:maxBillMoney});
    }else{
        //出票人只读
//        $("#payer").attr("readonly","readonly");
        if(Number($("#billMoney").val())>1000000000){
            moneyType.get("test").val("0");
            parent.layer.alert(bt.lang("table.abvoucherinfo.billmoney.billflag11"));//电票金额不能大于1000000000
        }
        moneyType.get("test").reset();
        moneyType.get("test").val({max:1000000000});
    }
}

function getFeemoneyScale(){
    var  feemoney = $("#feemoney").val();
    var  billMoney = $("#billMoney").val();
    if(feemoney == null || feemoney== undefined || feemoney=="" || billMoney == null || billMoney== undefined || billMoney==""){
        return false;
    }
    var  feemoneys = feemoney.toString();
    var  billMoneys = billMoney.toString();
    feemoneys = (feemoneys.indexOf(".") == -1) ? (feemoneys + "00") : (feemoneys.replace(".",""));
    billMoneys = (billMoneys.indexOf(".") == -1) ? (billMoneys + "00") : (billMoneys.replace(".",""));
    var fm = Number(feemoneys);
    var bm = Number(billMoneys);
    rate.get("feemoneyScale").val((fm/bm).toFixed(2));
}

function getFeemoneyMoney(){
    var  feemoneyScale = $("#feemoneyScale").val();
    var  billMoney = $("#billMoney").val();
    if(feemoneyScale == null || feemoneyScale== undefined || feemoneyScale=="" || feemoneyScale==0 || billMoney == null || billMoney== undefined || billMoney==""){
        return false;
    }
    var feemoney = bt.moneyType.getFun().toNumber(billMoney);
    feemoney = bt.moneyType.getFun().mul(feemoney,bt.moneyType.getFun().toNumber(feemoneyScale),2);
    // feemoney = bt.moneyType.getFun().div(feemoney,100,2);
    bt.moneyType.get("feemoney").val(feemoney);
}

//票据金额变更后计算保证金
function getBail(){
    var $=bt.jquery;
    var bailpercent = ratio.get("bailpercent").val();//利率
    if(bailpercent){
        //保证金比例计算保证金
        getBailmoney();
    }else{
        //保证金计算保证金比例
        getBailpercent();
    }
    $("#bailAcc").val("");//清除保证金账户
    $("#bondId").val("");//清除保证金id

    getFeemoneyScale();
    getFeemoneyMoney();
}
//保证金比例计算保证金
function getBailmoney(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val())*100;//票面金额
    if(billMoney>0){
        var bailpercent = ratio.get("bailpercent").val()*10000;//利率
        if(bailpercent>1000000){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
        billMoney = bt.moneyType.getFun().mul(billMoney,bailpercent)/1000000;
        billMoney = Math.ceil(billMoney*100)/100;
//        billMoney = bt.moneyType.getFun().div(billMoney,100);
        moneyType.get('bailmoney').val(billMoney.toFixed(2));

        var curBailpercent = ratio.get("bailpercent").val();
        if (isRelateRat) {
            if (isNotEmpty(curBailpercent)) {
                if (Number(curBailpercent) < Number(1)) {
                    dicts.get('isOpenRat').val("1")
                } else {
                    dicts.get('isOpenRat').val("0")
                }
            }
        }
    } else {
        var curBailpercent = ratio.get("bailpercent").val();
        if (isRelateRat) {
            if (isNotEmpty(curBailpercent)) {
                if (Number(curBailpercent) < Number(1)) {
                    dicts.get('isOpenRat').val("1")
                } else {
                    dicts.get('isOpenRat').val("0")
                }
            }
        }
    }
}

//保证金计算保证金比例
function getBailpercent(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val());//票面金额
    if(billMoney>0){
        var bailmoney = moneyType.get('bailmoney').val();//保证金
        var bailpercent = bt.moneyType.getFun().div(bailmoney,billMoney,4);
        //accDiv(bailmoney,billMoney);
        if(bailpercent>1){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
        //bailpercent = bailpercent.toFixed(4);//四舍五入保留四位小数
        ratio.get("bailpercent").val(bailpercent);
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}

/**表单提单校验保证金金额是否大于保证金可用余额 */
function checkBailMoney(){
    if($('input:radio[name="bailmodel"]:checked').val()=="1"){
        var bailmoney = moneyType.get('bailmoney').val();//保证金
        //判断保证金是否填写
        if(bailmoney>0){
            //判断保证金是否大于保证金可有余额
            if(bailmoney>availMoney){
                //提示保证金金额不能大于保证金可用余额
                parent.layer.msg(bt.lang("accebill.message.bailmoney.maxAccAvailMoney"), {icon: 2});
                return false;
            }
        }
    }
    return true;
}

/**控制保证金额度单选择显示或隐藏*/
function isBailOp(show){
    var $ = bt.jquery,form = bt.form();
    isBail=$("#isBail").val();
    if(isBail&&show){
        $('input[type=radio][name="bailmodel"][value="1"]').prop("checked",'true');
        $(".isBailDiv").show();
    }else{
        $('input[type=radio][name="bailmodel"][value="0"]').prop("checked",'true');
        $(".isBailDiv").hide();
    }
    isBailSelectShow();
    bt.form().render();
}
function bailTypeCallback() {
    $("#bailType").val(dicts.get("bailType").val());
}

/**根据是否挂保证金控制保证金额度单选择显示或隐藏*/
function isBailSelectShow(){
    if($('input:radio[name="bailmodel"]:checked').val()=="0"){
        $("#bailAcc").removeAttr("lay-verify","required");//移除保证金账户必填
        $("#bailAcc").removeAttr("readonly");//移除保证金账户只读
        $('[data-name="bailmoney"]').removeAttr("lay-verify","required");//移除保证金必填
        $('[data-name="bailpercent"]').removeAttr("lay-verify","required");//移除保证金必填
        dicts.get("bailType").attr({
            "data-disabled": false //保证金类型可手动修改
        });
        $("#fcBondQuotasTxtDiv").hide();//保证金额度单
        $("#fcBondQuotasValueDiv").hide();//保证金额度单
        $("#cmbBailAccountInfoBtn").hide();
        $("#cmbcBailAccountInfoBtn").hide();
    }else if($('input:radio[name="bailmodel"]:checked').val()=="1"){
        $("#bailAcc").attr("lay-verify","required|strlength");//添加保证金账户必填
        $("#bailAcc").attr("readonly","readonly");//添加保证金账户必
        bt.dicts.get("bailType").attr({
            "data-disabled": true //保证金类型不可手动修改
        });
        $('[data-name="bailmoney"]').attr("lay-verify","required");//添加保证金必填
        $('[data-name="bailpercent"]').attr("lay-verify","required");//添加保证金必填
        $("#fcBondQuotasTxtDiv").show();//保证金额度单
        $("#fcBondQuotasValueDiv").show();//保证金额度单
        if( $("#payerBankName").val().includes("招商银行")){
            $("#cmbcBailAccountInfoBtn").hide();
            $("#cmbBailAccountInfoBtn").show();
        }
        if( $("#payerBankName").val().includes("民生银行")){
            $("#cmbBailAccountInfoBtn").hide();
            $("#cmbcBailAccountInfoBtn").show();
        }
    }
    dicts.get("bailType").val("0");//保证金类型置为默认值（活期保证金）
    $("#bailType").val("0");
    $("#bailContract").val("");//清除保证金合同
    $("#bailAcc").val("");//清除保证金账户
    $("#bondId").val("");//清除保证金id
    bt.form().renderIcon();
}

//保证金参数传递
function bailParameters(){
    //保证金参数
    parameters.corpId=corp.get("corpId").val();
    parameters.bankCode=$("#acceptorbankno").val();
    parameters.curId=$("#curId").val();
    parameters.startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    parameters.endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    parameters.bailBankControlType = $("#bailBankControlType").val();//保证金银行控制类型
    bt.bondAccInfo.set(parameters);
}


//保证金额度单回调
function bondAccinfoCallback(data,dom) {
    var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
    //保证金可用余额
    availMoney = data.availMoney;
    $("#bondId").val(data.bondId);//保证金额度单
    $("#bailAcc").val(data.bankAcc);//保证金账号
    $("#bailType").val(data.bailType);//保证金类型
    $("#cmbBailFlag").val("");//招行保证金列表选择标识
    dicts.get("bailType").val(data.bailType);//保证金类型
//    $("#bailrate").val(data.bondRate);//保证金利率
    rate.get("bailrate").val(data.bondRate);//保证金利率
    //判断保证金是否填写
    if(bailmoney>0){
        //判断保证金是否大于保证金可有余额
        if(bailmoney>availMoney){
            //提示保证金金额不能大于保证金可用余额
            parent.layer.msg(bt.lang("accebill.message.bailmoney.maxAccAvailMoney"), {icon: 2});
        }
    }
    bt.form().renderIcon();
}

/**控制银行授信显示和隐藏*/
function relateRatOp(show){
    if(isRelateRat&&show){
        $('input[type=radio][name="ratmodel"][value="1"]').prop("checked",'true');
        if(mouthRat=="true"){
            $('input[type=radio][name="isOpenRat"][value="1"]').prop("checked",'true');
        }
        $("#relateRatDiv").show();
        $("#relateCpRatDiv").hide();
    }else{
        $('input[type=radio][name="ratmodel"][value="0"]').prop("checked",'true');
        if(mouthRat=="true"){
            $('input[type=radio][name="isOpenRat"][value="0"]').prop("checked",'true');
        }
        $("#relateRatDiv").hide();
        if(isOpenCpRat && $('input:radio[name="billType"]:checked').val() != "10"){
            $("#relateCpRatDiv").show();
        }
    }
    ratModelCut();
}


/**检查授信是否符合要求*/
function checkRelateRat(){
    if(isRelateRat
        && ($('input:radio[name="billType"]:checked').val() == "10" || $('input:radio[name="emitGuandisSign"]:checked').val() == '1')
        && dicts.get('ratmodel').val()=="1"){
	    var ratCode=$("#ratCode").val();
        var itemMoney=$("#itemMoney").val();
        if(!ratCode){
            parent.layer.msg(bt.lang("accebill.message.relateRat.isnotselect"), {icon: 2});
            return false;
        }

        if(isBail && mouthRat=="true" && $('input:radio[name="bailmodel"]:checked').val()=="1" ){
            var zyMoney = Number($("#billMoney").val())-Number($("#bailmoney").val());
            if(zyMoney>Number(itemMoney)){
                parent.layer.msg(bt.lang("accebill.message.itemMoney.balToosmall"), {icon: 2});
                return false;
            }

        }else if(Number($("#billMoney").val())>Number(itemMoney)){
            parent.layer.msg(bt.lang("accebill.message.itemMoney.balToosmall"), {icon: 2});
            return false;
        }

        //如果敞口是启用 则保证金金额不能为空
        if(dicts.get('isOpenRat').val()=="1"){
            if(moneyType.get('bailmoney').val()==""){
                parent.layer.msg(bt.lang("bt.bill.message.bondmoney.not.null"), {icon: 2});
                return false;
            }
        }

    }

    if (isOpenCpRat && $('input:radio[name="billType"]:checked').val() != "10"
        && dicts.get('isHangCredit').val()=="1"){
        var cpRatCode=$('#cpRatCode').val();
        if(!cpRatCode){
            parent.layer.msg(bt.lang("accebill.message.relateCpRat.isnotselect"), {icon: 2});
            return false;
        }
    }

    return true;
}
/**
 * 银行授信额度选择
 * @param params
 */
function beforeRation(){
    //挂银行授信
    var corpId = $("#ratBankCorpId").val();//授信单位
    var ratBankCorpType = $('input:radio[name="ratBankCorpType"]:checked').val();
    var ratBankType = $('input:radio[name="ratBankType"]:checked').val();
    var bankno;//授信银行行号
    var banknoMsg;//错误提醒
    //当选择授信单位是开票人时
    if(ratBankCorpType == "0"){
        if(bt.empty(corpId)){
            //请选择所属单位
            parent.layer.msg(bt.lang("accebill.message.corpid.isnotselect"), {icon: 2});
            return "";
        }
     /*   //银票类型
        if($('input:radio[name="billType"]:checked').val()=="10"){
            bankno = $("#acceptorbankno").val();//银行行号取承兑人银行行号
            banknoMsg="accebill.message.acceptorbankno.isnotselect";
        // 商票或第三方票据
        }else{
            bankno = $("#payerbankno").val();//银行行号取出票人银行行号
            banknoMsg="accebill.message.payerbankno.isnotselect";
        }*/
    //当选择授信单位是承兑人时
    }else{
        if(bt.empty(corpId)){
            //请选择承兑人单位
            parent.layer.msg(bt.lang("accebill.message.acceptorCorpid.isnotselect"), {icon: 2});
            return "";
        }
      /*  //商票或第三方票据
        if($('input:radio[name="billType"]:checked').val()!="10"){
            bankno = $("#acceptorbankno").val();//银行行号取承兑人银行行号
            banknoMsg="accebill.message.payerbankno.isnotselect";
        }*/
    }
    //授信银行类型：0承兑人开户银行 1出票人开户银行
    if(ratBankType == "0"){
        bankno = $("#acceptorbankno").val();//银行行号取承兑人银行行号
        banknoMsg="accebill.message.acceptorbankno.isnotselect";
    }else if(ratBankType == "1"){
        bankno = $("#payerbankno").val();//银行行号取出票人银行行号
        banknoMsg="accebill.message.payerbankno.isnotselect";
    }
    if(bt.empty(bankno.trim())){
        //选择银行开户行行号
        parent.layer.msg(bt.lang(banknoMsg), {icon: 2});
        return "";
    }
    var curId = $("#curId").val();//币别
    if(bt.empty(curId)){
        //选择币别
        parent.layer.msg(bt.lang("accebill.message.curid.isnotselect"), {icon: 2});
        return "";
    }

    var itemCode = bt.selectType.init().get("ratItemCode").val();//授信品种
    if(bt.empty(itemCode)){
        itemCode="";
    }

//    var itemCode = $('[data-biz-name="ratItemCode"]').val();//授信品种
    var beginDate = dateCheck.get("startDate").val();
    var endDate = dateCheck.get("endDate").val();
    if(bt.empty(beginDate.trim())){
        //选择银行
        parent.layer.msg(bt.lang("accebill.message.startDate.isnotselect"), {icon: 2});
        return "";
    }
    var params = {
        corpId : corpId,
        curCode : curId,
        bankCode : bankno,
        ratItemCode : itemCode.trim(),
        beginDate : beginDate.trim(),
        endDate : endDate.trim(),
    }
    return params;
}
//选择银行授信
function chooseRatCode(params){
    var params = beforeRation();
    if(bt.empty(params)){
        return;
    }
    openRation(params);
}


//查看授信
function viewRatCode(){
    var params = beforeRation();
    if(bt.empty(params)){
        return;
    }
    viewRationDtl(params);
}

//附件初始化
function initAttachmentInfo(){
    var attachmentData=uploader.getData();
    if(attachmentData){
        $("#attachmentId").val(uploader.getGroupId());
        $("#attachmentNum").val(attachmentData.length);
    }
}

//提示未选择单位
function checkCorp(){
	bailParameters();
    var corpIds = corp.get("corpId").val();
    if(corpIds==""){
        layer.alert(bt.lang("table.fcBond.corp.null"),{icon:2});
        return false;
    }else if($("#acceptorbankno").val()==""){
        layer.alert(bt.lang("accebill.message.acceptor.isnotnull"),{icon:2});
        return false
    }else{
        return true;
    }
}

var setTimeoutNum=0;
/**根据选择的业务设置必填标志
 * 电票合同号账户需要必填，纸票账户信息可以不必填。对内自开单位必填**/
function setVerify() {
    var isOnline = $('#isOnline').val();
    var isEmitMake = $('#isEmitMake').val();
    var billFlag = $('input:radio[name="billFlag"]:checked').val();
    var billType=dicts.get("billType").val();
    var insideSing=$('input:radio[name="insideSing"]:checked').val();
    $("#origNo").attr("lay-verify","strlength");
    // 电票且不是第三方票据类型
    if (billFlag == '11' && isNotBillTypeTP(billType)) {
        if($("#payerbankno").val() && $("#payerbankno").val().indexOf("303")==0){
            $("#aContractNo").attr("lay-verify","strlength|required");
        }else{
            $("#aContractNo").attr("lay-verify","strlength");
        }
        $("#payeracc").attr("lay-verify","required");
        $("#acceptoracc").attr("lay-verify","strlength|required");
        $("#acceptor").attr("lay-verify","strlength|required");
        $("#payeeacc").attr("lay-verify","required|strlength");
        $("#pledgeeAccSpanDiv").show();//收票人账号必填符号
        if(insideSing=="1"){
            $("#payeeacc1").attr("lay-verify","required|strlength");
            $("#payeeacc").removeAttr("lay-verify","strlength");
        }else{
            $("#payeeacc1").attr("lay-verify","strlength");
            $("#payeeacc").attr("lay-verify","required|strlength");
        }
        if(isEmitMake=='false'){
            $("#payeebankno").attr("lay-verify","required|strlength");
            $("#payeeBankName").attr("lay-verify","required|strlength");
            $("#acceptorbankno").attr("lay-verify","required|strlength");
            $("#acceptorBankName").attr("lay-verify","required|strlength");
            if(isOnline!='1'){
                $("#origNo").attr("lay-verify","required|strlength");
            }else{
                $("#origNo").attr("lay-verify","strlength");
            }
        }
    } else {
        $("#pledgeeAccSpanDiv").hide();//收票人账号必填符号
        $("#aContractNo").attr("lay-verify", "strlength");
        $("#payeracc").removeAttr("lay-verify","");
        $("#payeeacc").removeAttr("lay-verify","");
        $("#acceptor").attr("lay-verify","strlength");
        $("#acceptoracc").attr("lay-verify","");
        $("#origNo").attr("lay-verify","strlength");
        try{
            if(corp.get("corpId").data()[0]){
                $("#payer").val(corp.get("corpId").data()[0].corpName);
            }
        }catch(err){
            if(setTimeoutNum<4){
                setTimeout("setVerify()",2000);
            }
            setTimeoutNum++;
        }
        $("#payeeacc").attr("lay-verify","strlength");
        $("#payeeacc1").attr("lay-verify","strlength");
        if(isEmitMake=='false'){
            $("#acceptor").attr("lay-verify","required|strlength");
            $("#origNo").attr("lay-verify","required|strlength");
            $("#payeebankno").attr("lay-verify","strlength");
            $("#payeeBankName").attr("lay-verify","strlength");
            $("#acceptorbankno").attr("lay-verify","strlength");
            $("#acceptorBankName").attr("lay-verify","strlength");
        }
    }
    //如果开新票，但是业务银行不支持新票据功能，票据号必填
    if(bt.dicts.init().get("AB_BILL_NEW_STM_ISSUOFPPR_IND").val() == "1" && isnewBillFlg == false && isEmitMake=='false'){
        $("#origNo").attr("lay-verify","required|strlength");
    }
    bt.form().renderIcon();
}


function ratModelCut(){
    $("#bankTypeName").val("");
    $("#accptncagrmtBranch").val("");
    $("#accptncagrmtBranchNo").val("");
    // $("#accptncagrmtnb").val("");
    $("#cooperativeBankNo").val("");
    $("#cvmLimitNo").val("");
    $("#riskContractNumber").val("");
    if($('input:radio[name="ratmodel"]:checked').val()=="0"){
        $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
        bt.selectType.init().get("billName").val("");
        $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
        bt.selectType.init().get("ratItemCode").val("");
        //选择授信为否
        $("#rationSelectDIV").hide();
        $("#ratBankCorpTypeDiv").hide();
        $("#sureRatmodelTxt").hide();
        $("#sureRatmodelValue").hide();
        $("#ratOpenDiv").hide();
        $("#scHideTxt").show();
        $("#scHideValue").show();
        $("#ratIsHangGua").val("");
        $(".zhaohangclsr").val("");
        // $(".zhaohangclsr").attr("lay-verify", "strlength");
    }else{
        if(isRelateRat){
            $('[data-biz-name="ratItemCode"]').find('select').attr("lay-verify", "required");
        }
        $("#rationSelectDIV").show();
        $("#ratBankCorpTypeDiv").show();
        if(mouthRat=="true"){
            $("#ratOpenDiv").show();
            $("#sureRatmodelTxt").show();
            $("#sureRatmodelValue").show();
            $("#scHideTxt").hide();
            $("#scHideValue").hide();
        }
    }
    if($("#isRelateRat").val() == 'true'){
        setRationBtn();
    }
    bt.form().renderIcon();
    ratInfoExtSet();
    ratVerify();
}


function setOtherParam(param){
    var corpId = corp.get("corpId").val();
    var curId = $("#curId").val();//币别
    var startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    var endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}


function selectBefore(){
    var corpId = corp.get("corpId").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("table.rat.corp.null"), {icon: 2});
        return false;
    }else if (bt.empty($.trim(dateCheck.get("startDate").val()))) {//判断开始日期
        layer.alert(bt.lang("accebill.message.startdate.isnotnull"), {icon: 2});
        return false;
    }else if (bt.empty($.trim(dateCheck.get("endDate").val()))) {//判断结束日期
        layer.alert(bt.lang("accebill.message.enddate.isnotnull"), {icon: 2});
        return false;
    }else{
        return true;
    }
}

//如果状态是开启，且未选择担保，则提示信息必填
function checkBear(){
    if(isBear=="1"){
        //如果授信额度单挂接了担保合同， 则不允许提交
        if(dicts.get('isHangGuarantee').val()=="1" && $("#guaContractCodes").val()=="" ){
            layer.alert(bt.lang("table.accebill.rat.IsHangGua.refer"), {icon: 2});
            return false;
        }
    }
    return true;
}


function reselectBear(){
    if(isBear=="1"){
        resetGuaMsg();
    }
}


function ratInfoExtSet(params) {
    if(isBear == "1"){
        var ratIsHangGua = $("#ratIsHangGua").val();
        if ("1" == ratIsHangGua) {
            $("#isHangGuarantee").val("0");
            dicts.get("isHangGuarantee").attr({ 'data-defaultvalue':0});
            $("#guaContractCodes").removeAttr("lay-verify");
            $(".isHangGua").hide();
            $("#guaBtn").hide();
            resetGuaMsg();
            form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
        } else {
            $(".isHangGua").show();
            $("#guaContractCodes").attr("lay-verify", "required");
            $("#isHangGuarantee").val("1");
            dicts.get("isHangGuarantee").attr({ 'data-defaultvalue':1});
            form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        }
    }

    if(params && params.bankTypeName.indexOf('招商银行')>-1 ){
        $("#bankTypeName").val(params.bankTypeName);
        $("#accptncagrmtBranch").val(params.bankName);
        $("#accptncagrmtBranchNo").val(params.bankCode);
        $(".zhaohangcls").show();
        var isOnline = $('#isOnline').val();
        if(isOnline == '1'){
            $(".zhaohangclsr").val("");
            // $(".zhaohangclsr").attr("lay-verify", "strlength|required");
            // $.ajax({
            //     type:'post',
            //     url: basePath + "/bill/accebill/abemitbill/selectCmbCrlinfoByCVMNo",
            //     contentType:'application/json;charset=utf-8',
            //     data:JSON.stringify({"creditlineid":params.contractNo}),
            //     success:function(data){//返回json结果
            //         if(data){
            //             $("#accptncagrmtnb").val(data.contracttextno);
            //         }
            //
            //     }
            //
            // });
        }else {
            $(".zhaohangclsr").val("");
            // $(".zhaohangclsr").attr("lay-verify", "strlength");
        }
    }else {
        $("#bankTypeName").val("");
        $("#accptncagrmtBranch").val("");
        $("#accptncagrmtBranchNo").val("");
        $(".zhaohangclsr").val("");
        // $(".zhaohangclsr").attr("lay-verify", "strlength");
    }
    $("#cvmLimitNo").val(params ? params.contractNo : "");
    if($("#cvmLimitNo").val()){
        $(".zhaohangcls").show();
    }else{
        $(".zhaohangcls").hide();
    }

    if(params && params.bankTypeName.indexOf('汇丰银行')>-1 ){
        dicts.get('isOpenRat').val("0");
    }
}

/**
 * 授信额度选择
 * @param params
 */
function beforeCpRation(){
    var corpId = corp.get("corpId").val();
    if(bt.empty(corpId)){
        //选择单位
        parent.layer.msg(bt.lang("accebill.message.corpid.isnotselect"), {icon: 2});
        return "";
    }
    var curId = $("#curId").val();//币别
    if(bt.empty(curId)){
        //选择币别
        parent.layer.msg(bt.lang("accebill.message.curid.isnotselect"), {icon: 2});
        return "";
    }
    var itemCode = bt.selectType.init().get("cpRatItemCode").val();//授信品种
    if(bt.empty(itemCode)){
        itemCode="";
    }

    var beginDate = dateCheck.get("startDate").val();
    if(bt.empty(beginDate.trim())){
        parent.layer.msg(bt.lang("accebill.message.startDate.isnotselect"), {icon: 2});
        return "";
    }
    var params = {
        corpId : corpId,
        curCode : curId,
        ratItemCode : itemCode.trim(),
        beginDate : beginDate.trim(),
    }
    return params;
}

/**
 * 显示保贴
 */
var showEndors = function(flag){
    var $ = bt.jquery,form = bt.form();
    //显示
    if(flag){
        $("#emitGuandisSign").show();
        $("#emitGuandisSignText").show();
        $('input[type=radio][name="emitGuandisSign"][value="0"]').prop("checked",true);
        if(isOpenCpRat==true){
            $('input[type=radio][name="isHangCredit"][value="1"]').prop("checked",true);
            cpRatModelBack();
        }
    }else{
        $('input[type=radio][name="emitGuandisSign"]').prop("checked",false);
        $('input[type=radio][name="isHangCredit"]').prop("checked",false);
        $("#emitGuandisSign").hide();
        $("#emitGuandisSignText").hide();
//		$('input[type=radio][name="emitGuandisSign"][value="0"]').prop("checked",'true');
        $('[data-biz-name="cpRatItemCode"]').find('select').removeAttr('lay-verify');
    }
    bt.form().render();
}


//商票且是保贴则显示银行授信挂接
function resetBankDiv(){
    if(isRelateRat){
        var emitGuandisSign = $('input:radio[name="emitGuandisSign"]:checked').val();
        if(emitGuandisSign=="1"){
            dicts.get("ratmodel").attr({'data-defaultvalue':1});
            $("#relateRatDiv").show();

        }else{
            dicts.get("ratmodel").attr({'data-defaultvalue':0});
            $("#relateRatDiv").hide();

        }
        ratModelCut();
        bt.form().render();
    }
}


var billFlagVal = "11";
//出票标志 内容变更方法
function newStmIssuofpprIndChange(data){
    if(data.value == 1){
        $(".drawBillShow").show();
        dicts.get("billFlag").val("11");
        billFlagVal = "11";
        //$('input:radio[name="billFlag"]').attr("data-disabled","true");
        //$('input:radio[name="billFlag"]').attr("data-disabled",true);
        bt.dicts.get("billFlag").attr({"data-disabled": true});
        //$(".billFlagClass").attr("data-disabled","true");
    }else {
        $(".drawBillShow").hide();
        billFlagVal = dicts.get("billFlag").val();
        //bt.dicts.get("billFlag").attr("data-disabled","false");
        bt.dicts.get("billFlag").attr({"data-disabled": false});
    }
    billFlagCallback();
    form.renderIcon();
    setVerify();

    form.render();
    form.renderIcon();
}

//表单提交时判断额度信息是否正确，及余额是否大于10万
var formSubmitCpCheckRat = function(){
    var isOpenCpRat= $("#isOpenCpRat").val();
    var isRelateRat= $("#isRelateRat").val();
    if(isOpenCpRat=="true"){
        if(dicts.get('isHangCredit').val()=="1"
            && $('input:radio[name="billType"]:checked').val() != "10"){
            if($("#cpItemFlag").val()==""){
                parent.layer.msg(bt.lang("accebill.message.relateCpRat.isnotselect"), {icon: 2});
                return false;
            }
            //处理其它关联
        }
    }

    return true;
}

/**
 * 内部授信选择回调
 */
function cpRatModelBack(){
    var $ = bt.jquery,form = bt.form();
    // 票据类型为第三方票据时，挂接内部授信时授信品种必填
    if($('input:radio[name="isHangCredit"]:checked').val() == '1'
        && $('input:radio[name="billType"]:checked').val() != "10"){
        $('[data-biz-name="cpRatItemCode"]').find('select').attr("lay-verify", "required");
    }else{
        bt.selectType.init().get("cpRatItemCode").val(' ');
        $('[data-biz-name="cpRatItemCode"]').find('select').removeAttr('lay-verify');
    }
    setCpRatModel();
    bt.form().renderIcon();
}

//流通介质回调方法
function billFlagCallback(){
    var billType=dicts.get("billType").val();
//        aContractNo();
    if(($('input:radio[name="billFlag"]:checked').val()=="11" || billFlagVal == "11")
        && isNotBillTypeTP(billType)){
        $("#payerBankName").attr("readonly","true");
        $("#payerbankno").attr("readonly","true");
        //$("#payeeBankName").attr("readonly","true");
        //$("#payeebankno").attr("readonly","true");
        $("#acceptorBankName").attr("readonly","true");
        $("#acceptorbankno").attr("readonly","true");
        //if(isPayFlg != "1"){
        //    $("#payer").val("");
        //}
    }else{
        $("#payerBankName").removeAttr("readonly");
        $("#payerbankno").removeAttr("readonly");
        //$("#payeeBankName").removeAttr("readonly");
        //$("#payeebankno").removeAttr("readonly");
        $("#acceptorBankName").removeAttr("readonly");
        $("#acceptorbankno").removeAttr("readonly");
    }
    //出票人、收票人、承兑人重置
    if(isPayFlg != "1"){
       /* $("#payeracc").val("");
        $("#payerBankName").val("");
        $("#payerbankno").val("");
        $("#payeeacc1").val("");
        $("#payeeacc").val("");

        $("#payeeBankName").val("");
        $("#payeebankno").val("");*/
 /*       $("#acceptoracc").val("");
        $("#acceptor").val("");
        $("#acceptorBankName").val("");
        $("#acceptorbankno").val("");
        $("#acceptorCorpId").val("");*/
    }
    //$("#payeeAccName").val("");
    setVerify();
}

// 不是第三方票据类型则返回true
function isNotBillTypeTP(billType){
    if(billType!=10 && billType!=11){
        return false;
    }
    return true;
}

/**
 *当纸票或第三方票据挂接授信时，如果授信单位（银行授信单位或内部授信单位）为开票人,
 * 则出票人信息中的出票人开户银行名称及出票人开户银行行号不可为空
 */
function ratVerify(){
    var billFlag = $('input:radio[name="billFlag"]:checked').val();
    var billType = dicts.get("billType").val();
    var ratmodelVal = $('input:radio[name="ratmodel"]:checked').val();
    var ratBankCorpType = $('input:radio[name="ratBankCorpType"]:checked').val();
    //挂银行授信
    if(isRelateRat && ratmodelVal=="1") {
        //当选择授信单位是开票人时
        if (ratBankCorpType == "0") {
            //银行授信单位Id
            if(isPayFlg == 1){
                $("#ratBankCorpId").val($("#applyCorpIdInput").val());
            }else {
                $("#ratBankCorpId").val(bt.corp.get("applyCorpId").val());
            }
            //当纸票或第三方票据时出票人信息中的出票人开户银行名称及出票人开户银行行号不可为空
            if (billFlag == '10' || (billType != 10 && billType != 11)) {
                $("#payerBankName").attr("lay-verify", "required|strlength");
                $("#payerbankno").attr("lay-verify", "required|strlength");
            } else {
                $("#payerBankName").attr("lay-verify", "strlength");
                $("#payerbankno").attr("lay-verify", "strlength");
            }
            $("#acceptorBankName").attr("lay-verify", "strlength");
            $("#acceptorbankno").attr("lay-verify", "strlength");
            //当选择授信单位是承兑人时
        } else if (ratBankCorpType == "1") {
            $("#ratBankCorpId").val($("#acceptorCorpId").val());
            //当纸票或第三方票据时承兑人信息中的承兑人开户银行名称及承兑人开户银行行号不可为空
            if (billFlag == '10' || (billType != 10 && billType != 11)) {
                $("#acceptorBankName").attr("lay-verify", "required|strlength");
                $("#acceptorbankno").attr("lay-verify", "required|strlength");
            } else {
                $("#acceptorBankName").attr("lay-verify", "strlength");
                $("#acceptorbankno").attr("lay-verify", "strlength");
            }
            $("#payerBankName").attr("lay-verify", "strlength");
            $("#payerbankno").attr("lay-verify", "strlength");
        }
    }else if(isRelateRat && ratmodelVal=="0"){
        //银行授信单位Id
        if(isPayFlg != "1"){
            $("#ratBankCorpId").val("");
        }
        $("#payerBankName").attr("lay-verify", "strlength");
        $("#payerbankno").attr("lay-verify", "strlength");
        $("#acceptorBankName").attr("lay-verify", "strlength");
        $("#acceptorbankno").attr("lay-verify", "strlength");
    }

    bt.form().renderIcon();
}

//查询票据池额度和可用额度
function getLimitInfo() {
    if($("#isBillPool").val()){
        var emitType=dicts.get("emitType").val();
        var corpId=bt.corp.get("corpId").val(), bankAcc=$("#payeracc").val();
        // 当单位ID和票据池ID都不为空时查询
        if(emitType=='10' && corpId && bankAcc && $.trim(corpId) && $.trim(bankAcc)) {
            $("#poolListId").show();
            $.ajax({
                url: basePath + "/billPool/limitInfo/getLimitInfo",
                method: 'POST',
                data: {'corpId': corpId,'bankAcc' : bankAcc},
                dataType: 'JSON',
                success: function (result) {
                    if(result && result.id){
                        $('#poolLimitMoney').html(bt.money.format(result.poolMoney));
                        $('#poolLimitValidMoney').html(bt.money.format(result.validMoney));
                        $('#poolLimitValidMoneyVal').val(bt.money.format(result.validMoney));
                    }else{
                        $('#poolLimitMoney').html("0.00");
                        $('#poolLimitValidMoney').html("0.00");
                        $('#poolLimitValidMoneyVal').val("0.00");
                    }
                },
                error:function(){
                    $('#poolLimitMoney').html("0.00");
                    $('#poolLimitValidMoney').html("0.00");
                }
            });
        }else{
            $("#poolListId").hide()
        }
    }
}

function setCooperativeBankNumberData(data){
    $("#accptncagrmtnb").val(data.textno);
    $("#accptncagrmtBranch").val(data.orgname);
    $("#cooperativeBankNo").val(data.brnnbr);
    $("#accptncagrmtBranchNo").val(data.brnnbr);
    // $("#cooperativeBankNo").val(data.textno);//合作银行号
    $("#cmbBnkbilnegId").val(data.id);
}

function setRiskContractNumberData(data){
    $("#riskContractNumber").val(data.acrcon);//风险合同编号
    $("#cmbCrlConId").val(data.id);//风险合同表id
}

function aaaabbb(){

}

//招行保证金列表回调
function cmbBailAccountInfoCallback(data){
    var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
    //保证金可用余额
    availMoney = data.avlbal;
    $("#bondId").val(data.bondId);//保证金额度单
    $("#bailAcc").val(data.accnbr);//保证金账号
    $("#bailType").val(data.hmgtyp);//保证金类型
    $("#cmbBailFlag").val("1");//招行保证金列表选择标识
    dicts.get("bailType").val(data.hmgtyp);//保证金类型
    rate.get("bailrate").val(data.bondRate);//保证金利率
    //判断保证金是否填写
    if(bailmoney>0){
        //判断保证金是否大于保证金可有余额
        if(bailmoney>availMoney){
            //提示保证金金额不能大于保证金可用余额
            parent.layer.msg(bt.lang("accebill.message.bailmoney.maxAccAvailMoney"), {icon: 2});
        }
    }
    bt.form().renderIcon();
}

//民生保证金列表回调
function cmbcBailAccountInfoCallback(data){
    var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
    //保证金可用余额
    availMoney = data.availBal;
    $("#bondId").val(data.bondId);//保证金额度单
    $("#bailAcc").val(data.acctNo);//保证金账号
    $("#bailType").val(data.timTypeValue);//保证金类型
    $("#cmbBailFlag").val("2");//民生保证金列表选择标识
    dicts.get("bailType").val(data.timTypeValue);//保证金类型
    // 民生保证金是待百分号的。这里除以100
    if (data.rate && Number(data.rate)){
        rate.get("bailrate").val(Number(data.rate)/100);//保证金利率
    }

    //判断保证金是否填写
    if(bailmoney>0){
        //判断保证金是否大于保证金可有余额
        if(bailmoney>availMoney){
            //提示保证金金额不能大于保证金可用余额
            parent.layer.msg(bt.lang("accebill.message.bailmoney.maxAccAvailMoney"), {icon: 2});
        }
    }
    bt.form().renderIcon();
}

function deserialize(str) {
    let obj = {};
    let pairs = str.split('&');
    for (let i = 0; i < pairs.length; i++) {
        let pair = pairs[i].split('=');
        let key = decodeURIComponent(pair[0]);
        let value = decodeURIComponent(pair[1]);
        obj[key] = value;
    }
    return obj;
}
