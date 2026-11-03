bt.use(['form','selectType','dicts'],function () {
    var $ = bt.jquery;
    selectType=bt.selectType.init();
    form = bt.form();
    dict = bt.dicts.init();

    form.on('submit',function (data) {
        bt.loading.show();
        var action = '/chowmatistic/choproduct/save';
        $.post(basePath + action,data.field, function (result) {
            parent.layer.alert(result.msg,{icon:result.success?1:2},function () {
                parent.layer.closeAll();
                if (result.success){
                    if (bt.navtab){
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    } else{
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
                bt.loading.hide();
            })
        },'JSON');
        return false;
    })
    form.renderIcon();
});

// 理财类型回调函数
function financialTypeCallback(data) {
    var filterData
    if (data.value === '1') {
        filterData = '3,4,5,6'
    } else if (data.value === '0') {
        filterData = '1,2'
    } else if (data.value === '2') {
        filterData = '2,3,4,5,6'
    }
    dict.get("productType").attr({'data-filterData': filterData})

    bt.dicts.render()
}