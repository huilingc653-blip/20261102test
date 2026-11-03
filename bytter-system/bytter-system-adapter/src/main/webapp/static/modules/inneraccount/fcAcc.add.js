var netIdCallback;
var corpIdCallback;
var loadAagFcAccHtmlFlag = 0;//加载＃AagFcAcc内部html标识，当为０时代表第一次加载
bt.use(['form','select','corp','bankInfo','dateType','selectTableType', 'selectType','rateType','fcAccInfo','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.bankInfo.init();
    var curDate = bt.dateType.init();
    bt.selectType.init();
    var rate = bt.rateType.init();
    var dicts = bt.dicts.init();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
        var corpPoolUrl = basePath + "/inneraccount/fcacc?getUserCorpPools";
        bt.selectType.get("netId").attr("data-url", corpPoolUrl);

        var poolId = bt.selectType.get("netId").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacc?getUserCorpPoolChilds&poolId=" + poolId;
        }
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacc?getUserCorpPoolChilds&poolId=" + data.id;
            bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
            //设置网点编码为资金池编码
            $("#netCode").val(data.poolcode);
            bt.genBlnctrAcc();
            bt.genOtherHtml();
        }

        /**
         * 资金池子单位回调
         * @param data
         */
        corpIdCallback = function (data) {
            //账号的设置
            bt.showEle();
        }
    }

    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        data.field.accState=accState;
        if(!validNetDif() || !compareDate() || !validCorpDif()){
            bt.loading.hide();
            return false;
        }
        var action = "/inneraccount/fcacc/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                }else{
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });

    //网点/联行网点不能相同
    var validNetDif = function () {
        var netId= '', unionNet= '';
        if($('input[name="netId"]').length>0){
            netId=$('input[name="netId"]').val();
        }
        if($('input[name="unionNet"]').length>0){
            unionNet= $('input[name="unionNet"]').val();
        }
        if(netId != '' && netId == unionNet){
            sysAlert(bt.lang("table.fcAcc.validNetDif"));
            return false;
        }
        return true;
    };

    //委托存款单位不能与委托贷款单位是同一个单位
    var validCorpDif = function () {
        var corpId= '', precationCorp='';
        if($('input[name="corpId"]').length>0){
            corpId=$('input[name="corpId"]').val();
        }
        if($('input[name="precationCorp"]').length>0){
            precationCorp= $('input[name="precationCorp"]').val();
        }
        if(corpId != '' && corpId == precationCorp){
            sysAlert(bt.lang("nis.fcAcc.message.entrusted.deposit.loan"));
            return false;
        }

        return true;
    };
    var compareDate = function(){
        var dateFun = bt.dateType.getFun();//获取时间操作方法
        if($("input[name='interestStartDate']").length>0 && $("input[name='deadlineDate']").length>0 ){
            var startDate = curDate.get("interestStartDate").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(startDate,deadlineDate)>0){
                sysAlert(bt.lang('nis.fcAcc.message.less.Due.value'));
                return false;
            }
        }else if($("input[name='deadlineDate']").length>0 && $("input[name='accStartDate']").length>0 ){
            var startDate = $("input[name='accStartDate']").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(startDate,deadlineDate)>0){
                sysAlert(bt.lang('nis.fcAcc.message.earlier.expiration.activation'));
                return false;
            }
        }

        if($("input[name='deadlineDate']").length>0 && $("input[name='delayDeadlineDate']").length>0 ){
            var delayDeadlineDate = $("input[name='delayDeadlineDate']").val();
            var deadlineDate = curDate.get("deadlineDate").val();
            if(dateFun.compare(delayDeadlineDate,deadlineDate)<0 || dateFun.compare(delayDeadlineDate,deadlineDate)==0){
                sysAlert(bt.lang('nis.fcAcc.message.greater.extension.expiration'));
                return false;
            }
        }
        return true;
    };

    function sysAlert(val) {
        // layer.alert(val, {
        //     skin: 'layui-layer-molv', //样式类名
        //     closeBtn: 0
        // });
        layer.msg(val, {
            icon: 5
            ,shift: 6
        });
    }

    //根据账户类别获取科目代码
    form.on('select(accType)', function(data){
        //账户类别：02银行活期存款 04银行借款 06银行定期
        if(data.value == '02' || data.value == '04' || data.value == '06'){
            $(".bankClass").show();
        }else {
            $("#bankCode").val("");
            $("#bankAccName").text("");
            $(".bankClass").hide();
        }
        if(data.value == '07' || data.value == '14'){
            dicts.get('ifCompare').attr({
                'data-disabled' : true
            });
            dicts.get("ifCompare").val("0");
        }else{
            dicts.get('ifCompare').attr({
                'data-disabled' : false
            });
            dicts.get("ifCompare").val("1");
        }
        var poolSign = $("#poolSign").val();
        var url = basePath + "/finacial/paramconfig/fcsubject/jsonSubjectDate/";
        if(poolSign=="1") {
            url = basePath + "/finacial/paramconfig/FcSubjectPool/jsonSubjectDate/";
        }

        console.log("fcsubject-url:"+url);
        $.ajax({
            url: url+data.value+"?tk="+(new Date()).getTime(),
            method: 'GET',
            dataType: 'json',
            success: function(data){
                $("#subjectId").empty();
                $("#subjectId").append("<option value=''>"+bt.lang("common.placeholder.select")+"</option>");
                if(data){
                    $.each(data,function(index){
                        $("#subjectId").append("<option value='"+data[index].id+"'"
                            + " data-balanceWay='"+data[index].balanceWay+"'"  //余额方向
                            + " data-interestSign='"+data[index].interestSign+"'"  //计息标志
                            + " data-interestRate='"+(data[index].interestRate==null?"":data[index].interestRate)+"'"  //利率
                            + " data-subjectCode='"+data[index].subjectCode+"'"   //科目代码
                            + " data-predrawSign='"+data[index].predrawSign+"'" //结息方式
                            + " data-sysSubjectCode='"+data[index].sysSubjectCode+"'" //系统科目代码
                            + ">("+data[index].subjectCode+")"+ data[index].subjectName+"</option>");
                    });
                }
                form.render('select');
            }
        });
    });
    bt.unionAccTable=bt.selectTableType.init({
        elem:".acc",
        url: basePath+"/inneraccount/fcacc/getAccountTableData",
        paging:true,
        urldata:{
            type:'init'
        },
        columns:[
            {"data":"blnctrAcc",title:bt.lang("table.fcAcc.blnctrAcc")},
            {"data":"accName",title:bt.lang("table.fcAcc.accName")},
            {"data":"subjectName",title:bt.lang("table.fcAcc.subjectId")}
        ],
        clickCallback:function(data,$tr,$elem,full){
            $elem.val(data.blnctrAcc)   //给当前输入框赋name值;
        },
        tableInitCallback:function(full,$elem){
        }
    });

    //#银行代码
    bt.bailBankInfoCallback = function(data,dom){
        $("#bankCode").val(data.bankCode);
        $("#bankAccName").text(data.bankName);
        $("#bankName").val(data.bankName);
        bt.genOtherHtml();
    };

    form.on('select(subjectId)',function (data) {
        bt.genBlnctrAcc(data.elem);
        bt.showBankInfo();
        bt.genOtherHtml();
        form.render();
        bt.showEle();
    });
    form.on('select(curId)',function (data) {
        //账号赋值
        bt.genBlnctrAcc(data.elem);
        //网联账号赋值
        bt.unionAcc();
        //账号的设置
        bt.showEle();
        //选了科目才获取利率
        var subjectCode = $("#subjectId").find("option:selected").attr("data-subjectcode");
        if(subjectCode) {
            //获取利率
            bt.getInterestRate();
        }
    });

    //启用日期回调
    accStartDateCallback = function (data) {
        //获取利率
        bt.getInterestRate();
    }

    bt.unionAcc = function () {
        var unionNetId = $('input[name="unionNet"]').val();
        var curId = $("select[name='curId']").val();
        var url = basePath+"/inneraccount/fcacc/getAccountTableData";
        if(unionNetId && curId){
            var urldata={
                netId:unionNetId,
                corpId:unionNetId,
                curId:curId,
                type:''
            };
            $("#unionAcc").val("");
            bt.unionAccTable.get("unionAcc").setUrl(urldata);
        }else if(!unionNetId){
            //清空联行账号
            $("#unionAcc").val("");
            bt.unionAccTable.get("unionAcc").setUrl({type: 'init'});
        }
    };


    //不同的业务种类，初始化账号信息
    bt.showEle = function () {
        var accType =$("select[name='accType']").val();
        if( accType=='01'){ //企业活期
            bt.fn_chargeAcc("chargeAcc",accType);
            var blnctrAcc=$("#blnctrAcc").val();
            $("#chargeAcc").append("<option value="+blnctrAcc+">"+blnctrAcc+"</option>");
        }else if(accType=='03' || accType=='09'){ //放款03 委托贷款	09
            bt.fn_chargeAcc("accForDeduct",accType);
        }else if(accType=='14'){ //表外 14
            bt.fn_chargeAcc("accCussent",accType);
            bt.fn_chargeAcc("accForDeduct",accType);
        }
        if($("#relationAcc")){
            bt.relationAcc();
        }
        form.render('select');
    };

    //是否显示银行代码信息
    bt.showBankInfo = function(){
        //账户类别：02银行活期存款 04银行借款 06银行定期
        var accType =$("select[name='accType']").val();
        //系统编码：1032 应收利息-银行活期存款,1033 应收利息-银行定期存款,2014 应付利息-银行借款,5012 利息收入-银行活期存款,5013 利息收入-银行定期存款,5214 利息支出-银行借款,521Z 银行贴现利息支出
        var sysSubjectCode = $("#subjectId").find("option:selected").attr("data-sysSubjectCode");
        if(accType == '02'|| accType == '04' || accType == '06' || sysSubjectCode == '1032' || sysSubjectCode == '1033' || sysSubjectCode == '2014' || sysSubjectCode == '5012' || sysSubjectCode == '5013' || sysSubjectCode == '5214'|| sysSubjectCode == '521Z'){
            $(".bankClass").show();
        }else {
            $("#bankCode").val("");
            $("#bankAccName").text("");
            $(".bankClass").hide();
        }
    }


    $(document).ready(function(){
        $.ajax({url: basePath+"/basic/business/btcurrency/json",
            method: 'GET', dataType: 'json', async:false,
            success: function(data){
                if(data){
                    $("#curId").empty();

                    $.each(data,function(index){
                        $("#curId").append("<option value='"+data[index].id+"'"
                            + " data-curCode='"+data[index].curCode+"'"
                            + ">"+ data[index].curName+"</option>");
                    });
                    //默认选中人民币
                    $("select[name='curId']").val($("#rmbCur").val());
                }
                form.render('select');
            }
        });
        var accType =$("select[name='accType']").val();
        //账户类别：02银行活期存款 04银行借款 06银行定期
        if(accType == '02' || accType == '04' || accType == '06'){
            $(".bankClass").show();
        }else{
            $("#bankCode").val("");
            $("#bankAccName").text("");
            $(".bankClass").hide();
        }
        bt.showBankInfo();
    });

    /**
     * 手动生成账号
     */
    bt.genBlnctrManualAcc = function() {
        /**
         *	从末级科目得到计息标志和利率和透支利率，余额方向
         */
        lastSubjectChange();

        var netCode = $("#netCode").val();
        //自定义属性值获取
        var curCode = $("#curId").find("option:selected").attr("data-curcode");

        var subjectCode = $("#subjectId").find("option:selected").attr("data-subjectcode");
        var seqNum = $("#seqNum").val();
        if(!netCode || !seqNum || !curCode||!subjectCode){
            $("#blnctrAcc").attr("value","");
            return;
        }
        var rule = $("#accAccNoRule").val();
        if(rule == 4){
            //科目代码截取生成
            if(subjectCode&&subjectCode.length > 0){
                if(subjectCode.trim().length < 6){
                    subjectCode = subNum(subjectCode);
                }
            }
        }
        $("#blnctrAcc").val(netCode + "" + curCode +""+subjectCode+""+seqNum);
        form.render();
    };

    /**
     * 手动生成顺序号
     * @param data
     * @returns {boolean}
     */
    getNum = function(data) {
        var poolSign = $("#poolSign").val();
        var netId= $('input[name="netId"]').val();
        var curId=$("select[name='curId']").val();
        var accType=$("select[name='accType']").val();
        var seqNumTemp = data.value;
        if(poolSign==1){
            netId=bt.selectType.get("netId").val();
        }
        /**
         如果为空不生成
         **/
        if(netId.length==0||curId.length==0||accType.length==0 || seqNumTemp.length == 0) {
            return;
        }
        var param={
            netId:netId,
            curId:curId,
            accType:accType,
            poolSign:poolSign,
            date:(new Date()).getTime(),
            seqNum:seqNumTemp
        };
        var url=basePath + "/inneraccount/fcacc/createSequence";
        $.ajax({
            url: url, method: 'GET', data: param, async:false, dataType: 'json',
            success: function (data) {
                if (data) {
                    $("#seqNum").val(data.obj);
                    bt.genBlnctrManualAcc();
                }
                form.render();
            }
        });
    }

    /**
     * 生成账号
     */
    bt.genBlnctrAcc = function(element) {
        /**
         *	从末级科目得到计息标志和利率和透支利率，余额方向
         */
        lastSubjectChange();
        /**
         * 自动生成顺序号
         */
        createSequence();

        var netCode = $("#netCode").val();
        //自定义属性值获取
        var curCode = $("#curId").find("option:selected").attr("data-curcode");

        var subjectCode = $("#subjectId").find("option:selected").attr("data-subjectcode");
        var seqNum = $("#seqNum").val();
        if(!netCode || !seqNum || !curCode||!subjectCode){
            $("#blnctrAcc").attr("value","");
            return;
        }

        var rule = $("#accAccNoRule").val();
        if(rule == 4){
            //科目代码截取生成
            if(subjectCode&&subjectCode.length > 0){
                if(subjectCode.trim().length < 6){
                    subjectCode = subNum(subjectCode);
                }
            }
        }
        $("#blnctrAcc").val(netCode + "" + curCode +""+subjectCode+""+seqNum);
        form.render();
    };
    /**
     * 生成账户不公共的属性
     * @return
     */
    bt.genOtherHtml = function(){
        var id=$("#id").val();
        var accType =$("select[name='accType']").val();
        var curId = $("select[name='curId']").val();
        var url=basePath+"/inneraccount/fcacc/businessAccount";
        var netId='';
        if($('input[name="netId"]').length>0){
            netId=$('input[name="netId"]').val();
        }else{
            netId=bt.selectType.get("netId").val();
        }
        var poolSign = $("#poolSign").val();

        var param={
            id:id,
            netId:netId,
            accTypeCode:accType,
            poolSign:poolSign,
            type:'add',
            curId:curId,
            date:(new Date()).getTime()
        };
        if(accType && accType!=""){
            $.ajax({
                url:url,
                method: 'GET',
                data: param,
                async:false,
                success: function (json) {
                    $("#tagFcAcc").html("");
                    $("#tagFcAcc").html(json);
                    form.render();
                }
            });
        }
    };

    /**
     * 获取利率
     * @return
     */
    bt.getInterestRate = function(){
        var accType =$("select[name='accType']").val();
        var accStartDate = curDate.get("accStartDate").val();
        var curId = $("select[name='curId']").val();
        var referenceRateType = $('input[name="referenceRateType"]:checked').val();
        var subjectId = $("#subjectId").val();
        if(accType == "01" || accType != "05" || accType != "08" ) {
            //获取利率
            $.ajax({
                url: basePath + "/inneraccount/fcacc?getInterestRate",
                method: 'POST',
                data: {curId: curId,subjectId:subjectId},
                dataType: 'json',
                success: function (data) {
                    try {
                        if (data.obj) {
                            if (data.obj) {
                                rate.get("interestRate").val(data.obj);
                            }
                        }else{
                            rate.get("interestRate").val("");
                        }
                    }catch (e) {

                    }
                    form.render();
                }
            });
        }
    };


    /**
     *根据末级科目得到是否计息和透支利率 余额方向
     */
    var lastSubjectChange=function(){

        var balanceWay = $("#subjectId").find("option:selected").attr("data-balanceway");
        if(!balanceWay){
            return ;
        }
        $("select[name='balanceWay']").val(balanceWay);
        form.render();

    };

    /**
     自动生成序列号
     **/
    var createSequence=function(){
        var poolSign = $("#poolSign").val();
        var netId= $('input[name="netId"]').val();
        var curId=$("select[name='curId']").val();
        var accType=$("select[name='accType']").val();
        if(poolSign==1){
            netId=bt.selectType.get("netId").val();
        }
        /**
         如果为空不自动生成
         **/
        if(netId.length==0|| !curId || curId.length==0|| !accType || accType.length==0) {
            return;
        }
        var param={
            netId:netId,
            curId:curId,
            accType:accType,
            poolSign:poolSign,
            date:(new Date()).getTime()
        };
        var url=basePath + "/inneraccount/fcacc/createSequence";
        $.ajax({
            url: url, method: 'GET', data: param, async:false, dataType: 'json',
            success: function (data) {
                if (data) {
                    $("#seqNum").val(data.obj);
                }
                form.render();
            }
        });
    };

    bt.fn_chargeAcc = function(elem,accType) {
        $("#"+elem).empty();
        $("#"+elem).append("<option value=' '>"+bt.lang("common.placeholder.select")+"</option>");
        var curId=$("select[name='curId']").val();
        var corpId=$('input[name="corpId"]');
        //资金池标识 0，非资金池 1，资金池
        var poolSign = $("#poolSign").val();
        if(corpId && corpId.val()!=""){
            var param={
                curId:curId,
                corpId:corpId.val(),
                accType:accType,
                elem: elem,
                poolSign: poolSign,
                date:(new Date()).getTime()
            };
            var url= basePath+"/inneraccount/fcacc?getChangeAcc";
            $element(url,param,elem);
        }
    };

    bt.relationAcc= function() {
        $("#relationAcc").empty();
        $("#relationAcc").append("<option value=' '>"+bt.lang("common.placeholder.select")+"</option>");
        var curId=$("select[name='curId']").val();
        var corpId=$('input[name="corpId"]');
        if(corpId && corpId.val()!=""){
            var param={
                curId:curId,
                corpId:corpId.val()
            };
            var url= basePath+"/inneraccount/fcacc?listFcAccByCorpId";
            $element(url,param,"relationAcc");
        }
    };


    $element = function (url,param,elem) {
        $.ajax({url: url, method: 'POST', data: param,dataType: 'json',
            success: function(data){
                if(data && data.obj!=null){
                    $.each(data.obj,function(index,item){
                        $("#"+elem).append("<option value="+item.blnctrAcc+">"+item.blnctrAcc+"</option>");
                    });
                    form.render('select');
                }
            }
        });
    };
    //计息账号初始化
    var accInfo = bt.fcAccInfo.init();
    bt.accInfoParams = function () {
        accInfo.set({
            "netId": $('input[name="netId"]').val(),
            "corpId": $('input[name="corpId"]').val(),
            "sysSubjectCode": $("#subjectId").find("option:selected").attr("data-sysSubjectCode"),
            "curId":$("select[name='curId']").val(),
            "bankCode":$("#bankCode").val()
        });
        return true;
    }

    //计息账号回调
    bt.interestAccCallback = function (data, dom) {
        var interestAccTemp = "";
        $.each(data, function (i, item) {
            if(i == 0){
                interestAccTemp = item.blnctrAcc;
            }else{
                interestAccTemp += ";" + item.blnctrAcc;
            }
        });
        $("#interestAcc").val(interestAccTemp).attr("title",interestAccTemp);
    }
});

//网点下拉框回调
function netChangeId(id) {

}
//网点下拉框回调
function netChange(obj) {
    bt.jquery("#netCode").val(obj.code);
    bt.genBlnctrAcc();
    bt.genOtherHtml();
}

function corpChange(data) {
    if (typeof(data) == "undefined" || data == null || data == "" || data == " ") {
        $("#accName").val('');
    }else{
        $("#accName").val(data.corpName);
    }
    //账号的设置
    bt.showEle();
    bt.genOtherHtml();
}

function unionNetChange(id) {
    //网联账号赋值
    bt.unionAcc();
}

//只能输入数字
function zhzs(value) {
    value = value.replace(/[^\d]/g, '');
    return value;
}

//截取科目代码
function subNum(subjectCode) {
    var codeNum = subjectCode + "000000";
    if (subjectCode.trim()) {
        codeNum = codeNum.substring(0,6);
    }
    return codeNum;
}

/**
 * 获取计息账号的值
 * @returns {*|jQuery}
 */
function interestVal(){
    var intVal = $("#interestAcc").val();
    return intVal;
}
