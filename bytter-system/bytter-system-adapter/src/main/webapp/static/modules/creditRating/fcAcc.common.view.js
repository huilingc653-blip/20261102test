bt.use(['form','layedit','moneynum','btdate','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.corp.init();

    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            //     bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });

    /**
     * 全局调整
     */
    var getGlobal = function() {
        initseqNum();

        //生成附页面元素
        genOtherHtml();

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
        var accType =$("#accType").val();
        var status = $("#status").val();
        var url=basePath+"/inneraccount/fcacc/businessAccount";
        if(status == 'G' || status == 'F' || status == 'E'){
            url=basePath+"/inneraccount/fcaccchange/businessAccount";
        }
        var param={
            id:id,
            accTypeCode:accType,
            type:'view',
            date:(new Date()).getTime()
        };
        $.ajax({
            url:url,
            method: 'GET',
            data: param,
            async:false,
            success: function (json) {
                $("#tagFcAcc").html("");
                setTimeout(function(){
                    $("#tagFcAcc").html(json);
                },100)
                form.render();
            }
        });
    };

    $(document).ready(function(){

        $.ajax({
            url: basePath+"/basic/business/btcurrency/json",
            method: 'GET',
            dataType: 'json',
            async:false,
            success: function(data){
                if(data){
                    $("#curId").empty();

                    $.each(data,function(index){
                        if($("#curIdHid").val()==data[index].id){
                            $("#curId").append("<option value='"+data[index].id+"'"
                                + " data-curCode='"+data[index].curCode+"'"
                                + " selected>"+ data[index].curName+"</option>");
                        }else{
                            $("#curId").append("<option value='"+data[index].id+"'"
                                + " data-curCode='"+data[index].curCode+"'"
                                + ">"+ data[index].curName+"</option>");
                        }

                    });
                }

            }
        });
        //账户类别：02银行活期存款 04银行借款 06银行定期
        //系统编码：1032 应收利息-银行活期存款,1033 应收利息-银行定期存款,2014 应付利息-银行借款,5012 利息收入-银行活期存款,5013 利息收入-银行定期存款,5214 利息支出-银行借款,521Z 银行贴现利息支出
        var sysSubjectCode = $("#sysSubjectCode").val();
        if($("#accType").val() == '02' || $("#accType").val() == '04' || $("#accType").val() == '06' || sysSubjectCode == '1032' || sysSubjectCode == '1033' || sysSubjectCode == '2014' || sysSubjectCode == '5012' || sysSubjectCode == '5013' || sysSubjectCode == '5214' || sysSubjectCode == '521Z'){
            $(".bankClass").show();
        }else {
            $(".bankClass").hide();
        }
        getGlobal();

    });
});

var closeDoc=function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if(index){
        parent.layer.close(index); //再执行关闭
    }else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};
