bt.use(['form','layedit','dicts','selectType', 'selectTable', 'bttable'], function(){
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/sys/operationArea/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });


    var inputOperationareaSearchLayerIndex = null;
    $("#inputProvinceIdBtn").click(function (){
        //  layerType  = 1 表示 编辑
        var url = basePath + "/sys/systemProvince/selectPage"
        if ($("#newPage").val()){
            url += '?layerType=1'
        }

        inputOperationareaSearchLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '620px'],
            maxmin: true,
            content: url,
        })
    })

    /**
     * 省区维护查询选择回调
     * @param data
     * @param dom
     */
    inputSystemProvinceCallback = function (selectedData) {
        var ids = ''
        var names = ''
        bt.each(selectedData,function (i,item){
            ids += item.id
            names += item.provinceName
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
            }
        })
        $("#provinceId").val(ids)
        $("#systemProvinceName").val(names)
        inputOperationareaSearchLayerIndex && parent.layer.close(inputOperationareaSearchLayerIndex)
    }
});
