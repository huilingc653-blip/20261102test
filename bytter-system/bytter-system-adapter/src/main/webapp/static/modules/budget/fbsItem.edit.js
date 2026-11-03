bt.use(['form','layedit','rateType','selectTree','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.rateType.init();
    var dicts = bt.dicts.init();

    bt.selectTree.init({
        url:basePath+"/budget/fbsitem/tree",
        elem:".parentIdTree",
        name:'superItemCode',
        selected:$("#superItemCodeHide").val()
    });

    bt.selectTree.init({
        url:basePath+"/budget/fbsitem/tree",
        elem:".allotItemTree",
        name:'allotItemCode',
        selected:$("#allotItemCodeHide").val()
    });

    $("input[name='itemRelation']").bind("change",function(){
        setIsWrite();
    });

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        setIsWrite();
    }

    setIsWrite = function () {
        if($("#itemRelation").val()){
            dicts.get("isWrite").attr({"data-disabled":true});
            dicts.get("isWrite").val(0);
        }else {
            dicts.get("isWrite").attr({"data-disabled":false});
        }
    }

    //监听提交
    form.on('submit(submit)', function(data){
    	var action = "/budget/fbsitem/modify";
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
