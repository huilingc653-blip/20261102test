bt.use(['form'], function(){
    var $ = bt.jquery,form = bt.form();
    var genLogHtml=function(){
        var logType,bizId,account,bizType;
        if($("#logType")){
            // logType =$("#logType").val();
            logType = "ALL";
        }
        if($('#id')){
            bizId= $('#id').val();
        }
        if($('#blnctrAcc')){
            account= $('#blnctrAcc').val();
        }
        if($('#bizType')){
            bizType = $("#bizType").val();
        }
        var url=basePath+"/inneraccount/fcAccLog/viewFcAccLog";
        var param={
            bizId:bizId,
            account:account,
            bizType:bizType,
            logType:logType,
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:true, success: function (json) {
            $("#tagLog").html("");
            $("#tagLog").html(json);
            form.render();
        }
        });
    };
    $(document).ready(function(){
        genLogHtml();
    });
});
