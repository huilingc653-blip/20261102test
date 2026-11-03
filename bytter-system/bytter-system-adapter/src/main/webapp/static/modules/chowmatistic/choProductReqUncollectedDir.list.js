bt.use(['element'], function(){
    var element = layui.element();
    bt.jquery("#applyiframe").attr("src", basePath+"/chowmatistic/choproductreqquery/listChoProductReqUncollected");
    bt.jquery("#trackiframe").attr("src", basePath+"/chowmatistic/choproductreqquery/listChoProductReqUncollectedNon");

    //iframe高度自适应end
    var isApplyiframe;
    var trackiframe;
    //监听Tab切换
    element.on('tab(billPay)', function(data) {
        if(data.index==0&&!isApplyiframe){
        	isApplyiframe=true;
            refresh(data.index);
        }else if(data.index==1&&!trackiframe){
        	trackiframe=true;
            refresh(data.index);
        }
    });

    //刷新列表数据
    function refresh(tableIndex){
        var select = parent?$(parent.document): $(document);
        var items = select.find('.layui-tab-content .layui-tab-item');
        bt.each(items,function () {
            var iframe = $(this).find('iframe')[0];
            if(iframe){
                var fname = iframe.name;
                if( fname == '兑付确认' ){
                    var iframeDom = top.frames[fname];
                    var iframeId = '#applyiframe';
                    if(tableIndex==1){
                        iframeId = '#trackiframe';
                    }
                    var innerIframe = iframeDom.$(iframeId)[0];
                    if(innerIframe){
                        var name = innerIframe.name;
                        var applyiframe = iframeDom.frames[name];
                        applyiframe.$.fn.dataTable.tables( { visible: true, api: true, ie:true } ).ajax.reload(null, false);//null false代表页码不重置
                        return;
                    }
                }
            }
        })
    }

});