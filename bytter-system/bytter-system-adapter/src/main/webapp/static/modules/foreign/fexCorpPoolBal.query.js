bt.use(['form', 'datatable', 'selectType', 'dateType' ,'dicts', 'printType','treeTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.treeTable.init({elem:"#appTable",column:1});

    dateType = bt.dateType.init();
    dict = bt.dicts.init();

    var selectType = bt.selectType.init();

    bt.printType.init();

    /**
     * 查询
     */
    $("#search").click(function () {
        var action = basePath + "/foreign/fexcorppoolbankaccbal/query";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });

    /**
     * 重置
     * @param data
     * @param dom
     */
    bt.searchResetCallback = function (data, dom) {
        selectType.get("poolId").val("");
        selectType.get("curId").val("");
        $("#balDate").val(new Date().format('yyyy-MM-dd'));
        dict.get("rateType").val("");
        $("#curShow").val("");
    };
    //合并币种回调
    bt.curCallback = function(data, dom){
        $("#curShow").val(data.id);
    }
    /**
     * 返回重新查询
     */
    bt.send = function () {
        var action = basePath + "/foreign/fexcorppoolbankaccbal/query";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    }

    /**
     * 导出
     */
    form.on('submit(excel)', function(data) {
        var url = basePath + "/foreign/fexcorppoolbankaccbal/exportExcel";
        DownLoadFile({url:url,data:data.field,method:'post'});
        return false;
    });

    var DownLoadFile = function (options) {
        var config = $.extend(true, { method: 'post' }, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
        $form.attr("action", config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
    }

});
