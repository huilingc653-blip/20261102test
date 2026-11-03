bt.use(['form','layedit','moneynum','btdate','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.corp.init();
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem:"[field='"+$(item).attr("field")+"']",
            name:$(item).attr("field"),
            defaultVal:$(item).attr("defaultVal"),
            valueField:$(item).attr("dictField"),
            textField:$(item).attr("dictText"),
            placeholder:{value:"",text:bt.lang("common.placeholder.select")},
            search:true,
            disabled:false,
            url:$(item).attr("fieldhref")

        });
    });

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
    //监听提交
    form.on('submit(submit)', function(data){

        var accState=$(this).attr("value");
        data.field.accState=accState;
        var action = "/inneraccount/fcacc/modify";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });

    //根据账户类别获取科目代码
    form.on('select(accType)', function(data){
        // console.log(data.elem); //得到select原始DOM对象
        // console.log(data.value); //得到被选中的值
        // console.log(data.othis); //得到美化后的DOM对象
        genSubjectId(data.value,"");
    });



    form.on('select(subjectId)',function (data) {
        // console.log('select(subjectId)');
        bt.getGlobal('');

    });

    var genSubjectId = function(value,type) {

        $.ajax({
            url: basePath+"/finacial/paramconfig/fcsubject/jsonSubjectDate/"+value+"?tk="+(new Date()).getTime(),
            method: 'GET',
            dataType: 'json',
            async:false,
            success: function(data){
                $("#subjectId").empty();
                $("#subjectId").append("<option>"+bt.lang('common.placeholder.select')+"</option>");
                if(data){
                  //  console.log($("#subjectIdHid").val());
                    $.each(data,function(index){

                            $("#subjectId").append("<option value='"+data[index].id+"'"
                                + " data-balanceWay='"+data[index].balanceWay+"'"  //余额方向
                                + " data-interestSign='"+data[index].interestSign+"'"  //计息标志
                                + " data-interestRate='"+data[index].interestRate+"'"  //利率
                                + " data-subjectCode='"+data[index].subjectCode+"'"   //科目代码
                                + ((type=='init' && data[index].id==$("#subjectIdHid").val())?"selected":"")
                                + " >("+data[index].subjectCode+")"+  data[index].subjectName+"</option>");
                        // $("select[name='subjectId']").val(item.defId);
                    });
                }
                form.render('select');
            }
        });
    };



    /**
     * 全局调整
     */
    bt.getGlobal = function(type) {

        // 从末级科目得到计息标志和利率和透支利率，余额方向
        lastSubjectChange();

        //看是否有大账号
        //showFatherAcc();

        if (type == 'init'){
            initseqNum();
        }else{
            // 自动生成顺序号
            createSequence();

            //生成内部账号
            getBlnctrAcc();
        }

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
    }

    /**
     * 生成内部账号
     */
    var getBlnctrAcc=function () {
        var netCode = $("#netCode").val();
        //自定义属性值获取
        //$("#curId").find("option:selected").attr("data-curcode");
        //or $("#curId").find("option:selected").data("curcode")
        var curCode = $("#curId").find("option:selected").attr("data-curcode");

        var subjectCode = $("#subjectId").find("option:selected").attr("data-subjectcode");
        var seqNum = $("#seqNum").val();

        if(!netCode || !seqNum || !curCode||!subjectCode){
            $("#blnctrAcc").attr("value","");
            return;
        }
        $("#blnctrAcc").val(netCode + "" + curCode +""+subjectCode+""+seqNum);
    }


    /**
     * 生成账户不公共的属性
     * @return
     */
    var genOtherHtml=function(){
        var id=$("#id").val();
        var accType =$("select[name='accType']").val();
        var url=basePath+"/inneraccount/fcacc/businessAccount";
        var param={
            id:id,
            accTypeCode:accType,
            type:'update',
            date:(new Date()).getTime(),
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


    /**
     *根据末级科目得到是否计息和透支利率 余额方向
     */
    var lastSubjectChange=function(){

        var balanceWay = $("#subjectId").find("option:selected").attr("data-balanceway");
        $("select[name='balanceWay']").val(balanceWay);
        form.render();

    };

    /**
     自动生成序列号
     **/
    var createSequence=function(){
        var poolSign = $("#poolSign").val();
        var netId= $("input[name='netId']").val();
        var curId=$("select[name='curId']").val();
        var accType=$("select[name='accType']").val();
        if(poolSign==1){
            netId=bt.selectType.get("netId").val();
        }
        /**
         如果为空不自动生成
         **/
        if(netId.length==0||curId.length==0||accType.length==0) {
            return;
        }
        var param={
            netId:netId,
            curId:curId,
            accType:accType,
            poolSign:poolSign,
            date:(new Date()).getTime()
        };
        $.ajax({
            url: basePath + "/inneraccount/fcacc/createSequence",
            method: 'GET',
            data: param,
            async:false,
            dataType: 'json',
            success: function (data) {
                if (data) {
                    $("#seqNum").val(data.obj);
                }
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
                        //console.log(data[index].id+"=="+data[index].subjectName);
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


        $("select[name='accType']").find("option").each(function (){
            if ($(this).val() == $("#accTypeHid").val()) {
                $(this).attr("selected", "selected");
                genSubjectId($(this).val(),"init");
                //break;
            }
        });

        $("select[name='balanceWay']").find("option").each(function (){
            //console.log("#balanceWayHid"+$("#balanceWayHid").val()+"===this=="+$(this).val());

            if ($(this).val() == $("#balanceWayHid").val()) {
                $(this).attr("selected", "selected");
                //break;
            }
        });
        bt.getGlobal('init');

        if($('#isView').val() == 'v'){ //查看
            $('div.layui-input-block').hide();
        }

        if($('#status').val() == 'T'){ //暂存数据需要看到打回原因
            $('#checkReson').hide();
        }else{
            $('#checkReson').show();
        }

        form.render('select');
    });
});


//网点下拉框回调
function netChangeId(id) {
    //console.log("==========="+id);

}
//网点下拉框回调
function netChange(obj) {
    bt.jquery("#netCode").val(obj[0].code);
    bt.getGlobal();
}

