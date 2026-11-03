bt.use(['element'], function(){
    var element = layui.element();
    bt.jquery("#claimIframe").attr("src", basePath+"/bill/accebill/abClaim/listClaim");
    bt.jquery("#toBeConfirmedIframe").attr("src", basePath+"/bill/accebill/abClaim/listWaitConfirm");
    bt.jquery("#confirmedIframe").attr("src", basePath+"/bill/accebill/abClaim/listConfirmed");
    var claimIframe;
    var toBeConfirmedIframe;
    var confirmedIframe;
    //监听Tab切换
    element.on('tab(billExc)', function(data) {
        //data.index=0 待人工认领  data.index=1 待确认 data.index=2 已确认
        if(data.index===0&&!claimIframe){
            claimIframe=true;
            refresh(data.index);
        }else if(data.index===1&&!toBeConfirmedIframe){
            toBeConfirmedIframe=true;
            refresh(data.index);
        }else if(data.index===2&&!confirmedIframe){
            confirmedIframe=true;
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