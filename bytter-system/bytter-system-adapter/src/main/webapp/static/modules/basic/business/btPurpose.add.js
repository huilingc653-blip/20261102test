bt.use(['form', 'layedit', 'corp', 'dicts','mulselect'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.mulselect.init();

    form.verify({
        maxlength: function (value, item) {
            var len = 0;
            var maxLen = $(item).data('maxlength'.toLowerCase());
            var minLen = $(item).data('minlength'.toLowerCase());
            for (var i = 0; i < value.length; i++) {
                len += 1;
            }
            if(value && maxLen && minLen){
                if(minLen == maxLen){
                    if(len !=minLen){
                        return bt.lang('sys.form.verify.mustBeEqualLength')+ minLen//'输入的内容长度必须等于'+minLen;
                    }
                }else if(len < minLen || len > maxLen ){
                    return bt.lang('sys.form.verify.mustBeBetweenLength')+minLen+bt.lang('table.fcBond.rangeofdate')+maxLen//'输入的内容长度范围必须在'+minLen+'至'+maxLen+'之间';
                }
            }else if(value  && maxLen && maxLen < len ){
                return bt.lang('sys.form.verify.mustBeLessThanLength')+maxLen//'输入的内容长度必须小于'+maxLen;
            }else if(value && minLen && minLen > len ){
                return bt.lang('sys.form.verify.mustBeGreaterThanLength')+ minLen //'输入的内容长度必须大于'+minLen;
            }
        }
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        var busType;
        // 复选框选中
        $.each($("input[type='checkbox']:checked"), function (i, item) {
            if ($(item).attr("value") != "all") {
                if (null == busType) {
                    busType = $(item).attr("value");
                } else {
                    busType += "," + $(item).attr("value");
                }
            }
        });
        $("#sysBusType").val(busType);
        data.field.sysBusType = busType;
        var action = "/basic/business/btpurpose/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});
