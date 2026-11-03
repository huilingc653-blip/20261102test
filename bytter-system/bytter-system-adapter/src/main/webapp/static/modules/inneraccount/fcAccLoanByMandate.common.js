bt.use(['form','selectTableType'], function(){
    var $ = bt.jquery,form = bt.form();

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
        },
        tableInitCallback:function(full,$elem){
        }
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

function initPrecationCorpComplete() {
    //网联账号赋值
    $precationAcc("init");
}
function precationCorpChange(id) {
    $precationAcc();
}