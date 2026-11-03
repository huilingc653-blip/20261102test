bt.use(['form','select','corp','dateType','selectTableType','selectType','rateType','dicts','dateType'], function(){
	$ = bt.jquery,form = bt.form();
    corps = bt.corp.init();
    selectType = bt.selectType.init();
   	rateType = bt.rateType.init();
    dateType = bt.dateType.init();
   	dicts = bt.dicts.init();
    bt.precationAccTable=bt.selectTableType.init({
        elem:".precationAcc",
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
        }
    });

  //监控计息标志
    form.on('radio(interestSign)',function (data) {
        fn_interestSign(data.value);
        form.render();
    });
  
    $precationAcc = function (param) {
        var precationCorpId = $('input[name="precationCorp"]').val();
        var curId ="";
        if($("select[name='curId']").length>0){
            curId=$("select[name='curId']").val();
        }else{
            curId=$("input[name='curId']").val();
        }

        if(precationCorpId && curId){
            var urldata={
                corpId:precationCorpId,
                curId:curId,
                accType:"09",
                type:''
            };
            if("init" != param){
                $("#precationAcc").val("");
            }
            bt.precationAccTable.get("precationAcc").setUrl(urldata);
        }
    };
});

function precationCorpChange(id) {
    $precationAcc();
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
    form.renderIcon();
    form.render();
}