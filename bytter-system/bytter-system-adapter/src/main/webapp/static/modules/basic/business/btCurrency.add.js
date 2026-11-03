bt.use(['form','layedit','dicts','moneyType','checkboxs', 'selectTable'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    form.renderIcon();
    bt.selectTable.init({
        elem: "#sysCurTable",
        url: basePath + "/basic/business/btcurrency/listSysCur",
        searchInput: ".text",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "englishCode",},
            {data: "curName",},
        ],
        clickCallback: function (data, $tr) {
            $("#sysCurId").val(data.id);
            $("#sysCurName").val(data.curName);
            $("#curName").val(data.curName);
            $("#englishCode").val(data.englishCode);
            $("#englishCodeDiv").html(data.englishCode);
        },
    })
    //监听提交
    form.on('submit(submit)', function(data){
        var nonworkDay;
        // 复选框选中
        $.each($("input[type='checkbox']:checked"), function (i, item) {
            if ($(item).attr("value") != "all") {
                if (null == nonworkDay) {
                    nonworkDay = $(item).attr("value");
                } else {
                    nonworkDay += "," + $(item).attr("value");
                }
            }
        });
        $("#nonworkDay").val(nonworkDay);
        data.field.nonworkDay = nonworkDay;
        var action = "/basic/business/btcurrency/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
});
