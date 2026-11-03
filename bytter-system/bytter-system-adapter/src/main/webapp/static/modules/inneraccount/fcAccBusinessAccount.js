bt.use(['form'], function(){
    var $ = bt.jquery;
    form.on('select(chargeAcc)',function (data) {

    });

    function fn_chargeAcc() {
        var curId=$("select[name='curId']").val();
        var corpId=$('input[name="corpId"]');
        if(corpId){
            var param={
                curId:curId,
                corpId:corpId,
                date:(new Date()).getTime(),
            };
            $("#chargeAcc").empty();
            $.ajax({
                url: basePath+"/inneraccount/fcacc/getAccountTableData",
                method: 'POST',
                data: param,
                async:true,
                success: function(data){
                    console.log(data);
                    // if(data){
                    //     $("#chargeAcc").append("<option value=''>"+bt.lang('common.placeholder.select')+"</option>");
                    //     $.each(data,function(index){
                    //         $("#blnctrAcc").append("<option value='"+data[index].blnctrAcc+"'"
                    //             + " data-curName='"+data[index].curName+"'"
                    //             + " data-accName='"+data[index].accName+"'"
                    //             + " data-activeMoney='"+data[index].activeMoney+"'"
                    //             + " data-freezeMoney='"+data[index].freezeMoney+"'"
                    //             +">"+ data[index].blnctrAcc+"</option>");
                    //     });
                    // }
                    // form.render('select');
                }
            });
        }
    }

    $(document).ready(function(){
        // fn_chargeAcc();

        var $hid=$("#chargeAccHid");
        if($hid){
            $("select[name='chargeAcc']").find("option").each(function (){
                if ($(this).val() == $hid.val()) {
                    $(this).attr("selected", "selected");
                }
            });
        }
    });




});
