bt.use(['form','layedit','moneynum','dateType','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    var curDate = bt.dateType.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        data.field.accState=accState;
        if(!validNetDif() || !compareDate() || !validCorpDif()){
            bt.loading.hide();
            return false;
        }
        //校验计息账号
        if(!isEmpty(validInterestAcc())){
            bt.loading.hide();
            parent.layer.confirm(validInterestAcc() + bt.lang("inneraccount.interestacc.msg.cancel"),function(){
                $.post(basePath + "/inneraccount/fcaccchange/change",data.field,function(result){
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                    if(result.success){
                        if(bt.navtab) {
                            bt.navtab.closeCurrent();
                        }
                    }
                },'JSON');
            });
        }else{
            $.post(basePath + "/inneraccount/fcaccchange/change",data.field,function(result){
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }
                }
            },'JSON');
        }
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
            sysAlert(bt.lang('nis.fcAcc.message.entrusted.deposit.loan'));
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

    /**
     * 校验计息账号
     * @returns {string}
     */
    function validInterestAcc() {
        var interestAccTemp = $("#interestAcc").val();
        var interestAccVal = $("#interestAccVal").val();
        var acc = "";
        if(!isEmpty(interestAccVal)){
            var arr1 = interestAccVal.split(';');
            var arr2 = interestAccTemp.split(';');
            acc= compareArr(arr1,arr2);
        }
        if(!isEmpty(acc)){
            acc = acc.join(";");
        }
        return acc;
    }

    /**
     * 数组比较方法
     * @param arr1
     * @param arr2
     * @returns {[]}
     */
    function compareArr(arr1, arr2){
        var few = [];//少
        var more = [];//多
        var merge = arr1.concat(arr2);//合并
        merge.sort();
        merge.reverse();
        var quchong = $.unique(merge);
        // console.info(arr1);
        // console.info(arr2);
        // console.info(quchong);
        quchong.forEach(function(qitem,index,array){

            if(arr1.every(function(item,qindex,array){return qitem != item;}))
            {
                more.push(qitem);
            }

            if(arr2.every(function(item,qindex,array){return qitem != item;}))
            {
                few.push(qitem);
            }
        });
        // console.info("arr2比arr1多的元素"+more);
        // console.info("少的元素"+few);
        return few;
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

    $(document).ready(function(){
        if($('#status').val() == 'G' || $('#status').val() == 'N'){ //暂存数据需要看到打回原因
            $('#endTrCheckBackReason').hide();
        }else{
            $('#endTrCheckBackReason').show();
        }
    });
});
