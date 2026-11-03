var selectType,dict;
bt.use(['form','selectType','dicts'], function() {
    var $ = bt.jquery, form = bt.form();
    selectType = bt.selectType.init();
    dict = bt.dicts.init();
    //监听提交
    form.on('submit', function(data){
        var action = "/creditRating/crScoringIndicator/modify"
        var id = $("#id").val();
        var scoringIndicatorCode = $("#scoringIndicatorCode").val()
        var scoringIndicatorName = $("#scoringIndicatorName").val()
        var scoringIndicatorDescription = data.field.scoringIndicatorDescription;
        var status = dict.get("status").val()
        var param = {id:id,scoringIndicatorCode:scoringIndicatorCode,scoringIndicatorName:scoringIndicatorName,scoringIndicatorDescription:scoringIndicatorDescription,status:status}
        $.ajax({
            url: basePath + action,
            type: 'POST',
            contentType: 'application/json', // 设置为JSON格式
            data:JSON.stringify(param), // 将对象转换为JSON字符串
            dataType: 'json',
            success: function(result) {
                if(result.success) {
                    if(bt.navtab) {
                        bt.navtab.getParentDom().$('[type="submit"]').trigger('click');
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }else{
                    layer.msg(result.msg);
                }
            },
        });
        return false;
    });
});