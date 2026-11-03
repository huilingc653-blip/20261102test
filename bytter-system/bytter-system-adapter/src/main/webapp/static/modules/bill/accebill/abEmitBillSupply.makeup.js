var dateCheck,isBudget,rate,moneyType,ratio,bondAccInit,parameters,isBailModel,availMoney,selectTypes,form,isBail,mouthRat,isRelateRat,isBear,dicts;
bt.use(['form','select','moneyType','capNumber','btdate','jquery','element','selectType','dateType','externalCorpBankAcc','bankInfo','rateType','accountTable','bank','dicts','ratioType','bondAccInfo'], function(){
    var $ = bt.jquery;
    form = bt.form();
    dicts = bt.dicts.init();
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }
    //挂授信标识
    isRelateRat=$("#isRelateRat").val()=="true"?true:false;
    //挂内部授信标识
    isOpenCpRat=$("#isOpenCpRat").val()=="true"?true:false;
    //挂保证金标识
    isBailModel=$("#isBailModel").val();
    //是否挂担保
    isBear=$("#isGuarantee").val();
    //挂保证金标识
    isBail=$("#isBail").val()=="true"?true:false;
    //授信敞口标识
    mouthRat=$("#mouthRat").val();
    //保证金账号的初始化
    bondAccInit = bt.bondAccInfo.init();
    dateCheck = bt.dateType.init();
    moneyType = bt.moneyType.init();
    //保证金参数
    parameters=new Object();
    //bt.externalCorpBankAcc.init();
    bt.bankInfo.init();
    rate = bt.rateType.init();
    ratio = bt.ratioType.init();
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    if(($("#billFlag").val()=="10" || !isNotBillTypeTP($("#billType").val())) && $("#insideSing").val()=="1"){
        //通过收票人单位id加载收票人银行账号
        bt.accountTable.get("payeeacc").initCorp($("#payeeCorpId").val(),"1");
        $("#payeeacc").val($("#payeeaccOld").val());
    }
    bt.bank.init({
        elem:'.bank'
    })

    //加载保证金参数
    if($("#isBail").val()=="true"){
        bailParameters();
    }

    if($("#billType").val()=='10'){//流通介质：银票
        loadRatMenu(false);
    }else{
        loadRatMenu(true);
    }


    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        externalCorpBankAcc.set({"corpId": $("#corpId").val(),"type":4});
        return true;
    }

    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    var billType=dicts.get("billType").val();
    if($('#billFlag').val()=="10" || $('#isOnline').val()!="1" || $("#origNo").val() ||($('#sendSign').val() !="1"&&$('#newStmIssuofpprInd').val() =="1") || isNotBillTypeTP(billType) ){//纸票或离线电票或第三方票据类型
        $("#origNo").removeAttr("readonly");
        $("#payeracc").removeAttr("readonly");
        $("#acceptor").removeAttr("readonly");
        $("#acceptorbankno").removeAttr("readonly");
        $("#acceptoracc").removeAttr("readonly");
        $("#acceptorBankName").removeAttr("readonly");
        $("#acceptorbankaddress").removeAttr("readonly");
        $("#aContractNo").removeAttr("readonly");
        $("#accptncagrmtnb").removeAttr("readonly");
        //通过单位id加载出票人银行账号
        if( $('[name=payeracc]').attr('readonly')!='readonly') {
            bt.accountTable.get("payeracc").initCorp($("#corpId").val(), "1");
            bt.accountTable.get("payeracc").val($("#payeraccOld").val());
            //$("#payeracc").val($("#payeraccOld").val());
        }
    }else{
        //票据号码、出票人账号、出票日、到期日、承兑人 、承兑人开户行行号、承兑人账号、承兑人开户行名称、 承兑人开户行地址、承兑合同号、承兑协议编号  只读
        $("#origNo").attr("readonly","readonly");
        $("#payeracc").attr("readonly","readonly");
        $("#acceptor").attr("readonly","readonly");
        $("#acceptorbankno").attr("readonly","readonly");
        $("#acceptoracc").attr("readonly","readonly");
        $("#acceptorBankName").attr("readonly","readonly");
        $("#acceptorbankaddress").attr("readonly","readonly");
        $("#aContractNo").attr("readonly","readonly");
        $("#accptncagrmtnb").attr("readonly","readonly");
    }
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            required:true,
            istime: false
        });
    });

    var payItem = $("#payItem").val();//预算科目
    //监听补录提交
    form.on('submit(makeup)', function(data){
        bt.loading.show();
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

        //保证金判断
        if(!checkBear()){
            bt.loading.hide();
            return false;
        }

        //保证金判断
        if(!formSubmitCpCheckRat()){
            bt.loading.hide();
            return false;
        }

        var action = "/bill/accebill/abEmitBillSupply/makeupandstorage";
        $.post(basePath + action,$('#inputForm').serialize(),function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
            }
        },'JSON');
        return false;
    });
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
            $("#acceptoraccCorpDiv").hide();
            $("#acceptoraccCorp").attr("lay-verify","strlength");
            //$("#acceptoracc").attr("lay-verify","strlength|required");
        }else{
            $("#acceptoracc").val("");//承兑人账号
            $("#acceptorbankno").val("");//承兑人开户行号
            $("#acceptorBankName").val("");//承兑人开户行名称
            $("#acceptoracc").removeAttr("name");
            $("#acceptoracc").attr("type","hidden");
            //$("#acceptoracc").attr("lay-verify","strlength");
            $("#acceptoraccCorp").attr("name","acceptoracc");
            $("#acceptoraccCorp").attr("type","text");
            $("#acceptoraccCorpDiv").show();
            if($('#billFlag').val()=="10"){
                $("#acceptoraccCorp").attr("lay-verify","strlength");
            }else{
                $("#acceptoraccCorp").attr("lay-verify","strlength|required");
            }
            bt.accountTable.get("acceptoraccCorp").initCorp(data.id,"1");
        }
        $("#acceptor").val(data.name);//承兑人
        $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
        $("#acceptProvince").val(data.accProvince);//承兑人客商所在省
        $("#acceptCity").val(data.accCity);//承兑人客商所在市
        $("#acceptBankTypeId").val(data.bankType);//承兑人客商银行类别id
        //当选择的是客商时，则内部授信单位类型只能选择开票人，置灰承兑人
        if(data.corpId){
            dicts.get("ratBankCorpType").attr({"data-disabledValue": ""});
            $("#acceptorCorpId").val(data.corpId);
        }else{
            dicts.get("ratBankCorpType").attr({"data-disabledValue": "1"});
            $("#acceptorCorpId").val("");
        }
        //授信设置
        ratVerify();
    }
    //选择银行回调函数
    bt.bailBankInfoCallback = function(data,dom){
        $("#acceptor").val(data.bankName);//承兑人
        $("#acceptoracc").val("0");//承兑人账号--银行默认为0
        $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
        $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
        $("#acceptorbankaddress").val("");//承兑人开户行地址--银行组件里面没有，可手动填写
        $("#acceptoraccCorp").attr("lay-verify","strlength");
        //保证金参数设置
        bailParameters();
    }
    //选择银行回调函数
    bt.bailBankInfoCallbackAccept = function(data,dom){
        $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
        $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
        $("#acceptProvince").val(data.prov);//承兑人客商所在省
        $("#acceptCity").val(data.city);//承兑人客商所在市
        $("#acceptBankTypeId").val(data.bankTypeId);//承兑人客商银行类别id
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
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:true, success: function (json) {
                $("#fbsbudgetprocessDiv").show();
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };

    /**
     * 资金计划渲染
     */
    $renderBudget = function(corpId,processDate){
        if($("#voucherId").val()){
            //判断付款单是否有挂预算
            if($("#nisBillheadNoBudget").val()){
                isBudget = false;
                $("#tagItemDetail").html("");
            }else{
                isBudget = true;
                $genOtherHtml(corpId,processDate,$("#voucherId").val());
            }
        }else{
            //判断付款方式是否挂资金计划   1、票据模块配置开启挂接资金计划(控制层判断) 2、预算科目维护不为空  4、当前单位是否要挂预算
            $.ajax({
                type: 'POST',
                url: basePath+"/bill/accebill/abvoucherinfoVice/isBudget",
                data:{'corpId':corpId,'briefId':'001'},
                dataType:'json',
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
                        //parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                    }
                }});
        }
    }
    //修改页面初始化挂预算参数
    $renderBudget($("#corpId").val(),dateCheck.get("businessDate").val());
    relateRatOp($("#billType").val()=='10');
//    callbackBailmodel();
    ratInitLoadInfo();
});

//选择出票人账户回调方法
function accountPayerCallBack(data){
    var $ = bt.jquery;
    $("#payer").val(data.accName);//【出票人以户名为准】
    $("#payeracc").val(data.bankAcc);
    $("#payerBankName").val(data.bankName);
    $("#payerbankno").val(data.bankCode);
    if($("#billType").val()!="10"){
        $("#acceptorCorpId").val(data.corpId);//承兑人单位id
    }
    if($("#billType").val()=="10"){
        $("#acceptorCorpId").val("");//承兑人单位id
    }
    //授信设置
    ratVerify();
}

//选择收票人账户回调方法
function onChangePayeeacc(data){
    var $ = bt.jquery;
    $("#payeeAccName").val(data.accName);//【出票人以户名为准】
    $("#payeeacc").val(data.bankAcc);
    $("#payeeBankName").val(data.bankName);//开户行名称
    $("#payeebankno").val(data.bankCode);//开户行行号 bank_code 222333

}

//承兑人账号回调方法
function accountAcceptorCallBack(data){
    $("#acceptoraccCorp").val(data.bankAcc);//承兑人账号
    $("#acceptorbankno").val(data.bankCode);//承兑人开户行号
    $("#acceptorBankName").val(data.bankName);//承兑人开户行名称
}

//业务办理日期回调
function businessDateCallback(val){
        $renderBudget($("#corpId").val(),val);
}

//日期检查
function checkDate(val){
    var $ = bt.jquery;
    var startDate = dateCheck.get("startDate").val();
    var endDate = dateCheck.get("endDate").val();
    var date= new Date(Date.parse(startDate.replace(/-/g,"/"))); //转换成Data();
    //+半年后得到的值 与 endDate比较  如果小于 endDate 则提示纸票不得超过半年
    var year = date.getFullYear();//获取当前年份
    var month = date.getMonth()+1;//获取半年后的月份
    var day = date.getDate();//获取月份多少号
    if($("#billFlag").val()=="10"){//纸票
        //纸票加半年后计算
        month = month+6;
        if(month>12){
            month = month-12;
            year = year+1;
        }
        if(month<10){
            month = "0"+month;
        }
        if(day<10){
            day = "0"+day;
        }
        startDate = year+"-"+month+"-"+day;
        if(startDate<endDate){
            $("#endDate").val("");
            parent.layer.alert(bt.lang("bill.accebill.billFlag_10"));//纸票到期日不能超过出票日半年
        }
    }else{
        //电票加一年后计算
        year = year+1;
        if(month<10){
            month = "0"+month;
        }
        if(day<10){
            day = "0"+day;
        }
        startDate = year+"-"+month+"-"+day;
        if(startDate<endDate){
            $("#endDate").val("");
            parent.layer.alert(bt.lang("bill.accebill.billFlag_11"));//电票到期日不能超过出票日一年
        }
    }
    reselectBear();
}

//保证金比例计算保证金
function getBailmoney(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val());//票面金额
    if(billMoney>0){
        var bailpercent = ratio.get("bailpercent").val();//利率
        if(bailpercent>100){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
        billMoney = bt.moneyType.getFun().mul(billMoney,bailpercent,2);
        //billMoney = bt.moneyType.getFun().div(billMoney,100);
        moneyType.get('bailmoney').val(billMoney.toFixed(2));
    }
}
//保证金计算保证金比例
function getBailpercent(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val());//票面金额
    if(billMoney>0){
        var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
        var bailpercent = bt.moneyType.getFun().div(bailmoney,billMoney,4);
        if(bailpercent>1){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
//        bailpercent = bailpercent.toFixed(4);//四舍五入保留四位小数
        ratio.get("bailpercent").val(bailpercent);
    }
}

function bailTypeCallback() {
    $("#bailType").val(dicts.get("bailType").val());
}

/**控制银行授信显示和隐藏*/
function relateRatOp(show){
    if(isRelateRat&&show){
        $("#relateRatDiv").show();
        $(".isBailDiv").show();
    }else{
        $("#relateRatDiv").hide();
        $(".isBailDiv").hide();
    }
    if((isBail=="true"||isBail==true) && show ){
        $(".isBailDiv").show();
        $(".fcBondQuotasDiv").hide();//保证金额度单
        if(($("#isBailModel").val()=="1" || $("#isBailModel").val()==1)){
            $("#bailAcc").attr("lay-verify","required|strlength");//添加保证金账户必填
            $("#bailAcc").attr("readonly","readonly");//添加保证金账户必
            dicts.get("bailType").attr({
                "data-disabled": true //保证金类型不可手动修改
            });
            $('[data-name="bailmoney"]').attr("lay-verify","required");//添加保证金必填
            $(".fcBondQuotasDiv").show();//保证金额度单
            $("#bondAccBtDiv").show();//保证金额度单
        }
    }else{
        $(".isBailDiv").hide();
        // $("#bailContract").val("");//清除保证金合同
        // $("#bailAcc").val("");//清除保证金账户
        $("#bondId").val("");//清除保证金id
        $("#bailAcc").removeAttr("lay-verify","required");//移除保证金账户必填
        $("#bailAcc").removeAttr("readonly");//移除保证金账户只读
        dicts.get("bailType").attr({
            "data-disabled": false //保证金类型可手动修改
        });
        $(".fcBondQuotasDiv").hide();//保证金额度单
        $("#bondAccBtDiv").hide();//保证金额度单
        $('[data-name="bailmoney"]').removeAttr("lay-verify","required");//移除保证金必填
    }
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

        if(isBail && mouthRat=="true" &&  $('input:radio[name="bailmodel"]:checked').val()=="1" ){
            var zyMoney = Number($("#billMoney").val())-Number($("#bailmoney").val());
            if(Number(zyMoney)>Number(itemMoney)){
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
    var bankno;//授信银行行号
    var banknoMsg;//错误提醒
    //当选择授信单位是开票人时
    if(ratBankCorpType == "0"){
        if(bt.empty(corpId)){
            //请选择所属单位
            parent.layer.msg(bt.lang("accebill.message.corpid.isnotselect"), {icon: 2});
            return "";
        }
        //银票类型
        if($("#billType").val()=="10"){
            bankno = $("#acceptorbankno").val();//银行行号取承兑人银行行号
            banknoMsg="accebill.message.acceptorbankno.isnotselect";
            // 商票或第三方票据
        }else{
            bankno = $("#payerbankno").val();//银行行号取出票人银行行号
            banknoMsg="accebill.message.payerbankno.isnotselect";
        }
        //当选择授信单位是承兑人时
    }else{
        if(bt.empty(corpId)){
            //请选择承兑人单位
            parent.layer.msg(bt.lang("accebill.message.acceptorCorpid.isnotselect"), {icon: 2});
            return "";
        }
        //商票或第三方票据
        if($("#billType").val()!="10"){
            bankno = $("#acceptorbankno").val();//银行行号取承兑人银行行号
            banknoMsg="accebill.message.payerbankno.isnotselect";
        }
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


//保证金比例计算保证金
function getBailmoney(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val());//票面金额
    if(billMoney>0){
        var bailpercent = ratio.get("bailpercent").val();//利率
        if(bailpercent>100){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
        billMoney = bt.moneyType.getFun().mul(billMoney,bailpercent);
        moneyType.get('bailmoney').val(billMoney);
    }
}
//保证金计算保证金比例
function getBailpercent(){
    var $=bt.jquery;
    var billMoney = Number($("#billMoney").val());//票面金额
    if(billMoney>0){
        var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
        var bailpercent = bt.moneyType.getFun().div(bailmoney,billMoney,4);
        if(bailpercent>1){
            parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.bailpercent"));//保证金比例不能大于100%
            ratio.get("bailpercent").val("");
            moneyType.get('bailmoney').val(0);
            return;
        }
        ratio.get("bailpercent").val(bailpercent);
    }
}



//查看保证金额度单
function showBail(){
    var vvid=$("#vvid").val();
    var appNo = $("#appNo").val();
    viewFcBondApplyInfo(vvid,appNo);
}



//保证金额度单回调
function bondAccinfoCallback(data,dom) {
    var bailmoney = Number(moneyType.get('bailmoney').val());//保证金
    //保证金可用余额
    availMoney = data.availMoney;
    $("#bondId").val(data.bondId);
    $("#bailAcc").val(data.bankAcc);
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


//提示未选择单位
function checkCorp(){
    var corpIds = $("#corpId").val();
    if(bt.empty(corpIds)){
        layer.alert(bt.lang("table.fcBond.corp.null"),{icon:2});
        return false;
    }else{
        return true;
    }
}

//保证金参数传递
function bailParameters(){
    //保证金参数
    parameters.corpId=$("#corpId").val();
    parameters.bankCode=$("#acceptorbankno").val();
    parameters.curId=$("#curId").val();
    parameters.startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    parameters.endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    parameters.bailBankControlType = $("#bailBankControlType").val();//保证金银行控制类型
    bt.bondAccInfo.set(parameters);
}


//授信业务处理
function callbackRatmodel(){
    if($('[data-name="ratItemCode"]').length){
        selectTypes = bt.selectType.init();
        if($('input:radio[name="ratmodel"]:checked').val()=="0"){
            //选择授信为否
            selectTypes.get('ratItemCode').attr("data-disabled",true);
            $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
            $('[data-biz-name="billName"]').find('select').removeAttr('lay-verify');
            $("#rationSelectDIV").hide();
            $("#ratBankCorpTypeDiv").hide();
            $("#sureRatmodelTxt").hide();
            $("#sureRatmodelValue").hide();
            $("#hideTdRatModelTxt").show();
            $("#hideTdRatModelVue").show();
        }else{
            selectTypes.get('ratItemCode').attr("data-disabled",false);
            $("#rationSelectDIV").show();
            $("#ratBankCorpTypeDiv").show();
            if(mouthRat=="true"){
                $("#sureRatmodelTxt").show();
                $("#sureRatmodelValue").show();
            }
            $("#hideTdRatModelTxt").hide();
            $("#hideTdRatModelVue").hide();
            $('[data-biz-name="ratItemCode"]').find('select').attr("lay-verify","required");
            $("#scHideValue").show();
        }
        setRationBtn();
        form.render();
        ratInfoExtSet();
        ratVerify();
    }
    bt.form().renderIcon();

};


//保证金挂接
function  callbackBailmodel(){
    if(isBail=="true"||isBail==true){
        if($('input:radio[name="bailmodel"]:checked').val()=="0"){
            $("#bailContract").val("");//清除保证金合同
            $("#bailAcc").val("");//清除保证金账户
            $("#bondId").val("");//清除保证金id
            $("#bailAcc").removeAttr("lay-verify","required");//移除保证金账户必填
            $("#bailAcc").removeAttr("readonly");//移除保证金账户只读
            dicts.get("bailType").attr({
                "data-disabled": false //保证金类型可手动修改
            });
            $(".fcBondQuotasDiv").hide();//保证金额度单
            $("#bondAccBtDiv").hide();//保证金额度单
            $('[data-name="bailmoney"]').removeAttr("lay-verify","required");//移除保证金必填
        }else if($('input:radio[name="bailmodel"]:checked').val()=="1"){
            $("#bailAcc").attr("lay-verify","required|strlength");//添加保证金账户必填
            $("#bailAcc").attr("readonly","readonly");//添加保证金账户必
            dicts.get("bailType").attr({
                "data-disabled": true //保证金类型不可手动修改
            });
            $('[data-name="bailmoney"]').attr("lay-verify","required");//添加保证金必填
            $(".fcBondQuotasDiv").show();//保证金额度单
            $("#bondAccBtDiv").show();//保证金额度单
        }
        form.renderIcon();
    }else{
        $(".fcBondQuotasDiv").hide();//保证金额度单
    }
};

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


function ratInitLoadInfo(){
    if(($("#billType").val()=="10" || $('input:radio[name="emitGuandisSign"]:checked').val() == '1') && isRelateRat){
        $("#relateRatDiv").show();
        if(isRelateRat && $('input:radio[name="ratmodel"]:checked').val()=="0"){
            $("#rationSelectDIV").hide();
            $("#ratBankCorpTypeDiv").hide();
            $("#sureRatmodelTxt").hide();
            $("#sureRatmodelValue").hide();
            $("#scHideTxt").show();
            $("#scHideValue").show();
            $('[data-biz-name="ratItemCode"]').find('select').removeAttr('lay-verify');
        }else{
            if(mouthRat=="true"){
                $("#sureRatmodelTxt").show();
                $("#sureRatmodelValue").show();
                $("#scHideTxt").hide();
                $("#scHideValue").hide();
            }else if(mouthRat=="false"){
                $("#sureRatmodelTxt").hide();
                $("#sureRatmodelValue").hide();
                $("#scHideTxt").show();
                $("#scHideValue").show();
            }
            $('[data-biz-name="ratItemCode"]').find('select').attr("lay-verify","required");
            $("#rationSelectDIV").show();
            $("#ratBankCorpTypeDiv").show();
        }
    }
    form.render();
    ratInfoExtSet();
    ratVerify();
}



function setOtherParam(param){
    var corpId = $("#corpId").val();//网点id
    var curId = $("#curId").val();//币别
    var startDate = $.trim(dateCheck.get("startDate").val());//开始日期
    var endDate = $.trim(dateCheck.get("endDate").val());//结束日期
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}


function selectBefore(){
    var corpId = $("#corpId").val();
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
            dicts.get("isHangGuarantee").attr({ 'value':0});
            $("#guaContractCodes").removeAttr("lay-verify");
            $(".isHangGua").hide();
            $("#guaBtn").hide();
            resetGuaMsg();
            form.setReadonlyForRadio("#isHangGuaranteeDiv", true);
        } else if ("0" == ratIsHangGua){
            $(".isHangGua").show();
            $("#guaContractCodes").attr("lay-verify", "required");
            $("#isHangGuarantee").val("1");
            dicts.get("isHangGuarantee").attr({ 'value':1});
            form.setReadonlyForRadio("#isHangGuaranteeDiv", false);
        }
    }
}




//商票且是保贴则显示银行授信挂接
var resetBankDiv = function(){
    var emitGuandisSign = $('input:radio[name="emitGuandisSign"]:checked').val();
    if(emitGuandisSign=="1"){
        dicts.get("ratmodel").attr({'data-defaultvalue':1});
        $("#relateRatDiv").show();
        $("#relateCpRatDiv").show();
    }else{
        dicts.get("ratmodel").attr({'data-defaultvalue':0});
        $("#relateRatDiv").hide();
        $("#relateCpRatDiv").show();
    }
    if(isOpenCpRat==true){
        cpRatModelBack();
    }
    callbackRatmodel();
    bt.form().render();
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
        if(dicts.get('emitGuandisSign').val()=="1"){
            $("#relateRatDiv").show();
            $("#relateCpRatDiv").show();
        }else{
            $('input[type=radio][name="emitGuandisSign"][value="0"]').prop("checked",true);
            $("#relateCpRatDiv").show();
        }
    }else{
        $("#emitGuandisSign").hide();
        $("#emitGuandisSignText").hide();
    }

    if(isOpenCpRat==true){
        cpRatModelBack();
    }
    bt.form().render();
}


//商票且是保贴则显示银行授信挂接
var ratmodelHander = function(){
    callbackRatmodel();
    bt.form().render();
}
var formSubmitCpCheckRat = function(){
    var isOpenCpRat= $("#isOpenCpRat").val();
    var isRelateRat= $("#isRelateRat").val();
    if(isOpenCpRat=="true"){
        if(dicts.get('isHangCredit').val()=="1"){
            if($("#cpItemFlag").val()==""){
                parent.layer.msg(bt.lang("accebill.message.relateCpRat.isnotselect"), {icon: 2});
                return false;
            }

        }
    }
    return true;

}



/**
 * 授信额度选择
 * @param params
 */
function beforeCpRation(){
    var corpId = $("#corpId").val();
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

/**银行授信
 1)开票人：当纸票或第三方票据挂接授信时，如果被授信单位为开票人，则出票人信息中的出票人开户银行名称及出票人开户银行行号不可为空。
 2)	承兑人：当纸票或第三方票据挂接授信时，如果被授信单位为承兑人，则承兑人信息中的承兑人开户银行名称及承兑人开户银行行号不可为空
 */
function ratVerify(){
    var billFlag = $("#billFlag").val();
    var billType = $("#billType").val();
    var ratmodelVal = $('input:radio[name="ratmodel"]:checked').val();
    var ratBankCorpType = $('input:radio[name="ratBankCorpType"]:checked').val();
    //挂银行授信
    if(isRelateRat && ratmodelVal=="1") {
        //当选择授信单位是开票人时
        if (ratBankCorpType == "0") {
            //银行授信单位Id
            $("#ratBankCorpId").val($("#corpId").val());
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
        $("#ratBankCorpId").val("");
        $("#payerBankName").attr("lay-verify", "strlength");
        $("#payerbankno").attr("lay-verify", "strlength");
        $("#acceptorBankName").attr("lay-verify", "strlength");
        $("#acceptorbankno").attr("lay-verify", "strlength");
    }

    bt.form().renderIcon();
}
