var loadAagFcAccHtmlFlag = 0;//加载＃AagFcAcc内部html标识，当为０时代表第一次加载
bt.use(['form','select','corp','bankInfo','selectTableType','dateType','rateType','fcAccInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    var urldata;
	    var unionNetId = $('[ data-name="unionNet"]').data('defaultvalue');
        var curId = $("input[name='curId']").val();
        if(unionNetId && curId){
            urldata={
                netId:unionNetId,
                corpId:unionNetId,
                curId:curId,
                type:''
                }
          }else{
             urldata={
               type:'init'
        	}
       }
    bt.corp.init();
    bt.bankInfo.init();
    var curDate = bt.dateType.init();
    var rate = bt.rateType.init();
	 
    bt.unionAcc = function (param) {
        var unionNetId = $('input[name="unionNet"]').val();
        var curId = $("input[name='curId']").val();
        var url = basePath+"/inneraccount/fcacc/getAccountTableData";
        if(unionNetId && curId){
            var urldata={
                netId:unionNetId,
                corpId:unionNetId,
                curId:curId,
                type:''
            };
            if("init" != param){
                $("#unionAcc").val("");
            }
            bt.unionAccTable.get("unionAcc").setUrl(urldata);
        }
    };

    //#银行代码
    bt.bailBankInfoCallback = function(data,dom){
        $("#bankCode").val(data.bankCode);
        $("#bankAccName").text(data.bankName);
    };
	   
    bt.unionAccTable=bt.selectTableType.init({
        elem:".acc",
        url: basePath+"/inneraccount/fcacc/getAccountTableData",
        paging:true,
        urldata:urldata,
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

    /**
     * 全局调整
     */
    bt.getGlobal = function(type) {

        if (type == 'init'){
            initseqNum();
            //生成附页面元素
            genOtherHtml();
        }else{
            // // 自动生成顺序号
            // createSequence();
            //
            // //生成内部账号
            // getBlnctrAcc();
        }



        form.render();
    };
    /**
     *根据内部账户及其生成策略 初始化顺序号
     */
    var initseqNum=function () {
        var blnctrAcc = $("#blnctrAcc").val();
        if(!blnctrAcc){
            return;
        }

        /**
         * 内部账号生成法则
         * 顺序号 ==网点+币别+科目+ 科目类别（acc_type）后从0开始的数据。
         * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>网点+币别+科目+3位顺序号</option>
         * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>网点+币别+科目+4位顺序号</option>
         * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>网点+币别+科目+6位顺序号</option>
         * <option value="4" #if($config.fcs.accAccnoRule == 4)selected#end>网点+币别+科目(科目不足6位补齐)+4位顺序号</option>
         */
        var accAccNoRule=$("#accAccNoRule").val();
        accAccNoRule=parseInt(accAccNoRule);

        var tempLength=1;
        switch(accAccNoRule) {
            case 1:
                tempLength=3;
                break;
            case 2:
                tempLength=4;
                break;
            case 3:
                tempLength=6;
                break;
            case 4:
                tempLength=4;
                break;
            default:
                tempLength=4;
        }
        var str=blnctrAcc.substr(blnctrAcc.length-tempLength,blnctrAcc.length);
        $("#seqNum").text(str);
    }

    /**
     * 生成账户不公共的属性
     * @return
     */
    var genOtherHtml=function(){
        var id=$("#id").val();
        var accType =$("input[name='accType']").val();
        var status = $("#status").val();
        var url=basePath+"/inneraccount/fcacc/businessAccount";
        if(status.length>0 && status == 'G' || status == 'F' || status == 'E'){
            url=basePath+"/inneraccount/fcaccchange/businessAccount";
        }
        var param={
            id:id,
            accTypeCode:accType,
            type:'update',
            opType:'change',
            date:(new Date()).getTime()
        };
        if(accType && accType!="") {
            $.ajax({
                url: url,
                method: 'GET',
                data: param,
                async: false,
                success: function (json) {
                    $("#tagFcAcc").html("");
                    $("#tagFcAcc").html(json);
                    form.render();
                }
            });
        }
    };



    //不同的业务种类，初始化账号信息
    bt.showEle = function () {
        var accType =$("input[name='accType']").val();
        if( accType=='01'){
            bt.fn_chargeAcc("chargeAcc",accType);
        }else if(accType=='03' || accType=='09'){
            bt.fn_chargeAcc("accForDeduct",accType);
        }else if(accType=='14'){
            bt.fn_chargeAcc("accCussent",accType);
            bt.fn_chargeAcc("accForDeduct",accType);
        }
        if($("#relationAcc")){
            bt.relationAcc();
        }
        form.render('select');
    };
    bt.fn_chargeAcc = function(elem,accType) {
        $("#"+elem).empty();
        $("#"+elem).append("<option value=' '>"+bt.lang("common.placeholder.select")+"</option>");
        var curId=$("input[name='curId']").val();
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
                date:(new Date()).getTime(),
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
            $("select[name='relationAcc']").find("option").each(function (){
                if ($(this).val() == $("#relationAccHid").val()) {
                    $(this).attr("selected", "selected");
                }
            });
        }
    };
    /**
     * 获取利率
     * @return
     */
    bt.getInterestRate = function(){
        var accType =$("#accType").val();
        var accStartDate = curDate.get("accStartDate").val();
        var curId = $("#curId").val();
        var referenceRateType = $('input[name="referenceRateType"]:checked').val()
        if(accType == "01" || accType != "05" || accType != "08" ) {
            //获取利率
            $.ajax({
                url: basePath + "/inneraccount/fcacc?getInterestRate",
                method: 'POST',
                data: {accType: accType, accStartDate: accStartDate, curId: curId,referenceRateType:referenceRateType},
                dataType: 'json',
                success: function (data) {
                    try {
                        if (data.obj) {
                            if (data.obj.rate) {
                                rate.get("interestRate").val(data.obj.rate);
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


    $element = function (url,param,elem) {
        $.ajax({url: url, method: 'POST', data: param,dataType: 'json',async:false,
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

    $(document).ready(function(){
        bt.getGlobal('init');
        bt.unionAcc();
        bt.showEle();
        if($('#isView').val() == 'v'){ //查看
            $('div.layui-input-block').hide();
        }
        //账户类别：02银行活期存款 04银行借款 06银行定期
        //系统编码：1032 应收利息-银行活期存款,1033 应收利息-银行定期存款,2014 应付利息-银行借款,5012 利息收入-银行活期存款,5013 利息收入-银行定期存款,5214 利息支出-银行借款,521Z 银行贴现利息支出
        var sysSubjectCode = $("#sysSubjectCode").val();
        if($("#accType").val() == '02' || $("#accType").val() == '04' || $("#accType").val() == '06' || sysSubjectCode == '1032' || sysSubjectCode == '1033' || sysSubjectCode == '2014' || sysSubjectCode == '5012' || sysSubjectCode == '5013' || sysSubjectCode == '5214'|| sysSubjectCode == '521Z'){
            $(".bankClass").show();
        } else {
            $(".bankClass").hide();
        }
        form.render('select');
    });
    //计息账号初始化
    var accInfo = bt.fcAccInfo.init();
    bt.accInfoParams = function () {
        accInfo.set({
            "netId": $('input[name="netId"]').val(),
            "corpId": $('input[name="corpId"]').val(),
            "sysSubjectCode": $("#sysSubjectCode").val(),
            "curId":$("input[name='curId']").val(),
            "bankCode":$("#bankCode").val(),
            "interestAcc":$("#interestAccVal").val()
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

function initCorpComplete() {
	 bt.unionAcc("init");
}
function unionNetChange(id) {
    //网联账号赋值
    bt.unionAcc();
}

/**
 * 获取计息账号的值
 * @returns {*|jQuery}
 */
function interestVal(){
    var intVal = $("#interestAcc").val();
    return intVal;
}
