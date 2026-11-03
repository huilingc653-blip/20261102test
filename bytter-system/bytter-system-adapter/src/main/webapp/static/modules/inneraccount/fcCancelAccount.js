bt.use(['form','select','moneyType','selectType','dateType','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref"),
        });
    });

    form.on('select(transferAcc)',function (data) {
        var transferAcc =data.value;
        if(transferAcc.length==0){
            return false;
        }

        $("#accountSpanShow").empty();
        $("#accountSpanShow").append(transferAcc);
        $("#accountShow").val(transferAcc);
    });

    $(document).ready(function(){
        form.renderIcon();
    });


});