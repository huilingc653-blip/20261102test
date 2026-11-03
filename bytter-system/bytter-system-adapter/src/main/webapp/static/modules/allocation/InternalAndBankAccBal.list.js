bt.use(['form', 'datatable', 'dateType', 'select', 'page', 'moneyType', 'date', 'jquery', 'corp', 'bank'], function () {
    $ = bt.jquery, form = bt.form();

    form.on('submit(excel)', function (data) {
        var url = basePath + "/report/internalAndBankAccBal/doExportInterestBill";
        DownLoadFile({url: url, data: data.field, method: 'post'});
        return false;
    });

    var DownLoadFile = function (options) {
        var config = $.extend(true, { method: 'post' }, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form  target="_self" method="' + config.method + '" />');
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