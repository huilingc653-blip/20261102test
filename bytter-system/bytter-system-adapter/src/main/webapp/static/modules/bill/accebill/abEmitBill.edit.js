var corp,dateCheck,moneyType,isBudget,rate,ratio,form,parameters,isBail,bondAccInit,isBailModel,availMoney,mouthRat,isBear,maxBillMoney,isOpenCpRat,dicts,isBatchBill,oneTimeFlg;
var isPayFlg;
var cmbBailAccountInfo;
bt.use(['form','radioType','layedit','btdate','corp','select','moneyType','capNumber','selectType','jquery','element','dateType'
    ,'externalCorpBankAcc','bankInfo','rateType','accountTable','bank','dicts','ratioType','bondAccInfo','cmbBnkbilneg','cmbCrlcon','cmbBailAccountInfo'], function(){
    var $ = bt.jquery,form = bt.form(),date = new Date(),month;
    dicts = bt.dicts.init();
    isPayFlg = $("#isPayFlg").val();//是否是付款办理的开票
    if(dicts.get("AB_BILL_NEW_STM_ISSUOFPPR_IND").val() == "1"){
        $(".drawBillShow").show();
    }else {
        $(".drawBillShow").hide();
    }
    if(dicts.get("bailmodel").val() == '1' ){
        if($("#payerBankName").val().includes("招商银行")){
            $("#cmbcBailAccountInfoBtn").hide();
            $("#cmbBailAccountInfoBtn").show();
        }
        if($("#payerBankName").val().includes("民生银行")){
            $("#cmbBailAccountInfoBtn").hide();
            $("#cmbcBailAccountInfoBtn").show();
        }
    }else{
        $("#cmbBailAccountInfoBtn").hide();
        $("#cmbcBailAccountInfoBtn").hide();
    }
    if( isPayFlg == "1"){
        //保证金账号的初始化
        bondAccInit = bt.bondAccInfo.init({
            multiLayer:true
        });
    }else {
        bondAccInit = bt.bondAccInfo.init();
    }

    isBatchBill=$('#isBatchBill').val();
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }
    form.on('checkbox(isCommon)', function (data) {
        var checked = data.elem.checked;
        if(checked){
            $("#isCommonDiv").show();
        }else{
            $("#isCommonDiv").hide();
        }
        form.render("checkbox");
    });
    //挂银行授信标识
    isRelateRat=$("#isRelateRat").val()=="true"?true:false;
    //挂内部授信标识
    isOpenCpRat=$("#isOpenCpRat").val()=="true"?true:false;
    //挂保证金参数标识
    isBail=$("#isBail").val()=="true"?true:false;
    //当前单据是否挂保证金标识
    isBailModel=$("#isBailModel").val();
    //是否挂担保
    isBear=$("#isGuarantee").val();
    //授信敞口标识
    mouthRat=$("#mouthRat").val();
    //之前保存的保证金可用余额
    availMoney=$("#availMoney").val();
    //保证金账号的初始化
    if( isPayFlg == "1"){
        bt.bankInfo.init({
            multiLayer:true
        });
    }else {
        bt.bankInfo.init();
    }
    //保证金参数
    parameters={};
    maxBillMoney = Number($("#maxBillMoney").val());
    corp=bt.corp.init();
    dateCheck = bt.dateType.init();
    moneyType = bt.moneyType.init();
    //bt.externalCorpBankAcc.init();
    //bt.bankInfo.init();
    rate = bt.rateType.init();
    ratio = bt.ratioType.init();

    if($("#source").val()!=10){
        //设置票据单位
        //bt.corp.init().get("corpId").setUrl2("accCorp",$("#applyCorpIdOld").val());
    }
    bt.accountTable.init({urldata:{accountType:'2,3',corpId:$("#corpIdOld").val(),electricBill:'1'}});

    bt.bank.init({
        elem:'.bank'
    });
    var externalCorpBankAcc = "";
    if( isPayFlg == "1"){
        externalCorpBankAcc = bt.externalCorpBankAcc.init({
            multiLayer:true,
        });
    }else {
        externalCorpBankAcc = bt.externalCorpBankAcc.init();
    }
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查询
        if($("#source").val()!=10){
            externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : $("#corpIdDef").val(),"type":4});
        }
        return true;
    }
    //承兑人客商
    bt.acceptorCustomerBankAccParams = function () {
        if($("#source").val()!=10){
            externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : $("#corpIdDef").val(),"type":4});
        }else{
            externalCorpBankAcc.set({"corpId": $("#corpIdDef").val(),"type":4});
        }
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
        if($("#payeracc").val()){
            cmbBailAccountInfo.set({"curacc":$("#payeracc").val()});
        }
    }else {
        cmbBailAccountInfo = bt.cmbBailAccountInfo.init();
        if($("#payeracc").val()){
            cmbBailAccountInfo.set({"curacc":$("#payeracc").val()});
        }
    }

    if($("#billFlag").val()=='10'){
        moneyType.get("test").val({max:maxBillMoney});//纸票金额最大值
//        $("#payer").removeAttr("readonly");
        $("#payeracc").attr("lay-verify","");
        $("#acceptoracc").attr("lay-verify","");
        $("#acceptor").attr("lay-verify","strlength");
        bt.form().renderIcon();
    }else{
        $("#acceptoracc").attr("lay-verify","required");
        moneyType.get("test").val({max:1000000000});//电票金额最大值
    }
    if($("#insideSing").val()=='0' || !$("#insideSing").val()){//开出方式：对外
        $("#payeeCorpId").hide();//成员单位选择框
        $("#payeeAccDiv").hide();//成员单位账号
        $("#pledgeeAccDiv").show();//客商账号
        $("#pledgeeNameDiv").show();//客商按钮
        $("#corpPayee").hide();//收票单位
        $("#payeeacc").attr("lay-verify","required");
        $("#pledgeeAccSpanDiv").show();//收票人账号必填符号
        //$("#payeeacc1").attr("lay-verify","");
        //corp.get("corpId").attr("lay-verify","");
        $("#payeeBankBtDiv").show();//银行查询按钮
    }else{
        $("#payeeCorpId").show();
        $("#payeeAccDiv").show();
        $("#pledgeeAccDiv").hide();
        $("#pledgeeNameDiv").hide();
        $("#corpPayee").show();//收票单位
        $("#payeeSpanDiv").show();//收票单位必填符号
        //通过单位id加载收票人银行账号
        bt.accountTable.get("payeeacc1").initCorp($("#payeeCorpIdOld").val(),"1");
        $("#payeeacc1").val($("#payeeaccOld").val());
        $("#payeeacc").val($("#payeeaccOld").val());
        if($("#billFlag").val()=='10'){
            //纸票且对内时收票人账号选填否则必填
            $("#payeeacc").attr("lay-verify","strlength");
            $("#payeeacc1").attr("lay-verify","strlength");
            bt.form().renderIcon();
        }
        $("#pledgeeAccSpanDiv").hide();//收票人账号必填符号
        //$("#payeeacc1").attr("lay-verify","required");
        //corp.get("corpId").attr("lay-verify","required");
        $("#payeeBankBtDiv").hide();//银行查询按钮
    }

    if($("#billType").val()=='10'){//流通介质：银票
        $("#acceptorBankButtonDiv").show();//银行选择按钮
        $("#acceptorCustomerButtonDiv").hide();//客商按钮
        loadRatMenu(false);
        //置灰承兑人银行授信类型
        dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
    }else{
        $("#acceptorBankButtonDiv").hide();
        $("#acceptorCustomerButtonDiv").show();
        $("#isBailDiv").hide();
        loadRatMenu(true);
    }
    oneTimeFlg = true;
    ratModelCut();
    if(isOpenCpRat==true){
        cpRatModelBack();
    }

    var selectObj = bt.selectType.init();
    if(isRelateRat == "true"){
        if($("#ratmodel").val()=='0'){//是否挂授信：否
            selectObj.get('ratItemCode').attr("data-disabled",true);
            $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
            $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
            $("#rationSelectDIV").hide();
        }else{
            selectObj.get('ratItemCode').attr("data-disabled",false);
            $("#rationSelectDIV").show();
        }
    }

    if($("#source").val() != '10'){
        //开票登记票据来源为录入时，通过单位id加载出票人银行账号
        bt.accountTable.get("payeracc").initCorp($("#corpIdOld").val(),"1");
    }/*else{
    	//开票登记票据来源为付款生成时，承兑人默认为空
    	//付款开票登记
		if($("#acceptoraccOld").val()){
            $("#acceptoracc").val($("#acceptoraccOld").val());
		}else{
			//承兑人账号默认为出票人账号
            $("#acceptoracc").val($("#payeraccOld").val());
		}
    }*/
    $("#payeracc").val($("#payeraccOld").val());

    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            //required:true,
            istime: false
        });
    });

    var payItem = $("#payItem").val();//预算科目
    //var insideSing = $('input:radio[name="insideSing"]:checked').val();//0对外
    //监听修改保存
    form.on('submit(save)', function(data){
        //submit(data,"/bill/accebill/abemitbill/modify");
        submit(data,false);
    });
    //监听修改提交
    form.on('submit(submit)', function(data){
        if(!$("#openBillInfo").val() && $("#sysVoucherCode").val() == "49"){
            bt.loading.hide();
            layer.alert("请先完善票据信息！", {icon: 7});
            return false;
        }
        // submit(data,"/bill/accebill/abemitbill/submit/update");
        submit(data,true);
    });
    var submit = function(data,isSubmit){
        /*  if(!checkDateInput()){
              return false;
          }*/
        bt.loading.show();
        if(!data.field.billFlag){
            //流通介质必填
            bt.loading.hide();
            layer.alert(bt.lang('bill.accebill.abemitbill.billFlag'), {icon: 7});
            return false;
        }
        if(!data.field.billType){
            //票据类型必填
            bt.loading.hide();
            layer.alert(bt.lang('bill.accebill.abemitbill.billType'), {icon: 7});
            return false;
        }
        if(!data.field.insideSing){
            //开出方式必填
            bt.loading.hide();
            layer.alert(bt.lang('bill.accebill.abemitbill.insideSing'), {icon: 7});
            return false;
        }
        if($("#payeracc").val() && $("#payeracc").val() == $("#payeeacc").val()){
            //出票人账号和收票人账号不能一样！
            bt.loading.hide();
            layer.alert(bt.lang('common.accebill.abEmitBill.AccountErrorMessage'), {icon: 7});
            return false;
        }
        if($("#accptncagrmtBranch").val() && $("#accptncagrmtBranch").val().indexOf("招商银行")>-1  &&(
            !$("#accptncagrmtBranchNo").val() ||
            !$("#accptncagrmtnb").val() ||
            !$("#cooperativeBankNo").val() ||
            !$("#cvmLimitNo").val() ||
            !$("#riskContractNumber").val())
            && !$("#origNo").val()){//线下票据不强制需要协议编号的信息
            bt.loading.hide();
            //招商银行开票银行承兑协议编号等信息必填
            layer.alert("请完善承兑协议编号等信息", {icon: 7});
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
            if(bt.moneyType.get("sumMoney").val()!=data.field.billMoney){
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


        if(!formSubmitCpCheckRat()){
            bt.loading.hide();
            return false;
        }

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
        bt.loading.show();
        // initAttachmentInfo();
        var action="/bill/accebill/abemitbill/modify";
        if(isSubmit){
            action="/bill/accebill/abemitbill/submit/update";
        }
        if(isBatchBill=='1'){
            batchEmitSubmit(isSubmit);
        }else {
            var emitType=dicts.get("emitType").val();
            if($("#isBillPool").val()
                && emitType=='10'
                && $('#poolLimitValidMoneyVal').val()!="false"
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
            $.post(basePath + action, subData, function (result) {
                bt.loading.hide();
                alertMessage(result,function (){
                    if (result.success) {
                        if($("#isPayFlg").val() == 1){
                            bt.navtab.getCurrentDom().openBillResult(deserialize(subData));
                            parent.layer.closeAll();
                            return;
                        }
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                        bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
                    }
                })
            }, 'JSON');
        }
    }

    function alertMessage(result,fn){
        parent && parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2,yes:function (){
                fn & fn()
            }}) : layer.alert(result.msg, {icon: result.success ? 1 : 2,yes:function (){
                fn & fn()
            }})
    }
    //流通介质
    form.on('radio(billFlag)', function(data){
        checkDate();
        //金额控制
        checkMoney();
        bt.form().renderIcon();
        //出票人、收票人、承兑人重置
        if($("#source").val() == '11'){
            $("#payeracc").val("");
            $("#payer").val("");
            $("#payerBankName").val("");
            $("#payerbankno").val("");
            $("#payeeacc1").val("");
            $("#payeeacc").val("");
            //$("#payeeAccName").val("");
            $("#payeeBankName").val("");
            $("#payeebankno").val("");
            $("#acceptoracc").val("");
            $("#acceptor").val("");
            $("#acceptorBankName").val("");
            $("#acceptorbankno").val("");
        }
        $("#acceptorCorpId").val("");
        setVerify();
        //授信改造
        ratVerify();
    });
    $("#closeBtn").click(function(){
        parent.layer.closeAll();
    })
    //开出方式
    form.on('radio(insideSing)', function(data){
        if($("#source").val() == "10"){
            //选择对内时，校验收票账号是否为内部账号
            if($('input:radio[name="insideSing"]:checked').val()=="1"){
                $.ajax({
                    type: 'POST',
                    url: basePath+"/bill/accebill/abemitbill/getBankDataByPayeeacc",
                    data:{'bankAcc':$("#payeeaccOld").val()},
                    dataType:'json',
                    async:false,
                    success: function(result){
                        if(!result.success){
                            parent.layer.alert(bt.lang("bill.accebill.billFlag10.bankacc.isNotExist"), {icon: 7});//收票人票据专户账号不存在,不能对内开票!
                            dicts.get('insideSing').val("0");
                        }
                    }});
            }
            //付款生成的开票登记，收票人不变更
            return;
        }
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
//            $("#payeeacc").attr("lay-verify","required|strlength");
//            $("#pledgeeAccSpanDiv").show();//收票人账号必填符号
            $("#payeeSpanDiv").hide();//收票单位必填符号
            //$("#fbsbudgetprocessDiv").show();//资金计划div
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
            //$("#acceptorBankDiv").show();//银行承兑人
            $("#acceptorBankButtonDiv").show();//银行选择按钮
            $("#acceptorCustomer").hide();//客商承兑人输入框
            $("#acceptorCustomerButtonDiv").hide();//客商按钮
            $("#acceptorBankButtonAcceptDiv").hide();//商票承兑人银行按钮
            $("#blAcptrUnnIdNo").val('');//承兑人统一信用代码
            //$("#acceptoraccCorpDiv").hide();//承兑账号--成员单位
            $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行行号
            $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
            $("#acceptorbankaddress").val("");//承兑人开户行地址
            $("#acceptor").val($("#payerBankName").val());//银行名称
            $("#acceptoracc").val('0');//清空承兑人账号
            //银票授信单位类型把承兑人置灰
            dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
            relateRatOp(true);
            isBailOp(true);//银票显示保证金
            showEndors(false);
            $('[data-biz-name="ratItemCode"]').find('select').attr("lay-verify", "required");
        }else{
            //商票,承兑人选择成员单位或客商信息
            //$("#acceptorBankDiv").hide();
            $("#acceptorBankButtonDiv").hide();
            $("#acceptorCustomerButtonDiv").show();
            $("#acceptorBankButtonAcceptDiv").show();
            //$("#acceptoraccCorpDiv").hide();
            //$("#acceptoraccCustomerDiv").show();
            //商票时承兑人默认为出票人
            $("#acceptor").val($("#payer").val());//承兑人
            $("#acceptoracc").val($("#payeracc").val());//承兑人账号
            $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
            $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
            $("#acceptorbankaddress").val("");//承兑人开户行地址
            //授信单位类型都能选
            dicts.get("ratBankCorpType").attr({"data-disabledValue": ""});
            relateRatOp(false);
            if($("#isRelateRat").val()==true && $('input:radio[name="emitGuandisSign"]:checked').val() == '0'){
                setRationBtn();
            }
            isBailOp(false);//商票不显示保证金
            showEndors(true);
        }
        setVerify();
        zhaohangRequire();
    });

//    form.on('radio(ratmodel)', function(data){
//    	ratModelCut();
//    });
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
                $("#blRcvpymtpsUnnIdNo").val(data.socCode);//收票人统一识别代码
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
                $("#blAcptrUnnIdNo").val(data.socCode);//承兑人统一识别代码
                $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
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
    $genOtherHtml=function(corpId,processDate,billId){
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
        var url=basePath+"/budget/fbsBudgetProcess/getEditItemDetail";
        var param={
            corpId:corpId,
            itemCode: itemCode,
            defItemCode:defItemCode,
            billId: billId,
            curId:curId,
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: processDate,
            type:'select',
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:false,success: function (json) {
                $("#fbsbudgetprocessDiv").show();
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                bt.form().render();
            }
        });
    };

    /**
     * 资金计划渲染
     */
    $renderBudget = function(corpId,processDate){
        if($("#VoucherId").val()){
            //判断付款单是否有挂预算
            if($("#nisBillheadNoBudget").val()){
                isBudget = false;
                $("#tagItemDetail").html("");
            }else{
                isBudget = true;
                $genOtherHtml(corpId,processDate,$("#VoucherId").val());
            }
        }else{
            //判断付款方式是否挂资金计划   1、票据模块配置开启挂接资金计划(控制层判断) 2、预算科目维护不为空  3、当前单位是否要挂预算
            $.ajax({
                type: 'POST',
                url: basePath+"/bill/accebill/abvoucherinfoVice/isBudget",
                data:{'corpId':corpId,'briefId':'001'},
                dataType:'json',
                async:false,
                success: function(result){
                    if(result.success){
                        if(payItem && result.attributes.isBudget){
                            isBudget = true;
                            $genOtherHtml(corpId,processDate,$("#vvid").val());
                        }else{
                            isBudget = false;
                            $("#tagItemDetail").html("");
                        }
                    }else{
                        isBudget = false;
                        $("#tagItemDetail").html("");
                    }
                }});
        }
    }

    //修改页面初始化挂预算参数
    $renderBudget($("#corpIdOld").val(),dateCheck.get("businessDate").val());
    //setVerify();
    //保证金显示初始化
    initBailShow();
    //保证金参数设置
    bailParameters();
    ratInitLoadInfo();
    if(isBatchBill=='1'){
        uploader = bt.btUpload.init({
            elem: "#uploader",
            auto: false,
            gid: ''
        });
    }
    //getBankbillConfig($("#bifCode").val());

    newStmIssuofpprIndChange({value:dicts.get("AB_BILL_NEW_STM_ISSUOFPPR_IND").val()});
    getLimitInfo();

    GuaInfoMethod();

    // if($("#accptncagrmtBranch").val() && $("#accptncagrmtBranch").val().indexOf('招商银行')>-1 ){
    //     var isOnline = $('#isOnline').val();
    //     if(isOnline == '1'){
    //         $(".zhaohangclsr").attr("lay-verify", "strlength|required");
    //     }else {
    //         $(".zhaohangclsr").attr("lay-verify", "strlength");
    //     }
    // }
    zhaohangRatFun();
    if($("#cvmLimitNo").val()){
        $(".zhaohangcls").show();
    }
});

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
    // $("#endDate").val(bt.date.format(bt.date.addDay(AddMouth(date,month),-1)));
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
    }
}

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;

    if(data.socCode != undefined){
        $("#blRmtrUnnIdNo").val(data.socCode);

        if($('input:radio[name="billType"]:checked').val()!="10"){
            $("#blAcptrUnnIdNo").val(data.socCode);
        }else {
            $("#blAcptrUnnIdNo").val('');
        }

    }
//设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data.id);
    //初始化票据单位和业务单位
    initCorp(data.id);

    if($('input:radio[name="billFlag"]:checked').val()=="10" && isPayFlg != "1"){
        $("#payer").val(data.corpName);//出票人名称
        if($('input:radio[name="billType"]:checked').val()=="11"){
            $("#acceptoracc").val("");//承兌人账号
            $("#acceptor").val(data.corpName);//承兌人
            $("#acceptorbankno").val("");//承兑人开户行行号
            $("#acceptorBankName").val("");//承兑人开户行名称
        }
    }
    if(isPayFlg != "1"){
        $("#payerBankName").val("");
        $("#payerbankno").val("");
    }
    //通过单位id加载出票人银行账号
    bt.accountTable.get("payeracc").initCorp(data.id,"1");
    if(isPayFlg == "1"){
        $("#payeracc").val($("#payeracchidden").val())
    }
    //资金计划
    $renderBudget(data.id,dateCheck.get("businessDate").val());
    if($("#isRelateRat").val()==true){
        setRationBtn();
    }
    //保证金参数设置
    bailParameters();

    // 执行回调方法
    setVerify();

    reselectBear();

    //授信改造
    ratVerify();
    getLimitInfo();
    isPayFlg = "0";
}

//开票单位回调函数
function corpCallback(data){
    if(data.length==0)return;
    var $ = bt.jquery,form = bt.form();
    if($('input:radio[name="billFlag"]:checked').val()=="10"){
        $("#payer").val(data.corpName);//出票人名称
        if($('input:radio[name="billType"]:checked').val()=="11"){
            $("#acceptoracc").val("");//承兌人账号
            $("#acceptor").val(data.corpName);//承兌人
            $("#acceptorbankno").val("");//承兑人开户行行号
            $("#acceptorBankName").val("");//承兑人开户行名称
        }
    }
    $("#payerBankName").val("");
    $("#payerbankno").val("");
    //通过单位id加载出票人银行账号
    bt.accountTable.get("payeracc").initCorp(data.id,"1");
    //资金计划
    $renderBudget(data.id,dateCheck.get("businessDate").val());
    if($("#isRelateRat").val()==true){
        setRationBtn();
    }
    //保证金参数设置
    bailParameters();

    // 执行回调方法
    setVerify();

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
        $("#payeeCorpIdOld").val(data.id);
        bt.accountTable.get("payeeacc1").reload({urldata:{corpId:data.id,electricBill:"1",permission:"",ignorePermission:"true"}});
    }
    $("#payeeBankName").val("");//收票人开户行名称
    $("#payeebankno").val("")//收票人开户行行号
}

//选择收票人账户回调方法
function onChangePayeeacc(data){
    var $ = bt.jquery;
    $("#payeeAccName").val(data.accName);//【出票人以户名为准】
    $("#payeeacc1").val(data.bankAcc);
    $("#payeeacc").val(data.bankAcc);
    $("#payeeBankName").val(data.bankName);//开户行名称
    $("#payeebankno").val(data.bankCode);//开户行行号 bank_code 222333
}

//选择出票人账户回调方法
function accountPayeeCallBack(data){
    var $ = bt.jquery;
    $("#payer").val(data.accName);//【出票人以户名为准】
    $("#payeracc").val(data.bankAcc);
    $("#payerBankName").val(data.bankName);
    $("#payerbankno").val(data.bankCode);
    //挂接保证金 招行帐号显示招行保证金列表进行账号选择
    if(isBail && ("招商银行" == data.bankTypeName || data.bankName.includes("招商银行"))){
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
            cmbBailAccountInfo.set({"bankType":"Ms"});
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
        //票据类型不为银票票时，承兑人默认为出票人
        $("#acceptor").val($("#payer").val());//承兑人
        $("#acceptoracc").val($("#payeracc").val());//承兑人账号
        $("#acceptorbankno").val($("#payerbankno").val());//承兑人开户行号
        $("#acceptorBankName").val($("#payerBankName").val());//承兑人开户行名称
        $("#acceptorCorpId").val(data.corpId);//承兑人单位id
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

//承兑人账号回调方法
function accountAcceptorCallBack(data){
    $("#acceptoraccCorp").val(data.bankAcc);//承兑人账号
    $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
    $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
}

//日期检查
function checkDate(val){
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

//打回
function repulse(){
    var reason=$("#reason").val();
    if(isEmpty(reason)){
        //打回原因必填
        layer.alert(bt.lang('common.message.repulseRequired'), {icon: 7});
        return;
    }
    var action = "/bill/accebill/abemitbill/repulse";
    $.post(basePath + action,$('#inputForm').serialize(),function(result){
        parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        if(result.success){
            if(!bt.navtab){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
        }
    },'JSON');
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
    if(feemoneyScale == null || feemoneyScale== undefined || feemoneyScale=="" || billMoney == null || billMoney== undefined || billMoney==""){
        return false;
    }
    var feemoney = bt.moneyType.getFun().toNumber(billMoney);
    feemoney = bt.moneyType.getFun().mul(feemoney,bt.moneyType.getFun().toNumber(feemoneyScale),2);;
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
    getFeemoneyScale();
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
        billMoney = bt.moneyType.getFun().mul(billMoney,bailpercent,2)/1000000;
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
        if(bailpercent>1){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
//        bailpercent = bailpercent.toFixed(4);//四舍五入保留四位小数
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
        var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
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

function bailTypeCallback() {
    $("#bailType").val(dicts.get("bailType").val());
}

/**控制保证金额度单选择显示或隐藏*/
function isBailOp(show){
    var $ = bt.jquery,form = bt.form();
    isBail=$("#isBail").val();
    if((isBail=="true" || isBail==true) && show){
        $('input[type=radio][name="bailmodel"][value="1"]').prop("checked",'true');
        $(".isBailDiv").show();
    }else{
        $(".isBailDiv").hide();
        $('input[type=radio][name="bailmodel"][value="0"]').prop("checked",'true');
    }
    isBailSelectShow();
    bt.form().render();
}
/**根据是否挂保证金控制保证金额度单选择显示或隐藏*/
function isBailSelectShow(){
    if($('input:radio[name="bailmodel"]:checked').val()=="0"){
        $("#bailAcc").removeAttr("lay-verify","required");//移除保证金账户必填
        $("#bailAcc").removeAttr("readonly");//移除保证金账户只读
        dicts.get("bailType").attr({
            "data-disabled": false //保证金类型可手动修改
        });
        $('[data-name="bailmoney"]').removeAttr("lay-verify","required");//移除保证金必填
        $('[data-name="bailpercent"]').removeAttr("lay-verify","required");//移除保证金必填
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
    $("#bailContract").val("");//清除保证金合同
    $("#bailAcc").val("");//清除保证金账户
    $("#bondId").val("");//清除保证金id
    bt.form().renderIcon();
}

/**保证金显示初始化*/
function initBailShow(){
    //判断保证金加载信息
    if((isBail=="true"||isBail==true) && $("#billType").val()=="10"){
        if(isBailModel=="1"){
            moneyType.get('bailmoney').attr("lay-verify","required");//添加保证金必填
            $("#bailAcc").attr("readonly","true");//添加保证金账户只读
            $("#bailAcc").attr("lay-verify","required");//添加保证金必填
            $("#fcBondQuotasTxtDiv").show();
            $("#fcBondQuotasValueDiv").show();
            $(".isBailDiv").show();
        }else if(isBailModel=="0"){
            moneyType.get('bailmoney').removeAttr("lay-verify","required");
            dicts.get("isBailModel").attr({ 'value':0});
            $("#bailAcc").removeAttr("lay-verify","required");
            $("#bailAcc").removeAttr("readonly");
            $("#fcBondQuotasTxtDiv").hide();
            $("#fcBondQuotasValueDiv").hide();
        }else{
            moneyType.get('bailmoney').removeAttr("lay-verify","required");
            $("#bailAcc").removeAttr("lay-verify","required");
            $("#bailAcc").removeAttr("readonly");
            $("#fcBondQuotasTxtDiv").hide();
            $("#fcBondQuotasValueDiv").hide();
            $(".isBailDiv").hide();
        }
    }else{
        moneyType.get('bailmoney').removeAttr("lay-verify","required");
        $("#bailAcc").removeAttr("lay-verify","required");
        $("#bailAcc").removeAttr("readonly");
        $("#fcBondQuotasTxtDiv").hide();
        $("#fcBondQuotasValueDiv").hide();
        $(".isBailDiv").hide();
    }
    bt.form().renderIcon();
}
var setTimeBailNum=0;
//保证金参数传递
function bailParameters(){
    //保证金参数
    if($("#source").val()==10){
        parameters.corpId=$("#corpId").val();
    }else{
        try{
            if(bt.corp.get("corpId").val()==""){
                if(setTimeBailNum<4){
                    setTimeout("bailParameters()",2000);
                }
                setTimeBailNum++;
            }else{
                parameters.corpId=bt.corp.get("corpId").val();
            }
        }catch(err){
            if(setTimeoutNum<4){
                setTimeout("bailParameters()",2000);
            }
            setTimeoutNum++;
        }
    }
//    parameters.bankCode=$("#acceptorbankno").val();
    parameters.curId=$("#curId").val();
    parameters.bankCode=$("#acceptorbankno").val();
    parameters.startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    parameters.endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    parameters.bailBankControlType = $("#bailBankControlType").val();//保证金银行控制类型
    bt.bondAccInfo.set(parameters);
}

//保证金额度单回调
function bondAccinfoCallback(data,dom) {
    var bailmoney = moneyType.get('bailmoney').val();//保证金
    //保证金可用余额
    availMoney = data.availMoney;
    $("#bondId").val(data.bondId);
    $("#bailAcc").val(data.bankAcc);
    $("#cmbBailFlag").val("");//招行保证金列表选择标识
    rate.get("bailrate").val(data.bondRate);//保证金利率
    $("#bailType").val(data.bailType);//保证金类型
    dicts.get("bailType").val(data.bailType);//保证金类型
    //判断保证金是否填写
    if(bailmoney>0){
        //判断保证金是否大于保证金可有余额
        if(bailmoney>availMoney){
            //提示保证金金额不能大于保证金可用余额
            parent.layer.msg(bt.lang("accebill.message.bailmoney.maxAccAvailMoney"), {icon: 2});
        }
    }
}

//查看保证金额度单
function showBail(){
    var vvid=$("#vvid").val();
    var appNo = $("#appNo").val();
    viewFcBondApplyInfo(vvid,appNo);
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
        $("#relateCpRatDiv").show();
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

        if(isBail && mouthRat=="true" && ($('input:radio[name="bailmodel"]:checked').val()=="1") ){
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
        if(mouthRat=="true" && dicts.get('isOpenRat').val()=="1"){
            if(moneyType.get('bailmoney').val()==""){
                parent.layer.msg(bt.lang("bt.bill.message.bondmoney.not.null"), {icon: 2});
                return false;
            }
        }

    }
    // 内部授信判断
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
        /* //银票类型
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
        //商票或第三方票据
        /*if($('input:radio[name="billType"]:checked').val()!="10"){
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
    var beginDate = dateCheck.get("startDate").val();
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
/**判断字符串是否为空*/
function isEmpty(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == ""){
        return true;
    }
    return false;
}

var setTimeoutNum=0;
var origNoFlag = 0;
/**根据选择的业务设置必填标志
 * 电票合同号账户需要必填，纸票账户信息可以不必填。对内自开单位必填**/
function setVerify() {
    var isOnline = $('#isOnline').val();
    var isEmitMake = $('#isEmitMake').val();
    var billType=dicts.get("billType").val();
    var billFlag = $('input:radio[name="billFlag"]:checked').val();
    var insideSing=$('input:radio[name="insideSing"]:checked').val();
    $("#origNo").attr("lay-verify","strlength");
    if (billFlag == '11' && isNotBillTypeTP(billType)) {
        //电票
        if($("#payerbankno").val() && $("#payerbankno").val().indexOf("303")==0){
            $("#aContractNo").attr("lay-verify","strlength|required");
        }else{
            $("#aContractNo").attr("lay-verify","strlength");
        }
        $("#payeracc").attr("lay-verify","required");
        $("#acceptoracc").attr("lay-verify","required");
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
        // 是否开启补录  false -- 不开
        if(isEmitMake=='false'){
            $("#payeebankno").attr("lay-verify","required|strlength");
            $("#payeeBankName").attr("lay-verify","required|strlength");
            $("#acceptorbankno").attr("lay-verify","required|strlength");
            $("#acceptorBankName").attr("lay-verify","required|strlength");
            //是否在线
            if(isOnline!='1'){
                $("#origNo").attr("lay-verify","required|strlength");
            }
        }
    } else {
        //纸票
        $("#pledgeeAccSpanDiv").hide();//收票人账号必填符号
        $("#aContractNo").attr("lay-verify", "strlength");
        $("#payeracc").removeAttr("lay-verify","");
        $("#payeeacc").removeAttr("lay-verify","");
        $("#acceptor").attr("lay-verify","strlength");
        $("#acceptoracc").attr("lay-verify","");
        $("#origNo").attr("lay-verify","strlength");
        if($("#source").val()!=10){
            try{
                if(corp.get("corpId").data()[0].length!=0){
                    $("#payer").val(corp.get("corpId").data()[0].corpName);
                }
            }catch(err){
                if(setTimeoutNum<4){
                    setTimeout("setVerify()",2000);
                }
                setTimeoutNum++;
            }
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
        }else {
            $("#origNo").attr("lay-verify","strlength");
        }
    }

    /*if (origNoFlag == 0){
        origNoFlag = 1;
        if (isOnline =='0' && isEmitMake=='false'){
            $("#origNo").attr("lay-verify","required|strlength");
        }else {
            $("#origNo").attr("lay-verify","strlength");
        }
    }*/
    //如果开新票，但是业务银行不支持新票据功能，票据号必填
    if(bt.dicts.init().get("AB_BILL_NEW_STM_ISSUOFPPR_IND").val() == "1" && isEmitMake=='false'){
        if(($("#newBillFlg").val() == null || $("#newBillFlg").val() == undefined || $("#newBillFlg").val() != 1) && !isnewBillFlg){
            $("#origNo").attr("lay-verify","required|strlength");
        }
    }
    bt.form().renderIcon();
}

//提示未选择单位
function checkCorp(){
    bailParameters();
    var corpIds;
    if($("#source").val()=="10"){
        corpIds = $("#corpId").val();
        if(!corpIds){
            corpIds = bt.corp.init().get("corpId").val();
        }
    }else{
        corpIds = bt.corp.init().get("corpId").val();
    }
    if(bt.empty(corpIds)){
        layer.alert(bt.lang("table.fcBond.corp.null"),{icon:2});
        return false;
    }else{
        return true;
    }
}


//编辑界面加载判断是否启用授信
function ratInitLoadInfo(){
    if($('input:radio[name="billType"]:checked').val()=="10" && isRelateRat){
        $("#relateRatDiv").show();
        if(isRelateRat && $('input:radio[name="ratmodel"]:checked').val()=="0"){
            $("#rationSelectDIV").hide();
            $("#sureRatmodelTxt").hide();
            $("#sureRatmodelValue").hide();
            $("#scHideTxt").show();
            $("#scHideValue").show();
        }else{
            if(mouthRat=="true"){
                $("#sureRatmodelTxt").show();
                $("#sureRatmodelValue").show();
                $("#scHideTxt").hide();
                $("#scHideValue").hide();
            }
            $("#rationSelectDIV").show();
        }
    }
    bt.form().render();
    ratInfoExtSet();
    oneTimeFlg=false;
}


function ratModelCut(){
    if(!oneTimeFlg){
        $("#bankTypeName").val("");
        $("#accptncagrmtBranch").val("");
        $("#accptncagrmtBranchNo").val("");
        $("#accptncagrmtnb").val("");
        $("#cooperativeBankNo").val("");
        $("#cvmLimitNo").val("");
        $("#riskContractNumber").val("");
    }
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

        //$(".zhaohangclsr").val("");
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
    if($("#isRelateRat").val()==true){
        setRationBtn();
    }
    bt.form().renderIcon();
    ratInfoExtSet();
    ratVerify();
}

function setOtherParam(param){
    var corpId;
    if($("#source").val()=="10"){
        corpId = $("#corpId").val();
    }else{
        corpId = bt.corp.init().get("corpId").val();
    }
    var curId = $("#curId").val();//币别
    var startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    var endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}


function selectBefore(){
    var corpId;
    if($("#source").val()=="10"){
        corpId = $("#corpId").val();
    }else{
        corpId = bt.corp.init().get("corpId").val();
    }
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
            dicts.get("isHangGuarantee").attr({'data-defaultvalue':0});
            $("#guaContractCodes").removeAttr("lay-verify");
            $(".isHangGua").hide();
            $("#guaBtn").hide();
            resetGuaMsg();
            form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
        } else {
            $(".isHangGua").show();
            $("#guaContractCodes").attr("lay-verify", "required");
            $("#isHangGuarantee").val("1");
            dicts.get("isHangGuarantee").attr({'data-defaultvalue':1});
            form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        }
    }

    if(params && params.bankTypeName.indexOf('招商银行')>-1 ){
        $("#bankTypeName").val(params.bankTypeName);
        $("#accptncagrmtBranch").val(params.bankName);
        $("#accptncagrmtBranchNo").val(params.bankCode);
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
            // $(".zhaohangclsr").attr("lay-verify", "strlength");
            $(".zhaohangclsr").val("");
        }
    }else {
        if(!oneTimeFlg){
            $("#bankTypeName").val("");
            $("#accptncagrmtBranch").val("");
            $("#accptncagrmtBranchNo").val("");
            // $(".zhaohangclsr").attr("lay-verify", "strlength");
            $(".zhaohangclsr").val("");
        }
    }
    if(!oneTimeFlg){
        $("#cvmLimitNo").val(params ? params.contractNo : "");
        if($("#cvmLimitNo").val()){
            $(".zhaohangcls").show();
        }else{
            $(".zhaohangcls").hide();
        }
    }

    if(params && params.bankTypeName.indexOf('汇丰银行')>-1 ){
        dicts.get('isOpenRat').val("0");
    }
}


//商票且是保贴则显示银行授信挂接
function resetBankDiv(){
    if(isRelateRat){
        var emitGuandisSign = $('input:radio[name="emitGuandisSign"]:checked').val();
        if(emitGuandisSign=="1" && isRelateRat){
            dicts.get("ratmodel").attr({'data-defaultvalue':1});
            $("#relateRatDiv").show();
        }else{
            dicts.get("ratmodel").attr({'data-defaultvalue':0});
            $("#relateRatDiv").hide();
        }
        if(isOpenCpRat==true){
            cpRatModelBack();
        }
        ratModelCut();
        bt.form().render();
    }
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
        $('input[type=radio][name="isHangCredit"][value="0"]').prop("checked",true);
        $("#emitGuandisSign").hide();
        $("#emitGuandisSignText").hide();
        $("#relateCpRatDiv").hide();
        $('[data-biz-name="cpRatItemCode"]').find('select').removeAttr('lay-verify');
//		$('input[type=radio][name="emitGuandisSign"][value="0"]').prop("checked",'true');
    }
    bt.form().render();
}

var loadRatMenu = function(flag){
    //商票显示
    if(flag){
        $("#emitGuandisSign").show();
        $("#emitGuandisSignText").show();
        // $('input[type=radio][name="emitGuandisSign"][value="0"]').prop("checked",true);
        if(dicts.get('emitGuandisSign').val()=="1"){
            $("#relateRatDiv").show();
            $("#relateCpRatDiv").show();
        }else{
            $("#relateCpRatDiv").show();
        }
    }else{
        $("#emitGuandisSign").hide();
        $("#emitGuandisSignText").hide();
    }


//	if($('input:radio[name="isHangCredit"]:checked').val()=="0"){
//
//	}else{
//
//	}
    if(isOpenCpRat==true){
        cpRatModelBack();
    }
    bt.form().render();
}


//商票且是保贴则显示银行授信挂接
var ratmodelHander = function(){
    ratModelCut();
    bt.form().render();
}


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

        }
    }
    return true;

}
var billFlagVal = "11";
var firstFlg = true;
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
        dicts.get("blgrdbagcrclPrmtInd").val("0");
    }
    if(!firstFlg){
        firstFlg = false;
        billFlagCallback();
    }
    setVerify();
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
        //$("#payer").val("");
    }else{
        $("#payerBankName").removeAttr("readonly");
        $("#payerbankno").removeAttr("readonly");
        //$("#payeeBankName").removeAttr("readonly");
        //$("#payeebankno").removeAttr("readonly");
        $("#acceptorBankName").removeAttr("readonly");
        $("#acceptorbankno").removeAttr("readonly");
    }
    //出票人、收票人、承兑人重置
    /*$("#payeracc").val("");
    $("#payerBankName").val("");
    $("#payerbankno").val("");
    $("#payeeacc1").val("");
    $("#payeeacc").val("");
    //$("#payeeAccName").val("");
    $("#payeeBankName").val("");
    $("#payeebankno").val("");*/
    $("#acceptoracc").val("");
    $("#acceptor").val("");
    $("#acceptorBankName").val("");
    $("#acceptorbankno").val("");
    $("#acceptorCorpId").val("");
    //setVerify();
}


var setTimeoutNum=0;

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
 * 内部授信选择回调
 */
function cpRatModelBack(){
    var $ = bt.jquery,form = bt.form();
    //显示
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
            //付款联动过来的票据所属单位默认取corpId
            if($("#source").val()==10){
                $("#ratBankCorpId").val($("#applyCorpIdInput").val());
            }else{
                //当首次加载时，取隐藏域的所属单位id
                if(bt.corp.get("applyCorpId").val()==""){
                    //银行授信单位Id
                    $("#ratBankCorpId").val($("#applyCorpIdOld").val());
                }else{
                    $("#ratBankCorpId").val(bt.corp.get("applyCorpId").val());
                }
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
        if(isPayFlg != '1'){
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
    var corpId;
    if($("#source").val()==10){
        corpId=$("#corpId").val();
    }else{
        try{
            if(bt.corp.get("corpId").val()==""){
                if(setTimeBailNum<4){
                    setTimeout("getLimitInfo()",2000);
                }
                setTimeBailNum++;
            }else{
                corpId=bt.corp.get("corpId").val();
            }
        }catch(err){
            if(setTimeoutNum<4){
                setTimeout("getLimitInfo()",2000);
            }
            setTimeoutNum++;
        }
    }
    if($("#isBillPool").val()){
        var emitType=dicts.get("emitType").val();
        var bankAcc=$("#payeracc").val();
        if(corpId==""){
            corpId=$("#corpIdOld").val();
        }
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

//初始化票据单位和业务单位
function initCorp(id){
    if(id==undefined || id==null || id==""){
        return
    }
    //bt.corp.get("corpId").val(id);
    //bt.corp.get("busCorpId").val(id);
}



function GuaInfoMethod(){
    if("1" == dicts.get("isHangGuarantee").val()){
        $(".isHangGua").show();
        $("#guaContractCodes").attr("lay-verify", "required");
    } else {
        $("#guaContractCodes").removeAttr("lay-verify");
        $(".isHangGua").hide();
        $("#guaBtn").hide();
        //resetGuaMsg();
    }
    bt.form().renderIcon();
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
    rate.get("bailrate").val(data.rate);//保证金利率
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