bt.use(['form','layedit','dicts', 'dateType','corp', 'selectType','select'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.corp.init();
    $("#reset").click(function(){
        location.reload();
        return false ;
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/receipt/bisbankreceiptdtl/update";
        var ids=[];
        var arr=$('[name="id"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.id=ids.toString();
        ids=[];
        var arr=$('[name="payAccNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payAccNo=ids.toString();
        ids=[];
        var arr=$('[name="payAccName"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payAccName=ids.toString();
        ids=[];
        var arr=$('[name="payBankName"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payBankName=ids.toString();
        ids=[];
        var arr=$('[name="bifCode"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.bifCode=ids.toString();
        ids=[];
        var arr=$('[name="payeeAccNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payeeAccNo=ids.toString();
        ids=[];
        var arr=$('[name="payeeAccName"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payeeAccName=ids.toString();
        ids=[];
        var arr=$('[name="payeeBankName"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.payeeBankName=ids.toString();
        ids=[];
        var arr=$('[name="transAmt"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.transAmt=ids.toString();
        ids=[];
        var arr=$('[name="curId"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.curId=ids.toString();
        ids=[];
        var arr=$('[name="usefor"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.usefor=ids.toString();
        ids=[];
        var arr=$('[name="abs"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.abs=ids.toString();
        ids=[];
        var arr=$('[name="remark"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.remark=ids.toString();
        ids=[];
        var arr=$('[name="busType"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.busType=ids.toString();
        ids=[];
        var arr=$('[name="busNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.busNo=ids.toString();
        ids=[];
        var arr=$('[name="charageDate"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.charageDate=ids.toString();
        ids=[];
        var arr=$('[name="receiptNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.receiptNo=ids.toString();
        ids=[];
        var arr=$('[name="receiptCheckCode"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.receiptCheckCode=ids.toString();
        ids=[];
        var arr=$('[name="transSerialNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.transSerialNo=ids.toString();
        ids=[];
        var arr=$('[name="netCode"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.netCode=ids.toString();

        ids=[];
        var arr=$('[name="tellerNo"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.tellerNo=ids.toString();
        ids=[];
        var arr=$('[name="ebillimg"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.ebillimg=ids.toString();
        ids=[];
        var arr=$('[name="filetype"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.filetype=ids.toString();
        ids=[];
        var arr=$('[name="pdfToImg"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.pdfToImg=ids.toString();
        ids=[];
        var arr=$('[name="prints"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.prints=ids.toString();
        ids=[];
        var arr=$('[name="cdSign"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.cdSign=ids.toString();
        ids=[];
        var arr=$('[name="isFrom"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.isFrom=ids.toString();
        ids=[];
        var arr=$('[name="status"]');//获取copy相同name的对象，然后再遍历这个数组就行了。
        for(var i=0;i<arr.length;i++){
            var val=$(arr[i]).val();//获取value值
            ids.push(val);
        }
        data.field.status=ids.toString();

        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

});