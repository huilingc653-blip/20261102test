bt.use(['element'], function(){
    var element = layui.element();
    bt.jquery("#backiframe").attr("src", basePath+"/finacial/check/fccompareerp/listFcCheck");
    //bt.jquery("#backiframe").show(1000);
    bt.jquery("#corpiframe").attr("src", basePath+"/finacial/check/fccompareerp/listFcCorpCheck");
    //bt.jquery("#corpiframe").show(1000);

    var isBackiframe;
    var isCorpiframe;
    //监听Tab切换
    element.on('tab(check)', function(data) {
        //data.index=0 帐号对账  data.index=1 单位对账
        if(data.index==0&&!isBackiframe){
            isBackiframe=true;
            refresh(data.index);
        }else if(data.index==1&&!isCorpiframe){
            isCorpiframe=true;
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
                if( fname == 'ERP对账' ){
                    var iframeDom = top.frames[fname];
                    var iframeId = '#backiframe';
                    if(tableIndex==1){
                        iframeId = '#corpiframe';
                    }
                    var innerIframe = iframeDom.$(iframeId)[0];
                    if(innerIframe){
                        var name = innerIframe.name;
                        var backiframe = iframeDom.frames[name];
                        //backiframe.$.fn.dataTable.tables( { visible: true, api: true, ie:true } ).ajax.reload(null, false);//null false代表页码不重置
                        return;
                    }
                }
            }
        })
    }

});