bt.use(['form','layedit','select','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    var selectType = bt.selectType.init();
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
            required: true,
            url: $(item).attr("fieldhref")
        });
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/basic/business/bisbifinit/modify";
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

    /**境内境外控制begin*/
    //是否境内银行回调
    bt.isDomesticBank = function(data){
        if (data.value == '1') {//境内
            selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/1"});
        }else{//境外
            selectType.get("bankTypeId").attr({"data-defaultvalue":"","data-url":basePath+"/basic/business/btbanktype/json/0"});
        }
        form.render('select');
    };

    //重新渲染回调
    bt.dicts.init();
    /** 境内境外控制end*/
});
