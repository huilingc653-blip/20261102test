var dateType;
var selectType;
var dicts;
var corp;
var mulselect;
bt.use(['form','bttable','btdate','datatable','laypage','dateType','selectType','date','dicts','jquery','page','moneyType','corp','mulselect','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    dicts=bt.dicts.init();
    bt.moneyType.init();
    corp = bt.corp.init();
    bt.mulselect.init();

    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/bond/fcbond/getBondAcc",
        urldata: {corpId: $("#accCorpId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data:"curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
            }
        }
    });

    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""|| ms == " "){
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit(search)', function(data) {
        search();
    });

    //导出表格
    form.on('submit(excel)', function(data) {
        if(!checkTables()){
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }else if(checkTables()){
            var url = basePath + "/bond/fcbondInfo/excel";
            DownLoadFile({url:url,data:data.field,method:'post'});
        }
    });

    //监听重置
    resetCode = function(){
        var inputs = $('.rCode').find('input');
        inputs.each(function(i, item){
            $(this).val("");
        });

        var selects = $('.rCode').find('.bt-biz-selectType,.bt-biz-dict');
        bt.each(selects,function () {
            $(this).find('select').val(' ');
        });
        corp.get("corpId").val("");
        dicts.get("itemType").attr({'data-defaultvalue':''});
        dicts.get("itemType").val("");
        form.render();
    }
});

function search(){
    var action = basePath + "/bond/fcbondInfo/viewBondUseDtl";
    $("#searchForm").attr("action", action);
    $("#searchForm").attr("method", "post");
    $("#searchForm").submit();
}

/**表格不为空验证*/
var checkTables = function () {
    var  isreturn =true;
    //获取所有tr
    var trs=$("#appTable").find("tr");
    if(trs.length<=1){
        isreturn =false;
    }
    return isreturn;
}

//导出表格
var DownLoadFile = function (options) {
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