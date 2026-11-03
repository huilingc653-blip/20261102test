
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'corp','date','rateType','mulselect','freezeTable'], function () {
    $ = bt.jquery, form = bt.form();
    var corp = bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();

    bt.freezeTable.init({
        elem: '#appTable',//表格id
        freezeRowNum: '1',//冻结几行
        freezeColumnNum: 0,//冻结几列
        height: '450px' //表格高度
    })

    //监听提交
    form.on('submit(search)', function(data) {
        search();
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
        })
        form.render('select');
        corp.get("corpId").val("");
    };

    //导出表格
    form.on('submit(excel)', function(data) {
        if(!checkTables()){
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }else if(checkTables()){
            var url = basePath + "/report/accountManagementStatistics/excel";
            DownLoadFile({url:url,data:data.field,method:'post'});
        }
    });

});

function search(){
    var action = basePath + "/report/accountManagementStatistics/list";
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