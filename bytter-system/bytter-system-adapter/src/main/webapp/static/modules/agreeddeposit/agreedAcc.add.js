bt.use(['form','select','corp','dateType','selectTableType','dicts','selectType','rateType','date'], function(){
    $ = bt.jquery;
    form = bt.form();

    //监听提交
    form.on('submit(submit)', function(data){
        var webObj = bt.navtab.getParentDom();
        webObj.bt.jquery("#agrAcc").val(data.field.blnctrAcc);
        webObj.bt.jquery("#netId").val(data.field.netId);
        webObj.bt.jquery("#fcCorpId").val(data.field.corpId);
        webObj.bt.jquery("#fcBlnctrAcc").val(data.field.blnctrAcc);
        webObj.bt.jquery("#fcAccName").val(data.field.accName);
        webObj.bt.jquery("#fcCurId").val(data.field.curId);
        webObj.bt.jquery("#subjectId").val(data.field.subjectId);
        webObj.bt.jquery("#accType").val(data.field.accType);
        webObj.bt.jquery("#shortcut").val(data.field.shortcut);
        webObj.bt.jquery("#unionNet").val(data.field.unionNet);
        webObj.bt.jquery("#unionAcc").val(data.field.unionAcc);
        webObj.bt.jquery("#attributeId").val(data.field.attributeId);
        webObj.bt.jquery("#natureCodeId").val(data.field.natureCodeId);
        webObj.bt.jquery("#netId").val(data.field.netId);
        webObj.bt.jquery("#balanceWay").val(data.field.balanceWay);
        webObj.bt.jquery("#accOpenTime").val(data.field.accOpenTime);
        webObj.bt.jquery("#accStartDate").val(data.field.accStartDate);
        webObj.bt.jquery("#relationAcc").val(data.field.relationAcc);
        webObj.bt.jquery("#interestRate").val(data.field.interestRate);
        webObj.bt.jquery("#interestSign").val(data.field.interestSign);
        webObj.bt.jquery("#predrawSign").val(data.field.predrawSign);
        webObj.bt.jquery("#chargeAcc").val(data.field.chargeAcc);
        webObj.bt.jquery("#overdraftSign").val(data.field.overdraftSign);
        webObj.bt.jquery("#overdraftRate").val(data.field.overdraftRate);
        webObj.bt.jquery("#overdraftMoney").val(data.field.overdraftMoney);
        webObj.bt.jquery("#rmk").val(data.field.rmk);
        bt.navtab.closeCurrent('false');
    });


    //科目变更
    changeSubject = function(data,ele){
        $("select[name='balanceWay']").val(data.balanceway);
        createSequence();//生成顺序号
        var netCode = $("#netCode").val();
        var curCode = $("#curCode").val();
        var subjectCode = data.subjectcode;
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
        $("#blnctrAcc").val(netCode + "" + curCode +""+subjectCode+""+seqNum);//生成账号
    }

    //生成顺序号
    var createSequence=function(){
        var netId= $("#netId").val();
        var curId= $("#curId").val();
        /**
         如果为空不自动生成
         **/
        if(netId.length==0||curId.length==0) {
            return;
        }
        var param={
            netId:netId,
            curId:curId,
            accType:'16',
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


    bt.unionAcc = function () {
        var unionNetId = $('input[name="unionNet"]').val();
        var curId= $("#curId").val();
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
        }
    };

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
            $elem.val(data.blnctrAcc)
        },
        tableInitCallback:function(full,$elem){
        }
    });


    //监控计息标志
    changeInterestSign = function(data,ele){
        fn_interestSign(data.value);
        form.render();
    }

    corps = bt.corp.init();
    selectType = bt.selectType.init();
    rateType = bt.rateType.init();
    dateType = bt.dateType.init();
    dicts = bt.dicts.init();
    //表单验证
    function fn_interestSign(val) {
        if(val == "1"){
            if($("input[data-biz-name='interestRate']").length>0 ){
                $("input[data-biz-name='interestRate']").removeAttr("readonly");
                $("input[data-biz-name='interestRate']").attr("lay-verify","required");
            }

            if($("input[data-biz-name='overdueRate']").length>0) {
                $("input[data-biz-name='overdueRate']").attr("lay-verify", "required");
            }

            dicts.get("predrawSign").attr({'data-required':true,"data-disabled":false});
            dicts.get("predrawSign").render();

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
                rateType.get("interestRate").val("");
            }
            if($("input[data-biz-name='overdueRate']").length>0) {
                $("input[data-biz-name='overdueRate']").removeAttr("lay-verify");
                rateType.get("overdueRate").val("");
            }

            //预提类型
            dicts.get("predrawSign").attr({'data-required':false,"data-disabled":true});
            dicts.get("predrawSign").val("");
            dicts.get("predrawSign").render();

            if($("#interestStartDate").length>0){
                $("#interestStartDate").parent().removeAttr("lay-verify");
            }
            if( $("select[name='accForDeduct']").length>0){
                $("select[name='accForDeduct']").removeAttr("lay-verify");
            }

            if($("input[data-biz-name='overdraftRate']").length>0){
                $("input[data-biz-name='overdraftRate']").removeAttr("lay-verify");
                $("input[data-biz-name='overdraftRate']").attr("readonly","readonly");
                rateType.get("overdraftRate").val("");
            }
        }

        form.renderIcon();
        form.render();
    }

    $(document).ready(function(){
        initseqNum();

    });

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
     * 手动生成账号
     */
    bt.genBlnctrManualAcc = function() {
        /**
         *	从末级科目得到计息标志和利率和透支利率，余额方向
         */
        lastSubjectChange();

        var netCode = $("#netCode").val();
        //自定义属性值获取
        var curCode = $("#curCode").val();
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
        var netId= $("#netId").val();
        var curId= $("#curId").val();
        var accType="16";
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


});


function unionNetChange(id) {
    //网联账号赋值
    bt.unionAcc();
}

/**
 *根据内部账户及其生成策略 初始化顺序号
 */
function initseqNum() {
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
        default:
            tempLength=4;
    }
    var str=blnctrAcc.substr(blnctrAcc.length-tempLength,blnctrAcc.length);
    $("#seqNum").val(str);
};

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

