bt.use(['form','layedit','select','moneyType','selectType','dateType','corp','rateType','dateForm','selectTable'], function(){
    var $ = bt.jquery,form = bt.form();
    loadAagFcAccHtmlFlag++
    if(loadAagFcAccHtmlFlag > 1){
        new $.fn.dataTable.Api('#interestRecPayTable').destroy();
        new $.fn.dataTable.Api('#interestInExpTable').destroy();
        $("#interestRecPay").val('');
        $("#interestInExp").val('');
    }
    bt.corp.init();
    var money = bt.moneyType.init();
    var rate = bt.rateType.init();
    var curDate = bt.dateType.init();
    var selectType = bt.selectType.init();

    //根据账户类别不同渲染不同的预提类型
    function fn_predrawSignShow(type) {
        var accType="";
        if($("select[name='accType']").length>0){
            accType=$("select[name='accType']").val();
        }else{
            accType=$("input[name='accType']").val();
        }
        var gc='Admin.PredrawSign';
        $("select[name='predrawSign']").empty();

        $("select[name='predrawSign']").append("<option value=''>"+bt.lang("common.placeholder.select")+"</option>");
        var poolSign = $("#poolSign").val();
        switch(accType)
        {
            case '01'://企业活期存款01
                // 季末结息预提1
                // 季末结息不预提2
                // 每月结息3
                if(poolSign != 1) {
                    $("select[name='predrawSign']").append("<option value='1'"+((type=='init' && 1==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'1')+"</option>");
                }
                $("select[name='predrawSign']").append("<option value='2'"+((type=='init' && 2==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'2')+"</option>");
                $("select[name='predrawSign']").append("<option value='3'"+((type=='init' && 3==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'3')+"</option>");
                break;
            case '02': //银行活期02, 银行定期06,企业定期05，通知存款	08银行借款04
            case '04':
            case '05':
            case '06':
            case '08':
                // 季末不结息预提，4
                // 季末不结息不预提5
                $("select[name='predrawSign']").append("<option value='4'"+((type=='init' && 4==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'4')+"</option>");
                $("select[name='predrawSign']").append("<option value='5'"+((type=='init' && 5==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'5')+"</option>");
                break;
            case '03':  //放款03
                // 季末结息预提	1
                // 季末结息不预提	2
                // 每月结息	3
                // 季末不结息预提	4
                // 季末不结息不预提	5
                $("select[name='predrawSign']").append("<option value='1'"+((type=='init' && 1==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'1')+"</option>");
                $("select[name='predrawSign']").append("<option value='2'"+((type=='init' && 2==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'2')+"</option>");
                $("select[name='predrawSign']").append("<option value='3'"+((type=='init' && 3==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'3')+"</option>");
                $("select[name='predrawSign']").append("<option value='4'"+((type=='init' && 4==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'4')+"</option>");
                $("select[name='predrawSign']").append("<option value='5'"+((type=='init' && 5==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'5')+"</option>");
                break;
            case '09': //委托贷款09
                // 季末结息不预提	2
                // 每月结息	3
                // 季末不结息不预提	5'

                $("select[name='predrawSign']").append("<option value='2'"+((type=='init' && 2==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'2')+"</option>");
                $("select[name='predrawSign']").append("<option value='3'"+((type=='init' && 3==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'3')+"</option>");
                $("select[name='predrawSign']").append("<option value='5'"+((type=='init' && 5==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'5')+"</option>");
                break;
            case '07': //内部账户	07
                // 每月结息	3
                $("select[name='predrawSign']").append("<option value='3'"+((type=='init' && 3==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'3')+"</option>");
                break;
            case '10'://委托存款10
                // 季末不结息不预提	5
                $("select[name='predrawSign']").append("<option value='5'"+((type=='init' && 5==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'5')+"</option>");
                break;
            default:
                // 每月结息	3
                $("select[name='predrawSign']").append("<option value='3'"+((type=='init' && 3==$("#predrawSignHid").val())?"selected":"")+">"+ bt.dict(gc,'3')+"</option>");
        }


    }

    function fn_overdraftSign(val) {
        if(val=='N'){
            //透支利率r

            if($("input[data-biz-name='overdraftRate']").length>0) {
                $("input[data-biz-name='overdraftRate']").attr("readonly","readonly");
                $("input[data-biz-name='overdraftRate']").removeAttr("lay-verify");
                //rate.get("overdraftRate").val("");
            }
            if($("input[data-biz-name='overdraftMoney']").length>0){
                $("input[data-biz-name='overdraftMoney']").removeAttr("lay-verify");
                $("input[data-biz-name='overdraftMoney']").attr("readonly","readonly");
                money.get("overdraftMoney").val("");
            }

        }else if(val=='R'){
            if($("input[data-biz-name='overdraftRate']").length>0) {
                $("input[data-biz-name='overdraftRate']").attr("lay-verify","required");
                $("input[data-biz-name='overdraftRate']").removeAttr("readonly");
            }

            if($("input[data-biz-name='overdraftMoney']").length>0) {
                $("input[data-biz-name='overdraftMoney']").removeAttr("readonly");
                $("input[data-biz-name='overdraftMoney']").attr("lay-verify", "required");
            }
        }else{
            if($("input[data-biz-name='overdraftRate']").length>0) {
                $("input[data-biz-name='overdraftRate']").attr("lay-verify","required");
                $("input[data-biz-name='overdraftRate']").removeAttr("readonly");
            }

            if($("input[data-biz-name='overdraftMoney']").length>0){
                $("input[data-biz-name='overdraftMoney']").attr("readonly","readonly");
                $("input[data-biz-name='overdraftMoney']").removeAttr("lay-verify");
            }

        }

        //计息标志
        if($("input[name='interestSign']").length>0){
            var $is=$("input[name='interestSign']:checked").val();
            if($is == 0 && $("input[data-biz-name='overdraftRate']").length>0){
                $("input[data-biz-name='overdraftRate']").removeAttr("lay-verify");
                $("input[data-biz-name='overdraftRate']").attr("readonly","readonly");
                rate.get("overdraftRate").val("");
            }
        }

        form.render();
        form.renderIcon();
    }


    function fn_interestSign(val) {
        if(val == "1"){
            if($("input[data-biz-name='interestRate']").length>0 ){
                $("input[data-biz-name='interestRate']").removeAttr("readonly");
                $("input[data-biz-name='interestRate']").attr("lay-verify","required");
            }

            if($("input[data-biz-name='overdueRate']").length>0) {
                $("input[data-biz-name='overdueRate']").attr("lay-verify", "required");
            }

            if($("select[name='predrawSign']").length>0 ){
                $("select[name='predrawSign']").removeAttr("disabled");
                $("select[name='predrawSign']").attr("lay-verify","required");
            }

            if($("#interestStartDate").length>0){
                $("#interestStartDate").parent().attr("lay-verify","required");
            }

            if( $("select[name='accForDeduct']").length>0){
                $("select[name='accForDeduct']").attr("lay-verify","required");
            }

            if($("input[data-biz-name='overdraftRate']").length>0 ){
                if($("select[name='overdraftSign']").length>0){
                    var overdraftSign = $("select[name='overdraftSign']").val();
                    if(overdraftSign == "D" || overdraftSign == "R"){
                        $("input[data-biz-name='overdraftRate']").attr("lay-verify","required");
                        $("input[data-biz-name='overdraftRate']").removeAttr("readonly");
                    }
                }else{
                    $("input[data-biz-name='overdraftRate']").attr("lay-verify","required");
                    $("input[data-biz-name='overdraftRate']").removeAttr("readonly");
                }
            }

        }else{
            //利率
            if($("input[data-biz-name='interestRate']").length>0 ) {
                $("input[data-biz-name='interestRate']").attr("readonly", "readonly");
                $("input[data-biz-name='interestRate']").removeAttr("lay-verify");
                rate.get("interestRate").val("");
            }
            if($("input[data-biz-name='overdueRate']").length>0) {
                $("input[data-biz-name='overdueRate']").removeAttr("lay-verify");
                rate.get("overdueRate").val("");
            }

            //预提类型
            if($("select[name='predrawSign']").length>0 ) {
                var options = $("select[name='predrawSign']").find("option");
                options.attr("selected", false);
                $("select[name='predrawSign']").removeAttr("lay-verify");
                $("select[name='predrawSign']").attr("disabled", "disabled");
            }
            if($("#interestStartDate").length>0){
                $("#interestStartDate").parent().removeAttr("lay-verify");
            }
            if( $("select[name='accForDeduct']").length>0){
                $("select[name='accForDeduct']").removeAttr("lay-verify");
            }

            if($("input[data-biz-name='overdraftRate']").length>0){
                $("input[data-biz-name='overdraftRate']").removeAttr("lay-verify");
                $("input[data-biz-name='overdraftRate']").attr("readonly","readonly");
                rate.get("overdraftRate").val("");
            }
        }
        var accType="";
        if($("select[name='accType']").length>0){
            accType=$("select[name='accType']").val();
        }else{
            accType=$("input[name='accType']").val();
        }
        //内部账户改户的界面，定期存款、通知存款账户的“利率”字段不允许修改
        if(accType == '05' || accType == '06' || accType == '08'){
            if($("input[data-biz-name='interestRate']").length>0 ) {
                $("input[data-biz-name='interestRate']").attr("readonly", "readonly");
            }
        }
        form.renderIcon();
        form.render();
    }

    //监控透支类型
    form.on('select(overdraftSign)',function (data) {
        fn_overdraftSign(data.value);

    });

    //监控计息标志
    form.on('radio(interestSign)',function (data) {
        fn_predrawSignShow();
        fn_interestSign(data.value);
        var changeType = $("#changeType").val();
        //是否显示应付应收账号信息
        isShow(data.value,changeType);
        //是否显示计息账号信息
        // isShowInterAcc(data.value,changeType);
    });
    //监控基准利率/基础利率
    form.on('radio(referenceRateType)',function (data) {
        bt.getInterestRate();
        form.render();
    });

    //周期单位
    form.on('select(endUnit)',function (data) {
        $callNotice(data.value);
        form.render();
    });
    /**
     * 根据计息标志控制是否显示计息账号信息
     * @param signVal
     */
    function isShowInterAcc(signVal,changeType) {
        //changeType 为空，说明是修改页面，不为空，说明是改户页面。
        var sysSubjectCode = "";
        if(isEmpty(changeType)){
            sysSubjectCode = $("#subjectId").find("option:selected").attr("data-sysSubjectCode");
        }else{
            sysSubjectCode = $("#sysSubjectCode").val();
        }
        //2011-应付利息-企业活期,5212-利息支出-企业活期,2012-应付利息-企业定期,5211-利息支出-企业定期,2013-应付利息-通知存款,
        //5213-利息支出-通知存款,2016-应付利息-企业协定,5216-利息支出-企业协定,1032-应收利息-银行活期存款,5012-利息收入-银行活期存款,
        //2014-应付利息-银行借款,5214-利息支出-银行借款,1031-应收利息-企业放款,5011-利息收入-企业放款
        if ("2011" == sysSubjectCode || "5212" == sysSubjectCode || "2012" == sysSubjectCode || "5211" == sysSubjectCode || "2013" == sysSubjectCode ||
            "5213" == sysSubjectCode || "2016" == sysSubjectCode || "5216" == sysSubjectCode || "1032" == sysSubjectCode || "5012" == sysSubjectCode ||
            "2014" == sysSubjectCode || "5214" == sysSubjectCode || "1031" == sysSubjectCode || "5011" == sysSubjectCode){
            $("#intAccShow").show();
        }else{
            $("#intAccShow").hide();
            $("#interestAcc").val("");
        }
    }
    /**
     * 根据计息标志控制是否显示账号信息
     * @param signVal
     */
    function isShow(signVal,changeType) {
        var sysSubjectCode = "";
        //changeType 为空，说明是修改页面，不为空，说明是改户页面。
        if(isEmpty(changeType)){
            sysSubjectCode = $("#subjectId").find("option:selected").attr("data-sysSubjectCode");
        }else{
            sysSubjectCode = $("#sysSubjectCode").val();
        }
        if ("2120" == sysSubjectCode || "2130" == sysSubjectCode || "2140" == sysSubjectCode || "2150" == sysSubjectCode || "1020" == sysSubjectCode || "2110" == sysSubjectCode || "1120" == sysSubjectCode) {
            if ("1" == signVal) {
                $("#showAcc").show();
                getAccInfo(changeType);
            } else {
                $("#showAcc").hide();
                $("#interestRecPay").val('');
                $("#interestInExp").val('');
            }
        }else{
            $("#showAcc").hide();
        }
    }

    /**
     * 根据开户种类，获取应收应付利息户和利息收入支出户
     */
    function getAccInfo(changeType) {
        var sysSubjectCode = "";
        var curId = "";
        if(isEmpty(changeType)){
            sysSubjectCode = $("#subjectId").find("option:selected").attr("data-sysSubjectCode");
            curId = $("select[name='curId']").val();
        }else{
            sysSubjectCode = $("#sysSubjectCode").val();
            curId = $("#curId").val();
        }
        //应收应付利息户:interestRecPay, 利息收入支出户:interestInExp
        //账户类别：企业活期存款
        //对应系统科目：应付利息：应付利息-企业活期（2011），利息支出：利息支出-企业活期（5212）
        if ("2120" == sysSubjectCode) {
            recPayInit('2011',curId);
            interestInExp('5212',curId);
        } else if ("2130" == sysSubjectCode) {
            //账户类别：企业定期存款
            //对应系统科目：应付利息：应付利息-企业定期（2012），利息支出：利息支出-企业定期（5211）
            recPayInit('2012',curId);
            interestInExp('5211',curId);
        } else if ("2140" == sysSubjectCode) {
            //账户类别：企业通知存款
            //对应系统科目：应付利息：应付利息-通知存款（2013），利息支出：利息支出-通知存款（5213）
            recPayInit('2013',curId);
            interestInExp('5213',curId);
        } else if ("2150" == sysSubjectCode) {
            //账户类别：企业协定存款
            //对应系统科目：应付利息：应付利息-企业协定存款（2016），利息支出：利息支出-企业协定存款（5216）
            recPayInit('2016',curId);
            interestInExp('5216',curId);
        } else if ("1020" == sysSubjectCode) {
            //账户类别：银行活期
            //对应系统科目：应收利息：应收利息-银行活期存款（1032），利息收入：利息收入-银行活期存款（5012）
            recPayInit('1032',curId);
            interestInExp('5012',curId);
        } else if ("2110" == sysSubjectCode) {
            //账户类别：银行借款
            //对应系统科目：应付利息：应付利息-银行借款（2014），利息支出：利息支出-银行借款（5214）
            recPayInit('2014',curId);
            interestInExp('5214',curId);
        } else if ("1120" == sysSubjectCode) {
            //账户类别：企业放款（内部贷款）
            //对应系统科目：应收利息：应收企业放款利息（1031）（应收利息-内部贷款），利息收入：利息收入-放款（内部贷款）（5011）
            // 暂不做，应收逾期利息：应收逾期利息（1060）
            recPayInit('1031',curId);
            interestInExp('5011,5015',curId);
        } else {
            recPayInit('',curId);
            interestInExp('',curId);
        }
    }

    //应付利息账号下拉表格
    function recPayInit(code,curId){

        bt.selectTable.init({
            elem: "#interestRecPayTable",
            url: basePath + "/inneraccount/fcacc/getAccList",
            urldata: {
                netId: $('input[name="netId"]').val(),
                corpId : $('input[name="corpId"]').val(),
                sysSubjectCode :code,
                curId : curId,
                bankCode : $("#bankCode").val()
            },
            scrollX: true,
            scrollY: "200px",
            searchInput: "#interestRecPay",
            paging: true,
            columns: [
                {data: "blnctrAcc",},
                {data: "accName",},
                {data: "subjectName",}
            ],
            clickCallback: function (data) {
                if (data) {
                    $('#interestRecPay').val(data.blnctrAcc);
                } else {
                    $('input.text').val('');
                }
            }
        });
    }
    //利息支出账号下拉表格
    function interestInExp(code,curId){
        console.log('interestInExpTable');
        bt.selectTable.init({
            elem: "#interestInExpTable",
            url: basePath + "/inneraccount/fcacc/getAccList",
            urldata: {
                netId: $('input[name="netId"]').val(),
                corpId : $('input[name="corpId"]').val(),
                sysSubjectCode :code,
                curId : curId,
                bankCode : $("#bankCode").val()
            },
            scrollX: true,
            scrollY: "200px",
            searchInput: "#interestInExp",
            paging: true,
            columns: [
                {data: "blnctrAcc",},
                {data: "accName",},
                {data: "subjectName",}
            ],
            clickCallback: function (data) {
                if (data) {
                    $('#interestInExp').val(data.blnctrAcc);
                } else {
                    $('input.text').val('');
                }
            }
        });
    }

    $(document).ready(function(){
        //根据账户类别不同渲染不同的预提类型
        var accType="";
        if($("select[name='accType']").length>0){
            accType=$("select[name='accType']").val();
        }else{
            accType=$("input[name='accType']").val();
        }
        if($("select[name='predrawSign']").length>0){
            fn_predrawSignShow("init");
        }

        var $os=$("select[name='overdraftSign']");
        if($os){
            $("select[name='overdraftSign']").find("option").each(function (){
                if ($(this).val() == $("#overdraftSignHid").val()) {
                    $(this).attr("selected", "selected");
                }
            });
            fn_overdraftSign($os.val());
        }

        var $is=$("input[name='interestSign']");
        if($is.length>0){
            var val =$("input[name='interestSign']:checked").val();
            fn_interestSign(val);
        }

        var $eu=$("select[name='endUnit']");
        if($eu) {
            $("select[name='endUnit']").find("option").each(function () {
                if ($(this).val() == $("#endUnitHid").val()) {
                    $(this).attr("selected", "selected");
                }
            });
        }

        var $cw=$("select[name='certificateWay']");
        if($cw) {
            $("select[name='certificateWay']").find("option").each(function (){
                if ($(this).val() == $("#certificateWayHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });
        }


        var $nt=$("select[name='notifyType']");
        if($nt) {
            $("select[name='notifyType']").find("option").each(function (){
                if ($(this).val() == $("#notifyTypeHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });
        }
        bt.showEle();
        if( accType=='01'){
            $("select[name='chargeAcc']").find("option").each(function (){
                if ($(this).val() == $("#chargeAccHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });
        }else if(accType=='03' || accType=='09'){
            $("select[name='accForDeduct']").find("option").each(function (){
                if ($(this).val() == $("#accForDeductHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });
        }else if(accType=='14'){
            $("select[name='accCussent']").find("option").each(function (){
                if ($(this).val() == $("#accCussentHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });

            $("select[name='accForDeduct']").find("option").each(function (){
                if ($(this).val() == $("#accForDeductHid").val()) {
                    $(this).attr("selected", "selected");
                    //break;
                }
            });
        }
        var changeType = $("#changeType").val();
        if(changeType == "change"){
            if($("input[name='accStartDate']").length>0){
                curDate.get("accStartDate").attr("readonly","readonly")
            }
        }
        //计息标志
        var interestSign = $("input[name='interestSign']:checked").val();
        //是否显示应付应收账号信息
        isShow(interestSign,changeType)
        //是否显示计息账号信息
        isShowInterAcc(interestSign,changeType);
        form.render();
        form.addrequiredIcon();

    });

    $callNotice = function(value,$elem) {
        if($("select[name='endUnit']").length>0){
            var endUnit = $("select[name='endUnit']").val();
            if($("#notifyType").length>0) {
                var dateFun = bt.dateType.getFun();//获取时间操作方法
                var startDate = curDate.get("interestStartDate").val();
                var deadlineDate = curDate.get("deadlineDate").val();

                if(startDate == "" || deadlineDate == ""){
                    return;
                }

                if(dateFun.compare(startDate,deadlineDate)>0){
                    layer.msg(bt.lang('nis.fcAcc.message.less.Due.value'));
                }
                if(endUnit == "D"){
                    var days = dateFun.getBetweenDay(startDate, deadlineDate);//运用getBetweenDay()，获取天数
                    $("#notifyType").val(days);
                }else if(endUnit == "M"){
                    var months = dateFun.getBetweenMonth(startDate, deadlineDate);
                    $("#notifyType").val(months);
                }else{
                    var years = dateFun.getBetweenYear(startDate, deadlineDate);
                    $("#notifyType").val(years);
                }
            }
        }
    }
    $(document).ready(function(){
        //计息标志
        if($("input[name='interestSign']").length>0){
            var $is=$("input[name='interestSign']:checked").val();
            fn_interestSign($is);

            var poolSign = $("#poolSign").val();
            if(poolSign == "1"){
                form.setReadonlyForRadio('#interestSignDiv',true);
            }
        }

    });
});
//全局方法
function clickNotice(value,$elem){
    if(value!="") {
        $callNotice(value);
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