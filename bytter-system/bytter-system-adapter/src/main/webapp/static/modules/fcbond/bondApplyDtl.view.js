bt.use(['form','dicts','btdate'], function(){
    $ = bt.jquery,form = bt.form();
    var dicts = bt.dicts.init();
    var dateType = bt.dateType.init();
    //查询
    form.on('submit(search)', function(data) {
        var bondId = $("#bondId").val();
        var param = "";
        var status = dicts.get("status").val();
        var payBillNo = $("#payBillNo").val();
        var bursarBillNo = $("#bursarBillNo").val();
        var origNo = $("#origNo").val();
        var startDate = dateType.get("startDate").val();
        var endDate = dateType.get("endDate").val();
        param += status+","+payBillNo+","+bursarBillNo+","+origNo+","+startDate+","+endDate;
        if(param.trim() == ""){
            param = null;
        }
        $("#param").val(param);
        var url = basePath + "/bond/fcbondInfo/BondApplyDtl/"+bondId+"/"+param;
        window.location.href = url;
    });
    //导出表格
    form.on('submit(excel)', function(data) {
        if(!checkTables()){
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }else if(checkTables()){
            var url = basePath + "/bond/fcbondInfo/excelBondApplyDtl";
            DownLoadFile({url:url,data:data.field,method:'post'});
        }
    });
});
var checkTables = function () {
    var  isreturn =true;
    //获取所有tr
    var trs=$("#appTable").find("tr");
    if(trs.length<=2){
        isreturn =false;
    }
    return isreturn;
}


function DownLoadFile(options) {
    var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
    var $form = $('<form target="_self" method="post" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
    return false;
}

function link(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath+"/bond/fcbondInfo/view/"+id);
        return;
    }
}