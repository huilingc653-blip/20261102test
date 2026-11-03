bt.use(['element'], function(){
    var element = layui.element();
    //监听Tab切换
    element.on('tab(billPay)', function(data) {
        var key = $(data.elem.context).data('id');
        if(!$('#'+key)[0]){
            $('#trackiframeBox').html('<iframe id="trackiframe" src="'+basePath+'/bill/accebill/abemitbillpay/adEmitBillPayTrackList" frameborder="0" style="width: 100%;" name="trackiframe" ></iframe>')
        }else{
            var childWin = window.frames[key];
            if(childWin){
                childWin.$.fn.dataTable.tables( { visible: true, api: true, ie:true } ).ajax.reload(null, false);//null false代表页码不重置
            }
        }
    });
});

// 调整iframe容器的高度
function adjustMainWebHeight(height) {
    var height1 = $('.layui-tab-content .layui-show').height()
    height = height + 20;
    if(height && height1 < height){
        $('.layui-tab-content .layui-show').height(height)
    }
}
